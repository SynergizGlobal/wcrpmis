package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.reference.Iservice.DashboardTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard-type")
public class DashboardTypeController {

    @Autowired
    private DashboardTypeService dashboardTypeService;

    @GetMapping("/list")
    public ResponseEntity<List<TrainingType>> getDashboardTypes() {
        try {
            List<TrainingType> list = dashboardTypeService.getDashboardTypesList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addDashboardType(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();
        try {
            boolean isAdded = dashboardTypeService.addDashboardType(obj);

            if (isAdded) {
                response.put("status", "success");
                response.put("message", "Dashboard Type added successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "failure");
                response.put("message", "Failed to add Dashboard Type");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateDashboardType(
            @RequestBody Map<String, String> payload) {

        Map<String, Object> response = new HashMap<>();
        try {
            boolean updated = dashboardTypeService.updateDashboardType(payload);

            if (updated) {
                response.put("status", "success");
                response.put("message", "Dashboard Type updated successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "failure");
                response.put("message", "Dashboard Type not found");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteDashboardType(
            @RequestBody Map<String, String> payload) {

        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = dashboardTypeService.deleteDashboardType(payload);

            if (deleted) {
                response.put("status", "success");
                response.put("message", "Dashboard Type deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "failure");
                response.put("message", "Dashboard Type not found");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

