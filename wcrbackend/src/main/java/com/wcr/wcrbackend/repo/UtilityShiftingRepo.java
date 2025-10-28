package com.wcr.wcrbackend.repo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.Mail;
import com.wcr.wcrbackend.DTO.Messages;
import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.common.EMailSender;
import com.wcr.wcrbackend.common.FileUploads;
@Repository
public class UtilityShiftingRepo implements IUtilityShiftingRepo {
	public static Logger logger = Logger.getLogger(UtilityShiftingRepo.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	IFormsHistoryDao formsHistoryDao;
	
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
	@Override
	public boolean addUtilityShifting(UtilityShifting obj) throws Exception {
		boolean flag = false;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		String utility_shifting_id = null;		
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
			String USID = getAutoGeneratedUSId(obj);
			String qry = "INSERT INTO utility_shifting"
					+ "(utility_shifting_id, project_id_fk, identification, location_name, reference_number, utility_description, utility_type_fk, utility_category_fk, owner_name, "
					+ "execution_agency_fk, contract_id_fk, "
					+ "start_date, scope, completed, shifting_status_fk, shifting_completion_date, remarks, latitude,longitude, impacted_contract_id_fk, requirement_stage_fk, "
					+ "planned_completion_date, unit_fk,hod_user_id_fk,custodian,executed_by,impacted_element,affected_structures,chainage) "
					+ "VALUES "
					+ "('"+USID+"',:project_id_fk,:identification,:location_name,:reference_number,:utility_description,"
							+ ":utility_type_fk,"
							+ ":utility_category_fk,:owner_name,"
							+ ":execution_agency_fk,"
							+ ":contract_id_fk,:start_date,:scope,:completed,:shifting_status_fk,"
							+ ":shifting_completion_date,:remarks,:latitude,:longitude,"
							+ ":impacted_contract_id_fk,:requirement_stage_fk,:planned_completion_date,:unit_fk,"
							+ ":hod_user_id_fk,:custodian,:executed_by,:impacted_element,:affected_structures,:chainage"
							+ ")";	
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			KeyHolder keyHolder = new GeneratedKeyHolder();
		    int count = template.update(qry, paramSource, keyHolder);
			if(count > 0) {
				utility_shifting_id = String.valueOf(keyHolder.getKey().intValue());
				obj.setUtility_shifting_id(utility_shifting_id);
				flag = true;
				if(flag) 
				{
					if(!StringUtils.isEmpty(obj.getProgress_dates()) && obj.getProgress_dates().length > 0) {
						obj.setProgress_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_dates()));
					}
					if(!StringUtils.isEmpty(obj.getProgress_of_works()) && obj.getProgress_of_works().length > 0) {
						obj.setProgress_of_works(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_of_works()));
					}
					
					String[] progressDates = obj.getProgress_dates();
					String[] progressOfWorks = obj.getProgress_of_works();					
					
					String insertQry = "INSERT INTO utility_shifting_progress"
							+ "(progress_date, progress_of_work, utility_shifting_id)"
							+ "VALUES"
							+ "(?,?,?)";
					
					int[] counts = jdbcTemplate.batchUpdate(insertQry,
				            new BatchPreparedStatementSetter() {			                 
				                @Override
				                public void setValues(PreparedStatement ps, int i) throws SQLException {	
				                	int k = 1;
									ps.setString(k++, progressDates.length > 0 ?DateParser.parse(progressDates[i]):null);
									ps.setString(k++, progressOfWorks.length > 0 ?progressOfWorks[i]:null);
									ps.setString(k++, USID);
				                }
				                @Override  
				                public int getBatchSize() {		                	
				                	int arraySize = 0;
				    				if(!StringUtils.isEmpty(obj.getProgress_dates()) && obj.getProgress_dates().length > 0) {
				    					obj.setProgress_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_dates()));
				    					if(arraySize < obj.getProgress_dates().length) {
				    						arraySize = obj.getProgress_dates().length;
				    					}
				    				}
				    				if(!StringUtils.isEmpty(obj.getProgress_of_works()) && obj.getProgress_of_works().length > 0) {
				    					obj.setProgress_of_works(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_of_works()));
				    					if(arraySize < obj.getProgress_of_works().length) {
				    						arraySize = obj.getProgress_of_works().length;
				    					}
				    				}
				                    return arraySize;
				                }
				            });	
			
					
					
					if((!StringUtils.isEmpty(obj.getUtilityShiftingFiles()) && obj.getUtilityShiftingFiles().size() > 0) || (obj.getAttachment_file_types().length>0 && obj.getAttachmentNames().length>0)) 
					{
						
						String fileQry = "INSERT INTO utility_shifting_files (name,attachment,utility_shifting_id,utility_shifting_file_type_fk)VALUES(:name,:attachment,:utility_shifting_id,:utility_shifting_file_type)";
						
						List<MultipartFile> issueFiles = obj.getUtilityShiftingFiles();

						String[] Attachmentfiletypes = obj.getAttachment_file_types();
						String[] AttachmentNames = obj.getAttachmentNames();
						
						int m=0;
						for (MultipartFile multipartFile : issueFiles) {
							if (null != multipartFile && !multipartFile.isEmpty()){
								String saveDirectory = CommonConstants2.UTILITY_SHIFTING_FILE_SAVING_PATH;
								String fileName = multipartFile.getOriginalFilename();
								DateFormat df = new SimpleDateFormat("ddMMYY-HHmm");
								String fileName_new = "UtilityShifting-"+obj.getUtility_shifting_id() +"-"+ df.format(new Date()) +"."+ fileName.split("\\.")[1];
								FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName_new);
								
								UtilityShifting fileObj = new UtilityShifting();
								fileObj.setName(AttachmentNames[m]);
								fileObj.setAttachment(fileName_new);
								fileObj.setUtility_shifting_id(obj.getUtility_shifting_id());
								fileObj.setUtility_shifting_file_type(Attachmentfiletypes[m]);
								paramSource = new BeanPropertySqlParameterSource(fileObj);	
								template.update(fileQry, paramSource);
							}
							m++;
						}
					}
					FormHistory formHistory = new FormHistory();
					formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
					formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
					formHistory.setModule_name_fk("Utility Shifting");
					formHistory.setForm_name("Add Utility Shifting");
					formHistory.setForm_action_type("Add");
					formHistory.setForm_details("New Utility Shifting "+USID + " Created");
					formHistory.setProject_id_fk(obj.getProject_id_fk());
					formHistory.setContract_id_fk(obj.getContract_id_fk());
					
					boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
					/********************************************************************************/
					String messageQry = "INSERT INTO messages (message,user_id_fk,redirect_url,created_date,message_type)"
							+ "VALUES" + "(:message,:user_id_fk,:redirect_url,CURRENT_TIMESTAMP,:message_type)";	
					String executives=getUtilityExecutives(obj.getProject_id_fk());
					String executivesEmail=getUtilityExecutivesEmail(obj.getProject_id_fk());
					
					if(!StringUtils.isEmpty(executives)) {

						String [] SplitStr=executives.split(",");
						String [] SplitEmail=executivesEmail.split(",");
							
						for(int i=0;i<SplitStr.length;i++)
						{
							Messages msgObj = new Messages();
							msgObj.setUser_id_fk(SplitStr[i]);
							msgObj.setMessage("A new Utility Shifting against "+obj.getProject_id_fk()+" has been added");
							msgObj.setRedirect_url("/get-utility-shifting/"+USID);
							msgObj.setMessage_type("Utility Shifting");	
							BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
							template.update(messageQry, paramSource1);						
						}
						
						Messages msgObj = new Messages();
						msgObj.setUser_id_fk(obj.getHod_user_id_fk());
						msgObj.setMessage("A new Utility Shifting against "+obj.getProject_id_fk()+" has been added");
						msgObj.setRedirect_url("/get-utility-shifting/"+USID);
						msgObj.setMessage_type("Utility Shifting");	
						BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
						template.update(messageQry, paramSource1);							
					
					
					
					/*********************************************************************************************/
					String mailTo = "";
					String mailCC = "";
					for(int i=0;i<SplitEmail.length;i++)
					{
						if (!StringUtils.isEmpty(SplitEmail[i])) {
							mailTo = mailTo + SplitEmail[i] + ",";
						}
					}

					if (!StringUtils.isEmpty(mailTo)) {
						mailTo = org.apache.commons.lang3.StringUtils.chop(mailTo);
					}

					if (!StringUtils.isEmpty(mailCC)) {
						mailCC = org.apache.commons.lang3.StringUtils.chop(mailCC);
					}

					String mailBodyHeader =  "A new Utility Shifting against "+obj.getProject_id_fk()+" has been added";
					
					
					UtilityShifting sobj = null;

					String query = "SELECT distinct s.*,FORMAT(identification,'dd-MM-yyyy') as identification,FORMAT(start_date,'dd-MM-yyyy') as start_date,"
							+ "FORMAT(planned_completion_date,'dd-MM-yyyy') as planned_completion_date,FORMAT(shifting_completion_date,'dd-MM-yyyy') as shifting_completion_date,"
							+ "p.project_name,c.contract_short_name,p.project_id as project_id_fk "
							+ "from utility_shifting s "
							+ "LEFT JOIN contract c ON s.impacted_contract_id_fk  = c.contract_id "
							+ "LEFT JOIN utility_shifting_executives us on s.project_id_fk = us.project_id_fk  "
							+ "LEFT JOIN project p ON us.project_id_fk  = p.project_id "
							+ "where utility_shifting_id = ? " ;
					Object[] pValues = new Object[] { USID };
							
					sobj = (UtilityShifting)jdbcTemplate.queryForObject( query, pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));						

					sobj.setMail_body_header(mailBodyHeader);

					String emailSubject = "PMIS Utility Shifting Notification - Utility Shifting ";

					Mail mail = new Mail();
					mail.setMailTo(mailTo);
					mail.setMailCc(mailCC);
					mail.setMailBcc(CommonConstants.BCC_MAIL);
					mail.setMailSubject(emailSubject);
					mail.setTemplateName("UtilityShiftingAlert.vm");

					SimpleDateFormat monthFormat = new SimpleDateFormat("dd-MMM-YYYY");
					String today_date = monthFormat.format(new Date()).toUpperCase();

					SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");
					String current_year = yearFormat.format(new Date()).toUpperCase();

					if (!StringUtils.isEmpty(mailTo)) {
						EMailSender emailSender = new EMailSender();
						logger.error("sendEmailWithUtilityShiftingAlert() >> Sending mail to " + mailTo + ": Start ");
						logger.error("sendEmailWithUtilityShiftingAlert() >> Sending mail CC " + mailCC + ": Start ");
						emailSender.sendEmailWithUtilityShiftingAlert(mail, sobj, today_date, current_year);
						logger.error("sendEmailWithUtilityShiftingAlert() >> Sending mail to " + mailTo + ": end ");
						logger.error("sendEmailWithUtilityShiftingAlert() >> Sending mail CC " + mailCC + ": end ");
					}					
					
					
				}
			}
			}
			//transactionManager.commit(status);
		}catch(Exception e){ 
			//transactionManager.rollback(status);
			throw new Exception(e);
		}
		return flag;
	}
	private String getUtilityExecutives(String project_id) throws Exception {
		String executives="";
		try {
			String qry = "SELECT  STRING_AGG(u.user_id , ',') user_id FROM utility_shifting_executives re " + 
					"LEFT JOIN [user] u on re.executive_user_id_fk = u.user_id left join project w on re.project_id_fk = p.project_id  where project_id=? ";
			executives = (String) jdbcTemplate.queryForObject(qry, new Object[] { project_id }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return executives;
	}
	
	private String getUtilityExecutivesEmail(String project_id) throws Exception {
		String executivesEmail="";
		try {
			String qry = "SELECT  STRING_AGG(u.email_id , ',') email_id FROM utility_shifting_executives re " + 
					"LEFT JOIN [user] u on re.executive_user_id_fk = u.user_id left join project w on re.project_id_fk = p.project_id  where project_id=? ";
			executivesEmail = (String) jdbcTemplate.queryForObject(qry, new Object[] { project_id }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return executivesEmail;
	}
	
	private String getAutoGeneratedUSId(UtilityShifting obj) {
		UtilityShifting dObj = null;
		String utility_shifting_id = obj.getWork_code()+"-US-0001";
		try {
			String qry ="SELECT utility_shifting_id FROM utility_shifting where utility_shifting_id like '"+obj.getWork_code()+"-US%' " ;
			List<UtilityShifting> objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));	
			
			String qry2= "";
			if(!StringUtils.isEmpty(objsList) && objsList.size() > 0){
				qry2="select CONCAT('"+obj.getWork_code()+"','-US-',case when len(utility_shifting_id)=3 then concat('0',utility_shifting_id) when len(utility_shifting_id)=2 then concat('00',utility_shifting_id) when len(utility_shifting_id)=1 then concat('000',utility_shifting_id) end) as utility_shifting_id from(\r\n" + 
					"select Max(SUBSTRING( utility_shifting_id , LEN(utility_shifting_id) -  CHARINDEX('-',REVERSE(utility_shifting_id)) + 2  , LEN(utility_shifting_id)  ))+1 as utility_shifting_id from utility_shifting where left(utility_shifting_id,2) ='"+obj.getWork_code()+"') as a";
				dObj = (UtilityShifting)jdbcTemplate.queryForObject(qry2, new Object[] {}, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
				utility_shifting_id = dObj.getUtility_shifting_id();
			}		
		}catch(Exception e){ 
			e.printStackTrace();
		}
	    return utility_shifting_id;
	}
	@Override
	public int getTotalRecords(UtilityShifting obj, String searchParameter) throws Exception {
		int totalRecords = 0;
		try {
			String qry = "SELECT count(DISTINCT utility_shifting_id) as total_records "
					+ "from utility_shifting s "
					+ "LEFT OUTER JOIN contract c ON s.impacted_contract_id_fk  = c.contract_id "
					+ "left join utility_shifting_executives us on s.project_id_fk = us.project_id_fk  "
					+ "LEFT OUTER JOIN project p ON us.project_id_fk  = p.project_id "
					+ "where utility_shifting_id is not null " ;
			int arrSize = 0;
			
		
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLocation_name())) {
				qry = qry + " and location_name = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_category_fk())) {
				qry = qry + " and utility_category_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_type_fk())) {
				qry = qry + " and utility_type_fk = ? ";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getShifting_status_fk())) {
				qry = qry + " and shifting_status_fk=? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (utility_shifting_id like ? or utility_description like ? or utility_type_fk like ? or utility_category_fk like ? "
						+ "or owner_name like ? or execution_agency_fk like ? or shifting_status_fk like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}				

			Object[] pValues = new Object[arrSize];
			
			int i = 0;

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLocation_name())) {
				pValues[i++] = obj.getLocation_name();
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_category_fk())) {
				pValues[i++] = obj.getUtility_category_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_type_fk())) {
				pValues[i++] = obj.getUtility_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getShifting_status_fk())) {
				pValues[i++] = obj.getShifting_status_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
			if(!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
			}
			
			totalRecords = jdbcTemplate.queryForObject( qry,pValues,Integer.class);
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return totalRecords;
	}
	@Override
	public List<UtilityShifting> getUtilityShiftingList(UtilityShifting obj, int startIndex, int offset,
			String searchParameter) throws Exception{
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT distinct s.*,s.modified_by,FORMAT(s.modified_date,'dd-MM-yyyy') as modified_date,"
					+ "s.hod_user_id_fk,u.user_name,u.designation,chainage "
					+ "from utility_shifting s "
					+ "LEFT OUTER JOIN contract c ON s.impacted_contract_id_fk  = c.contract_id "
					+ "left join utility_shifting_executives us on c.project_id_fk = us.project_id_fk  "
					+ "LEFT OUTER JOIN project p ON us.project_id_fk  = p.project_id "
					+ "LEFT OUTER JOIN [user] u on s.hod_user_id_fk = u.user_id "
					+ "where utility_shifting_id is not null " ;
			
			
			
			int arrSize = 0;
		
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLocation_name())) {
				qry = qry + " and location_name = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_category_fk())) {
				qry = qry + " and utility_category_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_type_fk())) {
				qry = qry + " and utility_type_fk = ? ";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getShifting_status_fk())) {
				qry = qry + " and shifting_status_fk =? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (utility_shifting_id like ? or utility_description like ? or utility_type_fk like ? or utility_category_fk like ? "
						+ "or owner_name like ? or execution_agency_fk like ? or shifting_status_fk like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			if(!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(offset)) {
				qry = qry + " order by utility_shifting_id ASC offset ? rows  fetch next ? rows only";
				arrSize++;
				arrSize++;
			}			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLocation_name())) {
				pValues[i++] = obj.getLocation_name();
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_category_fk())) {
				pValues[i++] = obj.getUtility_category_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_type_fk())) {
				pValues[i++] = obj.getUtility_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getShifting_status_fk())) {
				pValues[i++] = obj.getShifting_status_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
			if(!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
			}
			if(!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(offset)) {
				pValues[i++] = startIndex;
				pValues[i++] = offset;
			}
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));	
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public UtilityShifting getUtilityShifting(UtilityShifting obj) throws Exception{
		UtilityShifting sobj = null;
		try {
			String qry = "SELECT distinct s.*,(select executive_user_id_fk from utility_shifting_executives re where s.project_id_fk = re.project_id_fk and executive_user_id_fk = ?) as executive_user_id_fk,FORMAT(identification,'dd-MM-yyyy') as identification,FORMAT(start_date,'dd-MM-yyyy') as start_date,"
					+ "FORMAT(planned_completion_date,'dd-MM-yyyy') as planned_completion_date,FORMAT(shifting_completion_date,'dd-MM-yyyy') as shifting_completion_date,"
					+ "p.project_name,c.contract_short_name,p.project_id as project_id_fk,"
					+ "s.hod_user_id_fk,custodian,executed_by,impacted_element,affected_structures,chainage "
					+ "from utility_shifting s "
					+ "LEFT OUTER JOIN contract c ON s.impacted_contract_id_fk  = c.contract_id "
					+ "left join utility_shifting_executives us on c.project_id_fk = us.project_id_fk  "
					+ "LEFT OUTER JOIN project p ON us.project_id_fk  = p.project_id "
					+ "where utility_shifting_id =? " ;
			
			int arrSize = 2;
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLocation_name())) {
				qry = qry + " and location_name = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_category_fk())) {
				qry = qry + " and utility_category_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_type_fk())) {
				qry = qry + " and utility_type_fk = ? ";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getShifting_status_fk())) {
				qry = qry + " and shifting_status_fk ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getUser_id();
			pValues[i++] = obj.getUtility_shifting_id();
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLocation_name())) {
				pValues[i++] = obj.getLocation_name();
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_category_fk())) {
				pValues[i++] = obj.getUtility_category_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_type_fk())) {
				pValues[i++] = obj.getUtility_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getShifting_status_fk())) {
				pValues[i++] = obj.getShifting_status_fk();
			}
			sobj = (UtilityShifting)jdbcTemplate.queryForObject( qry, pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));	
			
			if(!StringUtils.isEmpty(sobj)) {				
				String filesQry ="select id, utility_shifting_id,name, attachment,utility_shifting_file_type_fk as utility_shifting_file_type from utility_shifting_files where utility_shifting_id = ? ";					
				List<UtilityShifting> objsList = jdbcTemplate.query( filesQry,new Object[] {sobj.getId()}, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));					
				if(!StringUtils.isEmpty(objsList)) {
					sobj.setUtilityShiftingFilesList(objsList);
				}
				String filesCMQry ="select id, FORMAT(progress_date,'dd-MM-yyyy') as progress_date, progress_of_work from utility_shifting_progress where utility_shifting_id = ? ";					
				List<UtilityShifting> objsCMList = jdbcTemplate.query( filesCMQry,new Object[] {obj.getUtility_shifting_id()}, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));					
				if(!StringUtils.isEmpty(objsCMList)) {
					sobj.setUtilityShiftingProgressDetailsList(objsCMList);
				}				
			}	
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return sobj;
	}
	@Override
	public boolean updateUtilityShifting(UtilityShifting obj) throws Exception {
		boolean flag = false;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		int checkCnt=checkUtilityAnyColumnUpdate(obj);
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());			 
			String qry = "UPDATE utility_shifting SET project_id_fk=:project_id_fk, identification=:identification, location_name=:location_name,"
					+ "reference_number=:reference_number, utility_description=:utility_description, utility_type_fk=:utility_type_fk, utility_category_fk=:utility_category_fk,"
					+ " owner_name=:owner_name, execution_agency_fk=:execution_agency_fk, contract_id_fk=:contract_id_fk, start_date=:start_date, scope=:scope, completed=:completed,"
					+ " shifting_status_fk=:shifting_status_fk, shifting_completion_date=:shifting_completion_date, remarks=:remarks, latitude=:latitude,longitude=:longitude,"
					+ " impacted_contract_id_fk=:impacted_contract_id_fk, requirement_stage_fk=:requirement_stage_fk, planned_completion_date=:planned_completion_date, unit_fk=:unit_fk,"
					+ "modified_by=:created_by_user_id_fk,modified_date=CURRENT_TIMESTAMP,"
					+ "hod_user_id_fk=:hod_user_id_fk,custodian=:custodian,executed_by=:executed_by,impacted_element=:impacted_element,affected_structures=:affected_structures,chainage=:chainage "
					+ " WHERE id = :id";		 
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = template.update(qry, paramSource);			
			if(count > 0) {
				flag = true;
			}
			if(flag) {

				String deleteProgressDataQry = "delete from utility_shifting_progress where utility_shifting_id = :utility_shifting_id";
				
				UtilityShifting fileObj = new UtilityShifting();
				fileObj.setUtility_shifting_id(obj.getId());
				paramSource = new BeanPropertySqlParameterSource(obj);	
				template.update(deleteProgressDataQry, paramSource);
								
				
				if(!StringUtils.isEmpty(obj.getProgress_dates()) && obj.getProgress_dates().length > 0) {
					obj.setProgress_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_dates()));
				}
				if(!StringUtils.isEmpty(obj.getProgress_of_works()) && obj.getProgress_of_works().length > 0) {
					obj.setProgress_of_works(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_of_works()));
				}
				
				String[] progressDates = obj.getProgress_dates();
				String[] progressOfWorks = obj.getProgress_of_works();					
				
				String insertQry = "INSERT INTO utility_shifting_progress"
						+ "(progress_date, progress_of_work, utility_shifting_id)"
						+ "VALUES"
						+ "(?,?,?)";
				
				int[] counts = jdbcTemplate.batchUpdate(insertQry,
			            new BatchPreparedStatementSetter() {			                 
			                @Override
			                public void setValues(PreparedStatement ps, int i) throws SQLException {	
			                	int k = 1;
								ps.setString(k++, progressDates.length > 0 ?DateParser.parse(progressDates[i]):null);
								ps.setString(k++, progressOfWorks.length > 0 ?progressOfWorks[i]:null);
								ps.setString(k++, obj.getUtility_shifting_id());
			                }
			                @Override  
			                public int getBatchSize() {		                	
			                	int arraySize = 0;
			    				if(!StringUtils.isEmpty(obj.getProgress_dates()) && obj.getProgress_dates().length > 0) {
			    					obj.setProgress_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_dates()));
			    					if(arraySize < obj.getProgress_dates().length) {
			    						arraySize = obj.getProgress_dates().length;
			    					}
			    				}
			    				if(!StringUtils.isEmpty(obj.getProgress_of_works()) && obj.getProgress_of_works().length > 0) {
			    					obj.setProgress_of_works(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_of_works()));
			    					if(arraySize < obj.getProgress_of_works().length) {
			    						arraySize = obj.getProgress_of_works().length;
			    					}
			    				}
			                    return arraySize;
			                }
			            });	
				
				
				String deleteFilesQry = "delete from utility_shifting_files where utility_shifting_id = :id";
				
				UtilityShifting fileObj1 = new UtilityShifting();
				fileObj1.setUtility_shifting_id(obj.getId());
				paramSource = new BeanPropertySqlParameterSource(obj);	
				template.update(deleteFilesQry, paramSource);
								
				int arraySize = 0;
				if(!StringUtils.isEmpty(obj.getAttachment_file_types()) && obj.getAttachment_file_types().length > 0) {
					obj.setAttachment_file_types(CommonMethods.replaceEmptyByNullInSringArray(obj.getAttachment_file_types()));
					if(arraySize < obj.getAttachment_file_types().length) {
						arraySize = obj.getAttachment_file_types().length;
					}
				}
				if(!StringUtils.isEmpty(obj.getAttachmentNames()) && obj.getAttachmentNames().length > 0) {
					obj.setAttachmentNames(CommonMethods.replaceEmptyByNullInSringArray(obj.getAttachmentNames()));
					if(arraySize < obj.getAttachmentNames().length) {
						arraySize = obj.getAttachmentNames().length;
					}
				}
				if (!StringUtils.isEmpty(obj.getUtilityShiftingFiles()) && obj.getUtilityShiftingFiles().size() > 0) {
					if (arraySize < obj.getUtilityShiftingFiles().size()) {
						arraySize = obj.getUtilityShiftingFiles().size();
					}
				}		
				
				String fileQry = "INSERT INTO utility_shifting_files (name,attachment,utility_shifting_id,utility_shifting_file_type_fk)VALUES(:name,:attachment,:utility_shifting_id,:utility_shifting_file_type)";
				
				List<MultipartFile> usFiles = obj.getUtilityShiftingFiles();

				String[] attachmentfiletypes = obj.getAttachment_file_types();
				String[] attachmentNames = obj.getAttachmentNames();
				String[] attachmentFileNames = obj.getAttachmentFileNames();
				
				for (int i = 0; i < arraySize; i++) {
					MultipartFile multipartFile = obj.getUtilityShiftingFiles().get(i);
					if ((null != multipartFile && !multipartFile.isEmpty() && multipartFile.getSize() > 0)
							|| (!StringUtils.isEmpty(obj.getAttachmentNames()) && obj.getAttachmentNames().length > 0 
									&& !StringUtils.isEmpty(obj.getAttachmentNames()[i]) && !StringUtils.isEmpty(obj.getAttachmentNames()[i].trim()) )) {
					
							String saveDirectory = CommonConstants2.UTILITY_SHIFTING_FILE_SAVING_PATH;
							String fileName_new = attachmentFileNames.length > 0?attachmentFileNames[i]:null;
							if (null != multipartFile && !multipartFile.isEmpty()) {
								String fileName = attachmentNames[i];
								DateFormat df = new SimpleDateFormat("ddMMYY-HHmm");
								fileName_new = "UtilityShifting-"+obj.getUtility_shifting_id() +"-"+ df.format(new Date()) +"."+ fileName.split("\\.")[1];
								FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName_new);
							}
							
							UtilityShifting fileObj11 = new UtilityShifting();
							fileObj11.setName(attachmentNames[i]);
							fileObj11.setAttachment(fileName_new);
							fileObj11.setUtility_shifting_id(obj.getId());
							fileObj11.setUtility_shifting_file_type(attachmentfiletypes[i]);
							paramSource = new BeanPropertySqlParameterSource(fileObj11);	
							template.update(fileQry, paramSource);
						}
					}
					FormHistory formHistory = new FormHistory();
					formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
					formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
					formHistory.setModule_name_fk("Utility Shifting");
					formHistory.setForm_name("Update Utility Shifting");
					formHistory.setForm_action_type("Update");
					formHistory.setForm_details("Utility Shifting "+obj.getUtility_shifting_id() + " Updated");
					formHistory.setProject_id_fk(obj.getProject_id_fk());
					formHistory.setContract_id_fk(obj.getContract_id_fk());
					
					boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
					
				
					/********************************************************************************/
					
					if(checkCnt>0)
					{
					
						String messageQry = "INSERT INTO messages (message,user_id_fk,redirect_url,created_date,message_type)"
								+ "VALUES" + "(:message,:user_id_fk,:redirect_url,CURRENT_TIMESTAMP,:message_type)";	
						String executives=getUtilityExecutives(obj.getProject_id_fk());
						String [] SplitStr=executives.split(",");
							
						for(int i=0;i<SplitStr.length;i++)
						{
							Messages msgObj = new Messages();
							msgObj.setUser_id_fk(SplitStr[i]);
							msgObj.setMessage("A new Utility Shifting against "+obj.getProject_id_fk()+" has been updated");
							msgObj.setRedirect_url("/get-utility-shifting/"+obj.getUtility_shifting_id());
							msgObj.setMessage_type("Utility Shifting");	
							BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
							template.update(messageQry, paramSource1);						
						}	
						
						Messages msgObj = new Messages();
						msgObj.setUser_id_fk(obj.getHod_user_id_fk());
						msgObj.setMessage("A new Utility Shifting against "+obj.getProject_id_fk()+" has been updated");
						msgObj.setRedirect_url("/get-utility-shifting/"+obj.getUtility_shifting_id());
						msgObj.setMessage_type("Utility Shifting");	
						BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
						template.update(messageQry, paramSource1);						
					
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
	private int checkUtilityAnyColumnUpdate(UtilityShifting obj) throws Exception {
		int checkCnt=0;
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT * from utility_shifting where utility_shifting_id='"+obj.getUtility_shifting_id()+"'";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));	

			if(!StringUtils.isEmpty(objsList) && objsList.size() > 0)
			{
				UtilityShifting existingObj = objsList.get(0);
				if(!StringUtils.isEmpty(existingObj)) {
					
					if(!StringUtils.isEmpty(existingObj.getProject_id_fk()) && 
							!StringUtils.isEmpty(obj.getProject_id_fk()) && existingObj.getProject_id_fk().compareTo(obj.getProject_id_fk())!=0)
					{
						checkCnt=1;
					}
					if(!StringUtils.isEmpty(existingObj.getLocation_name()) && 
							!StringUtils.isEmpty(obj.getLocation_name()) && existingObj.getLocation_name().compareTo(obj.getLocation_name())!=0)
					{
						checkCnt=1;
					}
					if(!StringUtils.isEmpty(existingObj.getReference_number()) && 
							!StringUtils.isEmpty(obj.getReference_number()) && existingObj.getReference_number().compareTo(obj.getReference_number())!=0)
					{
						checkCnt=1;
					}
					if(!StringUtils.isEmpty(existingObj.getUtility_description()) && 
							!StringUtils.isEmpty(obj.getUtility_description()) && existingObj.getUtility_description().compareTo(obj.getUtility_description())!=0)
					{
						checkCnt=1;
					}
					if(!StringUtils.isEmpty(existingObj.getOwner_name()) && 
							!StringUtils.isEmpty(obj.getOwner_name()) && existingObj.getOwner_name().compareTo(obj.getOwner_name())!=0)
					{
						checkCnt=1;
					}
					if(!StringUtils.isEmpty(existingObj.getUtility_type_fk()) && 
							!StringUtils.isEmpty(obj.getUtility_type_fk()) && existingObj.getUtility_type_fk().compareTo(obj.getUtility_type_fk())!=0)
					{
						checkCnt=1;
					}
					if(!StringUtils.isEmpty(existingObj.getUtility_category_fk()) && 
							!StringUtils.isEmpty(obj.getUtility_category_fk()) && existingObj.getUtility_category_fk().compareTo(obj.getUtility_category_fk())!=0)
					{
						checkCnt=1;
					}
					if(!StringUtils.isEmpty(existingObj.getExecution_agency_fk()) && 
							!StringUtils.isEmpty(obj.getExecution_agency_fk()) && existingObj.getExecution_agency_fk().compareTo(obj.getExecution_agency_fk())!=0)
					{
						checkCnt=1;
					}
					if(!StringUtils.isEmpty(existingObj.getPlanned_completion_date()) && 
							!StringUtils.isEmpty(obj.getPlanned_completion_date()) && existingObj.getPlanned_completion_date().compareTo(obj.getPlanned_completion_date())!=0)
					{
						checkCnt=1;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}		
		return checkCnt;
	}
	@Override
	public List<UtilityShifting> getUtilityShiftingUploadsList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT utility_data_id, uploaded_file, us.status, us.remarks, uploaded_by_user_id_fk, FORMAT(uploaded_on,'dd-MMM-yyyy hh:mm tt') as uploaded_on "
					+ ",uploaded_on as date from utility_shifting_upload_data us " 
					+ "LEFT JOIN [user] u ON us.uploaded_by_user_id_fk = u.user_id "
					+ "where utility_data_id is not null order by utility_data_id desc ";
			
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));

		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<UtilityShifting> getUtilityShiftingList(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry = "SELECT distinct s.*,s.id, s.utility_shifting_id, s.project_id_fk,us.project_id_fk,p.project_name,c.contract_short_name,FORMAT(s.identification ,'dd-MM-yyyy') AS  identification, s.location_name, reference_number, utility_description, utility_type_fk, "
					+ "utility_category_fk, s.owner_name, execution_agency_fk, contract_id_fk,  FORMAT(s.start_date ,'dd-MM-yyyy') AS start_date, s.scope, s.completed, s.shifting_status_fk, FORMAT(shifting_completion_date ,'dd-MM-yyyy') AS shifting_completion_date, "
					+ "s.remarks, s.latitude, s.longitude, impacted_contract_id_fk, requirement_stage_fk, FORMAT(s.planned_completion_date ,'dd-MM-yyyy') AS planned_completion_date, unit_fk, s.created_by, s.created_date, s.modified_by,"
					+ " s.modified_date,custodian,executed_by,impacted_element,affected_structures,c.contract_id,c.contract_name,c.contract_short_name,us.project_id_fk,p.project_name,"
					+ "s.hod_user_id_fk,u.user_name,u.designation,chainage "
					+ " from utility_shifting s "					
					+ "LEFT OUTER JOIN contract c ON s.impacted_contract_id_fk  = c.contract_id "
					+ "LEFT OUTER JOIN project p ON s.project_id_fk = p.project_id "
					+ "LEFT OUTER JOIN utility_shifting_executives us on s.project_id_fk = us.project_id_fk "
					+ "LEFT OUTER JOIN [user] u on s.hod_user_id_fk = u.user_id "
					+ "where utility_shifting_id is not null " ;
			int arrSize = 0;
		
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and s.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLocation_name())) {
				qry = qry + " and location_name = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_category_fk())) {
				qry = qry + " and utility_category_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_type_fk())) {
				qry = qry + " and utility_type_fk = ? ";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getShifting_status_fk())) {
				qry = qry + " and shifting_status_fk =? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}	
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLocation_name())) {
				pValues[i++] = obj.getLocation_name();
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_category_fk())) {
				pValues[i++] = obj.getUtility_category_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getUtility_type_fk())) {
				pValues[i++] = obj.getUtility_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getShifting_status_fk())) {
				pValues[i++] = obj.getShifting_status_fk();
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
	public List<UtilityShifting> getRDetailsList(String utility_shifting_id) throws Exception {
		List<UtilityShifting> objsList = null;
		try {
			String qry ="select rc.id, FORMAT(progress_date ,'dd-MM-yyyy') AS progress_date, progress_of_work, r.utility_shifting_id as utility_shifting_id  "
					+ "from utility_shifting_progress rc "
					+ "LEFT JOIN utility_shifting r on rc.utility_shifting_id = r.utility_shifting_id "
					+ "WHERE rc.utility_shifting_id is not null ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(utility_shifting_id) ) {
				qry = qry + " and rc.utility_shifting_id  = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(utility_shifting_id)) {
				pValues[i++] = utility_shifting_id;
			}
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}	
}
