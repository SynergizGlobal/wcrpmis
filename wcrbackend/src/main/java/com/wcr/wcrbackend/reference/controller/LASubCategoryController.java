package com.wcr.wcrbackend.reference.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wcr.wcrbackend.reference.Iservice.LASubCategoryService;
import com.wcr.wcrbackend.reference.Iservice.LACategoryService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@RestController
public class LASubCategoryController {

    Logger logger = Logger.getLogger(LASubCategoryController.class);

    @Autowired
    private LASubCategoryService service;

    @Autowired
    private LACategoryService categoryService;

    /* =============================
       GET : PAGE DATA
       ============================= */
    @RequestMapping(
        value = "/la-sub-category",
        method = { RequestMethod.GET, RequestMethod.POST }
    )
    public ResponseEntity<Map<String, Object>> getLASubCategoryPage(
            @ModelAttribute TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<TrainingType> laCategories =
                    categoryService.getLACategoryList();
            response.put("LACategorysList", laCategories);

            List<TrainingType> laSubCategories =
                    service.getLASubCategoryList();
            response.put("LASubCategoryList", laSubCategories);

            TrainingType details =
                    service.getLandAcquisitionSubCategoryDetails(obj);
            response.put(
                "landAcquisitionSubCategoryDetails",
                details
            );

            response.put("status", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("LASubCategory error : " + e.getMessage(), e);
            response.put("status", false);
            response.put("error",
                    "Failed to fetch LA Sub Category data");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /* =============================
       ADD
       ============================= */
    @RequestMapping(
        value = "/add-la-sub-category",
        method = RequestMethod.POST
    )
    public ResponseEntity<Map<String, Object>> addLASubCategory(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag = service.addLASubCategory(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "LA Sub Category Added Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("error",
                        "Adding LA Sub Category failed");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

        } catch (Exception e) {
            logger.error("addLASubCategory error : " + e.getMessage(), e);
            response.put("status", false);
            response.put("error",
                    "Server error while adding LA Sub Category");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /* =============================
       AJAX : GET SUB CATEGORY BY CATEGORY
       ============================= */
    @RequestMapping(
        value = "/ajax/getLASubCategory",
        method = { RequestMethod.GET, RequestMethod.POST }
    )
    public ResponseEntity<List<TrainingType>> getLASubCategory(
            @ModelAttribute TrainingType obj) {

        try {
            List<TrainingType> list =
                    service.getLASubCategory(obj);
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            logger.error("getLASubCategory error : " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /* =============================
       UPDATE
       ============================= */
    @RequestMapping(
        value = "/update-la-sub-category",
        method = RequestMethod.POST
    )
    public ResponseEntity<Map<String, Object>>
    updateLandAcquisitionSubCategory(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag =
                    service.updateLandAcquisitionSubCategory(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "LA Sub Category Updated Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("error",
                        "Updating LA Sub Category failed");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

        } catch (Exception e) {
            logger.error(
                "updateLandAcquisitionSubCategory error : "
                + e.getMessage(), e);
            response.put("status", false);
            response.put("error",
                    "Server error while updating LA Sub Category");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    /* =============================
       DELETE
       ============================= */
    @RequestMapping(
        value = "/delete-la-sub-category",
        method = RequestMethod.POST
    )
    public ResponseEntity<Map<String, Object>>
    deleteLandAcquisitionSubCategory(
            @RequestBody TrainingType obj) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean flag =
                    service.deleteLandAcquisitionSubCategory(obj);

            if (flag) {
                response.put("status", true);
                response.put("message",
                        "LA Sub Category Deleted Successfully");
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
            logger.error(
                "deleteLandAcquisitionSubCategory error : "
                + e.getMessage(), e);
            response.put("status", false);
            response.put("error",
                    "Server error while deleting LA Sub Category");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
