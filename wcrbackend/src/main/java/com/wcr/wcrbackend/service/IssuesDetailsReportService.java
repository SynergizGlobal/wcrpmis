package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.repo.IssuesDetailsReportRepository;

@Service
public class IssuesDetailsReportService implements IIssuesDetailsReportService{
	
	@Autowired
	IssuesDetailsReportRepository repo;

	@Override
	public Issue getIssue(Issue obj) throws Exception {
		return repo.getIssue( obj);
	}

	@Override
	public List<Issue> getIssueHistory(Issue obj) throws Exception {
		return repo.getIssueHistory( obj);
	}

}
