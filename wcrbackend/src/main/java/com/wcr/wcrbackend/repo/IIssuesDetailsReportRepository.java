package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Issue;


public interface IIssuesDetailsReportRepository {
	
	public Issue getIssue(Issue obj) throws Exception;

	public List<Issue> getIssueHistory(Issue obj) throws Exception;


}
