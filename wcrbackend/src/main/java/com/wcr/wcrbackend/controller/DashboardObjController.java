package com.wcr.wcrbackend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.DashboardFilterDTO;
import com.wcr.wcrbackend.DTO.DashboardObj;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.service.DashboardObjService;
import com.wcr.wcrbackend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardObjController {

	
	 private final DashboardObjService dashboardService;
	    private final UserService userService; 

	    @GetMapping("/dashboards")
	    public ResponseEntity<List<DashboardObj>> dashboards() {
	        return ResponseEntity.ok(dashboardService.getDashboardNames());
	    }

	    @GetMapping("/modules")
	    public ResponseEntity<List<String>> modules() {
	        return ResponseEntity.ok(dashboardService.getModules());
	    }
	    
	    @GetMapping("/roles")
	    public ResponseEntity<List<User>> roles() throws Exception {
	        return ResponseEntity.ok(userService.getUserRoles());
	    }

	    @GetMapping("/types")
	    public ResponseEntity<List<User>> types() throws Exception {
	        return ResponseEntity.ok(userService.getUserTypes());
	    }

	    @GetMapping("/users")
	    public ResponseEntity<List<User>> users() throws Exception {
	        User u = new User();  
	        return ResponseEntity.ok(userService.getUsersList(u));
	    }
	    
	    
	    @PostMapping("/list")
	    public ResponseEntity<List<DashboardObj>> getDashboardList(@RequestBody DashboardFilterDTO filter) {
	        return ResponseEntity.ok(dashboardService.getDashboardList(filter));
	    }
	    
	    @GetMapping("/get/{id}")
	    public ResponseEntity<DashboardObj> getDashboardById(@PathVariable String id) {
	        return ResponseEntity.ok(dashboardService.getDashboardById(id));
	    }


	    @PostMapping("/save")
	    public ResponseEntity<Map<String,Object>> saveDashboard(@RequestBody DashboardObj dto) {
	        try {

	            int newId = dashboardService.saveDashboard(dto);
	            return ResponseEntity.ok(Map.of("dashboard_id", newId));

	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
	        }
	    }


	    @PutMapping("/update/{id}")
	    public ResponseEntity<?> updateDashboard(@PathVariable String id, @RequestBody DashboardObj dto) {
	        try {
	            dto.setDashboard_id(id);	            
	            dashboardService.updateDashboard(dto);
	            return ResponseEntity.ok(Map.of("success", true));

	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
	        }
	    }



}
