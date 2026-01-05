package com.wcr.wcrbackend.reference.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.AsBuiltStatusService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;


@RestController
public class AsBuiltStatusController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(AsBuiltStatusController.class);
	
	@Autowired
	AsBuiltStatusService service;
	
//	@RequestMapping(value = "/as-built-status1", method = { RequestMethod.GET, RequestMethod.POST })
//	public TrainingType asBuiltStatus1(HttpSession session, @RequestBody TrainingType obj) {
////		ModelAndView model = new ModelAndView();
//		TrainingType asBuiltStatusDetails = null;
//		try {
//			asBuiltStatusDetails = service.getAsBuiltStatusDetails(obj);
////			model.addObject("asBuiltStatusDetails",asBuiltStatusDetails);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("asBuiltStatus :" + e.getMessage());
//		}
//		return asBuiltStatusDetails;
//	}
//	
	@RequestMapping(
		    value = "/as-built-status",
		    method = { RequestMethod.GET, RequestMethod.POST }
		)
		@ResponseBody
		public List<String> asBuiltStatus(
		        HttpSession session,
		        @ModelAttribute TrainingType obj) {

		    List<String> list = new ArrayList<>();

		    try {
		        TrainingType details = service.getAsBuiltStatusDetails(obj);

		        // Example: assuming details.getdList() contains data
		        if (details != null && details.getdList() != null) {
		            details.getdList().forEach(t ->
		                list.add(t.getAs_built_status())
		            );
		        }

		    } catch (Exception e) {
		        logger.error("asBuiltStatus : ", e);
		    }

		    return list;
		}

	
	
	@RequestMapping(value = "/add-as-built-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addAsBuiltStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/as-built-status");
			boolean flag =  service.addAsBuiltStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Status is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Status is failed. Try again.");
			logger.error("addAsBuiltStatus : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-as-built-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateAsBuiltStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/as-built-status");
			boolean flag =  service.updateAsBuiltStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Status is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Status is failed. Try again.");
			logger.error("updateAsBuiltStatus : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/delete-as-built-status", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteAsBuiltStatus(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/as-built-status");
			boolean flag =  service.deleteAsBuiltStatus(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Status Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteAsBuiltStatus : " + e.getMessage());
		}
		return model;
	}
	
}




