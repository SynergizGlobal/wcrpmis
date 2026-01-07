package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.reference.Iservice.LAFileTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
public class LAFileTypeController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(LAFileTypeController.class);
	
	@Autowired
	LAFileTypeService service;
	
	@PostMapping("/la-file-type")
	public Map<String, Object> laFileType(@RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        TrainingType laFileTypeDetails = service.getLAFileTypeDetails(obj);

	        response.put("status", "SUCCESS");
	        response.put("laFileTypeDetails", laFileTypeDetails);

	    } catch (Exception e) {
	        logger.error("laFileType : " + e.getMessage(), e);
	        response.put("status", "ERROR");
	        response.put("message", "Failed to load LA File Type details.");
	    }

	    return response;
	}

	
	@PostMapping("/add-la-file-type")
	public Map<String, Object> addLAFileType(@RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        boolean flag = service.addLAFileType(obj);

	        if (flag) {
	            response.put("status", "SUCCESS");
	            response.put("message", "LA File Type Added Successfully.");
	        } else {
	            response.put("status", "ERROR");
	            response.put("message", "Adding LA File Type failed. Try again.");
	        }

	    } catch (Exception e) {
	        logger.error("addLAFileType : " + e.getMessage(), e);
	        response.put("status", "ERROR");
	        response.put("message", "Adding LA File Type failed. Try again.");
	    }

	    return response;
	}

	
	@PostMapping("/update-la-file-type")
	public Map<String, Object> updateLAFileType(@RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        boolean flag = service.updateLAFileType(obj);

	        if (flag) {
	            response.put("status", "SUCCESS");
	            response.put("message", "LA File Type Updated Successfully.");
	        } else {
	            response.put("status", "ERROR");
	            response.put("message", "Updating LA File Type failed. Try again.");
	        }

	    } catch (Exception e) {
	        logger.error("updateLAFileType : " + e.getMessage(), e);
	        response.put("status", "ERROR");
	        response.put("message", "Updating LA File Type failed. Try again.");
	    }

	    return response;
	}

	
	@PostMapping("/delete-la-file-type")
	public Map<String, Object> deleteLAFileType(@RequestBody TrainingType obj) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        boolean flag = service.deleteLAFileType(obj);

	        if (flag) {
	            response.put("status", "SUCCESS");
	            response.put("message", "LA File Type Deleted Successfully.");
	        } else {
	            response.put("status", "ERROR");
	            response.put("message", "Delete failed. Try again.");
	        }

	    } catch (Exception e) {
	        logger.error("deleteLAFileType : " + e.getMessage(), e);
	        response.put("status", "ERROR");
	        response.put("message", "Delete failed. Try again.");
	    }

	    return response;
	}

	
	
}



