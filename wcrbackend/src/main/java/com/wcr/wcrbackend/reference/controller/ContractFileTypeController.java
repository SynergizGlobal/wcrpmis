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
import com.wcr.wcrbackend.reference.Iservice.ContarctFileTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class ContractFileTypeController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(ContractFileTypeController.class);
	
	@Autowired
	ContarctFileTypeService service;
	
//	@RequestMapping(value="/contract-file-type",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView contractFileType(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.contractFileType);
//		try {
//			
//			List<TrainingType> contractFileType = service.getcontractFileType(obj);
//			model.addObject("contractFileType",contractFileType);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("contractFileType : " + e.getMessage());
//		}
//		return model;
//	}
	
	
	  @GetMapping("/contract-file-type")
	    public ResponseEntity<Map<String, Object>> getContractFileType(
	            HttpSession session,
	            @RequestBody(required = false) TrainingType obj) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            List<TrainingType> contractFileType = service.getcontractFileType(obj);

	            response.put("success", true);
	            response.put("contractFileType", contractFileType);

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {

	            response.put("success", false);
	            response.put("message", "Failed to fetch Contract File Types");

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
	
//	@RequestMapping(value = "/add-contract-file-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addcontractFileType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contract-file-type");
//			boolean flag =  service.addContractFileType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contract File Type Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding Contract File Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding Contract File Type is failed. Try again.");
//			logger.error("addContractFileType : " + e.getMessage());
//		}
//		return model;
//	}
	
	  
	  
	    @PostMapping("/add-contract-file-type")
	    public ResponseEntity<Map<String, Object>> addContractFileType(
	            @RequestBody TrainingType obj) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            boolean flag = service.addContractFileType(obj);

	            if (flag) {
	                response.put("success", true);
	                response.put("message", "Contract File Type Added Successfully.");
	                return ResponseEntity.ok(response);
	            } else {
	                response.put("success", false);
	                response.put("message", "Adding Contract File Type failed. Try again.");
	                return ResponseEntity.badRequest().body(response);
	            }

	        } catch (Exception e) {
	        
	            response.put("success", false);
	            response.put("message", "Internal Server Error");

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
	  
	  
//	@RequestMapping(value = "/update-contract-file-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateContractFileType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contract-file-type");
//			boolean flag =  service.updateContractFileType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contract File Type Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Contract File Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Contract File Type is failed. Try again.");
//			logger.error("updateContractFileType : " + e.getMessage());
//		}
//		return model;
//	}
//	
	
	
	

    @PostMapping("/update-contract-file-type")
    public ResponseEntity<Map<String, Object>> updateContractFileType(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag = service.updateContractFileType(obj);

            if (flag) {
                response.put("success", true);
                response.put("message", "Contract File Type Updated Successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Updating Contract File Type failed. Try again.");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
      
            response.put("success", false);
            response.put("message", "Internal Server Error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
	
//	@RequestMapping(value = "/delete-contract-file-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView deleteContractFileType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/contract-file-type");
//			boolean flag =  service.deleteContractFileType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Contract File Type Deleted Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			logger.error("deleteContractFileType : " + e.getMessage());
//		}
//		return model;
//	}
//	
	
    
    
    @DeleteMapping("/delete-contract-file-type")
    public ResponseEntity<Map<String, Object>> deleteContractFileType(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag = service.deleteContractFileType(obj);

            if (flag) {
                response.put("success", true);
                response.put("message", "Contract File Type Deleted Successfully.");
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
