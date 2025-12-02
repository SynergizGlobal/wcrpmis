package com.wcr.wcrbackend.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.ProjectStructureSummaryDto;
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



}

