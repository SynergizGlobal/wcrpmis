package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Alerts;
import com.wcr.wcrbackend.DTO.Messages;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IAlertsService;
import com.wcr.wcrbackend.service.IMessageService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/messages")
public class MessagesController {
	@Autowired
	private IMessageService messageService;
	
	/*@GetMapping("/api/getMessages")
	List<Messages> getMessages(HttpSession session) throws Exception{
		User user = (User) session.getAttribute("user");
		Messages mObj = new Messages();
		mObj.setUser_id_fk(user.getUserId());
		return messageService.getMessages(mObj);
	}*/
}
