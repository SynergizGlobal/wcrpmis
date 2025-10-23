package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Design;
@Repository
public class DesignRepository implements IDesignRepo {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
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

}
