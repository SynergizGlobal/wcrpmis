package com.wcr.wcrbackend.reference.controller;




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
import com.wcr.wcrbackend.reference.Iservice.UtilityTypeService;
import com.wcr.wcrbackend.reference.model.Safety;

import jakarta.servlet.http.HttpSession;


@Controller
public class UtilityTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(UtilityTypeController.class);
	
	@Autowired
	UtilityTypeService service;
	
	@RequestMapping(value="/utility-type",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView UtilityType(HttpSession session,@ModelAttribute Safety obj){
		ModelAndView model = new ModelAndView(PageConstants.utilityType);
		try {
			Safety usUtilityTypeDetails = service.getUtilityTypesList(obj);
			model.addObject("usUtilityTypeDetails",  usUtilityTypeDetails);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" UtilityType : " + e.getMessage());
		}
		return model;
	}
	
	
	@RequestMapping(value = "/add-utility-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addUtilityType(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/utility-type");
			boolean flag =  service.addUtilityType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", " Utility Type Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding  Utility Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding  Utility Type is failed. Try again.");
			logger.error("add UtilityType : " + e.getMessage());
		}
		return model;
	}
	
	
	@RequestMapping(value = "/update-utility-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateUtilityType(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/utility-type");
			boolean flag =  service.updateUtilityType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Utility Type Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Utility Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Utility Type is failed. Try again.");
			logger.error("updateUtilityType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-utility-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteUtilityType(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/utility-type");
			boolean flag =  service.deleteUtilityType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Utility Type Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteUtilityType : " + e.getMessage());
		}
		return model;
	}	
	
}









