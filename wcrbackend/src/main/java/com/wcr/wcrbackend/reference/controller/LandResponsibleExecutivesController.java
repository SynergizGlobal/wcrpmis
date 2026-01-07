package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.reference.Iservice.LandResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.Iservice.RrResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;
@RestController
public class LandResponsibleExecutivesController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(LandResponsibleExecutivesController.class);
	@Autowired
	RrResponsibleExecutivesService service;
	
	
	@Autowired
	LandResponsibleExecutivesService mainService;


	    @PostMapping("/land-executives")
	    public Map<String, Object> getLandExecutives(
	            HttpSession session,
	            @RequestBody TrainingType obj) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	        	List<TrainingType> projectDetails = mainService.getProjectDetails(obj);
	        	
	            List<TrainingType> executivesDetails =
	                    mainService.getExecutivesDetails(obj);

	            List<TrainingType> usersDetails =
	                    service.getUsersDetails(obj);
	            response.put("projectDetails", projectDetails);
	            response.put("executivesDetails", executivesDetails);
	            response.put("usersDetails", usersDetails);
	            response.put("status", "SUCCESS");


	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("executives : " + e.getMessage());

	            response.put("status", "ERROR");
	            response.put("message", e.getMessage());
	        }

	        return response;
	    }
	

	
	    @PostMapping("/add-land-executives")
	    public Map<String, Object> addLandAcquisitionExecutives(
	            @RequestBody TrainingType obj) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            boolean flag = mainService.addLandAcquisitionExecutives(obj);

	            if (flag) {
	                response.put("status", "SUCCESS");
	                response.put("message", "Executives Added Successfully.");
	            } else {
	                response.put("status", "ERROR");
	                response.put("message", "Adding Executives failed. Try again.");
	            }

	        } catch (Exception e) {
	            logger.error("addLandAcquisitionExecutives : " + e.getMessage(), e);
	            response.put("status", "ERROR");
	            response.put("message", "Adding Executives failed. Try again.");
	        }

	        return response;
	    }

	    @PostMapping("/update-land-executives")
	    public Map<String, Object> updateLandAcquisitionExecutives(
	            @RequestBody TrainingType obj) {

	        Map<String, Object> response = new HashMap<>();

	        try {
	            boolean flag = mainService.updateLandAcquisitionExecutives(obj);

	            if (flag) {
	                response.put("status", "SUCCESS");
	                response.put("message", "Executives Updated Successfully.");
	            } else {
	                response.put("status", "ERROR");
	                response.put("message", "Updating Executives failed. Try again.");
	            }

	        } catch (Exception e) {
	            logger.error("updateLandAcquisitionExecutives : " + e.getMessage(), e);
	            response.put("status", "ERROR");
	            response.put("message", "Updating Executives failed. Try again.");
	        }

	        return response;
	    }

}
