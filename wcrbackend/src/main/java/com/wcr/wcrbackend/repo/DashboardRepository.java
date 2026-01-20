package com.wcr.wcrbackend.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.Dashboard;
import com.wcr.wcrbackend.DTO.DashboardMenuDTO;
import com.wcr.wcrbackend.DTO.DashboardSubMenuDTO;
import com.wcr.wcrbackend.entity.User;

@Repository
public class DashboardRepository implements IDashboardRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Dashboard> getDashboardsList(String dashboardType, User user) {
		String sql = """
				SELECT DISTINCT
				    tum.dashboard_id,
				    tum.dashboard_name,
				    dashboard_url,
				    tum.priority,
				    icon_path,
				    mobile_view
				FROM dashboard tum
				LEFT JOIN user_module m ON tum.module_name_fk = m.module_fk
				WHERE m.executive_id_fk = ?
				  AND m.soft_delete_status = ?
				  AND parent_dashboard_id_sr_fk = tum.dashboard_id
				  AND tum.soft_delete_status_fk = ?
				  AND dashboard_type_fk = ?
				  AND (
				      (SELECT COUNT(*)
				       FROM dashboard_access
				       WHERE dashboard_id_fk = tum.dashboard_id
				         AND (access_value = ? OR access_value = ? OR access_value = ?)
				      ) > 0
				      OR dashboard_url IS NULL
				      OR dashboard_url = ''
				  )
				ORDER BY priority
				""";
		return jdbcTemplate.query(sql,
				new Object[] { user.getUserId(), "Active", "Active", dashboardType, user.getUserTypeFk(), user.getUserRoleNameFk(), user.getUserId() },
				new RowMapper<Dashboard>() {
					@Override
					public Dashboard mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						Dashboard dashboard = new Dashboard();
						dashboard.setTableauDashboardId(resultSet.getString("dashboard_id"));
						dashboard.setTableauDashboardName(resultSet.getString("dashboard_name"));
						//dashboard.setWork_id_fk(resultSet.getString("work_id_fk"));
						dashboard.setTableauUrl(resultSet.getString("dashboard_url"));
						dashboard.setPriority(resultSet.getString("priority"));
						dashboard.setImagePath(resultSet.getString("icon_path"));
						List<Dashboard> tableauSubList = getTableauSubList(dashboard.getTableauDashboardId(), user);
						if (!tableauSubList.isEmpty() && tableauSubList.size() > 0) {
							dashboard.setTableauSubList(tableauSubList);
						}

						return dashboard;
					}
				});
	}

	private List<Dashboard> getTableauSubList(String parentId, User user) {
		String sql = """
				SELECT DISTINCT
				    tum.dashboard_id,
				    tum.dashboard_name,
				    tum.dashboard_url,
				    tum.priority,
				    icon_path,
				    mobile_view
				FROM dashboard tum
				WHERE parent_dashboard_id_sr_fk <> tum.dashboard_id
				  AND parent_dashboard_id_sr_fk = ?
				  AND tum.soft_delete_status_fk = ?
				  AND (
				      (SELECT COUNT(*)
				       FROM dashboard_access
				       WHERE dashboard_id_fk = tum.dashboard_id
				         AND (access_value = ? OR access_value = ? OR access_value = ?)
				      ) > 0
				      OR dashboard_url IS NULL
				      OR dashboard_url = ''
				  )
				ORDER BY priority
				""";
		return jdbcTemplate.query(sql, new Object[] { parentId, "Active", user.getUserTypeFk(), user.getUserRoleNameFk(), user.getUserId() },
				new RowMapper<Dashboard>() {
					@Override
					public Dashboard mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						Dashboard dashboard = new Dashboard();
						dashboard.setTableauDashboardId(resultSet.getString("dashboard_id"));
						dashboard.setTableauDashboardName(resultSet.getString("dashboard_name"));
						//dashboard.setWork_id_fk(resultSet.getString("work_id_fk"));
						dashboard.setTableauUrl(resultSet.getString("dashboard_url"));
						dashboard.setPriority(resultSet.getString("priority"));
						dashboard.setImagePath(resultSet.getString("icon_path"));
						List<Dashboard> tableauSubList = getTableauSubListLevel2(dashboard.getTableauDashboardId(), user);
						if (!tableauSubList.isEmpty() && tableauSubList.size() > 0) {
							dashboard.setTableauSubList(tableauSubList);
						}

						return dashboard;
					}
				});
	}

	private List<Dashboard> getTableauSubListLevel2(String parentId, User user) {
		String sql =  """
			    SELECT DISTINCT 
		        tum.dashboard_id,
		        tum.dashboard_name,
		        tum.priority,
		        icon_path
		    FROM dashboard tum
		    WHERE parent_dashboard_id_sr_fk <> tum.dashboard_id
		      AND parent_dashboard_id_sr_fk = ?
		      AND tum.soft_delete_status_fk = ?
		      AND (
		          SELECT COUNT(*) 
		          FROM dashboard_access 
		          WHERE dashboard_id_fk = tum.dashboard_id 
		            AND (access_value = ? OR access_value = ? OR access_value = ?)
		      ) > 0
		    ORDER BY priority
		    """;
		return jdbcTemplate.query(sql, new Object[] { parentId, "Active", user.getUserId(), user.getUserRoleNameFk(), user.getUserTypeFk() },
				new RowMapper<Dashboard>() {
					@Override
					public Dashboard mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						Dashboard dashboard = new Dashboard();
						dashboard.setTableauDashboardId(resultSet.getString("dashboard_id"));
						dashboard.setTableauDashboardName(resultSet.getString("dashboard_name"));
						//dashboard.setWork_id_fk(resultSet.getString("work_id_fk"));
						dashboard.setTableauUrl(resultSet.getString("dashboard_url"));
						dashboard.setPriority(resultSet.getString("priority"));
						dashboard.setImagePath(resultSet.getString("icon_path"));

						return dashboard;
					}
				});
	}
	
	public List<DashboardMenuDTO> getActiveMenuItems() {
	    String mainMenuSql = """
	        SELECT menu_id, menu_name, url, has_sub_menu
	        FROM dashboard_menu
	        WHERE active = 1
	        ORDER BY menu_id
	        """;

	    List<DashboardMenuDTO> menuList = jdbcTemplate.query(mainMenuSql, (rs, rowNum) -> {
	        DashboardMenuDTO menu = new DashboardMenuDTO();
	        menu.setMenuId(rs.getInt("menu_id"));
	        menu.setMenuName(rs.getString("menu_name"));
	        menu.setUrl(rs.getString("url"));
	        menu.setHasSubMenu(rs.getBoolean("has_sub_menu"));
	        return menu;
	    });

	    String subMenuSql = """
	        SELECT sub_menu_id, menu_id AS menu_id_fk, sub_menu_name, url
	        FROM dashboard_sub_menu
	        WHERE active = 1 AND menu_id = ?
	        ORDER BY sub_menu_id
	        """;

	    for (DashboardMenuDTO menu : menuList) {
	        List<DashboardSubMenuDTO> subMenuList = jdbcTemplate.query(subMenuSql,
	            new Object[]{menu.getMenuId()},
	            (rs, rowNum) -> {
	                DashboardSubMenuDTO sub = new DashboardSubMenuDTO();
	                sub.setSubMenuId(rs.getInt("sub_menu_id"));
	                sub.setSubMenuName(rs.getString("sub_menu_name"));
	                sub.setUrl(rs.getString("url"));
	                return sub;
	            });
	        menu.setSubMenus(subMenuList);
	    }

	    return menuList;
	}


}
