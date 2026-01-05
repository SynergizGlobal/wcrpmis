package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.reference.Iservice.IssueStatusService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class IssueStatusController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(IssueStatusController.class);
	
	@Autowired
	IssueStatusService service;
	
//	@RequestMapping(value="/issue-status",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView issueStatus(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.issueStatus);
//		try {
//			List<Safety> issueStatusList = service.getIssueStatusList();
//			model.addObject("issueStatusList", issueStatusList);
//			TrainingType issueStatusDetails = service.getIssueStatusDetails(obj);
//			model.addObject("issueStatusDetails",issueStatusDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("issueStatus : " + e.getMessage());
//		}
//		return model;
//	}
	
	
	@RequestMapping(
	        value = "/issue-status",
	        method = { RequestMethod.GET, RequestMethod.POST }
	)
	@ResponseBody
	public Map<String, Object> issueStatus(
	        HttpSession session,
	        @RequestBody(required = false) TrainingType obj
	) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        List<Safety> issueStatusList =
	                service.getIssueStatusList();

	        TrainingType issueStatusDetails =
	                service.getIssueStatusDetails(obj);

	        result.put("issueStatusList", issueStatusList);
	        result.put("issueStatusDetails", issueStatusDetails);
	        result.put("status", "success");

	    } catch (Exception e) {
	        logger.error("issueStatus : ", e);

	        result.put("status", "error");
	        result.put("message", "Failed to fetch Issue Status details");
	    }

	    return result;
	}

	
	@RequestMapping(value = "/add-issue-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addIssueStatus(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-status");
			boolean flag =  service.addIssueStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Status is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Status is failed. Try again.");
			logger.error("addIssueStatus : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-issue-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateIssueStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-status");
			boolean flag =  service.updateIssueStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Status is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Status is failed. Try again.");
			logger.error("updateIssueStatus : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-issue-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteIssueStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-status");
			boolean flag =  service.deleteIssueStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteIssueStatus : " + e.getMessage());
		}
		return model;
	}
	
}




