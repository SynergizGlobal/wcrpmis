package com.wcr.wcrbackend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.Design;
import com.wcr.wcrbackend.DTO.DesignsPaginationObject;
import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IContractService;
import com.wcr.wcrbackend.service.IDesignService;
import com.wcr.wcrbackend.service.IIssueService;
import com.wcr.wcrbackend.service.ISafetyService;
import com.wcr.wcrbackend.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/design")
public class DesignController {
	
	@Autowired
	private IDesignService designService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ISafetyService safetyService;
	
	@Autowired
	private IContractService contractService;
	
	@Autowired
	private IIssueService issueService;
	
	
	Logger logger = Logger.getLogger(DesignController.class);
	
	
	
	@PostMapping(value = "/ajax/form/update-design-status/projectsList")
	public List<Safety> getProjectsListForSafetyForm(@RequestBody Safety obj, HttpSession session) {
		List<Safety> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_id(uObj.getUserId());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			objList = safetyService.getProjectsListForSafetyForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getP6ActivityData : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/form/update-design-status/contractsList")
	public List<Safety> getContractsListForSafetyForm(@RequestBody Safety obj, HttpSession session) {
		List<Safety> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_id(uObj.getUserId());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setDepartment_fk(uObj.getDepartmentFk());
			objList = safetyService.getContractsListForSafetyForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getP6ActivityData : " + e.getMessage());
		}
		return objList;
	}
	
	
	@PostMapping(value = "/ajax/getP6ActivitiesData")
	public List<Design> getP6ActivitiesData(@RequestBody Design obj) {
		List<Design> objList = null;
		try {
			objList = designService.getP6ActivitiesData(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getP6ActivityData : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getDesignUpdateStructures")
	public List<Design> getDesignUpdateStructures(@RequestBody Design obj) {
		List<Design> objList = null;
		try {
			objList = designService.getDesignUpdateStructures(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesignUpdateStructures : " + e.getMessage());
		}
		return objList;
	}	
	
	@PostMapping(value = "/ajax/getHodListFilterInDesign")
	public List<Design> getHodListFilter(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getHodListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getHodListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getDepartmentListFilterInDesign")
	public List<Design> getDepartmentListFilter(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getDepartmentListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDepartmentListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getContractListFilterInDesign")
	public List<Design> getContractListFilter(@RequestBody Design obj, HttpSession session) {
		List<Design> design = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());	
			
			design = designService.getContractListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getStructureListFilterInDesign")
	public List<Design> getStructureListFilter(@RequestBody Design obj, HttpSession session) {
		List<Design> design = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());				
			design = designService.getStructureListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getStructureIdListFilter")
	public List<Design> getStructureIdListFilter(@RequestBody Design obj, HttpSession session) {
		List<Design> design = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());			
			design = designService.getStructureIdsListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureIdsListFilter : " + e.getMessage());
		}
		return design;
	}	
	
	@PostMapping(value = "/ajax/getDrawingTypeListFilterInDesign")
	public List<Design> getDrawingTypeListFilter(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getDrawingTypeListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDrawingTypeListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getDesigns")
	public List<Design> getDesigns(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getDesigns(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getDesignsList")
	public DesignsPaginationObject getActivitiesList(@RequestBody Design obj, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		//PrintWriter pw = null;
		//JSONObject json = new JSONObject();
		String json2 = null;
		String userId = null;
		String userName = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			userId = uObj.getUserId();
			userName = uObj.getUserName();
			
			
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

			List<Design> designsList = new ArrayList<Design>();

			//Here is server side pagination logic. Based on the page number you could make call 
			//to the data base create new list and send back to the client. For demo I am shuffling 
			//the same list to show data randomly
			int startIndex = 0;
			int offset = pageDisplayLength;

			if (pageNumber == 1) {
				startIndex = 0;
				offset = pageDisplayLength;
				designsList = createPaginationData(startIndex, offset, obj, searchParameter);
			} else {
				startIndex = (pageNumber * offset) - offset;
				offset = pageDisplayLength;
				designsList = createPaginationData(startIndex, offset, obj, searchParameter);
			}

			//Search functionality: Returns filtered list based on search parameter
			//designsList = getListBasedOnSearchParameter(searchParameter,designsList);

			int totalRecords = getTotalRecords(obj, searchParameter,session);

			DesignsPaginationObject personJsonObject = new DesignsPaginationObject();
			//Set Total display record
			personJsonObject.setiTotalDisplayRecords(totalRecords);
			//Set Total record
			personJsonObject.setiTotalRecords(totalRecords);
			personJsonObject.setAaData(designsList);

			return personJsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"getActivitiesList : User Id - " + userId + " - User Name - " + userName + " - " + e.getMessage());
		}

		return null;
	}
	
	@RequestMapping(value = "/ajax/getDrawingRepositoryList")
	public DesignsPaginationObject getDrawingRepositoryList(@RequestBody Design obj, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		//PrintWriter pw = null;
		//JSONObject json = new JSONObject();
		String json2 = null;
		String userId = null;
		String userName = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			
			userId = uObj.getUserId();
			userName = uObj.getUserName();

			//pw = response.getWriter();
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

			List<Design> designsList = new ArrayList<Design>();

			//Here is server side pagination logic. Based on the page number you could make call 
			//to the data base create new list and send back to the client. For demo I am shuffling 
			//the same list to show data randomly
			int startIndex = 0;
			int offset = pageDisplayLength;

			if (pageNumber == 1) {
				startIndex = 0;
				offset = pageDisplayLength;
				designsList = createDrawingRepositoryPaginationData(startIndex, offset, obj, searchParameter,session);
			} else {
				startIndex = (pageNumber * offset) - offset;
				offset = pageDisplayLength;
				designsList = createDrawingRepositoryPaginationData(startIndex, offset, obj, searchParameter,session);
			}

			//Search functionality: Returns filtered list based on search parameter
			//designsList = getListBasedOnSearchParameter(searchParameter,designsList);

			int totalRecords = getTotalDrawingRepositoryRecords(obj, searchParameter,session);

			DesignsPaginationObject personJsonObject = new DesignsPaginationObject();
			//Set Total display record
			personJsonObject.setiTotalDisplayRecords(totalRecords);
			//Set Total record
			personJsonObject.setiTotalRecords(totalRecords);
			personJsonObject.setAaData(designsList);

			return personJsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"getActivitiesList : User Id - " + userId + " - User Name - " + userName + " - " + e.getMessage());
		}

		return null;
	}	
	
	
	public List<Design> createPaginationData(int startIndex, int offset, Design obj, String searchParameter) {
		List<Design> earthWorkList = null;
		try {
			earthWorkList = designService.getDesignsList(obj, startIndex, offset, searchParameter);
		} catch (Exception e) {
			logger.error("createPaginationData : " + e.getMessage());
		}
		return earthWorkList;
	}
	
	public int getTotalRecords(Design obj, String searchParameter,HttpSession session) {
		int totalRecords = 0;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());				
			totalRecords = designService.getTotalRecords(obj, searchParameter);
		} catch (Exception e) {
			logger.error("getTotalRecords : " + e.getMessage());
		}
		return totalRecords;
	}
	
	public List<Design> createDrawingRepositoryPaginationData(int startIndex, int offset, Design obj, String searchParameter, HttpSession session) {
		List<Design> earthWorkList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());				
			earthWorkList = designService.getDrawingRepositoryDesignsList(obj, startIndex, offset, searchParameter);
		} catch (Exception e) {
			logger.error("createDrawingRepositoryPaginationData : " + e.getMessage());
		}
		return earthWorkList;
	}	
	
	private int getTotalDrawingRepositoryRecords(Design obj, String searchParameter,HttpSession session) {
		int totalRecords = 0;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());			
			totalRecords = designService.getTotalDrawingRepositoryRecords(obj, searchParameter);
		} catch (Exception e) {
			logger.error("getTotalRecords : " + e.getMessage());
		}
		return totalRecords;
	}
	
	@PostMapping(value = "/ajax/form/get-design/projectsList")
	public List<Design> getProjectsListForDesignForm(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getProjectsListForDesignForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/departmentList")
	public List<Contract> getDepartmentList() {
		List<Contract> design = null;
		try {
			design = contractService.getDepartmentList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/contractList")
	public List<Design> getContractList() {
		List<Design> design = null;
		try {
			design = designService.getContractList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/preparedBy")
	public List<Design> getPreparedByList() {
		List<Design> design = null;
		try {
			design = designService.getPreparedByList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/componentList")
	public List<Design> componentList() {
		List<Design> design = null;
		try {
			design = designService.componentList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/approvingRailway")
	public List<Design> getApprovingRailwayList() {
		List<Design> design = null;
		try {
			design = designService.getApprovingRailwayList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/structureTypeList")
	public List<Design> structureList() {
		List<Design> design = null;
		try {
			design = designService.structureList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/drawingTypeList")
	public List<Design> drawingTypeList() {
		List<Design> design = null;
		try {
			design = designService.drawingTypeList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/revisionStatuses")
	public List<Design> getRevisionStatuses() {
		List<Design> design = null;
		try {
			design = designService.getRevisionStatuses();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/approvalAuthority")
	public List<Design> getApprovalAuthority() {
		List<Design> design = null;
		try {
			design = designService.getApprovalAuthority();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	@GetMapping(value = "/ajax/form/get-design/stage")
	public List<Design> getStage() {
		List<Design> design = null;
		try {
			design = designService.getStage();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	@GetMapping(value = "/ajax/form/get-design/submitted")
	public List<Design> getSubmitted() {
		List<Design> design = null;
		try {
			design = designService.getSubmitted();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/structureId")
	public List<Design> getStructureId() {
		List<Design> design = null;
		try {
			design = designService.getStructureId();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/submssionpurpose")
	public List<Design> getSubmssionpurpose() {
		List<Design> design = null;
		try {
			design = designService.getSubmssionpurpose();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@GetMapping(value = "/ajax/form/get-design/designFileType")
	public List<Design> getDesignFileType() {
		List<Design> design = null;
		try {
			design = designService.getDesignFileType();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	@GetMapping(value = "/ajax/form/get-design/asBuiltStatuses")
	public List<Design> getAsBuiltStatuses() {
		List<Design> design = null;
		try {
			design = designService.getAsBuiltStatuses();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/form/get-design/design")
	public Map<String,List<Design>> getDesignPayloadForDesign(@RequestBody Design obj) {
		Map<String, List<Design>> design = new HashMap<>();
		try {
			List<Design> projectsList = designService.getProjectsListForDesignForm(obj);
			design.put("projectsList", projectsList);
			
			List<Design> contractList = designService.getContractList();
			design.put("contractList", contractList);
			
			List<Design> preparedBy = designService.getPreparedByList();
			design.put("preparedBy", preparedBy);
			
			List<Design> componentList = designService.componentList();
			design.put("componentList", componentList);			

			List<Design> approvingRailway = designService.getApprovingRailwayList();
			design.put("approvingRailway", approvingRailway);
			
			List<Design> structureTypeList = designService.structureList();
			design.put("structureTypeList", structureTypeList);
			
			List<Design> drawingTypeList = designService.drawingTypeList();
			design.put("drawingTypeList", drawingTypeList);
			
			List<Design> revisionStatuses = designService.getRevisionStatuses();
			design.put("revisionStatuses", revisionStatuses);
			
			List<Design> approvalAuthority = designService.getApprovalAuthority();
			design.put("approvalAuthority", approvalAuthority);
			
			List<Design> stage = designService.getStage();
			design.put("stage", stage);
			
			List<Design> submitted = designService.getSubmitted();
			design.put("submitted", submitted);
			
			List<Design> structureId = designService.getStructureId();
			design.put("structureId", structureId);
			
			List<Design> submssionpurpose = designService.getSubmssionpurpose();
			design.put("submssionpurpose", submssionpurpose);
			
			List<Design> designFileType = designService.getDesignFileType();
			design.put("designFileType", designFileType);
		
			List<Design> asBuiltStatuses = designService.getAsBuiltStatuses();
			design.put("asBuiltStatuses", asBuiltStatuses);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	@GetMapping(value = "/ajax/form/get-design/issue")
	public Map<String,List<Issue>> getDesignPayloadForIssue() {
		Map<String, List<Issue>> design = new HashMap<>();
		try {
			Issue iObj = new Issue();
			List<Issue> issueCategoryList = issueService.getIssuesCategoryList(iObj);	
			design.put("issueCategoryList", issueCategoryList);
			
			List<Issue> issuePriorityList = issueService.getIssuesPriorityList();
			design.put("issuePriorityList", issuePriorityList);
					
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
}
