package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.repo.IIssueRepo;

@Service
public class IssueService implements IIssueService {

	@Autowired
	private IIssueRepo issueRepo;
	@Override
	public List<Issue> getIssuesCategoryList(Issue iObj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getIssuesCategoryList(iObj);
	}

	@Override
	public List<Issue> getIssuesPriorityList() throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getIssuesPriorityList();
	}

	@Override
	public List<Issue> getContractsListFilter(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getContractsListFilter(obj);
	}

	@Override
	public List<Issue> getDepartmentsListFilter(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getDepartmentsListFilter(obj);
	}

	@Override
	public List<Issue> getCategoryListFilter(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getCategoryListFilter(obj);
	}

	@Override
	public List<Issue> getResponsiblePersonsListFilter(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getResponsiblePersonsListFilter(obj);
	}

	@Override
	public List<Issue> getStatusListFilter(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getStatusListFilter(obj);
	}

	@Override
	public List<Issue> getHODListFilterInIssue(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getHODListFilterInIssue(obj);
	}

	@Override
	public List<Issue> getIssuesList(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getIssuesList(obj);
	}

}
