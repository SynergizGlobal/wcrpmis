package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Forms;
import com.wcr.wcrbackend.service.IFormsService;

@RestController
@RequestMapping("/forms")
public class FormsController {
	@Autowired
	private IFormsService formsService;
    @GetMapping("/api/getUpdateForms")
    public List<Forms> getUpdateForms() {
        return formsService.getUpdateForms();
    }
}