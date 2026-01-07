package com.wcr.wcrbackend.reference.IMPLdao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.reference.Idao.DashboardTypeDao;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Repository
public class DashboradTypeDaoImpl implements DashboardTypeDao{
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;

	@Override
	public List<TrainingType> getDashboardTypesList() throws Exception {
		List<TrainingType> objsList = null;
		try {
			String qry ="select dashboard_type from dashboard_type ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public boolean addDashboardType(TrainingType obj) throws Exception {
		boolean flag = false;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			String insertQry =
				    "INSERT INTO dashboard_type (dashboard_type) VALUES (:dashboard_type)";
			
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = namedParamJdbcTemplate.update(insertQry, paramSource);			
			if(count > 0) {
				flag = true;
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return flag;
	}
	
	@Override
	public boolean updateDashboardType(Map<String, String> payload) throws Exception {
	    String sql =
	        "UPDATE dashboard_type " +
	        "SET dashboard_type = :dashboard_type " +
	        "WHERE dashboard_type = :old_dashboard_type";

	    NamedParameterJdbcTemplate template =
	        new NamedParameterJdbcTemplate(dataSource);

	    MapSqlParameterSource params = new MapSqlParameterSource();
	    params.addValue("dashboard_type", payload.get("dashboard_type"));
	    params.addValue("old_dashboard_type", payload.get("old_dashboard_type"));

	    return template.update(sql, params) > 0;
	}

	@Override
	public boolean deleteDashboardType(Map<String, String> payload) throws Exception {
	    String sql =
	        "DELETE FROM dashboard_type WHERE dashboard_type = :dashboard_type";

	    NamedParameterJdbcTemplate template =
	        new NamedParameterJdbcTemplate(dataSource);

	    MapSqlParameterSource params = new MapSqlParameterSource();
	    params.addValue("dashboard_type", payload.get("dashboard_type"));

	    return template.update(sql, params) > 0;
	}


}