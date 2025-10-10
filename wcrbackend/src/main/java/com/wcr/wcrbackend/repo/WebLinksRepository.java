package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wcr.wcrbackend.DTO.WebLinks;

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

}
