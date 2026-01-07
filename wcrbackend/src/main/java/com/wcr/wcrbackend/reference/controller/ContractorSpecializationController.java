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
import com.wcr.wcrbackend.reference.Iservice.ContractorSpecializationService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;



@Controller
public class ContractorSpecializationController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(ContractorSpecializationController.class);
	
	@Autowired
	ContractorSpecializationService service;
	
//	@RequestMapping(value="/contractor-specialization",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView contractorSpecialization(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.contractorSpecialization);
//		try {
//			List<TrainingType> contractorSpecializationList = service.getContractorSpecializationsList();
//			model.addObject("contractorSpecializationList", contractorSpecializationList);
//			TrainingType contractorSpecializationDetails = service.getContractorSpecializationDetails(obj);
//			model.addObject("contractorSpecializationDetails", contractorSpecializationDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("contractorSpecialization : " + e.getMessage());
//		}
//		return model;
//	}
	
//	   @GetMapping("/contractor-specialization")
//	    public ResponseEntity<Map<String, Object>> getContractorSpecialization(
//	            HttpSession session,
//	            @RequestBody(required = false) TrainingType obj) {
//
//	        Map<String, Object> response = new HashMap<>();
//
//	        try {
//	            List<TrainingType> contractorSpecializationList =
//	                    service.getContractorSpecializationsList();
//
//	            TrainingType contractorSpecializationDetails =
//	                    service.getContractorSpecializationDetails(obj);
//
//	            response.put("success", true);
//	            response.put("contractorSpecializationList", contractorSpecializationList);
//	            response.put("contractorSpecializationDetails", contractorSpecializationDetails);
//
//	            return ResponseEntity.ok(response);
//
//	        } catch (Exception e) {
//	          
//	            response.put("success", false);
//	            response.put("message", "Failed to fetch Contractor Specialization data");
//
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//	        }
//	    }
	
	@GetMapping("/contractor-specialization")
	public ResponseEntity<Map<String, Object>> getContractorSpecialization(
	        HttpSession session) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        List<TrainingType> contractorSpecializationList =
	                service.getContractorSpecializationsList();

	        TrainingType contractorSpecializationDetails =
	                service.getContractorSpecializationDetails(new TrainingType());

	        response.put("success", true);
	        response.put("contractorSpecializationList", contractorSpecializationList);
	        response.put("contractorSpecializationDetails", contractorSpecializationDetails);

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "Failed to fetch Contractor Specialization data");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

//	@GetMapping("/contractor-specialization")
//	public ResponseEntity<Map<String, Object>> getContractorSpecialization(
//	        HttpSession session) {
//
//	    Map<String, Object> response = new HashMap<>();
//
//	    try {
//	        List<TrainingType> contractorSpecializationList =
//	                service.getContractorSpecializationsList();
//
//	        TrainingType contractorSpecializationDetails =
//	                service.getContractorSpecializationDetails(null);
//
//	        response.put("success", true);
//	        response.put("contractorSpecializationList", contractorSpecializationList);
//	        response.put("contractorSpecializationDetails", contractorSpecializationDetails);
//
//	        return ResponseEntity.ok(response);
//
//	    } catch (Exception e) {
//	        response.put("success", false);
//	        response.put("message", "Failed to fetch Contractor Specialization data");
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//	    }
//	}

//	@RequestMapping(value = "/add-contractor-specialization", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addContractorSpecialization(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contractor-specialization");
//			boolean flag =  service.addContractorSpecialization(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contractor Specialization Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding Contractor Specialization is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding Contractor Specialization is failed. Try again.");
//			logger.error("addContractorSpecialization : " + e.getMessage());
//		}
//		return model;
//	}
//	
	   
	   
	   @PostMapping("/add-contractor-specialization")
	    public ResponseEntity<Map<String, Object>> addContractorSpecialization(
	            @RequestBody TrainingType obj) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            boolean flag = service.addContractorSpecialization(obj);

	            if (flag) {
	                response.put("success", true);
	                response.put("message", "Contractor Specialization Added Successfully.");
	                return ResponseEntity.ok(response);
	            } else {
	                response.put("success", false);
	                response.put("message", "Adding Contractor Specialization failed. Try again.");
	                return ResponseEntity.badRequest().body(response);
	            }

	        } catch (Exception e) {
	         response.put("success", false);
	            response.put("message", "Internal Server Error");

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
//	@RequestMapping(value = "/update-contractor-specialization", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateContractorSpecialization(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contractor-specialization");
//			boolean flag =  service.updateContractorSpecialization(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contractor Specialization Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Contractor Specialization is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Contractor Specialization is failed. Try again.");
//			logger.error("updateContractorSpecialization : " + e.getMessage());
//		}
//		return model;
//	}
	
	   
	   
	   @PostMapping("/update-contractor-specialization")
	    public ResponseEntity<Map<String, Object>> updateContractorSpecialization(
	            @RequestBody TrainingType obj) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            boolean flag = service.updateContractorSpecialization(obj);

	            if (flag) {
	                response.put("success", true);
	                response.put("message", "Contractor Specialization Updated Successfully.");
	                return ResponseEntity.ok(response);
	            } else {
	                response.put("success", false);
	                response.put("message", "Updating Contractor Specialization failed. Try again.");
	                return ResponseEntity.badRequest().body(response);
	            }

	        } catch (Exception e) {
	           

	            response.put("success", false);
	            response.put("message", "Internal Server Error");

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
//	@RequestMapping(value = "/delete-contractor-specialization", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView deleteContractorSpecialization(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contractor-specialization");
//			boolean flag =  service.deleteContractorSpecialization(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contractor Specialization Deleted Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			logger.error("deleteContractorSpecialization : " + e.getMessage());
//		}
//		return model;
//	}
//	
	   
	   
	   @DeleteMapping("/delete-contractor-specialization")
	    public ResponseEntity<Map<String, Object>> deleteContractorSpecialization(
	            @RequestBody TrainingType obj) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            boolean flag = service.deleteContractorSpecialization(obj);

	            if (flag) {
	                response.put("success", true);
	                response.put("message", "Contractor Specialization Deleted Successfully.");
	                return ResponseEntity.ok(response);
	            } else {
	                response.put("success", false);
	                response.put("message", "Something went wrong. Try again.");
	                return ResponseEntity.badRequest().body(response);
	            }

	        } catch (Exception e) {
	         
	            response.put("success", false);
	            response.put("message", "Internal Server Error");

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
	   
	   
}





