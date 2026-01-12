package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.reference.Iservice.ExecutionStatusService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;

@RestController
public class ExecutionStatusController {

    Logger logger = Logger.getLogger(ExecutionStatusController.class);

    @Autowired
    private ExecutionStatusService service;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(
    	    value = "/execution-status",
    	    method = { RequestMethod.GET }
    	)
    	public Map<String, Object> getExecutionStatusDetails(HttpSession session) {

    	    Map<String, Object> result = new HashMap<>();

    	    try {
    	        TrainingType obj = new TrainingType();
    	        TrainingType details = service.getExecutionStatusDetails(obj);

    	        result.put("executionStatusDetails", details);
    	        result.put("status", "success");
    	        
    	        System.out.println(
    	        		  "Tables size = " + details.getTablesList().size()
    	        		);

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        logger.error("execution-status : " + e.getMessage());

    	        result.put("status", "error");
    	        result.put("message", "Failed to fetch Execution Status");
    	    }

    	    return result;
    	}


    @RequestMapping(
        value = "/add-execution-status",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    public ModelAndView addExecutionStatus(
            @ModelAttribute TrainingType obj,
            RedirectAttributes attributes
    ) {
        ModelAndView model = new ModelAndView();
        try {
            model.setViewName("redirect:/execution-status");
            boolean flag = service.addExecutionStatus(obj);

            if (flag) {
                attributes.addFlashAttribute(
                    "success", "Execution Status Added Successfully."
                );
            } else {
                attributes.addFlashAttribute(
                    "error", "Adding Execution Status failed."
                );
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Something went wrong.");
            e.printStackTrace();
            logger.error("addExecutionStatus : " + e.getMessage());
        }
        return model;
    }


    @RequestMapping(
        value = "/update-execution-status",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    public ModelAndView updateExecutionStatus(
            @ModelAttribute TrainingType obj,
            RedirectAttributes attributes
    ) {
        ModelAndView model = new ModelAndView();
        try {
            model.setViewName("redirect:/execution-status");
            boolean flag = service.updateExecutionStatus(obj);

            if (flag) {
                attributes.addFlashAttribute(
                    "success", "Execution Status Updated Successfully."
                );
            } else {
                attributes.addFlashAttribute(
                    "error", "Update Execution Status failed."
                );
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Something went wrong.");
            e.printStackTrace();
            logger.error("updateExecutionStatus : " + e.getMessage());
        }
        return model;
    }

    @RequestMapping(
        value = "/delete-execution-status",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseBody
    public ModelAndView deleteExecutionStatus(
            @ModelAttribute TrainingType obj,
            RedirectAttributes attributes
    ) {
        ModelAndView model = new ModelAndView();
        try {
            model.setViewName("redirect:/execution-status");
            boolean flag = service.deleteExecutionStatus(obj);

            if (flag) {
                attributes.addFlashAttribute(
                    "success", "Execution Status Deleted Successfully."
                );
            } else {
                attributes.addFlashAttribute(
                    "error", "Delete Execution Status failed."
                );
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Something went wrong.");
            e.printStackTrace();
            logger.error("deleteExecutionStatus : " + e.getMessage());
        }
        return model;
    }
}