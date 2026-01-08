package com.wcr.wcrbackend.reference.IMPLdao;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.reference.Idao.UtilityResponsibleExecutivesDao;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Repository
public class UtilityResponsibleExecutivesDaoImpl implements UtilityResponsibleExecutivesDao{
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;

	@Override
	public List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception {
		List<TrainingType> objList = null;
		try {
			String qry = "SELECT  project_id_fk, project_name, STRING_AGG(u.user_name , ',') user_name,STRING_AGG(u.user_id , ',') user_id FROM utility_shifting_executives re "
					+ "LEFT JOIN [user] u on re.executive_user_id_fk = u.user_id "
					+ "left join project p on re.project_id_fk = p.project_id "
					+ "GROUP BY project_id_fk, project_name";
			
			objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}
	@Override
	public boolean addUtilityShiftingExecutives(TrainingType obj) throws Exception {
		int count = 0;
		boolean flag = false;

		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);	
			String qry3 = "INSERT into utility_shifting_executives (project_id_fk, executive_user_id_fk) "
					+ "VALUES (:project_id_fk,:executive_user_id_fk)";

			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
			int executivesArrSize = 0,workSize = 0;;
			int len = obj.getProject_id_fks().length;
			if(!StringUtils.isEmpty(obj.getProject_id_fks()) && obj.getProject_id_fks().length > 0) {
				obj.setProject_id_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getProject_id_fks()));
				if(executivesArrSize < obj.getProject_id_fks().length) {
					executivesArrSize = obj.getProject_id_fks().length;
				}
			}
			if(executivesArrSize == 1 ) {
	    		String joined = String.join(",", obj.getExecutive_user_id_fks());
	    		String[] strArray = new String[] {joined};
	    		obj.setExecutive_user_id_fks(strArray);
	    	}
			if(!StringUtils.isEmpty(obj.getExecutive_user_id_fks()) && obj.getExecutive_user_id_fks().length > 0) {
				obj.setExecutive_user_id_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getExecutive_user_id_fks()));
				if(executivesArrSize < obj.getExecutive_user_id_fks().length) {
					executivesArrSize = obj.getExecutive_user_id_fks().length;
				}
			}
			for (int i = 0; i < executivesArrSize; i++){
				List<String> executives = null;
				if(!StringUtils.isEmpty(obj.getExecutive_user_id_fks()[i]) && !StringUtils.isEmpty(obj.getProject_id_fks()[i])){
					if(obj.getExecutive_user_id_fks()[i].contains(",")) {
						executives = new ArrayList<String>(Arrays.asList(obj.getExecutive_user_id_fks()[i].split(",")));
					}else {
						executives = new ArrayList<String>(Arrays.asList(obj.getExecutive_user_id_fks()[i]));
					}
					for(String eObj : executives) {
						if(!eObj.equals("null") && !StringUtils.isEmpty(obj.getProject_id_fks()) &&  !StringUtils.isEmpty(eObj)) {
							TrainingType fileObj = new TrainingType();
							fileObj.setProject_id_fk(obj.getProject_id_fks()[i]);
							fileObj.setExecutive_user_id_fk(eObj);
							paramSource = new BeanPropertySqlParameterSource(fileObj);
							 count = namedParamJdbcTemplate.update(qry3, paramSource);
						}
					}
					if(count > 0) {
						flag = true;
					}
				}
			
			}		
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} 
		return flag;
	}
	@Override
	public boolean updateUtilityShiftingExecutives(TrainingType obj) throws Exception {
		int count = 0;
		boolean flag = false;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);	
			if(!StringUtils.isEmpty(obj.getExecutive_user_id_fks()) && obj.getExecutive_user_id_fks().length > 0) {
					
					String conDeleteQry = "DELETE from utility_shifting_executives where project_id_fk = :project_id_fk_old";		 
					BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
					count = namedParamJdbcTemplate.update(conDeleteQry, paramSource);
			}
			String qry3 = "INSERT into utility_shifting_executives (project_id_fk,executive_user_id_fk) "
					+ "VALUES (:project_id_fk,:executive_user_id_fk)";

			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
			int executivesArrSize = 0;
			if(!StringUtils.isEmpty(obj.getProject_id_fks()) && obj.getProject_id_fks().length > 0) {
				obj.setProject_id_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getProject_id_fks()));
				if(executivesArrSize < obj.getProject_id_fks().length) {
					executivesArrSize = obj.getProject_id_fks().length;
				}
			}
			if(executivesArrSize == 1 ) {
	    		String joined = String.join(",", obj.getExecutive_user_id_fks());
	    		String[] strArray = new String[] {joined};
	    		obj.setExecutive_user_id_fks(strArray);
	    	}
			if(!StringUtils.isEmpty(obj.getExecutive_user_id_fks()) && obj.getExecutive_user_id_fks().length > 0) {
				obj.setExecutive_user_id_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getExecutive_user_id_fks()));
				if(executivesArrSize < obj.getExecutive_user_id_fks().length) {
					executivesArrSize = obj.getExecutive_user_id_fks().length;
				}
			}
			for (int i = 0; i < executivesArrSize; i++){
				List<String> executives = null;
				if(!StringUtils.isEmpty(obj.getExecutive_user_id_fks()[i])){
					if(obj.getExecutive_user_id_fks()[i].contains(",")) {
						executives = new ArrayList<String>(Arrays.asList(obj.getExecutive_user_id_fks()[i].split(",")));
					}else {
						executives = new ArrayList<String>(Arrays.asList(obj.getExecutive_user_id_fks()[i]));
					}
					for(String eObj : executives) {
						if(!eObj.equals("null") && !StringUtils.isEmpty(obj.getProject_id_fks()[i]) &&  !StringUtils.isEmpty(eObj)) {
							TrainingType fileObj = new TrainingType();
							fileObj.setProject_id_fk(obj.getProject_id_fks()[i]);
							fileObj.setExecutive_user_id_fk(eObj);
							paramSource = new BeanPropertySqlParameterSource(fileObj);
							 count = namedParamJdbcTemplate.update(qry3, paramSource);
						}
					}
					if(count > 0) {
						flag = true;
					}
				}
			
			}		
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} 
		return flag;
	}
	@Override
	public List<TrainingType> getProjectDetails(TrainingType obj) throws Exception {
		List<TrainingType> objList = null;
		try {
			String qry = "SELECT  project_id as project_id_fk, project_name FROM project ";
			
			objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}
	
	
}
