package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.YesOrNoStatusService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class YesOrNoStatusController {

    Logger logger = Logger.getLogger(YesOrNoStatusController.class);

    @Autowired
    private YesOrNoStatusService service;

    @RequestMapping(
    	    value = "/yes-or-no-status",
    	    method = { RequestMethod.GET }
    	)
    	public Map<String, Object> getYesOrNoStatus(HttpSession session) {

    	    Map<String, Object> result = new HashMap<>();

    	    try {
    	        TrainingType obj = new TrainingType();
    	        TrainingType details = service.getYesOrNoStatusDetails(obj);

    	        result.put("yesOrNoStatusDetails", details);
    	        result.put("status", "success");

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        logger.error("yes-or-no-status : " + e.getMessage());
    	        result.put("status", "error");
    	    }

    	    return result;
    	}

	
	@RequestMapping(value = "/add-yes-or-no-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addYesOrNoStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/yes-or-no-status");
			boolean flag =  service.addYesOrNoStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("addYesOrNoStatus : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-yes-or-no-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateYesOrNoStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/yes-or-no-status");
			boolean flag =  service.updateYesOrNoStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("updateYesOrNoStatus : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-yes-or-no-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteYesOrNoStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/yes-or-no-status");
			boolean flag =  service.deleteYesOrNoStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteYesOrNoStatus : " + e.getMessage());
		}
		return model;
	}
	
}
