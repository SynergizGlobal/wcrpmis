package com.wcr.wcrbackend.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Design;
import com.wcr.wcrbackend.service.IDesignService;

@RestController
@RequestMapping("/design")
public class DesignController {
	
	@Autowired
	private IDesignService designService;
	
	Logger logger = Logger.getLogger(DesignController.class);
	
	@PostMapping(value = "/ajax/getP6ActivitiesData")
	public List<Design> getP6ActivitiesData(@RequestBody Design obj) {
		List<Design> objList = null;
		try {
			objList = designService.getP6ActivitiesData(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getP6ActivityData : " + e.getMessage());
		}
		return objList;
	}	
}
