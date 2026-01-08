package com.wcr.wcrbackend.reference.controller;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.DocumentTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class DocumentTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(DocumentTypeController.class);
	
	@Autowired
	DocumentTypeService service;
	
	@RequestMapping(value="/document-type",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView documentType(HttpSession session,@ModelAttribute TrainingType obj){
		ModelAndView model = new ModelAndView(PageConstants.documentType);
		try {
			List<TrainingType> documentTypeList = service.getDocumentTypesList();
			model.addObject("documentTypeList", documentTypeList);
			TrainingType documentTypeDetails = service.getDocumentTypeDetails(obj);
			model.addObject("documentTypeDetails",documentTypeDetails);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("documentType : " + e.getMessage());
		}
		return model;
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
	public ModelAndView updateDocumentType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/document-type");
			boolean flag =  service.updateDocumentType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Document Type Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Document Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Document Type is failed. Try again.");
			logger.error("updateDocumentType : " + e.getMessage());
		}
		return model;
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





