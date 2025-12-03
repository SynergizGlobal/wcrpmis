package com.wcr.wcrbackend.repo;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.ProjectStructureSummaryDto;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.DTO.StructureNameDto;
import com.wcr.wcrbackend.DTO.StructureSummaryDto;
import com.wcr.wcrbackend.DTO.StructureTypeDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StructureRepository implements IStructureRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<StructureSummaryDto> getStructureSummaryByProject(String projectId) {

        String sql = """
            SELECT structure_type_fk AS typeName,
                   COUNT(*) AS total
            FROM structure
            WHERE project_id_fk = ?
            GROUP BY structure_type_fk
            ORDER BY structure_type_fk
        """;

        return jdbcTemplate.query(sql, new Object[]{projectId}, (rs, rowNum) ->
            new StructureSummaryDto(
                rs.getString("typeName"),  // maps to structureType
                rs.getLong("total")        // maps to count
            )
        );
    }


    @Override
    public List<Map<String, Object>> getStructuresByProject(String projectId) {
        String sql = """
            SELECT structure_id,
                   structure,
                   structure_name,
                   structure_type_fk,
                   structure_details,
                   from_chainage,
                   to_chainage
            FROM structure
            WHERE project_id_fk = ?
            ORDER BY structure_id
        """;

        return jdbcTemplate.queryForList(sql, projectId);
    }
    
    
    @Override
    public FullStructureResponse getFullStructureData(String projectId) {

        // STEP 1: Get project name
        String projectSql = """
            SELECT project_name 
            FROM project 
            WHERE project_id = ?
        """;

        String projectName = jdbcTemplate.queryForObject(projectSql, new Object[]{projectId}, String.class);

        // STEP 2: Get all structures for this project
        String structureSql = """
        	    SELECT structure_type_fk,
        	           structure_id,
        	           structure,
        	           structure_name,
        	           structure_details,
        	           from_chainage,
        	           to_chainage
        	    FROM structure
        	    WHERE project_id_fk = ?
        	    ORDER BY structure_type_fk, structure_id
        	""";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(structureSql, projectId);

        // STEP 3: Group by structure_type_fk
        Map<String, List<StructureNameDto>> grouped = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            String type = (String) row.get("structure_type_fk");
            String id = String.valueOf(row.get("structure_id"));
            String structure = (String) row.get("structure"); 
            String name = (String) row.get("structure_name");
            String details = (String) row.get("structure_details");
            BigDecimal from = row.get("from_chainage") != null ? (BigDecimal) row.get("from_chainage") : null;
            BigDecimal to   = row.get("to_chainage")   != null ? (BigDecimal) row.get("to_chainage")   : null;

            grouped
                .computeIfAbsent(type, k -> new ArrayList<>())
                .add(new StructureNameDto(id, structure, name, details, from, to));
        }

        // STEP 4: Convert to DTO list
        List<StructureTypeDto> typeList = new ArrayList<>();

        for (Map.Entry<String, List<StructureNameDto>> entry : grouped.entrySet()) {
            typeList.add(new StructureTypeDto(entry.getKey(), entry.getValue()));
        }

        return new FullStructureResponse(projectId, projectName, typeList);
    }
    
    @Override
    public List<String> getAllStructureTypes() {
        String sql = "SELECT DISTINCT structure_type_fk FROM structure ORDER BY structure_type_fk";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    
    
    @Override
    public void insertStructure(String structure, String name, String projectId, String type,
                  String details, BigDecimal fromChainage, BigDecimal toChainage) {
        String sql = """
            INSERT INTO structure 
            (structure, structure_name, project_id_fk, structure_type_fk, 
             structure_details, from_chainage, to_chainage)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql, structure, name, projectId, type, details, fromChainage, toChainage);
    }
    
 // UPDATE (structureId required)
    @Override
    public void updateStructure(String id, String structure, String name, String type,
                                String details, BigDecimal fromChainage, BigDecimal toChainage) {
        String sql = """
            UPDATE structure
            SET structure = ?,
                structure_name = ?,
                structure_type_fk = ?,
                structure_details = ?,
                from_chainage = ?,
                to_chainage = ?
            WHERE structure_id = ?
        """;

        jdbcTemplate.update(sql, structure, name, type, details, fromChainage, toChainage, id);
    }


    // DELETE by ID
    @Override
    public void deleteStructure(String structureId) {
        String sql = "DELETE FROM structure WHERE structure_id = ?";
        jdbcTemplate.update(sql, structureId);
    }
    
    @Override
    public void deleteStructureType(String projectId, String type) {
        String sql = """
            DELETE FROM structure
            WHERE project_id_fk = ? 
              AND structure_type_fk = ?
        """;

        jdbcTemplate.update(sql, projectId, type);
    }

    @Override
    public List<ProjectStructureSummaryDto> getAllProjectSummaries() {

        String sql = """
            SELECT project_id_fk, structure_type_fk, COUNT(*) AS total
            FROM structure
            GROUP BY project_id_fk, structure_type_fk
            ORDER BY project_id_fk
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        Map<String, ProjectStructureSummaryDto> map = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            String projectId = (String) row.get("project_id_fk");
            String type = (String) row.get("structure_type_fk");
            Long count = ((Number) row.get("total")).longValue();

            map.computeIfAbsent(projectId, ProjectStructureSummaryDto::new)
               .addType(type, count);
        }

        return new ArrayList<>(map.values());
    }
    
    
    @Override
    public Map<String, BigDecimal> getProjectChainage(String projectId) {
        String sql = """
            SELECT from_chainage, to_chainage
            FROM project
            WHERE project_id = ?
        """;

        return jdbcTemplate.queryForObject(sql, new Object[]{projectId}, (rs, rowNum) -> {
            Map<String, BigDecimal> map = new HashMap<>();
            map.put("fromChainage", rs.getBigDecimal("from_chainage"));
            map.put("toChainage", rs.getBigDecimal("to_chainage"));
            return map;
        });
    }


    @Override
	public List<Structure> getProjectsListForStructureForm(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "select project_id as project_id_fk,project_name from project order by project_id asc";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Structure>(Structure.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}
/////////////////////
	@Override
	public List<Structure> getWorkListForStructureForm(Structure obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Structure> getContractListForStructureFrom(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "select contract_id as contract_id_fk,contract_name,contract_short_name,c.project_id_fk,project_name "
					+ "from contract c "
					+ "LEFT JOIN project p ON c.project_id_fk = project_id " + "where contract_id is not null ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + "and project_id_fk = ? ";
				arrSize++;
			}

			qry = qry + " order by contract_id asc";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Structure>(Structure.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Structure> getStructuresListForStructureFrom(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "select structure_type from structure_type";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Structure>(Structure.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Structure> getDepartmentsListForStructureFrom(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "select department as department_fk,department_name,contract_id_code from department";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Structure>(Structure.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Structure> getResponsiblePeopleListForStructureForm(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "SELECT user_id,user_name,designation,department_fk FROM [user] u where user_name not like '%user%' and pmis_key_fk not like '%SGS%' and department_fk in('Engg','Elec','S&T')\r\n"
					+ "					ORDER BY \r\n" + "					case when user_type_fk='HOD' then 1\r\n"
					+ "					when user_type_fk='DYHOD' then 2\r\n"
					+ "					when user_type_fk='Officers ( Jr./Sr. Scale )' then 3\r\n"
					+ "					when user_type_fk='Others' then 4\r\n" + "					end asc ,\r\n"
					+ "case when u.designation='ED Civil' then 1 \r\n" + "   when u.designation='CPM I' then 2 \r\n"
					+ "   when u.designation='CPM II' then 3\r\n" + "   when u.designation='CPM III' then 4 \r\n"
					+ "   when u.designation='CPM V' then 5\r\n" + "   when u.designation='CE' then 6 \r\n"
					+ "   when u.designation='ED S&T' then 7 \r\n" + "   when u.designation='CSTE' then 8\r\n"
					+ "   when u.designation='GM Electrical' then 9\r\n"
					+ "   when u.designation='CEE Project I' then 10\r\n"
					+ "   when u.designation='CEE Project II' then 11\r\n"
					+ "   when u.designation='ED Finance & Planning' then 12\r\n"
					+ "   when u.designation='AGM Civil' then 13\r\n"
					+ "   when u.designation='DyCPM Civil' then 14\r\n"
					+ "   when u.designation='DyCPM III' then 15\r\n" + "   when u.designation='DyCPM V' then 16\r\n"
					+ "   when u.designation='DyCE EE' then 17\r\n"
					+ "   when u.designation='DyCE Badlapur' then 18\r\n"
					+ "   when u.designation='DyCPM Pune' then 19\r\n" + "   when u.designation='DyCE Proj' then 20\r\n"
					+ "   when u.designation='DyCEE I' then 21\r\n"
					+ "   when u.designation='DyCEE Projects' then 22\r\n"
					+ "   when u.designation='DyCEE PSI' then 23\r\n" + "   when u.designation='DyCSTE I' then 24\r\n"
					+ "   when u.designation='DyCSTE IT' then 25\r\n"
					+ "   when u.designation='DyCSTE Projects' then 26\r\n"
					+ "   when u.designation='XEN Consultant' then 27\r\n"
					+ "   when u.designation='AEN Adhoc' then 28\r\n"
					+ "   when u.designation='AEN Project' then 29\r\n"
					+ "   when u.designation='AEN P-Way' then 30\r\n" + "   when u.designation='AEN' then 31\r\n"
					+ "   when u.designation='Sr Manager Signal' then 32 \r\n"
					+ "   when u.designation='Manager Signal' then 33\r\n"
					+ "   when u.designation='Manager Civil' then 34 \r\n"
					+ "   when u.designation='Manager OHE' then 35\r\n"
					+ "   when u.designation='Manager GS' then 36\r\n"
					+ "   when u.designation='Manager Finance' then 37\r\n"
					+ "   when u.designation='Planning Manager' then 38\r\n"
					+ "   when u.designation='Manager Project' then 39\r\n"
					+ "   when u.designation='Manager' then 40 \r\n" + "   when u.designation='SSE' then 41\r\n"
					+ "   when u.designation='SSE Project' then 42\r\n"
					+ "   when u.designation='SSE Works' then 43\r\n" + "   when u.designation='SSE Drg' then 44\r\n"
					+ "   when u.designation='SSE BR' then 45\r\n" + "   when u.designation='SSE P-Way' then 46\r\n"
					+ "   when u.designation='SSE OHE' then 47\r\n" + "   when u.designation='SPE' then 48\r\n"
					+ "   when u.designation='PE' then 49\r\n" + "   when u.designation='JE' then 50\r\n"
					+ "   when u.designation='Demo-HOD-Elec' then 51\r\n"
					+ "   when u.designation='Demo-HOD-Engg' then 52\r\n"
					+ "   when u.designation='Demo-HOD-S&T' then 53\r\n" + "\r\n" + "   end asc";

			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Structure>(Structure.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}


	@Override
	public List<Structure> getWorkStatusListForStructureForm(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "SELECT DISTINCT(work_status_fk) from work where work_status_fk <> ''";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Structure>(Structure.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Structure> getUnitsListForStructureForm(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "SELECT id, unit, value from money_unit";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Structure>(Structure.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Structure> getFileTypeForStructureForm(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "SELECT structure_file_type from structure_file_type";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Structure>(Structure.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}



}

