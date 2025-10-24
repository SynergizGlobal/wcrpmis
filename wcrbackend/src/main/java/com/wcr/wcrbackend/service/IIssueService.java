package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Issue;

public interface IIssueService {

	List<Issue> getIssuesCategoryList(Issue iObj) throws Exception;

	List<Issue> getIssuesPriorityList() throws Exception;

}
