package com.wcr.wcrbackend.reference.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.RrResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.Iservice.UtilityResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;


@Controller
public class UtilityResponsibleExecutivesController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(UtilityResponsibleExecutivesController.class);
	@Autowired
	RrResponsibleExecutivesService service;
	
	
	@Autowired
	UtilityResponsibleExecutivesService mainService;
	
//	@RequestMapping(value="/utility-shifting-executives",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView executives(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.UtilityExecutives);
//		try {
//			
//			List<TrainingType> executivesDetails = mainService.getExecutivesDetails(obj);
//			model.addObject("executivesDetails",executivesDetails);
//			
//			List<TrainingType> workDetails = mainService.getWorkDetails(obj);
//			model.addObject("workDetails",workDetails);
//			
//			List<TrainingType> usersDetails = service.getUsersDetails(obj);
//			model.addObject("usersDetails",usersDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("executives : " + e.getMessage());
//		}
//		return model;
//	}
	
	@GetMapping(
		    value = "/utility-shifting-executives",
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<Map<String, Object>> getUtilityShiftingExecutives() {

		    Map<String, Object> response = new HashMap<>();

		    try {
		        // Explicit object creation (important for DAO safety)
		        TrainingType obj = new TrainingType();

		        List<TrainingType> executivesDetails =
		                mainService.getExecutivesDetails(obj);

		        List<TrainingType> workDetails =
		                mainService.getWorkDetails(obj);

		        List<TrainingType> usersDetails =
		                service.getUsersDetails(obj);

		        response.put("status", "success");
		        response.put("executivesDetails", executivesDetails);
		        response.put("workDetails", workDetails);
		        response.put("usersDetails", usersDetails);

		        return ResponseEntity.ok(response);

		    } catch (Exception e) {
		        logger.error("getUtilityShiftingExecutives : ", e);

		        response.put("status", "error");
		        response.put(
		            "message",
		            "Failed to fetch utility shifting executives details"
		        );

		        return ResponseEntity
		                .status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(response);
		    }
		}

	
	@RequestMapping(value = "/add-utility-shifting-executives", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addUtilityShiftingExecutives(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/utility-shifting-executives");
			boolean flag =  mainService.addUtilityShiftingExecutives(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Executives Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Executives is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Executives is failed. Try again.");
			logger.error("addUtilityUtilityisitionExecutives : " + e.getMessage());
		}
		return model;
	}
	@RequestMapping(value = "/update-utility-shifting-executives", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateUtilityShiftingExecutives(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/utility-shifting-executives");
			boolean flag =  mainService.updateUtilityShiftingExecutives(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Executives Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Executives is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Executives is failed. Try again.");
			logger.error("updateUtilityShiftingExecutives : " + e.getMessage());
		}
		return model;
	}
}
