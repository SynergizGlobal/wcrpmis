package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Issue;

public interface IIssueRepo {

	List<Issue> getIssuesCategoryList(Issue iObj) throws Exception;

	List<Issue> getIssuesPriorityList() throws Exception;

	List<Issue> getContractsListFilter(Issue obj) throws Exception;

	List<Issue> getDepartmentsListFilter(Issue obj) throws Exception;

	List<Issue> getCategoryListFilter(Issue obj) throws Exception;

	List<Issue> getResponsiblePersonsListFilter(Issue obj) throws Exception;

	List<Issue> getStatusListFilter(Issue obj) throws Exception;

	List<Issue> getHODListFilterInIssue(Issue obj) throws Exception;

	List<Issue> getIssuesList(Issue obj) throws Exception;

	List<Issue> getProjectsListForIssueForm(Issue obj) throws Exception;

	List<Issue> getContractsListForIssueForm(Issue obj) throws Exception;

	List<Issue> getIssuesStatusList() throws Exception;

	List<Issue> getIssueTitlesList(Issue obj) throws Exception;

	List<Issue> getDepartmentList() throws Exception;

	List<Issue> getRailwayList() throws Exception;

	List<Issue> getReportedByList() throws Exception;

	List<Issue> getResponsiblePersonList(Issue object) throws Exception;

	List<Issue> getEscalatedToList() throws Exception;

	List<Issue> getOtherOrganizationsList() throws Exception;

	List<Issue> getIssueFileTypes() throws Exception;

	List<Issue> getStructures(Issue obj) throws Exception;

	List<Issue> getComponents(Issue obj) throws Exception;

	List<Issue> getStructureListForIssue(Issue obj) throws Exception;

	List<Issue> getComponentListForIssue(Issue obj) throws Exception;

	Boolean addIssue(Issue obj) throws Exception;

	List<Issue> getActionTakens(Issue obj) throws Exception;

	Issue getIssue(Issue obj) throws Exception;

	boolean updateIssue(Issue obj) throws Exception;

	boolean readIssueMessage(String message_id) throws Exception;

}
