package com.wcr.wcrbackend.reference.controller;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.UtilityStatusService;
import com.wcr.wcrbackend.reference.model.Safety;

import jakarta.servlet.http.HttpSession;

@Controller
public class UtilityStatusController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(UtilityStatusController.class);
	
	@Autowired
	UtilityStatusService service;
	
//	@RequestMapping(value="/utility-status",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView UtilityStatus(HttpSession session,@ModelAttribute Safety obj){
//		ModelAndView model = new ModelAndView(PageConstants.utilityStatus);
//		try {
//			Safety  UtilityStatusList = service.getUtilityStatusList(obj);
//			model.addObject("utilityStatusList",  UtilityStatusList);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error(" UtilityStatus : " + e.getMessage());
//		}
//		return model;
//	}
	
	@GetMapping(
		    value = "/utility-status",
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> getUtilityStatus() {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        Safety obj = new Safety(); 
		        Safety utilityStatusList = service.getUtilityStatusList(obj);

		        response.put("status", "success");
		        response.put("utilityStatusList", utilityStatusList);
		        return ResponseEntity.ok(response);

		    } catch (Exception e) {
		        logger.error("getUtilityStatus : ", e);
		        response.put("status", "error");
		        response.put("message", "Failed to fetch utility status list");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}

//	
//	@RequestMapping(value = "/add-utility-status", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addUtilityStatus(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-status");
//			boolean flag =  service.addUtilityStatus(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", " Utility Status Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding  Utility Status is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding  Utility Status is failed. Try again.");
//			logger.error("add UtilityStatus : " + e.getMessage());
//		}
//		return model;
//	}
//	
	
	
	@PostMapping(
		    value = "/add-utility-status",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> addUtilityStatus(
		        @RequestBody Safety obj) {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        boolean flag = service.addUtilityStatus(obj);

		        if (flag) {
		            response.put("status", "success");
		            response.put("message", "Utility Status added successfully.");
		            return ResponseEntity.ok(response);
		        } else {
		            response.put("status", "error");
		            response.put("message", "Adding Utility Status failed. Try again.");
		            return ResponseEntity.badRequest().body(response);
		        }

		    } catch (Exception e) {
		        logger.error("addUtilityStatus : ", e);
		        response.put("status", "error");
		        response.put("message", "Internal server error.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}

//	@RequestMapping(value = "/update-utility-status", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateUtilityStatus(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-status");
//			boolean flag =  service.updateUtilityStatus(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Utility Status Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Utility Status is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Utility Status is failed. Try again.");
//			logger.error("updateUtilityStatus : " + e.getMessage());
//		}
//		return model;
//	}
//	
	
	
	@PostMapping(
		    value = "/update-utility-status",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> updateUtilityStatus(
		        @RequestBody Safety obj) {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        boolean flag = service.updateUtilityStatus(obj);

		        if (flag) {
		            response.put("status", "success");
		            response.put("message", "Utility Status updated successfully.");
		            return ResponseEntity.ok(response);
		        } else {
		            response.put("status", "error");
		            response.put("message", "Updating Utility Status failed. Try again.");
		            return ResponseEntity.badRequest().body(response);
		        }

		    } catch (Exception e) {
		        logger.error("updateUtilityStatus : ", e);
		        response.put("status", "error");
		        response.put("message", "Internal server error.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}

//	@RequestMapping(value = "/delete-utility-status", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView deleteUtilityStatus(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-status");
//			boolean flag =  service.deleteUtilityStatus(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Utility Status Deleted Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			logger.error("deleteUtilityStatus : " + e.getMessage());
//		}
//		return model;
//	}	
//	
	
	
	
	@DeleteMapping(
		    value = "/delete-utility-status",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> deleteUtilityStatus(
		        @RequestBody Safety obj) {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        boolean flag = service.deleteUtilityStatus(obj);

		        if (flag) {
		            response.put("status", "success");
		            response.put("message", "Utility Status deleted successfully.");
		            return ResponseEntity.ok(response);
		        } else {
		            response.put("status", "error");
		            response.put("message", "Utility Status not found or already deleted.");
		            return ResponseEntity.badRequest().body(response);
		        }

		    } catch (Exception e) {
		        logger.error("deleteUtilityStatus : ", e);
		        response.put("status", "error");
		        response.put("message", "Internal server error.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}

}









