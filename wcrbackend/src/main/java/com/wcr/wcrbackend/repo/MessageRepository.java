package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.Messages;

@Repository
public class MessageRepository implements IMessageRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Messages> getMessages(Messages mObj) throws Exception{
		List<Messages> objsList = null;
		try {
			String qry ="select message_type "
					+ "from messages where user_id_fk = ? "
					+ "and (read_time is null or read_time > (GETDATE() - 3)) "
					+ "group by message_type order by message_type ASC";
			objsList = jdbcTemplate.query( qry,new Object[] {mObj.getUser_id_fk()}, new BeanPropertyRowMapper<Messages>(Messages.class));	
		}catch(Exception e){ 
			throw new Exception(e);
		}
		return objsList;
	}

}
