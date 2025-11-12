package com.wcr.wcrbackend.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.DashboardObj;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IDashboardsAccessService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/dashboard-access")
public class DashboardsAccessController {
	
	Logger logger = Logger.getLogger(DashboardsAccessController.class);
	
	@Autowired
	private IDashboardsAccessService service;
	
	@PostMapping(value = "/ajax/get-dashboards-list")
	public List<DashboardObj> getDashboardsList(@RequestBody DashboardObj obj) {
		List<DashboardObj> objList = null;
		try {
			objList = service.getDashboardsList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDashboardsList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getModulesFilterListInDashboard")
	public List<DashboardObj> getModulesList(@RequestBody DashboardObj obj) {
		List<DashboardObj> objList = null;
		try {
			objList = service.getModulesFilterListInDashboard(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getModulesList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getDashboardTypesFilterListInDashboard")
	public List<DashboardObj> getDashboardTypesList(@RequestBody DashboardObj obj) {
		List<DashboardObj> objList = null;
		try {
			objList = service.getDashboardTypesFilterListInDashboard(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDashboardTypesList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getStatusFilterListInDashboard")
	public List<DashboardObj> getStatusList(@RequestBody DashboardObj obj) {
		List<DashboardObj> objList = null;
		try {
			objList = service.getStatusFilterListInDashboard(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStatusList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getContractsListForDashboardForm")
	public List<DashboardObj> getContractsListForDesignForm(@RequestBody DashboardObj obj) {
		List<DashboardObj> objsList = null;
		try {
			objsList = service.getContractsListForDashboardForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractsListForDesignForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getUserRolesInDashboardAccess")
	public List<DashboardObj> getUserRolesInDashboardAccess(@RequestBody DashboardObj obj) {
		List<DashboardObj> objsList = null;
		try {
			objsList = service.getUserRolesInDashboardAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserRolesInDashboardAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getUserTypesInDashboardAccess")
	public List<DashboardObj> getUserTypesInDashboardAccess(@RequestBody DashboardObj obj) {
		List<DashboardObj> objsList = null;
		try {
			objsList = service.getUserTypesInDashboardAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserTypesInDashboardAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getUsersInDashboardAccess")
	public List<DashboardObj> getUsersInDashboardAccess(@RequestBody DashboardObj obj) {
		List<DashboardObj> objsList = null;
		try {
			objsList = service.getUsersInDashboardAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUsersInDashboardAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value="/ajax/form/get-access-dashboard")
	public Map<String, List<DashboardObj>> getAccessDashboardDetails(@RequestBody DashboardObj obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		Map<String, List<DashboardObj>> map = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.NEW_DASHBOARD_ACCESS_FORM);
			
			List<DashboardObj> statusList = service.getStatusListForDashboardForm(obj);
			map.put("statusList", statusList);
			
			List<DashboardObj> user_roles = service.getUserRolesInDashboardAccess(obj);
			map.put("user_roles", user_roles);
			
			List<DashboardObj> user_types = service.getUserTypesInDashboardAccess(obj);
			map.put("user_types", user_types);
			
			List<DashboardObj> users = service.getUsersInDashboardAccess(obj);
			map.put("users", users);
			
			//Dashboard dashboardDetails = service.getDashboardForm(obj);
			//model.addObject("dashboardDetails", dashboardDetails);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getAccessDashboardDetails : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value="/ajax/form/get-access-dashboard/details")
	public DashboardObj getAccessDashboardDetails2(@RequestBody DashboardObj obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		DashboardObj dashboardDetails = null;
		try {
			dashboardDetails = service.getDashboardForm(obj);
			//model.addObject("dashboardDetails", dashboardDetails);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getAccessDashboardDetails : " + e.getMessage());
		}
		return dashboardDetails;
	}
	
	@PostMapping(value = "/update-access-dashboard")
	public Boolean updateAccessDashboard(@ModelAttribute DashboardObj obj,HttpSession session){
		Boolean flag = false;
		try{
			User uObj = (User) session.getAttribute("user");
			String user_Id = uObj.getUserId();
			String userName = uObj.getUserName();
			obj.setModified_by_user_id_fk(user_Id);
			//model.setViewName("redirect:/access-dashboards");
			flag =  service.updateTableauDashboard(obj);
			/*if(flag) {
				attributes.addFlashAttribute("success", "Dashboard Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Dashboard is failed. Try again.");
			}*/
		}catch (Exception e) {
			//attributes.addFlashAttribute("error","Updating Dashboard is failed. Try again.");
			logger.error("updateAccessDashboard : " + e.getMessage());
		}
		return flag;
	}
}
