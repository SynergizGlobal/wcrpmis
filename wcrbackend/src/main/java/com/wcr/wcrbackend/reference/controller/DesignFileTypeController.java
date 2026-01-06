package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.reference.Iservice.DesignFileTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class DesignFileTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(DesignFileTypeController.class);
	
	@Autowired
	DesignFileTypeService service;
	
	/*
	 * @RequestMapping(value="/design-file-type",method={RequestMethod.GET,
	 * RequestMethod.POST}) public ModelAndView designFileType(HttpSession
	 * session,@ModelAttribute TrainingType obj){ ModelAndView model = new
	 * ModelAndView(PageConstants.drawingFileType); try {
	 * 
	 * TrainingType designFileTypeDetails = service.getDesignFileTypeDetails(obj);
	 * model.addObject("designFileTypeDetails",designFileTypeDetails); }catch
	 * (Exception e) { e.printStackTrace(); logger.error("designFileType : " +
	 * e.getMessage()); } return model; }
	 */
	
	@RequestMapping(
		    value = "/design-file-type",
		    method = { RequestMethod.GET, RequestMethod.POST }
		)
		@ResponseBody
		public Map<String, Object> designFileType(
		        HttpSession session,
		        @RequestBody(required = false) TrainingType obj
		) {

		    Map<String, Object> result = new HashMap<>();

		    try {
		        TrainingType designFileTypeDetails =
		                service.getDesignFileTypeDetails(obj);

		        result.put("designFileTypeDetails", designFileTypeDetails);
		        result.put("status", "success");

		    } catch (Exception e) {
		        logger.error("designFileType : ", e);
		        result.put("status", "error");
		        result.put("message", "Failed to fetch Design File Type details");
		    }

		    return result;
		}

	
	@RequestMapping(value = "/add-design-file-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addDesignFileType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/design-file-type");
			boolean flag =  service.addDesignFileType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Design File Type Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Design File Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Design File Type is failed. Try again.");
			logger.error("addDesignFileType : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-design-file-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateDesignFileType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/design-file-type");
			boolean flag =  service.updateDesignFileType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Design File Type Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Design File Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Design File Type is failed. Try again.");
			logger.error("updateDesignFileType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-design-file-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteDesignFileType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/design-file-type");
			boolean flag =  service.deleteDesignFileType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Design File Type Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteDesignFileType : " + e.getMessage());
		}
		return model;
	}
	
}


