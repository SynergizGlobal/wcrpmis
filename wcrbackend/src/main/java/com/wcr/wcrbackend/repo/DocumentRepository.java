package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.WebDocuments;

@Repository
public class DocumentRepository implements IDocumentRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<WebDocuments> getDocumentTypes() {
		String categoriesQry ="select type from web_documents_type";
		
		List<WebDocuments> objsList = jdbcTemplate.query( categoriesQry, new BeanPropertyRowMapper<WebDocuments>(WebDocuments.class));	
		for (WebDocuments doc : objsList) {
			if(!StringUtils.isEmpty(doc.getType())) {
				String documentType = doc.getType();
				String modified_type = documentType.replaceAll("-", " ").toLowerCase();
				doc.setModified_type("web-documents/"+modified_type);
			}
		}
		
		return objsList;
	}

}
