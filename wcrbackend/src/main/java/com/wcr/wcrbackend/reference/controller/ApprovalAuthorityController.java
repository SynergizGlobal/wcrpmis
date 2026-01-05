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

import com.wcr.wcrbackend.reference.Iservice.ApprovalAuthorityService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class ApprovalAuthorityController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(ApprovalAuthorityController.class);
	
	@Autowired
	ApprovalAuthorityService service;
	
//	@RequestMapping(value="/approval-authority",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView approvalAuthority(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.approvalAuthority);
//		try {
//			
//			TrainingType approvalAuthorityDetails = service.getApprovalAuthorityDetails(obj);
//			model.addObject("approvalAuthorityDetails",approvalAuthorityDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("approvalAuthority : " + e.getMessage());
//		}
//		return model;
//	}
	
	@RequestMapping(
		    value = "/approval-authority",
		    method = { RequestMethod.GET, RequestMethod.POST }
		)
		@ResponseBody
		public Map<String, Object> approvalAuthority(
		        HttpSession session,
		        @RequestBody(required = false) TrainingType obj
		) {
		    Map<String, Object> result = new HashMap<>();

		    try {
		        TrainingType approvalAuthorityDetails =
		                service.getApprovalAuthorityDetails(obj);

		        result.put("approvalAuthorityDetails", approvalAuthorityDetails);
		        result.put("status", "success");

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("approvalAuthority : " + e.getMessage());

		        result.put("status", "error");
		        result.put("message", "Failed to fetch Approval Authority details");
		    }

		    return result;
		}

	
	
	@RequestMapping(value = "/add-approval-authority", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addApprovalAuthority(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/approval-authority");
			boolean flag =  service.addApprovalAuthority(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Approval Authority Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Approval Authority is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Approval Authority is failed. Try again.");
			logger.error("addApprovalAuthority : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-approval-authority", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateApprovalAuthority(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/approval-authority");
			boolean flag =  service.updateApprovalAuthority(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Approval Authority Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Approval Authority is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Approval Authority is failed. Try again.");
			logger.error("updateApprovalAuthority : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-approval-authority", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteApprovalAuthority(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/approval-authority");
			boolean flag =  service.deleteApprovalAuthority(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Approval Authority Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteApprovalAuthority : " + e.getMessage());
		}
		return model;
	}
	
}

