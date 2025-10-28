package com.wcr.wcrbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.common.JwtUtil;
import com.wcr.wcrbackend.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/dms")
public class DMSController {
	@Value("${dms.home.url}")
	private String dmsUrl;
	
	@Value("${dms.qa.home.url}")
	private String dmsQaUrl;
	
	@GetMapping(value="/dms")
	public ResponseEntity<String> redirectToDMS(HttpSession session, HttpServletRequest request) {
		User user = (User) session.getAttribute("user"); // or from Spring Security

		String user_Id = user.getUserId();
	    String token = JwtUtil.generateToken(user_Id);

	    String redirectUri = null;
	    if(request.getContextPath().contains("qa"))
	    	redirectUri = dmsQaUrl + "?token=" + token;
	    else 
	    	redirectUri = dmsUrl + "?token=" + token;
	    
	    return ResponseEntity.ok(redirectUri);
	}
}
