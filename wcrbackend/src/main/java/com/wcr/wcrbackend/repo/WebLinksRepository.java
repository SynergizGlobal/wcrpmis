package com.wcr.wcrbackend.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.WebLinks;
import com.wcr.wcrbackend.common.CommonMethods;
import com.wcr.wcrbackend.common.DBConnectionHandler;

@Repository
public class WebLinksRepository implements IWebLinksRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<WebLinks> getWebLinks() {
		List<WebLinks> objsList = null;
		String qry = "select id,name,link,priority from web_links ORDER BY priority ASC";
		objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<WebLinks>(WebLinks.class));
		return objsList;
	}

	@Override
	public List<WebLinks> getWebLinks(WebLinks obj) throws Exception {
		List<WebLinks> objsList = null;
		try {
			String qry = "select id,name,link,priority from web_links ORDER BY priority ASC" ;
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<WebLinks>(WebLinks.class));
		}catch(Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

	@Override
	public boolean updateWebLinks(WebLinks obj) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		boolean flag = false;
		try{
			con = jdbcTemplate.getDataSource().getConnection();
			con.setAutoCommit(false);
			
			if(!StringUtils.isEmpty(obj.getRemovableWebLinkIds())) {
				String qryDelete = "delete from web_links where id = ?";
				stmt = con.prepareStatement(qryDelete); 		
				String[] tempArr = obj.getRemovableWebLinkIds().split(",");
				for (int i = 0; i < tempArr.length; i++) {
					stmt.setString(1,tempArr[i]);
					stmt.addBatch();
				}
				stmt.executeBatch();
				DBConnectionHandler.closeJDBCResoucrs(null, stmt, null);
			}
			
			int arraySize = 0;
			
			if(!StringUtils.isEmpty(obj.getNames()) && obj.getNames().length > 0) {
				obj.setNames(CommonMethods.replaceEmptyByNullInSringArraySecond(obj.getNames()));
				if(arraySize < obj.getNames().length) {
					arraySize = obj.getNames().length;
				}
			}
			if(!StringUtils.isEmpty(obj.getLinks()) && obj.getLinks().length > 0) {
				obj.setLinks(CommonMethods.replaceEmptyByNullInSringArraySecond(obj.getLinks()));
				if(arraySize < obj.getLinks().length) {
					arraySize = obj.getLinks().length;
				}
			}
			if(!StringUtils.isEmpty(obj.getPrioritys()) && obj.getPrioritys().length > 0) {
				obj.setPrioritys(CommonMethods.replaceEmptyByNullInSringArraySecond(obj.getPrioritys()));
				if(arraySize < obj.getPrioritys().length) {
					arraySize = obj.getPrioritys().length;
				}
			}
			if(!StringUtils.isEmpty(obj.getIds()) && obj.getIds().length > 0) {
				obj.setIds(CommonMethods.replaceEmptyByNullInSringArraySecond(obj.getIds()));
				if(arraySize < obj.getIds().length) {
					arraySize = obj.getIds().length;
				}
			}
					
			String insertQry = "INSERT into web_links (name,link,priority) VALUES (?,?,?)";
			stmtInsert = con.prepareStatement(insertQry);
			
			String updateQry = "update web_links set name = ?,link = ?,priority = ? where id = ?";
			stmtUpdate = con.prepareStatement(updateQry);
			
			for (int i = 0; i < arraySize; i++) {
				int p = 1;
				if(!StringUtils.isEmpty(obj.getIds()[i])) {
					stmtUpdate.setString(p++,(obj.getNames().length > 0)?obj.getNames()[i]:null);
					stmtUpdate.setString(p++,(obj.getLinks().length > 0)?obj.getLinks()[i]:null);
					stmtUpdate.setString(p++,(obj.getPrioritys().length > 0)?obj.getPrioritys()[i]:null);
					stmtUpdate.setString(p++,(obj.getIds().length > 0)?obj.getIds()[i]:null);
					stmtUpdate.addBatch();
				}else {
					stmtInsert.setString(p++,(obj.getNames().length > 0)?obj.getNames()[i]:null);
					stmtInsert.setString(p++,(obj.getLinks().length > 0)?obj.getLinks()[i]:null);
					stmtInsert.setString(p++,(obj.getPrioritys().length > 0)?obj.getPrioritys()[i]:null);
					stmtInsert.addBatch();
				}
				
			}
			int[] insertCount = stmtInsert.executeBatch();
			int[] updateCount = stmtUpdate.executeBatch();
			if(insertCount.length > 0 || updateCount.length > 0) {
				flag = true;
			}
			con.commit();
		}catch(Exception e){ 
			con.rollback();
			e.printStackTrace();
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(con, stmt, null);
		}		
		return flag;
	}

}
