package com.wcr.wcrbackend.reference.controller;

import java.util.List;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import com.wcr.wcrbackend.reference.Iservice.InsuranceTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;


@Controller
public class InsuranceTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(InsuranceTypeController.class);
	
	@Autowired
	InsuranceTypeService service;
	
	@RequestMapping(value="/insurance-type",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView insuranceType(HttpSession session,@ModelAttribute TrainingType obj){
		ModelAndView model = new ModelAndView(PageConstants.insuranceType);
		try {
			List<TrainingType> insuranceType = service.getInsuranceTypesList();
			model.addObject("insuranceType", insuranceType);
			TrainingType insuranceTypesDetails = service.getInsuranceTypesDetails(obj);
			model.addObject("insuranceTypesDetails",insuranceTypesDetails);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("insuranceType : " + e.getMessage());
		}
		return model;
	}
	
	
	@RequestMapping(value = "/add-insurance-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addInsuranceType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/insurance-type");
			boolean flag =  service.addInsuranceType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Insurance Type Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Insurance Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Insurance Type is failed. Try again.");
			logger.error("addInsuranceType : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-insurance-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateInsuranceTypes(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/insurance-type");
			boolean flag =  service.updateInsuranceTypes(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Insurance Type Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Insurance Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Insurance Type is failed. Try again.");
			logger.error("updateInsuranceTypes : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-insurance-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteInsuranceTypes(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/insurance-type");
			boolean flag =  service.deleteInsuranceTypes(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Insurance Type Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteInsuranceTypes : " + e.getMessage());
		}
		return model;
	}
	
}









