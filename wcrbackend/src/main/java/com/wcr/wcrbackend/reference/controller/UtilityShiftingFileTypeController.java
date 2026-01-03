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
import com.wcr.wcrbackend.reference.Iservice.UtilityShiftingFileTypeService;
import com.wcr.wcrbackend.reference.model.Safety;

import jakarta.servlet.http.HttpSession;


@Controller
public class UtilityShiftingFileTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(UtilityShiftingFileTypeController.class);
	
	@Autowired
	UtilityShiftingFileTypeService service;
	
	@RequestMapping(value="/us-utility-shifting-file-type",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView UtilityShiftingFileType(HttpSession session,@ModelAttribute Safety obj){
		ModelAndView model = new ModelAndView(PageConstants.utilityShiftingFileType);
		try {
			Safety  UtilityShiftingFileTypeList = service.getUtilityShiftingFileTypeList(obj);
			model.addObject("utilityShiftingFileTypeList",  UtilityShiftingFileTypeList);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" UtilityShiftingFileType : " + e.getMessage());
		}
		return model;
	}
	
	
	@RequestMapping(value = "/add-us-utility-shifting-file-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addUtilityShiftingFileType(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/us-utility-shifting-file-type");
			boolean flag =  service.addUtilityShiftingFileType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", " Utility Status Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding  Utility Status is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding  Utility Status is failed. Try again.");
			logger.error("add UtilityShiftingFileType : " + e.getMessage());
		}
		return model;
	}
	
	
	@RequestMapping(value = "/update-us-utility-shifting-file-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateUtilityShiftingFileType(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/us-utility-shifting-file-type");
			boolean flag =  service.updateUtilityShiftingFileType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Utility Status Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Utility Status is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Utility Status is failed. Try again.");
			logger.error("updateUtilityShiftingFileType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-us-utility-shifting-file-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteUtilityShiftingFileType(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/us-utility-shifting-file-type");
			boolean flag =  service.deleteUtilityShiftingFileType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Utility Status Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteUtilityShiftingFileType : " + e.getMessage());
		}
		return model;
	}	
	
}









