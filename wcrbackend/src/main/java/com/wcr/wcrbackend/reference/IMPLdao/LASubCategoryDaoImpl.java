package com.wcr.wcrbackend.reference.IMPLdao;

import java.util.List;
import java.util.regex.Pattern;

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

import com.wcr.wcrbackend.reference.Idao.LASubCategoryDao;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Repository
public class LASubCategoryDaoImpl implements LASubCategoryDao{

	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;

	@Override
	public List<TrainingType> getLASubCategoryList() throws Exception {
		List<TrainingType> objsList = null;
		try {
			String qry ="select id, la_sub_category, la_category_fk from la_sub_category ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public boolean addLASubCategory(TrainingType obj) throws Exception {
		boolean flag = false;	int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			int size = 0,size2 = 0,size3=0;
			String area  = obj.getLa_category_fk();
			String sub_area  = obj.getLa_sub_category();
			//sub_area = sub_area.replaceFirst("^", "").replaceAll(",,", ",");
			String [] areaArr = new String [0];
			String [] La_sub_categoryArr = new String [0];
			String [] La_sub_categoryArr1 = new String [0];
			if(!StringUtils.isEmpty(area) && area.contains(",")) {
				areaArr = area.split(",");
				size = areaArr.length;
				La_sub_categoryArr = sub_area.split(",_,");
				size2 = areaArr.length;
				
			}else if(!StringUtils.isEmpty(sub_area) && sub_area.contains(",")) {
				La_sub_categoryArr1 = sub_area.split(",");
				size3 = La_sub_categoryArr1.length;
				size = 1;
			}else if(!StringUtils.isEmpty(sub_area)) {
				size = 1;
				size3 = 1;
			}
			for(int i =0;i< size; i++) {
				 if(areaArr.length > 0) {
					String [] subName = null;
					obj.setLa_category_fk(areaArr[i]);
					if((!StringUtils.isEmpty(sub_area) && La_sub_categoryArr1.length > 0) 
							|| (!StringUtils.isEmpty(sub_area) && La_sub_categoryArr.length > 0)) {
						if(La_sub_categoryArr[i].contains(",")) {
							subName = La_sub_categoryArr[i].split(",");
							size3 = (La_sub_categoryArr1.length > 0) ? La_sub_categoryArr1.length : subName.length ;
						}else {
							subName = La_sub_categoryArr; size3 = 1;
							String sub_name = La_sub_categoryArr[i];
							subName[0] = sub_name;
						}
					}
					for(int j =0;j< size3; j++) {
						obj.setLa_sub_category(subName[j].replaceAll("&", ","));
						String insertQry = "INSERT INTO la_sub_category"
								+ "( la_category_fk,la_sub_category) VALUES (:la_category_fk,:la_sub_category)";
						BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
						count = namedParamJdbcTemplate.update(insertQry, paramSource);		
					}
				 }else {
					for(int j =0;j< size3; j++) {
						if(!StringUtils.isEmpty(sub_area) && La_sub_categoryArr1.length > 0) {
							obj.setLa_sub_category(La_sub_categoryArr1[j].replaceAll("&", ","));
						}else {
							obj.setLa_sub_category(sub_area.replaceAll("&", ","));
						}
						String insertQry = "INSERT INTO la_sub_category"
								+ "( la_category_fk,la_sub_category) VALUES (:la_category_fk,:la_sub_category)";
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

	@Override
	public TrainingType getLandAcquisitionSubCategoryDetails(TrainingType obj) throws Exception {
		List<TrainingType> objsList = null;
		List<TrainingType> objsList1 = null;
		TrainingType sObj =null;
		try {
			String qry ="select STRING_AGG(id,',') id, STRING_AGG(la_sub_category,',') la_sub_category, la_category_fk from la_sub_category group by la_category_fk";
			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
			obj.setdList1(objsList);
			
			List<TrainingType> tablesList = getTablesList(obj);
			obj.setTablesList(tablesList);
			if(tablesList.size() > 0) {
				List<TrainingType> list = getDataDetails( obj);
				obj.setdList(list);
				String qry1 = "";
				int i = 1;
				for (TrainingType bObj : obj.getdList()) {
					
					qry1 = qry1 +"select "+bObj.getColumn_name()+" as la_category,count("+bObj.getColumn_name()+") as count,'"+bObj.getTable_name()+"' as tName from "+bObj.getTable_name()+" where "+bObj.getColumn_name()+" <> '' group by "+bObj.getColumn_name()+"  ";
					if( list.size() >  i) {
						qry1 = qry1 + " UNION ";
						i++;
					}
				}
				objsList1 = jdbcTemplate.query( qry1, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));
				obj.setdList(objsList1);
				obj.setCountList(objsList1);
				if(objsList1.size() > 0) {
					Object[] pValues  = new Object[objsList1.size()];
					  String qry2 = "select id, la_sub_category, la_category_fk from la_sub_category where id NOT IN (?";
	
						int j =0, p=1;
						for (TrainingType aObj : obj.getdList()) {
							pValues[j++] = aObj.getLa_category();
							if( objsList1.size() >  p) {
								qry2 = qry2 + ",?";
								p++;
							}
						}
						qry2 = qry2 + ")";
						objsList1 = jdbcTemplate.query( qry2,pValues, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));
						obj.setdList(objsList1);
				}else {
					 obj.setdList(objsList);
				}
			}else {
				obj.setdList(objsList);
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
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
					"where object_name(fkx.rkeyid)='la_sub_category' and s.name='dbo'\r\n" + 
					"order by object_name(fkx.fkeyid), fkx.keyno";
			
			tablesList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
			for(TrainingType tName : tablesList) {
				 String name = tName.gettName();
				 String result = name.split("la_")[1];
				 name = result.replaceAll("[_]", " ");  
				 String regex = "\\b(.)(.*?)\\b";
			     String captilizedName = Pattern.compile(regex).matcher(name).replaceAll(
			            matche -> matche.group(1).toUpperCase() + matche.group(2)
			     );
			     tName.setCaptiliszedTableName(captilizedName);
			}
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
					"where object_name(fkx.rkeyid)='la_sub_category' and s.name='dbo'\r\n" + 
					"order by object_name(fkx.fkeyid), fkx.keyno ";
			
			 list = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return list;
	}
	@Override
	public boolean updateLandAcquisitionSubCategory(TrainingType obj) throws Exception {
		boolean flag = false;
		int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			List<TrainingType> tablesList = getTablesList(obj);
			List<TrainingType> list = getDataDetails(obj);
			obj.setdList(list);

			String disableQry = "Alter Table la_sub_category NOCHECK Constraint All ";
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			namedParamJdbcTemplate.update(disableQry, paramSource);	
			
			String deleteQry = "delete from la_sub_category where la_category_fk= :la_category_fk_old";
			paramSource = new BeanPropertySqlParameterSource(obj);		 
			count = namedParamJdbcTemplate.update(deleteQry, paramSource);	
			
			int size = 0,size2 = 0,size3=0;
			String area  = obj.getLa_category_fk_new();
			String sub_area  = obj.getLa_sub_category_new();
			String value_old  = obj.getValue_old();
			//sub_area = sub_area.replaceFirst("^", "").replaceAll(",,", ",");
			String [] areaArr = new String [0];
			String [] La_sub_categoryArr = new String [0];
			String [] La_sub_categoryArr1 = new String [0];
			String [] value_oldArr1 = new String [0];
			if(!StringUtils.isEmpty(area) && area.contains(",")) {
				areaArr = area.split(",");
				size = areaArr.length;
				La_sub_categoryArr = sub_area.split(",_,");
				size2 = areaArr.length;
				
			}else if(!StringUtils.isEmpty(sub_area) && sub_area.contains(",")) {
				La_sub_categoryArr1 = sub_area.split(",");
				size3 = La_sub_categoryArr1.length;
				size = 1;
			}else if(!StringUtils.isEmpty(sub_area)) {
				size = 1;
				size3 = 1;
			}
			for(int i =0;i< size; i++) {
				 if(areaArr.length > 0) {
					String [] subName = null;
					obj.setLa_category_fk_new(areaArr[i]);
					if((!StringUtils.isEmpty(sub_area) && La_sub_categoryArr1.length > 0) 
							|| (!StringUtils.isEmpty(sub_area) && La_sub_categoryArr.length > 0)) {
						if(La_sub_categoryArr[i].contains(",")) {
							subName = La_sub_categoryArr[i].split(",");
							size3 = (La_sub_categoryArr1.length > 0) ? La_sub_categoryArr1.length : subName.length ;
						}else {
							subName = La_sub_categoryArr; size3 = 1;
							String sub_name = La_sub_categoryArr[i];
							subName[0] = sub_name;
						}
					}
					for(int j =0;j< size3; j++) {
						obj.setLa_sub_category_new(subName[j].replaceAll("&", ","));
						String insertQry = "INSERT INTO la_sub_category"
								+ "( la_category_fk,la_sub_category) VALUES (:la_category_fk_new,:la_sub_category_new)";
						 paramSource = new BeanPropertySqlParameterSource(obj);		 
						KeyHolder keyHolder = new GeneratedKeyHolder();
						count = namedParamJdbcTemplate.update(insertQry, paramSource,keyHolder);
						 String id = null;
							if(count > 0) {
								id = String.valueOf(keyHolder.getKey().intValue());
								 flag = true;
							}
							obj.setId(id);	
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
							
							String updateTableQry = "UPDATE "+bObj.getTable_name()+" SET "+bObj.getColumn_name()+" =:id WHERE "+bObj.getColumn_name()+"= :value_old " ;
							obj.setValue_old(idOld);
							paramSource = new BeanPropertySqlParameterSource(obj);		 
							namedParamJdbcTemplate.update(updateTableQry, paramSource);	
						}
					}
				 }else {
					for(int j =0;j< size3; j++) {
						if(!StringUtils.isEmpty(sub_area) && La_sub_categoryArr1.length > 0) {
							obj.setLa_sub_category_new(La_sub_categoryArr1[j].replaceAll("&", ","));
						}else {
							obj.setLa_sub_category_new(sub_area.replaceAll("&", ","));
						}
						String insertQry = "INSERT INTO la_sub_category"
								+ "( la_category_fk,la_sub_category) VALUES (:la_category_fk_new,:la_sub_category_new)";
						 paramSource = new BeanPropertySqlParameterSource(obj);		 
						KeyHolder keyHolder = new GeneratedKeyHolder();
						count = namedParamJdbcTemplate.update(insertQry, paramSource,keyHolder);
						 String id = null;
							if(count > 0) {
								id = String.valueOf(keyHolder.getKey().intValue());
								 flag = true;
							}
							obj.setId(id);	
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
							
							String updateTableQry = "UPDATE "+bObj.getTable_name()+" SET "+bObj.getColumn_name()+" =:id WHERE "+bObj.getColumn_name()+"= :value_old " ;
							obj.setValue_old(idOld);
							paramSource = new BeanPropertySqlParameterSource(obj);		 
							namedParamJdbcTemplate.update(updateTableQry, paramSource);	
						}
					}
			     }
			}
			String  enableQry =	"Alter Table la_sub_category CHECK Constraint All";
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
	public boolean deleteLandAcquisitionSubCategory(TrainingType obj) throws Exception {
		boolean flag = false;
		int count = 0;
		try {
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			String deleteQry ="DELETE from la_sub_category WHERE la_category_fk= :la_category_fk; ";
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
	public List<TrainingType> getLASubCategory(TrainingType obj) throws Exception {
		List<TrainingType> objList = null;
		try {
			String qry = "SELECT la_sub_category  from la_sub_category where la_category_fk= '"+ obj.getLa_category_fk()+"'";
			objList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));	
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}
}
