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

import com.wcr.wcrbackend.reference.Iservice.IssueContractCategoryService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class IssueContractCategoryContraoller {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(IssueContractCategoryContraoller.class);
	
	@Autowired
	IssueContractCategoryService service;
	
//	@RequestMapping(value="/issue-contract-category",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView issueContractCategory(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.issueContractCategory);
//		try {
//			List<TrainingType> contractTypeDetails = service.getContractTypeDetails(obj);
//			model.addObject("contractTypeDetails",contractTypeDetails);
//			
//			List<TrainingType> issueCategoryDetails = service.gtIssueCategoryDetails(obj);
//			model.addObject("issueCategoryDetails",issueCategoryDetails);
//			
//			List<TrainingType> issueContractCategory = service.getIssueContractCategory(obj);
//			model.addObject("issueContractCategory",issueContractCategory);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("issueContractCategory : " + e.getMessage());
//		}
//		return model;
//	}
	
	
	@RequestMapping(
	        value = "/issue-contract-category",
	        method = { RequestMethod.GET, RequestMethod.POST }
	)
	@ResponseBody
	public Map<String, Object> issueContractCategory(
	        HttpSession session,
	        @RequestBody(required = false) TrainingType obj
	) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        List<TrainingType> contractTypeDetails =
	                service.getContractTypeDetails(obj);

	        List<TrainingType> issueCategoryDetails =
	                service.gtIssueCategoryDetails(obj);

	        List<TrainingType> issueContractCategory =
	                service.getIssueContractCategory(obj);

	        result.put("contractTypeDetails", contractTypeDetails);
	        result.put("issueCategoryDetails", issueCategoryDetails);
	        result.put("issueContractCategory", issueContractCategory);
	        result.put("status", "success");

	    } catch (Exception e) {
	        logger.error("issueContractCategory : ", e);

	        result.put("status", "error");
	        result.put("message", "Failed to fetch Issue Contract Category details");
	    }

	    return result;
	}

	
	@RequestMapping(value = "/ajax/getContarctCategory", method = { RequestMethod.GET,RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<TrainingType> getContarctCategory(@ModelAttribute TrainingType obj) {
		List<TrainingType> objList = null;
		try {
			objList = service.getContarctCategory(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getContarctCategory : " + e.getMessage());
		}
		return objList;
	}
	
	@RequestMapping(value = "/add-issue-contract-category", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addIssueContractCategory(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-contract-category");
			boolean flag =  service.addIssueContractCategory(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Contract Category Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Issue Contract Category is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Issue Contract Category is failed. Try again.");
			logger.error("addIssueContractCategory : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-issue-contract-category", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateIssueContractCategory(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-contract-category");
			boolean flag =  service.updateIssueContractCategory(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Contract Category Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Issue Contract Category is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Issue Contract Category is failed. Try again.");
			logger.error("updateIssueContractCategory : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-issue-contract-category", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteIssueContractCategory(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-contract-category");
			boolean flag =  service.deleteIssueContractCategory(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Contract Category Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteIssueContractCategory : " + e.getMessage());
		}
		return model;
	}
}
