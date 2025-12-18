package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.BankGuarantee;
import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.Insurence;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.DBConnectionHandler;

@Repository
public class ContractRepository implements IContractRepo {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Contract> getDepartmentList() throws Exception {
		List<Contract> objsList = null;
		try {
			String qry = "select department as department_fk,department_name,contract_id_code from department";			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Contract> getDesignationsFilterList(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry = "SELECT c.hod_user_id_fk as hod_user_id,u.designation  "
					+ "from contract c " + 
					"left join [user] u on c.hod_user_id_fk = u.user_id "+
					"LEFT JOIN project p on c.project_id_fk = p.project_id " +
					"where hod_user_id_fk is not null and hod_user_id_fk <> '' ";

			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				qry = qry + " and c.hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				qry = qry + " and c.dy_hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				qry = qry + " and c.status = ?";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? or "
						+ "contract_id in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk)) ";
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry + " GROUP BY c.hod_user_id_fk,u.designation ORDER BY case when u.designation='ED Civil' then 1 " + 
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

			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				pValues[i++] = obj.getDesignation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				pValues[i++] = obj.getDy_hod_designation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				pValues[i++] = obj.getContract_status();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}	
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;

}

	@Override
	public List<Contract> getDyHODDesignationsFilterList(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry = "SELECT c.dy_hod_user_id_fk as dy_hod_user_id,u.designation as dy_hod_designation "
					+ "from contract c " + 
					"left join [user] u on c.dy_hod_user_id_fk = u.user_id "+
					"LEFT JOIN project p on c.project_id_fk = p.project_id " +
					"where dy_hod_user_id_fk is not null and dy_hod_user_id_fk <> '' ";
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				qry = qry + " and c.hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				qry = qry + " and c.dy_hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				qry = qry + " and c.status = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? or "
						+ "contract_id in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk)) ";
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry + " GROUP BY c.dy_hod_user_id_fk,u.designation  ORDER BY u.designation ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				pValues[i++] = obj.getDesignation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				pValues[i++] = obj.getDy_hod_designation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				pValues[i++] = obj.getContract_status();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}	
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Contract> contractorsFilterList(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry = "SELECT contractor_id_fk,cr.contractor_name "
					+ "from contract c "+
       "LEFT JOIN contractor cr on c.contractor_id_fk = cr.contractor_id "+
       "LEFT JOIN project p on c.project_id_fk = p.project_id " +
        "where contractor_id_fk is not null AND contractor_id_fk <> '' ";	
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				qry = qry + " and c.hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				qry = qry + " and c.dy_hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				qry = qry + " and c.status = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? or "
						+ "contract_id in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk))";
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry + " GROUP BY contractor_id_fk,cr.contractor_name ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				pValues[i++] = obj.getDesignation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				pValues[i++] = obj.getDy_hod_designation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				pValues[i++] = obj.getContract_status();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

    @Override
	public List<Contract> getContractStatusFilterListInContract(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry = "SELECT c.status as contract_status "
			           + "FROM contract c "
			           + "LEFT JOIN project p ON c.project_id_fk = p.project_id "+
					"where status is not null and status <> '' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				qry = qry + " and c.hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				qry = qry + " and c.dy_hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				qry = qry + " and c.status = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? or "
						+ "contract_id in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk)) ";
				arrSize++;
				arrSize++;
				arrSize++;
			}
			qry = qry + " GROUP BY c.status ORDER BY case when status='Open' then 1" + 
					"   when status='Closed' then 2 " + 
					"   when status='Yet to be Awarded' then 3 end asc";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				pValues[i++] = obj.getDesignation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				pValues[i++] = obj.getDy_hod_designation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				pValues[i++] = obj.getContract_status();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}	
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Contract> getStatusFilterListInContract(Contract obj) throws Exception {
		List<Contract> objsList = null;
		try {
			String qry = "SELECT contract_status_fk "
			           + "FROM contract c "
			           + "LEFT JOIN project p ON c.project_id_fk = p.project_id "
			           + "WHERE contract_status_fk IS NOT NULL AND contract_status_fk <> '' ";

			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				qry = qry + " and c.hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				qry = qry + " and c.dy_hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				qry = qry + " and c.status = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? or "
						+ "contract_id in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk)) ";
				arrSize++;
				arrSize++;
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				qry = qry + " and c.contract_department = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY contract_status_fk ";
			qry = qry + "    ORDER BY case when contract_status_fk='Commissioned' then 1" + 
					"   when contract_status_fk='Completed' then 2" + 
					"   when contract_status_fk='In Progress' then 3" + 
					"   when contract_status_fk='On Hold' then 4" + 
					"   when contract_status_fk='Dropped' then 5" + 
					"   when contract_status_fk='Not Started' then 6 end asc";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				pValues[i++] = obj.getDesignation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				pValues[i++] = obj.getDy_hod_designation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				pValues[i++] = obj.getContract_status();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
				pValues[i++] = obj.getDepartment_fk();
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	
	@Override
	public List<Contract> contractList(Contract obj)throws Exception{
		List<Contract> objsList = null;
		try {
			String qry ="select distinct  d1.department_name as department_name,c.project_id_fk,p.project_name," + 
					"u.designation,us.designation as dy_hod_designation,u.user_name,contract_type_fk," + 
					"c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name," + 
					"c.hod_user_id_fk,c.dy_hod_user_id_fk," + 
					"FORMAT(modified_date,'dd-MM-yyyy') as modified_date " + 
					"from contract c " + 
					"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
					"left join project p on c.project_id_fk = p.project_id " +
					"left join department d1 on c.contract_department = d1.department " +
					"left join [user] u on c.hod_user_id_fk = u.user_id "+
					"left join department hoddt on u.department_fk = hoddt.department "+
					"left join [user] us on c.dy_hod_user_id_fk = us.user_id "+
					"left join contract_executive ce on c.contract_id = ce.contract_id_fk "
					+"left join department dt on ce.department_id_fk = dt.department "
					+"where contract_id is not null ";
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				qry = qry + " and c.contractor_id_fk = ?";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				qry = qry + " and c.hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				qry = qry + " and c.dy_hod_user_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				qry = qry + " and c.status = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				qry = qry + " and c.contract_status_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? or "
						+ "contract_id in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) or contract_id in(	select distinct contract_id from contractexecutives ce inner join contract c on c.contract_id=ce.contract_id_fk where executive_user_id_fk = ? " + 
						") )";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}
			
			qry = qry + " group by c.project_id_fk,p.project_name,d1.department_name,u.designation,us.designation," + 
					"u.user_name,contract_type_fk,c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name," + 
					"c.hod_user_id_fk,c.dy_hod_user_id_fk,FORMAT(modified_date,'dd-MM-yyyy') ORDER BY contract_id ASC ";
			
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
				pValues[i++] = obj.getContractor_id_fk();
			}

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDesignation())) {
				pValues[i++] = obj.getDesignation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_designation())) {
				pValues[i++] = obj.getDy_hod_designation();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
				pValues[i++] = obj.getContract_status();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status_fk())) {
				pValues[i++] = obj.getContract_status_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
						
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				for (Contract cObj : objsList) {
					Contract deptObj = getDepartmentsLists(cObj);
					if(!StringUtils.isEmpty(deptObj)){
						if(!StringUtils.isEmpty(deptObj.getDepartment_name()) && !StringUtils.isEmpty(cObj.getHod_department()) && !deptObj.getDepartment_name().contains(cObj.getHod_department())) {
							cObj.setDepartment_name(deptObj.getDepartment_name() + "," +cObj.getHod_department() );
						}else if(StringUtils.isEmpty(deptObj.getDepartment_name())) {
							cObj.setDepartment_name(cObj.getHod_department() );
						}else {
							cObj.setDepartment_name(deptObj.getDepartment_name() );
						}
					}else {
						for (Contract cObj1 : objsList) {
							if(!StringUtils.isEmpty(cObj1.getDepartment_name()) && !StringUtils.isEmpty(cObj1.getHod_department()) && !cObj1.getDepartment_name().contains(cObj1.getHod_department())) {
								cObj1.setDepartment_name(cObj1.getDepartment_name() + "," +cObj1.getHod_department() );
							}else if(StringUtils.isEmpty(cObj1.getDepartment_name())) {
								cObj1.setDepartment_name(cObj1.getHod_department() );
							}
						}
					}
				}
			}else {
				for (Contract cObj : objsList) {
					if(!StringUtils.isEmpty(cObj.getDepartment_name()) && !StringUtils.isEmpty(cObj.getHod_department()) && !cObj.getDepartment_name().contains(cObj.getHod_department())) {
						cObj.setDepartment_name(cObj.getDepartment_name() + "," +cObj.getHod_department() );
					}else if(StringUtils.isEmpty(cObj.getDepartment_name())) {
						cObj.setDepartment_name(cObj.getHod_department() );
					}
				}
			}
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	   private Contract getDepartmentsLists(Contract cObj) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Contract contract = null;
		try{
			con = dataSource.getConnection();
			String contract_updateQry = "SELECT contract_id_fk,STRING_AGG(dt.department_name, ',' ) as department_name FROM contract_executive ce "
					+ " left join department dt on ce.department_id_fk = dt.department where contract_id_fk = ?  group by contract_id_fk  ";
			stmt = con.prepareStatement(contract_updateQry);
			stmt.setString(1, cObj.getContract_id());
			resultSet = stmt.executeQuery();
			while(resultSet.next()) {
				contract = new Contract();
				contract.setDepartment_name(resultSet.getString("department_name"));
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, null);
		}	
		return contract;
		
	}
		@Override
		public List<Contract> getProjectsListForContractForm(Contract obj) throws Exception {
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
		public List<Contract> getWorkListForContractForm(Contract obj) throws Exception {
			List<Contract> objsList = new ArrayList<Contract>();
			
			return objsList;
		}

		@Override
		public List<Contract> getContractFileTypeList(Contract obj) throws Exception {
			List<Contract> objsList = null;
			try {
				String qry = "select contract_file_type from contract_file_type ";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));			
			}catch(Exception e){ 
				throw new Exception(e);
			}
			return objsList;
		}

		@Override
		public List<User> setHodList()throws Exception{
			List<User> objsList = null;
			try {
				String qry ="select user_id,user_name,designation,department_fk,d.contract_id_code "
						+ "FROM [user] u "
						+ "LEFT JOIN department d on  u.department_fk = d.department "
						+ "where designation is not null and designation <> '' and user_type_fk = ? group by user_id,user_name,designation,department_fk,d.contract_id_code";
				qry = qry + " order by case when u.designation='ED Civil' then 1 " + 
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


				int arrSize = 1;
				Object[] pValues = new Object[arrSize];
				int i = 0;
				pValues[i++] = CommonConstants.USER_TYPE;
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<User>(User.class));	
			}catch(Exception e){ 
			throw new Exception(e);
			}
			return objsList;
		}
		@Override
		public List<User> getDyHodList() throws Exception {
			List<User> objsList = null;
			try {
				String qry ="SELECT u.user_id as dy_hod_user_id_fk,u.user_name,u.designation,u.department_fk,u.reporting_to_id_srfk as reporting_to_id_srfk FROM [user] u " + 
						"left join [user] u1 on u.reporting_to_id_srfk = u1.user_id "
						+ "where u.designation is not null and u.designation <> '' and u.user_type_fk = ?";

				int arrSize = 1;
				Object[] pValues = new Object[arrSize];
				int i = 0;
				pValues[i++] = CommonConstants.USER_TYPE2;
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<User>(User.class));	
			}catch(Exception e){ 
			throw new Exception(e);
			}
			return objsList;
		}

		@Override
		public List<Contract> getContractorsList() throws Exception {
			List<Contract> objsList = null;
			try {
				String qry = "select contractor_id as contractor_id_fk,contractor_name from contractor";			
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));			
			}catch(Exception e){ 
				throw new Exception(e);
			}
			return objsList;
		}
		
		@Override
		public List<Contract> getContractTypeList()throws Exception{
			List<Contract> objsList = null;
			try {
				String qry ="select contract_type as contract_type_fk from contract_type";
					objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));	
			}catch(Exception e){ 
				e.printStackTrace();
			throw new Exception(e);
			}
			return objsList;
			
		}
		
		@Override
		public List<Contract> getInsurenceTypeList()throws Exception{
			List<Contract> objsList = null;
			try {
				String qry ="select insurance_type from insurance_type";
					objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));	
			}catch(Exception e){ 
				e.printStackTrace();
			throw new Exception(e);
			}
			return objsList;
		}	
		@Override
		public List<BankGuarantee> bankGuarantee()throws Exception{
			List<BankGuarantee> objsList = null;
			try {
				String qry ="select bg_type as bg_type_fk from bg_type";
					objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<BankGuarantee>(BankGuarantee.class));	
			}catch(Exception e){ 
				e.printStackTrace();
			throw new Exception(e);
			}
			return objsList;
		}
		@Override
		public List<Insurence> insurenceType()throws Exception{
			List<Insurence> objsList = null;
			try {
				String qry ="select insurance_type as insurance_type_fk from insurance_type";
					objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Insurence>(Insurence.class));	
			}catch(Exception e){ 
				e.printStackTrace();
			throw new Exception(e);
			}
			return objsList;
		}

		@Override
		public List<Contract> getResponsiblePeopleList(Contract obj) throws Exception {
			List<Contract> objsList = null;
			try {
				String qry = "SELECT user_id,user_name,designation,department_fk FROM [user] where user_name not like '%user%' and pmis_key_fk not like '%SGS%' and user_type_fk not in('Others') ";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));			
			}catch(Exception e){ 
				throw new Exception(e);
			}
			return objsList;
		}
		@Override
		public List<Contract> getUnitsList(Contract obj) throws Exception {
			List<Contract> objsList = null;
			try {
				String qry = "select id, unit, value from money_unit";			
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));			
			}catch(Exception e){ 
				throw new Exception(e);
			}
			return objsList;
		}

	
		
		@Override
		public List<Contract> getContractStatus() throws Exception {
			List<Contract> objsList = null;
			try {
				String qry =" select distinct contract_status from general_status ";
						
					objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));	
			}catch(Exception e){ 
				e.printStackTrace();
			throw new Exception(e);
			}
			return objsList;
		}
//
		@Override
		public Contract getContract(Contract obj)throws Exception{
			Connection con = null;
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			Contract contract = null;
			try{
				
				con = dataSource.getConnection();
				
			
				String contract_updateQry = "select dt.contract_id_code,c.project_id_fk,p.project_name,u.designation,u.user_name,contract_type_fk,c.contract_id,"
										+ "c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name,dt.department as department_fk,dt.department_name,c.hod_user_id_fk,c.dy_hod_user_id_fk,  " 
										+ "scope_of_contract,cast(estimated_cost as decimal(18,2)) as estimated_cost,FORMAT(date_of_start,'dd-MM-yyyy') AS date_of_start,"
										+ "FORMAT(doc,'dd-MM-yyyy') AS doc,cast(awarded_cost as decimal(18,2)) as awarded_cost,loa_letter_number,FORMAT(loa_date,'dd-MM-yyyy') AS loa_date,"
										+ "ca_no,FORMAT(ca_date,'dd-MM-yyyy') AS ca_date,FORMAT(c.actual_completion_date,'dd-MM-yyyy') AS actual_completion_date,"
										+ "FORMAT(contract_closure_date,'dd-MM-yyyy') AS contract_closure_date,FORMAT(completion_certificate_release,'dd-MM-yyyy') AS completion_certificate_release,"
										+ "FORMAT(final_takeover,'dd-MM-yyyy') AS final_takeover,FORMAT(final_bill_release,'dd-MM-yyyy') AS final_bill_release,FORMAT(defect_liability_period,'dd-MM-yyyy') AS defect_liability_period,cast(completed_cost as decimal(18,2)) as completed_cost,"
										+ "FORMAT(retention_money_release,'dd-MM-yyyy') AS retention_money_release,FORMAT(pbg_release,'dd-MM-yyyy') AS pbg_release,contract_status_fk,bg_required,"
										+ "insurance_required,u.designation as hod_designation,us.designation as dy_hod_designation,u.user_name as hod_name,us.user_name as dy_hod_name,FORMAT(target_doc,'dd-MM-yyyy') AS target_doc,"
										+ "awarded_cost_units,contract_ifas_code,estimated_cost_units,completed_cost_units,mu.unit,status,milestone_requried,revision_requried,contractors_key_requried,FORMAT(actual_date_of_commissioning,'dd-MM-yyyy') AS actual_date_of_commissioning,is_contract_closure_initiated,FORMAT(tender_opening_date,'dd-MM-yyyy') as tender_opening_date ,FORMAT(technical_eval_submission,'dd-MM-yyyy') as technical_eval_submission,FORMAT(financial_eval_submission,'dd-MM-yyyy') as financial_eval_submission ,FORMAT(notice_inviting_tender,'dd-MM-yyyy') AS contract_notice_inviting_tender,FORMAT(planned_date_of_award,'dd-MM-yyyy') AS planned_date_of_award,c.remarks,FORMAT(planned_date_of_completion,'dd-MM-yyyy') AS planned_date_of_completion,c.contract_department,c.bank_funded,c.bank_name,c.type_of_review " + 
										"from contract c " + 
										"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
										"left join project p on c.project_id_fk = p.project_id " + 
										"left join [user] u on c.hod_user_id_fk = u.user_id "+
										"left join [user] us on c.dy_hod_user_id_fk = us.user_id "+
										"left join money_unit mu on c.completed_cost_units = mu.value  "
										+"left join department dt on c.contract_department = dt.department where contract_id = ?" ;
				stmt = con.prepareStatement(contract_updateQry);
				stmt.setString(1, obj.getContract_id());
			
				resultSet = stmt.executeQuery();
				if(resultSet.next()) {
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
					contract.setDepartment_name(resultSet.getString("department_name"));
					contract.setContract_department(resultSet.getString("contract_department"));
					
					contract.setBank_funded(resultSet.getString("bank_funded"));
					contract.setBank_name(resultSet.getString("bank_name"));
					contract.setType_of_review(resultSet.getString("type_of_review"));
					
					contract.setHod_user_id_fk(resultSet.getString("hod_user_id_fk"));
					contract.setDy_hod_user_id_fk(resultSet.getString("dy_hod_user_id_fk"));
					
					contract.setHod_designation(resultSet.getString("hod_designation"));
					contract.setDy_hod_designation(resultSet.getString("dy_hod_designation"));
					contract.setHod_name(resultSet.getString("hod_name"));
					contract.setDy_hod_name(resultSet.getString("dy_hod_name"));
					
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
					contract.setTarget_doc(resultSet.getString("target_doc"));
					contract.setAwarded_cost_units(resultSet.getString("awarded_cost_units"));
					contract.setContract_ifas_code(resultSet.getString("contract_ifas_code"));
					contract.setEstimated_cost_units(resultSet.getString("estimated_cost_units"));
					contract.setCompleted_cost_units(resultSet.getString("completed_cost_units"));
					contract.setUnit(resultSet.getString("unit"));
					contract.setStatus(resultSet.getString("status"));
					contract.setMilestone_requried(resultSet.getString("milestone_requried"));
					contract.setRevision_requried(resultSet.getString("revision_requried"));
					contract.setContractors_key_requried(resultSet.getString("contractors_key_requried"));
					contract.setActual_date_of_commissioning(resultSet.getString("actual_date_of_commissioning"));
					contract.setIs_contract_closure_initiated(resultSet.getString("is_contract_closure_initiated"));
					contract.setPlanned_date_of_award(resultSet.getString("planned_date_of_award"));
					
					contract.setContract_notice_inviting_tender(resultSet.getString("contract_notice_inviting_tender"));
					
					contract.setPlanned_date_of_completion(resultSet.getString("planned_date_of_completion"));
					
					
					contract.setTender_opening_date(resultSet.getString("tender_opening_date"));
					contract.setTechnical_eval_submission(resultSet.getString("technical_eval_submission"));
					contract.setFinancial_eval_submission(resultSet.getString("financial_eval_submission"));
					
					
					
					
					
					
					contract.setRemarks(resultSet.getString("remarks"));

					contract.setBankGauranree(getBankGauranree(contract.getContract_id(),con));	
					
					contract.setContractGstDetails(getContractGstDetails(contract.getContract_id(),con));	
					
					
					
					contract.setInsurence(getInsurence(contract.getContract_id(),con));	
					contract.setMilestones(getMilestones(contract.getContract_id(),con));
					contract.setContract_revisions(getContract_revisions(contract.getContract_id(),con));
					contract.setContract_revision(getContract_revision(contract.getContract_id(),con));	
					
					contract.setContractKeyPersonnels(getContractKeyPersonnels(contract.getContract_id(),con));	
					contract.setContractDocuments(getContractDocuments(contract.getContract_id(),con));
					
					contract.setDepartmentList(getDepartmentList(contract.getContract_id(),con));
					
				}
				
				if (!StringUtils.isEmpty(obj.getMessage_id())) {
					NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
					String msgUpdateqry = "UPDATE messages SET read_time=CURRENT_TIMESTAMP where message_id = :message_id";

					BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
					template.update(msgUpdateqry, paramSource);
				}
				if (!StringUtils.isEmpty(obj.getAlerts_user_id())) {
					NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
					String msgUpdateqry = "UPDATE alerts_user SET read_time = CURRENT_TIMESTAMP where alerts_user_id = :alerts_user_id";

					BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
					template.update(msgUpdateqry, paramSource);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(con, stmt, null);
			}		
			return contract;	
		}
		
		public static String trimToNull(String str) {
		    if (str == null) {
		        return null;
		    }
		    String trimmed = str.trim();
		    return trimmed.isEmpty() ? null : trimmed;
		}

		private List<Contract> getDepartmentList(String contract_id, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> objsList = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="SELECT distinct contract_id_fk, department_id_fk,department_name, string_agg(executive_user_id_fk,',') as executive_user_id_fk from contract_executive ce  "
						+ "Left JOIN department dt on ce.department_id_fk = dt.department  "
						+ " where contract_id_fk = ? group by contract_id_fk,department_id_fk,department_name ";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					obj.setDepartment_id_fk(resultSet.getString("department_id_fk"));
					obj.setExecutive_user_id_fk(resultSet.getString("executive_user_id_fk"));
					obj.setDepartment_name(resultSet.getString("department_name"));
					obj.setDepartment_fk(obj.getDepartment_id_fk());
					obj.setResponsiblePersonsList(getExecutivesListForContractForm(obj));
					obj.setExecutivesList(getExecutivesList(contract_id,obj.getDepartment_id_fk(),con));
					objsList.add(obj);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
			}
			return objsList;
		}
		
		private List<Contract> getExecutivesList(String contract_id,String departmentID, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> objsList = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="SELECT executive_user_id_fk,u.user_name,u.designation from contract_executive c "
						+ "left join [user] u on c.executive_user_id_fk = u.user_id where contract_id_fk = ? and  department_id_fk = ?"
						+ " ORDER BY case when u.designation='ED Civil' then 1 " + 
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
						"   end asc";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				stmt.setString(2, departmentID);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					obj.setExecutive_user_id_fk(resultSet.getString("executive_user_id_fk"));
					obj.setUser_name(resultSet.getString("user_name"));
					obj.setDesignation(resultSet.getString("designation"));
					objsList.add(obj);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
			}
			return objsList;
		}
		private List<Contract> getContractDocuments(String contract_id, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> objsList = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="SELECT contract_documents_id as contract_file_id, contract_id_fk, name, attachment, contract_file_type_fk from contract_documents where contract_id_fk = ?";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					obj.setContract_file_id(resultSet.getString("contract_file_id"));
					obj.setName(resultSet.getString("name"));
					obj.setAttachment(resultSet.getString("attachment"));
					obj.setContract_file_type_fk(resultSet.getString("contract_file_type_fk"));
					objsList.add(obj);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
			}
			return objsList;
		}

		private List<Contract> getContractKeyPersonnels(String contract_id, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> objsList = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="SELECT name,mobile_no,email_id,designation from contract_key_personnel where contract_id_fk = ?";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					obj.setName(resultSet.getString("name"));
					obj.setMobile_no(String.valueOf(resultSet.getString("mobile_no")));
					obj.setEmail_id(resultSet.getString("email_id"));
					obj.setDesignation(resultSet.getString("designation"));
					objsList.add(obj);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
			}
			return objsList;
		}
		
		private List<Contract> getContract_revisions(String contract_id, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> contract_revisions = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="select revision_no as revisionnumber,revision_estimated_cost as revisionestimatedcost,revision_planned_date_of_award as revisionplanneddateofaward,revision_planned_date_of_completion as revisionplanneddateofcompletion,notice_inviting_tender as noticeinvitingtender," + 
						"tender_bid_opening_date as tenderbidopeningdate,technical_eval_approval as technicalevalapproval,financial_eval_approval as financialevalapproval,tender_bid_remarks as tenderbidremarks from contract_revisions  "
						+ " where contract_id_fk = ?";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					obj.setRevisionnumber(resultSet.getString("revisionnumber"));
					obj.setRevisionestimatedcost(resultSet.getString("revisionestimatedcost"));
					obj.setRevisionplanneddateofaward(resultSet.getString("revisionplanneddateofaward"));
					obj.setRevisionplanneddateofcompletion(resultSet.getString("revisionplanneddateofcompletion"));
					obj.setNoticeinvitingtender(resultSet.getString("noticeinvitingtender"));
					

					obj.setTenderbidopeningdate(resultSet.getString("tenderbidopeningdate"));
					obj.setTechnicalevalapproval(resultSet.getString("technicalevalapproval"));
					obj.setFinancialevalapproval(resultSet.getString("financialevalapproval"));
					obj.setTenderbidremarks(resultSet.getString("tenderbidremarks"));
					
					
					
					contract_revisions.add(obj);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
			}
			return contract_revisions;
		}	

		private List<Contract> getContract_revision(String contract_id, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> contract_revision = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="SELECT revision_number,revised_amount,revised_amount_units ,FORMAT(revised_doc,'dd-MM-yyyy') AS revised_doc,approval_by_bank,attachment"
						+ ",action as revision_status,remarks,mu.unit,revision_amounts_status from contract_revision cr "+
						"left join money_unit mu on cr.revised_amount_units = mu.value  "
						+ " where contract_id_fk = ?";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					obj.setRevision_number(resultSet.getString("revision_number"));
					obj.setRevised_amount(resultSet.getString("revised_amount"));
					obj.setRevised_amount_units(resultSet.getString("revised_amount_units"));
					obj.setRevised_doc(resultSet.getString("revised_doc"));
					obj.setRevision_status(resultSet.getString("revision_status"));
					obj.setRemarks(resultSet.getString("remarks"));
					obj.setUnit(resultSet.getString("unit"));
					obj.setRevision_amounts_status(resultSet.getString("revision_amounts_status"));
					obj.setApprovalbybank(resultSet.getString("approval_by_bank"));
					obj.setAttachment(resultSet.getString("attachment"));
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
		

		private List<Contract> getMilestones(String contract_id, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> milestones = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="SELECT contract_milestones_id,milestone_id,milestone_name,FORMAT(milestone_date,'dd-MM-yyyy') AS milestone_date,FORMAT(actual_date,'dd-MM-yyyy') AS actual_date, revision"
						+ ",remarks from contract_milestones where contract_id_fk = ? and status = ?";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				stmt.setString(2, CommonConstants.ACTIVE);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					obj.setContract_milestones_id(resultSet.getString("contract_milestones_id"));
					obj.setMilestone_id(resultSet.getString("milestone_id"));
					obj.setMilestone_name(resultSet.getString("milestone_name"));
					obj.setMilestone_date(resultSet.getString("milestone_date"));
					obj.setActual_date(resultSet.getString("actual_date"));
					obj.setRevision(resultSet.getString("revision"));
					obj.setRemarks(resultSet.getString("remarks"));
					milestones.add(obj);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
			}
			return milestones;
		}

		private List<Contract> getInsurence(String contract_id, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> insurence = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="SELECT insurance_type_fk,issuing_agency,agency_address,insurance_number,insurance_value,FORMAT(valid_upto,'dd-MM-yyyy') AS valid_upto"
						+ ",released_fk as insurance_status,insurance_value_units,mu.unit from insurance i "+
						"left join money_unit mu on i.insurance_value_units = mu.value  "
						+ "where contract_id_fk = ?";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					obj.setInsurance_type_fk(resultSet.getString("insurance_type_fk"));
					obj.setIssuing_agency(resultSet.getString("issuing_agency"));
					obj.setAgency_address(resultSet.getString("agency_address"));
					obj.setInsurance_number(resultSet.getString("insurance_number"));
					obj.setInsurance_value(resultSet.getString("insurance_value"));
					obj.setInsurence_valid_upto(resultSet.getString("valid_upto"));
			
					obj.setInsurance_status(resultSet.getString("insurance_status"));
					obj.setInsurance_value_units(resultSet.getString("insurance_value_units"));
					obj.setUnit(resultSet.getString("unit"));
					insurence.add(obj);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
			}
			return insurence;
		}
		
		
		private List<Contract> getContractGstDetails(String contract_id, Connection con) throws Exception {
		       String qry = "SELECT [includes_gst], " +
	                   "      [gst_rate], " +
	                   "      [is_composite], " +
	                   "      [is_varying_price], " +
	                   "      FORMAT(base_month,'dd-MM-yyyy') as [base_month], " +
	                   "      [retention_amount], " +
	                   "      [retention_deduction_rate], " +
	                   "      FORMAT(retention_validity,'dd-MM-yyyy') as [retention_validity], " +
	                   "      [mobilization_advance], " +
	                   "      [mob_deduction_rate], " +
	                   "      FORMAT(applicable_date,'dd-MM-yyyy') as [applicable_date], " +
	                   "      [created_at], " +
	                   "      [updated_at], " +
	                   "      [contract_id_fk] " +
	                   "  FROM [pmis].[dbo].[contract_gst_details] " +
	                   "WHERE contract_id_fk = ?";

	      List<Contract> contractGstDetails = new ArrayList<>();
	      Contract obj = null;

	      try (PreparedStatement stmt = con.prepareStatement(qry)) {
	          stmt.setString(1, contract_id);
	          ResultSet resultSet = stmt.executeQuery();

	          while (resultSet.next()) {
	              obj = new Contract();
	              obj.setContract_value_gst(resultSet.getString("includes_gst"));
	              obj.setGst_rate(resultSet.getString("gst_rate"));
	              obj.setComposite_contract(resultSet.getString("is_composite"));
	              obj.setPrice_variation(resultSet.getString("is_varying_price"));
	              obj.setBase_month(resultSet.getString("base_month"));
	              obj.setRetention_amount(resultSet.getString("retention_amount"));
	              obj.setRate_of_deduction_retention(resultSet.getString("retention_deduction_rate"));
	              obj.setRetention_validity(resultSet.getString("retention_validity"));
	              obj.setMobilisation_advance(resultSet.getString("mobilization_advance"));
	              obj.setRate_of_deduction_advance(resultSet.getString("mob_deduction_rate"));
	              obj.setApplicable_till(resultSet.getString("applicable_date"));

	              contractGstDetails.add(obj);
	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      }

	      return contractGstDetails;
		}	

		private List<Contract> getBankGauranree(String contract_id, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet resultSet = null;
			List<Contract> bankGauranree = new ArrayList<Contract>();
			Contract obj = null;
			try {
				String qry ="SELECT bg_type_fk,issuing_bank, bg_number,bg_value,FORMAT(valid_upto,'dd-MM-yyyy') AS valid_upto"
						+ ",FORMAT(bg_date,'dd-MM-yyyy') AS bg_date,FORMAT(release_date,'dd-MM-yyyy') AS release_date,bg_value_units,mu.unit "
						+ " from bank_guarantee bg "+
						"left join money_unit mu on bg.bg_value_units = mu.value  "
						+ "where contract_id_fk = ?";
				stmt = con.prepareStatement(qry);
				stmt.setString(1, contract_id);
				resultSet = stmt.executeQuery();
				while(resultSet.next()) {
					obj = new Contract();
					
					//obj.setCode(resultSet.getString("code"));
					obj.setBg_type_fk(resultSet.getString("bg_type_fk"));
					obj.setIssuing_bank(resultSet.getString("issuing_bank"));
					obj.setBg_number(resultSet.getString("bg_number"));
					obj.setBg_value(resultSet.getString("bg_value"));
					obj.setBg_valid_upto(resultSet.getString("valid_upto"));
					obj.setBg_date(resultSet.getString("bg_date"));
					obj.setRelease_date(resultSet.getString("release_date"));
					obj.setBg_value_units(resultSet.getString("bg_value_units"));
					obj.setUnit(resultSet.getString("unit"));
					bankGauranree.add(obj);
				}
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, resultSet);
			}
			return bankGauranree;
		}
		
		public List<Contract> getExecutivesListForContractForm(Contract obj) throws Exception {
			List<Contract> objsList = null;
			try {
				
				String qry ="SELECT u.user_id as hod_user_id_fk,u.user_name,u.designation,u.department_fk "
						+ "FROM [user] u " 
						+ "left join department d on u.department_fk = d.department "
						+ "where  user_id is not null and user_type_fk <> ''  and u.user_type_fk not in('Others')  ";
				
				int arrSize = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and u.department_fk = ? ";
					arrSize++;
				}
				qry = qry + " and user_name not like '%user%' and pmis_key_fk not like '%SGS%'";// and department_fk in(Engg,Elec,S&T) 
				
				qry = qry + " ORDER BY 					case when user_type_fk='HOD' then 1" + 
						"					when user_type_fk='DYHOD' then 2" + 
						"					when user_type_fk='Officers ( Jr./Sr. Scale )' then 3" + 
						"					when user_type_fk='Others' then 4" + 
						"					end asc ,"
						+ "case when u.designation='ED Civil' then 1 " + 
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
						"   end asc";
				
				Object[] pValues = new Object[arrSize];
				int i = 0;
				//pValues[i++] = CommonConstants.USER_TYPE_HOD;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
				}
				
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
					
			}catch(Exception e){ 
				throw new Exception(e);
			}
			return objsList;
		}

		@Override
		public List<Contract> getBankNameList(Contract obj) throws Exception {
			List<Contract> objsList = null;
			try {
				String qry ="select bank_name from bank_name";
					objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Contract>(Contract.class));	
			}catch(Exception e){ 
				e.printStackTrace();
			throw new Exception(e);
			}
			return objsList;
		}

		@Override
		public List<Contract> getContractStatusType(Contract obj)throws Exception{
			List<Contract> objsList = null;
			try {
				//String qry ="select general_status as contract_status_fk,contract_status  from general_status  WHERE general_status NOT IN (Commissioned, Dropped,On Hold) ";
				String qry ="select general_status as contract_status_fk,contract_status  from general_status  WHERE general_status NOT IN ('Terminated','Not Started') ";
				int arrSize = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
					qry = qry + " and contract_status = ? ";
					arrSize++;
				}
				qry = qry + "    ORDER BY case when general_status='Commissioned' then 1" + 
						"   when general_status='Completed' then 2" + 
						"   when general_status='In Progress' then 3" + 
						"   when general_status='On Hold' then 4" + 
						"   when general_status='Dropped' then 5" + 
						"   when general_status='Not Started' then 6 end asc";
				Object[] pValues = new Object[arrSize];
				int i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_status())) {
					pValues[i++] = obj.getContract_status();
				}
					objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));	
			}catch(Exception e){ 
				e.printStackTrace();
			throw new Exception(e);
			}
			return objsList;
		}
}