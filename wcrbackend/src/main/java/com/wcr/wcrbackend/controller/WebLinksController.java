package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.WebLinks;
import com.wcr.wcrbackend.service.IWebLinkService;

@RestController
@RequestMapping("/weblinks")
public class WebLinksController {
	@Autowired
	private IWebLinkService weblinkService;
	
	/*@GetMapping("/api/getWebLinks")
	List<WebLinks> getWebLinks() {
		return weblinkService.getWebLinks();
	}*/
}
