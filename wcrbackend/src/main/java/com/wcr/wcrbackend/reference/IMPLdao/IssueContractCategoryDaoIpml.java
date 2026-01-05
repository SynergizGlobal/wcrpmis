package com.wcr.wcrbackend.reference.IMPLdao;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.reference.Idao.IssueContractCategoryDao;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Repository
public class IssueContractCategoryDaoIpml implements  IssueContractCategoryDao{

	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;

	@Override
	public List<TrainingType> getContractTypeDetails(TrainingType obj) throws Exception {
		List<TrainingType> objList = null;
		try {
			String qry = "SELECT contract_type as contract_category_fk from contract_type";
			
			objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<TrainingType> gtIssueCategoryDetails(TrainingType obj) throws Exception {
		List<TrainingType> objList = null;
		try {
			String qry = "SELECT category as issue_category_fk from issue_category";
			
			objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<TrainingType> getIssueContractCategory(TrainingType obj) throws Exception {
	    List<TrainingType> objList;
	    try {
	        String qry =
	            "SELECT issue_category_fk, " +
	            "STRING_AGG(contract_category_fk, ',') AS contract_category_fk " +
	            "FROM issue_contarct_category " +
	            "GROUP BY issue_category_fk";

	        objList = jdbcTemplate.query(
	            qry,
	            new BeanPropertyRowMapper<>(TrainingType.class)
	        );

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new Exception(e);
	    }
	    return objList;
	}


	@Override
	public boolean addIssueContractCategory(TrainingType obj) throws Exception {
		boolean flag = false;	int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			int size = 0,size2 = 0,size3=0;
			String area  = obj.getIssue_category_fk();
			String sub_area  = obj.getContract_category_fk();
			//sub_area = sub_area.replaceFirst("^", "").replaceAll(",,", ",");
			String [] areaArr = new String [0];
			String [] Contract_category_fkArr = new String [0];
			String [] Contract_category_fkArr1 = new String [0];
			if(!StringUtils.isEmpty(area) && area.contains(",")) {
				areaArr = area.split(",");
				size = areaArr.length;
				Contract_category_fkArr = sub_area.split(",_,");
				size2 = areaArr.length;
				
			}else if(!StringUtils.isEmpty(sub_area) && sub_area.contains(",")) {
				Contract_category_fkArr1 = sub_area.split(",");
				size3 = Contract_category_fkArr1.length;
				size = 1;
			}else if(!StringUtils.isEmpty(sub_area)) {
				size = 1;
				size3 = 1;
			}
			for(int i =0;i< size; i++) {
				 if(areaArr.length > 0) {
					String [] subName = null;
					obj.setIssue_category_fk(areaArr[i]);
					if((!StringUtils.isEmpty(sub_area) && Contract_category_fkArr1.length > 0) 
							|| (!StringUtils.isEmpty(sub_area) && Contract_category_fkArr.length > 0)) {
						if(Contract_category_fkArr[i].contains(",")) {
							subName = Contract_category_fkArr[i].split(",");
							size3 = (Contract_category_fkArr1.length > 0) ? Contract_category_fkArr1.length : subName.length ;
						}else {
							subName = Contract_category_fkArr; size3 = 1;
							String sub_name = Contract_category_fkArr[i];
							subName[0] = sub_name;
						}
					}
					for(int j =0;j< size3; j++) {
						obj.setContract_category_fk(subName[j].replaceAll("&", ","));
						String insertQry = "INSERT INTO issue_contarct_category"
								+ "(contract_category_fk, issue_category_fk) VALUES (:contract_category_fk,:issue_category_fk)";
						BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
						count = namedParamJdbcTemplate.update(insertQry, paramSource);		
					}
				 }else {
					for(int j =0;j< size3; j++) {
						if(!StringUtils.isEmpty(sub_area) && Contract_category_fkArr1.length > 0) {
							obj.setContract_category_fk(Contract_category_fkArr1[j].replaceAll("&", ","));
						}else {
							obj.setContract_category_fk(sub_area.replaceAll("&", ","));
						}
						String insertQry = "INSERT INTO issue_contarct_category"
								+ "(contract_category_fk, issue_category_fk) VALUES (:contract_category_fk,:issue_category_fk)";
						BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
						count = namedParamJdbcTemplate.update(insertQry, paramSource);		
					}
			     }
			
			}
			
			if(count > 0) {
				flag = true;
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return flag;
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
					"where object_name(fkx.rkeyid)='issue_contarct_category' and s.name='dbo'\r\n" + 
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
					"where object_name(fkx.rkeyid)='issue_contarct_category' and s.name='dbo'\r\n" + 
					"order by object_name(fkx.fkeyid), fkx.keyno";
			
			 list = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return list;
	}
	@Override
	public boolean updateIssueContractCategory(TrainingType obj) throws Exception {
		boolean flag = false;int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			List<TrainingType> tablesList = getTablesList(obj);
			List<TrainingType> list = getDataDetails(obj);
			obj.setdList(list);

			String disableQry = "Alter Table issue_contarct_category NOCHECK Constraint All ";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			namedParamJdbcTemplate.update(disableQry, paramSource);	
			
			String deleteQry = "delete from issue_contarct_category where issue_category_fk= :issue_category_fk_old";
			paramSource = new BeanPropertySqlParameterSource(obj);		 
			count = namedParamJdbcTemplate.update(deleteQry, paramSource);	
			
			
			int size = 0,size2 = 0,size3=0;
			String area  = obj.getIssue_category_fk_new();
			String sub_area  = obj.getContract_category_fk_new();
			String value_old  = obj.getValue_old();
			//sub_area = sub_area.replaceFirst("^", "").replaceAll(",,", ",");
			String [] areaArr = new String [0];
			String [] Contract_category_fkArr = new String [0];
			String [] Contract_category_fkArr1 = new String [0];
			String [] value_oldArr1 = new String [0];
			if(!StringUtils.isEmpty(area) && area.contains(",")) {
				areaArr = area.split(",");
				size = areaArr.length;
				Contract_category_fkArr = sub_area.split(",_,");
				size2 = areaArr.length;
				
			}else if(!StringUtils.isEmpty(sub_area) && sub_area.contains(",")) {
				Contract_category_fkArr1 = sub_area.split(",");
				
				size3 = Contract_category_fkArr1.length;
				size = 1;
			}else if(!StringUtils.isEmpty(sub_area)) {
				size = 1;
				size3 = 1;
			}
			for(int i =0;i< size; i++) {
				 if(areaArr.length > 0) {
					String [] subName = null;
					obj.setIssue_category_fk_new(areaArr[i]);
					if((!StringUtils.isEmpty(sub_area) && Contract_category_fkArr1.length > 0) 
							|| (!StringUtils.isEmpty(sub_area) && Contract_category_fkArr.length > 0)) {
						if(Contract_category_fkArr[i].contains(",")) {
							subName = Contract_category_fkArr[i].split(",");
							size3 = (Contract_category_fkArr1.length > 0) ? Contract_category_fkArr1.length : subName.length ;
						}else {
							subName = Contract_category_fkArr; size3 = 1;
							String sub_name = Contract_category_fkArr[i];
							subName[0] = sub_name;
						}
					}
					for(int j =0;j< size3; j++) {
						obj.setContract_category_fk_new(subName[j].replaceAll("&", ","));
						String insertQry = "INSERT INTO issue_contarct_category"
								+ "(contract_category_fk, issue_category_fk) VALUES (:contract_category_fk_new,:issue_category_fk_new)";
						paramSource = new BeanPropertySqlParameterSource(obj);	
						KeyHolder keyHolder = new GeneratedKeyHolder();
						count = namedParamJdbcTemplate.update(insertQry, paramSource,keyHolder);
						 String id = null;
							if(count > 0) {
								id = String.valueOf(keyHolder.getKey().intValue());
								 flag = true;
							}
							String idOld =  value_old;
							if(value_old.contains(",")) {
								value_oldArr1 = value_old.split(",") ;
								try {
									idOld = value_oldArr1[j];
								}catch(Exception e) {
									idOld = "no val";
								}
							}
						for (TrainingType bObj : obj.getdList()) {
							String updateTableQry = "UPDATE "+bObj.getTable_name()+" SET "+bObj.getColumn_name()+" =:contract_category_fk_new WHERE "+bObj.getColumn_name()+"= :value_old " ;
							obj.setValue_old(idOld);
							paramSource = new BeanPropertySqlParameterSource(obj);		 
							namedParamJdbcTemplate.update(updateTableQry, paramSource);	
						}
					}
				 }else {
					for(int j =0;j< size3; j++) {
						if(!StringUtils.isEmpty(sub_area) && Contract_category_fkArr1.length > 0) {
							obj.setContract_category_fk_new(Contract_category_fkArr1[j].replaceAll("&", ","));
						}else {
							obj.setContract_category_fk_new(sub_area.replaceAll("&", ","));
						}
						String insertQry = "INSERT INTO issue_contarct_category"
								+ "(contract_category_fk, issue_category_fk) VALUES (:contract_category_fk_new,:issue_category_fk_new)";
						paramSource = new BeanPropertySqlParameterSource(obj);	
						KeyHolder keyHolder = new GeneratedKeyHolder();
						count = namedParamJdbcTemplate.update(insertQry, paramSource,keyHolder);
						 String id = null;
							if(count > 0) {
								id = String.valueOf(keyHolder.getKey().intValue());
								 flag = true;
							}
							String idOld =  value_old;
							if(value_old.contains(",")) {
								value_oldArr1 = value_old.split(",") ;
								try {
									idOld = value_oldArr1[j];
								}catch(Exception e) {
									idOld = null;
								}
							}
						for (TrainingType bObj : obj.getdList()) {
							
							String updateTableQry = "UPDATE "+bObj.getTable_name()+" SET "+bObj.getColumn_name()+" =:contract_category_fk_new WHERE "+bObj.getColumn_name()+"= :value_old " ;
							obj.setValue_old(idOld);
							paramSource = new BeanPropertySqlParameterSource(obj);		 
							namedParamJdbcTemplate.update(updateTableQry, paramSource);	
						}
					}
			     }
			
			}
			String  enableQry =	"Alter Table issue_contarct_category CHECK Constraint All";
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
	public boolean deleteIssueContractCategory(TrainingType obj) throws Exception {
		boolean flag = false;
		int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			String deleteQry ="DELETE from issue_contarct_category WHERE issue_category_fk = :issue_category_fk ";
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

	@Override
	public List<TrainingType> getContarctCategory(TrainingType obj) throws Exception {
		List<TrainingType> objList = null;
		try {
			String qry = "SELECT contract_category_fk  from issue_contarct_category where issue_category_fk = '"+ obj.getIssue_category_fk()+"'";
			objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));	
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}
}
