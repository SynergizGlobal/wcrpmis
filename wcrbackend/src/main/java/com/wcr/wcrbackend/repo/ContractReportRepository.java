package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Contract;

@Repository
public class ContractReportRepository implements IContractReportRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Contract> getProjectList() throws Exception{
		List<Contract> objsList = null;
		try {
			String qry = "select project_id,project_name from project order by project_id asc";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Contract> getHODListInContractReport(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select hod_user_id_fk,user_id,user_name,designation,user_type_fk "
					+ "from contract c "
					+ "left join [user] u ON hod_user_id_fk = user_id "
					+ "where hod_user_id_fk IS NOT NULL and user_type_fk='HOD' and hod_user_id_fk <> ''";

			int arrSize = 0;
			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				qry = qry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i =0; i< (length-1); i++) {
						qry = qry + ",?";
						arrSize++;
					}
				}
				
				qry = qry + " ) ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + " and c.contract_id = ?";
				arrSize++;
			}
			
			qry = qry + " group by hod_user_id_fk,user_id,user_name,designation,user_type_fk";
			
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
					"   end asc ";
			
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				int length = obj.getHod_designations().length;
				if(length >= 1) {
					for(int j =0; j<= (length-1); j++) {
						pValues[i++] = obj.getHod_designations()[j];
					}
				}	
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Contract> getContractorsListInContractReport(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select contractor_id_fk,contractor_id,contractor_name "
					+ "from contract c "
					+ "LEFT JOIN contractor ctr ON contractor_id_fk = contractor_id "
					+ "left join [user] u ON hod_user_id_fk = user_id "
					+ "where contractor_id_fk IS NOT NULL and contractor_id_fk <> ''";

			int arrSize = 0;			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				qry = qry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i =0; i< (length-1); i++) {
						qry = qry + ",?";
						arrSize++;
					}
				}
				
				qry = qry + " ) ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + " and c.contract_id = ?";
				arrSize++;
			}
			qry = qry + " group by contractor_id_fk,contractor_id,contractor_name order by c.contractor_id_fk ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) 
			{
				
				int length = obj.getHod_designations().length;
				if(length >= 1) {
					for(int j =0; j<= (length-1); j++) {
						pValues[i++] = obj.getHod_designations()[j];
					}
				}				
				
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Contract> getContractStatusListInContractReport(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select contract_status_fk "
					+ "from contract c "
					+ "LEFT JOIN contractor ctr ON contractor_id_fk = contractor_id "
					+ "left join [user] u ON hod_user_id_fk = user_id "
					+ "where contract_status_fk IS NOT NULL and contract_status_fk <> ''";

			int arrSize = 0;			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				qry = qry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i =0; i< (length-1); i++) {
						qry = qry + ",?";
						arrSize++;
					}
				}
				
				qry = qry + " ) ";
				
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
				qry = qry + " and c.status = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + " and c.contract_id = ?";
				arrSize++;
			}
			qry = qry + " group by c.contract_status_fk "
					+ "   ORDER BY case when contract_status_fk='Commissioned' then 1 " + 
					"   when contract_status_fk='Completed' then 2 " + 
					"   when contract_status_fk='In Progress' then 3 " + 
					"   when contract_status_fk='On Hold' then 4 " + 
					"   when contract_status_fk='Dropped' then 5 " + 
					"   when contract_status_fk='Not Started' then 6 end asc";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				int length = obj.getHod_designations().length;
				if(length >= 1) {
					for(int j =0; j<= (length-1); j++) {
						pValues[i++] = obj.getHod_designations()[j];
					}
				}				
				
				
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
				pValues[i++] = obj.getStatus();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

}
