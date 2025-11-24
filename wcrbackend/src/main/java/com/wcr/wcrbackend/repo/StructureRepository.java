package com.wcr.wcrbackend.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.ProjectStructureSummaryDto;
import com.wcr.wcrbackend.DTO.StructureNameDto;
import com.wcr.wcrbackend.DTO.StructureSummaryDto;
import com.wcr.wcrbackend.DTO.StructureTypeDto;

import java.util.ArrayList;
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
        String sql =
            "SELECT structure_id, structure_name, structure_type_fk " +
            "FROM structure " +
            "WHERE project_id_fk = ?";

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
            SELECT structure_type_fk, structure_id, structure_name
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
            String name = (String) row.get("structure_name");

            grouped
                .computeIfAbsent(type, k -> new ArrayList<>())
                .add(new StructureNameDto(id, name));
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
    
    
    public void insertStructure(String name, String projectId, String type) {
        String sql = """
            INSERT INTO structure (structure_name, project_id_fk, structure_type_fk)
            VALUES (?, ?, ?)
        """;

        jdbcTemplate.update(sql, name, projectId, type);
    }
    
 // UPDATE (structureId required)
    @Override
    public void updateStructure(String id, String name, String type) {
        String sql = """
            UPDATE structure
            SET structure_name = ?, structure_type_fk = ?
            WHERE structure_id = ?
        """;
        jdbcTemplate.update(sql, name, type, id);
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


}

