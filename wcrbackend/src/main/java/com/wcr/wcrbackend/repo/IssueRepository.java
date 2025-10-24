package com.wcr.wcrbackend.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wcr.wcrbackend.DTO.Issue;
@Repository
public class IssueRepository implements IIssueRepo {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Issue> getIssuesCategoryList(Issue obj) throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "\r\n" + 
					"select distinct f.issue_category_fk as category,issues_related_to from issue_contarct_category f\r\n" + 
					"\r\n" + 
					"left join issue_category_title t on t.issue_category_fk=f.issue_category_fk  ";
			int arraSize = 0;
			if (!StringUtils.isEmpty(obj.getContract_type_fk())) {
				qry = qry + "where contract_category_fk = ? ";
				arraSize++;
			}
			qry = qry + "group by f.issue_category_fk,issues_related_to order by f.issue_category_fk ";
			Object[] pValues = new Object[arraSize];
			int i = 0;
			if (!StringUtils.isEmpty(obj.getContract_type_fk())) {
				pValues[i++] = obj.getContract_type_fk();
			}
			objsList = jdbcTemplate.query(qry, pValues, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList; 
	}

	@Override
	public List<Issue> getIssuesPriorityList() throws Exception {
		List<Issue> objsList = null;
		try {
			String qry = "select priority from issue_priority";
			objsList = jdbcTemplate.query(qry, new BeanPropertyRowMapper<Issue>(Issue.class));
		} catch (Exception e) {
			throw new Exception(e);
		}
		return objsList;
	}

}
