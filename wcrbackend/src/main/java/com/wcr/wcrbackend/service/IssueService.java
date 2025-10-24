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

}
