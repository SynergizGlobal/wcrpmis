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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.reference.Iservice.StructureTypeService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class StructureTypeController {

    Logger logger = Logger.getLogger(StructureTypeController.class);

    @Autowired
    private StructureTypeService service;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }


    @RequestMapping(
        value = "/structure-type",
        method = { RequestMethod.GET }
    )
    public Map<String, Object> getStructureTypeDetails(HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        try {
            TrainingType obj = new TrainingType();

            result.put("structureTypes", service.getStructureTypesList());
            result.put("details", service.getStructureTypeDetails(obj));
            result.put("status", "success");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("structure-type : " + e.getMessage());

            result.put("status", "error");
            result.put("message", "Failed to fetch Structure Types");
        }

        return result;
    }

    
    @RequestMapping(
    	    value = "/add-structure-type",
    	    method = RequestMethod.POST,
    	    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    	)
    	@ResponseBody
    	public ModelAndView addStructureType(
    	        @ModelAttribute Safety obj,
    	        RedirectAttributes attributes
    	) {
    	    ModelAndView model = new ModelAndView();
    	    try {
    	        model.setViewName("redirect:/structure-type");
    	        boolean flag = service.addStructureType(obj);

    	        if (flag) {
    	            attributes.addFlashAttribute("success", "Structure Type Added Successfully.");
    	        } else {
    	            attributes.addFlashAttribute("error", "Adding Structure Type failed.");
    	        }
    	    } catch (Exception e) {
    	        attributes.addFlashAttribute("error", "Something went wrong.");
    	        e.printStackTrace();
    	    }
    	    return model;
    	}


    @RequestMapping(
    	    value = "/update-structure-type",
    	    method = RequestMethod.POST,
    	    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    	)
    	@ResponseBody
    	public ModelAndView updateStructureType(
    	        @ModelAttribute TrainingType obj,
    	        RedirectAttributes attributes
    	) {
    	    ModelAndView model = new ModelAndView();
    	    try {
    	        model.setViewName("redirect:/structure-type");
    	        boolean flag = service.updateStructureType(obj);

    	        if (flag) {
    	            attributes.addFlashAttribute("success", "Structure Type Updated Successfully.");
    	        } else {
    	            attributes.addFlashAttribute("error", "Update failed.");
    	        }
    	    } catch (Exception e) {
    	        attributes.addFlashAttribute("error", "Something went wrong.");
    	        e.printStackTrace();
    	    }
    	    return model;
    	}


    @RequestMapping(
    	    value = "/delete-structure-type",
    	    method = RequestMethod.POST,
    	    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    	)
    	@ResponseBody
    	public ModelAndView deleteStructureType(
    	        @ModelAttribute TrainingType obj,
    	        RedirectAttributes attributes
    	) {
    	    ModelAndView model = new ModelAndView();
    	    try {
    	        model.setViewName("redirect:/structure-type");
    	        boolean flag = service.deleteStructureType(obj);

    	        if (flag) {
    	            attributes.addFlashAttribute("success", "Structure Type Deleted Successfully.");
    	        } else {
    	            attributes.addFlashAttribute("error", "Delete failed.");
    	        }
    	    } catch (Exception e) {
    	        attributes.addFlashAttribute("error", "Something went wrong.");
    	        e.printStackTrace();
    	    }
    	    return model;
    	}
}