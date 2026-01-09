package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.DocumentTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class DocumentTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(DocumentTypeController.class);
	
	@Autowired
	DocumentTypeService service;
	
	@RequestMapping(value="/document-type",method={RequestMethod.GET,RequestMethod.POST})
	public Map<String, Object> documentType(HttpSession session){
		Map<String, Object> res = new HashMap<>();
		TrainingType obj = new TrainingType();
		try {
			List<TrainingType> documentTypeList = service.getDocumentTypesList();
			res.put("documentTypeList", documentTypeList);
			TrainingType documentTypeDetails = service.getDocumentTypeDetails(obj);
			res.put("documentTypeDetails",documentTypeDetails);
			res.put("message", "success");
		}catch (Exception e) {
			e.printStackTrace();
			res.put("error", e);
			res.put("message","failed to fetch (or) something went wrong..");
			logger.error("documentType : " + e.getMessage());
		}
		return res;
	}
	
	
	@RequestMapping(value = "/add-document-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addDocumentType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/document-type");
			boolean flag =  service.addDocumentType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Document Type Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Document Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Document Type is failed. Try again.");
			logger.error("addDocumentType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-document-type", method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> updateDocumentType(@ModelAttribute TrainingType obj){
		Map<String, Object> res = new HashMap<>();
		try{
			boolean flag =  service.updateDocumentType(obj);
			if(flag) {
				res.put("message", "Document Type Updated Succesfully.");
			}
			else {
				res.put("message	","Updating Document Type is failed. Try again.");
			}
		}catch (Exception e) {
			res.put("error",e);
			res.put("error", "Failed to update");
		}
		return res;
	}
	
	@RequestMapping(value = "/delete-document-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteDocumentType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/document-type");
			boolean flag =  service.deleteDocumentType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Document Type Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteDocumentType : " + e.getMessage());
		}
		return model;
	}
	
}





