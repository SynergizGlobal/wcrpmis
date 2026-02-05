package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.DBConnectionHandler;
import com.wcr.wcrbackend.reference.model.TrainingType;


@Repository
public class TemplateUploadRepository implements ITemplateUploadRepository{

	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;

	@Override
	public List<TrainingType> getTemplatesList() throws Exception {
		List<TrainingType> objsList = null;
		try {
			String qry ="select id, template_name, attachment, FORMAT(uploaded_on,'dd-MM-yyyy') AS uploaded_on, uploaded_by,u.user_name, status from upload_templates ut "
					+ "LEFT JOIN [user] u on ut.uploaded_by = u.user_id "
					+ " where status = ?";
			int arrSize = 1;
			Object[] pValues = new Object[arrSize];
			int i = 0;
			pValues[i++] = CommonConstants.ACTIVE;
			objsList = jdbcTemplate.query( qry,pValues, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));
			
			for (TrainingType session : objsList) {
				List<TrainingType> objsList1 = null;
				String qryDetails = "select id, template_name, attachment, FORMAT(uploaded_on,'dd-MM-yyyy') AS uploaded_on,u.user_name, uploaded_by, status " + 
						"from upload_templates ut "
						+ "LEFT JOIN [user] u on ut.uploaded_by = u.user_id "
						+"where  template_name = ? and status = ? ORDER BY uploaded_on desc";
				
				objsList1 = jdbcTemplate.query(qryDetails, new Object[] {session.getTemplate_name(),CommonConstants.INACTIVE}, new BeanPropertyRowMapper<TrainingType>(TrainingType.class));	
				session.setTableHistoryList(objsList1); 
			}
		}catch(Exception e){ 
		throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public boolean uploadTemplate(TrainingType obj) throws Exception {
		boolean flag = false;
		int count = 0;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dataSource.getConnection();
			NamedParameterJdbcTemplate namedParamJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

			String updateReferenceTableQry =
				    "UPDATE upload_templates " +
				    "SET status = ?, attachment = ? " +
				    "WHERE template_name = ? AND status = ?";		
			stmt = con.prepareStatement(updateReferenceTableQry);

			stmt.setString(1, CommonConstants.INACTIVE);    
			stmt.setString(2, obj.getAttachment());         
			stmt.setString(3, obj.getTemplate_name());      
			stmt.setString(4, CommonConstants.ACTIVE);       

			stmt.executeUpdate();
			stmt.close();
			if(stmt != null){stmt.close();}	
			obj.setStatus(CommonConstants.ACTIVE);
			String insertQry =
				    "INSERT INTO upload_templates " +
				    "(template_name, attachment, uploaded_on, uploaded_by, status) " +
				    "VALUES (:template_name, :commonAttachment, :uploaded_on, :uploaded_by, :status)";			
			BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(obj);		 
			 count = namedParamJdbcTemplate.update(insertQry, paramSource);			
			if(count > 0) {
				flag = true;
			}
		}catch(Exception e){ 
			e.printStackTrace();
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(con, null, null);
		}	
		return flag;
	}

	@Override
	public boolean deleteTemplate(TrainingType obj) throws Exception {
		boolean flag = false;
		Connection con = null;
		PreparedStatement stmt = null;
		int count = 0;
		try {
			con = dataSource.getConnection();
			String deleteQry = "UPDATE upload_templates SET status = ? where id= ? ";
			stmt = con.prepareStatement(deleteQry);
			stmt.setString(1,CommonConstants.INACTIVE);
			stmt.setString(2,obj.getId());
			count = stmt.executeUpdate();
			if(count > 0) {
				flag = true;
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, null);
		}
		return flag;
	}
	
}