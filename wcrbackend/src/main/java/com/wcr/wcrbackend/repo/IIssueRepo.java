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

}
