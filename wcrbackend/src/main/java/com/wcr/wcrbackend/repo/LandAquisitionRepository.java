package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.DBConnectionHandler;

@Repository
public class LandAquisitionRepository implements ILandAquisitionRepo {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int getTotalRecords(LandAcquisition obj, String searchParameter) throws Exception{
		int totalRecords = 0;
		try {
			String qry = "select count(distinct la_id) as total_records from la_land_identification li "
					+ "left join land_executives le on li.project_id_fk = le.project_id_fk  "
					+ "left join project p on li.project_id_fk = p.project_id "
					+ "left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+ "left join la_category c on sc.la_category_fk = c.la_category "
					+ "where  c.la_category is not null and c.la_category <> '' and la_id is not null  ";
			int arrSize = 0;

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				qry = qry + " and la_land_status_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and li.project_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				qry = qry + " and village = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " and c.la_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and sc.la_sub_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (li.project_id_fk like ? or survey_number like ? or village like ?"
						+ " or c.la_category like ? or sc.la_sub_category like ? or area_of_plot like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}

			Object[] pValues = new Object[arrSize];
			int i = 0;

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				pValues[i++] = obj.getLa_land_status_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				pValues[i++] = obj.getVillage();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
			if (!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
			}

			totalRecords = jdbcTemplate.queryForObject(qry, pValues, Integer.class);

		} catch (Exception e) {
			throw new Exception(e);
		}
		return totalRecords;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionList(LandAcquisition obj, int startIndex, int offset,
			String searchParameter)  throws Exception{
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select distinct la_id,survey_number,li.remarks,li.area_to_be_acquired,li.area_acquired,li.category_fk as type_of_land,li.la_land_status_fk,li.project_id_fk,li.project_id_fk,p.project_name,ISNULL(li.category_fk,c.la_category) as type_of_land ,sc.la_sub_category as sub_category_of_land, village_id,la_sub_category_fk,village,area_of_plot  as area_of_plot,modified_by,FORMAT(modified_date,'dd-MM-yyyy') as modified_date "
					+ " from la_land_identification li "
					+ "left join land_executives le on li.project_id_fk = le.project_id_fk  "
					+ "left join project p on li.project_id_fk = p.project_id "
					+ "left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+ "left join la_category c on sc.la_category_fk = c.la_category "
					+ "where  c.la_category is not null and c.la_category <> '' and la_id is not null  ";
			int arrSize = 0;

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				qry = qry + " and la_land_status_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and li.project_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				qry = qry + " and village = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " and c.la_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and sc.la_sub_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (li.project_id_fk like ? or  survey_number like ? or village like ?"
						+ " or c.la_category like ? or sc.la_sub_category like ? or area_of_plot like ?)";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}

			if (!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(offset)) {
				qry = qry + " ORDER BY la_id ASC offset ? rows  fetch next ? rows only";
				arrSize++;
				arrSize++;
			}

			Object[] pValues = new Object[arrSize];
			int i = 0;

			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				pValues[i++] = obj.getLa_land_status_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				pValues[i++] = obj.getVillage();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
			if (!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
				pValues[i++] = "%" + searchParameter + "%";
			}
			if (!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(offset)) {
				pValues[i++] = startIndex;
				pValues[i++] = offset;
			}

			objsList = jdbcTemplate.query(qry, pValues,
					new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionStatusList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "SELECT la_land_status_fk from la_land_identification li " + 
					 "left join land_executives le on li.project_id_fk = le.project_id_fk  "+
					"left join project p on li.project_id_fk = p.project_id "
					+"left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+"left join la_category c on sc.la_category_fk = c.la_category "+
					"where li.la_land_status_fk is not null and li.la_land_status_fk <> '' ";
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				qry = qry + " and la_land_status_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and li.project_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				qry = qry + " and village = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " and la_category = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and la_sub_category = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY la_land_status_fk ";
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				pValues[i++] = obj.getLa_land_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				pValues[i++] = obj.getVillage();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionVillagesList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "SELECT village from la_land_identification li " + 
					 "left join land_executives le on li.project_id_fk = le.project_id_fk  "+
					"left join project p on li.project_id_fk = p.project_id "+
					"left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+"left join la_category c on sc.la_category_fk = c.la_category "+
					"where village is not null and village <> '' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				qry = qry + " and la_land_status_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and li.project_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				qry = qry + " and village = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " and la_category = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and la_sub_category = ? ";
				arrSize++;
			}		
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY village ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				pValues[i++] = obj.getLa_land_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				pValues[i++] = obj.getVillage();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionTypesOfLandsList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "SELECT c.la_category as type_of_land from la_land_identification li " + 
					 "left join land_executives le on li.project_id_fk = le.project_id_fk  "+
					"left join project p on li.project_id_fk = p.project_id "+
					"left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+"left join la_category c on sc.la_category_fk = c.la_category "+
					"where c.la_category is not null and c.la_category <> '' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				qry = qry + " and la_land_status_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and li.project_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				qry = qry + " and village = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " and la_category = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and la_sub_category = ? ";
				arrSize++;
			}		
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY la_category ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				pValues[i++] = obj.getLa_land_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				pValues[i++] = obj.getVillage();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionSubCategoryList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "SELECT distinct sc.la_sub_category as sub_category_of_land from la_land_identification li " + 
					 "left join land_executives le on li.project_id_fk = le.project_id_fk  "+
					"left join project p on li.project_id_fk = p.project_id "+
					"left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+"left join la_category c on sc.la_category_fk = c.la_category "+
					"where la_sub_category is not null and la_sub_category <> '' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				qry = qry + " and la_land_status_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and li.project_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				qry = qry + " and village = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " and la_category = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and la_sub_category = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY la_sub_category ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_land_status_fk())) {
				pValues[i++] = obj.getLa_land_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getVillage())) {
				pValues[i++] = obj.getVillage();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getCoordinates(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objList = null;
	    try {
	        String chainageFrom = obj.getChainage_from();

	        if (chainageFrom == null || chainageFrom.trim().isEmpty()) {
	            return Collections.emptyList(); // return empty list instead of throwing
	        }

	        String qry = "select string_agg(chainages,',') as chainage_from," +
	                     " string_agg(latitude,',') as latitude," +
	                     " string_agg(longitude,',') as longitude " +
	                     " from chainages_master " +
	                     " where project_id='" + obj.getProject_id_fk() + "' " +
	                     " and id between (" +
	                     "    select min(id)-1 from chainages_master " +
	                     "    where project_id='" + obj.getProject_id_fk() + "' " +
	                     "    and chainages >= cast('" + chainageFrom + "' as decimal(18,2))" +
	                     " ) and (" +
	                     "    select min(id) from chainages_master " +
	                     "    where project_id='" + obj.getProject_id_fk() + "' " +
	                     "    and chainages >= cast('" + chainageFrom + "' as decimal(18,2))" +
	                     " )";

	        objList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<>(LandAcquisition.class));

	    } catch (Exception e) { 
	        e.printStackTrace();
	        throw new Exception(e);
	    }
	    return objList;
	}

	@Override
	public List<LandAcquisition> getSubCategoryList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = new ArrayList<LandAcquisition>();
		try {
			String qry = "select id,la_sub_category as sub_category_of_land, la_category_fk from la_sub_category ls "
					+ "LEFT OUTER JOIN la_category lc ON la_category_fk = la_category ";
					
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " where la_category_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " where la_sub_category  = ? ";
				arrSize++;
			}
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}	
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandsList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = new ArrayList<LandAcquisition>();
		try {
			String sub_category_of_land = getSubCategoryLand(obj.getSub_category_of_land());
			obj.setSub_category_of_land(sub_category_of_land);
			String qry = "select id as category_id,la_category as type_of_land, ls.la_sub_category as sub_category_of_land from la_category lc "
					+ "LEFT OUTER JOIN la_sub_category ls ON la_category  = la_category_fk ";
					
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " where la_sub_category  = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " where la_category = ? ";
				arrSize++;
			}
			qry = qry + " group by id,la_category,ls.la_sub_category";
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}	
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	
	private String getSubCategoryLand(String id) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sub_category_of_land = null;;
		try{
			con = jdbcTemplate.getDataSource().getConnection();
			String qry = "SELECT  la_sub_category  FROM la_sub_category where id = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if(rs.next()) {
				sub_category_of_land = rs.getString("la_sub_category");
			}
		}catch(Exception e){ 		
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, rs);
		}
		return sub_category_of_land;
	}

	@Override
	public boolean checkSurveyNumber(String survey_number, String village_id, String la_id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<LandAcquisition> getLADetails(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objList = null;
		try {
			String qry ="select distinct la_id,survey_number,li.remarks,li.area_to_be_acquired,c1.contract_id as contract_id_fk,li.area_acquired,li.category_fk as type_of_land,li.la_land_status_fk,li.project_id_fk,p.project_name,ISNULL(li.category_fk,c.la_category) as type_of_land ,sc.la_sub_category as sub_category_of_land,village_id,la_sub_category_fk,village " + 
					" from la_land_identification li " + 
					"left join contract c1 on c1.project_id_fk = li.project_id_fk "
					+ "left join land_executives le on li.project_id_fk = le.project_id_fk  "+
					"left join project p on li.project_id_fk = p.project_id "
					+"left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+"left join la_category c on sc.la_category_fk = c.la_category "
					+"where la_id is not null  and la_id=? ";			
			objList = jdbcTemplate.query( qry, new Object[] {obj.getLa_id()}, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<LandAcquisition> getStatusList() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry ="select status from la_status ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));	
		}catch(Exception e){ 
		throw new Exception(e); 
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getProjectsList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = new ArrayList<LandAcquisition>();
		try {
			String qry = "select distinct project_id,project_name "
					+ "from project p "
					+ "left join land_executives us on p.project_id = us.project_id_fk   "
					+ "where project_id is not null ";
			
					
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + "and project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}			
			
			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}	
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
			}			
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandsListForLAForm(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select la_category as type_of_land from la_category";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getIssueCatogoriesList() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry ="select category from issue_category";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getSubCategorysListForLAForm(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select id,la_sub_category as sub_category_of_land,la_category_fk from la_sub_category";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getUnitsList() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select id, unit, value from money_unit";			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLaFileType() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select la_file_type from la_file_type ";			
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLaLandStatus() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry ="select la_land_status from la_land_status";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

}
