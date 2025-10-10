package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Dashboard;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IDashboardService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	@Autowired
	private IDashboardService dashboardService;
	
	/*@GetMapping("/api/getDashboardModules")
	List<Dashboard> getDashboardModules(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return dashboardService.getDashboardsList("Module", user);
	}
	
	@GetMapping("/api/getDashboardProjects")
	List<Dashboard> getDashboardProjects(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return dashboardService.getDashboardsList("Project", user);
	}*/
}
