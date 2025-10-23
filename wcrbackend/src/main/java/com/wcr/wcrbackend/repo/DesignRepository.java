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

}
