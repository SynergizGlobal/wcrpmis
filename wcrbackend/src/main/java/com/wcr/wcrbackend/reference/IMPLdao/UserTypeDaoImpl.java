package com.wcr.wcrbackend.reference.IMPLdao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import com.wcr.wcrbackend.reference.Idao.UserTypeDao;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Repository
public class UserTypeDaoImpl implements UserTypeDao{

	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;

	@Override
	public TrainingType getUserTypeDetails(TrainingType obj) throws Exception {
	    String qry = "SELECT user_type FROM user_type ORDER BY user_type";
	    List<TrainingType> list =
	        jdbcTemplate.query(qry, new BeanPropertyRowMapper<>(TrainingType.class));
	    obj.setdList(list);
	    return obj;
	}

	
	private List<TrainingType> getTablesList(TrainingType obj) throws Exception {
		List<TrainingType> tablesList = null;
		try {
			String qry = "select  object_name(fkx.fkeyid) as tName,\r\n" + 
					"        \r\n" + 
					"        col_name(fkx.fkeyid, fkx.fkey) as COLUMN_NAME,\r\n" + 
					"		'' as CONSTRAINT_NAME,\r\n" + 
					"        object_name(fkx.rkeyid) as REFERENCED_TABLE_NAME,\r\n" + 
					"        col_name(fkx.rkeyid, fkx.rkey) as REFERENCED_COLUMN_NAME\r\n" + 
					"from sysforeignkeys fkx \r\n" + 
					"inner join sys.tables t on object_name(fkx.rkeyid)=t.name \r\n" + 
					"inner join sys.schemas s on s.schema_id=t.schema_id \r\n" + 
					"where object_name(fkx.rkeyid)='user_type' and s.name='dbo'\r\n" + 
					"order by object_name(fkx.fkeyid), fkx.keyno";
			
			tablesList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return tablesList;
	}


	private List<TrainingType> getDataDetails(TrainingType obj) throws Exception {
		List<TrainingType> list = null;
		try {

			String qry = "select  object_name(fkx.fkeyid) as TABLE_NAME,\r\n" + 
					"        \r\n" + 
					"        col_name(fkx.fkeyid, fkx.fkey) as COLUMN_NAME,\r\n" + 
					"		'' as CONSTRAINT_NAME,\r\n" + 
					"        object_name(fkx.rkeyid) as REFERENCED_TABLE_NAME,\r\n" + 
					"        col_name(fkx.rkeyid, fkx.rkey) as REFERENCED_COLUMN_NAME\r\n" + 
					"from sysforeignkeys fkx \r\n" + 
					"inner join sys.tables t on object_name(fkx.rkeyid)=t.name \r\n" + 
					"inner join sys.schemas s on s.schema_id=t.schema_id \r\n" + 
					"where object_name(fkx.rkeyid)='user_type' and s.name='dbo'\r\n" + 
					"order by object_name(fkx.fkeyid), fkx.keyno";
			
			 list = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return list;
	}



	@Override
	public boolean addUserType(TrainingType obj) throws Exception {
		boolean flag = false;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			String insertQry = "INSERT INTO user_type"
					+ "( user_type) VALUES (:user_type)";
			
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

	@Override
	public boolean updateUserType(TrainingType obj) throws Exception {
		boolean flag = false;
		int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			List<TrainingType> tablesList = getTablesList(obj);
			List<TrainingType> list = getDataDetails(obj);
			obj.setdList(list);

			String disableQry = "Alter Table user_type NOCHECK Constraint All ";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			namedParamJdbcTemplate.update(disableQry, paramSource);	
			
			String  updatereferenceTableQry = "UPDATE user_type SET user_type= :value_new WHERE user_type= :value_old " ;
			paramSource = new BeanPropertySqlParameterSource(obj);		 
			count = namedParamJdbcTemplate.update(updatereferenceTableQry, paramSource);	
			
			for (TrainingType bObj : obj.getdList()) {
				
				String updateTableQry = "UPDATE ["+bObj.getTable_name()+"] SET "+bObj.getColumn_name()+" =:value_new WHERE "+bObj.getColumn_name()+"= :value_old " ;
				
				 paramSource = new BeanPropertySqlParameterSource(obj);		 
				 namedParamJdbcTemplate.update(updateTableQry, paramSource);	
			}
			String  enableQry =	"Alter Table user_type CHECK Constraint All";
			paramSource = new BeanPropertySqlParameterSource(obj);	
			namedParamJdbcTemplate.update(enableQry, paramSource);
			if(count > 0) {
				flag = true;
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return flag;
	}

	@Override
	public boolean deleteUserType(TrainingType obj) throws Exception {
		boolean flag = false;
		int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			String deleteQry ="DELETE from user_type WHERE user_type= :user_type; ";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			 count = namedParamJdbcTemplate.update(deleteQry, paramSource);
			if(count > 0) {
				flag = true;
			}
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return flag;
	}

}
