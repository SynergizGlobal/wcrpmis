package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Forms>> getUpdateForms(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Forms> forms = formsService.getUpdateForms(user);
        return ResponseEntity.ok(forms);
    }

    /*
     * @GetMapping("/api/getReportForms")
     * public List<Forms> getReportForms(HttpSession session) {
     * User user = (User) session.getAttribute("user");
     * return formsService.getReportForms(user);
     * }
     */
}