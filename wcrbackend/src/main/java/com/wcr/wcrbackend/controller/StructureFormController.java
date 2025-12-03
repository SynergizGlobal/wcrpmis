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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
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
	
//	@RequestMapping(value="/structure-form",method={RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView structureForm(@ModelAttribute Structure obj,HttpSession session){
//		ModelAndView model = new ModelAndView(PageConstants.structureFormGrid);		
//		try {
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("structure : " + e.getMessage());
//		}
//		return model;
//	}
//	
//	@RequestMapping(value = "/ajax/getWorkStatusFilterListInStructure", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public List<Structure> getWorkStatusList(@ModelAttribute Structure obj,HttpSession session) {
//		List<Structure> workStatusList = null;
//		try {
//			User uObj = (User) session.getAttribute("user");
//			obj.setUser_type_fk(uObj.getUser_type_fk());
//			obj.setUser_role_code(uObj.getUser_role_code());
//			obj.setUser_id(uObj.getUser_id());				
//			workStatusList = structureFormService.getWorkStatusList(obj);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("getWorkStatusList : " + e.getMessage());
//		}
//		return workStatusList;
//	}
//	

	
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
			
			
			System.out.println("uObj UserType Fk"+ uObj.getUserTypeFk());
			System.out.println("uObj UserRole"+ uObj.getUserTypeFk());
			System.out.println("uObj UserId"+ uObj.getUserId());
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
	
	//////////////
	///
//	@RequestMapping(value = "/get-structure-form", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView getStructuresForm(@ModelAttribute Structure obj){
//		ModelAndView model = new ModelAndView();
//		
//		System.out.println("Method called.......");
//		try{
//			//model.setViewName(PageConstants.addEditStructureForm);	
//			model.addObject("action", "edit");	
//			List<Structure> projectsList = structureService.getProjectsListForStructureForm(obj);
//			model.addObject("projectsList", projectsList);
//			
//			List<Structure> worksList = structureService.getWorkListForStructureForm(obj);
//			model.addObject("worksList", worksList);
//			
//			List<Structure> contractsList = structureService.getContractListForStructureFrom(obj);
//			model.addObject("contractsList", contractsList);
//			
//			List<Structure> structuresList = structureService.getStructuresListForStructureFrom(obj);
//			model.addObject("structuresList", structuresList);
//			
//			List<Structure> departmentsList = structureService.getDepartmentsListForStructureFrom(obj);
//			model.addObject("departmentsList", departmentsList);
//		
//			List<Structure> responsiblePeopleList = structureService.getResponsiblePeopleListForStructureForm(obj);
//			model.addObject("responsiblePeopleList", responsiblePeopleList);
//			
//			List<Structure> workStatusList = structureService.getWorkStatusListForStructureForm(obj);
//			model.addObject("workStatusList", workStatusList);
//			
//			List<Structure> unitsList = structureService.getUnitsListForStructureForm(obj);
//			model.addObject("unitsList", unitsList);
//			
//			List<Structure> fileType = structureService.getFileTypeForStructureForm(obj);
//			model.addObject("fileType", fileType);
//			
//			List<String> executionStatusList = homeService.getExecutionStatusList();
//			model.addObject("executionStatusList", executionStatusList);
//			
//			List<Structure> structureDetailsLocations = structureFormService.getStructureDetailsLocations(obj);
//			model.addObject("structureDetailsLocations", structureDetailsLocations);
//			
//			List<Structure> structureDetailsTypes = structureFormService.getStructureDetailsTypes(obj);
//			model.addObject("structureDetailsTypes", structureDetailsTypes);
//			
//			
//			Structure structuresListDetails = structureFormService.getStructuresFormDetails(obj);
//			model.addObject("structuresListDetails", structuresListDetails);
//			
//		}catch (Exception e) {
//			logger.error("getStructuresForm : " + e.getMessage());
//		}
//		return model;
//	}	
	
	@RequestMapping(value = "/get-structure-form", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody  // ← Add this annotation
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
	
	
//	@RequestMapping(value = "/update-structure-form", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView updateStructuresForm(@ModelAttribute Structure obj,RedirectAttributes attributes,HttpSession session){
//		ModelAndView model = new ModelAndView();
//		try{
//			//model.setViewName("redirect:/structure-form"); 
//			String user_Id = (String) session.getAttribute("USER_ID");
//			String userName = (String) session.getAttribute("USER_NAME");
//			String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
//			
//			//obj.setCreated_by_user_id_fk(user_Id);
//			obj.setUser_id(user_Id);
//			obj.setUser_name(userName);
//			obj.setDesignation(userDesignation);
//			obj.setConstruction_start_date(DateParser.parse(obj.getConstruction_start_date()));	
//			obj.setTarget_date(DateParser.parse(obj.getTarget_date()));	
//			obj.setRevised_completion(DateParser.parse(obj.getRevised_completion()));	
//			obj.setCommissioning_date(DateParser.parse(obj.getCommissioning_date()));	
//			obj.setActual_completion_date(DateParser.parse(obj.getActual_completion_date()));	
//			boolean flag =  structureFormService.updateStructureForm(obj);	
//			if(flag) {
//				attributes.addFlashAttribute("success", "Structure Updated Succesfully......."); 
//			} else {
//				attributes.addFlashAttribute("error","Updating Structure is failed. Try again.");
//			}		
//			
//			
//		}catch (Exception e) {
//			logger.error("updateStructurseForm : " + e.getMessage());
//		}
//		return model;
//	}	
	
	
	
	

    @PostMapping("/update-structure-form")
    public ResponseEntity<?> updateStructure(@RequestBody Structure obj,
                                             HttpSession session) {
        try {
            String user_Id = (String) session.getAttribute("USER_ID");
            String userName = (String) session.getAttribute("USER_NAME");
            String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
            
            System.out.println("Method called..................");
            
            System.out.println("USer ID"+ user_Id);
            System.out.println("USER Name"+ userName);
            System.out.println("USer Degination"+ userDesignation);

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
                return ResponseEntity.ok().body(Map.of("success", true, "message", "Structure updated"));
            } else {
                return ResponseEntity.status(400).body(Map.of("success", false, "message", "Update failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}
//	@RequestMapping(value = "/get-structure-form/{structure_id}", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView getStructuresForm(@ModelAttribute Structure obj,@PathVariable("structure_id") String structure_id,HttpSession session,RedirectAttributes attributes ){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName(PageConstants.addEditStructureForm);	
//			model.addObject("action", "edit");	
//			List<Structure> projectsList = structureService.getProjectsListForStructureForm(obj);
//			model.addObject("projectsList", projectsList);
//			
//			List<Structure> worksList = structureService.getWorkListForStructureForm(obj);
//			model.addObject("worksList", worksList);
//			
//			List<Structure> contractsList = structureService.getContractListForStructureFrom(obj);
//			model.addObject("contractsList", contractsList);
//			
//			List<Structure> structuresList = structureService.getStructuresListForStructureFrom(obj);
//			model.addObject("structuresList", structuresList);
//			
//			List<Structure> departmentsList = structureService.getDepartmentsListForStructureFrom(obj);
//			model.addObject("departmentsList", departmentsList);
//		
//			List<Structure> responsiblePeopleList = structureService.getResponsiblePeopleListForStructureForm(obj);
//			model.addObject("responsiblePeopleList", responsiblePeopleList);
//			
//			List<Structure> workStatusList = structureService.getWorkStatusListForStructureForm(obj);
//			model.addObject("workStatusList", workStatusList);
//			
//			List<Structure> unitsList = structureService.getUnitsListForStructureForm(obj);
//			model.addObject("unitsList", unitsList);
//			
//			List<Structure> fileType = structureService.getFileTypeForStructureForm(obj);
//			model.addObject("fileType", fileType);
//			
//			List<String> executionStatusList = homeService.getExecutionStatusList();
//			model.addObject("executionStatusList", executionStatusList);
//			
//			List<Structure> structureDetailsLocations = structureFormService.getStructureDetailsLocations(obj);
//			model.addObject("structureDetailsLocations", structureDetailsLocations);
//			
//			List<Structure> structureDetailsTypes = structureFormService.getStructureDetailsTypes(obj);
//			model.addObject("structureDetailsTypes", structureDetailsTypes);
//			
//			
//			Structure structuresListDetails = structureFormService.getStructuresFormDetails(obj);
//			model.addObject("structuresListDetails", structuresListDetails);
//			
//		}catch (Exception e) {
//			logger.error("getStructuresForm : " + e.getMessage());
//		}
//		return model;
//	}

	
//	@RequestMapping(value = "/add-structures-form", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView addStructuresForm(@ModelAttribute Structure obj){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName(PageConstants.addEditStructureForm);	
//			model.addObject("action", "add");
//			List<Structure> projectsList = structureService.getProjectsListForStructureForm(obj);
//			model.addObject("projectsList", projectsList);
//			
//			List<Structure> worksList = structureService.getWorkListForStructureForm(obj);
//			model.addObject("worksList", worksList);
//			
//			List<Structure> contractsList = structureService.getContractListForStructureFrom(obj);
//			model.addObject("contractsList", contractsList);
//			
//			List<Structure> structuresList = structureService.getStructuresListForStructureFrom(obj);
//			model.addObject("structuresList", structuresList);
//			
//			List<Structure> departmentsList = structureService.getDepartmentsListForStructureFrom(obj);
//			model.addObject("departmentsList", departmentsList);
//			
//			List<Structure> responsiblePeopleList = structureService.getResponsiblePeopleListForStructureForm(obj);
//			model.addObject("responsiblePeopleList", responsiblePeopleList);
//			
//			List<Structure> workStatusList = structureService.getWorkStatusListForStructureForm(obj);
//			model.addObject("workStatusList", workStatusList);
//			
//			List<Structure> unitsList = structureService.getUnitsListForStructureForm(obj);
//			model.addObject("unitsList", unitsList);
//			
//			List<Structure> fileType = structureService.getFileTypeForStructureForm(obj);
//			model.addObject("fileType", fileType);
//			
//			List<String> executionStatusList = homeService.getExecutionStatusList();
//			model.addObject("executionStatusList", executionStatusList);
//		
//		}catch (Exception e) {
//			logger.error("addStructuresForm : " + e.getMessage());
//		}
//		return model;
//	}	
//	
//	@RequestMapping(value = "/ajax/getContractsListForStructureFrom", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public List<Structure> getContractsListForStructureFrom(@ModelAttribute Structure obj,HttpSession session) {
//		List<Structure> objList = null;
//		try {
//			objList = structureService.getContractListForStructureFrom(obj);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("getContractListForStructureFrom : " + e.getMessage());
//		}
//		return objList;
//	}
//	
//
//	
//	@RequestMapping(value = "/get-structure-form", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView getStructuresForm(@ModelAttribute Structure obj){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName(PageConstants.addEditStructureForm);	
//			model.addObject("action", "edit");	
//			List<Structure> projectsList = structureService.getProjectsListForStructureForm(obj);
//			model.addObject("projectsList", projectsList);
//			
//			List<Structure> worksList = structureService.getWorkListForStructureForm(obj);
//			model.addObject("worksList", worksList);
//			
//			List<Structure> contractsList = structureService.getContractListForStructureFrom(obj);
//			model.addObject("contractsList", contractsList);
//			
//			List<Structure> structuresList = structureService.getStructuresListForStructureFrom(obj);
//			model.addObject("structuresList", structuresList);
//			
//			List<Structure> departmentsList = structureService.getDepartmentsListForStructureFrom(obj);
//			model.addObject("departmentsList", departmentsList);
//		
//			List<Structure> responsiblePeopleList = structureService.getResponsiblePeopleListForStructureForm(obj);
//			model.addObject("responsiblePeopleList", responsiblePeopleList);
//			
//			List<Structure> workStatusList = structureService.getWorkStatusListForStructureForm(obj);
//			model.addObject("workStatusList", workStatusList);
//			
//			List<Structure> unitsList = structureService.getUnitsListForStructureForm(obj);
//			model.addObject("unitsList", unitsList);
//			
//			List<Structure> fileType = structureService.getFileTypeForStructureForm(obj);
//			model.addObject("fileType", fileType);
//			
//			List<String> executionStatusList = homeService.getExecutionStatusList();
//			model.addObject("executionStatusList", executionStatusList);
//			
//			List<Structure> structureDetailsLocations = structureFormService.getStructureDetailsLocations(obj);
//			model.addObject("structureDetailsLocations", structureDetailsLocations);
//			
//			List<Structure> structureDetailsTypes = structureFormService.getStructureDetailsTypes(obj);
//			model.addObject("structureDetailsTypes", structureDetailsTypes);
//			
//			
//			Structure structuresListDetails = structureFormService.getStructuresFormDetails(obj);
//			model.addObject("structuresListDetails", structuresListDetails);
//			
//		}catch (Exception e) {
//			logger.error("getStructuresForm : " + e.getMessage());
//		}
//		return model;
//	}	
//	
//	@RequestMapping(value = "/get-structure-form/{structure_id}", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView getStructuresForm(@ModelAttribute Structure obj,@PathVariable("structure_id") String structure_id,HttpSession session,RedirectAttributes attributes ){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName(PageConstants.addEditStructureForm);	
//			model.addObject("action", "edit");	
//			List<Structure> projectsList = structureService.getProjectsListForStructureForm(obj);
//			model.addObject("projectsList", projectsList);
//			
//			List<Structure> worksList = structureService.getWorkListForStructureForm(obj);
//			model.addObject("worksList", worksList);
//			
//			List<Structure> contractsList = structureService.getContractListForStructureFrom(obj);
//			model.addObject("contractsList", contractsList);
//			
//			List<Structure> structuresList = structureService.getStructuresListForStructureFrom(obj);
//			model.addObject("structuresList", structuresList);
//			
//			List<Structure> departmentsList = structureService.getDepartmentsListForStructureFrom(obj);
//			model.addObject("departmentsList", departmentsList);
//		
//			List<Structure> responsiblePeopleList = structureService.getResponsiblePeopleListForStructureForm(obj);
//			model.addObject("responsiblePeopleList", responsiblePeopleList);
//			
//			List<Structure> workStatusList = structureService.getWorkStatusListForStructureForm(obj);
//			model.addObject("workStatusList", workStatusList);
//			
//			List<Structure> unitsList = structureService.getUnitsListForStructureForm(obj);
//			model.addObject("unitsList", unitsList);
//			
//			List<Structure> fileType = structureService.getFileTypeForStructureForm(obj);
//			model.addObject("fileType", fileType);
//			
//			List<String> executionStatusList = homeService.getExecutionStatusList();
//			model.addObject("executionStatusList", executionStatusList);
//			
//			List<Structure> structureDetailsLocations = structureFormService.getStructureDetailsLocations(obj);
//			model.addObject("structureDetailsLocations", structureDetailsLocations);
//			
//			List<Structure> structureDetailsTypes = structureFormService.getStructureDetailsTypes(obj);
//			model.addObject("structureDetailsTypes", structureDetailsTypes);
//			
//			
//			Structure structuresListDetails = structureFormService.getStructuresFormDetails(obj);
//			model.addObject("structuresListDetails", structuresListDetails);
//			
//		}catch (Exception e) {
//			logger.error("getStructuresForm : " + e.getMessage());
//		}
//		return model;
//	}
//	@RequestMapping(value = "/add-structures", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView addStructureForm(@ModelAttribute Structure obj,RedirectAttributes attributes,HttpSession session){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/structure-form");
//			String user_Id = (String) session.getAttribute("USER_ID");
//			String userName = (String) session.getAttribute("USER_NAME");
//			String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
//			
//			//obj.setCreated_by_user_id_fk(user_Id);
//			obj.setUser_id(user_Id);
//			obj.setUser_name(userName);
//			obj.setDesignation(userDesignation);
//			obj.setConstruction_start_date(DateParser.parse(obj.getConstruction_start_date()));	
//			obj.setTarget_date(DateParser.parse(obj.getTarget_date()));	
//			obj.setRevised_completion(DateParser.parse(obj.getRevised_completion()));	
//			obj.setCommissioning_date(DateParser.parse(obj.getCommissioning_date()));	
//			obj.setActual_completion_date(DateParser.parse(obj.getActual_completion_date()));	
//			boolean flag =  structureFormService.addStructureForm(obj);	
//			if(flag) {
//				attributes.addFlashAttribute("success", "Structure Added Succesfully."); 
//			} else {
//				attributes.addFlashAttribute("error","Adding Structure is failed. Try again.");
//			}
//			
//		}catch (Exception e) {
//			logger.error("addStructureForm : " + e.getMessage());
//		}
//		return model;
//	}	
//	
//	@RequestMapping(value = "/update-structure-form", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView updateStructuresForm(@ModelAttribute Structure obj,RedirectAttributes attributes,HttpSession session){
//		ModelAndView model = new ModelAndView();
//		try{
//			model.setViewName("redirect:/structure-form"); 
//			String user_Id = (String) session.getAttribute("USER_ID");
//			String userName = (String) session.getAttribute("USER_NAME");
//			String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
//			
//			//obj.setCreated_by_user_id_fk(user_Id);
//			obj.setUser_id(user_Id);
//			obj.setUser_name(userName);
//			obj.setDesignation(userDesignation);
//			obj.setConstruction_start_date(DateParser.parse(obj.getConstruction_start_date()));	
//			obj.setTarget_date(DateParser.parse(obj.getTarget_date()));	
//			obj.setRevised_completion(DateParser.parse(obj.getRevised_completion()));	
//			obj.setCommissioning_date(DateParser.parse(obj.getCommissioning_date()));	
//			obj.setActual_completion_date(DateParser.parse(obj.getActual_completion_date()));	
//			boolean flag =  structureFormService.updateStructureForm(obj);	
//			if(flag) {
//				attributes.addFlashAttribute("success", "Structure Updated Succesfully......."); 
//			} else {
//				attributes.addFlashAttribute("error","Updating Structure is failed. Try again.");
//			}		
//			
//			
//		}catch (Exception e) {
//			logger.error("updateStructurseForm : " + e.getMessage());
//		}
//		return model;
//	}	

