package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Forms;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IFormsService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/forms")
public class FormsController {
	@Autowired
	private IFormsService formsService;
    @GetMapping("/api/getUpdateForms")
    public List<Forms> getUpdateForms(HttpSession session) {
    	User user = (User) session.getAttribute("user");
        return formsService.getUpdateForms(user);
    }
    
    /*@GetMapping("/api/getReportForms")
    public List<Forms> getReportForms(HttpSession session) {
    	User user = (User) session.getAttribute("user");
        return formsService.getReportForms(user);
    }*/
}