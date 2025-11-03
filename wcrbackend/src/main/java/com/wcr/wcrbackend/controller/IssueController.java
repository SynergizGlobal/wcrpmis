package com.wcr.wcrbackend.controller;

import java.util.List;

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
}
