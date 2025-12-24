package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.DTO.BankGuarantee;
import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.Insurence;
import com.wcr.wcrbackend.DTO.Messages;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DBConnectionHandler;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.common.FileUploads;

@Repository
public class ContractRepository implements IContractRepo {

	@Autowired
	DataSource dataSource;
	
	@Autowired
	MessagesDao messagesDao;
	
	@Autowired
	FormsHistoryDao formsHistoryDao;
	
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
		
		
		@Override
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
		
		@Override
		public String addContract(Contract contract)throws Exception{
			Connection con = null;
			PreparedStatement stmt = null;
			String contract_id=null;
			//ResultSet rs = null;
			int count = 0;
			boolean flag = false;
			int[] c = {};
			try{
				con = dataSource.getConnection();
				con.setAutoCommit(false);
				
				if(StringUtils.isEmpty(contract.getContract_status()) || "No".equals(contract.getContract_status())) {
					contract.setContract_status(null);
					contract.setContract_status_fk("Not Awarded");
				}
				if(!StringUtils.isEmpty(contract.getContract_status()) && "Yes".equals(contract.getContract_status())) {
					contract.setContract_status("Open");
				}
				String Contract_id_code=getDepartmentCode(contract.getContract_department(),con);
				
				contract_id = getContractIdByWorkId(contract.getProject_id_fk(),Contract_id_code,con);
				contract.setContract_id(contract_id);
				String ContractQry = "INSERT INTO contract "
								+"(contract_id,project_id_fk,contract_name,contract_short_name,contractor_id_fk,contract_type_fk,scope_of_contract,hod_user_id_fk,"
								+ "dy_hod_user_id_fk,doc,awarded_cost,loa_letter_number,loa_date,ca_no,ca_date,actual_completion_date,completed_cost,date_of_start,"
								+ "estimated_cost,contract_closure_date,completion_certificate_release,final_takeover,final_bill_release,defect_liability_period,"
								+ "retention_money_release,pbg_release,contract_status_fk,bg_required,insurance_required,estimated_cost_units,awarded_cost_units,"
								+ "status,milestone_requried,revision_requried,contractors_key_requried,is_contract_closure_initiated,planned_date_of_award,remarks,planned_date_of_completion,contract_department,bank_funded,bank_name,type_of_review,notice_inviting_tender,tender_opening_date,technical_eval_submission,financial_eval_submission,contract_ifas_code)"
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				stmt = con.prepareStatement(ContractQry);
				int q = 1;
			    int r =0;
				stmt.setString(q++,contract_id); 
				stmt.setString(q++,contract.getProject_id_fk()); 
				//stmt.setString(q++,contract.getDepartment_fk()); 
				stmt.setString(q++,contract.getContract_name()); 
				stmt.setString(q++,contract.getContract_short_name()); 
				stmt.setString(q++,contract.getContractor_id_fk()); 
				stmt.setString(q++,contract.getContract_type_fk());
				stmt.setString(q++,contract.getScope_of_contract()); 
				stmt.setString(q++,contract.getHod_user_id_fk());
				stmt.setString(q++,contract.getDy_hod_user_id_fk());
				stmt.setString(q++,contract.getDoc());
				stmt.setString(q++,contract.getAwarded_cost());
				stmt.setString(q++,contract.getLoa_letter_number());
				stmt.setString(q++,contract.getLoa_date());
				stmt.setString(q++,contract.getCa_no());
				stmt.setString(q++,contract.getCa_date());
				stmt.setString(q++,contract.getActual_completion_date());
				stmt.setString(q++,contract.getCompleted_cost()); 
				stmt.setString(q++,contract.getDate_of_start()); 
				stmt.setString(q++,contract.getEstimated_cost()); 
				stmt.setString(q++,contract.getContract_closure_date()); 
				stmt.setString(q++,contract.getCompletion_certificate_release()); 
				stmt.setString(q++,contract.getFinal_takeover()); 
				stmt.setString(q++,contract.getFinal_bill_release()); 
				stmt.setString(q++,contract.getDefect_liability_period()); 
				stmt.setString(q++,contract.getRetention_money_release()); 
				stmt.setString(q++,contract.getPbg_release()); 
				stmt.setString(q++,contract.getContract_status_fk()); 
				stmt.setString(q++,contract.getBg_required()); 
				stmt.setString(q++,contract.getBg_required());
				stmt.setString(q++,contract.getEstimated_cost_units());
				stmt.setString(q++,contract.getAwarded_cost_units());
				stmt.setString(q++,contract.getContract_status());
				stmt.setString(q++,contract.getMilestone_requried());
				stmt.setString(q++,contract.getRevision_requried());
				stmt.setString(q++,contract.getContractors_key_requried());
				stmt.setString(q++,contract.getIs_contract_closure_initiated());
				stmt.setString(q++,contract.getPlanned_date_of_award());
				stmt.setString(q++,contract.getRemarks());
				stmt.setString(q++,contract.getPlanned_date_of_completion());
				stmt.setString(q++,contract.getContract_department());
				stmt.setString(q++,contract.getBank_funded());
				stmt.setString(q++,contract.getBank_name());
				stmt.setString(q++,contract.getType_of_review());
				
				
				if(!StringUtils.isEmpty(contract.getContract_notice_inviting_tender()))
				{
					SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatter3 = new SimpleDateFormat("dd-MM-yyyy");
					Date date24 = null;
					String dateString24 = null;
					date24 = formatter3.parse(contract.getContract_notice_inviting_tender());
					dateString24 = formatter2.format(date24);
					stmt.setString(q++,dateString24);
				}
				else
				{
					stmt.setString(q++,contract.getContract_notice_inviting_tender());
				}
				
				
				if(!StringUtils.isEmpty(contract.getTender_opening_date()))
				{
					SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatter3 = new SimpleDateFormat("dd-MM-yyyy");
					Date date24 = null;
					String dateString24 = null;
					date24 = formatter3.parse(contract.getTender_opening_date());
					dateString24 = formatter2.format(date24);
					stmt.setString(q++,dateString24);
				}
				else
				{
					stmt.setString(q++,contract.getTender_opening_date());
				}	
				
				
				if(!StringUtils.isEmpty(contract.getTechnical_eval_submission()))
				{
					SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatter3 = new SimpleDateFormat("dd-MM-yyyy");
					Date date24 = null;
					String dateString24 = null;
					date24 = formatter3.parse(contract.getTechnical_eval_submission());
					dateString24 = formatter2.format(date24);
					stmt.setString(q++,dateString24);
				}
				else
				{
					stmt.setString(q++,contract.getTechnical_eval_submission());
				}	
				

				if(!StringUtils.isEmpty(contract.getFinancial_eval_submission()))
				{
					SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat formatter3 = new SimpleDateFormat("dd-MM-yyyy");
					Date date24 = null;
					String dateString24 = null;
					date24 = formatter3.parse(contract.getFinancial_eval_submission());
					dateString24 = formatter2.format(date24);
					stmt.setString(q++,dateString24);
				}
				else
				{
					stmt.setString(q++,contract.getFinancial_eval_submission());
					
					
				}				
				
				stmt.setString(q++,contract.getContract_ifas_code());
				
				count = stmt.executeUpdate();
				
				if(count > 0) {
					flag = true;
				}
				if(stmt != null){stmt.close();}
				
				int arraySize = 0;
				if(flag) {
					
					/*******************************************************************************************/
					if(!StringUtils.isEmpty(contract.getResponsible_people_id_fks()) && contract.getResponsible_people_id_fks().length > 0) {
						contract.setResponsible_people_id_fks(CommonMethods.replaceEmptyByNullInSringArray(contract.getResponsible_people_id_fks()));
						if(arraySize < contract.getResponsible_people_id_fks().length) {
							arraySize = contract.getResponsible_people_id_fks().length;
						}
					}
					
					if(!StringUtils.isEmpty(contract.getDepartment_fks()) && contract.getDepartment_fks().length > 0) {
						contract.setDepartment_fks(CommonMethods.replaceEmptyByNullInSringArray(contract.getDepartment_fks()));
						if(arraySize < contract.getDepartment_fks().length) {
							arraySize = contract.getDepartment_fks().length;
						}
					}
					
					if(!StringUtils.isEmpty(contract.getDepartment_fks())) {
						String file_insert_qry = "INSERT into  contract_executive (contract_id_fk, department_id_fk, executive_user_id_fk) VALUES (?,?,?)";
						PreparedStatement multiExecutiveStmt = con.prepareStatement(file_insert_qry);
						int len = contract.getDepartment_fks().length;
						for(int i =0; i< len; i++) {
							int  j = 0;
							while ( j < contract.getFilecounts()[i] ){
								int ffffff = contract.getFilecounts()[i];
		   int k = 1;
		   int a = r++;  
		   if(i <= (len)){
		   	String deptName = contract.getDepartment_fks()[i];
		   	if(!StringUtils.isEmpty(deptName)) {
										multiExecutiveStmt.setString(k++,(contract.getContract_id()));
										multiExecutiveStmt.setString(k++,(deptName));
										multiExecutiveStmt.setString(k++,(contract.getResponsible_people_id_fks().length > 0)?contract.getResponsible_people_id_fks()[a]:null);
										multiExecutiveStmt.addBatch();
		   	}
									j++;
		   }else{
		   	j++;
		   }
							}
						}
						int[] insertCount = multiExecutiveStmt.executeBatch();
						if(multiExecutiveStmt != null){multiExecutiveStmt.close();}
					}
					
					/*******************************************************************************************/
					
					String BankG_qry = "INSERT into  bank_guarantee (bg_type_fk,issuing_bank,"
							+"bg_number,bg_value,valid_upto,contract_id_fk,bg_date,release_date,bg_value_units) "
							+"VALUES (?,?,?,?,?,?,?,?,?)";
					stmt = con.prepareStatement(BankG_qry);
			
					if(!StringUtils.isEmpty(contract.getBg_type_fks()) && contract.getBg_type_fks().length > 0) {
						contract.setBg_type_fks(CommonMethods.replaceEmptyByNullInSringArray(contract.getBg_type_fks()));
						if(arraySize < contract.getBg_type_fks().length) {
							arraySize = contract.getBg_type_fks().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getCodes()) && contract.getCodes().length > 0) {
						contract.setCodes(CommonMethods.replaceEmptyByNullInSringArray(contract.getCodes()));
						if(arraySize < contract.getCodes().length) {
							arraySize = contract.getCodes().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getBg_dates()) && contract.getBg_dates().length > 0) {
						contract.setBg_dates(CommonMethods.replaceEmptyByNullInSringArray(contract.getBg_dates()));
						if(arraySize < contract.getBg_dates().length) {
							arraySize = contract.getBg_dates().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getRelease_dates()) && contract.getRelease_dates().length > 0) {
						contract.setRelease_dates(CommonMethods.replaceEmptyByNullInSringArray(contract.getRelease_dates()));
						if(arraySize < contract.getRelease_dates().length) {
							arraySize = contract.getRelease_dates().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getIssuing_banks()) && contract.getIssuing_banks().length > 0) {
						contract.setIssuing_banks(CommonMethods.replaceEmptyByNullInSringArray(contract.getIssuing_banks()));
						if(arraySize < contract.getIssuing_banks().length) {
							arraySize = contract.getIssuing_banks().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getBg_numbers()) && contract.getBg_numbers().length > 0) {
						contract.setBg_numbers(CommonMethods.replaceEmptyByNullInSringArray(contract.getBg_numbers()));
						if(arraySize < contract.getBg_numbers().length) {
							arraySize = contract.getBg_numbers().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getBg_values()) && contract.getBg_values().length > 0) {
						contract.setBg_values(CommonMethods.replaceEmptyByNullInSringArray(contract.getBg_values()));
						if(arraySize < contract.getBg_values().length) {
							arraySize = contract.getBg_values().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getBg_valid_uptos()) && contract.getBg_valid_uptos().length > 0) {
						contract.setBg_valid_uptos(CommonMethods.replaceEmptyByNullInSringArray(contract.getBg_valid_uptos()));
						if(arraySize < contract.getBg_valid_uptos().length) {
							arraySize = contract.getBg_valid_uptos().length;
						}
					}	
					if(!StringUtils.isEmpty(contract.getBg_value_unitss()) && contract.getBg_value_unitss().length > 0) {
						contract.setBg_value_unitss(CommonMethods.replaceEmptyByNullInSringArray(contract.getBg_value_unitss()));
						if(arraySize < contract.getBg_value_unitss().length) {
							arraySize = contract.getBg_value_unitss().length;
						}
					}	
					
					if(!StringUtils.isEmpty(contract.getBg_type_fks()) && contract.getBg_type_fks().length > 0 && !StringUtils.isEmpty(contract.getBg_valid_uptos())) {
					    for (int i = 0; i < arraySize; i++) {
							int k = 1;
							if( contract.getBg_type_fks().length > 0 && !StringUtils.isEmpty(contract.getBg_type_fks()[i])) {
								stmt.setString(k++,(contract.getBg_type_fks().length > 0)?contract.getBg_type_fks()[i]:null);
								stmt.setString(k++,(contract.getIssuing_banks().length > 0)?contract.getIssuing_banks()[i]:null);
								stmt.setString(k++,(contract.getBg_numbers().length > 0)?contract.getBg_numbers()[i]:null);
								stmt.setString(k++,(contract.getBg_values().length > 0)?contract.getBg_values()[i]:null);
								stmt.setString(k++,DateParser.parse((contract.getBg_valid_uptos().length > 0)?contract.getBg_valid_uptos()[i]:null));
								stmt.setString(k++,contract.getContract_id());
								//stmt.setString(k++,(contract.getCodes().length > 0)?contract.getCodes()[i]:null);
								stmt.setString(k++,DateParser.parse((contract.getBg_dates().length > 0)?contract.getBg_dates()[i]:null));
								stmt.setString(k++,DateParser.parse((contract.getRelease_dates().length > 0)?contract.getRelease_dates()[i]:null));
								stmt.setString(k++,(contract.getBg_value_unitss().length > 0)?contract.getBg_value_unitss()[i]:null);
								stmt.addBatch();
							}
						}
					}
				    c = stmt.executeBatch();
					if(stmt != null){stmt.close();}
					
					if((!StringUtils.isEmpty(contract.getRevision_estimated_cost()) && contract.getRevision_estimated_cost().length>0 && contract.getRevision_estimated_cost()!=null) || 
							
							(!StringUtils.isEmpty(contract.getRevision_planned_date_of_award()) && contract.getRevision_planned_date_of_award().length>0 && contract.getRevision_planned_date_of_award()!=null)
							
							|| (!StringUtils.isEmpty(contract.getRevision_planned_date_of_completion()) && contract.getRevision_planned_date_of_completion().length>0 && contract.getRevision_planned_date_of_completion()!=null)
							|| !StringUtils.isEmpty(contract.getRevisionno())
							)
					{
						PreparedStatement stmtRevision = null;
						
						String deleteQryRevision = "DELETE from contract_revisions where contract_id_fk = ?";		 
						stmtRevision = con.prepareStatement(deleteQryRevision);
						stmtRevision.setString(1,contract.getContract_id()); 
						stmtRevision.executeUpdate();
						if(stmtRevision != null){stmtRevision.close();}					
						
						
						PreparedStatement preparedStmtRevisions = null;
						String queryTdcRevisions = " insert into contract_revisions (contract_id_fk,revision_no,revision_estimated_cost,revision_planned_date_of_award,revision_planned_date_of_completion,notice_inviting_tender,tender_bid_opening_date,technical_eval_approval,financial_eval_approval,tender_bid_remarks)"
					               + " values (?,?,?,?,?,?,?,?,?,?)";				
						
						preparedStmtRevisions = con.prepareStatement(queryTdcRevisions);
		
						if(!StringUtils.isEmpty(contract) && !StringUtils.isEmpty(contract.getRevisionno())) 
						{

							for (int i = 0; i < contract.getRevisionno().length; i++) 
							{
								/*if(!StringUtils.isEmpty(contract.getRevision_estimated_cost()[i]) || !StringUtils.isEmpty(contract.getRevision_planned_date_of_award()[i]) || !StringUtils.isEmpty(contract.getRevision_planned_date_of_completion()[i]) || !StringUtils.isEmpty(contract.getNotice_inviting_tender()[i]))
								{*/
									preparedStmtRevisions.setString(1, contract.getContract_id());
									preparedStmtRevisions.setString(2, contract.getRevisionno()[i]);
									if(contract.getRevision_estimated_cost().length>0)
									{
										preparedStmtRevisions.setString(3, contract.getRevision_estimated_cost()[i]==null || contract.getRevision_estimated_cost()[i]==""?"0":contract.getRevision_estimated_cost()[i]);
									}
									else
									{
										preparedStmtRevisions.setString(3,null);
									}
									if(contract.getRevision_planned_date_of_award().length>0)
									{
										preparedStmtRevisions.setString(4, contract.getRevision_planned_date_of_award()[i]==null?"0":contract.getRevision_planned_date_of_award()[i]);
									}
									else
									{
										preparedStmtRevisions.setString(4,null);
									}									
									if(contract.getRevision_planned_date_of_completion().length>0)
									{
										preparedStmtRevisions.setString(5, contract.getRevision_planned_date_of_completion()[i]==null?"0":contract.getRevision_planned_date_of_completion()[i]);
									}
									else
									{
										preparedStmtRevisions.setString(5,null);
									}	
									
									
									if(contract.getNotice_inviting_tender().length>0)
									{
										preparedStmtRevisions.setString(6, contract.getNotice_inviting_tender()[i]==null?"0":contract.getNotice_inviting_tender()[i]);
									}
									else
									{
										preparedStmtRevisions.setString(6,null);
									}
									
									
									
									
									if(contract.getTender_bid_opening_date().length>0)
									{
										preparedStmtRevisions.setString(7, contract.getTender_bid_opening_date()[i]==null?"0":contract.getTender_bid_opening_date()[i]);
									}
									else
									{
										preparedStmtRevisions.setString(7,null);
									}
									
									
									if(contract.getTechnical_eval_approval().length>0)
									{
										preparedStmtRevisions.setString(8, contract.getTechnical_eval_approval()[i]==null?"0":contract.getTechnical_eval_approval()[i]);
									}
									else
									{
										preparedStmtRevisions.setString(8,null);
									}								
									
									if(contract.getFinancial_eval_approval().length>0)
									{
										preparedStmtRevisions.setString(9, contract.getFinancial_eval_approval()[i]==null?"0":contract.getFinancial_eval_approval()[i]);
									}
									else
									{
										preparedStmtRevisions.setString(9,null);
									}
									
									
									if(contract.getTender_bid_remarks().length>0)
									{
										preparedStmtRevisions.setString(10, contract.getTender_bid_remarks()[i]==null?"0":contract.getTender_bid_remarks()[i]);
									}
									else
									{
										preparedStmtRevisions.setString(10,null);
									}								
									
									
									
									preparedStmtRevisions.execute();
								/* } */
							}
						}
						if(preparedStmtRevisions != null){preparedStmtRevisions.close();}	
					}
					
					
					String Insurence_qry = "INSERT into  insurance (insurance_type_fk,issuing_agency,agency_address,"
										+"insurance_number,insurance_value,valid_upto,contract_id_fk,released_fk,insurance_value_units) "
										+"VALUES (?,?,?,?,?,?,?,?,?)";
					stmt = con.prepareStatement(Insurence_qry); 
					arraySize = 0;
					if(!StringUtils.isEmpty(contract.getInsurance_type_fks()) && contract.getInsurance_type_fks().length > 0) {
						contract.setInsurance_type_fks(CommonMethods.replaceEmptyByNullInSringArray(contract.getInsurance_type_fks()));
						if(arraySize < contract.getInsurance_type_fks().length) {
							arraySize = contract.getInsurance_type_fks().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getIssuing_agencys()) && contract.getIssuing_agencys().length > 0) {
						contract.setIssuing_agencys(CommonMethods.replaceEmptyByNullInSringArray(contract.getIssuing_agencys()));
						if(arraySize < contract.getIssuing_agencys().length) {
							arraySize = contract.getIssuing_agencys().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getAgency_addresss()) && contract.getAgency_addresss().length > 0) {
						contract.setAgency_addresss(CommonMethods.replaceEmptyByNullInSringArray(contract.getAgency_addresss()));
						if(arraySize < contract.getAgency_addresss().length) {
							arraySize = contract.getAgency_addresss().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getInsurance_numbers()) && contract.getInsurance_numbers().length > 0) {
						contract.setInsurance_numbers(CommonMethods.replaceEmptyByNullInSringArray(contract.getInsurance_numbers()));
						if(arraySize < contract.getInsurance_numbers().length) {
							arraySize = contract.getInsurance_numbers().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getInsurance_values()) && contract.getInsurance_values().length > 0) {
						contract.setInsurance_values(CommonMethods.replaceEmptyByNullInSringArray(contract.getInsurance_values()));
						if(arraySize < contract.getInsurance_values().length) {
							arraySize = contract.getInsurance_values().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getInsurence_valid_uptos()) && contract.getInsurence_valid_uptos().length > 0) {
						contract.setInsurence_valid_uptos(CommonMethods.replaceEmptyByNullInSringArray(contract.getInsurence_valid_uptos()));
						if(arraySize < contract.getInsurence_valid_uptos().length) {
							arraySize = contract.getInsurence_valid_uptos().length;
						}
					}			
					if(!StringUtils.isEmpty(contract.getInsurence_remarks()) && contract.getInsurence_remarks().length > 0) {
						contract.setInsurence_remarks(CommonMethods.replaceEmptyByNullInSringArray(contract.getInsurence_remarks()));
						if(arraySize < contract.getInsurence_remarks().length) {
							arraySize = contract.getInsurence_remarks().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getInsurance_revisions()) && contract.getInsurance_revisions().length > 0) {
						contract.setInsurance_revisions(CommonMethods.replaceEmptyByNullInSringArray(contract.getInsurance_revisions()));
						if(arraySize < contract.getInsurance_revisions().length) {
							arraySize = contract.getInsurance_revisions().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getInsuranceStatus()) && contract.getInsuranceStatus().length > 0) {
						contract.setReleased_fks(CommonMethods.replaceEmptyByNullInSringArray(contract.getInsuranceStatus()));
						if(arraySize < contract.getInsuranceStatus().length) {
							arraySize = contract.getInsuranceStatus().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getInsurance_value_unitss()) && contract.getInsurance_value_unitss().length > 0) {
						contract.setInsurance_value_unitss(CommonMethods.replaceEmptyByNullInSringArray(contract.getInsurance_value_unitss()));
						if(arraySize < contract.getInsurance_value_unitss().length) {
							arraySize = contract.getInsurance_value_unitss().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getInsurance_type_fks()) && contract.getInsurance_type_fks().length > 0 && !StringUtils.isEmpty(contract.getInsurence_valid_uptos())) {
						for (int i = 0; i < arraySize; i++) {
	   int k = 1;
	   if( contract.getInsurance_type_fks().length > 0 && !StringUtils.isEmpty(contract.getInsurance_type_fks()[i])) {
		   stmt.setString(k++,(contract.getInsurance_type_fks().length > 0)?contract.getInsurance_type_fks()[i]:null);
								stmt.setString(k++,(contract.getIssuing_agencys().length > 0)?contract.getIssuing_agencys()[i]:null);
								stmt.setString(k++,(contract.getAgency_addresss().length > 0)?contract.getAgency_addresss()[i]:null);
								stmt.setString(k++,(contract.getInsurance_numbers().length > 0)?contract.getInsurance_numbers()[i]:null);
								stmt.setString(k++,(contract.getInsurance_values().length > 0)?contract.getInsurance_values()[i]:null);
								stmt.setString(k++,DateParser.parse((contract.getInsurence_valid_uptos().length > 0)?contract.getInsurence_valid_uptos()[i]:null));
								//stmt.setString(k++,(contract.getInsurence_remarks().length > 0)?contract.getInsurence_remarks()[i]:null);
								stmt.setString(k++,contract.getContract_id());
								//stmt.setString(k++,(contract.getInsurance_revisions().length > 0)?contract.getInsurance_revisions()[i]:null);
								stmt.setString(k++,(contract.getInsuranceStatus().length > 0)?contract.getInsuranceStatus()[i]:null);
								stmt.setString(k++,(contract.getInsurance_value_unitss().length > 0)?contract.getInsurance_value_unitss()[i]:null);
								stmt.addBatch();
	   }
						}
					}
					c = stmt.executeBatch();
					if(stmt != null){stmt.close();}
					
					String milestone_qry = "INSERT into  contract_milestones (milestone_id,milestone_name,milestone_date,actual_date,revision,remarks,contract_id_fk,status) "
										+"VALUES (?,?,?,?,?,?,?,?)";
					stmt = con.prepareStatement(milestone_qry); 
					arraySize = 0; 
					if(!StringUtils.isEmpty(contract.getMilestone_names()) && contract.getMilestone_names().length > 0) {
						contract.setMilestone_names(CommonMethods.replaceEmptyByNullInSringArray(contract.getMilestone_names()));
						if(arraySize < contract.getMilestone_names().length) {
							arraySize = contract.getMilestone_names().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getMilestone_ids()) && contract.getMilestone_ids().length > 0) {
						contract.setMilestone_ids(CommonMethods.replaceEmptyByNullInSringArray(contract.getMilestone_ids()));
						if(arraySize < contract.getMilestone_ids().length) {
							arraySize = contract.getMilestone_ids().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getMilestone_dates()) && contract.getMilestone_dates().length > 0) {
						contract.setMilestone_dates(CommonMethods.replaceEmptyByNullInSringArray(contract.getMilestone_dates()));
						if(arraySize < contract.getMilestone_dates().length) {
							arraySize = contract.getMilestone_dates().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getActual_dates()) && contract.getActual_dates().length > 0) {
						contract.setActual_dates(CommonMethods.replaceEmptyByNullInSringArray(contract.getActual_dates()));
						if(arraySize < contract.getActual_dates().length) {
							arraySize = contract.getActual_dates().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getRevisions()) && contract.getRevisions().length > 0) {
						contract.setRevisions(CommonMethods.replaceEmptyByNullInSringArray(contract.getRevisions()));
						if(arraySize < contract.getRevisions().length) {
							arraySize = contract.getRevisions().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getMile_remarks()) && contract.getMile_remarks().length > 0) {
						contract.setMile_remarks(CommonMethods.replaceEmptyByNullInSringArray(contract.getMile_remarks()));
						if(arraySize < contract.getMile_remarks().length) {
							arraySize = contract.getMile_remarks().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getMilestone_ids()) && contract.getMilestone_ids().length > 0) {
						for (int i = 0; i < arraySize; i++) {
		int k = 1;
		if( contract.getMilestone_names().length > 0 && !StringUtils.isEmpty(contract.getMilestone_names()[i])) {
			stmt.setString(k++,(contract.getMilestone_ids().length > 0)?contract.getMilestone_ids()[i]:null);
		   stmt.setString(k++,(contract.getMilestone_names().length > 0)?contract.getMilestone_names()[i]:null);
								stmt.setString(k++,DateParser.parse((contract.getMilestone_dates().length > 0)?contract.getMilestone_dates()[i]:null));
								stmt.setString(k++,DateParser.parse((contract.getActual_dates().length > 0)?contract.getActual_dates()[i]:null));
								stmt.setString(k++,(contract.getRevisions().length > 0)?contract.getRevisions()[i]:null);
								stmt.setString(k++,(contract.getMile_remarks().length > 0)?contract.getMile_remarks()[i]:null);
								stmt.setString(k++,contract.getContract_id());
								stmt.setString(k++,CommonConstants.ACTIVE);
								stmt.addBatch();
		}
						}
					}
					c = stmt.executeBatch();
					if(stmt != null){stmt.close();}
					
					String Revision_qry = "INSERT into  contract_revision (revision_number,revised_amount,revised_doc,remarks,action,contract_id_fk,revised_amount_units,revision_amounts_statuss,approval_by_bank,attachment) "
					 +"VALUES (?,?,?,?,?,?,?,?,?,?)";
					stmt = con.prepareStatement(Revision_qry); 
					
					arraySize = 0;
					if(!StringUtils.isEmpty(contract.getRevision_numbers()) && contract.getRevision_numbers().length > 0) {
						contract.setRevision_numbers(CommonMethods.replaceEmptyByNullInSringArray(contract.getRevision_numbers()));
						if(arraySize < contract.getRevision_numbers().length) {
							arraySize = contract.getRevision_numbers().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getRevised_amounts()) && contract.getRevised_amounts().length > 0) {
						contract.setRevised_amounts(CommonMethods.replaceEmptyByNullInSringArray(contract.getRevised_amounts()));
						if(arraySize < contract.getRevised_amounts().length) {
							arraySize = contract.getRevised_amounts().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getRevised_docs()) && contract.getRevised_docs().length > 0) {
						contract.setRevised_docs(CommonMethods.replaceEmptyByNullInSringArray(contract.getRevised_docs()));
						if(arraySize < contract.getRevised_docs().length) {
							arraySize = contract.getRevised_docs().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getRevision_remarks()) && contract.getRevision_remarks().length > 0) {
						contract.setRevision_remarks(CommonMethods.replaceEmptyByNullInSringArray(contract.getRevision_remarks()));
						if(arraySize < contract.getRevision_remarks().length) {
							arraySize = contract.getRevision_remarks().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getRevision_statuss()) && contract.getRevision_statuss().length > 0) {
						contract.setRevision_statuss(CommonMethods.replaceEmptyByNullInSringArray(contract.getRevision_statuss()));
						if(arraySize < contract.getRevision_statuss().length) {
							arraySize = contract.getRevision_statuss().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getRevised_amount_unitss()) && contract.getRevised_amount_unitss().length > 0) {
						contract.setRevised_amount_unitss(CommonMethods.replaceEmptyByNullInSringArray(contract.getRevised_amount_unitss()));
						if(arraySize < contract.getRevised_amount_unitss().length) {
							arraySize = contract.getRevised_amount_unitss().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getRevision_amounts_statuss()) && contract.getRevision_amounts_statuss().length > 0) {
						contract.setRevision_amounts_statuss(CommonMethods.replaceEmptyByNullInSringArray(contract.getRevision_amounts_statuss()));
						if(arraySize < contract.getRevision_amounts_statuss().length) {
							arraySize = contract.getRevision_amounts_statuss().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getApproval_by_bank()) && contract.getApproval_by_bank().length > 0) {
						contract.setApproval_by_bank(CommonMethods.replaceEmptyByNullInSringArray(contract.getApproval_by_bank()));
						if(arraySize < contract.getApproval_by_bank().length) {
							arraySize = contract.getApproval_by_bank().length;
						}
					}				
					if(!StringUtils.isEmpty(contract.getRevision_numbers()) && contract.getRevision_numbers().length > 0) {
						for (int i = 0; i < arraySize; i++) {
							int k = 1;
							if( contract.getRevision_numbers().length > 0 && !StringUtils.isEmpty(contract.getRevision_numbers()[i]) && contract.getRevision_statuss()[i].equalsIgnoreCase("Yes")) {
								stmt.setString(k++,(contract.getRevision_numbers().length > 0)?contract.getRevision_numbers()[i]:null);
								stmt.setString(k++,(contract.getRevised_amounts().length > 0)?contract.getRevised_amounts()[i]:null);
								stmt.setString(k++,DateParser.parse((contract.getRevised_docs().length > 0)?contract.getRevised_docs()[i]:null));								
								stmt.setString(k++,(contract.getRevision_remarks().length > 0)?contract.getRevision_remarks()[i]:null);
								stmt.setString(k++,(contract.getRevision_statuss().length > 0)?contract.getRevision_statuss()[i]:null);
								stmt.setString(k++,contract.getContract_id());
								stmt.setString(k++,(contract.getRevised_amount_unitss().length > 0)?contract.getRevised_amount_unitss()[i]:null);
								stmt.setString(k++,(contract.getRevision_amounts_statuss().length > 0)?contract.getRevision_amounts_statuss()[i]:null);
								stmt.setString(k++,(contract.getApproval_by_bank().length > 0)?contract.getApproval_by_bank()[i]:null);
								stmt.addBatch();
							}
						}
					}
					c = stmt.executeBatch();
					DBConnectionHandler.closeJDBCResoucrs(null, stmt, null);
					
					String key_personnel_qry = "INSERT into contract_key_personnel (name,mobile_no,email_id,contract_id_fk,designation) "
		 +"VALUES (?,?,?,?,?)";
					stmt = con.prepareStatement(key_personnel_qry); 
					
					arraySize = 0;
					if(!StringUtils.isEmpty(contract.getContractKeyPersonnelNames()) && contract.getContractKeyPersonnelNames().length > 0) {
						contract.setContractKeyPersonnelNames(CommonMethods.replaceEmptyByNullInSringArray(contract.getContractKeyPersonnelNames()));
						if(arraySize < contract.getContractKeyPersonnelNames().length) {
							arraySize = contract.getContractKeyPersonnelNames().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getContractKeyPersonnelMobileNos()) && contract.getContractKeyPersonnelMobileNos().length > 0) {
						contract.setContractKeyPersonnelMobileNos(CommonMethods.replaceEmptyByNullInSringArray(contract.getContractKeyPersonnelMobileNos()));
						if(arraySize < contract.getContractKeyPersonnelMobileNos().length) {
							arraySize = contract.getContractKeyPersonnelMobileNos().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getContractKeyPersonnelEmailIds()) && contract.getContractKeyPersonnelEmailIds().length > 0) {
						contract.setContractKeyPersonnelEmailIds(CommonMethods.replaceEmptyByNullInSringArray(contract.getContractKeyPersonnelEmailIds()));
						if(arraySize < contract.getContractKeyPersonnelEmailIds().length) {
							arraySize = contract.getContractKeyPersonnelEmailIds().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getContractKeyPersonnelDesignations()) && contract.getContractKeyPersonnelDesignations().length > 0) {
						contract.setContractKeyPersonnelDesignations(CommonMethods.replaceEmptyByNullInSringArray(contract.getContractKeyPersonnelDesignations()));
						if(arraySize < contract.getContractKeyPersonnelDesignations().length) {
							arraySize = contract.getContractKeyPersonnelDesignations().length;
						}
					}
					
				
					
					if(!StringUtils.isEmpty(contract.getContractKeyPersonnelNames()) && contract.getContractKeyPersonnelNames().length > 0) {
						for (int i = 0; i < arraySize; i++) {
							int k = 1;
								if( contract.getContractKeyPersonnelNames().length > 0 && !StringUtils.isEmpty(contract.getContractKeyPersonnelNames()[i])) {
									stmt.setString(k++,(contract.getContractKeyPersonnelNames().length > 0)?contract.getContractKeyPersonnelNames()[i]:null);
									stmt.setString(k++,(contract.getContractKeyPersonnelMobileNos().length > 0)?contract.getContractKeyPersonnelMobileNos()[i]:null);
									stmt.setString(k++,(contract.getContractKeyPersonnelEmailIds().length > 0)?contract.getContractKeyPersonnelEmailIds()[i]:null);
									stmt.setString(k++,contract.getContract_id());
									stmt.setString(k++,(contract.getContractKeyPersonnelDesignations().length > 0)?contract.getContractKeyPersonnelDesignations()[i]:null);
									stmt.addBatch();
							}
						}
					}
					c = stmt.executeBatch();
					DBConnectionHandler.closeJDBCResoucrs(null, stmt, null);
					
					
					arraySize = 0;
					if(!StringUtils.isEmpty(contract.getContractDocumentNames()) && contract.getContractDocumentNames().length > 0) {
						contract.setContractDocumentNames(CommonMethods.replaceEmptyByNullInSringArray(contract.getContractDocumentNames()));
						if(arraySize < contract.getContractDocumentNames().length) {
							arraySize = contract.getContractDocumentNames().length;
						}
					}
					if(!StringUtils.isEmpty(contract.getContractDocumentFileNames()) && contract.getContractDocumentFileNames().length > 0) {
						contract.setContractDocumentFileNames(CommonMethods.replaceEmptyByNullInSringArray(contract.getContractDocumentFileNames()));
						if(arraySize < contract.getContractDocumentFileNames().length) {
							arraySize = contract.getContractDocumentFileNames().length;
						}
					}
					if (!StringUtils.isEmpty(contract.getContract_file_types()) && contract.getContract_file_types().length > 0) {
						contract.setContract_file_types(CommonMethods.replaceEmptyByNullInSringArray(contract.getContract_file_types()));
						if (arraySize < contract.getContract_file_types().length) {
							arraySize = contract.getContract_file_types().length;
						}
					}					
					
					String insertFileQry = "INSERT into contract_documents (name,attachment,contract_id_fk,contract_file_type_fk,created_date) "
							 +"VALUES (?,?,?,?,CURRENT_TIMESTAMP)";
					stmt = con.prepareStatement(insertFileQry); 
					for (int i = 0; i < arraySize; i++) {
						String docFileName = null;
						MultipartFile multipartFile = contract.getContractDocumentFiles()[i];
						if ((null != multipartFile && !multipartFile.isEmpty() && multipartFile.getSize() > 0)
								|| (!StringUtils.isEmpty(contract.getContractDocumentFileNames()) && contract.getContractDocumentFileNames().length > 0 && !StringUtils.isEmpty(contract.getContractDocumentFileNames()[i]) && !StringUtils.isEmpty(contract.getContractDocumentFileNames()[i].trim()) )) {
							String saveDirectory = CommonConstants.CONTRACT_FILE_SAVING_PATH ;
							String fileName = contract.getContractDocumentFileNames()[i];
							DateFormat df = new SimpleDateFormat("ddMMYY-HHmm-ssSSSSSSS"); 
							String fileName_new = "Contract-"+contract_id +"-"+ df.format(new Date()) +"."+ fileName.split("\\.")[1];
							docFileName = fileName_new;
							if (null != multipartFile && !multipartFile.isEmpty()) {
								FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName_new);
							}
							  int k = 1; 
			 					stmt.setString(k++,(contract.getContractDocumentNames().length > 0)?contract.getContractDocumentNames()[i]:null);
								  stmt.setString(k++,docFileName);
			 					stmt.setString(k++,contract_id);
			 					stmt.setString(k++,(contract.getContract_file_types().length > 0)?contract.getContract_file_types()[i]:null); 
							  stmt.addBatch();
							  
						}
					}
					c = stmt.executeBatch();
					
					DBConnectionHandler.closeJDBCResoucrs(null, stmt, null);
				
					/**********************************************************************************************/
					
					con.commit();
					
					/********************************************************************************/
					
					if(!StringUtils.isEmpty(contract.getHod_user_id_fk()) && !StringUtils.isEmpty(contract.getDy_hod_user_id_fk())) {
						NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
						
						int arrSize = 2;					
						List<Contract> departments = getDepartmentList(contract_id, con);
						for (Contract dept : departments) {
							int size = dept.getExecutivesList().size();
							arrSize = arrSize + size;
						}
						
						int i = 0;
						String userIds[]  = new String[arrSize];
						userIds[i++] = contract.getHod_user_id_fk();
						userIds[i++] = contract.getDy_hod_user_id_fk();
						for (Contract dept : departments) {
							for (Contract exec : dept.getExecutivesList()) {
								userIds[i++] = exec.getExecutive_user_id_fk();
							}
						}
						
						for(int k=0; k<userIds.length-1; k++) {
					         for (int j=k+1; j<userIds.length; j++) {
					            if(userIds[k] == userIds[j]) {
					            	userIds = ArrayUtils.remove(userIds, j);
					            }
					         }
					    }
						
						//String userIds[]  = {contract.getHod_user_id_fk(),contract.getDy_hod_user_id_fk()};
						
						String messageType = "Contract";
						String redirect_url = "/InfoViz/contract/contract-details/" + contract_id;
						String contract_name = contract.getContract_short_name();
						if(StringUtils.isEmpty(contract_name)) {contract_name = contract.getContract_name();}
						String project_name = contract.getProject_name();
						if(StringUtils.isEmpty(project_name)) {project_name = contract.getProject_name();}
						String message = "New contract "+contract_id+" is added under work "+project_name+" on PMIS ";

						Messages msgObj = new Messages();
						msgObj.setUser_ids(userIds);
						msgObj.setMessage_type(messageType);
						msgObj.setRedirect_url(redirect_url);
						msgObj.setMessage(message);
						messagesDao.addMessages(msgObj,template);
					}
					
					FormHistory formHistory = new FormHistory();
					formHistory.setCreated_by_user_id_fk(contract.getCreated_by_user_id_fk());
					formHistory.setUser(contract.getDesignation()+" - "+contract.getUser_name());
					formHistory.setModule_name_fk("Contracts");
					formHistory.setForm_name("Add Contract");
					formHistory.setForm_action_type("Add");
					formHistory.setForm_details("New Contract "+contract.getContract_short_name()+" created");
					formHistory.setContract_id_fk(contract.getContract_id());
					
					boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
					/********************************************************************************/
				}			
			}catch(Exception e){ 
				con.rollback();
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(con, stmt, null);
			}		
			return contract_id;
		}
		private String getDepartmentCode(String deptId, Connection con) throws Exception {
			PreparedStatement ps = null;
			ResultSet rs = null;
			String obj = null;
			try {
				String qry = "select contract_id_code from department where department = ?";
				ps  = con.prepareStatement(qry);
				ps.setString(1, deptId);
				rs = ps.executeQuery();
				if(rs.next()) {
					obj = rs.getString("contract_id_code");
				}
			}catch(Exception e) {
				e.printStackTrace();
				throw new Exception(e);
			}finally {
				DBConnectionHandler.closeJDBCResoucrs(null, ps, rs);
			}
			return obj;
		}

		private String getContractIdByWorkId(String project_id_fk, String department_code, Connection con) throws Exception {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String contract_id = null;
			try{
				String maxIdQry = "SELECT top 1 CONCAT(SUBSTRING(contract_id, 1, LEN(contract_id)-4),'"+department_code+"'," + 
						"IIF(" + 
						"SUBSTRING(CAST(MAX(SUBSTRING(contract_id, CHARINDEX('"+department_code+"',contract_id)+LEN('"+department_code+"'), LEN(contract_id)))+1 AS VARCHAR),0,3)>CHARINDEX('"+department_code+"',contract_id)+LEN('"+department_code+"'),SUBSTRING(CAST(MAX(SUBSTRING(contract_id, CHARINDEX('"+department_code+"',contract_id)+LEN('"+department_code+"'), LEN(contract_id)))+1 AS VARCHAR),0,3),CONCAT('0',SUBSTRING(CAST(MAX(SUBSTRING(contract_id, CHARINDEX('"+department_code+"',contract_id)+LEN('"+department_code+"'), LEN(contract_id)))+1 AS VARCHAR),0,3)))" + 
						"" + 
						") AS maxId FROM contract WHERE contract_id LIKE ? group by contract_id ORDER BY maxId desc ";
				stmt = con.prepareStatement(maxIdQry);
				stmt.setString(1, project_id_fk+department_code+"%");
				rs = stmt.executeQuery();  
				if(rs.next()) {
					contract_id = rs.getString("maxId");
					if(StringUtils.isEmpty(contract_id)) {
						contract_id =  project_id_fk+department_code+"01";
					}
				}
				else
				{
					contract_id =  project_id_fk+department_code+"01";
				}
			}catch(Exception e){ 		
				e.printStackTrace();
				throw new Exception(e);
			}
			finally {
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
			}
			return contract_id;
		}

		@Override
		public List<Contract> getHodList(Contract obj) throws Exception {
			List<Contract> objsList = null;
			try {
				String qry ="SELECT u.user_id as hod_user_id_fk,u.user_name,u.designation,u.department_fk,d.contract_id_code,u.reporting_to_id_srfk "
						+ "FROM [user] u " 
						+ "left join [user] u1 on u.reporting_to_id_srfk = u1.user_id "
						+ "LEFT JOIN department d on u.department_fk = d.department "
						+ "where  u.user_type_fk = ?  ";
				
				int arrSize = 1;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_user_id_fk()) && !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
					qry = qry + " and u.user_id = ? ";
					arrSize++;
				}
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
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDy_hod_user_id_fk()) && !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
					pValues[i++] = obj.getDy_hod_user_id_fk();
				}
				
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
					
			}catch(Exception e){ 
				throw new Exception(e);
			}
			return objsList;
		}

		@Override
		public List<Contract> getDyHodList(Contract obj) throws Exception {
			List<Contract> objsList = null;
			try {
				String qry ="SELECT u.user_id as dy_hod_user_id_fk,u.user_name,u.designation,u.department_fk,u.reporting_to_id_srfk as reporting_to_id_srfk FROM [user] u " + 
						"left join [user] u1 on u.reporting_to_id_srfk = u1.user_id " + 
						"where u.user_type_fk = ?  ";
				
				int arrSize = 1;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_user_id_fk()) && !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
					qry = qry + " and u.reporting_to_id_srfk = ? ";
					arrSize++;
				}
				
				Object[] pValues = new Object[arrSize];
				int i = 0;
				pValues[i++] = CommonConstants.USER_TYPE_DYHOD;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getHod_user_id_fk()) && !obj.getUser_role_code().equals(CommonConstants.ROLE_CODE_IT_ADMIN)) {
					pValues[i++] = obj.getHod_user_id_fk();
				}
					
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
					
			}catch(Exception e){ 
				throw new Exception(e);
			}
			return objsList;
		}

		@Override
		public List<Contract> contractListForExport(Contract obj) throws Exception {
			List<Contract> objsList = null;
			try {
				String qry ="select dt.department_name,dt.contract_id_code,c.project_id_fk,p.project_name,u.designation,us.designation as dy_hod_designation,u.user_name,contract_type_fk,c.contract_id,c.contract_name,c.contract_short_name,contractor_id_fk,cr.contractor_name,dt.department as department_fk,c.hod_user_id_fk,c.dy_hod_user_id_fk  " + 
						",scope_of_contract,estimated_cost,FORMAT(date_of_start,'dd-MM-yyyy') AS date_of_start,FORMAT(doc,'dd-MM-yyyy') AS doc,awarded_cost,loa_letter_number,FORMAT(loa_date,'dd-MM-yyyy') AS loa_date,ca_no,FORMAT(ca_date,'dd-MM-yyyy') AS ca_date,FORMAT(c.actual_completion_date,'dd-MM-yyyy') AS actual_completion_date,"
						+"FORMAT(contract_closure_date,'dd-MM-yyyy') AS contract_closure_date,FORMAT(completion_certificate_release,'dd-MM-yyyy') AS completion_certificate_release,FORMAT(final_takeover,'dd-MM-yyyy') AS final_takeover,FORMAT(final_bill_release,'dd-MM-yyyy') AS final_bill_release,FORMAT(defect_liability_period,'dd-MM-yyyy') AS defect_liability_period,completed_cost,"
						+"FORMAT(retention_money_release,'dd-MM-yyyy') AS retention_money_release,FORMAT(pbg_release,'dd-MM-yyyy') AS pbg_release,c.status as contract_status, contract_status_fk,bg_required,insurance_required,dth.department_name as hod_department,estimated_cost_units,awarded_cost_units,completed_cost_units,mu1.unit as estimated_cost_unit,mu2.unit as awarded_cost_unit,mu3.unit as completed_cost_unit,FORMAT(notice_inviting_tender,'dd-MM-yyyy') AS contract_notice_inviting_tender,FORMAT(planned_date_of_award,'dd-MM-yyyy') AS planned_date_of_award,bank_funded,c.bank_name,type_of_review,c.notice_inviting_tender as noticeinvitingtender " + 
						"from contract c " + 
						"left join contractor cr on c.contractor_id_fk = cr.contractor_id " + 
						"left join project p on c.project_id_fk = p.project_id " + 
						"left join [user] u on c.hod_user_id_fk = u.user_id "+
						"left join [user] us on c.dy_hod_user_id_fk = us.user_id "
						+"left join department dt on c.contract_department = dt.department "
						+"left join department dth on u.department_fk = dth.department "
						+"left join money_unit mu1 on c.estimated_cost_units = mu1.value "
						+"left join money_unit mu2 on c.awarded_cost_units = mu2.value "
						+"left join money_unit mu3 on c.completed_cost_units = mu3.value "
						+"where contract_id is not null ";
				
				int arrSize = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					qry = qry + " and c.contractor_id_fk = ?";
					arrSize++;
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c.contract_department = ?";
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
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					qry = qry + " and (contract_id like ?"
							+ " or c.contract_short_name like ? or cr.contractor_name like ? or dt.department_name like ? or u.designation like ? or us.designation like ?)";
					arrSize++;
					arrSize++;
					arrSize++;
					arrSize++;
					arrSize++;
					arrSize++;
				}			
				
				Object[] pValues = new Object[arrSize];
				int i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
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
					
				if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
				}
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					pValues[i++] = "%"+obj.getSearchStr()+"%";
					pValues[i++] = "%"+obj.getSearchStr()+"%";
					pValues[i++] = "%"+obj.getSearchStr()+"%";
					pValues[i++] = "%"+obj.getSearchStr()+"%";
					pValues[i++] = "%"+obj.getSearchStr()+"%";
					pValues[i++] = "%"+obj.getSearchStr()+"%";
				}			
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
					
			}catch(Exception e){ 
				throw new Exception(e);
			}
			return objsList;
		}

		@Override
		public List<Contract> contractRevisionsList(Contract obj) throws Exception {
			List<Contract> objsList = new ArrayList<Contract>();
			try {
				String qry ="SELECT distinct revision_number,revised_amount,revised_amount_units ,FORMAT(revised_doc,'dd-MM-yyyy') AS revised_doc,"
						+ "action as revision_status,cr.remarks,mu.unit,revision_amounts_status,c.contract_short_name,c.contract_id,approval_by_bank as approvalbybank,attachment "
						+ "from contract_revision cr "
						+ "left join money_unit mu on cr.revised_amount_units = mu.value  "
						+ "left join contract c on cr.contract_id_fk = c.contract_id "
						+ "where cr.contract_id_fk IS NOT NULL";
				
				int arrSize = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					qry = qry + " and c.contractor_id_fk = ?";
					arrSize++;
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c.contract_department = ?";
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
				
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					qry = qry + " and (c.contract_id like ?)";
					arrSize++;
				}
				
				qry = qry + " ORDER BY c.contract_id ASC";
				
				Object[] pValues = new Object[arrSize];
				int i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
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
					
				if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
				}

				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					pValues[i++] = "%"+obj.getSearchStr()+"%";
				}
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
				
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			return objsList;
		}

		
		@Override
		public List<Contract> contractBGList(Contract obj) throws Exception {
			List<Contract> objsList = new ArrayList<Contract>();
			try {
				String qry ="SELECT distinct bg_type_fk,issuing_bank, bg_number,bg_value,FORMAT(valid_upto,'dd-MM-yyyy') AS bg_valid_upto,"
						+ "FORMAT(bg_date,'dd-MM-yyyy') AS bg_date,FORMAT(release_date,'dd-MM-yyyy') AS release_date,bg_value_units,mu.unit,c.contract_short_name,c.contract_id "
						+ "from bank_guarantee bg "
						+ "left join money_unit mu on bg.bg_value_units = mu.value  "
						+ "left join contract c on bg.contract_id_fk = c.contract_id "
						+ "where bg.contract_id_fk IS NOT NULL";
				
				
				int arrSize = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					qry = qry + " and c.contractor_id_fk = ?";
					arrSize++;
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c.contract_department = ?";
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
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					qry = qry + " and (c.contract_id like ?)";
					arrSize++;
				}			
				qry = qry + " ORDER BY c.contract_id ASC";
				
				Object[] pValues = new Object[arrSize];
				int i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
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
					
				if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
				}
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					pValues[i++] = "%"+obj.getSearchStr()+"%";
				}			
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			return objsList;
		}

		
		@Override
		public List<Contract> contractInsuranceList(Contract obj) throws Exception {
			List<Contract> objsList = new ArrayList<Contract>();
			try {
				String qry ="SELECT distinct insurance_type_fk,issuing_agency,agency_address,insurance_number,insurance_value,FORMAT(valid_upto,'dd-MM-yyyy') AS insurence_valid_upto,"
						+ "released_fk as insurance_status,insurance_value_units,mu.unit,c.contract_short_name,c.contract_id "
						+ "from insurance i "
						+ "left join money_unit mu on i.insurance_value_units = mu.value  "
						+ "left join contract c on i.contract_id_fk = c.contract_id "
						+ "where i.contract_id_fk IS NOT NULL";
				
				int arrSize = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					qry = qry + " and c.contractor_id_fk = ?";
					arrSize++;
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c.contract_department = ?";
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
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					qry = qry + " and (c.contract_id like ?)";
					arrSize++;
				}			
				qry = qry + " ORDER BY c.contract_id ASC";
				
				Object[] pValues = new Object[arrSize];
				int i = 0;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
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
					
				if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
				}
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					pValues[i++] = "%"+obj.getSearchStr()+"%";
				}			
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
				
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			return objsList;
		}
		

		@Override
		public List<Contract> contractMilestoneList(Contract obj) throws Exception {
			List<Contract> objsList = new ArrayList<Contract>();
			try {
				String qry ="SELECT distinct contract_milestones_id,milestone_id,milestone_name,FORMAT(milestone_date,'dd-MM-yyyy') AS milestone_date,"
						+ "FORMAT(actual_date,'dd-MM-yyyy') AS actual_date, revision,cm.remarks,c.contract_short_name,c.contract_id "
						+ "from contract_milestones cm "
						+ "left join contract c on cm.contract_id_fk = c.contract_id "
						+ "where cm.contract_id_fk IS NOT NULL and cm.status = ?";
				
				int arrSize = 1;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					qry = qry + " and c.contractor_id_fk = ?";
					arrSize++;
				}	
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c.contract_department = ?";
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
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					qry = qry + " and (c.contract_id like ?)";
					arrSize++;
				}			
				qry = qry + " ORDER BY c.contract_id ASC";
				
				Object[] pValues = new Object[arrSize];
				int i = 0;
				pValues[i++] = CommonConstants.ACTIVE;
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContractor_id_fk())) {
					pValues[i++] = obj.getContractor_id_fk();
				}
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
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
					
				if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
					pValues[i++] = obj.getUser_id();
				}
				if(!StringUtils.isEmpty(obj.getSearchStr())) {
					pValues[i++] = "%"+obj.getSearchStr()+"%";
				}			
				objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Contract>(Contract.class));
			}catch(Exception e){ 
				e.printStackTrace();
				throw new Exception(e);
			}
			return objsList;
		}

}