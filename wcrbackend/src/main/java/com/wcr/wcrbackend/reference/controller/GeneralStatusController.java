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

import com.wcr.wcrbackend.reference.Iservice.GeneralStatusService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
@RequestMapping("/api/general-status")
public class GeneralStatusController {

    @Autowired
    private GeneralStatusService generalStatusService;

    @GetMapping("/list")
    public ResponseEntity<List<Safety>> getGeneralStatusList() {
        try {
            List<Safety> list = generalStatusService.getGeneralStatusList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/details")
    public ResponseEntity<List<TrainingType>> getGeneralStatusDetails() {
        try {
            TrainingType obj = new TrainingType();
            TrainingType result = generalStatusService.getGeneralStatusDetails(obj);

            // return only the final list used by UI
            return ResponseEntity.ok(result.getdList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addGeneralStatus(@RequestBody Safety obj) {
        try {
            boolean flag = generalStatusService.addGeneralStatus(obj);
            return flag
                    ? ResponseEntity.ok("General Status added successfully")
                    : ResponseEntity.badRequest().body("Failed to add General Status");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateGeneralStatus(@RequestBody TrainingType obj) {
        try {
            boolean flag = generalStatusService.updateGeneralStatus(obj);
            return flag
                    ? ResponseEntity.ok("General Status updated successfully")
                    : ResponseEntity.badRequest().body("Failed to update General Status");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGeneralStatus(
            @RequestParam String general_status) {
        try {
            TrainingType obj = new TrainingType();
            obj.setGeneral_status(general_status);

            boolean flag = generalStatusService.deleteGeneralStatus(obj);
            return flag
                    ? ResponseEntity.ok("General Status deleted successfully")
                    : ResponseEntity.badRequest().body("Failed to delete General Status");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

