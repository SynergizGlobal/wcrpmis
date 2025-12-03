package com.wcr.wcrbackend.repo;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.common.DBConnectionHandler;





@Repository
public class HomeDaoImpl implements HomeDao {
	Logger logger = Logger.getLogger(HomeDao.class);
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;


	
	@Override
	public List<String> getExecutionStatusList() throws Exception {
		List<String> objsList = new ArrayList<String>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			
			String qry = "select execution_status from execution_status where execution_status is not null and execution_status <> '' \r\n" + 
					"   ORDER BY case when execution_status='Not Started' then 1\r\n" + 
					"		    when execution_status='In Progress' then 2\r\n" + 
					"		    when execution_status='On Hold' then 3\r\n" + 
					"		    when execution_status='Commissioned' then 4\r\n" + 
					"		    when execution_status='Completed' then 5\r\n" + 
					"		    when execution_status='Dropped' then 6  end asc";
			statement = connection.prepareStatement(qry);
			resultSet = statement.executeQuery();  
			while(resultSet.next()) {
				objsList.add(resultSet.getString("execution_status").trim());
			}
		}catch(Exception e){ 
			throw new Exception(e);
		}
		finally {
			DBConnectionHandler.closeJDBCResoucrs(connection, statement, resultSet);
		}
		return objsList;
	}

}