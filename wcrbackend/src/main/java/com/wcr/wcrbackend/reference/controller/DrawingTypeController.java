package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
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

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.DrawingTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@Controller
public class DrawingTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(DrawingTypeController.class);
	
	@Autowired
	DrawingTypeService service;
	
	@RequestMapping(value = "/drawing-type", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> drawingType(HttpSession session, @RequestBody TrainingType obj) {
//		ModelAndView model = new ModelAndView(PageConstants.drawingType);
		Map<String, Object> result = new HashMap<>();

		try {
			List<TrainingType> drawingTypeList = service.getDrawingTypesList();
			result.put("drawingTypeList", drawingTypeList);
			TrainingType drawingTypeDetails = service.getDrawingTypeDetails(obj);
			result.put("drawingTypeDetails", drawingTypeDetails);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("drawingType : " + e.getMessage());
		}
		return result;
	}
	
	
	
	@RequestMapping(value = "/add-drawing-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addDrawingType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/drawing-type");
			boolean flag =  service.addDrawingType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Drawing Type Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Drawing Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Drawing Type is failed. Try again.");
			logger.error("addDrawingType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/update-drawing-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateDrawingType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/drawing-type");
			boolean flag =  service.updateDrawingType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Drawing Type Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Drawing Type is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Drawing Type is failed. Try again.");
			logger.error("updateDrawingType : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-drawing-type", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteDrawingType(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/drawing-type");
			boolean flag =  service.deleteDrawingType(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Drawing Type Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteDrawingType : " + e.getMessage());
		}
		return model;
	}
	
}







