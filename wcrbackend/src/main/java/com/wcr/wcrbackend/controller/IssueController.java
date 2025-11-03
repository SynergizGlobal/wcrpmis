package com.wcr.wcrbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IIssueService;
import com.wcr.wcrbackend.service.IUserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/issue")
public class IssueController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IIssueService issueService;
	
	Logger logger = Logger.getLogger(IssueController.class);
	
	@PostMapping(value = "/ajax/getContractsListFilterInIssue")
	public List<Issue> getContractsListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getContractsListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractsListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getDepartmentsListFilterInIssue")
	public List<Issue> getDepartmentsListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getDepartmentsListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getDepartmentsListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getCategoryListFilterInIssue")
	public List<Issue> getCategoryListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getCategoryListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getCategoryListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	
	@PostMapping(value = "/ajax/getResponsiblePersonsListFilterInIssue")
	public List<Issue> getResponsiblePersonsListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getResponsiblePersonsListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getResponsiblePersonsListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getStatusListFilterInIssue")
	public List<Issue> getStatusListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getStatusListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getStatusListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getHODListFilterInIssue")
	public List<Issue> getHODListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getHODListFilterInIssue(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getHODListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getIssuesList")
	public List<Issue> getIssuesList(@RequestBody Issue obj,HttpSession session) {
		List<Issue> issues = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			issues = issueService.getIssuesList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssuesList : " + e.getMessage());
		}
		return issues;
	}
	
	@PostMapping(value = "/ajax/form/add-issue-form")
	public Map<String, List<Issue>> addIssueForm(HttpSession session,@RequestBody Issue obj) {
		Map<String, List<Issue>> map = new HashMap<>();
		
		List<Issue> issues = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			List<Issue> projectsList = issueService.getProjectsListForIssueForm(obj);
			map.put("projectsList", projectsList);
			List<Issue> contractsList = issueService.getContractsListForIssueForm(obj);
			map.put("contractsList", contractsList);
			List<Issue> issuesStatusList = issueService.getIssuesStatusList();
			map.put("issuesStatusList", issuesStatusList);
			List<Issue> issuesPriorityList = issueService.getIssuesPriorityList();
			map.put("issuesPriorityList", issuesPriorityList);
			List<Issue> issuesCategoryList = issueService.getIssuesCategoryList(obj);
			map.put("issuesCategoryList", issuesCategoryList);
			List<Issue> issueTitlesList = issueService.getIssueTitlesList(obj);
			map.put("issueTitlesList", issueTitlesList);
			List<Issue> departmentList = issueService.getDepartmentList();
			map.put("departmentList", departmentList);
			List<Issue> railwayList = issueService.getRailwayList();
			map.put("railwayList", railwayList);
			List<Issue> reportedByList = issueService.getReportedByList();
			map.put("reportedByList", reportedByList);
			List<Issue> responsiblePersonList = issueService.getResponsiblePersonList(null);
			map.put("responsiblePersonList", responsiblePersonList);
			List<Issue> escalatedToList = issueService.getEscalatedToList();
			map.put("escalatedToList", escalatedToList);
			List<Issue> otherOrganizations = issueService.getOtherOrganizationsList();
			map.put("otherOrganizations", otherOrganizations);
			List<Issue> issueFileTypes = issueService.getIssueFileTypes();
			map.put("issueFileTypes", issueFileTypes);
			List<Issue> structures = issueService.getStructures(obj);
			map.put("structures", structures);
			List<Issue> components = issueService.getComponents(obj);
			map.put("components", components);	
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssuesList : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value = "/ajax/getIssueTitlesListForIssuesForm")
	public List<Issue> getIssueTitlesListForIssuesForm(@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getIssueTitlesList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssueTitlesListForIssuesForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getStructureListForIssue")
	public List<Issue> getStructureListForIssue(@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getStructureListForIssue(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureListForIssue : " + e.getMessage());
		}
		return objsList;
	}	
	
	@PostMapping(value = "/ajax/getComponentListForIssue")
	public List<Issue> getComponentListForIssue(@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getComponentListForIssue(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getComponentListForIssue : " + e.getMessage());
		}
		return objsList;
	}	
	
	@PostMapping(value = "/ajax/getIssueCategoryListForIssuesForm")
	public List<Issue> getIssueCategoryListForIssuesForm(@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getIssuesCategoryList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssueCategoryListForIssuesForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getProjectsListForIssuesForm")
	public List<Issue> getProjectsListForIssueForm(HttpSession session,@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objsList = issueService.getProjectsListForIssueForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getProjectsListForIssueForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getContractsListForIssuesForm")
	public List<Issue> getContractsListForIssueForm(HttpSession session,@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			objsList = issueService.getContractsListForIssueForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractsListForIssueForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getIssueStatusListForIssuesForm")
	public List<Issue> getIssueStatusListForIssuesForm() {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getIssuesStatusList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssueStatusListForIssuesForm : " + e.getMessage());
		}
		return objsList;
	}
}
