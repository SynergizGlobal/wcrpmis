package com.wcr.wcrbackend.reference.controller;


import java.util.HashMap;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.wcr.wcrbackend.reference.Iservice.UtilityCategoryService;
import com.wcr.wcrbackend.reference.model.Safety;
import jakarta.servlet.http.HttpSession;


@RestController
public class UtilityCategoryController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(UtilityCategoryController.class);
	
	@Autowired
	UtilityCategoryService service;


	@PostMapping("/utility-category")
	@ResponseBody
	public Map<String, Object> utilityCategory(
	        HttpSession session,
	        @RequestBody(required = false) Safety obj) {

	    Map<String, Object> res = new HashMap<>();

	    try {
	        Safety utilityCategoryDetails = service.getUtilityCategorysList(obj);

	        res.put("success", true);
	        res.put("UtilityCategoryDetails", utilityCategoryDetails);

	    } catch (Exception e) {
	        logger.error("utilityCategory error", e);

	        res.put("success", false);
	        res.put("message", "Failed to fetch Utility Category details");
	    }

	    return res;
	}

	@PostMapping(
		    value = "/add-utility-category",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<?> addUtilityCategory(@RequestBody Safety obj) {

		    if (obj.getUtility_category() == null || obj.getUtility_category().trim().isEmpty()) {
		        return ResponseEntity.badRequest().body(
		            Map.of(
		                "success", false,
		                "message", "Utility Category is required"
		            )
		        );
		    }

		    try {
		        boolean flag = service.addUtilityCategory(obj);

		        if (flag) {
		            return ResponseEntity.ok(
		                Map.of(
		                    "success", true,
		                    "message", "Utility Category added successfully"
		                )
		            );
		        }

		        return ResponseEntity.badRequest().body(
		            Map.of(
		                "success", false,
		                "message", "Adding Utility Category failed"
		            )
		        );

		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
		            Map.of(
		                "success", false,
		                "message", "Error while adding Utility Category"
		            )
		        );
		    }
	}

    @PostMapping(
        value = "/update-utility-category",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateUtilityCategory(@RequestBody Safety obj) {

        try {
            boolean flag = service.updateUtilityCategory(obj);

            if (flag) {
                return ResponseEntity.ok(
                    Map.of(
                        "success", true,
                        "message", "Utility Category updated successfully"
                    )
                );
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                        Map.of(
                            "success", false,
                            "message", "Updating Utility Category failed. Try again."
                        )
                    );
            }

        } catch (Exception e) {
           
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    Map.of(
                        "success", false,
                        "message", "Error while updating Utility Category"
                    )
                );
        }
    }
	
    @DeleteMapping(
		        value = "/delete-utility-category",
		        consumes = MediaType.APPLICATION_JSON_VALUE,
		        produces = MediaType.APPLICATION_JSON_VALUE
		    )
		    public ResponseEntity<?> deleteUtilityCategory(@RequestBody Safety obj) {

		        try {
		            boolean flag = service.deleteUtilityCategory(obj);

		            if (flag) {
		                return ResponseEntity.ok(
		                    Map.of(
		                        "success", true,
		                        "message", "Utility Category deleted successfully"
		                    )
		                );
		            } else {
		                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                    .body(
		                        Map.of(
		                            "success", false,
		                            "message", "Something went wrong. Try again."
		                        )
		                    );
		            }

		        } catch (Exception e) {
		    
		          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(
		                    Map.of(
		                        "success", false,
		                        "message", "Error while deleting Utility Category"
		                    )
		                );
		        }
		    }
}









