package com.wcr.wcrbackend.reference.controller;



import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.reference.Iservice.StageService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class StageController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(StageController.class);
	
	@Autowired
	StageService service;
	
//	@RequestMapping(value="/stage",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView stage(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.stage);
//		try {
//			
//			TrainingType stageDetails = service.getStageDetails(obj);
//			model.addObject("stageDetails",stageDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("stage : " + e.getMessage());
//		}
//		return model;
//	}
//	
	
	
	@RequestMapping(
		    value = "/stage",
		    method = { RequestMethod.GET, RequestMethod.POST }
		)
		@ResponseBody
		public Map<String, Object> stage(
		        HttpSession session,
		        @RequestBody(required = false) TrainingType obj
		) {
		    Map<String, Object> result = new HashMap<>();

		    try {
		        TrainingType stageDetails = service.getStageDetails(obj);

		        result.put("stageDetails", stageDetails);
		        result.put("status", "success");

		    } catch (Exception e) {
		        e.printStackTrace();
		        logger.error("stage : " + e.getMessage());

		        result.put("status", "error");
		        result.put("message", "Failed to fetch Stage details");
		    }

		    return result;
		}

	
	@RequestMapping(value = "/add-stage", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addStage(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/stage");
			boolean flag =  service.addStage(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Stage Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Stage is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Stage is failed. Try again.");
			logger.error("addStage : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-stage", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateStage(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/stage");
			boolean flag =  service.updateStage(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Stage Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Stage is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Stage is failed. Try again.");
			logger.error("updateStage : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-stage", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteStage(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/stage");
			boolean flag =  service.deleteStage(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Stage Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteStage : " + e.getMessage());
		}
		return model;
	}
	
}


