package com.wcr.wcrbackend.reference.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.reference.Iservice.ExecutionStatusService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
@RequestMapping("/api/execution-status")
public class ExecutionStatusController {

    @Autowired
    private ExecutionStatusService executionStatusService;

    @GetMapping("/list")
    public ResponseEntity<List<TrainingType>> getExecutionStatusList() {
        try {
            List<TrainingType> list = executionStatusService.getExecutionStatusList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addExecutionStatus(@RequestBody TrainingType obj) {
        try {
            boolean flag = executionStatusService.addExecutionStatus(obj);
            return flag
                    ? ResponseEntity.ok("Execution Status added successfully")
                    : ResponseEntity.badRequest().body("Failed to add Execution Status");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateExecutionStatus(@RequestBody TrainingType obj) {
        try {
            boolean flag = executionStatusService.updateExecutionStatus(obj);
            return flag
                    ? ResponseEntity.ok("Execution Status updated successfully")
                    : ResponseEntity.badRequest().body("Failed to update Execution Status");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteExecutionStatus(
            @RequestParam String execution_status) {
        try {
            TrainingType obj = new TrainingType();
            obj.setExecution_status(execution_status);

            boolean flag = executionStatusService.deleteExecutionStatus(obj);
            return flag
                    ? ResponseEntity.ok("Execution Status deleted successfully")
                    : ResponseEntity.badRequest().body("Failed to delete Execution Status");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
