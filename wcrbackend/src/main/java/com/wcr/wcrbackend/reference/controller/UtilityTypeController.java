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
	
//	@RequestMapping(value="/utility-type",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView UtilityType(HttpSession session,@ModelAttribute Safety obj){
//		ModelAndView model = new ModelAndView(PageConstants.utilityType);
//		try {
//			Safety usUtilityTypeDetails = service.getUtilityTypesList(obj);
//			model.addObject("usUtilityTypeDetails",  usUtilityTypeDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error(" UtilityType : " + e.getMessage());
//		}
//		return model;
//	}
//	
	
	@GetMapping(
		    value = "/utility-typess",
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> getUtilityTypes() {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        // Create Safety object explicitly (no @RequestBody for GET)
		        Safety obj = new Safety();

		        Safety utilityTypeDetails = service.getUtilityTypesList(obj);

		        response.put("status", "success");
		        response.put("utilityTypeDetails", utilityTypeDetails);
		        return ResponseEntity.ok(response);

		    } catch (Exception e) {
		        logger.error("getUtilityTypes : ", e);
		        response.put("status", "error");
		        response.put("message", "Failed to fetch utility types");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}

	
//	@RequestMapping(value = "/add-utility-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addUtilityType(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-type");
//			boolean flag =  service.addUtilityType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", " Utility Type Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding  Utility Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding  Utility Type is failed. Try again.");
//			logger.error("add UtilityType : " + e.getMessage());
//		}
//		return model;
//	}
	
	
	@PostMapping(
		    value = "/add-utility-type",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> addUtilityType(
		        @RequestBody Safety obj) {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        boolean flag = service.addUtilityType(obj);

		        if (flag) {
		            response.put("status", "success");
		            response.put("message", "Utility Type added successfully.");
		            return ResponseEntity.ok(response);
		        } else {
		            response.put("status", "error");
		            response.put("message", "Adding Utility Type failed.");
		            return ResponseEntity.badRequest().body(response);
		        }

		    } catch (Exception e) {
		        logger.error("addUtilityType : ", e);
		        response.put("status", "error");
		        response.put("message", "Internal server error.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}

	
//	@RequestMapping(value = "/update-utility-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateUtilityType(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-type");
//			boolean flag =  service.updateUtilityType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Utility Type Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Utility Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Utility Type is failed. Try again.");
//			logger.error("updateUtilityType : " + e.getMessage());
//		}
//		return model;
//	}
//	
	
	
	@PostMapping(
		    value = "/update-utility-type",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> updateUtilityType(
		        @RequestBody Safety obj) {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        boolean flag = service.updateUtilityType(obj);

		        if (flag) {
		            response.put("status", "success");
		            response.put("message", "Utility Type updated successfully.");
		            return ResponseEntity.ok(response);
		        } else {
		            response.put("status", "error");
		            response.put("message", "Updating Utility Type failed.");
		            return ResponseEntity.badRequest().body(response);
		        }

		    } catch (Exception e) {
		        logger.error("updateUtilityType : ", e);
		        response.put("status", "error");
		        response.put("message", "Internal server error.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}
//
//	@DeleteMapping(
//		    value = "/delete-utility-type",
//		    consumes = MediaType.APPLICATION_JSON_VALUE,
//		    produces = MediaType.APPLICATION_JSON_VALUE
//		)
//		public ResponseEntity<Map<String, Object>> deleteUtilityType(
//		        @RequestBody Safety obj) {
//
//		    Map<String, Object> response = new HashMap<>();
//
//		    try {
//		        boolean flag = service.deleteUtilityType(obj);
//
//		        if (flag) {
//		            response.put("status", "success");
//		            response.put("message", "Utility Type deleted successfully.");
//		            return ResponseEntity.ok(response);
//		        } else {
//		            response.put("status", "error");
//		            response.put("message", "Utility Type not found or already deleted.");
//		            return ResponseEntity.badRequest().body(response);
//		        }
//
//		    } catch (Exception e) {
//		        logger.error("deleteUtilityType : ", e);
//		        response.put("status", "error");
//		        response.put("message", "Internal server error.");
//		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		    }
//		}
	@DeleteMapping(
		    value = "/delete-utility-type",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> deleteUtilityType(
		        @RequestBody Safety obj) throws Exception {

		    Map<String, Object> response = new HashMap<>();
		    boolean flag = service.deleteUtilityType(obj);

		    if (flag) {
		        response.put("status", "success");
		        response.put("message", "Utility Type deleted successfully.");
		        return ResponseEntity.ok(response);
		    }

		    response.put("status", "error");
		    response.put("message", "Utility Type not found.");
		    return ResponseEntity.badRequest().body(response);
		}

	
}









