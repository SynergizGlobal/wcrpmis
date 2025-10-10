package com.wcr.wcrbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.ErrorResponse;
import com.wcr.wcrbackend.DTO.LoginRequest;
import com.wcr.wcrbackend.DTO.LoginResponse;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.ILoginService;

import jakarta.servlet.http.HttpSession;

@RestController
public class LoginController {
	
	@Autowired
	private ILoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
		try {
		

			User user = loginService.authenticate(loginRequest.getUserId(), loginRequest.getPassword());
	     

			session.setAttribute("user", user);
			session.setAttribute("userId", user.getUserId());
			session.setAttribute("userName", user.getUserName());
			session.setAttribute("emailId", user.getEmailId());
			session.setAttribute("userRoleNameFk", user.getUserRoleNameFk());
			session.setAttribute("userTypeFk", user.getUserTypeFk());
			session.setAttribute("departmentFk", user.getDepartmentFk());
			session.setAttribute("reportingToIdSrfk", user.getReportingToIdSrfk());

			System.out.println("User ID: " + user.getUserId());
			System.out.println("Reporting To: " + user.getReportingToIdSrfk());
			loginService.updateSessionId(user.getUserId(), session.getId());

			LoginResponse response = new LoginResponse(user.getUserId(), user.getUserName(), user.getEmailId(), user.getUserRoleNameFk(),
					user.getUserTypeFk(), user.getDepartmentFk());

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ErrorResponse("AUTHENTICATION_FAILED", e.getMessage()));

		} 
	}
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		try {
			String userName = (String) session.getAttribute("userName");
			session.invalidate();
			return ResponseEntity.ok(new LoginResponse(null, null,null,  null, null, null));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("LOGOUT_ERROR", "Error occurred during logout"));
		}
	}
}
