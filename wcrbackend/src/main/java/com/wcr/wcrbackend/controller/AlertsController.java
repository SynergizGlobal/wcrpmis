package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Alerts;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IAlertsService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/alerts")
public class AlertsController {
	@Autowired
	private IAlertsService alertsService;
	
	@GetMapping("/api/getAlerts")
	List<Alerts> getAlerts(HttpSession session) throws Exception{
		User user = (User) session.getAttribute("user");
		Alerts aObj = new Alerts();
		aObj.setUser_id(user.getUserId());
		aObj.setEmail_id(user.getEmailId());
		aObj.setUser_role_name(user.getUserRoleNameFk());
		return alertsService.getAdminForms(aObj);
	}
}
