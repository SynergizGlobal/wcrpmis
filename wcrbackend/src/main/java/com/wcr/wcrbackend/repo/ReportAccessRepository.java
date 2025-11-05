package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Report;
@Repository
public class ReportAccessRepository implements IReportAccessRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Report> getReportsList(Report obj) throws Exception {
		List<Report>  objsList = null;
		try {
			String qry ="SELECT f1.form_id,f1.module_name_fk,f1.form_name,f1.parent_form_id_sr_fk,f1.web_form_url,f1.mobile_form_url,f1.priority,f1.soft_delete_status_fk,"
					+ "f2.form_name as folder_name,f1.display_in_mobile, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_role_access, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_type_access, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_access "
					+ "FROM form f1 "
					+ "LEFT OUTER JOIN form f2 on f1.parent_form_id_sr_fk = f2.form_id "
					+ "where f1.parent_form_id_sr_fk is not null ";
			qry = qry + " and f1.url_type = ?";
			int arrSize = 4;
			if("MRVC".equals(obj.getUser_type_access())) {
				qry = qry + " and f1.web_form_url IS NOT NULL and f1.web_form_url <> '' ";
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				qry = qry + " and f1.module_name_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				qry = qry + " and f1.soft_delete_status_fk = ?";
				arrSize++;
			}

			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = "user_role";
			pValues[i++] = "user_type";
			pValues[i++] = "user";
			pValues[i++] = "Reports";
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				pValues[i++] = obj.getModule_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				pValues[i++] = obj.getSoft_delete_status_fk();
			}
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Report>(Report.class));
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Report> getModulesFilterListInReport(Report obj) throws Exception {
		List<Report> objsList = null;
		try {
			String qry = "SELECT module_name_fk from form  " + 
					"where module_name_fk is not null and module_name_fk <> '' ";
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				qry = qry + " and module_name_fk = ?";
				arrSize++; 
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				qry = qry + " and soft_delete_status_fk = ? ";
				arrSize++;
			}
			qry = qry + " and url_type = ?";
			qry = qry + " GROUP BY module_name_fk ORDER BY module_name_fk";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				pValues[i++] = obj.getModule_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				pValues[i++] = obj.getSoft_delete_status_fk();
			}
			pValues[i++] = "Reports";
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Report>(Report.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Report> getStatusFilterListInReport(Report obj) throws Exception {
		List<Report> objsList = null;
		try {
			String qry = "SELECT soft_delete_status_fk from form  " + 
					"where soft_delete_status_fk is not null and soft_delete_status_fk <> '' ";
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				qry = qry + " and module_name_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				qry = qry + " and soft_delete_status_fk = ? ";
				arrSize++;
			}
			qry = qry + " and url_type = ?";
			qry = qry + " GROUP BY soft_delete_status_fk ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				pValues[i++] = obj.getModule_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				pValues[i++] = obj.getSoft_delete_status_fk();
			}
			pValues[i++] = "Reports";
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Report>(Report.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Report> getUserRolesInReportAccess(Report obj) throws Exception {
		List<Report> objsList = null;
		try {
			String qry = "select user_role_name as access_value_id from user_role";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Report>(Report.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Report> getUserTypesInReportAccess(Report obj) throws Exception {
		List<Report> objsList = null;
		try {
			String qry = "select user_type as access_value_id from user_type";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Report>(Report.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Report> getUsersInReportAccess(Report obj) throws Exception {
		List<Report> objsList = null;
		try {
			String qry = "select user_id as access_value_id,user_name as access_value_name from [user]";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Report>(Report.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Report> getModulesListForReportAccess(Report obj) throws Exception {
		List<Report> objsList = null;
		try {
			String qry = "select module_name as module_name_fk from module ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Report>(Report.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Report> getFolderssListForReportAccess(Report obj) throws Exception {
		List<Report> objsList = null;
		try {
			String qry = "SELECT form_id,form_name FROM form where url_type = ?";
			objsList = jdbcTemplate.query( qry,new Object[]{"Reports"}, new BeanPropertyRowMapper<Report>(Report.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return objsList;
	}
	@Override
	public List<Report> getStatusListForReportAccess(Report obj) throws Exception {
		List<Report> objsList = null;
		try {
			String qry = "select soft_delete_status as soft_delete_status_fk from soft_delete_status ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Report>(Report.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return objsList;
	}
	@Override
	public Report getReport(Report obj) throws Exception {
		Report dObj = null;
		try {
			String qry ="SELECT f1.form_id,f1.module_name_fk,f1.form_name,f1.parent_form_id_sr_fk,f1.web_form_url,f1.mobile_form_url,f1.priority,f1.soft_delete_status_fk,"
					+ "f2.form_name as folder_name,f1.display_in_mobile,f1.url_type, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_role_access, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_type_access, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_access "
					+ "FROM form f1 " 
					+ "LEFT OUTER JOIN form f2 on f1.parent_form_id_sr_fk = f2.form_id "
					+ "where f1.form_id = ?" ;
			
			int arrSize = 4;

			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = "user_role";
			pValues[i++] = "user_type";
			pValues[i++] = "user";
			pValues[i++] = obj.getForm_id();
			
			dObj = (Report)jdbcTemplate.queryForObject(qry, pValues, new BeanPropertyRowMapper<Report>(Report.class));
			
			if(!StringUtils.isEmpty(dObj) && !StringUtils.isEmpty(dObj.getForm_id())) {
				List<Report> objsList = null;
				String qryUserPermission = "select access_type,access_value from form_access where form_id_fk = ? " ;
				
				objsList = jdbcTemplate.query(qryUserPermission, new Object[] {dObj.getForm_id()}, new BeanPropertyRowMapper<Report>(Report.class));	
				
				dObj.setAccessPermissions(objsList);
			}
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return dObj;
	}
	@Override
	public boolean updateAccessReport(Report obj) throws Exception {
		boolean flag = false;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());	
			String updateQry = "UPDATE form set "
					+ "priority= :priority,soft_delete_status_fk= :soft_delete_status_fk,display_in_mobile=:display_in_mobile "
					+ "where form_id = :form_id";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = namedParamJdbcTemplate.update(updateQry, paramSource);			
			if(count > 0) {
				flag = true;
				
				String deleteQry ="delete from form_access where form_id_fk = :form_id ";
				paramSource = new BeanPropertySqlParameterSource(obj);		 
				count = namedParamJdbcTemplate.update(deleteQry, paramSource);
				
				if(!StringUtils.isEmpty(obj.getUser_role_access())) {
					String[] user_role_access = obj.getUser_role_access().split(",");
					int user_role_access_count = user_role_access.length;
					SqlParameterSource[] source = new SqlParameterSource[user_role_access_count];
					String messageQry = "INSERT INTO form_access (form_id_fk,access_type,access_value)"
							+ "VALUES" + "(:form_id,:access_type,:access_value)";
					
					for (int i = 0; i < user_role_access_count; i++) {
						Report msgObj = new Report();
						msgObj.setForm_id(obj.getForm_id());
						msgObj.setAccess_type("user_role");
						msgObj.setAccess_value(user_role_access[i]);
				        source[i] = new BeanPropertySqlParameterSource(msgObj);
				    }
					namedParamJdbcTemplate.batchUpdate(messageQry, source);
				}
				
				if(!StringUtils.isEmpty(obj.getUser_type_access())) {
					String[] user_type_access = obj.getUser_type_access().split(",");
					int user_type_access_count = user_type_access.length;
					SqlParameterSource[] source = new SqlParameterSource[user_type_access_count];
					String messageQry = "INSERT INTO form_access (form_id_fk,access_type,access_value)"
							+ "VALUES" + "(:form_id,:access_type,:access_value)";
					
					for (int i = 0; i < user_type_access_count; i++) {
						Report msgObj = new Report();
						msgObj.setForm_id(obj.getForm_id());
						msgObj.setAccess_type("user_type");
						msgObj.setAccess_value(user_type_access[i]);
				        source[i] = new BeanPropertySqlParameterSource(msgObj);
				    }
					namedParamJdbcTemplate.batchUpdate(messageQry, source);
				}
				
				if(!StringUtils.isEmpty(obj.getUser_access())) {
					String[] user_access = obj.getUser_access().split(",");
					int user_access_count = user_access.length;
					SqlParameterSource[] source = new SqlParameterSource[user_access_count];
					String messageQry = "INSERT INTO form_access (form_id_fk,access_type,access_value)"
							+ "VALUES" + "(:form_id,:access_type,:access_value)";
					
					for (int i = 0; i < user_access_count; i++) {
						Report msgObj = new Report();
						msgObj.setForm_id(obj.getForm_id());
						msgObj.setAccess_type("user");
						msgObj.setAccess_value(user_access[i]);
				        source[i] = new BeanPropertySqlParameterSource(msgObj);
				    }
					namedParamJdbcTemplate.batchUpdate(messageQry, source);
				}
				
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return flag;
	}

}
