package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Admin;
import com.wcr.wcrbackend.service.IAdminFormsService;

@RestController
@RequestMapping("/adminForms")
public class AdminFormsController {
	@Autowired
	private IAdminFormsService adminFormsService;
	
	@GetMapping("/api/getAdminForms")
	List<Admin> getAdminForms() {
		return adminFormsService.getAdminForms();
	}
}
