package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Dashboard;
import com.wcr.wcrbackend.DTO.WebDocuments;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IDashboardService;
import com.wcr.wcrbackend.service.IDocumentService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/documents")
public class DocumentsController {
	
	@Autowired
	private IDocumentService documentService;
	
	@GetMapping("/api/getDocumentTypes")
	List<WebDocuments> getDocumentTypes(HttpSession session) {
		return documentService.getDocumentTypes();
	}
}
