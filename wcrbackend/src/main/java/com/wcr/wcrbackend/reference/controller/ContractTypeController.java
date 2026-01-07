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
import com.wcr.wcrbackend.reference.Iservice.ContractTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;


@Controller
public class ContractTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(ContractTypeController.class);
	
	@Autowired
	ContractTypeService service;
	
//	@RequestMapping(value="/contract-type",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView contractType(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.contractType);
//		try {
//			List<TrainingType> contractTypeList = service.getContractTypesList();
//			model.addObject("contractTypeList", contractTypeList);
//			TrainingType contractTypeDetails = service.getContractTypeDetails(obj);
//			model.addObject("contractTypeDetails", contractTypeDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("contractType : " + e.getMessage());
//		}
//		return model;
//	}
	
	
	@GetMapping("/contract-type")
	public ResponseEntity<Map<String, Object>> getContractType(HttpSession session) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        List<TrainingType> contractTypeList =
	                service.getContractTypesList();

	        TrainingType contractTypeDetails =
	                service.getContractTypeDetails(new TrainingType());

	        response.put("success", true);
	        response.put("contractTypeList", contractTypeList);
	        response.put("contractTypeDetails", contractTypeDetails);

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("getContractType : " + e.getMessage(), e);
	        response.put("success", false);
	        response.put("message", "Failed to fetch Contract Type data");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	@PostMapping("/add-contract-type")
	public ResponseEntity<Map<String, Object>> addContractType(
	        @RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        boolean flag = service.addContractType(obj);

	        if (flag) {
	            response.put("success", true);
	            response.put("message", "Contract Type Added Successfully.");
	        } else {
	            response.put("success", false);
	            response.put("message", "Adding Contract Type failed. Try again.");
	        }

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("addContractType : " + e.getMessage(), e);
	        response.put("success", false);
	        response.put("message", "Adding Contract Type failed. Try again.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

//	@RequestMapping(value = "/add-contract-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addContractType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contract-type");
//			boolean flag =  service.addContractType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contract Type Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding Contract Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding Contract Type is failed. Try again.");
//			logger.error("addContractType : " + e.getMessage());
//		}
//		return model;
//	}
	
//	@RequestMapping(value = "/update-contract-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateContractType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contract-type");
//			boolean flag =  service.updateContractType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contract Type Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Contract Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Contract Type is failed. Try again.");
//			logger.error("updateContractType : " + e.getMessage());
//		}
//		return model;
//	}
	
	
	@PostMapping("/update-contract-type")
	public ResponseEntity<Map<String, Object>> updateContractType(
	        @RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        boolean flag = service.updateContractType(obj);

	        if (flag) {
	            response.put("success", true);
	            response.put("message", "Contract Type Updated Successfully.");
	        } else {
	            response.put("success", false);
	            response.put("message", "Updating Contract Type failed. Try again.");
	        }

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("updateContractType : " + e.getMessage(), e);
	        response.put("success", false);
	        response.put("message", "Updating Contract Type failed. Try again.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

//	@RequestMapping(value = "/delete-contract-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView deleteContractType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contract-type");
//			boolean flag =  service.deleteContractType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contract Type Deleted Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			logger.error("deleteContractType : " + e.getMessage());
//		}
//		return model;
//	}
	
	@DeleteMapping("/delete-contract-type")
	public ResponseEntity<Map<String, Object>> deleteContractType(
	        @RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        boolean flag = service.deleteContractType(obj);

	        if (flag) {
	            response.put("success", true);
	            response.put("message", "Contract Type Deleted Successfully.");
	        } else {
	            response.put("success", false);
	            response.put("message", "Deleting Contract Type failed. Try again.");
	        }

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        logger.error("deleteContractType : " + e.getMessage(), e);
	        response.put("success", false);
	        response.put("message", "Something went wrong. Try again.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

}






