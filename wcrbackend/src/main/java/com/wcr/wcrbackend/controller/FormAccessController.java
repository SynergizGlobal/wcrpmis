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

import com.wcr.wcrbackend.DTO.Form;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IFormAccessService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/form-access")
public class FormAccessController {
	Logger logger = Logger.getLogger(FormAccessController.class);
	
	@Autowired
	IFormAccessService service;
	
	@PostMapping(value = "/ajax/get-forms-list")
	public List<Form> getFormsList(@RequestBody Form obj) {
		List<Form> objList = null;
		try {
			objList = service.getFormsList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getFormsList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getModulesFilterListInForm")
	public List<Form> getModulesList(@RequestBody Form obj) {
		List<Form> objList = null;
		try {
			objList = service.getModulesFilterListInForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getModulesList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getStatusFilterListInForm")
	public List<Form> getStatusList(@RequestBody Form obj) {
		List<Form> objList = null;
		try {
			objList = service.getStatusFilterListInForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStatusList : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/form/add-form-access")
	public Map<String, List<Form>> addFormAccess(@RequestBody Form obj){
		//ModelAndView model = new ModelAndView();
		Map<String, List<Form>> map = new HashMap<>(); 
		try{
			//model.setViewName(PageConstants.addEditFormAccessForm);
			//model.addObject("action", "add");
			
			List<Form> modulesList = service.getModulesListForFormAccess(obj);
			map.put("modulesList", modulesList);
			
			List<Form> foldersList = service.getFolderssListForFormAccess(obj);
			map.put("foldersList", foldersList);
			
			List<Form> statusList = service.getStatusListForFormAccess(obj);
			map.put("statusList", statusList);
			
			List<Form> user_roles = service.getUserRolesInFormAccess(obj);
			map.put("user_roles", user_roles);
			
			List<Form> user_types = service.getUserTypesInFormAccess(obj);
			map.put("user_types", user_types);
			
			List<Form> users = service.getUsersInFormAccess(obj);
			map.put("users", users);
			
			
		}catch (Exception e) {
				logger.error("addFormAccess : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value = "/ajax/getUserRolesInFormAccess")
	public List<Form> getUserRolesInDashboardAccess(@RequestBody Form obj) {
		List<Form> objsList = null;
		try {
			objsList = service.getUserRolesInFormAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserRolesInFormAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getUserTypesInFormAccess")
	public List<Form> getUserTypesInFormAccess(@RequestBody Form obj) {
		List<Form> objsList = null;
		try {
			objsList = service.getUserTypesInFormAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserTypesInFormAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getUsersInFormAccess")
	public List<Form> getUsersInFormAccess(@RequestBody Form obj) {
		List<Form> objsList = null;
		try {
			objsList = service.getUsersInFormAccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUsersInFormAccess : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/form/get-form/details")
	public Form getForm(@RequestBody Form obj ){
		Form formDetails = null;
		try{
			formDetails = service.getForm(obj);
		}catch (Exception e) {
				e.printStackTrace();
				logger.error("getForm : " + e.getMessage());
		}
		return formDetails;
	 }
	
	@PostMapping(value = "/add-form")
	public Boolean addForm(@ModelAttribute Form obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		Boolean flag = false;
		try{
			User uObj = (User) session.getAttribute("user");
			String user_Id = uObj.getUserId();
			String userName = uObj.getUserName();
			//model.setViewName("redirect:/forms");
			flag =  service.addForm(obj);
			/*if(flag) {
				attributes.addFlashAttribute("success", "Form Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Form is failed. Try again.");
			}*/
		}catch (Exception e) {
			//attributes.addFlashAttribute("error","Adding Form is failed. Try again.");
			logger.error("addForm : " + e.getMessage());
		}
		return flag;
	}
	
	@PostMapping(value = "/update-form")
	public Boolean updateForm(@ModelAttribute Form obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		Boolean flag = false;
		try{
			User uObj = (User) session.getAttribute("user");
			String user_Id = uObj.getUserId();
			String userName = uObj.getUserName();
			//model.setViewName("redirect:/forms");
			flag =  service.updateForm(obj);
			/*if(flag) {
				attributes.addFlashAttribute("success", "Form Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Form is failed. Try again.");
			}*/
		}catch (Exception e) {
			//attributes.addFlashAttribute("error","Updating Form is failed. Try again.");
			logger.error("updateForm : " + e.getMessage());
		}
		return flag;
	}
	
	@PostMapping(value = "/update-access-form")
	public Boolean updateAccessForm(@ModelAttribute Form obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		Boolean flag = false;
		try{
			//String user_Id = (String) session.getAttribute("USER_ID");String userName = (String) session.getAttribute("USER_NAME");
			User uObj = (User) session.getAttribute("user");
			String user_Id = uObj.getUserId();
			String userName = uObj.getUserName();
			
			//model.setViewName("redirect:/access-forms");
			flag =  service.updateAccessForm(obj);
			/*if(flag) {
				attributes.addFlashAttribute("success", "Form Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Form is failed. Try again.");
			}*/
		}catch (Exception e) {
			//attributes.addFlashAttribute("error","Updating Form is failed. Try again.");
			logger.error("updateAccessForm : " + e.getMessage());
		}
		return flag;
	}
}