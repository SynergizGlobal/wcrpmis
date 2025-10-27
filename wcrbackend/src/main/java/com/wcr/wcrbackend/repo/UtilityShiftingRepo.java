package com.wcr.wcrbackend.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.common.CommonConstants;
@Repository
public class UtilityShiftingRepo implements IUtilityShiftingRepo {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<UtilityShifting> getImpactedContractsListForUtilityShifting(UtilityShifting obj) throws Exception{
		List<UtilityShifting> objsList = null;
		try {
			String qry ="select distinct a.contract_id_fk,c.hod_user_id_fk,c.contract_name,c.contract_short_name,c.project_id_fk "
					+ "from p6_activities a "
					+ "left join contract c on c.contract_id = a.contract_id_fk "
					+ "left join contract_executive c1 on c1.contract_id_fk = c.contract_id "
					+ "left join utility_shifting u1 on u1.project_id_fk = c.project_id_fk  "
					+ "left join utility_shifting_executives us on u1.project_id_fk = us.project_id_fk  "					
					+ "LEFT JOIN [user] u ON c.hod_user_id_fk= u.user_id "
					+ "where a.contract_id_fk is not null ";
			
			int arrSize = 0;			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
		
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) 
			{			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c1.department_id_fk = ? and u.department_fk = ? ";
					arrSize++;
					arrSize++;
				}
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;				
			}			
			qry = qry + " order by a.contract_id_fk asc";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
				
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) 
			{			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
					pValues[i++] = obj.getDepartment_fk();
				
				}
				pValues[i++] = obj.getUser_id();
			}			
				
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getLocationListFilter(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT location_name " + "from utility_shifting i "
					+ "LEFT JOIN contract c on i.impacted_contract_id_fk = c.contract_id "
					+ "left join utility_shifting_executives us on i.project_id_fk = us.project_id_fk  "
					+ "LEFT JOIN [user] u on c.hod_user_id_fk = u.user_id "
					+ "where i.project_id_fk is not null and i.project_id_fk <> '' ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and i.project_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and impacted_contract_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ? ";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY location_name ";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityCategoryListFilter(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT utility_category_fk " + "from utility_shifting i "
					+ "LEFT JOIN contract c on i.impacted_contract_id_fk = c.contract_id "
					
					+ "left join utility_shifting_executives us on c.project_id_fk = us.project_id_fk  "
					+ "LEFT JOIN [user] u on c.hod_user_id_fk = u.user_id "
					+ "where i.project_id_fk is not null and i.project_id_fk <> '' ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and i.project_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and impacted_contract_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ? ";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY utility_category_fk ";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityTypeListFilter(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT utility_type_fk " + "from utility_shifting i "
					+ "LEFT JOIN contract c on i.impacted_contract_id_fk = c.contract_id "
					
					+ "left join utility_shifting_executives us on i.project_id_fk = us.project_id_fk  "
					+ "LEFT JOIN [user] u on c.hod_user_id_fk = u.user_id "
					+ "where i.project_id_fk is not null and i.project_id_fk <> '' ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and i.project_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and impacted_contract_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ? ";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY utility_type_fk ";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityStatusListFilter(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT shifting_status_fk " + "from utility_shifting i "
					+ "LEFT JOIN contract c on i.impacted_contract_id_fk = c.contract_id "
					
					+ "left join utility_shifting_executives us on i.project_id_fk = us.project_id_fk  "
					+ "LEFT JOIN [user] u on c.hod_user_id_fk = u.user_id "
					+ "where i.project_id_fk is not null and i.project_id_fk <> '' ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and i.project_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and impacted_contract_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				qry = qry + " and category_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				qry = qry + " and status_fk = ? ";
				arrSize++;
			}

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.department_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY shifting_status_fk ";

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getCategory_fk())) {
				pValues[i++] = obj.getCategory_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus_fk())) {
				pValues[i++] = obj.getStatus_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getHodListForUtilityShifting(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry ="SELECT u.user_id as hod_user_id_fk,u.user_name,u.designation,u.department_fk,d.contract_id_code,u.reporting_to_id_srfk "
					+ "FROM [user] u " 
					+ "left join [user] u1 on u.reporting_to_id_srfk = u1.user_id "
					+ "LEFT JOIN department d on u.department_fk = d.department "
					+ "where  u.user_type_fk = ?  ";
			
			int arrSize = 1;
			/*
			 * if(!StringUtils.isEmpty(obj) &&
			 * !StringUtils.isEmpty(obj.getDy_hod_user_id_fk()) &&
			 * !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) { qry =
			 * qry + " and u.user_id = ? "; arrSize++; }
			 */
			qry = qry + " ORDER BY case when u.designation='ED Civil' then 1 " + 
					"   when u.designation='CPM I' then 2 " + 
					"   when u.designation='CPM II' then 3" + 
					"   when u.designation='CPM III' then 4 " + 
					"   when u.designation='CPM V' then 5" + 
					"   when u.designation='CE' then 6 " + 
					"   when u.designation='ED S&T' then 7 " + 
					"   when u.designation='CSTE' then 8" + 
					"   when u.designation='GM Electrical' then 9" + 
					"   when u.designation='CEE Project I' then 10" + 
					"   when u.designation='CEE Project II' then 11" + 
					"   when u.designation='ED Finance & Planning' then 12" + 
					"   when u.designation='AGM Civil' then 13" + 
					"   when u.designation='DyCPM Civil' then 14" + 
					"   when u.designation='DyCPM III' then 15" + 
					"   when u.designation='DyCPM V' then 16" + 
					"   when u.designation='DyCE EE' then 17" + 
					"   when u.designation='DyCE Badlapur' then 18" + 
					"   when u.designation='DyCPM Pune' then 19" + 
					"   when u.designation='DyCE Proj' then 20" + 
					"   when u.designation='DyCEE I' then 21" + 
					"   when u.designation='DyCEE Projects' then 22" + 
					"   when u.designation='DyCEE PSI' then 23" + 
					"   when u.designation='DyCSTE I' then 24" + 
					"   when u.designation='DyCSTE IT' then 25" + 
					"   when u.designation='DyCSTE Projects' then 26" + 
					"   when u.designation='XEN Consultant' then 27" + 
					"   when u.designation='AEN Adhoc' then 28" + 
					"   when u.designation='AEN Project' then 29" + 
					"   when u.designation='AEN P-Way' then 30" + 
					"   when u.designation='AEN' then 31" + 
					"   when u.designation='Sr Manager Signal' then 32 " + 
					"   when u.designation='Manager Signal' then 33" + 
					"   when u.designation='Manager Civil' then 34 " + 
					"   when u.designation='Manager OHE' then 35" + 
					"   when u.designation='Manager GS' then 36" + 
					"   when u.designation='Manager Finance' then 37" + 
					"   when u.designation='Planning Manager' then 38" + 
					"   when u.designation='Manager Project' then 39" + 
					"   when u.designation='Manager' then 40 " + 
					"   when u.designation='SSE' then 41" + 
					"   when u.designation='SSE Project' then 42" + 
					"   when u.designation='SSE Works' then 43" + 
					"   when u.designation='SSE Drg' then 44" + 
					"   when u.designation='SSE BR' then 45" + 
					"   when u.designation='SSE P-Way' then 46" + 
					"   when u.designation='SSE OHE' then 47" + 
					"   when u.designation='SPE' then 48" + 
					"   when u.designation='PE' then 49" + 
					"   when u.designation='JE' then 50" + 
					"   when u.designation='Demo-HOD-Elec' then 51" + 
					"   when u.designation='Demo-HOD-Engg' then 52" + 
					"   when u.designation='Demo-HOD-S&T' then 53" + 
					"" + 
					"   end asc" ;

			//qry = qry + " ORDER BY Field(u.designation, ED Civil,CPM I,CPM II,CPM III,CPM V,CE,ED S&T,CSTE,GM Electrical,GGM Civil,CEE Project I,CEE Project II,ED Finance & Planning)";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = CommonConstants.USER_TYPE_HOD;
			/*
			 * if(!StringUtils.isEmpty(obj) &&
			 * !StringUtils.isEmpty(obj.getDy_hod_user_id_fk()) &&
			 * !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
			 * pValues[i++] = obj.getDy_hod_user_id_fk(); }
			 */
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getReqStageList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry ="select distinct a.component as requirement_stage_fk "
					+ "from p6_activities a "
					+ "left join contract c on c.contract_id = a.contract_id_fk "
					+ "left join contract_executive c1 on c1.contract_id_fk = c.contract_id "
					+ "left join utility_shifting u1 on u1.project_id_fk = c.project_id_fk  "
					+ "left join utility_shifting_executives us on u1.project_id_fk = us.project_id_fk  "					
					+ "LEFT JOIN [user] u ON c.hod_user_id_fk= u.user_id "
					+ "where a.component is not null ";
			
			int arrSize = 0;	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getImpacted_contract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) 
			{			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c1.department_id_fk = ? and u.department_fk = ? ";
					arrSize++;
					arrSize++;
				}
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;				
			}			
			qry = qry + " order by a.component asc";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getImpacted_contract_id_fk())) {
				pValues[i++] = obj.getImpacted_contract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}				
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) 
			{			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
					pValues[i++] = obj.getDepartment_fk();
				
				}
				pValues[i++] = obj.getUser_id();
			}			
				
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getImpactedElementList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry ="select distinct a.component_id as impacted_element "
					+ "from p6_activities a "
					+ "left join contract c on c.contract_id = a.contract_id_fk "
					+ "left join contract_executive c1 on c1.contract_id_fk = c.contract_id "
					+ "left join utility_shifting u1 on u1.project_id_fk = c.project_id_fk  "
					+ "left join utility_shifting_executives us on u1.project_id_fk = us.project_id_fk  "					
					+ "LEFT JOIN [user] u ON c.hod_user_id_fk= u.user_id "
					+ "where a.component_id is not null ";
			
			int arrSize = 0;	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getRequirement_stage_fk())) {
				qry = qry + " and a.component = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getImpacted_contract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + "and project_id_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) 
			{			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c1.department_id_fk = ? and u.department_fk = ? ";
					arrSize++;
					arrSize++;
				}
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;				
			}			
			qry = qry + " order by a.component_id asc";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getRequirement_stage_fk())) {
				pValues[i++] = obj.getRequirement_stage_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getImpacted_contract_id_fk())) {
				pValues[i++] = obj.getImpacted_contract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}				
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) 
			{			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
					pValues[i++] = obj.getDepartment_fk();
				
				}
				pValues[i++] = obj.getUser_id();
			}			
				
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getProjectsListForUtilityShifting(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = new ArrayList<UtilityShifting>();
		try {
			String qry = "select distinct project_id as project_id_fk,project_name "
					+ "from project p "
					+ "left join utility_shifting u on u.project_id_fk = p.project_id  "
					+ "left join utility_shifting_executives us on u.project_id_fk = us.project_id_fk  "
					+ "where project_id is not null ";
					
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + "and project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}			
			
			
			qry = qry + " order by project_id  asc";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}	
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}			
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getContractsListForUtilityShifting(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry ="select distinct c.contract_id as contract_id_fk,c.hod_user_id_fk,c.contract_name,c.contract_short_name,c.project_id_fk "
					+ "from contract c "
					+ "left join contract_executive c1 on c1.contract_id_fk = c.contract_id "
					+ "left join utility_shifting u1 on u1.project_id_fk = c.project_id_fk  "
					+ "left join utility_shifting_executives us on u1.project_id_fk = us.project_id_fk  "					
					+ "LEFT JOIN [user] u ON c.hod_user_id_fk= u.user_id "
					+ "where c.contract_id is not null ";
			
			int arrSize = 0;			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + "and project_id_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) 
			{			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c1.department_id_fk = ? and u.department_fk = ? ";
					arrSize++;
					arrSize++;
				}
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;				
			}			
			qry = qry + " order by c.contract_id asc";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}				
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) 
			{			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
					pValues[i++] = obj.getDepartment_fk();
				
				}
				pValues[i++] = obj.getUser_id();
			}			
				
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityTypeList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT utility_type as utility_type_fk FROM utility_type order by utility_type";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityCategoryList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT utility_category as utility_category_fk FROM utility_category order by utility_category";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityExecutionAgencyList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT execution_agency as execution_agency_fk FROM utility_execution_agency order by execution_agency";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getImpactedContractList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT execution_agency as execution_agency_fk FROM utility_execution_agency order by execution_agency";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getRequirementStageList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT requirement_stage as requirement_stage_fk FROM utility_requirement_stage order by requirement_stage";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUnitListForUtilityShifting(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT unit as unit_fk FROM unit order by unit";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityTypeListForUtilityShifting(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT utility_shifting_file_type FROM utility_shifting_file_type order by utility_shifting_file_type";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getStatusListForUtilityShifting(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT status as shifting_status_fk FROM utility_status order by status";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

}
