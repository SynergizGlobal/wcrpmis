package com.wcr.wcrbackend.repo;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.DBConnectionHandler;

@Repository
public class ContractReportRepository implements IContractReportRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	DataSource dataSource;
	
	//@Autowired
//	DataSourceTransactionManager transactionManager;
	
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
	public List<Contract> getStatusofWorkItems(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select distinct contract_status_fk "
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
			qry = qry + " group by c.contract_status_fk ";
			
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
	
	
	@Override
	public List<Contract> getContractStatusListInContractReport(Contract obj) throws Exception {
		/*List<Contract> objsList = null;
		try {
			String qry ="select general_status as contract_status_fk from general_status";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;*/
		
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
	
	@Override
	public List<Contract> getContractListInContractReport(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select contract_id,contract_name,contract_short_name "
					+ "from contract c "
					+ "left join [user] u ON hod_user_id_fk = user_id "
					+ "where contract_id IS NOT NULL and contract_id <> ''";

			int arrSize = 0;
			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) 
			{
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
			qry = qry + " group by contract_id,contract_name,contract_short_name order by contract_id ";
			
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
	public Map<String,List<Contract>> getContractsListForReport(Contract obj) throws Exception {
		Map<String,List<Contract>> mapObjsList = new LinkedHashMap<String, List<Contract>>();
		List<Contract> objsList = null;
		NumberFormat numberFormatter = new DecimalFormat("#0.00");
		try {
			
			String hodQry ="select u.designation as hod_designation,c.hod_user_id_fk,FORMAT(doc,'dd-MMM-yy') AS doc,doc as doc_date, "
					+ "(select revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id  order by revised_doc offset 0 rows  fetch next 1 rows only) as  revised_doc_temp "
					+ "from contract c "
					+ "left join contractor cr on c.contractor_id_fk = cr.contractor_id "  
					+ "left join project p on c.project_id_fk = p.project_id "  
					+ "left join [user] u on c.hod_user_id_fk = u.user_id "
					+ "left join [user] us on c.dy_hod_user_id_fk = us.user_id "
					+ "left join department dt on u.department_fk = dt.department "
					+ "where c.contract_id is not null ";
			
			int arrSize = 0;
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				hodQry = hodQry + " and c.contract_id = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				hodQry = hodQry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i1 =0; i1< (length-1); i1++) {
						hodQry = hodQry + ",?";
						arrSize++;
					}
				}
				
				hodQry = hodQry + " ) ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				hodQry = hodQry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				hodQry = hodQry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
				hodQry = hodQry + " having ((revised_doc_temp is not null and DATE(revised_doc_temp) <= DATE(?) ) or (revised_doc_temp is null and DATE(doc_date) <= DATE(?)))";
				arrSize++;
				arrSize++;
			}
			
			hodQry = hodQry + " group by c.hod_user_id_fk,u.designation,doc,contract_id ";
			hodQry = hodQry + " order by case when u.designation='ED Civil' then 1  " + 
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
				pValues[i++] = obj.getDate();
				pValues[i++] = obj.getDate();
			}
			
			List<Contract> hodObjsList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));	
			for (Contract hodObj : hodObjsList) {
				
				obj.setHod_designation(hodObj.getHod_designation());
				
				String qry ="select dt.department_name,dt.contract_id_code,c.project_id_fk,p.project_name,u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name,contract_type_fk,c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name,dt.department as department_fk,c.hod_user_id_fk,c.dy_hod_user_id_fk,"  
						+"scope_of_contract,cast(estimated_cost as CHAR) as estimated_cost,FORMAT(date_of_start,'dd-MM-yyyy') AS date_of_start,FORMAT(doc,'dd-MMM-yy') AS doc,doc as doc_date,cast((ISNULL(awarded_cost,0)*ISNULL(awarded_cost_units,0)/10000000) as decimal(10,2)) as awarded_cost,loa_letter_number,FORMAT(loa_date,'dd-MMM-yy') AS loa_date,ca_no,FORMAT(ca_date,'dd-MM-yyyy') AS ca_date,FORMAT(actual_completion_date,'dd-MMM-yy') AS actual_completion_date,"
						+"FORMAT(contract_closure_date,'dd-MM-yyyy') AS contract_closure_date,FORMAT(completion_certificate_release,'dd-MM-yyyy') AS completion_certificate_release,FORMAT(final_takeover,'dd-MM-yyyy') AS final_takeover,FORMAT(final_bill_release,'dd-MM-yyyy') AS final_bill_release,FORMAT(defect_liability_period,'dd-MM-yyyy') AS defect_liability_period,cast(completed_cost as CHAR) as completed_cost,"
						+"FORMAT(retention_money_release,'dd-MM-yyyy') AS retention_money_release,FORMAT(pbg_release,'dd-MM-yyyy') AS pbg_release,contract_status_fk,bg_required,insurance_required, "
						/*+ "(select revision_number from contract_revision where contract_revision_id = (select max(contract_revision_id) from contract_revision where contract_id_fk = contract_id)) as  revision_number," 
						+ "(select revision_date from contract_revision where contract_revision_id = (select max(contract_revision_id) from contract_revision where contract_id_fk = contract_id)) as  revision_date," 
						+ "(select cast(revised_amount as CHAR) as revised_amount from contract_revision where contract_revision_id = (select max(contract_revision_id) from contract_revision where contract_id_fk = contract_id)) as  revised_amount," 
						+ "(select FORMAT(revised_doc,'dd-MMM-yy') AS revised_doc from contract_revision where contract_revision_id = (select max(contract_revision_id) from contract_revision where contract_id_fk = contract_id)) as  revised_doc," 
						+ "(select revised_doc from contract_revision where contract_revision_id = (select max(contract_revision_id) from contract_revision where contract_id_fk = contract_id)) as  revised_doc_temp," 
						+ "(select remarks from contract_revision where contract_revision_id = (select max(contract_revision_id) from contract_revision where contract_id_fk = contract_id)) as  revision_remarks " */
						+ "(select revision_number from contract_revision where revision_number is not null and action = 'Yes' and contract_id_fk = c.contract_id order by revision_number offset 0 rows  fetch next 1 rows only) as  revision_number," 
						/*						+ "(select revision_date from contract_revision where revision_date is not null and action = 'Yes' and contract_id_fk = c.contract_id offset 0 rows  fetch next 1 rows only) as  revision_date," 
						*/						+ "(select cast((ISNULL(revised_amount,0)/10000000) as CHAR) as revised_amount from contract_revision where revised_amount is not null and revision_amounts_status = 'Yes' and contract_id_fk = c.contract_id) as  revised_amount,"
						+ "(select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = c.contract_id order by FORMAT(MAX(revised_doc),'dd-MMM-yy') offset 0 rows  fetch next 1 rows only) as  revised_doc," 
						+ "(select revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = c.contract_id order by revised_doc offset 0 rows  fetch next 1 rows only) as  revised_doc_temp," 
						+ "(select remarks from contract_revision where action = 'Yes' and contract_id_fk = c.contract_id order by remarks offset 0 rows  fetch next 1 rows only) as revision_remarks,"
						
						+ "(select cast((ISNULL(SUM(gross_work_done),0)/10000000) as CHAR) AS gross_work_done from expenditure where contract_id_fk = c.contract_id) as cumulative_expenditure, "
						+ "(select STRING_AGG( FORMAT(i1.valid_upto,'dd-MMM-yy') , '\n<space>' )  from insurance i1 where i1.contract_id_fk = c.contract_id  and (released_fk is null or released_fk<>'Yes')) as insurance_valid_till, "
						+ "(select STRING_AGG( FORMAT(valid_upto,'dd-MMM-yy') , '\n<space>' ) from bank_guarantee bg where bg.contract_id_fk = c.contract_id  and bg_type_fk is not null and release_date is null) as pbg_valid_till, "
						+ "(SELECT cast(sum(contract_per)*100 as decimal(10,2)) FROM activities_scurve where contract_id = c.contract_id and category = 'Actual' ) as PhysicalProgress,"
						+ "FORMAT(c.target_doc,'dd-MMM-yy') AS target_doc,status  "
						+ " from contract c "  
						+ "left join contractor cr on c.contractor_id_fk = cr.contractor_id "  
						+ "left join project p on c.project_id_fk = p.project_id "  
						+ "left join [user] u on c.hod_user_id_fk = u.user_id "
						+ "left join [user] us on c.dy_hod_user_id_fk = us.user_id "
						+ "left join department dt on c.contract_department = dt.department "
						+ "where contract_id is not null and status is not null ";
				
				arrSize = 0;			
	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					qry = qry + " and c.contract_id = ? ";
					arrSize++;
				}
				if(!StringUtils.isEmpty(hodObj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					qry = qry + " and u.designation = ? ";
					arrSize++;
				}
				else 
				{
					qry = qry + " and c.hod_user_id_fk is null ";
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					qry = qry + " and c.contractor_id_fk = ?";
					arrSize++;
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
					qry = qry + " and c.status = ?";
					arrSize++;
				}				
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
					qry = qry + " and c.contract_status_fk = ?";
					arrSize++;
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					qry = qry + " having ((revised_doc_temp is not null and DATE(revised_doc_temp) <= DATE(?) ) or (revised_doc_temp is null and DATE(doc_date) <= DATE(?))) order by c.status ";
					arrSize++;
					arrSize++;
				}

				
				pValues = new Object[arrSize];
				i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					pValues[i++] = obj.getContract_id();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					pValues[i++] = hodObj.getHod_designation();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
					pValues[i++] = obj.getStatus();
				}				
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
					pValues[i++] = obj.getContract_status_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					pValues[i++] = obj.getDate();
					pValues[i++] = obj.getDate();
				}
					
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
				
				for (Contract contract : objsList) {
					double awarded_cost = 0,revised_contract_cost = 0,cumulative_expenditure = 0;
					if(!StringUtils.isEmpty(contract.getAwarded_cost())) {
						awarded_cost = Double.parseDouble(contract.getAwarded_cost());
					}
					if(!StringUtils.isEmpty(contract.getRevised_amount())) {
						revised_contract_cost = Double.parseDouble(contract.getRevised_amount());
					}
					if(!StringUtils.isEmpty(contract.getCumulative_expenditure())) {
						cumulative_expenditure = Double.parseDouble(contract.getCumulative_expenditure());
					}
					if(awarded_cost == 0) {
						contract.setAwarded_cost("");
					}else {
						contract.setAwarded_cost(numberFormatter.format(awarded_cost));
					}
					if(revised_contract_cost == 0) {
						contract.setRevised_amount("");
					}else {
						contract.setRevised_amount(numberFormatter.format(revised_contract_cost));
					}
					if(cumulative_expenditure == 0) {
						contract.setCumulative_expenditure("");
					}else {
						contract.setCumulative_expenditure(numberFormatter.format(cumulative_expenditure));
					}
					/*contract.setRevised_amount(numberFormatter.format(revised_contract_cost));
					contract.setRevised_amount(numberFormatter.format(revised_contract_cost));
					contract.setCumulative_expenditure(numberFormatter.format(cumulative_expenditure));*/
				}
				String hod = hodObj.getHod_designation();
				if(StringUtils.isEmpty(hod)) {
					hod = "Not assigned to this contract";
				}
				mapObjsList.put(hod, objsList);
			}	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return mapObjsList;
	}

	
	@Override
	public Map<String,List<Contract>> getContractsBankGuaranteeForReport(Contract obj) throws Exception {
		Map<String,List<Contract>> objsList = new LinkedHashMap<String, List<Contract>>();
		try {
			
			String hodQry ="select u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name " + 
					"from bank_guarantee bg " +
					"left join contract c on bg.contract_id_fk = c.contract_id " +
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
					"left join project p on c.project_id_fk = p.project_id " + 
					"left join [user] u on c.hod_user_id_fk = u.user_id "+
					"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
					+"left join department dt on c.contract_department = dt.department "
					+"where contract_id is not null and bg.release_date is null ";
			
			int arrSize = 0;			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				hodQry = hodQry + " and c.contract_id = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				hodQry = hodQry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i1 =0; i1< (length-1); i1++) {
						hodQry = hodQry + ",?";
						arrSize++;
					}
				}
				
				hodQry = hodQry + " ) ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				hodQry = hodQry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				hodQry = hodQry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			/*
			 * if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) { hodQry
			 * = hodQry + " and bg.valid_upto <= ?"; arrSize++; }
			 */
			
			hodQry = hodQry + " GROUP BY c.hod_user_id_fk,us.designation,u.designation,u.user_name ";
			hodQry = hodQry + " ORDER BY case when u.designation='ED Civil' then 1  " + 
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
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
			/*
			 * if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
			 * pValues[i++] = obj.getDate(); }
			 */
				
			List<Contract> hodList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			for (Contract hodObj : hodList) {			
				String qry ="select DISTINCT contract_short_name,contractor_name,STRING_AGG( bg_number , '\n<space>' ) as bg_number,STRING_AGG( bg_value , '\n<space>' ) as bg_value,STRING_AGG(bg_valid_upto , '\n<space>' ) as bg_valid_upto,STRING_AGG( ContractAlertRemarks , '\n' ) as ContractAlertRemarks from(select distinct bank_guarantee_id,bg.contract_id_fk,bg_type_fk,issuing_bank,bg_number,cast((ISNULL(bg_value,0)*ISNULL(bg_value_units,0)/100000) as decimal(10,2)) as bg_value,FORMAT(valid_upto,'dd-MMM-yy') AS bg_valid_upto,FORMAT(bg_date,'dd-MMM-yy') AS bg_date,FORMAT(release_date,'dd-MMM-yy') AS release_date,"
						+ "dt.department_name,dt.contract_id_code,c.project_id_fk,p.project_name,u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name,c.project_id_fk,contract_type_fk,c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name,dt.department as department_fk,c.hod_user_id_fk,c.dy_hod_user_id_fk," + 
						"scope_of_contract,cast(estimated_cost as CHAR) as estimated_cost,FORMAT(date_of_start,'dd-MMM-yy') AS date_of_start,FORMAT(doc,'dd-MMM-yy') AS doc,cast(awarded_cost as CHAR) as awarded_cost,loa_letter_number,FORMAT(loa_date,'dd-MMM-yy') AS loa_date,ca_no,FORMAT(ca_date,'dd-MMM-yy') AS ca_date,FORMAT(actual_completion_date,'dd-MM-yyyy') AS actual_completion_date,"
						+"FORMAT(contract_closure_date,'dd-MMM-yy') AS contract_closure_date,FORMAT(completion_certificate_release,'dd-MM-yyyy') AS completion_certificate_release,FORMAT(final_takeover,'dd-MMM-yy') AS final_takeover,FORMAT(final_bill_release,'dd-MMM-yy') AS final_bill_release,FORMAT(defect_liability_period,'dd-MMM-yy') AS defect_liability_period,cast(completed_cost as CHAR) as completed_cost,"
						+"FORMAT(retention_money_release,'dd-MMM-yy') AS retention_money_release,FORMAT(pbg_release,'dd-MMM-yy') AS pbg_release,contract_status_fk,bg_required,insurance_required, " + 
						"(select DISTINCT remarks from alerts where alert_status='Active' and alert_type_fk = 'Bank Guarantee' and contract_id = c.contract_id and alert_value = (case when (bg.bg_type_fk is not null and bg.bg_number is not null) then CONCAT(bg.bg_type_fk,' ',bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
								+ "when (bg.bg_type_fk is null and bg.bg_number is not null) then CONCAT(bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
								+ "when (bg.bg_type_fk is not null and bg.bg_number is null) then CONCAT(bg.bg_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
								+ "else CONCAT('Bank guarantee valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) "
		+ "AND created_date = (select DISTINCT max(created_date) from alerts where alert_status='Active' and alert_type_fk = 'Bank Guarantee' and contract_id = c.contract_id and alert_value = (case when (bg.bg_type_fk is not null and bg.bg_number is not null) then CONCAT(bg.bg_type_fk,' ',bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
								+ "when (bg.bg_type_fk is null and bg.bg_number is not null) then CONCAT(bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
								+ "when (bg.bg_type_fk is not null and bg.bg_number is null) then CONCAT(bg.bg_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
								+ "else CONCAT('Bank guarantee valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ))) as ContractAlertRemarks "+						
						
						"from bank_guarantee bg " +
						"left join contract c on bg.contract_id_fk = c.contract_id " +
						"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
						"left join project p on c.project_id_fk = p.project_id " + 
						"left join [user] u on c.hod_user_id_fk = u.user_id "+
						"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
						+"left join department dt on c.contract_department = dt.department "
						+"where contract_id is not null and bg.release_date is null ";
				
				arrSize = 0;			
	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					qry = qry + " and c.contract_id = ? ";
					arrSize++;
				}
				if(!StringUtils.isEmpty(hodObj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					qry = qry + " and u.designation = ? ";
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
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					qry = qry + " and bg.valid_upto <= ?";
					arrSize++;
				}
				pValues = new Object[arrSize];
				i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					pValues[i++] = obj.getContract_id();
				}
				if(!StringUtils.isEmpty(hodObj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					pValues[i++] = hodObj.getHod_designation();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
					pValues[i++] = obj.getContract_status_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					pValues[i++] = obj.getDate();
				}
				
				qry = qry + " ) as a ";
				
				if(obj.getDate()!=null && obj.getDate()!="")
				{
					qry = qry +" where 1=(case when (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"') then 0 else 1 end) ";
				}		
				
				qry = qry + " GROUP BY contractor_name,contract_short_name";
					
				List<Contract> bgList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
				
				objsList.put(hodObj.getHod_designation(), bgList);
			}	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public Map<String,List<Contract>> getContractsDocBGInsuranceForReport(Contract obj) throws Exception {
		Map<String,List<Contract>> objsList = new LinkedHashMap<String, List<Contract>>();
		try {
			
			String hodQry ="select u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name "
					+"from contract c " + 
					"left join bank_guarantee bg on bg.contract_id_fk = c.contract_id " +
					"left join insurance i on i.contract_id_fk = c.contract_id " +
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
					"left join project p on c.project_id_fk = p.project_id " + 
					"left join [user] u on c.hod_user_id_fk = u.user_id "+
					"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
					+"left join department dt on c.contract_department = dt.department "
					+"where contract_id is not null and (i.released_fk <> 'Yes' or i.released_fk is null) ";
			
			int arrSize = 0;			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && obj.getDepartment_fk().equals("'Elec','S&T'")) {
				hodQry = hodQry + " and u.department_fk in (?,?)";
				arrSize++;
				arrSize++;
			}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())){
				hodQry = hodQry + " and u.department_fk = ?";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				hodQry = hodQry + " and c.contract_id = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				hodQry = hodQry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i1 =0; i1< (length-1); i1++) {
						hodQry = hodQry + ",?";
						arrSize++;
					}
				}
				
				hodQry = hodQry + " ) ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				hodQry = hodQry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				hodQry = hodQry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
				hodQry = hodQry + " and (";
				for(int i1=0; i1<3; i1++) 
				{
					if(i1==0)
					{
						hodQry = hodQry + " c.doc<= ? ";
					}
					if(i1==1)
					{
						hodQry = hodQry + " or bg.valid_upto <= ? ";
					}
					if(i1==2)
					{
						hodQry = hodQry + " or i.valid_upto <= ? ";
					}					
					arrSize++;
				}
				hodQry = hodQry + " ) ";
			}
			
			hodQry = hodQry + " GROUP BY u.designation,us.designation,u.user_name";
			hodQry = hodQry + " order by case when u.designation='ED Civil' then 1  " + 
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && obj.getDepartment_fk().equals("'Elec','S&T'")) {
				pValues[i++] = "Elec";
				pValues[i++] = "S&T";
			}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) 
			{
				for(int j=0; j<3; j++) 
				{
					pValues[i++] = obj.getDate();
				}
			}
			
			List<Contract> hodList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			for (Contract hodObj : hodList) {		
				
				String conCatBGQry = "(select STRING_AGG( FORMAT(valid_upto,'dd-MMM-yy') , '\n' ) " +
	                     "from bank_guarantee bg " +
	                     "where bg.contract_id_fk = c.contract_id " +
	                     "and bg_type_fk is not null " +
	                     "and release_date is null )";				
				String conCatQry="(select STRING_AGG( FORMAT(i1.valid_upto,'dd-MMM-yy') , '\n' )  from insurance i1 where i1.contract_id_fk = c.contract_id  and (released_fk is null or released_fk<>'Yes') )";
				
				String conCatDocQry="case when (select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id order by FORMAT(MAX(revised_doc),'dd-MMM-yy') offset 0 rows  fetch next 1 rows only) is not null then (select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id order by FORMAT(MAX(revised_doc),'dd-MMM-yy') offset 0 rows  fetch next 1 rows only) else FORMAT(doc,'dd-MMM-yy') end ";
				

				String qry ="select contract_short_name,contractor_name,STRING_AGG( doc , '\n' ) as doc,STRING_AGG( bg_valid_upto , '\n' ) as bg_valid_upto,STRING_AGG( insurance_valid_upto , '\n' ) as insurance_valid_upto,STRING_AGG( ContractAlertRemarks , '\n' ) as ContractAlertRemarks from (select distinct "+conCatQry+" AS insurance_valid_upto,"
						+ "c.contract_short_name,cr.contractor_name," + 
						conCatDocQry+" AS doc,"
						+conCatBGQry+" AS bg_valid_upto, "
						
						+"STRING_AGG( CONCAT(replace(replace((coalesce((select CONCAT('DOC-',coalesce(remarks,'NO Data')) from alerts where alert_status='Active' and alert_type_fk = 'Contract Period' and contract_id = c.contract_id and alert_value ="

					+ "(case when (cr1.action = 'Yes' and cr1.revised_doc is not null) then (CONCAT('Date of Completion : ',FORMAT(cr1.revised_doc,'dd-MMM-yy') )) " 
					+ "when doc is not null then CONCAT('Date of Completion : ',FORMAT(doc,'dd-MMM-yy') ) else '' end )),'')),'DOC-NO Data',''),'DOC-.',''),"+			
						
					"replace((coalesce((select distinct CONCAT('\nBG-',coalesce(remarks,'NO Data')) from alerts where alert_status='Active' and alert_type_fk = 'Bank Guarantee' and contract_id = c.contract_id and alert_value = (case when (bg.bg_type_fk is not null and bg.bg_number is not null) then CONCAT(bg.bg_type_fk,' ',bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (bg.bg_type_fk is null and bg.bg_number is not null) then CONCAT(bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (bg.bg_type_fk is not null and bg.bg_number is null) then CONCAT(bg.bg_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
					+ "else CONCAT('Bank guarantee valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) "
+ "AND created_date = (select max(created_date) from alerts where alert_status='Active' and alert_type_fk = 'Bank Guarantee' and contract_id = c.contract_id and alert_value = (case when (bg.bg_type_fk is not null and bg.bg_number is not null) then CONCAT(bg.bg_type_fk,' ',bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (bg.bg_type_fk is null and bg.bg_number is not null) then CONCAT(bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (bg.bg_type_fk is not null and bg.bg_number is null) then CONCAT(bg.bg_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
					+ "else CONCAT('Bank guarantee valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) order by max(created_date) offset 0 rows  fetch next 1 rows only) order by max(created_date) offset 0 rows  fetch next 1 rows only),'')),'\nBG-NO Data','')," +
					
					"replace((coalesce((select CONCAT('Insurance-',coalesce(remarks,'NO Data')) from alerts where alert_status='Active' and alert_type_fk = 'Insurance' and contract_id = c.contract_id and alert_value = (case when (i.insurance_type_fk is not null and i.insurance_number is not null) then CONCAT(i.insurance_type_fk,' ',i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (i.insurance_type_fk is null and i.insurance_number is not null) then CONCAT(i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (i.insurance_type_fk is not null and i.insurance_number is null) then CONCAT(i.insurance_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
					+ "else CONCAT('Insurance valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) "
+ "AND created_date = (select max(created_date) from alerts where alert_status='Active' and alert_type_fk = 'Insurance' and contract_id = c.contract_id  and alert_value = (case when (i.insurance_type_fk is not null and i.insurance_number is not null) then CONCAT(i.insurance_type_fk,' ',i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (i.insurance_type_fk is null and i.insurance_number is not null) then CONCAT(i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (i.insurance_type_fk is not null and i.insurance_number is null) then CONCAT(i.insurance_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
					+ "else CONCAT('Insurance valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) order by max(created_date) offset 0 rows  fetch next 1 rows only) order by max(created_date) offset 0 rows  fetch next 1 rows only),'')),'Insurance-NO Data',''))) AS ContractAlertRemarks "
					
					
						
						
						+"from contract c " + 
						"left join bank_guarantee bg on bg.contract_id_fk = c.contract_id " +
						"left join insurance i on i.contract_id_fk = c.contract_id " +
						"LEFT JOIN contract_revision cr1 on cr1.contract_id_fk = c.contract_id and cr1.action = 'Yes' and cr1.revised_doc is not null "+
						"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
						"left join project p on c.project_id_fk = p.project_id " + 
						"left join [user] u on c.hod_user_id_fk = u.user_id "+
						"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
						+"left join department dt on c.contract_department = dt.department "
						+"where contract_id is not null ";
				
				arrSize = 0;			
	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && obj.getDepartment_fk().equals("'Elec','S&T'")) {
					qry = qry + " and u.department_fk in (?,?)";
					arrSize++;
					arrSize++;
				}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())){
					qry = qry + " and u.department_fk = ?";
					arrSize++;
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					qry = qry + " and c.contract_id = ? ";
					arrSize++;
				}
				if(!StringUtils.isEmpty(hodObj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					qry = qry + " and u.designation = ? ";
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
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					qry = qry + " and (";
					for(int i1=0; i1<3; i1++) 
					{
						if(i1==0)
						{
							qry = qry + " c.doc<= ? ";
						}
						if(i1==1)
						{
							qry = qry + " or bg.valid_upto <= ? ";
						}
						if(i1==2)
						{
							qry = qry + " or i.valid_upto <= ? ";
						}					
						arrSize++;
					}
					qry = qry + " )  ";
				}
				pValues = new Object[arrSize];
				i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && obj.getDepartment_fk().equals("'Elec','S&T'")) {
					pValues[i++] = "Elec";
					pValues[i++] = "S&T";
				}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					pValues[i++] = obj.getContract_id();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
							pValues[i++] = hodObj.getHod_designation();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
					pValues[i++] = obj.getContract_status_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) 
				{
					for(int j=0; j<3; j++) 
					{
						pValues[i++] = obj.getDate();
					}
				}
				qry=qry+" group by insurance_valid_upto,c.contract_short_name,cr.contractor_name,doc,revised_doc,bg_valid_upto ) as a ";
				
				if(obj.getDate()!=null && obj.getDate()!="")
				{
					qry=qry+"where 1=(case WHEN (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and LEN(bg_valid_upto)<=10 and insurance_valid_upto is null and doc is null) then 0 when (doc is null and bg_valid_upto is null and insurance_valid_upto is null) then 0 when (STR_TO_DATE(doc,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"') then 0  WHEN (STR_TO_DATE(doc,'dd-MM-yyyy')>'"+obj.getDate()+"' and insurance_valid_upto is null and bg_valid_upto is null) then 0  WHEN (STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and doc is null and bg_valid_upto is null) then 0 WHEN (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and insurance_valid_upto is null and doc is null) then 0 when (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(doc,'dd-MM-yyyy')>'"+obj.getDate()+"' and insurance_valid_upto is null) then 0 when (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and doc is null) then 0 when (STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(doc,'dd-MM-yyyy')>'"+obj.getDate()+"' and bg_valid_upto is null) then 0 else 1 end)";
				}
				
				qry = qry + " GROUP BY contractor_name,contract_short_name";
				
				List<Contract> insuranceList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
				objsList.put(hodObj.getHod_designation(), insuranceList);
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public Map<String,List<Contract>> getContractsDocBGInsuranceForAutoEmailReport(Contract obj) throws Exception {
		Map<String,List<Contract>> objsList = new LinkedHashMap<String, List<Contract>>();
		try {
			
			String hodQry ="select u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name "
					+"from contract c " + 
					"left join bank_guarantee bg on bg.contract_id_fk = c.contract_id " +
					"left join insurance i on i.contract_id_fk = c.contract_id " +
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
					"left join project p on c.project_id_fk = p.project_id " + 
					"left join [user] u on c.hod_user_id_fk = u.user_id "+
					"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
					+"left join department dt on c.contract_department = dt.department "
					+"where contract_id is not null and (i.released_fk <> 'Yes' or i.released_fk is null) and c.contract_status_fk in ('In Progress') ";
			
			int arrSize = 0;			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && obj.getDepartment_fk().equals("'Elec','S&T'")) {
				hodQry = hodQry + " and u.department_fk in (?,?)";
				arrSize++;
				arrSize++;
			}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())){
				hodQry = hodQry + " and u.department_fk = ?";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				hodQry = hodQry + " and c.contract_id = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				hodQry = hodQry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i1 =0; i1< (length-1); i1++) {
						hodQry = hodQry + ",?";
						arrSize++;
					}
				}
				
				hodQry = hodQry + " ) ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				hodQry = hodQry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				hodQry = hodQry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
				hodQry = hodQry + " and (";
				for(int i1=0; i1<3; i1++) 
				{
					if(i1==0)
					{
						hodQry = hodQry + " c.doc<= ? ";
					}
					if(i1==1)
					{
						hodQry = hodQry + " or bg.valid_upto <= ? ";
					}
					if(i1==2)
					{
						hodQry = hodQry + " or i.valid_upto <= ? ";
					}					
					arrSize++;
				}
				hodQry = hodQry + " ) ";
			}
			
			hodQry = hodQry + " GROUP BY u.designation,us.designation,u.user_name";
			hodQry = hodQry + " ORDER BY case when u.designation='ED Civil' then 1  " + 
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && obj.getDepartment_fk().equals("'Elec','S&T'")) {
				pValues[i++] = "Elec";
				pValues[i++] = "S&T";
			}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) 
			{
				for(int j=0; j<3; j++) 
				{
					pValues[i++] = obj.getDate();
				}
			}
			
			List<Contract> hodList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			for (Contract hodObj : hodList) {		
				
				String conCatBGQry="(select STRING_AGG( FORMAT(valid_upto,'dd-MMM-yy') , '\n' ) from bank_guarantee bg where bg.contract_id_fk = c.contract_id  and bg_type_fk is not null and release_date is null )";
				
				String conCatQry="(select STRING_AGG( FORMAT(i1.valid_upto,'dd-MMM-yy')  , '\n' )  from insurance i1 where i1.contract_id_fk = c.contract_id  and (released_fk is null or released_fk<>'Yes') )";
				
				String conCatDocQry="case when (select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id order by FORMAT(MAX(revised_doc),'dd-MMM-yy') offset 0 rows  fetch next 1 rows only) is not null then (select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id order by FORMAT(MAX(revised_doc),'dd-MMM-yy') order by FORMAT(MAX(revised_doc) offset 0 rows  fetch next 1 rows only) else FORMAT(doc,'dd-MMM-yy') end ";
				

				String qry ="select contract_short_name,contractor_name,STRING_AGG( doc , '\n' ) as doc,STRING_AGG( bg_valid_upto , '\n' ) as bg_valid_upto,STRING_AGG( insurance_valid_upto , '\n' ) as insurance_valid_upto,STRING_AGG( ContractAlertRemarks , '\n' ) as ContractAlertRemarks from (select distinct "+conCatQry+" AS insurance_valid_upto,"
						+ "c.contract_short_name,cr.contractor_name," + 
						conCatDocQry+" AS doc,"
						+conCatBGQry+" AS bg_valid_upto, "
						
						+"STRING_AGG( CONCAT(replace(replace((coalesce((select CONCAT('DOC-',coalesce(remarks,'NO Data')) from alerts where alert_status='Active' and alert_type_fk = 'Contract Period' and contract_id = c.contract_id and alert_value ="

					+ "(case when (cr1.action = 'Yes' and cr1.revised_doc is not null) then (CONCAT('Date of Completion : ',FORMAT(cr1.revised_doc,'dd-MMM-yy') )) " 
					+ "when doc is not null then CONCAT('Date of Completion : ',FORMAT(doc,'dd-MMM-yy') ) else '' end )),'')),'DOC-NO Data',''),'DOC-.',''),"+			
						
					"replace((coalesce((select distinct CONCAT('\nBG-',coalesce(remarks,'NO Data')) from alerts where alert_status='Active' and alert_type_fk = 'Bank Guarantee' and contract_id = c.contract_id and alert_value = (case when (bg.bg_type_fk is not null and bg.bg_number is not null) then CONCAT(bg.bg_type_fk,' ',bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (bg.bg_type_fk is null and bg.bg_number is not null) then CONCAT(bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (bg.bg_type_fk is not null and bg.bg_number is null) then CONCAT(bg.bg_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
					+ "else CONCAT('Bank guarantee valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) "
+ "AND created_date = (select max(created_date) from alerts where alert_status='Active' and alert_type_fk = 'Bank Guarantee' and contract_id = c.contract_id and alert_value = (case when (bg.bg_type_fk is not null and bg.bg_number is not null) then CONCAT(bg.bg_type_fk,' ',bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (bg.bg_type_fk is null and bg.bg_number is not null) then CONCAT(bg.bg_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (bg.bg_type_fk is not null and bg.bg_number is null) then CONCAT(bg.bg_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
					+ "else CONCAT('Bank guarantee valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) order by max(created_date) offset 0 rows  fetch next 1 rows only) offset 0 rows  fetch next 1 rows only),'')),'\nBG-NO Data','')," +
					
					"replace((coalesce((select CONCAT('Insurance-',coalesce(remarks,'NO Data')) from alerts where alert_status='Active' and alert_type_fk = 'Insurance' and contract_id = c.contract_id and alert_value = (case when (i.insurance_type_fk is not null and i.insurance_number is not null) then CONCAT(i.insurance_type_fk,' ',i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (i.insurance_type_fk is null and i.insurance_number is not null) then CONCAT(i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (i.insurance_type_fk is not null and i.insurance_number is null) then CONCAT(i.insurance_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
					+ "else CONCAT('Insurance valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) "
+ "AND created_date = (select max(created_date) from alerts where alert_status='Active' and alert_type_fk = 'Insurance' and contract_id = c.contract_id  and alert_value = (case when (i.insurance_type_fk is not null and i.insurance_number is not null) then CONCAT(i.insurance_type_fk,' ',i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (i.insurance_type_fk is null and i.insurance_number is not null) then CONCAT(i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
					+ "when (i.insurance_type_fk is not null and i.insurance_number is null) then CONCAT(i.insurance_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
					+ "else CONCAT('Insurance valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) order by max(created_date) offset 0 rows  fetch next 1 rows only) order by max(created_date) offset 0 rows  fetch next 1 rows only),'')),'Insurance-NO Data','')),',') AS ContractAlertRemarks "
					
					
						
						
						+"from contract c " + 
						"left join bank_guarantee bg on bg.contract_id_fk = c.contract_id " +
						"left join insurance i on i.contract_id_fk = c.contract_id " +
						"LEFT JOIN contract_revision cr1 on cr1.contract_id_fk = c.contract_id and cr1.action = 'Yes' and cr1.revised_doc is not null "+
						"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
						"left join project p on c.project_id_fk = p.project_id " + 
						"left join [user] u on c.hod_user_id_fk = u.user_id "+
						"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
						+"left join department dt on c.contract_department = dt.department "
						+"where contract_id is not null and c.contract_status_fk in ('In Progress') ";
				
				arrSize = 0;			
	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && obj.getDepartment_fk().equals("'Elec','S&T'")) {
					qry = qry + " and u.department_fk in (?,?)";
					arrSize++;
					arrSize++;
				}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())){
					qry = qry + " and u.department_fk = ?";
					arrSize++;
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					qry = qry + " and c.contract_id = ? ";
					arrSize++;
				}
				if(!StringUtils.isEmpty(hodObj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					qry = qry + " and u.designation = ? ";
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
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					qry = qry + " and (";
					for(int i1=0; i1<3; i1++) 
					{
						if(i1==0)
						{
							qry = qry + " c.doc<= ? ";
						}
						if(i1==1)
						{
							qry = qry + " or bg.valid_upto <= ? ";
						}
						if(i1==2)
						{
							qry = qry + " or i.valid_upto <= ? ";
						}					
						arrSize++;
					}
					qry = qry + " )  ";
				}
				pValues = new Object[arrSize];
				i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk()) && obj.getDepartment_fk().equals("'Elec','S&T'")) {
					pValues[i++] = "Elec";
					pValues[i++] = "S&T";
				}else if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					pValues[i++] = obj.getContract_id();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
							pValues[i++] = hodObj.getHod_designation();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
					pValues[i++] = obj.getContract_status_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) 
				{
					for(int j=0; j<3; j++) 
					{
						pValues[i++] = obj.getDate();
					}
				}
				qry=qry+" group by insurance_valid_upto,c.contract_short_name,cr.contractor_name,doc,revised_doc,bg_valid_upto ) as a ";
				
				if(obj.getDate()!=null && obj.getDate()!="")
				{
					qry=qry+"where 1=(case WHEN (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and LEN(bg_valid_upto)<=10 and insurance_valid_upto is null and doc is null) then 0 when (doc is null and bg_valid_upto is null and insurance_valid_upto is null) then 0 when (STR_TO_DATE(doc,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"') then 0  WHEN (STR_TO_DATE(doc,'dd-MM-yyyy')>'"+obj.getDate()+"' and insurance_valid_upto is null and bg_valid_upto is null) then 0  WHEN (STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and doc is null and bg_valid_upto is null) then 0 WHEN (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and insurance_valid_upto is null and doc is null) then 0 when (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(doc,'dd-MM-yyyy')>'"+obj.getDate()+"' and insurance_valid_upto is null) then 0 when (STR_TO_DATE(bg_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and doc is null) then 0 when (STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"' and STR_TO_DATE(doc,'dd-MM-yyyy')>'"+obj.getDate()+"' and bg_valid_upto is null) then 0 else 1 end)";
				}
				
				qry = qry + " GROUP BY contractor_name,contract_short_name";
				
				List<Contract> insuranceList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
				objsList.put(hodObj.getHod_designation(), insuranceList);
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}


	@Override
	public Map<String,List<Contract>> getContractsDocReport(Contract obj) throws Exception {
		Map<String,List<Contract>> objsList = new LinkedHashMap<String, List<Contract>>();
		try {
			
			String hodQry ="select u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name "
					+"from insurance i " + 
					"left join contract c on i.contract_id_fk = c.contract_id " +
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
					"left join project p on c.project_id_fk = p.project_id " + 
					"left join [user] u on c.hod_user_id_fk = u.user_id "+
					"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
					+"left join department dt on c.contract_department = dt.department "
					+"where contract_id is not null and (i.released_fk <> 'Yes' or i.released_fk is null) ";
			
			int arrSize = 0;			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				hodQry = hodQry + " and c.contract_id = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				hodQry = hodQry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i1 =0; i1< (length-1); i1++) {
						hodQry = hodQry + ",?";
						arrSize++;
					}
				}
				
				hodQry = hodQry + " ) ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				hodQry = hodQry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				hodQry = hodQry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			/*
			 * if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) { hodQry
			 * = hodQry + " and i.valid_upto <= ?"; arrSize++; }
			 */
			
			hodQry = hodQry + " GROUP BY u.designation,us.designation,u.user_name";
			hodQry = hodQry + " ORDER BY case when u.designation='ED Civil' then 1  " + 
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
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
			/*
			 * if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
			 * pValues[i++] = obj.getDate(); }
			 */
			
			List<Contract> hodList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			for (Contract hodObj : hodList) 
			{
				
				String conCatQry="case when (select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id order by FORMAT(MAX(revised_doc),'dd-MMM-yy') order by FORMAT(MAX(revised_doc),'dd-MMM-yy') offset 0 rows  fetch next 1 rows only) is not null then (select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id  order by FORMAT(MAX(revised_doc),'dd-MMM-yy') offset 0 rows  fetch next 1 rows only) else FORMAT(doc,'dd-MMM-yy') end";
				if(obj.getDate()!=null && obj.getDate()!="")
				{
					conCatQry="case when (case when (select revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id order by revised_doc offset 0 rows  fetch next 1 rows only) is not null then (select  revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id  order by revised_doc offset 0 rows  fetch next 1 rows only) else doc end)>'"+obj.getDate()+"' then 'NO DOC' else (case when (select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id order by FORMAT(MAX(revised_doc),'dd-MMM-yy') offset 0 rows  fetch next 1 rows only) is not null then (select FORMAT(MAX(revised_doc),'dd-MMM-yy') AS revised_doc from contract_revision where revised_doc is not null and action = 'Yes' and contract_id_fk = contract_id  order by FORMAT(MAX(revised_doc),'dd-MMM-yy') offset 0 rows  fetch next 1 rows only) else FORMAT(doc,'dd-MMM-yy') end) end";
				}
								
				
				String qry ="select contract_short_name,contractor_name,STRING_AGG( loa_date , '\n' ) as loa_date,STRING_AGG( doc , '\n' ) as doc,STRING_AGG( ContractAlertRemarks , '\n' ) as ContractAlertRemarks from (select distinct "
						+ "dt.department_name,dt.contract_id_code,c.project_id_fk,p.project_name,u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name,c.project_id_fk,contract_type_fk,c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name,dt.department as department_fk,c.hod_user_id_fk,c.dy_hod_user_id_fk," + 
						"scope_of_contract,cast(estimated_cost as CHAR) as estimated_cost,FORMAT(date_of_start,'dd-MMM-yy') AS date_of_start,"
						+conCatQry+ " AS doc,cast(awarded_cost as CHAR) as awarded_cost,loa_letter_number,FORMAT(loa_date,'dd-MMM-yy') AS loa_date,ca_no,FORMAT(ca_date,'dd-MMM-yy') AS ca_date,FORMAT(actual_completion_date,'dd-MMM-yy') AS actual_completion_date,"
						+"FORMAT(contract_closure_date,'dd-MMM-yy') AS contract_closure_date,FORMAT(completion_certificate_release,'dd-MMM-yy') AS completion_certificate_release,FORMAT(final_takeover,'dd-MMM-yy') AS final_takeover,FORMAT(final_bill_release,'dd-MMM-yy') AS final_bill_release,FORMAT(defect_liability_period,'dd-MMM-yy') AS defect_liability_period,cast(completed_cost as CHAR) as completed_cost,"
						+"FORMAT(retention_money_release,'dd-MMM-yy') AS retention_money_release,FORMAT(pbg_release,'dd-MMM-yy') AS pbg_release,contract_status_fk,bg_required,insurance_required, "
						+"(select remarks from alerts where alert_status='Active' and alert_type_fk = 'Contract Period' and contract_id = c.contract_id and alert_value ="

					+ "(case when (cr1.action = 'Yes' and cr1.revised_doc is not null) then (CONCAT('Date of Completion : ',FORMAT(cr1.revised_doc,'dd-MMM-yy') )) " 
					+ "when doc is not null then CONCAT('Date of Completion : ',FORMAT(doc,'dd-MMM-yy') ) else '' end )) as ContractAlertRemarks "						
						
						+"from contract c " + 
						"LEFT JOIN contract_revision cr1 on cr1.contract_id_fk = c.contract_id and cr1.action = 'Yes' and cr1.revised_doc is not null "+
						"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
						"left join project p on c.project_id_fk = p.project_id " + 
						"left join [user] u on c.hod_user_id_fk = u.user_id "+
						"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
						+"left join department dt on c.contract_department = dt.department "
						+"where contract_id is not null ";	
				
				arrSize = 0;			
	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					qry = qry + " and c.contract_id = ? ";
					arrSize++;
				}
				
				if(!StringUtils.isEmpty(hodObj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					qry = qry + " and u.designation = ? ";
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
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					qry = qry + " and c.doc <= ?";
					arrSize++;
				}
				pValues = new Object[arrSize];
				i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					pValues[i++] = obj.getContract_id();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					pValues[i++] = hodObj.getHod_designation();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
					pValues[i++] = obj.getContract_status_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					pValues[i++] = obj.getDate();
				}
				
				qry = qry + " ) as a ";
				
				if(obj.getDate()!=null && obj.getDate()!="")
				{
					qry = qry +" where 1=(case when loa_date is not null and doc='NO DOC' then 0 else 1 end) ";
				}
				
				qry = qry + " GROUP BY contractor_name,contract_short_name";

				List<Contract> insuranceList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
				objsList.put(hodObj.getHod_designation(), insuranceList);
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public Map<String,List<Contract>> getContractsInsuranceForReport(Contract obj) throws Exception {
		Map<String,List<Contract>> objsList = new LinkedHashMap<String, List<Contract>>();
		try {
			
			String hodQry ="select u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name "
					+"from insurance i " + 
					"left join contract c on i.contract_id_fk = c.contract_id " +
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
					"left join project p on c.project_id_fk = p.project_id " + 
					"left join [user] u on c.hod_user_id_fk = u.user_id "+
					"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
					+"left join department dt on c.contract_department = dt.department "
					+"where contract_id is not null and (i.released_fk <> 'Yes' or i.released_fk is null) ";
			
			int arrSize = 0;			

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				hodQry = hodQry + " and c.contract_id = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())) {
				hodQry = hodQry + " and u.designation in (?";
				int length = obj.getHod_designations().length;
				if(length > 1) {
					for(int i1 =0; i1< (length-1); i1++) {
						hodQry = hodQry + ",?";
						arrSize++;
					}
				}
				
				hodQry = hodQry + " ) ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				hodQry = hodQry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				hodQry = hodQry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			/*
			 * if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) { hodQry
			 * = hodQry + " and i.valid_upto <= ?"; arrSize++; }
			 */
			
			hodQry = hodQry + " GROUP BY u.designation,us.designation,u.user_name";
			hodQry = hodQry + " ORDER BY case when u.designation='ED Civil' then 1  " + 
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
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
			/*
			 * if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
			 * pValues[i++] = obj.getDate(); }
			 */
			
			List<Contract> hodList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			for (Contract hodObj : hodList) {			
				String qry ="select contract_short_name,contractor_name,STRING_AGG( insurance_number , '\n<space>' ) as insurance_number,STRING_AGG( insurance_value , '\n<space>' ) as insurance_value,STRING_AGG(insurance_valid_upto , '\n<space>' ) as insurance_valid_upto,STRING_AGG( ContractAlertRemarks , '\n' ) as ContractAlertRemarks from(select insurance_id,i.contract_id_fk,insurance_type_fk,issuing_agency,agency_address,insurance_number,cast((ISNULL(insurance_value,0)*ISNULL(insurance_value_units,0)/100000) as decimal(10,2)) as insurance_value,FORMAT(valid_upto,'dd-MMM-yy') AS insurance_valid_upto,released_fk,"
						+ "dt.department_name,dt.contract_id_code,c.project_id_fk,p.project_name,u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name,c.project_id_fk,contract_type_fk,c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name,dt.department as department_fk,c.hod_user_id_fk,c.dy_hod_user_id_fk," + 
						"scope_of_contract,cast(estimated_cost as CHAR) as estimated_cost,FORMAT(date_of_start,'dd-MMM-yy') AS date_of_start,FORMAT(doc,'dd-MMM-yy') AS doc,cast(awarded_cost as CHAR) as awarded_cost,loa_letter_number,FORMAT(loa_date,'dd-MMM-yy') AS loa_date,ca_no,FORMAT(ca_date,'dd-MMM-yy') AS ca_date,FORMAT(actual_completion_date,'dd-MMM-yy') AS actual_completion_date,"
						+"FORMAT(contract_closure_date,'dd-MMM-yy') AS contract_closure_date,FORMAT(completion_certificate_release,'dd-MMM-yy') AS completion_certificate_release,FORMAT(final_takeover,'dd-MMM-yy') AS final_takeover,FORMAT(final_bill_release,'dd-MMM-yy') AS final_bill_release,FORMAT(defect_liability_period,'dd-MMM-yy') AS defect_liability_period,cast(completed_cost as CHAR) as completed_cost,"
						+"FORMAT(retention_money_release,'dd-MMM-yy') AS retention_money_release,FORMAT(pbg_release,'dd-MMM-yy') AS pbg_release ,contract_status_fk,bg_required,insurance_required, "
						+"(select remarks from alerts where alert_status='Active' and alert_type_fk = 'Insurance' and contract_id = c.contract_id and alert_value = (case when (i.insurance_type_fk is not null and i.insurance_number is not null) then CONCAT(i.insurance_type_fk,' ',i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
						+ "when (i.insurance_type_fk is null and i.insurance_number is not null) then CONCAT(i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
						+ "when (i.insurance_type_fk is not null and i.insurance_number is null) then CONCAT(i.insurance_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
						+ "else CONCAT('Insurance valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ) "
+ "AND created_date = (select max(created_date) from alerts where alert_status='Active' and alert_type_fk = 'Insurance' and contract_id = c.contract_id and alert_value = (case when (i.insurance_type_fk is not null and i.insurance_number is not null) then CONCAT(i.insurance_type_fk,' ',i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
						+ "when (i.insurance_type_fk is null and i.insurance_number is not null) then CONCAT(i.insurance_number, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) "
						+ "when (i.insurance_type_fk is not null and i.insurance_number is null) then CONCAT(i.insurance_type_fk, ' valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) " 
						+ "else CONCAT('Insurance valid upto ',FORMAT(valid_upto,'dd-MMM-yy') ) end ))) as ContractAlertRemarks "						
						
						+"from insurance i " + 
						"left join contract c on i.contract_id_fk = c.contract_id " +
						"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
						"left join project p on c.project_id_fk = p.project_id " + 
						"left join [user] u on c.hod_user_id_fk = u.user_id "+
						"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
						+"left join department dt on c.contract_department = dt.department "
						+"where contract_id is not null and (i.released_fk <> 'Yes' or i.released_fk is null) ";
				
				arrSize = 0;			
	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					qry = qry + " and c.contract_id = ? ";
					arrSize++;
				}
				if(!StringUtils.isEmpty(hodObj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					qry = qry + " and u.designation = ? ";
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
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					qry = qry + " and i.valid_upto <= ?";
					arrSize++;
				}
				pValues = new Object[arrSize];
				i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
					pValues[i++] = obj.getContract_id();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(hodObj.getHod_designation())) {
					pValues[i++] = hodObj.getHod_designation();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
					pValues[i++] = obj.getContract_status_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
					pValues[i++] = obj.getDate();
				}
				qry = qry + " ) as a ";
				
				if(obj.getDate()!=null && obj.getDate()!="")
				{
					qry = qry +" where 1=(case when (STR_TO_DATE(insurance_valid_upto,'dd-MM-yyyy')>'"+obj.getDate()+"') then 0 else 1 end) ";
				}
				
				qry = qry + " GROUP BY contractor_name,contract_short_name";
				
				List<Contract> insuranceList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
				objsList.put(hodObj.getHod_designation(), insuranceList);
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	

	@Override
	public Contract getContractDetailsForReport(Contract obj) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Contract contract = null;
		try{
			con = dataSource.getConnection();
			String contract_updateQry = "select dt.contract_id_code,c.project_id_fk,p.project_name,u.designation,u.user_name,contract_type_fk,c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name,u.department_fk,c.hod_user_id_fk,c.dy_hod_user_id_fk  " + 
									",scope_of_contract,cast((estimated_cost * estimated_cost_units) as CHAR) as estimated_cost,FORMAT(date_of_start,'dd-MM-yyyy') AS date_of_start,FORMAT(doc,'dd-MM-yyyy') AS doc,FORMAT(target_doc,'dd-MM-yyyy') AS target_doc,cast((awarded_cost * awarded_cost_units) as CHAR) as awarded_cost,loa_letter_number,FORMAT(loa_date,'dd-MM-yyyy') AS loa_date,ca_no,FORMAT(ca_date,'dd-MM-yyyy') AS ca_date,FORMAT(c.actual_completion_date,'dd-MM-yyyy') AS actual_completion_date,"
									+"FORMAT(contract_closure_date,'dd-MM-yyyy') AS contract_closure_date,FORMAT(completion_certificate_release,'dd-MM-yyyy') AS completion_certificate_release,FORMAT(final_takeover,'dd-MM-yyyy') AS final_takeover,FORMAT(final_bill_release,'dd-MM-yyyy') AS final_bill_release,FORMAT(defect_liability_period,'dd-MM-yyyy') AS defect_liability_period,cast((completed_cost * completed_cost_units) as CHAR) as completed_cost,"
									+"FORMAT(retention_money_release,'dd-MM-yyyy') AS retention_money_release,FORMAT(pbg_release,'dd-MM-yyyy') AS pbg_release,contract_status_fk,c.status,bg_required,insurance_required,department_name,"
									+ "u.designation as hod_designation,us.designation as dy_hod_designation,revision_requried,milestone_requried " + 
									"from contract c " + 
									"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
									"left join project p on c.project_id_fk = p.project_id " + 
									"left join [user] u on c.hod_user_id_fk = u.user_id "+
									"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
									+"left join department dt on u.department_fk = dt.department "
									+ "where contract_id = ?" ;
			stmt = con.prepareStatement(contract_updateQry);
			stmt.setString(1, obj.getContract_id());
			resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				contract = new Contract();
				contract.setDesignation(resultSet.getString("designation"));
				contract.setUser_name(resultSet.getString("user_name"));
				contract.setContract_id_code(resultSet.getString("contract_id_code"));
				contract.setProject_id_fk(resultSet.getString("project_id_fk"));
				contract.setProject_name(resultSet.getString("project_name"));
				contract.setContract_id(resultSet.getString("contract_id"));
				contract.setContract_type_fk(resultSet.getString("contract_type_fk"));
				contract.setContract_name(resultSet.getString("contract_name"));
				contract.setContract_short_name(resultSet.getString("contract_short_name"));
				contract.setContractor_id_fk(resultSet.getString("contractor_id_fk"));
				contract.setContractor_name(resultSet.getString("contractor_name"));
				contract.setDepartment_fk(resultSet.getString("department_fk"));
				contract.setHod_user_id_fk(resultSet.getString("hod_user_id_fk"));
				contract.setDy_hod_user_id_fk(resultSet.getString("dy_hod_user_id_fk"));
				contract.setScope_of_contract(resultSet.getString("scope_of_contract"));
				contract.setDoc(resultSet.getString("doc"));
				contract.setAwarded_cost(resultSet.getString("awarded_cost"));
				contract.setLoa_letter_number(resultSet.getString("loa_letter_number"));
				contract.setLoa_date(resultSet.getString("loa_date"));
				contract.setCa_no(resultSet.getString("ca_no"));
				contract.setCa_date(resultSet.getString("ca_date"));
				contract.setActual_completion_date(resultSet.getString("actual_completion_date"));
				contract.setCompleted_cost(resultSet.getString("completed_cost"));
				contract.setEstimated_cost(resultSet.getString("estimated_cost"));
				contract.setDate_of_start(resultSet.getString("date_of_start"));
				contract.setContract_closure_date(resultSet.getString("contract_closure_date"));
				contract.setCompletion_certificate_release(resultSet.getString("completion_certificate_release"));
				contract.setFinal_takeover(resultSet.getString("final_takeover"));
				contract.setFinal_bill_release(resultSet.getString("final_bill_release"));
				contract.setDefect_liability_period(resultSet.getString("defect_liability_period"));
				contract.setRetention_money_release(resultSet.getString("retention_money_release"));
				contract.setPbg_release(resultSet.getString("pbg_release"));
				contract.setContract_status_fk(resultSet.getString("contract_status_fk"));
				contract.setBg_required(resultSet.getString("bg_required"));
				contract.setInsurance_required(resultSet.getString("insurance_required"));
				//contract.setRemarks(resultSet.getString("remarks"));
				contract.setDepartment_name(resultSet.getString("department_name"));
				contract.setHod_designation(resultSet.getString("hod_designation"));
				contract.setDy_hod_designation(resultSet.getString("dy_hod_designation"));
				contract.setStatus(resultSet.getString("status"));
				contract.setRevision_requried(resultSet.getString("revision_requried"));
				contract.setMilestone_requried(resultSet.getString("milestone_requried"));
				contract.setTarget_doc(resultSet.getString("target_doc"));
				
				contract.setContract_revision(getContract_revision(contract.getContract_id(),con));	
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, resultSet);
		}		
		return contract;
	}
	
	private List<Contract> getContract_revision(String contract_id, Connection con) throws Exception {
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<Contract> contract_revision = new ArrayList<Contract>();
		Contract obj = null;
		try {
			String qry ="SELECT revision_number,cast((revised_amount * revised_amount_units) as CHAR) as revised_amount ,FORMAT(revised_doc,'dd-MM-yyyy') AS revised_doc"
					+ ",action as revision_status,remarks from contract_revision where contract_id_fk = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, contract_id);
			resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				obj = new Contract();
				obj.setRevision_number(resultSet.getString("revision_number"));
				obj.setRevised_amount(resultSet.getString("revised_amount"));
				obj.setRevised_doc(resultSet.getString("revised_doc"));
				obj.setRevision_status(resultSet.getString("revision_status"));
				obj.setRemarks(resultSet.getString("remarks"));
				contract_revision.add(obj);
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
		}
		return contract_revision;
	}
	
	@Override
	public Contract getProgressDetailsAsOnDate(Contract obj) throws Exception {
		Contract pObj = null;
		NumberFormat numberFormatter = new DecimalFormat("#0.00");
		try {
			String qry ="select contract_status_fk,"
					+ "(select cast(ISNULL(sum(gross_work_done * gross_work_done_units),0) as CHAR) from expenditure where contract_id_fk = c.contract_id) as payment_made, "
					+ "(select cast(ISNULL(revised_amount,0) as CHAR) from contract_revision where revised_amount is not null and action = 'Yes' and contract_id_fk = c.contract_id order by cast(ISNULL(revised_amount,0) as CHAR) offset 0 rows  fetch next 1 rows only) as revised_amount,"
					+ "cast(awarded_cost as CHAR) as awarded_cost,"
					+ "(SELECT cast(sum(contract_per)*100 as decimal(10,2)) FROM activities_scurve where contract_id = c.contract_id and category  = 'Actual') as actual_physical_progress,"
					+ "FORMAT(date_of_start,'dd-MMM-yy') AS date_of_start,FORMAT(doc,'dd-MMM-yy') AS doc "
					+ "from contract c "
					+ "where c.contract_id = ?";
	
			
			Object[] pValues = new Object[]{obj.getContract_id()};
			
			pObj = jdbcTemplate.queryForObject( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));	
			if(!StringUtils.isEmpty(pObj)) {
				double physical_progress = 0;
				String p_progress = pObj.getActual_physical_progress();
				if(!StringUtils.isEmpty(p_progress)) {
					physical_progress = Double.parseDouble(p_progress);
				}
				
				double revised_amount = 0;
				double awarded_cost = 0;
				String r_amount = pObj.getRevised_amount();
				String a_cost = pObj.getAwarded_cost();
				if(!StringUtils.isEmpty(r_amount)) {
					revised_amount = Double.parseDouble(r_amount);
				}
				if(!StringUtils.isEmpty(a_cost)) {
					awarded_cost = Double.parseDouble(a_cost);
				}
				String p_made = pObj.getPayment_made();
				double payment_made = 0;
				if(!StringUtils.isEmpty(p_made)) {
					payment_made = Double.parseDouble(p_made);
				}
				double financial_progress = 0;
				if(payment_made != 0) {
					if(revised_amount != 0) {
						financial_progress = (payment_made*100)/revised_amount;
					}else {
						financial_progress = (payment_made*100)/awarded_cost;
					}					
				}
				String contract_status = pObj.getContract_status_fk();
				if(!StringUtils.isEmpty(contract_status) && contract_status.equals("Completed")) {
					physical_progress = 100;
					financial_progress = 100;
				}
				pObj.setActual_physical_progress(numberFormatter.format(physical_progress) + " %");
				pObj.setActual_financial_progress(numberFormatter.format(financial_progress) + " %");
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return pObj;
	}

	@Override
	public List<Contract> getMilestoneDetailsForReport(Contract obj) throws Exception {
		Connection con  = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<Contract> milestones = new ArrayList<Contract>();
		Contract mObj = null;
		try {
			con = dataSource.getConnection();
			String qry ="SELECT contract_milestones_id,milestone_id,milestone_name,FORMAT(milestone_date,'dd-MM-yyyy') AS milestone_date,FORMAT(actual_date,'dd-MM-yyyy') AS actual_date, revision"
					+ ",remarks from contract_milestones where contract_id_fk = ? and status = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, obj.getContract_id());
			stmt.setString(2, CommonConstants.ACTIVE);
			resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				mObj = new Contract();
				mObj.setContract_milestones_id(resultSet.getString("contract_milestones_id"));
				mObj.setMilestone_id(resultSet.getString("milestone_id"));
				mObj.setMilestone_name(resultSet.getString("milestone_name"));
				mObj.setMilestone_date(resultSet.getString("milestone_date"));
				mObj.setActual_date(resultSet.getString("actual_date"));
				mObj.setRevision(resultSet.getString("revision"));
				mObj.setRemarks(resultSet.getString("remarks"));
				milestones.add(mObj);
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, resultSet);
		}
		return milestones;
	}

	@Override
	public List<Contract> getBGDetailsForReport(Contract obj) throws Exception {
		Connection con  = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<Contract> objsList = new ArrayList<Contract>();
		Contract bObj = null;
		try {
			con = dataSource.getConnection();
			String qry ="SELECT bg_type_fk,issuing_bank, bg_number,cast(cast((bg_value * bg_value_units) as decimal(18,2)) as varchar) as bg_value,FORMAT(valid_upto,'dd-MM-yyyy') AS valid_upto"
					+ ",FORMAT(bg_date,'dd-MM-yyyy') AS bg_date,FORMAT(release_date,'dd-MM-yyyy') AS release_date"
					+ " from bank_guarantee where contract_id_fk = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, obj.getContract_id());
			resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				bObj = new Contract();
				
				//bObj.setCode(resultSet.getString("code"));
				bObj.setBg_type_fk(resultSet.getString("bg_type_fk"));
				bObj.setIssuing_bank(resultSet.getString("issuing_bank"));
				bObj.setBg_number(resultSet.getString("bg_number"));
				bObj.setBg_value(resultSet.getString("bg_value"));
				bObj.setBg_valid_upto(resultSet.getString("valid_upto"));
				bObj.setBg_date(resultSet.getString("bg_date"));
				bObj.setRelease_date(resultSet.getString("release_date"));
				objsList.add(bObj);
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, resultSet);
		}
		return objsList;
	}
	
	

	@Override
	public List<Contract> getInsuranceDetailsForReport(Contract obj) throws Exception {
		Connection con  = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<Contract> objsList = new ArrayList<Contract>();
		Contract iObj = null;
		try {
			con = dataSource.getConnection();
			String qry ="SELECT insurance_type_fk,issuing_agency,agency_address,insurance_number,cast(cast((insurance_value * insurance_value_units) as decimal(18,2)) as varchar) as insurance_value,FORMAT(valid_upto,'dd-MM-yyyy') AS valid_upto"
					+ ",released_fk as insurance_status from insurance where contract_id_fk = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, obj.getContract_id());
			resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				iObj = new Contract();
				iObj.setInsurance_type_fk(resultSet.getString("insurance_type_fk"));
				iObj.setIssuing_agency(resultSet.getString("issuing_agency"));
				iObj.setAgency_address(resultSet.getString("agency_address"));
				iObj.setInsurance_number(resultSet.getString("insurance_number"));
				iObj.setInsurance_value(resultSet.getString("insurance_value"));
				iObj.setInsurance_valid_upto(resultSet.getString("valid_upto"));
				
				iObj.setInsurance_status(resultSet.getString("insurance_status"));
				objsList.add(iObj);
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, resultSet);
		}
		return objsList;
	}

	@Override
	public Contract getContractClosureDetails(Contract obj) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Contract contract = null;
		try{ 
			con = dataSource.getConnection();
			String contract_updateQry = "select dt.contract_id_code,c.project_id_fk,p.project_name,u.designation,u.user_name,contract_type_fk,c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name,u.department_fk,c.hod_user_id_fk,c.dy_hod_user_id_fk  " + 
									",scope_of_contract,cast((estimated_cost * estimated_cost_units) as CHAR) as estimated_cost,FORMAT(date_of_start,'dd-MM-yyyy') AS date_of_start,FORMAT(doc,'dd-MM-yyyy') AS doc,cast((awarded_cost * awarded_cost_units) as CHAR) as awarded_cost,loa_letter_number,FORMAT(loa_date,'dd-MM-yyyy') AS loa_date,ca_no,FORMAT(ca_date,'dd-MM-yyyy') AS ca_date,FORMAT(c.actual_completion_date,'dd-MM-yyyy') AS actual_completion_date,"
									+"FORMAT(contract_closure_date,'dd-MM-yyyy') AS contract_closure_date,FORMAT(completion_certificate_release,'dd-MM-yyyy') AS completion_certificate_release,FORMAT(final_takeover,'dd-MM-yyyy') AS final_takeover,FORMAT(final_bill_release,'dd-MM-yyyy') AS final_bill_release,FORMAT(defect_liability_period,'dd-MM-yyyy') AS defect_liability_period,cast((completed_cost * completed_cost_units) as CHAR) as completed_cost,"
									+"FORMAT(retention_money_release,'dd-MM-yyyy') AS retention_money_release,FORMAT(pbg_release,'dd-MM-yyyy') AS pbg_release,contract_status_fk,bg_required,insurance_required " + 
									"from contract c " + 
									"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
									"left join project p on c.project_id_fk = p.project_id " + 
									"left join [user] u on c.hod_user_id_fk = u.user_id "+
									"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
									+"left join department dt on u.department_fk = dt.department "
									+ "where contract_id = ?" ;
			stmt = con.prepareStatement(contract_updateQry);
			stmt.setString(1, obj.getContract_id());
			resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				contract = new Contract();
				contract.setDesignation(resultSet.getString("designation"));
				contract.setUser_name(resultSet.getString("user_name"));
				contract.setContract_id_code(resultSet.getString("contract_id_code"));
				contract.setProject_id_fk(resultSet.getString("project_id_fk"));
				contract.setProject_name(resultSet.getString("project_name"));
				contract.setContract_id(resultSet.getString("contract_id"));
				contract.setContract_type_fk(resultSet.getString("contract_type_fk"));
				contract.setContract_name(resultSet.getString("contract_name"));
				contract.setContract_short_name(resultSet.getString("contract_short_name"));
				contract.setContractor_id_fk(resultSet.getString("contractor_id_fk"));
				contract.setContractor_name(resultSet.getString("contractor_name"));
				contract.setDepartment_fk(resultSet.getString("department_fk"));
				contract.setHod_user_id_fk(resultSet.getString("hod_user_id_fk"));
				contract.setDy_hod_user_id_fk(resultSet.getString("dy_hod_user_id_fk"));
				contract.setScope_of_contract(resultSet.getString("scope_of_contract"));
				contract.setDoc(resultSet.getString("doc"));
				contract.setAwarded_cost(resultSet.getString("awarded_cost"));
				contract.setLoa_letter_number(resultSet.getString("loa_letter_number"));
				contract.setLoa_date(resultSet.getString("loa_date"));
				contract.setCa_no(resultSet.getString("ca_no"));
				contract.setCa_date(resultSet.getString("ca_date"));
				contract.setActual_completion_date(resultSet.getString("actual_completion_date"));
				contract.setCompleted_cost(resultSet.getString("completed_cost"));
				contract.setEstimated_cost(resultSet.getString("estimated_cost"));
				contract.setDate_of_start(resultSet.getString("date_of_start"));
				contract.setContract_closure_date(resultSet.getString("contract_closure_date"));
				contract.setCompletion_certificate_release(resultSet.getString("completion_certificate_release"));
				contract.setFinal_takeover(resultSet.getString("final_takeover"));
				contract.setFinal_bill_release(resultSet.getString("final_bill_release"));
				contract.setDefect_liability_period(resultSet.getString("defect_liability_period"));
				contract.setRetention_money_release(resultSet.getString("retention_money_release"));
				contract.setPbg_release(resultSet.getString("pbg_release"));
				contract.setContract_status_fk(resultSet.getString("contract_status_fk"));
				contract.setBg_required(resultSet.getString("bg_required"));
				contract.setInsurance_required(resultSet.getString("insurance_required"));
				//contract.setRemarks(resultSet.getString("remarks"));
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, resultSet);
		}		
		return contract;
	}

	@Override
	public Contract getContractorDetails(Contract obj) throws Exception {
		Contract cObj = null;
		try {
			String qry ="SELECT ctr.contractor_id, ctr.contractor_name, ctr.contractor_specilization_fk, ctr.address, ctr.primary_contact_name, "
					+ "ctr.phone_number, ctr.email_id, ctr.pan_number, ctr.gst_number, ctr.bank_name, ctr.account_number, ctr.ifsc_code, ctr.remarks "
					+ "FROM contract c "
					+ "LEFT JOIN contractor ctr ON contractor_id_fk = contractor_id "
					+ "WHERE contract_id = ?";
			
			Object[] pValues = new Object[]{obj.getContract_id()};
			
			cObj = jdbcTemplate.queryForObject( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return cObj;
	}

	@Override
	public List<Contract> getKeyPersonnelForReport(Contract obj) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		List<Contract> objsList = new ArrayList<Contract>();
		try{
			con = dataSource.getConnection();
			String qry ="SELECT name,cast(mobile_no as CHAR) as mobile_no,email_id,designation from contract_key_personnel where contract_id_fk = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, obj.getContract_id());
			resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				obj = new Contract();
				obj.setName(resultSet.getString("name"));
				obj.setMobile_no(resultSet.getString("mobile_no"));
				obj.setEmail_id(resultSet.getString("email_id"));
				obj.setDesignation(resultSet.getString("designation"));
				objsList.add(obj);
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, resultSet);
		}
		return objsList;
	}

	@Override
	public String getEmailIdsOfDepartments(String management) throws Exception {
		String email_ids = null;
		try {
			String qry = "";
			if(!StringUtils.isEmpty(management) && management.equals("CMD&DF")) {
				qry = "select STRING_AGG(email_id,',') FROM [user] where designation in('CMD','DIR Finance') and EMAIL_ID is not null";
			}else if(!StringUtils.isEmpty(management) && management.equals("DP&CE")) {
				qry = "select STRING_AGG(email_id,',') FROM [user] where designation in('CE','DIR Project') and EMAIL_ID is not null";
			}else if(!StringUtils.isEmpty(management) && management.equals("DT")) {
				qry = "select STRING_AGG(email_id,',') FROM [user] where designation in('DIR Technical') and EMAIL_ID is not null";
			}
			if(!StringUtils.isEmpty(qry)) {
				email_ids = jdbcTemplate.queryForObject( qry, String.class);	
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return email_ids;
	}

	@Override
	public List<Contract> getStatsuListInContractReport(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select status from contract c "
					+ "left join [user] u ON hod_user_id_fk = user_id "
					+ "where status IS NOT NULL and status <> ''";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designations())){
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ?";
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
			qry = qry + " group by status  ORDER BY case when status='Open' then 1 " + 
					"   when status='Closed' then 2 " + 
					"   when status='Yet to be Awarded' then 3 end asc ";
			
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
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
	public Contract generateListofContractsReport(Contract obj) throws Exception {
				List<Contract> objsList = null;
				try {
					String hodQry ="SELECT DISTINCT FORMAT(c.loa_date,'dd-MM-yyyy') as loa_date, c.contract_status_fk AS Contract_status, (c.contract_id) AS contract_id, c.contract_short_name AS contract_name, " + 
							"                      ((SELECT ((cr.contractor_name)) " + 
							"                        FROM      dbo.contractor cr " + 
							"                        WHERE   (c.contractor_id_fk = cr.contractor_id AND c.contract_status_fk != 'Not Awarded'))) AS Contractor_name, FORMAT((IIF(c.contract_status_fk = 'Not Awarded', ISNULL(cast(CONVERT(CHAR(20), CONVERT(DATETIME, LEFT " + 
							"                      ((SELECT (revision_planned_date_of_completion) " + 
							"                        FROM      contract_revisions " + 
							"                        WHERE   contract_id_fk = c.contract_id AND revision_no = " + 
							"                                              (SELECT 'R' + '' + Max(SUBSTRING(revision_no, 2, LEN(revision_no))) " + 
							"                                               FROM      contract_revisions " + 
							"                                               WHERE   contract_id_fk = c.contract_id AND revision_planned_date_of_completion IS NOT NULL AND revision_planned_date_of_completion != '')), 10), 105), 101) AS date), c.planned_date_of_completion), c.doc)),'dd-MM-yyyy')  " + 
							"                  AS Original_completion_date, FORMAT((IIF(c.contract_status_fk = 'Completed', c.actual_completion_date, IIF(c.contract_status_fk = 'Not Awarded', NULL, " + 
							"                      (SELECT top 1 (revised_doc) " + 
							"                       FROM      contract_revision " + 
							"                       WHERE   contract_id_fk = c.contract_id AND revision_number = " + 
							"                                             (SELECT 'R' + '' + Max(SUBSTRING(revision_number, 2, LEN(revision_number))) " + 
							"                                              FROM      contract_revision " + 
							"                                              WHERE   contract_id_fk = c.contract_id AND revised_doc IS NOT NULL AND revised_doc != ''))))),'dd-MM-yyyy') AS Revised_date_of_completion, " + 
							"                      isnull(((SELECT (ROUND(SUM(contract_per), 2)) " + 
							"                        FROM      dbo.activities_scurve ac " + 
							"                        WHERE   (ac.contract_id = c.contract_id) and category='Actual' )),0)*100 AS Percent_progress,  (ISNULL " + 
							"                      ((SELECT ((revision_estimated_cost)) " + 
							"                        FROM      contract_revisions " + 
							"                        WHERE   contract_id_fk = c.contract_id AND revision_no = " + 
							"                                              (SELECT 'R' + '' + Max(SUBSTRING(revision_no, 2, LEN(revision_no))) " + 
							"                                               FROM      contract_revisions " + 
							"                                               WHERE   contract_id_fk = c.contract_id AND revision_estimated_cost IS NOT NULL AND revision_estimated_cost != '')), c.estimated_cost*c.estimated_cost_units)) AS Estimated_cost,  " + 
							"                  IIF(c.contract_status_fk = 'Not Awarded', NULL, c.awarded_cost*c.awarded_cost_units) AS Awarded_cost,  IIF(contract_status_fk = 'Not Awarded', NULL, IIF(contract_status_fk = 'In progress', " + 
							"                      ((SELECT top 1 (revised_amount* revised_amount_units) " + 
							"                        FROM      contract_revision " + 
							"                        WHERE   contract_id_fk = c.contract_id AND revision_number = " + 
							"                                              (SELECT 'R' + '' + Max(SUBSTRING(revision_number, 2, LEN(revision_number))) " + 
							"                                               FROM      contract_revision " + 
							"                                               WHERE   contract_id_fk = c.contract_id AND revised_amount IS NOT NULL AND revised_amount != 0))), (c.completed_cost*c.completed_cost_units))) AS Revised_cost, " + 
							"                      (SELECT IIF(contract_status_fk = 'Not Awarded', NULL, (SUM(e.gross_work_done*e.gross_work_done_units))) " + 
							"                       FROM      dbo.expenditure e " + 
							"                       WHERE   (e.contract_id_fk = c.contract_id)) AS Expenditure, " + 
							"                      ((SELECT IIF(c.contract_status_fk = 'Completed', MAX(b.release_date), IIF(contract_status_fk = 'Not Awarded', NULL, IIF(b.release_date = NULL, MIN(b.valid_upto), NULL))) " + 
							"                        FROM      dbo.bank_guarantee b " + 
							"                        WHERE   (b.contract_id_fk = c.contract_id))) AS BG_valid_Upto, " + 
							"                      ((SELECT IIF(contract_status_fk = 'Not Awarded', NULL, MAX((i.valid_upto))) " + 
							"                        FROM      dbo.insurance i " + 
							"                        WHERE   (i.contract_id_fk = c.contract_id))) AS Insurance_valid_Upto " + 
							"FROM     (((((dbo.contract c LEFT JOIN " + 
							"                  dbo.contractor cr ON ((c.contractor_id_fk = cr.contractor_id))) LEFT JOIN " + 
							"                  dbo.activities_scurve ac ON ((ac.contract_id = c.contract_id)) LEFT JOIN " + 
							"                  dbo.bank_guarantee b ON ((c.contract_id = b.contract_id_fk)) LEFT JOIN " + 
							"                  dbo.bank_guarantee i ON ((c.contract_id = i.contract_id_fk)) LEFT JOIN " + 
							"                  dbo.contract_revision crd ON (c.contract_id = crd.contract_id_fk) LEFT JOIN " + 
							"                  dbo.contract_revisions crs ON ((c.contract_id = crs.contract_id_fk))  "  
							+ "left join [user] u on c.hod_user_id_fk = u.user_id "
							+ "left join [user] us on c.dy_hod_user_id_fk = us.user_id "
							+ "left join department dt on c.contract_department = dt.department "					
							+"left join dbo.contract_details cd ON ((cd.contract_id = c.contract_id)))))) where 0=0 ";
					
					int arrSize = 0;
					NumberFormat numberFormatter = new DecimalFormat("#0.00");
					
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
						hodQry = hodQry + " and cd.project_id = ? ";
						arrSize++;
					}					
					
					
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
						hodQry = hodQry + " and c.contract_id = ? ";
						arrSize++;
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designation())) {
						hodQry = hodQry + " and u.designation = ? ";
						arrSize++;
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
						hodQry = hodQry + " and c.project_id_fk = ?";
						arrSize++;
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
						hodQry = hodQry + " and c.contractor_id_fk = ?";
						arrSize++;
					}	
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
						hodQry = hodQry + " and c.status = ?";
						arrSize++;
					}				
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
						hodQry = hodQry + " and c.contract_status_fk = ?";
						arrSize++;
					}
					hodQry = hodQry + " order by contract_status desc";
					
					Object[] pValues = new Object[arrSize];
					int i = 0;
					
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
						pValues[i++] = obj.getProject_id_fk();
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
						pValues[i++] = obj.getContract_id();
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designation())) {
						pValues[i++] = obj.getHod_designation();
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
						pValues[i++] = obj.getProject_id_fk();
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
						pValues[i++] = obj.getContractor_id_fk();
					}
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
						pValues[i++] = obj.getStatus();
					}				
					if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
						pValues[i++] = obj.getContract_status_fk();
					}
					objsList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
					
					for (Contract cObj : objsList) {
						
						String estimated_cost = cObj.getEstimated_cost();
						String estimated_cost_value = "";
						if(!StringUtils.isEmpty(estimated_cost)) {
							double val = (Double.parseDouble(estimated_cost))/10000000;
							estimated_cost_value = numberFormatter.format(val);
						}
						cObj.setEstimated_cost(estimated_cost_value);
						
						
						String awarded_cost = cObj.getAwarded_cost();
						String awarded_cost_value = "";
						if(!StringUtils.isEmpty(awarded_cost)) {
							double val = (Double.parseDouble(awarded_cost))/10000000;
							awarded_cost_value = numberFormatter.format(val);
						}
						cObj.setAwarded_cost(awarded_cost_value);
						
						String expenditure_value = "";
						if(!StringUtils.isEmpty(cObj.getExpenditure())) {
							double val = (Double.parseDouble(cObj.getExpenditure()))/10000000;
							expenditure_value = numberFormatter.format(val);
						}
						cObj.setExpenditure(expenditure_value);
						
						String revised_cost_value = "";
						if(!StringUtils.isEmpty(cObj.getRevised_cost())) {
							double val = (Double.parseDouble(cObj.getRevised_cost()))/10000000;
							revised_cost_value = numberFormatter.format(val) ;
						}
						cObj.setRevised_cost(revised_cost_value);
					}					
					
					obj.setReport1List(objsList);
					
				}catch(Exception e){ 
					throw new Exception(e);
				}
				return obj;
	}

	@Override
	public Contract generateContractBgInsuranceReport(Contract obj) throws Exception {
					List<Contract> objsList = null;
					try {
						String hodQry ="  select contract_name,contractor_name,contract_id,bg_insurance,bg_insurance_type,issuing_bank,bg_insurance_number,amount_inr,raised_date,expiry_date,release_date from(SELECT DISTINCT cd.contract_name,contractor_name,bg.contract_id_fk AS 'Contract_ID','BG' As BG_Insurance,bg.bg_type_fk AS 'BG_Insurance_Type',bg.issuing_bank AS 'Issuing_Bank', bg.bg_number AS 'BG_Insurance_Number',(bg.bg_value*bg_value_units) AS 'Amount_INR', CAST(format(bg.bg_date,'dd-MM-yyyy') as varchar) AS 'Raised_Date',format(bg.valid_upto,'dd-MM-yyyy') AS 'Expiry_Date',CAST(format(bg.release_date,'dd-MM-yyyy') as varchar) AS 'Release_Date'  From bank_guarantee bg inner join contract cd on contract_id_fk=cd.contract_id   inner join contractor cr on cr.contractor_id=cd.contractor_id_fk " + 
								" inner join (select  contract_id_fk,bg_number,max(bank_guarantee_id) as bank_guarantee_id  from bank_guarantee bg1 left join contract cd1 on bg1.contract_id_fk=cd1.contract_id  " + 
								" inner join contractor cr1 on cr1.contractor_id=cd1.contractor_id_fk " + 
								" where bg1.bg_number is not null group by contract_id_fk,bg_number) as AA on AA.contract_id_fk=bg.contract_id_fk  and AA.bank_guarantee_id=BG.bank_guarantee_id where bg.bg_number is not null ";
						
						int arrSize = 0;			
						
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
							hodQry = hodQry + " and cd.contract_id = ? ";
							arrSize++;
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designation())) {
							hodQry = hodQry + " and u.designation = ? ";
							arrSize++;
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
							hodQry = hodQry + " and cd.project_id_fk = ?";
							arrSize++;
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
							hodQry = hodQry + " and cd.contractor_id_fk = ?";
							arrSize++;
						}	
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
							hodQry = hodQry + " and cd.status = ?";
							arrSize++;
						}				
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
							hodQry = hodQry + " and cd.contract_status_fk = ?";
							arrSize++;
						}

						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
							hodQry = hodQry + " and format(bg.valid_upto,'yyyy-MM-dd') >= format(cast(? as date),'yyyy-MM-dd')";
							arrSize++;
						}
						
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getTodate())) {
							hodQry = hodQry + " and format(bg.valid_upto,'yyyy-MM-dd') <= format(cast(? as date),'yyyy-MM-dd')";
							arrSize++;
						}
						
						hodQry = hodQry + " union all ";
						
						
						hodQry =hodQry+"  SELECT DISTINCT cd.contract_name,contractor_name,i.contract_id_fk AS 'Contract_ID','Insurance' As BG_Insurance,i.insurance_type_fk AS 'BG_Insurance_Type',i.issuing_agency AS 'Issuing_Bank', i.insurance_number AS 'BG_Insurance_Number',(i.insurance_value*i.insurance_value_units) AS 'Amount_INR','NA' AS 'Raised_Date',format(i.valid_upto,'dd-MM-yyyy') AS 'Expiry_Date','NA' AS 'Release_Date'  From insurance i inner join contract cd on contract_id_fk=cd.contract_id   " + 
								" inner join contractor cr on cr.contractor_id=cd.contractor_id_fk " + 
								"  inner join (select contract_id_fk,insurance_number,max(insurance_id) as bank_guarantee_id  from insurance i1 left join contract cd1 on i1.contract_id_fk=cd1.contract_id  inner join contractor cr1 on cr1.contractor_id=cd1.contractor_id_fk where i1.insurance_number is not null  " + 
								"group by contract_id_fk,insurance_number) as AA on AA.contract_id_fk=i.contract_id_fk  and AA.bank_guarantee_id=i.insurance_id where i.insurance_number is not null ";
						
						
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
							hodQry = hodQry + " and cd.contract_id = ? ";
							arrSize++;
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designation())) {
							hodQry = hodQry + " and u.designation = ? ";
							arrSize++;
						}

						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
							hodQry = hodQry + " and cd.contractor_id_fk = ?";
							arrSize++;
						}	
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
							hodQry = hodQry + " and cd.status = ?";
							arrSize++;
						}				
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
							hodQry = hodQry + " and cd.contract_status_fk = ?";
							arrSize++;
						}
						
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) {
							hodQry = hodQry + " and format(i.valid_upto,'yyyy-MM-dd') >= format(cast(? as date),'yyyy-MM-dd')";
							arrSize++;
						}
						
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getTodate())) {
							hodQry = hodQry + " and format(i.valid_upto,'yyyy-MM-dd') <= format(cast(? as date),'yyyy-MM-dd')";
							arrSize++;
						}						
						
						hodQry = hodQry + " ) as a ";
						
						Object[] pValues = new Object[arrSize];
						int i = 0;
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
							pValues[i++] = obj.getContract_id();
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designation())) {
							pValues[i++] = obj.getHod_designation();
						}

						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
							pValues[i++] = obj.getContractor_id_fk();
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
							pValues[i++] = obj.getStatus();
						}				
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
							pValues[i++] = obj.getContract_status_fk();
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) 
						{

							String[] SpltResPersonsArray = obj.getDate().split("-");
							String ddm = "";

							for (int f = 0; f < SpltResPersonsArray.length; f++) {
							    ddm = SpltResPersonsArray[1] + "-" + SpltResPersonsArray[0] + "-" + SpltResPersonsArray[2];
							}

							pValues[i++] = ddm;
							
						}
						
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getTodate())) 
						{

							String[] SpltResPersonsArray = obj.getTodate().split("-");
							String ddm = "";

							for (int f = 0; f < SpltResPersonsArray.length; f++) {
							    ddm = SpltResPersonsArray[1] + "-" + SpltResPersonsArray[0] + "-" + SpltResPersonsArray[2];
							}

							pValues[i++] = ddm;
							
						}						
						
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
							pValues[i++] = obj.getContract_id();
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designation())) {
							pValues[i++] = obj.getHod_designation();
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
							pValues[i++] = obj.getContractor_id_fk();
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
							pValues[i++] = obj.getStatus();
						}				
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
							pValues[i++] = obj.getContract_status_fk();
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDate())) 
						{
							String[] SpltResPersonsArray = obj.getDate().split("-");
							String ddm = "";

							if (SpltResPersonsArray.length == 3) {
							    ddm = SpltResPersonsArray[1] + "-" + SpltResPersonsArray[0] + "-" + SpltResPersonsArray[2];
							}

							pValues[i++] = ddm;
						}
						if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getTodate())) 
						{

							String[] SpltResPersonsArray = obj.getTodate().split("-");
							String ddm = "";

							if (SpltResPersonsArray.length == 3) {
							    ddm = SpltResPersonsArray[1] + "-" + SpltResPersonsArray[0] + "-" + SpltResPersonsArray[2];
							}

							pValues[i++] = ddm;
							
						}						
						
						objsList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
						/*NumberFormat numberFormatter = new DecimalFormat("#0.00");
						for (Contract cObj : objsList) {
							
							String amount_inr_cost = cObj.getAmount_inr();
							String amount_inr_cost_value = "";
							if(!StringUtils.isEmpty(amount_inr_cost)) {
								double val = (Double.parseDouble(amount_inr_cost))/10000000;
								amount_inr_cost_value = numberFormatter.format(val);
							}
							cObj.setAmount_inr(amount_inr_cost_value);
						}*/					
						
						obj.setReport1List(objsList);
						
					}catch(Exception e){ 
						throw new Exception(e);
					}
					return obj;
		}

	@Override
	public Contract generateContractCompletionReport(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String hodQry ="SELECT DISTINCT FORMAT(c.loa_date,'dd-MM-yyyy') as loa_date, c.contract_status_fk AS Contract_status, (c.contract_id) AS contract_id, c.contract_short_name AS contract_name, " + 
					"                      ((SELECT ((cr.contractor_name)) " + 
					"                        FROM      dbo.contractor cr " + 
					"                        WHERE   (c.contractor_id_fk = cr.contractor_id AND c.contract_status_fk != 'Not Awarded'))) AS Contractor_name, FORMAT((IIF(c.contract_status_fk = 'Not Awarded', ISNULL(cast(CONVERT(CHAR(20), CONVERT(DATETIME, LEFT " + 
					"                      ((SELECT (revision_planned_date_of_completion) " + 
					"                        FROM      contract_revisions " + 
					"                        WHERE   contract_id_fk = c.contract_id AND revision_no = " + 
					"                                              (SELECT 'R' + '' + Max(SUBSTRING(revision_no, 2, LEN(revision_no))) " + 
					"                                               FROM      contract_revisions " + 
					"                                               WHERE   contract_id_fk = c.contract_id AND revision_planned_date_of_completion IS NOT NULL AND revision_planned_date_of_completion != '')), 10), 105), 101) AS date), c.planned_date_of_completion), c.doc)),'dd-MM-yyyy')  " + 
					"                  AS Original_completion_date, FORMAT((IIF(c.contract_status_fk = 'Completed', c.actual_completion_date, IIF(c.contract_status_fk = 'Not Awarded', NULL, " + 
					"                      (SELECT top 1 (revised_doc) " + 
					"                       FROM      contract_revision " + 
					"                       WHERE   contract_id_fk = c.contract_id AND revision_number = " + 
					"                                             (SELECT 'R' + '' + Max(SUBSTRING(revision_number, 2, LEN(revision_number))) " + 
					"                                              FROM      contract_revision " + 
					"                                              WHERE   contract_id_fk = c.contract_id AND revised_doc IS NOT NULL AND revised_doc != ''))))),'dd-MM-yyyy') AS Revised_date_of_completion, " + 
					"                      isnull(((SELECT (ROUND(SUM(contract_per), 2)) " + 
					"                        FROM      dbo.activities_scurve ac " + 
					"                        WHERE   (ac.contract_id = c.contract_id) and category='Actual' )),0)*100 AS Percent_progress,  (ISNULL " + 
					"                      ((SELECT ((revision_estimated_cost)) " + 
					"                        FROM      contract_revisions " + 
					"                        WHERE   contract_id_fk = c.contract_id AND revision_no = " + 
					"                                              (SELECT 'R' + '' + Max(SUBSTRING(revision_no, 2, LEN(revision_no))) " + 
					"                                               FROM      contract_revisions " + 
					"                                               WHERE   contract_id_fk = c.contract_id AND revision_estimated_cost IS NOT NULL AND revision_estimated_cost != '')), c.estimated_cost*c.estimated_cost_units)) AS Estimated_cost,  " + 
					"                  IIF(c.contract_status_fk = 'Not Awarded', NULL, c.awarded_cost*c.awarded_cost_units) AS Awarded_cost,  IIF(contract_status_fk = 'Not Awarded', NULL, IIF(contract_status_fk = 'In progress', " + 
					"                      ((SELECT top 1 (revised_amount* revised_amount_units) " + 
					"                        FROM      contract_revision " + 
					"                        WHERE   contract_id_fk = c.contract_id AND revision_number = " + 
					"                                              (SELECT 'R' + '' + Max(SUBSTRING(revision_number, 2, LEN(revision_number))) " + 
					"                                               FROM      contract_revision " + 
					"                                               WHERE   contract_id_fk = c.contract_id AND revised_amount IS NOT NULL AND revised_amount != 0))), (c.completed_cost*c.completed_cost_units))) AS Revised_cost, " + 
					"                      (SELECT IIF(contract_status_fk = 'Not Awarded', NULL, (SUM(e.gross_work_done*e.gross_work_done_units))) " + 
					"                       FROM      dbo.expenditure e " + 
					"                       WHERE   (e.contract_id_fk = c.contract_id)) AS Expenditure, " + 
					"                      ((SELECT IIF(c.contract_status_fk = 'Completed', MAX(b.release_date), IIF(contract_status_fk = 'Not Awarded', NULL, IIF(b.release_date = NULL, MIN(b.valid_upto), NULL))) " + 
					"                        FROM      dbo.bank_guarantee b " + 
					"                        WHERE   (b.contract_id_fk = c.contract_id))) AS BG_valid_Upto, " + 
					"                      ((SELECT IIF(contract_status_fk = 'Not Awarded', NULL, MAX((i.valid_upto))) " + 
					"                        FROM      dbo.insurance i " + 
					"                        WHERE   (i.contract_id_fk = c.contract_id))) AS Insurance_valid_Upto " + 
					"FROM     (((((dbo.contract c LEFT JOIN " + 
					"                  dbo.contractor cr ON ((c.contractor_id_fk = cr.contractor_id))) LEFT JOIN " + 
					"                  dbo.activities_scurve ac ON ((ac.contract_id = c.contract_id)) LEFT JOIN " + 
					"                  dbo.bank_guarantee b ON ((c.contract_id = b.contract_id_fk)) LEFT JOIN " + 
					"                  dbo.bank_guarantee i ON ((c.contract_id = i.contract_id_fk)) LEFT JOIN " + 
					"                  dbo.contract_revision crd ON (c.contract_id = crd.contract_id_fk) LEFT JOIN " + 
					"                  dbo.contract_revisions crs ON ((c.contract_id = crs.contract_id_fk))  "  
					+ "left join [user] u on c.hod_user_id_fk = u.user_id "
					+ "left join [user] us on c.dy_hod_user_id_fk = us.user_id "
					+ "left join department dt on c.contract_department = dt.department "					
					+"left join dbo.contract_details cd ON ((cd.contract_id = c.contract_id)))))) where 0=0 ";
			
			int arrSize = 0;
			NumberFormat numberFormatter = new DecimalFormat("#0.00");
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				hodQry = hodQry + " and c.contract_id = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designation())) {
				hodQry = hodQry + " and u.designation = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				hodQry = hodQry + " and c.project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				hodQry = hodQry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
				hodQry = hodQry + " and c.status = ?";
				arrSize++;
			}				
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				hodQry = hodQry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			hodQry = hodQry + " order by contract_status desc";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_designation())) {
				pValues[i++] = obj.getHod_designation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStatus())) {
				pValues[i++] = obj.getStatus();
			}				
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			objsList = jdbcTemplate.query( hodQry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			
			for (Contract cObj : objsList) {
				
				String estimated_cost = cObj.getEstimated_cost();
				String estimated_cost_value = "";
				if(!StringUtils.isEmpty(estimated_cost)) {
					double val = (Double.parseDouble(estimated_cost))/10000000;
					estimated_cost_value = numberFormatter.format(val);
				}
				cObj.setEstimated_cost(estimated_cost_value);
				
				
				String awarded_cost = cObj.getAwarded_cost();
				String awarded_cost_value = "";
				if(!StringUtils.isEmpty(awarded_cost)) {
					double val = (Double.parseDouble(awarded_cost))/10000000;
					awarded_cost_value = numberFormatter.format(val);
				}
				cObj.setAwarded_cost(awarded_cost_value);
				
				String expenditure_value = "";
				if(!StringUtils.isEmpty(cObj.getExpenditure())) {
					double val = (Double.parseDouble(cObj.getExpenditure()))/10000000;
					expenditure_value = numberFormatter.format(val);
				}
				cObj.setExpenditure(expenditure_value);
				
				String revised_cost_value = "";
				if(!StringUtils.isEmpty(cObj.getRevised_cost())) {
					double val = (Double.parseDouble(cObj.getRevised_cost()))/10000000;
					revised_cost_value = numberFormatter.format(val) ;
				}
				cObj.setRevised_cost(revised_cost_value);
			}					
			
			obj.setReport1List(objsList);
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return obj;
	}
	
	@Override
	public List<Contract> generateContractBGDetails(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select  contract_id,u.designation as hod_user_id_fk,loa_letter_number,contract_short_name,contractor_name,address,bg_type_fk,issuing_bank,bg_number,bg_value,(select case when release_date is null then valid_upto else release_date end) as bg_valid_upto,bg_letter_status " + 
					"from contract c   " + 
					"left join [user] u on u.user_id = c.hod_user_id_fk    " + 
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id   " + 
					"left join bank_guarantee bg on bg.contract_id_fk = c.contract_id  " + 
					"where contract_id is not null and (select case when release_date is null then valid_upto else release_date end)>=DATEADD(M,DATEDIFF(M,0,getdate())-1,0) " + 
					"and valid_upto<=DATEADD(month, +2, GETDATE())  ";
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + " and c.contract_id = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getBg_number())) {
				qry = qry + " and bg.bg_number = ? ";
				arrSize++;
			}			

			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getBg_number())) {
				pValues[i++] = obj.getBg_number();
			}	
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}	

	@Override
	public List<Contract> getTheListOfExpiringBgs(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			/*String qry ="select  contract_id,contract_short_name,contractor_name,bg_type_fk,issuing_bank,bg_number,bg_value,(select case when release_date is null then valid_upto else release_date end) as bg_valid_upto,bg_letter_status " + 
					"from contract c   " + 
					"left join work w on c.project_id_fk = w.work_id    " + 
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id   " + 
					"left join bank_guarantee bg on bg.contract_id_fk = c.contract_id  " + 
					"where contract_id is not null and (select case when release_date is null then valid_upto else release_date end)>=(select case when release_date is null then DATEADD(M,DATEDIFF(M,0,getdate())-1,0) else getdate() end) " + 
					"and (select case when release_date is null then valid_upto else release_date end)<=(select case when release_date is null then DATEADD(M,DATEDIFF(M,0,getdate())+2,0) else getdate() end)  ";*/
			
			String qry="select  contract_id,contract_short_name,contractor_name,bg_type_fk,issuing_bank,bg_number,bg_value,(select case when release_date is null then valid_upto else release_date end) as bg_valid_upto,bg_letter_status   " + 
					"					from contract c     " + 
					"					left join contractor cr on c.contractor_id_fk = cr.contractor_id     " + 
					"					left join bank_guarantee bg on bg.contract_id_fk = c.contract_id    " + 
					"					where contract_id is not null  and release_date is null and valid_upto<=DATEADD(month, +2, GETDATE()) ";
			
			if(!StringUtils.isEmpty(obj.getDate_of_start()) && !StringUtils.isEmpty(obj.getBg_date())) {
				qry = qry+" and valid_upto>='"+obj.getDate_of_start()+"' and valid_upto<='"+obj.getBg_date()+"'";
			}
			qry = qry+" order by valid_upto asc";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Contract> getContractDownload(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select  contract_id,contract_short_name,contractor_name,bg_type_fk,issuing_bank,bg_number,bg_value,(select case when release_date is null then valid_upto else release_date end) as bg_valid_upto " + 
					"from contract c   " + 
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id   " + 
					"left join bank_guarantee bg on bg.contract_id_fk = c.contract_id  " + 
					"where contract_id is not null and (select case when release_date is null then valid_upto else release_date end)>=(select case when release_date is null then DATEADD(M,DATEDIFF(M,0,getdate())-1,0) else getdate() end) " + 
					"and (select case when release_date is null then valid_upto else release_date end)<=(select case when release_date is null then DATEADD(M,DATEDIFF(M,0,getdate())+2,0) else getdate() end) ";
			
			int arrSize = 0;
			NumberFormat numberFormatter = new DecimalFormat("#0.00");
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + " and c.contract_id = ? ";
				arrSize++;
			}	

			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}			
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public boolean UpdateLetterStatus(Contract obj) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		boolean flag = false;
		try{  
			con = dataSource.getConnection();
			String updateQry = "UPDATE bank_guarantee set bg_letter_status = ? WHERE contract_id_fk = ? and bg_number=?";
			stmt = con.prepareStatement(updateQry);
			stmt.setString(1, obj.getLetter_status());
			stmt.setString(2, obj.getContract_id());
			stmt.setString(3, obj.getBg_number());
			int c = stmt.executeUpdate(); 
			if(c > 0) {		
				flag = true;				
			}
		}catch(Exception e){ 
			throw new SQLException(e.getMessage());
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return flag;
	}

	@Override
	public List<Contract> getTheListOfExpiringInsurances(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select  contract_id,contract_short_name,contractor_name,STRING_AGG(insurance_type_fk,',')  as insurance_type_fk, " + 
					"count(insurance_type_fk) as insurance_count,STRING_AGG(insurance_number,',') as insurance_number,FORMAT (valid_upto, 'MMM yyyy')  as insurance_valid_upto,insurance_letter_status " + 
					"from contract c    " + 
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id    " + 
					"left join insurance bg on bg.contract_id_fk = c.contract_id   " + 
					"where contract_id is not null and (released_fk is null or released_fk<>'Yes') and valid_upto>=DATEADD(M,DATEDIFF(M,0,getdate())-1,0) and valid_upto<=GETDATE() and insurance_number is not null " + 
					"group by contract_id,contract_short_name,contractor_name,valid_upto,insurance_letter_status order by contract_id";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Contract> generatContractInsuranceDetails(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select  contract_id,contract_short_name,contractor_name,address,insurance_type_fk,  " + 
					"					insurance_number,FORMAT (valid_upto, 'dd-MMM-yy')  as insurance_valid_upto,insurance_value,issuing_agency  " + 
					"					from contract c     " + 
					"					left join contractor cr on c.contractor_id_fk = cr.contractor_id     " + 
					"					left join insurance bg on bg.contract_id_fk = c.contract_id    " + 
					"					where contract_id is not null and (released_fk is null or released_fk<>'Yes') and valid_upto>=DATEADD(M,DATEDIFF(M,0,getdate())-1,0) and valid_upto<=GETDATE() and insurance_number is not null ";
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + " and contract_id = ? ";
				arrSize++;
			}	
			/*if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getInsurance_number())) {
				qry = qry + " and insurance_number = ? ";
				arrSize++;
			}*/			

			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}			
			/*if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getInsurance_number())) {
				pValues[i++] = obj.getInsurance_number();
			}*/	
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public boolean UpdateInsuranceLetterStatus(Contract obj) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		boolean flag = false;
		try{  
			con = dataSource.getConnection();
			String updateQry = "UPDATE insurance set insurance_letter_status = ? WHERE contract_id_fk = ? and insurance_number in (					select     " + 
					"					insurance_number " + 
					"					from contract c      " + 
					"					left join contractor cr on c.contractor_id_fk = cr.contractor_id      " + 
					"					left join insurance bg on bg.contract_id_fk = c.contract_id     " + 
					"					where contract_id is not null and (released_fk is null or released_fk<>'Yes') and valid_upto>=DATEADD(M,DATEDIFF(M,0,getdate())-1,0) and valid_upto<=GETDATE() and insurance_number is not null   " + 
					" " + 
					"					and contract_id=?)";
			stmt = con.prepareStatement(updateQry);
			stmt.setString(1, obj.getLetter_status());
			stmt.setString(2, obj.getContract_id());
			stmt.setString(3, obj.getContract_id());
			int c = stmt.executeUpdate(); 
			if(c > 0) {		
				flag = true;				
			}
		}catch(Exception e){ 
			throw new SQLException(e.getMessage());
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return flag;
	}

	@Override
	public boolean UpdateDateOfCompletionLetterStatus(Contract obj) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		boolean flag = false;
		try{  
			con = dataSource.getConnection();
			String updateQry = "UPDATE contract_revision set doc_letter_status = ? WHERE contract_id_fk = ?";
			stmt = con.prepareStatement(updateQry);
			stmt.setString(1, obj.getLetter_status());
			stmt.setString(2, obj.getContract_id());
			int c = stmt.executeUpdate(); 
			if(c > 0) {		
				flag = true;				
			}
		}catch(Exception e){ 
			throw new SQLException(e.getMessage());
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return flag;
	}

	@Override
	public List<Contract> generatContractDOCDetails(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select  distinct contract_id,contract_short_name,contractor_name,address,case when revised_doc is null then doc else revised_doc end as doc,doc_letter_status   " + 
					"from contract c      " + 
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id      " + 
					"left join contract_revision bg on bg.contract_id_fk = c.contract_id     " + 
					"where contract_id is not null and c.contract_status_fk='In Progress'  " + 
					"					 " + 
					"and contract_status_fk='In Progress' and action=(case when (select count(*) from contract_revision where contract_id_fk=c.contract_id)>0 then 'Yes' else 'No' end) " + 
					"				 " + 
					"and (case when revised_doc is null then doc else revised_doc end)<=DATEADD(M,DATEDIFF(M,0,getdate())+3,0)  ";
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				qry = qry + " and c.contract_id = ? ";
				arrSize++;
			}	
	

			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id())) {
				pValues[i++] = obj.getContract_id();
			}			

			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Contract> getTheListOfExpiringDocs(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry ="select  distinct contract_id,contract_short_name,contractor_name,case when revised_doc is null then doc else revised_doc end as doc,doc_letter_status   " + 
					"from contract c      " + 
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id      " + 
					"left join contract_revision bg on bg.contract_id_fk = c.contract_id     " + 
					"where contract_id is not null   " + 
					"and (case when revised_doc is null then doc else revised_doc end)<=DATEADD(M,DATEDIFF(M,0,getdate())+3,0)  " + 
					" " + 
					"and contract_status_fk='In Progress' and action=(case when (select count(*) from contract_revision where contract_id_fk=c.contract_id)>0 then 'Yes' else 'No' end) " + 
					" " + 
					"order by doc_letter_status,(case when revised_doc is null then doc else revised_doc end) ";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
}


