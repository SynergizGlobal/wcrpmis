package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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

import com.wcr.wcrbackend.reference.Iservice.IssuePriorityService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class IssuePriorityController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(IssuePriorityController.class);
	
	@Autowired
	IssuePriorityService service;
	
//	@RequestMapping(value="/issue-priority",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView issuePriority(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.issuePriority);
//		try {
//			List<Safety> issuePriority = service.getIssuePriorityList();
//			model.addObject("issuePriority", issuePriority);
//			TrainingType issuePriorityDetails = service.getIssuePriorityDetails(obj);
//			model.addObject("issuePriorityDetails",issuePriorityDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("issuePriority : " + e.getMessage());
//		}
//		return model;
//	}
	
	@RequestMapping(
	        value = "/issue-priority",
	        method = { RequestMethod.GET, RequestMethod.POST }
	)
	@ResponseBody
	public Map<String, Object> issuePriority(
	        HttpSession session,
	        @RequestBody(required = false) TrainingType obj
	) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        List<Safety> issuePriorityList =
	                service.getIssuePriorityList();

	        TrainingType issuePriorityDetails =
	                service.getIssuePriorityDetails(obj);

	        result.put("issuePriority", issuePriorityList);
	        result.put("issuePriorityDetails", issuePriorityDetails);
	        result.put("status", "success");

	    } catch (Exception e) {
	        logger.error("issuePriority : ", e);

	        result.put("status", "error");
	        result.put("message", "Failed to fetch Issue Priority details");
	    }

	    return result;
	}

	
	
	@RequestMapping(value = "/add-issue-priority", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addIssuePriority(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-priority");
			boolean flag =  service.addIssuePriority(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Priority Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Issue Priority is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Issue Priority is failed. Try again.");
			logger.error("addIssuePriority : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-issue-priority", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateIssuePriority(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-priority");
			boolean flag =  service.updateIssuePriority(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Priority Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Issue Priority is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Issue Priority is failed. Try again.");
			logger.error("updateIssuePriority : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-issue-priority", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteIssuePriority(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-priority");
			boolean flag =  service.deleteIssuePriority(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Priority Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteIssuePriority : " + e.getMessage());
		}
		return model;
	}
	
}





