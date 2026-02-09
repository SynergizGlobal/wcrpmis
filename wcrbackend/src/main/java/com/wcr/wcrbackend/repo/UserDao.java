package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.DTO.RandRMain;
import com.wcr.wcrbackend.DTO.Risk;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DBConnectionHandler;
import com.wcr.wcrbackend.config.EncryptDecrypt;

@Repository
public class UserDao implements IUserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public String getRoleCode(String userRoleNameFk) {
		String sql = "SELECT user_role_code FROM user_role WHERE user_role_name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userRoleNameFk}, String.class);
	}
	@Override
	public List<User> getUserTypesFilter(User obj) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select u.user_type_fk FROM [user] u "
					+ "left outer join [user] usr ON u.reporting_to_id_srfk = usr.user_id "
					+ "where u.user_type_fk is not null and u.user_type_fk <> '' " ;
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				qry = qry + " and u.user_role_name_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and u.department_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				qry = qry + " and u.reporting_to_id_srfk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				qry = qry + " and u.user_type_fk = ?";
				arrSize++;
			}
			qry = qry + " GROUP BY u.user_type_fk ORDER BY  " + 
					"					case when u.user_type_fk='HOD' then 1 " + 
					"					when u.user_type_fk='DYHOD' then 2 " + 
					"					when u.user_type_fk='Officers ( Jr./Sr. Scale )' then 3 " + 
					"					when u.user_type_fk='Others' then 4 when u.user_type_fk='Training' then 5  " + 
					"					end asc";
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				pValues[i++] = obj.getUser_role_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				pValues[i++] = obj.getReporting_to_id_srfk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_type_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<User>(User.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getUserRolesFilter(User obj) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select user_role_name_fk FROM [user] u "
					+ "where user_role_name_fk is not null and user_role_name_fk <> '' " ;
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				qry = qry + " and user_role_name_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and department_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				qry = qry + " and reporting_to_id_srfk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				qry = qry + " and u.user_type_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY user_role_name_fk ";
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				pValues[i++] = obj.getUser_role_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				pValues[i++] = obj.getReporting_to_id_srfk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_type_fk();
			}
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<User>(User.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getUserDepartmentsFilter(User obj) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select u.department_fk,department_name "
					+ "FROM [user] u "
					+ "LEFT OUTER JOIN department d ON u.department_fk = d.department "
					+ "where u.department_fk is not null and u.department_fk <> '' " ;
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				qry = qry + " and u.user_role_name_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and u.department_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				qry = qry + " and u.reporting_to_id_srfk = ? ";
				arrSize++;
			}if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				qry = qry + " and u.user_type_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY u.department_fk,department_name";
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				pValues[i++] = obj.getUser_role_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				pValues[i++] = obj.getReporting_to_id_srfk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_type_fk();
			}
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<User>(User.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getUserReportingToListFilter(User obj) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select usr.designation,u.reporting_to_id_srfk AS user_id,usr.user_name as reporting_to_name "
					+ "FROM [user] u "
					+ "left outer join [user] usr ON u.reporting_to_id_srfk = usr.user_id "
					+ "where u.reporting_to_id_srfk is not null and u.reporting_to_id_srfk <> '' and usr.user_name<> '' " ;
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				qry = qry + " and u.user_role_name_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and u.department_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				qry = qry + " and u.reporting_to_id_srfk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				qry = qry + " and u.user_type_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY usr.designation,u.reporting_to_id_srfk,usr.user_name  ";


			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				pValues[i++] = obj.getUser_role_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				pValues[i++] = obj.getReporting_to_id_srfk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_type_fk();
			}
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<User>(User.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getStructuresByContractId(User obj) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select DISTINCT Structure,structure_id as structure_id_fk from p6_activities p " + 
					"left join structure s on s.structure_id=p.structure_id_fk " + 
					"where p.contract_id_fk=?";
			
			int arrSize = 1;
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = obj.getContract_id_fk();
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<User>(User.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getUsersList(User obj) throws Exception {
		List<User> objsList = new ArrayList<User>();
		try {
			String qry = "select u.user_id,u.user_name,u.password,u.designation,u.email_id,u.mobile_number as mobile_number, " + 
					"u.personal_contact_number as personal_contact_number,u.landline as landline, " + 
					"u.extension as extension,u.department_fk,u.reporting_to_id_srfk,u.pmis_key_fk,u.user_role_name_fk, " + 
					"u.remarks,u.user_type_fk,u.user_image,department_name,usr.designation as reporting_to_name, " + 
					"(select FORMAT(max(login_date_time),'dd-MM-yyyy hh : mm : ss tt') from user_login_details where user_id_fk = u.user_id ) as last_login, " + 
					"(select COUNT(*) from user_login_details where user_id_fk = u.user_id and login_date_time >= DATEADD(day, -7, GETDATE())) as last7DaysLogins, " + 
					"(select COUNT(*) from user_login_details where user_id_fk = u.user_id and login_date_time >= DATEADD(day, -30, GETDATE())) as last30DaysLogins  " + 
					"FROM [user] u LEFT OUTER JOIN department d ON u.department_fk = d.department left outer join [user] usr ON u.reporting_to_id_srfk = usr.user_id  " + 
					"where u.user_id is not null " ;
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				qry = qry + " and u.user_role_name_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and u.department_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				qry = qry + " and u.reporting_to_id_srfk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				qry = qry + " and u.user_type_fk = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			/*pValues[i++] = CommonConstants2.LOGIN_EVENT_TYPE_LOGIN;
			pValues[i++] = CommonConstants2.LOGIN_EVENT_TYPE_LOGIN;
			pValues[i++] = CommonConstants2.LOGIN_EVENT_TYPE_LOGIN;*/
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				pValues[i++] = obj.getUser_role_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				pValues[i++] = obj.getReporting_to_id_srfk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_type_fk();
			}
			
			
			//qry = qry + " order by case when (u.user_id like '%Dummy%') then 0 else 1 end desc,case when (u.user_name like '%user%')  then 0 else 1 end desc,  " + 
					//"case when(u.pmis_key_fk like '%SGS%') then 0 else 1 end desc";
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<User>(User.class));	
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
//	@Override
//	public String checkPMISKeyAvailability(User obj) throws Exception {
//		String pmis_key = "NoKey";
//		try {
//			String qry = "select count(*) from pmis_key where pmis_key = ? ";
//			
//			int count = jdbcTemplate.queryForObject( qry,new Object[] {obj.getPmis_key_fk()}, Integer.class);	
//			
//			if(count > 0) {
//				pmis_key = "Available";
//				qry = "select count(*) FROM [user] where pmis_key_fk = ? ";				
//				count = jdbcTemplate.queryForObject( qry,new Object[] {obj.getPmis_key_fk()}, Integer.class);	
//				if(count > 0) {
//					pmis_key = "Taken";
//				}
//			}
//		}catch(Exception e){ 
//			throw new Exception(e);
//		}
//		return pmis_key;
//	}
	
	
	@Override
	public String checkPMISKeyAvailability(User obj) throws Exception {
	    String pmis_key = "Available";  // CHANGED: Default to "Available" instead of "NoKey"
	    
	    try {
	       
	        String qry = "SELECT COUNT(*) FROM [user] WHERE pmis_key_fk = ?";				
	        int count = jdbcTemplate.queryForObject(qry, new Object[] {obj.getPmis_key_fk()}, Integer.class);	
	        
	        if(count > 0) {
	            pmis_key = "Taken";  // Key is already assigned to a user
	        }
	        // If count == 0, pmis_key remains "Available"
	        
	    } catch(Exception e) { 
	        // Optional: Log the error but don't necessarily throw
	        // logger.error("Error checking PMIS key: " + e.getMessage());
	        throw new Exception(e);
	    }
	    
	    return pmis_key;
	}
	@Override
	public List<User> getUserReportingToList(User obj) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select user_id,designation,user_name FROM [user] u where u.user_name not like '%user%' and u.pmis_key_fk not like '%SGS%' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) {
				qry = qry + " and (department_fk = ? or department_fk = ?) ";
				arrSize++;
				arrSize++;
			}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and department_fk = ? ";
				arrSize++;
			} 
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) {
				pValues[i++] = obj.getDepartment_fk();
				pValues[i++] = CommonConstants.DEPARTMENT_ID_MANAGEMENT;
			}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			} 
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<User>(User.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getUserRoles() throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select user_role_name,user_role_code from user_role";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<User>(User.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getUserTypes() throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select user_type as user_type_fk from user_type";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<User>(User.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getUserDepartments() throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select department,department_name from department";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<User>(User.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getPmisKeys() throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select pmis_key as pmis_key_fk from pmis_key";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<User>(User.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getModuleSList(User obj) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select distinct module_name from module";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<User>(User.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Contract> getContractsList(User obj) throws Exception {
		List<Contract> objsList = new ArrayList<Contract>();
		try {
			String qry = "select contract_id,contract_short_name from contract " ;
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Structure> getStructuresList(User obj) throws Exception {
		List<Structure> objsList = new ArrayList<Structure>();
		try {
			String qry = "select DISTINCT structure,structure_id,p.contract_id_fk from p6_activities p " + 
					"left join structure s on s.structure_id=p.structure_id_fk " ;
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Structure>(Structure.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Risk> getRiskList(User obj) throws Exception {
		List<Risk> objsList = new ArrayList<Risk>();
		try {
			String qry = "select contract_id,contract_short_name  from contract " ;
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Risk>(Risk.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<LandAcquisition> getLandList(User obj) throws Exception {
		List<LandAcquisition> objsList = new ArrayList<LandAcquisition>();
		try {
			String qry = "select project_id_fk,project_name from la_land_identification la left join project w on la.project_id_fk = w.project_id group by project_id_fk,project_name " ;
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityList(User obj) throws Exception {
		List<UtilityShifting> objsList = new ArrayList<UtilityShifting>();
		try {
			String qry = "select project_id_fk,project_name from utility_shifting us left join project w on us.project_id_fk = w.project_id group by project_id_fk,project_name " ;
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<RandRMain> getRRList(User obj) throws Exception {
		List<RandRMain> objsList = new ArrayList<RandRMain>();
		try {
			String qry = "select r.project_id as project_id_fk,project_name from rr r left join project w on r.project_id = w.project_id group by r.project_id,project_name " ;
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<RandRMain>(RandRMain.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public String addUser(User obj) throws Exception {
		String userId = null;
		setDefaultEncryptedPassword(obj);
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);...
		try {
			String user_id = getMaxUserId(obj.getUser_role_code());
			obj.setUser_id(user_id);
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());			 
			String qry = "INSERT INTO [user]"
					+ "(user_id,user_name,password,designation,email_id,mobile_number,personal_contact_number,landline,extension,department_fk,reporting_to_id_srfk,user_type_fk,pmis_key_fk,user_role_name_fk,remarks,user_image,is_password_encrypted) "
					+ "VALUES "
					+ "(:user_id,:user_name,:password,:designation,:email_id,:mobile_number,:personal_contact_number,:landline,:extension,:department_fk,:reporting_to_id_srfk,:user_type_fk,:pmis_key_fk,:user_role_name_fk,:remarks,:user_image,:is_password_encrypted)";		 
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = namedParamJdbcTemplate.update(qry, paramSource);			
			if(count > 0) {
				userId = user_id;
				obj.setUser_id(userId);
				List<User> module =  getModuleSList(obj);
				String qryM = "INSERT INTO user_module  (soft_delete_status,executive_id_fk,module_fk  )VALUES (:soft_delete_status, :executive_id_fk ,:module_fk)" ;
				for(User uObj : module) {
					obj.setModule_fk(uObj.getModule_name());
					obj.setExecutive_id_fk(userId);
					obj.setSoft_delete_status(CommonConstants.INACTIVE);
					
					paramSource = new BeanPropertySqlParameterSource(obj);		 
					namedParamJdbcTemplate.update(qryM, paramSource);
				}
				String [] permissions = obj.getPermissions_check();
				if (permissions != null && permissions.length > 0) {
					String qry2 = "INSERT INTO user_module  (soft_delete_status,executive_id_fk,module_fk  )VALUES (:soft_delete_status, :executive_id_fk ,:module_fk)" ;
					for(int k = 0; k <= (permissions.length - 1); k++) {
							String[] permissionVal = permissions[k].split("_");
							obj.setModule_fk(permissionVal[0]);
							obj.setExecutive_id_fk(userId);
							obj.setSoft_delete_status(CommonConstants.ACTIVE);
							
							paramSource = new BeanPropertySqlParameterSource(obj);		 
							namedParamJdbcTemplate.update(qry2, paramSource);
						
						if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("Contracts")) {
							int c = 0;
							String contarctIds = obj.getContract_id();
							obj.setDepartment_id_fk(obj.getDepartment_fk());
							String qry1 = "INSERT INTO contract_executive"
									+ "(contract_id_fk,department_id_fk,executive_user_id_fk) VALUES (:contract_id_fk,:department_id_fk,:executive_user_id_fk)";	
							obj.setExecutive_user_id_fk(userId); 
							if(!StringUtils.isEmpty(contarctIds)) {
						
								
								if(contarctIds.contains(",")) {
									String [] values = contarctIds.split(",");
									int arrSz = values.length;
									for(int i = 0; i < arrSz; i++) {
										obj.setContract_id_fk(values[i]);
										paramSource = new BeanPropertySqlParameterSource(obj);		 
										c = namedParamJdbcTemplate.update(qry1, paramSource);
									}
								}else {
									obj.setContract_id_fk(contarctIds);
									paramSource = new BeanPropertySqlParameterSource(obj);		 
									c = namedParamJdbcTemplate.update(qry1, paramSource);
								}
							}
						}
						if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("Land Acquisition")) {
							int c = 0;
							String workIds = obj.getLand_work();
							String qry1 = "INSERT INTO land_executives"
									+ "(project_id_fk,executive_user_id_fk) VALUES (:project_id_fk,:executive_user_id_fk)";	
							obj.setExecutive_user_id_fk(userId); 
							if(!StringUtils.isEmpty(workIds)) {
						
								
								if(workIds.contains(",")) {
									String [] values = workIds.split(",");
									int arrSz = values.length;
									for(int i = 0; i < arrSz; i++) {
										obj.setProject_id_fk(values[i]);
										paramSource = new BeanPropertySqlParameterSource(obj);		 
										c = namedParamJdbcTemplate.update(qry1, paramSource);
									}
								}else {
									obj.setProject_id_fk(workIds);
									paramSource = new BeanPropertySqlParameterSource(obj);		 
									c = namedParamJdbcTemplate.update(qry1, paramSource);
								}
							}
						}
						if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("Utility Shifting")) {
							int c = 0;
							String workIds = obj.getUs_work();
							String qry1 = "INSERT INTO utility_shifting_executives"
									+ "(project_id_fk,executive_user_id_fk) VALUES (:project_id_fk,:executive_user_id_fk)";	
							obj.setExecutive_user_id_fk(userId); 
							if(!StringUtils.isEmpty(workIds)) {
						
								if(workIds.contains(",")) {
									String [] values = workIds.split(",");
									int arrSz = values.length;
									for(int i = 0; i < arrSz; i++) {
										obj.setProject_id_fk(values[i]);
										paramSource = new BeanPropertySqlParameterSource(obj);		 
										c = namedParamJdbcTemplate.update(qry1, paramSource);
									}
								}else {
									obj.setProject_id_fk(workIds);
									paramSource = new BeanPropertySqlParameterSource(obj);		 
									c = namedParamJdbcTemplate.update(qry1, paramSource);
								}
							}
						
						 }
							if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("R & R")) {
								int c = 0;
								String workIds = obj.getRr_work();
								String qry1 = "INSERT INTO rr_executives"
										+ "(project_id_fk,executive_user_id_fk) VALUES (:project_id_fk,:executive_user_id_fk)";	
								obj.setExecutive_user_id_fk(userId); 
								if(!StringUtils.isEmpty(workIds)) {
									if(workIds.contains(",")) {
										String [] values = workIds.split(",");
										int arrSz = values.length;
										for(int i = 0; i < arrSz; i++) {
											obj.setProject_id_fk(values[i]);
											paramSource = new BeanPropertySqlParameterSource(obj);		 
											c = namedParamJdbcTemplate.update(qry1, paramSource);
										}
									}else {
										obj.setProject_id_fk(workIds);
										paramSource = new BeanPropertySqlParameterSource(obj);		 
										c = namedParamJdbcTemplate.update(qry1, paramSource);
									}
								
							   }
						  }
							obj.setResponsible_people_id_fk(userId);
							if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("Execution &  Monitoring")) {
								int c = 0;
								String qry1 = "INSERT INTO structure_contract_responsible_people"
										+ "( structure_id_fk, contract_id_fk, responsible_people_id_fk) VALUES (:structure_id_fk,:contract_id_fk,:responsible_people_id_fk)";	
								
								int len = obj.getContract_ids().length;
								
								int size = 0;
								if(!StringUtils.isEmpty(obj.getContract_ids()) && obj.getContract_ids().length > 0) {
			    					obj.setContract_ids(CommonMethods.replaceEmptyByNullInSringArray(obj.getContract_ids()));
			    					if(size < obj.getContract_ids().length) {
			    						size = obj.getContract_ids().length;
			    					}
			    				}
								if(size == 1 ) {
						    		String joined = String.join(",", obj.getStructures());
						    		String[] strArray = new String[] {joined};
						    		obj.setStructures(strArray);
						    	}
								if(!StringUtils.isEmpty(obj.getStructures()) && obj.getStructures().length > 0) {
			    					obj.setStructures(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructures()));
			    					if(size < obj.getStructures().length) {
			    						size = obj.getStructures().length;
			    					}
			    				}
								for (int i = 0; i < size; i++){
									List<String> executives = null;
									if(!StringUtils.isEmpty(obj.getStructures()[i])){
										if(obj.getStructures()[i].contains(",")) {
											executives = new ArrayList<String>(Arrays.asList(obj.getStructures()[i].split(",")));
										}else {
											executives = new ArrayList<String>(Arrays.asList(obj.getStructures()[i]));
										}
										for(String eObj : executives) {
											if(!eObj.equals("null") && !StringUtils.isEmpty(obj.getContract_ids()[i]) &&  !StringUtils.isEmpty(eObj)) {
												User filesObj = new User();
												filesObj.setResponsible_people_id_fk(obj.getResponsible_people_id_fk());
												filesObj.setContract_id_fk(obj.getContract_ids()[i]);
												filesObj.setStructure_id_fk(eObj);
												paramSource = new BeanPropertySqlParameterSource(filesObj);
												namedParamJdbcTemplate.update(qry1, paramSource);
											}
										}
									}
								}
						  }
					 }
				}
			}
			//transactionManager.commit(status);
			String [] permissions = obj.getPermissions_check();
			String qryU = "Update user_module set soft_delete_status = :soft_delete_status" + 
					" where executive_id_fk = :executive_id_fk and module_fk = :module_fk";	
			for(int k = 0; k <= (permissions.length - 1); k++) {
				if(permissions[k].contains("Active")) {
					String[] permissionVal = permissions[k].split("_");
					obj.setModule_fk(permissionVal[0]);
					obj.setExecutive_id_fk(userId);
					obj.setSoft_delete_status(CommonConstants.ACTIVE);
					
					paramSource = new BeanPropertySqlParameterSource(obj);		 
					namedParamJdbcTemplate.update(qryU, paramSource);
				}
			}
		}catch(Exception e){ 
			//transactionManager.rollback(status);
			throw new Exception(e);
		}
		return userId;
	}
	public String getMaxUserId(String role_code) throws Exception {
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    String user_id = null;
	    try {
	        connection = jdbcTemplate.getDataSource().getConnection();
	        if (getUserCount(role_code) > 0) {
	            // Corrected SQL query
	            String maxIdQry = "SELECT TOP 1 CONCAT('PMIS_', ?, '_', " +
	                              "RIGHT('000' + CAST(CAST(MAX(SUBSTRING(user_id, 9, LEN(user_id))) AS INT) + 1 AS VARCHAR), 3)) " +
	                              "AS maxId " +
	                              "FROM [user] WHERE user_id LIKE 'PMIS_' + ? + '_%' " +
	                              "AND ISNUMERIC(SUBSTRING(user_id, 9, LEN(user_id))) = 1 " +
	                              "GROUP BY user_id " +
	                              "ORDER BY MAX(CAST(SUBSTRING(user_id, 9, LEN(user_id)) AS INT)) DESC";

	            stmt = connection.prepareStatement(maxIdQry);
	            stmt.setString(1, role_code);  // Set the role_code for PMIS_<role_code>_ pattern
	            stmt.setString(2, role_code);  // Same for the LIKE pattern

	            rs = stmt.executeQuery();  
	            if (rs.next()) {
	                user_id = rs.getString("maxId");
	            }
	        } else {
	            user_id = "PMIS_" + role_code + "_001";  // Default value if no records exist
	        }
	    } catch (Exception e) {             
	        throw new Exception(e);
	    } finally {
	        DBConnectionHandler.closeJDBCResoucrs(connection, stmt, rs);
	    }
	    return user_id;
	}
	
	private int getUserCount(String role_code) throws Exception{
		int cnt=0;
		try {
			String qry = "SELECT count(*) FROM [user] WHERE user_id LIKE 'PMIS_"+role_code+"_%'";
			cnt = (int) jdbcTemplate.queryForObject(qry, int.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return cnt;
	}
	private void setDefaultEncryptedPassword(User user) throws Exception {
	 
	    String plainPassword = "1234";
	    String encryptedPassword = EncryptDecrypt.encrypt(plainPassword);
	    user.setPassword(encryptedPassword);
	    user.setIs_password_encrypted("true");
	}
	@Override
	public User getUser(User obj) throws Exception {
		User uobj = null;
		try {
			String qry = "select u.user_id,u.user_name,u.password,u.designation,u.email_id,u.mobile_number as mobile_number,u.personal_contact_number as personal_contact_number,u.landline as landline,u.extension as extension,u.department_fk,"
					+ "u.reporting_to_id_srfk,u.pmis_key_fk,u.user_role_name_fk,u.remarks,u.user_type_fk,u.user_image,department_name,usr.user_name as reporting_to_name "
					+ "FROM [user] u "
					+ "LEFT OUTER JOIN department d ON u.department_fk = d.department "
					+ "left outer join [user] usr ON u.reporting_to_id_srfk = usr.user_id "
					+ "where u.user_id = ? " ;
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				qry = qry + " and u.user_role_name_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and u.department_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				qry = qry + " and u.reporting_to_id_srfk = ? ";
				arrSize++;
			}
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			
			pValues[i++] = obj.getUser_id();			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				pValues[i++] = obj.getUser_role_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				pValues[i++] = obj.getReporting_to_id_srfk();
			}
			
			uobj = (User)jdbcTemplate.queryForObject( qry, pValues, new BeanPropertyRowMapper<User>(User.class));
			
			if(!StringUtils.isEmpty(uobj.getUser_id())) {
				List<User> contarctsList = null;
				String contarct_qry = "select contract_id_fk from contract_executive  where executive_user_id_fk = ?";
				int contarct_arrSize = 1;
				Object[] pValues1 = new Object[contarct_arrSize];
				pValues1[0] = uobj.getUser_id();
				contarctsList = jdbcTemplate.query( contarct_qry,pValues1, new BeanPropertyRowMapper<User>(User.class));
				uobj.setContractExecutivesList(contarctsList);
				
				List<User> executivesList = null;
				
				String executivesList_qry = "select contract_id_fk,contract_short_name from structure_contract_responsible_people  sc "
						+ "left join contract c on sc.contract_id_fk = c.contract_id where responsible_people_id_fk = ? group by contract_id_fk,contract_short_name";
				int executives_arrSize = 1;
				Object[] pValues2 = new Object[executives_arrSize];
				pValues2[0] = uobj.getUser_id();
				executivesList = jdbcTemplate.query( executivesList_qry,pValues2, new BeanPropertyRowMapper<User>(User.class));
				uobj.setExecutivesList(executivesList);
				for(User uObj : uobj.getExecutivesList()) {
					List<User> structuresList = null;
					String structure_qry = "select structure_id_fk from structure_contract_responsible_people  where responsible_people_id_fk = ? and contract_id_fk =?";
					int structure_arrSize = 2;
					Object[] pValues3 = new Object[structure_arrSize];
					pValues3[0] = uobj.getUser_id();
					pValues3[1] = uObj.getContract_id_fk();
					structuresList = jdbcTemplate.query( structure_qry,pValues3, new BeanPropertyRowMapper<User>(User.class));
					uObj.setStructureExecutivesList(structuresList);
				}
				
				List<User> riskList = null;
				String risk_qry = "select user_id FROM [user]  where designation = ?";
				int risk_arrSize = 1;
				Object[] pValues3 = new Object[risk_arrSize];
				pValues3[0] = uobj.getUser_id();
				riskList = jdbcTemplate.query( risk_qry,pValues3, new BeanPropertyRowMapper<User>(User.class));
				uobj.setRiskExecutivesList(riskList);
				
				List<User> landList = null;
				String land_qry = "select project_id_fk from land_executives  where executive_user_id_fk = ?";
				int land_arrSize = 1;
				Object[] pValues4 = new Object[land_arrSize];
				pValues4[0] = uobj.getUser_id();
				landList = jdbcTemplate.query( land_qry,pValues4, new BeanPropertyRowMapper<User>(User.class));
				uobj.setLandExecutivesList(landList);
				
				List<User> utilityList = null;
				String utility_qry = "select project_id_fk from utility_shifting_executives  where executive_user_id_fk = ?";
				int utility_arrSize = 1;
				Object[] pValues5 = new Object[utility_arrSize];
				pValues5[0] = uobj.getUser_id();
				utilityList = jdbcTemplate.query( utility_qry,pValues5, new BeanPropertyRowMapper<User>(User.class));
				uobj.setUtilityExecutivesList(utilityList);
				
				List<User> rrList = null;
				String rr_qry = "select project_id_fk from rr_executives  where executive_user_id_fk = ?";
				int r_arrSize = 1;
				Object[] pValues6 = new Object[r_arrSize];
				pValues6[0] = uobj.getUser_id();
				rrList = jdbcTemplate.query( rr_qry,pValues6, new BeanPropertyRowMapper<User>(User.class));
				uobj.setRrExecutivesList(rrList);
				
			}
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return uobj;
	}
	@Override
	public boolean updateUser(User obj) throws Exception {
		boolean flag = false;
		
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		try {			  
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());			 
			String qry = "UPDATE [user] SET user_name=:user_name,designation=:designation,email_id=:email_id,user_type_fk= :user_type_fk,mobile_number=:mobile_number,personal_contact_number=:personal_contact_number,landline=:landline,extension=:extension,department_fk=:department_fk,reporting_to_id_srfk=:reporting_to_id_srfk,pmis_key_fk=:pmis_key_fk,user_role_name_fk=:user_role_name_fk,user_image=:user_image "
					+ "WHERE user_id = :user_id";		 
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = namedParamJdbcTemplate.update(qry, paramSource);			
			if(count > 0) {
				flag = true;
				String userId = obj.getUser_id();
				String [] permissions = obj.getPermissions_check();
				
					String qryy = "Update user_module set soft_delete_status = :soft_delete_status" + 
							" where executive_id_fk = :executive_id_fk and module_fk <> 'Others' ";	
					obj.setExecutive_id_fk(userId);
					obj.setSoft_delete_status(CommonConstants.INACTIVE);
					paramSource = new BeanPropertySqlParameterSource(obj);		 
					namedParamJdbcTemplate.update(qryy, paramSource);
					if(!StringUtils.isEmpty(permissions) && permissions.length > 0) {
						String qry2 = "Update user_module set soft_delete_status = :soft_delete_status" + 
								" where executive_id_fk = :executive_id_fk and module_fk = :module_fk";	
						for(int k = 0; k <= (permissions.length - 1); k++) {
							if(permissions[k].contains("Active")) {
								String[] permissionVal = permissions[k].split("_");
								obj.setModule_fk(permissionVal[0]);
								obj.setExecutive_id_fk(userId);
								obj.setSoft_delete_status(CommonConstants.ACTIVE);
								
								paramSource = new BeanPropertySqlParameterSource(obj);		 
								namedParamJdbcTemplate.update(qry2, paramSource);
							}
							if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("Contracts")) {
								int c = 0;
								String contarctIds = obj.getContract_id();
								obj.setDepartment_id_fk(obj.getDepartment_fk());
								String qry1 = "INSERT INTO contract_executive"
										+ "(contract_id_fk,department_id_fk,executive_user_id_fk) VALUES (:contract_id_fk,:department_id_fk,:executive_user_id_fk)";	
								obj.setExecutive_user_id_fk(userId); 
								if(!StringUtils.isEmpty(contarctIds)) {
									
									String deleteFilesQry = "delete from contract_executive  where executive_user_id_fk = :executive_user_id_fk";		 
									User fileObj = new User();
									fileObj.setExecutive_user_id_fk(userId);
									paramSource = new BeanPropertySqlParameterSource(obj);	
									namedParamJdbcTemplate.update(deleteFilesQry, paramSource);
									
									if(contarctIds.contains(",")) {
										String [] values = contarctIds.split(",");
										int arrSz = values.length;
										for(int i = 0; i < arrSz; i++) {
											obj.setContract_id_fk(values[i]);
											paramSource = new BeanPropertySqlParameterSource(obj);		 
											c = namedParamJdbcTemplate.update(qry1, paramSource);
										}
									}else {
										obj.setContract_id_fk(contarctIds);
										paramSource = new BeanPropertySqlParameterSource(obj);		 
										c = namedParamJdbcTemplate.update(qry1, paramSource);
									}
								}
							}
							
							if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("Land Acquisition")) {
								int c = 0;
								String workIds = obj.getLand_work();
								String qry1 = "INSERT INTO land_executives"
										+ "(project_id_fk,executive_user_id_fk) VALUES (:project_id_fk,:executive_user_id_fk)";	
								obj.setExecutive_user_id_fk(userId); 
								if(!StringUtils.isEmpty(workIds)) {
									String deleteFilesQry = "delete from land_executives  where executive_user_id_fk = :executive_user_id_fk";		 
									User fileObj = new User();
									fileObj.setExecutive_user_id_fk(userId);
									paramSource = new BeanPropertySqlParameterSource(obj);	
									namedParamJdbcTemplate.update(deleteFilesQry, paramSource);
									
									if(workIds.contains(",")) {
										String [] values = workIds.split(",");
										int arrSz = values.length;
										for(int i = 0; i < arrSz; i++) {
											obj.setProject_id_fk(values[i]);
											paramSource = new BeanPropertySqlParameterSource(obj);		 
											c = namedParamJdbcTemplate.update(qry1, paramSource);
										}
									}else {
										obj.setProject_id_fk(workIds);
										paramSource = new BeanPropertySqlParameterSource(obj);		 
										c = namedParamJdbcTemplate.update(qry1, paramSource);
									}
								}
							}
							if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("Utility Shifting")) {
								int c = 0;
								String workIds = obj.getUs_work();
								String qry1 = "INSERT INTO utility_shifting_executives"
										+ "(project_id_fk,executive_user_id_fk) VALUES (:project_id_fk,:executive_user_id_fk)";	
								obj.setExecutive_user_id_fk(userId); 
								if(!StringUtils.isEmpty(workIds)) {
									String deleteFilesQry = "delete from utility_shifting_executives  where executive_user_id_fk = :executive_user_id_fk";		 
									User fileObj = new User();
									fileObj.setExecutive_user_id_fk(userId);
									paramSource = new BeanPropertySqlParameterSource(obj);	
									namedParamJdbcTemplate.update(deleteFilesQry, paramSource);
									if(workIds.contains(",")) {
										String [] values = workIds.split(",");
										int arrSz = values.length;
										for(int i = 0; i < arrSz; i++) {
											obj.setProject_id_fk(values[i]);
											paramSource = new BeanPropertySqlParameterSource(obj);		 
											c = namedParamJdbcTemplate.update(qry1, paramSource);
										}
									}else {
										obj.setProject_id_fk(workIds);
										paramSource = new BeanPropertySqlParameterSource(obj);		 
										c = namedParamJdbcTemplate.update(qry1, paramSource);
									}
								}
							
							 }
								if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("R & R")) {
									int c = 0;
									String workIds = obj.getRr_work();
									String qry1 = "INSERT INTO rr_executives"
											+ "(project_id_fk,executive_user_id_fk) VALUES (:project_id_fk,:executive_user_id_fk)";	
									obj.setExecutive_user_id_fk(userId); 
									if(!StringUtils.isEmpty(workIds)) {
										String deleteFilesQry = "delete from rr_executives  where executive_user_id_fk = :executive_user_id_fk";		 
										User fileObj = new User();
										fileObj.setExecutive_user_id_fk(userId);
										paramSource = new BeanPropertySqlParameterSource(obj);	
										namedParamJdbcTemplate.update(deleteFilesQry, paramSource);
										if(workIds.contains(",")) {
											String [] values = workIds.split(",");
											int arrSz = values.length;
											for(int i = 0; i < arrSz; i++) {
												obj.setProject_id_fk(values[i]);
												paramSource = new BeanPropertySqlParameterSource(obj);		 
												c = namedParamJdbcTemplate.update(qry1, paramSource);
											}
										}else {
											obj.setProject_id_fk(workIds);
											paramSource = new BeanPropertySqlParameterSource(obj);		 
											c = namedParamJdbcTemplate.update(qry1, paramSource);
										}
									
								   }
							  }
								
								if(!StringUtils.isEmpty(permissions[k]) && permissions[k].contains("Execution &  Monitoring")) {
									String qry1 = "INSERT INTO structure_contract_responsible_people"
											+ "( structure_id_fk, contract_id_fk, responsible_people_id_fk) VALUES (:structure_id_fk,:contract_id_fk,:responsible_people_id_fk)";	
									obj.setResponsible_people_id_fk(userId); 
									if(!StringUtils.isEmpty(obj.getContract_ids()) && obj.getContract_ids().length > 0) {
										String deleteFilesQry = "delete from structure_contract_responsible_people  where responsible_people_id_fk = :responsible_people_id_fk";		 
										User fileObj = new User();
										fileObj.setExecutive_user_id_fk(userId);
										paramSource = new BeanPropertySqlParameterSource(obj);	
										namedParamJdbcTemplate.update(deleteFilesQry, paramSource);
										
										int len = obj.getContract_ids().length;
										
										int size = 0;
										if(!StringUtils.isEmpty(obj.getContract_ids()) && obj.getContract_ids().length > 0) {
					    					obj.setContract_ids(CommonMethods.replaceEmptyByNullInSringArray(obj.getContract_ids()));
					    					if(size < obj.getContract_ids().length) {
					    						size = obj.getContract_ids().length;
					    					}
					    				}
										if(size == 1 ) {
								    		String joined = String.join(",", obj.getStructures());
								    		String[] strArray = new String[] {joined};
								    		obj.setStructures(strArray);
								    	}
										if(!StringUtils.isEmpty(obj.getStructures()) && obj.getStructures().length > 0) {
					    					obj.setStructures(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructures()));
					    					if(size < obj.getStructures().length) {
					    						size = obj.getStructures().length;
					    					}
					    				}
										for (int i = 0; i < size; i++){
											List<String> executives = null;
											if(!StringUtils.isEmpty(obj.getStructures()[i])){
												if(obj.getStructures()[i].contains(",")) {
													executives = new ArrayList<String>(Arrays.asList(obj.getStructures()[i].split(",")));
												}else {
													executives = new ArrayList<String>(Arrays.asList(obj.getStructures()[i]));
												}
												for(String eObj : executives) {
													if(!eObj.equals("null") && !StringUtils.isEmpty(obj.getContract_ids()[i]) &&  !StringUtils.isEmpty(eObj)) {
														User filesObj = new User();
														filesObj.setResponsible_people_id_fk(obj.getResponsible_people_id_fk());
														filesObj.setContract_id_fk(obj.getContract_ids()[i]);
														filesObj.setStructure_id_fk(eObj);
														paramSource = new BeanPropertySqlParameterSource(filesObj);
														namedParamJdbcTemplate.update(qry1, paramSource);
													}
												}
											}
										
										}
										
										
									
								   }
						  }
					 }
	
				}
	
			}
			//transactionManager.commit(status);
		}catch(Exception e){ 
			e.printStackTrace();
			//transactionManager.rollback(status);
			throw new Exception(e);
		}
		return flag;
	}
	@Override
	public boolean deleteUser(User obj) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public List<User> getUsersExportList(User obj) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select u.user_id,u.user_name,u.password,u.designation,u.email_id,u.mobile_number as mobile_number,u.personal_contact_number as personal_contact_number,u.landline as landline,u.extension as extension,u.department_fk,"
					+ "u.reporting_to_id_srfk,u.pmis_key_fk,u.user_role_name_fk,u.remarks,u.user_image,department_name,usr.user_name as reporting_to_name,usr.designation as reporting_to_designation,u.user_type_fk  "
					+ "FROM [user] u "
					+ "LEFT OUTER JOIN department d ON u.department_fk = d.department "
					+ "left outer join [user] usr ON u.reporting_to_id_srfk = usr.user_id "
					+ "left outer join user_type ut ON u.user_type_fk = ut.user_type "
					+ "where u.user_id is not null and u.user_id <> ?" ;
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				qry = qry + " and u.user_role_name_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and u.department_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				qry = qry + " and u.reporting_to_id_srfk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				qry = qry + " and u.user_type_fk = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = CommonConstants.USER;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_role_name_fk())) {
				pValues[i++] = obj.getUser_role_name_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getReporting_to_id_srfk())) {
				pValues[i++] = obj.getReporting_to_id_srfk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_type_fk();
			}
			
			//qry = qry + " order by case when (u.user_id like '%Dummy%') then 0 else 1 end desc,case when (u.user_name like '%user%')  then 0 else 1 end desc, case when(u.pmis_key_fk like '%SGS%') then 0 else 1 end desc";
			//qry = qry + "order by u.user_type_fk is null, field(u.user_type_fk,'Management','HOD','DyHOD','Officers ( Jr./Sr. Scale )','Others','Training')";;
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<User>(User.class));	
			
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<User> getReportingToUserId(String reporting_to_id_srfk) throws Exception {
		List<User> objsList = null;
		try {
			String qry = "select user_id FROM [user]  where designation = ? ";
			int arrSize = 1;
			Object[] pValues = new Object[arrSize];
			pValues[0] = reporting_to_id_srfk;
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<User>(User.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public int uploadUsers(List<User> usersList) throws Exception {
		int count = 0;
		int uploadedCount = 0;
		try {
			for (User user : usersList) {				
				
				String userId = checkUserIdExitence(user.getUser_id());
				String user_id = null;
				if(StringUtils.isEmpty(user.getUser_id())) {
					String user_role_code_qry = "select user_role_code from user_role where user_role_name = ? ";
					String user_role_code = (String)jdbcTemplate.queryForObject( user_role_code_qry,new Object[] {user.getUser_role_name_fk()}, String.class);
					user_id = getMaxUserId(user_role_code);
				}else {
					user_id = user.getUser_id();
				}
				String department_qry = "select department from department where department_name = ? ";
				String department_id = (String)jdbcTemplate.queryForObject( department_qry,new Object[] {user.getDepartment_name()}, String.class);					
				user.setDepartment_fk(department_id);
				user.setUser_id(user_id);
				
				List<User> reporting_to_list = getReportingToUserId(user.getReporting_to_id_srfk());
				if(!StringUtils.isEmpty(reporting_to_list) && reporting_to_list.size() == 1) {
					String reporting_to = reporting_to_list.get(0).getUser_id();
					user.setReporting_to_id_srfk(reporting_to);
					if(StringUtils.isEmpty(userId)) {
						NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());		 
						String qry = "INSERT INTO [user]"
								+ "(user_id,user_name,password,designation,email_id,mobile_number,personal_contact_number,landline,extension,department_fk,reporting_to_id_srfk,pmis_key_fk,user_role_name_fk,remarks) "
								+ "VALUES "
								+ "(:user_id,:user_name,:password,:designation,:email_id,:mobile_number,:personal_contact_number,:landline,:extension,:department_fk,:reporting_to_id_srfk,:pmis_key_fk,:user_role_name_fk,:remarks)";		 
						BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);		 
						count = namedParamJdbcTemplate.update(qry, paramSource);
					}else {
						NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());			 
						String qry = "UPDATE [user] set "
								+ "user_name = :user_name,designation =:designation,email_id =:email_id,mobile_number =:mobile_number,"
								+ "department_fk =:department_fk,reporting_to_id_srfk =:reporting_to_id_srfk,user_role_name_fk =:user_role_name_fk"
								+ " where user_id = :user_id";
							BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);		 
						count = namedParamJdbcTemplate.update(qry, paramSource);
					}
					uploadedCount++;
				}else {
					if(StringUtils.isEmpty(userId)) {
						NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());			 
						String qry = "INSERT INTO [user]"
								+ "(user_id,user_name,password,designation,email_id,mobile_number,personal_contact_number,landline,extension,department_fk,reporting_to_id_srfk,pmis_key_fk,user_role_name_fk,remarks) "
								+ "VALUES "
								+ "(:user_id,:user_name,:password,:designation,:email_id,:mobile_number,:personal_contact_number,:landline,:extension,:department_fk,:reporting_to_id_srfk,:pmis_key_fk,:user_role_name_fk,:remarks)";		 
						BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);		 
						count = namedParamJdbcTemplate.update(qry, paramSource);
					}else {
						NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());			 
						String qry = "UPDATE [user] set "
								+ "user_name = :user_name,designation =:designation,email_id =:email_id,mobile_number =:mobile_number,"
								+ "department_fk =:department_fk,reporting_to_id_srfk =:reporting_to_id_srfk,user_role_name_fk =:user_role_name_fk"
								+ " where user_id = :user_id";
							BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);		 
						count = namedParamJdbcTemplate.update(qry, paramSource);
					}
					uploadedCount++;
				}
			}
			
			count = usersList.size();
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return uploadedCount;
	}
	private String checkUserIdExitence(String user_id) throws Exception {
		PreparedStatement stmt = null;
		Connection con = null;
		ResultSet rs = null;
		String userId = null;
		try{
			con = jdbcTemplate.getDataSource().getConnection();
			String maxIdQry = "select user_id FROM [user] where user_id = ? ";
			stmt = con.prepareStatement(maxIdQry);
			stmt.setString(1, user_id);
			rs = stmt.executeQuery();  
			if(rs.next()) {
				userId = rs.getString("user_id");
				
			}
		}catch(Exception e){ 		
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, rs);
		}
		return userId;
	}
}
