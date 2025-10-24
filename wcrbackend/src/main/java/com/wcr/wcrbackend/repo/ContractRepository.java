package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.Contract;

@Repository
public class ContractRepository implements IContractRepo {

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

}
