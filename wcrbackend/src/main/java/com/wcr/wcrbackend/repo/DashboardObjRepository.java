package com.wcr.wcrbackend.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.DashboardFilterDTO;
import com.wcr.wcrbackend.DTO.DashboardObj;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DashboardObjRepository implements IDashboardObjRepository{

	private final JdbcTemplate jdbcTemplate;
	
	private int parseDashboardId(Object idObj) {
        if (idObj == null) return 0;
        try {
            return Integer.parseInt(String.valueOf(idObj));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
	
	 public void saveAccess(int dashboardId, String accessType, String accessValue) {
	        if (accessValue == null || accessValue.trim().isEmpty() || accessType == null) return;

	        // split on comma and trim each value
	        String[] parts = accessValue.split("\\s*,\\s*");
	        String insert = "INSERT INTO dashboard_access (dashboard_id_fk, access_type, access_value) VALUES (?, ?, ?)";

	        for (String val : parts) {
	            if (val == null || val.isBlank()) continue;
	            jdbcTemplate.update(insert, dashboardId, accessType, val);
	        }
	    }

	 public void saveAccess(String dashboardIdStr, String accessType, String accessValue) {
	        int id = parseDashboardId(dashboardIdStr);
	        if (id == 0) return;
	        saveAccess(id, accessType, accessValue);
	    }
	
	 private int generateNewDashboardId() {
	        Integer next = jdbcTemplate.queryForObject("SELECT ISNULL(MAX(dashboard_id), 0) + 1 FROM dashboard", Integer.class);
	        return next == null ? 1 : next;
	    }

	 
    @Override
    public List<DashboardObj> getDashboardNames() {

        String sql = """
            SELECT dashboard_id, dashboard_name
            FROM dashboard
            WHERE soft_delete_status_fk = 'Active'
            ORDER BY dashboard_name
        """;

        return jdbcTemplate.query(sql, (rs, row) -> {
            DashboardObj dto = new DashboardObj();
            dto.setDashboard_id(rs.getString("dashboard_id"));
            dto.setDashboard_name(rs.getString("dashboard_name"));
            return dto;
        });
    }
    
    
	    @Override
	    public List<DashboardObj> getDashboardList(DashboardFilterDTO f) {
	    	String sql = """
	    		    SELECT 
	    		        d.dashboard_id,
	    		        d.dashboard_name,
	    		        d.module_name_fk,
	    		        d1.dashboard_name AS folder,
	    		        d.soft_delete_status_fk,

	    		        ISNULL(STUFF((
	    		            SELECT DISTINCT ',' + a.access_value 
	    		            FROM dashboard_access a 
	    		            WHERE a.dashboard_id_fk = d.dashboard_id 
	    		              AND a.access_type = 'user_role'
	    		            FOR XML PATH('')),1,1,''), '-') AS user_role_access,

	    		        ISNULL(STUFF((
	    		            SELECT DISTINCT ',' + a.access_value 
	    		            FROM dashboard_access a 
	    		            WHERE a.dashboard_id_fk = d.dashboard_id
	    		              AND a.access_type = 'user_type'
	    		            FOR XML PATH('')),1,1,''), '-') AS user_type_access,

	    		        ISNULL(STUFF((
	    		            SELECT DISTINCT ',' + a.access_value 
	    		            FROM dashboard_access a 
	    		            WHERE a.dashboard_id_fk = d.dashboard_id
	    		              AND a.access_type = 'user'
	    		            FOR XML PATH('')),1,1,''), '-') AS user_access

	    		    FROM dashboard d
	    		    LEFT JOIN dashboard d1 ON d.parent_dashboard_id_sr_fk = d1.dashboard_id
	    		    WHERE 1 = 1
	    		""";
	
	        List<Object> params = new ArrayList<>();
	
	        if (f.getModule_name_fk() != null && !f.getModule_name_fk().isEmpty()) {
	            sql += " AND d.module_name_fk = ? ";
	            params.add(f.getModule_name_fk());
	        }
	
	        if (f.getDashboard_type_fk() != null && !f.getDashboard_type_fk().isEmpty()) {
	            sql += " AND d.dashboard_type_fk = ? ";
	            params.add(f.getDashboard_type_fk());
	        }
	
	        if (f.getSoft_delete_status_fk() != null && !f.getSoft_delete_status_fk().isEmpty()) {
	            sql += " AND d.soft_delete_status_fk = ? ";
	            params.add(f.getSoft_delete_status_fk());
	        }
	
	        sql += " ORDER BY d.dashboard_name";
	
	        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> {
	            DashboardObj dto = new DashboardObj();
	            dto.setDashboard_id(rs.getString("dashboard_id"));
	            dto.setDashboard_name(rs.getString("dashboard_name"));
	            dto.setModule_name_fk(rs.getString("module_name_fk"));
	            dto.setFolder(rs.getString("folder"));
	            dto.setSoft_delete_status_fk(rs.getString("soft_delete_status_fk"));
	
	            dto.setUser_role_access(rs.getString("user_role_access"));
	            dto.setUser_type_access(rs.getString("user_type_access"));
	            dto.setUser_access(rs.getString("user_access"));
	
	            return dto;
	        });
	    }

	    
	    public DashboardObj getDashboardById(String id) {

	        String sql = """
	            SELECT
	                d.dashboard_id,
	                d.dashboard_name,
	                d.module_name_fk,
	                d.dashboard_type_fk,
	                d.priority,
	                d.mobile_view,
	                d.soft_delete_status_fk,
	                d1.dashboard_name AS folder,
	                d.parent_dashboard_id_sr_fk,

	                (SELECT STRING_AGG(access_value, ',')
	                 FROM dashboard_access
	                 WHERE dashboard_id_fk = d.dashboard_id
	                   AND access_type = 'user_role') AS user_role_access,

	                (SELECT STRING_AGG(access_value, ',')
	                 FROM dashboard_access
	                 WHERE dashboard_id_fk = d.dashboard_id
	                   AND access_type = 'user_type') AS user_type_access,

	                (SELECT STRING_AGG(access_value, ',')
	                 FROM dashboard_access
	                 WHERE dashboard_id_fk = d.dashboard_id
	                   AND access_type = 'user') AS user_access
	            FROM dashboard d
	            LEFT JOIN dashboard d1 ON d.parent_dashboard_id_sr_fk = d1.dashboard_id
	            WHERE d.dashboard_id = ?
	        """;

	        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
	            DashboardObj dto = new DashboardObj();
	            dto.setDashboard_id(rs.getString("dashboard_id"));
	            dto.setDashboard_name(rs.getString("dashboard_name"));
	            dto.setModule_name_fk(rs.getString("module_name_fk"));
	            dto.setDashboard_type_fk(rs.getString("dashboard_type_fk"));
	            dto.setPriority(rs.getString("priority"));
	            dto.setMobile_view(rs.getString("mobile_view"));
	            dto.setSoft_delete_status_fk(rs.getString("soft_delete_status_fk"));
	            dto.setFolder(rs.getString("folder"));
	            dto.setParent_dashboard_id_sr_fk(rs.getString("parent_dashboard_id_sr_fk"));

	            dto.setUser_role_access(rs.getString("user_role_access"));
	            dto.setUser_type_access(rs.getString("user_type_access"));
	            dto.setUser_access(rs.getString("user_access"));
	            return dto;
	        }, id);
	    }
	    
	 // ---------- saveDashboard ---------
	    public int saveDashboard(DashboardObj dto) throws Exception {
	    	
	    	int newId = generateNewDashboardId();
	    	
	    	Integer parentId = dto.getParent_dashboard_id_sr_fk() != null
	    	        ? Integer.parseInt(dto.getParent_dashboard_id_sr_fk())
	    	        : newId;


	        String dashUrl = "/overview-dashboard/" + newId;

	        String sql = """
	        	    INSERT INTO dashboard (
	        	        dashboard_id,
	        	        dashboard_name,
	        	        project_id_fk,
	        	        contract_id_fk,
	        	        module_name_fk,
	        	        parent_dashboard_id_sr_fk,
	        	        dashboard_url,
	        	        mobile_view,
	        	        dashboard_type_fk,
	        	        priority,
	        	        icon_path,
	        	        published_by_user_id_fk,
	        	        published_on,
	        	        modified_by_user_id_fk,
	        	        modified_on,
	        	        soft_delete_status_fk
	        	    )
	        	    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), NULL, NULL, ?)
	        	""";

	        	jdbcTemplate.update(sql,
	        	    newId,
	        	    dto.getDashboard_name(),
	        	    dto.getProject_id_fk(),
	        	    dto.getContract_id_fk(),
	        	    dto.getModule_name_fk(),
	        	    parentId,
	        	    dashUrl,
	        	    dto.getMobile_view(),
	        	    dto.getDashboard_type_fk(),
	        	    dto.getPriority(),
	        	    dto.getIcon_path(),
	        	    dto.getPublished_by_user_id_fk(),   // THIS WORKS NOW  
	        	    dto.getSoft_delete_status_fk()
	        	);

	        saveAccess(newId, "user_role", dto.getUser_role_access());
	        saveAccess(newId, "user_type", dto.getUser_type_access());
	        saveAccess(newId, "user", dto.getUser_access());

	        return newId;
	    }


	    // ---------- updateDashboard
	    public void updateDashboard(DashboardObj dto) throws Exception {
	    	
	    	Integer parentId = dto.getParent_dashboard_id_sr_fk() != null
	    	        ? Integer.parseInt(dto.getParent_dashboard_id_sr_fk())
	    	        : Integer.parseInt(dto.getDashboard_id());

	    	String sql = """
	    		    UPDATE dashboard
	    		    SET 
	    		        dashboard_name = ?,
	    		        project_id_fk = ?,
	    		        contract_id_fk = ?,
	    		        module_name_fk = ?,
	    		        parent_dashboard_id_sr_fk = ?,
	    		        mobile_view = ?,
	    		        dashboard_type_fk = ?,
	    		        priority = ?,
	    		        icon_path = ?,
	    		        modified_by_user_id_fk = ?, 
	    		        modified_on = GETDATE(),
	    		        soft_delete_status_fk = ?
	    		    WHERE dashboard_id = ?
	    		""";

	    		jdbcTemplate.update(sql,
	    		    dto.getDashboard_name(),
	    		    dto.getProject_id_fk(),
	    		    dto.getContract_id_fk(),
	    		    dto.getModule_name_fk(),
	    		    parentId,
	    		    dto.getMobile_view(),
	    		    dto.getDashboard_type_fk(),
	    		    dto.getPriority(),
	    		    dto.getIcon_path(),
	    		    dto.getModified_by_user_id_fk(),  // NOW CORRECT
	    		    dto.getSoft_delete_status_fk(),
	    		    Integer.parseInt(dto.getDashboard_id())
	    		);

	        jdbcTemplate.update("DELETE FROM dashboard_access WHERE dashboard_id_fk = ?", dto.getDashboard_id());

	        // Save updated access
	        saveAccess(Integer.parseInt(dto.getDashboard_id()), "user_role", dto.getUser_role_access());
	        saveAccess(Integer.parseInt(dto.getDashboard_id()), "user_type", dto.getUser_type_access());
	        saveAccess(Integer.parseInt(dto.getDashboard_id()), "user", dto.getUser_access());
	    }


}

