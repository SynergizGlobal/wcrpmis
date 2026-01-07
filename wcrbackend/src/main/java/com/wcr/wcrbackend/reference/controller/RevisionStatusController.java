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
import com.wcr.wcrbackend.reference.Iservice.RevisionStatusService;
import com.wcr.wcrbackend.reference.model.Risk;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;
@Controller
public class RevisionStatusController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(RevisionStatusController.class);
	
	@Autowired
	RevisionStatusService service;
	
	@RequestMapping(value="/revision-status",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView revisionStatus(HttpSession session,@ModelAttribute TrainingType obj){
		ModelAndView model = new ModelAndView(PageConstants.revisionStatus);
		try {
			List<Risk> revisionStatusList = service.getRevisionStatusList();
			model.addObject("revisionStatusList", revisionStatusList);
			TrainingType revisionStatusDetails = service.getRevisionStatusDetails(obj);
			model.addObject("revisionStatusDetails",revisionStatusDetails);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("revisionStatus : " + e.getMessage());
		}
		return model;
	}
	
	
	@RequestMapping(value = "/add-revision-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addRevisionStatus(@ModelAttribute Risk obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/revision-status");
			boolean flag =  service.addRevisionStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Revision Status Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Revision Status is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Revision Status is failed. Try again.");
			logger.error("addRevisionStatus : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-revision-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateRevisionStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/revision-status");
			boolean flag =  service.updateRevisionStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Revision Status Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Revision Status is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Revision Status is failed. Try again.");
			logger.error("updateRevisionStatus : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-revision-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteRevisionStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/revision-status");
			boolean flag =  service.deleteRevisionStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Revision Status Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteRevisionStatus : " + e.getMessage());
		}
		return model;
	}
	
}






