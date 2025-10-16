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
    public ResponseEntity<?> getUpdateForms(HttpSession session) {
        try {
            // ✅ 1️⃣ Get user from session
            User user = (User) session.getAttribute("user");

            // ✅ 2️⃣ Handle expired or invalid session
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Session expired or user not logged in.");
            }

            // ✅ 3️⃣ Call service safely
            List<Forms> forms = formsService.getUpdateForms(user);

            // ✅ 4️⃣ Handle empty results
            if (forms == null || forms.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No forms available for this user.");
            }

            // ✅ 5️⃣ Return success
            return ResponseEntity.ok(forms);

        } catch (Exception e) {
            // ✅ 6️⃣ Catch any runtime errors and return clean JSON response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching update forms: " + e.getMessage());
        }
    }

    /*
     * @GetMapping("/api/getReportForms")
     * public List<Forms> getReportForms(HttpSession session) {
     * User user = (User) session.getAttribute("user");
     * return formsService.getReportForms(user);
     * }
     */
}