package com.wcr.wcrbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Report;
import com.wcr.wcrbackend.service.IReportAccessService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/report-access")
public class ReportAccessController {
	
	Logger logger = Logger.getLogger(ReportAccessController.class);
	
	@Autowired
	private IReportAccessService service;
	
	@PostMapping(value = "/ajax/get-reports-list")
	public List<Report> getReportsList(@RequestBody Report obj) {
		List<Report> objList = null;
		try {
			objList = service.getReportsList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getReportsList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getModulesFilterListInReport")
	public List<Report> getModulesList(@RequestBody Report obj) {
		List<Report> objList = null;
		try {
			objList = service.getModulesFilterListInReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getModulesList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getStatusFilterListInReport")
	public List<Report> getStatusList(@RequestBody Report obj) {
		List<Report> objList = null;
		try {
			objList = service.getStatusFilterListInReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStatusList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getUserRolesInReportAccess")
	public List<Report> getUserRolesInDashboardAccess(@RequestBody Report obj) {
		List<Report> objsList = null;
		try {
			objsList = service.getUserRolesInReportAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserRolesInReportAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getUserTypesInReportAccess")
	public List<Report> getUserTypesInReportAccess(@RequestBody Report obj) {
		List<Report> objsList = null;
		try {
			objsList = service.getUserTypesInReportAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserTypesInReportAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getUsersInReportAccess")
	public List<Report> getUsersInReportAccess(@RequestBody Report obj) {
		List<Report> objsList = null;
		try {
			objsList = service.getUsersInReportAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUsersInReportAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value="/ajax/form/get-access-report")
	public Map<String, List<Report>> getAccessReportDetails(@RequestBody Report obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		Map<String, List<Report>> model = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditReportAccessFormNew);
			
			List<Report> modulesList = service.getModulesListForReportAccess(obj);
			model.put("modulesList", modulesList);
			
			List<Report> foldersList = service.getFolderssListForReportAccess(obj);
			model.put("foldersList", foldersList);
			
			List<Report> statusList = service.getStatusListForReportAccess(obj);
			model.put("statusList", statusList);
			
			List<Report> user_roles = service.getUserRolesInReportAccess(obj);
			model.put("user_roles", user_roles);
			
			List<Report> user_types = service.getUserTypesInReportAccess(obj);
			model.put("user_types", user_types);
			
			List<Report> users = service.getUsersInReportAccess(obj);
			model.put("users", users);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getAccessReportDetails : " + e.getMessage());
		}
		return model;
	}
	
	@PostMapping(value="/ajax/form/get-access-report/details")
	public Map<String, Report> getAccessReportDetails2(@RequestBody Report obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		Map<String, Report> model = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditReportAccessFormNew);
			
			Report reportDetails = service.getReport(obj);
			model.put("reportDetails", reportDetails);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getAccessReportDetails : " + e.getMessage());
		}
		return model;
	}
	
	@PostMapping(value = "/update-access-report")
	public ResponseEntity<?> updateAccessReport(@RequestBody Report obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		String attributeKey = "";
		String attributeMsg = "";
		try{
			String user_Id = (String) session.getAttribute("USER_ID");String userName = (String) session.getAttribute("USER_NAME");
			
			//model.setViewName("redirect:/access-reports");
			boolean flag =  service.updateAccessReport(obj);
			if(flag) {
				attributeKey = "success";
				attributeMsg = "Report Updated Succesfully.";
			}
			else {
				attributeKey = "error";
				attributeMsg = "Updating Report is failed. Try again.";
			}
		}catch (Exception e) {
			attributeKey = "error";
			attributeMsg = "Updating Report is failed. Try again.";
			//attributes.addFlashAttribute("error","Updating Report is failed. Try again.");
			logger.error("updateAccessReport : " + e.getMessage());
		}
		return ResponseEntity.ok(Map.of(attributeKey,attributeMsg));
	}
}
