package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Dashboard;
import com.wcr.wcrbackend.service.IDashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	@Autowired
	private IDashboardService dashboardService;
	
	@GetMapping("/api/getDashboardModules")
	List<Dashboard> getDashboardModules() {
		return dashboardService.getDashboardsList("Module");
	}
	
	@GetMapping("/api/getDashboardProjects")
	List<Dashboard> getDashboardProjects() {
		return dashboardService.getDashboardsList("Project");
	}
}
