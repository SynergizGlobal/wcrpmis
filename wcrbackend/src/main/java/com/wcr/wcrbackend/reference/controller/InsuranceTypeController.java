package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
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
	
//	@RequestMapping(value="/insurance-type",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView insuranceType(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.insuranceType);
//		try {
//			List<TrainingType> insuranceType = service.getInsuranceTypesList();
//			model.addObject("insuranceType", insuranceType);
//			TrainingType insuranceTypesDetails = service.getInsuranceTypesDetails(obj);
//			model.addObject("insuranceTypesDetails",insuranceTypesDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("insuranceType : " + e.getMessage());
//		}
//		return model;
//	}
	
	@GetMapping("/insurance-type")
	public ResponseEntity<Map<String, Object>> getInsuranceType(HttpSession session) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        List<TrainingType> insuranceTypeList =
	                service.getInsuranceTypesList();

	        TrainingType insuranceTypesDetails =
	                service.getInsuranceTypesDetails(new TrainingType());

	        response.put("success", true);
	        response.put("insuranceType", insuranceTypeList);
	        response.put("insuranceTypesDetails", insuranceTypesDetails);

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("getInsuranceType : " + e.getMessage(), e);
	        response.put("success", false);
	        response.put("message", "Failed to fetch Insurance Type data");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

//	@RequestMapping(value = "/add-insurance-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addInsuranceType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/insurance-type");
//			boolean flag =  service.addInsuranceType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Insurance Type Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding Insurance Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding Insurance Type is failed. Try again.");
//			logger.error("addInsuranceType : " + e.getMessage());
//		}
//		return model;
//	}
	
	
	@PostMapping("/add-insurance-type")
	public ResponseEntity<Map<String, Object>> addInsuranceType(
	        @RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        boolean flag = service.addInsuranceType(obj);

	        if (flag) {
	            response.put("success", true);
	            response.put("message", "Insurance Type Added Successfully.");
	        } else {
	            response.put("success", false);
	            response.put("message", "Adding Insurance Type failed. Try again.");
	        }

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("addInsuranceType : " + e.getMessage(), e);
	        response.put("success", false);
	        response.put("message", "Adding Insurance Type failed. Try again.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

//	@RequestMapping(value = "/update-insurance-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateInsuranceTypes(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/insurance-type");
//			boolean flag =  service.updateInsuranceTypes(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Insurance Type Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Insurance Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Insurance Type is failed. Try again.");
//			logger.error("updateInsuranceTypes : " + e.getMessage());
//		}
//		return model;
//	}
//	
	
	
	@PostMapping("/update-insurance-type")
	public ResponseEntity<Map<String, Object>> updateInsuranceType(
	        @RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        boolean flag = service.updateInsuranceTypes(obj);

	        if (flag) {
	            response.put("success", true);
	            response.put("message", "Insurance Type Updated Successfully.");
	        } else {
	            response.put("success", false);
	            response.put("message", "Updating Insurance Type failed. Try again.");
	        }

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("updateInsuranceType : " + e.getMessage(), e);
	        response.put("success", false);
	        response.put("message", "Updating Insurance Type failed. Try again.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
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









