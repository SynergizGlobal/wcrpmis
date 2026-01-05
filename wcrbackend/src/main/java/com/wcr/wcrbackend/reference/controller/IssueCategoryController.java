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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.reference.Iservice.IssueCategoryService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class IssueCategoryController {


	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(IssueCategoryController.class);
	
	@Autowired
	IssueCategoryService service;
	
	@RequestMapping(value = "/issue-category", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> issueCategory(HttpSession session, @RequestBody TrainingType obj) {

		Map<String, Object> map = new HashMap<>();

		try {
			List<Safety> issueCategoryList = service.getIssueCategoryList();
			map.put("issueCategoryList", issueCategoryList);

			TrainingType issueCategoryDetails = service.getIssueCategoryDetails(obj);
			map.put("issueCategoryDetails", issueCategoryDetails);

			map.put("status", "success");
		} catch (Exception e) {
			logger.error("issueCategory : " + e.getMessage(), e);
			map.put("status", "error");
			map.put("message", e.getMessage());
		}

		return map;
	}
	
	@RequestMapping(value = "/add-issue-category", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addIssueCategory(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-category");
			boolean flag =  service.addIssueCategory(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Category Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Issue Category is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Issue Category is failed. Try again.");
			logger.error("addIssueCategory : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-issue-category", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateIssueCategory(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-category");
			boolean flag =  service.updateIssueCategory(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Category Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Issue Category is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Issue Category is failed. Try again.");
			logger.error("updateIssueCategory : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-issue-category", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteIssueCategory(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-category");
			boolean flag =  service.deleteIssueCategory(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Category Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteIssueCategory : " + e.getMessage());
		}
		return model;
	}
	
}




