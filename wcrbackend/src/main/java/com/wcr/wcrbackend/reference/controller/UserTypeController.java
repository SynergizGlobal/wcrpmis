package com.wcr.wcrbackend.reference.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.wcr.wcrbackend.reference.Iservice.UserTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
@RequestMapping("/api/user-type")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    @GetMapping("/list")
    public ResponseEntity<List<String>> getUserTypes() {
        try {
            TrainingType result =
                userTypeService.getUserTypeDetails(new TrainingType());

            List<String> userTypes = result.getdList()
                    .stream()
                    .map(TrainingType::getUser_type)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userTypes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUserType(@RequestBody TrainingType obj) {
        try {
            boolean flag = userTypeService.addUserType(obj);
            return flag
                    ? ResponseEntity.ok("UserType added successfully")
                    : ResponseEntity.badRequest().body("Failed to add UserType");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserType(@RequestBody TrainingType obj) {
        try {
            boolean flag = userTypeService.updateUserType(obj);
            return flag
                    ? ResponseEntity.ok("UserType updated successfully")
                    : ResponseEntity.badRequest().body("Failed to update UserType");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserType(@RequestParam String user_type) {
        try {
            TrainingType obj = new TrainingType();
            obj.setUser_type(user_type);

            boolean flag = userTypeService.deleteUserType(obj);
            return flag
                    ? ResponseEntity.ok("UserType deleted successfully")
                    : ResponseEntity.badRequest().body("Failed to delete UserType");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

