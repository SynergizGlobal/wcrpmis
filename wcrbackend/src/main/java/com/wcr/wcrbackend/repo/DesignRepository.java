package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import com.wcr.wcrbackend.DTO.Design;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.Messages;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DBConnectionHandler;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.common.FileUploads;
@Repository
public class DesignRepository implements IDesignRepo {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	IFormsHistoryDao formsHistoryDao;
	
	@Override
	public List<Design> getP6ActivitiesData(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct p.task_code,p6_activity_id,s.structure,p.component,component_id as element,p6_activity_name as activity,p.scope,format(finish,'dd-MMM-yy') as target_date, " + 
					"dr.actual_date,dr.remarks from p6_activities p  " + 
					"left join structure s on s.structure_id=p.structure_id_fk  " + 
					"left join (select structure,component,element,activity,scope,target_date,actual_date,remarks,task_code from designdrawingstatusremarks where 0=0 and (actual_date!='' or remarks!='')) dr " + 
					"on dr.task_code=p.task_code  " + 
					"where 0=0 and structure_type_fk = 'design' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and p.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				qry = qry + " and s.structure = ? ";
				arrSize++;
			}
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure())) {
				pValues[i++] = obj.getStructure();
			}			
			qry=qry+" order by p6_activity_id asc";
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getDesignUpdateStructures(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct s.structure from p6_activities p  " + 
					"left join structure s on s.structure_id=p.structure_id_fk  " + 
					"left join (select structure,component,element,activity,scope,target_date,actual_date,remarks from designdrawingstatusremarks where 0=0 and (actual_date!='' or remarks!='')) dr " + 
					"on dr.structure=s.structure and dr.component=p.component and component_id=dr.element and p6_activity_name=dr.activity " + 
					"and p.scope=dr.scope and format(finish,'dd-MMM-yy')=dr.target_date " + 
					"where 0=0  and structure_type_fk = 'design' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and p.contract_id_fk = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getHodListFilter(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select user_id,u.designation as hod,u.user_name from design d "
					+ " left join [user] u on d.hod = u.user_id "
					+ " where hod is not null and hod <> '' ";
				
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ?";
				arrSize++;
			}
			qry = qry + " group by user_id,u.designation,u.user_name  ORDER BY case when u.designation='ED Civil' then 1  " + 
					"   when u.designation='CPM I' then 2  " + 
					"   when u.designation='CPM II' then 3 " + 
					"   when u.designation='CPM III' then 4  " + 
					"   when u.designation='CPM V' then 5 " + 
					"   when u.designation='CE' then 6  " + 
					"   when u.designation='ED S&T' then 7  " + 
					"   when u.designation='CSTE' then 8 " + 
					"   when u.designation='GM Electrical' then 9 " + 
					"   when u.designation='CEE Project I' then 10 " + 
					"   when u.designation='CEE Project II' then 11 " + 
					"   when u.designation='ED Finance & Planning' then 12 " + 
					"   when u.designation='AGM Civil' then 13 " + 
					"   when u.designation='DyCPM Civil' then 14 " + 
					"   when u.designation='DyCPM III' then 15 " + 
					"   when u.designation='DyCPM V' then 16 " + 
					"   when u.designation='DyCE EE' then 17 " + 
					"   when u.designation='DyCE Badlapur' then 18 " + 
					"   when u.designation='DyCPM Pune' then 19 " + 
					"   when u.designation='DyCE Proj' then 20 " + 
					"   when u.designation='DyCEE I' then 21 " + 
					"   when u.designation='DyCEE Projects' then 22 " + 
					"   when u.designation='DyCEE PSI' then 23 " + 
					"   when u.designation='DyCSTE I' then 24 " + 
					"   when u.designation='DyCSTE IT' then 25 " + 
					"   when u.designation='DyCSTE Projects' then 26 " + 
					"   when u.designation='XEN Consultant' then 27 " + 
					"   when u.designation='AEN Adhoc' then 28 " + 
					"   when u.designation='AEN Project' then 29 " + 
					"   when u.designation='AEN P-Way' then 30 " + 
					"   when u.designation='AEN' then 31 " + 
					"   when u.designation='Sr Manager Signal' then 32  " + 
					"   when u.designation='Manager Signal' then 33 " + 
					"   when u.designation='Manager Civil' then 34  " + 
					"   when u.designation='Manager OHE' then 35 " + 
					"   when u.designation='Manager GS' then 36 " + 
					"   when u.designation='Manager Finance' then 37 " + 
					"   when u.designation='Planning Manager' then 38 " + 
					"   when u.designation='Manager Project' then 39 " + 
					"   when u.designation='Manager' then 40  " + 
					"   when u.designation='SSE' then 41 " + 
					"   when u.designation='SSE Project' then 42 " + 
					"   when u.designation='SSE Works' then 43 " + 
					"   when u.designation='SSE Drg' then 44 " + 
					"   when u.designation='SSE BR' then 45 " + 
					"   when u.designation='SSE P-Way' then 46 " + 
					"   when u.designation='SSE OHE' then 47 " + 
					"   when u.designation='SPE' then 48 " + 
					"   when u.designation='PE' then 49 " + 
					"   when u.designation='JE' then 50 " + 
					"   when u.designation='Demo-HOD-Elec' then 51 " + 
					"   when u.designation='Demo-HOD-Engg' then 52 " + 
					"   when u.designation='Demo-HOD-S&T' then 53 " + 
					" " + 
					"   end asc " + 
					"";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getDepartmentListFilter(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select department_id_fk,department_name from design "  
					+"LEFT OUTER JOIN department ON department_id_fk = department "
					+ " where department_id_fk is not null and department_id_fk <> '' ";
				
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ? ";
				arrSize++;
			}
			qry = qry + " group by department_id_fk,department_name";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getContractListFilter(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct d.contract_id_fk,contract_name,contract_short_name "
					+ "from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "
					+"left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+ " where d.contract_id_fk is not null and d.contract_id_fk <> '' ";
				
			int arrSize = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and d.contract_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ? ";
				arrSize++;
			}
			qry = qry + " group by d.contract_id_fk,contract_name,contract_short_name";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getStructureListFilter(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct d.structure_type_fk "
					+ "from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "
					+"left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+ " where d.contract_id_fk is not null and d.contract_id_fk <> '' ";
				
			int arrSize = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and d.contract_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ? ";
				arrSize++;
			}
			qry = qry + " group by d.structure_type_fk";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getStructureIdsListFilter(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct d.structure_id_fk "
					+ "from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "
					+"left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+ " where d.structure_type_fk is not null and d.structure_type_fk <> '' ";
				
			int arrSize = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and d.contract_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ? ";
				arrSize++;
			}
			qry = qry + " group by d.structure_id_fk";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getDrawingTypeListFilter(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select drawing_type_fk from design where drawing_type_fk is not null and drawing_type_fk <> '' ";
				
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ?";
				arrSize++;
			}
			qry = qry + " group by drawing_type_fk";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getDesigns(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select design_id,d.project_id_fk,d.structure_type_fk,d.structure_id_fk,d.approving_railway,d.approval_authority_fk,c.contract_name, " + 
					"c.contract_short_name,d.contract_id_fk,d.department_id_fk,isnull(d.consultant_contract_id_fk,'') as consultant_contract_id_fk,isnull(d.proof_consultant_contract_id_fk,'') as proof_consultant_contract_id_fk,d.hod,d.dy_hod,d.prepared_by_id_fk,d.structure_type_fk, " + 
					"d.drawing_type_fk,d.contractor_drawing_no,d.mrvc_drawing_no,d.division_drawing_no,d.hq_drawing_no,d.drawing_title,FORMAT(d.required_date,'dd-MM-yyyy') AS required_date,  " + 
					"FORMAT(d.gfc_released,'dd-MM-yyyy') AS gfc_released,d.remarks,(case when (SELECT count(dss.submitted_date) FROM design_status dss  " + 
					"where dss.submitted_date = max(ds.submitted_date)) > 1 then  (SELECT submssion_purpose FROM design_status dss where max(ds.id) = dss.id )  " + 
					"else (SELECT submssion_purpose FROM design_status dss where dss.submitted_date = max(ds.submitted_date)) end) as submission_purpose, " + 
					"(case when (SELECT count(dss.submitted_date) FROM design_status dss where dss.submitted_date = max(ds.submitted_date)) > 1 then   " + 
					"(SELECT stage_fk FROM design_status dss where max(ds.id) = dss.id  ) else (SELECT stage_fk FROM design_status dss where dss.submitted_date = max(ds.submitted_date)) end) as stage_fk, " + 
					"(case when (SELECT count(dss.submitted_date) FROM design_status dss where dss.submitted_date = max(ds.submitted_date)) > 1 then   " + 
					"(SELECT submitted_by FROM design_status dss where max(ds.id) = dss.id ) else (SELECT submitted_by FROM design_status dss where dss.submitted_date = max(ds.submitted_date)) end) as submitted_by, " + 
					"(case when (SELECT count(dss.submitted_date) FROM design_status dss where dss.submitted_date = max(ds.submitted_date)) > 1 then   " + 
					"(SELECT submitted_to FROM design_status dss where max(ds.id) = dss.id) else (SELECT submitted_to FROM design_status dss  " + 
					"where dss.submitted_date = max(ds.submitted_date)) end) as submitted_to ,FORMAT(max(ds.submitted_date) ,'dd-MM-yyyy') AS submitted_date,  " + 
					"FORMAT(required_date ,'dd-MM-yyyy') AS required_date ,u.user_name,u.designation as hod_designation,u1.user_name,u1.designation as dy_hod_designation, " + 
					"dt.department_name ,isnull(c1.contract_short_name,'') as consult_contarct, isnull(c2.contract_short_name,'') as proof_consult_contarct,component,design_seq_id,[3pvc] as threepvc   " + 
					" " + 
					"from design d  " + 
					"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id  " + 
					"LEFT OUTER JOIN contract c1 ON d.consultant_contract_id_fk = c1.contract_id  " + 
					"LEFT OUTER JOIN contract c2 ON d.proof_consultant_contract_id_fk = c2.contract_id  " + 
					"LEFT OUTER JOIN project p  ON d.project_id_fk  =  p.project_id  " + 
					"left outer join [user] u  ON d.hod  =  u.user_id  " + 
					"left outer join [user] u1  ON d.dy_hod  =  u1.user_id  " + 
					"LEFT OUTER JOIN department dt  ON d.department_id_fk  =  dt.department   " + 
					"left join design_status ds on d.design_id = ds.design_id_fk  where design_id is not null ";
				
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSearchStr())) {
				qry = qry + " and (contract_id_fk like ? or c.contract_short_name like ? or d.drawing_title like ? or d.structure_type_fk like ?"
						+ " or drawing_type_fk like ? or d.contractor_drawing_no like ? or d.mrvc_drawing_no like ? or d.division_drawing_no like ? or d.hq_drawing_no like ? or d.design_seq_id like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}
			
			qry = qry + " group by design_id,d.project_id_fk,d.structure_type_fk,d.structure_id_fk, " + 
					"d.approving_railway,d.approval_authority_fk,c.contract_name,c.contract_short_name,d.contract_id_fk,d.department_id_fk,d.consultant_contract_id_fk,d.proof_consultant_contract_id_fk,d.hod,d.dy_hod,d.prepared_by_id_fk,d.structure_type_fk, " + 
					"d.drawing_type_fk,d.contractor_drawing_no,d.mrvc_drawing_no,d.division_drawing_no,d.hq_drawing_no,d.drawing_title,FORMAT(d.required_date,'dd-MM-yyyy'),FORMAT(d.gfc_released,'dd-MM-yyyy'),d.remarks, " + 
					"u.user_name,u.designation,u1.user_name,u1.designation,dt.department_name,c1.contract_short_name,c2.contract_short_name,component,design_seq_id,[3pvc] ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSearchStr())) {		
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";	
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getDesignsList(Design obj, int startIndex, int offset, String searchParameter) throws Exception{
		List<Design> objsList = null;
		try {
			String qry ="select design_id,d.project_id_fk,d.structure_id_fk,c.contract_name,c.contract_short_name,d.contract_id_fk,d.department_id_fk,d.consultant_contract_id_fk,d.proof_consultant_contract_id_fk,d.hod,d.dy_hod," + 
					"d.prepared_by_id_fk,d.structure_type_fk,d.drawing_type_fk,d.contractor_drawing_no,d.mrvc_drawing_no,d.division_drawing_no" + 
					",d.hq_drawing_no,d.drawing_title"
					+",FORMAT(d.gfc_released,'dd-MM-yyyy') AS gfc_released,d.remarks,d.modified_by,FORMAT(d.modified_date,'dd-MM-yyyy') as modified_date,d.design_seq_id,[3pvc] as threepvc   "
					+ "from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "
					+"LEFT OUTER JOIN project p  ON d.project_id_fk  =  p.project_id "
					+ " where design_id is not null";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ?";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (contract_id_fk like ? or c.contract_short_name like ? or d.drawing_title like ? or d.structure_type_fk like ?"
						+ " or drawing_type_fk like ? or d.contractor_drawing_no like ? or d.mrvc_drawing_no like ? or d.division_drawing_no like ? or d.hq_drawing_no like ? or d.design_seq_id like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			
			if(!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(offset)) {
				qry = qry + " ORDER BY design_id ASC offset ? rows  fetch next ? rows only";
				arrSize++;
				arrSize++;
			}	
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			if(!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
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
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public int getTotalRecords(Design obj, String searchParameter) throws Exception{
		int totalRecords = 0;
		try {
			String qry ="select count(*) as total_records from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "
					+"left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+"LEFT OUTER JOIN project p  ON d.project_id_fk  =  p.project_id "
					+ " where design_id is not null";
				
			int arrSize = 0;
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ?";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (contract_id_fk like ? or c.contract_short_name like ? or d.drawing_title like ? or d.structure_type_fk like ?"
						+ " or drawing_type_fk like ? or d.contractor_drawing_no like ? or d.mrvc_drawing_no like ? or d.division_drawing_no like ? or d.hq_drawing_no like ? or d.design_seq_id like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			
			//qry = qry + " offset 0 rows  fetch next 1 rows only0";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}			
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			if(!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
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
			throw new Exception(e);
		}
		return totalRecords;
	}

	@Override
	public List<Design> getDrawingRepositoryDesignsList(Design obj, int startIndex, int offset, String searchParameter)
			throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select design_id,d.project_id_fk,d.structure_id_fk,c.contract_name,c.contract_short_name,d.contract_id_fk,d.department_id_fk,d.consultant_contract_id_fk,d.proof_consultant_contract_id_fk,d.hod,d.dy_hod," + 
					"d.prepared_by_id_fk,d.structure_type_fk,d.drawing_type_fk,d.contractor_drawing_no,d.mrvc_drawing_no,d.division_drawing_no" + 
					",d.hq_drawing_no,d.drawing_title"
					+",FORMAT(d.gfc_released,'dd-MM-yyyy') AS gfc_released,d.remarks,d.modified_by,FORMAT(d.modified_date,'dd-MM-yyyy') as modified_date,d.design_seq_id,component,(select distinct revision from design_revisions where design_id_fk=d.design_id and [current]='Yes') as revisions,(select distinct drawing_no from design_revisions where design_id_fk=d.design_id and [current]='Yes') as drawing_no,(select distinct upload_file from design_revisions where design_id_fk=d.design_id and [current]='Yes') as upload_file,(select distinct correspondence_letter_no from design_revisions where design_id_fk=d.design_id and [current]='Yes') as correspondence_letter_no,[3pvc] as threepvc   "
					+ "from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "
					+"left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+"LEFT OUTER JOIN project p  ON d.project_id_fk  =  p.project_id "
					+ " where design_id is not null";
				
			int arrSize = 0;
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}		
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_id_fk())) {
				qry = qry + " and structure_id_fk = ?";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (contract_id_fk like ? or c.contract_short_name like ? or d.drawing_title like ? or d.structure_type_fk like ?"
						+ " or drawing_type_fk like ? or d.contractor_drawing_no like ? or d.mrvc_drawing_no like ? or d.division_drawing_no like ? or d.hq_drawing_no like ? or d.design_seq_id like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			
			if(!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(offset)) {
				qry = qry + " ORDER BY design_id ASC offset ? rows  fetch next ? rows only";
				arrSize++;
				arrSize++;
			}	
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_id_fk())) {
				pValues[i++] = obj.getStructure_id_fk();
			}
			
			if(!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
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
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public int getTotalDrawingRepositoryRecords(Design obj, String searchParameter) throws Exception {
		int totalRecords = 0;
		try {
			String qry ="select count(*) as total_records from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "
					+"left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+"LEFT OUTER JOIN project p  ON d.project_id_fk  =  p.project_id "
					+ " where design_id is not null";
				
			int arrSize = 0;
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ?";
				arrSize++;
			}
//			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_id_fk())) {
//				qry = qry + " and strcuture_id_fk = ?";
//				arrSize++;
//			}
			
			if(!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (contract_id_fk like ? or c.contract_short_name like ? or d.drawing_title like ? or d.structure_type_fk like ?"
						+ " or drawing_type_fk like ? or d.contractor_drawing_no like ? or d.mrvc_drawing_no like ? or d.division_drawing_no like ? or d.hq_drawing_no like ? or d.design_seq_id like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			
			//qry = qry + " offset 0 rows  fetch next 1 rows only0";
			Object[] pValues = new Object[arrSize];
			int i = 0;

			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}
			
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
//			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_id_fk())) {
//				pValues[i++] = obj.getStructure_id_fk();
//			}
//			
			if(!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
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
			throw new Exception(e);
		}
		return totalRecords;
	}

	@Override
	public List<Design> getProjectsListForDesignForm(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "select project_id,project_name from project order by project_id asc";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getContractList() throws Exception {
		List<Design> objList = null;
		try {
			String qry ="select contract_id as contract_id_fk,contract_name from contract";
				objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<Design> getPreparedByList() throws Exception {
		List<Design> objList = null;
		try {
			String qry ="select prepared_by as prepared_by_id_fk from design_prepared_by";
				objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<Design> componentList() throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct component from p6_activities";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getApprovingRailwayList() throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "SELECT railway_id from railway where railway_id in ('WR','CR','MRVC','Others') order by case when railway_id='WR' then 1 when railway_id='CR' then 2 when railway_id='MRVC' then 3 when railway_id='Others' then 4 end asc";
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> structureList() throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select structure_type as structure_type_fk from structure_type";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> drawingTypeList() throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select drawing_type as drawing_type_fk from drawing_type";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getRevisionStatuses() throws Exception {
		List<Design> objList = null;
		try {
			String qry ="select revision_status from revision_status";
				objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<Design> getApprovalAuthority() throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "SELECT approval_authority as approval_authority_fk from approval_authority where approval_authority is not null and approval_authority <> '' ";
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getStage() throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "SELECT stage as stage_fk from stage where stage is not null and stage <> '' ";
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getSubmitted() throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "SELECT design_status_submit from design_status_submit where design_status_submit is not null and design_status_submit <> ''  ";
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getStructureId() throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "SELECT structure from structure where structure is not null and structure <> '' group by structure ";
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getSubmssionpurpose() throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "SELECT submission_purpose from design_status_submission_purpose where submission_purpose is not null and submission_purpose <> '' ";
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getDesignFileType() throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "SELECT design_file_type from design_file_type where design_file_type is not null and design_file_type <> '' ";
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getAsBuiltStatuses() throws Exception {
		List<Design> objList = null;
		try {
			String qry ="select as_built_status from as_built_status";
				objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objList;
	}

	@Override
	public Design getDesignDetails(Design obj) throws Exception {
		Design dObj = null;
		try {
			String qry ="select design_id,approving_railway,approval_authority_fk,structure_id_fk,FORMAT(d.required_date,'dd-MM-yyyy') AS required_date,d.project_id_fk,p.project_name,c.contract_short_name,d.contract_id_fk,d.department_id_fk,d.consultant_contract_id_fk,d.proof_consultant_contract_id_fk,d.hod,d.dy_hod," + 
					"d.prepared_by_id_fk,d.structure_type_fk,d.drawing_type_fk,d.contractor_drawing_no,d.mrvc_drawing_no,d.division_drawing_no" + 
					",d.hq_drawing_no,d.drawing_title"+
					",FORMAT(d.gfc_released,'dd-MM-yyyy') AS gfc_released,"
					+ "d.remarks,d.component,d.design_seq_id,[3pvc] as threepvc "
					+ "from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "+
					"LEFT OUTER JOIN project p  ON d.project_id_fk  =  p.project_id "
					+ "where design_id is not null and design_id = ?" ;
			
			dObj = (Design)jdbcTemplate.queryForObject(qry, new Object[] {obj.getDesign_id()}, new BeanPropertyRowMapper<Design>(Design.class));
			//FORMAT(consultant_submission,'dd-MM-yyyy') AS consultant_submission,
			if(!StringUtils.isEmpty(dObj)) {
				String qry2 ="select distinct revision,[current],FORMAT(revision_date,'dd-MM-yyyy') AS revision_date,remarks,revision_status_fk,drawing_no,correspondence_letter_no,upload_file from design_revisions where design_id_fk = ?";
				List<Design> objList = jdbcTemplate.query( qry2,new Object[] {obj.getDesign_id()}, new BeanPropertyRowMapper<Design>(Design.class));
				dObj.setDesignRevisions(objList);
			}
			
			if(!StringUtils.isEmpty(dObj)) {
				String qry2 ="select id, design_file_type_fk,name,design_id_fk, attachment  from design_files where design_id_fk = ?"; 
				List<Design> objList = jdbcTemplate.query( qry2,new Object[] {obj.getDesign_id()}, new BeanPropertyRowMapper<Design>(Design.class));
				dObj.setDesignFilesList(objList);
			}
			if(!StringUtils.isEmpty(dObj)) {
				String qry3 ="select id, design_id_fk, stage_fk, submitted_by, submitted_to,FORMAT(submitted_date,'dd-MM-yyyy') AS submitted_date, submssion_purpose,latest from design_status where design_id_fk = ? and (latest is null or latest = 'Yes' or latest='No') order by submitted_date DESC, id DESC ";
				List<Design> objList = jdbcTemplate.query( qry3,new Object[] {obj.getDesign_id()}, new BeanPropertyRowMapper<Design>(Design.class));
				dObj.setDesignStatusList(objList);
			}
			
		}catch(Exception e){  
			throw new Exception(e);
		}
		return dObj;
	}

	@Override
	public List<Design> getStructureTypeListFilter(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct d.structure_type_fk "
					+ "from design d "  
					+"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id "
					+"left join contractor c1 on c1.contractor_id=c.contractor_id_fk "
					+ " where d.contract_id_fk is not null and d.contract_id_fk <> '' ";
				
			int arrSize = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				qry = qry + " and contractor_name=? "; 
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and d.contract_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ? ";
				arrSize++;
			}
			qry = qry + " group by d.structure_type_fk";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			
			if("Contractor".compareTo(obj.getUser_role_code())==0 && !StringUtils.isEmpty(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_name();
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getContractsListForDesignForm(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select contract_id,contract_name,contract_short_name,project_id_fk "
					+ "from contract "
					+ "where contract_id is not null ";
			
			int arrSize = 0;			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ?";
				arrSize++;
			}
			qry = qry + " order by contract_id asc";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
				
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getComponentsforDesign(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct component from p6_activities a left join structure s on s.structure_id=a.structure_id_fk where 0=0  ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and s.structure_type_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_id_fk())) {
				qry = qry + " and s.structure = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_id_fk())) {
				pValues[i++] = obj.getStructure_id_fk();
			}
				
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getStructureIdsforDesign(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct structure_id as structure_id_fk,structure_name from structure where 0=0  ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ? ";
				arrSize++;
			}			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getStructureTypesforDesign(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct structure_type_fk from structure where 0=0 ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and project_id_fk = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getDesignUploadsList(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry = "SELECT design_data_id, uploaded_file, dd.status, dd.remarks, uploaded_by_user_id_fk, FORMAT(uploaded_on,'dd-MM-yyyy  hh:mm ss') as uploaded_on "
					+ ",uploaded_on as date from design_data dd " 
					+ "left join [user] u ON dd.uploaded_by_user_id_fk = u.user_id "
					+ "where design_data_id is not null order by FORMAT(uploaded_on,'%y-%m-%d %H : %i : %s') desc ";
			
		    objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Design>(Design.class));

		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getHodList(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="SELECT u.user_id as hod,u.user_name,u.designation "
					+ "FROM [user] u " 
					+ "left join [user] u1 on u.reporting_to_id_srfk = u1.user_id "
					+ "LEFT JOIN department d on u.department_fk = d.department "
					+ "where  u.user_type_fk = ?  ";
			
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod()) && !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
				qry = qry + " and u.user_id = ? ";
				arrSize++;
			}
			qry = qry + " ORDER BY case when u.designation='ED Civil' then 1  " + 
					"   when u.designation='CPM I' then 2  " + 
					"   when u.designation='CPM II' then 3 " + 
					"   when u.designation='CPM III' then 4  " + 
					"   when u.designation='CPM V' then 5 " + 
					"   when u.designation='CE' then 6  " + 
					"   when u.designation='ED S&T' then 7  " + 
					"   when u.designation='CSTE' then 8 " + 
					"   when u.designation='GM Electrical' then 9 " + 
					"   when u.designation='CEE Project I' then 10 " + 
					"   when u.designation='CEE Project II' then 11 " + 
					"   when u.designation='ED Finance & Planning' then 12 " + 
					"   when u.designation='AGM Civil' then 13 " + 
					"   when u.designation='DyCPM Civil' then 14 " + 
					"   when u.designation='DyCPM III' then 15 " + 
					"   when u.designation='DyCPM V' then 16 " + 
					"   when u.designation='DyCE EE' then 17 " + 
					"   when u.designation='DyCE Badlapur' then 18 " + 
					"   when u.designation='DyCPM Pune' then 19 " + 
					"   when u.designation='DyCE Proj' then 20 " + 
					"   when u.designation='DyCEE I' then 21 " + 
					"   when u.designation='DyCEE Projects' then 22 " + 
					"   when u.designation='DyCEE PSI' then 23 " + 
					"   when u.designation='DyCSTE I' then 24 " + 
					"   when u.designation='DyCSTE IT' then 25 " + 
					"   when u.designation='DyCSTE Projects' then 26 " + 
					"   when u.designation='XEN Consultant' then 27 " + 
					"   when u.designation='AEN Adhoc' then 28 " + 
					"   when u.designation='AEN Project' then 29 " + 
					"   when u.designation='AEN P-Way' then 30 " + 
					"   when u.designation='AEN' then 31 " + 
					"   when u.designation='Sr Manager Signal' then 32  " + 
					"   when u.designation='Manager Signal' then 33 " + 
					"   when u.designation='Manager Civil' then 34  " + 
					"   when u.designation='Manager OHE' then 35 " + 
					"   when u.designation='Manager GS' then 36 " + 
					"   when u.designation='Manager Finance' then 37 " + 
					"   when u.designation='Planning Manager' then 38 " + 
					"   when u.designation='Manager Project' then 39 " + 
					"   when u.designation='Manager' then 40  " + 
					"   when u.designation='SSE' then 41 " + 
					"   when u.designation='SSE Project' then 42 " + 
					"   when u.designation='SSE Works' then 43 " + 
					"   when u.designation='SSE Drg' then 44 " + 
					"   when u.designation='SSE BR' then 45 " + 
					"   when u.designation='SSE P-Way' then 46 " + 
					"   when u.designation='SSE OHE' then 47 " + 
					"   when u.designation='SPE' then 48 " + 
					"   when u.designation='PE' then 49 " + 
					"   when u.designation='JE' then 50 " + 
					"   when u.designation='Demo-HOD-Elec' then 51 " + 
					"   when u.designation='Demo-HOD-Engg' then 52 " + 
					"   when u.designation='Demo-HOD-S&T' then 53 " + 
					" " + 
					"   end asc " + 
					"" ;

			//qry = qry + " ORDER BY Field(u.designation, 'ED Civil','CPM I','CPM II','CPM III','CPM V','CE','ED S&T','CSTE','GM Electrical','GGM Civil','CEE Project I','CEE Project II','ED Finance & Planning')";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = CommonConstants.USER_TYPE_HOD;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod()) && !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
				pValues[i++] = obj.getDy_hod();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Design> getDyHodList(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="SELECT u.user_id as dy_hod,u.user_name,u.designation "
					+ "FROM [user] u " 
					+ "left join [user] u1 on u.reporting_to_id_srfk = u1.user_id " 
					+ "where u.user_type_fk = ?  ";
			
			int arrSize = 1;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod()) && !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
				qry = qry + " and u.reporting_to_id_srfk = ? ";
				arrSize++;
			}
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = CommonConstants.USER_TYPE_DYHOD;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod()) && !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
				pValues[i++] = obj.getHod();
			}
				
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	
// old generate designSeqId method with workdId
//	private String getAutoGeneratedDesignId(Design obj) {
//		Design dObj = null;
//		Design WorkCodedObj = null;
//		Design DrawTypeObj = null;
//		String designid=null;
//		try {
//			String qry ="select project_id  from project where project_id='"+obj.getProject_id_fk()+"'" ;
//			WorkCodedObj = (Design)jdbcTemplate.queryForObject(qry, new BeanPropertyRowMapper<Design>(Design.class));
//			
//			String qry1 ="select drawing_type_code  from drawing_type where drawing_type='"+obj.getDrawing_type_fk()+"'" ;
//			DrawTypeObj = (Design)jdbcTemplate.queryForObject(qry1, new BeanPropertyRowMapper<Design>(Design.class));			
//			
//			
//			String input = obj.getStructure_type_fk()+"-"+obj.getStructure_id_fk()+"-"+obj.getComponent()+"-"+DrawTypeObj.getDrawing_type_code();
//			String lastFourDigits = input;   
//	
//
//
//				String qry2="select CONCAT('"+WorkCodedObj.getWork_code()+"','-"+lastFourDigits+"-',case when len(design_seq_id)=3 then concat('0',design_seq_id) when len(design_seq_id)=2 then concat('00',design_seq_id) when len(design_seq_id)=1 then concat('000',design_seq_id) end) as design_id from(" + 
//						"select (case when (select count(*) from design where left(design_seq_id,2) ='"+WorkCodedObj.getWork_code()+"')>0 then Max(SUBSTRING( design_seq_id , LEN(design_seq_id) -  CHARINDEX('-',REVERSE(design_seq_id)) + 2  , LEN(design_seq_id)  ))+1 else 1 end )as design_seq_id from design where left(design_seq_id,2) ='"+WorkCodedObj.getWork_code()+"') as a";
//						dObj = (Design)jdbcTemplate.queryForObject(qry2, new Object[] {}, new BeanPropertyRowMapper<Design>(Design.class));
//						designid = dObj.getDesign_id();
//			
//		}catch(Exception e){ 
//			e.printStackTrace();
//		}
//	    return designid;
//	}
	
	// new generateDesignId without workId 
	private String getAutoGeneratedDesignId(Design obj) {
	    Design dObj = null;
	    Design DrawTypeObj = null;
	    String designid = null;

	    try {
	        // Fetch drawing type code
	        String qry1 = "select drawing_type_code from drawing_type where drawing_type = ?";
	        DrawTypeObj = jdbcTemplate.queryForObject(
	                qry1,
	                new Object[]{obj.getDrawing_type_fk()},
	                new BeanPropertyRowMapper<>(Design.class)
	        );

	        // Build middle part
	        String lastFourDigits = obj.getStructure_type_fk() + "-" +
	                                obj.getStructure_id_fk() + "-" +
	                                obj.getComponent() + "-" +
	                                DrawTypeObj.getDrawing_type_code();

	        // Query to check previous IDs and generate next
	        String qry2 =
	            "SELECT CONCAT('" + lastFourDigits + "-', " +
	            "       RIGHT('0000' + CAST(design_seq_id AS VARCHAR(4)), 4)) AS design_id " +
	            "FROM (" +
	            "   SELECT CASE " +
	            "       WHEN EXISTS (SELECT 1 FROM design WHERE design_seq_id LIKE '" + lastFourDigits + "-%') THEN " +
	            "           (SELECT MAX(CAST(RIGHT(design_seq_id, 4) AS INT)) " +
	            "            FROM design WHERE design_seq_id LIKE '" + lastFourDigits + "-%') + 1 " +
	            "       ELSE 1 " +
	            "   END AS design_seq_id" +
	            ") AS a";

	        dObj = jdbcTemplate.queryForObject(
	                qry2,
	                new Object[]{},
	                new BeanPropertyRowMapper<>(Design.class)
	        );

	        designid = dObj.getDesign_id();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return designid;
	}

	@Override
	public String addDesign(Design obj) throws Exception {
		boolean flag = false;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		String design_seq_id = null;
		String designId = null;
		
		try{
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());	
			
			design_seq_id = getAutoGeneratedDesignId(obj);
			obj.setDesign_seq_id(design_seq_id);
			
			
			String qry = "INSERT INTO design (project_id_fk,contract_id_fk,department_id_fk,hod,dy_hod,prepared_by_id_fk,consultant_contract_id_fk,proof_consultant_contract_id_fk,"
					+ "structure_type_fk,drawing_type_fk,contractor_drawing_no,mrvc_drawing_no,division_drawing_no,hq_drawing_no,drawing_title,"
					+ "gfc_released,remarks," + 
					"approving_railway,approval_authority_fk,structure_id_fk,required_date,component,design_seq_id,[3pvc],created_by,created_date) "
					+ "VALUES(:project_id_fk,:contract_id_fk,:department_id_fk,:hod,:dy_hod,:prepared_by_id_fk,:consultant_contract_id_fk,:proof_consultant_contract_id_fk,:structure_type_fk"
					+ ",:drawing_type_fk,:contractor_drawing_no,:mrvc_drawing_no,:division_drawing_no,:hq_drawing_no,:drawing_title,"
					+ ":gfc_released,:remarks"
					+ ",:approving_railway,:approval_authority_fk,:structure_id_fk,:required_date,:component,:design_seq_id,:threepvc,:created_by_user_id_fk,CURRENT_TIMESTAMP)";
			
			org.springframework.jdbc.core.namedparam.SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
			org.springframework.jdbc.support.KeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
		    int count = namedParamJdbcTemplate.update(qry, paramSource, keyHolder);
		    //return keyHolder.getKey().intValue();
		   
		    designId = String.valueOf(keyHolder.getKey().intValue());
			 obj.setDesign_id(designId);
			//BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			//int count = namedParamJdbcTemplate.update(qry, paramSource);		
		  
			/*			if(count > 0) {
							 designId = String.valueOf(keyHolder.getKey().intValue());
							 obj.setDesign_id(designId);
							 flag = true;
							 int  docArrSize = 0;
							 if (!StringUtils.isEmpty(obj.getDesignDocumentFileNames()) && obj.getDesignDocumentFileNames().length > 0) {
									obj.setDesignDocumentFileNames(CommonMethods.replaceEmptyByNullInSringArray(obj.getDesignDocumentFileNames()));
									if (docArrSize < obj.getDesignDocumentFileNames().length) {
										docArrSize = obj.getDesignDocumentFileNames().length;
									}
								}
								if (!StringUtils.isEmpty(obj.getDesign_file_typess()) && obj.getDesign_file_typess().length > 0) {
									obj.setDesign_file_typess(CommonMethods.replaceEmptyByNullInSringArray(obj.getDesign_file_typess()));
									if (docArrSize < obj.getDesign_file_typess().length) {
										docArrSize = obj.getDesign_file_typess().length;
									}
								}
								if (!StringUtils.isEmpty(obj.getDesignDocumentNames()) && obj.getDesignDocumentNames().length > 0) {
									obj.setDesignDocumentNames(CommonMethods.replaceEmptyByNullInSringArray(obj.getDesignDocumentNames()));
									if (docArrSize < obj.getDesignDocumentNames().length) {
										docArrSize = obj.getDesignDocumentNames().length;
									}
								}
								for (int i = 0; i < docArrSize; i++) {
								   if(!StringUtils.isEmpty(obj.getDesignDocumentFiles()) && obj.getDesignDocumentFiles().length > 0) {
										
										String file_insert_qry = "INSERT into  design_files ( design_id_fk,design_file_type_fk,name, attachment) VALUES (:design_id,:design_file_type_fk,:name,:attachment)";
										MultipartFile designFiles = obj.getDesignDocumentFiles()[i];
										if ((null != designFiles && !designFiles.isEmpty()) && !StringUtils.isEmpty(obj.getDesign_file_typess()[i]) && !StringUtils.isEmpty(obj.getDesignDocumentNames()[i]))  {
											if (null != designFiles && !designFiles.isEmpty()){
												String saveDirectory = CommonConstants2.DESIGN_FILE_SAVING_PATH ;
												String fileName = designFiles.getOriginalFilename();
												DateFormat df = new SimpleDateFormat("ddMMYY-HHmm"); 
												String fileName_new = "Design-"+obj.getDesign_id() +"-"+ df.format(new Date()) +"."+ fileName.split("\\.")[1];
												FileUploads.singleFileSaving(designFiles, saveDirectory, fileName_new);
												String design_file_type_fk = obj.getDesign_file_typess()[i];
												String name = obj.getDesignDocumentNames()[i];
												Design fileObj = new Design();
												fileObj.setDesign_id(obj.getDesign_id());
												fileObj.setDesign_file_types(design_file_type_fk);
												fileObj.setName(name);
												fileObj.setAttachment(fileName_new);
												//fileObj.setStatus(CommonConstants.ACTIVE);
												
												paramSource = new BeanPropertySqlParameterSource(fileObj);	
												namedParamJdbcTemplate.update(file_insert_qry, paramSource);
											}
										}
									}	
								}
						}*/
			
		    if(count > 0) {
		    	 flag = true;
				/*			
							String qryDesignRevision = "INSERT INTO design_revisions (design_id_fk,revision,consultant_submission,mrvc_reviewed,divisional_approval,"
									+ "hq_approval,revision_status_fk,remarks) VALUES(?,?,?,?,?,?,?,?)";
							
							int[] counts = jdbcTemplate.batchUpdate(qryDesignRevision,
				        new BatchPreparedStatementSetter() {
										@Override
										public void setValues(PreparedStatement ps, int i) throws SQLException {
											try {
												int k = 1;
												ps.setString(k++, obj.getDesign_id());
												ps.setString(k++,(obj.getRevisions().length > 0)?obj.getRevisions()[i]:null);									
												ps.setString(k++,DateParser.parse((obj.getConsultant_submissions().length > 0)?obj.getConsultant_submissions()[i]:null));
												ps.setString(k++,DateParser.parse((obj.getMrvc_revieweds().length > 0)?obj.getMrvc_revieweds()[i]:null));
												ps.setString(k++,DateParser.parse((obj.getDivisional_approvals().length > 0)?obj.getDivisional_approvals()[i]:null));
												ps.setString(k++,DateParser.parse((obj.getHq_approvals().length > 0)?obj.getHq_approvals()[i]:null));
												ps.setString(k++,(obj.getRevision_status_fks().length > 0)?obj.getRevision_status_fks()[i]:null);
												ps.setString(k++,(obj.getRemarkss().length > 0)?obj.getRemarkss()[i]:null);
											
											} catch (Exception e) {
												
											}
										}
										@Override
										public int getBatchSize() {
											int arraySize = 0;
											if(!StringUtils.isEmpty(obj.getRevisions()) && obj.getRevisions().length > 0) {
												obj.setRevisions(CommonMethods.replaceEmptyByNullInSringArray(obj.getRevisions()));
												if(arraySize < obj.getRevisions().length) {
													arraySize = obj.getRevisions().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getConsultant_submissions()) && obj.getConsultant_submissions().length > 0) {
												obj.setConsultant_submissions(CommonMethods.replaceEmptyByNullInSringArray(obj.getConsultant_submissions()));
												if(arraySize < obj.getConsultant_submissions().length) {
													arraySize = obj.getConsultant_submissions().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getMrvc_revieweds()) && obj.getMrvc_revieweds().length > 0) {
												obj.setMrvc_revieweds(CommonMethods.replaceEmptyByNullInSringArray(obj.getMrvc_revieweds()));
												if(arraySize < obj.getMrvc_revieweds().length) {
													arraySize = obj.getMrvc_revieweds().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getDivisional_approvals()) && obj.getDivisional_approvals().length > 0) {
												obj.setDivisional_approvals(CommonMethods.replaceEmptyByNullInSringArray(obj.getDivisional_approvals()));
												if(arraySize < obj.getDivisional_approvals().length) {
													arraySize = obj.getDivisional_approvals().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getHq_approvals()) && obj.getHq_approvals().length > 0) {
												obj.setHq_approvals(CommonMethods.replaceEmptyByNullInSringArray(obj.getHq_approvals()));
												if(arraySize < obj.getHq_approvals().length) {
													arraySize = obj.getHq_approvals().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getRevision_status_fks()) && obj.getRevision_status_fks().length > 0) {
												obj.setRevision_status_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getRevision_status_fks()));
												if(arraySize < obj.getRevision_status_fks().length) {
													arraySize = obj.getRevision_status_fks().length;
												}
											}			
											if(!StringUtils.isEmpty(obj.getRemarkss()) && obj.getRemarkss().length > 0) {
												obj.setRemarkss(CommonMethods.replaceEmptyByNullInSringArray(obj.getRemarkss()));
												if(arraySize < obj.getRemarkss().length) {
													arraySize = obj.getRemarkss().length;
												}
											}
											return arraySize;
									}
							  });
							String qryDesignStatus = "INSERT INTO design_status (design_id_fk,stage_fk,submitted_by,submitted_to,submitted_date,"
									+ "submssion_purpose) VALUES(?,?,?,?,?,?)";
							
							int[] statusCounts = jdbcTemplate.batchUpdate(qryDesignStatus,
				        new BatchPreparedStatementSetter() {
										@Override
										public void setValues(PreparedStatement ps, int i) throws SQLException {
											try {
												int k = 1;
												ps.setString(k++, obj.getDesign_id());
												ps.setString(k++,(obj.getStage_fks().length > 0)?obj.getStage_fks()[i]:null);	
												ps.setString(k++,(obj.getSubmitted_bys().length > 0)?obj.getSubmitted_bys()[i]:null);	
												ps.setString(k++,(obj.getSubmitted_tos().length > 0)?obj.getSubmitted_tos()[i]:null);	
												ps.setString(k++,DateParser.parse((obj.getSubmitted_dates().length > 0)?obj.getSubmitted_dates()[i]:null));
												ps.setString(k++,(obj.getSubmssion_purposes().length > 0)?obj.getSubmssion_purposes()[i]:null);
											
											} catch (Exception e) {
												
											}
										}
										@Override
										public int getBatchSize() {
											int arraySize = 0;
											if(!StringUtils.isEmpty(obj.getStage_fks()) && obj.getStage_fks().length > 0) {
												obj.setStage_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getStage_fks()));
												if(arraySize < obj.getStage_fks().length) {
													arraySize = obj.getStage_fks().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getSubmitted_bys()) && obj.getSubmitted_bys().length > 0) {
												obj.setSubmitted_bys(CommonMethods.replaceEmptyByNullInSringArray(obj.getSubmitted_bys()));
												if(arraySize < obj.getSubmitted_bys().length) {
													arraySize = obj.getSubmitted_bys().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getSubmitted_tos()) && obj.getSubmitted_tos().length > 0) {
												obj.setSubmitted_tos(CommonMethods.replaceEmptyByNullInSringArray(obj.getSubmitted_tos()));
												if(arraySize < obj.getSubmitted_tos().length) {
													arraySize = obj.getSubmitted_tos().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getSubmitted_dates()) && obj.getSubmitted_dates().length > 0) {
												obj.setSubmitted_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getSubmitted_dates()));
												if(arraySize < obj.getSubmitted_dates().length) {
													arraySize = obj.getSubmitted_dates().length;
												}
											}
											if(!StringUtils.isEmpty(obj.getSubmssion_purposes()) && obj.getSubmssion_purposes().length > 0) {
												obj.setSubmssion_purposes(CommonMethods.replaceEmptyByNullInSringArray(obj.getSubmssion_purposes()));
												if(arraySize < obj.getSubmssion_purposes().length) {
													arraySize = obj.getSubmssion_purposes().length;
												}
											}
											return arraySize;
									}
							  });*/
		    	 
		    	 
					String deleteQry = "DELETE from design_revisions where design_id_fk = :design_id";		 
					paramSource = new BeanPropertySqlParameterSource(obj);		 
					count = namedParamJdbcTemplate.update(deleteQry, paramSource);
				
					String qryDesignRevision = "INSERT INTO design_revisions (design_id_fk,revision,drawing_no,correspondence_letter_no,revision_date,"
							+ "revision_status_fk,[current],remarks,upload_file) VALUES(?,?,?,?,?,?,?,?,?)";
					
					int[] counts = jdbcTemplate.batchUpdate(qryDesignRevision,
				            new BatchPreparedStatementSetter() {
								@Override
								public void setValues(PreparedStatement ps, int i) throws SQLException {
									try {
										int k = 1;
										ps.setString(k++, obj.getDesign_id());
										ps.setString(k++,(obj.getRevisions().length > 0)?obj.getRevisions()[i]:null);
										
										ps.setString(k++,(obj.getDrawing_nos().length > 0)?obj.getDrawing_nos()[i]:null);
										ps.setString(k++,(obj.getCorrespondence_letter_nos().length > 0)?obj.getCorrespondence_letter_nos()[i]:null);	
										
										
										ps.setString(k++,DateParser.parse((obj.getRevision_dates().length > 0)?obj.getRevision_dates()[i]:null));
										ps.setString(k++,(obj.getRevision_status_fks().length > 0)?obj.getRevision_status_fks()[i]:null);
										ps.setString(k++,(obj.getCurrents().length > 0)?obj.getCurrents()[i]:null);
										ps.setString(k++,(obj.getRemarkss().length > 0)?obj.getRemarkss()[i]:null);
										
										
										String docFileName = null;
										MultipartFile multipartFile = obj.getUploadFiles()[i];
										if ((null != multipartFile && !multipartFile.isEmpty() && multipartFile.getSize() > 0)
												|| (!StringUtils.isEmpty(obj.getUploadFileNames()) && obj.getUploadFileNames().length > 0 && !StringUtils.isEmpty(obj.getUploadFileNames()[i]) && !StringUtils.isEmpty(obj.getUploadFileNames()[i].trim()) )) {
											String saveDirectory = CommonConstants.DESIGN_REVISION_FILES ;
											String fileName = obj.getUploadFileNames()[i];
											DateFormat df = new SimpleDateFormat("ddMMYY-HHmm-ssSSSSSSS"); 
											String fileName_new = "Design-Revision-"+ df.format(new Date()) +"."+ fileName.split("\\.")[1];
											docFileName = fileName_new;
											if (null != multipartFile && !multipartFile.isEmpty()) {
												FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
											}
										  ps.setString(k++,fileName);
											  
										}									
										
									
									} catch (Exception e) {
										
									}
								}
								@Override
								public int getBatchSize() {
									int arraySize = 0;
									if(!StringUtils.isEmpty(obj.getRevisions()) && obj.getRevisions().length > 0) {
										obj.setRevisions(CommonMethods.replaceEmptyByNullInSringArray(obj.getRevisions()));
										if(arraySize < obj.getRevisions().length) {
											arraySize = obj.getRevisions().length;
										}
									}
									if(!StringUtils.isEmpty(obj.getDrawing_nos()) && obj.getDrawing_nos().length > 0) {
										obj.setDrawing_nos(CommonMethods.replaceEmptyByNullInSringArray(obj.getDrawing_nos()));
										if(arraySize < obj.getDrawing_nos().length) {
											arraySize = obj.getDrawing_nos().length;
										}
									}									
									if(!StringUtils.isEmpty(obj.getCorrespondence_letter_nos()) && obj.getCorrespondence_letter_nos().length > 0) {
										obj.setCorrespondence_letter_nos(CommonMethods.replaceEmptyByNullInSringArray(obj.getCorrespondence_letter_nos()));
										if(arraySize < obj.getCorrespondence_letter_nos().length) {
											arraySize = obj.getCorrespondence_letter_nos().length;
										}
									}	
								
									if(!StringUtils.isEmpty(obj.getRevision_dates()) && obj.getRevision_dates().length > 0) {
										obj.setRevision_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getRevision_dates()));
										if(arraySize < obj.getRevision_dates().length) {
											arraySize = obj.getRevision_dates().length;
										}
									}
									if(!StringUtils.isEmpty(obj.getRevision_status_fks()) && obj.getRevision_status_fks().length > 0) {
										obj.setRevision_status_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getRevision_status_fks()));
										if(arraySize < obj.getRevision_status_fks().length) {
											arraySize = obj.getRevision_status_fks().length;
										}
									}			
									if(!StringUtils.isEmpty(obj.getRemarkss()) && obj.getRemarkss().length > 0) {
										obj.setRemarkss(CommonMethods.replaceEmptyByNullInSringArray(obj.getRemarkss()));
										if(arraySize < obj.getRemarkss().length) {
											arraySize = obj.getRemarkss().length;
										}
									}
									if(!StringUtils.isEmpty(obj.getUploadFiles()) && obj.getUploadFiles().length > 0) {
										if(arraySize < obj.getUploadFiles().length) {
											arraySize = obj.getUploadFiles().length;
										}
									}								
									return arraySize;
							}
					  });		    	 
		    	 
				
				String issueId = null;
				if(!StringUtils.isEmpty(obj.getIs_there_issue()) && obj.getIs_there_issue().equalsIgnoreCase("yes")){
					String issuesQry = "INSERT INTO issue(contract_id_fk,title,reported_by,priority_fk,category_fk,status_fk,date)VALUES(?,?,?,?,?,?,CONVERT(date, getdate()))";				
					KeyHolder holder = new GeneratedKeyHolder();
					jdbcTemplate.update(new PreparedStatementCreator() {
						@Override
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(issuesQry, Statement.RETURN_GENERATED_KEYS);
							int i = 1;
							ps.setString(i++, !StringUtils.isEmpty(obj.getContract_id_fk())?obj.getContract_id_fk():null);
							ps.setString(i++, obj.getIssue_description());
							ps.setString(i++, !StringUtils.isEmpty(obj.getCreated_by_user_id_fk())?obj.getCreated_by_user_id_fk():null);
							ps.setString(i++, !StringUtils.isEmpty(obj.getIssue_priority_id())?obj.getIssue_priority_id():null);
							ps.setString(i++, !StringUtils.isEmpty(obj.getIssue_category_id())?obj.getIssue_category_id():null);
							ps.setString(i++, "Raised");
							return ps;
						}
					}, holder);

					issueId = String.valueOf(holder.getKey().longValue());				
				}else{
					issueId = null;
				}
				FormHistory formHistory = new FormHistory();
				formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
				formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
				formHistory.setModule_name_fk("Design");
				formHistory.setForm_name("Add Design");
				formHistory.setForm_action_type("Add");
				formHistory.setForm_details("New Design "+obj.getDesign_id() + " Created");
				formHistory.setContract_id_fk(obj.getContract_id_fk());
				
				boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
				
				String messageQry = "INSERT INTO messages (message,user_id_fk,redirect_url,created_date,message_type)"
						+ "VALUES" + "(:message,:user_id_fk,:redirect_url,CURRENT_TIMESTAMP,:message_type)";	
				String executives=getDDExecutives(obj.getProject_id_fk());
				
				if(!StringUtils.isEmpty(executives)) {

					String [] SplitStr=executives.split(",");
						
					for(int i=0;i<SplitStr.length;i++)
					{
						Messages msgObj = new Messages();
						msgObj.setUser_id_fk(SplitStr[i]);
						msgObj.setMessage("A new Design & Drawing against "+obj.getProject_id_fk()+" has been added");
						msgObj.setRedirect_url("/get-design/"+designId);
						msgObj.setMessage_type("Design & Drawing");	
						BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
						namedParamJdbcTemplate.update(messageQry, paramSource1);						
					}
					
					Messages msgObj = new Messages();
					msgObj.setUser_id_fk(obj.getHod());
					msgObj.setMessage("A new Design & Drawing against "+obj.getProject_id_fk()+" has been added");
					msgObj.setRedirect_url("/get-design/"+designId);
					msgObj.setMessage_type("Design & Drawing");	
					BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
					namedParamJdbcTemplate.update(messageQry, paramSource1);
					
					Messages msgObj1 = new Messages();
					msgObj1.setUser_id_fk(obj.getDy_hod());
					msgObj1.setMessage("A new Design & Drawing against "+obj.getProject_id_fk()+" has been added");
					msgObj1.setRedirect_url("/get-design/"+designId);
					msgObj1.setMessage_type("Design & Drawing");	
					BeanPropertySqlParameterSource paramSource2 = new BeanPropertySqlParameterSource(msgObj1);
					namedParamJdbcTemplate.update(messageQry, paramSource2);					
				}
				
				
				
				
				
				
				/********************************************************************************/
			}

			//transactionManager.commit(status);
		}catch(Exception e){ 
			//transactionManager.rollback(status);
			e.printStackTrace();
			throw new Exception(e);
		}
			
		return design_seq_id;
	}
	private String getDDExecutives(String project_id) throws Exception {
	    String executives = "";
	    try {
	        String qry = 
	            "SELECT STRING_AGG(u.user_id, ',') AS user_id " +
	            "FROM designexecutives re " +
	            "LEFT JOIN [user] u ON re.executive_user_id_fk = u.user_id " +
	            "LEFT JOIN project p ON re.project_id_fk = p.project_id " +
	            "WHERE re.project_id_fk = ?";
	        
	        executives = jdbcTemplate.queryForObject(qry, new Object[]{project_id}, String.class);
	    } catch (Exception e) {
	        throw new Exception(e);
	    }
	    return executives;
	}


	@Override
	public String updateDesign(Design obj) throws Exception {
		boolean flag = false;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		try{
			
			if (StringUtils.isEmpty(obj.getDesign_seq_id()))
			{
				String design_seq_id = getAutoGeneratedDesignId(obj);
				obj.setDesign_seq_id(design_seq_id);
			}
			
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());	
			String qry = "UPDATE design SET contract_id_fk= :contract_id_fk,department_id_fk=:department_id_fk,hod=:hod,dy_hod=:dy_hod,prepared_by_id_fk=:prepared_by_id_fk,consultant_contract_id_fk=:consultant_contract_id_fk,proof_consultant_contract_id_fk=:proof_consultant_contract_id_fk,structure_type_fk=:structure_type_fk"
					+ ",drawing_type_fk=:drawing_type_fk,contractor_drawing_no=:contractor_drawing_no,mrvc_drawing_no=:mrvc_drawing_no,division_drawing_no=:division_drawing_no,hq_drawing_no=:hq_drawing_no,drawing_title=:drawing_title,"
					+ "gfc_released=:gfc_released,remarks=:remarks,design_seq_id=:design_seq_id,"
					+ "approving_railway=:approving_railway,approval_authority_fk=:approval_authority_fk,required_date=:required_date,structure_id_fk=:structure_id_fk,modified_by=:created_by_user_id_fk,modified_date=CURRENT_TIMESTAMP,component=:component,[3pvc]=:threepvc   "
					+ "WHERE design_id = :design_id";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = namedParamJdbcTemplate.update(qry, paramSource);			
			if(count > 0) {
				flag = true;
				
				String deleteFilesQry = "delete from design_files  where design_id_fk = :design_id";		 
				Design fileObj = new Design();
				fileObj.setDesign_id(obj.getDesign_id());
				paramSource = new BeanPropertySqlParameterSource(obj);	
				namedParamJdbcTemplate.update(deleteFilesQry, paramSource);
			
				 flag = true;
				 int  docArrSize = 0;
				 if (!StringUtils.isEmpty(obj.getDesignDocumentFileNames()) && obj.getDesignDocumentFileNames().length > 0) {
						obj.setDesignDocumentFileNames(CommonMethods.replaceEmptyByNullInSringArray(obj.getDesignDocumentFileNames()));
						if (docArrSize < obj.getDesignDocumentFileNames().length) {
							docArrSize = obj.getDesignDocumentFileNames().length;
						}
					}
					if (!StringUtils.isEmpty(obj.getDesign_file_typess()) && obj.getDesign_file_typess().length > 0) {
						obj.setDesign_file_typess(CommonMethods.replaceEmptyByNullInSringArray(obj.getDesign_file_typess()));
						if (docArrSize < obj.getDesign_file_typess().length) {
							docArrSize = obj.getDesign_file_typess().length;
						}
					}
					if (!StringUtils.isEmpty(obj.getDesignDocumentNames()) && obj.getDesignDocumentNames().length > 0) {
						obj.setDesignDocumentNames(CommonMethods.replaceEmptyByNullInSringArray(obj.getDesignDocumentNames()));
						if (docArrSize < obj.getDesignDocumentNames().length) {
							docArrSize = obj.getDesignDocumentNames().length;
						} 
					}
					for (int i = 0; i < docArrSize; i++) {
					   if(!StringUtils.isEmpty(obj.getDesignDocumentFiles()) && obj.getDesignDocumentFiles().length > 0) {
							
							String file_insert_qry = "INSERT into  design_files ( design_id_fk,design_file_type_fk,name, attachment) VALUES (:design_id,:design_file_type_fk,:name,:attachment)";
							MultipartFile designFiles = obj.getDesignDocumentFiles()[i];
							if (((null != designFiles && !designFiles.isEmpty()) || !StringUtils.isEmpty(obj.getDesignDocumentFileNames())) 
									&& !StringUtils.isEmpty(obj.getDesign_file_typess().length > 0) && !StringUtils.isEmpty(obj.getDesign_file_typess()[i])
									&& !StringUtils.isEmpty(obj.getDesignDocumentNames()) && !StringUtils.isEmpty(obj.getDesignDocumentNames()[i]))  {
									String saveDirectory = CommonConstants2.DESIGN_FILE_SAVING_PATH ;
									String fileName = designFiles.getOriginalFilename();
									DateFormat df = new SimpleDateFormat("ddMMYY-HHmm"); 
									String fileName_new = null;
									if(!designFiles.isEmpty()) {
										 fileName_new = "Design-"+obj.getDesign_id() +"-"+ df.format(new Date()) +"."+ fileName.split("\\.")[1];
									}else {
										 fileName_new = obj.getDesignDocumentFileNames()[i];
									}
									FileUploads.singleFileSaving(designFiles, saveDirectory, fileName_new);
									int lwn = obj.getDesign_file_typess().length;
									String design_file_type_fk =  null;
									if(obj.getDesign_file_typess().length > 0) {
										design_file_type_fk = obj.getDesign_file_typess()[i];
									}
									String name = null;
									if(obj.getDesignDocumentNames().length > 0) {
										name = obj.getDesignDocumentNames()[i];
									}
									
									fileObj = new Design();
									fileObj.setDesign_id(obj.getDesign_id());
									fileObj.setDesign_file_type_fk(design_file_type_fk);
									fileObj.setName(name);
									fileObj.setAttachment(fileName_new);
									//fileObj.setStatus(CommonConstants.ACTIVE);
									
									paramSource = new BeanPropertySqlParameterSource(fileObj);	
									namedParamJdbcTemplate.update(file_insert_qry, paramSource);
							}
						}	
					}
			}
			if(flag) {
				String deleteQry = "DELETE from design_revisions where design_id_fk = :design_id";		 
				paramSource = new BeanPropertySqlParameterSource(obj);		 
				count = namedParamJdbcTemplate.update(deleteQry, paramSource);
			
				String qryDesignRevision = "INSERT INTO design_revisions (design_id_fk,revision,drawing_no,correspondence_letter_no,revision_date,"
						+ "revision_status_fk,[current],remarks,upload_file) VALUES(?,?,?,?,?,?,?,?,?)";
				
				int[] counts = jdbcTemplate.batchUpdate(qryDesignRevision,
			            new BatchPreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {
								try {
									int k = 1;
									ps.setString(k++, obj.getDesign_id());
									ps.setString(k++,(obj.getRevisions().length > 0)?obj.getRevisions()[i]:null);
									
									ps.setString(k++,(obj.getDrawing_nos().length > 0)?obj.getDrawing_nos()[i]:null);
									ps.setString(k++,(obj.getCorrespondence_letter_nos().length > 0)?obj.getCorrespondence_letter_nos()[i]:null);	
									
									
									ps.setString(k++,DateParser.parse((obj.getRevision_dates().length > 0)?obj.getRevision_dates()[i]:null));
									ps.setString(k++,(obj.getRevision_status_fks().length > 0)?obj.getRevision_status_fks()[i]:null);
									ps.setString(k++,(obj.getCurrents().length > 0)?obj.getCurrents()[i]:null);
									ps.setString(k++,(obj.getRemarkss().length > 0)?obj.getRemarkss()[i]:null);
									
									
									String docFileName = null;
									MultipartFile multipartFile = obj.getUploadFiles()[i];
									if ((null != multipartFile && !multipartFile.isEmpty() && multipartFile.getSize() > 0)
											|| (!StringUtils.isEmpty(obj.getUploadFileNames()) && obj.getUploadFileNames().length > 0 && !StringUtils.isEmpty(obj.getUploadFileNames()[i]) && !StringUtils.isEmpty(obj.getUploadFileNames()[i].trim()) )) {
										String saveDirectory = CommonConstants.DESIGN_REVISION_FILES ;
										String fileName = obj.getUploadFileNames()[i];
										DateFormat df = new SimpleDateFormat("ddMMYY-HHmm-ssSSSSSSS"); 
										String fileName_new = "Design-Revision-"+ df.format(new Date()) +"."+ fileName.split("\\.")[1];
										docFileName = fileName_new;
										if (null != multipartFile && !multipartFile.isEmpty()) {
											FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
										}
									  ps.setString(k++,fileName);
										  
									}									
									
								
								} catch (Exception e) {
									
								}
							}
							@Override
							public int getBatchSize() {
								int arraySize = 0;
								if(!StringUtils.isEmpty(obj.getRevisions()) && obj.getRevisions().length > 0) {
									obj.setRevisions(CommonMethods.replaceEmptyByNullInSringArray(obj.getRevisions()));
									if(arraySize < obj.getRevisions().length) {
										arraySize = obj.getRevisions().length;
									}
								}
								if(!StringUtils.isEmpty(obj.getDrawing_nos()) && obj.getDrawing_nos().length > 0) {
									obj.setDrawing_nos(CommonMethods.replaceEmptyByNullInSringArray(obj.getDrawing_nos()));
									if(arraySize < obj.getDrawing_nos().length) {
										arraySize = obj.getDrawing_nos().length;
									}
								}									
								if(!StringUtils.isEmpty(obj.getCorrespondence_letter_nos()) && obj.getCorrespondence_letter_nos().length > 0) {
									obj.setCorrespondence_letter_nos(CommonMethods.replaceEmptyByNullInSringArray(obj.getCorrespondence_letter_nos()));
									if(arraySize < obj.getCorrespondence_letter_nos().length) {
										arraySize = obj.getCorrespondence_letter_nos().length;
									}
								}	
							
								if(!StringUtils.isEmpty(obj.getRevision_dates()) && obj.getRevision_dates().length > 0) {
									obj.setRevision_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getRevision_dates()));
									if(arraySize < obj.getRevision_dates().length) {
										arraySize = obj.getRevision_dates().length;
									}
								}
								if(!StringUtils.isEmpty(obj.getRevision_status_fks()) && obj.getRevision_status_fks().length > 0) {
									obj.setRevision_status_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getRevision_status_fks()));
									if(arraySize < obj.getRevision_status_fks().length) {
										arraySize = obj.getRevision_status_fks().length;
									}
								}			
								if(!StringUtils.isEmpty(obj.getRemarkss()) && obj.getRemarkss().length > 0) {
									obj.setRemarkss(CommonMethods.replaceEmptyByNullInSringArray(obj.getRemarkss()));
									if(arraySize < obj.getRemarkss().length) {
										arraySize = obj.getRemarkss().length;
									}
								}
								if(!StringUtils.isEmpty(obj.getUploadFiles()) && obj.getUploadFiles().length > 0) {
									if(arraySize < obj.getUploadFiles().length) {
										arraySize = obj.getUploadFiles().length;
									}
								}								
								return arraySize;
						}
				  });
				
				String deleteQryForDesignStatus = "DELETE from design_status where design_id_fk = :design_id";	
				paramSource = new BeanPropertySqlParameterSource(obj);		 
				count = namedParamJdbcTemplate.update(deleteQryForDesignStatus, paramSource);
			
				String qryDesignStatus = "INSERT INTO design_status (design_id_fk,stage_fk,submitted_by,submitted_to,submitted_date,"
						+ "submssion_purpose,latest) VALUES(?,?,?,?,?,?,?)";
				
				int[] statusCounts = jdbcTemplate.batchUpdate(qryDesignStatus,
			            new BatchPreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {
								try {
									int k = 1;
									ps.setString(k++, obj.getDesign_id());
									ps.setString(k++,(obj.getStage_fks().length > 0 && !StringUtils.isEmpty(obj.getStage_fks()[i]))?obj.getStage_fks()[i]:null);	
									ps.setString(k++,(obj.getSubmitted_bys().length > 0 && !StringUtils.isEmpty(obj.getSubmitted_bys()[i]))?obj.getSubmitted_bys()[i]:null);	
									ps.setString(k++,(obj.getSubmitted_tos().length > 0 && !StringUtils.isEmpty(obj.getSubmitted_tos()[i]))?obj.getSubmitted_tos()[i]:null);	
									ps.setString(k++,DateParser.parse((obj.getSubmitted_dates().length > 0 && !StringUtils.isEmpty(obj.getSubmitted_dates()[i]))?obj.getSubmitted_dates()[i]:null));
									ps.setString(k++,(obj.getSubmssion_purposes().length > 0 && !StringUtils.isEmpty(obj.getSubmssion_purposes()[i]))?obj.getSubmssion_purposes()[i]:null);
									ps.setString(k++,(obj.getLatests().length > 0 && !StringUtils.isEmpty(obj.getLatests()[i]))?obj.getLatests()[i]:null);
								} catch (Exception e) {
									
								}
							}
							@Override
							public int getBatchSize() {
								int arraySize = 0;
								if(!StringUtils.isEmpty(obj.getStage_fks()) && obj.getStage_fks().length > 0) {
									obj.setStage_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getStage_fks()));
									if(arraySize < obj.getStage_fks().length) {
										arraySize = obj.getStage_fks().length;
									}
								}
								if(!StringUtils.isEmpty(obj.getSubmitted_bys()) && obj.getSubmitted_bys().length > 0) {
									obj.setSubmitted_bys(CommonMethods.replaceEmptyByNullInSringArray(obj.getSubmitted_bys()));
									if(arraySize < obj.getSubmitted_bys().length) {
										arraySize = obj.getSubmitted_bys().length;
									}
								}
								if(!StringUtils.isEmpty(obj.getSubmitted_tos()) && obj.getSubmitted_tos().length > 0) {
									obj.setSubmitted_tos(CommonMethods.replaceEmptyByNullInSringArray(obj.getSubmitted_tos()));
									if(arraySize < obj.getSubmitted_tos().length) {
										arraySize = obj.getSubmitted_tos().length;
									}
								}
								if(!StringUtils.isEmpty(obj.getSubmitted_dates()) && obj.getSubmitted_dates().length > 0) {
									obj.setSubmitted_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getSubmitted_dates()));
									if(arraySize < obj.getSubmitted_dates().length) {
										arraySize = obj.getSubmitted_dates().length;
									}
								}
								if(!StringUtils.isEmpty(obj.getSubmssion_purposes()) && obj.getSubmssion_purposes().length > 0) {
									obj.setSubmssion_purposes(CommonMethods.replaceEmptyByNullInSringArray(obj.getSubmssion_purposes()));
									if(arraySize < obj.getSubmssion_purposes().length) {
										arraySize = obj.getSubmssion_purposes().length;
									}
								}
								return arraySize;
						}
				  });
				
				String issueId = null;
				if(!StringUtils.isEmpty(obj.getIs_there_issue()) && obj.getIs_there_issue().equalsIgnoreCase("yes")){
					String issuesQry = "INSERT INTO issue(contract_id_fk,title,reported_by,priority_fk,category_fk,status_fk,date)VALUES(?,?,?,?,?,?,CONVERT(date, getdate()))";				
					KeyHolder holder = new GeneratedKeyHolder();
					jdbcTemplate.update(new PreparedStatementCreator() {
						@Override
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(issuesQry, Statement.RETURN_GENERATED_KEYS);
							int i = 1;
							ps.setString(i++, !StringUtils.isEmpty(obj.getContract_id_fk())?obj.getContract_id_fk():null);
							ps.setString(i++, obj.getIssue_description());
							ps.setString(i++, !StringUtils.isEmpty(obj.getCreated_by_user_id_fk())?obj.getCreated_by_user_id_fk():null);
							ps.setString(i++, !StringUtils.isEmpty(obj.getIssue_priority_id())?obj.getIssue_priority_id():null);
							ps.setString(i++, !StringUtils.isEmpty(obj.getIssue_category_id())?obj.getIssue_category_id():null);
							ps.setString(i++, "Raised");
							return ps;
						}
					}, holder);

					issueId = String.valueOf(holder.getKey().longValue());				
				}else{
					issueId = null;
				}
				FormHistory formHistory = new FormHistory();
				formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
				formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
				formHistory.setModule_name_fk("Design");
				formHistory.setForm_name("Update Design");
				formHistory.setForm_action_type("Update");
				formHistory.setForm_details("Design "+obj.getDesign_id() + " Updated");
				formHistory.setContract_id_fk(obj.getContract_id_fk());
				
				boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
				/********************************************************************************/
				
				String messageQry = "INSERT INTO messages (message,user_id_fk,redirect_url,created_date,message_type)"
						+ "VALUES" + "(:message,:user_id_fk,:redirect_url,CURRENT_TIMESTAMP,:message_type)";	
				String executives=getDDExecutives(obj.getProject_id_fk());
				
				if(!StringUtils.isEmpty(executives)) {

					String [] SplitStr=executives.split(",");
						
					for(int i=0;i<SplitStr.length;i++)
					{
						Messages msgObj = new Messages();
						msgObj.setUser_id_fk(SplitStr[i]);
						msgObj.setMessage("A new Design & Drawing against "+obj.getProject_id_fk()+" has been updated");
						msgObj.setRedirect_url("/get-design/"+obj.getDesign_id());
						msgObj.setMessage_type("Design & Drawing");	
						BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
						namedParamJdbcTemplate.update(messageQry, paramSource1);						
					}
					
					Messages msgObj = new Messages();
					msgObj.setUser_id_fk(obj.getHod());
					msgObj.setMessage("A new Design & Drawing against "+obj.getProject_id_fk()+" has been updated");
					msgObj.setRedirect_url("/get-design/"+obj.getDesign_id());
					msgObj.setMessage_type("Design & Drawing");	
					BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
					namedParamJdbcTemplate.update(messageQry, paramSource1);
					
					Messages msgObj1 = new Messages();
					msgObj1.setUser_id_fk(obj.getDy_hod());
					msgObj1.setMessage("A new Design & Drawing against "+obj.getProject_id_fk()+" has been updated");
					msgObj1.setRedirect_url("/get-design/"+obj.getDesign_id());
					msgObj1.setMessage_type("Design & Drawing");	
					BeanPropertySqlParameterSource paramSource2 = new BeanPropertySqlParameterSource(msgObj1);
					namedParamJdbcTemplate.update(messageQry, paramSource2);					
				}				
				
				
				
				
			}

			//transactionManager.commit(status);
		}catch(Exception e){ 
			//transactionManager.rollback(status);
			e.printStackTrace();
			throw new Exception(e);
		}
			
		return obj.getDesign_seq_id();
	}

	@Override
	public List<Design> getDesignRevisions(Design obj) throws Exception {
		List<Design> objsList = null;
		try {
			String qry ="select distinct d.design_seq_id , design_revisions_id,drawing_no,correspondence_letter_no,upload_file,	design_id_fk,	revision,	revision_date,	revision_status_fk,	[current],	ds.remarks " + 
					"" + 
					"from design d  " + 
					"LEFT OUTER JOIN contract c ON d.contract_id_fk = c.contract_id  " + 
					"LEFT OUTER JOIN contract c1 ON d.consultant_contract_id_fk = c1.contract_id  " + 
					"LEFT OUTER JOIN contract c2 ON d.proof_consultant_contract_id_fk = c2.contract_id  " + 
					"LEFT OUTER JOIN project p  ON d.project_id_fk  =  p.project_id  " + 
					"left outer join [user] u  ON d.hod  =  u.user_id  " + 
					"left outer join [user] u1  ON d.dy_hod  =  u1.user_id  " + 
					"LEFT OUTER JOIN department dt  ON d.department_id_fk  =  dt.department   " + 
					"left join design_revisions ds on d.design_id = ds.design_id_fk  where design_id_fk is not null ";
				
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and d.project_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and contract_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				qry = qry + " and department_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				qry = qry + " and hod = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				qry = qry + " and structure_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				qry = qry + " and drawing_type_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSearchStr())) {
				qry = qry + " and (contract_id_fk like ? or c.contract_short_name like ? or d.drawing_title like ? or d.structure_type_fk like ?"
						+ " or drawing_type_fk like ? or d.contractor_drawing_no like ? or d.mrvc_drawing_no like ? or d.division_drawing_no like ? or d.hq_drawing_no like ? or d.design_seq_id like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_id_fk())) {
				pValues[i++] = obj.getDepartment_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod())) {
				pValues[i++] = obj.getHod();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())) {
				pValues[i++] = obj.getStructure_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDrawing_type_fk())) {
				pValues[i++] = obj.getDrawing_type_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSearchStr())) {		
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";	
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Design>(Design.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public boolean saveDesignDataUploadFile(Design design) throws Exception {
		boolean flag = false;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		String design_data_id = null;		
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());			 
			String qry = "INSERT INTO design_data"
					+ "(uploaded_file, status, remarks, uploaded_by_user_id_fk, uploaded_on) "
					+ "VALUES "
					+ "( :uploaded_file, :status, :remarks, :uploaded_by_user_id_fk,CURRENT_TIMESTAMP)";	
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(design);		 
			KeyHolder keyHolder = new GeneratedKeyHolder();
		    int count = template.update(qry, paramSource, keyHolder);
			if(count > 0) {
				design_data_id = String.valueOf(keyHolder.getKey().intValue());
				design.setDesign_data_id(design_data_id);
				flag = true;
				
				MultipartFile file = design.getDesignFile();
				if (null != file && !file.isEmpty() && file.getSize() > 0){
					String saveDirectory = CommonConstants.DESIGN_UPLOADED_FILE_SAVING_PATH ;
					String fileName = design_data_id + "_" +file.getOriginalFilename();
					FileUploads.singleFileSaving(file, saveDirectory, fileName);
					
					design.setUploaded_file(fileName);
					String updateQry = "UPDATE design_data set uploaded_file= :uploaded_file where design_data_id= :design_data_id ";
					BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(design);		
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
	public int uploadDesignsNew(List<Design> designsList) throws Exception {
		int count = 0;
		boolean flag = false;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
			String qry = "INSERT INTO design (project_id_fk,contract_id_fk,department_id_fk,hod,dy_hod,prepared_by_id_fk,consultant_contract_id_fk,proof_consultant_contract_id_fk,"
					+ "structure_type_fk,drawing_type_fk,contractor_drawing_no,mrvc_drawing_no,division_drawing_no,hq_drawing_no,drawing_title,"
					+ "gfc_released,remarks," + 
					"approving_railway,approval_authority_fk,structure_id_fk,required_date,component,design_seq_id,[3pvc]) "
					+ "VALUES(:project_id_fk,:contract_id_fk,:department_id_fk,:hod,:dy_hod,:prepared_by_id_fk,:consultant_contract_id_fk,:proof_consultant_contract_id_fk,:structure_type_fk"
					+ ",:drawing_type_fk,:mrvc_drawing_no,:mrvc_drawing_no,:division_drawing_no,:hq_drawing_no,:drawing_title,"
					+ ":gfc_released,:remarks,"
					+ ":approving_railway,:approval_authority_fk,:structure_id_fk,:required_date,:component,:design_seq_id,:threepvc)";
			
			String updateQry = "UPDATE design set contract_id_fk= :contract_id_fk, approving_railway= :approving_railway, department_id_fk= :department_id_fk,hod= :hod,"
					+ "dy_hod= :dy_hod,structure_type_fk= :structure_type_fk,structure_id_fk= :structure_id_fk,prepared_by_id_fk= :prepared_by_id_fk ,consultant_contract_id_fk= :consultant_contract_id_fk,"
					+ "proof_consultant_contract_id_fk= :proof_consultant_contract_id_fk,drawing_type_fk= :drawing_type_fk,drawing_title= :drawing_title,approval_authority_fk= :approval_authority_fk,"
					+ "required_date=:required_date,contractor_drawing_no= :contractor_drawing_no,mrvc_drawing_no= :mrvc_drawing_no,division_drawing_no= :division_drawing_no,"
					+ "hq_drawing_no= :hq_drawing_no,gfc_released= :gfc_released,remarks=:remarks,modified_by=:created_by_user_id_fk,modified_date=CURRENT_TIMESTAMP,[3pvc]=:Threepvc where design_seq_id= :design_seq_id ";
			for (Design obj : designsList) {
				
				if(!StringUtils.isEmpty(obj.getConsultant_contract_id_fk())) {
				String[] splitStr=obj.getConsultant_contract_id_fk().split(" - "); 
				obj.setConsultant_contract_id_fk(splitStr[0]);
				}
				
				if(!StringUtils.isEmpty(obj.getProof_consultant_contract_id_fk())) {
				String[] splitStr1=obj.getProof_consultant_contract_id_fk().split(" - "); 
				obj.setProof_consultant_contract_id_fk(splitStr1[0]);
				}
				
				
				
				/*String department = null;
				if(!StringUtils.isEmpty(obj.getDepartment_id_fk())) { 
				  String deptqry ="SELECT department from department where department_name = ?"; 
				  department = (String)jdbcTemplate.queryForObject( deptqry,new Object[]{obj.getDepartment_id_fk()} ,String.class); 
				}
				obj.setDepartment_id_fk(department);*/
				//String hod = getHod(obj.getHod());
				//String dyHod = getDyHod(obj.getDy_hod());
				//obj.setHod(hod);obj.setDy_hod(dyHod);
				if(!StringUtils.isEmpty(obj.getPrepared_by_id_fk())) {
					String preparedByQry = "INSERT INTO design_prepared_by (prepared_by) SELECT * FROM (SELECT ? AS tmp " + 
							"WHERE NOT EXISTS ( SELECT prepared_by FROM design_prepared_by WHERE prepared_by = ?  )) as tmp";
					jdbcTemplate.update( preparedByQry, new Object[] {obj.getPrepared_by_id_fk(),obj.getPrepared_by_id_fk()});
				}
				
				/*if(!StringUtils.isEmpty(obj.getStructure_type_fk())) {
					String stQry = "INSERT INTO structure_type (structure_type) SELECT * FROM (SELECT ?) AS tmp "
							+ "WHERE NOT EXISTS ( SELECT structure_type FROM structure_type WHERE structure_type = ? offset 0 rows  fetch next 1 rows only )";
					jdbcTemplate.update( stQry, new Object[] {obj.getStructure_type_fk(),obj.getStructure_type_fk()});
				}*/
				
				if(!StringUtils.isEmpty(obj.getDrawing_type_fk())) {
					String dtQry = "INSERT INTO drawing_type (drawing_type) SELECT * FROM (SELECT ? AS tmp "
							+ "WHERE NOT EXISTS ( SELECT drawing_type FROM drawing_type WHERE drawing_type = ? )) as tmp";
					jdbcTemplate.update( dtQry, new Object[] {obj.getDrawing_type_fk(),obj.getDrawing_type_fk()});
				}
				
				String designId = obj.getDesign_seq_id();
				if(!StringUtils.isEmpty(designId)) {
					obj.setDesign_id(getDesignId(obj));
					SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
				    count = namedParamJdbcTemplate.update(updateQry, paramSource);
				    if(count > 0) {
						 flag = true;
						 String deleteQryForDesignStatus = "UPDATE design_status set latest = 'No' where design_id_fk = :design_id";		 
						 paramSource = new BeanPropertySqlParameterSource(obj);		 
						 count = namedParamJdbcTemplate.update(deleteQryForDesignStatus, paramSource);
					}
				}else {
					
					String design_seq_id = getAutoGeneratedDesignId(obj);
					obj.setDesign_seq_id(design_seq_id);
					
					SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
				    KeyHolder keyHolder = new GeneratedKeyHolder();
				    count = namedParamJdbcTemplate.update(qry, paramSource, keyHolder);
				    designId = null;
					if(count > 0) {
						 designId = String.valueOf(keyHolder.getKey().intValue());
						 obj.setDesign_id(getDesignId(obj));
						 flag = true;
					}
				}
				
				
				if(flag) {
					con = jdbcTemplate.getDataSource().getConnection();
					String qryDesignRevision = "INSERT INTO design_status (design_id_fk, stage_fk, submitted_by, submitted_to, submitted_date, submssion_purpose, latest) VALUES(?,?,?,?,?,?,?)";
					stmt = con.prepareStatement(qryDesignRevision); 				
					String stage_fk = obj.getStage_fk();
					String submitted_date = obj.getSubmitted_date();
					String submitted_by = obj.getSubmitted_by();
					String submitted_to = obj.getSubmitted_to();
					String submssion_purpose = obj.getSubmission_purpose();
					if( !StringUtils.isEmpty(stage_fk)) {
							int k = 1;
							stmt.setString(k++, obj.getDesign_id());
							stmt.setString(k++,!StringUtils.isEmpty(stage_fk)?stage_fk:null);
							stmt.setString(k++,!StringUtils.isEmpty(submitted_by)?submitted_by:null);
							stmt.setString(k++,!StringUtils.isEmpty(submitted_to)?submitted_to:null);
							stmt.setString(k++,DateParser.parse(!StringUtils.isEmpty(submitted_date)?submitted_date:null));
							stmt.setString(k++,!StringUtils.isEmpty(submssion_purpose)?submssion_purpose:null);
							stmt.setString(k++,(CommonConstants.YES ));
							stmt.addBatch();
					}
					stmt.executeBatch();
					DBConnectionHandler.closeJDBCResoucrs(con, stmt, null);
				}
				
			
				
				if(!StringUtils.isEmpty(obj.getDesignRevisions())) {
					String qryDesignRevision = "INSERT INTO design_revisions (design_id_fk,revision,revision_date,drawing_no,correspondence_letter_no,upload_file,"
							+ "revision_status_fk,[current],remarks) VALUES(?,?,?,?,?,?,?,?,?)";
					

					int[] counts = jdbcTemplate.batchUpdate(qryDesignRevision,
				            new BatchPreparedStatementSetter() {
								@Override
								public void setValues(PreparedStatement ps, int i) throws SQLException {
									try {	
										obj.setDesign_id(getDesignIdByNo(obj.getDesignRevisions().get(i).getMrvc_drawing_no()));
										SqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(obj);
										String deleteQry = "DELETE from design_revisions where design_id_fk = :design_id";		 
										paramSource1 = new BeanPropertySqlParameterSource(obj);		 
										int count = namedParamJdbcTemplate.update(deleteQry, paramSource1);											
										
										String revision = obj.getDesignRevisions().get(i).getRevision();
										String revision_date = obj.getDesignRevisions().get(i).getRevision_date();
										String drawing_no = obj.getDesignRevisions().get(i).getDrawing_no();
										String correspondence_letter_no = obj.getDesignRevisions().get(i).getCorrespondence_letter_no();
										String upload_file = obj.getDesignRevisions().get(i).getUpload_file();
										String revision_status_fk = obj.getDesignRevisions().get(i).getRevision_status_fk();
										String remarks = obj.getDesignRevisions().get(i).getRemarks();
										String current = obj.getDesignRevisions().get(i).getCurrent();
										String pmisdrawingno = obj.getDesignRevisions().get(i).getMrvc_drawing_no();
										
										
										int k = 1;
										//ps.setString(k++, obj.getDesign_id());
										ps.setString(k++,getDesignIdByNo(pmisdrawingno));
										ps.setString(k++,!StringUtils.isEmpty(revision)?revision:null);
										ps.setString(k++,!StringUtils.isEmpty(revision_date)?revision_date:null);
										ps.setString(k++,!StringUtils.isEmpty(drawing_no)?drawing_no:null);
										ps.setString(k++,!StringUtils.isEmpty(correspondence_letter_no)?correspondence_letter_no:null);
										ps.setString(k++,!StringUtils.isEmpty(upload_file)?upload_file:null);
										ps.setString(k++,!StringUtils.isEmpty(revision_status_fk)?revision_status_fk:null);
										ps.setString(k++,!StringUtils.isEmpty(current)?current:null);
										ps.setString(k++,!StringUtils.isEmpty(remarks)?remarks:null);
									
									} catch (Exception e) {
										
									}
								}
								@Override
								public int getBatchSize() {
									return obj.getDesignRevisions().size();
							}
					  });
					
					
				}					
				
				
			
				 
			}
			count = designsList.size();
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return count;
	}	
	
	private String getDesignId(Design obj){
		Design dObj = null;
		String designId = null;
		try {
			String qry ="select design_id from design where project_id_fk = ? and mrvc_drawing_no = ? " ;
			dObj = (Design)jdbcTemplate.queryForObject(qry, new Object[] {obj.getProject_id_fk(),obj.getMrvc_drawing_no()}, new BeanPropertyRowMapper<Design>(Design.class));
			designId = dObj.getDesign_id();
			return designId;
		}
		catch(Exception e){ 
			designId = null;
			return designId;
		}
	}
	
	private String getDesignIdByNo(String pmisdrawingno) throws Exception
	{
		String design_id=null;
		try {
				String qry ="select design_id from design where design_seq_id=?";
				design_id = (String) jdbcTemplate.queryForObject(qry, new Object[] { pmisdrawingno }, String.class);
			
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return design_id;
	}

	@Override
	public boolean updateDesignStatusBulk(Design obj) throws Exception {
		Connection con = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		PreparedStatement updateRemarksActualDateQryStmt = null;
		boolean flag = false;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String insertQry = "INSERT INTO designdrawingstatusremarks"
					+ "(structure,component,element,activity,scope,target_date,actual_date,remarks,task_code)"
					+ "VALUES"
					+ "(?,?,?,?,?,?,?,?,?)";
			
			String updateQry = "update designdrawingstatusremarks set remarks=?,actual_date=? where task_code=?";
			String updateRemarksActualDateQry = "update designdrawingstatusremarks set remarks='',actual_date='' where task_code=?";
	
			
			insertStmt = con.prepareStatement(insertQry,Statement.RETURN_GENERATED_KEYS);
			updateStmt = con.prepareStatement(updateQry);
			
			 updateRemarksActualDateQryStmt = con.prepareStatement(updateRemarksActualDateQry);
			
			int	arraySize = 0;
			  if( !StringUtils.isEmpty(obj.getActual_dates()) && obj.getActual_dates().length > 0 ||  !StringUtils.isEmpty(obj.getDesignremarks()) && obj.getDesignremarks().length > 0) 
			  {
				 obj.setActual_dates(CommonMethods.replaceEmptyByNullInSringArray(obj.getActual_dates())); 
				 if(arraySize < obj.getActual_dates().length) 
				 { 
					 arraySize= obj.getActual_dates().length; 
				 } 
				 obj.setDesignremarks(CommonMethods.replaceEmptyByNullInSringArray(obj.getDesignremarks())); 
				 if(arraySize < obj.getDesignremarks().length) 
				 { 
					 arraySize= obj.getDesignremarks().length; 
				 }				 
			 }
			
			for (int i = 0; i < arraySize; i++) 
			{				
				if( (obj.getActual_dates()[i]!="" && obj.getActual_dates()[i]!=null && obj.getActual_dates()[i]!="" && !StringUtils.isEmpty(obj.getActual_dates()[i])) || (obj.getDesignremarks()[i]!="" && obj.getDesignremarks()[i]!=null && obj.getDesignremarks()[i]!="" && !StringUtils.isEmpty(obj.getDesignremarks()[i])))
			    {
			            if(getDesignP6ActivitiesData(obj.getTaskcodes()[i])>0)
			            {

			            	
				            	updateStmt.setString(1, obj.getDesignremarks().length>0 ?obj.getDesignremarks()[i]:"");
				            	updateStmt.setString(2, obj.getActual_dates().length>0 ?obj.getActual_dates()[i]:"");
				            	updateStmt.setString(3, obj.getTaskcodes()[i]);
				            	updateStmt.executeUpdate();
			            	
			            }
			            else
			            {
						    insertStmt.setString(1, obj.getStructures()[i]);
						    insertStmt.setString(2, obj.getComponents()[i]);
						    insertStmt.setString(3, obj.getElements()[i]);					    
						    insertStmt.setString(4, obj.getActivities()[i]);
						    insertStmt.setString(5, obj.getScopes()[i]);
						    insertStmt.setString(6, obj.getTarget_dates()[i]);
						    
						    insertStmt.setString(7, obj.getActual_dates().length>0 ?obj.getActual_dates()[i]:"");
						    insertStmt.setString(8, obj.getDesignremarks().length>0 ?obj.getDesignremarks()[i]:"");
						    
						    insertStmt.setString(9, obj.getTaskcodes()[i]);
						    
						    insertStmt.executeUpdate();			            	
			            }
						  
			    }
				else
				{
		            if(getDesignP6ActivitiesData(obj.getTaskcodes()[i])>0)
		            {

		            	updateRemarksActualDateQryStmt.setString(1, obj.getTaskcodes()[i]);
		            	updateRemarksActualDateQryStmt.executeUpdate();
		            	
		            }					
				}
			}

		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, updateStmt, null);
		}	
		return flag;
	}	
	
	private int getDesignP6ActivitiesData(String taskcode) throws Exception
	{
		int cnt=0;
		try {
				String qry ="select distinct(count (*)) as cnt from designdrawingstatusremarks where task_code=?";
				cnt = (int) jdbcTemplate.queryForObject(qry, new Object[] { taskcode }, int.class);
			
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return cnt;
	}
}
