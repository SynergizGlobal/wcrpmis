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
	public int getTotalRecords(LandAcquisition obj, String searchParameter) throws Exception {
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
			String searchParameter) throws Exception {
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
			String qry = "SELECT la_land_status_fk from la_land_identification li "
					+ "left join land_executives le on li.project_id_fk = le.project_id_fk  "
					+ "left join project p on li.project_id_fk = p.project_id "
					+ "left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+ "left join la_category c on sc.la_category_fk = c.la_category "
					+ "where li.la_land_status_fk is not null and li.la_land_status_fk <> '' ";

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
				qry = qry + " and la_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and la_sub_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY la_land_status_fk ";
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
			objsList = jdbcTemplate.query(qry, pValues,
					new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionVillagesList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "SELECT village from la_land_identification li "
					+ "left join land_executives le on li.project_id_fk = le.project_id_fk  "
					+ "left join project p on li.project_id_fk = p.project_id "
					+ "left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+ "left join la_category c on sc.la_category_fk = c.la_category "
					+ "where village is not null and village <> '' ";
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
				qry = qry + " and la_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and la_sub_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY village ";
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
			objsList = jdbcTemplate.query(qry, pValues,
					new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionTypesOfLandsList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "SELECT c.la_category as type_of_land from la_land_identification li "
					+ "left join land_executives le on li.project_id_fk = le.project_id_fk  "
					+ "left join project p on li.project_id_fk = p.project_id "
					+ "left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+ "left join la_category c on sc.la_category_fk = c.la_category "
					+ "where c.la_category is not null and c.la_category <> '' ";
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
				qry = qry + " and la_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and la_sub_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY la_category ";
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
			objsList = jdbcTemplate.query(qry, pValues,
					new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionSubCategoryList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "SELECT distinct sc.la_sub_category as sub_category_of_land from la_land_identification li "
					+ "left join land_executives le on li.project_id_fk = le.project_id_fk  "
					+ "left join project p on li.project_id_fk = p.project_id "
					+ "left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+ "left join la_category c on sc.la_category_fk = c.la_category "
					+ "where la_sub_category is not null and la_sub_category <> '' ";
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
				qry = qry + " and la_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and la_sub_category = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and le.executive_user_id_fk = ? ";
				arrSize++;
			}
			qry = qry + " GROUP BY la_sub_category ";
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
			objsList = jdbcTemplate.query(qry, pValues,
					new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
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

			String qry = "select string_agg(chainages,',') as chainage_from," + " string_agg(latitude,',') as latitude,"
					+ " string_agg(longitude,',') as longitude " + " from chainages_master " + " where project_id='"
					+ obj.getProject_id_fk() + "' " + " and id between ("
					+ "    select min(id)-1 from chainages_master " + "    where project_id='" + obj.getProject_id_fk()
					+ "' " + "    and chainages >= cast('" + chainageFrom + "' as decimal(18,2))" + " ) and ("
					+ "    select min(id) from chainages_master " + "    where project_id='" + obj.getProject_id_fk()
					+ "' " + "    and chainages >= cast('" + chainageFrom + "' as decimal(18,2))" + " )";

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
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " where la_category_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " where la_sub_category  = ? ";
				arrSize++;
			}

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
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
	public List<LandAcquisition> getLandsList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = new ArrayList<LandAcquisition>();
		try {
			String sub_category_of_land = getSubCategoryLand(obj.getSub_category_of_land());
			obj.setSub_category_of_land(sub_category_of_land);
			String qry = "select id as category_id,la_category as type_of_land, ls.la_sub_category as sub_category_of_land from la_category lc "
					+ "LEFT OUTER JOIN la_sub_category ls ON la_category  = la_category_fk ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " where la_sub_category  = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				qry = qry + " where la_category = ? ";
				arrSize++;
			}
			qry = qry + " group by id,la_category,ls.la_sub_category";
			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				pValues[i++] = obj.getSub_category_of_land();
			}
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getType_of_land())) {
				pValues[i++] = obj.getType_of_land();
			}

			objsList = jdbcTemplate.query(qry, pValues,
					new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	private String getSubCategoryLand(String id) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sub_category_of_land = null;
		;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String qry = "SELECT  la_sub_category  FROM la_sub_category where id = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				sub_category_of_land = rs.getString("la_sub_category");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
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
			String qry = "select distinct la_id,survey_number,li.remarks,li.area_to_be_acquired,c1.contract_id as contract_id_fk,li.area_acquired,li.category_fk as type_of_land,li.la_land_status_fk,li.project_id_fk,p.project_name,ISNULL(li.category_fk,c.la_category) as type_of_land ,sc.la_sub_category as sub_category_of_land,village_id,la_sub_category_fk,village "
					+ " from la_land_identification li "
					+ "left join contract c1 on c1.project_id_fk = li.project_id_fk "
					+ "left join land_executives le on li.project_id_fk = le.project_id_fk  "
					+ "left join project p on li.project_id_fk = p.project_id "
					+ "left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+ "left join la_category c on sc.la_category_fk = c.la_category "
					+ "where la_id is not null  and la_id=? ";
			objList = jdbcTemplate.query(qry, new Object[] { obj.getLa_id() },
					new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<LandAcquisition> getStatusList() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select status from la_status ";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getProjectsList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = new ArrayList<LandAcquisition>();
		try {
			String qry = "select distinct project_id,project_name " + "from project p "
					+ "left join land_executives us on p.project_id = us.project_id_fk   "
					+ "where project_id is not null ";

			int arrSize = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + "and project_id_fk = ? ";
				arrSize++;
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and us.executive_user_id_fk = ? ";
				arrSize++;
			}

			Object[] pValues = new Object[arrSize];

			int i = 0;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			if (!StringUtils.isEmpty(obj) && !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
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
	public List<LandAcquisition> getLandsListForLAForm(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select la_category as type_of_land from la_category";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getIssueCatogoriesList() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select category from issue_category";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getSubCategorysListForLAForm(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select id,la_sub_category as sub_category_of_land,la_category_fk from la_sub_category";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getUnitsList() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select id, unit, value from money_unit";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLaFileType() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select la_file_type from la_file_type ";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<LandAcquisition> getLaLandStatus() throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry = "select la_land_status from la_land_status";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public LandAcquisition getLandAcquisitionForm(LandAcquisition obj) throws Exception {
		LandAcquisition LADetails = null;
		try {
			String qry = "select distinct la_id,li.remarks,(select executive_user_id_fk from land_executives re where li.project_id_fk = re.project_id_fk and executive_user_id_fk = ?) as executive_user_id_fk,"
					+ "li.area_to_be_acquired as area_to_be_acquired,ISNULL(li.category_fk,c.la_category) as type_of_land,li.la_land_status_fk, project_id_fk,li.project_id_fk,p.project_name,"
					+ "sc.la_sub_category as sub_category_of_land, li.survey_number, li.village_id, li.village, taluka, dy_slr, sdo, li.collector, "
					+ "FORMAT(proposal_submission_date_to_collector,'dd-MM-yyyy') AS proposal_submission_date_to_collector, area_of_plot as area_of_plot, "
					+ "jm_fee_amount,jm_fee_amount_units, li.special_feature,li.area_acquired as area_acquired,li.private_land_process,chainage_from,"
					+ "chainage_to, FORMAT(jm_fee_letter_received_date,'dd-MM-yyyy') AS jm_fee_letter_received_date,FORMAT(jm_fee_paid_date,'dd-MM-yyyy') AS jm_fee_paid_date,"
					+ "FORMAT(jm_start_date,'dd-MM-yyyy') AS  jm_start_date,FORMAT(jm_completion_date,'dd-MM-yyyy') AS jm_completion_date, FORMAT(jm_sheet_date_to_sdo,'dd-MM-yyyy') AS jm_sheet_date_to_sdo,"
					+ "jm_remarks, jm_approval, li.issues,lg.id, lg.la_id_fk,FORMAT(lg.proposal_submission,'dd-MM-yyyy') AS proposal_submission, lg.proposal_submission_status_fk, "
					+ "FORMAT(lg.valuation_date,'dd-MM-yyyy') AS valuation_date, FORMAT(lg.letter_for_payment,'dd-MM-yyyy') AS letter_for_payment,lg.amount_demanded,lg.lfp_status_fk as lfp_status_fk,"
					+ "FORMAT(lg.approval_for_payment,'dd-MM-yyyy') AS approval_for_payment,FORMAT(lg.payment_date,'dd-MM-yyyy') AS payment_date, lg.amount_paid, lg.payment_status_fk, "
					+ "FORMAT(lg.possession_date,'dd-MM-yyyy') AS possession_date,lg.possession_status_fk,lf.demanded_amount_units as demanded_amount_units_forest,"
					+ "lf.payment_amount_units as payment_amount_units_forest, lg.amount_demanded_units,lg.amount_paid_units, FORMAT(lf.on_line_submission,'dd-MM-yyyy') AS forest_online_submission, "
					+ "FORMAT(lf.submission_date_to_dycfo,'dd-MM-yyyy') AS forest_submission_date_to_dycfo, FORMAT(lf.submission_date_to_ccf_thane,'dd-MM-yyyy') AS forest_submission_date_to_ccf_thane, "
					+ "FORMAT(lf.submission_date_to_nodal_officer,'dd-MM-yyyy') AS forest_submission_date_to_nodal_officer, "
					+ "FORMAT(lf.submission_date_to_revenue_secretary_mantralaya,'dd-MM-yyyy') AS forest_submission_date_to_revenue_secretary_mantralaya, "
					+ "FORMAT(lf.submission_date_to_regional_office_nagpur,'dd-MM-yyyy') AS forest_submission_date_to_regional_office_nagpur, "
					+ "FORMAT(lf.date_of_approval_by_regional_office_nagpur,'dd-MM-yyyy') AS forest_date_of_approval_by_regional_office_nagpur,"
					+ "FORMAT(cast(lf.valuation_by_DyCFO as date)  ,'dd-MM-yyyy') AS forest_valuation_by_dycfo,lf.demanded_amount as forest_demanded_amount,"
					+ "lf.payment_amount as forest_payment_amount, FORMAT(lf.approval_for_payment,'dd-MM-yyyy') AS forest_approval_for_payment,"
					+ "FORMAT(lf.payment_date,'dd-MM-yyyy') AS forest_payment_date,FORMAT(lf.possession_date,'dd-MM-yyyy') AS forest_possession_date,"
					+ "lf.possession_status_fk as forest_possession_status_fk,lf.payment_status_fk as forest_payment_status_fk ,"
					+ "lr.demanded_amount_units,lr.payment_amount_units as payment_amount_units_railway,FORMAT(lr.online_submission,'dd-MM-yyyy') AS railway_online_submission,"
					+ "FORMAT(lr.submission_date_to_DyCFO,'dd-MM-yyyy') AS railway_submission_date_to_DyCFO, FORMAT(lr.submission_date_to_CCF_Thane,'dd-MM-yyyy') AS railway_submission_date_to_CCF_Thane,"
					+ "FORMAT([submission_date_to_nodal_officer/CCF Nagpur] ,'dd-MM-yyyy') AS railway_submission_date_to_nodal_officer_CCF_Nagpur, "
					+ "FORMAT(lr.submission_date_to_revenue_secretary_mantralaya,'dd-MM-yyyy') AS railway_submission_date_to_revenue_secretary_mantralaya, "
					+ "FORMAT(lr.submission_date_to_regional_office_nagpur,'dd-MM-yyyy') AS railway_submission_date_to_regional_office_nagpur, "
					+ "FORMAT( lr.date_of_approval_by_Rregional_Office_agpur,'dd-MM-yyyy') AS railway_date_of_approval_by_Rregional_Office_agpur,"
					+ "FORMAT(cast(lr.valuation_by_DyCFO as date)  ,'dd-MM-yyyy') AS railway_valuation_by_DyCFO, lr.demanded_amount as railway_demanded_amount, "
					+ "FORMAT(cast(lr.approval_for_payment as date),'dd-MM-yyyy') AS railway_approval_for_payment, FORMAT(lr.payment_date,'dd-MM-yyyy') AS railway_payment_date,"
					+ "lr.payment_amount as railway_payment_amount, lr.payment_status as railway_payment_status, FORMAT(lr.possession_date,'dd-MM-yyyy') AS railway_possession_date,"
					+ "lr.possession_status as railway_possession_status,  lpa.basic_rate_units,lpa.agriculture_tree_rate_units,lpa.forest_tree_rate_units, "
					+ "lpa.name_of_the_owner, lpa.basic_rate, lpa.agriculture_tree_nos, lpa.agriculture_tree_rate, lpa.forest_tree_nos,lpa.forest_tree_rate,"
					+ "FORMAT(lpa.consent_from_owner,'dd-MM-yyyy') AS consent_from_owner, FORMAT(lpa.legal_search_report,'dd-MM-yyyy') AS legal_search_report, "
					+ "FORMAT(lpa.date_of_registration,'dd-MM-yyyy') AS date_of_registration, FORMAT(lpa.date_of_possession,'dd-MM-yyyy') AS date_of_possession,"
					+ "lpa.possession_status_fk as private_possession_status_fk, lpa.hundred_percent_Solatium as hundred_percent_Solatium,"
					+ "lpa.extra_25_percent as extra_25_percent, lpa.total_rate_divide_m2 as total_rate_divide_m2,lpa.land_compensation as land_compensation, "
					+ "lpa.agriculture_tree_compensation as agriculture_tree_compensation,lpa.forest_tree_compensation as forest_tree_compensation,"
					+ "lpa.structure_compensation as structure_compensation,lpa.borewell_compensation as borewell_compensation,lpa.total_compensation as total_compensation,"
					+ "lpv.payment_amount_units,FORMAT(lpv.forest_tree_survey ,'dd-MM-yyyy') AS forest_tree_survey,FORMAT(lpv.forest_tree_valuation ,'dd-MM-yyyy') AS forest_tree_valuation, "
					+ "lpv.forest_tree_valuation_status_fk,FORMAT(lpv.horticulture_tree_survey ,'dd-MM-yyyy') AS horticulture_tree_survey,"
					+ "FORMAT(lpv.horticulture_tree_valuation ,'dd-MM-yyyy') AS horticulture_tree_valuation, FORMAT(lpv.structure_survey ,'dd-MM-yyyy') AS structure_survey,"
					+ "FORMAT(lpv.structure_valuation ,'dd-MM-yyyy') AS structure_valuation,FORMAT(lpv.borewell_survey ,'dd-MM-yyyy') AS borewell_survey,"
					+ "FORMAT(lpv.borewell_valuation ,'dd-MM-yyyy') AS borewell_valuation, lpv.horticulture_tree_valuation_status_fk, "
					+ "lpv.structure_valuation_status_fk, lpv.borewell_valuation_status_fk, lpv.rfp_to_adtp_status_fk, "
					+ "FORMAT(lpv.date_of_rfp_to_adtp ,'dd-MM-yyyy') AS date_of_rfp_to_adtp,FORMAT(lpv.date_of_rate_fixation_of_land ,'dd-MM-yyyy') AS date_of_rate_fixation_of_land, "
					+ "FORMAT(lpv.sdo_demand_for_payment ,'dd-MM-yyyy') AS sdo_demand_for_payment,FORMAT(lpv.date_of_approval_for_payment ,'dd-MM-yyyy') AS date_of_approval_for_payment, "
					+ "lpv.payment_amount as payment_amount, FORMAT(lpv.payment_date ,'dd-MM-yyyy') AS private_payment_date   ,ira.collector as private_ira_collector, "
					+ "FORMAT(submission_of_proposal_to_GM ,'dd-MM-yyyy') AS submission_of_proposal_to_GM,FORMAT(approval_of_GM ,'dd-MM-yyyy') AS  approval_of_GM,"
					+ "FORMAT(draft_letter_to_con_for_approval_rp ,'dd-MM-yyyy') AS draft_letter_to_con_for_approval_rp,"
					+ "FORMAT(date_of_approval_of_construction_rp ,'dd-MM-yyyy') AS  date_of_approval_of_construction_rp,"
					+ "FORMAT(date_of_uploading_of_gazette_notification_rp ,'dd-MM-yyyy') AS date_of_uploading_of_gazette_notification_rp,"
					+ "FORMAT(publication_in_gazette_rp ,'dd-MM-yyyy') AS publication_in_gazette_rp,"
					+ "FORMAT(date_of_proposal_to_DC_for_nomination ,'dd-MM-yyyy') AS  date_of_proposal_to_DC_for_nomination, "
					+ "FORMAT(date_of_nomination_of_competenta_authority ,'dd-MM-yyyy') AS date_of_nomination_of_competenta_authority,longitude,latitude,FORMAT(draft_letter_to_con_for_approval_ca,'dd-MM-yyyy') as draft_letter_to_con_for_approval_ca,"
					+ "FORMAT(date_of_approval_of_construction_ca,'dd-MM-yyyy') as date_of_approval_of_construction_ca,"
					+ "FORMAT(date_of_uploading_of_gazette_notification_ca,'dd-MM-yyyy') as date_of_uploading_of_gazette_notification_ca,"
					+ "FORMAT(publication_in_gazette_ca,'dd-MM-yyyy') as publication_in_gazette_ca,"
					+ "FORMAT(date_of_submission_of_draft_notification_to_CALA,'dd-MM-yyyy') as date_of_submission_of_draft_notification_to_CALA,"
					+ "FORMAT(approval_of_CALA_20a,'dd-MM-yyyy') as approval_of_CALA_20a,"
					+ "FORMAT(draft_letter_to_con_for_approval_20a,'dd-MM-yyyy') as draft_letter_to_con_for_approval_20a,"
					+ "FORMAT(date_of_approval_of_construction_20a,'dd-MM-yyyy') as date_of_approval_of_construction_20a,"
					+ "FORMAT(date_of_uploading_of_gazette_notification_20a,'dd-MM-yyyy') as date_of_uploading_of_gazette_notification_20a,"
					+ "FORMAT(publication_in_gazette_20a,'dd-MM-yyyy') as publication_in_gazette_20a,"
					+ "FORMAT(publication_in_2_local_news_papers_20a,'dd-MM-yyyy') as publication_in_2_local_news_papers_20a,"
					+ "FORMAT(pasting_of_notification_in_villages_20a,'dd-MM-yyyy') as pasting_of_notification_in_villages_20a,"
					+ "FORMAT(receipt_of_grievances,'dd-MM-yyyy') as receipt_of_grievances,"
					+ "FORMAT(disposal_of_grievances,'dd-MM-yyyy') as disposal_of_grievances,"
					+ "FORMAT(date_of_submission_of_draft_notification_to_CALA_20e,'dd-MM-yyyy') as date_of_submission_of_draft_notification_to_CALA_20e,"
					+ "FORMAT(approval_of_CALA_20e,'dd-MM-yyyy') as approval_of_CALA_20e,"
					+ "FORMAT(publication_of_notice_in_2_local_news_papers_20e,'dd-MM-yyyy') as publication_of_notice_in_2_local_news_papers_20e,"
					+ "FORMAT(date_of_submission_of_draft_notification_to_CALA_20f,'dd-MM-yyyy') as date_of_submission_of_draft_notification_to_CALA_20f,"
					+ "FORMAT(approval_of_CALA_20f,'dd-MM-yyyy') as approval_of_CALA_20f,"
					+ "FORMAT(draft_letter_to_con_for_approval_20f,'dd-MM-yyyy') as draft_letter_to_con_for_approval_20f,"
					+ "FORMAT(date_of_approval_of_construction_20f,'dd-MM-yyyy') as date_of_approval_of_construction_20f,"
					+ "FORMAT(date_of_uploading_of_gazette_notification_20f,'dd-MM-yyyy') as date_of_uploading_of_gazette_notification_20f,"
					+ "FORMAT(publication_in_gazette_20f,'dd-MM-yyyy') as publication_in_gazette_20f,"
					+ "FORMAT(publication_of_notice_in_2_local_news_papers_20f,'dd-MM-yyyy') as publication_of_notice_in_2_local_news_papers_20f,FORMAT(draft_letter_to_con_for_approval_20e,'dd-MM-yyyy') as draft_letter_to_con_for_approval_20e,"
					+ "FORMAT(date_of_approval_of_construction_20e,'dd-MM-yyyy') as date_of_approval_of_construction_20e,FORMAT(date_of_uploading_of_gazette_notification_20e,'dd-MM-yyyy') as date_of_uploading_of_gazette_notification_20e,"
					+ "FORMAT(publication_in_gazette_20e,'dd-MM-yyyy') as publication_in_gazette_20e  "
					+ "from la_land_identification li "
					+ "left join la_government_land_acquisition lg on li.la_id = lg.la_id_fk "
					+ "left join la_forest_land_acquisition lf on li.la_id = lf.la_id_fk "
					+ "left join la_railway_land_acquisition lr on li.la_id = lr.la_id_fk "
					+ "left join la_private_land_acquisition lpa on li.la_id = lpa.la_id_fk "
					+ "left join la_private_land_valuation lpv on li.la_id = lpv.la_id_fk "
					+ "left join la_private_ira ira on li.la_id = ira.la_id_fk "
					+ "left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+ "left join project p on li.project_id_fk = p.project_id "
					+ "left join la_category c on sc.la_category_fk = c.la_category " + " where la_id is not null";
			int arrSize = 1;
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_id())) {
				qry = qry + " and la_id = ?";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = obj.getUser_id();
			if (!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getLa_id())) {
				pValues[i++] = obj.getLa_id();
			}
			LADetails = (LandAcquisition) jdbcTemplate.queryForObject(qry, pValues,
					new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));

			if (!StringUtils.isEmpty(LADetails)) {
				String qry2 = "select id as la_file_id, la_id_fk, la_file_type_fk, name, attachment from la_files where la_id_fk = ? ";
				List<LandAcquisition> objList = jdbcTemplate.query(qry2, new Object[] { obj.getLa_id() },
						new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
				LADetails.setLaFilesList(objList);
			}

		} catch (Exception e) {
			throw new Exception(e);
		}
		return LADetails;
	}

	@Override
	public List<LandAcquisition> getRailwayList(String la_id) throws Exception {
		List<LandAcquisition> objList = null;
		try {
			String qry = "select la_id_fk,lr.demanded_amount_units,lr.payment_amount_units as payment_amount_units_railway,FORMAT(lr.online_submission,'dd-MM-yyyy') AS railway_online_submission," + 
					"FORMAT(lr.submission_date_to_DyCFO,'dd-MM-yyyy') AS railway_submission_date_to_DyCFO, FORMAT(lr.submission_date_to_CCF_Thane,'dd-MM-yyyy') AS railway_submission_date_to_CCF_Thane, FORMAT(lr.[submission_date_to_nodal_officer/CCF Nagpur],'dd-MM-yyyy') AS railway_submission_date_to_nodal_officer_CCF_Nagpur, " + 
					" FORMAT(lr.submission_date_to_revenue_secretary_mantralaya,'dd-MM-yyyy') AS railway_submission_date_to_revenue_secretary_mantralaya, FORMAT(lr.submission_date_to_regional_office_nagpur,'dd-MM-yyyy') AS railway_submission_date_to_regional_office_nagpur, FORMAT( lr.date_of_approval_by_Rregional_Office_agpur,'dd-MM-yyyy') AS railway_date_of_approval_by_Rregional_Office_agpur," + 
					"FORMAT(cast(lr.valuation_by_DyCFO as date) ,'dd-MM-yyyy') AS railway_valuation_by_DyCFO, lr.demanded_amount as railway_demanded_amount, FORMAT(cast(lr.approval_for_payment as date),'dd-MM-yyyy') AS railway_approval_for_payment, FORMAT(lr.payment_date,'dd-MM-yyyy') AS railway_payment_date,lr.payment_amount as railway_payment_amount, lr.payment_status as railway_payment_status, FORMAT(lr.possession_date,'dd-MM-yyyy') AS railway_possession_date, lr.possession_status as railway_possession_status" + 
					" from la_railway_land_acquisition lr " + 
					"left join la_land_identification li on lr.la_id_fk = li.la_id  " + 
					"where la_id_fk = ? ";
			
			objList = jdbcTemplate.query( qry, new Object[] {la_id}, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<LandAcquisition> getForestList(String la_id) throws Exception {
		List<LandAcquisition> objList = null;
		try {
			String qry = "select la_id_fk, FORMAT(lf.on_line_submission,'dd-MM-yyyy') AS forest_online_submission, FORMAT(lf.submission_date_to_dycfo,'dd-MM-yyyy') AS forest_submission_date_to_dycfo, FORMAT(lf.submission_date_to_ccf_thane,'dd-MM-yyyy') AS forest_submission_date_to_ccf_thane, " + 
					"FORMAT(lf.submission_date_to_nodal_officer,'dd-MM-yyyy') AS forest_submission_date_to_nodal_officer, FORMAT(lf.submission_date_to_revenue_secretary_mantralaya,'dd-MM-yyyy') AS forest_submission_date_to_revenue_secretary_mantralaya, FORMAT(lf.submission_date_to_regional_office_nagpur,'dd-MM-yyyy') AS forest_submission_date_to_regional_office_nagpur," + 
					" FORMAT(lf.date_of_approval_by_regional_office_nagpur,'dd-MM-yyyy') AS forest_date_of_approval_by_regional_office_nagpur, FORMAT(lf.valuation_by_dycfo,'dd-MM-yyyy') AS forest_valuation_by_dycfo,cast(lf.demanded_amount as CHAR) as forest_demanded_amount,cast(lf.payment_amount  as CHAR) as forest_payment_amount, FORMAT(lf.approval_for_payment,'dd-MM-yyyy') AS forest_approval_for_payment" + 
					", FORMAT(lf.payment_date,'dd-MM-yyyy') AS forest_payment_date,FORMAT(lf.possession_date,'dd-MM-yyyy') AS forest_possession_date,lf.possession_status_fk as forest_possession_status_fk," + 
					"lf.payment_status_fk as forest_payment_status_fk" + 
					" from la_forest_land_acquisition lf " + 
					"left join la_land_identification li on lf.la_id_fk = li.la_id  " + 
					"where la_id_fk = ? ";
			
			objList = jdbcTemplate.query( qry, new Object[] {la_id}, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<LandAcquisition> getGovList(String la_id) throws Exception {
		List<LandAcquisition> objList = null;
		try {
			String qry = "select la_id_fk,lg.id, lg.la_id_fk,FORMAT(lg.proposal_submission,'dd-MM-yyyy') AS proposal_submission, lg.proposal_submission_status_fk, FORMAT(lg.valuation_date,'dd-MM-yyyy') AS valuation_date, FORMAT(lg.letter_for_payment,'dd-MM-yyyy') AS letter_for_payment," + 
					"lg.amount_demanded,cast(lg.lfp_status_fk as CHAR) as lfp_status_fk,FORMAT(lg.approval_for_payment,'dd-MM-yyyy') AS approval_for_payment,FORMAT(lg.payment_date,'dd-MM-yyyy') AS payment_date, lg.amount_paid, lg.payment_status_fk, FORMAT(lg.possession_date,'dd-MM-yyyy') AS possession_date," + 
					"lg.possession_status_fk,FORMAT(lg.planned_date_of_possession ,'dd-MM-yyyy') as planned_date_of_possession " + 
					" from la_government_land_acquisition lg " + 
					"left join la_land_identification li on lg.la_id_fk = li.la_id  " + 
					"where la_id_fk = ? ";
			
			objList = jdbcTemplate.query( qry, new Object[] {la_id}, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<LandAcquisition> getPrivateLandList(String la_id) throws Exception {
		List<LandAcquisition> objList = null;
		try {
			String qry = "select la_id_fk,lpa.basic_rate_units,lpa.agriculture_tree_rate_units,lpa.forest_tree_rate_units, lpa.name_of_the_owner, lpa.basic_rate, isnull(lpa.agriculture_tree_nos,0) as agriculture_tree_nos, lpa.agriculture_tree_rate, isnull(lpa.forest_tree_nos,0) as forest_tree_nos," + 
					"lpa.forest_tree_rate,FORMAT(lpa.consent_from_owner,'dd-MM-yyyy') AS consent_from_owner, FORMAT(lpa.legal_search_report,'dd-MM-yyyy') AS legal_search_report, FORMAT(lpa.date_of_registration,'dd-MM-yyyy') AS date_of_registration, FORMAT(lpa.date_of_possession,'dd-MM-yyyy') AS date_of_possession, lpa.possession_status_fk as private_possession_status_fk," + 
					"lpa.hundred_percent_Solatium,lpa.extra_25_percent, lpa.total_rate_divide_m2,lpa.land_compensation," + 
					"lpa.agriculture_tree_compensation,lpa.forest_tree_compensation,lpa.structure_compensation,lpa.borewell_compensation,lpa.total_compensation" + 
					" from la_private_land_acquisition lpa " + 
					"left join la_land_identification li on lpa.la_id_fk = li.la_id  " + 
					"where la_id_fk = ? ";
			
			objList = jdbcTemplate.query( qry, new Object[] {la_id}, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<LandAcquisition> getPrivateValList(String la_id) throws Exception {
		List<LandAcquisition> objList = null;
		try {
			String qry = "select la_id_fk,lpv.payment_amount_units,FORMAT(lpv.forest_tree_survey ,'dd-MM-yyyy') AS forest_tree_survey,FORMAT(lpv.forest_tree_valuation ,'dd-MM-yyyy') AS forest_tree_valuation, lpv.forest_tree_valuation_status_fk,FORMAT(lpv.horticulture_tree_survey ,'dd-MM-yyyy') AS horticulture_tree_survey,FORMAT(lpv.horticulture_tree_valuation ,'dd-MM-yyyy') AS horticulture_tree_valuation," + 
					"FORMAT(lpv.structure_survey ,'dd-MM-yyyy') AS structure_survey,FORMAT(lpv.structure_valuation ,'dd-MM-yyyy') AS structure_valuation,FORMAT(lpv.borewell_survey ,'dd-MM-yyyy') AS borewell_survey,FORMAT(lpv.borewell_valuation ,'dd-MM-yyyy') AS borewell_valuation, lpv.horticulture_tree_valuation_status_fk, lpv.structure_valuation_status_fk," + 
					"lpv.borewell_valuation_status_fk, lpv.rfp_to_adtp_status_fk, FORMAT(lpv.date_of_rfp_to_adtp ,'dd-MM-yyyy') AS date_of_rfp_to_adtp,FORMAT(lpv.date_of_rate_fixation_of_land ,'dd-MM-yyyy') AS date_of_rate_fixation_of_land, FORMAT(lpv.sdo_demand_for_payment ,'dd-MM-yyyy') AS sdo_demand_for_payment,FORMAT(lpv.date_of_approval_for_payment ,'dd-MM-yyyy') AS date_of_approval_for_payment," + 
					"cast(lpv.payment_amount as CHAR) as payment_amount, FORMAT(lpv.payment_date ,'dd-MM-yyyy') AS private_payment_date  " + 
					" from la_private_land_valuation lpv " + 
					"left join la_land_identification li on lpv.la_id_fk = li.la_id  " + 
					"where la_id_fk = ? ";
			
			objList = jdbcTemplate.query( qry, new Object[] {la_id}, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<LandAcquisition> geprivateIRAList(String la_id) throws Exception {
		List<LandAcquisition> objList = null;
		try {
			String qry = "select la_id_fk,ira.collector as private_ira_collector, FORMAT(submission_of_proposal_to_GM ,'dd-MM-yyyy') AS submission_of_proposal_to_GM,FORMAT(approval_of_GM ,'dd-MM-yyyy') AS  approval_of_GM,FORMAT(draft_letter_to_con_for_approval_rp ,'dd-MM-yyyy') AS draft_letter_to_con_for_approval_rp,FORMAT(date_of_approval_of_construction_rp ,'dd-MM-yyyy') AS  date_of_approval_of_construction_rp,FORMAT(date_of_uploading_of_gazette_notification_rp ,'dd-MM-yyyy') AS date_of_uploading_of_gazette_notification_rp," + 
					"FORMAT(publication_in_gazette_rp ,'dd-MM-yyyy') AS publication_in_gazette_rp,FORMAT(date_of_proposal_to_DC_for_nomination ,'dd-MM-yyyy') AS  date_of_proposal_to_DC_for_nomination, FORMAT(date_of_nomination_of_competenta_authority ,'dd-MM-yyyy') AS date_of_nomination_of_competenta_authority, FORMAT(draft_letter_to_con_for_approval_ca ,'dd-MM-yyyy') AS draft_letter_to_con_for_approval_ca, FORMAT(date_of_approval_of_construction_ca ,'dd-MM-yyyy') AS date_of_approval_of_construction_ca, " + 
					"FORMAT(date_of_uploading_of_gazette_notification_ca ,'dd-MM-yyyy') AS date_of_uploading_of_gazette_notification_ca, FORMAT(publication_in_gazette_ca ,'dd-MM-yyyy') AS publication_in_gazette_ca, FORMAT(date_of_submission_of_draft_notification_to_CALA ,'dd-MM-yyyy') AS date_of_submission_of_draft_notification_to_CALA,FORMAT(approval_of_CALA_20a ,'dd-MM-yyyy') AS approval_of_CALA_20a,FORMAT(draft_letter_to_con_for_approval_20a ,'dd-MM-yyyy') AS draft_letter_to_con_for_approval_20a," + 
					"FORMAT(date_of_approval_of_construction_20a ,'dd-MM-yyyy') AS date_of_approval_of_construction_20a,FORMAT(date_of_uploading_of_gazette_notification_20a ,'dd-MM-yyyy') AS date_of_uploading_of_gazette_notification_20a,FORMAT(publication_in_gazette_20a ,'dd-MM-yyyy') AS publication_in_gazette_20a,FORMAT(publication_in_2_local_news_papers_20a ,'dd-MM-yyyy') AS publication_in_2_local_news_papers_20a,FORMAT(pasting_of_notification_in_villages_20a ,'dd-MM-yyyy') AS pasting_of_notification_in_villages_20a," + 
					"FORMAT(receipt_of_grievances ,'dd-MM-yyyy') AS  receipt_of_grievances, FORMAT(disposal_of_grievances ,'dd-MM-yyyy') AS disposal_of_grievances, FORMAT(date_of_submission_of_draft_notification_to_CALA_20e ,'dd-MM-yyyy') AS date_of_submission_of_draft_notification_to_CALA_20e, FORMAT(approval_of_CALA_20e ,'dd-MM-yyyy') AS  approval_of_CALA_20e,FORMAT(draft_letter_to_con_for_approval_20e ,'dd-MM-yyyy') AS  draft_letter_to_con_for_approval_20e,"
					+ "FORMAT(date_of_approval_of_construction_20e ,'dd-MM-yyyy') AS  date_of_approval_of_construction_20e,FORMAT(date_of_uploading_of_gazette_notification_20e ,'dd-MM-yyyy') AS date_of_uploading_of_gazette_notification_20e,FORMAT(publication_in_gazette_20e ,'dd-MM-yyyy') AS  publication_in_gazette_20e,FORMAT(publication_of_notice_in_2_local_news_papers_20e ,'dd-MM-yyyy') AS publication_of_notice_in_2_local_news_papers_20e,FORMAT(date_of_submission_of_draft_notification_to_CALA_20f ,'dd-MM-yyyy') AS date_of_submission_of_draft_notification_to_CALA_20f," + 
					"FORMAT(approval_of_CALA_20f ,'dd-MM-yyyy') AS approval_of_CALA_20f,FORMAT(draft_letter_to_con_for_approval_20f ,'dd-MM-yyyy') AS draft_letter_to_con_for_approval_20f,FORMAT(date_of_approval_of_construction_20f ,'dd-MM-yyyy') AS date_of_approval_of_construction_20f,FORMAT(date_of_uploading_of_gazette_notification_20f ,'dd-MM-yyyy') AS date_of_uploading_of_gazette_notification_20f,FORMAT(publication_in_gazette_20f ,'dd-MM-yyyy') AS publication_in_gazette_20f," + 
					"FORMAT(publication_of_notice_in_2_local_news_papers_20f ,'dd-MM-yyyy') AS publication_of_notice_in_2_local_news_papers_20f " + 
					"from la_private_ira ira " + 
					"left join la_land_identification li on ira.la_id_fk = li.la_id  " + 
					"where la_id_fk = ? ";
			
			objList = jdbcTemplate.query( qry, new Object[] {la_id}, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objList;
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionList(LandAcquisition obj) throws Exception {
		List<LandAcquisition> objsList = null;
		try {
			String qry ="select distinct la_id,li.remarks,cast(li.area_to_be_acquired as CHAR) as area_to_be_acquired,ISNULL(li.category_fk,c.la_category) as type_of_land,li.la_land_status_fk, li.project_id_fk,li.project_id_fk,p.project_name,sc.la_sub_category as sub_category_of_land, li.survey_number, li.village_id, li.village, taluka, dy_slr, sdo, li.collector, FORMAT(proposal_submission_date_to_collector,'dd-MM-yyyy') AS proposal_submission_date_to_collector, cast(area_of_plot as CHAR) as area_of_plot, jm_fee_amount,jm_fee_amount_units, " + 
					"li.special_feature,latitude,longitude,cast(li.area_acquired as CHAR) as area_acquired,li.private_land_process,cast(chainage_from as CHAR) as chainage_from,cast(chainage_to as CHAR) as chainage_to, FORMAT(jm_fee_letter_received_date,'dd-MM-yyyy') AS jm_fee_letter_received_date,FORMAT(jm_fee_paid_date,'dd-MM-yyyy') AS jm_fee_paid_date,FORMAT(jm_start_date,'dd-MM-yyyy') AS  jm_start_date,FORMAT(jm_completion_date,'dd-MM-yyyy') AS jm_completion_date, FORMAT(jm_sheet_date_to_sdo,'dd-MM-yyyy') AS jm_sheet_date_to_sdo, jm_remarks, jm_approval, li.issues " + 
					" from la_land_identification li " +
					"left join land_executives le on li.project_id_fk = le.project_id_fk  "+
					"left join project p on li.project_id_fk = p.project_id "
					+"left join la_sub_category sc on li.la_sub_category_fk = sc.id "
					+"left join la_category c on sc.la_category_fk = c.la_category "
					+"where la_id is not null  ";
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
				qry = qry + " and c.la_category = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSub_category_of_land())) {
				qry = qry + " and sc.la_sub_category = ? ";
				arrSize++;
			}	
 			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
 				qry = qry + " and le.executive_user_id_fk = ? ";
 				arrSize++;
			}
 			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSearchStr())) {
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
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSearchStr())) {
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
			}			
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<LandAcquisition>(LandAcquisition.class));

		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

}
