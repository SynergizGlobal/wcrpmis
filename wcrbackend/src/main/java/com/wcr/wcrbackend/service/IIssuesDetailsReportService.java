package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Issue;

public interface IIssuesDetailsReportService {
	
	public Issue getIssue(Issue obj) throws Exception;

	public List<Issue> getIssueHistory(Issue obj) throws Exception;


}
