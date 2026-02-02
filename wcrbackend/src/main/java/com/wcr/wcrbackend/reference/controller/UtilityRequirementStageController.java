package com.wcr.wcrbackend.reference.controller;


import java.util.HashMap;
import java.util.List;
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
import com.wcr.wcrbackend.reference.Iservice.UtilityRequirementStageService;
import com.wcr.wcrbackend.reference.model.Safety;

import jakarta.servlet.http.HttpSession;


@Controller
public class UtilityRequirementStageController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(UtilityRequirementStageController.class);
	
	@Autowired
	UtilityRequirementStageService service;
	
//	@RequestMapping(value="/utility-requirement-stage",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView UtilityRequirementStage(HttpSession session,@ModelAttribute Safety obj){
//		ModelAndView model = new ModelAndView(PageConstants.utilityRequirementStage);
//		try {
//			Safety  UtilityRequirementStageList = service.getUtilityRequirementStagesList(obj);
//			model.addObject("utilityRequirementStageList",  UtilityRequirementStageList);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error(" UtilityRequirementStage : " + e.getMessage());
//		}
//		return model;
//	}
	
//	@GetMapping(
//		    value = "/utility-requirement-stage",
//		    produces = MediaType.APPLICATION_JSON_VALUE
//		)
//		public ResponseEntity<?> getUtilityRequirementStage(HttpSession session) {
//
//		    try {
//		        Safety obj = new Safety(); // always non-null
//
//		        Safety utilityRequirementStageList =
//		                service.getUtilityRequirementStagesList(obj);
//
//		        return ResponseEntity.ok(
//		            Map.of(
//		                "success", true,
//		                "utilityRequirementStageList",
//		                utilityRequirementStageList
//		            )
//		        );
//
//		    } catch (Exception e) {
//		        logger.error("getUtilityRequirementStage", e);
//		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//		            Map.of(
//		                "success", false,
//		                "message", "Error while fetching Utility Requirement Stage list"
//		            )
//		        );
//		    }
//		}

	
	@GetMapping(
		    value = "/utility-requirement-stagee",
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<?> getUtilityRequirementStage(
		        @RequestBody(required = false) Safety obj) {

		    try {
		        if (obj == null) {
		            obj = new Safety();
		        }

		        Safety utilityRequirementStageList =
		                service.getUtilityRequirementStagesList(obj);

		        return ResponseEntity.ok(
		            Map.of(
		                "success", true,
		                "utilityRequirementStageList",
		                utilityRequirementStageList
		            )
		        );

		    } catch (Exception e) {
		        logger.error("getUtilityRequirementStage", e);
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
		            Map.of(
		                "success", false,
		                "message", "Error while fetching Utility Requirement Stage list"
		            )
		        );
		    }
		}

//	@RequestMapping(value = "/add-utility-requirement-stage", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addUtilityRequirementStage(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-requirement-stage");
//			boolean flag =  service.addUtilityRequirementStage(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", " Utility Requirement Stage Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding  Utility Requirement Stage is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding  Utility Requirement Stage is failed. Try again.");
//			logger.error("add UtilityRequirementStage : " + e.getMessage());
//		}
//		return model;
//	}
//	
	@PostMapping(
		    value = "/add-utility-requirement-stage",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> addUtilityRequirementStage(
		        @RequestBody Safety obj) {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        boolean flag = service.addUtilityRequirementStage(obj);

		        if (flag) {
		            response.put("status", "success");
		            response.put("message", "Utility Requirement Stage added successfully.");
		            return ResponseEntity.ok(response);
		        } else {
		            response.put("status", "error");
		            response.put("message", "Adding Utility Requirement Stage failed. Try again.");
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		        }

		    } catch (Exception e) {
		        logger.error("addUtilityRequirementStage : ", e);

		        response.put("status", "error");
		        response.put("message", "Internal server error.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}


//	@RequestMapping(value = "/update-utility-requirement-stage", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateUtilityRequirementStage(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-requirement-stage");
//			boolean flag =  service.updateUtilityRequirementStage(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Utility Requirement Stage Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Utility Requirement Stage is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Utility Requirement Stage is failed. Try again.");
//			logger.error("updateUtilityRequirementStage : " + e.getMessage());
//		}
//		return model;
//	}
	
	
	
	@PostMapping(
		    value = "/update-utility-requirement-stage",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> updateUtilityRequirementStage(
		        @RequestBody Safety obj) {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        boolean flag = service.updateUtilityRequirementStage(obj);

		        if (flag) {
		            response.put("status", "success");
		            response.put("message", "Utility Requirement Stage updated successfully.");
		            return ResponseEntity.ok(response);
		        } else {
		            response.put("status", "error");
		            response.put("message", "Updating Utility Requirement Stage failed. Try again.");
		            return ResponseEntity.badRequest().body(response);
		        }

		    } catch (Exception e) {
		        logger.error("updateUtilityRequirementStage : ", e);
		        response.put("status", "error");
		        response.put("message", "Internal server error.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}

//	@RequestMapping(value = "/delete-utility-requirement-stage", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView deleteUtilityRequirementStage(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-requirement-stage");
//			boolean flag =  service.deleteUtilityRequirementStage(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Utility Requirement Stage Deleted Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			logger.error("deleteUtilityRequirementStage : " + e.getMessage());
//		}
//		return model;
//	}	
	
	
	@DeleteMapping(
		    value = "/delete-utility-requirement-stage",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> deleteUtilityRequirementStage(
		        @RequestBody Safety obj) {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        boolean flag = service.deleteUtilityRequirementStage(obj);

		        if (flag) {
		            response.put("status", "success");
		            response.put("message", "Utility Requirement Stage deleted successfully.");
		            return ResponseEntity.ok(response);
		        } else {
		            response.put("status", "error");
		            response.put("message", "Utility Requirement Stage not found or already deleted.");
		            return ResponseEntity.badRequest().body(response);
		        }

		    } catch (Exception e) {
		        logger.error("deleteUtilityRequirementStage : ", e);
		        response.put("status", "error");
		        response.put("message", "Internal server error.");
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }
		}

	
}









