package com.wcr.wcrbackend.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
	@Override
	public String checkPMISKeyAvailability(User obj) throws Exception {
		String pmis_key = "NoKey";
		try {
			String qry = "select count(*) from pmis_key where pmis_key = ? ";
			
			int count = jdbcTemplate.queryForObject( qry,new Object[] {obj.getPmis_key_fk()}, Integer.class);	
			
			if(count > 0) {
				pmis_key = "Available";
				qry = "select count(*) FROM [user] where pmis_key_fk = ? ";				
				count = jdbcTemplate.queryForObject( qry,new Object[] {obj.getPmis_key_fk()}, Integer.class);	
				if(count > 0) {
					pmis_key = "Taken";
				}
			}
		}catch(Exception e){ 
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
			String qry = "select contract_id from contract " ;
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

}
