package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.repo.IIssueRepo;

import jakarta.transaction.Transactional;

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

	@Override
	public List<Issue> getProjectsListForIssueForm(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getProjectsListForIssueForm(obj);
	}

	@Override
	public List<Issue> getContractsListForIssueForm(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getContractsListForIssueForm(obj);
	}

	@Override
	public List<Issue> getIssuesStatusList() throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getIssuesStatusList();
	}

	@Override
	public List<Issue> getIssueTitlesList(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getIssueTitlesList(obj);
	}

	@Override
	public List<Issue> getDepartmentList() throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getDepartmentList();
	}

	@Override
	public List<Issue> getRailwayList() throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getRailwayList();
	}

	@Override
	public List<Issue> getReportedByList() throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getReportedByList();
	}

	@Override
	public List<Issue> getResponsiblePersonList(Issue object) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getResponsiblePersonList(object);
	}

	@Override
	public List<Issue> getEscalatedToList() throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getEscalatedToList();
	}

	@Override
	public List<Issue> getOtherOrganizationsList() throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getOtherOrganizationsList();
	}

	@Override
	public List<Issue> getIssueFileTypes() throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getIssueFileTypes();
	}

	@Override
	public List<Issue> getStructures(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getStructures(obj);
	}

	@Override
	public List<Issue> getComponents(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getComponents(obj);
	}

	@Override
	public List<Issue> getStructureListForIssue(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getStructureListForIssue(obj);
	}

	@Override
	public List<Issue> getComponentListForIssue(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getComponentListForIssue(obj);
	}

	@Override
	@Transactional
	public Boolean addIssue(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.addIssue(obj);
	}

	@Override
	public List<Issue> getActionTakens(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getActionTakens(obj);
	}

	@Override
	public Issue getIssue(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.getIssue(obj);
	}

	@Override
	@Transactional
	public boolean updateIssue(Issue obj) throws Exception {
		// TODO Auto-generated method stub
		return issueRepo.updateIssue(obj);
	}

	@Override
	public boolean readIssueMessage(String message_id) throws Exception {
		return issueRepo.readIssueMessage(message_id);
	}

}
