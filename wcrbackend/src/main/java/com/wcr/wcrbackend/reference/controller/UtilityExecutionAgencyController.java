package com.wcr.wcrbackend.reference.controller;



import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.wcr.wcrbackend.reference.Iservice.UtilityExecutionAgencyService;
import com.wcr.wcrbackend.reference.model.Safety;

import jakarta.servlet.http.HttpSession;

@Controller
public class UtilityExecutionAgencyController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(UtilityExecutionAgencyController.class);
	
	@Autowired
	UtilityExecutionAgencyService service;
	
//	@RequestMapping(value="/utility-execution-agency",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView UtilityExecutionAgency(HttpSession session,@ModelAttribute Safety obj){
//		ModelAndView model = new ModelAndView(PageConstants.utilityExecutionAgency);
//		try {
//			Safety  UtilityExecutionAgencyList = service.getUtilityExecutionAgencysList(obj);
//			model.addObject("utilityExecutionAgencyList",  UtilityExecutionAgencyList);
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error(" UtilityExecutionAgency : " + e.getMessage());
//		}
//		return model;
//	}
	
	   @GetMapping(
		        value = "/utility-execution-agency",
		        produces = MediaType.APPLICATION_JSON_VALUE
		    )
		    public ResponseEntity<?> getUtilityExecutionAgency(HttpSession session) {

		        try {
		            Safety obj = new Safety(); // always non-null

		            Safety utilityExecutionAgencyList =
		                    service.getUtilityExecutionAgencysList(obj);

		            return ResponseEntity.ok(
		                Map.of(
		                    "success", true,
		                    "utilityExecutionAgencyList",
		                    utilityExecutionAgencyList
		                )
		            );

		        } catch (Exception e) {
		            e.printStackTrace();

		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(
		                    Map.of(
		                        "success", false,
		                        "message", "Error while fetching Utility Execution Agency list"
		                    )
		                );
		        }
		    }
	
	
//	@RequestMapping(value = "/add-utility-execution-agency", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView addUtilityExecutionAgency(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-execution-agency");
//			boolean flag =  service.addUtilityExecutionAgency(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", " Utility Execution Agency Added Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Adding  Utility Execution Agency is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding  Utility Execution Agency is failed. Try again.");
//			logger.error("add UtilityExecutionAgency : " + e.getMessage());
//		}
//		return model;
//	}
//	
	   
	   @PostMapping(
			    value = "/add-utility-execution-agency",
			    consumes = MediaType.APPLICATION_JSON_VALUE,
			    produces = MediaType.APPLICATION_JSON_VALUE
			)
			public ResponseEntity<?> addUtilityExecutionAgency(
			        @RequestBody Safety obj) {

			    try {
			        // basic validation
			        if (obj.getExecution_agency() == null || obj.getExecution_agency().trim().isEmpty()) {
			            return ResponseEntity.badRequest().body(
			                Map.of(
			                    "success", false,
			                    "message", "Execution agency is required"
			                )
			            );
			        }

			        boolean flag = service.addUtilityExecutionAgency(obj);

			        if (flag) {
			            return ResponseEntity.ok(
			                Map.of(
			                    "success", true,
			                    "message", "Utility Execution Agency added successfully"
			                )
			            );
			        }

			        return ResponseEntity.badRequest().body(
			            Map.of(
			                "success", false,
			                "message", "Adding Utility Execution Agency failed"
			            )
			        );

			    } catch (Exception e) {
			        logger.error("addUtilityExecutionAgency", e);
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
			            Map.of(
			                "success", false,
			                "message", "Error while adding Utility Execution Agency"
			            )
			        );
			    }
			}

	
//	@RequestMapping(value = "/update-utility-execution-agency", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView updateUtilityExecutionAgency(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-execution-agency");
//			boolean flag =  service.updateUtilityExecutionAgency(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Utility Execution Agency Updated Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Updating Utility Execution Agency is failed. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Utility Execution Agency is failed. Try again.");
//			logger.error("updateUtilityExecutionAgency : " + e.getMessage());
//		}
//		return model;
//	}
	
	   
	   @PostMapping(
			    value = "/update-utility-execution-agency",
			    consumes = MediaType.APPLICATION_JSON_VALUE,
			    produces = MediaType.APPLICATION_JSON_VALUE
			)
			public ResponseEntity<?> updateUtilityExecutionAgency(
			        @RequestBody Safety obj) {

			    try {
			        // validation (based on your service logic)
			        if (obj.getValue_old() == null || obj.getValue_old().trim().isEmpty()
			                || obj.getValue_new() == null || obj.getValue_new().trim().isEmpty()) {

			            return ResponseEntity.badRequest().body(
			                Map.of(
			                    "success", false,
			                    "message", "Both value_old and value_new are required"
			                )
			            );
			        }

			        boolean flag = service.updateUtilityExecutionAgency(obj);

			        if (flag) {
			            return ResponseEntity.ok(
			                Map.of(
			                    "success", true,
			                    "message", "Utility Execution Agency updated successfully"
			                )
			            );
			        }

			        return ResponseEntity.badRequest().body(
			            Map.of(
			                "success", false,
			                "message", "Updating Utility Execution Agency failed"
			            )
			        );

			    } catch (Exception e) {
			        logger.error("updateUtilityExecutionAgency", e);
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
			            Map.of(
			                "success", false,
			                "message", "Error while updating Utility Execution Agency"
			            )
			        );
			    }
			}

//	@RequestMapping(value = "/delete-utility-execution-agency", method = {RequestMethod.POST})
//	@ResponseBody
//	public ModelAndView deleteUtilityExecutionAgency(@ModelAttribute Safety obj,RedirectAttributes attributes){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/utility-execution-agency");
//			boolean flag =  service.deleteUtilityExecutionAgency(obj);
//			if(flag) {
//				attributes.addFlashAttribute("success", "Utility Execution Agency Deleted Succesfully.");
//			}
//			else {
//				attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			}
//		}catch (Exception e) {
//			attributes.addFlashAttribute("error","Something went Wrong. Try again.");
//			logger.error("deleteUtilityExecutionAgency : " + e.getMessage());
//		}
//		return model;
//	}	
	
	
	
	
	
//	@DeleteMapping(
//		    value = "/delete-utility-execution-agency",
//		    consumes = MediaType.APPLICATION_JSON_VALUE,
//		    produces = MediaType.APPLICATION_JSON_VALUE
//		)
//		public ResponseEntity<?> deleteUtilityExecutionAgency(
//		        @RequestBody Safety obj) {
//
//		    try {
//		       
//
//		        boolean flag = service.deleteUtilityExecutionAgency(obj);
//
//		        if (flag) {
//		            return ResponseEntity.ok(
//		                Map.of("success", true, "message", "Utility Execution Agency deleted successfully")
//		            );
//		        }
//
//		        return ResponseEntity.badRequest().body(
//		            Map.of("success", false, "message", "Delete failed")
//		        );
//
//		    } catch (Exception e) {
//		        logger.error("deleteUtilityExecutionAgency", e);
//		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//		            Map.of("success", false, "message", "Error while deleting Utility Execution Agency")
//		        );
//		    }
//		}

	   
	   @DeleteMapping(
			    value = "/delete-utility-execution-agency/{executionAgency}",
			    produces = MediaType.APPLICATION_JSON_VALUE
			)
			public ResponseEntity<?> deleteUtilityExecutionAgency(
			        @PathVariable String executionAgency) {

			    try {
			        Safety obj = new Safety();
			        obj.setExecution_agency(executionAgency);

			        boolean flag = service.deleteUtilityExecutionAgency(obj);

			        if (flag) {
			            return ResponseEntity.ok(
			                Map.of("success", true, "message", "Utility Execution Agency deleted successfully")
			            );
			        }

			        return ResponseEntity.badRequest().body(
			            Map.of("success", false, "message", "Delete failed")
			        );

			    } catch (Exception e) {
			        logger.error("deleteUtilityExecutionAgency", e);
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
			            Map.of("success", false, "message", "Error while deleting Utility Execution Agency")
			        );
			    }
			}

}









