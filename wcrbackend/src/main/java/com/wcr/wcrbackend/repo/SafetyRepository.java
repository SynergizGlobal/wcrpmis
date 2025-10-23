package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Design;
import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.common.CommonConstants;

@Repository
public class SafetyRepository implements ISafetyRepo {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Safety> getProjectsListForSafetyForm(Safety obj) throws Exception {
		List<Safety> objsList = null;
		try {
			String qry = "select distinct project_id as project_id_fk,project_name from  project p "
					+ "LEFT OUTER JOIN contract c ON p.project_id  = c.project_id_fk "
					+ "left outer join [user] u ON c.hod_user_id_fk= u.user_id where project_id is not null ";

			
			if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				/*qry = qry + " and c.contract_id in(select distinct contract_id from( " + 
						"SELECT distinct contract_id FROM contract where hod_user_id_fk='"+obj.getUser_id()+"' or dy_hod_user_id_fk='"+obj.getUser_id()+"' " + 
						"union all " + 
						"SELECT distinct contract_id FROM contract where hod_user_id_fk=(select reporting_to_id_srfk from [user] where user_id='"+obj.getUser_id()+"')  " + 
						"or dy_hod_user_id_fk=(select reporting_to_id_srfk from [user] where user_id='"+obj.getUser_id()+"')) as a) ";	*/	
				
				qry = qry + " and c.contract_id in(SELECT contract_id_fk FROM contract_executive where executive_user_id_fk='"+obj.getUser_id()+"'" + 
						") ";				
				
			}
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Safety>(Safety.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Safety> getContractsListForSafetyForm(Safety obj) throws Exception {
		List<Safety> objsList = null;
		try {
			String qry ="select distinct c.contract_id as contract_id_fk,c.hod_user_id_fk,c.contract_name,c.contract_short_name "
					+ "from contract c "
					+ "left join contract_executive c1 on c1.contract_id_fk = c.contract_id "
					+ "left join [user] u ON c.hod_user_id_fk= u.user_id "
					+ "where c.contract_id is not null ";
			
			if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				/*qry = qry + " and c.contract_id in(select distinct contract_id from( " + 
						"SELECT distinct contract_id FROM contract where hod_user_id_fk='"+obj.getUser_id()+"' or dy_hod_user_id_fk='"+obj.getUser_id()+"' " + 
						"union all " + 
						"SELECT distinct contract_id FROM contract where hod_user_id_fk=(select reporting_to_id_srfk from [user] where user_id='"+obj.getUser_id()+"')  " + 
						"or dy_hod_user_id_fk=(select reporting_to_id_srfk from [user] where user_id='"+obj.getUser_id()+"')) as a) ";*/
				qry = qry + " and c.contract_id in(SELECT contract_id_fk FROM contract_executive where executive_user_id_fk='"+obj.getUser_id()+"'" + 
						") ";					
				
			}			
			
			int arrSize = 0;			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				qry = qry + " and c.work_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					qry = qry + " and c1.department_id_fk = ? and u.department_fk = ?";
					arrSize++;
					arrSize++;
				}
			}			
			qry = qry + " order by c.contract_id asc";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_id_fk())) {
				pValues[i++] = obj.getWork_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {			
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getDepartment_fk())) {
					pValues[i++] = obj.getDepartment_fk();
					pValues[i++] = obj.getDepartment_fk();
				
				}
			}			
				
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Safety>(Safety.class));
				
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

}
