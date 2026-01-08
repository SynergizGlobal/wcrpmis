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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.reference.Iservice.RrResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.Iservice.UtilityResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;


@RestController
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

	

	@GetMapping(
		value = "/utility-shifting-executives",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, Object>> getUtilityShiftingExecutives(
			HttpSession session,
			@ModelAttribute TrainingType obj
	) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<TrainingType> executivesDetails =
					mainService.getExecutivesDetails(obj);

			List<TrainingType> projectDetails =
					mainService.getProjectDetails(obj);

			List<TrainingType> usersDetails =
					service.getUsersDetails(obj);

			response.put("executivesDetails", executivesDetails);
			response.put("projectDetails", projectDetails);
			response.put("usersDetails", usersDetails);
			response.put("status", "SUCCESS"); // ✅ MATCH REACT

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("getUtilityShiftingExecutives : ", e);

			response.put("status", "ERROR");
			response.put(
				"message",
				"Failed to fetch utility shifting executives details"
			);

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(response);
		}
	}

	@PostMapping(
		value = "/add-utility-shifting-executives",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, Object>> addUtilityShiftingExecutives(
			@RequestBody TrainingType obj
	) {
		Map<String, Object> response = new HashMap<>();

		try {
			boolean flag = mainService.addUtilityShiftingExecutives(obj);

			if (flag) {
				response.put("status", "SUCCESS");
				response.put("message", "Executives added successfully");
			} else {
				response.put("status", "ERROR");
				response.put("message", "Adding executives failed");
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("addUtilityShiftingExecutives : ", e);

			response.put("status", "ERROR");
			response.put("message", "Server error");

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(response);
		}
	}

	@PostMapping(
		value = "/update-utility-shifting-executives",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, Object>> updateUtilityShiftingExecutives(
			@RequestBody TrainingType obj
	) {
		Map<String, Object> response = new HashMap<>();

		try {
			boolean flag = mainService.updateUtilityShiftingExecutives(obj);

			if (flag) {
				response.put("status", "SUCCESS");
				response.put("message", "Executives updated successfully");
			} else {
				response.put("status", "ERROR");
				response.put("message", "Updating executives failed");
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("updateUtilityShiftingExecutives : ", e);

			response.put("status", "ERROR");
			response.put("message", "Server error");

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(response);
		}
	}
}
