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
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.reference.Iservice.StructureTypeService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
@RequestMapping("/api/structure-type")
public class StructureTypeController {

    @Autowired
    private StructureTypeService structureTypeService;

    @GetMapping("/list")
    public ResponseEntity<List<Safety>> getStructureTypesList() {
        try {
            List<Safety> list = structureTypeService.getStructureTypesList();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/details")
    public ResponseEntity<TrainingType> getStructureTypeDetails(
            @RequestBody TrainingType obj) {
        try {
            TrainingType result = structureTypeService.getStructureTypeDetails(obj);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addStructureType(
            @RequestBody Safety obj) {
        try {
            boolean status = structureTypeService.addStructureType(obj);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateStructureType(
            @RequestBody TrainingType obj) {
        try {
            boolean status = structureTypeService.updateStructureType(obj);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteStructureType(
            @RequestBody TrainingType obj) {
        try {
            boolean status = structureTypeService.deleteStructureType(obj);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

