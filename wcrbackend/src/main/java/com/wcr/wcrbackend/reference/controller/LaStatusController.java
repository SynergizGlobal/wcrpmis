package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.reference.Iservice.LaStatusService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
public class LaStatusController {

    Logger logger = Logger.getLogger(LaStatusController.class);

    @Autowired
    private LaStatusService service;

    @RequestMapping(
        value = "/la-status",
        method = { RequestMethod.GET, RequestMethod.POST }
    )
    public ResponseEntity<Map<String, Object>> getLaStatus(
            @ModelAttribute TrainingType obj) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Safety> laStatusList =
                    service.getIaStatusList();
            response.put("laStatusList", laStatusList);

            TrainingType details =
                    service.getLandAcquisitionStatusDetails(obj);
            response.put("landAcquisitionStatusDetails", details);

            response.put("status", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("laStatus error", e);
            response.put("status", false);
            response.put("error",
                    "Failed to fetch LA Status data");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @RequestMapping(
        value = "/add-la-status",
        method = RequestMethod.POST
    )
    public ResponseEntity<Map<String, Object>> addLaStatus(
            @RequestBody Safety obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag = service.addLaStatus(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "Status Added Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("error",
                        "Adding Status failed");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

        } catch (Exception e) {
            logger.error("addLaStatus error", e);
            response.put("status", false);
            response.put("error",
                    "Server error while adding Status");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @RequestMapping(
        value = "/update-la-status",
        method = RequestMethod.POST
    )
    public ResponseEntity<Map<String, Object>>
    updateLandAcquisitionStatus(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag =
                    service.updatelandAcquisitionStatus(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "Status Updated Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("error",
                        "Updating Status failed");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

        } catch (Exception e) {
            logger.error("updatelandAcquisitionStatus error", e);
            response.put("status", false);
            response.put("error",
                    "Server error while updating Status");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @RequestMapping(
        value = "/delete-la-status",
        method = RequestMethod.POST
    )
    public ResponseEntity<Map<String, Object>>
    deleteLandAcquisitionStatus(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag =
                    service.deletelandAcquisitionStatus(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "Status Deleted Successfully");
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
            logger.error("deletelandAcquisitionStatus error", e);
            response.put("status", false);
            response.put("error",
                    "Server error while deleting Status");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
