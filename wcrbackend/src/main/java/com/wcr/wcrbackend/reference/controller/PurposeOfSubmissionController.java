package com.wcr.wcrbackend.reference.controller;


import java.util.HashMap;
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

import com.wcr.wcrbackend.reference.Iservice.PurposeOfSubmissionService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class PurposeOfSubmissionController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(PurposeOfSubmissionController.class);
	
	@Autowired
	PurposeOfSubmissionService service;
	
//	@RequestMapping(value="/purpose-of-submission",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView purposeOfSubmission(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.purposeOfSubmission);
//		try {
//			
//			TrainingType purposeOfSubmissionDetails = service.getPurposeOfSubmissionDetails(obj);
//			model.addObject("purposeOfSubmissionDetails",purposeOfSubmissionDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("purposeOfSubmission : " + e.getMessage());
//		}
//		return model;
//	}
	
	@RequestMapping(
	        value = "/purpose-of-submission",
	        method = { RequestMethod.GET, RequestMethod.POST }
	)
	@ResponseBody
	public Map<String, Object> purposeOfSubmission(
	        HttpSession session,
	        @RequestBody(required = false) TrainingType obj
	) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        TrainingType purposeOfSubmissionDetails =
	                service.getPurposeOfSubmissionDetails(obj);

	        result.put("purposeOfSubmissionDetails", purposeOfSubmissionDetails);
	        result.put("status", "success");

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("purposeOfSubmission : " + e.getMessage());

	        result.put("status", "error");
	        result.put("message", "Failed to fetch Purpose Of Submission details");
	    }

	    return result;
	}

	
	
	@RequestMapping(value = "/add-purpose-of-submission", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addPurposeOfSubmission(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/purpose-of-submission");
			boolean flag =  service.addPurposeOfSubmission(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Purpose Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Purpose is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Purpose is failed. Try again.");
			logger.error("addPurposeOfSubmission : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-purpose-of-submission", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updatePurposeOfSubmission(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/purpose-of-submission");
			boolean flag =  service.updatePurposeOfSubmission(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Purpose Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Purpose is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Purpose is failed. Try again.");
			logger.error("updatePurposeOfSubmission : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-purpose-of-submission", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deletePurposeOfSubmission(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/purpose-of-submission");
			boolean flag =  service.deletePurposeOfSubmission(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Purpose Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deletePurposeOfSubmission : " + e.getMessage());
		}
		return model;
	}
	
}


