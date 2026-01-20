package com.wcr.wcrbackend.controller;

import com.wcr.wcrbackend.DTO.DashboardMenuDTO;
import com.wcr.wcrbackend.DTO.DashboardSubMenuDTO;
import com.wcr.wcrbackend.DTO.ExecutionProgress;
import com.wcr.wcrbackend.service.IDashboardService;
import com.wcr.wcrbackend.service.IExecutionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/execution")
public class ExecutionController {

    @Autowired
    private IExecutionService executionService;
    
    @Autowired
    private IDashboardService dashboardService;    

    @GetMapping("/progress")
    public ResponseEntity<List<ExecutionProgress>> getExecutionProgress(
            @RequestParam(name = "project_id", required = false) String projectId) { // <-- capture query param
        try {
            List<ExecutionProgress> progressList;

                progressList = executionService.getExecutionProgress(projectId);

            return ResponseEntity.ok(progressList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/contracts")
    public ResponseEntity<List<ExecutionProgress>> getContracts(
            @RequestParam(name = "project_id", required = false) String projectId) { // <-- capture query param
        try {
            List<ExecutionProgress> contractsList;

            contractsList = executionService.getContractsList(projectId);

            return ResponseEntity.ok(contractsList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }  
    
    
    @GetMapping("/project-contract-info")
    public Map<String, String> getProjectAndContractInfo(
            @RequestParam String projectId,
            @RequestParam String contractId) {

        String projectName = executionService.getProjectName(projectId);
        String contractName = executionService.getContractName(contractId);

        Map<String, String> map = new HashMap<>();
        map.put("projectName", projectName);
        map.put("contractName", contractName);

        return map;
    }
    
    @GetMapping("/project-info")
    public Map<String, String> getProjectInfo(
            @RequestParam String projectId) {

        String projectName = executionService.getProjectName(projectId);

        Map<String, String> map = new HashMap<>();
        map.put("projectName", projectName);

        return map;
    }  
    
    @GetMapping("/menu")
    public ResponseEntity<List<DashboardMenuDTO>> getMenuItems() {
        try {
            // Fetch all active menus along with their submenus
            List<DashboardMenuDTO> menuList = dashboardService.getActiveMenuItems();

            return ResponseEntity.ok(menuList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

   

}
