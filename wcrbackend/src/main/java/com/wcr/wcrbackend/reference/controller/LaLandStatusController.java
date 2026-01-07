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

import com.wcr.wcrbackend.reference.Iservice.LaLandStatusService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
public class LaLandStatusController {
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(LaLandStatusController.class);
	
	@Autowired
	LaLandStatusService service;
	
	@PostMapping("/la-land-status")
	public Map<String, Object> getLaLandStatus(@RequestBody TrainingType obj) {

		Map<String, Object> response = new HashMap<>();

		try {
			TrainingType landAcquisitionStatusDetails =
					service.getLandAcquisitionStatusDetails(obj);

			response.put("status", "SUCCESS");
			response.put("landAcquisitionStatusDetails",
					landAcquisitionStatusDetails);

		} catch (Exception e) {
			logger.error("laLandStatus : " + e.getMessage(), e);
			response.put("status", "ERROR");
			response.put("message", "Failed to load Land Acquisition Status");
		}

		return response;
	}

	/* ================= ADD ================= */
	@PostMapping("/add-la-land-status")
	public Map<String, Object> addLaStatus(@RequestBody Safety obj) {

		Map<String, Object> response = new HashMap<>();

		try {
			boolean flag = service.addLaStatus(obj);

			if (flag) {
				response.put("status", "SUCCESS");
				response.put("message", "Status Added Successfully.");
			} else {
				response.put("status", "ERROR");
				response.put("message", "Adding Status failed. Try again.");
			}

		} catch (Exception e) {
			logger.error("addLaStatus : " + e.getMessage(), e);
			response.put("status", "ERROR");
			response.put("message", "Adding Status failed. Try again.");
		}

		return response;
	}

	/* ================= UPDATE ================= */
	@PostMapping("/update-la-land-status")
	public Map<String, Object> updateLandAcquisitionStatus(
			@RequestBody TrainingType obj) {

		Map<String, Object> response = new HashMap<>();

		try {
			boolean flag = service.updatelandAcquisitionStatus(obj);

			if (flag) {
				response.put("status", "SUCCESS");
				response.put("message", "Status Updated Successfully.");
			} else {
				response.put("status", "ERROR");
				response.put("message", "Updating Status failed. Try again.");
			}

		} catch (Exception e) {
			logger.error("updatelandAcquisitionStatus : " + e.getMessage(), e);
			response.put("status", "ERROR");
			response.put("message", "Updating Status failed. Try again.");
		}

		return response;
	}

	/* ================= DELETE ================= */
	@PostMapping("/delete-la-land-status")
	public Map<String, Object> deleteLandAcquisitionStatus(
			@RequestBody TrainingType obj) {

		Map<String, Object> response = new HashMap<>();

		try {
			boolean flag = service.deletelandAcquisitionStatus(obj);

			if (flag) {
				response.put("status", "SUCCESS");
				response.put("message", "Status Deleted Successfully.");
			} else {
				response.put("status", "ERROR");
				response.put("message", "Delete failed. Status may be in use.");
			}

		} catch (Exception e) {
			logger.error("deletelandAcquisitionStatus : " + e.getMessage(), e);
			response.put("status", "ERROR");
			response.put("message", "Delete failed. Try again.");
		}

		return response;
	}
	
	
}




