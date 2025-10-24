package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Issue;

public interface IIssueRepo {

	List<Issue> getIssuesCategoryList(Issue iObj) throws Exception;

	List<Issue> getIssuesPriorityList() throws Exception;

}
