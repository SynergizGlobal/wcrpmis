package com.wcr.wcrbackend.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.Design;
import com.wcr.wcrbackend.DTO.DesignsPaginationObject;
import com.wcr.wcrbackend.DTO.FileFormatModel;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.IFormsHistoryDao;
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
	
	@Autowired
	IFormsHistoryDao formsHistoryDao;
	
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
	
	@PostMapping(value = "/ajax/form/update-design-status/update-design-status")
	public boolean updateDesignStatusBulk(@RequestBody Design obj,RedirectAttributes attributes,HttpSession session){
		//ModelAndView model = new ModelAndView();
		try{
			//model.setViewName("redirect:/update-design-status");
			User uObj = (User) session.getAttribute("user");
			String user_Id = uObj.getUserId();
			String userName = uObj.getUserName();
			String userDesignation = uObj.getDesignation();

			obj.setCreated_by_user_id_fk(user_Id);
			
			obj.setCreated_by_user_id_fk(user_Id);
			obj.setUser_name(userName);
			obj.setDesignation(userDesignation);
			
			//User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());			
			//obj.setProgress_date(DateParser.parse(obj.getProgress_date()));
			boolean flag =  designService.updateDesignStatusBulk(obj);
			/*if(flag) {
				attributes.addFlashAttribute("success", "Design Status Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("success", "Design Status Updated Succesfully.");
				//attributes.addFlashAttribute("error","Updating Design Status are failed. Try again.");
			}*/
			return flag;
		}catch (Exception e) {
			//attributes.addFlashAttribute("success", "Design Status Updated Succesfully.");
			//attributes.addFlashAttribute("error","Updating Design Status are failed. Try again.");
			logger.error("updateAcivitiesBulk : " + e.getMessage());
		}
		return false;
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
		    String contractId = request.getParameter("contract_id_fk");
		    String structureType = request.getParameter("structure_type_fk");
		    String drawingType = request.getParameter("drawing_type_fk");
		    
		    obj.setContract_id_fk(contractId);
		    obj.setStructure_type_fk(structureType);
		    obj.setDrawing_type_fk(drawingType);


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
	
	/*@PostMapping(value = "/ajax/form/get-design/projectsList")
	public List<Design> getProjectsListForDesignForm(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getProjectsListForDesignForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}*/
	
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
	
	/*@GetMapping(value = "/ajax/form/get-design/contractList")
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
	}*/
	
	@PostMapping(value = "/ajax/form/get-design/design")
	public Map<String,List<Design>> getDesignPayloadForDesign(@RequestBody Design obj) {
		Map<String, List<Design>> design = new LinkedHashMap<>();
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
		Map<String, List<Issue>> design = new LinkedHashMap<>();
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
	@PostMapping(value = "/ajax/form/get-design/designDetails")
	public Design getDesignDetails(@RequestBody Design obj) {
		Design designDetails = null;
		try {
			designDetails = designService.getDesignDetails(obj);	
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return designDetails;
	}
	
	@PostMapping(value = "/ajax/form/drawing-repository")
	public Map<String, List<Design>> DrawingRepository(@RequestBody Design obj,HttpSession session){
		Map<String, List<Design>> model = new LinkedHashMap<>();
		try{
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());			
			
			List<Design> contractList = designService.getContractListFilter(obj);
			model.put("contractList", contractList);
			
			
			List<Design> structureTypeList = designService.getStructureTypeListFilter(obj);
			model.put("structureTypeList", structureTypeList);
			
			List<Design> structureId = designService.getStructureIdsListFilter(obj);
			model.put("structureId", structureId);			


		}catch (Exception e) {
			logger.error("Drawing Repository : " + e.getMessage());
		}
		return model;
	}
	
	@PostMapping(value = "/ajax/form/add-design-form")
	public Map<String, List<Design>> addDesignForm(@RequestBody Design obj){
		Map<String, List<Design>> model = new LinkedHashMap<>();
		try{
			
			List<Design> projectsList = designService.getProjectsListForDesignForm(obj);
			model.put("projectsList", projectsList);
			
			List<Design> contractsList = designService.getContractsListForDesignForm(obj);
			model.put("contractsList", contractsList);
			
			List<Design> contractList = designService.getContractList();
			model.put("contractList", contractList);
			
			List<Design> preparedBy = designService.getPreparedByList();
			model.put("preparedBy", preparedBy);
			
			List<Design> componentList = designService.componentList();
			model.put("componentList", componentList);			
			
			List<Design> approvingRailway = designService.getApprovingRailwayList();
			model.put("approvingRailway", approvingRailway);
			
			List<Design> structureTypeList = designService.structureList();
			model.put("structureTypeList", structureTypeList);
			
			List<Design> drawingTypeList = designService.drawingTypeList();
			model.put("drawingTypeList", drawingTypeList);
			
			List<Design> revisionStatuses = designService.getRevisionStatuses();
			model.put("revisionStatuses", revisionStatuses);
			
			List<Design> approvalAuthority = designService.getApprovalAuthority();
			model.put("approvalAuthority", approvalAuthority);
			
			List<Design> structureId = designService.getStructureId();
			model.put("structureId", structureId);
			
			List<Design> stage = designService.getStage();
			model.put("stage", stage);
			
			List<Design> submitted = designService.getSubmitted();
			model.put("submitted", submitted);
			
			List<Design> submssionpurpose = designService.getSubmssionpurpose();
			model.put("submssionpurpose", submssionpurpose);
			
			List<Design> designFileType = designService.getDesignFileType();
			model.put("designFileType", designFileType);
		
			List<Design> asBuiltStatuses = designService.getAsBuiltStatuses();
			model.put("asBuiltStatuses", asBuiltStatuses);		


		}catch (Exception e) {
			logger.error("Drawing Repository : " + e.getMessage());
		}
		return model;
	}
	
	@PostMapping(value = "/ajax/getProjectsListForDesignForm")
	public List<Design> getProjectsListForDesignForm1(@RequestBody Design obj) {
		List<Design> objsList = null;
		try {
			objsList = designService.getProjectsListForDesignForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getProjectsListForDesignForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@RequestMapping(value = "/ajax/getComponentsforDesign")
	public List<Design> getComponentsforDesign(@RequestBody Design obj) {
		List<Design> objsList = null;
		try {
			objsList = designService.getComponentsforDesign(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getComponentsforDesign : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getStructureIdsforDesign")
	public List<Design> getStructureIdsforDesign(@RequestBody Design obj) {
		List<Design> objsList = null;
		try {
			objsList = designService.getStructureIdsforDesign(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureIdsforDesign : " + e.getMessage());
		}
		return objsList;
	}	
	
	@PostMapping(value = "/ajax/getStructureTypesforDesign")
	public List<Design> getStructureTypesforDesign(@RequestBody Design obj) {
		List<Design> objsList = null;
		try {
			objsList = designService.getStructureTypesforDesign(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureTypesforDesign : " + e.getMessage());
		}
		return objsList;
	}	
	
	@PostMapping(value = "/ajax/getContractsListForDesignForm")
	public List<Design> getContractsListForDesignForm(@RequestBody Design obj) {
		List<Design> objsList = null;
		try {
			objsList = designService.getContractsListForDesignForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractsListForDesignForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getDesignUploadsList")
	public List<Design> getDesignUploadsList(@RequestBody Design obj) {
		List<Design> objsList = null;
		try {
			objsList = designService.getDesignUploadsList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesignUploadsList : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getHodListForDesignForm")
	public List<Design> getHodList(@RequestBody Design obj,HttpSession session) {
		List<Design> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			
			dataList = designService.getHodList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getHodList : " + e.getMessage());
		}
		return dataList;
	}
	
	@PostMapping(value = "/ajax/getDyHodListForDesignForm")
	public List<Design> getDyHodList(@RequestBody Design obj,HttpSession session) {
		List<Design> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			dataList = designService.getDyHodList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDyHodList : " + e.getMessage());
		}
		return dataList;
	}
	
	@PostMapping(value = "/add-design")
	public String addDesign(@ModelAttribute Design obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		try {			
			//String user_Id = (String) session.getAttribute("USER_ID");
			//String userName = (String) session.getAttribute("USER_NAME");
			//String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
			User uObj = (User) session.getAttribute("user");
			obj.setCreated_by_user_id_fk(uObj.getUserId());
			obj.setUser_id(uObj.getUserId());
			obj.setUser_name(uObj.getUserName());
			obj.setDesignation(uObj.getDesignation());
			//model.setViewName("redirect:/design");
			
			
			obj.setPlanned_start(DateParser.parse(obj.getPlanned_start()));
			obj.setPlanned_finish(DateParser.parse(obj.getPlanned_finish()));
			obj.setConsultant_submission(DateParser.parse(obj.getConsultant_submission()));
			obj.setMrvc_reviewed(DateParser.parse(obj.getMrvc_reviewed()));
			obj.setDivisional_approval(DateParser.parse(obj.getDivisional_approval()));
			obj.setHq_approval(DateParser.parse(obj.getHq_approval()));
			obj.setGfc_released(DateParser.parse(obj.getGfc_released()));
			obj.setAs_built_date(DateParser.parse(obj.getAs_built_date()));
			obj.setSubmitted_to_division(DateParser.parse(obj.getSubmitted_to_division()));
			obj.setSubmitted_to_hq(DateParser.parse(obj.getSubmitted_to_hq()));
			
			obj.setQuery_raised_by_division(DateParser.parse(obj.getQuery_raised_by_division()));
			obj.setQuery_replied_to_division(DateParser.parse(obj.getQuery_replied_to_division()));
			obj.setQuery_raised_by_hq(DateParser.parse(obj.getQuery_raised_by_hq()));
			obj.setQuery_replied_to_hq(DateParser.parse(obj.getQuery_replied_to_hq()));
			obj.setSubmitted_for_crs_sanction(DateParser.parse(obj.getSubmitted_for_crs_sanction()));
			obj.setQuery_raised_for_crs_sanction(DateParser.parse(obj.getQuery_raised_for_crs_sanction()));
			obj.setQuery_replied_for_crs_sanction(DateParser.parse(obj.getQuery_replied_for_crs_sanction()));
			obj.setCrs_sanction_approved(DateParser.parse(obj.getCrs_sanction_approved()));
			
			obj.setRequired_date(DateParser.parse(obj.getRequired_date()));
			obj.setSubmitted_date(DateParser.parse(obj.getSubmitted_date()));
			String designid =  designService.addDesign(obj);
			/*if(!StringUtils.isEmpty(designid)) {
				attributes.addFlashAttribute("success", "Design "+designid+" Added Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Adding Design is failed. Try again.");
			}*/
			return designid;
		}catch (Exception e) {
			//attributes.addFlashAttribute("error","Adding Design is failed. Try again.");
			logger.error("addDesign : " + e.getMessage());
		}
		return null;
	}
	
	@PostMapping(value = "/update-design")
	public String updateDesign(@ModelAttribute Design obj,HttpSession session){
		//ModelAndView model = new ModelAndView();
		try{
			//String user_Id = (String) session.getAttribute("USER_ID");
			//String userName = (String) session.getAttribute("USER_NAME");
			//String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
			
			User uObj = (User) session.getAttribute("user");
			obj.setCreated_by_user_id_fk(uObj.getUserId());
			obj.setUser_id(uObj.getUserId());
			obj.setUser_name(uObj.getUserName());
			obj.setDesignation(uObj.getDesignation());
			//obj.setCreated_by_user_id_fk(user_Id);
			//obj.setUser_id(user_Id);
			//obj.setUser_name(userName);
			//obj.setDesignation(userDesignation);
			//model.setViewName("redirect:/design");
			
			obj.setPlanned_start(DateParser.parse(obj.getPlanned_start()));
			obj.setPlanned_finish(DateParser.parse(obj.getPlanned_finish()));
			obj.setConsultant_submission(DateParser.parse(obj.getConsultant_submission()));
			obj.setMrvc_reviewed(DateParser.parse(obj.getMrvc_reviewed()));
			obj.setDivisional_approval(DateParser.parse(obj.getDivisional_approval()));
			obj.setHq_approval(DateParser.parse(obj.getHq_approval()));
			obj.setGfc_released(DateParser.parse(obj.getGfc_released()));
			obj.setAs_built_date(DateParser.parse(obj.getAs_built_date()));
			obj.setSubmitted_to_division(DateParser.parse(obj.getSubmitted_to_division()));
			obj.setSubmitted_to_hq(DateParser.parse(obj.getSubmitted_to_hq()));
			
			obj.setQuery_raised_by_division(DateParser.parse(obj.getQuery_raised_by_division()));
			obj.setQuery_replied_to_division(DateParser.parse(obj.getQuery_replied_to_division()));
			obj.setQuery_raised_by_hq(DateParser.parse(obj.getQuery_raised_by_hq()));
			obj.setQuery_replied_to_hq(DateParser.parse(obj.getQuery_replied_to_hq()));
			obj.setSubmitted_for_crs_sanction(DateParser.parse(obj.getSubmitted_for_crs_sanction()));
			obj.setQuery_raised_for_crs_sanction(DateParser.parse(obj.getQuery_raised_for_crs_sanction()));
			obj.setQuery_replied_for_crs_sanction(DateParser.parse(obj.getQuery_replied_for_crs_sanction()));
			obj.setCrs_sanction_approved(DateParser.parse(obj.getCrs_sanction_approved()));
			
			obj.setRequired_date(DateParser.parse(obj.getRequired_date()));
			obj.setSubmitted_date(DateParser.parse(obj.getSubmitted_date()));
			
			String designid =  designService.updateDesign(obj);
			/*if(!StringUtils.isEmpty(designid)) {
				attributes.addFlashAttribute("success", "Design "+designid+" Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Design is failed. Try again.");
			}*/
			return designid;
		}catch (Exception e) {
			e.printStackTrace();
			//attributes.addFlashAttribute("error","Updating Design is failed. Try again.");
			logger.error("updateDesign : " + e.getMessage());
		}
		return null;
	}
	

	@PostMapping(value = "/export-design")
	public ResponseEntity<?> exportDesign(HttpServletRequest request, HttpServletResponse response,HttpSession session,@RequestBody Design design,RedirectAttributes attributes){
		//ModelAndView view = new ModelAndView(PageConstants.designGrid);
		List<Design> dataList = new ArrayList<Design>();
		List<Design> dataRevisionsList = new ArrayList<Design>();
		String userId = null;String userName = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			userId = uObj.getUserId();
			userName = uObj.getUserName();
			
			
			//view.setViewName("redirect:/design");
			dataList = designService.getDesigns(design); 
			dataRevisionsList = designService.getDesignRevisions(design);  
			if(dataList != null && dataList.size() > 0){
	            XSSFWorkbook  workBook = new XSSFWorkbook ();
	            XSSFSheet sheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Design & Drawing"));
	            XSSFSheet sheet1 = workBook.createSheet(WorkbookUtil.createSafeSheetName("Drawing Revisions"));
	            
		        workBook.setSheetOrder(sheet.getSheetName(), 0);
		        workBook.setSheetOrder(sheet1.getSheetName(), 1);
		        
		        byte[] blueRGB = new byte[]{(byte)0, (byte)176, (byte)240};
		        byte[] yellowRGB = new byte[]{(byte)255, (byte)192, (byte)0};
		        byte[] greenRGB = new byte[]{(byte)146, (byte)208, (byte)80};
		        byte[] redRGB = new byte[]{(byte)255, (byte)0, (byte)0};
		        byte[] whiteRGB = new byte[]{(byte)255, (byte)255, (byte)255};
		        
		        boolean isWrapText = true;boolean isBoldText = true;boolean isItalicText = false; int fontSize = 9;String fontName = "Times New Roman";
		        CellStyle blueStyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        CellStyle yellowStyle = cellFormating(workBook,yellowRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        CellStyle greenStyle = cellFormating(workBook,greenRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        CellStyle redStyle = cellFormating(workBook,redRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        CellStyle whiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,false,isItalicText,fontSize,fontName);
		        
		        CellStyle indexWhiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        
		        isWrapText = true;isBoldText = false;isItalicText = false; fontSize = 9;fontName = "Times New Roman";
		        CellStyle sectionStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        
		        XSSFRow headingRowFirst = sheet.createRow(0);

	        	Cell cell1 = headingRowFirst.createCell(0);
		        cell1.setCellStyle(whiteStyle);
				cell1.setCellValue("Unique ID of the Work");
				
	        	Cell cell3 = headingRowFirst.createCell(1);
		        cell3.setCellStyle(whiteStyle);
				cell3.setCellValue("");
				
	        	Cell cell4 = headingRowFirst.createCell(2);
		        cell4.setCellStyle(whiteStyle);
				cell4.setCellValue("Department to which the design or drawing belongs to");
				
	        	Cell cell5 = headingRowFirst.createCell(3);
		        cell5.setCellStyle(whiteStyle);
				cell5.setCellValue("Dropdown from Reference Table");	
				
	        	Cell cell6 = headingRowFirst.createCell(4);
		        cell6.setCellStyle(whiteStyle);
				cell6.setCellValue("Dropdown from Reference Table");
				
	        	Cell cell7 = headingRowFirst.createCell(5);
		        cell7.setCellStyle(whiteStyle);
				cell7.setCellValue("");
				
	        	Cell cell8 = headingRowFirst.createCell(6);
		        cell8.setCellStyle(whiteStyle);
				cell8.setCellValue("");
				
	        	Cell cell81 = headingRowFirst.createCell(7);
		        cell81.setCellStyle(whiteStyle);
				cell81.setCellValue("");				
				
	        	Cell cell9 = headingRowFirst.createCell(8);
		        cell9.setCellStyle(whiteStyle);
				cell9.setCellValue("Drawing prepared By from reference table");
				
	        	Cell cell10 = headingRowFirst.createCell(9);
		        cell10.setCellStyle(whiteStyle);
				cell10.setCellValue("");
				
	        	Cell cell11 = headingRowFirst.createCell(10);
		        cell11.setCellStyle(whiteStyle);
				cell11.setCellValue("Contract ID of the consultant preparing the drawing (If prepared by MRVC or Contractor, leave blank)");
				
	        	Cell cell12 = headingRowFirst.createCell(11);
		        cell12.setCellStyle(whiteStyle);
				cell12.setCellValue("Proof Consultant Contract ID checking the design/ drg.(Leave blank if no proof checking) ");
				
	        	Cell cell13 = headingRowFirst.createCell(12);
		        cell13.setCellStyle(whiteStyle);
				cell13.setCellValue("Type of Drawing");
				
	        	Cell cell14 = headingRowFirst.createCell(13);
		        cell14.setCellStyle(whiteStyle);
				cell14.setCellValue("");
				
	        	Cell cell15 = headingRowFirst.createCell(14);
		        cell15.setCellStyle(whiteStyle);
				cell15.setCellValue("");
				
	        	Cell cell16 = headingRowFirst.createCell(15);
		        cell16.setCellStyle(whiteStyle);
				cell16.setCellValue("");
				
	        	Cell cell17 = headingRowFirst.createCell(16);
		        cell17.setCellStyle(whiteStyle);
				cell17.setCellValue("Title of the drawing");
				
	        	Cell cell18 = headingRowFirst.createCell(17);
		        cell18.setCellStyle(whiteStyle);
				cell18.setCellValue("Drawing No assigned by the Agency");
				
	        	Cell cell19 = headingRowFirst.createCell(18);
		        cell19.setCellStyle(whiteStyle);
				cell19.setCellValue("Drawing No assigned by MRVC");
				
	        	Cell cell20 = headingRowFirst.createCell(19);
	        	cell20.setCellStyle(whiteStyle);
	        	cell20.setCellValue("Drawing No assigned by the Division");
				
	        	Cell cell21 = headingRowFirst.createCell(20);
		        cell21.setCellStyle(whiteStyle);
				cell21.setCellValue("Drawing No assigned by the Head Quarters");
				
	        	Cell cell22 = headingRowFirst.createCell(21);
		        cell22.setCellStyle(whiteStyle);
				cell22.setCellValue("");
				
	        	Cell cell23 = headingRowFirst.createCell(22);
		        cell23.setCellStyle(whiteStyle);
				cell23.setCellValue("");
				
				
	        	Cell cell26 = headingRowFirst.createCell(23);
		        cell26.setCellStyle(whiteStyle);
				cell26.setCellValue("");
				
	        	Cell cell27 = headingRowFirst.createCell(24);
		        cell27.setCellStyle(whiteStyle);
				cell27.setCellValue("Any additional remarks");
				
								
				
		        
	            XSSFRow headingRow = sheet.createRow(1);
	        	//String headerString = "PMIS Drawing No,Work ID,Approving Railway,Department,HOD,Dy HOD,Structure,Structure ID,Component,Prepared By,Contract ID,Consultant ,Proof Consultant ID,Drawing Type,Approval Authority,Required Date,GFC Release Date,Drawing Title,Agency Drawing No,MRVC Drawing No,Division Drawing No,HQ Drawing No,Stage,Submtted by,Submitted to,Purpose of Submission/Remarks,Submitted Date,Remarks";
	        	String headerString = "PMIS Drawing No,Work ID, Contract ID, Approving Railway, Structure Type, Structure, Component, Consultant, Proof Consultant, 3PV Consultant, Prepared By, Drawing Type, Approval Authority, Required Date, GFC Approval Date, Drawing Title, Agency Drawing No., MRVC Drawing No., Division Drawing No., HQ Drawing No., Stage, Submitted By, Submitted To, Purpose of Submission/Remarks, Submitted Date, Remarks";
	            String[] firstHeaderStringArr = headerString.split("\\,");
	            
	            for (int i = 0; i < firstHeaderStringArr.length; i++) {		        	
		        	Cell cell = headingRow.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(firstHeaderStringArr[i]);
				}
	            
	            short rowNo = 2;
	            for (Design obj : dataList) {
	                XSSFRow row = sheet.createRow(rowNo);
	                int c = 0;
	                Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDesign_seq_id());
					
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getWork_id_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_id_fk());							
				    
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getApproving_railway());
			
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getStructure_type_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getStructure_id_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getComponent());					
				
			
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					if(!StringUtils.isEmpty(obj.getConsultant_contract_id_fk())) {
					cell.setCellValue(obj.getConsultant_contract_id_fk()+" - "+obj.getConsult_contarct());
					}
					else
					{
						cell.setCellValue("");
					}
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					if(!StringUtils.isEmpty(obj.getProof_consultant_contract_id_fk())) {
					cell.setCellValue(obj.getProof_consultant_contract_id_fk()+" - "+obj.getProof_consult_contarct());
					}
					else
					{
						cell.setCellValue("");
					}
					
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getThreepvc());	
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getPrepared_by_id_fk());					

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDrawing_type_fk());

					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getApproval_authority_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRequired_date());
					
					
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getGfc_released());
					
					
					
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDrawing_title());					
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContractor_drawing_no());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getMrvc_drawing_no());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDivision_drawing_no());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getHq_drawing_no());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getStage_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSubmitted_by());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSubmitted_to());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSubmission_purpose());
	                
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSubmitted_date());
	                
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRemarks());
	                
	                
	                rowNo++;
	            }
	            
	            
	            XSSFRow headingRow1 = sheet1.createRow(0);
	        	String headerString1 = "PMIS Drawing No,Revision No.,Drawing No.,Correspondence Letter No,Revision Date,Revision Status,Remarks,Upload File";
	            String[] firstHeaderStringArr1 = headerString1.split("\\,");
	            
	            for (int i = 0; i < firstHeaderStringArr1.length; i++) {		        	
		        	Cell cell = headingRow1.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(firstHeaderStringArr1[i]);
				}
	            
	            short rowNo1 = 1;
	            for (Design obj : dataRevisionsList) {
	                XSSFRow row = sheet1.createRow(rowNo1);
	                int c = 0;
	                
	                Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDesign_seq_id());	                
	            
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRevision());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDrawing_no());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCorrespondence_letter_no());					
				    
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRevision_date());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRevision_status());
				
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRemarks());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getUpload_file());					

	                rowNo1++;
	            }	            
	            
	            
	            for(int columnIndex = 0; columnIndex < firstHeaderStringArr.length; columnIndex++) {
	        		sheet.setColumnWidth(columnIndex, 25 * 200);
				}
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
                Date date = new Date();
                String fileName = "Design_"+dateFormat.format(date);
                
	            try{
	                /*FileOutputStream fos = new FileOutputStream(fileDirectory +fileName+".xls");
	                workBook.write(fos);
	                fos.flush();*/
	            	
	               response.setContentType("application/.csv");
	 			   response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	 			   response.setContentType("application/vnd.ms-excel");
	 			   // add response header
	 			   response.addHeader("Content-Disposition", "attachment; filename=" + fileName+".xlsx");
	 			   
	 			    //copies all bytes from a file to an output stream
	 			   workBook.write(response.getOutputStream()); // Write workbook to response.
		           workBook.close();
	 			    //flushes output stream
	 			    response.getOutputStream().flush();
	            	
	                return ResponseEntity.ok(Map.of("success",dataExportSucess));
	                //attributes.addFlashAttribute();
	            	//response.setContentType("application/vnd.ms-excel");
	            	//response.setHeader("Content-Disposition", "attachment; filename=filename.xls");
	            	//XSSFWorkbook  workbook = new XSSFWorkbook ();
	            	// ...
	            	// Now populate workbook the usual way.
	            	// ...
	            	//workbook.write(response.getOutputStream()); // Write workbook to response.
	            	//workbook.close();
	            }catch(FileNotFoundException e){
	                //e.printStackTrace();
	            	 return ResponseEntity.ok(Map.of("error",dataExportInvalid));
	                //attributes.addFlashAttribute("error",dataExportInvalid);
	            }catch(IOException e){
	                //e.printStackTrace();
	            	return ResponseEntity.ok(Map.of("error",dataExportError));
	                //attributes.addFlashAttribute("error",dataExportError);
	            }
         }else{
        	 return ResponseEntity.ok(Map.of("error",dataExportNoData));
        	 //attributes.addFlashAttribute("error",dataExportNoData);
         }
		}catch(Exception e){	
			e.printStackTrace();
			logger.error("exportDesign : : User Id - "+userId+" - User Name - "+userName+" - "+e.getMessage());
			//attributes.addFlashAttribute("error", commonError);
			return ResponseEntity.ok(Map.of("error",commonError));
		}
		//return view;
	}
	

	private CellStyle cellFormating(XSSFWorkbook workBook,byte[] rgb,HorizontalAlignment hAllign, VerticalAlignment vAllign, boolean isWrapText,boolean isBoldText,boolean isItalicText,int fontSize,String fontName) {
		CellStyle style = workBook.createCellStyle();
		//Setting Background color  
		//style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		if (style instanceof XSSFCellStyle) {
		   XSSFCellStyle xssfcellcolorstyle = (XSSFCellStyle)style;
		   xssfcellcolorstyle.setFillForegroundColor(new XSSFColor(rgb, null));
		}
		//style.setFillPattern(FillPatternType.ALT_BARS);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setAlignment(hAllign);
		style.setVerticalAlignment(vAllign);
		style.setWrapText(isWrapText);
		
		Font font = workBook.createFont();
        //font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setFontHeightInPoints((short)fontSize);  
        font.setFontName(fontName);  //"Times New Roman"
        
        font.setItalic(isItalicText); 
        font.setBold(isBoldText);
        // Applying font to the style  
        style.setFont(font); 
        
        return style;
	}
	
	@PostMapping(value = "/upload-designs")
	public void uploadDesigns(@ModelAttribute Design design,RedirectAttributes attributes,HttpSession session){
		//ModelAndView model = new ModelAndView();
		String msg = "";String userId = null;
		try {
			User uObj = (User) session.getAttribute("user");
			
			 userId = uObj.getUserId();
			String userName = uObj.getUserName();
			String userDesignation = uObj.getDesignation();
			
			design.setCreated_by_user_id_fk(userId);
			design.setUser_id(userId);
			design.setUser_name(userName);
			design.setDesignation(userDesignation);
			//model.setViewName("redirect:/design");
			
			if(!StringUtils.isEmpty(design.getDesignFile())){
				MultipartFile multipartFile = design.getDesignFile();
				// Creates a workbook object from the uploaded excelfile
				if (multipartFile.getSize() > 0){					
					XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
					// Creates a worksheet object representing the first sheet
					int sheetsCount = workbook.getNumberOfSheets();
					if(sheetsCount > 0) {
						XSSFSheet designsDrawingsSheet = workbook.getSheetAt(0);
						//System.out.println(uploadFilesSheet.getSheetName());
						//header row
						XSSFRow headerRow = designsDrawingsSheet.getRow(1);
						//checking given file format
						if(headerRow != null)
						{
						    List<String> fileFormat = FileFormatModel.getDesignFileFormat();
							
							  if(headerRow.getCell(0).getStringCellValue().trim().equals("PMIS Drawing No")) {
							 
								int noOfColumns = headerRow.getLastCellNum();
								if(noOfColumns == fileFormat.size()){
									for (int i = 0; i < fileFormat.size();i++) {
					                	//System.out.println(headerRow.getCell(i).getStringCellValue().trim());
					                	//if(!fileFormat.get(i).trim().equals(headerRow.getCell(i).getStringCellValue().trim())){
										String columnName = headerRow.getCell(i).getStringCellValue().trim();
										if(!columnName.equals(fileFormat.get(i).trim()) && !columnName.contains(fileFormat.get(i).trim())){
					                		attributes.addFlashAttribute("error",uploadformatError);
					                		msg = uploadformatError;
					                		design.setUploaded_by_user_id_fk(userId);
					                		design.setStatus("Fail");
					                		design.setRemarks(msg);
											boolean flag = designService.saveDesignDataUploadFile(design);
					                		return;
					                	}
									}
								}else{
									attributes.addFlashAttribute("error",uploadformatError);
									msg = uploadformatError;
			                		design.setUploaded_by_user_id_fk(userId);
			                		design.setStatus("Fail");
			                		design.setRemarks(msg);
									boolean flag = designService.saveDesignDataUploadFile(design);
			                		return;
								}
							}
							else
							{
								List<String> fileFormatDesign = FileFormatModel.getDesignFirstFileFormat();
								int noOfColumns = headerRow.getLastCellNum();
								if(noOfColumns == fileFormatDesign.size()){
									for (int i = 0; i < fileFormatDesign.size();i++) {
					                	//System.out.println(headerRow.getCell(i).getStringCellValue().trim());
					                	//if(!fileFormat.get(i).trim().equals(headerRow.getCell(i).getStringCellValue().trim())){
										String columnName = headerRow.getCell(i).getStringCellValue().trim();
										if(!columnName.equals(fileFormatDesign.get(i).trim()) && !columnName.contains(fileFormatDesign.get(i).trim())){
					                		attributes.addFlashAttribute("error",uploadformatError);
					                		msg = uploadformatError;
					                		design.setUploaded_by_user_id_fk(userId);
					                		design.setStatus("Fail");
					                		design.setRemarks(msg);
											boolean flag = designService.saveDesignDataUploadFile(design);
					                		return;
					                	}
									}
								}else{
									attributes.addFlashAttribute("error",uploadformatError);
									msg = uploadformatError;
			                		design.setUploaded_by_user_id_fk(userId);
			                		design.setStatus("Fail");
			                		design.setRemarks(msg);
									boolean flag = designService.saveDesignDataUploadFile(design);
			                		return;
								}								
								
							}
						}else{
							attributes.addFlashAttribute("error",uploadformatError);
	                		return ;
						}
						int count =0;
						if(headerRow.getCell(0).getStringCellValue().trim().equals("PMIS Drawing No"))
						{
							count = uploadDesigns(design,userId,userName);
						}
						else
						{
							count = uploadDesignsFirst(design,userId,userName);
						}
						if(count > 0) {
							
							if(headerRow.getCell(0).getStringCellValue().trim().equals("PMIS Drawing No"))
							{
								attributes.addFlashAttribute("success", count + " Designs updated successfully.");	

							}
							else
							{
								attributes.addFlashAttribute("success", count + " Designs added successfully.");	
							}
							
							msg = count + " Designs added successfully.";
							
							FormHistory formHistory = new FormHistory();
							formHistory.setCreated_by_user_id_fk(design.getCreated_by_user_id_fk());
							formHistory.setUser(design.getDesignation()+" - "+design.getUser_name());
							formHistory.setModule_name_fk("Design");
							formHistory.setForm_name("Upload Design");
							formHistory.setForm_action_type("Upload");
							formHistory.setForm_details( msg);
							formHistory.setWork(design.getWork_id_fk());
							//formHistory.setContract(obj.getContract_id_fk());
							
							boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
							/********************************************************************************/
						}else {
							attributes.addFlashAttribute("success"," No records found.");	
							msg = " No records found.";
						}
						design.setUploaded_by_user_id_fk(userId);
						design.setStatus("Success");
						design.setRemarks(msg);
						boolean flag = designService.saveDesignDataUploadFile(design);
					}
					workbook.close();
				}
			} else {
				attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");

				msg = "No file exists";
				design.setUploaded_by_user_id_fk(userId);
				design.setStatus("Fail");
				design.setRemarks(msg);
				boolean flag = designService.saveDesignDataUploadFile(design);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
			logger.fatal("updateDataDate() : "+e.getMessage());
			
			msg = "Something went wrong. Please try after some time";
			design.setUploaded_by_user_id_fk(userId);
			design.setStatus("Fail");
			design.setRemarks(msg);
			try {
				boolean flag = designService.saveDesignDataUploadFile(design);
			} catch (Exception e1) {
				attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
				logger.fatal("saveDesignDataUploadFile() : "+e.getMessage());
			}
		}
		return;
	}
	
	/**
	 * This method uploadDesigns() is called when user upload the file
	 * 
	 * @param obj is object for the model class User.
	 * @param userId is type of String it store the userId
	 * @param userName is type of String it store the userName
	 * @param workbook is type of XSSWorkbook variable it takes the workbook as input.
	 * @return type of this method is count.
	 * @throws IOException will raise an exception when abnormal termination occur.
	 */
	
	public int uploadDesigns(Design obj, String userId, String userName) throws Exception {
		Design design = null;
		List<Design> designsList = new ArrayList<Design>();
		
		Writer w = null;
		int count = 0;
		try {	
			MultipartFile excelfile = obj.getDesignFile();
			if (!StringUtils.isEmpty(excelfile) && excelfile.getSize() > 0 ){
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				int sheetsCount = workbook.getNumberOfSheets();
				if(sheetsCount > 0) {
					XSSFSheet designsDrawingsSheet = workbook.getSheetAt(0);
					XSSFSheet designsRevisionSheet = workbook.getSheetAt(1);
						
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					for(int i = 2; i <= designsDrawingsSheet.getLastRowNum();i++){
						int v = designsDrawingsSheet.getLastRowNum();
						XSSFRow row = designsDrawingsSheet.getRow(i);

						design = new Design();
						String val = null;
						if(!StringUtils.isEmpty(row)) {
						
							val = formatter.formatCellValue(row.getCell(0)).trim();
							if(!StringUtils.isEmpty(val)) { design.setDesign_seq_id(val);}
							
							val = formatter.formatCellValue(row.getCell(1)).trim();
							if(!StringUtils.isEmpty(val)) { design.setWork_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(2)).trim();
							if(!StringUtils.isEmpty(val)) { design.setContract_id_fk(val);}									
							
							
							val = formatter.formatCellValue(row.getCell(3)).trim();
							if(!StringUtils.isEmpty(val)) { design.setApproving_railway(val);}
							
							val = formatter.formatCellValue(row.getCell(4)).trim();
							if(!StringUtils.isEmpty(val)) { design.setStructure_type_fk(val);}	
							
							val = formatter.formatCellValue(row.getCell(5)).trim();
							if(!StringUtils.isEmpty(val)) { design.setStructure_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(6)).trim();
							if(!StringUtils.isEmpty(val)) { design.setComponent(val);}	
							
							val = formatter.formatCellValue(row.getCell(7)).trim();
							if(!StringUtils.isEmpty(val)) { design.setConsultant_contract_id_fk(val);}										
							
							val = formatter.formatCellValue(row.getCell(8)).trim();
							if(!StringUtils.isEmpty(val)) { design.setProof_consultant_contract_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(9)).trim();
							if(!StringUtils.isEmpty(val)) { design.setThreepvc(val);}	
							
							val = formatter.formatCellValue(row.getCell(10)).trim();
							if(!StringUtils.isEmpty(val)) { design.setPrepared_by_id_fk(val);}								
							
							
							val = formatter.formatCellValue(row.getCell(11)).trim();
							if(!StringUtils.isEmpty(val)) { design.setDrawing_type_fk(val);}
							

							val = formatter.formatCellValue(row.getCell(12)).trim();
							if(!StringUtils.isEmpty(val)) { design.setApproval_authority_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(13)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									LocalDate receivedDate = LocalDate.parse(val, DateTimeFormatter.ofPattern("M/dd/yy"));
									val = receivedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); 
								}
								design.setRequired_date(val);}
							
							val = formatter.formatCellValue(row.getCell(14)).trim();
							if(!StringUtils.isEmpty(val)) { design.setGfc_released(val);}	
														
							
							val = formatter.formatCellValue(row.getCell(15)).trim();
							if(!StringUtils.isEmpty(val)) { design.setDrawing_title(val);}	
														
							
							val = formatter.formatCellValue(row.getCell(16)).trim();
							if(!StringUtils.isEmpty(val)) { design.setContractor_drawing_no(val);}
							
							val = formatter.formatCellValue(row.getCell(17)).trim();
							if(!StringUtils.isEmpty(val)) { design.setMrvc_drawing_no(val);}
							
							val = formatter.formatCellValue(row.getCell(18)).trim();
							if(!StringUtils.isEmpty(val)) { design.setDivision_drawing_no(val);}								
							
							val = formatter.formatCellValue(row.getCell(19)).trim();
							if(!StringUtils.isEmpty(val)) { design.setHq_drawing_no(val);}											
							
													
							
							val = formatter.formatCellValue(row.getCell(20)).trim();
							if(!StringUtils.isEmpty(val)) { design.setStage_fk(val);}										
						
							val = formatter.formatCellValue(row.getCell(21)).trim();
							if(!StringUtils.isEmpty(val)) { design.setSubmitted_by(val);}
							
							val = formatter.formatCellValue(row.getCell(22)).trim();
							if(!StringUtils.isEmpty(val)) { design.setSubmitted_to(val);}
							
							val = formatter.formatCellValue(row.getCell(23)).trim();
							if(!StringUtils.isEmpty(val)) { design.setSubmission_purpose(val);}
							
							val = formatter.formatCellValue(row.getCell(24)).trim();
							if(!StringUtils.isEmpty(val)) { 
								design.setSubmitted_date(val);}
							
							val = formatter.formatCellValue(row.getCell(25)).trim();
							if(!StringUtils.isEmpty(val)) { 
								design.setGfc_released(val);}
							
							val = formatter.formatCellValue(row.getCell(26)).trim();
							if(!StringUtils.isEmpty(val)) { design.setRemarks(val);}
							
							design.setGfc_released(DateParser.parse(design.getGfc_released()));
							design.setSubmitted_date(DateParser.parse(design.getSubmitted_date()));
							design.setRequired_date(DateParser.parse(design.getRequired_date()));
							designsList.add(design);
							
						}
						
					}
						Design designRevision = null;
												
						List<Design> pObjList = new ArrayList<Design>();
						
						for(int i1 = 1; i1 <= designsRevisionSheet.getLastRowNum();i1++)
						{
							int v1 = designsRevisionSheet.getLastRowNum();
							XSSFRow row1 = designsRevisionSheet.getRow(i1);

							designRevision = new Design();
							String val1 = null;
							if(!StringUtils.isEmpty(row1)) {
								
								val1 = formatter.formatCellValue(row1.getCell(0)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setMrvc_drawing_no(val1);}								
							
								val1 = formatter.formatCellValue(row1.getCell(1)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setRevision(val1);}
								
								val1 = formatter.formatCellValue(row1.getCell(2)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setDrawing_no(val1);}
								
								val1 = formatter.formatCellValue(row1.getCell(3)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setCorrespondence_letter_no(val1);}
								
								val1 = formatter.formatCellValue(row1.getCell(4)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setRevision_date(val1);}
								
								val1 = formatter.formatCellValue(row1.getCell(5)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setRevision_status(val1);}
								
								
								val1 = formatter.formatCellValue(row1.getCell(6)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setRemarks(val1);}		
								
								val1 = formatter.formatCellValue(row1.getCell(7)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setUpload_file(val1);}
								
								pObjList.add(designRevision);

								
							}
						}
						
						//if(!StringUtils.isEmpty(design.getMrvc_drawing_no())) 
						//{
	
							design.setDesignRevisions(pObjList);
						//}
						
						boolean flag = design.checkNullOrEmpty();
						
						if(!flag) {
							designsList.add(design);
						}
					
					
					if(!designsList.isEmpty() && designsList != null){
						count  = designService.uploadDesignsNew(designsList);
					}
				}
				workbook.close();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uploadDesigns() : "+e.getMessage());
			throw new Exception(e);	
		}finally{
		    try{
		        if ( w != null)
		        	w.close( );
		    }catch ( IOException e){
		    	e.printStackTrace();
		    	logger.error("uploadDesigns() : "+e.getMessage());
		    	throw new Exception(e);
		    }
		}
		
		return count;
	}
	
	public int uploadDesignsFirst(Design obj, String userId, String userName) throws Exception {
		Design design = null;
		List<Design> designsList = new ArrayList<Design>();
		
		Writer w = null;
		int count = 0;
		try {	
			MultipartFile excelfile = obj.getDesignFile();
			// Creates a workbook object from the uploaded excelfile
			if (!StringUtils.isEmpty(excelfile) && excelfile.getSize() > 0 ){
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				int sheetsCount = workbook.getNumberOfSheets();
				if(sheetsCount > 0) {
					XSSFSheet designsDrawingsSheet = workbook.getSheetAt(0);
					XSSFSheet designsRevisionSheet = workbook.getSheetAt(1);
						
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					for(int i = 2; i < designsDrawingsSheet.getLastRowNum();i++){
						int v = designsDrawingsSheet.getLastRowNum();
						XSSFRow row = designsDrawingsSheet.getRow(i);
						design = new Design();
						String val = null;
						if(!StringUtils.isEmpty(row)) {								
							val = formatter.formatCellValue(row.getCell(0)).trim();
							if(!StringUtils.isEmpty(val)) { design.setWork_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(1)).trim();
							if(!StringUtils.isEmpty(val)) { design.setContract_id_fk(val);}									
							
							
							val = formatter.formatCellValue(row.getCell(2)).trim();
							if(!StringUtils.isEmpty(val)) { design.setApproving_railway(val);}
							
							val = formatter.formatCellValue(row.getCell(3)).trim();
							if(!StringUtils.isEmpty(val)) { design.setStructure_type_fk(val);}	
							
							val = formatter.formatCellValue(row.getCell(4)).trim();
							if(!StringUtils.isEmpty(val)) { design.setStructure_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(5)).trim();
							if(!StringUtils.isEmpty(val)) { design.setComponent(val);}	
							
							val = formatter.formatCellValue(row.getCell(6)).trim();
							if(!StringUtils.isEmpty(val)) { design.setConsultant_contract_id_fk(val);}										
							
							val = formatter.formatCellValue(row.getCell(7)).trim();
							if(!StringUtils.isEmpty(val)) { design.setProof_consultant_contract_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(8)).trim();
							if(!StringUtils.isEmpty(val)) { design.setThreepvc(val);}	
							
							val = formatter.formatCellValue(row.getCell(9)).trim();
							if(!StringUtils.isEmpty(val)) { design.setPrepared_by_id_fk(val);}								
							
							
							val = formatter.formatCellValue(row.getCell(10)).trim();
							if(!StringUtils.isEmpty(val)) { design.setDrawing_type_fk(val);}								
							
							val = formatter.formatCellValue(row.getCell(11)).trim();
							if(!StringUtils.isEmpty(val)) { design.setApproval_authority_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(12)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									LocalDate receivedDate = LocalDate.parse(val, DateTimeFormatter.ofPattern("M/dd/yy"));
									val = receivedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); 
								}
								design.setRequired_date(val);}
							
							val = formatter.formatCellValue(row.getCell(13)).trim();
							if(!StringUtils.isEmpty(val)) { design.setGfc_released(val);}	
							
							val = formatter.formatCellValue(row.getCell(14)).trim();
							if(!StringUtils.isEmpty(val)) { design.setDrawing_title(val);}								
							
							val = formatter.formatCellValue(row.getCell(15)).trim();
							if(!StringUtils.isEmpty(val)) { design.setContractor_drawing_no(val);}
							
							val = formatter.formatCellValue(row.getCell(16)).trim();
							if(!StringUtils.isEmpty(val)) { design.setMrvc_drawing_no(val);}
							
							val = formatter.formatCellValue(row.getCell(17)).trim();
							if(!StringUtils.isEmpty(val)) { design.setDivision_drawing_no(val);}								
							
							val = formatter.formatCellValue(row.getCell(18)).trim();
							if(!StringUtils.isEmpty(val)) { design.setHq_drawing_no(val);}											
							
													
							
							val = formatter.formatCellValue(row.getCell(19)).trim();
							if(!StringUtils.isEmpty(val)) { design.setStage_fk(val);}										
						
							val = formatter.formatCellValue(row.getCell(20)).trim();
							if(!StringUtils.isEmpty(val)) { design.setSubmitted_by(val);}
							
							val = formatter.formatCellValue(row.getCell(21)).trim();
							if(!StringUtils.isEmpty(val)) { design.setSubmitted_to(val);}
							
							val = formatter.formatCellValue(row.getCell(22)).trim();
							if(!StringUtils.isEmpty(val)) { design.setSubmission_purpose(val);}
							
							val = formatter.formatCellValue(row.getCell(23)).trim();
							if(!StringUtils.isEmpty(val)) { 
								design.setSubmitted_date(val);}
							
							val = formatter.formatCellValue(row.getCell(24)).trim();
							if(!StringUtils.isEmpty(val)) { 
								design.setGfc_released(val);}
							
							val = formatter.formatCellValue(row.getCell(25)).trim();
							if(!StringUtils.isEmpty(val)) { design.setRemarks(val);}
							
							design.setGfc_released(DateParser.parse(design.getGfc_released()));
							design.setSubmitted_date(DateParser.parse(design.getSubmitted_date()));
							design.setRequired_date(DateParser.parse(design.getRequired_date()));
							
						}
					}
						Design designRevision = null;
						
						List<Design> pObjList = new ArrayList<Design>();
						
						for(int i1 = 1; i1 <= designsRevisionSheet.getLastRowNum();i1++)
						{
							int v1 = designsRevisionSheet.getLastRowNum();
							XSSFRow row1 = designsRevisionSheet.getRow(i1);

							designRevision = new Design();
							String val1 = null;
							if(!StringUtils.isEmpty(row1)) {
								
								val1 = formatter.formatCellValue(row1.getCell(0)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setMrvc_drawing_no(val1);}								
							
								val1 = formatter.formatCellValue(row1.getCell(1)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setRevision(val1);}
								
								val1 = formatter.formatCellValue(row1.getCell(2)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setDrawing_no(val1);}
								
								val1 = formatter.formatCellValue(row1.getCell(3)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setCorrespondence_letter_no(val1);}
								
								val1 = formatter.formatCellValue(row1.getCell(4)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setRevision_date(val1);}
								
								val1 = formatter.formatCellValue(row1.getCell(5)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setRevision_status(val1);}
								
								
								val1 = formatter.formatCellValue(row1.getCell(6)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setRemarks(val1);}		
								
								val1 = formatter.formatCellValue(row1.getCell(7)).trim();
								if(!StringUtils.isEmpty(val1)) { designRevision.setUpload_file(val1);}
								
								pObjList.add(designRevision);

								
							}
						}
						
						//if(!StringUtils.isEmpty(design.getMrvc_drawing_no())) 
						//{
	
							design.setDesignRevisions(pObjList);
						
						boolean flag = design.checkNullOrEmpty();
						
						if(!flag) {
							designsList.add(design);
						}
					
					
					if(!designsList.isEmpty() && designsList != null){
						count  = designService.uploadDesignsNew(designsList);
					}
				}
				workbook.close();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uploadDesigns() : "+e.getMessage());
			throw new Exception(e);	
		}finally{
		    try{
		        if ( w != null)
		        	w.close( );
		    }catch ( IOException e){
		    	e.printStackTrace();
		    	logger.error("uploadDesigns() : "+e.getMessage());
		    	throw new Exception(e);
		    }
		}
		
		return count;
	}	
}
