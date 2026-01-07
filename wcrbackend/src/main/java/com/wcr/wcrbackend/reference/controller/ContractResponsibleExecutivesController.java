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

import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.ContractResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.Iservice.RrResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class ContractResponsibleExecutivesController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(ContractResponsibleExecutivesController.class);
	@Autowired
	RrResponsibleExecutivesService service;
	
	
	@Autowired
	ContractResponsibleExecutivesService mainService;
	
	@RequestMapping(value="/contract-executives",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView executives(HttpSession session,@ModelAttribute TrainingType obj){
		ModelAndView model = new ModelAndView(PageConstants.contractExecutives);
		try {
			
			List<TrainingType> executivesDetails = mainService.getExecutivesDetails(obj);
			model.addObject("executivesDetails",executivesDetails);
			
			List<TrainingType> workDetails = mainService.getWorkDetails(obj);
			model.addObject("workDetails",workDetails);
			
			List<TrainingType> usersDetails = mainService.getUsersDetails(obj);
			model.addObject("usersDetails",usersDetails);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("executives : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/ajax/getWorkWiseContractResponsibleUsers", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<TrainingType> getWorkWiseContractResponsibleUsers(@ModelAttribute TrainingType obj,HttpSession session) {
		List<TrainingType> contractorsFilterList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUser_role_code());
			obj.setUser_id(uObj.getUser_id());
			contractorsFilterList = mainService.getUsersDetails(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractorsFilterList : " + e.getMessage());
		}
		return contractorsFilterList;
	}	

	
	@RequestMapping(value = "/add-contract-executives", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addContractExecutives(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/contract-executives");
			boolean flag =  mainService.addContractExecutives(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Executives Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Executives is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Executives is failed. Try again.");
			logger.error("addContractExecutives : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-contract-executives", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateContractExecutives(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/contract-executives");
			boolean flag =  mainService.updateContractExecutives(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Executives Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Executives is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Executives is failed. Try again.");
			logger.error("updateContractExecutives : " + e.getMessage());
		}
		return model;
	}
}
