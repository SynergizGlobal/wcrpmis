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
import com.wcr.wcrbackend.reference.Iservice.BackgroundTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;


@Controller
public class BackgroundTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(BackgroundTypeController.class);
	
	@Autowired
	
	BackgroundTypeService service;
	
//	@RequestMapping(value="/bank-guarantee-type",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView backgroundType(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.bankGuaranteeType);
//		try {
//			List<TrainingType> backgroundTypeList = service.getBackgroundTypesList();
//			model.addObject("backgroundTypeList", backgroundTypeList);
//			TrainingType bankGuaranteeDetails = service.getBankGuaranteeDetails(obj);
//			model.addObject("bankGuaranteeDetails", bankGuaranteeDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("backgroundType : " + e.getMessage());
//		}
//		return model;
//	}
	
	

    @PostMapping("/bank-guarantee-type")
    public ResponseEntity<Map<String, Object>> getBankGuaranteeType(
            HttpSession session,
            @RequestBody(required = false) TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<TrainingType> backgroundTypeList = service.getBackgroundTypesList();
            TrainingType bankGuaranteeDetails = service.getBankGuaranteeDetails(obj);

            response.put("success", true);
            response.put("backgroundTypeList", backgroundTypeList);
            response.put("bankGuaranteeDetails", bankGuaranteeDetails);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
           

            response.put("success", false);
            response.put("message", "Something went wrong while fetching Bank Guarantee details");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
	
//	@RequestMapping(value = "/add-bank-guarantee-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addBackgroundType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/bank-guarantee-type");
//			boolean flag =  service.addBackgroundType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Bank Guarantee Type Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding Bank Guarantee Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding Bank Guarantee Type is failed. Try again.");
//			logger.error("addBackgroundType : " + e.getMessage());
//		}
//		return model;
//	}
//	
    
    
    @PostMapping("/add-bank-guarantee-type")
    public ResponseEntity<Map<String, Object>> addBankGuaranteeType(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag = service.addBackgroundType(obj);

            if (flag) {
                response.put("success", true);
                response.put("message", "Bank Guarantee Type Added Successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Adding Bank Guarantee Type failed. Try again.");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
         
            response.put("success", false);
            response.put("message", "Internal Server Error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
//	@RequestMapping(value = "/update-bank-guarantee-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateBackgroundType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/bank-guarantee-type");
//			boolean flag =  service.updateBackgroundType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Bank Guarantee Type Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Bank Guarantee Type is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Bank Guarantee Type is failed. Try again.");
//			logger.error("updateBackgroundType : " + e.getMessage());
//		}
//		return model;
//	}
//	
    
    @PostMapping("/update-bank-guarantee-type")
    public ResponseEntity<Map<String, Object>> updateBankGuaranteeType(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag = service.updateBackgroundType(obj);

            if (flag) {
                response.put("success", true);
                response.put("message", "Bank Guarantee Type Updated Successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Updating Bank Guarantee Type failed. Try again.");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
 
            response.put("success", false);
            response.put("message", "Internal Server Error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
//	@RequestMapping(value = "/delete-bank-guarantee-type", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView deleteBankGuaranteeType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/bank-guarantee-type");
//			boolean flag =  service.deleteBankGuaranteeType(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Bank Guarantee Type Deleted Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			logger.error("deleteBankGuaranteeType : " + e.getMessage());
//		}
//		return model;
//	}
    

    @DeleteMapping("/delete-bank-guarantee-type")
    public ResponseEntity<Map<String, Object>> deleteBankGuaranteeType(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag = service.deleteBankGuaranteeType(obj);

            if (flag) {
                response.put("success", true);
                response.put("message", "Bank Guarantee Type Deleted Successfully.");
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




