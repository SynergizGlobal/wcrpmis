package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.reference.Iservice.DesignResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;
import com.wcr.wcrbackend.service.UserService;

import jakarta.servlet.http.HttpSession;
@RestController
public class DesignResponsibleExecutivesController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(DesignResponsibleExecutivesController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	DesignResponsibleExecutivesService mainService;
	
//	@RequestMapping(value="/design-executives",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView executives(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.designExecutives);
//		try {
//			
//			List<TrainingType> executivesDetails = mainService.getExecutivesDetails(obj);
//			model.addObject("executivesDetails",executivesDetails);
//			
//			List<TrainingType> usersDetails = mainService.getUsersDetails(obj);
//			model.addObject("usersDetails",usersDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("executives : " + e.getMessage());
//		}
//		return model;
//	}
	
	@RequestMapping(
	        value = "/design-executives",
	        method = { RequestMethod.GET, RequestMethod.POST }
	)
	@ResponseBody
	public Map<String, Object> designExecutives(
	        HttpSession session,
	        @RequestBody(required = false) TrainingType obj
	) {

	    Map<String, Object> result = new HashMap<>();

	    try {
	        List<TrainingType> projectDetails =
	                mainService.getProjectDetails(obj);
	        List<TrainingType> executivesDetails =
	                mainService.getExecutivesDetails(obj);

	        List<TrainingType> usersDetails =
	                mainService.getUsersDetails(obj);
	        result.put("projectDetails", projectDetails);
	        result.put("executivesDetails", executivesDetails);
	        result.put("usersDetails", usersDetails);
	        result.put("status", "success");

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("design-executives : " + e.getMessage());

	        result.put("status", "error");
	        result.put("message", "Failed to fetch Design Executives details");
	    }

	    return result;
	}

	
	@RequestMapping(value = "/ajax/getProjectWiseDesignResponsibleUsers", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<TrainingType> getProjectWiseDesignResponsibleUsers(@RequestBody TrainingType obj,HttpSession session) {
		List<TrainingType> contractorsFilterList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			 			obj.setUser_id(uObj.getUserId());
			contractorsFilterList = mainService.getUsersDetails(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractorsFilterList : " + e.getMessage());
		}
		return contractorsFilterList;
	}	

	
	@RequestMapping(value = "/add-design-executives", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addDesignExecutives(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/design-executives");
			boolean flag =  mainService.addDesignExecutives(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Executives Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Executives is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Executives is failed. Try again.");
			logger.error("addDesignExecutives : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-design-executives", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateDesignExecutives(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/design-executives");
			boolean flag =  mainService.updateDesignExecutives(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Executives Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Executives is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Executives is failed. Try again.");
			logger.error("updateDesignExecutives : " + e.getMessage());
		}
		return model;
	}
}
