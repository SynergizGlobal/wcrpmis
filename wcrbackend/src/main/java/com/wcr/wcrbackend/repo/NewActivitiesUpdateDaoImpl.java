package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.Messages;
import com.wcr.wcrbackend.DTO.StripChart;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DBConnectionHandler;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.common.FileUploads;
@Repository
public class NewActivitiesUpdateDaoImpl implements NewActivitiesUpdateDao{
	public static Logger logger = Logger.getLogger(NewActivitiesUpdateDaoImpl.class);
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;
	

	@Autowired
	MessagesDao messagesDao;
	
	@Autowired
	FormsHistoryDao formsHistoryDao;
	
	@Override
	public List<StripChart> getNewActivitiesUpdateProjectsList(StripChart obj) throws Exception { 
		List<StripChart> objsList = null;
		try {
			String qry = "select p.project_id as project_id_fk ,p.project_id,p.project_name "
					+ "from project p  "					
					+ "where p.project_id is not null "
					+ "AND p.project_id IN ("
					+"select c.project_id_fk "
					+ "from contract c "
					+ "WHERE c.contract_id IN ("
					+ "select a.contract_id_fk "
					+ "FROM p6_activities a "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "left outer join contract co on a.contract_id_fk = co.contract_id "	
					+ "WHERE a.contract_id_fk is not null  ";
					
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ?  "
						//+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by structure_id_fk,structure) "
						+ "or  a.contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk)  "
						//+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.responsible_people_id_fk = ? group by structure_id_fk,structure) "
						+ ") group by a.contract_id_fk "
						+ ")";
				//arrSize++;
				//arrSize++;
				//arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}else {
				qry = qry + ")";
			}
			qry = qry + " ) GROUP BY p.project_id,p.project_name ORDER BY p.project_id ASC";	
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				//pValues[i++] = obj.getUser_id();
				//pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				//pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			
			}
	
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getNewActivitiesUpdateWorksList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select c.project_id_fk,w.work_id,w.work_name ,w.work_short_name "
					+ "from contract c "
					+ "left outer join work w on c.project_id_fk = w.work_id "
					+ "WHERE c.contract_id IN ("
					+ "select a.contract_id_fk "
					+ "from p6_activities a "
					+ "left outer join contract c on a.contract_id_fk = c.contract_id "					
					+ "where a.contract_id_fk is not null " ;
					
					int arrSize = 0;
					if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
						qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? "
								//+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by structure_id_fk,structure) "
								+ "or  a.contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk)  "
								//+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.responsible_people_id_fk = ? group by structure_id_fk,structure) "
								+ ") group by a.contract_id_fk "
								+ ")";
						//arrSize++;
						//arrSize++;
						//arrSize++;
						arrSize++;
						arrSize++;
						arrSize++;
					}else {
						qry = qry + ")";
					}
					
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and w.project_id_fk = ?";
				arrSize++;
			}
			qry = qry + " GROUP BY c.project_id_fk,w.work_id,w.work_name ,w.work_short_name ORDER BY c.project_id_fk ASC";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				//pValues[i++] = obj.getUser_id();
				//pValues[i++] = obj.getUser_id();
				//pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	
 	

	@Override
	public List<StripChart> getNewActivitiesUpdateContractsList(StripChart obj) throws Exception {
		List<StripChart> objsList = new ArrayList<StripChart>();
		List<StripChart> objsList1 = null;
		try {
			/*String qry = "select distinct a.contract_id_fk as contract_id,c.project_id_fk,c.contract_name,c.contract_short_name "
					+ "from activities a "
					+ "left outer join contract c on a.contract_id_fk = c.contract_id "*/
			
			String qry = "select distinct a.contract_id_fk as contract_id,c.project_id_fk,c.contract_name,c.contract_short_name "
					+ "from p6_activities a "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "left outer join contract c on a.contract_id_fk = c.contract_id "
					+ "left outer join contract_executive c1 on c1.contract_id_fk = c.contract_id "	
					+ "where c.project_id_fk is not null and a.contract_id_fk is not null   " ;
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				qry = qry + " and c1.department_id_fk=? and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? "
						+" or contract_id in (select contract_id from contract where contract_id in(select contract_id_fk from structure_contract_responsible_people where structure_id_fk in(select structure_id_fk from structure_contract_responsible_people where responsible_people_id_fk = ?))) )";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;

			}
			
			
			qry = qry + " group by a.contract_id_fk,c.project_id_fk,c.contract_name,c.contract_short_name ORDER BY a.contract_id_fk ASC ";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				
				pValues[i++] = obj.getDepartment_fk();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();

				//objsList1 = getExecutivesList(obj);	
			
			}

			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
				
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	
	
	@Override
	public List<StripChart> getNewActivitiesUpdateStructures(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT s1.structure as strip_chart_structure_id_fk "
					+ "FROM p6_activities s "
					+ "left join structure s1 on s1.structure_id = s.structure_id_fk "
					+ "LEFT JOIN contract c ON c.contract_id = s.contract_id_fk "
					+ "WHERE s1.structure is not null  and s1.structure_type_fk = ? AND s1.structure <> '' AND s.contract_id_fk = ? ";
					//+ "AND (select count(*) from p6_activities WHERE scope <> ISNULL(completed,0)  and s1.structure_type_fk = ? and contract_id_fk = ? AND structure = s1.structure ) > 0 ";
			int arrSize = 2;
			
		
			/*if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and ( "
						+ "structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by structure_id_fk,structure) "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) group by structure_id_fk,structure) "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.responsible_people_id_fk = ? group by structure_id_fk,structure)) ";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	*/
			
			qry = qry + " group by s1.structure ";			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getStructure_type_fk();
			pValues[i++] = obj.getContract_id_fk();
			//pValues[i++] = obj.getStructure_type_fk();
			//pValues[i++] = obj.getContract_id_fk();
			
			
			/*if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}	*/
		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	
	
	@Override
	public List<StripChart> getNewActivitiesUpdateInProgressStructures(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		List<StripChart> objsList1 = null;
		try {
			String qry = "SELECT s.structure as strip_chart_structure_id_fk "
					+ "FROM p6_activities s left join structure s1 on s1.structure_id = s.structure_id_fk "
					+ "LEFT JOIN contract c ON c.contract_id = s.contract_id_fk "
					+ "WHERE s.structure is not null and (s.component_details != 'OBC' or s.component_details is null)  and f.work_status_fk='In Progress' AND s.structure <> '' AND s.contract_id_fk = ? "
					+ "AND (select count(*) from p6_activities WHERE scope <> ISNULL(completed,0) and (component_details != 'OBC' or component_details is null)  and contract_id_fk = ? AND structure = s.structure ) > 0 ";
			int arrSize = 2;
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and ( "
						+ "structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by structure_id_fk,structure) "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) group by structure_id_fk,structure) "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.responsible_people_id_fk = ? group by structure_id_fk,structure)) ";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			
			qry = qry + " group by s.structure ";			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getContract_id_fk();
			pValues[i++] = obj.getContract_id_fk();
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
			}	
		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}	

	@Override
	public List<StripChart> getNewActivitiesUpdateLines(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select line as strip_chart_line_id_fk from p6_activities a left join structure s on s.structure_id = a.structure_id_fk "
					+ "where line is not null and (component_details != 'OBC' or component_details is null) and line <> ''  ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + "and a.contract_id_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
				qry = qry + "and structure = ? ";
				arrSize++;
			}
			qry = qry + "group by line ";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
				pValues[i++] = obj.getStrip_chart_structure_id_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public List<StripChart> getNewActivitiesUpdateSections(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select section as strip_chart_section_name "
					+ "from p6_activities a "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "where section is not null and (component_details != 'OBC' or component_details is null)  ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + "and a.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
				qry = qry + "and structure = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				qry = qry + "and line = ? ";
				arrSize++;
			}
			
			qry = qry + "group by section ";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
				pValues[i++] = obj.getStrip_chart_structure_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				pValues[i++] = obj.getStrip_chart_line_id_fk();
			}
			
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public StripChart getAcivitiesBulkData(StripChart obj) throws Exception {
		StripChart sObj = null;
		try {
			String qry = "select p6_activity_id AS strip_chart_activity_id,scv.contract_id_fk AS contract_id,scv.structure AS strip_chart_structure_id,scv.component_id AS strip_chart_component_id_name,"
					+ "scv.component AS strip_chart_component,scv.activity_name AS strip_chart_activity_name,"
					+ "scv.line AS strip_chart_line,scv.structure AS structure_type,scv.section AS strip_chart_section_name,completed,scope,remaining,units as unit_fk,scv.status AS status_name,scv.remarks,"
					+ "case  " + 
					" when (ISNULL(completed, 0)=0 or completed is null) then '' " + 
					" when ISNULL(completed, 0)>=ISNULL(scope, 0) then (select FORMAT(min(progress_date),'dd-MM-yyyy') from p6_activity_progress where p6_activity_id_fk=a.p6_activity_id) " + 
					" else (select FORMAT(min(progress_date),'dd-MM-yyyy') from p6_activity_progress where p6_activity_id_fk=a.p6_activity_id) end as actual_start,case  " + 
					" when (ISNULL(completed, 0)=0 or completed is null) then '' " + 
					" when ISNULL(completed, 0)>=ISNULL(scope, 0) then (select FORMAT(max(progress_date),'dd-MM-yyyy') from p6_activity_progress where p6_activity_id_fk=a.p6_activity_id) " + 
					" else '' end as actual_finish,FORMAT(scv.baseline_start,'dd-MM-yyyy') AS planned_start,"
					+ "FORMAT(scv.baseline_finish,'dd-MM-yyyy') AS planned_finish,c.project_id_fk as work_id,c.contract_name,c.contract_short_name,w.project_id_fk as project_id "
					+ "from p6_activities scv "
					+ "left join structure s on s.structure_id = scv.structure_id_fk "
					+ "left outer join contract c on scv.contract_id_fk = c.contract_id "
					+ "where p6_activity_id = ? and (scv.component_details != 'OBC' or scv.component_details is null) and s.structure_type_fk <> 'FOB' ";
			
			sObj =  (StripChart) jdbcTemplate.queryForObject( qry, new Object[] {obj.getActivity_id()}, new BeanPropertyRowMapper<StripChart>(StripChart.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return sObj;
	}


	@Override
	public List<StripChart> getNewActivitiesUpdateComponentsList(StripChart obj) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<StripChart> objsList = new ArrayList<StripChart>();
		StripChart sobj = null;
		try {
			connection = dataSource.getConnection();
			
			String qry = "select component as strip_chart_component "
					+ "from p6_activities a "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "where component is not null and component <> '' and (component_details != 'OBC' or component_details is null)   and a.contract_id_fk = ? and structure_type_fk = ? and structure = ?";
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				qry = qry + " and line = ?";
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				qry = qry + " and section = ?";
			}	
			
			qry = qry + " group by component";
			
			statement = connection.prepareStatement(qry);
			int i = 1;
			statement.setString(i++,obj.getContract_id_fk());
			statement.setString(i++,obj.getStructure_type_fk());
			statement.setString(i++,obj.getStrip_chart_structure_id_fk());
						
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				statement.setString(i++,obj.getStrip_chart_line_id_fk());
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				statement.setString(i++,obj.getStrip_chart_section_name());
			}		
			
			resultSet = statement.executeQuery();  
			while(resultSet.next()) {
				sobj = new StripChart();
				sobj.setStrip_chart_component(resultSet.getString("strip_chart_component"));
				
				obj.setStrip_chart_component_id(sobj.getStrip_chart_component_id());
				sobj.setComponent_id_color(getComponentIdColor(obj,connection));
				objsList.add(sobj);
			}
						
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, statement, resultSet);
		}
		return objsList;
	}
	
	@Override
	public List<StripChart> getNewActivitiesUpdateComponentIds(StripChart obj) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<StripChart> objsList = new ArrayList<StripChart>();
		StripChart sobj = null;
		try {
			connection = dataSource.getConnection();
			/*String qry = "select actual_finish as actual_finish,actual_start as actual_start,planned_start as planned_start,planned_finish as planned_finish,"
					+ "component_id as strip_chart_component_id,component_id as strip_chart_component_id,"
					+ "component as strip_chart_component "
					+ "from activities "
					+ "where component_id is not null and contract_id_fk = ? and structure_id_fk = ?";*/
			
			String qry = "select distinct component_id as strip_chart_component_id_name,"
					+ "component as strip_chart_component "
					+ "from p6_activities a "
					+ "left join structure s on s.structure_id = a.structure_id_fk "
					+ "where component_id is not null and (component_details != 'OBC' or component_details is null) and component_id <> ''  and a.contract_id_fk = ? and structure_type_fk = ?   and structure = ? and component = ?";
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				qry = qry + " and line = ?";
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				qry = qry + " and section = ?";
			}	
			
			//qry = qry + " group by component_id";
			
			statement = connection.prepareStatement(qry);
			int i = 1;
			statement.setString(i++,obj.getContract_id_fk());
			statement.setString(i++,obj.getStructure_type_fk());
			statement.setString(i++,obj.getStrip_chart_structure_id_fk());
			statement.setString(i++,obj.getStrip_chart_component());
						
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				statement.setString(i++,obj.getStrip_chart_line_id_fk());
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				statement.setString(i++,obj.getStrip_chart_section_name());
			}		
			
			resultSet = statement.executeQuery();  
			while(resultSet.next()) {
				sobj = new StripChart();
				/*sobj.setActual_start(resultSet.getString("actual_start"));
				sobj.setActual_finish(resultSet.getString("actual_finish"));
				sobj.setPlanned_start(resultSet.getString("planned_start"));
				sobj.setPlanned_finish(resultSet.getString("planned_finish"));*/
				sobj.setStrip_chart_component_id(resultSet.getString("strip_chart_component_id_name"));
				sobj.setStrip_chart_component(resultSet.getString("strip_chart_component"));
				
				obj.setStrip_chart_component_id(sobj.getStrip_chart_component_id());
				sobj.setComponent_id_color(getComponentIdColor(obj,connection));
				objsList.add(sobj);
			}
						
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, statement, resultSet);
		}
		return objsList;
	}

	private String getComponentIdColor(StripChart sobj, Connection connection) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String color = "";
		try {	
			String qry = " select * from (select (case "  
					+" when ((select count(*) from p6_activities s1 left join structure s11 on s11.structure_id = s1.structure_id_fk where (s1.component_details != 'OBC' or s1.component_details is null) and (s1.scope - ISNULL(s1.completed,0)) <> 0 "
					+ " and s1.contract_id_fk = ? and s11.structure = ? and s1.component_id = ? and s1.component = ? AND p6_activity_id=a.p6_activity_id ";
					if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_line_id_fk())) {
						qry = qry + " and s1.line = ?";
					}			
					if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_section_name())) {
						qry = qry + " and s1.section = ?";
					}	
					qry = qry + ") = 0) then 'over' "  
					//+" when ((select count(*) from activities where (select FORMAT(max(planned_finish),'%Y-%m-%d') from activities s2 where "
					//+ " s2.completed <> 0 and s2.contract_id_fk = ? and s2.structure = ? and s2.component_id = ? ";
				//	if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_line_id_fk())) {
				//		qry = qry + " and s2.line = ?";
				//	}			
				//	if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_section_name())) {
					//	qry = qry + " and s2.section = ?";
					//}	
					//qry = qry + ") < getDate() ) > 0) then 'delayed' "  
					+" when ((select count(*) from p6_activities s3 left join structure s11 on s11.structure_id = s3.structure_id_fk where  (s3.component_details != 'OBC' or s3.component_details is null) and ISNULL(s3.completed,0) = 0 and scope <> 0 "
					+ "and s3.contract_id_fk = ? and s11.structure = ? and s3.component_id = ? and s3.component = ? AND p6_activity_id=a.p6_activity_id ";
					if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_line_id_fk())) {
						qry = qry + " and s3.line = ?";
					}			
					if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_section_name())) {
						qry = qry + " and s3.section = ?";
					}	
					qry = qry + ") > 0) then 'not-started'  "  
					+" else 'in-progress' "  
					+" end ) as color " 
					+" from p6_activities a left join structure s11 on s11.structure_id = a.structure_id_fk where scope <> 0  and (a.component_details != 'OBC' or a.component_details is null) and a.contract_id_fk = ? and structure = ? and component_id = ? and component = ?) as a order by color ";				
			
			stmt = connection.prepareStatement(qry);
			int p = 1;
			//stmt.setString(p++,CommonConstants2.STATUS_COMPLETED );
			stmt.setString(p++,sobj.getContract_id_fk());
			stmt.setString(p++,sobj.getStrip_chart_structure_id_fk());
			stmt.setString(p++,sobj.getStrip_chart_component_id());
			stmt.setString(p++,sobj.getStrip_chart_component());
			
			if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_line_id_fk())) {
				stmt.setString(p++,sobj.getStrip_chart_line_id_fk());
			}			
			if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_section_name())) {
				stmt.setString(p++,sobj.getStrip_chart_section_name());
			}	
			
			//stmt.setString(p++,CommonConstants2.STATUS_NOT_STARTED );
			//stmt.setString(p++,sobj.getContract_id_fk());
			//stmt.setString(p++,sobj.getStrip_chart_structure_id_fk());
			//stmt.setString(p++,sobj.getStrip_chart_component_id());
			//if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_line_id_fk())) {
			//	stmt.setString(p++,sobj.getStrip_chart_line_id_fk());
			//}			
			//if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_section_name())) {
			//	stmt.setString(p++,sobj.getStrip_chart_section_name());
			//}
			
			//stmt.setString(p++,CommonConstants2.STATUS_NOT_STARTED );
			stmt.setString(p++,sobj.getContract_id_fk());
			stmt.setString(p++,sobj.getStrip_chart_structure_id_fk());
			stmt.setString(p++,sobj.getStrip_chart_component_id());
			stmt.setString(p++,sobj.getStrip_chart_component());
			
			stmt.setString(p++,sobj.getContract_id_fk());
			stmt.setString(p++,sobj.getStrip_chart_structure_id_fk());
			stmt.setString(p++,sobj.getStrip_chart_component_id());
			stmt.setString(p++,sobj.getStrip_chart_component());			
			
			
			if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_line_id_fk())) {
				stmt.setString(p++,sobj.getStrip_chart_line_id_fk());
			}			
			if(!StringUtils.isEmpty(sobj) && !StringUtils.isEmpty(sobj.getStrip_chart_section_name())) {
				stmt.setString(p++,sobj.getStrip_chart_section_name());
			}
			
			rs = stmt.executeQuery();
				int Ncount=0;
				int Icount=0;
				int Ocount=0;				
		        if (rs != null) 
		        {
		            while (rs.next()) 
		            {
						if(rs.getString("color").compareTo("not-started")==0)
						{
							Ncount++;
						}
						if(rs.getString("color").compareTo("in-progress")==0)
						{
							Icount++;
						}
						if(rs.getString("color").compareTo("over")==0)
						{
							Ocount++;
						}	
						if(Ncount>0 && Icount>0 && Ocount==0)
						{
							color="in-progress";
						}
						else if(Ncount>0 && Ocount>0 && Icount==0)
						{
							color="in-progress";
						}
						else if(Ocount>0 && Icount>0 && Ncount==0)
						{
							color="in-progress";
						}
						else if(Ncount==0 && Ocount==0 && Icount>0)
						{
							color="in-progress";
						}
						else if(Icount==0 && Ocount==0 && Ncount>0)
						{
							color="not-started";
						}	
						else if(Ocount==0 && Ncount==0 && Icount>0)
						{
							color="in-progress";
						}	
						else if(Icount==0 && Ncount==0 && Ocount>0)
						{
							color="over";
						}
						
						if(color.compareTo("over")==0)
						{
							color="completed";
						}						
		            }
		        }
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return color;
	}

	@Override
	public List<StripChart> getAcivitiesBulkActivitiesList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select p6_activity_id as strip_chart_activity_id,p6_activity_name as strip_chart_activity_name "
					+ "from p6_activities a left join structure s11 on s11.structure_id = a.structure_id_fk"
					
					+ "where p6_activity_id is not null and (component_details != 'OBC' or component_details is null)  and scope <> ISNULL(completed,0)  "
					+ "and component_id = ? and structure_type_fk = ?  and structure = ? and component = ? ";
			int arrSize = 2;			
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				qry = qry + " and line = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				qry = qry + " and section = ?";
				arrSize++;
			}
			
			Object[] pValues = new Object[arrSize];			
			
			int i = 0;
			//pValues[i++] = CommonConstants2.STATUS_COMPLETED + "," + CommonConstants2.STATUS_DROPPED;
			//pValues[i++] = CommonConstants2.STATUS_COMPLETED;
			pValues[i++] = obj.getStrip_chart_component_id();
			pValues[i++] = obj.getStructure_type_fk();
			
			pValues[i++] = obj.getStrip_chart_structure_id_fk();
			pValues[i++] = obj.getStrip_chart_component();
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				pValues[i++] = obj.getStrip_chart_line_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				pValues[i++] = obj.getStrip_chart_section_name();
			}
			
			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public StripChart getNewActivitiesUpdateDetails(StripChart obj) throws Exception {
		StripChart sObj = null;
		try {
			String qry = "select p6_activity_id as activity_id,"
					+ "FORMAT(start,'dd-MM-yyyy') AS actual_start,FORMAT(finish,'dd-MM-yyyy') AS actual_finish,FORMAT(baseline_start,'dd-MM-yyyy') AS planned_start,"
					+ "FORMAT(baseline_finish,'dd-MM-yyyy') AS planned_finish,"
					+ "component_id as strip_chart_component_id_name,completed as completed,scope as scope,remaining as remaining, units as unit_fk "
					+ "from p6_activities a left join structure s11 on s11.structure_id = a.structure_id_fk"
					+ "where p6_activity_id is not null  and (component_details != 'OBC' or component_details is null) and component_id = ? and structure = ? and activity_id = ? ";
			
			int arrSize = 3;
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				qry = qry + " and line = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				qry = qry + " and section = ?";
				arrSize++;
			}
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getStrip_chart_component_id();
			pValues[i++] = obj.getStrip_chart_structure_id_fk();
			pValues[i++] = obj.getStrip_chart_activity_id();		
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				pValues[i++] = obj.getStrip_chart_line_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				pValues[i++] = obj.getStrip_chart_section_name();
			}
			
			sObj =  (StripChart) jdbcTemplate.queryForObject( qry, pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return sObj;
	}

	@Override
	public List<StripChart> getActivitiesfiltersList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select task_code as p6_task_code,(select top 1 isnull(created_date,'') from p6_validation where p6_activity_id_fk=a.p6_activity_id order by progress_id desc) as data_date,isnull((select sum(isnull(completed_scope,0)) as completed_scope from p6_validation where p6_activity_id_fk=a.p6_activity_id and approval_status_fk='Pending'\r\n" + 
					"),0) as validation_pending,p6_activity_id as activity_id,component_id as strip_chart_component_id_name,component as strip_chart_component,p6_activity_id as strip_chart_activity_id,p6_activity_name as strip_chart_activity_name,FORMAT(baseline_start,'dd-MMM-yy') AS planned_start "  
					+",FORMAT(baseline_finish,'dd-MMM-yy') AS planned_finish,FORMAT(start,'dd-MMM-yy') AS start,FORMAT(finish,'dd-MMM-yy') AS finish,ISNULL(scope, 0) as scope,ISNULL(completed, 0) as completed, unit as unit_fk from p6_activities a left join structure s11 on s11.structure_id = a.structure_id_fk " 
					+ " where p6_activity_id is not null and (component_details != 'OBC' or component_details is null)  ";
			
			
				if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()))
				{
					//qry = qry + " and scope <> completed ";
				}			
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_component())) {
				qry = qry + " and component = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_component_id())) {
				qry = qry + " and component_id = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_activity_id())) {
				qry = qry + " and p6_activity_id = ? ";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
				qry = qry + " and structure = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())){
				qry = qry + " and s11.structure_type_fk = ?";
				arrSize++;
			}			
 			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getSearchStr())) {
				qry = qry + " and (task_code like ? or p6_activity_name like ? )";
				arrSize++;
				arrSize++;
			}
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			//pValues[i++] = CommonConstants2.STATUS_COMPLETED;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_component())) {
				pValues[i++] = obj.getStrip_chart_component();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_component_id())) {
				pValues[i++] = obj.getStrip_chart_component_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_activity_id())) {
				pValues[i++] = obj.getStrip_chart_activity_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
				pValues[i++] = obj.getStrip_chart_structure_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())){
				pValues[i++] = obj.getStructure_type_fk();
			}	
			if(!StringUtils.isEmpty(obj.getSearchStr())) {
				pValues[i++] = "%"+obj.getSearchStr()+"%";
				pValues[i++] = "%"+obj.getSearchStr()+"%";
			} 			
			
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	private String getScopeValue(String activity_id) throws Exception
	{
		String Scope="";
		try {
			String qry = "select scope from p6_activities where (component_details != 'OBC' or component_details is null) and p6_activity_id = ?";
			Scope = (String) jdbcTemplate.queryForObject(qry, new Object[] { activity_id }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return Scope;
	}
	private String getUpdatedScopeValue(String activity_id) throws Exception
	{
		String completedscope="";
		try {
			String qry = "select sum(isnull(completed_scope,0)) as completed_scope from p6_activity_progress where p6_activity_id_fk=?";
			completedscope = (String) jdbcTemplate.queryForObject(qry, new Object[] { activity_id }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return completedscope;
	}	
	
	
	private String getPlannedStart(String activity_id) throws Exception
	{
		String PlannedStart="";
		try {
			String qry = "select baseline_start as planned_start from p6_activities where (component_details != 'OBC' or component_details is null)  and p6_activity_id = ?";
			PlannedStart = (String) jdbcTemplate.queryForObject(qry, new Object[] { activity_id }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return PlannedStart;
	}
	
	private String getPlannedFinish(String activity_id) throws Exception
	{
		String PlannedFinish="";
		try {
			String qry = "select baseline_finish as planned_finish from p6_activities where (component_details != 'OBC' or component_details is null) and p6_activity_id = ?";
			PlannedFinish = (String) jdbcTemplate.queryForObject(qry, new Object[] { activity_id }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return PlannedFinish;
	}	
	
	
	@Override
	public boolean insertFOBDailyUpdate(StripChart obj) throws Exception 
	{
		Connection con = null;
		PreparedStatement stmt = null;
		boolean flag = false;
		try {
			con = dataSource.getConnection();
			String insertQry = "INSERT INTO fobdailyupdate (contract_id_fk,structure,reporting_date,remarks,created_date,created_by_user_id_fk)"  
					+ " VALUES (?,?,?,?,CURRENT_TIMESTAMP,?)";
			stmt = con.prepareStatement(insertQry,Statement.RETURN_GENERATED_KEYS);
			int p = 1;
			
	    	String Prdate=null;
			if(!StringUtils.isEmpty(obj.getProgress_date())) 
			{	
		    	Calendar c3 = Calendar.getInstance();
		    	String[] SplitWith3=obj.getProgress_date().split("-");
		    	
	            SimpleDateFormat PrFormat = new SimpleDateFormat("MMMM");
	            c3.setTime(PrFormat.parse(SplitWith3[1]));
	            c3.set(Calendar.DATE, Integer.parseInt(SplitWith3[0]));
	            
				DateFormat dfm3 = new SimpleDateFormat("dd-MM-yy");	
				DateFormat rdfm3 = new SimpleDateFormat("YYYY");
				Date Cdfm3=dfm3.parse(SplitWith3[0]+'-'+c3.get(Calendar.MONTH)+'-'+SplitWith3[2]);	
				
	            String gdate3=rdfm3.format(Cdfm3);
	            
            
	            
	            c3.set(Calendar.YEAR, Integer.parseInt(gdate3));		            
	            
	            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	            
	            Prdate=df.format(c3.getTime());
			}
			
			stmt.setString(p++,obj.getContract_id_fk());
			stmt.setString(p++,obj.getStrip_chart_structure_id_fk());
			stmt.setString(p++,Prdate);
			stmt.setString(p++,obj.getRemarks());
			stmt.setString(p++,obj.getCreated_by_user_id_fk());
			
			int c = stmt.executeUpdate();
			if (c > 0) {
				flag=true;	
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, null);
		}
		return flag;
	}	
	
	
	
	@Override
	public boolean updateAcivitiesBulk(StripChart obj) throws Exception {
		Connection con = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		boolean flag = false;
		try {
			con = dataSource.getConnection();
			String insertQry = "INSERT INTO p6_validation"
					+ "(created_by_user_id_fk, remarks, completed_scope, p6_activity_id_fk,progress_date,approval_status_fk,updated_scope)"
					+ "VALUES"
					+ "(?,?,?,?,?,?,?)";
			insertStmt = con.prepareStatement(insertQry,Statement.RETURN_GENERATED_KEYS);
			int	arraySize = 0;
			if( !StringUtils.isEmpty(obj.getActualScopes()) && obj.getActualScopes().length > 0) {
				obj.setActualScopes(CommonMethods.replaceEmptyByNullInSringArray(obj.getActualScopes()));
				if(arraySize < obj.getActualScopes().length) {
					arraySize = obj.getActualScopes().length;
				}
			}
			  if( !StringUtils.isEmpty(obj.getActivity_ids()) && obj.getActivity_ids().length > 0) 
			  {
				 obj.setActivity_ids(CommonMethods.replaceEmptyByNullInSringArray(obj.getActivity_ids())); if(arraySize < obj.getActivity_ids().length) 
				 { 
					 arraySize= obj.getActivity_ids().length; 
				 } 
			 }
			 
			 // updated this line for NPE issue...
//				String[] SplitScope=obj.getScope().split(",");

			  String[] SplitScope = (obj.getScope() != null) ? obj.getScope().split(",") : new String[0];
			String Message="Scope";
			int loopTimes=0;
			List<String> generatedIds = new ArrayList<String>();

			
			for (int i = 0; i < arraySize; i++) 
			{				
			    int k = 1;
			    //if( obj.getActualScopes().length > 0 && !StringUtils.isEmpty(obj.getActualScopes()[i]))
			    if( obj.getActualScopes().length > 0)
			    {
			    	
			    	String Str1=getScopeValue(obj.getActivity_ids()[i]);
			    	
			    	//String UpdatedScope=getUpdatedScopeValue(obj.getActivity_ids()[i]);
			    	
			    	String Str2[]=obj.getScope().split(",");
			    	DecimalFormat decimalFormat = new DecimalFormat("0.00");
			    	String Str=decimalFormat.format(Double.parseDouble(Str2[i]));

			    	boolean insertFlag=false;		
			    	if((Str1.compareTo(String.valueOf(Str))!=0) || (obj.getActualScopes()[i]!=null && obj.getActualScopes()[i]!="") || (obj.getPlanned_start()!=null) || (obj.getPlanned_finish()!=null))
			    	{
				    	String Prdate=null;
						if(!StringUtils.isEmpty(obj.getProgress_date())) 
						{	
			            
							Prdate=obj.getData_date();
						}
						
						if(Str1.compareTo(String.valueOf(Str))!=0)
						{
							Message="Scope";
						}
			            if(obj.getActualScopes()[i]!=null && obj.getActualScopes()[i]!="")
			            {
			            	Message="Progress";
			            }
			            
			            if(obj.getActualScopes()[i]!=null && obj.getActualScopes()[i]!="" && Str1.compareTo(String.valueOf(Str))!=0)
			            {
			            	Message="Progress";
			            }
			            
					    insertStmt.setString(k++, obj.getCreated_by_user_id_fk());
					    insertStmt.setString(k++, obj.getRemarks());
					    
					    if(!StringUtils.isEmpty(obj.getActualScopes()[i])) 
					    {
					    	insertStmt.setString(k++, obj.getActualScopes().length > 0 ?obj.getActualScopes()[i]:"0");
					    }
					    else
					    {
					    	insertStmt.setString(k++, "0");
					    }
					    
					    insertStmt.setString(k++,(obj.getActivity_ids()[i]));
					    insertStmt.setString(k++, Prdate);
					    insertStmt.setString(k++, "Pending");
					    
					    if(obj.getActualScopes().length > 0 && obj.getActualScopes()[i]!=null && obj.getActualScopes()[i]!="")
					    {
					    	insertFlag=true;
					    }
					    
					    if(Str1.compareTo(String.valueOf(Str))!=0)
					    {
					    	insertFlag=true;
						    insertStmt.setString(k++,(SplitScope[i]));
					    }
					    else
					    {
						    insertStmt.setString(k++,(null));
					    }
					  if(insertFlag==true)
					  {
						  //insertStmt.addBatch();
						  
						  if((obj.getActualScopes()[i]!=null && obj.getActualScopes()[i]!="" && Double.parseDouble(obj.getActualScopes()[i])>0) || (Str1.compareTo(String.valueOf(Str))!=0)) 
						  {
							  insertStmt.executeUpdate();
							  
								ResultSet rs = insertStmt.getGeneratedKeys();
						        if (rs != null) {
						            while (rs.next()) {
						                int generatedId = rs.getInt(1);
						                generatedIds.add(String.valueOf(generatedId));
						            }
						        }						  
							  
							  loopTimes++;
						  }
					  }
			    	}
			    }
			    else 
			    {
			    	
			    	String Str1=getScopeValue(obj.getActivity_ids()[i]);
			    	String Str2[]=obj.getScope().split(",");
			    	Float Str=Float.parseFloat(Str2[i]);

			    	boolean insertFlag=false;
			    	
			    	String Prdate=null;
					if(!StringUtils.isEmpty(obj.getProgress_date())) 
					{	
						Prdate=obj.getData_date();
					}			    	
			    	
			    	if(Str1.compareTo(String.valueOf(Str))!=0)
					{
						Message="Scope";
					}
					
										    insertStmt.setString(k++, obj.getCreated_by_user_id_fk());
										    insertStmt.setString(k++, obj.getRemarks());
										    
										    if( obj.getActualScopes().length==0)
										    {
										    	insertStmt.setString(k++, "0");
										    }
										    else
										    {
										    
											    if(!StringUtils.isEmpty(obj.getActualScopes()[i])) 
											    {
											    	insertStmt.setString(k++, obj.getActualScopes().length > 0 ?obj.getActualScopes()[i]:"0");
											    }
											    else
											    {
											    	insertStmt.setString(k++, "0");
											    }
										    }
									    	
									    	
										    insertStmt.setString(k++,(obj.getActivity_ids()[i]));
										    insertStmt.setString(k++, Prdate);
				    insertStmt.setString(k++, "Pending");
				    					    if(Str1.compareTo(String.valueOf(Str))!=0)
				    					    {
				    					    	insertFlag=true;
				    						    insertStmt.setString(k++,(SplitScope[i]));
				    					    }
				    					    else
				    					    {
				    						    insertStmt.setString(k++,(null));
				    					    }
				  
					  if(insertFlag==true)
					  {
						  //insertStmt.addBatch();
						  insertStmt.executeUpdate();
						  
							ResultSet rs = insertStmt.getGeneratedKeys();
					        if (rs != null) {
					            while (rs.next()) {
					                int generatedId = rs.getInt(1);
					                generatedIds.add(String.valueOf(generatedId));
					            }
					        }						  
						  loopTimes++;
					  }					    			    	
			    	
			    }
			}
			//int[] insertCount = insertStmt.executeBatch();
			NamedParameterJdbcTemplate template1 = new NamedParameterJdbcTemplate(dataSource);			
			if(loopTimes > 0) {
				flag = true;

		        DBConnectionHandler.closeJDBCResoucrs(null, insertStmt, null);
		        
		        String qry = "INSERT INTO p6_validation_dyhod(dyhod_user_id_fk, progress_id_fk)values(?,?)";
		        insertStmt = con.prepareStatement(qry);
		        List<String> dyHodsList = getDyHodsOfActivity(obj.getContract_id_fk());
		        for (String dyhod : dyHodsList) {
					for (String generated_id : generatedIds) {
						insertStmt.setString(1, dyhod);
					    insertStmt.setString(2, generated_id);
					    insertStmt.addBatch();
					}
					String messageQry = "INSERT INTO messages (message,user_id_fk,redirect_url,created_date,message_type)"
							+ "VALUES" + "(:message,:user_id_fk,:redirect_url,CURRENT_TIMESTAMP,:message_type)";	

						Messages msgObj = new Messages();
						msgObj.setUser_id_fk(dyhod);
						msgObj.setMessage("New Activities has been updated");
						msgObj.setRedirect_url("/progress-approval-page/");
						msgObj.setMessage_type("Execution");	
						BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
						template1.update(messageQry, paramSource1);						
											
				}
		        insertStmt.executeBatch();
		        
		        DBConnectionHandler.closeJDBCResoucrs(null, insertStmt, null);
			}
			
			/*if(CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()))
			{*/			
			/*if(flag) {*/
				int arrSize =0;
				if( !StringUtils.isEmpty(obj.getCompletedScopes()) && obj.getCompletedScopes().length > 0) {
					obj.setCompletedScopes(CommonMethods.replaceEmptyByNullInSringArray(obj.getCompletedScopes()));
					if(arrSize < obj.getCompletedScopes().length) {
						arrSize = obj.getCompletedScopes().length;
					}
				}
				if( !StringUtils.isEmpty(obj.getTotalScopes()) && obj.getTotalScopes().length > 0) {
					obj.setTotalScopes(CommonMethods.replaceEmptyByNullInSringArray(obj.getTotalScopes()));
					if(arrSize < obj.getTotalScopes().length) {
						arrSize = obj.getTotalScopes().length;
					}
				}
				if( !StringUtils.isEmpty(obj.getActualScopes()) && obj.getActualScopes().length > 0) {
					obj.setActualScopes(CommonMethods.replaceEmptyByNullInSringArray(obj.getActualScopes()));
					if(arrSize < obj.getActualScopes().length) {
						arrSize = obj.getActualScopes().length;
					}
				}
				if( !StringUtils.isEmpty(obj.getActivity_ids()) && obj.getActivity_ids().length > 0) {
					obj.setActivity_ids(CommonMethods.replaceEmptyByNullInSringArray(obj.getActivity_ids()));
					if(arrSize < obj.getActivity_ids().length) {
						arrSize = obj.getActivity_ids().length;
					}
				}
				for (int i = 0; i < arrSize; i++) 
				{	
					
					
					float scope = 0,completed=0,actual=0;
					if(!StringUtils.isEmpty(obj.getTotalScopes()[i])) {
						 scope = Float.parseFloat(obj.getTotalScopes()[i]);
					}
					if(!StringUtils.isEmpty(obj.getCompletedScopes()[i])) {
						 completed = Float.parseFloat(obj.getCompletedScopes()[i]);
					}	
					if( !StringUtils.isEmpty(obj.getActualScopes()) && obj.getActualScopes().length > 0) {
						 actual = Float.parseFloat(obj.getActualScopes()[i]==null?"0":obj.getActualScopes()[i]);
					}
					else
					{
						actual=0;
					}
					/*Date planstartDate =null;*/
					
					if(obj.getPlanned_start()!=null && obj.getPlanned_finish()!=null)
					{

					String SplitStr=obj.getPlanned_start();
					String[] StrVar=SplitStr.split(",");
					
					String SplitStr1=obj.getPlanned_finish();
					String[] StrVar1=SplitStr1.split(",");	
					
					
					String SplitStr2=obj.getScope();
					String[] StrVar2=SplitStr2.split(",");	
					
					String[] SplitWith=null;
					String[] SplitWith1=null;
					
					if(i >=0 && i < StrVar.length)
					{
							SplitWith=StrVar[i].split("-");
					}
					
					if(i >=0 && i < StrVar1.length)
					{
						
							SplitWith1=StrVar1[i].split("-");
					}

					
		            Calendar c1 = Calendar.getInstance();
		            SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM");
		            
		            if(i >=0 && i < StrVar.length)
					{
		            	if(SplitWith[0].length()>0) 
		            	{
				            c1.setTime(inputFormat.parse(SplitWith[1]));
				            c1.set(Calendar.DATE, Integer.parseInt(SplitWith[0]));
	
							DateFormat dfm1 = new SimpleDateFormat("dd-MM-yy");
							DateFormat rdfm1 = new SimpleDateFormat("YYYY");
							int YearPlus=Integer.parseInt(SplitWith[2])+1;
							Date Cdfm1=dfm1.parse(SplitWith[0]+'-'+c1.get(Calendar.MONTH)+'-'+String.valueOf(YearPlus));	
							
				            String gdate1=rdfm1.format(Cdfm1);
				            
				            c1.set(Calendar.YEAR, Integer.parseInt(gdate1));
		            	}
		            
					}
		            Calendar c2 = Calendar.getInstance();
		            
		            if(i >=0 && i < StrVar1.length)
					{		            
		            	if(SplitWith1[0].length()>0) 
		            	{
				            c2.setTime(inputFormat.parse(SplitWith1[1]));
				            c2.set(Calendar.DATE, Integer.parseInt(SplitWith1[0]));
				            
							DateFormat dfm2 = new SimpleDateFormat("dd-MM-yy");
							DateFormat rdfm2 = new SimpleDateFormat("YYYY");
							Date Cdfm2=dfm2.parse(SplitWith1[0]+'-'+c2.get(Calendar.MONTH)+'-'+SplitWith1[2]);	
							
				            String gdate2=rdfm2.format(Cdfm2);
				            
				            c2.set(Calendar.YEAR, Integer.parseInt(gdate2));
		            	}
					}
		            
		            
		            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		            
		            String date1=sdf.format(c1.getTime());
		            String date2=sdf.format(c2.getTime());
		            
					
		            String Str1=getScopeValue(obj.getActivity_ids()[i]);
		            String Str2[]=obj.getScope().split(",");
		            Float Str=Float.parseFloat(Str2[i]);		            
		            
					if(Float.parseFloat(StrVar2[i])>=completed && date2.compareTo(date1)>=0 && ((Str1.compareTo(String.valueOf(Str))!=0) || ( !StringUtils.isEmpty(obj.getActualScopes()) && obj.getActualScopes().length > 0) ) )
					{
						//String updateQry = "UPDATE  activities set completed = ISNULL(NULLIF(completed, '' ), 0) + ?";	
						
						String updateQry = "UPDATE  p6_activities set modified_by_user_id_fk=?,modified_date=?,completed = ISNULL(completed, 0) ";
						
						if(StrVar.length>0)
						{
							if(!StringUtils.isEmpty(StrVar[i])) 
							{
								updateQry = updateQry + ", baseline_start = ? ";	
							}
						}

						if(StrVar1.length>0)
						{
							if(!StringUtils.isEmpty(StrVar1[i])) 
							{
								updateQry = updateQry + ", baseline_finish = ? ";	
							}
						}
						
						/*if(!StringUtils.isEmpty(scope)) 
						{
							updateQry = updateQry + ", scope = ? ";	
						}*/							
						
						updateQry = updateQry + " WHERE p6_activity_id = ? ";
						updateStmt = con.prepareStatement(updateQry);
							
						int k = 1;
						//updateStmt.setString(k++, String.valueOf(actual) );
						updateStmt.setString(k++, obj.getCreated_by_user_id_fk() );	
						if(!StringUtils.isEmpty(obj.getProgress_date())) 
						{
					    	Calendar c4 = Calendar.getInstance();
					    	String[] SplitWith4=obj.getProgress_date().split("-");
							
				            SimpleDateFormat PrFormat = new SimpleDateFormat("MMMM");
				            c4.setTime(PrFormat.parse(SplitWith4[1]));
				            c4.set(Calendar.DATE, Integer.parseInt(SplitWith4[0]));
				            
							DateFormat dfm = new SimpleDateFormat("dd-MM-yy");
							DateFormat rdfm = new SimpleDateFormat("YYYY");
							Date Cdfm=dfm.parse(SplitWith4[0]+'-'+c4.get(Calendar.MONTH)+'-'+SplitWith4[2]);	
							
				            String gdate=rdfm.format(Cdfm);
				            
				            c4.set(Calendar.YEAR, Integer.parseInt(gdate));		            
				            
				            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				            
				            String Prdate=df.format(c4.getTime());
				            
							updateStmt.setString(k++, Prdate );
						}
						else
						{
							updateStmt.setString(k++, null );
						}						
						

						if(StrVar.length>0)
						{
							if(!StringUtils.isEmpty(StrVar[i])) 
							{
								updateStmt.setString(k++, DateParser.parse(date1) );	
							}
						}

						if(StrVar1.length>0)
						{
							if(!StringUtils.isEmpty(StrVar1[i])) 
							{
								updateStmt.setString(k++, DateParser.parse(date2) );	
							}	
						}
						
						
						
						/*if(!StringUtils.isEmpty(scope)) 
						{
							updateStmt.setString(k++, StrVar2[i] );
						}	*/					
						
						updateStmt.setString(k++,(obj.getActivity_ids()[i]));
						updateStmt.executeUpdate();
						flag=true;
						DBConnectionHandler.closeJDBCResoucrs(null, updateStmt, null);
					}
				}
					else
					{
						
						String SplitStr2=obj.getScope();
						String[] StrVar2=SplitStr2.split(",");	

			            String Str1=getScopeValue(obj.getActivity_ids()[i]);
			            String Str2[]=obj.getScope().split(",");
			            Float Str=Float.parseFloat(Str2[i]);
					
						if(Float.parseFloat(StrVar2[i])>=completed && ((Str1.compareTo(String.valueOf(Str))!=0) || (obj.getActualScopes()[i]!=null && obj.getActualScopes()[i]!="") ) )
						{
						
							String updateQry = "UPDATE  p6_activities set modified_by_user_id_fk=?,modified_date=?,completed = ISNULL(completed, 0) ";
				
							
							updateQry = updateQry + " WHERE p6_activity_id = ? ";
							updateStmt = con.prepareStatement(updateQry);
								
							int k = 1;
							updateStmt.setString(k++, obj.getCreated_by_user_id_fk() );	
							if(!StringUtils.isEmpty(obj.getProgress_date())) 
							{
								
						    	Calendar c4 = Calendar.getInstance();
						    	String[] SplitWith4=obj.getProgress_date().split("-");
								
					            SimpleDateFormat PrFormat = new SimpleDateFormat("MMMM");
					            c4.setTime(PrFormat.parse(SplitWith4[1]));
					            c4.set(Calendar.DATE, Integer.parseInt(SplitWith4[0]));
					            
								DateFormat dfm = new SimpleDateFormat("dd-MM-yy");
								DateFormat rdfm = new SimpleDateFormat("YYYY");
								Date Cdfm=dfm.parse(SplitWith4[0]+'-'+c4.get(Calendar.MONTH)+'-'+SplitWith4[2]);	
								
					            String gdate=rdfm.format(Cdfm);
					            
					            c4.set(Calendar.YEAR, Integer.parseInt(gdate));		            
					            
					            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					            
					            String Prdate=df.format(c4.getTime());
					            
								updateStmt.setString(k++, Prdate );
							}
							else
							{
								updateStmt.setString(k++, null );
							}
							updateStmt.setString(k++,(obj.getActivity_ids()[i]));
							updateStmt.executeUpdate();
							flag=true;
							DBConnectionHandler.closeJDBCResoucrs(null, updateStmt, null);
						}
					}
				}
			//}
				/*}*/
				
				/********************************************************************************/
				if(!StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
					String qryUsers ="SELECT dy_hod_user_id_fk "
							+ "FROM structure_contract_responsible_people rs "
							+ "left join contract c on rs.contract_id_fk = c.contract_id "
							+ "left join structure s on s.structure_id=rs.structure_id_fk "
							+ "where dy_hod_user_id_fk is not null and structure = ? ";
					List<String> users = jdbcTemplate.queryForList( qryUsers,new Object[]{obj.getStrip_chart_structure_id_fk()}, String.class);	
					if(!StringUtils.isEmpty(users) && users.size() > 0) {
						NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
						String userIds[]  = new String[users.size()];	
						userIds = users.toArray(userIds);
						String messageType = "Activity Progress";
						String redirect_url = "/progress-approval-page";
						
						String qryFOBName ="SELECT distinct structure_name FROM structure where structure = ? and structure_type_fk= ? ";
						String fob_name = jdbcTemplate.queryForObject( qryFOBName,new Object[]{obj.getStrip_chart_structure_id_fk(),obj.getStructure_type_fk()}, String.class);	
						if(StringUtils.isEmpty(fob_name)) {
							fob_name = obj.getStrip_chart_structure_id_fk();
						}
						String message = Message+" Update for Structure "+fob_name+". Pending approval.";
						 
						Messages msgObj = new Messages();
						msgObj.setUser_ids(userIds);
						msgObj.setMessage_type(messageType);
						msgObj.setRedirect_url(redirect_url);
						msgObj.setMessage(message);
						messagesDao.addMessages(msgObj,template);
					}
				}
				
				FormHistory formHistory = new FormHistory();
				formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
				formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
				formHistory.setModule_name_fk("Execution &  Monitoring");
				formHistory.setForm_name("New Update Activities");
				formHistory.setForm_action_type("Update");
				formHistory.setForm_details(loopTimes + " activities updated for "+obj.getStrip_chart_structure_id_fk());
				formHistory.setProject_id_fk(obj.getProject_id_fk());
				formHistory.setContract(obj.getContract_id_fk());
				
				boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
				if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getRemarks())) 
				{
					boolean FOBDailyUpdate_flag = insertFOBDailyUpdate(obj);
					
				}
				flag=true;
				
				String document_insert_qry = "INSERT into  structure_documents ( structure_id_fk, attachment,structure_file_type_fk,name,created_date) VALUES (:structure_id,:attachment,:structure_file_type_fk,:name,:target_date)";
				int docArrSize = 0;
				
				if (!StringUtils.isEmpty(obj.getStructureFileNames()) && obj.getStructureFileNames().length > 0) {
					if (docArrSize < obj.getStructureFileNames().length) {
						docArrSize = obj.getStructureFileNames().length;
					}
				}
				NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);	
				BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
				for (int i = 0; i < docArrSize; i++) {
					if (!StringUtils.isEmpty(obj.getStructureFileNames()) && obj.getStructureFileNames().length > 0) {
						MultipartFile multipartFile = obj.getStructureFileNames()[i];
						if ((null != multipartFile && !multipartFile.isEmpty()))  {
							String saveDirectory =  CommonConstants2.STRUCTURE_FILE_SAVING_PATH ;
							String fileName = obj.getStructure_type_fk()+"_"+obj.getStrip_chart_structure_id_fk()+"_"+obj.getStrip_chart_component();
							
							String Ext=multipartFile.getContentType();
							Ext=Ext.replaceAll("image/", "");
						
							String CheckStrString="."+Ext;
							String verifyStr=multipartFile.getOriginalFilename();
							String mainStr=verifyStr.replaceAll(CheckStrString, "");
							
							String ConcatStr=mainStr+"_"+LocalDate.now()+""+CheckStrString;
							;
							if (null != multipartFile && !multipartFile.isEmpty()) {
								FileUploads.singleFileSaving(multipartFile, saveDirectory, ConcatStr);
							}
							Structure fileObj = new Structure();
							fileObj.setAttachment(ConcatStr);
							
							if(!StringUtils.isEmpty(obj.getProgress_date())) 
							{	
								
						    	Calendar c4 = Calendar.getInstance();
						    	String[] SplitWith4=obj.getProgress_date().split("-");
								
					            SimpleDateFormat PrFormat = new SimpleDateFormat("MMMM");
					            c4.setTime(PrFormat.parse(SplitWith4[1]));
					            c4.set(Calendar.DATE, Integer.parseInt(SplitWith4[0]));
					            
								DateFormat dfm = new SimpleDateFormat("dd-MM-yy");
								DateFormat rdfm = new SimpleDateFormat("YYYY");
								Date Cdfm=dfm.parse(SplitWith4[0]+'-'+c4.get(Calendar.MONTH)+'-'+SplitWith4[2]);	
								
					            String gdate=rdfm.format(Cdfm);
					            
					            c4.set(Calendar.YEAR, Integer.parseInt(gdate));		            
					            
					            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					            
					            String Prdate=df.format(c4.getTime());	
					            
								fileObj.setTarget_date(Prdate);
							}
							
							
							
							fileObj.setStructure_file_type_fk("Site photograph");
							
							String qryStructure ="select top 1 structure_id from structure where structure=? and structure_type_fk=?";
							String StructureText= jdbcTemplate.queryForObject( qryStructure,new Object[]{obj.getStrip_chart_structure_id_fk(),obj.getStructure_type_fk()}, String.class);
							
							
							fileObj.setStructure_id(StructureText);
							fileObj.setName(fileName);
							
							paramSource = new BeanPropertySqlParameterSource(fileObj);
							namedParamJdbcTemplate.update(document_insert_qry, paramSource);
						}
					}
				}
			
				/********************************************************************************/	
			//}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, updateStmt, null);
		}	
		return flag;
	}
	
	private List<String> getDyHodsOfActivity(String contract_id) throws Exception {
		List<String>  dy_hods = null;
		try {
			String qryUsers ="";

				qryUsers ="select distinct USER_ID as dy_hod_user_id_fk from [user] where user_role_name_fk='Data Admin' and USER_ID IN(select distinct responsible_people_id_fk from structure_contract_responsible_people where contract_id_fk=?) union " + 
						"select distinct  dy_hod_user_id_fk from contract where contract_id=?";
				dy_hods = jdbcTemplate.queryForList( qryUsers,new Object[]{contract_id,contract_id}, String.class);
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return dy_hods;
	}

	@Override
	public List<StripChart> getStructureTypesInActivitiesUpdate(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT distinct structure_type_fk as structure_type "
					+ "FROM p6_activities s left join structure s11 on s11.structure_id = s.structure_id_fk "
					+ "LEFT JOIN contract c ON c.contract_id = s.contract_id_fk "
					+ "WHERE s11.structure_type_fk is not null AND s11.structure_type_fk <> '' AND s.contract_id_fk = ? ";
			int arrSize = 1;
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getContract_id_fk();
		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	

	@Override
	public List<StripChart> getStructureTypesInDeleteActivities(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT distinct s11.structure_type_fk as structure_type "
					+ "FROM p6_activities s left join structure s11 on s11.structure_id = s.structure_id_fk "
					+ "LEFT JOIN contract c ON c.contract_id = s.contract_id_fk "
					+ "WHERE s11.structure is not null AND s11.structure <> '' AND s.contract_id_fk = ? ";
			int arrSize = 1;
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getContract_id_fk();
		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	

	@Override
	public StripChart getNewAcivitiesUpdateData(StripChart obj) throws Exception {
		StripChart sObj = null;
		try {
			String qry = "select a.p6_activity_id as activity_id,a.contract_id_fk AS contract_id,s11.structure AS strip_chart_structure_id,a.component_id AS strip_chart_component_id,"
					+ "a.component AS strip_chart_component,a.p6_activity_id AS strip_chart_activity_id,a.p6_activity_name AS strip_chart_activity_name,"
					+ "a.line AS strip_chart_line,s11.structure_type_fk AS structure_type,a.section AS strip_chart_section_id,"
					+ "c.project_id_fk as work_id,c.contract_name,c.contract_short_name,w.project_id_fk as project_id "
					+ "from p6_activities a left join structure s11 on s11.structure_id = a.structure_id_fk "
					+ "left outer join contract c on a.contract_id_fk = c.contract_id "
					+ "where (a.component_details != 'OBC' or a.component_details is null)  and p6_activity_id = ? ";

			sObj = (StripChart) jdbcTemplate.queryForObject(qry, new Object[] { obj.getActivity_id() },
					new BeanPropertyRowMapper<StripChart>(StripChart.class));

		} catch (Exception e) {
			throw new Exception(e);
		}
		return sObj;
	}
	
	@Override
	public List<StripChart> getProjectsList(StripChart obj) throws Exception { 
		List<StripChart> objsList = null;
		try {
			String qry = "select wr.project_id_fk ,p.project_id,p.project_name "
					+ "from project p  "					
					+ "where p.project_id is not null "
					+ "AND p.project_id IN ("
					+"select c.project_id_fk "
					+ "from contract c "
					+ "left outer join project w on c.project_id_fk = w.project_id "
					+ "WHERE c.contract_id IN ("
					+ "select a.contract_id_fk "
					+ "FROM p6_activities a "
					+ "left outer join contract co on a.contract_id_fk = co.contract_id "	
					+ "WHERE a.contract_id_fk is not null  and a.scope <> ISNULL(Completed,0) ";
					
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by structure_id_fk) "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) group by structure_id_fk) "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.responsible_people_id_fk = ? group by structure_id_fk) "
						+ ") group by a.contract_id_fk ORDER BY a.contract_id_fk ASC "
						+ ")";
				
				qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? "
						+ "or structure in (select fob_id_fk from fob_contract_responsible_people where contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by fob_id_fk) "
						+ "or structure in (select fob_id_fk from fob_contract_responsible_people where contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) group by fob_id_fk) "
						+ "or structure in (select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ? group by fob_id_fk) "
						+ ") group by a.contract_id_fk "
						+ ")";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;				
				
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}else {
				qry = qry + ")";
			}
			qry = qry + "GROUP BY c.project_id_fk) GROUP BY p.project_id,p.project_name ";	
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();				
			
			}
	
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getWorksList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select c.project_id_fk,w.work_id,w.work_name ,w.work_short_name "
					+ "from contract c "
					+ "left outer join work w on c.project_id_fk = w.work_id "
					+ "WHERE c.contract_id IN ("
					+ "select a.contract_id_fk "
					+ "from p6_activities a "
					+ "left outer join contract c on a.contract_id_fk = c.contract_id "					
					+ "where a.contract_id_fk is not null  and a.scope <> ISNULL(Completed,0) " ;
					
					int arrSize = 0;
					if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
						qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? "
								+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by structure_id_fk) "
								+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) group by structure_id_fk) "
								+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.responsible_people_id_fk = ? group by structure_id_fk) "
								+ ") group by a.contract_id_fk ORDER BY a.contract_id_fk ASC "
								+ ")";
						
						qry = qry + " and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? "
								+ "or structure in (select fob_id_fk from fob_contract_responsible_people where contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by fob_id_fk) "
								+ "or structure in (select fob_id_fk from fob_contract_responsible_people where contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) group by fob_id_fk) "
								+ "or structure in (select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ? group by fob_id_fk) "
								+ ") group by a.contract_id_fk ORDER BY a.contract_id_fk ASC "
								+ ")";
						arrSize++;
						arrSize++;
						arrSize++;
						arrSize++;
						arrSize++;
						arrSize++;						
						
						arrSize++;
						arrSize++;
						arrSize++;
						arrSize++;
						arrSize++;
						arrSize++;						
						
					}else {
						qry = qry + ")";
					}
					
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and w.project_id_fk = ?";
				arrSize++;
			}
			qry = qry + " GROUP BY c.project_id_fk,w.work_id,w.work_name ,w.work_short_name ORDER BY c.project_id_fk ASC";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();				
			
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	
 	

	@Override
	public List<StripChart> getContractsList(StripChart obj) throws Exception {
		List<StripChart> objsList = new ArrayList<StripChart>();
		List<StripChart> objsList1 = null;
		try {
			/*String qry = "select distinct a.contract_id_fk as contract_id,c.project_id_fk,c.contract_name,c.contract_short_name "
					+ "from activities a "
					+ "left outer join contract c on a.contract_id_fk = c.contract_id "*/
			
			String qry = "select distinct a.contract_id_fk as contract_id,c.project_id_fk,c.contract_name,c.contract_short_name "
					+ "from p6_activities a "
					+ "left outer join contract c on a.contract_id_fk = c.contract_id "
					+ "left outer join contract_executive c1 on c1.contract_id_fk = c.contract_id "	
					+ "where c.project_id_fk is not null and contract_id not like '%ms%' and a.contract_id_fk is not null and a.scope <> ISNULL(Completed,0) " ;
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				qry = qry + " and c.project_id_fk = ?";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				qry = qry + " and c1.department_id_fk=? and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? "
						+" or contract_id in (select contract_id from contract where contract_id in(select contract_id_fk from structure_contract_responsible_people where structure_id_fk in(select structure_id_fk from structure_contract_responsible_people where responsible_people_id_fk = ?))) )";
			
				qry = qry + " and c1.department_id_fk=? and (hod_user_id_fk = ? or dy_hod_user_id_fk = ? "
						+" or contract_id in (select contract_id from contract where contract_id in(select contract_id_fk from fob_contract_responsible_people where fob_id_fk in(select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ?))) )";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;	
				
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;				
				

			}
			
			
			qry = qry + " group by a.contract_id_fk,c.project_id_fk,c.contract_name,c.contract_short_name ORDER BY a.contract_id_fk ASC ";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())) {
				pValues[i++] = obj.getProject_id_fk();
			}
			
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				
				pValues[i++] = obj.getDepartment_fk();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				
				pValues[i++] = obj.getDepartment_fk();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();				

				//objsList1 = getExecutivesList(obj);	
			
			}

			objsList = jdbcTemplate.query( qry, pValues, new BeanPropertyRowMapper<StripChart>(StripChart.class));
				
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}	
	
	
	
	@Override
	public List<StripChart> getDeleteActivitiesfiltersList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select p6_activity_id as activity_id,weightage,component_details,task_code as p6_task_code,component_id as strip_chart_component_id_name,component as strip_chart_component,p6_activity_id as strip_chart_activity_id,p6_activity_name as strip_chart_activity_name,FORMAT(baseline_start,'dd-MMM-yy') AS planned_start "  
					+",FORMAT(baseline_finish,'dd-MMM-yy') AS planned_finish,FORMAT(start,'dd-MMM-yy') AS start,FORMAT(finish,'dd-MMM-yy') AS finish,ISNULL(scope, 0) as scope,\r\n" + 
					"ISNULL(completed, 0) as completed, unit as unit_fk from p6_activities  a left join structure s11 on s11.structure_id = a.structure_id_fk" 
					+ " where p6_activity_id is not null  ";

			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_component())) {
				qry = qry + " and component = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_component_id())) {
				qry = qry + " and component_id = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_activity_id())) {
				qry = qry + " and p6_activity_id = ? ";
				arrSize++;
			}
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
				qry = qry + " and structure = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and a.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())){
				qry = qry + " and s11.structure_type_fk = ?";
				arrSize++;
			}			
			//qry = qry + " group by p6_activity_id ";
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_component())) {
				pValues[i++] = obj.getStrip_chart_component();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_component_id())) {
				pValues[i++] = obj.getStrip_chart_component_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_activity_id())) {
				pValues[i++] = obj.getStrip_chart_activity_id();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_structure_id_fk())) {
				pValues[i++] = obj.getStrip_chart_structure_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())){
				pValues[i++] = obj.getStructure_type_fk();
			}			
			
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getDeleteActivitiesStructures(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT s11.structure as strip_chart_structure_id_fk "
					+ "FROM p6_activities s left join structure s11 on s11.structure_id = s.structure_id_fk "
					+ "LEFT JOIN contract c ON c.contract_id = s.contract_id_fk "
					+ "WHERE s11.structure is not null and (s.component_details != 'OBC' or s.component_details is null) and s11.structure_type_fk = ? AND s11.structure <> '' AND s.contract_id_fk = ? "
					+ "AND (select count(*) from p6_activities a left join structure s12 on s12.structure_id = a.structure_id_fk WHERE scope <> ISNULL(completed,0) and  s11.structure_type_fk = ? and s.contract_id_fk = ? AND structure = s11.structure ) > 0 ";
			int arrSize = 4;
			
		
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				qry = qry + " and ( "
						+ "structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by structure_id_fk,structure) "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) group by structure_id_fk,structure) "
						+ "or structure in (select structure from structure_contract_responsible_people s inner join structure s1 on s1.structure_id=s.structure_id_fk where s.responsible_people_id_fk = ? group by structure_id_fk,structure)) ";

				qry = qry + " and ( "
						+ "structure in (select fob_id_fk from fob_contract_responsible_people where contract_id_fk in(select contract_id from contract where (hod_user_id_fk = ? or dy_hod_user_id_fk = ?) group by contract_id) group by fob_id_fk) "
						+ "or structure in (select fob_id_fk from fob_contract_responsible_people where contract_id_fk in(select contract_id_fk from contract_executive where executive_user_id_fk = ? group by contract_id_fk) group by fob_id_fk) "
						+ "or structure in (select fob_id_fk from fob_contract_responsible_people where responsible_people_id_fk = ? group by fob_id_fk)) ";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			
			qry = qry + " group by s11.structure ";			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getStructure_type_fk();
			pValues[i++] = obj.getContract_id_fk();
			pValues[i++] = obj.getStructure_type_fk();
			pValues[i++] = obj.getContract_id_fk();
			
			
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code())) {
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();
				pValues[i++] = obj.getUser_id();				
				
			}	
		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getDeleteActivitiesComponentsList(StripChart obj) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<StripChart> objsList = new ArrayList<StripChart>();
		StripChart sobj = null;
		try {
			connection = dataSource.getConnection();
			
			String qry = "select component as strip_chart_component "
					+ "from p6_activities a1 left join structure s11 on s11.structure_id = a1.structure_id_fk "
					+ "where component is not null and component <> '' and a1.contract_id_fk = ? and  structure_type_fk = ? and structure = ?";
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				qry = qry + " and line = ?";
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				qry = qry + " and section = ?";
			}	
			
			qry = qry + " group by component";
			
			statement = connection.prepareStatement(qry);
			int i = 1;
			statement.setString(i++,obj.getContract_id_fk());
			statement.setString(i++,obj.getStructure_type_fk());
			statement.setString(i++,obj.getStrip_chart_structure_id_fk());
						
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				statement.setString(i++,obj.getStrip_chart_line_id_fk());
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				statement.setString(i++,obj.getStrip_chart_section_name());
			}		
			
			resultSet = statement.executeQuery();  
			while(resultSet.next()) {
				sobj = new StripChart();
				sobj.setStrip_chart_component(resultSet.getString("strip_chart_component"));
				
				obj.setStrip_chart_component_id(sobj.getStrip_chart_component_id());
				sobj.setComponent_id_color(getComponentIdColor(obj,connection));
				objsList.add(sobj);
			}
						
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, statement, resultSet);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getDeleteActivitiesComponentIds(StripChart obj) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<StripChart> objsList = new ArrayList<StripChart>();
		StripChart sobj = null;
		try {
			connection = dataSource.getConnection();
			/*String qry = "select actual_finish as actual_finish,actual_start as actual_start,planned_start as planned_start,planned_finish as planned_finish,"
					+ "component_id as strip_chart_component_id,component_id as strip_chart_component_id,"
					+ "component as strip_chart_component "
					+ "from activities "
					+ "where component_id is not null and contract_id_fk = ? and structure_id_fk = ?";*/
			
			String qry = "select distinct component_id as strip_chart_component_id_name,"
					+ "component as strip_chart_component "
					+ "from p6_activities a1 left join structure s11 on s11.structure_id = a1.structure_id_fk "
					+ "where component_id is not null  and component_id <> '' and a1.contract_id_fk = ? and  structure_type_fk = ? and structure = ? and component = ?";
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				qry = qry + " and line = ?";
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				qry = qry + " and section = ?";
			}	
			
			//qry = qry + " group by component_id";
			
			statement = connection.prepareStatement(qry);
			int i = 1;
			statement.setString(i++,obj.getContract_id_fk());
			statement.setString(i++,obj.getStructure_type_fk());
			statement.setString(i++,obj.getStrip_chart_structure_id_fk());
			statement.setString(i++,obj.getStrip_chart_component());
						
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_line_id_fk())) {
				statement.setString(i++,obj.getStrip_chart_line_id_fk());
			}			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStrip_chart_section_name())) {
				statement.setString(i++,obj.getStrip_chart_section_name());
			}		
			
			resultSet = statement.executeQuery();  
			while(resultSet.next()) {
				sobj = new StripChart();
				sobj.setStrip_chart_component_id(resultSet.getString("strip_chart_component_id_name"));
				sobj.setStrip_chart_component(resultSet.getString("strip_chart_component"));
				
				obj.setStrip_chart_component_id(sobj.getStrip_chart_component_id());
				sobj.setComponent_id_color(getComponentIdColor(obj,connection));
				objsList.add(sobj);
			}
						
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, statement, resultSet);
		}
		return objsList;
	}

	@Override
	public boolean deleteAcivitiesBulk(StripChart obj) throws Exception {
		boolean flag=false;
		Connection connection = null;
		ResultSet resultSet = null;
		java.sql.CallableStatement cstmt=null;
		
		try{
			
			String concat="";
			for (int i = 0; i < obj.getActivity_ids().length; i++)
			{
				if(obj.getIds()[i].compareTo("1")==0)
				{
					concat=concat+obj.getActivity_ids()[i]+",";
				}
			}
			concat=concat.substring(0, concat.length() - 1);  
			connection = dataSource.getConnection();				
			
			cstmt = connection.prepareCall("{call dbo.deleteActivities(?, ?)}"); 
	        cstmt.setString(1, concat);
	        cstmt.registerOutParameter(2, java.sql.Types.BOOLEAN);  
	        cstmt.execute();  
	        System.out.println("return flag " + cstmt.getBoolean(2));
	        flag=cstmt.getBoolean(2);
		}catch(Exception e){ 
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, cstmt, resultSet);
		}
		return flag;				
	}
	
	private String getContractIdByContractShortName(String ContractShortName)throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String val = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			String qry = "select contract_id from contract where contract_short_name = ? ";
			stmt = connection.prepareStatement(qry);
			stmt.setString(1, ContractShortName);
			
			rs = stmt.executeQuery();  
			if(rs.next()) {
				val = rs.getString("contract_id");
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, stmt, rs);
		}
		return val;
	}	

	@Override
	public boolean uploadNewActivities(List<StripChart> stripChartList) throws Exception {
		Connection con = null;
		PreparedStatement insertStmt = null;
		PreparedStatement insertStmt1=null;
		ResultSet rs = null;
		int count = 0;
		boolean flag =false;
		try{
			con = dataSource.getConnection();
			
	
			
			String insertQry = "INSERT INTO p6_validation"
					+ "(created_by_user_id_fk, remarks, completed_scope, p6_activity_id_fk,progress_date,approval_status_fk)"
					+ "VALUES"
					+ "(?,?,?,?,?,?)";
			insertStmt = con.prepareStatement(insertQry,Statement.RETURN_GENERATED_KEYS);
			PreparedStatement stmt = null;
			PreparedStatement stmt1 = null;
			int loopTimes=0;
			
			for (StripChart obj : stripChartList) 
			{
				String[] StrVar=obj.getProgress_date().split("###");
				String[] StrVar1=obj.getCompleted().split("###");
				
				for(int k=0;k<StrVar.length;k++)
				{
					List<String> generatedIds = new ArrayList<String>();
					obj.setContract_id_fk(getContractIdByContractShortName(obj.getContract_short_name()));
					if(!StringUtils.isEmpty(StrVar1[k]) && StrVar1[k].compareTo("NoValue")!=0)
					{
						
						if(Float.parseFloat(getPreviousCompletedScopeApproved(getActivityId(obj.getP6_task_code(),getContractIdByContractShortName(obj.getContract_short_name())),StrVar[k]))!=Float.parseFloat(StrVar1[k]) && Float.parseFloat(StrVar1[k])>0 && getPreviousCompletedScope(getActivityId(obj.getP6_task_code(),getContractIdByContractShortName(obj.getContract_short_name())),StrVar[k])==0)
						
						/*if((getPreviousCompletedScope(getActivityId(obj.getP6_task_code()),StrVar[k]).compareTo(StrVar1[k])!=0 && Float.parseFloat(StrVar1[k])>0 && getPreviousCompletedScopeApproved(getActivityId(obj.getP6_task_code()),StrVar[k])!=StrVar1[k]))*/
						{
								
								String deleteQry = "delete from p6_validation_dyhod where progress_id_fk in(select progress_id from p6_validation where p6_activity_id_fk = ? and progress_date=? and approval_status_fk='pending')";
								stmt = con.prepareStatement(deleteQry);
								stmt.setString(1,getActivityId(obj.getP6_task_code(),getContractIdByContractShortName(obj.getContract_short_name())));
								stmt.setString(2,StrVar[k]);
								stmt.executeUpdate();
								
								
								String deleteQry1 = "delete from p6_validation where p6_activity_id_fk = ? and progress_date=? and approval_status_fk='pending'";
								stmt1 = con.prepareStatement(deleteQry1);
								stmt1.setString(1,getActivityId(obj.getP6_task_code(),getContractIdByContractShortName(obj.getContract_short_name())));
								stmt1.setString(2,StrVar[k]);
								stmt1.executeUpdate();						
								
							    insertStmt.setString(1, obj.getCreated_by_user_id_fk());
							    insertStmt.setString(2, obj.getRemarks());
						    	insertStmt.setString(3,StrVar1[k]);
							    insertStmt.setString(4,getActivityId(obj.getP6_task_code(),getContractIdByContractShortName(obj.getContract_short_name())));
							    insertStmt.setString(5, StrVar[k]);
							    insertStmt.setString(6, "Pending");
							    count=insertStmt.executeUpdate();
								ResultSet rs1 = insertStmt.getGeneratedKeys();
						        if (rs1 != null) {
						            while (rs1.next()) {
						                int generatedId = rs1.getInt(1);
						                generatedIds.add(String.valueOf(generatedId));
						            }
						        }						  
							  loopTimes++;
						}
				
					}

				
				if(stmt != null){stmt.close();}	
				if(stmt1 != null){stmt1.close();}	
				
				NamedParameterJdbcTemplate template1 = new NamedParameterJdbcTemplate(dataSource);			
				if(loopTimes > 0) {
					flag = true;

			        //DBConnectionHandler.closeJDBCResoucrs(null, insertStmt, null);
			        
			        String qry = "INSERT INTO p6_validation_dyhod(dyhod_user_id_fk, progress_id_fk)values(?,?)";
			        insertStmt1 = con.prepareStatement(qry);
			        List<String> dyHodsList = getDyHodsOfActivity(obj.getContract_id_fk());
			        for (String dyhod : dyHodsList) {
						for (String generated_id : generatedIds) {
							insertStmt1.setString(1, dyhod);
						    insertStmt1.setString(2, generated_id);
						    insertStmt1.executeUpdate();
						}
						String messageQry = "INSERT INTO messages (message,user_id_fk,redirect_url,created_date,message_type)"
								+ "VALUES" + "(:message,:user_id_fk,:redirect_url,CURRENT_TIMESTAMP,:message_type)";	

							Messages msgObj = new Messages();
							msgObj.setUser_id_fk(dyhod);
							msgObj.setMessage("New Activities has been updated");
							msgObj.setRedirect_url("/progress-approval-page/");
							msgObj.setMessage_type("Execution");	
							BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(msgObj);
							template1.update(messageQry, paramSource1);						
												
					}
			        
			        
				}				

				if(count > 0) {
					flag=true;
				}
				}
			}
			DBConnectionHandler.closeJDBCResoucrs(null, insertStmt1, null);
			DBConnectionHandler.closeJDBCResoucrs(null, insertStmt, null);
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, insertStmt, rs);
		}
		return flag;
	}
	
	private String getActivityId(String task_code,String ContractId) throws Exception{
		String p6_activity_id="";
		try {
			String qry = "select distinct p6_activity_id from p6_activities where task_code=? and contract_id_fk=?";
			p6_activity_id = (String) jdbcTemplate.queryForObject(qry, new Object[] { task_code,ContractId }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return p6_activity_id;
	}
	
	private int getPreviousCompletedScope(String p6_activity_id_fk,String progress_date) throws Exception{
		int cnt=0;
		try {
			String qry = "select count(*) from p6_validation where p6_activity_id_fk = ? and progress_date=? and approval_status_fk='Approved'";
			cnt = (int) jdbcTemplate.queryForObject(qry, new Object[] { p6_activity_id_fk,progress_date }, int.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return cnt;
	}
	
	private String getPreviousCompletedScopeApproved(String p6_activity_id_fk,String progress_date) throws Exception{
		int cnt=0;
		String completed_scope="0";
		try {
			String qry = "select count(*) from p6_validation where p6_activity_id_fk = ? and progress_date=? and approval_status_fk='Approved'";
			cnt = (int) jdbcTemplate.queryForObject(qry, new Object[] { p6_activity_id_fk,progress_date }, int.class);
			if(cnt==1)
			{
				String qry1 = "select completed_scope from p6_validation where p6_activity_id_fk = ? and progress_date=? and approval_status_fk='Approved'";
				completed_scope = (String) jdbcTemplate.queryForObject(qry1, new Object[] { p6_activity_id_fk,progress_date }, String.class);				
			}
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return completed_scope;
	}	
	
	
	private int getPreviousCompletedScopeCount(String p6_activity_id_fk,String progress_date) throws Exception{
		int cnt=0;
		try {
			String qry = "select count(*) from p6_validation where p6_activity_id_fk = ? and progress_date=? and approval_status_fk='pending'";
			cnt = (int) jdbcTemplate.queryForObject(qry, new Object[] { p6_activity_id_fk,progress_date }, int.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return cnt;
	}	
	
	private String getContractId(String task_code) throws Exception{
		String contract_id_fk="";
		try {
			String qry = "select distinct contract_id_fk from p6_activities where task_code=?";
			contract_id_fk = (String) jdbcTemplate.queryForObject(qry, new Object[] { task_code }, String.class);
		} catch (Exception e) {
			throw new Exception(e);
		}		
		return contract_id_fk;
	}

	@Override
	public ResultSet getExportActivitiesbyContract(StripChart obj) throws Exception {
		Connection connection = null;
		java.sql.CallableStatement statement = null;
        connection = dataSource.getConnection();
		ResultSet rs = null;
		try{
				connection = dataSource.getConnection();	
				logger.error("callingStoredProcedures exportActivities :"+ new Date());	
				String qry1 = "exec dbo.[exportActivities] ?,?,?,?";			
				statement = connection.prepareCall(qry1);
				statement.setString(1, obj.getContract_id_fk());
				statement.setString(2, obj.getStructure_type_fk());
				statement.setString(3, obj.getStrip_chart_structure_id_fk());
				statement.setString(4, obj.getProgress_date());
				rs=statement.executeQuery();
				//DBConnectionHandler.closeJDBCResoucrs(null, statement, rs);
				logger.error("callingStoredProcedures Ends exportActivities :"+ new Date());	

		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);			
		}
        return rs;				

	}

	@Override
	public List<StripChart> getTaskCodesList(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT distinct task_code as p6_task_code "
					+ "FROM p6_activities s left join structure s11 on s11.structure_id = s.structure_id_fk "
					+ "LEFT JOIN contract c ON c.contract_id = s.contract_id_fk "
					+ "WHERE 0=0 and task_code is not null ";
			int arrSize = 0;
			
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				qry = qry + " and s.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getP6_task_code())){
				qry = qry + " and task_code = ?";
				arrSize++;
			}			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())) {
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getP6_task_code())){
				pValues[i++] = obj.getP6_task_code();
			}			
			
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public boolean updateModifyActualsBulk(StripChart obj) throws Exception {
		boolean flag = false;

		Connection con = null;
		PreparedStatement updateStmt = null;
		PreparedStatement updateStmt1 = null;
		PreparedStatement updateStmt2 = null;
		PreparedStatement updateStmt3 = null;
		ResultSet resultSet = null;
		java.sql.CallableStatement cstmt=null;
		
		con = dataSource.getConnection();	
		
		if(obj.getPending().compareTo("1")==0 || obj.getPending().compareTo("2")==0)
		{
			for (int i = 0; i < obj.getActivity_ids().length; i++)
			{
				if(obj.getIds()[i].compareTo("1")==0)
				{
		
					String deleteQry = "delete from p6_validation_dyhod where progress_id_fk in    " + 
							"(select progress_id from p6_validation where p6_activity_id_fk in(select  p6_activity_id from p6_activities where task_code=? and contract_id_fk=?)); ";		 
					updateStmt = con.prepareStatement(deleteQry);
					
					updateStmt.setString(1, obj.getP6_task_codes()[i]);
					updateStmt.setString(2, obj.getContract_id_fk());
					updateStmt.execute();
					if(updateStmt != null){updateStmt.close();}	
					
			
					String deleteQry1 = "delete from p6_validation where p6_activity_id_fk in(select  p6_activity_id from p6_activities where task_code=? and contract_id_fk=?)";		 
					updateStmt1 = con.prepareStatement(deleteQry1);
					
					updateStmt1.setString(1, obj.getP6_task_codes()[i]);
					updateStmt1.setString(2, obj.getContract_id_fk());
					updateStmt1.execute();
					if(updateStmt1 != null){updateStmt1.close();}
					
			
					String deleteQry2 = "delete from p6_activity_progress where p6_activity_id_fk in(select  p6_activity_id from p6_activities where task_code=? and contract_id_fk=?)";		 
					updateStmt2 = con.prepareStatement(deleteQry2);
					
					updateStmt2.setString(1, obj.getP6_task_codes()[i]);
					updateStmt2.setString(2, obj.getContract_id_fk());
					updateStmt2.execute();
					if(updateStmt2 != null){updateStmt2.close();}
				}
			}
		}
		
		if(obj.getPending().compareTo("1")==0)
		{
			for (int i = 0; i < obj.getActivity_ids().length; i++)
			{
				if(obj.getIds()[i].compareTo("1")==0)
				{			
					String deleteQry3 = "update p6_activities set completed=0 where p6_activity_id in(select  p6_activity_id from p6_activities where task_code=? and contract_id_fk=?)";		 
					updateStmt3 = con.prepareStatement(deleteQry3);
					
					updateStmt3.setString(1, obj.getP6_task_codes()[i]);
					updateStmt3.setString(2, obj.getContract_id_fk());
					updateStmt3.execute();
					if(updateStmt3 != null){updateStmt3.close();}	
				}
			}
		}
		if(obj.getPending().compareTo("2")==0)
		{
			for (int i = 0; i < obj.getActivity_ids().length; i++)
			{
				if(obj.getIds()[i].compareTo("1")==0)
				{			
					String deleteQry3 = "delete from p6_activities where p6_activity_id in(select  p6_activity_id from p6_activities where task_code=? and contract_id_fk=?)";		 
					updateStmt3 = con.prepareStatement(deleteQry3);
					
					updateStmt3.setString(1, obj.getP6_task_codes()[i]);
					updateStmt3.setString(2, obj.getContract_id_fk());
					updateStmt3.execute();
					if(updateStmt3 != null){updateStmt3.close();}
				}
			}
		}
		if(obj.getPending().compareTo("3")==0)
		{		
			String concat="";
			for (int i = 0; i < obj.getActivity_ids().length; i++)
			{
				if(obj.getIds()[i].compareTo("1")==0)
				{
					concat=concat+obj.getActivity_ids()[i]+",";
				}
			}
			concat=concat.substring(0, concat.length() - 1);  
			
			cstmt = con.prepareCall("{call dbo.deleteActivities(?, ?)}"); 
	        cstmt.setString(1, concat);
	        cstmt.registerOutParameter(2, java.sql.Types.BOOLEAN);  
	        cstmt.execute(); 
		}
		
		return true;
		
	}

	@Override
	public List<StripChart> getContractStructures(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "SELECT s1.structure as strip_chart_structure_id_fk "
					+ "FROM p6_activities s "
					+ "left join structure s1 on s1.structure_id = s.structure_id_fk "
					+ "LEFT JOIN contract c ON c.contract_id = s.contract_id_fk "
					+ "WHERE s1.structure is not null  and s1.structure <> '' AND s.contract_id_fk = ? ";
			int arrSize = 1;
			
			qry = qry + " group by s1.structure ";			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getContract_id_fk();

		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getLatestRowData(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select distinct a.contract_id_fk,structure_type_fk,structure,component as strip_chart_component from p6_activities a " + 
					"left join structure s on s.structure_id=a.structure_id_fk " + 
					"where p6_activity_id=(select top 1 p6_activity_id_fk from p6_validation where created_by_user_id_fk=? order by progress_id desc)";
			int arrSize = 1;
			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getUser_id();

		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> bindData(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select distinct a.contract_id_fk,structure_type_fk,structure,component as strip_chart_component from p6_activities a " + 
					"left join structure s on s.structure_id=a.structure_id_fk " + 
					"where p6_activity_id=?";
			int arrSize = 1;
			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getActivity_id();

		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<StripChart> getLastUpdateRows(StripChart obj) throws Exception {
		List<StripChart> objsList = null;
		try {
			String qry = "select distinct structure,p6_activity_id as activity_id,component as strip_chart_component from p6_activities a " + 
					"left join structure s on s.structure_id=a.structure_id_fk " + 
					"where p6_activity_id in( " + 
					"select distinct p6_activity_id_fk from ( " + 
					"select top 40 p6_activity_id_fk from p6_validation where created_by_user_id_fk=? order by progress_id desc) as a)";
			int arrSize = 1;
			
			
			Object[] pValues = new Object[arrSize];
			
			int i = 0;
			pValues[i++] = obj.getUser_id();

		
			objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<StripChart>(StripChart.class));			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}	

}

