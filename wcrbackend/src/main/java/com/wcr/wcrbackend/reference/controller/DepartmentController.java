package com.wcr.wcrbackend.reference.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.reference.Iservice.DepartmentService;
import com.wcr.wcrbackend.reference.model.TrainingType;

import jakarta.servlet.http.HttpSession;


@Controller
public class DepartmentController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(DepartmentController.class);
	
	@Autowired
	DepartmentService service;
	
//	@RequestMapping(value="/department",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView department(HttpSession session,@ModelAttribute TrainingType obj){
//		ModelAndView model = new ModelAndView(PageConstants.department);
//		try {
//			List<TrainingType> departmentList = service.getDepartmentsList();
//			model.addObject("departmentList", departmentList);
//			TrainingType departmentDetails = service.getDpartmentDetails(obj);
//			model.addObject("departmentDetails", departmentDetails);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("department : " + e.getMessage());
//		}
//		return model;
//	}
//    @PostMapping("/department")
//    public ResponseEntity<?> getDepartmentData(@RequestBody(required = false) TrainingType obj) {
//        try {
//            Map<String, Object> response = new HashMap<>();
//            
//            // Get department list
//            List<TrainingType> departmentList = service.getDepartmentsList();
//            
//            // Get department details
//            TrainingType departmentDetails = service.getDpartmentDetails(obj);
//            
//            // Structure the response to match what your React component expects
//            Map<String, Object> data = new HashMap<>();
//            
//            // dList1 for main table data
//            data.put("dList1", departmentList);
//            
//            // dList for deletable items (you might need to adjust this based on your business logic)
//            data.put("dList", departmentList); // Or separate query if different
//            
//            // countList for counts per table
//            // You need to implement this based on your requirements
//            List<Map<String, Object>> countList = new ArrayList<>();
//            // Add your count logic here
//            
//            // tablesList for table names
//            List<Map<String, Object>> tablesList = new ArrayList<>();
//            // Add your tables logic here
//            
//            data.put("countList", countList);
//            data.put("tablesList", tablesList);
//            
//            response.put("departmentDetails", data);
//            response.put("success", true);
//            
//            return ResponseEntity.ok(response);
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//    
	
	@PostMapping("/get-department")
	public ResponseEntity<?> getDepartmentData(@RequestBody(required = false) TrainingType obj) {
	    try {
	        Map<String, Object> response = new HashMap<>();
	        
	        // Get department details (this already includes dList1, dList, tablesList, countList)
	        TrainingType departmentDetails = service.getDpartmentDetails(obj);
	        
	        // Structure the response to match what your React component expects
	        Map<String, Object> data = new HashMap<>();
	        
	        // Get data from the departmentDetails object
	        data.put("dList1", departmentDetails.getdList1());  // Main department list
	        data.put("dList", departmentDetails.getdList());    // Deletable departments
	        data.put("tablesList", departmentDetails.getTablesList());  // Table names
	        data.put("countList", departmentDetails.getCountList());    // Counts for each table
	        
	        response.put("departmentDetails", data);
	        response.put("success", true);
	        
	        return ResponseEntity.ok(response);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("success", false);
	        errorResponse.put("message", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}
//	@PostMapping("/department")
//	public ResponseEntity<Map<String, Object>> getDepartmentPageData(
//	        @RequestParam(required = false) Long departmentId) {
//
//	    try {
//	        Map<String, Object> response = new HashMap<>();
//
//	        List<TrainingType> departmentList = service.getDepartmentsList();
//	        response.put("departmentList", departmentList);
//
//	        if (departmentId != null) {
//	            TrainingType obj = new TrainingType();
//	           // obj.setId(departmentId);
//
//	           // obj.setId(departmentId);
//	            TrainingType departmentDetails = service.getDpartmentDetails(obj);
//	            response.put("departmentDetails", departmentDetails);
//	        }
//
//	        return ResponseEntity.ok(response);
//	    } catch (Exception e) {
//	      
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	    }
//	}

//	
//	@RequestMapping(value = "/add-department", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addDepartment(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/department");
//			boolean flag =  service.addDepartment(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Department Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding Department is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding Department is failed. Try again.");
//			logger.error("addDepartment : " + e.getMessage());
//		}
//		return model;
//	}
//	
//	
//	   @PostMapping(value = "/add-department")
//	    public ResponseEntity<String> addDepartment(
//	            @RequestBody TrainingType obj) {
//
//	        try {
//	            boolean flag = service.addDepartment(obj);
//
//	            if (flag) {
//	                return ResponseEntity
//	                        .status(HttpStatus.CREATED)
//	                        .body("Department added successfully.");
//	            } else {
//	                return ResponseEntity
//	                        .status(HttpStatus.BAD_REQUEST)
//	                        .body("Adding department failed.");
//	            }
//
//	        } catch (Exception e) {
//	            logger.error("addDepartment error", e);
//	            return ResponseEntity
//	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                    .body("Internal server error.");
//	        }
//	    }
	
	@PostMapping(value = "/add-department")
	public ResponseEntity<Map<String, Object>> addDepartment(@RequestBody TrainingType obj) {
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        boolean flag = service.addDepartment(obj);

	        if (flag) {
	            response.put("success", true);
	            response.put("message", "Department added successfully.");
	            return ResponseEntity.status(HttpStatus.CREATED).body(response);
	        } else {
	            response.put("success", false);
	            response.put("message", "Adding department failed.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	    } catch (Exception e) {
	        logger.error("addDepartment error", e);
	        response.put("success", false);
	        response.put("message", "Internal server error.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
//	@RequestMapping(value = "/update-department", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateDepartment(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/department");
//			boolean flag =  service.updateDepartment(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Department Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Department is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Department is failed. Try again.");
//			logger.error("updateDepartment : " + e.getMessage());
//		}
//		return model;
//	}
//	
	@RequestMapping(value = "/delete-department", method = {RequestMethod.POST})
	@ResponseBody
	public ModelAndView deleteDepartment(@ModelAttribute TrainingType obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/department");
			boolean flag =  service.deleteDepartment(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Department Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
			logger.error("deleteDepartment : " + e.getMessage());
		}
		return model;
	}
	
	
	// For UPDATE endpoint
	@PostMapping(value = "/update-department")
	public ResponseEntity<Map<String, Object>> updateDepartment(@RequestBody TrainingType obj) {
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        boolean flag = service.updateDepartment(obj);

	        if (flag) {
	            response.put("success", true);
	            response.put("message", "Department updated successfully.");
	            return ResponseEntity.ok(response);
	        } else {
	            response.put("success", false);
	            response.put("message", "Updating department failed.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	    } catch (Exception e) {
	        logger.error("updateDepartment error", e);
	        response.put("success", false);
	        response.put("message", "Internal server error.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	
//	// For DELETE endpoint
//	@DeleteMapping(value = "/delete-department")
//	public ResponseEntity<Map<String, Object>> deleteDepartment(@RequestBody Map<String, String> request) {
//	    Map<String, Object> response = new HashMap<>();
//	    
//	    try {
//	        String department = request.get("department");
//	        boolean flag = service.deleteDepartment(department);
//	        
//	        if (flag) {
//	            response.put("success", true);
//	            response.put("message", "Department deleted successfully.");
//	            return ResponseEntity.ok(response);
//	        } else {
//	            response.put("success", false);
//	            response.put("message", "Deleting department failed.");
//	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//	        }
//	        
//	    } catch (Exception e) {
//	        logger.error("deleteDepartment error", e);
//	        response.put("success", false);
//	        response.put("message", "Internal server error.");
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//	    }
//	    
	    
	
	@DeleteMapping(value = "/delete-department")
	public ResponseEntity<Map<String, Object>> deleteDepartment(@RequestBody Map<String, String> request) {
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        String department = request.get("department");
	        if (department == null || department.trim().isEmpty()) {
	            response.put("success", false);
	            response.put("message", "Department is required");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	        
	        TrainingType obj = new TrainingType();
	        obj.setDepartment(department);
	        
	        boolean flag = service.deleteDepartment(obj);
	        
	        if (flag) {
	            response.put("success", true);
	            response.put("message", "Department deleted successfully.");
	            return ResponseEntity.ok(response);
	        } else {
	            response.put("success", false);
	            response.put("message", "Failed to delete department.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	        
	    } catch (Exception e) {
	        logger.error("deleteDepartment error", e);
	        response.put("success", false);
	        response.put("message", "Internal server error.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	}




