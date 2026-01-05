package com.wcr.wcrbackend.reference.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.IssueCategoryTitleService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class IssueCategoryTitleController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(IssueCategoryTitleController.class);
	
	@Autowired
	IssueCategoryTitleService service;
	
	
	@RequestMapping(value="/issue-category-title",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView issueCategoryTitle(HttpSession session,@ModelAttribute TrainingType obj){
		ModelAndView model = new ModelAndView(PageConstants.issueCategoryTitle);
		try {
			
			List<TrainingType> issueCategoryDetails = service.gtIssueCategoryDetails(obj);
			model.addObject("issueCategoryDetails",issueCategoryDetails);
			
			List<TrainingType> issueCategoryTitleDetails = service.getIssueCategoryTitle(obj);
			model.addObject("issueCategoryTitleDetails",issueCategoryTitleDetails);
			
			List<TrainingType> getTitles = service.getTitles(obj);
			model.addObject("getTitles",getTitles);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("issueContractCategory : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/ajax/getTitles", method = { RequestMethod.GET,RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<TrainingType> getTitles(@ModelAttribute TrainingType obj) {
		List<TrainingType> objList = null;
		try {
			objList = service.getTitles(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getTitles : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/add-issue-category-title", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addIssueCategoryTitle(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-category-title");
			boolean flag =  service.addIssueCategoryTitle(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Category Title Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Issue Category Title is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Issue Category Title is failed. Try again.");
			logger.error("addIssueCategoryTitle : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-issue-category-title", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateIssueCategoryTitle(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-category-title");
			boolean flag =  service.updateIssueCategoryTitle(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Category Title Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Issue Category Title is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Issue Category Title is failed. Try again.");
			logger.error("updateIssueCategoryTitle : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-issue-category-title", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteIssueCategoryTitle(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/issue-category-title");
			boolean flag =  service.deleteIssueCategoryTitle(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Issue Category Title Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteIssueCategoryTitle : " + e.getMessage());
		}
		return model;
	}
}

