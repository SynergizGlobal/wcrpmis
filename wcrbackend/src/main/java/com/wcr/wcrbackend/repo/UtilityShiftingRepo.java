package com.wcr.wcrbackend.repo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.Mail;
import com.wcr.wcrbackend.DTO.Messages;
import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DBConnectionHandler;
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
			
					
					
					if((!StringUtils.isEmpty(obj.getUtilityShiftingFiles()) && obj.getUtilityShiftingFiles().size() > 0) || (obj.getAttachment_file_types().length>0 && obj.getAttachmentNames().length>0) || obj.getAttachment_file_types() != null) 
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
	    if (StringUtils.isEmpty(project_id)) return "";

	    try {
	        String qry = "SELECT STRING_AGG(CAST(u.user_id AS varchar(100)), ',') AS user_id "
	                   + "FROM utility_shifting_executives re "
	                   + "LEFT JOIN [user] u ON re.executive_user_id_fk = u.user_id "
	                   + "WHERE re.project_id_fk = ?";

	        String executives = jdbcTemplate.queryForObject(qry, new Object[]{ project_id }, String.class);
	        return (executives == null) ? "" : executives;
	    } catch (EmptyResultDataAccessException ex) {
	        return "";
	    } catch (Exception e) {
	        throw new Exception("Error fetching utility executives for project: " + project_id, e);
	    }
	}

	private String getUtilityExecutivesEmail(String project_id) throws Exception {
	    if (StringUtils.isEmpty(project_id)) return "";

	    try {
	        String qry = "SELECT STRING_AGG(u.email_id, ',') AS email_id "
	                   + "FROM utility_shifting_executives re "
	                   + "LEFT JOIN [user] u ON re.executive_user_id_fk = u.user_id "
	                   + "WHERE re.project_id_fk = ?";

	        String emails = jdbcTemplate.queryForObject(qry, new Object[]{ project_id }, String.class);
	        return (emails == null) ? "" : emails;
	    } catch (EmptyResultDataAccessException ex) {
	        return "";
	    } catch (Exception e) {
	        throw new Exception("Error fetching utility executives emails for project: " + project_id, e);
	    }
	}


	
	// returns comma-separated user_ids or empty string
//	private String getUtilityExecutives(String project_id) throws Exception {
//	    try {
//	        String qry = "SELECT STRING_AGG(u.user_id, ',') AS user_id " +
//	                     "FROM utility_shifting_executives re " +
//	                     "LEFT JOIN [user] u ON re.executive_user_id_fk = u.user_id " +
//	                     "WHERE re.project_id_fk = ?";
//
//	        try {
//	            String result = jdbcTemplate.queryForObject(qry, new Object[] { project_id }, String.class);
//	            return result == null ? "" : result;
//	        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
//	            return "";
//	        }
//	    } catch (Exception e) {
//	        throw new Exception(e);
//	    }
//	}
//
//	private String getUtilityExecutivesEmail(String project_id) throws Exception {
//	    try {
//	        String qry = "SELECT STRING_AGG(u.email_id, ',') AS email_id " +
//	                     "FROM utility_shifting_executives re " +
//	                     "LEFT JOIN [user] u ON re.executive_user_id_fk = u.user_id " +
//	                     "WHERE re.project_id_fk = ?";
//
//	        try {
//	            String result = jdbcTemplate.queryForObject(qry, new Object[] { project_id }, String.class);
//	            return result == null ? "" : result;
//	        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
//	            return "";
//	        }
//	    } catch (Exception e) {
//	        throw new Exception(e);
//	    }
//	}

	private String getAutoGeneratedUSId(UtilityShifting obj) throws Exception {

	    if (obj == null || StringUtils.isEmpty(obj.getProject_id_fk())) {
	        throw new IllegalArgumentException("project_id_fk is required to generate utility_shifting_id");
	    }

	    String projectId = obj.getProject_id_fk();
	    String prefix = projectId + "-US-";

	    try {
	        // fetch all existing IDs for the same project prefix
	        String qry = "SELECT utility_shifting_id FROM utility_shifting WHERE utility_shifting_id LIKE ?";
	        List<String> existing = jdbcTemplate.query(
	                qry,
	                new Object[]{ prefix + "%" },
	                (rs, rowNum) -> rs.getString("utility_shifting_id")
	        );

	        int max = 0;
	        for (String id : existing) {
	            if (id == null) continue;

	            // extract numeric suffix after last '-'
	            int dashIndex = id.lastIndexOf('-');
	            if (dashIndex >= 0 && dashIndex + 1 < id.length()) {
	                String suffix = id.substring(dashIndex + 1);
	                try {
	                    int val = Integer.parseInt(suffix);
	                    max = Math.max(max, val);
	                } catch (NumberFormatException ignore) {}
	            }
	        }

	        int next = max + 1;
	        String formatted = String.format("%04d", next); // 0001, 0002, ...

	        return prefix + formatted;

	    } catch (Exception e) {
	        throw new Exception("Error generating Utility Shifting ID", e);
	    }
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
			String qry = "SELECT DISTINCT " +
				    "s.*, " +
				    "(SELECT executive_user_id_fk " +
				    " FROM utility_shifting_executives re " +
				    " WHERE s.project_id_fk = re.project_id_fk " +
				    " AND executive_user_id_fk = ?) as executive_user_id_fk, " +
				    "FORMAT(identification, 'dd-MM-yyyy') as identification, " +
				    "FORMAT(start_date, 'dd-MM-yyyy') as start_date, " +
				    "FORMAT(planned_completion_date, 'dd-MM-yyyy') as planned_completion_date, " +
				    "FORMAT(shifting_completion_date, 'dd-MM-yyyy') as shifting_completion_date, " +
				    "p.project_name, " +
				    "c.contract_short_name, " +
				    "p.project_id as project_id_fk, " +
				    "s.hod_user_id_fk, " +
				    "custodian, " +
				    "executed_by, " +
				    "impacted_element, " +
				    "affected_structures, " +
				    "chainage, " +
				    "u.user_name as user_name " +
				    "FROM utility_shifting s " +
				    "LEFT OUTER JOIN contract c ON s.impacted_contract_id_fk = c.contract_id " +
				    "LEFT OUTER JOIN project p ON s.project_id_fk = p.project_id " +
				    "LEFT OUTER JOIN [USER] u ON u.user_id = s.hod_user_id_fk " +
				    "LEFT OUTER JOIN utility_shifting_executives us ON c.project_id_fk = us.project_id_fk " +
				    "WHERE utility_shifting_id = ?";
			
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

	    int checkCnt = checkUtilityAnyColumnUpdate(obj);
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
	        if (count > 0) {
	            flag = true;
	        }

	        if (flag) {

	            // ----------------- PROGRESS DETAILS -----------------
	            String deleteProgressDataQry = "delete from utility_shifting_progress where utility_shifting_id = :utility_shifting_id";

	            UtilityShifting fileObj = new UtilityShifting();
	            fileObj.setUtility_shifting_id(obj.getId());
	            paramSource = new BeanPropertySqlParameterSource(obj);
	            template.update(deleteProgressDataQry, paramSource);

	            if (!StringUtils.isEmpty(obj.getProgress_dates()) && obj.getProgress_dates().length > 0) {
	                obj.setProgress_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_dates()));
	            }
	            if (!StringUtils.isEmpty(obj.getProgress_of_works()) && obj.getProgress_of_works().length > 0) {
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
	                            ps.setString(k++, progressDates.length > 0 ? DateParser.parse(progressDates[i]) : null);
	                            ps.setString(k++, progressOfWorks.length > 0 ? progressOfWorks[i] : null);
	                            ps.setString(k++, obj.getUtility_shifting_id());
	                        }

	                        @Override
	                        public int getBatchSize() {
	                            int arraySize = 0;
	                            if (!StringUtils.isEmpty(obj.getProgress_dates()) && obj.getProgress_dates().length > 0) {
	                                obj.setProgress_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_dates()));
	                                if (arraySize < obj.getProgress_dates().length) {
	                                    arraySize = obj.getProgress_dates().length;
	                                }
	                            }
	                            if (!StringUtils.isEmpty(obj.getProgress_of_works()) && obj.getProgress_of_works().length > 0) {
	                                obj.setProgress_of_works(CommonMethods.replaceEmptyByNullInSringArray(obj.getProgress_of_works()));
	                                if (arraySize < obj.getProgress_of_works().length) {
	                                    arraySize = obj.getProgress_of_works().length;
	                                }
	                            }
	                            return arraySize;
	                        }
	                    });

	            // ----------------- FILES: DELETE OLD -----------------
	            String deleteFilesQry = "delete from utility_shifting_files where utility_shifting_id = :id";

	            UtilityShifting fileObj1 = new UtilityShifting();
	            fileObj1.setUtility_shifting_id(obj.getId());
	            paramSource = new BeanPropertySqlParameterSource(obj);
	            template.update(deleteFilesQry, paramSource);

	            // ----------------- FILES: INSERT NEW (FIXED) -----------------
	            int arraySize = 0;

	            // Normalize and get max size from attachment_file_types
	            if (!StringUtils.isEmpty(obj.getAttachment_file_types())
	                    && obj.getAttachment_file_types().length > 0) {
	                obj.setAttachment_file_types(
	                        CommonMethods.replaceEmptyByNullInSringArray(obj.getAttachment_file_types()));
	                if (arraySize < obj.getAttachment_file_types().length) {
	                    arraySize = obj.getAttachment_file_types().length;
	                }
	            }

	            // Normalize and get max size from attachmentNames
	            if (!StringUtils.isEmpty(obj.getAttachmentNames())
	                    && obj.getAttachmentNames().length > 0) {
	                obj.setAttachmentNames(
	                        CommonMethods.replaceEmptyByNullInSringArray(obj.getAttachmentNames()));
	                if (arraySize < obj.getAttachmentNames().length) {
	                    arraySize = obj.getAttachmentNames().length;
	                }
	            }

	            // Get max size from files list
	            if (!StringUtils.isEmpty(obj.getUtilityShiftingFiles())
	                    && obj.getUtilityShiftingFiles().size() > 0) {
	                if (arraySize < obj.getUtilityShiftingFiles().size()) {
	                    arraySize = obj.getUtilityShiftingFiles().size();
	                }
	            }

	            // Also consider attachmentFileNames length
	            if (!StringUtils.isEmpty(obj.getAttachmentFileNames())
	                    && obj.getAttachmentFileNames().length > 0) {
	                if (arraySize < obj.getAttachmentFileNames().length) {
	                    arraySize = obj.getAttachmentFileNames().length;
	                }
	            }

	            String fileQry = "INSERT INTO utility_shifting_files "
	                    + "(name,attachment,utility_shifting_id,utility_shifting_file_type_fk) "
	                    + "VALUES(:name,:attachment,:utility_shifting_id,:utility_shifting_file_type)";

	            List<MultipartFile> usFiles = obj.getUtilityShiftingFiles();
	            String[] attachmentfiletypes = obj.getAttachment_file_types();
	            String[] attachmentNames = obj.getAttachmentNames();
	            String[] attachmentFileNames = obj.getAttachmentFileNames();

	            for (int i = 0; i < arraySize; i++) {

	                MultipartFile multipartFile = (usFiles != null && usFiles.size() > i)
	                        ? usFiles.get(i)
	                        : null;

	                String type = (attachmentfiletypes != null && attachmentfiletypes.length > i)
	                        ? attachmentfiletypes[i]
	                        : null;

	                String name = (attachmentNames != null && attachmentNames.length > i)
	                        ? attachmentNames[i]
	                        : null;

	                String fileName_new = (attachmentFileNames != null && attachmentFileNames.length > i)
	                        ? attachmentFileNames[i]
	                        : null;

	                // Skip row if nothing meaningful is present
	                boolean hasFile = multipartFile != null && !multipartFile.isEmpty() && multipartFile.getSize() > 0;
	                boolean hasName = name != null && !name.trim().isEmpty();

	                if (!hasFile && !hasName) {
	                    continue;
	                }

	                String saveDirectory = CommonConstants2.UTILITY_SHIFTING_FILE_SAVING_PATH;

	                if (hasFile) {
	                    // if name doesn't contain extension, fallback to original file name
	                    String baseName = (name != null && name.contains("."))
	                            ? name
	                            : multipartFile.getOriginalFilename();

	                    DateFormat df = new SimpleDateFormat("ddMMYY-HHmm");
	                    // safe split
	                    String ext = "";
	                    if (baseName != null && baseName.contains(".")) {
	                        String[] parts = baseName.split("\\.");
	                        ext = parts[parts.length - 1];
	                    }
	                    fileName_new = "UtilityShifting-" + obj.getUtility_shifting_id()
	                            + "-" + df.format(new Date())
	                            + (ext.isEmpty() ? "" : "." + ext);

	                    FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName_new);
	                }

	                UtilityShifting fileObj11 = new UtilityShifting();
	                fileObj11.setName(name);
	                fileObj11.setAttachment(fileName_new);
	                fileObj11.setUtility_shifting_id(obj.getId());
	                fileObj11.setUtility_shifting_file_type(type);

	                BeanPropertySqlParameterSource paramSourceFiles =
	                        new BeanPropertySqlParameterSource(fileObj11);
	                template.update(fileQry, paramSourceFiles);
	            }

	            // ----------------- FORM HISTORY -----------------
	            FormHistory formHistory = new FormHistory();
	            formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
	            formHistory.setUser(obj.getDesignation() + " - " + obj.getUser_name());
	            formHistory.setModule_name_fk("Utility Shifting");
	            formHistory.setForm_name("Update Utility Shifting");
	            formHistory.setForm_action_type("Update");
	            formHistory.setForm_details("Utility Shifting " + obj.getUtility_shifting_id() + " Updated");
	            formHistory.setProject_id_fk(obj.getProject_id_fk());
	            formHistory.setContract_id_fk(obj.getContract_id_fk());

	            boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);

	            // ----------------- MESSAGES IF ANY COLUMN CHANGED -----------------
	            if (checkCnt > 0) {

	                String messageQry = "INSERT INTO messages (message,user_id_fk,redirect_url,created_date,message_type)"
	                        + "VALUES" + "(:message,:user_id_fk,:redirect_url,CURRENT_TIMESTAMP,:message_type)";
	                String executives = getUtilityExecutives(obj.getProject_id_fk());
	                String[] SplitStr = executives.split(",");

	                for (int i = 0; i < SplitStr.length; i++) {
	                    Messages msgObj = new Messages();
	                    msgObj.setUser_id_fk(SplitStr[i]);
	                    msgObj.setMessage("A new Utility Shifting against " + obj.getProject_id_fk() + " has been updated");
	                    msgObj.setRedirect_url("/get-utility-shifting/" + obj.getUtility_shifting_id());
	                    msgObj.setMessage_type("Utility Shifting");
	                    BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
	                    template.update(messageQry, paramSource1);
	                }

	                Messages msgObj = new Messages();
	                msgObj.setUser_id_fk(obj.getHod_user_id_fk());
	                msgObj.setMessage("A new Utility Shifting against " + obj.getProject_id_fk() + " has been updated");
	                msgObj.setRedirect_url("/get-utility-shifting/" + obj.getUtility_shifting_id());
	                msgObj.setMessage_type("Utility Shifting");
	                BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
	                template.update(messageQry, paramSource1);
	            }
	        }
	        //transactionManager.commit(status);
	    } catch (Exception e) {
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
	@Override
	public boolean saveUSDataUploadFile(UtilityShifting obj) throws Exception {
		boolean flag = false;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		String utility_data_id = null;		
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());			 
			String qry = "INSERT INTO utility_shifting_upload_data"
					+ "(uploaded_file, status, remarks, uploaded_by_user_id_fk, uploaded_on) "
					+ "VALUES "
					+ "( :uploaded_file, :status, :remarks, :uploaded_by_user_id_fk,CURRENT_TIMESTAMP)";	
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			KeyHolder keyHolder = new GeneratedKeyHolder();
		    int count = template.update(qry, paramSource, keyHolder);
			if(count > 0) {
				utility_data_id = String.valueOf(keyHolder.getKey().intValue());
				obj.setUtility_data_id(utility_data_id);
				flag = true;
				
				MultipartFile file = obj.getUtilityFile();
				if (null != file && !file.isEmpty() && file.getSize() > 0){
					String saveDirectory = CommonConstants.UTILITY_UPLOADED_FILE_SAVING_PATH ;
					String fileName = utility_data_id + "_" +file.getOriginalFilename();
					FileUploads.singleFileSaving(file, saveDirectory, fileName);
					
					obj.setUploaded_file(fileName);
					String updateQry = "UPDATE utility_shifting_upload_data set uploaded_file= :uploaded_file where utility_data_id= :utility_data_id ";
					BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(obj);		
					template.update(updateQry, paramSource1);
				}
			}
			//transactionManager.commit(status);
		}catch(Exception e){ 
			//transactionManager.rollback(status);
			throw new Exception(e);
		}
		return flag;
	}
	@Override
	public String[] uploadUtilityShiftingData(List<UtilityShifting> ussList, UtilityShifting us) throws Exception {
		boolean flag = false;
		int count = 0,row =1,sheet = 1,subRow = 1;
		int sheet1 =1;
		String errMsg = null;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
			String insertQry  = "INSERT INTO utility_shifting"
					+ "(utility_shifting_id, project_id_fk,execution_agency_fk,hod_user_id_fk, utility_type_fk, utility_description, "
					+ "location_name,custodian,identification,reference_number,chainage,executed_by,impacted_contract_id_fk,requirement_stage_fk, "
					+ "impacted_element,affected_structures,planned_completion_date,scope, completed,unit_fk,start_date,shifting_status_fk, "
					+ "shifting_completion_date, remarks,created_by,created_date,latitude,longitude) "
					+ "VALUES "
					+ "(:utility_shifting_id,:project_id_fk,:execution_agency_fk,:hod_user_id_fk,:utility_type_fk,:utility_description,"
					+ ":location_name,:custodian,:identification,:reference_number,:chainage,:executed_by,:impacted_contract_id_fk,:requirement_stage_fk,"
					+ ":impacted_element,:affected_structures,:planned_completion_date,:scope,:completed,:unit_fk,:start_date,:shifting_status_fk,"
					+ ":shifting_completion_date,:remarks,:created_by_user_id_fk,CURRENT_TIMESTAMP,:latitude,:longitude"
					+ ")";	
			
			String updatetQry = "UPDATE utility_shifting SET  project_id_fk=:project_id_fk, execution_agency_fk=:execution_agency_fk,"
					+ "hod_user_id_fk=:hod_user_id_fk, utility_type_fk=:utility_type_fk, utility_description=:utility_description, location_name=:location_name,"
					+ " custodian=:custodian, identification=:identification, reference_number=:reference_number, chainage=:chainage, executed_by=:executed_by, impacted_contract_id_fk=:impacted_contract_id_fk,"
					+ " requirement_stage_fk=:requirement_stage_fk, impacted_element=:impacted_element, affected_structures=:affected_structures, planned_completion_date=:planned_completion_date,"
					+ " scope=:scope, completed=:completed, unit_fk=:unit_fk,start_date=:start_date,shifting_status_fk=:shifting_status_fk, "
					+ " shifting_completion_date=:shifting_completion_date, remarks=:remarks,modified_by=:created_by_user_id_fk,modified_date=CURRENT_TIMESTAMP,latitude=:latitude,longitude=:longitude "
					+ " WHERE utility_shifting_id = :utility_shifting_id";	
		
			for (UtilityShifting obj : ussList) {
				
				String work_id = getWorkId(obj);
				String contract_id = getContractId(obj);
				String hod_user_id = getHodUserId(obj);
				
				obj.setProject_id_fk(work_id);
				obj.setImpacted_contract_id_fk(contract_id);
				obj.setHod_user_id_fk(hod_user_id);
				
				String work_code = getWorkCode(obj);
				obj.setWork_code(work_code);
				
				if(!StringUtils.isEmpty(obj.getChainage())) {
					double c1=Double.parseDouble(obj.getChainage());
					obj.setProject_id_fk(obj.getProject_id_fk());
					
					List<UtilityShifting> getChainageCoordinates=getRRCoordinates(obj);
					
					if(!StringUtils.isEmpty(getChainageCoordinates.get(0).getChainage()))
					{
					
						String splitChainage=getChainageCoordinates.get(0).getChainage();
						String splitChainage1=splitChainage.toString();
						String[] splitChainage2=splitChainage1.split(",");
							
						String splitLatitude=getChainageCoordinates.get(0).getLatitude();
						String splitLatitude1=splitLatitude.toString();
						String[] splitLatitude2=splitLatitude1.split(",");
		            	
						String splitLongitude=getChainageCoordinates.get(0).getLongitude();
						String splitLongitude1=splitLongitude.toString();
						String[] splitLongitude2=splitLongitude1.split(",");                    	
		            	
		            	
						String a1= splitChainage2[0];    String x1=splitLatitude2[0]; String y1=splitLongitude2[0];
						String b1=splitChainage2[1];	 String x2=splitLatitude2[1]; String y2=splitLongitude2[1];
		
						double x3=0;   double y3=0;
		
		                x3=Double.parseDouble(x2)+(((c1-Double.parseDouble(b1))/(Double.parseDouble(b1)-Double.parseDouble(a1)))*(Double.parseDouble(x2)-Double.parseDouble(x1)));
		                y3=Double.parseDouble(y2)+(((c1-Double.parseDouble(b1))/(Double.parseDouble(b1)-Double.parseDouble(a1)))*(Double.parseDouble(y2)-Double.parseDouble(y1)));				
						
						obj.setLatitude(String.valueOf(x3));
						obj.setLongitude(String.valueOf(y3));
					}
					else
					{
						
						String splitLatitude=getChainageCoordinates.get(0).getLatitude();
						String splitLongitude=getChainageCoordinates.get(0).getLongitude();
						
						obj.setLatitude(String.valueOf(splitLatitude));
						obj.setLongitude(String.valueOf(splitLongitude));					
					}
				}				
				
				
				
				
				
				String table_name = "utility_shifting";
				String utility_shifting_id = checkLAIdMethod(obj,table_name);
				row++;sheet = 1;
				if(!StringUtils.isEmpty(utility_shifting_id)) {
					obj.setUtility_shifting_id(utility_shifting_id);					
					/*if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
						if(checkWorkinUtility(obj.getProject_id_fk(),obj.getCreated_by_user_id_fk())>0){
							SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
						    count = namedParamJdbcTemplate.update(updatetQry, paramSource);						
						}
					}else{*/
						SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
					    count = namedParamJdbcTemplate.update(updatetQry, paramSource);
					//}
				}else {		
					String usId = getAutoGeneratedUSId(obj);
					obj.setUtility_shifting_id(usId);
					/*if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())){
						if(checkWorkinUtility(obj.getProject_id_fk(),obj.getCreated_by_user_id_fk())>0){
							SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
						    count = namedParamJdbcTemplate.update(insertQry, paramSource);					
						}
					}else{*/
						SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
					    count = namedParamJdbcTemplate.update(insertQry, paramSource);
					//}					

				}
				
				if(!StringUtils.isEmpty(obj.getProcessList()) && obj.getProcessList().size() > 0) {
					subRow = sheet1;
					String comInsertQry = "INSERT INTO utility_shifting_progress"
							+ "( utility_shifting_id, progress_date, progress_of_work) "
							+ "VALUES"
							+ "(:utility_shifting_id, :progress_date, :progress_of_work)";
					
					String  comUpdateQry  = "UPDATE utility_shifting_progress SET "
							+ " progress_date= :progress_date, progress_of_work= :progress_of_work Where utility_shifting_id = :utility_shifting_id";
					
					for (UtilityShifting obj1 : obj.getProcessList()) {
						String table_name1 = "utility_shifting_progress";
						String rr_id1 = checkLAIdMethod(obj1,table_name1);
						sheet = 2;subRow++;
						if(!StringUtils.isEmpty(rr_id1)) {
							obj.setUtility_shifting_id(rr_id1);
							SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj1);
						    count = namedParamJdbcTemplate.update(comUpdateQry, paramSource);
						}else {
							SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj1);
						    count = namedParamJdbcTemplate.update(comInsertQry, paramSource);
						   
						}
					}
					sheet1 = sheet1 + obj.getProcessList().size();
				}
			}
		   count = ussList.size();
		   //transactionManager.commit(status);
		}catch(Exception e){ 
			//transactionManager.rollback(status);
			//e.printStackTrace();
			errMsg = "Something went wrong. Please try with correct data ";			
		}
		String arr[] = new String[5];
		arr[0] = errMsg;
	    arr[1] = String.valueOf(count);
	    arr[2] = String.valueOf(row);
	    arr[3] = String.valueOf(sheet);
	    arr[4] = String.valueOf(subRow);
		return arr;
	}
	
	private String checkLAIdMethod(UtilityShifting obj,String table_name) throws Exception {
		String usId = null;
		String column_name = "utility_shifting_id";
		try {
			String qry ="select "+column_name+" as utility_shifting_id from "+table_name+" where "+column_name+" = ? " ;
			List<UtilityShifting> dObjList = jdbcTemplate.query(qry, new Object[] {obj.getUtility_shifting_id()}, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
			if(dObjList != null && dObjList.size() > 0) {
				usId = dObjList.get(0).getUtility_shifting_id();
				if(!StringUtils.isEmpty(obj.getProject_id_fk()) && table_name.equals("utility_shifting")){
					qry = qry + " and project_id_fk = ? ";
					dObjList = jdbcTemplate.query(qry, new Object[] {obj.getUtility_shifting_id(),obj.getProject_id_fk()}, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
					if(dObjList != null && dObjList.size() > 0) {
						usId = dObjList.get(0).getUtility_shifting_id();
					}
				}
			}
		}catch(Exception e){ 
			usId = null;
			throw new Exception(e);
		}
		return usId;
	}
	
	private String getWorkId(UtilityShifting obj)throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String val = null;
		Connection connection = null;
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			String qry = "select project_id from project where project_name = ? or project_name = ? ";
			stmt = connection.prepareStatement(qry);
			stmt.setString(1, obj.getProject_name());
			stmt.setString(2, obj.getProject_name());
			
			rs = stmt.executeQuery();  
			if(rs.next()) {
				val = rs.getString("work_id");
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, stmt, rs);
		}
		return val;
	}
	
	private String getContractId(UtilityShifting obj)throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String val = null;
		Connection connection = null;
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			String qry = "select contract_id from contract where contract_name = ? or contract_short_name = ? ";
			stmt = connection.prepareStatement(qry);
			stmt.setString(1, obj.getContract_short_name());
			stmt.setString(2, obj.getContract_short_name());
			
			rs = stmt.executeQuery();  
			if(rs.next()) {
				val = rs.getString("contract_id");
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, stmt, rs);
		}
		return val;
	}
	
	private String getHodUserId(UtilityShifting obj)throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String val = null;
		Connection connection = null;
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			String qry = "select user_id from [user] where user_type_fk = 'HOD' and designation = ? ";
			stmt = connection.prepareStatement(qry);
			stmt.setString(1, obj.getDesignation());
			
			rs = stmt.executeQuery();  
			if(rs.next()) {
				val = rs.getString("user_id");
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, stmt, rs);
		}
		return val;
	}
	
	private String getWorkCode(UtilityShifting obj)throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String val = null;
		Connection connection = null;
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			String qry = "select project_id from project where project_id = ? ";
			stmt = connection.prepareStatement(qry);
			stmt.setString(1, obj.getProject_id_fk());
			
			rs = stmt.executeQuery();  
			if(rs.next()) {
				val = rs.getString("work_code");
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, stmt, rs);
		}
		return val;
	}
	
	public List<UtilityShifting> getRRCoordinates(UtilityShifting obj) throws Exception {
		List<UtilityShifting> objList = null;
			
		if(getCoordinatesCount(obj)>0)
		{
			try {
				String qry ="select latitude,longitude from chainages_master where project_id='"+obj.getProject_id_fk()+"' and chainages='"+obj.getChainage()+"'";
				objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
				
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
		}
		else
		{
			try {
				String qry ="select string_agg(chainages,',') as chainage,string_agg(latitude,',') as latitude,string_agg(longitude,',') as longitude from chainages_master where project_id='"+obj.getProject_id_fk()+"' and id between (select min(id)-1 from chainages_master where project_id='"+obj.getProject_id_fk()+"' and chainages>=cast('"+obj.getChainage()+"' as decimal(18,2))) and (select min(id) from chainages_master where project_id='"+obj.getProject_id_fk()+"' and chainages>=cast('"+obj.getChainage()+"' as decimal(18,2)))";
				objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<UtilityShifting>(UtilityShifting.class));
				
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			return objList;				
		}
		return objList;
	}	
	
	public int getCoordinatesCount(UtilityShifting obj) throws Exception
	{
		int count=0;
		try {
			String qry = "select count(*) from chainages_master where project_id='"+obj.getProject_id_fk()+"' and chainages='"+obj.getChainage()+"'";
			count = (int) jdbcTemplate.queryForObject(qry, int.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return count;
	}	
}
