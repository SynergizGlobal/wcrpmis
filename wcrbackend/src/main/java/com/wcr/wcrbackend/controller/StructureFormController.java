package com.wcr.wcrbackend.controller;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.DTO.StructurePaginationObject;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.HomeService;
import com.wcr.wcrbackend.service.IStructureFormService;
import com.wcr.wcrbackend.service.IUserService;
import com.wcr.wcrbackend.service.StructureService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class StructureFormController {
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(StructureFormController.class);

	@Autowired
	IStructureFormService structureFormService;

	@Autowired
	StructureService structureService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	HomeService homeService;
	
	@Value("${common.error.message}")
	public String commonError;
	
	@Value("${record.dataexport.success}")
	public String dataExportSucess;
	
	@Value("${record.dataexport.invalid.directory}")
	public String dataExportInvalid;
	
	@Value("${record.dataexport.error}")
	public String dataExportError;
	
	@Value("${record.dataexport.nodata}")
	public String dataExportNoData;
	
	@Value("${template.upload.common.error}")
	public String uploadCommonError;
	
	@Value("${template.upload.formatError}")
	public String uploadformatError;
		

	
	@RequestMapping(value = "/ajax/getContractsFilterListInStructure", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Structure> getContractsListForFilter(@ModelAttribute Structure obj,HttpSession session) {
		List<Structure> contractsList = null;
		
		
		try {
			User uObj = (User) session.getAttribute("user"); 
			 obj.setUser_type_fk(uObj.getUserTypeFk());
			 obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		    obj.setUser_id(uObj.getUserId());	
				
			contractsList = structureFormService.getContractsListForFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractsListForFilter : " + e.getMessage());
		} 
		return contractsList;
	}
	
	
	  @RequestMapping(value = "/ajax/getStructureTypeListForFilter", method ={RequestMethod.GET,RequestMethod.POST},produces=MediaType. APPLICATION_JSON_VALUE)
	  @ResponseBody public List<Structure>getStructureTypeListForFilter(@ModelAttribute Structure obj,HttpSession session) { 
		  List<Structure> contractsList = null; 
		  try { User uObj = (User)
				  session.getAttribute("user"); 
			    obj.setUser_type_fk(uObj.getUserTypeFk());
			    obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		         obj.setUser_id(uObj.getUserId());	
				
				  contractsList = structureFormService.getStructureTypeListForFilter(obj); 
			 } 
		  catch (Exception e) { 
			  e.printStackTrace(); 
			  logger.error("getStructureTypeListForFilter : " + e.getMessage()); 
			 } 
		  return contractsList; 
	 }
	  
	  
	  @RequestMapping(value = "/ajax/getWorkStatusListInStructure", method ={RequestMethod.GET,RequestMethod.POST},produces=MediaType. APPLICATION_JSON_VALUE)
	  @ResponseBody public List<Structure> getWorkStatusListForFilter(@ModelAttribute Structure obj,HttpSession session) {
		  List<Structure> contractsList = null; 
		  try {   User uObj = (User)
				  session.getAttribute("user");
	      	    obj.setUser_type_fk(uObj.getUserTypeFk());
			    obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		    	obj.setUser_id(uObj.getUserId());	
			
				  
		    	contractsList =     structureFormService.getWorkStatusListForFilter(obj); 
			 } 
		  catch (Exception e) {
			  e.printStackTrace(); 
			  logger.error("getWorkStatusListForFilter : " +e.getMessage()); 
			  } 
		  return contractsList; 
		}
	 	
	
	
	@RequestMapping(value = "/ajax/getStructuresList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getStructureList(@ModelAttribute Structure obj, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		PrintWriter pw = null;
		//JSONObject json = new JSONObject();
		String json2 = null;
		String userId = null;
		String userName = null;
		List<Structure> contractList = null;
		
		try {
			userId = (String) session.getAttribute("USER_ID");
			userName = (String) session.getAttribute("USER_NAME");
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
		
			pw = response.getWriter();
			//Fetch the page number from client
			Integer pageNumber = 0;
			Integer pageDisplayLength = 0;
			if (null != request.getParameter("iDisplayStart")) {
				pageDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));
				pageNumber = (Integer.valueOf(request.getParameter("iDisplayStart")) / pageDisplayLength) + 1;
			}
			//Fetch search parameter
			String searchParameter = request.getParameter("sSearch");

			//Fetch Page display length
			pageDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));

			 contractList = new ArrayList<Structure>();

			//Here is server side pagination logic. Based on the page number you could make call 
			//to the data base create new list and send back to the client. For demo I am shuffling 
			//the same list to show data randomly
			int startIndex = 0;
			int offset = pageDisplayLength;

			if (pageNumber == 1) {
				startIndex = 0;
				offset = pageDisplayLength;
				contractList = createPaginationData(startIndex, offset, obj, searchParameter, session);
			} else {
				startIndex = (pageNumber * offset) - offset;
				offset = pageDisplayLength;
				contractList = createPaginationData(startIndex, offset, obj, searchParameter, session);
			}

			//Search functionality: Returns filtered list based on search parameter
			//contractList = getListBasedOnSearchParameter(searchParameter,contractList);

		

			StructurePaginationObject personJsonObject = new StructurePaginationObject();
			int totalRecords = getTotalRecords(obj, searchParameter);
			//Set Total display record
			personJsonObject.setITotalDisplayRecords(totalRecords);
			//Set Total record
			personJsonObject.setITotalRecords(totalRecords);
			personJsonObject.setAaData(contractList);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			json2 = gson.toJson(personJsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			contractList = new ArrayList<Structure>();
			logger.error("getStructureList : User Id - " + userId + " - User Name - " + userName + " - " + e.getMessage());
		}

		pw.println(json2);
	}

	/**
	 * @param searchParameter 
	 * @param activity 
	 * @return
	 */
	public int getTotalRecords(Structure obj, String searchParameter) {
		int totalRecords = 0;
		try {
			totalRecords = structureFormService.getTotalRecords(obj, searchParameter);
		} catch (Exception e) {
			logger.error("getTotalRecords : " + e.getMessage());
		}
		return totalRecords;
	}

	/**
	 * @param pageDisplayLength
	 * @param offset 
	 * @param activity 
	 * @param clientId 
	 * @return
	 */
	public List<Structure> createPaginationData(int startIndex, int offset,Structure obj, String searchParameter,HttpSession session) {
		List<Structure> contractsGridList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			contractsGridList = structureFormService.getStructuresList(obj, startIndex, offset, searchParameter);
		} catch (Exception e) {
			logger.error("createPaginationData : " + e.getMessage());
		}
		return contractsGridList;
	}
	
	
	
	@RequestMapping(value = "/get-structure-form", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody 
	public Map<String, Object> getStructuresForm(@ModelAttribute Structure obj) {
	    Map<String, Object> response = new HashMap<>();
	    
	
	    try {
	        response.put("action", "edit");
	        response.put("projectsList", structureService.getProjectsListForStructureForm(obj));
	        response.put("worksList", structureService.getWorkListForStructureForm(obj));
	        response.put("contractsList", structureService.getContractListForStructureFrom(obj));
	        response.put("structuresList", structureService.getStructuresListForStructureFrom(obj));
	        response.put("departmentsList", structureService.getDepartmentsListForStructureFrom(obj));
	        response.put("responsiblePeopleList", structureService.getResponsiblePeopleListForStructureForm(obj));
	        response.put("workStatusList", structureService.getWorkStatusListForStructureForm(obj));
	        response.put("unitsList", structureService.getUnitsListForStructureForm(obj));
	        response.put("fileType", structureService.getFileTypeForStructureForm(obj));
	        response.put("executionStatusList", homeService.getExecutionStatusList());
	        response.put("structureDetailsLocations", structureFormService.getStructureDetailsLocations(obj));
	        response.put("structureDetailsTypes", structureFormService.getStructureDetailsTypes(obj));
	        response.put("structuresListDetails", structureFormService.getStructuresFormDetails(obj));
	        
	    } catch (Exception e) {
	        logger.error("getStructuresForm : " + e.getMessage());
	        response.put("error", e.getMessage());
	        response.put("status", "error");
	    }
	    return response;
	}
	
	@RequestMapping(value = "/get-structure-form/{structure_id}", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody 
	public Map<String, Object> getStructuresForm1(@ModelAttribute Structure obj) {
	    Map<String, Object> response = new HashMap<>();
	    
	
	    try {
	        response.put("action", "edit");
	        response.put("projectsList", structureService.getProjectsListForStructureForm(obj));
	        response.put("worksList", structureService.getWorkListForStructureForm(obj));
	        response.put("contractsList", structureService.getContractListForStructureFrom(obj));
	        response.put("structuresList", structureService.getStructuresListForStructureFrom(obj));
	        response.put("departmentsList", structureService.getDepartmentsListForStructureFrom(obj));
	        response.put("responsiblePeopleList", structureService.getResponsiblePeopleListForStructureForm(obj));
	        response.put("workStatusList", structureService.getWorkStatusListForStructureForm(obj));
	        response.put("unitsList", structureService.getUnitsListForStructureForm(obj));
	        response.put("fileType", structureService.getFileTypeForStructureForm(obj));
	        response.put("executionStatusList", homeService.getExecutionStatusList());
	        response.put("structureDetailsLocations", structureFormService.getStructureDetailsLocations(obj));
	        response.put("structureDetailsTypes", structureFormService.getStructureDetailsTypes(obj));
	        response.put("structuresListDetails", structureFormService.getStructuresFormDetails(obj));
	        
	    } catch (Exception e) {
	        logger.error("getStructuresForm : " + e.getMessage());
	        response.put("error", e.getMessage());
	        response.put("status", "error");
	    }
	    return response;
	}
	
	
	@PostMapping("/update-structure-form")
	public ResponseEntity<?> updateStructure(@ModelAttribute Structure obj,
	                                         HttpSession session) {
	    try {
	        // Get user from session
	        User uObj = (User) session.getAttribute("user");
	        if (uObj == null) {
	            // session expired or user not logged in
	            return ResponseEntity.status(401).body(
	                Map.of("success", false, "message", "User session expired. Please login again.")
	            );
	        }

	        String user_Id = uObj.getUserId();
	        String userName = uObj.getUserName();
	        String userDesignation = uObj.getDesignation(); 

	        obj.setUser_id(user_Id);
	        obj.setUser_name(userName);
	        obj.setDesignation(userDesignation);

	        obj.setConstruction_start_date(DateParser.parse(obj.getConstruction_start_date()));	
	        obj.setTarget_date(DateParser.parse(obj.getTarget_date()));	
	        obj.setRevised_completion(DateParser.parse(obj.getRevised_completion()));	
	        obj.setCommissioning_date(DateParser.parse(obj.getCommissioning_date()));	
	        obj.setActual_completion_date(DateParser.parse(obj.getActual_completion_date()));	

	        boolean flag = structureFormService.updateStructureForm(obj);

	        if (flag) {
	            return ResponseEntity.ok().body(
	                Map.of("success", true, "message", "Structure updated")
	            );
	        } else {
	            return ResponseEntity.status(400).body(
	                Map.of("success", false, "message", "Update failed")
	            );
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(
	            Map.of("success", false, "error", e.getMessage())
	        );
	    }
	}

}
