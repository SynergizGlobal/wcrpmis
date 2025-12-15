package com.wcr.wcrbackend.repo;

import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.FileUploads;

@Repository
public class StructureFormDaoImpl implements IStructureFormDao{
	
	
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;
	
//	@Autowired
//	MessagesDao messagesDao;
//	
    @Autowired
    IFormsHistoryDao formsHistoryDao;
	
	@Autowired
	public PlatformTransactionManager transactionManager;


	@Override
	public List<Structure> getStructuresList(Structure obj, int startIndex, int offset, String searchParameter)
			throws Exception {
		List<Structure> objsList = null;
		try {
			String qry ="SELECT distinct structure_id,s.project_id_fk,cp.contract_id_fk,c.contract_name,c.contract_short_name,s.structure_name, " + 
					"s.structure,s.work_status_fk,structure_type_fk "
					+ "FROM structure s " + 
					"left join structure_contract_responsible_people cp on s.structure_id =  cp.structure_id_fk " + 
					"left join contract c on cp.contract_id_fk = c.contract_id  " + 
					"where structure_id is not null and s.status <> 'Inactive' " ;
			
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				qry = qry + " and s.work_status_fk = ? ";
				arrSize++;
			}

			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				qry = qry + " and cp.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())){
				qry = qry + " and structure_type_fk = ? ";
				arrSize++;
			}			
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				qry = qry + " and  cp.responsible_people_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (cp.contract_id_fk like ? or c.contract_short_name like ?   "
						+ "or s.work_status_fk like ? or structure_type_fk like ? or structure like ? or structure_name like ?) ";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			if(!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(offset)) {
				qry = qry + " ORDER BY structure_id ASC offset ? rows  fetch next ? rows only";
				arrSize++;
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				pValues[i++] = obj.getWork_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())){
				pValues[i++] = obj.getStructure_type_fk();
			}			
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_id();
			
			}	
			if(!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
			}
			if(!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(offset)) {
				pValues[i++] = startIndex;
				pValues[i++] = offset;
			}
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Structure>(Structure.class));
		
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public int getTotalRecords(Structure obj, String searchParameter) throws Exception {
		int totalRecords = 0;
		try {
			
			String qry ="select count(*) from (select structure_id  " + 
					"from structure s " + 
					"left join structure_contract_responsible_people cp on s.structure_id =  cp.structure_id_fk " + 
					"left join contract c on cp.contract_id_fk = c.contract_id  " 
					+"where structure_id is not null and s.status <> 'Inactive' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				qry = qry + " and s.work_status_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				qry = qry + " and cp.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())){
				qry = qry + " and structure_type_fk = ? ";
				arrSize++;
			}				
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				qry = qry + " and  cp.responsible_people_id_fk = ? ";
				arrSize++;
			}	
			if(!StringUtils.isEmpty(searchParameter)) {
				qry = qry + " and (cp.contract_id_fk like ? or c.contract_short_name like ?  "
						+ "or s.work_status_fk like ? or structure_type_fk like ? or structure like ? or structure_name like ?) ";
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
				arrSize++;
			}	
			qry = qry + " group by  ISNULL(cp.contract_id_fk,structure_id), structure_id) as total_records ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				pValues[i++] = obj.getWork_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_type_fk())){
				pValues[i++] = obj.getStructure_type_fk();
			}				
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_id();
			
			}	
			if(!StringUtils.isEmpty(searchParameter)) {
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
				pValues[i++] = "%"+searchParameter+"%";
			}
			totalRecords = jdbcTemplate.queryForObject( qry,pValues,Integer.class);
			
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return totalRecords;
	}
	@Override
	public List<Structure> getWorkStatusListForFilter(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "SELECT distinct s.work_status_fk FROM structure s " + 
					"left join structure_contract_responsible_people cp on cp.structure_id_fk = s.structure_id " + 
					"left join contract c on cp.contract_id_fk = c.contract_id "+
					"where cp.contract_id_fk is not null and s.status <> 'Inactive' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				qry = qry + " and s.work_status_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())){
				qry = qry + " and s.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				qry = qry + " and cp.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				qry = qry + " and  cp.responsible_people_id_fk = ? ";
				arrSize++;
			}	
			qry = qry + " GROUP BY s.work_status_fk ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				pValues[i++] = obj.getWork_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())){
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_id();
			
			}	
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Structure>(Structure.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	@Override
	public List<Structure> getContractsListForFilter(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "SELECT cp.contract_id_fk ,c.contract_short_name,c.contract_name \r\n" + 
					"					FROM structure s \r\n" + 
					"					left join structure_contract_responsible_people cp on cp.structure_id_fk = s.structure_id  \r\n" + 
					"					left join contract c on cp.contract_id_fk = c.contract_id \r\n" + 
					"					where cp.contract_id_fk is not null and s.status <> 'Inactive' and contract_name is not null and contract_id not like '%MS%' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				qry = qry + " and s.work_status_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())){
				qry = qry + " and s.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				qry = qry + " and cp.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				qry = qry + " and  cp.responsible_people_id_fk = ? ";
				arrSize++;
			}	
			qry = qry + " GROUP BY cp.contract_id_fk,c.contract_short_name,c.contract_name ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				pValues[i++] = obj.getWork_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())){
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_id();
			
			}	
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Structure>(Structure.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public List<Structure> getStructureTypeListForFilter(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "SELECT s.structure_type_fk as structure_type "
					+ "FROM structure s " + 
					"left join structure_contract_responsible_people cp on cp.structure_id_fk = s.structure_id " + 
					"left join contract c on cp.contract_id_fk = c.contract_id "+
					"where cp.contract_id_fk is not null and s.status <> 'Inactive' ";
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				qry = qry + " and s.work_status_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())){
				qry = qry + " and s.project_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				qry = qry + " and cp.contract_id_fk = ? ";
				arrSize++;
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				qry = qry + " and  cp.responsible_people_id_fk = ? ";
				arrSize++;
			}	
			qry = qry + " GROUP BY s.structure_type_fk ";
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getWork_status_fk())){
				pValues[i++] = obj.getWork_status_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getProject_id_fk())){
				pValues[i++] = obj.getProject_id_fk();
			}
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getContract_id_fk())){
				pValues[i++] = obj.getContract_id_fk();
			}
			if(!StringUtils.isEmpty(obj) &&  !CommonConstants.ROLE_CODE_IT_ADMIN.equals(obj.getUser_role_code()) &&  (!CommonConstants.USER_TYPE_HOD.equals(obj.getUser_type_fk())) && !CommonConstants.USER_TYPE_DYHOD.equals(obj.getUser_type_fk())) {
				pValues[i++] = obj.getUser_id();
			
			}	
		    objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Structure>(Structure.class));
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Structure> getStructureDetailsLocations(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "select fob_details_location from fob_details_location ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Structure>(Structure.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Structure> getStructureDetailsTypes(Structure obj) throws Exception {
		List<Structure> objsList = null;
		try {
			String qry = "select fob_details_type from fob_details_type ";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Structure>(Structure.class));			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public Structure getStructuresFormDetails(Structure obj) throws Exception {
		Structure structure = null;
		try {
			String qry = "select structure_id, s.project_id_fk, structure_type_fk, structure,p.project_name,  "
					+ "s.work_status_fk,s.structure_name,s.latitude as latitude,s.longitude as longitude,  FORMAT(s.target_date,'dd-MM-yyyy') AS target_date, "
					+ "s.estimated_cost,s.completion_cost,s.completion_cost_units,FORMAT(s.commissioning_date,'dd-MM-yyyy') AS commissioning_date,FORMAT(s.actual_completion_date,'dd-MM-yyyy') AS actual_completion_date, s.estimated_cost_units,"
					+ " FORMAT(s.construction_start_date,'dd-MM-yyyy') AS construction_start_date, FORMAT(s.revised_completion,'dd-MM-yyyy') AS revised_completion, s.remarks "
					+ "from structure s " + 
					"left join project p on s.project_id_fk = p.project_id where s.status <> 'Inactive' and structure_id is not null " ; 
			int arrSize = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_id())) {
				qry = qry + " and structure_id = ? ";
				arrSize++;
			}
			Object[] pValues = new Object[arrSize];
			int i = 0;
			if(!StringUtils.isEmpty(obj) && !StringUtils.isEmpty(obj.getStructure_id())) {
				pValues[i++] = obj.getStructure_id();
			}
			structure = (Structure)jdbcTemplate.queryForObject(qry, pValues, new BeanPropertyRowMapper<Structure>(Structure.class));	
			if(!StringUtils.isEmpty(structure) && !StringUtils.isEmpty(structure.getStructure_id())) {
				if(!StringUtils.isEmpty(structure)) {
					String executivesQry ="select DISTINCT structure_id_fk as structure_id,contract_id_fk,contract_short_name from structure_contract_responsible_people sp left join contract c on c.contract_id = sp.contract_id_fk where  structure_id_fk = ? ";
					List<Structure> executivesList = jdbcTemplate.query( executivesQry,new Object[] {structure.getStructure_id()}, new BeanPropertyRowMapper<Structure>(Structure.class));
					structure.setExecutivesList(executivesList);
					for(Structure peopleList : structure.getExecutivesList()) {
						String peopleQry ="select id, structure_id_fk, contract_id_fk,designation,user_name, responsible_people_id_fk from structure_contract_responsible_people sp LEFT JOIN [user] u on u.user_id=sp.responsible_people_id_fk where contract_id_fk = ? and  structure_id_fk = ? ";
						List<Structure> peopleLists = jdbcTemplate.query( peopleQry,new Object[] {peopleList.getContract_id_fk(),peopleList.getStructure_id()}, new BeanPropertyRowMapper<Structure>(Structure.class));
						peopleList.setResponsiblePeopleLists(peopleLists);
					}
				}
				if(!StringUtils.isEmpty(structure)) {
					String detailsQry ="select id, structure_id_fk, structure_detail, value as structure_value from structure_details sd where  structure_id_fk = ? ";
					detailsQry = detailsQry + " and structure_detail in('Type','Location of FOB','KM','FOB Length (m)','FOB Width (m)','Platforms Connecting','No. of Staircases','No. of Skywalk','Future Provision Escalators nos') order by \r\n" + 
							"case\r\n" + 
							"when structure_detail='Type' then 1\r\n" + 
							"when structure_detail='Location of FOB' then 2\r\n" + 
							"when structure_detail='KM' then 3\r\n" + 
							"when structure_detail='FOB Length (m)' then 4\r\n" + 
							"when structure_detail='FOB Width (m)' then 5\r\n" + 
							"when structure_detail='Platforms Connecting' then 6\r\n" + 
							"when structure_detail='No. of Staircases' then 7\r\n" + 
							"when structure_detail='No. of Skywalk' then 8\r\n" + 
							"when structure_detail='Future Provision Escalators nos' then 9 end asc";

					List<Structure> detailsList = jdbcTemplate.query( detailsQry,new Object[] {structure.getStructure_id()}, new BeanPropertyRowMapper<Structure>(Structure.class));
					if(StringUtils.isEmpty(detailsList) || detailsList.size() == 0) {
						detailsQry = "select structure_detail from structure_details sd " ;
						detailsQry = detailsQry + " where structure_detail in('Type','Location of FOB','KM','FOB Length (m)','FOB Width (m)','Platforms Connecting','No. of Staircases','No. of Skywalk','Future Provision Escalators nos') "
								+ "group by structure_detail order by case\r\n" + 
								"when structure_detail='Type' then 1\r\n" + 
								"when structure_detail='Location of FOB' then 2\r\n" + 
								"when structure_detail='KM' then 3 \r\n" + 
								"when structure_detail='FOB Length (m)' then 4\r\n" + 
								"when structure_detail='FOB Width (m)' then 5\r\n" + 
								"when structure_detail='Platforms Connecting' then 6\r\n" + 
								"when structure_detail='No. of Staircases' then 7\r\n" + 
								"when structure_detail='No. of Skywalk' then 8\r\n" + 
								"when structure_detail='Future Provision Escalators nos' then 9 end asc";
						
						detailsList = jdbcTemplate.query(detailsQry, new Object[] {}, new BeanPropertyRowMapper<Structure>(Structure.class));
					}
					structure.setStructureDetailsList(detailsList);
				}
				
				if(!StringUtils.isEmpty(structure)) {
						String detailsQry ="select id, structure_id_fk, structure_detail, value as structure_value from structure_details sd where  structure_id_fk = ? "
								+ "and structure_detail not in('Type','Location of FOB','KM','FOB Length (m)','FOB Width (m)','Platforms Connecting','No. of Staircases','No. of Skywalk','Future Provision Escalators nos') " ;
						List<Structure> detailsList = jdbcTemplate.query( detailsQry,new Object[] {structure.getStructure_id()}, new BeanPropertyRowMapper<Structure>(Structure.class));
						structure.setStructureDetailsList1(detailsList);
				}
				
				if(!StringUtils.isEmpty(structure)) {
					String documentsQry ="select id as structure_file_id, structure_id_fk, name, attachment,structure_file_type_fk,created_date from structure_documents d where  structure_id_fk = ? ";
					List<Structure> documentsList = jdbcTemplate.query( documentsQry,new Object[] {structure.getStructure_id()}, new BeanPropertyRowMapper<Structure>(Structure.class));
					structure.setDocumentsList(documentsList);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return structure;
	}
	
//
	@Override
	public boolean updateStructureForm(Structure obj) throws Exception {
	    boolean flag = false;
	    TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = transactionManager.getTransaction(def);
	    try {
	        NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	        String structure_id = null;
	        String qry = "UPDATE structure set "
	                + "structure =:structure,structure_name =:structure_name,structure_type_fk =:structure_type_fk,target_date =:target_date,"
	                + "construction_start_date =:construction_start_date,actual_completion_date =:actual_completion_date,commissioning_date =:commissioning_date,"
	                + "estimated_cost =:estimated_cost,completion_cost =:completion_cost,work_status_fk =:work_status_fk,latitude =:latitude,longitude =:longitude"
	                + ",remarks =:remarks,revised_completion =:revised_completion,estimated_cost_units =:estimated_cost_units,completion_cost_units =:completion_cost_units "
	                + " where structure_id= :structure_id";
	        BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
	        int count = namedParamJdbcTemplate.update(qry, paramSource);
	        if (count > 0) {
	            flag = true;
	        }
	        if (flag) {

	            String deleteQry = "DELETE from structure_details where structure_id_fk = :structure_id";
	            paramSource = new BeanPropertySqlParameterSource(obj);
	            count = namedParamJdbcTemplate.update(deleteQry, paramSource);

	            if (flag && !StringUtils.isEmpty(obj.getStructure_details()) && obj.getStructure_details().length > 0) {
	                obj.setStructure_details(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructure_details()));
	            }
	            if (flag && !StringUtils.isEmpty(obj.getStructure_values()) && obj.getStructure_values().length > 0) {
	                obj.setStructure_values(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructure_values()));
	            }

	            String[] fobDetailNames = obj.getStructure_details();
	            String[] fobDetailValues = obj.getStructure_values();

	            String qryFOBDetail = "INSERT INTO structure_details (structure_id_fk,structure_detail,value) VALUES  (?,?,?)";

	            int[] counts = jdbcTemplate.batchUpdate(qryFOBDetail,
	                    new BatchPreparedStatementSetter() {
	                        @Override
	                        public void setValues(PreparedStatement ps, int i) throws SQLException {
	                            int k = 1;
	                            ps.setString(k++, obj.getStructure_id());
	                            // SAFELY get detail name
	                            String detailName = (fobDetailNames != null && fobDetailNames.length > i) ? fobDetailNames[i] : null;
	                            ps.setString(k++, detailName);
	                            // SAFELY get detail value
	                            String detailValue = (fobDetailValues != null && fobDetailValues.length > i) ? fobDetailValues[i] : null;
	                            ps.setString(k++, detailValue);
	                        }

	                        @Override
	                        public int getBatchSize() {
	                            int arraySize = 0;
	                            if (!StringUtils.isEmpty(obj.getStructure_details()) && obj.getStructure_details().length > 0) {
	                                obj.setStructure_details(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructure_details()));
	                                if (arraySize < obj.getStructure_details().length) {
	                                    arraySize = obj.getStructure_details().length;
	                                }
	                            }
	                            if (!StringUtils.isEmpty(obj.getStructure_values()) && obj.getStructure_values().length > 0) {
	                                obj.setStructure_values(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructure_values()));
	                                if (arraySize < obj.getStructure_values().length) {
	                                    arraySize = obj.getStructure_values().length;
	                                }
	                            }
	                            return arraySize;
	                        }
	                    });
	            String docDeleteQry = "DELETE from structure_documents where structure_id_fk = :structure_id";
	            paramSource = new BeanPropertySqlParameterSource(obj);
	            count = namedParamJdbcTemplate.update(docDeleteQry, paramSource);

	            //String file_insert_qry = "INSERT into  fob_files ( fob_id_fk, attachment,created_date,fob_file_type_fk,name) VALUES (:fob_id,:attachment,:created_date,:fob_file_type_fk,:name)";
	            String document_insert_qry = "INSERT into  structure_documents ( structure_id_fk, attachment,structure_file_type_fk,name,created_date) VALUES (:structure_id,:attachment,:structure_file_type_fk,:name,CONVERT(date, getdate()))";

	            int arraySize = 0, docArrSize = 0;

	            if (!StringUtils.isEmpty(obj.getStructureFileNames()) && obj.getStructureFileNames().length > 0) {
	                obj.setStructureFileNames(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructureFileNames()));
	                if (docArrSize < obj.getStructureFileNames().length) {
	                    docArrSize = obj.getStructureFileNames().length;
	                }
	            }
	            if (!StringUtils.isEmpty(obj.getStructure_file_types()) && obj.getStructure_file_types().length > 0) {
	                obj.setStructure_file_types(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructure_file_types()));
	                if (docArrSize < obj.getStructure_file_types().length) {
	                    docArrSize = obj.getStructure_file_types().length;
	                }
	            }
	            if (!StringUtils.isEmpty(obj.getStructureDocumentNames()) && obj.getStructureDocumentNames().length > 0) {
	                obj.setStructureDocumentNames(CommonMethods.replaceEmptyByNullInSringArray(obj.getStructureDocumentNames()));
	                if (docArrSize < obj.getStructureDocumentNames().length) {
	                    docArrSize = obj.getStructureDocumentNames().length;
	                }
	            }

	            // FIXED DOCUMENT PROCESSING LOOP
	            for (int i = 0; i < docArrSize; i++) {
	                // SAFELY get the file at index i
	                MultipartFile multipartFile = null;
	                if (obj.getStructureFiles() != null && i < obj.getStructureFiles().length) {
	                    multipartFile = obj.getStructureFiles()[i];
	                }

	                // SAFELY get array values at index i
	                String fileName = null;
	                String fileType = null;
	                String docName = null;

	                if (obj.getStructureFileNames() != null && i < obj.getStructureFileNames().length) {
	                    fileName = obj.getStructureFileNames()[i];
	                }
	                if (obj.getStructure_file_types() != null && i < obj.getStructure_file_types().length) {
	                    fileType = obj.getStructure_file_types()[i];
	                }
	                if (obj.getStructureDocumentNames() != null && i < obj.getStructureDocumentNames().length) {
	                    docName = obj.getStructureDocumentNames()[i];
	                }

	                // Check if we have enough data to save this document
	                boolean hasNewFile = (multipartFile != null && !multipartFile.isEmpty());
	                boolean hasFileName = !StringUtils.isEmpty(fileName);
	                boolean hasFileType = !StringUtils.isEmpty(fileType);
	                boolean hasDocName = !StringUtils.isEmpty(docName);

	                // Save only if we have required data
	                if ((hasNewFile || hasFileName) && hasFileType && hasDocName) {
	                    String saveDirectory = CommonConstants2.STRUCTURE_FILE_SAVING_PATH;

	                    // If new file uploaded, save it
	                    if (hasNewFile) {
	                        FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
	                    }

	                    Structure fileObj = new Structure();
	                    fileObj.setAttachment(fileName);
	                    fileObj.setStructure_id(obj.getStructure_id());
	                    fileObj.setStructure_file_type_fk(fileType);
	                    fileObj.setName(docName);

	                    paramSource = new BeanPropertySqlParameterSource(fileObj);
	                    namedParamJdbcTemplate.update(document_insert_qry, paramSource);
	                }
	            }

	            String conDeleteQry = "DELETE from structure_contract_responsible_people where structure_id_fk = :structure_id";
	            paramSource = new BeanPropertySqlParameterSource(obj);
	            count = namedParamJdbcTemplate.update(conDeleteQry, paramSource);

	            if (!StringUtils.isEmpty(obj.getContracts_id_fk()) && obj.getContracts_id_fk().length > 0) {
	                String qry3 = "INSERT into structure_contract_responsible_people (structure_id_fk,contract_id_fk,responsible_people_id_fk) VALUES (:structure_id_fk,:contract_id_fk,:responsible_people_id_fk)";
	                
	                // FIXED: Add null check for contracts array
	                int len = (obj.getContracts_id_fk() != null) ? obj.getContracts_id_fk().length : 0;

	                int size = 0;
	                if (!StringUtils.isEmpty(obj.getContracts_id_fk()) && obj.getContracts_id_fk().length > 0) {
	                    obj.setContracts_id_fk(CommonMethods.replaceEmptyByNullInSringArray(obj.getContracts_id_fk()));
	                    if (size < obj.getContracts_id_fk().length) {
	                        size = obj.getContracts_id_fk().length;
	                    }
	                }
	                if (size == 1) {
	                    String joined = "";
	                    if (obj.getResponsible_people_id_fks() != null && obj.getResponsible_people_id_fks().length > 0) {
	                        joined = String.join(",", obj.getResponsible_people_id_fks());
	                    }
	                    String[] strArray = new String[]{joined};
	                    obj.setResponsible_people_id_fks(strArray);
	                }
	                if (!StringUtils.isEmpty(obj.getResponsible_people_id_fks()) && obj.getResponsible_people_id_fks().length > 0) {
	                    obj.setResponsible_people_id_fks(CommonMethods.replaceEmptyByNullInSringArray(obj.getResponsible_people_id_fks()));
	                    if (size < obj.getResponsible_people_id_fks().length) {
	                        size = obj.getResponsible_people_id_fks().length;
	                    }
	                }
	                for (int i = 0; i < size; i++) {
	                    List<String> executives = null;
	                    
	                    // SAFELY get executives at index i
	                    String execString = null;
	                    if (obj.getResponsible_people_id_fks() != null && i < obj.getResponsible_people_id_fks().length) {
	                        execString = obj.getResponsible_people_id_fks()[i];
	                    }
	                    
	                    if (!StringUtils.isEmpty(execString)) {
	                        if (execString.contains(",")) {
	                            executives = new ArrayList<String>(Arrays.asList(execString.split(",")));
	                        } else {
	                            executives = new ArrayList<String>(Arrays.asList(execString));
	                        }
	                        for (String eObj : executives) {
	                            // SAFELY get contract at index i
	                            String contractId = null;
	                            if (obj.getContracts_id_fk() != null && i < obj.getContracts_id_fk().length) {
	                                contractId = obj.getContracts_id_fk()[i];
	                            }
	                            
	                            if (!eObj.equals("null") && !StringUtils.isEmpty(contractId) && !StringUtils.isEmpty(eObj)) {
	                                Structure fileObj = new Structure();
	                                fileObj.setStructure_id_fk(obj.getStructure_id());
	                                fileObj.setContract_id_fk(contractId);
	                                fileObj.setResponsible_people_id_fk(eObj);
	                                paramSource = new BeanPropertySqlParameterSource(fileObj);
	                                namedParamJdbcTemplate.update(qry3, paramSource);
	                            }
	                        }
	                    }

	                }

	            }

	            FormHistory formHistory = new FormHistory();
	            formHistory.setCreated_by_user_id_fk(obj.getUser_id());
	            formHistory.setUser(obj.getDesignation() + " - " + obj.getUser_name());
	            formHistory.setModule_name_fk("Works");
	            formHistory.setForm_name("Update Structure Form");
	            formHistory.setForm_action_type("Update");
	            formHistory.setForm_details("Structure for " + obj.getProject_id_fk() + " Updated");
	            formHistory.setProject_id_fk(obj.getProject_id_fk());
	            //formHistory.setContract(obj.getContract_id_fk());

	            boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
	            /********************************************************************************/
	        }
	        transactionManager.commit(status);
	    } catch (Exception e) {
	        transactionManager.rollback(status);
	        e.printStackTrace();
	        throw new Exception(e);
	    }
	    return flag;
	}

	
}