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
import com.wcr.wcrbackend.reference.Iservice.UtilityAlignmentService;
import com.wcr.wcrbackend.reference.model.Safety;

import jakarta.servlet.http.HttpSession;


@Controller
public class UtilityAlignmentController {
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(UtilityAlignmentController.class);
	
	@Autowired
	UtilityAlignmentService service;
	
	@RequestMapping(value="/utility-alignment",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView utilityAlignment(HttpSession session){
		ModelAndView model = new ModelAndView(PageConstants.utilityAlignment);
		try {
			List<Safety>  utilityAlignmentList = service.getUtilityAlignmentsList();
			model.addObject("utilityAlignmentList",  utilityAlignmentList);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(" utilityAlignment : " + e.getMessage());
		}
		return model;
	}
	
	
	@RequestMapping(value = "/add-utility-alignment", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView addUtilityAlignment(@ModelAttribute Safety obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/utility-alignment");
			boolean flag =  service.addUtilityAlignment(obj);
			if(flag) {
				attributes.addFlashAttribute("success", " Utility Alignment Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding  Utility Alignment is failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Adding  Utility Alignment is failed. Try again.");
			logger.error("add UtilityAlignment : " + e.getMessage());
		}
		return model;
	}
	
}








