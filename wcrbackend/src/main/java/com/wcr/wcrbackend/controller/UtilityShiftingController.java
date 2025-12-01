package com.wcr.wcrbackend.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.text.DateFormat;
import java.text.NumberFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
import org.apache.poi.ss.formula.WorkbookEvaluatorProvider;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcr.wcrbackend.DTO.FileFormatModel;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.DTO.UtilityShiftingPaginationObject;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.IFormsHistoryDao;
import com.wcr.wcrbackend.service.IUserService;
import com.wcr.wcrbackend.service.IUtilityShiftingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/utility-shifting")
public class UtilityShiftingController {
	
	Logger logger = Logger.getLogger(UtilityShiftingController.class);
	@Autowired
	private IUtilityShiftingService utilityShiftingService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IFormsHistoryDao formsHistoryDao;
	
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
	
	@PostMapping(value = "/ajax/getImpactedContractsListForUSForm")
	public List<UtilityShifting> getImpactedContractsListForUSForm(HttpSession session,@RequestBody UtilityShifting obj) throws Exception{
		List<UtilityShifting> objsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			objsList = utilityShiftingService.getImpactedContractsListForUtilityShifting(obj);
			


		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getImpactedContractsListForUtilityShifting : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getLocationListFilter")
	public List<UtilityShifting> getLocationListFilter(HttpSession session,@RequestBody UtilityShifting obj) {
		List<UtilityShifting> objList = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = utilityShiftingService.getLocationListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getWorksListFilterInUtilityShifting : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getUtilityCategoryListFilter")
	public List<UtilityShifting> getUtilityCategoryListFilter(HttpSession session,@RequestBody UtilityShifting obj) {
		List<UtilityShifting> objList = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			objList = utilityShiftingService.getUtilityCategoryListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getWorksListFilterInUtilityShifting : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getUtilityTypeListFilter")
	public List<UtilityShifting> getUtilityTypeListFilter(HttpSession session,@RequestBody UtilityShifting obj) {
		List<UtilityShifting> objList = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = utilityShiftingService.getUtilityTypeListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getWorksListFilterInUtilityShifting : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getStatusListFilter")
	public List<UtilityShifting> getStatusListFilter(HttpSession session,@RequestBody UtilityShifting obj) {
		List<UtilityShifting> objList = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			objList = utilityShiftingService.getUtilityStatusListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getWorksListFilterInUtilityShifting : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getHodListForUtilityShifting")
	public List<UtilityShifting> getHodListForUtilityShifting(@RequestBody UtilityShifting obj,HttpSession session) {
		List<UtilityShifting> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			dataList = utilityShiftingService.getHodListForUtilityShifting(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getHodListForUtilityShifting : " + e.getMessage());
		}
		return dataList;
	}
	
	@PostMapping(value = "/ajax/getReqStageListForUSForm")
	public List<UtilityShifting> getReqStageListForUSForm(@RequestBody UtilityShifting obj,HttpSession session) {
		List<UtilityShifting> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			dataList = utilityShiftingService.getReqStageList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getReqStageListForUSForm : " + e.getMessage());
		}
		return dataList;
	}
	
	@PostMapping(value = "/ajax/getImpactedElementListForUSForm")
	public List<UtilityShifting> getImpactedElementListForUSForm(@RequestBody UtilityShifting obj,HttpSession session) {
		List<UtilityShifting> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			dataList = utilityShiftingService.getImpactedElementList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getImpactedElementListForUSForm : " + e.getMessage());
		}
		return dataList;
	}
	
	@PostMapping(value = "/ajax/form/add-utility-shifting")
	public Map<String,List<UtilityShifting>> addUtilityShiftingForm(@ModelAttribute UtilityShifting obj,HttpSession session) {
		Map<String,List<UtilityShifting>> map = new LinkedHashMap<>();
		List<UtilityShifting> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			List<UtilityShifting> projectsList = utilityShiftingService.getProjectsListForUtilityShifting(obj);
			map.put("projectsList", projectsList);
			
			List<UtilityShifting> contractsList = utilityShiftingService.getContractsListForUtilityShifting(obj);
			map.put("contractsList", contractsList);
			
			List<UtilityShifting> utilityTypeList = utilityShiftingService.getUtilityTypeList(obj);
			map.put("utilityTypeList", utilityTypeList);
			
			List<UtilityShifting> utilityCategoryList = utilityShiftingService.getUtilityCategoryList(obj);
			map.put("utilityCategoryList", utilityCategoryList);
			
			List<UtilityShifting> utilityExecutionAgencyList = utilityShiftingService.getUtilityExecutionAgencyList(obj);
			map.put("utilityExecutionAgencyList", utilityExecutionAgencyList);
			
			List<UtilityShifting> impactedContractList = utilityShiftingService.getImpactedContractList(obj);
			map.put("impactedContractList", impactedContractList);
			
			List<UtilityShifting> requirementStageList = utilityShiftingService.getRequirementStageList(obj);
			map.put("requirementStageList", requirementStageList);
			
			List<UtilityShifting> unitList = utilityShiftingService.getUnitListForUtilityShifting(obj);
			map.put("unitList", unitList);
			
			List<UtilityShifting> utilityshiftingfiletypeList = utilityShiftingService.getUtilityTypeListForUtilityShifting(obj);
			map.put("utilityshiftingfiletypeList", utilityshiftingfiletypeList);
			
			List<UtilityShifting> statusList = utilityShiftingService.getStatusListForUtilityShifting(obj);
			map.put("statusList", statusList);
			
			List<UtilityShifting> utilityHODList = utilityShiftingService.getHodListForUtilityShifting(obj);
			map.put("utilityHODList", utilityHODList);
			
			List<UtilityShifting> impactedContractsList = utilityShiftingService.getImpactedContractsListForUtilityShifting(obj);
			map.put("impactedContractsList", impactedContractsList);
			
			List<UtilityShifting> reqStageList = utilityShiftingService.getReqStageList(obj);
			map.put("reqStageList", reqStageList);
			
			List<UtilityShifting> impactedElementList = utilityShiftingService.getImpactedElementList(obj);
			map.put("impactedElementList", impactedElementList);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getImpactedElementListForUSForm : " + e.getMessage());
		}
		return map;
	}
//
//@PostMapping(value = "/addUtilityShifting",
//             consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//             produces = MediaType.APPLICATION_JSON_VALUE)
//public ResponseEntity<Boolean> addUtilityShifting(
//        @ModelAttribute UtilityShifting obj,
//        HttpSession session) {
//    try {
//        User uObj = (User) session.getAttribute("user");
//        String user_Id = uObj.getUserId();
//        String userName = uObj.getUserName();
//        String userDesignation = uObj.getDesignation();
//
//        obj.setCreated_by_user_id_fk(user_Id);
//        obj.setUser_id(user_Id);
//        obj.setUser_name(userName);
//        obj.setDesignation(userDesignation);
//
//        // If your DTO has String fields for dates, parse them here
//        obj.setStart_date(DateParser.parse(obj.getStart_date()));
//        obj.setShifting_completion_date(DateParser.parse(obj.getShifting_completion_date()));
//        obj.setPlanned_completion_date(DateParser.parse(obj.getPlanned_completion_date()));
//        obj.setIdentification(DateParser.parse(obj.getIdentification()));
//
//        // If files arrived as part of a different param (see frontend below),
//        // they will already be bound to obj.setUtilityShiftingFiles(...) if you use that field name.
//
//        boolean flag = utilityShiftingService.addUtilityShifting(obj);
//        return ResponseEntity.ok(flag);
//    } catch (Exception e) {
//        logger.error("addUtilityShifting error", e);
//        return ResponseEntity.status(500).body(false);
//    }
//}
	@PostMapping(value="/addUtilityShifting")
	public boolean addUtilityShifting(@ModelAttribute UtilityShifting obj,HttpSession session) {
		User uObj = (User) session.getAttribute("user");
		String user_Id = uObj.getUserId();
		String userName = uObj.getUserName();
		String userDesignation = uObj.getDesignation();
		
		try {
			
			obj.setCreated_by_user_id_fk(user_Id);
			obj.setUser_id(user_Id);
			obj.setUser_name(userName);
			obj.setDesignation(userDesignation);
			User user = (User)session.getAttribute("user");
			
			obj.setStart_date(DateParser.parse(obj.getStart_date()));			
			obj.setShifting_completion_date(DateParser.parse(obj.getShifting_completion_date()));
			obj.setPlanned_completion_date(DateParser.parse(obj.getPlanned_completion_date()));
			obj.setIdentification(DateParser.parse(obj.getIdentification()));
			
			
			boolean flag = utilityShiftingService.addUtilityShifting(obj);
			 
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			//attributes.addFlashAttribute("error", commonError);
			logger.error("addUtilityShifting : " + e.getMessage());
		}
		return false;
	}
	
	@PostMapping(value = "/ajax/getUtilityShiftingList")
	public UtilityShiftingPaginationObject getUtilityShiftingList(@RequestBody UtilityShifting obj, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		//PrintWriter pw = null;
		//JSONObject json = new JSONObject();
		String json2 = null;
		String userId = null;
		String userName = null,user_role_name=null;
		
	
		try {
			User uObj = (User) session.getAttribute("user");
			userId = uObj.getUserId();
			userName = uObj.getUserName();
			user_role_name = uObj.getUserRoleNameFk();
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

			List<UtilityShifting> utilityShiftingList = new ArrayList<UtilityShifting>();
           
			
			//Here is server side pagination logic. Based on the page number you could make call 
			//to the data base create new list and send back to the client. For demo I am shuffling 
			//the same list to show data randomly
			int startIndex = 0;
			int offset = pageDisplayLength;

			if (pageNumber == 1) {
				startIndex = 0;
				offset = pageDisplayLength;
				utilityShiftingList = createPaginationData(session,startIndex, offset, obj, searchParameter);
			} else {
				startIndex = (pageNumber * offset) - offset;
				offset = pageDisplayLength;
				utilityShiftingList = createPaginationData(session,startIndex, offset, obj, searchParameter);
			}

			//Search functionality: Returns filtered list based on search parameter
			//UtilityShiftingList = getListBasedOnSearchParameter(searchParameter,UtilityShiftingList);

			int totalRecords = getTotalRecords(obj, searchParameter, session);

			UtilityShiftingPaginationObject personJsonObject = new UtilityShiftingPaginationObject();
			//Set Total display record
			personJsonObject.setiTotalDisplayRecords(totalRecords);
			//Set Total record
			personJsonObject.setiTotalRecords(totalRecords);
			personJsonObject.setAaData(utilityShiftingList);
			return personJsonObject;
			//Gson gson = new GsonBuilder().setPrettyPrinting().create();
			//json2 = gson.toJson(personJsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"getUtilityShiftingList : User Id - " + userId + " - User Name - " + userName + " - " + e.getMessage());
		}
		return null;
		//pw.println(json2);
	}

	/**
	 * @param searchParameter 
	 * @param activity 
	 * @return
	 */
	public int getTotalRecords(UtilityShifting obj, String searchParameter,HttpSession session) {
		int totalRecords = 0;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			totalRecords = utilityShiftingService.getTotalRecords(obj, searchParameter);
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
	public List<UtilityShifting> createPaginationData(HttpSession session, int startIndex, int offset, UtilityShifting obj, String searchParameter) {
		List<UtilityShifting> objList = null;
		try {

			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			objList = utilityShiftingService.getUtilityShiftingList(obj, startIndex, offset, searchParameter);
		} catch (Exception e) {
			logger.error("createPaginationData : " + e.getMessage());
		}
		return objList;
	}	
	
	@PostMapping(value="/ajax/form/get-utility-shifting")
	public Map<String, List<UtilityShifting>> getUtilityShifting(@RequestBody UtilityShifting obj,HttpSession session) {
		//ModelAndView model = new ModelAndView();
		Map<String, List<UtilityShifting>> map = new LinkedHashMap<>();
		try {
		
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			//model.setViewName(PageConstants.addEditUtilityShifting);
			//model.addObject("action", "edit");
			
			List<UtilityShifting> projectsList = utilityShiftingService.getProjectsListForUtilityShifting(obj);
			map.put("projectsList", projectsList);
			
			

			
			List<UtilityShifting> contractsList = utilityShiftingService.getContractsListForUtilityShifting(obj);
			map.put("contractsList", contractsList);
			
			
			List<UtilityShifting> utilityTypeList = utilityShiftingService.getUtilityTypeList(obj);
			map.put("utilityTypeList", utilityTypeList);
			
			List<UtilityShifting> utilityCategoryList = utilityShiftingService.getUtilityCategoryList(obj);
			map.put("utilityCategoryList", utilityCategoryList);
			
			List<UtilityShifting> utilityExecutionAgencyList = utilityShiftingService.getUtilityExecutionAgencyList(obj);
			map.put("utilityExecutionAgencyList", utilityExecutionAgencyList);
			
			List<UtilityShifting> impactedContractList = utilityShiftingService.getImpactedContractList(obj);
			map.put("impactedContractList", impactedContractList);
			
			List<UtilityShifting> requirementStageList = utilityShiftingService.getRequirementStageList(obj);
			map.put("requirementStageList", requirementStageList);
			
			List<UtilityShifting> unitList = utilityShiftingService.getUnitListForUtilityShifting(obj);
			map.put("unitList", unitList);
			
			List<UtilityShifting> utilityshiftingfiletypeList = utilityShiftingService.getUtilityTypeListForUtilityShifting(obj);
			map.put("utilityshiftingfiletypeList", utilityshiftingfiletypeList);			
			
			List<UtilityShifting> statusList = utilityShiftingService.getStatusListForUtilityShifting(obj);
			map.put("statusList", statusList);	
			
			List<UtilityShifting> utilityHODList = utilityShiftingService.getHodListForUtilityShifting(obj);
			map.put("utilityHODList", utilityHODList);	
			
			List<UtilityShifting> impactedContractsList = utilityShiftingService.getImpactedContractsListForUtilityShifting(obj);
			map.put("impactedContractsList", impactedContractsList);
			
			List<UtilityShifting> reqStageList = utilityShiftingService.getReqStageList(obj);
			map.put("reqStageList", reqStageList);
			
			List<UtilityShifting> impactedElementList = utilityShiftingService.getImpactedElementList(obj);
			map.put("impactedElementList", impactedElementList);
			
		} catch (Exception e) {
			//attributes.addFlashAttribute("error", commonError);
			logger.error("getUtilityShifting : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value="/ajax/form/get-utility-shifting/get-utility-shifting")
	public UtilityShifting getUtilityShifting2(@RequestBody UtilityShifting obj,HttpSession session) throws Exception{
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		
		UtilityShifting utilityShifting = utilityShiftingService.getUtilityShifting(obj);
		return utilityShifting;
	}
	
	@PostMapping(value="/updateUtilityShifting")
	public boolean updateUtilityShifting(@ModelAttribute UtilityShifting obj,HttpSession session) {
		//ModelAndView model = new ModelAndView();
		
	
		try {
			//model.setViewName("redirect:/utilityshifting");

			User uObj = (User) session.getAttribute("user");
			String user_Id = uObj.getUserId();
			String userName = uObj.getUserName();
			String userDesignation = uObj.getDesignation();
			
			obj.setCreated_by_user_id_fk(user_Id);
			obj.setUser_id(user_Id);
			obj.setUser_name(userName);
			obj.setDesignation(userDesignation);
			
			User user = (User)session.getAttribute("user");
			
	
			obj.setStart_date(DateParser.parse(obj.getStart_date()));			
			obj.setShifting_completion_date(DateParser.parse(obj.getShifting_completion_date()));
			obj.setPlanned_completion_date(DateParser.parse(obj.getPlanned_completion_date()));
			obj.setIdentification(DateParser.parse(obj.getIdentification()));
			obj.setCreated_by_user_id_fk(user_Id);
			
			boolean flag = utilityShiftingService.updateUtilityShifting(obj);
			return flag;  
		} catch (Exception e) {
			//attributes.addFlashAttribute("error", commonError);
			logger.error("updateUtilityShifting : " + e.getMessage());
		}
		return false;
	}
	
	@PostMapping(value = "/ajax/getUtilityShiftingUploadsList")
	public List<UtilityShifting> getUtilityShiftingUploadsList(@RequestBody UtilityShifting obj) {
		List<UtilityShifting> objsList = null;
		try {
			objsList = utilityShiftingService.getUtilityShiftingUploadsList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getUtilityShiftingUploadsList : " + e.getMessage());
		}
		return objsList;
	}
//	@PostMapping(value = "/export-utility-shifting")
//	public ResponseEntity<?> exportUtilityShifting(HttpServletRequest request, HttpServletResponse response,HttpSession session,@ModelAttribute UtilityShifting dObj,RedirectAttributes attributes){
//		//ModelAndView view = new ModelAndView(PageConstants.utilityShifting);
//		List<UtilityShifting> dataList = new ArrayList<UtilityShifting>();
//		
//		List<UtilityShifting> subList = new ArrayList<UtilityShifting>();
//		
//		try {
//			//view.setViewName("redirect:/utilityshifting");
//			User uObj = (User) session.getAttribute("user");
//			dObj.setUser_type_fk(uObj.getUserTypeFk());
//			dObj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
//			dObj.setUser_id(uObj.getUserId());
//			dataList =   utilityShiftingService.getUtilityShiftingList(dObj);
//			
//						
//			if(dataList != null && dataList.size() > 0){
//	            XSSFWorkbook  workBook = new XSSFWorkbook ();
//	            XSSFSheet utilityShiftingSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Utility Shifting"));
//				XSSFSheet subSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Progress Details"));
//				XSSFSheet refSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Reference Data"));
//				
//				
//		        workBook.setSheetOrder(utilityShiftingSheet.getSheetName(), 0);
//				workBook.setSheetOrder(subSheet.getSheetName(), 1);
//				workBook.setSheetOrder(refSheet.getSheetName(), 2);
//			
//		        
//		        byte[] blueRGB = new byte[]{(byte)0, (byte)176, (byte)240};
//		        byte[] yellowRGB = new byte[]{(byte)255, (byte)192, (byte)0};
//		        byte[] greenRGB = new byte[]{(byte)146, (byte)208, (byte)80};
//		        byte[] redRGB = new byte[]{(byte)255, (byte)0, (byte)0};
//		        byte[] whiteRGB = new byte[]{(byte)255, (byte)255, (byte)255};
//		        
//		        boolean isWrapText = true;boolean isBoldText = true;boolean isItalicText = false; int fontSize = 11;String fontName = "Times New Roman";
//		        CellStyle blueStyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        CellStyle yellowStyle = cellFormating(workBook,yellowRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        CellStyle greenStyle = cellFormating(workBook,greenRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        CellStyle redStyle = cellFormating(workBook,redRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        CellStyle whiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        
//		        CellStyle indexWhiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        
//		        isWrapText = true;isBoldText = false;isItalicText = false; fontSize = 9;fontName = "Times New Roman";
//		        CellStyle sectionStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        
//		        
//		        
//	            XSSFRow headingRow = utilityShiftingSheet.createRow(0);
//	            String headerString = "Utility ID ^Project ^Execution Agency ^HOD ^Utility Type ^Utility Description ^Location ^Custodian ^Identification Date ^Reference No. "
//	            		+ "^Chainage ^Executed By ^Impacted Contract  ^Requirement stage "
//	            		+ "^Impacted Element ^Affected Structures  ^Target Date ^Scope ^Completed ^Unit ^Start Date ^Status ^Completetion Date ^Remarks";
//	            
//	            String[] firstHeaderStringArr = headerString.split("\\^");
//	            utilityShiftingSheet.createFreezePane(0,1);
//	            for (int i = 0; i < firstHeaderStringArr.length; i++) {		        	
//		        	Cell cell = headingRow.createCell(i);
//			        cell.setCellStyle(greenStyle);
//					cell.setCellValue(firstHeaderStringArr[i]);
//				}
//	            
//				XSSFRow headingRow1 = subSheet.createRow(0);
//	            String headerString1 = "Utility ID ^Progress Date ^Progress Of Work";
//	            
//	            String[] secondHeaderStringArr = headerString1.split("\\^");
//	            subSheet.createFreezePane(0,1);
//	            for (int i = 0; i < secondHeaderStringArr.length; i++) {		        	
//		        	Cell cell = headingRow1.createCell(i);
//			        cell.setCellStyle(greenStyle);
//					cell.setCellValue(secondHeaderStringArr[i]);
//				}
//				
//			
//				 short rowNo5 = 1;
//				 for (UtilityShifting rDetails : dataList) { 
//					String utility_shifting_id = rDetails.getUtility_shifting_id();
//					subList = utilityShiftingService.getRDetailsList(utility_shifting_id);
//						
//					 for (UtilityShifting obj : subList) {
//			                XSSFRow row = subSheet.createRow(rowNo5);
//			                int b = 0;
//			                
//			                Cell cell1 = row.createCell(b++);
//							cell1.setCellStyle(sectionStyle);
//							cell1.setCellValue(obj.getUtility_shifting_id());
//							
//			                cell1 = row.createCell(b++);
//							cell1.setCellStyle(sectionStyle);
//							cell1.setCellValue(obj.getProgress_date());
//							
//			                cell1 = row.createCell(b++);
//							cell1.setCellStyle(sectionStyle);
//							cell1.setCellValue(obj.getProgress_of_work());
//							
//			               
//							rowNo5++;
//					    }
//			       }
//				 short rowNo = 1;
//		         for (UtilityShifting obj : dataList) {
//					
//	                XSSFRow row = utilityShiftingSheet.createRow(rowNo);
//	                int c = 0;
//	               
//	                Cell cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getUtility_shifting_id());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getProject_name());
//					
//	                cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getWork_short_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getExecution_agency_fk());
//					
//					/*
//					 * String hod = ""; if(!StringUtils.isEmpty(obj.getHod_user_id_fk())) {hod =
//					 * obj.getHod_user_id_fk() +" - "+obj.getUser_name();}
//					 */
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getDesignation());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getUtility_type_fk());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getUtility_description());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getLocation_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getCustodian());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getIdentification());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getReference_number());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getChainage());	
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getExecuted_by());
//					
//				    cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_short_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getRequirement_stage_fk());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getImpacted_element());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getAffected_structures());
//					
//	                cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getPlanned_completion_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getScope());									
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getCompleted());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getUnit_fk());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getStart_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getShifting_status_fk());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getShifting_completion_date());
//									
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getRemarks());
//					
//	                rowNo++;
//	            }
//		         
//		        /*************************************************************/ 
//		         
//		        List<UtilityShifting> projectsList = utilityShiftingService.getProjectsListForUtilityShifting(null);
//				List<UtilityShifting> utilityTypeList = utilityShiftingService.getUtilityTypeList(null);
//				List<UtilityShifting> utilityExecutionAgencyList = utilityShiftingService.getUtilityExecutionAgencyList(null);
//				List<UtilityShifting> statusList = utilityShiftingService.getStatusListForUtilityShifting(null);
//				List<UtilityShifting> utilityHODList = utilityShiftingService.getHodListForUtilityShifting(null);
//				List<UtilityShifting> impactedContractsList = utilityShiftingService.getImpactedContractsListForUtilityShifting(null);
//				List<UtilityShifting> reqStageList = utilityShiftingService.getReqStageList(null);
//				List<UtilityShifting> impactedElementList = utilityShiftingService.getImpactedElementList(null);
//				
//				
//					
//		        XSSFRow headerRow = refSheet.createRow(0);
//	            refSheet.createFreezePane(0,1);
//	            
//	            int b = 1;	
//	            Cell cell = headerRow.createCell(b);
//		        cell.setCellStyle(greenStyle);
//				cell.setCellValue("Project Name");
//				int rowNoRef = 1;			
//				XSSFRow row = null;
//				for (UtilityShifting obj : projectsList) {
//	                row = refSheet.createRow(rowNoRef++);	                	                
//	                cell = row.createCell(b);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getProject_name());
//				}
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//
//				int p = 1;
//
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//				b = b+2;
//				cell = headerRow.createCell(b);
//		        cell.setCellStyle(greenStyle);
//				cell.setCellValue("Execution Agencies");	
//				p = 1;
//				for (UtilityShifting obj : utilityExecutionAgencyList) {
//					row = refSheet.getRow(p++);
//					if(row == null) {
//		                row = refSheet.createRow(rowNoRef++);
//					}
//	                cell = row.createCell(b);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getExecution_agency_fk());
//				}
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//				b = b+2;
//				cell = headerRow.createCell(b);
//		        cell.setCellStyle(greenStyle);
//				cell.setCellValue("HOD");	
//				p = 1;
//				for (UtilityShifting obj : utilityHODList) {
//					row = refSheet.getRow(p++);
//					if(row == null) {
//		                row = refSheet.createRow(rowNoRef++);
//					}
//	                cell = row.createCell(b);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getDesignation());
//				}
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//				b = b+2;
//				cell = headerRow.createCell(b);
//		        cell.setCellStyle(greenStyle);
//				cell.setCellValue("Utility Type");	
//				p = 1;
//				for (UtilityShifting obj : utilityTypeList) {
//					row = refSheet.getRow(p++);
//					if(row == null) {
//		                row = refSheet.createRow(rowNoRef++);
//					}
//	                cell = row.createCell(b);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getUtility_type_fk());
//				}
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//				b = b+2;
//				cell = headerRow.createCell(b);
//		        cell.setCellStyle(greenStyle);
//				cell.setCellValue("Impacted Contract");	
//				p = 1;
//				for (UtilityShifting obj : impactedContractsList) {
//					row = refSheet.getRow(p++);
//					if(row == null) {
//		                row = refSheet.createRow(rowNoRef++);
//					}
//	                cell = row.createCell(b);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_short_name());
//				}
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//				b = b+2;
//				cell = headerRow.createCell(b);
//		        cell.setCellStyle(greenStyle);
//				cell.setCellValue("Requirement stage ");	
//				p = 1;
//				for (UtilityShifting obj : reqStageList) {
//					row = refSheet.getRow(p++);
//					if(row == null) {
//		                row = refSheet.createRow(rowNoRef++);
//					}
//	                cell = row.createCell(b);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getRequirement_stage_fk());
//				}
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//				b = b+2;
//				cell = headerRow.createCell(b);
//		        cell.setCellStyle(greenStyle);
//				cell.setCellValue("Impacted Element");	
//				p = 1;
//				for (UtilityShifting obj : impactedElementList) {
//					row = refSheet.getRow(p++);
//					if(row == null) {
//		                row = refSheet.createRow(rowNoRef++);
//					}
//	                cell = row.createCell(b);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getImpacted_element());
//				}
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//				b = b+2;
//				cell = headerRow.createCell(b);
//		        cell.setCellStyle(greenStyle);
//				cell.setCellValue("Status");	
//				p = 1;
//				for (UtilityShifting obj : statusList) {
//					row = refSheet.getRow(p++);
//					if(row == null) {
//		                row = refSheet.createRow(rowNoRef++);
//					}
//	                cell = row.createCell(b);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getShifting_status_fk());
//				}
//				refSheet.setColumnWidth(b, 25 * 200);
//				
//			
//				
//	        	for(int columnIndex = 0; columnIndex < 29; columnIndex++) {
//	        		utilityShiftingSheet.setColumnWidth(columnIndex, 25 * 200);
//				}
//	        	for(int columnIndex = 0; columnIndex < 38; columnIndex++) {
//	        		subSheet.setColumnWidth(columnIndex, 25 * 200);
//				}
//	        	
//	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
//	            Date date = new Date();
//	            String fileName = "Utility_Shifting_"+dateFormat.format(date);
//	            
//	            try{
//	              
//	               response.setContentType("application/.csv");
//	 			   response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//	 			   response.setContentType("application/vnd.ms-excel");
//	 			   // add response header
//	 			   response.addHeader("Content-Disposition", "attachment; filename=" + fileName+".xlsx");
//	 			   
//	 			    //copies all bytes from a file to an output stream
//	 			   workBook.write(response.getOutputStream()); // Write workbook to response.
//		           workBook.close();
//	 			    //flushes output stream
//	 			    response.getOutputStream().flush();
//	            	
//	                
//	                return ResponseEntity.ok(Map.of("success",dataExportSucess));
//	            
//	            }catch(FileNotFoundException e){
//	                //e.printStackTrace();
//	            	return ResponseEntity.ok(Map.of("error",dataExportInvalid));
//	                //attributes.addFlashAttribute("error",dataExportInvalid);
//	            }catch(IOException e){
//	                //e.printStackTrace();
//	            	return ResponseEntity.ok(Map.of("error",dataExportError));
//	                //attributes.addFlashAttribute("error",dataExportError);
//	            }
//			}else{
//				return ResponseEntity.ok(Map.of("error",dataExportNoData));
//				//attributes.addFlashAttribute("error",dataExportNoData);
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ResponseEntity.ok(Map.of("success",dataExportSucess));
//	}
//	private CellStyle cellFormating(XSSFWorkbook workBook,byte[] rgb,HorizontalAlignment hAllign, VerticalAlignment vAllign, boolean isWrapText,boolean isBoldText,boolean isItalicText,int fontSize,String fontName) {
//		CellStyle style = workBook.createCellStyle();
//		//Setting Background color  
//		//style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
//		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		
//		if (style instanceof XSSFCellStyle) {
//		   XSSFCellStyle xssfcellcolorstyle = (XSSFCellStyle)style;
//		   xssfcellcolorstyle.setFillForegroundColor(new XSSFColor(rgb, null));
//		}
//		//style.setFillPattern(FillPatternType.ALT_BARS);
//		style.setBorderBottom(BorderStyle.MEDIUM);
//		style.setBorderTop(BorderStyle.MEDIUM);
//		style.setBorderLeft(BorderStyle.MEDIUM);
//		style.setBorderRight(BorderStyle.MEDIUM);
//		style.setAlignment(hAllign);
//		style.setVerticalAlignment(vAllign);
//		style.setWrapText(isWrapText);
//		
//		Font font = workBook.createFont();
//        //font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
//        font.setFontHeightInPoints((short)fontSize);  
//        font.setFontName(fontName);  //"Times New Roman"
//        
//        font.setItalic(isItalicText); 
//        font.setBold(isBoldText);
//        // Applying font to the style  
//        style.setFont(font); 
//        
//        return style;
//	}

	

@PostMapping(value = "/export-utility-shifting")
public ResponseEntity<byte[]> exportUtilityShifting(HttpSession session,
                                                   @ModelAttribute UtilityShifting dObj) {
    try {
        // 1) get current user and populate filter
        User uObj = (User) session.getAttribute("user");
        if (uObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(("error: user not logged in").getBytes(StandardCharsets.UTF_8));
        }
        dObj.setUser_type_fk(uObj.getUserTypeFk());
        dObj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
        dObj.setUser_id(uObj.getUserId());

        // 2) fetch main data
        List<UtilityShifting> dataList = utilityShiftingService.getUtilityShiftingList(dObj);
        if (dataList == null || dataList.isEmpty()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("{" + "\"error\":\"No data to export\"" + "}").getBytes(StandardCharsets.UTF_8));
        }

        // 3) build workbook
        try (XSSFWorkbook workbook = buildWorkbook(dataList)) {
            // write to byte array
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);
                byte[] bytes = bos.toByteArray();

                // prepare filename
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
                String baseFileName = "Utility_Shifting_" + dateFormat.format(new Date()) + ".xlsx";
                String encodedFilename = URLEncoder.encode(baseFileName, StandardCharsets.UTF_8.toString())
                                                  .replaceAll("\\+", "%20");

                HttpHeaders headers = new HttpHeaders();
                ContentDisposition contentDisposition = ContentDisposition
                        .attachment()
                        .filename(baseFileName, StandardCharsets.UTF_8)
                        .build();
                headers.setContentDisposition(contentDisposition);
                headers.setContentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentLength(bytes.length);

                return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            }
        }
    } catch (Exception e) {
        // TODO: replace with logger.error(...)
        e.printStackTrace();
        String errorJson = "{\"error\":\"Export failed: " + escapeForJson(e.getMessage()) + "\"}";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorJson.getBytes(StandardCharsets.UTF_8));
    }
}

/**
 * Build and return the XSSFWorkbook. Keep this simple and deterministic.
 * Note: this method fetches sub-records per item (retains your existing getRDetailsList call).
 * Consider batching sub-detail fetches if dataList is large to avoid N+1 DB calls.
 */
private XSSFWorkbook buildWorkbook(List<UtilityShifting> dataList) throws Exception {
    XSSFWorkbook workBook = new XSSFWorkbook();

    XSSFSheet utilityShiftingSheet = workBook.createSheet("Utility Shifting");
    XSSFSheet subSheet = workBook.createSheet("Progress Details");
    XSSFSheet refSheet = workBook.createSheet("Reference Data");

    workBook.setSheetOrder(utilityShiftingSheet.getSheetName(), 0);
    workBook.setSheetOrder(subSheet.getSheetName(), 1);
    workBook.setSheetOrder(refSheet.getSheetName(), 2);

    // color bytes
    byte[] blueRGB = new byte[]{(byte) 0, (byte) 176, (byte) 240};
    byte[] yellowRGB = new byte[]{(byte) 255, (byte) 192, (byte) 0};
    byte[] greenRGB = new byte[]{(byte) 146, (byte) 208, (byte) 80};
    byte[] redRGB = new byte[]{(byte) 255, (byte) 0, (byte) 0};
    byte[] whiteRGB = new byte[]{(byte) 255, (byte) 255, (byte) 255};

    // shared styles (reuse these)
    CellStyle greenStyle = cellFormating(workBook, greenRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
            true, true, false, 11, "Times New Roman");
    CellStyle sectionStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT, VerticalAlignment.CENTER,
            true, false, false, 9, "Times New Roman");

    // --- Utility Shifting header ---
    XSSFRow headingRow = utilityShiftingSheet.createRow(0);
    String headerString = "Utility ID ^Project ^Execution Agency ^HOD ^Utility Type ^Utility Description ^Location ^Custodian ^Identification Date ^Reference No. "
            + "^Chainage ^Executed By ^Impacted Contract  ^Requirement stage "
            + "^Impacted Element ^Affected Structures  ^Target Date ^Scope ^Completed ^Unit ^Start Date ^Status ^Completetion Date ^Remarks";
    String[] firstHeaderStringArr = headerString.split("\\^");
    utilityShiftingSheet.createFreezePane(0, 1);
    for (int i = 0; i < firstHeaderStringArr.length; i++) {
        Cell cell = headingRow.createCell(i);
        cell.setCellStyle(greenStyle);
        cell.setCellValue(firstHeaderStringArr[i].trim());
    }

    // --- Progress Details header ---
    XSSFRow headingRow1 = subSheet.createRow(0);
    String headerString1 = "Utility ID ^Progress Date ^Progress Of Work";
    String[] secondHeaderStringArr = headerString1.split("\\^");
    subSheet.createFreezePane(0, 1);
    for (int i = 0; i < secondHeaderStringArr.length; i++) {
        Cell cell = headingRow1.createCell(i);
        cell.setCellStyle(greenStyle);
        cell.setCellValue(secondHeaderStringArr[i].trim());
    }

    // --- Fill Progress Details (subSheet) ---
    int subRowNo = 1;
    for (UtilityShifting rDetails : dataList) {
        String utility_shifting_id = rDetails.getUtility_shifting_id();
        // keep existing service call - this is N+1 if many rows; consider a batch method
        List<UtilityShifting> subList = utilityShiftingService.getRDetailsList(utility_shifting_id);
        if (subList == null) {
            continue;
        }
        for (UtilityShifting obj : subList) {
            XSSFRow row = subSheet.createRow(subRowNo++);
            int b = 0;
            Cell cell1 = row.createCell(b++);
            cell1.setCellStyle(sectionStyle);
            cell1.setCellValue(obj.getUtility_shifting_id());

            cell1 = row.createCell(b++);
            cell1.setCellStyle(sectionStyle);
            cell1.setCellValue(nullSafe(obj.getProgress_date()));

            cell1 = row.createCell(b++);
            cell1.setCellStyle(sectionStyle);
            cell1.setCellValue(nullSafe(obj.getProgress_of_work()));
        }
    }

    // --- Fill main sheet (utilityShiftingSheet) ---
    int rowNo = 1;
    for (UtilityShifting obj : dataList) {
        XSSFRow row = utilityShiftingSheet.createRow(rowNo++);
        int c = 0;

        Cell cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getUtility_shifting_id()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getProject_name()));

//        cell = row.createCell(c++);
//        cell.setCellStyle(sectionStyle);
//        cell.setCellValue(nullSafe(obj.getWork_short_name()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getExecution_agency_fk()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getDesignation()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getUtility_type_fk()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getUtility_description()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getLocation_name()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getCustodian()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getIdentification()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getReference_number()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getChainage()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getExecuted_by()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getContract_short_name()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getRequirement_stage_fk()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getImpacted_element()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getAffected_structures()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getPlanned_completion_date()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getScope()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getCompleted()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getUnit_fk()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getStart_date()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getShifting_status_fk()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getShifting_completion_date()));

        cell = row.createCell(c++);
        cell.setCellStyle(sectionStyle);
        cell.setCellValue(nullSafe(obj.getRemarks()));
    }

    // --- Reference sheet: fetch lists and populate ---
    List<UtilityShifting> projectsList = utilityShiftingService.getProjectsListForUtilityShifting(null);
    List<UtilityShifting> utilityTypeList = utilityShiftingService.getUtilityTypeList(null);
    List<UtilityShifting> utilityExecutionAgencyList = utilityShiftingService.getUtilityExecutionAgencyList(null);
    List<UtilityShifting> statusList = utilityShiftingService.getStatusListForUtilityShifting(null);
    List<UtilityShifting> utilityHODList = utilityShiftingService.getHodListForUtilityShifting(null);
    List<UtilityShifting> impactedContractsList = utilityShiftingService.getImpactedContractsListForUtilityShifting(null);
    List<UtilityShifting> reqStageList = utilityShiftingService.getReqStageList(null);
    List<UtilityShifting> impactedElementList = utilityShiftingService.getImpactedElementList(null);

    XSSFRow headerRow = refSheet.createRow(0);
    refSheet.createFreezePane(0, 1);

    int col = 1;
    int refRowNo = 1;
    // Projects
    Cell cell = headerRow.createCell(col);
    cell.setCellStyle(greenStyle);
    cell.setCellValue("Project Name");
    for (UtilityShifting p : safeList(projectsList)) {
        XSSFRow r = refSheet.getRow(refRowNo);
        if (r == null) r = refSheet.createRow(refRowNo);
        Cell ccell = r.createCell(col);
        ccell.setCellStyle(sectionStyle);
        ccell.setCellValue(nullSafe(p.getProject_name()));
        refRowNo++;
    }
    refSheet.setColumnWidth(col, 25 * 256);

    // Execution Agencies (next column +2 to leave empty column as before)
    col = col + 2;
    cell = headerRow.createCell(col);
    cell.setCellStyle(greenStyle);
    cell.setCellValue("Execution Agencies");
    refRowNo = 1;
    for (UtilityShifting p : safeList(utilityExecutionAgencyList)) {
        XSSFRow r = refSheet.getRow(refRowNo);
        if (r == null) r = refSheet.createRow(refRowNo);
        Cell ccell = r.createCell(col);
        ccell.setCellStyle(sectionStyle);
        ccell.setCellValue(nullSafe(p.getExecution_agency_fk()));
        refRowNo++;
    }
    refSheet.setColumnWidth(col, 25 * 256);

    // HOD
    col = col + 2;
    cell = headerRow.createCell(col);
    cell.setCellStyle(greenStyle);
    cell.setCellValue("HOD");
    refRowNo = 1;
    for (UtilityShifting p : safeList(utilityHODList)) {
        XSSFRow r = refSheet.getRow(refRowNo);
        if (r == null) r = refSheet.createRow(refRowNo);
        Cell ccell = r.createCell(col);
        ccell.setCellStyle(sectionStyle);
        ccell.setCellValue(nullSafe(p.getDesignation()));
        refRowNo++;
    }
    refSheet.setColumnWidth(col, 25 * 256);

    // Utility Type
    col = col + 2;
    cell = headerRow.createCell(col);
    cell.setCellStyle(greenStyle);
    cell.setCellValue("Utility Type");
    refRowNo = 1;
    for (UtilityShifting p : safeList(utilityTypeList)) {
        XSSFRow r = refSheet.getRow(refRowNo);
        if (r == null) r = refSheet.createRow(refRowNo);
        Cell ccell = r.createCell(col);
        ccell.setCellStyle(sectionStyle);
        ccell.setCellValue(nullSafe(p.getUtility_type_fk()));
        refRowNo++;
    }
    refSheet.setColumnWidth(col, 25 * 256);

    // Impacted Contract
    col = col + 2;
    cell = headerRow.createCell(col);
    cell.setCellStyle(greenStyle);
    cell.setCellValue("Impacted Contract");
    refRowNo = 1;
    for (UtilityShifting p : safeList(impactedContractsList)) {
        XSSFRow r = refSheet.getRow(refRowNo);
        if (r == null) r = refSheet.createRow(refRowNo);
        Cell ccell = r.createCell(col);
        ccell.setCellStyle(sectionStyle);
        ccell.setCellValue(nullSafe(p.getContract_short_name()));
        refRowNo++;
    }
    refSheet.setColumnWidth(col, 25 * 256);

    // Requirement stage
    col = col + 2;
    cell = headerRow.createCell(col);
    cell.setCellStyle(greenStyle);
    cell.setCellValue("Requirement stage ");
    refRowNo = 1;
    for (UtilityShifting p : safeList(reqStageList)) {
        XSSFRow r = refSheet.getRow(refRowNo);
        if (r == null) r = refSheet.createRow(refRowNo);
        Cell ccell = r.createCell(col);
        ccell.setCellStyle(sectionStyle);
        ccell.setCellValue(nullSafe(p.getRequirement_stage_fk()));
        refRowNo++;
    }
    refSheet.setColumnWidth(col, 25 * 256);

    // Impacted Element
    col = col + 2;
    cell = headerRow.createCell(col);
    cell.setCellStyle(greenStyle);
    cell.setCellValue("Impacted Element");
    refRowNo = 1;
    for (UtilityShifting p : safeList(impactedElementList)) {
        XSSFRow r = refSheet.getRow(refRowNo);
        if (r == null) r = refSheet.createRow(refRowNo);
        Cell ccell = r.createCell(col);
        ccell.setCellStyle(sectionStyle);
        ccell.setCellValue(nullSafe(p.getImpacted_element()));
        refRowNo++;
    }
    refSheet.setColumnWidth(col, 25 * 256);

    // Status
    col = col + 2;
    cell = headerRow.createCell(col);
    cell.setCellStyle(greenStyle);
    cell.setCellValue("Status");
    refRowNo = 1;
    for (UtilityShifting p : safeList(statusList)) {
        XSSFRow r = refSheet.getRow(refRowNo);
        if (r == null) r = refSheet.createRow(refRowNo);
        Cell ccell = r.createCell(col);
        ccell.setCellStyle(sectionStyle);
        ccell.setCellValue(nullSafe(p.getShifting_status_fk()));
        refRowNo++;
    }
    refSheet.setColumnWidth(col, 25 * 256);

    // set sensible column widths for main sheets
    for (int columnIndex = 0; columnIndex < firstHeaderStringArr.length; columnIndex++) {
        utilityShiftingSheet.setColumnWidth(columnIndex, 25 * 256);
    }
    for (int columnIndex = 0; columnIndex < 10; columnIndex++) {
        subSheet.setColumnWidth(columnIndex, 25 * 256);
    }

    return workBook;
}

/** Helper: null-safe string conversion */
private String nullSafe(Object o) {
    return o == null ? "" : String.valueOf(o);
}

/** Helper: return non-null list */
private <T> List<T> safeList(List<T> l) {
    return l == null ? new ArrayList<>() : l;
}

/** Helper: escape JSON string value for error message (very small helper) */
private String escapeForJson(String s) {
    if (s == null) return "";
    return s.replace("\"", "\\\"");
}

/**
 * Cell style factory (keeps style creation in one place)
 */
private CellStyle cellFormating(XSSFWorkbook workBook, byte[] rgb,
                                HorizontalAlignment hAlign, VerticalAlignment vAlign,
                                boolean isWrapText, boolean isBoldText, boolean isItalicText,
                                int fontSize, String fontName) {
    CellStyle style = workBook.createCellStyle();
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    if (style instanceof XSSFCellStyle) {
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(rgb, null));
    }

    style.setBorderBottom(BorderStyle.MEDIUM);
    style.setBorderTop(BorderStyle.MEDIUM);
    style.setBorderLeft(BorderStyle.MEDIUM);
    style.setBorderRight(BorderStyle.MEDIUM);

    style.setAlignment(hAlign);
    style.setVerticalAlignment(vAlign);
    style.setWrapText(isWrapText);

    Font font = workBook.createFont();
    font.setFontHeightInPoints((short) fontSize);
    font.setFontName(fontName);
    font.setItalic(isItalicText);
    font.setBold(isBoldText);
    style.setFont(font);

    return style;
}
	
	
	@PostMapping(value = "/upload-utility-shifting")
    public ResponseEntity<?> uploadUtilityShifting(
            @RequestParam("file") MultipartFile file,
            HttpSession session) {
        
        String msg = "";
        String userId = null;
        String attributeKey = "";
        String attributeMsg = "";
        
        try {
            // Get user from session
            User uObj = (User) session.getAttribute("user");
            if (uObj == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not logged in"));
            }
            
            userId = uObj.getUserId();
            String userName = uObj.getUserName();
            String userDesignation = uObj.getDesignation();

            // Create UtilityShifting object and set user info
            UtilityShifting obj = new UtilityShifting();
            obj.setCreated_by_user_id_fk(userId);
            obj.setUser_id(userId);
            obj.setUser_name(userName);
            obj.setDesignation(userDesignation);
            obj.setUtilityFile(file); // Set the file

            if (file != null && !file.isEmpty()) {
                // Creates a workbook object from the uploaded excelfile
                if (file.getSize() > 0) {
                    XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
                    // Creates a worksheet object representing the first sheet
                    int sheetsCount = workbook.getNumberOfSheets();
                    if (sheetsCount > 0) {
                        XSSFSheet laSheet = workbook.getSheetAt(0);
                        // header row
                        XSSFRow headerRow = laSheet.getRow(0);
                        // checking given file format
                        if (headerRow != null) {
                            List<String> fileFormat = FileFormatModel.getUtilityShiftingFileFormat();
                            int columnSize = fileFormat.size();
                            int noOfColumns = headerRow.getLastCellNum();
                            String columnName = headerRow.getCell(0).getStringCellValue().trim();
                            
                            if (!columnName.equalsIgnoreCase(fileFormat.get(0).trim()) && columnName.equals("Project")) {
                                columnSize = columnSize - 1;
                            }
                            
                            if (noOfColumns == columnSize) {
                                boolean tempFlag = false;
                                for (int i = 0; i < columnSize; i++) {
                                    columnName = headerRow.getCell(i).getStringCellValue().trim();
                                    if (i == 0 && "Utility ID".equalsIgnoreCase(fileFormat.get(i).trim()) && columnName.equals("Project") 
                                    		&& !columnName.equals(fileFormat.get(i).trim())) {
                                        tempFlag = true;
                                    }
                                    if (tempFlag) {
                                        i++;
                                    }
                                    if (!columnName.equals(fileFormat.get(i).trim()) && !columnName.contains(fileFormat.get(i).trim())) {
                                        msg = "Upload format error - column mismatch";
                                        obj.setUploaded_by_user_id_fk(userId);
                                        obj.setStatus("Fail");
                                        obj.setRemarks(msg);
                                        utilityShiftingService.saveUSDataUploadFile(obj);
                                        return ResponseEntity.ok(Map.of("error", msg));
                                    }
                                }
                            } else {
                                msg = "Upload format error - column count mismatch";
                                obj.setUploaded_by_user_id_fk(userId);
                                obj.setStatus("Fail");
                                obj.setRemarks(msg);
                                utilityShiftingService.saveUSDataUploadFile(obj);
                                return ResponseEntity.ok(Map.of("error", msg));
                            }
                        } else {
                            msg = "Upload format error - header row missing";
                            obj.setUploaded_by_user_id_fk(userId);
                            obj.setStatus("Fail");
                            obj.setRemarks(msg);
                            utilityShiftingService.saveUSDataUploadFile(obj);
                            return ResponseEntity.ok(Map.of("error", msg));
                        }
                        
                        String[] result = uploadUtilityShifting(obj, userId, userName);
                        String errMsg = result[0];
                        String actualVal = "";
                        int count = 0, row = 0, sheet = 0, subRow = 0;
                        List<String> fileFormat = FileFormatModel.getUtilityShiftingFileFormat();
                        
                        if (!StringUtils.isEmpty(result[1])) {
                            count = Integer.parseInt(result[1]);
                        }
                        if (!StringUtils.isEmpty(result[2])) {
                            row = Integer.parseInt(result[2]);
                        }
                        if (!StringUtils.isEmpty(result[3])) {
                            sheet = Integer.parseInt(result[3]);
                        }
                        if (!StringUtils.isEmpty(result[4])) {
                            subRow = Integer.parseInt(result[4]);
                        }
                        
                        if (!StringUtils.isEmpty(errMsg)) {
                            // Handle different error types
                            if (!StringUtils.isEmpty(errMsg) && errMsg.contains("Duplicate entry")) {
                                attributeKey = "error";
                                attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;<b>Work and Utility Shifting Id Mismatch at row: (" + row + ")</b> please check and Upload again.</span>";
                                msg = "Work and Utility Shifting Id Mismatch at row: " + row;
                            } else if (!StringUtils.isEmpty(errMsg) && errMsg.contains("Data truncated")) {
                                actualVal = Integer.toString(subRow);
                                if (sheet == 1) {
                                    subRow = row;
                                    String error = "Data truncated";
                                    actualVal = FileFormatModel.getActualValue(error, errMsg, subRow, fileFormat);
                                }
                                attributeKey = "error";
                                attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect Value identified in <b>Sheet: [" + sheet + "]</b> at <b>row: [" + actualVal + "]</b> please check and Upload again.</span>";
                                msg = "Incorrect value identified in Sheet: " + sheet + " at row: " + actualVal;
                            } 
                            // ... handle other error types as in your original code
                            else {
                                attributeKey = "error";
                                attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;" + errMsg + "</span>";
                                msg = errMsg;
                            }

                            obj.setUploaded_by_user_id_fk(userId);
                            obj.setStatus("Fail");
                            obj.setRemarks(msg);
                            utilityShiftingService.saveUSDataUploadFile(obj);
                            return ResponseEntity.ok(Map.of(attributeKey, attributeMsg));
                        }

                        if (count > 0) {
                            attributeKey = "success";
                            String columnName = headerRow.getCell(0).getStringCellValue().trim();
                            if( columnName.equals("Utility ID"))
                            {
                            	attributeMsg = "<i class='fa fa-check'></i>&nbsp;" + count + "<span style='color:green;'> records updated successfully.</span>";
                            	msg = count + " records Uploaded successfully.";}
                            else if(columnName.equals("Project"))
                            {
                            	attributeMsg = "<i class='fa fa-check'></i>&nbsp;" + count + "<span style='color:green;'> records Added successfully.</span>";
                                msg = count + " records Added successfully.";
                            }
                            else
                            {
                            	attributeMsg = "<i class='fa fa-check'></i>&nbsp;" + count + "<span style='color:green;'> records Added / updated successfully.</span>";
                                msg = count + " records Added / updated successfully.";
                            }
                            
                            // Save form history
                            FormHistory formHistory = new FormHistory();
                            formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
                            formHistory.setUser(obj.getDesignation() + " - " + obj.getUser_name());
                            formHistory.setModule_name_fk("Utility Shifting");
                            formHistory.setForm_name("Upload Utility Shifting");
                            formHistory.setForm_action_type("Upload");
                            formHistory.setForm_details(msg);
                            formHistory.setWork(obj.getWork_id_fk());
                            // formsHistoryDao.saveFormHistory(formHistory);

                        } else {
                            attributeKey = "success";
                            attributeMsg = "No records found.";
                            msg = "No records found.";
                        }
                        
                        obj.setUploaded_by_user_id_fk(userId);
                        obj.setStatus("Success");
                        obj.setRemarks(msg);
                        utilityShiftingService.saveUSDataUploadFile(obj);
                    }
                    workbook.close();
                }
            } else {
                attributeKey = "error";
                attributeMsg = "Something went wrong. Please try after some time";
                msg = "No file exists";
                obj.setUploaded_by_user_id_fk(userId);
                obj.setStatus("Fail");
                obj.setRemarks(msg);
                utilityShiftingService.saveUSDataUploadFile(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
            attributeKey = "error";
            attributeMsg = "Something went wrong. Please try after some time";
            logger.fatal("uploadUtilityShifting() : " + e.getMessage());
            msg = "Something went wrong. Please try after some time";
            
            UtilityShifting obj = new UtilityShifting();
            obj.setUploaded_by_user_id_fk(userId);
            obj.setStatus("Fail");
            obj.setRemarks(msg);
            try {
                utilityShiftingService.saveUSDataUploadFile(obj);
            } catch (Exception e1) {
                logger.fatal("saveUSDataUploadFile() : " + e.getMessage());
            }
        }
        
        return ResponseEntity.ok(Map.of(attributeKey, attributeMsg));
    }

	private  String[]  uploadUtilityShifting(UtilityShifting obj, String userId, String userName) throws Exception {
		UtilityShifting us = null;
	
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter3 = new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");	
		
		List<UtilityShifting> ussList = new ArrayList<UtilityShifting>();
		String[] result = new String[5];
		Writer w = null;
		int count = 0;
		String invalidRows = "";
		try {	
			MultipartFile excelfile = obj.getUtilityFile();
			// Creates a workbook object from the uploaded excelfile
			if (!StringUtils.isEmpty(excelfile) && excelfile.getSize() > 0 ){
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				int sheetsCount = workbook.getNumberOfSheets();
				if(sheetsCount > 0) {					
					XSSFSheet laSheet = workbook.getSheetAt(0);
					
				
							
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					//System.out.println(uploadFilesSheet.getLastRowNum());


					for(int i = 1; i <= laSheet.getLastRowNum();i++){
						XSSFRow headerRow = laSheet.getRow(0);
						String columnName = headerRow.getCell(0).getStringCellValue();
						XSSFRow row = laSheet.getRow(i);
						// Sets the Read data to the model class
						// Cell cell = row.getCell(0);
						// String j_username = formatter.formatCellValue(row.getCell(0));
						//System.out.println(i);
						us = new UtilityShifting();
						String val = null;
						if(!StringUtils.isEmpty(row)) {		
							int p = 0;
							if("Utility ID".equalsIgnoreCase(columnName.trim())) {
								val = formatter.formatCellValue(row.getCell(p++)).trim();
								if(!StringUtils.isEmpty(val)) { us.setUtility_shifting_id(val);}
							}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setProject_id_fk(val);}

							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setExecution_agency_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setHod_user_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setUtility_type_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setUtility_description(val);}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setLocation_name(val);}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setCustodian(val);}
							
							//val = formatter.formatCellValue(row.getCell(p++)).trim();
							//val = formatter.formatCellValue(row.getCell(p++),evaluator,cfEvaluator).trim();
							val = getCellTypeDateValue(row.getCell(p++));
							if(!StringUtils.isEmpty(val)) { us.setIdentification(val);}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setReference_number(val);}							
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setChainage(val);}							
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setExecuted_by(val);}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setImpacted_contract_id_fk(val);}					
							
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setRequirement_stage_fk(val);}	
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { us.setImpacted_element(val);}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) {us.setAffected_structures(val);}
							
							//val = formatter.formatCellValue(row.getCell(p++)).trim();
							//val = formatter.formatCellValue(row.getCell(p++),evaluator,cfEvaluator).trim();
							val = getCellTypeDateValue(row.getCell(p++));
							if(!StringUtils.isEmpty(val)) {us.setTarget_date(val);}							
						
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(p-1));
								}
								us.setScope(val);
							}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) {
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(p-1));
								}
								us.setCompleted(val);
							}
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) {us.setUnit_fk(val);}
							
							//val = formatter.formatCellValue(row.getCell(p++)).trim();
							//val = formatter.formatCellValue(row.getCell(p++),evaluator,cfEvaluator).trim();
							val = getCellTypeDateValue(row.getCell(p++));
							if(!StringUtils.isEmpty(val)) { us.setStart_date(val);}	
							
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) {us.setShifting_status_fk(val);}
							
							//val = formatter.formatCellValue(row.getCell(p++)).trim();
							//val = formatter.formatCellValue(row.getCell(p++),evaluator,cfEvaluator).trim();
							val = getCellTypeDateValue(row.getCell(p++));
							if(!StringUtils.isEmpty(val)) { us.setShifting_completion_date(val);}
						
							val = formatter.formatCellValue(row.getCell(p++)).trim();
							if(!StringUtils.isEmpty(val)) {us.setRemarks(val);}								
						
							us.setStart_date(DateParser.parse(us.getStart_date()));
							us.setShifting_completion_date(DateParser.parse(us.getShifting_completion_date()));
							us.setCreated_by_user_id_fk(userId);
							us.setPlanned_completion_date(DateParser.parse(us.getPlanned_completion_date()));
							us.setIdentification(DateParser.parse(us.getIdentification()));
				
						List<UtilityShifting> pObjList = new ArrayList<UtilityShifting>();
						XSSFSheet laComercialDetailsSheet = workbook.getSheetAt(1);
						XSSFRow comDetails = laComercialDetailsSheet.getRow(1);
					
						if(comDetails != null){
							for(int j = 1; j <= laComercialDetailsSheet.getLastRowNum();j++){
								XSSFRow row2 = laComercialDetailsSheet.getRow(j);
								UtilityShifting pObj = new UtilityShifting();
								if(!StringUtils.isEmpty(row2) && formatter.formatCellValue(row2.getCell(0)).trim().equals(us.getUtility_shifting_id())) {
									val = formatter.formatCellValue(row2.getCell(0)).trim();
									if(!StringUtils.isEmpty(val)) { pObj.setUtility_shifting_id(val);}
									
									val = formatter.formatCellValue(row2.getCell(1)).trim();
									
									if(!StringUtils.isEmpty(val)) { 
										if(val.contains("/")) 
										{
											Date date24 = null;
											String dateString24 = null;
											date24 = formatter3.parse(val);
											dateString24 = formatter2.format(date24);										
											pObj.setProgress_date(dateString24);
											 
										}
										else
										{
										
											Date date24 = null;
											String dateString24 = null;
											date24 = formatter1.parse(val);
											dateString24 = formatter2.format(date24);
											pObj.setProgress_date(dateString24);
											
										}
										
									}
									
//									val = formatter.formatCellValue(row2.getCell(2)).trim();
//									pObj.setProgress_of_work(val);									
									
									pObj.setProgress_date(DateParser.parse(pObj.getProgress_date()));
								
								}
								if(!StringUtils.isEmpty(row2) && formatter.formatCellValue(row2.getCell(0)).trim().equals(us.getUtility_shifting_id())) {
									pObjList.add(pObj);
								}
										
							}
							us.setProcessList(pObjList);
						}

						boolean isNull = us.checkNullOrEmpty();
						if (!isNull && !StringUtils.isEmpty(us.getUtility_type_fk())
								&& !StringUtils.isEmpty(us.getUtility_description())
								&& !StringUtils.isEmpty(us.getImpacted_contract_id_fk())
								&& !StringUtils.isEmpty(us.getRequirement_stage_fk())
								&& !StringUtils.isEmpty(us.getExecution_agency_fk())) {
							ussList.add(us);
						}else {						
							invalidRows = invalidRows + (i-1) +",";
						}
					}
				}
				if(!ussList.isEmpty() && ussList != null && StringUtils.isEmpty(invalidRows)){
					String[] arr  = utilityShiftingService.uploadUtilityShiftingData(ussList,us);
					result[0] = arr[0];
					result[1] = arr[1];
					result[2] = arr[2];
					result[3] = arr[3];
					result[4] = arr[4];
				}
				if(!StringUtils.isEmpty(invalidRows)){					
					String invalidRowsList = invalidRows.substring(0, invalidRows.length() - 1);
					result[0] = "Invalid Rows :"+ invalidRowsList + " <br>Required fields: Execution Agency, HOD, Utility Type, Utility Description, Impacted Contract,Requirement stage ";
				}
				workbook.close();
			}
			}
						
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uploadUtilityShifting() : "+e.getMessage());
			throw new Exception(e);	
		}finally{
		    try{
		        if ( w != null)
		        	w.close( );
		    }catch ( IOException e){
		    	e.printStackTrace();
		    	logger.error("uploadRUtilityShifting() : "+e.getMessage());
		    	throw new Exception(e);
		    }
		}
		
		return result;
	}
	
	private String getCellTypeDateValue(XSSFCell cell) {
		String val = null;
		try {
			if (!StringUtils.isEmpty(cell) && !StringUtils.isEmpty(cell.getRawValue())) {
				CellType type = cell.getCellType();
			    switch (type) {
			        case BOOLEAN:
			            val = String.valueOf(cell.getBooleanCellValue());
			            break;
			        case NUMERIC:
			        	if (DateUtil.isCellDateFormatted(cell)) {
			        		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			        		Date date = cell.getDateCellValue();
			        		val = df.format(date);
	                    } else {
	                    	val = String.valueOf(cell.getNumericCellValue());
				        	if(val.contains("E")){
				        		val = BigDecimal.valueOf(Double.parseDouble(val)).toPlainString();
				        	}
	                    }
			            break;
			        case STRING:
			        	val = cell.getStringCellValue();
			            break;
			        case BLANK:
			        	val = cell.getStringCellValue();
			            break;
			        case ERROR:
			            val = cell.getStringCellValue();
			            break;
			        case _NONE:
			            val = cell.getStringCellValue();
			            break;
					default:
						break;
			    }
			}
		}catch(Exception e) {
			val = null;
		}
	
		return val;
	}
	
	private String getCellDataType(XSSFWorkbook workbook, XSSFCell cell) {
		String val = null;
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); 

		// existing Sheet, Row, and Cell setup
		//workbook.setForceFormulaRecalculation(true);
		try {
			if (!StringUtils.isEmpty(cell) && !StringUtils.isEmpty(cell.getRawValue())) {
				CellType type = cell.getCellType();
			    switch (type) {
			        case BOOLEAN:
			            val = String.valueOf(cell.getBooleanCellValue());
			            break;
			        case NUMERIC:
			        	if (DateUtil.isCellDateFormatted(cell)) {
			        		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			        		Date date = cell.getDateCellValue();
			        		val = df.format(date);
	                    } else {
	                    	val = String.valueOf(cell.getNumericCellValue());
				        	if(val.contains("E")){
				        		val = BigDecimal.valueOf(Double.parseDouble(val)).toPlainString();
				        	}
	                    }
			        	
			       
			            break;
			        case STRING:
			        	try {  
			        		val = cell.getStringCellValue();
			        		NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			        		Number number = format.parse(val);
			        		int d = number.intValue();
			        		val = String.valueOf(d);
			        		if(val.contains("E")){
			        			val = BigDecimal.valueOf(Double.parseDouble(val)).toPlainString();
			        		}
			        	  } catch(NumberFormatException e){  
			        		  val = cell.getStringCellValue();
			        	  }  
			            
			            break;
			        case BLANK:
			        	val = cell.getStringCellValue();
			            break;
			        case ERROR:
			            val = cell.getStringCellValue();
			            break;
			        case _NONE:
			            val = cell.getStringCellValue();
			            break;
					default:
						break;
			    }
			}else if (!StringUtils.isEmpty(cell)) {
				DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
				val = formatter.formatCellValue(cell).trim();
			}
		}catch(Exception e) {
			try {
				 val = cell.getStringCellValue();
			}catch(Exception e1) {
				val = String.valueOf(cell.getNumericCellValue());
			}
			
		}
	
		return val;
	}
	
	@GetMapping(value = "/utility-shifting-template")
	public ResponseEntity<?> utilityShiftingTemplate(HttpServletRequest request, HttpServletResponse response,HttpSession session,RedirectAttributes attributes){
		//ModelAndView view = new ModelAndView(PageConstants.utilityShifting);
		String attributeMsg ="";
		String attributeKey ="";
		try {
			//view.setViewName("redirect:/utilityshifting");
            XSSFWorkbook  workBook = new XSSFWorkbook ();
            XSSFSheet utilityShiftingSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Utility Shifting"));
			XSSFSheet refSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Reference Data"));
			
			
	        workBook.setSheetOrder(utilityShiftingSheet.getSheetName(), 0);
			workBook.setSheetOrder(refSheet.getSheetName(), 1);
		
	        
	        byte[] blueRGB = new byte[]{(byte)0, (byte)176, (byte)240};
	        byte[] yellowRGB = new byte[]{(byte)255, (byte)192, (byte)0};
	        byte[] greenRGB = new byte[]{(byte)146, (byte)208, (byte)80};
	        byte[] redRGB = new byte[]{(byte)255, (byte)0, (byte)0};
	        byte[] whiteRGB = new byte[]{(byte)255, (byte)255, (byte)255};
	        
	        boolean isWrapText = true;boolean isBoldText = true;boolean isItalicText = false; int fontSize = 11;String fontName = "Times New Roman";
	        CellStyle blueStyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle yellowStyle = cellFormating(workBook,yellowRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle greenStyle = cellFormating(workBook,greenRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle redStyle = cellFormating(workBook,redRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle whiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        
	        CellStyle indexWhiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        
	        isWrapText = true;isBoldText = false;isItalicText = false; fontSize = 9;fontName = "Times New Roman";
	        CellStyle sectionStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        
	        
	        
            XSSFRow headingRow = utilityShiftingSheet.createRow(0);
            String headerString = "Project  ^Execution Agency ^HOD ^Utility Type ^Utility Description ^Location ^Custodian ^Identification Date ^Reference No. "
            		+ "^Chainage ^Executed By ^Impacted Contract  ^Requirement stage "
            		+ "^Impacted Element ^Affected Structures  ^Target Date ^Scope ^Completed ^Unit ^Start Date ^Status ^Completetion Date ^Remarks";
            
            String[] firstHeaderStringArr = headerString.split("\\^");
            utilityShiftingSheet.createFreezePane(0,1);
            for (int i = 0; i < firstHeaderStringArr.length; i++) {		        	
	        	Cell cell = headingRow.createCell(i);
		        cell.setCellStyle(greenStyle);
				cell.setCellValue(firstHeaderStringArr[i]);
			}
            
	        /*************************************************************/ 
	         
	        List<UtilityShifting> projectsList = utilityShiftingService.getProjectsListForUtilityShifting(null);
			List<UtilityShifting> utilityTypeList = utilityShiftingService.getUtilityTypeList(null);
			List<UtilityShifting> utilityExecutionAgencyList = utilityShiftingService.getUtilityExecutionAgencyList(null);
			List<UtilityShifting> statusList = utilityShiftingService.getStatusListForUtilityShifting(null);
			List<UtilityShifting> utilityHODList = utilityShiftingService.getHodListForUtilityShifting(null);
			List<UtilityShifting> impactedContractsList = utilityShiftingService.getImpactedContractsListForUtilityShifting(null);
			List<UtilityShifting> reqStageList = utilityShiftingService.getReqStageList(null);
			List<UtilityShifting> impactedElementList = utilityShiftingService.getImpactedElementList(null);
			
			/*List<UtilityShifting> contractsList = utilityShiftingService.getContractsListForUtilityShifting(null);
			List<UtilityShifting> utilityCategoryList = utilityShiftingService.getUtilityCategoryList(null);
			List<UtilityShifting> impactedContractList = utilityShiftingService.getImpactedContractList(null);
			List<UtilityShifting> requirementStageList = utilityShiftingService.getRequirementStageList(null);
			List<UtilityShifting> unitList = utilityShiftingService.getUnitListForUtilityShifting(null);
			List<UtilityShifting> utilityshiftingfiletypeList = utilityShiftingService.getUtilityTypeListForUtilityShifting(null);
			*/
				
	        XSSFRow headerRow = refSheet.createRow(0);
            refSheet.createFreezePane(0,1);
            
            int b = 1;	
            Cell cell = headerRow.createCell(b);
	        cell.setCellStyle(greenStyle);
			cell.setCellValue("Project Name");
			int rowNoRef = 1;			
			XSSFRow row = null;
			for (UtilityShifting obj : projectsList) {
                row = refSheet.createRow(rowNoRef++);	                	                
                cell = row.createCell(b);
				cell.setCellStyle(sectionStyle);
				cell.setCellValue(obj.getProject_name());
			}
			refSheet.setColumnWidth(b, 25 * 200);
			

			int p = 1;

			refSheet.setColumnWidth(b, 25 * 200);
			
			b = b+2;
			cell = headerRow.createCell(b);
	        cell.setCellStyle(greenStyle);
			cell.setCellValue("Execution Agencies");	
			p = 1;
			for (UtilityShifting obj : utilityExecutionAgencyList) {
				row = refSheet.getRow(p++);
				if(row == null) {
	                row = refSheet.createRow(rowNoRef++);
				}
                cell = row.createCell(b);
				cell.setCellStyle(sectionStyle);
				cell.setCellValue(obj.getExecution_agency_fk());
			}
			refSheet.setColumnWidth(b, 25 * 200);
			
			b = b+2;
			cell = headerRow.createCell(b);
	        cell.setCellStyle(greenStyle);
			cell.setCellValue("HOD");	
			p = 1;
			for (UtilityShifting obj : utilityHODList) {
				row = refSheet.getRow(p++);
				if(row == null) {
	                row = refSheet.createRow(rowNoRef++);
				}
                cell = row.createCell(b);
				cell.setCellStyle(sectionStyle);
				cell.setCellValue(obj.getDesignation());
			}
			refSheet.setColumnWidth(b, 25 * 200);
			
			b = b+2;
			cell = headerRow.createCell(b);
	        cell.setCellStyle(greenStyle);
			cell.setCellValue("Utility Type");	
			p = 1;
			for (UtilityShifting obj : utilityTypeList) {
				row = refSheet.getRow(p++);
				if(row == null) {
	                row = refSheet.createRow(rowNoRef++);
				}
                cell = row.createCell(b);
				cell.setCellStyle(sectionStyle);
				cell.setCellValue(obj.getUtility_type_fk());
			}
			refSheet.setColumnWidth(b, 25 * 200);
			
			b = b+2;
			cell = headerRow.createCell(b);
	        cell.setCellStyle(greenStyle);
			cell.setCellValue("Impacted Contract");	
			p = 1;
			for (UtilityShifting obj : impactedContractsList) {
				row = refSheet.getRow(p++);
				if(row == null) {
	                row = refSheet.createRow(rowNoRef++);
				}
                cell = row.createCell(b);
				cell.setCellStyle(sectionStyle);
				cell.setCellValue(obj.getContract_short_name());
			}
			refSheet.setColumnWidth(b, 25 * 200);
			
			b = b+2;
			cell = headerRow.createCell(b);
	        cell.setCellStyle(greenStyle);
			cell.setCellValue("Requirement stage ");	
			p = 1;
			for (UtilityShifting obj : reqStageList) {
				row = refSheet.getRow(p++);
				if(row == null) {
	                row = refSheet.createRow(rowNoRef++);
				}
                cell = row.createCell(b);
				cell.setCellStyle(sectionStyle);
				cell.setCellValue(obj.getRequirement_stage_fk());
			}
			refSheet.setColumnWidth(b, 25 * 200);
			
			b = b+2;
			cell = headerRow.createCell(b);
	        cell.setCellStyle(greenStyle);
			cell.setCellValue("Impacted Element");	
			p = 1;
			for (UtilityShifting obj : impactedElementList) {
				row = refSheet.getRow(p++);
				if(row == null) {
	                row = refSheet.createRow(rowNoRef++);
				}
                cell = row.createCell(b);
				cell.setCellStyle(sectionStyle);
				cell.setCellValue(obj.getImpacted_element());
			}
			refSheet.setColumnWidth(b, 25 * 200);
			
			b = b+2;
			cell = headerRow.createCell(b);
	        cell.setCellStyle(greenStyle);
			cell.setCellValue("Status");	
			p = 1;
			for (UtilityShifting obj : statusList) {
				row = refSheet.getRow(p++);
				if(row == null) {
	                row = refSheet.createRow(rowNoRef++);
				}
                cell = row.createCell(b);
				cell.setCellStyle(sectionStyle);
				cell.setCellValue(obj.getShifting_status_fk());
			}
			refSheet.setColumnWidth(b, 25 * 200);
			
			/*************************************************************/
			
			
        	for(int columnIndex = 0; columnIndex < 29; columnIndex++) {
        		utilityShiftingSheet.setColumnWidth(columnIndex, 25 * 200);
			}
        	
            String fileName = "Utility_Shifting_Template";
            
            try{
            	
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
            	
                
                attributes.addFlashAttribute("success",dataExportSucess);
                attributeKey = "success";
                attributeMsg = dataExportSucess;
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
                attributes.addFlashAttribute("error",dataExportInvalid);
                attributeKey = "error";
                attributeMsg = dataExportInvalid;
            }catch(IOException e){
                //e.printStackTrace();
                attributes.addFlashAttribute("error",dataExportError);
                attributeKey = "error";
                attributeMsg = dataExportError;
            }
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(Map.of(attributeKey,attributeMsg));
	}
	
}
