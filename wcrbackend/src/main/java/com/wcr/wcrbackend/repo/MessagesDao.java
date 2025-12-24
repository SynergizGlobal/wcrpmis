package com.wcr.wcrbackend.repo;

import java.util.Arrays;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Messages;



@Repository
public class MessagesDao {
	
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	JdbcTemplate jdbcTemplate ;


	public boolean addMessages(Messages obj,NamedParameterJdbcTemplate template) throws Exception {
		boolean flag = false;
		try {
			if(!StringUtils.isEmpty(obj.getUser_ids())) {
				String[] user_ids = obj.getUser_ids();
				String[] filtered_user_ids = Arrays.stream(user_ids).distinct().toArray(s -> new String[s]);
				user_ids = filtered_user_ids;
				int count = user_ids.length;
				SqlParameterSource[] source = new SqlParameterSource[count];
				String messageQry = "INSERT INTO messages (message,user_id_fk,redirect_url,created_date,message_type)"
						+ "VALUES" + "(:message,:user_id_fk,:redirect_url,CURRENT_TIMESTAMP,:message_type)";
				
				for (int i = 0; i < count; i++) {
					Messages msgObj = new Messages();
					msgObj.setUser_id_fk(user_ids[i]);
					msgObj.setMessage(obj.getMessage());
					msgObj.setRedirect_url(obj.getRedirect_url());
					msgObj.setMessage_type(obj.getMessage_type());
			        source[i] = new BeanPropertySqlParameterSource(msgObj);
			    }
				template.batchUpdate(messageQry, source);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return flag;
	}
}

