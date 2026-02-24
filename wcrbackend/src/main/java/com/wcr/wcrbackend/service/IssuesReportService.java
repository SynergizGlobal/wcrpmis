package com.wcr.wcrbackend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.repo.IssuesReportRepository;


@Service
public class IssuesReportService implements IIssuesReportService{
	
	@Autowired
	IssuesReportRepository issuesRepo;

	@Override
	public List<Issue> getWorksListInIssuesReport(Issue obj) throws Exception {
		return issuesRepo.getWorksListInIssuesReport(obj);
	}

	@Override
	public List<Issue> getContractsListInIssuesReport(Issue obj) throws Exception {
		return issuesRepo.getContractsListInIssuesReport(obj);
	}

	@Override
	public List<Issue> getHODListInIssuesReport(Issue obj) throws Exception {
		return issuesRepo.getHODListInIssuesReport(obj);
	}
	
	@Override
	public Map<String,Map<String,List<Issue>>> getPendingIssues(Issue obj) throws Exception {
		return issuesRepo.getPendingIssues(obj);
	}

	@Override
	public String getEmailIdsOfHodDyHodManagement() throws Exception {
		return issuesRepo.getEmailIdsOfHodDyHodManagement();
	}

	@Override
	public List<Issue> getIssuesSummaryData(Issue obj) throws Exception {
		return issuesRepo.getIssuesSummaryData(obj);
	}
	
	@Override
	public List<Issue> IssuesSummaryData(Issue obj) throws Exception {
		return issuesRepo.IssuesSummaryData(obj);
	}	

	@Override
	public List<Issue> getStatusListInIssuesReport(Issue obj) throws Exception {
		return issuesRepo.getStatusListInIssuesReport(obj);
	}

	@Override
	public List<Issue> getTitlesListInIssuesReport(Issue obj) throws Exception {
		return issuesRepo.getTitlesListInIssuesReport(obj);
	}

	@Override
	public List<Issue> getLocationsListInIssuesReport(Issue obj) throws Exception {
		return issuesRepo.getLocationsListInIssuesReport(obj);
	}

	@Override
	public List<Issue> getCategoriesListInIssuesReport(Issue obj) throws Exception {
		return issuesRepo.getCategoriesListInIssuesReport(obj);
	}

	@Override
	public boolean processAndSendReminder(Issue unresolvedIssue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Issue> getUnresolvedIssues() {
		return issuesRepo.getUnresolvedIssues();
	}

}
