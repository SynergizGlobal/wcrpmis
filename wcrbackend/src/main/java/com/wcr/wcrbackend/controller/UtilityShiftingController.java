package com.wcr.wcrbackend.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.DTO.UtilityShiftingPaginationObject;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.entity.User;
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
	public Map<String,List<UtilityShifting>> addUtilityShiftingForm(@RequestBody UtilityShifting obj,HttpSession session) {
		Map<String,List<UtilityShifting>> map = new HashMap<>();
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
	
	@PostMapping(value="/addUtilityShifting")
	public boolean addUtilityShifting(@RequestBody UtilityShifting obj,HttpSession session) {
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
		Map<String, List<UtilityShifting>> map = new HashMap<>();
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
	public boolean updateUtilityShifting(@RequestBody UtilityShifting obj,HttpSession session) {
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
	@PostMapping(value = "/export-utility-shifting")
	public ResponseEntity<?> exportUtilityShifting(HttpServletRequest request, HttpServletResponse response,HttpSession session,@RequestBody UtilityShifting dObj,RedirectAttributes attributes){
		//ModelAndView view = new ModelAndView(PageConstants.utilityShifting);
		List<UtilityShifting> dataList = new ArrayList<UtilityShifting>();
		
		List<UtilityShifting> subList = new ArrayList<UtilityShifting>();
		
		try {
			//view.setViewName("redirect:/utilityshifting");
			User uObj = (User) session.getAttribute("user");
			dObj.setUser_type_fk(uObj.getUserTypeFk());
			dObj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			dObj.setUser_id(uObj.getUserId());
			dataList =   utilityShiftingService.getUtilityShiftingList(dObj);
			
						
			if(dataList != null && dataList.size() > 0){
	            XSSFWorkbook  workBook = new XSSFWorkbook ();
	            XSSFSheet utilityShiftingSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Utility Shifting"));
				XSSFSheet subSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Progress Details"));
				XSSFSheet refSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Reference Data"));
				
				
		        workBook.setSheetOrder(utilityShiftingSheet.getSheetName(), 0);
				workBook.setSheetOrder(subSheet.getSheetName(), 1);
				workBook.setSheetOrder(refSheet.getSheetName(), 2);
			
		        
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
	            String headerString = "Utility ID ^Project ^Execution Agency ^HOD ^Utility Type ^Utility Description ^Location ^Custodian ^Identification Date ^Reference No. "
	            		+ "^Chainage ^Executed By ^Impacted Contract  ^Requirement stage "
	            		+ "^Impacted Element ^Affected Structures  ^Target Date ^Scope ^Completed ^Unit ^Start Date ^Status ^Completetion Date ^Remarks";
	            
	            String[] firstHeaderStringArr = headerString.split("\\^");
	            utilityShiftingSheet.createFreezePane(0,1);
	            for (int i = 0; i < firstHeaderStringArr.length; i++) {		        	
		        	Cell cell = headingRow.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(firstHeaderStringArr[i]);
				}
	            
				XSSFRow headingRow1 = subSheet.createRow(0);
	            String headerString1 = "Utility ID ^Progress Date ^Progress Of Work";
	            
	            String[] secondHeaderStringArr = headerString1.split("\\^");
	            subSheet.createFreezePane(0,1);
	            for (int i = 0; i < secondHeaderStringArr.length; i++) {		        	
		        	Cell cell = headingRow1.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(secondHeaderStringArr[i]);
				}
				
			
				 short rowNo5 = 1;
				 for (UtilityShifting rDetails : dataList) { 
					String utility_shifting_id = rDetails.getUtility_shifting_id();
					subList = utilityShiftingService.getRDetailsList(utility_shifting_id);
						
					 for (UtilityShifting obj : subList) {
			                XSSFRow row = subSheet.createRow(rowNo5);
			                int b = 0;
			                
			                Cell cell1 = row.createCell(b++);
							cell1.setCellStyle(sectionStyle);
							cell1.setCellValue(obj.getUtility_shifting_id());
							
			                cell1 = row.createCell(b++);
							cell1.setCellStyle(sectionStyle);
							cell1.setCellValue(obj.getProgress_date());
							
			                cell1 = row.createCell(b++);
							cell1.setCellStyle(sectionStyle);
							cell1.setCellValue(obj.getProgress_of_work());
							
			               
							rowNo5++;
					    }
			       }
				 short rowNo = 1;
		         for (UtilityShifting obj : dataList) {
					
	                XSSFRow row = utilityShiftingSheet.createRow(rowNo);
	                int c = 0;
	               
	                Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getUtility_shifting_id());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getProject_name());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getWork_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getExecution_agency_fk());
					
					/*
					 * String hod = ""; if(!StringUtils.isEmpty(obj.getHod_user_id_fk())) {hod =
					 * obj.getHod_user_id_fk() +" - "+obj.getUser_name();}
					 */
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDesignation());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getUtility_type_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getUtility_description());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLocation_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCustodian());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getIdentification());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getReference_number());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getChainage());	
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getExecuted_by());
					
				    cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRequirement_stage_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getImpacted_element());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getAffected_structures());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getPlanned_completion_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getScope());									
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCompleted());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getUnit_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getStart_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getShifting_status_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getShifting_completion_date());
									
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRemarks());
					
	                rowNo++;
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
	        	for(int columnIndex = 0; columnIndex < 38; columnIndex++) {
	        		subSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	        	
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
	            Date date = new Date();
	            String fileName = "Utility_Shifting_"+dateFormat.format(date);
	            
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(Map.of("success",dataExportSucess));
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
		
	
}
