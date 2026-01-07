package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wcr.wcrbackend.reference.Iservice.LACategoryService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
public class LACategoryController {

    private static final Logger logger =
            Logger.getLogger(LACategoryController.class);

    @Autowired
    private LACategoryService service;

    
    @RequestMapping(value = "/la-category",
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> LACategory(
            @ModelAttribute TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<TrainingType> laCategoryList =
                    service.getLACategoryList();
            response.put("LACategoryList", laCategoryList);

            TrainingType details =
                    service.getLandAcquisitionCategoryDetails(obj);
            response.put("landAcquisitionCategoryDetails", details);

            response.put("status", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("LACategory error", e);
            response.put("status", false);
            response.put("error",
                    "Failed to fetch LA Category data");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @RequestMapping(value = "/add-la-category",
            method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addLACategory(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag = service.addLACategory(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "LA Category Added Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("error",
                        "Adding LA Category failed");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

        } catch (Exception e) {
            logger.error("addLACategory error", e);
            response.put("status", false);
            response.put("error",
                    "Server error while adding LA Category");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @RequestMapping(value = "/update-la-category",
            method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> updateLandAcquisitionCategory(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag =
                    service.updateLandAcquisitionCategory(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "LA Category Updated Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("error",
                        "Updating LA Category failed");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

        } catch (Exception e) {
            logger.error("updateLandAcquisitionCategory error", e);
            response.put("status", false);
            response.put("error",
                    "Server error while updating LA Category");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @RequestMapping(value = "/delete-la-category",
            method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> deleteLandAcquisitionCategory(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag =
                    service.deleteLandAcquisitionCategory(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "LA Category Deleted Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("error",
                        "Something went wrong");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

        } catch (Exception e) {
            logger.error("deleteLandAcquisitionCategory error", e);
            response.put("status", false);
            response.put("error",
                    "Server error while deleting LA Category");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
