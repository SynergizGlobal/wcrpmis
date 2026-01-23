package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Form;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DBConnectionHandler;
@Repository
public class FormAccessRepository implements IFormAccessRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Form> getFormsList(Form obj) throws Exception {
		List<Form>  objsList = null;
		try {
			String qry ="SELECT f1.form_id,f1.module_name_fk,f1.form_name,f1.parent_form_id_sr_fk,f1.web_form_url,f1.mobile_form_url,f1.priority,f1.soft_delete_status_fk,"
					+ "f2.form_name as folder_name,f1.display_in_mobile,f1.url_type, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_role_access, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_type_access, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_access "
					+ "FROM form f1 "
					+ "LEFT OUTER JOIN form f2 on f1.parent_form_id_sr_fk = f2.form_id "
					+ "where f1.parent_form_id_sr_fk is not null";
			qry = qry + " and f1.url_type = ? ";
			int arrSize = 4;
			if("MRVC".equals(obj.getUser_type_access())) {
				qry = qry + " and f1.web_form_url IS NOT NULL and f1.web_form_url <> '' ";
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				qry = qry + " and f1.module_name_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				qry = qry + " and f1.soft_delete_status_fk = ? ";
				arrSize++;
			}

			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = "user_role";
			pValues[i++] = "user_type";
			pValues[i++] = "user";
			pValues[i++] = "Update Forms";
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				pValues[i++] = obj.getModule_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				pValues[i++] = obj.getSoft_delete_status_fk();
			}
			
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Form>(Form.class));
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Form> getModulesFilterListInForm(Form obj) throws Exception {
		List<Form> objsList = null;
		try { 
			String qry = "SELECT module_name_fk from form  " + 
					"where module_name_fk is not null and module_name_fk <> '' ";
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				qry = qry + " and module_name_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				qry = qry + " and soft_delete_status_fk = ? ";
				arrSize++;
			}
			qry = qry + " and url_type = ? ";
			qry = qry + " GROUP BY module_name_fk ORDER BY module_name_fk";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				pValues[i++] = obj.getModule_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				pValues[i++] = obj.getSoft_delete_status_fk();
			}
			pValues[i++] = "Update Forms";
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Form>(Form.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Form> getStatusFilterListInForm(Form obj) throws Exception {
		List<Form> objsList = null;
		try {
			String qry = "SELECT soft_delete_status_fk from form  " + 
					"where soft_delete_status_fk is not null and soft_delete_status_fk <> '' ";
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				qry = qry + " and module_name_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				qry = qry + " and soft_delete_status_fk = ? ";
				arrSize++;
			}
			qry = qry + " and url_type = ? ";
			qry = qry + " GROUP BY soft_delete_status_fk ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getModule_name_fk())) {
				pValues[i++] = obj.getModule_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSoft_delete_status_fk())) {
				pValues[i++] = obj.getSoft_delete_status_fk();
			}
			pValues[i++] = "Update Forms";
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Form>(Form.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Form> getModulesListForFormAccess(Form obj) throws Exception {
		List<Form> objsList = null;
		try {
			String qry = "select module_name as module_name_fk from module ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Form>(Form.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return objsList;
	}
	@Override
	public List<Form> getFolderssListForFormAccess(Form obj) throws Exception {
		List<Form> objsList = null;
		try {
			String qry = "SELECT form_id,form_name FROM form where url_type = ? ";
			objsList = jdbcTemplate.query( qry,new Object[]{"Update Forms"}, new BeanPropertyRowMapper<Form>(Form.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return objsList;
	}
	@Override
	public List<Form> getStatusListForFormAccess(Form obj) throws Exception {
		List<Form> objsList = null;
		try {
			String qry = "select soft_delete_status as soft_delete_status_fk from soft_delete_status ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Form>(Form.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return objsList;
	}
	@Override
	public List<Form> getUserRolesInFormAccess(Form obj) throws Exception {
		List<Form> objsList = null;
		try {
			String qry = "select user_role_name as access_value_id FROM user_role";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Form>(Form.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Form> getUserTypesInFormAccess(Form obj) throws Exception {
		List<Form> objsList = null;
		try {
			String qry = "select user_type as access_value_id FROM user_type";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Form>(Form.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Form> getUsersInFormAccess(Form obj) throws Exception {
		List<Form> objsList = null;
		try {
			String qry = "select user_id as access_value_id,user_name as access_value_name FROM [user]";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Form>(Form.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public Form getForm(Form obj) throws Exception {
		Form dObj = null;
		try {
			String qry ="SELECT f1.form_id,f1.module_name_fk,f1.form_name,f1.parent_form_id_sr_fk,f1.web_form_url,f1.mobile_form_url,f1.priority,f1.soft_delete_status_fk,"
					+ "f2.form_name as folder_name,f1.display_in_mobile,f1.url_type, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_role_access, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_type_access, "
					+ "(select STRING_AGG(access_value,',') from form_access where form_id_fk = f1.form_id and access_type = ?) as user_access "
					+ "FROM form f1 " 
					+ "LEFT OUTER JOIN form f2 on f1.parent_form_id_sr_fk = f2.form_id "
					+ "where f1.form_id = ? " ;
			
			int arrSize = 4;

			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = "user_role";
			pValues[i++] = "user_type";
			pValues[i++] = "user";
			pValues[i++] = obj.getForm_id();
			
			dObj = (Form)jdbcTemplate.queryForObject(qry, pValues, new BeanPropertyRowMapper<Form>(Form.class));
			
			if(!StringUtils.isEmpty(dObj) && !StringUtils.isEmpty(dObj.getForm_id())) {
				String qryUserPermission = "select access_type,access_value from form_access where form_id_fk = ? " ;
				
				List<Form> objsList = jdbcTemplate.query(qryUserPermission, new Object[] {dObj.getForm_id()}, new BeanPropertyRowMapper<Form>(Form.class));	
				
				dObj.setAccessPermissions(objsList);
			}
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return dObj;
	}
	@Override
	public Boolean addForm(Form obj) throws Exception {
		boolean flag = false;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
			String form_id = getformId(con);
			obj.setForm_id(form_id);
			obj.setUrl_type("Update Forms");
			String insertQry = "INSERT INTO form"
					+ "( form_id,form_name, module_name_fk, web_form_url,mobile_form_url, "
					+ "soft_delete_status_fk,display_in_mobile,url_type)"
					+ "VALUES"
					+ "(:form_id,:form_name,:module_name_fk,:web_form_url,:mobile_form_url,"
					+ ":soft_delete_status_fk,:display_in_mobile,:url_type)";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = namedParamJdbcTemplate.update(insertQry, paramSource);			
			if(count > 0) {
				flag = true;
				
				int arraySize = 0;
				if(!StringUtils.isEmpty(obj.getAccess_types()) && obj.getAccess_types().length > 0 ) {
					obj.setAccess_types(CommonMethods.replaceEmptyByNullInSringArray(obj.getAccess_types()));
					if(arraySize < obj.getAccess_types().length) {
						arraySize = obj.getAccess_types().length;
					}
				}
				if(!StringUtils.isEmpty(obj.getAccess_values()) && obj.getAccess_values().length > 0 ) {
					obj.setAccess_values(CommonMethods.replaceEmptyByNullInSringArray(obj.getAccess_values()));
					if(arraySize < obj.getAccess_values().length) {
						arraySize = obj.getAccess_values().length;
					}
				}
				
				if(arraySize > 0) {				
					
					String[] types = obj.getAccess_types();
					String[] values = obj.getAccess_values();
					
					String qryUserPermissions = "INSERT INTO form_access (form_id_fk,access_type,access_value) VALUES  (?,?,?)";		
					
					int[] counts = jdbcTemplate.batchUpdate(qryUserPermissions,
				            new BatchPreparedStatementSetter() {
				                 
				                @Override
				                public void setValues(PreparedStatement ps, int i) throws SQLException {
				                    ps.setString(1, form_id);
				                    ps.setString(2, types.length > 0?types[i]:null);
				                    ps.setString(3, values.length > 0?values[i]:null);			                    
				                }
				                @Override  
				                public int getBatchSize() {				                	
				                	int arraySize = 0;
				    				if(!StringUtils.isEmpty(obj.getAccess_types()) && obj.getAccess_types().length > 0 ) {
				    					obj.setAccess_types(CommonMethods.replaceEmptyByNullInSringArray(obj.getAccess_types()));
				    					if(arraySize < obj.getAccess_types().length) {
				    						arraySize = obj.getAccess_types().length;
				    					}
				    				}
				    				if(!StringUtils.isEmpty(obj.getAccess_values()) && obj.getAccess_values().length > 0 ) {
				    					obj.setAccess_values(CommonMethods.replaceEmptyByNullInSringArray(obj.getAccess_values()));
				    					if(arraySize < obj.getAccess_values().length) {
				    						arraySize = obj.getAccess_values().length;
				    					}
				    				}
				    				return arraySize; 
				                }
				            });
					
				}
			}
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return flag;
	}
	
	private String getformId(Connection con) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String obj = null;
		try {
		
			String qry = "select (form_id + 1) as form_id from form ORDER BY form_id DESC offset 0 rows  fetch next 1 rows only";
			ps  = con.prepareStatement(qry);
			rs = ps.executeQuery();
			if(rs.next()) {
				obj = rs.getString("form_id");
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(con, ps, rs);
		}
		return obj;
	}
	@Override
	public Boolean updateForm(Form obj) throws Exception {
		boolean flag = false;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());	
			String updateQry = "UPDATE form set "
					+ "form_name= :form_name,"
					+ "module_name_fk= :module_name_fk,parent_form_id_sr_fk=:parent_form_id_sr_fk, web_form_url= :web_form_url, mobile_form_url= :mobile_form_url, priority= :priority, "
					+ "soft_delete_status_fk= :soft_delete_status_fk,display_in_mobile=:display_in_mobile "
					+ "where form_id= :form_id";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = namedParamJdbcTemplate.update(updateQry, paramSource);			
			if(count > 0) {
				flag = true;
				
				String deleteQry ="delete from form_access where form_id_fk = :form_id ";
				paramSource = new BeanPropertySqlParameterSource(obj);		 
				count = namedParamJdbcTemplate.update(deleteQry, paramSource);
				
				int arraySize = 0;
				if(!StringUtils.isEmpty(obj.getAccess_types()) && obj.getAccess_types().length > 0 ) {
					obj.setAccess_types(CommonMethods.replaceEmptyByNullInSringArray(obj.getAccess_types()));
					if(arraySize < obj.getAccess_types().length) {
						arraySize = obj.getAccess_types().length;
					}
				}
				if(!StringUtils.isEmpty(obj.getAccess_values()) && obj.getAccess_values().length > 0 ) {
					obj.setAccess_values(CommonMethods.replaceEmptyByNullInSringArray(obj.getAccess_values()));
					if(arraySize < obj.getAccess_values().length) {
						arraySize = obj.getAccess_values().length;
					}
				}
				
				
				if(arraySize > 0) {				
					
					String[] types = obj.getAccess_types();
					String[] values = obj.getAccess_values();
					
					String qryUserPermissions = "INSERT INTO form_access (form_id_fk,access_type,access_value) VALUES  (?,?,?)";		
					
					int[] counts = jdbcTemplate.batchUpdate(qryUserPermissions,
				            new BatchPreparedStatementSetter() {
				                 
				                @Override
				                public void setValues(PreparedStatement ps, int i) throws SQLException {
				                    ps.setString(1, obj.getForm_id());
				                    ps.setString(2, types.length > 0?types[i]:null);
				                    ps.setString(3, values.length > 0?values[i]:null);			                    
				                }
				                @Override  
				                public int getBatchSize() {				                	
				                	int arraySize = 0;
				    				if(!StringUtils.isEmpty(obj.getAccess_types()) && obj.getAccess_types().length > 0 ) {
				    					obj.setAccess_types(CommonMethods.replaceEmptyByNullInSringArray(obj.getAccess_types()));
				    					if(arraySize < obj.getAccess_types().length) {
				    						arraySize = obj.getAccess_types().length;
				    					}
				    				}
				    				if(!StringUtils.isEmpty(obj.getAccess_values()) && obj.getAccess_values().length > 0 ) {
				    					obj.setAccess_values(CommonMethods.replaceEmptyByNullInSringArray(obj.getAccess_values()));
				    					if(arraySize < obj.getAccess_values().length) {
				    						arraySize = obj.getAccess_values().length;
				    					}
				    				}
				    				return arraySize; 
				                }
				            });
					
				}
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return flag;
	}
	@Override
	public Boolean updateAccessForm(Form obj) throws Exception {
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
						Form msgObj = new Form();
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
						Form msgObj = new Form();
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
						Form msgObj = new Form();
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
