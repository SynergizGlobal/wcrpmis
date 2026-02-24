package com.wcr.wcrbackend.service;

import java.util.List;
import java.util.Map;

import com.wcr.wcrbackend.DTO.Issue;

public interface IIssuesReportService {

	List<Issue> getWorksListInIssuesReport(Issue obj) throws Exception;

	List<Issue> getContractsListInIssuesReport(Issue obj) throws Exception;
	
	List<Issue> getHODListInIssuesReport(Issue obj) throws Exception;
	
	Map<String,Map<String,List<Issue>>> getPendingIssues(Issue obj) throws Exception;

	String getEmailIdsOfHodDyHodManagement() throws Exception;

	List<Issue> getIssuesSummaryData(Issue obj) throws Exception;

	List<Issue> getStatusListInIssuesReport(Issue obj) throws Exception;

	List<Issue> getTitlesListInIssuesReport(Issue obj) throws Exception;

	List<Issue> getLocationsListInIssuesReport(Issue obj) throws Exception;

	List<Issue> getCategoriesListInIssuesReport(Issue obj) throws Exception;

	List<Issue> IssuesSummaryData(Issue obj) throws Exception;

	List<Issue> getUnresolvedIssues();

	boolean processAndSendReminder(Issue unresolvedIssue);

}
