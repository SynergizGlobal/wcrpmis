package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.OverviewDashboard;
import com.wcr.wcrbackend.common.DBConnectionHandler;
@Repository
public class NavRepository implements INavRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<OverviewDashboard> getNavMenu(OverviewDashboard dObj) throws Exception{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<OverviewDashboard> objsList = new ArrayList<OverviewDashboard>();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			String qry = "SELECT dashboard_id,dashboard_name,dashboard_icon,dashboard_url,parent_id,source_table_name,source_field_name,source_field_value,show_left_menu "
					+ "FROM left_menu "
					+ "WHERE status = ? and parent_id = ? AND show_left_menu = ? AND dashboard_type_fk = ? and isnull(work_type,'')<>'TA' and isnull(work_type,'')<>'RollingStock' ";
			if(!StringUtils.isEmpty(dObj.getDashboard_type()) && dObj.getDashboard_type().equals("Modules")) {
				qry = qry + " AND dashboard_id = ?";
			}
			qry = qry + " ORDER BY [order]";
			statement = connection.prepareStatement(qry);
			statement.setString(1, "Active");
			statement.setString(2, dObj.getParent_id());
			statement.setString(3, "Yes");
			if(!StringUtils.isEmpty(dObj.getDashboard_type()) && dObj.getDashboard_type().equals("Modules")) 
			{
				String dType=getDashboardType(dObj.getDashboard_id(),connection);
				statement.setString(4, dType);
				statement.setString(5, dObj.getDashboard_id());
			}
			else
			{
				statement.setString(4, dObj.getDashboard_type());
			}
			resultSet = statement.executeQuery();  
			
			String work_id = dObj.getProject_id();
			while(resultSet.next()) {
				OverviewDashboard obj = new OverviewDashboard();
				String childParentId = resultSet.getString("dashboard_id");
				List<OverviewDashboard> subList = getDashboardsSubList(work_id,childParentId,connection);
				obj.setFormsSubMenu(subList);
				
				obj.setDashboard_id(resultSet.getString("dashboard_id"));
				obj.setDashboard_name(resultSet.getString("dashboard_name"));
				obj.setDashboard_icon(resultSet.getString("dashboard_icon"));
				obj.setDashboard_url(resultSet.getString("dashboard_url"));
				obj.setSource_table_name(resultSet.getString("source_table_name"));
				obj.setSource_field_name(resultSet.getString("source_field_name"));
				obj.setSource_field_value(resultSet.getString("source_field_value"));				
				if(!StringUtils.isEmpty(work_id)) {
					obj.setSource_field_value(work_id);
				}
				
				if(!StringUtils.isEmpty(obj.getSource_table_name()) && !StringUtils.isEmpty(obj.getSource_field_name()) && !StringUtils.isEmpty(obj.getSource_field_value())) {
					//obj.setWork_exists_or_not(getWorkExistsOrNot(obj,connection));
				}
				
				objsList.add(obj);
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, statement, resultSet);
		}
		return objsList;
	}
	
	private String getDashboardType(String dashboard_id,Connection con) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String dashboardtypefk = null;
		try {
			String qry = "select dashboard_type_fk from left_menu where dashboard_id = ? ";
	    	
			
			stmt = con.prepareStatement(qry);
			int k = 1;			
			
			stmt.setString(k++, dashboard_id);
			
			rs = stmt.executeQuery();  
			if(rs.next()) {
				dashboardtypefk = rs.getString("dashboard_type_fk");
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return dashboardtypefk;
	}	
	
	private List<OverviewDashboard> getDashboardsSubList(String work_id, String parentId, Connection connection) throws Exception {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<OverviewDashboard> objsList = new ArrayList<OverviewDashboard>();
		OverviewDashboard obj = null;
		try {
			String qry = "select dashboard_id,dashboard_name,dashboard_icon,dashboard_url,source_table_name,source_field_name,source_field_value,show_left_menu "
					+ "FROM left_menu "
					+ "WHERE status = ? AND parent_id <> dashboard_id AND parent_id = ? AND show_left_menu = ? and isnull(work_type,'')<>'TA' and isnull(work_type,'')<>'RollingStock' ORDER BY [order]";
			statement = connection.prepareStatement(qry);
			statement.setString(1, "Active");
			statement.setString(2, parentId);
			statement.setString(3, "Yes");
			resultSet = statement.executeQuery();  
			while(resultSet.next()) {
				obj = new OverviewDashboard();
				
				String childParentId = resultSet.getString("dashboard_id");
				List<OverviewDashboard> subList = getDashboardsSubList(work_id,childParentId,connection);
				obj.setFormsSubMenu(subList);
				
				obj.setDashboard_id(resultSet.getString("dashboard_id"));
				obj.setDashboard_name(resultSet.getString("dashboard_name"));
				obj.setDashboard_icon(resultSet.getString("dashboard_icon"));
				obj.setDashboard_url(resultSet.getString("dashboard_url"));
				obj.setSource_table_name(resultSet.getString("source_table_name"));
				obj.setSource_field_name(resultSet.getString("source_field_name"));
				obj.setSource_field_value(resultSet.getString("source_field_value"));
				if(!StringUtils.isEmpty(work_id)) {
					obj.setSource_field_value(work_id);
				}
				if(!StringUtils.isEmpty(obj.getSource_table_name()) && !StringUtils.isEmpty(obj.getSource_field_name()) && !StringUtils.isEmpty(obj.getSource_field_value())) {
					//obj.setWork_exists_or_not(getWorkExistsOrNot(obj,connection));
				}
				
				objsList.add(obj);				
			}
		
		}catch(Exception e){ 
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(null, statement, resultSet);
		}
		return objsList;
	}

}
