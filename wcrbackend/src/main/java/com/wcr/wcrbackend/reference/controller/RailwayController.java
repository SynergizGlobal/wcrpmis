package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.reference.Iservice.RailwayService;
import com.wcr.wcrbackend.reference.model.Risk;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class RailwayController {
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	Logger logger = Logger.getLogger(RailwayController.class);

	@Autowired
	RailwayService service;

	@GetMapping("/get-railway")
	public Map<String, Object> railway(HttpSession session) {
		Map<String, Object> res = new HashMap<>();
		TrainingType obj = new TrainingType();
		try {
			res.put("railwayList", service.getRailwayList());
			res.put("railwayDetails", service.getRailwayDetails(obj));
			res.put("message", "success");
		} catch (Exception e) {
			res.put("message", "Failed to fetch");
		}
		return res;
	}

	@RequestMapping(value = "/add-railway", method = { RequestMethod.POST })
	@ResponseBody
	public ModelAndView addRailway(@ModelAttribute Risk obj, RedirectAttributes attributes) {
		ModelAndView model = new ModelAndView();
		try {
			model.setViewName("redirect:/railway");
			boolean flag = service.addRailway(obj);
			if (flag) {
				attributes.addFlashAttribute("success", "Railway Added Succesfully.");
			} else {
				attributes.addFlashAttribute("error", "Adding Railway is failed. Try again.");
			}
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Adding Railway is failed. Try again.");
			logger.error("addRailway : " + e.getMessage());
		}
		return model;
	}

	@RequestMapping(value = "/update-railway", method = { RequestMethod.POST })
	@ResponseBody
	public ModelAndView updateRailway(@ModelAttribute TrainingType obj, RedirectAttributes attributes) {
		ModelAndView model = new ModelAndView();
		try {
			model.setViewName("redirect:/railway");
			boolean flag = service.updateRailway(obj);
			if (flag) {
				attributes.addFlashAttribute("success", "Railway Updated Succesfully.");
			} else {
				attributes.addFlashAttribute("error", "Updating Railway is failed. Try again.");
			}
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Updating Railway is failed. Try again.");
			logger.error("updateRailway : " + e.getMessage());
		}
		return model;
	}

	@RequestMapping(value = "/delete-railway", method = { RequestMethod.POST })
	@ResponseBody
	public ModelAndView deleteRailway(@ModelAttribute TrainingType obj, RedirectAttributes attributes) {
		ModelAndView model = new ModelAndView();
		try {
			model.setViewName("redirect:/railway");
			boolean flag = service.deleteRailway(obj);
			if (flag) {
				attributes.addFlashAttribute("success", "Railway Deleted Succesfully.");
			} else {
				attributes.addFlashAttribute("error", "Something went Wrong. Try again.");
			}
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Something went Wrong. Try again.");
			logger.error("deleteRailway : " + e.getMessage());
		}
		return model;
	}

}
