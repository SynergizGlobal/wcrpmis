package com.wcr.wcrbackend.reference.IMPLdao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.reference.Idao.IssueCategoryTitleDao;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Repository
public class IssueCategoryTitleDaoImpl implements IssueCategoryTitleDao{
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;

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
	public List<TrainingType> getIssueCategoryTitle(TrainingType obj) throws Exception {
	    List<TrainingType> objList;
	    try {
	        String qry =
	            "SELECT issue_category_fk, " +
	            "STRING_AGG(CAST(id AS VARCHAR), ',') AS id, " +
	            "STRING_AGG(short_description, ',') AS short_description " +
	            "FROM issue_category_title " +
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
	public boolean addIssueCategoryTitle(TrainingType obj) throws Exception {
		boolean flag = false;	int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			int size = 0,size2 = 0,size3=0;
			String area  = obj.getIssue_category_fk();
			String sub_area  = obj.getShort_description();
			//sub_area = sub_area.replaceFirst("^", "").replaceAll(",,", ",");
			String [] areaArr = new String [0];
			String [] Short_descriptionArr = new String [0];
			String [] Short_descriptionArr1 = new String [0];
			if(!StringUtils.isEmpty(area) && area.contains(",")) {
				areaArr = area.split(",");
				size = areaArr.length;
				Short_descriptionArr = sub_area.split(",_,");
				size2 = areaArr.length;
				
			}else if(!StringUtils.isEmpty(sub_area) && sub_area.contains(",")) {
				Short_descriptionArr1 = sub_area.split(",");
				size3 = Short_descriptionArr1.length;
				size = 1;
			}else if(!StringUtils.isEmpty(sub_area)) {
				size = 1;
				size3 = 1;
			}
			for(int i =0;i< size; i++) {
				 if(areaArr.length > 0) {
					String [] subName = null;
					obj.setIssue_category_fk(areaArr[i]);
					if((!StringUtils.isEmpty(sub_area) && Short_descriptionArr1.length > 0) 
							|| (!StringUtils.isEmpty(sub_area) && Short_descriptionArr.length > 0)) {
						if(Short_descriptionArr[i].contains(",")) {
							subName = Short_descriptionArr[i].split(",");
							size3 = (Short_descriptionArr1.length > 0) ? Short_descriptionArr1.length : subName.length ;
						}else {
							subName = Short_descriptionArr; size3 = 1;
							String sub_name = Short_descriptionArr[i];
							subName[0] = sub_name;
						}
					}
					for(int j =0;j< size3; j++) {
						obj.setShort_description(subName[j].replaceAll("&", ","));
						String insertQry = "INSERT INTO issue_category_title"
								+ "(short_description, issue_category_fk) VALUES (:short_description,:issue_category_fk)";
						BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
						count = namedParamJdbcTemplate.update(insertQry, paramSource);		
					}
				 }else {
					for(int j =0;j< size3; j++) {
						if(!StringUtils.isEmpty(sub_area) && Short_descriptionArr1.length > 0) {
							obj.setShort_description(Short_descriptionArr1[j].replaceAll("&", ","));
						}else {
							obj.setShort_description(sub_area.replaceAll("&", ","));
						}
						String insertQry = "INSERT INTO issue_category_title"
								+ "(short_description, issue_category_fk) VALUES (:short_description,:issue_category_fk)";
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
					"where object_name(fkx.rkeyid)='issue_category_title' and s.name='dbo'\r\n" + 
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
					"where object_name(fkx.rkeyid)='issue_category_title' and s.name='dbo'\r\n" + 
					"order by object_name(fkx.fkeyid), fkx.keyno";
			
			 list = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return list;
	}
	@Override
	public boolean updateIssueCategoryTitle(TrainingType obj) throws Exception {
		boolean flag = false;
		int count = 0;
		try {
			
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			List<TrainingType> tablesList = getTablesList(obj);
			List<TrainingType> list = getDataDetails(obj);
			obj.setdList(list);

			String disableQry = "Alter Table issue_category_title NOCHECK Constraint All ";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			namedParamJdbcTemplate.update(disableQry, paramSource);	
	
			String deleteQry = "delete from issue_category_title where issue_category_fk= :issue_category_fk_old";
			 paramSource = new BeanPropertySqlParameterSource(obj);		 
			count = namedParamJdbcTemplate.update(deleteQry, paramSource);	
			
			int size = 0,size2 = 0,size3=0;
			String area  = obj.getIssue_category_fk_new();
			String sub_area  = obj.getShort_description_new();
			String value_old  = obj.getValue_old();
			//sub_area = sub_area.replaceFirst("^", "").replaceAll(",,", ",");
			String [] areaArr = new String [0];
			String [] Short_descriptionArr = new String [0];
			String [] Short_descriptionArr1 = new String [0];
			String [] value_oldArr1 = new String [0];
			if(!StringUtils.isEmpty(area) && area.contains(",")) {
				areaArr = area.split(",");
				size = areaArr.length;
				Short_descriptionArr = sub_area.split(",_,");
				size2 = areaArr.length;
				
			}else if(!StringUtils.isEmpty(sub_area) && sub_area.contains(",")) {
				Short_descriptionArr1 = sub_area.split(",");
				size3 = Short_descriptionArr1.length;
				size = 1;
			}else if(!StringUtils.isEmpty(sub_area)) {
				size = 1;
				size3 = 1;
			}
			for(int i =0;i< size; i++) {
				 if(areaArr.length > 0) {
					String [] subName = null;
					obj.setIssue_category_fk_new(areaArr[i]);
					if((!StringUtils.isEmpty(sub_area) && Short_descriptionArr1.length > 0) 
							|| (!StringUtils.isEmpty(sub_area) && Short_descriptionArr.length > 0)) {
						if(Short_descriptionArr[i].contains(",")) {
							subName = Short_descriptionArr[i].split(",");
							size3 = (Short_descriptionArr1.length > 0) ? Short_descriptionArr1.length : subName.length ;
						}else {
							subName = Short_descriptionArr; size3 = 1;
							String sub_name = Short_descriptionArr[i];
							subName[0] = sub_name;
						}
					}
					for(int j =0;j< size3; j++) {
						obj.setShort_description_new(subName[j].replaceAll("&", ","));
						String insertQry = "INSERT INTO issue_category_title"
								+ "(short_description, issue_category_fk) VALUES (:short_description_new,:issue_category_fk_new)";
						 paramSource = new BeanPropertySqlParameterSource(obj);		 
						count = namedParamJdbcTemplate.update(insertQry, paramSource);	
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
						String updateTableQry = "UPDATE "+bObj.getTable_name()+" SET "+bObj.getColumn_name()+" =:short_description_new WHERE "+bObj.getColumn_name()+"= :value_old " ;
						obj.setValue_old(idOld);
						paramSource = new BeanPropertySqlParameterSource(obj);		 
						namedParamJdbcTemplate.update(updateTableQry, paramSource);	
					}
					}
				 }else {
					for(int j =0;j< size3; j++) {
						if(!StringUtils.isEmpty(sub_area) && Short_descriptionArr1.length > 0) {
							obj.setShort_description_new(Short_descriptionArr1[j].replaceAll("&", ","));
						}else {
							obj.setShort_description_new(sub_area.replaceAll("&", ","));
						}
						String insertQry = "INSERT INTO issue_category_title"
								+ "(short_description, issue_category_fk) VALUES (:short_description_new,:issue_category_fk_new)";
						 paramSource = new BeanPropertySqlParameterSource(obj);		 
						count = namedParamJdbcTemplate.update(insertQry, paramSource);		
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
						String updateTableQry = "UPDATE "+bObj.getTable_name()+" SET "+bObj.getColumn_name()+" =:short_description_new WHERE "+bObj.getColumn_name()+"= :value_old " ;
						obj.setValue_old(idOld);
						paramSource = new BeanPropertySqlParameterSource(obj);		 
						namedParamJdbcTemplate.update(updateTableQry, paramSource);	
					}
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

	@Override
	public boolean deleteIssueCategoryTitle(TrainingType obj) throws Exception {
		boolean flag = false;
		int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			String deleteQry ="DELETE from issue_category_title WHERE issue_category_fk= :issue_category_fk ";
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
	public List<TrainingType> getTitles(TrainingType obj) throws Exception {
		List<TrainingType> objList = null;
		try {
			String qry = "SELECT short_description  from issue_category_title where issue_category_fk= '"+ obj.getIssue_category_fk()+"'";
			objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));	
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}
}
