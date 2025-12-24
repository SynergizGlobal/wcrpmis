package com.wcr.wcrbackend.repo;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.FormHistory;


@Repository
public class FormsHistoryDaoImpl implements FormsHistoryDao{
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;
	
	@Override
	public boolean saveFormHistory(FormHistory obj) throws Exception {
		boolean flag = false;
		try {
			String qry = "";
			if(obj.getModule_name_fk().equalsIgnoreCase("Risk")) {
				qry = ":sub_work";
			}else {
				qry = "ISNULL((select project_name from project where project_id=:project_id_fk),:sub_work)";
			}
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			String insertQry = "INSERT INTO forms_history"
					+ "(module_name_fk,form_name,project_id_fk,contract_id_fk,work,contract,form_action_type,form_details,created_by_user_id_fk,[user])"
					+ "VALUES"
					+ "(:module_name_fk,:form_name,:project_id_fk,:contract_id_fk,"+qry+",(select contract_short_name from contract where contract_id=:contract_id_fk),:form_action_type,:form_details,:created_by_user_id_fk,:user)";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			int count = namedParamJdbcTemplate.update(insertQry, paramSource);			
			if(count > 0) {
				flag = true;
			}
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return flag;
	}

}
	