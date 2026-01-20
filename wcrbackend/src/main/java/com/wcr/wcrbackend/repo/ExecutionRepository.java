package com.wcr.wcrbackend.repo;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.ExecutionProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExecutionRepository implements IExecutionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ExecutionProgress> getExecutionProgress(String projectId) {

        String sql =
            "SELECT " +
            "  p.project_name AS project, " +
            "  ISNULL(p.from_chainage,0)/1000 AS projectFromKm, " +
            "  ISNULL(p.to_chainage,0)/1000 AS projectToKm, " +
            "  s.structure_type_fk AS structureType, " +
            "  s.structure AS subStructure, " +
            "  ISNULL(s.from_chainage,0)/1000 AS fromKm, " +
            "  ISNULL(s.to_chainage,0)/1000 AS toKm, " +
            "  s.work_status_fk AS status, " +
            "  c.contract_name AS contract, " +
            "  c.contract_name, " +
            "  c.contract_id AS contract_id, " +
            "  ctr.contractor_name AS contractor, " +
            "  COALESCE( " +
            "      SUM((ISNULL(a.completed,0)/NULLIF(a.scope,0)) * ISNULL(a.weightage,0)) " +
            "      / NULLIF(SUM(ISNULL(a.weightage,0)),0), 0 " +
            "  ) AS progress, " +
            "  CASE " +
            "    WHEN c.contract_id IS NULL THEN 'Grey' " +
            "    WHEN COALESCE( " +
            "         SUM((ISNULL(a.completed,0)/NULLIF(a.scope,0)) * ISNULL(a.weightage,0)) " +
            "         / NULLIF(SUM(ISNULL(a.weightage,0)),0), 0 " +
            "       ) * 100 = 100 THEN 'Green' " +
            "    WHEN COALESCE( " +
            "         SUM((ISNULL(a.completed,0)/NULLIF(a.scope,0)) * ISNULL(a.weightage,0)) " +
            "         / NULLIF(SUM(ISNULL(a.weightage,0)),0), 0 " +
            "       ) * 100 BETWEEN 90 AND 99.99 THEN 'Light Green' " +
            "    WHEN COALESCE( " +
            "         SUM((ISNULL(a.completed,0)/NULLIF(a.scope,0)) * ISNULL(a.weightage,0)) " +
            "         / NULLIF(SUM(ISNULL(a.weightage,0)),0), 0 " +
            "       ) * 100 BETWEEN 1 AND 89.99 THEN 'Orange' " +
            "    WHEN COALESCE( " +
            "         SUM((ISNULL(a.completed,0)/NULLIF(a.scope,0)) * ISNULL(a.weightage,0)) " +
            "         / NULLIF(SUM(ISNULL(a.weightage,0)),0), 0 " +
            "       ) * 100 = 0 THEN 'Blue' " +
            "    ELSE 'Grey' " +
            "  END AS barColor, " +
            "  CASE " +
            "    WHEN ABS(MAX(s.to_chainage) - MIN(s.from_chainage)) > 50 THEN c.contract_name " +
            "    ELSE NULL " +
            "  END AS barLabel " +
            "FROM structure s " +
            "INNER JOIN structure_type st ON st.structure_type = s.structure_type_fk " +
            "INNER JOIN contract c ON c.contract_id = s.contract_id_fk " +
            "INNER JOIN project p ON p.project_id = c.project_id_fk " +
            "LEFT JOIN contractor ctr ON ctr.contractor_id = c.contractor_id_fk " +
            "LEFT JOIN p6_activities a ON a.contract_id_fk = c.contract_id " +
            "WHERE s.from_chainage IS NOT NULL " +
            "  AND s.to_chainage IS NOT NULL " ;
            //"  AND s.structure_type_fk NOT IN ('LAND ACQUISITION','UTILITY SHIFTING') ";

        String groupBy =
            "GROUP BY p.project_name, p.from_chainage, p.to_chainage, " +
            "s.structure_type_fk, s.structure, s.from_chainage, s.to_chainage, " +
            "s.work_status_fk, c.contract_id, c.contract_name, ctr.contractor_name " +
            "ORDER BY p.project_name, s.structure_type_fk";

        if (projectId != null && !projectId.isEmpty()) {
            sql += " AND p.project_id = ? ";
            return jdbcTemplate.query(sql + groupBy,
                new Object[]{projectId},
                (rs, rowNum) -> new ExecutionProgress(
                    rs.getString("project"),
                    rs.getDouble("projectFromKm"),
                    rs.getDouble("projectToKm"),
                    rs.getString("contract"),
                    rs.getString("contract_id"),
                    rs.getString("contract_name"),
                    rs.getString("contractor"),
                    rs.getString("subStructure"),
                    rs.getDouble("fromKm"),
                    rs.getDouble("toKm"),
                    rs.getString("status"),
                    rs.getDouble("progress") * 100,
                    rs.getString("barColor"),
                    rs.getString("barLabel"),
                    rs.getString("structureType")
                ));
        } else {
            return jdbcTemplate.query(sql + groupBy,
                (rs, rowNum) -> new ExecutionProgress(
                    rs.getString("project"),
                    rs.getDouble("projectFromKm"),
                    rs.getDouble("projectToKm"),
                    rs.getString("contract"),
                    rs.getString("contract_id"),
                    rs.getString("contract_name"),
                    rs.getString("contractor"),
                    rs.getString("subStructure"),
                    rs.getDouble("fromKm"),
                    rs.getDouble("toKm"),
                    rs.getString("status"),
                    rs.getDouble("progress") * 100,
                    rs.getString("barColor"),
                    rs.getString("barLabel"),
                    rs.getString("structureType")
                ));
        }
    }


    @Override
    public String getProjectName(String projectId) {
        String sql = "SELECT project_name FROM project WHERE project_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{projectId}, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getContractName(String contractId) {
        String sql = "SELECT contract_short_name as contract_name FROM contract WHERE contract_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{contractId}, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


	@Override
	public List<ExecutionProgress> getContractsList(String projectId) throws Exception {
		List<ExecutionProgress> objsList = null;
		String sql = "SELECT contract_short_name as contract_name,contract_id FROM contract WHERE project_id_fk = ?";
        try {
			objsList = jdbcTemplate.query( sql,new Object[]{projectId}, new BeanPropertyRowMapper<ExecutionProgress>(ExecutionProgress.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

}
