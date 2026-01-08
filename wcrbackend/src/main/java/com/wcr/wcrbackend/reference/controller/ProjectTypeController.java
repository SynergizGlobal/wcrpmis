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
import com.wcr.wcrbackend.reference.Iservice.ProjectTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;


@Controller
public class ProjectTypeController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(ProjectTypeController.class);
	
	@Autowired
	ProjectTypeService service;
	
	@RequestMapping(value="/project-type",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView ProjectType(HttpSession session,@ModelAttribute TrainingType obj){
		ModelAndView model = new ModelAndView(PageConstants.projectType);
		try {
			
			List<TrainingType> ProjectType = service.getProjectType(obj);
			model.addObject("ProjectType",ProjectType);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("ProjectType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/add-project-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addProjectType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/project-type");
			boolean flag =  service.addProjectType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Project Type Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Project Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Project Type is failed. Try again.");
			logger.error("addProjectType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-project-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateProjectType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/project-type");
			boolean flag =  service.updateProjectType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Project Type Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Project Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Project Type is failed. Try again.");
			logger.error("updateProjectType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-project-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteProjectType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/project-type");
			boolean flag =  service.deleteProjectType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Project Type Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteProjectType : " + e.getMessage());
		}
		return model;
	}
	
	
}
