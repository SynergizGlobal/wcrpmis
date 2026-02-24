package com.wcr.wcrbackend.repo;


import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.DTO.Training;
import com.wcr.wcrbackend.DTO.Year;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DBConnectionHandler;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.common.FileUploads;


@Repository
public class ProjectRepository implements IProjectRepository {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	

    private final BeanPropertyRowMapper<Project> rowMapper =
            new BeanPropertyRowMapper<>(Project.class);


    // =========================================================
    // 1️⃣ Get ALL projects
    // (equivalent to JPA findAll())
    // =========================================================
    public List<Project> findAll() {

        String sql = """
                SELECT *
                FROM project
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }


    // =========================================================
    // 2️⃣ findByUserId (converted from JPA @Query)
    // =========================================================
    public List<String> findByUserId(String userId) {

        String sql = """
            SELECT DISTINCT p.project_name
            FROM dbo.[project] p
            JOIN [contract] c ON c.project_id_fk = p.project_id
            JOIN contractor co ON co.contractor_id = c.contractor_id_fk
            JOIN [user] u ON co.contractor_name = u.[user_name]
            WHERE u.user_id = ?
            """;

        return jdbcTemplate.queryForList(sql, String.class, userId);
    }


    // =========================================================
    // 3️⃣ getProjectsForOtherUsersByUserId (converted from JPA)
    // =========================================================
    public List<String> getProjectsForOtherUsersByUserId(String userId) {

        String sql = """
            SELECT DISTINCT p.project_name
            FROM project p
            JOIN [contract] c ON c.project_id_fk = p.project_id
            JOIN contract_executive scrp ON scrp.contract_id_fk = c.contract_id
            WHERE scrp.executive_user_id_fk = ?
            """;

        return jdbcTemplate.queryForList(sql, String.class, userId);
    }

	
	
	@Override
	public List<Project> getProjectList(Project project) throws Exception {
	    List<Project> objsList = null;
	    try {
	    	String qry = "SELECT " +
	    		    "p.project_id, " +
	    		    "p.project_name, " +
	    		    "p.project_status, " +
	    		    "p.project_type_id_fk, " +
	    		    "pt.project_type_name, " +
	    		    "p.railway_zone, " +
	    		    "p.plan_head_number, " +
	    		    "p.sanctioned_year, " +
	    		    "p.sanctioned_amount, " +
	    		    "p.sanctioned_commissioning_date, " +
	    		    "p.division, " +
	    		    "p.sections, " +
	    		    "p.pb_item_number, " +
	    		    "p.remarks, " +
	    		    "p.benefits, " +
	    		    "p.structure_details, "+ 
	    		    "p.from_chainage, "+ 
	    		    "p.to_chainage," +
	    		    "(SELECT financial_year_fk " +
	    		    "   FROM project_pinkbook " +
	    		    "  WHERE project_id_fk = p.project_id " +
	    		    "  ORDER BY project_pinkbook_id ASC " +
	    		    "  OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY) AS financial_year_fk, " +
	    		    "(SELECT pb_item_no " +
	    		    "   FROM project_pinkbook " +
	    		    "  WHERE project_id_fk = p.project_id " +
	    		    "  ORDER BY project_pinkbook_id ASC " +
	    		    "  OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY) AS pb_item_no " +
	    		"FROM project p " +
	    		"INNER JOIN project_type pt ON pt.project_type_id = p.project_type_id_fk";


	        objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<>(Project.class));

	    } catch (Exception e) {
	        throw new Exception(e);
	    }
	    return objsList;
	}

	
	
	@Override 
	public Project getProject(String projectId, Project projects) throws Exception {
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;
	    Project project = null;

	    try {
	        connection = dataSource.getConnection();
	        String qry = "SELECT p.project_id, p.project_name, p.plan_head_number, p.remarks, p.project_status, "
	                + "p.section_id, p.division_id, p.sanctioned_amount, p.sanctioned_commissioning_date,p.railway_zone,rz.railway_name AS railway_zone_name, "
	                + "p.estimated_completion_cost, p.revised_completion_date, "
	                   + "p.benefits, pt.project_type_id, pt.project_type_name,actual_completion_cost,completion_date,actual_completion_date, "
	                   + "p.structure_details,p.from_chainage, p.to_chainage, "
	                   + "(SELECT financial_year_fk FROM project_pinkbook WHERE project_id_fk = ? ORDER BY project_pinkbook_id ASC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY) AS financial_year_fk, "
	                   + "(SELECT pb_item_no FROM project_pinkbook WHERE project_id_fk = ? ORDER BY project_pinkbook_id ASC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY) AS pb_item_no "
	                   + "FROM project p "
	                   + "INNER JOIN project_type pt ON p.project_type_id_fk = pt.project_type_id "
	                   + "LEFT JOIN railway rz  ON rz.railway_id = p.railway_zone"

	               + "LEFT JOIN divisions d ON d.division_id = p.division_id"

	               +"LEFT JOIN sections s ON s.section_id = p.section_id"

	               + "LEFT JOIN financial_year fy ON fy.financial_year_id = p.sanctioned_year"
	                   + "WHERE p.project_id = ?";

	        stmt = connection.prepareStatement(qry);
	        stmt.setString(1, projectId);
	        stmt.setString(2, projectId);
	        stmt.setString(3, projectId);

	        resultSet = stmt.executeQuery();
	        while (resultSet.next()) {
	            project = new Project();
	            project.setProject_id(resultSet.getString("project_id"));
	            
	            project.setCompletion_date(resultSet.getString("completion_date"));
	            project.setActual_completion_cost(resultSet.getString("actual_completion_cost"));
	            project.setActual_completion_date(resultSet.getString("actual_completion_date"));
	            project.setSection_id(resultSet.getString("section_id"));
	            project.setDivision_id(resultSet.getString("division_id"));
	            project.setSanctioned_amount(resultSet.getString("sanctioned_amount"));
	            project.setSanctioned_commissioning_date(resultSet.getString("sanctioned_commissioning_date"));
	            project.setEstimated_completion_cost(resultSet.getString("estimated_completion_cost"));
	            project.setRevised_completion_date(resultSet.getString("revised_completion_date"));
	            project.setRailway_zone(resultSet.getString("railway_zone"));
	            
            
	            
	            
	            
	            
	            project.setProject_name(resultSet.getString("project_name")); 
	            project.setPlan_head_number(resultSet.getString("plan_head_number"));
	            project.setRemarks(resultSet.getString("remarks"));
	            project.setProject_status(resultSet.getString("project_status"));
	            project.setBenefits(resultSet.getString("benefits"));

	            // Set project type
	            project.setProject_type_id(resultSet.getString("project_type_id"));
	            project.setProject_type_name(resultSet.getString("project_type_name"));

	            if (!StringUtils.isEmpty(resultSet.getString("financial_year_fk"))) {
	                project.setFinancial_year_fk(resultSet.getString("financial_year_fk"));
	            }

	            String pbItemNo = resultSet.getString("pb_item_no");

	            if (!StringUtils.isEmpty(pbItemNo)) {
	                String railway = null;
	                String pb_item_no = null;

	                if (pbItemNo.contains("-")) {
	                    String[] pb_item_no_temp = pbItemNo.split("-");
	                    if (pb_item_no_temp.length > 0) railway = pb_item_no_temp[0].trim();
	                    if (pb_item_no_temp.length > 1) pb_item_no = pb_item_no_temp[1].trim();
	                } else if (pbItemNo.equals("CR") || pbItemNo.equals("WR")) {
	                    railway = pbItemNo;
	                }

	                if (!StringUtils.isEmpty(railway)) project.setRailway(railway);
	                if (!StringUtils.isEmpty(pb_item_no)) project.setPb_item_no(pb_item_no);
	            }

	            project.setProjectGalleryFilesList(getProjectGalleryFiles(project.getProject_id(), connection));
	            project.setProjectPinkBooks(getProjectPinkBooks(project.getProject_id(), connection));
	            project.setProjectFilesList(getProjectFiles(project.getProject_id(), connection));
	        }
	    } catch (Exception e) { 
	        e.printStackTrace();
	        throw new Exception(e);
	    } finally {
	        DBConnectionHandler.closeJDBCResoucrs(connection, stmt, resultSet);
	    }

	    return project;
	}

	
	
	private List<Project> getProjectFiles(String project_id, Connection con) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Project> objsList = new ArrayList<Project>();
		try{
			String qry = "select id as project_file_id,project_id_fk,attachment,project_file_type_fk from project_files where project_id_fk = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1,project_id);
			rs = stmt.executeQuery();  
			while(rs.next()) {
				Project obj = new Project();
				obj.setProject_file_id(rs.getString("project_file_id"));
				obj.setProject_id_fk(rs.getString("project_id_fk"));
				obj.setAttachment(rs.getString("attachment"));
				obj.setProject_file_type_fk(rs.getString("project_file_type_fk"));
				objsList.add(obj);
			}
		}catch(Exception e){ 		
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return objsList;
	}


	private List<Project> getProjectPinkBooks(String project_id, Connection con) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Project> objsList = new ArrayList<Project>();
		try{
			String qry = "select project_pinkbook_id,project_id_fk,financial_year_fk,pb_item_no "
					+ "from project_pinkbook "
					+ "where project_id_fk = ? "
					+ "and project_pinkbook_id NOT IN (select min(project_pinkbook_id) from project_pinkbook where project_id_fk = ?)";
			stmt = con.prepareStatement(qry);
			stmt.setString(1,project_id);
			stmt.setString(2,project_id);
			rs = stmt.executeQuery();  
			while(rs.next()) {
				Project obj = new Project();
				obj.setProject_pinkbook_id(rs.getString("project_pinkbook_id"));
				obj.setProject_id_fk(rs.getString("project_id_fk"));
				obj.setFinancial_year_fk(rs.getString("financial_year_fk"));
				obj.setPb_item_no(rs.getString("pb_item_no"));
				String railway = null;
				String pb_item_no = null;
				if(!StringUtils.isEmpty(obj.getPb_item_no())) {
					pb_item_no = obj.getPb_item_no();
					if(pb_item_no.contains("-")) {
						String[] pb_item_no_temp = pb_item_no.split("-");
						if(pb_item_no_temp.length > 0) {
							railway = pb_item_no_temp[0].trim();
						}
						if(pb_item_no_temp.length > 1) {
							pb_item_no = pb_item_no_temp[1].trim();
						}
					}else if(pb_item_no.equals("CR") || pb_item_no.equals("WR")) {
						railway = pb_item_no;
					}
				}
				obj.setRailway(railway);
				obj.setPb_item_no(pb_item_no);
				objsList.add(obj);
			}
		}catch(Exception e){ 		
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return objsList;
	}


	private List<Project> getProjectGalleryFiles(String project_id, Connection con) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Project> filesObj = new ArrayList<Project>();
		try{
			String qry = "select id,file_name,project_id_fk,FORMAT(created_date,'dd-MM-yyyy') AS created_date,created_by from project_gallery where project_id_fk = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1,project_id);
			rs = stmt.executeQuery();  
			while(rs.next()) {
				Project obj = new Project();
				obj.setId(rs.getString("id"));
				obj.setFile_name(rs.getString("file_name"));
				obj.setCreated_date(rs.getString("created_date"));
				obj.setProject_id_fk(rs.getString("project_id_fk"));
				filesObj.add(obj);
			}
		}catch(Exception e){ 		
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return filesObj;
	}


	@Override
	public boolean updateProject(Project project)throws Exception{
		int count = 0;
		boolean flag = false;
		try{
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
			
			String qry = "update project set project_name = :project_name,sanctioned_amount=:sanctioned_amount,actual_completion_cost=:actual_completion_cost,completion_date=:completion_date,actual_completion_date=:actual_completion_date,sanctioned_commissioning_date=:sanctioned_commissioning_date,division_id=:division_id,section_id=:section_id,railway_zone=:railway_zone,project_type_id_fk=:project_type_id,estimated_completion_cost=:estimated_completion_cost,revised_completion_date=:revised_completion_date,plan_head_number = :plan_head_number,remarks = :remarks,project_status=:project_status,benefits = :benefits,"
					+"structure_details = :structure_details,from_chainage = :from_chainage,to_chainage = :to_chainage "
					 + "where project_id = :project_id";			
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(project);
			count = template.update(qry, paramSource);
			if(count > 0 ){
				flag = true;
			}			
			if(flag) {	
				int arraySize = 0;
				if(!StringUtils.isEmpty(project.getProjectGalleryFileNames()) && project.getProjectGalleryFileNames().length > 0 ) {
					project.setProjectGalleryFileNames(CommonMethods.replaceEmptyByNullInSringArray(project.getProjectGalleryFileNames()));
					if(arraySize < project.getProjectGalleryFileNames().length) {
						arraySize = project.getProjectGalleryFileNames().length;
					}
				}
				
				String deleteQry ="delete from project_gallery where project_id_fk = :project_id ";
				Project dObj = new Project();
				dObj.setProject_id(project.getProject_id());
				paramSource = new BeanPropertySqlParameterSource(dObj);
				template.update(deleteQry, paramSource);

				String galleryQry ="INSERT into project_gallery (file_name,project_id_fk,created_by,created_date)VALUES(:file_name,:project_id,:created_by,:created_date)";
				for (int i = 0; i < arraySize; i++) {
					MultipartFile multipartFile = project.getProjectGalleryFiles()[i];
					if ((null != multipartFile && !multipartFile.isEmpty())
							|| !StringUtils.isEmpty(project.getProjectGalleryFileNames()[i])) {
						String saveDirectory = CommonConstants2.PROJECT_GALLERY_FILE_SAVING_PATH + project.getProject_id() + File.separator;
						String fileName = project.getProjectGalleryFileNames()[i];
						String date = project.getCreated_dates()[i];
						project.setCreated_date(DateParser.parse(date));
						if (null != multipartFile && !multipartFile.isEmpty()) {
							FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
						}
						Project imgObj = new Project();
						imgObj.setFile_name(fileName);
						imgObj.setProject_id(project.getProject_id());
						imgObj.setCreated_by(project.getCreated_by());
						imgObj.setCreated_date(project.getCreated_date());
						paramSource = new BeanPropertySqlParameterSource(imgObj);
						template.update(galleryQry, paramSource);
					}
				}
				
				/************************************************************************************************************************************/
				
				
				
				arraySize = 0;
				if (!StringUtils.isEmpty(project.getProjectFileNames()) && project.getProjectFileNames().length > 0) {
					project.setProjectFileNames(CommonMethods.replaceEmptyByNullInSringArray(project.getProjectFileNames()));
					if (arraySize < project.getProjectFileNames().length) {
						arraySize = project.getProjectFileNames().length;
					}
				}

				if (!StringUtils.isEmpty(project.getProject_file_types()) && project.getProject_file_types().length > 0) {
					project.setProject_file_types(CommonMethods.replaceEmptyByNullInSringArray(project.getProject_file_types()));
					if (arraySize < project.getProject_file_types().length) {
						arraySize = project.getProject_file_types().length;
					}
				}
				
				if (!StringUtils.isEmpty(project.getProject_file_ids()) && project.getProject_file_ids().length > 0) {
					project.setProject_file_ids(CommonMethods.replaceEmptyByNullInSringArray(project.getProject_file_ids()));
					if (arraySize < project.getProject_file_ids().length) {
						arraySize = project.getProject_file_ids().length;
					}
				}
				
				String file_ids = "";
				for (int i = 0; i < arraySize; i++) {
					if(!StringUtils.isEmpty(project.getProject_file_ids()) && project.getProject_file_ids().length > 0 && !StringUtils.isEmpty(project.getProject_file_ids()[i])) {
						file_ids = file_ids + project.getProject_file_ids()[i] + ",";
					}
				}
				
				if (!StringUtils.isEmpty(file_ids)) {			
					file_ids = org.apache.commons.lang3.StringUtils.chop(file_ids);

					String deleteFilesQry = "delete from project_files where id not in("+file_ids+") and project_id_fk = :project_id";
					Project fileObj = new Project();
					fileObj.setProject_id(project.getProject_id());
					paramSource = new BeanPropertySqlParameterSource(fileObj);
					template.update(deleteFilesQry, paramSource);
				}

				String insertFileQry = "INSERT INTO project_files (attachment,project_id_fk,project_file_type_fk,created_date)VALUES(:attachment,:project_id,:project_file_type_fk,CURRENT_TIMESTAMP)";
				String updateFileQry = "UPDATE project_files set attachment=:attachment,project_id_fk=:project_id,project_file_type_fk=:project_file_type_fk WHERE id=:project_file_id";
				
				for (int i = 0; i < arraySize; i++) {
					MultipartFile multipartFile = project.getProjectFiles()[i];
					if ((null != multipartFile && !multipartFile.isEmpty())
							|| !StringUtils.isEmpty(project.getProjectFileNames()) && project.getProjectFileNames().length > 0 && !StringUtils.isEmpty(project.getProjectFileNames()[i])) {
						String saveDirectory = CommonConstants.PROJECT_FILE_SAVING_PATH + project.getProject_id() + File.separator;
						String fileName = project.getProjectFileNames()[i];
						String file_id = project.getProject_file_ids()[i];
						if (null != multipartFile && !multipartFile.isEmpty()) {
							FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
						}
						Project fObj = new Project();
						fObj.setAttachment(fileName);
						fObj.setProject_file_type_fk(project.getProject_file_types()[i]);
						fObj.setProject_file_id(file_id);
						fObj.setProject_id(project.getProject_id());
						paramSource = new BeanPropertySqlParameterSource(fObj);
						if(!StringUtils.isEmpty(file_id)) {
							template.update(updateFileQry, paramSource);
						}else {
							template.update(insertFileQry, paramSource);
						}
					}
				}
				
				/********************************************************************************************************************************************/
				
				arraySize = 0;
				if(!StringUtils.isEmpty(project.getFinancial_years()) && project.getFinancial_years().length > 0 ) {
					project.setFinancial_years(CommonMethods.replaceEmptyByNullInSringArray(project.getFinancial_years()));
					if(arraySize < project.getFinancial_years().length) {
						arraySize = project.getFinancial_years().length;
					}
				}
				if(!StringUtils.isEmpty(project.getRailways()) && project.getRailways().length > 0 ) {
					project.setRailways(CommonMethods.replaceEmptyByNullInSringArray(project.getRailways()));
					if(arraySize < project.getRailways().length) {
						arraySize = project.getRailways().length;
					}
				}
				if(!StringUtils.isEmpty(project.getPink_book_item_numbers()) && project.getPink_book_item_numbers().length > 0 ) {
					project.setPink_book_item_numbers(CommonMethods.replaceEmptyByNullInSringArray(project.getPink_book_item_numbers()));
					if(arraySize < project.getPink_book_item_numbers().length) {
						arraySize = project.getPink_book_item_numbers().length;
					}
				}
				
				if (arraySize > 0) {
					String pinkbookDeleteQry ="delete from project_pinkbook where project_id_fk = :project_id ";
					Project obj = new Project();
					obj.setProject_id(project.getProject_id());
					paramSource = new BeanPropertySqlParameterSource(obj);
					template.update(pinkbookDeleteQry, paramSource);
				}
				
	
				String pinkbookQry = "INSERT into project_pinkbook (project_id_fk,financial_year_fk,pb_item_no) VALUES (:project_id, :financial_year_fk, :pb_item_no)";
				if (arraySize > 0) {
				    String[] financialYears = project.getFinancial_years();
				    String[] pinkBookItems = project.getPink_book_item_numbers();
				    String[] railways = project.getRailways();

				    for (int i = 0; i < arraySize; i++) {
				        String fy = (financialYears != null && i < financialYears.length) ? financialYears[i] : null;
				        String pbItem = (pinkBookItems != null && i < pinkBookItems.length) ? pinkBookItems[i] : null;
				        String railway = (railways != null && i < railways.length) ? railways[i] : null;

				        if (!StringUtils.isEmpty(fy) || !StringUtils.isEmpty(pbItem)) {
				            String pb_item_no = pbItem;

				            if (!StringUtils.isEmpty(railway) && !StringUtils.isEmpty(pbItem)) {
				                pb_item_no = railway + "-" + pbItem;
				            } else if (!StringUtils.isEmpty(railway) && StringUtils.isEmpty(pbItem)) {
				                pb_item_no = railway + "-";
				            } else if (StringUtils.isEmpty(railway) && !StringUtils.isEmpty(pbItem)) {
				                pb_item_no = pbItem;
				            }

				            Project obj = new Project();
				            obj.setProject_id(project.getProject_id());
				            obj.setFinancial_year_fk(fy);
				            obj.setPb_item_no(pb_item_no);

				            paramSource = new BeanPropertySqlParameterSource(obj);
				            template.update(pinkbookQry, paramSource);
				        }
				    }
				}				
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}	
		return flag;	
	}

	@Override
	public boolean addProject(Project project)throws Exception{
		int count = 0;
		boolean flag = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		try{
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
			String projectId = null;	
			
			String maxIdQry = 
				    "SELECT CASE " +
				    "    WHEN COUNT(*) = 0 THEN 'P01' " +
				    "    ELSE CONCAT('P', RIGHT('0' + CAST(MAX(CAST(SUBSTRING(project_id, 2, LEN(project_id) - 1) AS INT)) + 1 AS VARCHAR), 2)) " +
				    "END AS project_id " +
				    "FROM project";			
			
			Project pId = template.queryForObject(maxIdQry,new MapSqlParameterSource(), BeanPropertyRowMapper.newInstance(Project.class));
			if(StringUtils.isEmpty(pId)) {
				projectId = "P01";
			}else {
				projectId = pId.getProject_id();
			}
			project.setProject_id(projectId);  

			String qry = "INSERT INTO project (" +
				    "project_id, project_name, plan_head_number, remarks, " +
				    "project_description, project_status, attachment, benefits, " +
				    "project_type_id_fk, railway_zone, sanctioned_year, sanctioned_amount, " +
				    "sanctioned_commissioning_date, date_of_sanction, division_id, section_id, " +
				    "actual_completion_cost, actual_completion_date, " +
				    "structure_details, from_chainage, to_chainage" +
				") VALUES (" +
				    ":project_id, :project_name, :plan_head_number, :remarks, " +
				    ":project_description, :project_status, :attachment, :benefits, " +
				    ":project_type_id, :railway_zone, :sanctioned_year, :sanctioned_amount, " +
				    ":sanctioned_commissioning_date, :date_of_sanction, :division_id, :section_id, " +
				    ":actual_completion_cost, :actual_completion_date, " +
				    ":structure_details, :from_chainage, :to_chainage" +
				")";

			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(project);

			count = template.update(qry, paramSource);
			if(count > 0 ){
				flag = true; 
			}
			if(flag) {
				int arraySize = 0;

			    // Sanitize & update arrays
			    if (!StringUtils.isEmpty(project.getCompletion_dates()) && project.getCompletion_dates().length > 0) {
			        project.setCompletion_dates(CommonMethods.replaceEmptyByNullInSringArray(project.getCompletion_dates()));
			        arraySize = Math.max(arraySize, project.getCompletion_dates().length);
			    }

			    if (!StringUtils.isEmpty(project.getEstimated_completion_costs()) && project.getEstimated_completion_costs().length > 0) {
			        project.setEstimated_completion_costs(CommonMethods.replaceEmptyByNullInSringArray(project.getEstimated_completion_costs()));
			        arraySize = Math.max(arraySize, project.getEstimated_completion_costs().length);
			    }

			    if (!StringUtils.isEmpty(project.getRevised_completion_dates()) && project.getRevised_completion_dates().length > 0) {
			        project.setRevised_completion_dates(CommonMethods.replaceEmptyByNullInSringArray(project.getRevised_completion_dates()));
			        arraySize = Math.max(arraySize, project.getRevised_completion_dates().length);
			    }

			    String estimateQry = "INSERT INTO project_estimates (project_id_fk, completion_date, estimated_completion_cost, revised_completion_date, created_date) " +
			                         "VALUES (:project_id_fk, :completion_date, :estimated_completion_cost, :revised_completion_date, GETDATE())";

			    for (int i = 0; i < arraySize; i++) {
			        Project estimateObj = new Project();

			        estimateObj.setProject_id_fk(projectId);
			        estimateObj.setCompletion_date(DateParser.parse(project.getCompletion_dates()[i]));
			        estimateObj.setEstimated_completion_cost(project.getEstimated_completion_costs()[i]);
			        estimateObj.setRevised_completion_date(DateParser.parse(project.getRevised_completion_dates()[i]));

			        paramSource = new BeanPropertySqlParameterSource(estimateObj);
			        template.update(estimateQry, paramSource);
			    }
				
				/************************************************************************************************************************************/
				
				
				
				arraySize = 0;
				if (!StringUtils.isEmpty(project.getProjectFileNames()) && project.getProjectFileNames().length > 0) {
					project.setProjectFileNames(CommonMethods.replaceEmptyByNullInSringArray(project.getProjectFileNames()));
					if (arraySize < project.getProjectFileNames().length) {
						arraySize = project.getProjectFileNames().length;
					}
				}

				if (!StringUtils.isEmpty(project.getProject_file_types()) && project.getProject_file_types().length > 0) {
					project.setProject_file_types(CommonMethods.replaceEmptyByNullInSringArray(project.getProject_file_types()));
					if (arraySize < project.getProject_file_types().length) {
						arraySize = project.getProject_file_types().length;
					}
				}
				
				if (!StringUtils.isEmpty(project.getProject_file_ids()) && project.getProject_file_ids().length > 0) {
					project.setProject_file_ids(CommonMethods.replaceEmptyByNullInSringArray(project.getProject_file_ids()));
					if (arraySize < project.getProject_file_ids().length) {
						arraySize = project.getProject_file_ids().length;
					}
				}

				String insertFileQry = "INSERT INTO project_files (attachment,project_id_fk,project_file_type_fk,created_date)VALUES(:attachment,:project_id,:project_file_type_fk,CURRENT_TIMESTAMP)";
				
				for (int i = 0; i < arraySize; i++) {
					MultipartFile multipartFile = project.getProjectFiles()[i];
					if ((null != multipartFile && !multipartFile.isEmpty())) {
						String saveDirectory = CommonConstants.PROJECT_FILE_SAVING_PATH + projectId + File.separator;
						String fileName = project.getProjectFileNames()[i];
						if (null != multipartFile && !multipartFile.isEmpty()) {
							FileUploads.singleFileSaving(multipartFile, saveDirectory, fileName);
						}
						Project fileObj = new Project();
						fileObj.setAttachment(fileName);
						fileObj.setProject_file_type_fk((project.getProject_file_types().length > 0)?project.getProject_file_types()[i]:null);
						fileObj.setProject_id(projectId);
						paramSource = new BeanPropertySqlParameterSource(fileObj);
						template.update(insertFileQry, paramSource);
					}
				}
				
				/********************************************************************************************************************************************/
				
				
				arraySize = 0;
				if(!StringUtils.isEmpty(project.getFinancial_years()) && project.getFinancial_years().length > 0 ) {
					project.setFinancial_years(CommonMethods.replaceEmptyByNullInSringArray(project.getFinancial_years()));
					if(arraySize < project.getFinancial_years().length) {
						arraySize = project.getFinancial_years().length;
					}
				}
				
				if(!StringUtils.isEmpty(project.getRailways()) && project.getRailways().length > 0 ) {
					project.setRailways(CommonMethods.replaceEmptyByNullInSringArray(project.getRailways()));
					if(arraySize < project.getRailways().length) {
						arraySize = project.getRailways().length;
					}
				}
				
				if(!StringUtils.isEmpty(project.getPink_book_item_numbers()) && project.getPink_book_item_numbers().length > 0 ) {
					project.setPink_book_item_numbers(CommonMethods.replaceEmptyByNullInSringArray(project.getPink_book_item_numbers()));
					if(arraySize < project.getPink_book_item_numbers().length) {
						arraySize = project.getPink_book_item_numbers().length;
					}
				}
				
				String pinkbookQry = "INSERT into project_pinkbook (project_id_fk,financial_year_fk,pb_item_no) VALUES (:project_id, :financial_year_fk, :pb_item_no)";
				if (arraySize > 0) {
				    String[] financialYears = project.getFinancial_years();
				    String[] pinkBookItems = project.getPink_book_item_numbers();
				    String[] railways = project.getRailways();

				    for (int i = 0; i < arraySize; i++) {
				        String fy = (financialYears != null && i < financialYears.length) ? financialYears[i] : null;
				        String pbItem = (pinkBookItems != null && i < pinkBookItems.length) ? pinkBookItems[i] : null;
				        String railway = (railways != null && i < railways.length) ? railways[i] : null;

				        if (!StringUtils.isEmpty(fy) || !StringUtils.isEmpty(pbItem)) {
				            String pb_item_no = pbItem;

				            if (!StringUtils.isEmpty(railway) && !StringUtils.isEmpty(pbItem)) {
				                pb_item_no = railway + "-" + pbItem;
				            } else if (!StringUtils.isEmpty(railway) && StringUtils.isEmpty(pbItem)) {
				                pb_item_no = railway + "-";
				            } else if (StringUtils.isEmpty(railway) && !StringUtils.isEmpty(pbItem)) {
				                pb_item_no = pbItem;
				            }

				            Project obj = new Project();
				            obj.setProject_id(project.getProject_id());
				            obj.setFinancial_year_fk(fy);
				            obj.setPb_item_no(pb_item_no);

				            paramSource = new BeanPropertySqlParameterSource(obj);
				            template.update(pinkbookQry, paramSource);
				        }
				    }
				}

			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}
		return flag;
		
	}
	private String getProjectId(Connection con) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String projectId = null;;
		try{
			String maxIdQry = "SELECT CONCAT(SUBSTRING(project_id, 1, LEN(project_id)-2),SUBSTRING(cast(MAX(SUBSTRING(project_id, 2, LEN(project_id)))+1 as varchar),0,2) ) AS maxId FROM project";
			stmt = con.prepareStatement(maxIdQry);
			rs = stmt.executeQuery();  
			if(rs.next()) {
				projectId = rs.getString("maxId");
				if(StringUtils.isEmpty(projectId)) {
					projectId = "P01";
				}
			}
		}catch(Exception e){ 		
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(null, stmt, rs);
		}
		return projectId;
	}


	@Override
	public boolean deleteProject(String projectId, Project project)throws Exception{
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean flag = false;
		
		try{  
			con = dataSource.getConnection();
			String qry = "delete from project where project_id = ?";
			stmt = con.prepareStatement(qry);
			stmt.setString(1, projectId);
			int c = stmt.executeUpdate();  
			if(c > 0) {
				flag = true;
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, rs);
		}
		return flag;
	}


	@Override
	public List<Project> getFileNames(String projectId) throws Exception {
		List<Project> objsList = null;
		try {
			String qry ="SELECT id, file_name, project_id_fk, created_date, created_by FROM project_gallery where project_id_fk = ? ";
			int arrSize = 1;
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = projectId;
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<Project>(Project.class));	
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}
	
	@Override
	public List<Project> getAllDivisionsForRailWayZone(String railwayZone) throws Exception {
		List<Project> objsList = null;
		try {
			String qry ="SELECT division_id, division_name FROM divisions where railway_zone = ? order by division_name";
			
			int arrSize = 1;
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = railwayZone;

				objsList = jdbcTemplate.query( qry, pValues ,new BeanPropertyRowMapper<Project>(Project.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}


	@Override
	public List<Year> getYearList()throws Exception{
		List<Year> objsList = null;
		try {
			String qry = "SELECT financial_year FROM financial_year";
			objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Year>(Year.class));
			
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}


	@Override
	public List<Project> getProjectPinkBookList() throws Exception {
		List<Project> objsList = null;
		try {
			String qry ="SELECT project_pinkbook_id, project_id_fk, financial_year_fk, pb_item_no FROM project_pinkbook pb "
					+ "LEFT JOIN project p on pb.project_id_fk = p.project_id order by project_id_fk ASC,financial_year_fk DESC,pb_item_no DESC";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Project>(Project.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}


	@Override
	public List<Project> getProjectFileTypes() throws Exception {
		List<Project> objsList = null;
		try {
			String qry ="SELECT project_file_type FROM project_file_type";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Project>(Project.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public List<Project> getProjectTypeDetails() throws Exception {
	    List<Project> objsList;
	    try {
	        String qry = "SELECT project_type_id, project_type_name FROM project_type ORDER BY project_type_name";
	        objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<>(Project.class));
	    } catch (Exception e) {
	        throw new Exception("Error fetching project type details", e);
	    }
	    return objsList;
	}


	@Override
	public List<Project> getRailwayZones() throws Exception {
	    List<Project> objsList;
	    try {
	        String qry = "SELECT railway_id, railway_name FROM railway";
	        objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<>(Project.class));
	    } catch (Exception e) {
	        throw new Exception("Error fetching railway zones", e);
	    }
	    return objsList;
	}




	@Override
	public List<Project> getAllDivisions() throws Exception {
		List<Project> objsList = null;
		try {
			String qry ="SELECT division_id, division_name FROM divisions";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Project>(Project.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

	


	@Override
	public List<Project> getAllSections() throws Exception {
		List<Project> objsList = null;
		try {
			String qry ="SELECT section_id, section_name, division_id FROM sections";
				objsList = jdbcTemplate.query( qry, new BeanPropertyRowMapper<Project>(Project.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}



	@Override
	public String[] uploadProjectChainagesData(List<Project> projectChainagesList, Project project) throws Exception {
	    int count = 0, row = 1, sheet = 1, subRow = 1, cnt = 0;
	    String errMsg = null;

	    TransactionDefinition def = new DefaultTransactionDefinition();

	    Connection con = null;
	    PreparedStatement stmt = null;

	    try {
	        NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	        con = dataSource.getConnection();

	        // Delete existing chainages
	        String deleteWorkChainageQry = "DELETE FROM chainages_master WHERE project_id = ?";
	        stmt = con.prepareStatement(deleteWorkChainageQry);
	        stmt.setString(1, project.getProject_id());
	        stmt.executeUpdate();
	        stmt.close(); // close early

	        // Insert new chainages
	        String insertQry = "INSERT INTO chainages_master (srno, project_id, chainages, latitude, longitude) " +
	                           "VALUES (:srno, :project_id, :chainages, :Latitude, :Longitude)";

	        for (Project obj : projectChainagesList) {
	            row++;
	            sheet = 1;

	            if (StringUtils.hasText(obj.getProject_id())) {
	                //obj.setProject_id(project.getProject_id()); // Ensure consistent project_id
	                SqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);
	                cnt += namedParamJdbcTemplate.update(insertQry, paramSource);
	            }
	        }

	        count = cnt;
	    } catch (Exception e) {
	        e.printStackTrace();
	        errMsg = e.getMessage();
	    } finally {
	        try {
	            if (stmt != null && !stmt.isClosed()) stmt.close();
	            if (con != null && !con.isClosed()) con.close();
	        } catch (Exception ex) {
	            ex.printStackTrace(); // Log but don’t override main exception
	        }
	    }

	    return new String[] {
	        errMsg,
	        String.valueOf(count),
	        String.valueOf(row),
	        String.valueOf(sheet),
	        String.valueOf(subRow)
	    };
	}




	@Override
	public boolean saveProjectChainagesDataUploadFile(Project obj) throws Exception {
		boolean flag = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		String work_data_id = null;
		Connection con = null;
		PreparedStatement stmt = null;
		
		try {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);	
			con = dataSource.getConnection();
			
			String deleteWorkChainageQry = "delete from work_chainages_upload_data where project_chainage_id = ?";
			stmt = con.prepareStatement(deleteWorkChainageQry); 
			stmt.setString(1,obj.getProject_id());
			int count1 = stmt.executeUpdate();
			if(stmt != null){stmt.close();}	
			
			
			String qry = "INSERT INTO work_chainages_upload_data"
					+ "(uploaded_file, status, remarks, uploaded_by_user_id_fk, uploaded_on,project_chainage_id) "
					+ "VALUES "
					+ "( :uploaded_file, :status, :remarks, :uploaded_by_user_id_fk,CURRENT_TIMESTAMP,:project_id)";	
			
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			KeyHolder keyHolder = new GeneratedKeyHolder();
		    int count = template.update(qry, paramSource, keyHolder);
			if(count > 0) {
				work_data_id = String.valueOf(keyHolder.getKey().intValue());
				obj.setProject_data_id(work_data_id);
				flag = true;
				
				MultipartFile file = obj.getProjectChainagesFile();
				if (null != file && !file.isEmpty() && file.getSize() > 0){
					String saveDirectory = CommonConstants.WORK_CHAINAGES_UPLOADED_FILE_SAVING_PATH ;
					String fileName = work_data_id + "_" +file.getOriginalFilename();
					FileUploads.singleFileSaving(file, saveDirectory, fileName);
					
					obj.setUploaded_file(fileName);
					String updateQry = "UPDATE work_chainages_upload_data set uploaded_file= :uploaded_file where project_chainage_id= :project_id ";
					BeanPropertySqlParameterSource paramSource1 = new BeanPropertySqlParameterSource(obj);		
					template.update(updateQry, paramSource1);
				}
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return flag;
	}
}
	
