package com.wcr.wcrbackend.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
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

import com.wcr.wcrbackend.DTO.FileFormatModel;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.DTO.LandAquisationPaginationObject;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.IFormsHistoryDao;
import com.wcr.wcrbackend.service.ILandAquisitionService;
import com.wcr.wcrbackend.service.IUserService;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/land-acquisition")
public class LandAquisitionController {

	@Autowired
	private ILandAquisitionService landAquisitionService;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IFormsHistoryDao formHistoryDao;

	Logger logger = Logger.getLogger(LandAquisitionController.class);
	
	@Value("${template.upload.formatError}")
	public String uploadformatError;
	
	@PostMapping("/ajax/get-land-acquisition")
	public ResponseEntity<?> getLandAquisition(@RequestBody LandAcquisition obj, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		String json2 = null;
		String userId = null;
		String userName = null;
		try {
			userId = (String) session.getAttribute("USER_ID");
			userName = (String) session.getAttribute("USER_NAME");

			// Fetch the page number from client
			Integer pageNumber = 0;
			Integer pageDisplayLength = 0;
			if (null != request.getParameter("iDisplayStart")) {
				pageDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));
				pageNumber = (Integer.valueOf(request.getParameter("iDisplayStart")) / pageDisplayLength) + 1;
			}
			// Fetch search parameter
			String searchParameter = request.getParameter("sSearch");

			// Fetch Page display length
			pageDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));

			List<LandAcquisition> dataList = new ArrayList<LandAcquisition>();

			// Here is server side pagination logic. Based on the page number you could make
			// call
			// to the data base create new list and send back to the client. For demo I am
			// shuffling
			// the same list to show data randomly
			int startIndex = 0;
			int offset = pageDisplayLength;

			if (pageNumber == 1) {
				startIndex = 0;
				offset = pageDisplayLength;
				dataList = createPaginationData(startIndex, offset, obj, searchParameter, session);
			} else {
				startIndex = (pageNumber * offset) - offset;
				offset = pageDisplayLength;
				dataList = createPaginationData(startIndex, offset, obj, searchParameter, session);
			}

			// Search functionality: Returns filtered list based on search parameter
			// lasList = getListBasedOnSearchParameter(searchParameter,lasList);

			int totalRecords = getTotalRecords(obj, searchParameter, session);

			LandAquisationPaginationObject personJsonObject = new LandAquisationPaginationObject();
			// Set Total display record
			personJsonObject.setiTotalDisplayRecords(totalRecords);
			// Set Total record
			personJsonObject.setiTotalRecords(totalRecords);
			personJsonObject.setAaData(dataList);
			return ResponseEntity.ok(personJsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}

	/**
	 * @param searchParameter
	 * @param activity
	 * @return
	 */
	public int getTotalRecords(LandAcquisition obj, String searchParameter, HttpSession session) {
		int totalRecords = 0;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			totalRecords = landAquisitionService.getTotalRecords(obj, searchParameter);
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
	public List<LandAcquisition> createPaginationData(int startIndex, int offset, LandAcquisition obj,
			String searchParameter, HttpSession session) {
		List<LandAcquisition> earthWorkList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			earthWorkList = landAquisitionService.getLandAcquisitionList(obj, startIndex, offset, searchParameter);
		} catch (Exception e) {
			logger.error("createPaginationData : " + e.getMessage());
		}
		return earthWorkList;
	}

	@PostMapping("/ajax/getStatussFilterListInLandAcquisition")
	public List<LandAcquisition> getProjectsList(@RequestBody LandAcquisition obj, HttpSession session) {
		List<LandAcquisition> projectsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			projectsList = landAquisitionService.getLandAcquisitionStatusList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getProjectsList : " + e.getMessage());
		}
		return projectsList;
	}

	@PostMapping("/ajax/getVillagesFilterListInLandAcquisition")
	public List<LandAcquisition> getVillagesList(@RequestBody LandAcquisition obj, HttpSession session) {
		List<LandAcquisition> villagesList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			villagesList = landAquisitionService.getLandAcquisitionVillagesList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getVillagesList : " + e.getMessage());
		}
		return villagesList;
	}

	@PostMapping("/ajax/getTypesOfLandsFilterListInLandAcquisition")
	public List<LandAcquisition> getTypesOfLandsList(@RequestBody LandAcquisition obj, HttpSession session) {
		List<LandAcquisition> typesOfLandsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			typesOfLandsList = landAquisitionService.getLandAcquisitionTypesOfLandsList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getTypesOfLandsList : " + e.getMessage());
		}
		return typesOfLandsList;
	}

	@PostMapping("/ajax/getSubCategoryFilterListInLandAcquisition")
	public List<LandAcquisition> getSubCategorysList(@RequestBody LandAcquisition obj, HttpSession session) {
		List<LandAcquisition> subCategoryList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			subCategoryList = landAquisitionService.getLandAcquisitionSubCategoryList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getSubCategorysList : " + e.getMessage());
		}
		return subCategoryList;
	}

	@PostMapping("/ajax/getCoordinates")
	public List<LandAcquisition> getCoordinates(@RequestBody LandAcquisition obj, HttpSession session) {
		List<LandAcquisition> objsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());

			objsList = landAquisitionService.getCoordinates(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getCoordinates : " + e.getMessage());
		}
		return objsList;
	}

	@PostMapping("/ajax/getSubCategorysList")
	public List<LandAcquisition> getSubCategoryList(@RequestBody LandAcquisition obj) {
		List<LandAcquisition> objList = null;
		try {
			objList = landAquisitionService.getSubCategoryList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getSubCategoryList() : " + e.getMessage());
		}
		return objList;
	}

	@PostMapping("/ajax/getLandsList")
	public List<LandAcquisition> getLandsList(@RequestBody LandAcquisition obj) {
		List<LandAcquisition> objList = null;
		try {
			objList = landAquisitionService.getLandsList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getLandsList() : " + e.getMessage());
		}
		return objList;
	}

	@PostMapping("/ajax/checkSurveyNumber")
	public boolean checkSurveyNumber(String survey_number, String village_id, String la_id) throws Exception {
		boolean flag = false;
		try {
			flag = landAquisitionService.checkSurveyNumber(survey_number, village_id, la_id);
		} catch (Exception e) {
			logger.error("checkSurveyNumber : " + e.getMessage());
		}
		return flag;
	}

	@PostMapping("/ajax/getLADetails")
	public List<LandAcquisition> getLADetails(@RequestBody LandAcquisition obj, HttpSession session) {
		List<LandAcquisition> LADetails = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			LADetails = landAquisitionService.getLADetails(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getLADetails : " + e.getMessage());
		}
		return LADetails;
	}

	@GetMapping("form/ajax/getStatusList")
	public List<LandAcquisition> getStatusList() throws Exception {
		return landAquisitionService.getStatusList();
	}

	@PostMapping("form/ajax/getProjectsList")
	public List<LandAcquisition> getProjectsList0(@RequestBody LandAcquisition obj, HttpSession session)
			throws Exception {
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		return landAquisitionService.getProjectsList(obj);
	}

	@PostMapping("form/ajax/getLandsListForLAForm")
	public List<LandAcquisition> getLandsListForLAForm(@RequestBody LandAcquisition obj, HttpSession session)
			throws Exception {
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		return landAquisitionService.getLandsListForLAForm(obj);
	}

	@GetMapping("form/ajax/getIssueCatogoriesList")
	public List<LandAcquisition> getIssueCatogoriesList() throws Exception {
		return landAquisitionService.getIssueCatogoriesList();
	}

	@PostMapping("form/ajax/getSubCategorysListForLAForm")
	public List<LandAcquisition> getSubCategorysListForLAForm(@RequestBody LandAcquisition obj, HttpSession session)
			throws Exception {
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		return landAquisitionService.getSubCategorysListForLAForm(obj);
	}

	@GetMapping("form/ajax/getUnitsList")
	public List<LandAcquisition> getUnitsList() throws Exception {
		return landAquisitionService.getUnitsList();
	}

	@GetMapping("form/ajax/getLaFileType")
	public List<LandAcquisition> getLaFileType() throws Exception {
		return landAquisitionService.getLaFileType();
	}

	@GetMapping("form/ajax/getLaLandStatus")
	public List<LandAcquisition> getLaLandStatus() throws Exception {
		return landAquisitionService.getLaLandStatus();
	}

	@PostMapping("form/ajax/getLandAcquisitionForm")
	public LandAcquisition getLandAcquisitionForm(@RequestBody LandAcquisition obj, HttpSession session)
			throws Exception {
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		return landAquisitionService.getLandAcquisitionForm(obj);
	}

	@PostMapping(value = "/export-land-acquisition")
	public void exportLandAcquisition(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestBody LandAcquisition dObj) {
		// ModelAndView view = new ModelAndView(PageConstants.landAcquisition);
		List<LandAcquisition> dataList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> privateIRAList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> privateValList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> privateLandList = new ArrayList<LandAcquisition>();

		List<LandAcquisition> govList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> forestList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> railwayList = new ArrayList<LandAcquisition>();
		try {

			User uObj = (User) session.getAttribute("user");
			dObj.setUser_type_fk(uObj.getUserTypeFk());
			dObj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			dObj.setUser_id(uObj.getUserId());

			// view.setViewName("redirect:/land-acquisition");
			dataList = landAquisitionService.getLandAcquisitionList(dObj);

			if (dataList != null && dataList.size() > 0) {
				XSSFWorkbook workBook = new XSSFWorkbook();
				XSSFSheet Landsheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Land Acquisition"));
				XSSFSheet privateIRASheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Private (Indian Railway Act)"));
				XSSFSheet privateValSheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Private Land valuation"));
				XSSFSheet privateLandSheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Private Land Acquisition"));
				XSSFSheet govSheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Government Land Acquisition"));
				XSSFSheet forestSheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Forest Land Acquisition"));
				XSSFSheet railwaySheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Railway Land Aquisition"));

				workBook.setSheetOrder(Landsheet.getSheetName(), 0);
				workBook.setSheetOrder(privateIRASheet.getSheetName(), 1);
				workBook.setSheetOrder(privateValSheet.getSheetName(), 2);
				workBook.setSheetOrder(privateLandSheet.getSheetName(), 3);
				workBook.setSheetOrder(govSheet.getSheetName(), 4);
				workBook.setSheetOrder(forestSheet.getSheetName(), 5);
				workBook.setSheetOrder(railwaySheet.getSheetName(), 6);

				byte[] blueRGB = new byte[] { (byte) 0, (byte) 176, (byte) 240 };
				byte[] yellowRGB = new byte[] { (byte) 255, (byte) 192, (byte) 0 };
				byte[] greenRGB = new byte[] { (byte) 146, (byte) 208, (byte) 80 };
				byte[] redRGB = new byte[] { (byte) 255, (byte) 0, (byte) 0 };
				byte[] whiteRGB = new byte[] { (byte) 255, (byte) 255, (byte) 255 };

				boolean isWrapText = true;
				boolean isBoldText = true;
				boolean isItalicText = false;
				int fontSize = 11;
				String fontName = "Times New Roman";
				CellStyle blueStyle = cellFormating(workBook, blueRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
				CellStyle yellowStyle = cellFormating(workBook, yellowRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
				CellStyle greenStyle = cellFormating(workBook, greenRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
				CellStyle redStyle = cellFormating(workBook, redRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
				CellStyle whiteStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

				CellStyle indexWhiteStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

				isWrapText = true;
				isBoldText = false;
				isItalicText = false;
				fontSize = 9;
				fontName = "Times New Roman";
				CellStyle sectionStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

				XSSFRow headingRow = Landsheet.createRow(0);
				String headerString = "Work ID^LA_ID^Survey Number^Type of Land^Sub Category of Land^Area^Area to be Acquired^Area Acquired^Land Status^Chainage From"
						+ "^Chainage To^Village^Taluka^Latitude^Longitude^Dy SLR^SDO^Collector^Proposal submission Date to collector^JM Fee Letter received Date^JM Fee Amount^JM Fee Paid Date^"
						+ "JM Start Date^JM Completion Date^JM Sheet Date to SDO^JM Remarks^JM Approval^Special Feature^Remarks^Issues";

				String[] firstHeaderStringArr = headerString.split("\\^");

				for (int i = 0; i < firstHeaderStringArr.length; i++) {
					Cell cell = headingRow.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(firstHeaderStringArr[i]);
				}

				XSSFRow headingRow1 = privateIRASheet.createRow(0);
				String headerString1 = "LA_ID^Collector^Submission of Proposal to GM.^Approval of GM^Draft Letter to CE/Con for Approval^"
						+ "Date of Approval of CE/Construction^Date of Uploading of  Gazette notification^Publication in gazette^Date of Proposal to DC for nomination^"
						+ "Date of Nomination of competent Authority.^Draft Letter to CE/Con for Approval^Date of Approval of CE/Construction^"
						+ "Date of Uploading of  Gazette notification^Publication in gazette^Date of Submission of draft notification to CALA^Approval of CALA^"
						+ "Draft Letter to CE/Con for Approval^Date of Approval of CE/Construction^Date of Uploading of  Gazette notification^Publication in gazette^"
						+ "Publication in 2 Local Newspapers^Pasting of notification in villages^Receipt of Grievances^Disposal of Grievances^"
						+ "Date of Submission of draft notification to CALA^Approval of CALA^Draft Letter to CE/Con for Approval^Date of Approval of CE/Construction^"
						+ "Date of Uploading of  Gazette notification^Publication in gazette^Publication of notice in 2 Local News papers ^"
						+ "Date of Submission of draft notification to CALA^Approval of CALA^Draft Letter to CE/Con for Approval^Date of Approval of CE/Construction^"
						+ "Date of Uploading of  Gazette notification^Publication in gazette^Publication of notice in 2 Local News papers";

				String[] secondHeaderStringArr = headerString1.split("\\^");

				for (int i = 0; i < secondHeaderStringArr.length; i++) {
					Cell cell = headingRow1.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(secondHeaderStringArr[i]);
				}

				XSSFRow headingRow2 = privateValSheet.createRow(0);
				String headerString2 = "LA_ID^Forest Tree Survey^Forest Tree Valuation^Horticulture Tree Survey^Horticulture Tree Valuation^Structure Survey^"
						+ "Structure Valuation^Borewell Survey^Borewell Valuation^Date of RFP to ADTP^Date of Rate Fixation of Land^"
						+ "SDO demand for payment^Date of Approval for Payment^Payment Amount^Payment Date";

				String[] thirdHeaderStringArr = headerString2.split("\\^");

				for (int i = 0; i < thirdHeaderStringArr.length; i++) {
					Cell cell = headingRow2.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(thirdHeaderStringArr[i]);
				}

				XSSFRow headingRow3 = privateLandSheet.createRow(0);
				String headerString3 = "LA_ID^Name of Owner^Basic Rate^100% Solatium^Extra 25%^Total Rate/m2^Land Compensation^Agriculture tree nos^"
						+ "Agriculture tree rate^Agriculture tree compensation^Forest tree nos^Forest tree rate^Forest tree compensation^Structure compensation^"
						+ "Borewell compensation^Total Compensation^Consent from Owner^Legal Search Report^Date of Registration^Date of Possession";

				String[] fourthHeaderStringArr = headerString3.split("\\^");

				for (int i = 0; i < fourthHeaderStringArr.length; i++) {
					Cell cell = headingRow3.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(fourthHeaderStringArr[i]);
				}

				XSSFRow headingRow4 = govSheet.createRow(0);
				String headerString4 = "LA_ID^Proposal Submission^Valuation Date^Letter for Payment^Amount Demanded^"
						+ "Approval for Payment^Payment date^Amount Paid^Possession Date";

				String[] fifthHeaderStringArr = headerString4.split("\\^");

				for (int i = 0; i < fifthHeaderStringArr.length; i++) {
					Cell cell = headingRow4.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(fifthHeaderStringArr[i]);
				}

				XSSFRow headingRow5 = forestSheet.createRow(0);
				String headerString5 = "LA_ID^On line Submission^Submission Date to DyCFO^Submission Date to CCF Thane^Submission Date to Nodal Officer/CCF Nagpur^"
						+ "Submission Date to Revenue Secretary Mantralaya^Submission Date to Regional Office Nagpur^Date of Approval by Regional Office Nagpur^"
						+ "Valuation by DyCFO^Demanded Amount^Approval for Payment^Payment Date^Payment Amount^Possession Date";

				String[] sixthHeaderStringArr = headerString5.split("\\^");

				for (int i = 0; i < sixthHeaderStringArr.length; i++) {
					Cell cell = headingRow5.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(sixthHeaderStringArr[i]);
				}

				XSSFRow headingRow6 = railwaySheet.createRow(0);
				String headerString6 = "LA_ID^On line Submission^Submission Date to DyCFO^Submission Date to CCF Thane^Submission Date to Nodal Officer/CCF Nagpur^"
						+ "Submission Date to Revenue Secretary Mantralaya^Submission Date to Regional Office Nagpur^Date of Approval by Regional Office Nagpur^"
						+ "Valuation by DyCFO^Demanded Amount^Approval for Payment^Payment Date^Payment Amount^Possession Date";

				String[] seventhHeaderStringArr = headerString6.split("\\^");

				for (int i = 0; i < seventhHeaderStringArr.length; i++) {
					Cell cell = headingRow6.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(seventhHeaderStringArr[i]);
				}

				short rowNo3 = 1;
				for (LandAcquisition privateIRA : dataList) {
					String la_id = privateIRA.getLa_id();
					privateIRAList = landAquisitionService.geprivateIRAList(la_id);
					/*
					 * if(privateIRAList.size()< 1) { int a = 0; XSSFRow row =
					 * privateIRASheet.createRow(rowNo3); for(int k = 0;k <
					 * secondHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * privateIRASheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(secondHeaderStringArr.length - 1 ))); break; }
					 */

					for (LandAcquisition obj : privateIRAList) {
						XSSFRow row = privateIRASheet.createRow(rowNo3);
						int a = 0;

						Cell cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getLa_id_fk());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPrivate_ira_collector());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getSubmission_of_proposal_to_GM());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getApproval_of_GM());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_rp());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_rp());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_rp());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_rp());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_proposal_to_DC_for_nomination());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_nomination_of_competenta_authority());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_ca());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_ca());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_ca());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_ca());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_submission_of_draft_notification_to_CALA());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getApproval_of_CALA_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_2_local_news_papers_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPasting_of_notification_in_villages_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getReceipt_of_grievances());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDisposal_of_grievances());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_submission_of_draft_notification_to_CALA_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getApproval_of_CALA_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_of_notice_in_2_local_news_papers_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_submission_of_draft_notification_to_CALA_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getApproval_of_CALA_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_of_notice_in_2_local_news_papers_20f());

						rowNo3++;
					}
				}

				short rowNo2 = 1;
				for (LandAcquisition privateVal : dataList) {
					String la_id = privateVal.getLa_id();
					privateValList = landAquisitionService.getPrivateValList(la_id);
					/*
					 * if(privateValList.size()< 1) { int a = 0; XSSFRow row =
					 * privateValSheet.createRow(rowNo2); for(int k = 0;k <
					 * thirdHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * privateValSheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(thirdHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : privateValList) {
						XSSFRow row = privateValSheet.createRow(rowNo2);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_tree_survey());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_tree_valuation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getHorticulture_tree_survey());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getHorticulture_tree_valuation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getStructure_survey());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getStructure_valuation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getBorewell_survey());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getBorewell_valuation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_rfp_to_adtp());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_rate_fixation_of_land());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getSdo_demand_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_approval_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getPayment_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getPrivate_payment_date());

						rowNo2++;
					}
				}

				short rowNo4 = 1;
				for (LandAcquisition privateLand : dataList) {
					String la_id = privateLand.getLa_id();
					privateLandList = landAquisitionService.getPrivateLandList(la_id);
					/*
					 * if(privateLandList.size()< 1) { int a = 0; XSSFRow row =
					 * privateLandSheet.createRow(rowNo4); for(int k = 0;k <
					 * fourthHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * privateLandSheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(fourthHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : privateLandList) {
						XSSFRow row = privateLandSheet.createRow(rowNo4);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getName_of_the_owner());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getBasic_rate());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getHundred_percent_Solatium());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getExtra_25_percent());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getTotal_rate_divide_m2());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLand_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(String.valueOf(obj.getAgriculture_tree_nos()));

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getAgriculture_tree_rate());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getAgriculture_tree_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(String.valueOf(obj.getForest_tree_nos()));

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_tree_rate());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_tree_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getStructure_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getBorewell_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getTotal_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getConsent_from_owner());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLegal_search_report());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_registration());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_possession());

						rowNo4++;
					}
				}

				short rowNo5 = 1;
				for (LandAcquisition gov : dataList) {
					String la_id = gov.getLa_id();
					govList = landAquisitionService.getGovList(la_id);
					/*
					 * if(govList.size()< 1) { int a = 0; XSSFRow row = govSheet.createRow(rowNo5);
					 * for(int k = 0;k < fifthHeaderStringArr.length;k++) { Cell cell2 =
					 * row.createCell(a++); cell2.setCellStyle(whiteStyle);
					 * cell2.setCellValue("No Data"); } govSheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(fifthHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : govList) {
						XSSFRow row = govSheet.createRow(rowNo5);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getProposal_submission());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getValuation_date());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLetter_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getAmount_demanded());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getApproval_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getPayment_date());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getAmount_paid());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getPossession_date());

						rowNo5++;
					}
				}

				short rowNo6 = 1;
				for (LandAcquisition forest : dataList) {
					String la_id = forest.getLa_id();
					forestList = landAquisitionService.getForestList(la_id);
					/*
					 * if(forestList.size()< 1) { int a = 0; XSSFRow row =
					 * forestSheet.createRow(rowNo6); for(int k = 0;k <
					 * sixthHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * forestSheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(sixthHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : forestList) {
						XSSFRow row = forestSheet.createRow(rowNo6);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_online_submission());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_dycfo());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_ccf_thane());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_nodal_officer());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_revenue_secretary_mantralaya());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_regional_office_nagpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_date_of_approval_by_regional_office_nagpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_valuation_by_dycfo());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_demanded_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_approval_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_payment_date());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_payment_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_possession_date());

						rowNo6++;
					}
				}

				short rowNo7 = 1;
				for (LandAcquisition railway : dataList) {
					String la_id = railway.getLa_id();
					railwayList = landAquisitionService.getRailwayList(la_id);
					/*
					 * if(railwayList.size()< 1) { int a = 0; XSSFRow row =
					 * railwaySheet.createRow(rowNo7); for(int k = 0;k <
					 * seventhHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * railwaySheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(seventhHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : railwayList) {
						XSSFRow row = railwaySheet.createRow(rowNo7);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_online_submission());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_DyCFO());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_CCF_Thane());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_nodal_officer_CCF_Nagpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_revenue_secretary_mantralaya());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_regional_office_nagpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_date_of_approval_by_Rregional_Office_agpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_valuation_by_DyCFO());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_demanded_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_approval_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_payment_date());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_payment_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_possession_date());

						rowNo7++;
					}
				}
				short rowNo = 1;
				for (LandAcquisition obj : dataList) {
					/*
					 * if(dataList.size()< 1) { int a = 0; XSSFRow row = Landsheet.createRow(rowNo);
					 * for(int k = 0;k < firstHeaderStringArr.length;k++) { Cell cell2 =
					 * row.createCell(a++); cell2.setCellStyle(whiteStyle);
					 * cell2.setCellValue("No Data"); } Landsheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(firstHeaderStringArr.length - 1 ))); break; }
					 */
					XSSFRow row = Landsheet.createRow(rowNo);
					int c = 0;

					Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getWork_id_fk() + " - " + obj.getWork_short_name());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLa_id());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSurvey_number());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getType_of_land());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSub_category_of_land());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getArea_of_plot());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getArea_to_be_acquired());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getArea_acquired());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLa_land_status_fk());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getChainage_from());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getChainage_to());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getVillage());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getTaluka());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLatitude());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLongitude());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDy_slr());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSdo());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCollector());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getProposal_submission_date_to_collector());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_fee_letter_received_date());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_fee_amount());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_fee_paid_date());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_start_date());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_completion_date());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_sheet_date_to_sdo());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_remarks());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_approval());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSpecial_feature());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRemarks());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getIssues());

					rowNo++;
				}

				for (int columnIndex = 0; columnIndex < 29; columnIndex++) {
					Landsheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 38; columnIndex++) {
					privateIRASheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 16; columnIndex++) {
					privateValSheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 21; columnIndex++) {
					privateLandSheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 21; columnIndex++) {
					govSheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 16; columnIndex++) {
					forestSheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 16; columnIndex++) {
					railwaySheet.setColumnWidth(columnIndex, 25 * 200);
				}

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
				Date date = new Date();
				String fileName = "Land_Aquisition" + dateFormat.format(date);

				try {
					/*
					 * FileOutputStream fos = new FileOutputStream(fileDirectory +fileName+".xls");
					 * workBook.write(fos); fos.flush();
					 */

					response.setContentType("application/.csv");
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setContentType("application/vnd.ms-excel");
					// add response header
					response.addHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

					// copies all bytes from a file to an output stream
					workBook.write(response.getOutputStream()); // Write workbook to response.
					workBook.close();
					// flushes output stream
					response.getOutputStream().flush();

					// attributes.addFlashAttribute("success",dataExportSucess);
					// response.setContentType("application/vnd.ms-excel");
					// response.setHeader("Content-Disposition", "attachment;
					// filename=filename.xls");
					// XSSFWorkbook workbook = new XSSFWorkbook ();
					// ...
					// Now populate workbook the usual way.
					// ...
					// workbook.write(response.getOutputStream()); // Write workbook to response.
					// workbook.close();
				} catch (FileNotFoundException e) {
					// e.printStackTrace();
					// attributes.addFlashAttribute("error",dataExportInvalid);
				} catch (IOException e) {
					// e.printStackTrace();
					// attributes.addFlashAttribute("error",dataExportError);
				}
			} else {
				// attributes.addFlashAttribute("error",dataExportNoData);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private CellStyle cellFormating(XSSFWorkbook workBook, byte[] rgb, HorizontalAlignment hAllign,
			VerticalAlignment vAllign, boolean isWrapText, boolean isBoldText, boolean isItalicText, int fontSize,
			String fontName) {
		CellStyle style = workBook.createCellStyle();
		// Setting Background color
		// style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		if (style instanceof XSSFCellStyle) {
			XSSFCellStyle xssfcellcolorstyle = (XSSFCellStyle) style;
			xssfcellcolorstyle.setFillForegroundColor(new XSSFColor(rgb, null));
		}
		// style.setFillPattern(FillPatternType.ALT_BARS);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setAlignment(hAllign);
		style.setVerticalAlignment(vAllign);
		style.setWrapText(isWrapText);

		org.apache.poi.ss.usermodel.Font font = workBook.createFont();
		// font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		font.setFontHeightInPoints((short) fontSize);
		font.setFontName(fontName); // "Times New Roman"

		font.setItalic(isItalicText);
		font.setBold(isBoldText);
		// Applying font to the style
		style.setFont(font);

		return style;
	}

	private CellStyle cellFormatingColor(XSSFWorkbook workBook, byte[] rgb, HorizontalAlignment hAllign,
			VerticalAlignment vAllign, boolean isWrapText, boolean isBoldText, boolean isItalicText, int fontSize,
			String fontName) {
		CellStyle style = workBook.createCellStyle();
		// Setting Background color
		// style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		if (style instanceof XSSFCellStyle) {
			XSSFCellStyle xssfcellcolorstyle = (XSSFCellStyle) style;
			xssfcellcolorstyle.setFillForegroundColor(new XSSFColor(rgb, null));
		}
		// style.setFillPattern(FillPatternType.ALT_BARS);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);
		style.setAlignment(hAllign);
		style.setVerticalAlignment(vAllign);
		style.setWrapText(isWrapText);

		org.apache.poi.ss.usermodel.Font font = workBook.createFont();
		font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
		font.setFontHeightInPoints((short) fontSize);
		font.setFontName(fontName); // "Times New Roman"

		font.setItalic(isItalicText);
		font.setBold(isBoldText);
		// Applying font to the style
		style.setFont(font);

		return style;
	}

	@PostMapping("/export-land")
	public void exportLand(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestBody LandAcquisition dObj) {
		// ModelAndView view = new ModelAndView(PageConstants.landAcquisition);
		List<LandAcquisition> dataList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> privateIRAList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> privateValList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> privateLandList = new ArrayList<LandAcquisition>();

		List<LandAcquisition> govList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> forestList = new ArrayList<LandAcquisition>();
		List<LandAcquisition> railwayList = new ArrayList<LandAcquisition>();
		try {

			User uObj = (User) session.getAttribute("user");
			dObj.setUser_type_fk(uObj.getUserTypeFk());
			dObj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			dObj.setUser_id(uObj.getUserId());

			// view.setViewName("redirect:/land-acquisition");
			dataList = landAquisitionService.getLandAcquisitionList(dObj);

			if (dataList != null && dataList.size() > 0) {
				XSSFWorkbook workBook = new XSSFWorkbook();
				XSSFSheet Instruction = workBook.createSheet(WorkbookUtil.createSafeSheetName("Instruction"));
				XSSFSheet Working = workBook.createSheet(WorkbookUtil.createSafeSheetName("Working"));
				XSSFSheet Index = workBook.createSheet(WorkbookUtil.createSafeSheetName("Index"));

				XSSFSheet Landsheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Land Acquisition"));
				XSSFSheet privateIRASheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Private (Indian Railway Act)"));
				XSSFSheet privateValSheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Private Land valuation"));
				XSSFSheet privateLandSheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Private Land Acquisition"));
				XSSFSheet govSheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Government Land Acquisition"));
				XSSFSheet forestSheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Forest Land Acquisition"));
				XSSFSheet railwaySheet = workBook
						.createSheet(WorkbookUtil.createSafeSheetName("Railway Land Aquisition"));

				workBook.setSheetOrder(Instruction.getSheetName(), 0);
				workBook.setSheetOrder(Index.getSheetName(), 1);
				workBook.setSheetOrder(Working.getSheetName(), 2);
				workBook.setSheetOrder(Landsheet.getSheetName(), 3);
				workBook.setSheetOrder(privateIRASheet.getSheetName(), 4);
				workBook.setSheetOrder(privateValSheet.getSheetName(), 5);
				workBook.setSheetOrder(privateLandSheet.getSheetName(), 6);
				workBook.setSheetOrder(govSheet.getSheetName(), 7);
				workBook.setSheetOrder(forestSheet.getSheetName(), 8);
				workBook.setSheetOrder(railwaySheet.getSheetName(), 9);

				workBook.setSheetHidden(1, true);
				workBook.setSheetHidden(2, true);

				CellStyle lockedStyle = workBook.createCellStyle();
				lockedStyle.setLocked(true);

				byte[] blueRGB = new byte[] { (byte) 0, (byte) 176, (byte) 240 };
				byte[] yellowRGB = new byte[] { (byte) 255, (byte) 255, (byte) 0 };
				byte[] greenRGB = new byte[] { (byte) 146, (byte) 208, (byte) 80 };
				byte[] redRGB = new byte[] { (byte) 255, (byte) 0, (byte) 0 };
				byte[] whiteRGB = new byte[] { (byte) 255, (byte) 255, (byte) 255 };

				boolean isWrapText = true;
				boolean isBoldText = true;
				boolean isItalicText = false;
				int fontSize = 11;
				String fontName = "Times New Roman";
				CellStyle blueStyle = cellFormating(workBook, blueRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
				CellStyle yellowStyle = cellFormating(workBook, yellowRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, true, isItalicText, fontSize, fontName);
				CellStyle greenStyle = cellFormating(workBook, greenRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
				CellStyle redStyle = cellFormating(workBook, redRGB, HorizontalAlignment.CENTER,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
				CellStyle whiteStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT,
						VerticalAlignment.CENTER, isWrapText, false, isItalicText, fontSize, fontName);

				CellStyle whiteStyleRedFont = cellFormatingColor(workBook, whiteRGB, HorizontalAlignment.LEFT,
						VerticalAlignment.CENTER, isWrapText, false, isItalicText, fontSize, fontName);

				CellStyle indexWhiteStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

				isWrapText = true;
				isBoldText = false;
				isItalicText = false;
				fontSize = 9;
				fontName = "Times New Roman";
				CellStyle sectionStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT,
						VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

				XSSFRow headingInstructionRow = Instruction.createRow(0);
				Cell cell5 = headingInstructionRow.createCell(0);
				Instruction.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
				cell5.setCellStyle(whiteStyleRedFont);
				cell5.setCellValue("Note : User Should not Edit highlighed fields Marked in Red Color");
				String headerInstructionString = "SN^Sheet Name^Description^Column Name";
				String[] firstInstrHeaderStringArr = headerInstructionString.split("\\^");

				XSSFRow headingInstrRowHead = Instruction.createRow(1);

				for (int i = 0; i < firstInstrHeaderStringArr.length; i++) {
					Cell cell = headingInstrRowHead.createCell(i);
					cell.setCellStyle(yellowStyle);
					cell.setCellValue(firstInstrHeaderStringArr[i]);
				}

				XSSFRow headingInstrRow1 = Instruction.createRow(2);

				Cell cell11 = headingInstrRow1.createCell(0);
				cell11.setCellStyle(whiteStyle);
				cell11.setCellValue(1);

				Cell cell21 = headingInstrRow1.createCell(1);
				cell21.setCellStyle(whiteStyle);
				cell21.setCellValue("Land Identification");

				Cell cell4 = headingInstrRow1.createCell(2);
				cell4.setCellStyle(whiteStyleRedFont);
				cell4.setCellValue("Work ID along with work name (DO NOT MODIFY)");

				Cell cell3 = headingInstrRow1.createCell(3);
				cell3.setCellStyle(redStyle);
				cell3.setCellValue("Work ID");

				XSSFRow headingInstrRow2v = Instruction.createRow(3);

				Cell cell51v = headingInstrRow2v.createCell(0);
				cell51v.setCellStyle(whiteStyle);
				cell51v.setCellValue(2);

				Cell cell6v = headingInstrRow2v.createCell(1);
				cell6v.setCellStyle(whiteStyle);
				cell6v.setCellValue("Land Identification");

				Cell cell8v = headingInstrRow2v.createCell(2);
				cell8v.setCellStyle(whiteStyleRedFont);
				cell8v.setCellValue("Land Acquisition Unique ID (DO NOT MODIFY)");

				Cell cell7v = headingInstrRow2v.createCell(3);
				cell7v.setCellStyle(redStyle);
				cell7v.setCellValue("LA_ID");

				XSSFRow headingInstrRow2 = Instruction.createRow(4);

				Cell cell51 = headingInstrRow2.createCell(0);
				cell51.setCellStyle(whiteStyle);
				cell51.setCellValue(3);

				Cell cell6 = headingInstrRow2.createCell(1);
				cell6.setCellStyle(whiteStyle);
				cell6.setCellValue("Land Identification");

				Cell cell7 = headingInstrRow2.createCell(2);
				cell7.setCellStyle(whiteStyle);
				cell7.setCellValue("Survey Number");

				Cell cell8 = headingInstrRow2.createCell(3);
				cell8.setCellStyle(whiteStyle);
				cell8.setCellValue("Survey Number");

				XSSFRow headingInstrRow3 = Instruction.createRow(5);

				Cell cell9 = headingInstrRow3.createCell(0);
				cell9.setCellStyle(whiteStyle);
				cell9.setCellValue(4);

				Cell cell10 = headingInstrRow3.createCell(1);
				cell10.setCellStyle(whiteStyle);
				cell10.setCellValue("Land Identification");

				Cell cell12 = headingInstrRow3.createCell(2);
				cell12.setCellStyle(whiteStyleRedFont);
				cell12.setCellValue("INPUT 3: Type of land  (DO NOT MODIFY)    \r\n" + "(i). Private\r\n"
						+ "(ii). Government\r\n" + "(iii). Forest\r\n" + "(iv). Railway");

				Cell cell111 = headingInstrRow3.createCell(3);
				cell111.setCellStyle(redStyle);
				cell111.setCellValue("Type of Land");

				XSSFRow headingInstrRow4 = Instruction.createRow(6);

				Cell cell13 = headingInstrRow4.createCell(0);
				cell13.setCellStyle(whiteStyle);
				cell13.setCellValue(5);

				Cell cell14 = headingInstrRow4.createCell(1);
				cell14.setCellStyle(whiteStyle);
				cell14.setCellValue("Land Identification");

				Cell cell15 = headingInstrRow4.createCell(2);
				cell15.setCellStyle(whiteStyleRedFont);
				cell15.setCellValue("Sub Category of Land (DO NOT MODIFY)");

				Cell cell16 = headingInstrRow4.createCell(3);
				cell16.setCellStyle(redStyle);
				cell16.setCellValue("Sub Category of Land");

				XSSFRow headingInstrRow5 = Instruction.createRow(7);

				Cell cell17 = headingInstrRow5.createCell(0);
				cell17.setCellStyle(whiteStyle);
				cell17.setCellValue(6);

				Cell cell18 = headingInstrRow5.createCell(1);
				cell18.setCellStyle(whiteStyle);
				cell18.setCellValue("Land Identification");

				Cell cell19 = headingInstrRow5.createCell(3);
				cell19.setCellStyle(whiteStyle);
				cell19.setCellValue("Area (Ha.)");

				Cell cell20 = headingInstrRow5.createCell(2);
				cell20.setCellStyle(whiteStyle);
				cell20.setCellValue("Total Area of Plot (Ha.) ");

				XSSFRow headingInstrRow6 = Instruction.createRow(8);

				Cell cell211 = headingInstrRow6.createCell(0);
				cell211.setCellStyle(whiteStyle);
				cell211.setCellValue(7);

				Cell cell22 = headingInstrRow6.createCell(1);
				cell22.setCellStyle(whiteStyle);
				cell22.setCellValue("Land Identification");

				Cell cell23 = headingInstrRow6.createCell(3);
				cell23.setCellStyle(whiteStyle);
				cell23.setCellValue("Area to be Acquired (Ha.)");

				Cell cell24 = headingInstrRow6.createCell(2);
				cell24.setCellStyle(whiteStyle);
				cell24.setCellValue("INPUT 4: \r\n" + "Total Area to be Acquired (Ha.)");

				XSSFRow headingInstrRow7 = Instruction.createRow(9);

				Cell cell25 = headingInstrRow7.createCell(0);
				cell25.setCellStyle(whiteStyle);
				cell25.setCellValue(8);

				Cell cell26 = headingInstrRow7.createCell(1);
				cell26.setCellStyle(whiteStyle);
				cell26.setCellValue("Land Identification");

				Cell cell27 = headingInstrRow7.createCell(3);
				cell27.setCellStyle(whiteStyle);
				cell27.setCellValue("Area Acquired (Ha.)");

				Cell cell28 = headingInstrRow7.createCell(2);
				cell28.setCellStyle(whiteStyle);
				cell28.setCellValue("INPUT 5: \r\n" + "Area Acquired (Ha.)\r\n"
						+ " If Land status is \"Acquired\", then it is treated as \"Area to be Acquired\", else \"enter acquired area\".");

				XSSFRow headingInstrRow8 = Instruction.createRow(10);

				Cell cell29 = headingInstrRow8.createCell(0);
				cell29.setCellStyle(whiteStyle);
				cell29.setCellValue(9);

				Cell cell30 = headingInstrRow8.createCell(1);
				cell30.setCellStyle(whiteStyle);
				cell30.setCellValue("Land Identification");

				Cell cell31 = headingInstrRow8.createCell(3);
				cell31.setCellStyle(whiteStyle);
				cell31.setCellValue("Land Status");

				Cell cell32 = headingInstrRow8.createCell(2);
				cell32.setCellStyle(whiteStyle);
				cell32.setCellValue("INPUT 6: \r\n" + "(i). Acquired\r\n" + "(ii). Yet to be Acquired\r\n"
						+ "(iii). Forest Clearance Required");

				XSSFRow headingInstrRow9 = Instruction.createRow(11);

				Cell cell33 = headingInstrRow9.createCell(0);
				cell33.setCellStyle(whiteStyle);
				cell33.setCellValue(10);

				Cell cell34 = headingInstrRow9.createCell(1);
				cell34.setCellStyle(whiteStyle);
				cell34.setCellValue("Land Identification");

				Cell cell35 = headingInstrRow9.createCell(3);
				cell35.setCellStyle(whiteStyle);
				cell35.setCellValue("Chainage From");

				Cell cell36 = headingInstrRow9.createCell(2);
				cell36.setCellStyle(whiteStyle);
				cell36.setCellValue("INPUT 7: \r\n" + "Chainage from (in Meter.)");

				XSSFRow headingInstrRow10 = Instruction.createRow(12);

				Cell cell37 = headingInstrRow10.createCell(0);
				cell37.setCellStyle(whiteStyle);
				cell37.setCellValue(11);

				Cell cell38 = headingInstrRow10.createCell(1);
				cell38.setCellStyle(whiteStyle);
				cell38.setCellValue("Land Identification");

				Cell cell39 = headingInstrRow10.createCell(3);
				cell39.setCellStyle(whiteStyle);
				cell39.setCellValue("Chainage To");

				Cell cell40 = headingInstrRow10.createCell(2);
				cell40.setCellStyle(whiteStyle);
				cell40.setCellValue("INPUT 8: \r\n" + "Chainage To (in Meter.)");

				XSSFRow headingInstrRow11 = Instruction.createRow(13);

				Cell cell41 = headingInstrRow11.createCell(0);
				cell41.setCellStyle(whiteStyle);
				cell41.setCellValue(12);

				Cell cell42 = headingInstrRow11.createCell(1);
				cell42.setCellStyle(whiteStyle);
				cell42.setCellValue("Land Identification");

				Cell cell43 = headingInstrRow11.createCell(3);
				cell43.setCellStyle(whiteStyle);
				cell43.setCellValue("Village");

				Cell cell44 = headingInstrRow11.createCell(2);
				cell44.setCellStyle(whiteStyle);
				cell44.setCellValue("INPUT 9: \r\n" + "Village Name");

				XSSFRow headingInstrRow12 = Instruction.createRow(14);

				Cell cell45 = headingInstrRow12.createCell(0);
				cell45.setCellStyle(whiteStyle);
				cell45.setCellValue(13);

				Cell cell46 = headingInstrRow12.createCell(1);
				cell46.setCellStyle(whiteStyle);
				cell46.setCellValue("Land Identification");

				Cell cell47 = headingInstrRow12.createCell(3);
				cell47.setCellStyle(whiteStyle);
				cell47.setCellValue("Taluka");

				Cell cell48 = headingInstrRow12.createCell(2);
				cell48.setCellStyle(whiteStyle);
				cell48.setCellValue("INPUT 10: \r\n" + "Taluka Name");

				XSSFRow headingInstrRow13 = Instruction.createRow(15);

				Cell cell49 = headingInstrRow13.createCell(0);
				cell49.setCellStyle(whiteStyle);
				cell49.setCellValue(14);

				Cell cell50 = headingInstrRow13.createCell(1);
				cell50.setCellStyle(whiteStyle);
				cell50.setCellValue("Land Identification");

				Cell cell511 = headingInstrRow13.createCell(3);
				cell511.setCellStyle(whiteStyle);
				cell511.setCellValue("Latitude");

				Cell cell52 = headingInstrRow13.createCell(2);
				cell52.setCellStyle(whiteStyle);
				cell52.setCellValue("Latitude of Chainage");

				XSSFRow headingInstrRow14 = Instruction.createRow(16);

				Cell cell53 = headingInstrRow14.createCell(0);
				cell53.setCellStyle(whiteStyle);
				cell53.setCellValue(15);

				Cell cell54 = headingInstrRow14.createCell(1);
				cell54.setCellStyle(whiteStyle);
				cell54.setCellValue("Land Identification");

				Cell cell55 = headingInstrRow14.createCell(3);
				cell55.setCellStyle(whiteStyle);
				cell55.setCellValue("Longitude");

				Cell cell56 = headingInstrRow14.createCell(2);
				cell56.setCellStyle(whiteStyle);
				cell56.setCellValue("Longitude of Chainage");

				XSSFRow headingInstrRow15 = Instruction.createRow(17);

				Cell cell57 = headingInstrRow15.createCell(0);
				cell57.setCellStyle(whiteStyle);
				cell57.setCellValue(16);

				Cell cell58 = headingInstrRow15.createCell(1);
				cell58.setCellStyle(whiteStyle);
				cell58.setCellValue("Land Identification");

				Cell cell59 = headingInstrRow15.createCell(2);
				cell59.setCellStyle(whiteStyle);
				cell59.setCellValue("Dy SLR");

				Cell cell60 = headingInstrRow15.createCell(3);
				cell60.setCellStyle(whiteStyle);
				cell60.setCellValue("Dy SLR");

				XSSFRow headingInstrRow16 = Instruction.createRow(18);

				Cell cell61 = headingInstrRow16.createCell(0);
				cell61.setCellStyle(whiteStyle);
				cell61.setCellValue(17);

				Cell cell62 = headingInstrRow16.createCell(1);
				cell62.setCellStyle(whiteStyle);
				cell62.setCellValue("Land Identification");

				Cell cell63 = headingInstrRow16.createCell(2);
				cell63.setCellStyle(whiteStyle);
				cell63.setCellValue("SDO");

				Cell cell64 = headingInstrRow16.createCell(3);
				cell64.setCellStyle(whiteStyle);
				cell64.setCellValue("SDO");

				XSSFRow headingInstrRow17 = Instruction.createRow(19);

				Cell cell65 = headingInstrRow17.createCell(0);
				cell65.setCellStyle(whiteStyle);
				cell65.setCellValue(18);

				Cell cell66 = headingInstrRow17.createCell(1);
				cell66.setCellStyle(whiteStyle);
				cell66.setCellValue("Land Identification");

				Cell cell67 = headingInstrRow17.createCell(2);
				cell67.setCellStyle(whiteStyle);
				cell67.setCellValue("Collector");

				Cell cell68 = headingInstrRow17.createCell(3);
				cell68.setCellStyle(whiteStyle);
				cell68.setCellValue("Collector");

				XSSFRow headingInstrRow18 = Instruction.createRow(20);

				Cell cell69 = headingInstrRow18.createCell(0);
				cell69.setCellStyle(whiteStyle);
				cell69.setCellValue(19);

				Cell cell70 = headingInstrRow18.createCell(1);
				cell70.setCellStyle(whiteStyle);
				cell70.setCellValue("Land Identification");

				Cell cell71 = headingInstrRow18.createCell(3);
				cell71.setCellStyle(whiteStyle);
				cell71.setCellValue("Proposal submission Date to collector");

				Cell cell72 = headingInstrRow18.createCell(2);
				cell72.setCellStyle(whiteStyle);
				cell72.setCellValue("Date of Proposal submission Date to collector 'DD/MM/YYYY'");

				XSSFRow headingInstrRow19 = Instruction.createRow(21);

				Cell cell73 = headingInstrRow19.createCell(0);
				cell73.setCellStyle(whiteStyle);
				cell73.setCellValue(20);

				Cell cell74 = headingInstrRow19.createCell(1);
				cell74.setCellStyle(whiteStyle);
				cell74.setCellValue("Land Identification");

				Cell cell75 = headingInstrRow19.createCell(3);
				cell75.setCellStyle(whiteStyle);
				cell75.setCellValue("JM Fee Letter received Date");

				Cell cell76 = headingInstrRow19.createCell(2);
				cell76.setCellStyle(whiteStyle);
				cell76.setCellValue("Date of JM Fee Letter received Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow20 = Instruction.createRow(22);

				Cell cell77 = headingInstrRow20.createCell(0);
				cell77.setCellStyle(whiteStyle);
				cell77.setCellValue(21);

				Cell cell78 = headingInstrRow20.createCell(1);
				cell78.setCellStyle(whiteStyle);
				cell78.setCellValue("Land Identification");

				Cell cell79 = headingInstrRow20.createCell(3);
				cell79.setCellStyle(whiteStyle);
				cell79.setCellValue("JM Fee Amount");

				Cell cell80 = headingInstrRow20.createCell(2);
				cell80.setCellStyle(whiteStyle);
				cell80.setCellValue("Joint Measurement Fee amount (Rs)");

				XSSFRow headingInstrRow21 = Instruction.createRow(23);

				Cell cell81 = headingInstrRow21.createCell(0);
				cell81.setCellStyle(whiteStyle);
				cell81.setCellValue(22);

				Cell cell82 = headingInstrRow21.createCell(1);
				cell82.setCellStyle(whiteStyle);
				cell82.setCellValue("Land Identification");

				Cell cell83 = headingInstrRow21.createCell(3);
				cell83.setCellStyle(whiteStyle);
				cell83.setCellValue("JM Fee Paid Date");

				Cell cell84 = headingInstrRow21.createCell(2);
				cell84.setCellStyle(whiteStyle);
				cell84.setCellValue("Joint Measurement Fee Paid date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow22 = Instruction.createRow(24);

				Cell cell85 = headingInstrRow22.createCell(0);
				cell85.setCellStyle(whiteStyle);
				cell85.setCellValue(23);

				Cell cell86 = headingInstrRow22.createCell(1);
				cell86.setCellStyle(whiteStyle);
				cell86.setCellValue("Land Identification");

				Cell cell87 = headingInstrRow22.createCell(3);
				cell87.setCellStyle(whiteStyle);
				cell87.setCellValue("JM Start Date");

				Cell cell88 = headingInstrRow22.createCell(2);
				cell88.setCellStyle(whiteStyle);
				cell88.setCellValue("Joint Measurement Start date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow23 = Instruction.createRow(25);

				Cell cell89 = headingInstrRow23.createCell(0);
				cell89.setCellStyle(whiteStyle);
				cell89.setCellValue(24);

				Cell cell90 = headingInstrRow23.createCell(1);
				cell90.setCellStyle(whiteStyle);
				cell90.setCellValue("Land Identification");

				Cell cell91 = headingInstrRow23.createCell(3);
				cell91.setCellStyle(whiteStyle);
				cell91.setCellValue("JM Completion Date");

				Cell cell92 = headingInstrRow23.createCell(2);
				cell92.setCellStyle(whiteStyle);
				cell92.setCellValue("Joint Measurement Completion date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow24 = Instruction.createRow(26);

				Cell cell93 = headingInstrRow24.createCell(0);
				cell93.setCellStyle(whiteStyle);
				cell93.setCellValue(25);

				Cell cell94 = headingInstrRow24.createCell(1);
				cell94.setCellStyle(whiteStyle);
				cell94.setCellValue("Land Identification");

				Cell cell95 = headingInstrRow24.createCell(3);
				cell95.setCellStyle(whiteStyle);
				cell95.setCellValue("JM Sheet Date to SDO");

				Cell cell96 = headingInstrRow24.createCell(2);
				cell96.setCellStyle(whiteStyle);
				cell96.setCellValue("Joint Measurement sheet to SDO date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow25 = Instruction.createRow(27);

				Cell cell97 = headingInstrRow25.createCell(0);
				cell97.setCellStyle(whiteStyle);
				cell97.setCellValue(26);

				Cell cell98 = headingInstrRow25.createCell(1);
				cell98.setCellStyle(whiteStyle);
				cell98.setCellValue("Land Identification");

				Cell cell99 = headingInstrRow25.createCell(3);
				cell99.setCellStyle(whiteStyle);
				cell99.setCellValue("JM Remarks");

				Cell cell100 = headingInstrRow25.createCell(2);
				cell100.setCellStyle(whiteStyle);
				cell100.setCellValue("Joint Measurement Remarks");

				XSSFRow headingInstrRow26 = Instruction.createRow(28);

				Cell cell101 = headingInstrRow26.createCell(0);
				cell101.setCellStyle(whiteStyle);
				cell101.setCellValue(27);

				Cell cell102 = headingInstrRow26.createCell(1);
				cell102.setCellStyle(whiteStyle);
				cell102.setCellValue("Land Identification");

				Cell cell103 = headingInstrRow26.createCell(3);
				cell103.setCellStyle(whiteStyle);
				cell103.setCellValue("JM Approval");

				Cell cell104 = headingInstrRow26.createCell(2);
				cell104.setCellStyle(whiteStyle);
				cell104.setCellValue("JM Approval\r\n" + "(i). Accept\r\n" + "(ii). Reject");

				XSSFRow headingInstrRow27 = Instruction.createRow(29);

				Cell cell105 = headingInstrRow27.createCell(0);
				cell105.setCellStyle(whiteStyle);
				cell105.setCellValue(28);

				Cell cell106 = headingInstrRow27.createCell(1);
				cell106.setCellStyle(whiteStyle);
				cell106.setCellValue("Land Identification");

				Cell cell107 = headingInstrRow27.createCell(2);
				cell107.setCellStyle(whiteStyle);
				cell107.setCellValue("Special Feature");

				Cell cell108 = headingInstrRow27.createCell(3);
				cell108.setCellStyle(whiteStyle);
				cell108.setCellValue("Special Feature");

				XSSFRow headingInstrRow28 = Instruction.createRow(30);

				Cell cell109 = headingInstrRow28.createCell(0);
				cell109.setCellStyle(whiteStyle);
				cell109.setCellValue(29);

				Cell cell110 = headingInstrRow28.createCell(1);
				cell110.setCellStyle(whiteStyle);
				cell110.setCellValue("Land Identification");

				Cell cell112 = headingInstrRow28.createCell(2);
				cell112.setCellStyle(whiteStyle);
				cell112.setCellValue("Remarks");

				Cell cell113 = headingInstrRow28.createCell(3);
				cell113.setCellStyle(whiteStyle);
				cell113.setCellValue("Remarks");

				XSSFRow headingInstrRow29 = Instruction.createRow(31);

				Cell cell114 = headingInstrRow29.createCell(0);
				cell114.setCellStyle(whiteStyle);
				cell114.setCellValue(30);

				Cell cell115 = headingInstrRow29.createCell(1);
				cell115.setCellStyle(whiteStyle);
				cell115.setCellValue("Land Identification");

				Cell cell116 = headingInstrRow29.createCell(3);
				cell116.setCellStyle(whiteStyle);
				cell116.setCellValue("Issues");

				Cell cell117 = headingInstrRow29.createCell(2);
				cell117.setCellStyle(whiteStyle);
				cell117.setCellValue("Issues in Land Acquisition");

				XSSFRow headingInstrRow30 = Instruction.createRow(32);

				Cell cell160 = headingInstrRow30.createCell(0);
				cell160.setCellStyle(whiteStyle);
				cell160.setCellValue(31);

				Cell cell161 = headingInstrRow30.createCell(1);
				cell161.setCellStyle(whiteStyle);
				cell161.setCellValue("Private (Indian Railway Act)");

				Cell cell162 = headingInstrRow30.createCell(3);
				cell162.setCellStyle(whiteStyle);
				cell162.setCellValue("LA_ID");

				Cell cell163 = headingInstrRow30.createCell(2);
				cell163.setCellStyle(whiteStyle);
				cell163.setCellValue("INPUT 2: \r\n" + "Land Acquisition Unique ID ");

				XSSFRow headingInstrRow31 = Instruction.createRow(33);

				Cell cell164 = headingInstrRow31.createCell(0);
				cell164.setCellStyle(whiteStyle);
				cell164.setCellValue(32);

				Cell cell165 = headingInstrRow31.createCell(1);
				cell165.setCellStyle(whiteStyle);
				cell165.setCellValue("Private (Indian Railway Act)");

				Cell cell166 = headingInstrRow31.createCell(3);
				cell166.setCellStyle(whiteStyle);
				cell166.setCellValue("Collector");

				Cell cell167 = headingInstrRow31.createCell(2);
				cell167.setCellStyle(whiteStyle);
				cell167.setCellValue("Collector");

				XSSFRow headingInstrRow32 = Instruction.createRow(34);

				Cell cell168 = headingInstrRow32.createCell(0);
				cell168.setCellStyle(whiteStyle);
				cell168.setCellValue(33);

				Cell cell169 = headingInstrRow32.createCell(1);
				cell169.setCellStyle(whiteStyle);
				cell169.setCellValue("Private (Indian Railway Act)");

				Cell cell170 = headingInstrRow32.createCell(3);
				cell170.setCellStyle(whiteStyle);
				cell170.setCellValue("Declaration of Special Railway project  - Submission of Proposal to GM.");

				Cell cell171 = headingInstrRow32.createCell(2);
				cell171.setCellStyle(whiteStyle);
				cell171.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow33 = Instruction.createRow(35);

				Cell cell172 = headingInstrRow33.createCell(0);
				cell172.setCellStyle(whiteStyle);
				cell172.setCellValue(34);

				Cell cell173 = headingInstrRow33.createCell(1);
				cell173.setCellStyle(whiteStyle);
				cell173.setCellValue("Private (Indian Railway Act)");

				Cell cell174 = headingInstrRow33.createCell(3);
				cell174.setCellStyle(whiteStyle);
				cell174.setCellValue("Declaration of Special Railway project  - Approval of GM");

				Cell cell175 = headingInstrRow33.createCell(2);
				cell175.setCellStyle(whiteStyle);
				cell175.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow34 = Instruction.createRow(36);

				Cell cell176 = headingInstrRow34.createCell(0);
				cell176.setCellStyle(whiteStyle);
				cell176.setCellValue(35);

				Cell cell177 = headingInstrRow34.createCell(1);
				cell177.setCellStyle(whiteStyle);
				cell177.setCellValue("Private (Indian Railway Act)");

				Cell cell178 = headingInstrRow34.createCell(3);
				cell178.setCellStyle(whiteStyle);
				cell178.setCellValue("Declaration of Special Railway project  - Draft Letter to CE/Con for Approval");

				Cell cell179 = headingInstrRow34.createCell(2);
				cell179.setCellStyle(whiteStyle);
				cell179.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow35 = Instruction.createRow(37);

				Cell cell180 = headingInstrRow35.createCell(0);
				cell180.setCellStyle(whiteStyle);
				cell180.setCellValue(36);

				Cell cell181 = headingInstrRow35.createCell(1);
				cell181.setCellStyle(whiteStyle);
				cell181.setCellValue("Private (Indian Railway Act)");

				Cell cell182 = headingInstrRow35.createCell(3);
				cell182.setCellStyle(whiteStyle);
				cell182.setCellValue("Declaration of Special Railway project  - Date of Approval of CE/Construction");

				Cell cell183 = headingInstrRow35.createCell(2);
				cell183.setCellStyle(whiteStyle);
				cell183.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow36 = Instruction.createRow(38);

				Cell cell184 = headingInstrRow36.createCell(0);
				cell184.setCellStyle(whiteStyle);
				cell184.setCellValue(37);

				Cell cell185 = headingInstrRow36.createCell(1);
				cell185.setCellStyle(whiteStyle);
				cell185.setCellValue("Private (Indian Railway Act)");

				Cell cell186 = headingInstrRow36.createCell(3);
				cell186.setCellStyle(whiteStyle);
				cell186.setCellValue(
						"Declaration of Special Railway project  - Date of Uploading of  Gazette notification");

				Cell cell187 = headingInstrRow36.createCell(2);
				cell187.setCellStyle(whiteStyle);
				cell187.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow37 = Instruction.createRow(39);

				Cell cell188 = headingInstrRow37.createCell(0);
				cell188.setCellStyle(whiteStyle);
				cell188.setCellValue(38);

				Cell cell189 = headingInstrRow37.createCell(1);
				cell189.setCellStyle(whiteStyle);
				cell189.setCellValue("Private (Indian Railway Act)");

				Cell cell190 = headingInstrRow37.createCell(3);
				cell190.setCellStyle(whiteStyle);
				cell190.setCellValue("Declaration of Special Railway project  - Publication in gazette");

				Cell cell191 = headingInstrRow37.createCell(2);
				cell191.setCellStyle(whiteStyle);
				cell191.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow38 = Instruction.createRow(40);

				Cell cell192 = headingInstrRow38.createCell(0);
				cell192.setCellStyle(whiteStyle);
				cell192.setCellValue(39);

				Cell cell193 = headingInstrRow38.createCell(1);
				cell193.setCellStyle(whiteStyle);
				cell193.setCellValue("Private (Indian Railway Act)");

				Cell cell194 = headingInstrRow38.createCell(3);
				cell194.setCellStyle(whiteStyle);
				cell194.setCellValue("Nomination of competent Authority - Date of Proposal to DC for nomination");

				Cell cell195 = headingInstrRow38.createCell(2);
				cell195.setCellStyle(whiteStyle);
				cell195.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow39 = Instruction.createRow(41);

				Cell cell196 = headingInstrRow39.createCell(0);
				cell196.setCellStyle(whiteStyle);
				cell196.setCellValue(40);

				Cell cell197 = headingInstrRow39.createCell(1);
				cell197.setCellStyle(whiteStyle);
				cell197.setCellValue("Private (Indian Railway Act)");

				Cell cell198 = headingInstrRow39.createCell(3);
				cell198.setCellStyle(whiteStyle);
				cell198.setCellValue("Nomination of competent Authority - Date of Nomination of competent Authority.");

				Cell cell199 = headingInstrRow39.createCell(2);
				cell199.setCellStyle(whiteStyle);
				cell199.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow40 = Instruction.createRow(42);

				Cell cell200 = headingInstrRow40.createCell(0);
				cell200.setCellStyle(whiteStyle);
				cell200.setCellValue(41);

				Cell cell201 = headingInstrRow40.createCell(1);
				cell201.setCellStyle(whiteStyle);
				cell201.setCellValue("Private (Indian Railway Act)");

				Cell cell202 = headingInstrRow40.createCell(3);
				cell202.setCellStyle(whiteStyle);
				cell202.setCellValue("Nomination of competent Authority - Draft Letter to CE/Con for Approval");

				Cell cell203 = headingInstrRow40.createCell(2);
				cell203.setCellStyle(whiteStyle);
				cell203.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow41 = Instruction.createRow(43);

				Cell cell204 = headingInstrRow41.createCell(0);
				cell204.setCellStyle(whiteStyle);
				cell204.setCellValue(42);

				Cell cell205 = headingInstrRow41.createCell(1);
				cell205.setCellStyle(whiteStyle);
				cell205.setCellValue("Private (Indian Railway Act)");

				Cell cell206 = headingInstrRow41.createCell(3);
				cell206.setCellStyle(whiteStyle);
				cell206.setCellValue("Nomination of competent Authority - Date of Approval of CE/Construction");

				Cell cell207 = headingInstrRow41.createCell(2);
				cell207.setCellStyle(whiteStyle);
				cell207.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow42 = Instruction.createRow(44);

				Cell cell208 = headingInstrRow42.createCell(0);
				cell208.setCellStyle(whiteStyle);
				cell208.setCellValue(43);

				Cell cell209 = headingInstrRow42.createCell(1);
				cell209.setCellStyle(whiteStyle);
				cell209.setCellValue("Private (Indian Railway Act)");

				Cell cell210 = headingInstrRow42.createCell(3);
				cell210.setCellStyle(whiteStyle);
				cell210.setCellValue("Nomination of competent Authority - Date of Uploading of  Gazette notification");

				Cell cell311 = headingInstrRow42.createCell(2);
				cell311.setCellStyle(whiteStyle);
				cell311.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow43 = Instruction.createRow(45);

				Cell cell212 = headingInstrRow43.createCell(0);
				cell212.setCellStyle(whiteStyle);
				cell212.setCellValue(44);

				Cell cell213 = headingInstrRow43.createCell(1);
				cell213.setCellStyle(whiteStyle);
				cell213.setCellValue("Private (Indian Railway Act)");

				Cell cell214 = headingInstrRow43.createCell(3);
				cell214.setCellStyle(whiteStyle);
				cell214.setCellValue("Publication in gazette");

				Cell cell215 = headingInstrRow43.createCell(2);
				cell215.setCellStyle(whiteStyle);
				cell215.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow44 = Instruction.createRow(46);

				Cell cell216 = headingInstrRow44.createCell(0);
				cell216.setCellStyle(whiteStyle);
				cell216.setCellValue(45);

				Cell cell217 = headingInstrRow44.createCell(1);
				cell217.setCellStyle(whiteStyle);
				cell217.setCellValue("Private (Indian Railway Act)");

				Cell cell218 = headingInstrRow44.createCell(3);
				cell218.setCellStyle(whiteStyle);
				cell218.setCellValue(
						"Publication of notification under 20 A - Date of Submission of draft notification to CALA");

				Cell cell219 = headingInstrRow44.createCell(2);
				cell219.setCellStyle(whiteStyle);
				cell219.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow45 = Instruction.createRow(47);

				Cell cell220 = headingInstrRow45.createCell(0);
				cell220.setCellStyle(whiteStyle);
				cell220.setCellValue(46);

				Cell cell221 = headingInstrRow45.createCell(1);
				cell221.setCellStyle(whiteStyle);
				cell221.setCellValue("Private (Indian Railway Act)");

				Cell cell222 = headingInstrRow45.createCell(3);
				cell222.setCellStyle(whiteStyle);
				cell222.setCellValue("Publication of notification under 20 A - Approval of CALA");

				Cell cell223 = headingInstrRow45.createCell(2);
				cell223.setCellStyle(whiteStyle);
				cell223.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow46 = Instruction.createRow(48);

				Cell cell224 = headingInstrRow46.createCell(0);
				cell224.setCellStyle(whiteStyle);
				cell224.setCellValue(47);

				Cell cell225 = headingInstrRow46.createCell(1);
				cell225.setCellStyle(whiteStyle);
				cell225.setCellValue("Private (Indian Railway Act)");

				Cell cell226 = headingInstrRow46.createCell(3);
				cell226.setCellStyle(whiteStyle);
				cell226.setCellValue("Publication of notification under 20 A - Draft Letter to CE/Con for Approval");

				Cell cell227 = headingInstrRow46.createCell(2);
				cell227.setCellStyle(whiteStyle);
				cell227.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow47 = Instruction.createRow(49);

				Cell cell228 = headingInstrRow47.createCell(0);
				cell228.setCellStyle(whiteStyle);
				cell228.setCellValue(48);

				Cell cell229 = headingInstrRow47.createCell(1);
				cell229.setCellStyle(whiteStyle);
				cell229.setCellValue("Private (Indian Railway Act)");

				Cell cell230 = headingInstrRow47.createCell(3);
				cell230.setCellStyle(whiteStyle);
				cell230.setCellValue("Publication of notification under 20 A - Date of Approval of CE/Construction");

				Cell cell231 = headingInstrRow47.createCell(2);
				cell231.setCellStyle(whiteStyle);
				cell231.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow48 = Instruction.createRow(50);

				Cell cell232 = headingInstrRow48.createCell(0);
				cell232.setCellStyle(whiteStyle);
				cell232.setCellValue(49);

				Cell cell233 = headingInstrRow48.createCell(1);
				cell233.setCellStyle(whiteStyle);
				cell233.setCellValue("Private (Indian Railway Act)");

				Cell cell234 = headingInstrRow48.createCell(3);
				cell234.setCellStyle(whiteStyle);
				cell234.setCellValue(
						"Publication of notification under 20 A - Date of Uploading of  Gazette notification");

				Cell cell235 = headingInstrRow48.createCell(2);
				cell235.setCellStyle(whiteStyle);
				cell235.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow49 = Instruction.createRow(51);

				Cell cell236 = headingInstrRow48.createCell(0);
				cell236.setCellStyle(whiteStyle);
				cell236.setCellValue(50);

				Cell cell237 = headingInstrRow48.createCell(1);
				cell237.setCellStyle(whiteStyle);
				cell237.setCellValue("Private (Indian Railway Act)");

				Cell cell238 = headingInstrRow48.createCell(3);
				cell238.setCellStyle(whiteStyle);
				cell238.setCellValue("Publication of notification under 20 A - Publication in gazette");

				Cell cell239 = headingInstrRow48.createCell(2);
				cell239.setCellStyle(whiteStyle);
				cell239.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow50 = Instruction.createRow(52);

				Cell cell240 = headingInstrRow50.createCell(0);
				cell240.setCellStyle(whiteStyle);
				cell240.setCellValue(51);

				Cell cell241 = headingInstrRow50.createCell(1);
				cell241.setCellStyle(whiteStyle);
				cell241.setCellValue("Private (Indian Railway Act)");

				Cell cell242 = headingInstrRow50.createCell(3);
				cell242.setCellStyle(whiteStyle);
				cell242.setCellValue("Publication of notification under 20 A - Publication in 2 Local Newspapers");

				Cell cell243 = headingInstrRow50.createCell(2);
				cell243.setCellStyle(whiteStyle);
				cell243.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow51 = Instruction.createRow(53);

				Cell cell244 = headingInstrRow51.createCell(0);
				cell244.setCellStyle(whiteStyle);
				cell244.setCellValue(52);

				Cell cell245 = headingInstrRow51.createCell(1);
				cell245.setCellStyle(whiteStyle);
				cell245.setCellValue("Private (Indian Railway Act)");

				Cell cell246 = headingInstrRow51.createCell(3);
				cell246.setCellStyle(whiteStyle);
				cell246.setCellValue("Publication of notification under 20 A - Pasting of notification in villages");

				Cell cell247 = headingInstrRow51.createCell(2);
				cell247.setCellStyle(whiteStyle);
				cell247.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow52 = Instruction.createRow(54);

				Cell cell248 = headingInstrRow52.createCell(0);
				cell248.setCellStyle(whiteStyle);
				cell248.setCellValue(53);

				Cell cell249 = headingInstrRow52.createCell(1);
				cell249.setCellStyle(whiteStyle);
				cell249.setCellValue("Private (Indian Railway Act)");

				Cell cell250 = headingInstrRow52.createCell(3);
				cell250.setCellStyle(whiteStyle);
				cell250.setCellValue("Grievances Redressal - Receipt of Grievances");

				Cell cell251 = headingInstrRow52.createCell(2);
				cell251.setCellStyle(whiteStyle);
				cell251.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow53 = Instruction.createRow(55);

				Cell cell252 = headingInstrRow53.createCell(0);
				cell252.setCellStyle(whiteStyle);
				cell252.setCellValue(54);

				Cell cell253 = headingInstrRow53.createCell(1);
				cell253.setCellStyle(whiteStyle);
				cell253.setCellValue("Private (Indian Railway Act)");

				Cell cell254 = headingInstrRow53.createCell(3);
				cell254.setCellStyle(whiteStyle);
				cell254.setCellValue("Grievances Redressal - Disposal of Grievances");

				Cell cell255 = headingInstrRow53.createCell(2);
				cell255.setCellStyle(whiteStyle);
				cell255.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow54 = Instruction.createRow(56);

				Cell cell256 = headingInstrRow54.createCell(0);
				cell256.setCellStyle(whiteStyle);
				cell256.setCellValue(55);

				Cell cell257 = headingInstrRow54.createCell(1);
				cell257.setCellStyle(whiteStyle);
				cell257.setCellValue("Private (Indian Railway Act)");

				Cell cell258 = headingInstrRow54.createCell(3);
				cell258.setCellStyle(whiteStyle);
				cell258.setCellValue("Acquisition notice under 20E - Date of Submission of draft notification to CALA");

				Cell cell259 = headingInstrRow54.createCell(2);
				cell259.setCellStyle(whiteStyle);
				cell259.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow55 = Instruction.createRow(57);

				Cell cell260 = headingInstrRow55.createCell(0);
				cell260.setCellStyle(whiteStyle);
				cell260.setCellValue(56);

				Cell cell261 = headingInstrRow55.createCell(1);
				cell261.setCellStyle(whiteStyle);
				cell261.setCellValue("Private (Indian Railway Act)");

				Cell cell262 = headingInstrRow55.createCell(3);
				cell262.setCellStyle(whiteStyle);
				cell262.setCellValue("Acquisition notice under 20E - Approval of CALA");

				Cell cell263 = headingInstrRow55.createCell(2);
				cell263.setCellStyle(whiteStyle);
				cell263.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow56 = Instruction.createRow(58);

				Cell cell264 = headingInstrRow56.createCell(0);
				cell264.setCellStyle(whiteStyle);
				cell264.setCellValue(57);

				Cell cell265 = headingInstrRow56.createCell(1);
				cell265.setCellStyle(whiteStyle);
				cell265.setCellValue("Private (Indian Railway Act)");

				Cell cell266 = headingInstrRow56.createCell(3);
				cell266.setCellStyle(whiteStyle);
				cell266.setCellValue("Acquisition notice under 20E - Draft Letter to CE/Con for Approval");

				Cell cell267 = headingInstrRow56.createCell(2);
				cell267.setCellStyle(whiteStyle);
				cell267.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow57 = Instruction.createRow(59);

				Cell cell268 = headingInstrRow57.createCell(0);
				cell268.setCellStyle(whiteStyle);
				cell268.setCellValue(58);

				Cell cell269 = headingInstrRow57.createCell(1);
				cell269.setCellStyle(whiteStyle);
				cell269.setCellValue("Private (Indian Railway Act)");

				Cell cell270 = headingInstrRow57.createCell(3);
				cell270.setCellStyle(whiteStyle);
				cell270.setCellValue("Acquisition notice under 20E - Date of Approval of CE/Construction");

				Cell cell271 = headingInstrRow57.createCell(2);
				cell271.setCellStyle(whiteStyle);
				cell271.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow58 = Instruction.createRow(60);

				Cell cell272 = headingInstrRow58.createCell(0);
				cell272.setCellStyle(whiteStyle);
				cell272.setCellValue(59);

				Cell cell273 = headingInstrRow58.createCell(1);
				cell273.setCellStyle(whiteStyle);
				cell273.setCellValue("Private (Indian Railway Act)");

				Cell cell274 = headingInstrRow58.createCell(3);
				cell274.setCellStyle(whiteStyle);
				cell274.setCellValue("Acquisition notice under 20E - Date of Uploading of  Gazette notification");

				Cell cell275 = headingInstrRow58.createCell(2);
				cell275.setCellStyle(whiteStyle);
				cell275.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow59 = Instruction.createRow(61);

				Cell cell276 = headingInstrRow59.createCell(0);
				cell276.setCellStyle(whiteStyle);
				cell276.setCellValue(60);

				Cell cell277 = headingInstrRow59.createCell(1);
				cell277.setCellStyle(whiteStyle);
				cell277.setCellValue("Private (Indian Railway Act)");

				Cell cell278 = headingInstrRow59.createCell(3);
				cell278.setCellStyle(whiteStyle);
				cell278.setCellValue("Acquisition notice under 20E - Publication in gazette");

				Cell cell279 = headingInstrRow59.createCell(2);
				cell279.setCellStyle(whiteStyle);
				cell279.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow60 = Instruction.createRow(62);

				Cell cell280 = headingInstrRow60.createCell(0);
				cell280.setCellStyle(whiteStyle);
				cell280.setCellValue(61);

				Cell cell281 = headingInstrRow60.createCell(1);
				cell281.setCellStyle(whiteStyle);
				cell281.setCellValue("Private (Indian Railway Act)");

				Cell cell282 = headingInstrRow60.createCell(3);
				cell282.setCellStyle(whiteStyle);
				cell282.setCellValue("Acquisition notice under 20E - Publication of notice in 2 Local News papers ");

				Cell cell283 = headingInstrRow60.createCell(2);
				cell283.setCellStyle(whiteStyle);
				cell283.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow61 = Instruction.createRow(63);

				Cell cell284 = headingInstrRow61.createCell(0);
				cell284.setCellStyle(whiteStyle);
				cell284.setCellValue(62);

				Cell cell285 = headingInstrRow61.createCell(1);
				cell285.setCellStyle(whiteStyle);
				cell285.setCellValue("Private (Indian Railway Act)");

				Cell cell286 = headingInstrRow61.createCell(3);
				cell286.setCellStyle(whiteStyle);
				cell286.setCellValue("Acquisition notice under 20F - Date of Submission of draft notification to CALA");

				Cell cell287 = headingInstrRow61.createCell(2);
				cell287.setCellStyle(whiteStyle);
				cell287.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow62 = Instruction.createRow(64);

				Cell cell288 = headingInstrRow62.createCell(0);
				cell288.setCellStyle(whiteStyle);
				cell288.setCellValue(63);

				Cell cell289 = headingInstrRow62.createCell(1);
				cell289.setCellStyle(whiteStyle);
				cell289.setCellValue("Private (Indian Railway Act)");

				Cell cell290 = headingInstrRow62.createCell(3);
				cell290.setCellStyle(whiteStyle);
				cell290.setCellValue("Acquisition notice under 20F - Approval of CALA");

				Cell cell291 = headingInstrRow62.createCell(2);
				cell291.setCellStyle(whiteStyle);
				cell291.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow63 = Instruction.createRow(65);

				Cell cell292 = headingInstrRow63.createCell(0);
				cell292.setCellStyle(whiteStyle);
				cell292.setCellValue(64);

				Cell cell293 = headingInstrRow63.createCell(1);
				cell293.setCellStyle(whiteStyle);
				cell293.setCellValue("Private (Indian Railway Act)");

				Cell cell294 = headingInstrRow63.createCell(3);
				cell294.setCellStyle(whiteStyle);
				cell294.setCellValue("Acquisition notice under 20F - Draft Letter to CE/Con for Approval");

				Cell cell295 = headingInstrRow63.createCell(2);
				cell295.setCellStyle(whiteStyle);
				cell295.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow64 = Instruction.createRow(66);

				Cell cell296 = headingInstrRow64.createCell(0);
				cell296.setCellStyle(whiteStyle);
				cell296.setCellValue(65);

				Cell cell297 = headingInstrRow64.createCell(1);
				cell297.setCellStyle(whiteStyle);
				cell297.setCellValue("Private (Indian Railway Act)");

				Cell cell298 = headingInstrRow64.createCell(3);
				cell298.setCellStyle(whiteStyle);
				cell298.setCellValue("Acquisition notice under 20F - Date of Approval of CE/Construction");

				Cell cell299 = headingInstrRow64.createCell(2);
				cell299.setCellStyle(whiteStyle);
				cell299.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow65 = Instruction.createRow(67);

				Cell cell300 = headingInstrRow65.createCell(0);
				cell300.setCellStyle(whiteStyle);
				cell300.setCellValue(66);

				Cell cell301 = headingInstrRow65.createCell(1);
				cell301.setCellStyle(whiteStyle);
				cell301.setCellValue("Private (Indian Railway Act)");

				Cell cell302 = headingInstrRow65.createCell(3);
				cell302.setCellStyle(whiteStyle);
				cell302.setCellValue("Acquisition notice under 20F - Date of Uploading of  Gazette notification");

				Cell cell303 = headingInstrRow65.createCell(2);
				cell303.setCellStyle(whiteStyle);
				cell303.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow66 = Instruction.createRow(68);

				Cell cell304 = headingInstrRow66.createCell(0);
				cell304.setCellStyle(whiteStyle);
				cell304.setCellValue(67);

				Cell cell305 = headingInstrRow66.createCell(1);
				cell305.setCellStyle(whiteStyle);
				cell305.setCellValue("Private (Indian Railway Act)");

				Cell cell306 = headingInstrRow66.createCell(3);
				cell306.setCellStyle(whiteStyle);
				cell306.setCellValue("Acquisition notice under 20F - Publication in gazette");

				Cell cell307 = headingInstrRow66.createCell(2);
				cell307.setCellStyle(whiteStyle);
				cell307.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow67 = Instruction.createRow(69);

				Cell cell308 = headingInstrRow67.createCell(0);
				cell308.setCellStyle(whiteStyle);
				cell308.setCellValue(68);

				Cell cell309 = headingInstrRow67.createCell(1);
				cell309.setCellStyle(whiteStyle);
				cell309.setCellValue("Private (Indian Railway Act)");

				Cell cell310 = headingInstrRow67.createCell(3);
				cell310.setCellStyle(whiteStyle);
				cell310.setCellValue("Acquisition notice under 20F - Publication of notice in 2 Local News papers ");

				Cell cell411 = headingInstrRow67.createCell(2);
				cell411.setCellStyle(whiteStyle);
				cell411.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow68 = Instruction.createRow(70);

				Cell cell312 = headingInstrRow68.createCell(0);
				cell312.setCellStyle(whiteStyle);
				cell312.setCellValue(69);

				Cell cell313 = headingInstrRow68.createCell(1);
				cell313.setCellStyle(whiteStyle);
				cell313.setCellValue("Private Land valuation");

				Cell cell314 = headingInstrRow68.createCell(3);
				cell314.setCellStyle(whiteStyle);
				cell314.setCellValue("LA_ID");

				Cell cell315 = headingInstrRow68.createCell(2);
				cell315.setCellStyle(whiteStyle);
				cell315.setCellValue("");

				XSSFRow headingInstrRow69 = Instruction.createRow(71);

				Cell cell316 = headingInstrRow69.createCell(0);
				cell316.setCellStyle(whiteStyle);
				cell316.setCellValue(70);

				Cell cell317 = headingInstrRow69.createCell(1);
				cell317.setCellStyle(whiteStyle);
				cell317.setCellValue("Private Land valuation");

				Cell cell318 = headingInstrRow69.createCell(3);
				cell318.setCellStyle(whiteStyle);
				cell318.setCellValue("Forest Tree Survey");

				Cell cell319 = headingInstrRow69.createCell(2);
				cell319.setCellStyle(whiteStyle);
				cell319.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow70 = Instruction.createRow(72);

				Cell cell320 = headingInstrRow70.createCell(0);
				cell320.setCellStyle(whiteStyle);
				cell320.setCellValue(71);

				Cell cell321 = headingInstrRow70.createCell(1);
				cell321.setCellStyle(whiteStyle);
				cell321.setCellValue("Private Land valuation");

				Cell cell322 = headingInstrRow70.createCell(3);
				cell322.setCellStyle(whiteStyle);
				cell322.setCellValue("Forest Tree Valuation");

				Cell cell323 = headingInstrRow70.createCell(2);
				cell323.setCellStyle(whiteStyle);
				cell323.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow71 = Instruction.createRow(73);

				Cell cell324 = headingInstrRow71.createCell(0);
				cell324.setCellStyle(whiteStyle);
				cell324.setCellValue(72);

				Cell cell325 = headingInstrRow71.createCell(1);
				cell325.setCellStyle(whiteStyle);
				cell325.setCellValue("Private Land valuation");

				Cell cell326 = headingInstrRow71.createCell(3);
				cell326.setCellStyle(whiteStyle);
				cell326.setCellValue("Horticulture Tree Survey");

				Cell cell327 = headingInstrRow71.createCell(2);
				cell327.setCellStyle(whiteStyle);
				cell327.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow72 = Instruction.createRow(74);

				Cell cell328 = headingInstrRow72.createCell(0);
				cell328.setCellStyle(whiteStyle);
				cell328.setCellValue(73);

				Cell cell329 = headingInstrRow72.createCell(1);
				cell329.setCellStyle(whiteStyle);
				cell329.setCellValue("Private Land valuation");

				Cell cell330 = headingInstrRow72.createCell(3);
				cell330.setCellStyle(whiteStyle);
				cell330.setCellValue("Horticulture Tree Valuation");

				Cell cell331 = headingInstrRow72.createCell(2);
				cell331.setCellStyle(whiteStyle);
				cell331.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow73 = Instruction.createRow(75);

				Cell cell332 = headingInstrRow73.createCell(0);
				cell332.setCellStyle(whiteStyle);
				cell332.setCellValue(74);

				Cell cell333 = headingInstrRow73.createCell(1);
				cell333.setCellStyle(whiteStyle);
				cell333.setCellValue("Private Land valuation");

				Cell cell334 = headingInstrRow73.createCell(3);
				cell334.setCellStyle(whiteStyle);
				cell334.setCellValue("Structure Survey");

				Cell cell335 = headingInstrRow73.createCell(2);
				cell335.setCellStyle(whiteStyle);
				cell335.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow74 = Instruction.createRow(76);

				Cell cell336 = headingInstrRow74.createCell(0);
				cell336.setCellStyle(whiteStyle);
				cell336.setCellValue(75);

				Cell cell337 = headingInstrRow74.createCell(1);
				cell337.setCellStyle(whiteStyle);
				cell337.setCellValue("Private Land valuation");

				Cell cell338 = headingInstrRow74.createCell(3);
				cell338.setCellStyle(whiteStyle);
				cell338.setCellValue("Structure Valuation");

				Cell cell339 = headingInstrRow74.createCell(2);
				cell339.setCellStyle(whiteStyle);
				cell339.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow75 = Instruction.createRow(77);

				Cell cell340 = headingInstrRow75.createCell(0);
				cell340.setCellStyle(whiteStyle);
				cell340.setCellValue(76);

				Cell cell341 = headingInstrRow75.createCell(1);
				cell341.setCellStyle(whiteStyle);
				cell341.setCellValue("Private Land valuation");

				Cell cell342 = headingInstrRow75.createCell(3);
				cell342.setCellStyle(whiteStyle);
				cell342.setCellValue("Borewell Survey");

				Cell cell343 = headingInstrRow75.createCell(2);
				cell343.setCellStyle(whiteStyle);
				cell343.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow76 = Instruction.createRow(78);

				Cell cell344 = headingInstrRow76.createCell(0);
				cell344.setCellStyle(whiteStyle);
				cell344.setCellValue(77);

				Cell cell345 = headingInstrRow76.createCell(1);
				cell345.setCellStyle(whiteStyle);
				cell345.setCellValue("Private Land valuation");

				Cell cell346 = headingInstrRow76.createCell(3);
				cell346.setCellStyle(whiteStyle);
				cell346.setCellValue("Borewell Valuation");

				Cell cell347 = headingInstrRow76.createCell(2);
				cell347.setCellStyle(whiteStyle);
				cell347.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow77 = Instruction.createRow(79);

				Cell cell348 = headingInstrRow77.createCell(0);
				cell348.setCellStyle(whiteStyle);
				cell348.setCellValue(78);

				Cell cell349 = headingInstrRow77.createCell(1);
				cell349.setCellStyle(whiteStyle);
				cell349.setCellValue("Private Land valuation");

				Cell cell350 = headingInstrRow77.createCell(3);
				cell350.setCellStyle(whiteStyle);
				cell350.setCellValue("Date of RFP to ADTP");

				Cell cell351 = headingInstrRow77.createCell(2);
				cell351.setCellStyle(whiteStyle);
				cell351.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow78 = Instruction.createRow(80);

				Cell cell352 = headingInstrRow78.createCell(0);
				cell352.setCellStyle(whiteStyle);
				cell352.setCellValue(79);

				Cell cell353 = headingInstrRow78.createCell(1);
				cell353.setCellStyle(whiteStyle);
				cell353.setCellValue("Private Land valuation");

				Cell cell354 = headingInstrRow78.createCell(3);
				cell354.setCellStyle(whiteStyle);
				cell354.setCellValue("Date of Rate Fixation of Land");

				Cell cell355 = headingInstrRow78.createCell(2);
				cell355.setCellStyle(whiteStyle);
				cell355.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow79 = Instruction.createRow(81);

				Cell cell356 = headingInstrRow79.createCell(0);
				cell356.setCellStyle(whiteStyle);
				cell356.setCellValue(80);

				Cell cell357 = headingInstrRow79.createCell(1);
				cell357.setCellStyle(whiteStyle);
				cell357.setCellValue("Private Land valuation");

				Cell cell358 = headingInstrRow79.createCell(3);
				cell358.setCellStyle(whiteStyle);
				cell358.setCellValue("SDO demand for payment");

				Cell cell359 = headingInstrRow79.createCell(2);
				cell359.setCellStyle(whiteStyle);
				cell359.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow80 = Instruction.createRow(82);

				Cell cell360 = headingInstrRow80.createCell(0);
				cell360.setCellStyle(whiteStyle);
				cell360.setCellValue(81);

				Cell cell361 = headingInstrRow80.createCell(1);
				cell361.setCellStyle(whiteStyle);
				cell361.setCellValue("Private Land valuation");

				Cell cell362 = headingInstrRow80.createCell(3);
				cell362.setCellStyle(whiteStyle);
				cell362.setCellValue("Date of Approval for Payment");

				Cell cell363 = headingInstrRow80.createCell(2);
				cell363.setCellStyle(whiteStyle);
				cell363.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow81 = Instruction.createRow(83);

				Cell cell364 = headingInstrRow81.createCell(0);
				cell364.setCellStyle(whiteStyle);
				cell364.setCellValue(82);

				Cell cell365 = headingInstrRow81.createCell(1);
				cell365.setCellStyle(whiteStyle);
				cell365.setCellValue("Private Land valuation");

				Cell cell366 = headingInstrRow81.createCell(3);
				cell366.setCellStyle(whiteStyle);
				cell366.setCellValue("Payment Amount");

				Cell cell367 = headingInstrRow81.createCell(2);
				cell367.setCellStyle(whiteStyle);
				cell367.setCellValue("Payment amount in Rupees");

				XSSFRow headingInstrRow82 = Instruction.createRow(84);

				Cell cell368 = headingInstrRow82.createCell(0);
				cell368.setCellStyle(whiteStyle);
				cell368.setCellValue(83);

				Cell cell369 = headingInstrRow82.createCell(1);
				cell369.setCellStyle(whiteStyle);
				cell369.setCellValue("Private Land valuation");

				Cell cell370 = headingInstrRow82.createCell(3);
				cell370.setCellStyle(whiteStyle);
				cell370.setCellValue("Payment Date");

				Cell cell371 = headingInstrRow82.createCell(2);
				cell371.setCellStyle(whiteStyle);
				cell371.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow83 = Instruction.createRow(85);

				Cell cell372 = headingInstrRow83.createCell(0);
				cell372.setCellStyle(whiteStyle);
				cell372.setCellValue(84);

				Cell cell373 = headingInstrRow83.createCell(1);
				cell373.setCellStyle(whiteStyle);
				cell373.setCellValue("Private Land Acquisition");

				Cell cell374 = headingInstrRow83.createCell(3);
				cell374.setCellStyle(whiteStyle);
				cell374.setCellValue("LA_ID");

				Cell cell375 = headingInstrRow83.createCell(2);
				cell375.setCellStyle(whiteStyle);
				cell375.setCellValue("INPUT 2: Land Acquisition Unique ID ");

				XSSFRow headingInstrRow84 = Instruction.createRow(86);

				Cell cell376 = headingInstrRow84.createCell(0);
				cell376.setCellStyle(whiteStyle);
				cell376.setCellValue(85);

				Cell cell377 = headingInstrRow84.createCell(1);
				cell377.setCellStyle(whiteStyle);
				cell377.setCellValue("Private Land Acquisition");

				Cell cell378 = headingInstrRow84.createCell(3);
				cell378.setCellStyle(whiteStyle);
				cell378.setCellValue("Name of Owner");

				Cell cell379 = headingInstrRow84.createCell(2);
				cell379.setCellStyle(whiteStyle);
				cell379.setCellValue("Name of Owner");

				XSSFRow headingInstrRow85 = Instruction.createRow(87);

				Cell cell380 = headingInstrRow85.createCell(0);
				cell380.setCellStyle(whiteStyle);
				cell380.setCellValue(86);

				Cell cell381 = headingInstrRow85.createCell(1);
				cell381.setCellStyle(whiteStyle);
				cell381.setCellValue("Private Land Acquisition");

				Cell cell382 = headingInstrRow85.createCell(3);
				cell382.setCellStyle(whiteStyle);
				cell382.setCellValue("Basic Rate");

				Cell cell383 = headingInstrRow85.createCell(2);
				cell383.setCellStyle(whiteStyle);
				cell383.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow86 = Instruction.createRow(88);

				Cell cell384 = headingInstrRow86.createCell(0);
				cell384.setCellStyle(whiteStyle);
				cell384.setCellValue(87);

				Cell cell385 = headingInstrRow86.createCell(1);
				cell385.setCellStyle(whiteStyle);
				cell385.setCellValue("Private Land Acquisition");

				Cell cell386 = headingInstrRow86.createCell(3);
				cell386.setCellStyle(whiteStyle);
				cell386.setCellValue("100% Solatium");

				Cell cell387 = headingInstrRow86.createCell(2);
				cell387.setCellStyle(whiteStyle);
				cell387.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow87 = Instruction.createRow(89);

				Cell cell388 = headingInstrRow87.createCell(0);
				cell388.setCellStyle(whiteStyle);
				cell388.setCellValue(88);

				Cell cell389 = headingInstrRow87.createCell(1);
				cell389.setCellStyle(whiteStyle);
				cell389.setCellValue("Private Land Acquisition");

				Cell cell390 = headingInstrRow87.createCell(3);
				cell390.setCellStyle(whiteStyle);
				cell390.setCellValue("Extra 25%");

				Cell cell391 = headingInstrRow87.createCell(2);
				cell391.setCellStyle(whiteStyle);
				cell391.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow88 = Instruction.createRow(90);

				Cell cell392 = headingInstrRow88.createCell(0);
				cell392.setCellStyle(whiteStyle);
				cell392.setCellValue(89);

				Cell cell393 = headingInstrRow88.createCell(1);
				cell393.setCellStyle(whiteStyle);
				cell393.setCellValue("Private Land Acquisition");

				Cell cell394 = headingInstrRow88.createCell(3);
				cell394.setCellStyle(whiteStyle);
				cell394.setCellValue("Total Rate/m2");

				Cell cell395 = headingInstrRow88.createCell(2);
				cell395.setCellStyle(whiteStyle);
				cell395.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow89 = Instruction.createRow(91);

				Cell cell396 = headingInstrRow89.createCell(0);
				cell396.setCellStyle(whiteStyle);
				cell396.setCellValue(90);

				Cell cell397 = headingInstrRow89.createCell(1);
				cell397.setCellStyle(whiteStyle);
				cell397.setCellValue("Private Land Acquisition");

				Cell cell398 = headingInstrRow89.createCell(3);
				cell398.setCellStyle(whiteStyle);
				cell398.setCellValue("Land Compensation");

				Cell cell399 = headingInstrRow89.createCell(2);
				cell399.setCellStyle(whiteStyle);
				cell399.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow90 = Instruction.createRow(92);

				Cell cell400 = headingInstrRow90.createCell(0);
				cell400.setCellStyle(whiteStyle);
				cell400.setCellValue(91);

				Cell cell401 = headingInstrRow90.createCell(1);
				cell401.setCellStyle(whiteStyle);
				cell401.setCellValue("Private Land Acquisition");

				Cell cell402 = headingInstrRow90.createCell(3);
				cell402.setCellStyle(whiteStyle);
				cell402.setCellValue("Agriculture tree nos");

				Cell cell403 = headingInstrRow90.createCell(2);
				cell403.setCellStyle(whiteStyle);
				cell403.setCellValue("Number of trees");

				XSSFRow headingInstrRow91 = Instruction.createRow(93);

				Cell cell404 = headingInstrRow91.createCell(0);
				cell404.setCellStyle(whiteStyle);
				cell404.setCellValue(92);

				Cell cell405 = headingInstrRow91.createCell(1);
				cell405.setCellStyle(whiteStyle);
				cell405.setCellValue("Private Land Acquisition");

				Cell cell406 = headingInstrRow91.createCell(3);
				cell406.setCellStyle(whiteStyle);
				cell406.setCellValue("Agriculture tree rate");

				Cell cell407 = headingInstrRow91.createCell(2);
				cell407.setCellStyle(whiteStyle);
				cell407.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow92 = Instruction.createRow(94);

				Cell cell408 = headingInstrRow92.createCell(0);
				cell408.setCellStyle(whiteStyle);
				cell408.setCellValue(93);

				Cell cell409 = headingInstrRow92.createCell(1);
				cell409.setCellStyle(whiteStyle);
				cell409.setCellValue("Private Land Acquisition");

				Cell cell410 = headingInstrRow92.createCell(3);
				cell410.setCellStyle(whiteStyle);
				cell410.setCellValue("Agriculture tree compensation");

				Cell cell5111 = headingInstrRow92.createCell(2);
				cell5111.setCellStyle(whiteStyle);
				cell5111.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow93 = Instruction.createRow(95);

				Cell cell412 = headingInstrRow93.createCell(0);
				cell412.setCellStyle(whiteStyle);
				cell412.setCellValue(94);

				Cell cell413 = headingInstrRow93.createCell(1);
				cell413.setCellStyle(whiteStyle);
				cell413.setCellValue("Private Land Acquisition");

				Cell cell414 = headingInstrRow93.createCell(3);
				cell414.setCellStyle(whiteStyle);
				cell414.setCellValue("Forest tree nos");

				Cell cell415 = headingInstrRow93.createCell(2);
				cell415.setCellStyle(whiteStyle);
				cell415.setCellValue("Number of trees");

				XSSFRow headingInstrRow94 = Instruction.createRow(96);

				Cell cell416 = headingInstrRow94.createCell(0);
				cell416.setCellStyle(whiteStyle);
				cell416.setCellValue(95);

				Cell cell417 = headingInstrRow94.createCell(1);
				cell417.setCellStyle(whiteStyle);
				cell417.setCellValue("Private Land Acquisition");

				Cell cell418 = headingInstrRow94.createCell(3);
				cell418.setCellStyle(whiteStyle);
				cell418.setCellValue("Forest tree rate");

				Cell cell419 = headingInstrRow94.createCell(2);
				cell419.setCellStyle(whiteStyle);
				cell419.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow95 = Instruction.createRow(97);

				Cell cell420 = headingInstrRow95.createCell(0);
				cell420.setCellStyle(whiteStyle);
				cell420.setCellValue(96);

				Cell cell421 = headingInstrRow95.createCell(1);
				cell421.setCellStyle(whiteStyle);
				cell421.setCellValue("Private Land Acquisition");

				Cell cell422 = headingInstrRow95.createCell(3);
				cell422.setCellStyle(whiteStyle);
				cell422.setCellValue("Forest tree compensation");

				Cell cell423 = headingInstrRow95.createCell(2);
				cell423.setCellStyle(whiteStyle);
				cell423.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow96 = Instruction.createRow(98);

				Cell cell424 = headingInstrRow96.createCell(0);
				cell424.setCellStyle(whiteStyle);
				cell424.setCellValue(97);

				Cell cell425 = headingInstrRow96.createCell(1);
				cell425.setCellStyle(whiteStyle);
				cell425.setCellValue("Private Land Acquisition");

				Cell cell426 = headingInstrRow96.createCell(3);
				cell426.setCellStyle(whiteStyle);
				cell426.setCellValue("Structure compensation");

				Cell cell427 = headingInstrRow96.createCell(2);
				cell427.setCellStyle(whiteStyle);
				cell427.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow97 = Instruction.createRow(99);

				Cell cell428 = headingInstrRow97.createCell(0);
				cell428.setCellStyle(whiteStyle);
				cell428.setCellValue(98);

				Cell cell429 = headingInstrRow97.createCell(1);
				cell429.setCellStyle(whiteStyle);
				cell429.setCellValue("Private Land Acquisition");

				Cell cell430 = headingInstrRow97.createCell(3);
				cell430.setCellStyle(whiteStyle);
				cell430.setCellValue("Borewell compensation");

				Cell cell431 = headingInstrRow97.createCell(2);
				cell431.setCellStyle(whiteStyle);
				cell431.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow98 = Instruction.createRow(100);

				Cell cell432 = headingInstrRow98.createCell(0);
				cell432.setCellStyle(whiteStyle);
				cell432.setCellValue(99);

				Cell cell433 = headingInstrRow98.createCell(1);
				cell433.setCellStyle(whiteStyle);
				cell433.setCellValue("Private Land Acquisition");

				Cell cell434 = headingInstrRow98.createCell(3);
				cell434.setCellStyle(whiteStyle);
				cell434.setCellValue("Total Compensation");

				Cell cell435 = headingInstrRow98.createCell(2);
				cell435.setCellStyle(whiteStyle);
				cell435.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow99 = Instruction.createRow(101);

				Cell cell436 = headingInstrRow99.createCell(0);
				cell436.setCellStyle(whiteStyle);
				cell436.setCellValue(100);

				Cell cell437 = headingInstrRow99.createCell(1);
				cell437.setCellStyle(whiteStyle);
				cell437.setCellValue("Private Land Acquisition");

				Cell cell438 = headingInstrRow99.createCell(3);
				cell438.setCellStyle(whiteStyle);
				cell438.setCellValue("Consent from Owner");

				Cell cell439 = headingInstrRow99.createCell(2);
				cell439.setCellStyle(whiteStyle);
				cell439.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow100 = Instruction.createRow(102);

				Cell cell440 = headingInstrRow100.createCell(0);
				cell440.setCellStyle(whiteStyle);
				cell440.setCellValue(101);

				Cell cell441 = headingInstrRow100.createCell(1);
				cell441.setCellStyle(whiteStyle);
				cell441.setCellValue("Private Land Acquisition");

				Cell cell442 = headingInstrRow100.createCell(3);
				cell442.setCellStyle(whiteStyle);
				cell442.setCellValue("Legal Search Report");

				Cell cell443 = headingInstrRow100.createCell(2);
				cell443.setCellStyle(whiteStyle);
				cell443.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow101 = Instruction.createRow(103);

				Cell cell444 = headingInstrRow101.createCell(0);
				cell444.setCellStyle(whiteStyle);
				cell444.setCellValue(102);

				Cell cell445 = headingInstrRow101.createCell(1);
				cell445.setCellStyle(whiteStyle);
				cell445.setCellValue("Private Land Acquisition");

				Cell cell446 = headingInstrRow101.createCell(3);
				cell446.setCellStyle(whiteStyle);
				cell446.setCellValue("Date of Registration");

				Cell cell447 = headingInstrRow101.createCell(2);
				cell447.setCellStyle(whiteStyle);
				cell447.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow102 = Instruction.createRow(104);

				Cell cell448 = headingInstrRow102.createCell(0);
				cell448.setCellStyle(whiteStyle);
				cell448.setCellValue(103);

				Cell cell449 = headingInstrRow102.createCell(1);
				cell449.setCellStyle(whiteStyle);
				cell449.setCellValue("Private Land Acquisition");

				Cell cell450 = headingInstrRow102.createCell(3);
				cell450.setCellStyle(whiteStyle);
				cell450.setCellValue("Date of Possession");

				Cell cell451 = headingInstrRow102.createCell(2);
				cell451.setCellStyle(whiteStyle);
				cell451.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow103 = Instruction.createRow(105);

				Cell cell452 = headingInstrRow103.createCell(0);
				cell452.setCellStyle(whiteStyle);
				cell452.setCellValue(104);

				Cell cell453 = headingInstrRow103.createCell(1);
				cell453.setCellStyle(whiteStyle);
				cell453.setCellValue("Government Land Acquisition");

				Cell cell454 = headingInstrRow103.createCell(3);
				cell454.setCellStyle(whiteStyle);
				cell454.setCellValue("LA_ID");

				Cell cell455 = headingInstrRow103.createCell(2);
				cell455.setCellStyle(whiteStyle);
				cell455.setCellValue("INPUT 2: Land Acquisition Unique ID ");

				XSSFRow headingInstrRow104 = Instruction.createRow(106);

				Cell cell456 = headingInstrRow104.createCell(0);
				cell456.setCellStyle(whiteStyle);
				cell456.setCellValue(105);

				Cell cell457 = headingInstrRow104.createCell(1);
				cell457.setCellStyle(whiteStyle);
				cell457.setCellValue("Government Land Acquisition");

				Cell cell458 = headingInstrRow104.createCell(3);
				cell458.setCellStyle(whiteStyle);
				cell458.setCellValue("Proposal Submission");

				Cell cell459 = headingInstrRow104.createCell(2);
				cell459.setCellStyle(whiteStyle);
				cell459.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow105 = Instruction.createRow(107);

				Cell cell460 = headingInstrRow105.createCell(0);
				cell460.setCellStyle(whiteStyle);
				cell460.setCellValue(106);

				Cell cell461 = headingInstrRow105.createCell(1);
				cell461.setCellStyle(whiteStyle);
				cell461.setCellValue("Government Land Acquisition");

				Cell cell462 = headingInstrRow105.createCell(3);
				cell462.setCellStyle(whiteStyle);
				cell462.setCellValue("Valuation Date");

				Cell cell463 = headingInstrRow105.createCell(2);
				cell463.setCellStyle(whiteStyle);
				cell463.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow106 = Instruction.createRow(108);

				Cell cell464 = headingInstrRow106.createCell(0);
				cell464.setCellStyle(whiteStyle);
				cell464.setCellValue(107);

				Cell cell465 = headingInstrRow106.createCell(1);
				cell465.setCellStyle(whiteStyle);
				cell465.setCellValue("Government Land Acquisition");

				Cell cell466 = headingInstrRow106.createCell(3);
				cell466.setCellStyle(whiteStyle);
				cell466.setCellValue("Letter for Payment");

				Cell cell467 = headingInstrRow106.createCell(2);
				cell467.setCellStyle(whiteStyle);
				cell467.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow107 = Instruction.createRow(109);

				Cell cell468 = headingInstrRow107.createCell(0);
				cell468.setCellStyle(whiteStyle);
				cell468.setCellValue(108);

				Cell cell469 = headingInstrRow107.createCell(1);
				cell469.setCellStyle(whiteStyle);
				cell469.setCellValue("Government Land Acquisition");

				Cell cell470 = headingInstrRow107.createCell(3);
				cell470.setCellStyle(whiteStyle);
				cell470.setCellValue("Amount Demanded");

				Cell cell471 = headingInstrRow107.createCell(2);
				cell471.setCellStyle(whiteStyle);
				cell471.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow108 = Instruction.createRow(110);

				Cell cell472 = headingInstrRow108.createCell(0);
				cell472.setCellStyle(whiteStyle);
				cell472.setCellValue(109);

				Cell cell473 = headingInstrRow108.createCell(1);
				cell473.setCellStyle(whiteStyle);
				cell473.setCellValue("Government Land Acquisition");

				Cell cell474 = headingInstrRow108.createCell(3);
				cell474.setCellStyle(whiteStyle);
				cell474.setCellValue("Approval for Payment");

				Cell cell475 = headingInstrRow108.createCell(2);
				cell475.setCellStyle(whiteStyle);
				cell475.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow109 = Instruction.createRow(111);

				Cell cell476 = headingInstrRow109.createCell(0);
				cell476.setCellStyle(whiteStyle);
				cell476.setCellValue(110);

				Cell cell477 = headingInstrRow109.createCell(1);
				cell477.setCellStyle(whiteStyle);
				cell477.setCellValue("Government Land Acquisition");

				Cell cell478 = headingInstrRow109.createCell(3);
				cell478.setCellStyle(whiteStyle);
				cell478.setCellValue("Payment date");

				Cell cell479 = headingInstrRow109.createCell(2);
				cell479.setCellStyle(whiteStyle);
				cell479.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow110 = Instruction.createRow(112);

				Cell cell480 = headingInstrRow110.createCell(0);
				cell480.setCellStyle(whiteStyle);
				cell480.setCellValue(111);

				Cell cell481 = headingInstrRow110.createCell(1);
				cell481.setCellStyle(whiteStyle);
				cell481.setCellValue("Government Land Acquisition");

				Cell cell482 = headingInstrRow110.createCell(3);
				cell482.setCellStyle(whiteStyle);
				cell482.setCellValue("Amount Paid");

				Cell cell483 = headingInstrRow110.createCell(2);
				cell483.setCellStyle(whiteStyle);
				cell483.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow111 = Instruction.createRow(113);

				Cell cell484 = headingInstrRow111.createCell(0);
				cell484.setCellStyle(whiteStyle);
				cell484.setCellValue(112);

				Cell cell485 = headingInstrRow111.createCell(1);
				cell485.setCellStyle(whiteStyle);
				cell485.setCellValue("Government Land Acquisition");

				Cell cell486 = headingInstrRow111.createCell(3);
				cell486.setCellStyle(whiteStyle);
				cell486.setCellValue("Possession Date");

				Cell cell487 = headingInstrRow111.createCell(2);
				cell487.setCellStyle(whiteStyle);
				cell487.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow112 = Instruction.createRow(114);

				Cell cell488 = headingInstrRow112.createCell(0);
				cell488.setCellStyle(whiteStyle);
				cell488.setCellValue(113);

				Cell cell489 = headingInstrRow112.createCell(1);
				cell489.setCellStyle(whiteStyle);
				cell489.setCellValue("Forest  Land Acquisition");

				Cell cell490 = headingInstrRow112.createCell(3);
				cell490.setCellStyle(whiteStyle);
				cell490.setCellValue("LA_ID");

				Cell cell491 = headingInstrRow112.createCell(2);
				cell491.setCellStyle(whiteStyle);
				cell491.setCellValue("INPUT 2: Land Acquisition Unique ID ");

				XSSFRow headingInstrRow113 = Instruction.createRow(115);

				Cell cell492 = headingInstrRow113.createCell(0);
				cell492.setCellStyle(whiteStyle);
				cell492.setCellValue(114);

				Cell cell493 = headingInstrRow113.createCell(1);
				cell493.setCellStyle(whiteStyle);
				cell493.setCellValue("Forest  Land Acquisition");

				Cell cell494 = headingInstrRow113.createCell(3);
				cell494.setCellStyle(whiteStyle);
				cell494.setCellValue("On line Submission");

				Cell cell495 = headingInstrRow113.createCell(2);
				cell495.setCellStyle(whiteStyle);
				cell495.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow114 = Instruction.createRow(116);

				Cell cell496 = headingInstrRow114.createCell(0);
				cell496.setCellStyle(whiteStyle);
				cell496.setCellValue(115);

				Cell cell497 = headingInstrRow114.createCell(1);
				cell497.setCellStyle(whiteStyle);
				cell497.setCellValue("Forest  Land Acquisition");

				Cell cell498 = headingInstrRow114.createCell(3);
				cell498.setCellStyle(whiteStyle);
				cell498.setCellValue("Submission Date to DyCFO");

				Cell cell499 = headingInstrRow114.createCell(2);
				cell499.setCellStyle(whiteStyle);
				cell499.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow115 = Instruction.createRow(117);

				Cell cell500 = headingInstrRow115.createCell(0);
				cell500.setCellStyle(whiteStyle);
				cell500.setCellValue(116);

				Cell cell501 = headingInstrRow115.createCell(1);
				cell501.setCellStyle(whiteStyle);
				cell501.setCellValue("Forest  Land Acquisition");

				Cell cell502 = headingInstrRow115.createCell(3);
				cell502.setCellStyle(whiteStyle);
				cell502.setCellValue("Submission Date to CCF Thane");

				Cell cell503 = headingInstrRow115.createCell(2);
				cell503.setCellStyle(whiteStyle);
				cell503.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow116 = Instruction.createRow(118);

				Cell cell504 = headingInstrRow116.createCell(0);
				cell504.setCellStyle(whiteStyle);
				cell504.setCellValue(117);

				Cell cell505 = headingInstrRow116.createCell(1);
				cell505.setCellStyle(whiteStyle);
				cell505.setCellValue("Forest  Land Acquisition");

				Cell cell506 = headingInstrRow116.createCell(3);
				cell506.setCellStyle(whiteStyle);
				cell506.setCellValue("Submission Date to Nodal Officer/CCF Nagpur");

				Cell cell507 = headingInstrRow116.createCell(2);
				cell507.setCellStyle(whiteStyle);
				cell507.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow117 = Instruction.createRow(119);

				Cell cell508 = headingInstrRow117.createCell(0);
				cell508.setCellStyle(whiteStyle);
				cell508.setCellValue(118);

				Cell cell509 = headingInstrRow117.createCell(1);
				cell509.setCellStyle(whiteStyle);
				cell509.setCellValue("Forest  Land Acquisition");

				Cell cell510 = headingInstrRow117.createCell(3);
				cell510.setCellStyle(whiteStyle);
				cell510.setCellValue("Submission Date to Revenue Secretary Mantralaya");

				Cell cell5211 = headingInstrRow117.createCell(2);
				cell5211.setCellStyle(whiteStyle);
				cell5211.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow118 = Instruction.createRow(120);

				Cell cell512 = headingInstrRow118.createCell(0);
				cell512.setCellStyle(whiteStyle);
				cell512.setCellValue(119);

				Cell cell513 = headingInstrRow118.createCell(1);
				cell513.setCellStyle(whiteStyle);
				cell513.setCellValue("Forest  Land Acquisition");

				Cell cell514 = headingInstrRow118.createCell(3);
				cell514.setCellStyle(whiteStyle);
				cell514.setCellValue("Submission Date to Regional Office Nagpur");

				Cell cell515 = headingInstrRow118.createCell(2);
				cell515.setCellStyle(whiteStyle);
				cell515.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow119 = Instruction.createRow(121);

				Cell cell516 = headingInstrRow119.createCell(0);
				cell516.setCellStyle(whiteStyle);
				cell516.setCellValue(120);

				Cell cell517 = headingInstrRow119.createCell(1);
				cell517.setCellStyle(whiteStyle);
				cell517.setCellValue("Forest  Land Acquisition");

				Cell cell518 = headingInstrRow119.createCell(3);
				cell518.setCellStyle(whiteStyle);
				cell518.setCellValue("Date of Approval by Regional Office Nagpur");

				Cell cell519 = headingInstrRow119.createCell(2);
				cell519.setCellStyle(whiteStyle);
				cell519.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow120 = Instruction.createRow(122);

				Cell cell520 = headingInstrRow120.createCell(0);
				cell520.setCellStyle(whiteStyle);
				cell520.setCellValue(121);

				Cell cell521 = headingInstrRow120.createCell(1);
				cell521.setCellStyle(whiteStyle);
				cell521.setCellValue("Forest  Land Acquisition");

				Cell cell522 = headingInstrRow120.createCell(3);
				cell522.setCellStyle(whiteStyle);
				cell522.setCellValue("Valuation by DyCFO");

				Cell cell523 = headingInstrRow120.createCell(2);
				cell523.setCellStyle(whiteStyle);
				cell523.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow121 = Instruction.createRow(123);

				Cell cell524 = headingInstrRow121.createCell(0);
				cell524.setCellStyle(whiteStyle);
				cell524.setCellValue(122);

				Cell cell525 = headingInstrRow121.createCell(1);
				cell525.setCellStyle(whiteStyle);
				cell525.setCellValue("Forest  Land Acquisition");

				Cell cell526 = headingInstrRow121.createCell(3);
				cell526.setCellStyle(whiteStyle);
				cell526.setCellValue("Demanded Amount");

				Cell cell527 = headingInstrRow121.createCell(2);
				cell527.setCellStyle(whiteStyle);
				cell527.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow122 = Instruction.createRow(124);

				Cell cell528 = headingInstrRow122.createCell(0);
				cell528.setCellStyle(whiteStyle);
				cell528.setCellValue(123);

				Cell cell529 = headingInstrRow122.createCell(1);
				cell529.setCellStyle(whiteStyle);
				cell529.setCellValue("Forest  Land Acquisition");

				Cell cell530 = headingInstrRow122.createCell(3);
				cell530.setCellStyle(whiteStyle);
				cell530.setCellValue("Approval for Payment");

				Cell cell531 = headingInstrRow122.createCell(2);
				cell531.setCellStyle(whiteStyle);
				cell531.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow123 = Instruction.createRow(125);

				Cell cell532 = headingInstrRow123.createCell(0);
				cell532.setCellStyle(whiteStyle);
				cell532.setCellValue(124);

				Cell cell533 = headingInstrRow123.createCell(1);
				cell533.setCellStyle(whiteStyle);
				cell533.setCellValue("Forest  Land Acquisition");

				Cell cell534 = headingInstrRow123.createCell(3);
				cell534.setCellStyle(whiteStyle);
				cell534.setCellValue("Payment Date");

				Cell cell535 = headingInstrRow123.createCell(2);
				cell535.setCellStyle(whiteStyle);
				cell535.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow124 = Instruction.createRow(126);

				Cell cell536 = headingInstrRow124.createCell(0);
				cell536.setCellStyle(whiteStyle);
				cell536.setCellValue(125);

				Cell cell537 = headingInstrRow124.createCell(1);
				cell537.setCellStyle(whiteStyle);
				cell537.setCellValue("Forest  Land Acquisition");

				Cell cell538 = headingInstrRow124.createCell(3);
				cell538.setCellStyle(whiteStyle);
				cell538.setCellValue("Payment Amount");

				Cell cell539 = headingInstrRow124.createCell(2);
				cell539.setCellStyle(whiteStyle);
				cell539.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow125 = Instruction.createRow(127);

				Cell cell540 = headingInstrRow125.createCell(0);
				cell540.setCellStyle(whiteStyle);
				cell540.setCellValue(126);

				Cell cell541 = headingInstrRow125.createCell(1);
				cell541.setCellStyle(whiteStyle);
				cell541.setCellValue("Forest  Land Acquisition");

				Cell cell542 = headingInstrRow125.createCell(3);
				cell542.setCellStyle(whiteStyle);
				cell542.setCellValue("Possession Date");

				Cell cell543 = headingInstrRow125.createCell(2);
				cell543.setCellStyle(whiteStyle);
				cell543.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow126 = Instruction.createRow(128);

				Cell cell544 = headingInstrRow126.createCell(0);
				cell544.setCellStyle(whiteStyle);
				cell544.setCellValue(127);

				Cell cell545 = headingInstrRow126.createCell(1);
				cell545.setCellStyle(whiteStyle);
				cell545.setCellValue("Railway Land Aquisition");

				Cell cell546 = headingInstrRow126.createCell(3);
				cell546.setCellStyle(whiteStyle);
				cell546.setCellValue("LA_ID");

				Cell cell547 = headingInstrRow126.createCell(2);
				cell547.setCellStyle(whiteStyle);
				cell547.setCellValue("INPUT 2: Land Acquisition Unique ID ");

				XSSFRow headingInstrRow127 = Instruction.createRow(129);

				Cell cell548 = headingInstrRow127.createCell(0);
				cell548.setCellStyle(whiteStyle);
				cell548.setCellValue(128);

				Cell cell549 = headingInstrRow127.createCell(1);
				cell549.setCellStyle(whiteStyle);
				cell549.setCellValue("Railway Land Aquisition");

				Cell cell550 = headingInstrRow127.createCell(3);
				cell550.setCellStyle(whiteStyle);
				cell550.setCellValue("On line Submission");

				Cell cell551 = headingInstrRow127.createCell(2);
				cell551.setCellStyle(whiteStyle);
				cell551.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow128 = Instruction.createRow(130);

				Cell cell552 = headingInstrRow128.createCell(0);
				cell552.setCellStyle(whiteStyle);
				cell552.setCellValue(129);

				Cell cell553 = headingInstrRow128.createCell(1);
				cell553.setCellStyle(whiteStyle);
				cell553.setCellValue("Railway Land Aquisition");

				Cell cell554 = headingInstrRow128.createCell(3);
				cell554.setCellStyle(whiteStyle);
				cell554.setCellValue("Submission Date to DyCFO");

				Cell cell555 = headingInstrRow128.createCell(2);
				cell555.setCellStyle(whiteStyle);
				cell555.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow129 = Instruction.createRow(131);

				Cell cell556 = headingInstrRow129.createCell(0);
				cell556.setCellStyle(whiteStyle);
				cell556.setCellValue(130);

				Cell cell557 = headingInstrRow129.createCell(1);
				cell557.setCellStyle(whiteStyle);
				cell557.setCellValue("Railway Land Aquisition");

				Cell cell558 = headingInstrRow129.createCell(3);
				cell558.setCellStyle(whiteStyle);
				cell558.setCellValue("Submission Date to CCF Thane");

				Cell cell559 = headingInstrRow129.createCell(2);
				cell559.setCellStyle(whiteStyle);
				cell559.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow130 = Instruction.createRow(132);

				Cell cell560 = headingInstrRow130.createCell(0);
				cell560.setCellStyle(whiteStyle);
				cell560.setCellValue(131);

				Cell cell561 = headingInstrRow130.createCell(1);
				cell561.setCellStyle(whiteStyle);
				cell561.setCellValue("Railway Land Aquisition");

				Cell cell562 = headingInstrRow130.createCell(3);
				cell562.setCellStyle(whiteStyle);
				cell562.setCellValue("Submission Date to Nodal Officer/CCF Nagpur");

				Cell cell563 = headingInstrRow130.createCell(2);
				cell563.setCellStyle(whiteStyle);
				cell563.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow131 = Instruction.createRow(133);

				Cell cell564 = headingInstrRow131.createCell(0);
				cell564.setCellStyle(whiteStyle);
				cell564.setCellValue(132);

				Cell cell565 = headingInstrRow131.createCell(1);
				cell565.setCellStyle(whiteStyle);
				cell565.setCellValue("Railway Land Aquisition");

				Cell cell566 = headingInstrRow131.createCell(3);
				cell566.setCellStyle(whiteStyle);
				cell566.setCellValue("Submission Date to Revenue Secretary Mantralaya");

				Cell cell567 = headingInstrRow131.createCell(2);
				cell567.setCellStyle(whiteStyle);
				cell567.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow132 = Instruction.createRow(134);

				Cell cell568 = headingInstrRow132.createCell(0);
				cell568.setCellStyle(whiteStyle);
				cell568.setCellValue(133);

				Cell cell569 = headingInstrRow132.createCell(1);
				cell569.setCellStyle(whiteStyle);
				cell569.setCellValue("Railway Land Aquisition");

				Cell cell570 = headingInstrRow132.createCell(3);
				cell570.setCellStyle(whiteStyle);
				cell570.setCellValue("Submission Date to Regional Office Nagpur");

				Cell cell571 = headingInstrRow132.createCell(2);
				cell571.setCellStyle(whiteStyle);
				cell571.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow133 = Instruction.createRow(135);

				Cell cell572 = headingInstrRow133.createCell(0);
				cell572.setCellStyle(whiteStyle);
				cell572.setCellValue(134);

				Cell cell573 = headingInstrRow133.createCell(1);
				cell573.setCellStyle(whiteStyle);
				cell573.setCellValue("Railway Land Aquisition");

				Cell cell574 = headingInstrRow133.createCell(3);
				cell574.setCellStyle(whiteStyle);
				cell574.setCellValue("Date of Approval by Regional Office Nagpur");

				Cell cell575 = headingInstrRow133.createCell(2);
				cell575.setCellStyle(whiteStyle);
				cell575.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow134 = Instruction.createRow(136);

				Cell cell576 = headingInstrRow134.createCell(0);
				cell576.setCellStyle(whiteStyle);
				cell576.setCellValue(135);

				Cell cell577 = headingInstrRow134.createCell(1);
				cell577.setCellStyle(whiteStyle);
				cell577.setCellValue("Railway Land Aquisition");

				Cell cell578 = headingInstrRow134.createCell(3);
				cell578.setCellStyle(whiteStyle);
				cell578.setCellValue("Valuation by DyCFO");

				Cell cell579 = headingInstrRow134.createCell(2);
				cell579.setCellStyle(whiteStyle);
				cell579.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow135 = Instruction.createRow(137);

				Cell cell580 = headingInstrRow135.createCell(0);
				cell580.setCellStyle(whiteStyle);
				cell580.setCellValue(136);

				Cell cell581 = headingInstrRow135.createCell(1);
				cell581.setCellStyle(whiteStyle);
				cell581.setCellValue("Railway Land Aquisition");

				Cell cell582 = headingInstrRow135.createCell(3);
				cell582.setCellStyle(whiteStyle);
				cell582.setCellValue("Demanded Amount");

				Cell cell583 = headingInstrRow135.createCell(2);
				cell583.setCellStyle(whiteStyle);
				cell583.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow136 = Instruction.createRow(138);

				Cell cell584 = headingInstrRow136.createCell(0);
				cell584.setCellStyle(whiteStyle);
				cell584.setCellValue(137);

				Cell cell585 = headingInstrRow136.createCell(1);
				cell585.setCellStyle(whiteStyle);
				cell585.setCellValue("Railway Land Aquisition");

				Cell cell586 = headingInstrRow136.createCell(3);
				cell586.setCellStyle(whiteStyle);
				cell586.setCellValue("Approval for Payment");

				Cell cell587 = headingInstrRow136.createCell(2);
				cell587.setCellStyle(whiteStyle);
				cell587.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow137 = Instruction.createRow(139);

				Cell cell588 = headingInstrRow137.createCell(0);
				cell588.setCellStyle(whiteStyle);
				cell588.setCellValue(138);

				Cell cell589 = headingInstrRow137.createCell(1);
				cell589.setCellStyle(whiteStyle);
				cell589.setCellValue("Railway Land Aquisition");

				Cell cell590 = headingInstrRow137.createCell(3);
				cell590.setCellStyle(whiteStyle);
				cell590.setCellValue("Payment Date");

				Cell cell591 = headingInstrRow137.createCell(2);
				cell591.setCellStyle(whiteStyle);
				cell591.setCellValue("Date 'DD/MM/YYYY'");

				XSSFRow headingInstrRow138 = Instruction.createRow(140);

				Cell cell592 = headingInstrRow138.createCell(0);
				cell592.setCellStyle(whiteStyle);
				cell592.setCellValue(139);

				Cell cell593 = headingInstrRow138.createCell(1);
				cell593.setCellStyle(whiteStyle);
				cell593.setCellValue("Railway Land Aquisition");

				Cell cell594 = headingInstrRow138.createCell(3);
				cell594.setCellStyle(whiteStyle);
				cell594.setCellValue("Payment Amount");

				Cell cell595 = headingInstrRow138.createCell(2);
				cell595.setCellStyle(whiteStyle);
				cell595.setCellValue("Amount in Rupees");

				XSSFRow headingInstrRow139 = Instruction.createRow(141);

				Cell cell596 = headingInstrRow139.createCell(0);
				cell596.setCellStyle(whiteStyle);
				cell596.setCellValue(140);

				Cell cell597 = headingInstrRow139.createCell(1);
				cell597.setCellStyle(whiteStyle);
				cell597.setCellValue("Railway Land Aquisition");

				Cell cell598 = headingInstrRow139.createCell(3);
				cell598.setCellStyle(whiteStyle);
				cell598.setCellValue("Possession Date");

				Cell cell599 = headingInstrRow139.createCell(2);
				cell599.setCellStyle(whiteStyle);
				cell599.setCellValue("Date 'DD/MM/YYYY'");

				Instruction.setColumnWidth(0, 25 * 100);
				Instruction.setColumnWidth(1, 25 * 300);
				Instruction.setColumnWidth(2, 25 * 600);
				Instruction.setColumnWidth(3, 25 * 600);

				XSSFRow headingRow = Landsheet.createRow(1);
				String headerString = "Work ID^LA_ID^Survey Number^Type of Land^Sub Category of Land^Area^Area to be Acquired^Area Acquired^Land Status^Chainage From^Chainage To^Village^Taluka^Latitude^Longitude^Dy SLR^SDO^Collector^Proposal submission Date to collector^JM Fee Letter received Date^JM Fee Amount^JM Fee Paid Date^JM Start Date^JM Completion Date^JM Sheet Date to SDO^JM Remarks^JM Approval^Special Feature^Remarks^Issues";

				String[] firstHeaderStringArr = headerString.split("\\^");

				for (int i = 0; i < firstHeaderStringArr.length; i++) {

					Cell cell = headingRow.createCell(i);
					if (i == 0 || i == 1 || i == 3 || i == 4) {
						cell.setCellStyle(redStyle);
					} else {
						cell.setCellStyle(greenStyle);
					}
					cell.setCellValue(firstHeaderStringArr[i]);
				}

				XSSFRow headingRow1 = privateIRASheet.createRow(1);
				String headerString1 = "LA_ID^Collector^Submission of Proposal to GM.^Approval of GM^Draft Letter to CE/Con for Approval^"
						+ "Date of Approval of CE/Construction^Date of Uploading of  Gazette notification^Publication in gazette^Date of Proposal to DC for nomination^"
						+ "Date of Nomination of competent Authority.^Draft Letter to CE/Con for Approval^Date of Approval of CE/Construction^"
						+ "Date of Uploading of  Gazette notification^Publication in gazette^Date of Submission of draft notification to CALA^Approval of CALA^"
						+ "Draft Letter to CE/Con for Approval^Date of Approval of CE/Construction^Date of Uploading of  Gazette notification^Publication in gazette^"
						+ "Publication in 2 Local Newspapers^Pasting of notification in villages^Receipt of Grievances^Disposal of Grievances^"
						+ "Date of Submission of draft notification to CALA^Approval of CALA^Draft Letter to CE/Con for Approval^Date of Approval of CE/Construction^"
						+ "Date of Uploading of  Gazette notification^Publication in gazette^Publication of notice in 2 Local News papers ^"
						+ "Date of Submission of draft notification to CALA^Approval of CALA^Draft Letter to CE/Con for Approval^Date of Approval of CE/Construction^"
						+ "Date of Uploading of  Gazette notification^Publication in gazette^Publication of notice in 2 Local News papers";

				String[] secondHeaderStringArr = headerString1.split("\\^");

				for (int i = 0; i < secondHeaderStringArr.length; i++) {
					Cell cell = headingRow1.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(secondHeaderStringArr[i]);
				}

				XSSFRow headingRow2 = privateValSheet.createRow(1);
				String headerString2 = "LA_ID^Forest Tree Survey^Forest Tree Valuation^Horticulture Tree Survey^Horticulture Tree Valuation^Structure Survey^"
						+ "Structure Valuation^Borewell Survey^Borewell Valuation^Date of RFP to ADTP^Date of Rate Fixation of Land^"
						+ "SDO demand for payment^Date of Approval for Payment^Payment Amount^Payment Date";

				String[] thirdHeaderStringArr = headerString2.split("\\^");

				for (int i = 0; i < thirdHeaderStringArr.length; i++) {
					Cell cell = headingRow2.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(thirdHeaderStringArr[i]);
				}

				XSSFRow headingRow3 = privateLandSheet.createRow(1);
				String headerString3 = "LA_ID^Name of Owner^Basic Rate^100% Solatium^Extra 25%^Total Rate/m2^Land Compensation^Agriculture tree nos^"
						+ "Agriculture tree rate^Agriculture tree compensation^Forest tree nos^Forest tree rate^Forest tree compensation^Structure compensation^"
						+ "Borewell compensation^Total Compensation^Consent from Owner^Legal Search Report^Date of Registration^Date of Possession";

				String[] fourthHeaderStringArr = headerString3.split("\\^");

				for (int i = 0; i < fourthHeaderStringArr.length; i++) {
					Cell cell = headingRow3.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(fourthHeaderStringArr[i]);
				}

				XSSFRow headingRow4 = govSheet.createRow(1);
				String headerString4 = "LA_ID^Proposal Submission^Valuation Date^Letter for Payment^Amount Demanded^"
						+ "Approval for Payment^Payment date^Amount Paid^Possession Date";

				String[] fifthHeaderStringArr = headerString4.split("\\^");

				for (int i = 0; i < fifthHeaderStringArr.length; i++) {
					Cell cell = headingRow4.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(fifthHeaderStringArr[i]);
				}

				XSSFRow headingRow5 = forestSheet.createRow(1);
				String headerString5 = "LA_ID^On line Submission^Submission Date to DyCFO^Submission Date to CCF Thane^Submission Date to Nodal Officer/CCF Nagpur^"
						+ "Submission Date to Revenue Secretary Mantralaya^Submission Date to Regional Office Nagpur^Date of Approval by Regional Office Nagpur^"
						+ "Valuation by DyCFO^Demanded Amount^Approval for Payment^Payment Date^Payment Amount^Possession Date";

				String[] sixthHeaderStringArr = headerString5.split("\\^");

				for (int i = 0; i < sixthHeaderStringArr.length; i++) {
					Cell cell = headingRow5.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(sixthHeaderStringArr[i]);
				}

				XSSFRow headingRow6 = railwaySheet.createRow(1);
				String headerString6 = "LA_ID^On line Submission^Submission Date to DyCFO^Submission Date to CCF Thane^Submission Date to Nodal Officer/CCF Nagpur^"
						+ "Submission Date to Revenue Secretary Mantralaya^Submission Date to Regional Office Nagpur^Date of Approval by Regional Office Nagpur^"
						+ "Valuation by DyCFO^Demanded Amount^Approval for Payment^Payment Date^Payment Amount^Possession Date";

				String[] seventhHeaderStringArr = headerString6.split("\\^");

				for (int i = 0; i < seventhHeaderStringArr.length; i++) {
					Cell cell = headingRow6.createCell(i);
					cell.setCellStyle(greenStyle);
					cell.setCellValue(seventhHeaderStringArr[i]);
				}

				short rowNo3 = 2;
				for (LandAcquisition privateIRA : dataList) {
					String la_id = privateIRA.getLa_id();
					privateIRAList = landAquisitionService.geprivateIRAList(la_id);
					/*
					 * if(privateIRAList.size()< 1) { int a = 0; XSSFRow row =
					 * privateIRASheet.createRow(rowNo3); for(int k = 0;k <
					 * secondHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * privateIRASheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(secondHeaderStringArr.length - 1 ))); break; }
					 */

					for (LandAcquisition obj : privateIRAList) {
						XSSFRow row = privateIRASheet.createRow(rowNo3);
						int a = 0;

						Cell cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getLa_id_fk());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPrivate_ira_collector());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getSubmission_of_proposal_to_GM());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getApproval_of_GM());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_rp());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_rp());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_rp());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_rp());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_proposal_to_DC_for_nomination());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_nomination_of_competenta_authority());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_ca());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_ca());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_ca());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_ca());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_submission_of_draft_notification_to_CALA());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getApproval_of_CALA_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_2_local_news_papers_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPasting_of_notification_in_villages_20a());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getReceipt_of_grievances());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDisposal_of_grievances());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_submission_of_draft_notification_to_CALA_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getApproval_of_CALA_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_of_notice_in_2_local_news_papers_20e());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_submission_of_draft_notification_to_CALA_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getApproval_of_CALA_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDraft_letter_to_con_for_approval_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_approval_of_construction_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getDate_of_uploading_of_gazette_notification_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_in_gazette_20f());

						cell2 = row.createCell(a++);
						cell2.setCellStyle(sectionStyle);
						cell2.setCellValue(obj.getPublication_of_notice_in_2_local_news_papers_20f());

						rowNo3++;
					}
				}

				short rowNo2 = 2;
				for (LandAcquisition privateVal : dataList) {
					String la_id = privateVal.getLa_id();
					privateValList = landAquisitionService.getPrivateValList(la_id);
					/*
					 * if(privateValList.size()< 1) { int a = 0; XSSFRow row =
					 * privateValSheet.createRow(rowNo2); for(int k = 0;k <
					 * thirdHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * privateValSheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(thirdHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : privateValList) {
						XSSFRow row = privateValSheet.createRow(rowNo2);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_tree_survey());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_tree_valuation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getHorticulture_tree_survey());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getHorticulture_tree_valuation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getStructure_survey());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getStructure_valuation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getBorewell_survey());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getBorewell_valuation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_rfp_to_adtp());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_rate_fixation_of_land());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getSdo_demand_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_approval_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getPayment_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getPrivate_payment_date());

						rowNo2++;
					}
				}

				short rowNo4 = 2;
				for (LandAcquisition privateLand : dataList) {
					String la_id = privateLand.getLa_id();
					privateLandList = landAquisitionService.getPrivateLandList(la_id);
					/*
					 * if(privateLandList.size()< 1) { int a = 0; XSSFRow row =
					 * privateLandSheet.createRow(rowNo4); for(int k = 0;k <
					 * fourthHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * privateLandSheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(fourthHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : privateLandList) {
						XSSFRow row = privateLandSheet.createRow(rowNo4);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getName_of_the_owner());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getBasic_rate());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getHundred_percent_Solatium());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getExtra_25_percent());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getTotal_rate_divide_m2());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLand_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(String.valueOf(obj.getAgriculture_tree_nos()));

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getAgriculture_tree_rate());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getAgriculture_tree_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(String.valueOf(obj.getForest_tree_nos()));

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_tree_rate());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_tree_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getStructure_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getBorewell_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getTotal_compensation());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getConsent_from_owner());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLegal_search_report());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_registration());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getDate_of_possession());

						rowNo4++;
					}
				}

				short rowNo5 = 2;
				for (LandAcquisition gov : dataList) {
					String la_id = gov.getLa_id();
					govList = landAquisitionService.getGovList(la_id);
					/*
					 * if(govList.size()< 1) { int a = 0; XSSFRow row = govSheet.createRow(rowNo5);
					 * for(int k = 0;k < fifthHeaderStringArr.length;k++) { Cell cell2 =
					 * row.createCell(a++); cell2.setCellStyle(whiteStyle);
					 * cell2.setCellValue("No Data"); } govSheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(fifthHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : govList) {
						XSSFRow row = govSheet.createRow(rowNo5);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getProposal_submission());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getValuation_date());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLetter_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getAmount_demanded());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getApproval_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getPayment_date());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getAmount_paid());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getPossession_date());

						rowNo5++;
					}
				}

				short rowNo6 = 2;
				for (LandAcquisition forest : dataList) {
					String la_id = forest.getLa_id();
					forestList = landAquisitionService.getForestList(la_id);
					/*
					 * if(forestList.size()< 1) { int a = 0; XSSFRow row =
					 * forestSheet.createRow(rowNo6); for(int k = 0;k <
					 * sixthHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * forestSheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(sixthHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : forestList) {
						XSSFRow row = forestSheet.createRow(rowNo6);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_online_submission());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_dycfo());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_ccf_thane());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_nodal_officer());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_revenue_secretary_mantralaya());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_submission_date_to_regional_office_nagpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_date_of_approval_by_regional_office_nagpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_valuation_by_dycfo());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_demanded_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_approval_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_payment_date());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_payment_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getForest_possession_date());

						rowNo6++;
					}
				}

				short rowNo7 = 2;
				for (LandAcquisition railway : dataList) {
					String la_id = railway.getLa_id();
					railwayList = landAquisitionService.getRailwayList(la_id);
					/*
					 * if(railwayList.size()< 1) { int a = 0; XSSFRow row =
					 * railwaySheet.createRow(rowNo7); for(int k = 0;k <
					 * seventhHeaderStringArr.length;k++) { Cell cell2 = row.createCell(a++);
					 * cell2.setCellStyle(whiteStyle); cell2.setCellValue("No Data"); }
					 * railwaySheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(seventhHeaderStringArr.length - 1 ))); break; }
					 */
					for (LandAcquisition obj : railwayList) {
						XSSFRow row = railwaySheet.createRow(rowNo7);
						int b = 0;

						Cell cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getLa_id_fk());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_online_submission());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_DyCFO());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_CCF_Thane());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_nodal_officer_CCF_Nagpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_revenue_secretary_mantralaya());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_submission_date_to_regional_office_nagpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_date_of_approval_by_Rregional_Office_agpur());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_valuation_by_DyCFO());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_demanded_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_approval_for_payment());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_payment_date());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_payment_amount());

						cell1 = row.createCell(b++);
						cell1.setCellStyle(sectionStyle);
						cell1.setCellValue(obj.getRailway_possession_date());

						rowNo7++;
					}
				}
				short rowNo = 2;
				for (LandAcquisition obj : dataList) {
					/*
					 * if(dataList.size()< 1) { int a = 0; XSSFRow row = Landsheet.createRow(rowNo);
					 * for(int k = 0;k < firstHeaderStringArr.length;k++) { Cell cell2 =
					 * row.createCell(a++); cell2.setCellStyle(whiteStyle);
					 * cell2.setCellValue("No Data"); } Landsheet.addMergedRegion(new
					 * CellRangeAddress(1,1,0,(firstHeaderStringArr.length - 1 ))); break; }
					 */
					XSSFRow row = Landsheet.createRow(rowNo);
					int c = 0;

					Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getWork_id_fk() + " - " + obj.getWork_short_name());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLa_id());
					cell.setCellStyle(lockedStyle);

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSurvey_number());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getType_of_land());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSub_category_of_land());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getArea_of_plot());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getArea_to_be_acquired());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getArea_acquired());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLa_land_status_fk());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getChainage_from());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getChainage_to());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getVillage());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getTaluka());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLatitude());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLongitude());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDy_slr());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSdo());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCollector());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getProposal_submission_date_to_collector());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_fee_letter_received_date());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_fee_amount());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_fee_paid_date());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_start_date());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_completion_date());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_sheet_date_to_sdo());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_remarks());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getJm_approval());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getSpecial_feature());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRemarks());

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getIssues());

					rowNo++;
				}

				for (int columnIndex = 0; columnIndex < 29; columnIndex++) {
					Landsheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 38; columnIndex++) {
					privateIRASheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 16; columnIndex++) {
					privateValSheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 21; columnIndex++) {
					privateLandSheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 21; columnIndex++) {
					govSheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 16; columnIndex++) {
					forestSheet.setColumnWidth(columnIndex, 25 * 200);
				}
				for (int columnIndex = 0; columnIndex < 16; columnIndex++) {
					railwaySheet.setColumnWidth(columnIndex, 25 * 200);
				}

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
				Date date = new Date();
				String fileName = "Land_Aquisition" + dateFormat.format(date);

				try {
					/*
					 * FileOutputStream fos = new FileOutputStream(fileDirectory +fileName+".xls");
					 * workBook.write(fos); fos.flush();
					 */

					response.setContentType("application/.csv");
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setContentType("application/vnd.ms-excel");
					// add response header
					response.addHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

					// copies all bytes from a file to an output stream
					workBook.write(response.getOutputStream()); // Write workbook to response.
					workBook.close();
					// flushes output stream
					response.getOutputStream().flush();

					// attributes.addFlashAttribute("success",dataExportSucess);
					// response.setContentType("application/vnd.ms-excel");
					// response.setHeader("Content-Disposition", "attachment;
					// filename=filename.xls");
					// XSSFWorkbook workbook = new XSSFWorkbook ();
					// ...
					// Now populate workbook the usual way.
					// ...
					// workbook.write(response.getOutputStream()); // Write workbook to response.
					// workbook.close();
				} catch (FileNotFoundException e) {
					// e.printStackTrace();
					// attributes.addFlashAttribute("error",dataExportInvalid);
				} catch (IOException e) {
					// e.printStackTrace();
					// attributes.addFlashAttribute("error",dataExportError);
				}
			} else {
				// attributes.addFlashAttribute("error",dataExportNoData);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@PostMapping("/add-land-acquisition")
	public boolean addLandAcquisition(@ModelAttribute LandAcquisition obj, HttpSession session) throws Exception {
		// ModelAndView model = new ModelAndView();
		// try{
		// model.setViewName("redirect:/land-acquisition");
		User uObj = (User) session.getAttribute("user");
		;

		obj.setCreated_by_user_id_fk(uObj.getUserId());
		obj.setUser_id(uObj.getUserId());
		obj.setUser_name(uObj.getUserName());
		obj.setDesignation(uObj.getDesignation());

		obj.setProposal_submission_date_to_collector(DateParser.parse(obj.getProposal_submission_date_to_collector()));
		obj.setJm_fee_letter_received_date(DateParser.parse(obj.getJm_fee_letter_received_date()));
		obj.setJm_fee_paid_date(DateParser.parse(obj.getJm_fee_paid_date()));
		obj.setJm_start_date(DateParser.parse(obj.getJm_start_date()));
		obj.setJm_completion_date(DateParser.parse(obj.getJm_completion_date()));
		obj.setJm_sheet_date_to_sdo(DateParser.parse(obj.getJm_sheet_date_to_sdo()));

		obj.setForest_online_submission(DateParser.parse(obj.getForest_online_submission()));
		obj.setForest_submission_date_to_dycfo(DateParser.parse(obj.getForest_submission_date_to_dycfo()));
		obj.setForest_submission_date_to_ccf_thane(DateParser.parse(obj.getForest_submission_date_to_ccf_thane()));
		obj.setForest_submission_date_to_nodal_officer(
				DateParser.parse(obj.getForest_submission_date_to_nodal_officer()));
		obj.setForest_submission_date_to_revenue_secretary_mantralaya(
				DateParser.parse(obj.getForest_submission_date_to_revenue_secretary_mantralaya()));
		obj.setForest_submission_date_to_regional_office_nagpur(
				DateParser.parse(obj.getForest_submission_date_to_regional_office_nagpur()));
		obj.setForest_date_of_approval_by_regional_office_nagpur(
				DateParser.parse(obj.getForest_date_of_approval_by_regional_office_nagpur()));
		obj.setForest_valuation_by_dycfo(DateParser.parse(obj.getForest_valuation_by_dycfo()));
		obj.setForest_approval_for_payment(DateParser.parse(obj.getForest_approval_for_payment()));
		obj.setForest_payment_date(DateParser.parse(obj.getForest_payment_date()));
		obj.setForest_possession_date(DateParser.parse(obj.getForest_possession_date()));

		obj.setProposal_submission(DateParser.parse(obj.getProposal_submission()));
		obj.setLetter_for_payment(DateParser.parse(obj.getLetter_for_payment()));
		obj.setApproval_for_payment(DateParser.parse(obj.getApproval_for_payment()));
		obj.setPayment_date(DateParser.parse(obj.getPayment_date()));
		obj.setPossession_date(DateParser.parse(obj.getPossession_date()));
		obj.setValuation_date(DateParser.parse(obj.getValuation_date()));

		obj.setPlanned_date_of_possession(DateParser.parse(obj.getPlanned_date_of_possession()));

		obj.setRailway_online_submission(DateParser.parse(obj.getRailway_online_submission()));
		obj.setRailway_submission_date_to_DyCFO(DateParser.parse(obj.getRailway_submission_date_to_DyCFO()));
		obj.setRailway_submission_date_to_CCF_Thane(DateParser.parse(obj.getRailway_submission_date_to_CCF_Thane()));
		obj.setRailway_submission_date_to_nodal_officer_CCF_Nagpur(
				DateParser.parse(obj.getRailway_submission_date_to_nodal_officer_CCF_Nagpur()));
		obj.setRailway_submission_date_to_revenue_secretary_mantralaya(
				DateParser.parse(obj.getRailway_submission_date_to_revenue_secretary_mantralaya()));
		obj.setRailway_submission_date_to_regional_office_nagpur(
				DateParser.parse(obj.getRailway_submission_date_to_regional_office_nagpur()));
		obj.setRailway_date_of_approval_by_Rregional_Office_agpur(
				DateParser.parse(obj.getRailway_date_of_approval_by_Rregional_Office_agpur()));
		obj.setRailway_valuation_by_DyCFO(DateParser.parse(obj.getRailway_valuation_by_DyCFO()));
		obj.setRailway_approval_for_payment(DateParser.parse(obj.getRailway_approval_for_payment()));
		obj.setRailway_payment_date(DateParser.parse(obj.getRailway_payment_date()));
		obj.setRailway_possession_date(DateParser.parse(obj.getRailway_possession_date()));

		obj.setConsent_from_owner(DateParser.parse(obj.getConsent_from_owner()));
		obj.setLegal_search_report(DateParser.parse(obj.getLegal_search_report()));
		obj.setDate_of_registration(DateParser.parse(obj.getDate_of_registration()));
		obj.setDate_of_possession(DateParser.parse(obj.getDate_of_possession()));
		obj.setForest_tree_survey(DateParser.parse(obj.getForest_tree_survey()));
		obj.setForest_tree_valuation(DateParser.parse(obj.getForest_tree_valuation()));
		obj.setHorticulture_tree_survey(DateParser.parse(obj.getHorticulture_tree_survey()));
		obj.setHorticulture_tree_valuation(DateParser.parse(obj.getHorticulture_tree_valuation()));
		obj.setStructure_survey(DateParser.parse(obj.getStructure_survey()));
		obj.setStructure_valuation(DateParser.parse(obj.getStructure_valuation()));
		obj.setBorewell_survey(DateParser.parse(obj.getBorewell_survey()));
		obj.setBorewell_valuation(DateParser.parse(obj.getBorewell_valuation()));
		obj.setDate_of_rfp_to_adtp(DateParser.parse(obj.getDate_of_rfp_to_adtp()));
		obj.setDate_of_rate_fixation_of_land(DateParser.parse(obj.getDate_of_rate_fixation_of_land()));
		obj.setSdo_demand_for_payment(DateParser.parse(obj.getSdo_demand_for_payment()));
		obj.setDate_of_approval_for_payment(DateParser.parse(obj.getDate_of_approval_for_payment()));
		obj.setPrivate_payment_date(DateParser.parse(obj.getPrivate_payment_date()));

		obj.setSubmission_of_proposal_to_GM(DateParser.parse(obj.getSubmission_of_proposal_to_GM()));
		obj.setApproval_of_GM(DateParser.parse(obj.getApproval_of_GM()));
		obj.setDraft_letter_to_con_for_approval_rp(DateParser.parse(obj.getDraft_letter_to_con_for_approval_rp()));
		obj.setDate_of_approval_of_construction_rp(DateParser.parse(obj.getDate_of_approval_of_construction_rp()));
		obj.setDate_of_uploading_of_gazette_notification_rp(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_rp()));
		obj.setPublication_in_gazette_rp(DateParser.parse(obj.getPublication_in_gazette_rp()));

		obj.setDate_of_proposal_to_DC_for_nomination(DateParser.parse(obj.getDate_of_proposal_to_DC_for_nomination()));
		obj.setDate_of_nomination_of_competenta_authority(
				DateParser.parse(obj.getDate_of_nomination_of_competenta_authority()));
		obj.setDraft_letter_to_con_for_approval_ca(DateParser.parse(obj.getDraft_letter_to_con_for_approval_ca()));
		obj.setDate_of_approval_of_construction_ca(DateParser.parse(obj.getDate_of_approval_of_construction_ca()));
		obj.setDate_of_uploading_of_gazette_notification_ca(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_ca()));
		obj.setPublication_in_gazette_ca(DateParser.parse(obj.getPublication_in_gazette_ca()));
		obj.setDate_of_submission_of_draft_notification_to_CALA(
				DateParser.parse(obj.getDate_of_submission_of_draft_notification_to_CALA()));
		obj.setApproval_of_CALA_20a(DateParser.parse(obj.getApproval_of_CALA_20a()));
		obj.setDraft_letter_to_con_for_approval_20a(DateParser.parse(obj.getDraft_letter_to_con_for_approval_20a()));
		obj.setDate_of_approval_of_construction_20a(DateParser.parse(obj.getDate_of_approval_of_construction_20a()));
		obj.setDate_of_uploading_of_gazette_notification_20a(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_20a()));
		obj.setPublication_in_gazette_20a(DateParser.parse(obj.getPublication_in_gazette_20a()));
		obj.setPublication_in_2_local_news_papers_20a(
				DateParser.parse(obj.getPublication_in_2_local_news_papers_20a()));
		obj.setPasting_of_notification_in_villages_20a(
				DateParser.parse(obj.getPasting_of_notification_in_villages_20a()));
		obj.setReceipt_of_grievances(DateParser.parse(obj.getReceipt_of_grievances()));
		obj.setDisposal_of_grievances(DateParser.parse(obj.getDisposal_of_grievances()));
		obj.setDate_of_submission_of_draft_notification_to_CALA_20e(
				DateParser.parse(obj.getDate_of_submission_of_draft_notification_to_CALA_20e()));
		obj.setApproval_of_CALA_20e(DateParser.parse(obj.getApproval_of_CALA_20e()));
		obj.setDraft_letter_to_con_for_approval_20e(DateParser.parse(obj.getDraft_letter_to_con_for_approval_20e()));
		obj.setDate_of_approval_of_construction_20e(DateParser.parse(obj.getDate_of_approval_of_construction_20e()));
		obj.setDate_of_uploading_of_gazette_notification_20e(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_20e()));
		obj.setPublication_in_gazette_20e(DateParser.parse(obj.getPublication_in_gazette_20e()));
		obj.setPublication_of_notice_in_2_local_news_papers_20e(
				DateParser.parse(obj.getPublication_of_notice_in_2_local_news_papers_20e()));
		obj.setDate_of_submission_of_draft_notification_to_CALA_20f(
				DateParser.parse(obj.getDate_of_submission_of_draft_notification_to_CALA_20f()));
		obj.setApproval_of_CALA_20f(DateParser.parse(obj.getApproval_of_CALA_20f()));
		obj.setDraft_letter_to_con_for_approval_20f(DateParser.parse(obj.getDraft_letter_to_con_for_approval_20f()));
		obj.setDate_of_approval_of_construction_20f(DateParser.parse(obj.getDate_of_approval_of_construction_20f()));
		obj.setDate_of_uploading_of_gazette_notification_20f(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_20f()));
		obj.setPublication_in_gazette_20f(DateParser.parse(obj.getPublication_in_gazette_20f()));
		obj.setPublication_of_notice_in_2_local_news_papers_20f(
				DateParser.parse(obj.getPublication_of_notice_in_2_local_news_papers_20f()));

		return landAquisitionService.addLandAcquisition(obj);
		// if(flag) {
		// attributes.addFlashAttribute("success", "Land Acquisition Added
		// Succesfully.");
		// }
		// else {
		// attributes.addFlashAttribute("error","Adding Land Acquisition is failed. Try
		// again.");
		// }
		// }catch (Exception e) {
		// attributes.addFlashAttribute("error","Adding Land Acquisition is failed. Try
		// again.");
		// logger.error("addLandAcquisition : " + e.getMessage());
		// }

	}

	@PostMapping("/update-land-acquisition")
	public boolean updateLandAcquisition(@ModelAttribute LandAcquisition obj, HttpSession session) throws Exception{
		// ModelAndView model = new ModelAndView();
		// try{

		// model.setViewName("redirect:/land-acquisition");
		User uObj = (User) session.getAttribute("user");
		;

		obj.setCreated_by_user_id_fk(uObj.getUserId());
		obj.setUser_id(uObj.getUserId());
		obj.setUser_name(uObj.getUserName());
		obj.setDesignation(uObj.getDesignation());

		//obj.setCreated_by_user_id_fk(user_Id);
		//obj.setUser_id(user_Id);
		//obj.setUser_name(userName);
		//obj.setDesignation(userDesignation);
		obj.setProposal_submission_date_to_collector(DateParser.parse(obj.getProposal_submission_date_to_collector()));
		obj.setJm_fee_letter_received_date(DateParser.parse(obj.getJm_fee_letter_received_date()));
		obj.setJm_fee_paid_date(DateParser.parse(obj.getJm_fee_paid_date()));
		obj.setJm_start_date(DateParser.parse(obj.getJm_start_date()));
		obj.setJm_completion_date(DateParser.parse(obj.getJm_completion_date()));
		obj.setJm_sheet_date_to_sdo(DateParser.parse(obj.getJm_sheet_date_to_sdo()));

		obj.setForest_online_submission(DateParser.parse(obj.getForest_online_submission()));
		obj.setForest_submission_date_to_dycfo(DateParser.parse(obj.getForest_submission_date_to_dycfo()));
		obj.setForest_submission_date_to_ccf_thane(DateParser.parse(obj.getForest_submission_date_to_ccf_thane()));
		obj.setForest_submission_date_to_nodal_officer(
				DateParser.parse(obj.getForest_submission_date_to_nodal_officer()));
		obj.setForest_submission_date_to_revenue_secretary_mantralaya(
				DateParser.parse(obj.getForest_submission_date_to_revenue_secretary_mantralaya()));
		obj.setForest_submission_date_to_regional_office_nagpur(
				DateParser.parse(obj.getForest_submission_date_to_regional_office_nagpur()));
		obj.setForest_date_of_approval_by_regional_office_nagpur(
				DateParser.parse(obj.getForest_date_of_approval_by_regional_office_nagpur()));
		obj.setForest_valuation_by_dycfo(DateParser.parse(obj.getForest_valuation_by_dycfo()));
		obj.setForest_approval_for_payment(DateParser.parse(obj.getForest_approval_for_payment()));
		obj.setForest_payment_date(DateParser.parse(obj.getForest_payment_date()));
		obj.setForest_possession_date(DateParser.parse(obj.getForest_possession_date()));

		obj.setProposal_submission(DateParser.parse(obj.getProposal_submission()));
		obj.setLetter_for_payment(DateParser.parse(obj.getLetter_for_payment()));
		obj.setApproval_for_payment(DateParser.parse(obj.getApproval_for_payment()));
		obj.setPayment_date(DateParser.parse(obj.getPayment_date()));
		obj.setPossession_date(DateParser.parse(obj.getPossession_date()));
		obj.setValuation_date(DateParser.parse(obj.getValuation_date()));

		obj.setPlanned_date_of_possession(DateParser.parse(obj.getPlanned_date_of_possession()));

		obj.setRailway_online_submission(DateParser.parse(obj.getRailway_online_submission()));
		obj.setRailway_submission_date_to_DyCFO(DateParser.parse(obj.getRailway_submission_date_to_DyCFO()));
		obj.setRailway_submission_date_to_CCF_Thane(DateParser.parse(obj.getRailway_submission_date_to_CCF_Thane()));
		obj.setRailway_submission_date_to_nodal_officer_CCF_Nagpur(
				DateParser.parse(obj.getRailway_submission_date_to_nodal_officer_CCF_Nagpur()));
		obj.setRailway_submission_date_to_revenue_secretary_mantralaya(
				DateParser.parse(obj.getRailway_submission_date_to_revenue_secretary_mantralaya()));
		obj.setRailway_submission_date_to_regional_office_nagpur(
				DateParser.parse(obj.getRailway_submission_date_to_regional_office_nagpur()));
		obj.setRailway_date_of_approval_by_Rregional_Office_agpur(
				DateParser.parse(obj.getRailway_date_of_approval_by_Rregional_Office_agpur()));
		obj.setRailway_valuation_by_DyCFO(DateParser.parse(obj.getRailway_valuation_by_DyCFO()));
		obj.setRailway_approval_for_payment(DateParser.parse(obj.getRailway_approval_for_payment()));
		obj.setRailway_payment_date(DateParser.parse(obj.getRailway_payment_date()));
		obj.setRailway_possession_date(DateParser.parse(obj.getRailway_possession_date()));

		obj.setConsent_from_owner(DateParser.parse(obj.getConsent_from_owner()));
		obj.setLegal_search_report(DateParser.parse(obj.getLegal_search_report()));
		obj.setDate_of_registration(DateParser.parse(obj.getDate_of_registration()));
		obj.setDate_of_possession(DateParser.parse(obj.getDate_of_possession()));
		obj.setForest_tree_survey(DateParser.parse(obj.getForest_tree_survey()));
		obj.setForest_tree_valuation(DateParser.parse(obj.getForest_tree_valuation()));
		obj.setHorticulture_tree_survey(DateParser.parse(obj.getHorticulture_tree_survey()));
		obj.setHorticulture_tree_valuation(DateParser.parse(obj.getHorticulture_tree_valuation()));
		obj.setStructure_survey(DateParser.parse(obj.getStructure_survey()));
		obj.setStructure_valuation(DateParser.parse(obj.getStructure_valuation()));
		obj.setBorewell_survey(DateParser.parse(obj.getBorewell_survey()));
		obj.setBorewell_valuation(DateParser.parse(obj.getBorewell_valuation()));
		obj.setDate_of_rfp_to_adtp(DateParser.parse(obj.getDate_of_rfp_to_adtp()));
		obj.setDate_of_rate_fixation_of_land(DateParser.parse(obj.getDate_of_rate_fixation_of_land()));
		obj.setSdo_demand_for_payment(DateParser.parse(obj.getSdo_demand_for_payment()));
		obj.setDate_of_approval_for_payment(DateParser.parse(obj.getDate_of_approval_for_payment()));
		obj.setPrivate_payment_date(DateParser.parse(obj.getPrivate_payment_date()));

		obj.setSubmission_of_proposal_to_GM(DateParser.parse(obj.getSubmission_of_proposal_to_GM()));
		obj.setApproval_of_GM(DateParser.parse(obj.getApproval_of_GM()));
		obj.setDraft_letter_to_con_for_approval_rp(DateParser.parse(obj.getDraft_letter_to_con_for_approval_rp()));
		obj.setDate_of_approval_of_construction_rp(DateParser.parse(obj.getDate_of_approval_of_construction_rp()));
		obj.setDate_of_uploading_of_gazette_notification_rp(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_rp()));
		obj.setPublication_in_gazette_rp(DateParser.parse(obj.getPublication_in_gazette_rp()));

		obj.setDate_of_proposal_to_DC_for_nomination(DateParser.parse(obj.getDate_of_proposal_to_DC_for_nomination()));
		obj.setDate_of_nomination_of_competenta_authority(
				DateParser.parse(obj.getDate_of_nomination_of_competenta_authority()));
		obj.setDraft_letter_to_con_for_approval_ca(DateParser.parse(obj.getDraft_letter_to_con_for_approval_ca()));
		obj.setDate_of_approval_of_construction_ca(DateParser.parse(obj.getDate_of_approval_of_construction_ca()));
		obj.setDate_of_uploading_of_gazette_notification_ca(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_ca()));
		obj.setPublication_in_gazette_ca(DateParser.parse(obj.getPublication_in_gazette_ca()));
		obj.setDate_of_submission_of_draft_notification_to_CALA(
				DateParser.parse(obj.getDate_of_submission_of_draft_notification_to_CALA()));
		obj.setApproval_of_CALA_20a(DateParser.parse(obj.getApproval_of_CALA_20a()));
		obj.setDraft_letter_to_con_for_approval_20a(DateParser.parse(obj.getDraft_letter_to_con_for_approval_20a()));
		obj.setDate_of_approval_of_construction_20a(DateParser.parse(obj.getDate_of_approval_of_construction_20a()));
		obj.setDate_of_uploading_of_gazette_notification_20a(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_20a()));
		obj.setPublication_in_gazette_20a(DateParser.parse(obj.getPublication_in_gazette_20a()));
		obj.setPublication_in_2_local_news_papers_20a(
				DateParser.parse(obj.getPublication_in_2_local_news_papers_20a()));
		obj.setPasting_of_notification_in_villages_20a(
				DateParser.parse(obj.getPasting_of_notification_in_villages_20a()));
		obj.setReceipt_of_grievances(DateParser.parse(obj.getReceipt_of_grievances()));
		obj.setDisposal_of_grievances(DateParser.parse(obj.getDisposal_of_grievances()));
		obj.setDate_of_submission_of_draft_notification_to_CALA_20e(
				DateParser.parse(obj.getDate_of_submission_of_draft_notification_to_CALA_20e()));
		obj.setApproval_of_CALA_20e(DateParser.parse(obj.getApproval_of_CALA_20e()));
		obj.setDraft_letter_to_con_for_approval_20e(DateParser.parse(obj.getDraft_letter_to_con_for_approval_20e()));
		obj.setDate_of_approval_of_construction_20e(DateParser.parse(obj.getDate_of_approval_of_construction_20e()));
		obj.setDate_of_uploading_of_gazette_notification_20e(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_20e()));
		obj.setPublication_in_gazette_20e(DateParser.parse(obj.getPublication_in_gazette_20e()));
		obj.setPublication_of_notice_in_2_local_news_papers_20e(
				DateParser.parse(obj.getPublication_of_notice_in_2_local_news_papers_20e()));
		obj.setDate_of_submission_of_draft_notification_to_CALA_20f(
				DateParser.parse(obj.getDate_of_submission_of_draft_notification_to_CALA_20f()));
		obj.setApproval_of_CALA_20f(DateParser.parse(obj.getApproval_of_CALA_20f()));
		obj.setDraft_letter_to_con_for_approval_20f(DateParser.parse(obj.getDraft_letter_to_con_for_approval_20f()));
		obj.setDate_of_approval_of_construction_20f(DateParser.parse(obj.getDate_of_approval_of_construction_20f()));
		obj.setDate_of_uploading_of_gazette_notification_20f(
				DateParser.parse(obj.getDate_of_uploading_of_gazette_notification_20f()));
		obj.setPublication_in_gazette_20f(DateParser.parse(obj.getPublication_in_gazette_20f()));
		obj.setPublication_of_notice_in_2_local_news_papers_20f(
				DateParser.parse(obj.getPublication_of_notice_in_2_local_news_papers_20f()));
		return landAquisitionService.updateLandAcquisition(obj);

	}
	@PostMapping("/upload-la")
	public ResponseEntity<?> uploadLA(@ModelAttribute LandAcquisition obj,RedirectAttributes attributes,HttpSession session){
		//ModelAndView model = new ModelAndView();
		String msg = "";
		String attributeKey ="";
		String attributeMsg = "";
		try {
			User uObj = (User) session.getAttribute("user");
			String userId = uObj.getUserId();
			String userName = uObj.getUserName();
			obj.setCreated_by_user_id_fk(uObj.getUserId());
			obj.setUser_id(uObj.getUserId());
			obj.setUser_name(uObj.getUserName());
			obj.setDesignation(uObj.getDesignation());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			//model.setViewName("redirect:/land-acquisition");
			
			if(!StringUtils.isEmpty(obj.getLaUploadFile())){
				MultipartFile multipartFile = obj.getLaUploadFile();
				// Creates a workbook object from the uploaded excelfile
				if (multipartFile.getSize() > 0){					
					XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
					// Creates a worksheet object representing the first sheet
					int sheetsCount = workbook.getNumberOfSheets();
					if(sheetsCount > 0) {
						XSSFSheet laSheet = workbook.getSheetAt(1);
						//System.out.println(uploadFilesSheet.getSheetName());
						//header row
						XSSFRow headerRow = laSheet.getRow(1);
						//checking given file format
						if(headerRow != null){
							List<String> fileFormat = FileFormatModel.getLAFileFormat();
							List<String> fileFormatLand = FileFormatModel.getLAFileFormatOnlyLandIndentification();
							
							int noOfColumns = headerRow.getLastCellNum();
							if(sheetsCount==2)
							{
								if(noOfColumns == fileFormatLand.size()){
									for (int i = 0; i < fileFormatLand.size();i++) {
					                	//System.out.println(headerRow.getCell(i).getStringCellValue().trim());
					                	//if(!fileFormat.get(i).trim().equals(headerRow.getCell(i).getStringCellValue().trim())){
										String columnName = headerRow.getCell(i).getStringCellValue().trim();
										if(!columnName.equals(fileFormatLand.get(i).trim()) && !columnName.contains(fileFormatLand.get(i).trim())){
					                		//attributes.addFlashAttribute("error",uploadformatError);
					                		return ResponseEntity.ok(Map.of("error",uploadformatError));
					                	}
									}
								}else{
									//attributes.addFlashAttribute("error",uploadformatError);
			                		return ResponseEntity.ok(Map.of("error",uploadformatError));
								}								
							}
							else
							{
								if(noOfColumns == fileFormat.size()){
									for (int i = 0; i < fileFormat.size();i++) {
					                	//System.out.println(headerRow.getCell(i).getStringCellValue().trim());
					                	//if(!fileFormat.get(i).trim().equals(headerRow.getCell(i).getStringCellValue().trim())){
										String columnName = headerRow.getCell(i).getStringCellValue().trim();
										if(!columnName.equals(fileFormat.get(i).trim()) && !columnName.contains(fileFormat.get(i).trim())){
					                		//attributes.addFlashAttribute("error",uploadformatError);
					                		return ResponseEntity.ok(Map.of("error",uploadformatError));
					                	}
									}
								}else{
									//attributes.addFlashAttribute("error",uploadformatError);
									return ResponseEntity.ok(Map.of("error",uploadformatError));
								}								
							}
							
						}else{
							//attributes.addFlashAttribute("error",uploadformatError);
							return ResponseEntity.ok(Map.of("error",uploadformatError));
						}
						String[]  result = null;
						if(sheetsCount==2)
						{
							 result = uploadLASingleSheet(obj,userId,userName);
						}
						else
						{
							result = uploadLA(obj,userId,userName);
						}
						String errMsg = result[0];String actualVal = "";
						int count = 0,row = 0,sheet = 0,subRow = 0;
						List<String> fileFormat = FileFormatModel.getLAFileFormat();
						if(!StringUtils.isEmpty(result[1])){count = Integer.parseInt(result[1]);}
						if(!StringUtils.isEmpty(result[2])){row = Integer.parseInt(result[2]);}
						if(!StringUtils.isEmpty(result[3])){sheet = Integer.parseInt(result[3]);}
						if(!StringUtils.isEmpty(result[4])){subRow = Integer.parseInt(result[4]);}
						//System.out.println(errMsg);
						if(!StringUtils.isEmpty(errMsg)) {
							if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Duplicate entry")) {    
								//attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;<b>Work and RR Id Mismatch at row: ("+row+")</b> please check and Upload again.</span>");
								attributeKey = "error";
								attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;<b>Work and RR Id Mismatch at row: ("+row+")</b> please check and Upload again.</span>";
								msg = "Work and Utility Shifting Id Mismatch at row: "+row;
							}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Data truncated")) {
								actualVal = Integer.toString(subRow);
								if(sheet == 1) {subRow = row; 
									String error = "Data truncated";
									actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
								} 
								//attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect Value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
								attributeKey = "error";
								attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect Value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>";
								
								msg = "Incorrect value identified in Sheet: "+sheet+" at row: "+actualVal;
							}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Cannot add or update a child row")) {
								actualVal = Integer.toString(subRow);
								if(sheet == 1) {subRow = row;
									String error = "Cannot add or update a child row";
									actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
								}
								//attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect Value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
								attributeKey = "error";
								attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect Value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>";
								
								msg = "Incorrect value identified in Sheet: "+sheet+" at row: "+actualVal;
							}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Incorrect date value")) {
								actualVal = Integer.toString(subRow);
								if(sheet == 1) {subRow = row;
									String error = "Incorrect date value";
									actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
								}
								//attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect date value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
								attributeKey = "error";
								attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect date value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>";
								
								msg = "Incorrect date value identified in Sheet: "+sheet+" at row: "+actualVal;
							}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Incorrect integer value")) {
								actualVal = Integer.toString(subRow);
								if(sheet == 1) {subRow = row; 
									String error = "Incorrect integer value";
									actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
								}
								//attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect integer value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
								attributeKey = "error";
								attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect integer value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>";
								
								msg = "Incorrect integer value identified in Sheet: "+sheet+" at row: "+actualVal;
							}else if(!StringUtils.isEmpty(errMsg) && errMsg.contains("Incorrect decimal value")) {
								actualVal = Integer.toString(subRow);
								if(sheet == 1) {subRow = row;
									String error = "Incorrect decimal value";
									actualVal = FileFormatModel.getActualValue(error,errMsg,subRow,fileFormat);
								}
								//attributes.addFlashAttribute("error","<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect decimal value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>");
								attributeKey = "error";
								attributeMsg = "<span style='color:red;'><i class='fa fa-warning'></i>&nbsp;Incorrect decimal value identified in <b>Sheet: ["+sheet+"]</b> at <b>row: ["+actualVal+"]</b> please check and Upload again.</span>";
								
								msg = "Incorrect decimal value identified in Sheet: "+sheet+" at row: "+actualVal;
							}
						
	                		//obj.setUploaded_by_user_id_fk(userId);
	                		//obj.setStatus("Fail");
	                		//obj.setRemarks(msg);
							//boolean flag = service.saveRRDataUploadFile(obj);
	                		//return model;
						}
						if(count > 0) {
							//attributes.addFlashAttribute("success", count + " Land Acquisition added successfully.");	
							attributeKey = "success";
							attributeMsg = count + " Land Acquisition added successfully.";
							
							msg = count + " Land Acquisition added successfully.";
							
							FormHistory formHistory = new FormHistory();
							formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
							formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
							formHistory.setModule_name_fk("Land Acquisition");
							formHistory.setForm_name("Upload Land Acquisition");
							formHistory.setForm_action_type("Upload");
							formHistory.setForm_details( msg);
							formHistory.setWork(obj.getProject_id_fk());
							//formHistory.setContract(obj.getContract_id_fk());
							
							boolean history_flag = formHistoryDao.saveFormHistory(formHistory);
							/********************************************************************************/
						}else {
							//attributes.addFlashAttribute("success"," No records found.");	
							attributeKey = "success";
							attributeMsg = " No records found.";
							
							msg = " No records found.";
						}
					}
					workbook.close();
				}
			} else {
				attributeKey = "error";
				attributeMsg = "Something went wrong. Please try after some time";
				//attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			//attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
			attributeKey = "error";
			attributeMsg = "Something went wrong. Please try after some time";
			
			logger.fatal("updateDataDate() : "+e.getMessage());
		}
		//return model;
		return ResponseEntity.ok(Map.of(attributeKey,attributeMsg));
	}

	
	/**
	 * This method uploadLA() is called when user upload the file
	 * 
	 * @param obj is object for the model class User.
	 * @param userId is type of String it store the userId
	 * @param userName is type of String it store the userName
	 * @param workbook is type of XSSWorkbook variable it takes the workbook as input.
	 * @return type of this method is count.
	 * @throws Exception 
	 * @throws IOException will raise an exception when abnormal termination occur.
	 */

	private String[] uploadLA(LandAcquisition obj, String userId, String userName) throws Exception {
		LandAcquisition la = null;
		List<LandAcquisition> lasList = new ArrayList<LandAcquisition>();
		String[] result = new String[5];
		Writer w = null;
		int count = 0;

		SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter3 = new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
		
		try {	
			MultipartFile excelfile = obj.getLaUploadFile();
			// Creates a workbook object from the uploaded excelfile
			if (!StringUtils.isEmpty(excelfile) && excelfile.getSize() > 0 ){
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				int sheetsCount = workbook.getNumberOfSheets();
				if(sheetsCount > 0) {
					
					XSSFSheet laSheet = workbook.getSheetAt(1);
					//System.out.println(uploadFilesSheet.getSheetName());
					//header row
					//XSSFRow headerRow = uploadFilesSheet.getRow(0);							
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					//System.out.println(uploadFilesSheet.getLastRowNum());
					for(int i = 2; i <= laSheet.getLastRowNum();i++){
						int v = laSheet.getLastRowNum();
						XSSFRow row = laSheet.getRow(i);
						// Sets the Read data to the model class
						// Cell cell = row.getCell(0);
						// String j_username = formatter.formatCellValue(row.getCell(0));
						//System.out.println(i);
						la = new LandAcquisition();
						la.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
						la.setUser_role_code(obj.getUser_role_code());
						String val = null;
						if(!StringUtils.isEmpty(row)) {								
							val = formatter.formatCellValue(row.getCell(0)).trim();
							if(!StringUtils.isEmpty(val)) { la.setProject_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(1)).trim();
							if(!StringUtils.isEmpty(val)) { la.setLa_id(val);}
							
							
							val = formatter.formatCellValue(row.getCell(2)).trim();
							if(!StringUtils.isEmpty(val)) { la.setSurvey_number(val);}
							
							
							val = formatter.formatCellValue(row.getCell(3)).trim();
							if(!StringUtils.isEmpty(val)) { la.setCategory_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(4)).trim();
							if(!StringUtils.isEmpty(val)) { la.setLa_sub_category_fk(val);}	
							
							
							val = formatter.formatCellValue(row.getCell(5)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(5));
								}
							
								la.setArea_of_plot(val);}	
							
							val = formatter.formatCellValue(row.getCell(6)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(6));
								}
								la.setArea_to_be_acquired(val);}	
							
							val = formatter.formatCellValue(row.getCell(7)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(7));
								}
								la.setArea_acquired(val);}	
							
							val = formatter.formatCellValue(row.getCell(8)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(8));
								}
								la.setLa_land_status_fk(val);}	
							
							val = formatter.formatCellValue(row.getCell(9)).trim();
							if(!StringUtils.isEmpty(val)) { la.setChainage_from(val);}	
							
							
							val = formatter.formatCellValue(row.getCell(10)).trim();
							if(!StringUtils.isEmpty(val)) { la.setChainage_to(val);}								
							
							val = formatter.formatCellValue(row.getCell(11)).trim();
							if(!StringUtils.isEmpty(val)) { la.setVillage(val);}										
							
							val = formatter.formatCellValue(row.getCell(12)).trim();
							if(!StringUtils.isEmpty(val)) { la.setTaluka(val);}
							
							val = formatter.formatCellValue(row.getCell(13)).trim();
							if(!StringUtils.isEmpty(val)) { la.setLatitude(val);}							
							
							val = formatter.formatCellValue(row.getCell(14)).trim();
							if(!StringUtils.isEmpty(val)) { la.setLongitude(val);}
							
							val = formatter.formatCellValue(row.getCell(15)).trim();
							if(!StringUtils.isEmpty(val)) { la.setDy_slr(val);}							
							
							val = formatter.formatCellValue(row.getCell(16)).trim();
							if(!StringUtils.isEmpty(val)) { la.setSdo(val);}	
							
							val = formatter.formatCellValue(row.getCell(17)).trim();
							if(!StringUtils.isEmpty(val)) { la.setCollector(val);}
							
							val = formatter.formatCellValue(row.getCell(18)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setProposal_submission_date_to_collector(val);}
							
							val = formatter.formatCellValue(row.getCell(19)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_fee_letter_received_date(val);}
							
							
							val = formatter.formatCellValue(row.getCell(20)).trim();
							if(!StringUtils.isEmpty(val)) {
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(20));
								}
								la.setJm_fee_amount(val);
							}
						
							val = formatter.formatCellValue(row.getCell(21)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_fee_paid_date(val);}								
							
							val = formatter.formatCellValue(row.getCell(22)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_start_date(val);}	
							
							val = formatter.formatCellValue(row.getCell(23)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_completion_date(val);}	
							
							val = formatter.formatCellValue(row.getCell(24)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_sheet_date_to_sdo(val);}				
							
							val = formatter.formatCellValue(row.getCell(25)).trim();
							if(!StringUtils.isEmpty(val)) { la.setJm_remarks(val);}										
						
							val = formatter.formatCellValue(row.getCell(26)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(26));
								}
								la.setJm_approval(val);}
							
							val = formatter.formatCellValue(row.getCell(27)).trim();
							if(!StringUtils.isEmpty(val)) { la.setSpecial_feature(val);}
							
							val = formatter.formatCellValue(row.getCell(28)).trim();
							if(!StringUtils.isEmpty(val)) { la.setRemarks(val);}
							
							val = formatter.formatCellValue(row.getCell(29)).trim();
							if(!StringUtils.isEmpty(val)) { la.setIssues(val);}
					
							la.setProposal_submission_date_to_collector(DateParser.parse(la.getProposal_submission_date_to_collector()));
							la.setJm_fee_letter_received_date(DateParser.parse(la.getJm_fee_letter_received_date()));
							la.setJm_fee_paid_date(DateParser.parse(la.getJm_fee_paid_date()));
							la.setJm_start_date(DateParser.parse(la.getJm_start_date()));
							la.setJm_completion_date(DateParser.parse(la.getJm_completion_date()));
							la.setJm_sheet_date_to_sdo(DateParser.parse(la.getJm_sheet_date_to_sdo()));
						}
				
						List<LandAcquisition> pObjList = new ArrayList<LandAcquisition>();
						List<LandAcquisition> pObjList1 = new ArrayList<LandAcquisition>();
						List<LandAcquisition> pObjList2 = new ArrayList<LandAcquisition>();
						List<LandAcquisition> gObjList = new ArrayList<LandAcquisition>();
						List<LandAcquisition> fObjList = new ArrayList<LandAcquisition>();
						List<LandAcquisition> rObjList = new ArrayList<LandAcquisition>();
						
									XSSFSheet laPrivateSheet = workbook.getSheetAt(4);
									XSSFSheet laPrivateLVSheet = workbook.getSheetAt(5);
									XSSFSheet laprivateLASheet = workbook.getSheetAt(6);
									XSSFSheet GovSheet = workbook.getSheetAt(7);
									XSSFSheet forestSheet = workbook.getSheetAt(8);
									XSSFSheet railwaySheet = workbook.getSheetAt(9);

									XSSFRow privateIRA = laPrivateSheet.getRow(1);
									XSSFRow privateLV = laPrivateLVSheet.getRow(1);
									XSSFRow privateLA = laprivateLASheet.getRow(1);
									XSSFRow Gov = GovSheet.getRow(1);
									XSSFRow forest = forestSheet.getRow(1);
									XSSFRow railway = railwaySheet.getRow(1);
									//String val = null;
									if(privateIRA != null){
										for(int j = 2; j <= laPrivateSheet.getLastRowNum();j++){
											XSSFRow row2 = laPrivateSheet.getRow(j);
											LandAcquisition pObj = new LandAcquisition();
											if(!StringUtils.isEmpty(row2) && formatter.formatCellValue(row2.getCell(0)).trim().equals(la.getLa_id())) {
												val = formatter.formatCellValue(row2.getCell(0)).trim();
												if(!StringUtils.isEmpty(val)) { pObj.setLa_id(val);}
												
												val = formatter.formatCellValue(row2.getCell(1)).trim();
												if(!StringUtils.isEmpty(val)) { pObj.setPrivate_ira_collector(val);}
												//Declaration of Special Railway project 
												val = formatter.formatCellValue(row2.getCell(2)).trim();
												



												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date24 = null;
														String dateString24 = null;
														date24 = formatter3.parse(val);
														dateString24 = formatter2.format(date24);										
														pObj.setSubmission_of_proposal_to_GM(dateString24);
														 
													}
													else
													{
													
														Date date24 = null;
														String dateString24 = null;
														date24 = formatter1.parse(val);
														dateString24 = formatter2.format(date24);
														pObj.setSubmission_of_proposal_to_GM(dateString24);
														
													}
													
												}	
												val = formatter.formatCellValue(row2.getCell(3)).trim();
												
											
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date25 = null;
														String dateString25 = null;
														date25 = formatter3.parse(val);
														dateString25 = formatter2.format(date25);										
														pObj.setApproval_of_GM(dateString25);
														 
													}
													else
													{
													
														Date date25 = null;
														String dateString25 = null;
														date25 = formatter1.parse(val);
														dateString25 = formatter2.format(date25);	
														pObj.setApproval_of_GM(dateString25);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(4)).trim();


												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date26 = null;
														String dateString26 = null;
														date26 = formatter3.parse(val);
														dateString26 = formatter2.format(date26);										
														pObj.setDraft_letter_to_con_for_approval_rp(dateString26);
														 
													}
													else
													{
													
														Date date26 = null;
														String dateString26 = null;
														date26 = formatter1.parse(val);
														dateString26 = formatter2.format(date26);
														pObj.setDraft_letter_to_con_for_approval_rp(dateString26);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(5)).trim();
												
												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date27 = null;
														String dateString27 = null;
														date27 = formatter3.parse(val);
														dateString27 = formatter2.format(date27);									
														pObj.setDate_of_approval_of_construction_rp(dateString27);
														 
													}
													else
													{
													
														Date date27 = null;
														String dateString27 = null;
														date27 = formatter1.parse(val);
														dateString27 = formatter2.format(date27);
														pObj.setDate_of_approval_of_construction_rp(dateString27);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(6)).trim();


												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date28 = null;
														String dateString28 = null;
														date28 = formatter3.parse(val);
														dateString28 = formatter2.format(date28);									
														pObj.setDate_of_uploading_of_gazette_notification_rp(dateString28);
														 
													}
													else
													{
													
														Date date28 = null;
														String dateString28 = null;
														date28 = formatter1.parse(val);
														dateString28 = formatter2.format(date28);
														pObj.setDate_of_uploading_of_gazette_notification_rp(dateString28);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(7)).trim();
												
											
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date29 = null;
														String dateString29 = null;
														date29 = formatter3.parse(val);
														dateString29 = formatter2.format(date29);									
														pObj.setPublication_in_gazette_rp(dateString29);
														 
													}
													else
													{
													
														Date date29 = null;
														String dateString29 = null;
														date29 = formatter1.parse(val);
														dateString29 = formatter2.format(date29);	
														pObj.setPublication_in_gazette_rp(dateString29);
														
													}
													
												}
												//Nomination of competent Authority	
												val = formatter.formatCellValue(row2.getCell(8)).trim();
												
											
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date30 = null;
														String dateString30 = null;
														date30= formatter3.parse(val);
														dateString30 = formatter2.format(date30);								
														pObj.setDate_of_proposal_to_DC_for_nomination(dateString30);
														 
													}
													else
													{
													
														Date date30 = null;
														String dateString30 = null;
														date30= formatter1.parse(val);
														dateString30 = formatter2.format(date30);		
														pObj.setDate_of_proposal_to_DC_for_nomination(dateString30);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(9)).trim();


												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date31 = null;
														String dateString31 = null;
														date31= formatter3.parse(val);
														dateString31 = formatter2.format(date31);						
														pObj.setDate_of_nomination_of_competenta_authority(dateString31);
														 
													}
													else
													{
													
														Date date31 = null;
														String dateString31 = null;
														date31= formatter1.parse(val);
														dateString31 = formatter2.format(date31);	
														pObj.setDate_of_nomination_of_competenta_authority(dateString31);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(10)).trim();
												
											
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date32 = null;
														String dateString32 = null;
														date32 = formatter3.parse(val);
														dateString32 = formatter2.format(date32);						
														pObj.setDraft_letter_to_con_for_approval_ca(dateString32);
														 
													}
													else
													{
													
														Date date32 = null;
														String dateString32 = null;
														date32 = formatter1.parse(val);
														dateString32 = formatter2.format(date32);	
														pObj.setDraft_letter_to_con_for_approval_ca(dateString32);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(11)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date33 = null;
														String dateString33 = null;
														date33 = formatter3.parse(val);
														dateString33 = formatter2.format(date33);					
														pObj.setDate_of_approval_of_construction_ca(dateString33);
														 
													}
													else
													{
													
														Date date33 = null;
														String dateString33 = null;
														date33 = formatter1.parse(val);
														dateString33 = formatter2.format(date33);
														pObj.setDate_of_approval_of_construction_ca(dateString33);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(12)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date34 = null;
														String dateString34 = null;
														date34 = formatter3.parse(val);
														dateString34 = formatter2.format(date34);				
														pObj.setDate_of_uploading_of_gazette_notification_ca(dateString34);
														 
													}
													else
													{
													
														Date date34 = null;
														String dateString34 = null;
														date34 = formatter1.parse(val);
														dateString34 = formatter2.format(date34);
														pObj.setDate_of_uploading_of_gazette_notification_ca(dateString34);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(13)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date35 = null;
														String dateString35 = null;
														date35 = formatter3.parse(val);
														dateString35 = formatter2.format(date35);			
														pObj.setPublication_in_gazette_ca(dateString35);
														 
													}
													else
													{
													
														Date date35 = null;
														String dateString35 = null;
														date35 = formatter1.parse(val);
														dateString35 = formatter2.format(date35);
														pObj.setPublication_in_gazette_ca(dateString35);
														
													}
													
												}
											//Publication of notification under 20 A 
												val = formatter.formatCellValue(row2.getCell(14)).trim();
												
										
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date36 = null;
														String dateString36 = null;
														date36 = formatter3.parse(val);
														dateString36 = formatter2.format(date36);			
														pObj.setDate_of_submission_of_draft_notification_to_CALA(dateString36);
														 
													}
													else
													{
													
														Date date36 = null;
														String dateString36 = null;
														date36 = formatter1.parse(val);
														dateString36 = formatter2.format(date36);		
														pObj.setDate_of_submission_of_draft_notification_to_CALA(dateString36);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(15)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date37 = null;
														String dateString37 = null;
														date37 = formatter3.parse(val);
														dateString37 = formatter2.format(date37);		
														pObj.setApproval_of_CALA_20a(dateString37);
														 
													}
													else
													{
													
														Date date37 = null;
														String dateString37 = null;
														date37 = formatter1.parse(val);
														dateString37 = formatter2.format(date37);		
														pObj.setApproval_of_CALA_20a(dateString37);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(16)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date38 = null;
														String dateString38 = null;
														date38 = formatter3.parse(val);
														dateString38 = formatter2.format(date38);		
														pObj.setDraft_letter_to_con_for_approval_20a(dateString38);
														 
													}
													else
													{
													
														Date date38 = null;
														String dateString38 = null;
														date38 = formatter1.parse(val);
														dateString38 = formatter2.format(date38);		
														pObj.setDraft_letter_to_con_for_approval_20a(dateString38);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(17)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date39 = null;
														String dateString39 = null;
														date39 = formatter3.parse(val);
														dateString39 = formatter2.format(date39);
														pObj.setDate_of_approval_of_construction_20a(val);
														 
													}
													else
													{
													
														Date date39 = null;
														String dateString39 = null;
														date39 = formatter1.parse(val);
														dateString39 = formatter2.format(date39);	
														pObj.setDate_of_approval_of_construction_20a(val);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(18)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date40 = null;
														String dateString40 = null;
														date40 = formatter3.parse(val);
														dateString40 = formatter2.format(date40);
														pObj.setDate_of_uploading_of_gazette_notification_20a(dateString40);
														 
													}
													else
													{
													
														Date date40 = null;
														String dateString40 = null;
														date40 = formatter1.parse(val);
														dateString40 = formatter2.format(date40);	
														pObj.setDate_of_uploading_of_gazette_notification_20a(dateString40);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(19)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date41 = null;
														String dateString41 = null;
														date41 = formatter3.parse(val);
														dateString41 = formatter2.format(date41);
														pObj.setPublication_in_gazette_20a(dateString41);
														 
													}
													else
													{
													
														Date date41 = null;
														String dateString41 = null;
														date41 = formatter1.parse(val);
														dateString41 = formatter2.format(date41);
														pObj.setPublication_in_gazette_20a(dateString41);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(20)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date42 = null;
														String dateString42 = null;
														date42 = formatter3.parse(val);
														dateString42 = formatter2.format(date42);
														pObj.setPublication_in_2_local_news_papers_20a(dateString42);
														 
													}
													else
													{
													
														Date date42 = null;
														String dateString42 = null;
														date42 = formatter1.parse(val);
														dateString42 = formatter2.format(date42);
														pObj.setPublication_in_2_local_news_papers_20a(dateString42);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(21)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date43 = null;
														String dateString43 = null;
														date43 = formatter3.parse(val);
														dateString43 = formatter2.format(date43);
														pObj.setPasting_of_notification_in_villages_20a(dateString43);
														 
													}
													else
													{
													
														Date date43 = null;
														String dateString43 = null;
														date43 = formatter1.parse(val);
														dateString43 = formatter2.format(date43);
														pObj.setPasting_of_notification_in_villages_20a(dateString43);
														
													}
													
												}
										    //Grievances Redressal	
												val = formatter.formatCellValue(row2.getCell(22)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date44 = null;
														String dateString44 = null;
														date44 = formatter3.parse(val);
														dateString44 = formatter2.format(date44);
														pObj.setReceipt_of_grievances(dateString44);
														 
													}
													else
													{
													
														Date date44 = null;
														String dateString44 = null;
														date44 = formatter1.parse(val);
														dateString44 = formatter2.format(date44);
														pObj.setReceipt_of_grievances(dateString44);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(23)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date45 = null;
														String dateString45 = null;
														date45 = formatter3.parse(val);
														dateString45= formatter2.format(date45);
														pObj.setDisposal_of_grievances(dateString45);
														 
													}
													else
													{
													
														Date date45 = null;
														String dateString45 = null;
														date45 = formatter1.parse(val);
														dateString45= formatter2.format(date45);
														pObj.setDisposal_of_grievances(dateString45);
														
													}
													
												}
											//Acquisition notice under 20E	
												val = formatter.formatCellValue(row2.getCell(24)).trim();
												
										
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date46 = null;
														String dateString46 = null;
														date46 = formatter3.parse(val);
														dateString46 = formatter2.format(date46);	
														pObj.setDate_of_submission_of_draft_notification_to_CALA_20e(dateString46);
														 
													}
													else
													{
													
														Date date46 = null;
														String dateString46 = null;
														date46 = formatter1.parse(val);
														dateString46 = formatter2.format(date46);	
														pObj.setDate_of_submission_of_draft_notification_to_CALA_20e(dateString46);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(25)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date47 = null;
														String dateString47 = null;
														date47 = formatter3.parse(val);
														dateString47 = formatter2.format(date47);	
														pObj.setApproval_of_CALA_20e(dateString47);
														 
													}
													else
													{
													
														Date date47 = null;
														String dateString47 = null;
														date47 = formatter1.parse(val);
														dateString47 = formatter2.format(date47);	
														pObj.setApproval_of_CALA_20e(dateString47);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(26)).trim();


												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date48 = null;
														String dateString48 = null;
														date48 = formatter3.parse(val);
														dateString48 = formatter2.format(date48);
														pObj.setDraft_letter_to_con_for_approval_20e(dateString48);
														 
													}
													else
													{
													
														Date date48 = null;
														String dateString48 = null;
														date48 = formatter1.parse(val);
														dateString48 = formatter2.format(date48);
														pObj.setDraft_letter_to_con_for_approval_20e(dateString48);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(27)).trim();


												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date49 = null;
														String dateString49 = null;
														date49 = formatter3.parse(val);
														dateString49 = formatter2.format(date49);
														pObj.setDate_of_approval_of_construction_20e(dateString49);
														 
													}
													else
													{
													
														Date date49 = null;
														String dateString49 = null;
														date49 = formatter1.parse(val);
														dateString49 = formatter2.format(date49);
														pObj.setDate_of_approval_of_construction_20e(dateString49);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(28)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date50 = null;
														String dateString50 = null;
														date50 = formatter3.parse(val);
														dateString50 = formatter2.format(date50);
														pObj.setDate_of_uploading_of_gazette_notification_20e(dateString50);
														 
													}
													else
													{
													
														Date date50 = null;
														String dateString50 = null;
														date50 = formatter1.parse(val);
														dateString50 = formatter2.format(date50);
														pObj.setDate_of_uploading_of_gazette_notification_20e(dateString50);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(29)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date51 = null;
														String dateString51 = null;
														date51 = formatter3.parse(val);
														dateString51 = formatter2.format(date51);
														pObj.setPublication_in_gazette_20e(dateString51);
														 
													}
													else
													{
													
														Date date51 = null;
														String dateString51 = null;
														date51 = formatter1.parse(val);
														dateString51 = formatter2.format(date51);
														pObj.setPublication_in_gazette_20e(dateString51);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(30)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date52 = null;
														String dateString52 = null;
														date52 = formatter3.parse(val);
														dateString52 = formatter2.format(date52);
														pObj.setPublication_of_notice_in_2_local_news_papers_20e(dateString52);
														 
													}
													else
													{
													
														Date date52 = null;
														String dateString52 = null;
														date52 = formatter1.parse(val);
														dateString52 = formatter2.format(date52);
														pObj.setPublication_of_notice_in_2_local_news_papers_20e(dateString52);
														
													}
													
												}
											//Acquisition notice under 20F						
												val = formatter.formatCellValue(row2.getCell(31)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date53 = null;
														String dateString53 = null;
														date53 = formatter3.parse(val);
														dateString53 = formatter2.format(date53);
														pObj.setDate_of_submission_of_draft_notification_to_CALA_20f(dateString53);
														 
													}
													else
													{
													
														Date date53 = null;
														String dateString53 = null;
														date53 = formatter1.parse(val);
														dateString53 = formatter2.format(date53);
														pObj.setDate_of_submission_of_draft_notification_to_CALA_20f(dateString53);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(32)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date54 = null;
														String dateString54 = null;
														date54 = formatter3.parse(val);
														dateString54 = formatter2.format(date54);
														pObj.setApproval_of_CALA_20f(dateString54);
														 
													}
													else
													{
													
														Date date54 = null;
														String dateString54 = null;
														date54 = formatter1.parse(val);
														dateString54 = formatter2.format(date54);
														pObj.setApproval_of_CALA_20f(dateString54);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(33)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date55 = null;
														String dateString55 = null;
														date55 = formatter3.parse(val);
														dateString55 = formatter2.format(date55);
														pObj.setDraft_letter_to_con_for_approval_20f(dateString55);
														 
													}
													else
													{
													
														Date date55 = null;
														String dateString55 = null;
														date55 = formatter1.parse(val);
														dateString55 = formatter2.format(date55);
														pObj.setDraft_letter_to_con_for_approval_20f(dateString55);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(34)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date56 = null;
														String dateString56 = null;
														date56 = formatter3.parse(val);
														dateString56 = formatter2.format(date56);
														pObj.setDate_of_approval_of_construction_20f(dateString56);
														 
													}
													else
													{
													
														Date date56 = null;
														String dateString56 = null;
														date56 = formatter1.parse(val);
														dateString56 = formatter2.format(date56);
														pObj.setDate_of_approval_of_construction_20f(dateString56);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(35)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date57 = null;
														String dateString57 = null;
														date57 = formatter3.parse(val);
														dateString57 = formatter2.format(date57);
														pObj.setDate_of_uploading_of_gazette_notification_20f(dateString57);
														 
													}
													else
													{
													
														Date date57 = null;
														String dateString57 = null;
														date57 = formatter1.parse(val);
														dateString57 = formatter2.format(date57);
														pObj.setDate_of_uploading_of_gazette_notification_20f(dateString57);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(36)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date58 = null;
														String dateString58 = null;
														date58 = formatter3.parse(val);
														dateString58 = formatter2.format(date58);
														pObj.setPublication_in_gazette_20f(dateString58);
														 
													}
													else
													{
													
														Date date58 = null;
														String dateString58 = null;
														date58 = formatter1.parse(val);
														dateString58 = formatter2.format(date58);
														pObj.setPublication_in_gazette_20f(dateString58);
														
													}
													
												}
												val = formatter.formatCellValue(row2.getCell(37)).trim();
												

												
												
												if(!StringUtils.isEmpty(val)) { 
													if(val.contains("/")) 
													{
														Date date59 = null;
														String dateString59 = null;
														date59 = formatter3.parse(val);
														dateString59 = formatter2.format(date59);
														pObj.setPublication_of_notice_in_2_local_news_papers_20f(dateString59);
														 
													}
													else
													{
													
														Date date59 = null;
														String dateString59 = null;
														date59 = formatter1.parse(val);
														dateString59 = formatter2.format(date59);
														pObj.setPublication_of_notice_in_2_local_news_papers_20f(dateString59);
														
													}
													
												}
											}
											if(!StringUtils.isEmpty(pObj) && !StringUtils.isEmpty(pObj.getLa_id())
													&& pObj.getLa_id().equals(la.getLa_id())) {
												pObjList.add(pObj);
											}
										}
								la.setPrivateIRAList(pObjList);
								}
								if(privateLA != null){
									int b  = laprivateLASheet.getLastRowNum();
									for(int j = 2; j <= laprivateLASheet.getLastRowNum();j++){
										XSSFRow row2 = laprivateLASheet.getRow(j);
										LandAcquisition pObj1 = new LandAcquisition();
										if(!StringUtils.isEmpty(row2) && formatter.formatCellValue(row2.getCell(0)).trim().equals(la.getLa_id())) {
											val = formatter.formatCellValue(row2.getCell(0)).trim();
											if(!StringUtils.isEmpty(val)) { pObj1.setLa_id(val);}
										
											val = formatter.formatCellValue(row2.getCell(1)).trim();
											if(!StringUtils.isEmpty(val)) { pObj1.setName_of_the_owner(val);}
											
											val = formatter.formatCellValue(row2.getCell(2)).trim();
											if(!StringUtils.isEmpty(val)) {
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(2));
												}
												pObj1.setBasic_rate(val);}
											
											val = formatter.formatCellValue(row2.getCell(3)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(3));
												}
												pObj1.setHundred_percent_Solatium(val);}
											
											val = formatter.formatCellValue(row2.getCell(4)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(4));
												}
												pObj1.setExtra_25_percent(val);}
											
											val = formatter.formatCellValue(row2.getCell(5)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(5));
												}
												
												pObj1.setTotal_rate_divide_m2(val);}
											
											val = formatter.formatCellValue(row2.getCell(6)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(6));
												}
										
												pObj1.setLand_compensation(val);}
											
											val = formatter.formatCellValue(row2.getCell(7)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(7));
												}
											
												pObj1.setAgriculture_tree_nos(Double.parseDouble(val));}
											
											val = formatter.formatCellValue(row2.getCell(8)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(8));
												}
											
												pObj1.setAgriculture_tree_rate(val);}
											
											val = formatter.formatCellValue(row2.getCell(9)).trim();
											if(!StringUtils.isEmpty(val)) {
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(9));
												}
											
												pObj1.setAgriculture_tree_compensation(val);}
											
											val = formatter.formatCellValue(row2.getCell(10)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(10));
												}
												pObj1.setForest_tree_nos(Double.parseDouble(val));}
											
											val = formatter.formatCellValue(row2.getCell(11)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(11));
												}
												pObj1.setForest_tree_rate(val);}
											
											val = formatter.formatCellValue(row2.getCell(12)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(12));
												}
												pObj1.setForest_tree_compensation(val);}
											
											val = formatter.formatCellValue(row2.getCell(13)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(13));
												}
												pObj1.setStructure_compensation(val);}
											
											val = formatter.formatCellValue(row2.getCell(14)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(14));
												}
												pObj1.setBorewell_compensation(val);}
											
											val = formatter.formatCellValue(row2.getCell(15)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(15));
												}
												pObj1.setTotal_compensation(val);}
											
											val = formatter.formatCellValue(row2.getCell(16)).trim();
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{
													Date date215 = null;
													String dateString215 = null;
													date215 = formatter3.parse(val);
													dateString215 = formatter2.format(date215);
													pObj1.setConsent_from_owner(dateString215);
													 
												}
												else
												{
													Date date215 = null;
													String dateString215 = null;
													date215 = formatter1.parse(val);
													dateString215 = formatter2.format(date215);
													pObj1.setConsent_from_owner(dateString215);
												}												
												
												
												
												
												
											
											}
											val = formatter.formatCellValue(row2.getCell(17)).trim();
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{
													Date date216 = null;
													String dateString216 = null;
													date216 = formatter3.parse(val);
													dateString216 = formatter2.format(date216);
													pObj1.setLegal_search_report(dateString216);
													 
												}
												else
												{
													Date date216 = null;
													String dateString216 = null;
													date216 = formatter1.parse(val);
													dateString216 = formatter2.format(date216);
													pObj1.setLegal_search_report(dateString216);
												}													
												
												
												
												
											
											}
											
											val = formatter.formatCellValue(row2.getCell(18)).trim();
											

											
											
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) 
												{
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter3.parse(val);
													dateString15 = formatter2.format(date15);
													pObj1.setDate_of_registration(dateString15);
													 
												}
												else
												{
												
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter1.parse(val);
													dateString15 = formatter2.format(date15);
													pObj1.setDate_of_registration(dateString15);
												}													
												
												
											
											}
											val = formatter.formatCellValue(row2.getCell(19)).trim();

											

											
											
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{
													Date date16 = null;
													String dateString16 = null;
													date16 = formatter3.parse(val);
													dateString16 = formatter2.format(date16);
													pObj1.setDate_of_possession(dateString16);
													 
												}
												else
												{
												
													Date date16 = null;
													String dateString16 = null;
													date16 = formatter1.parse(val);
													dateString16 = formatter2.format(date16);
													pObj1.setDate_of_possession(dateString16);
													
												}												
												
												
												
											
											}
											

							
											
											pObj1.setDate_of_registration(DateParser.parse(pObj1.getDate_of_registration()));
											pObj1.setDate_of_possession(DateParser.parse(pObj1.getDate_of_possession()));
											pObj1.setConsent_from_owner(DateParser.parse(pObj1.getConsent_from_owner()));
											pObj1.setLegal_search_report(DateParser.parse(pObj1.getLegal_search_report()));
											pObj1.setForest_tree_survey(DateParser.parse(pObj1.getForest_tree_survey()));
											pObj1.setForest_tree_valuation(DateParser.parse(pObj1.getForest_tree_valuation()));
											pObj1.setHorticulture_tree_survey(DateParser.parse(pObj1.getHorticulture_tree_survey()));
											pObj1.setHorticulture_tree_valuation(DateParser.parse(pObj1.getHorticulture_tree_valuation()));
											pObj1.setStructure_survey(DateParser.parse(pObj1.getStructure_survey()));
											pObj1.setStructure_valuation(DateParser.parse(pObj1.getStructure_valuation()));
											pObj1.setBorewell_survey(DateParser.parse(pObj1.getBorewell_survey()));
											pObj1.setBorewell_valuation(DateParser.parse(pObj1.getBorewell_valuation()));
											pObj1.setDate_of_rfp_to_adtp(DateParser.parse(pObj1.getDate_of_rfp_to_adtp()));
											pObj1.setDate_of_rate_fixation_of_land(DateParser.parse(pObj1.getDate_of_rate_fixation_of_land()));
											pObj1.setSdo_demand_for_payment(DateParser.parse(pObj1.getSdo_demand_for_payment()));
											pObj1.setDate_of_approval_for_payment(DateParser.parse(pObj1.getDate_of_approval_for_payment()));
											pObj1.setPrivate_payment_date(DateParser.parse(pObj1.getPrivate_payment_date()));
										} 
										if(!StringUtils.isEmpty(pObj1) && !StringUtils.isEmpty(pObj1.getLa_id())
												&& pObj1.getLa_id().equals(la.getLa_id())) {
											pObjList1.add(pObj1);
										}
									}
							la.setPrivateLAList(pObjList1);
							}	
								if(privateLV != null){
									for(int j = 2; j <= laPrivateLVSheet.getLastRowNum();j++){
										XSSFRow row2 = laPrivateLVSheet.getRow(j);
										LandAcquisition pObj2 = new LandAcquisition();
										if(!StringUtils.isEmpty(row2) && formatter.formatCellValue(row2.getCell(0)).trim().equals(la.getLa_id())) {
											val = formatter.formatCellValue(row2.getCell(0)).trim();
											if(!StringUtils.isEmpty(val)) { pObj2.setLa_id(val);}
										
											val = formatter.formatCellValue(row2.getCell(1)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter3.parse(val);
													dateString15 = formatter2.format(date15);	
													pObj2.setForest_tree_survey(dateString15);
													 
												}
												else
												{
												
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter1.parse(val);
													dateString15 = formatter2.format(date15);	
													pObj2.setForest_tree_survey(dateString15);
													
												}													
												
												
												
											
											
											}
											
											val = formatter.formatCellValue(row2.getCell(2)).trim();

											
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date19 = null;
													String dateString19 = null;
													date19 = formatter3.parse(val);
													dateString19 = formatter2.format(date19);
													pObj2.setForest_tree_valuation(dateString19);
													 
												}
												else
												{
												
													Date date19 = null;
													String dateString19 = null;
													date19 = formatter1.parse(val);
													dateString19 = formatter2.format(date19);
													pObj2.setForest_tree_valuation(dateString19);
													
												}													
												
												
											
												
											
											
											}
											
											val = formatter.formatCellValue(row2.getCell(3)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date20 = null;
													String dateString20 = null;
													date20 = formatter3.parse(val);
													dateString20 = formatter2.format(date20);	
													pObj2.setHorticulture_tree_survey(dateString20);
													 
												}
												else
												{
												
													Date date20 = null;
													String dateString20 = null;
													date20 = formatter1.parse(val);
													dateString20 = formatter2.format(date20);	
													pObj2.setHorticulture_tree_survey(dateString20);
													
												}													
												
												
												
											
											}
											
											val = formatter.formatCellValue(row2.getCell(4)).trim();


											
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date21 = null;
													String dateString21 = null;
													date21 = formatter3.parse(val);
													dateString21 = formatter2.format(date21);
													pObj2.setHorticulture_tree_valuation(dateString21);
													 
												}
												else
												{
												
													Date date21 = null;
													String dateString21 = null;
													date21 = formatter1.parse(val);
													dateString21 = formatter2.format(date21);
													pObj2.setHorticulture_tree_valuation(dateString21);
													
												}													
												
												
												
											
											}
											
											val = formatter.formatCellValue(row2.getCell(5)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{
													Date date22 = null;
													String dateString22 = null;
													date22 = formatter3.parse(val);
													dateString22 = formatter2.format(date22);	
													pObj2.setStructure_survey(dateString22);
													 
												}
												else
												{
												
													Date date22 = null;
													String dateString22 = null;
													date22 = formatter1.parse(val);
													dateString22 = formatter2.format(date22);	
													pObj2.setStructure_survey(dateString22);
													
												}													
												
												
												
											
											}
											
											val = formatter.formatCellValue(row2.getCell(6)).trim();


											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date23 = null;
													String dateString23 = null;
													date23 = formatter3.parse(val);
													dateString23 = formatter2.format(date23);
													pObj2.setStructure_valuation(dateString23);
													 
												}
												else
												{
												
													Date date23 = null;
													String dateString23 = null;
													date23 = formatter1.parse(val);
													dateString23 = formatter2.format(date23);
													pObj2.setStructure_valuation(dateString23);
													
												}													
												
												
												
											
											
											}
											
											val = formatter.formatCellValue(row2.getCell(7)).trim();


											
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date24 = null;
													String dateString24 = null;
													date24 = formatter3.parse(val);
													dateString24 = formatter2.format(date24);
													pObj2.setBorewell_survey(dateString24);
													 
												}
												else
												{
												
													Date date24 = null;
													String dateString24 = null;
													date24 = formatter1.parse(val);
													dateString24 = formatter2.format(date24);
													pObj2.setBorewell_survey(dateString24);
													
												}													
												
												
												
												
											}
											
											val = formatter.formatCellValue(row2.getCell(8)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date25 = null;
													String dateString25 = null;
													date25 = formatter3.parse(val);
													dateString25 = formatter2.format(date25);	
													pObj2.setBorewell_valuation(dateString25);
													 
												}
												else
												{
												
													Date date25 = null;
													String dateString25 = null;
													date25 = formatter1.parse(val);
													dateString25 = formatter2.format(date25);	
													pObj2.setBorewell_valuation(dateString25);
													
												}													
												
												
												
											
											}
											
											val = formatter.formatCellValue(row2.getCell(9)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date26 = null;
													String dateString26 = null;
													date26 = formatter3.parse(val);
													dateString26 = formatter2.format(date26);	
													pObj2.setDate_of_rfp_to_adtp(dateString26);
													 
												}
												else
												{
												
													Date date26 = null;
													String dateString26 = null;
													date26 = formatter1.parse(val);
													dateString26 = formatter2.format(date26);	
													pObj2.setDate_of_rfp_to_adtp(dateString26);
													
												}													
												
												
												
												
											
											}
											
											
											val = formatter.formatCellValue(row2.getCell(10)).trim();
											

											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date27 = null;
													String dateString27 = null;
													date27 = formatter3.parse(val);
													dateString27 = formatter2.format(date27);
													pObj2.setDate_of_rate_fixation_of_land(dateString27);
													 
												}
												else
												{
												
													Date date27 = null;
													String dateString27 = null;
													date27 = formatter1.parse(val);
													dateString27 = formatter2.format(date27);
													pObj2.setDate_of_rate_fixation_of_land(dateString27);
													
												}													
												
												
												
											}
											
											val = formatter.formatCellValue(row2.getCell(11)).trim();


											
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) 
												{
													Date date28 = null;
													String dateString28 = null;
													date28 = formatter3.parse(val);
													dateString28 = formatter2.format(date28);
													pObj2.setSdo_demand_for_payment(dateString28);
													 
												}
												else
												{
												
													Date date28 = null;
													String dateString28 = null;
													date28 = formatter1.parse(val);
													dateString28 = formatter2.format(date28);
													pObj2.setSdo_demand_for_payment(dateString28);
													
												}													
												
												
												
											}
											
											val = formatter.formatCellValue(row2.getCell(12)).trim();


											
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) 
												{
													Date date29 = null;
													String dateString29 = null;
													date29 = formatter3.parse(val);
													dateString29 = formatter2.format(date29);
													pObj2.setDate_of_approval_for_payment(dateString29);
													 
												}
												else
												{
												
													Date date29 = null;
													String dateString29 = null;
													date29 = formatter1.parse(val);
													dateString29 = formatter2.format(date29);
													pObj2.setDate_of_approval_for_payment(dateString29);
													
												}													
												
												}
											
											val = formatter.formatCellValue(row2.getCell(13)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(13));
												}
												pObj2.setPayment_amount(val);}
											
											val = formatter.formatCellValue(row2.getCell(14)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) 
												{
													Date date30 = null;
													String dateString30 = null;
													date30 = formatter3.parse(val);
													dateString30 = formatter2.format(date30);	
													pObj2.setPrivate_payment_date(dateString30);
													 
												}
												else
												{
												
													Date date30 = null;
													String dateString30 = null;
													date30 = formatter1.parse(val);
													dateString30 = formatter2.format(date30);	
													pObj2.setPrivate_payment_date(dateString30);
													
												}													
												
												
												
											
											}
						
											
											pObj2.setForest_tree_survey(DateParser.parse(pObj2.getForest_tree_survey()));
											pObj2.setForest_tree_valuation(DateParser.parse(pObj2.getForest_tree_valuation()));
											pObj2.setHorticulture_tree_survey(DateParser.parse(pObj2.getHorticulture_tree_survey()));
											pObj2.setHorticulture_tree_valuation(DateParser.parse(pObj2.getHorticulture_tree_valuation()));
											pObj2.setStructure_survey(DateParser.parse(pObj2.getStructure_survey()));
											pObj2.setStructure_valuation(DateParser.parse(pObj2.getStructure_valuation()));
											pObj2.setBorewell_survey(DateParser.parse(pObj2.getBorewell_survey()));
											pObj2.setBorewell_valuation(DateParser.parse(pObj2.getBorewell_valuation()));
											pObj2.setDate_of_rfp_to_adtp(DateParser.parse(pObj2.getDate_of_rfp_to_adtp()));
											pObj2.setDate_of_rate_fixation_of_land(DateParser.parse(pObj2.getDate_of_rate_fixation_of_land()));
											pObj2.setSdo_demand_for_payment(DateParser.parse(pObj2.getSdo_demand_for_payment()));
											pObj2.setDate_of_approval_for_payment(DateParser.parse(pObj2.getDate_of_approval_for_payment()));
											pObj2.setPrivate_payment_date(DateParser.parse(pObj2.getPrivate_payment_date()));
											
										}
										if(!StringUtils.isEmpty(pObj2) && !StringUtils.isEmpty(pObj2.getLa_id())
												&& pObj2.getLa_id().equals(la.getLa_id())) {
											pObjList2.add(pObj2);
										}
									}
							la.setPrivateLVList(pObjList2);
							}
								if(Gov != null){
									for(int j = 2; j <= GovSheet.getLastRowNum();j++){
										XSSFRow row2 = GovSheet.getRow(j);
										LandAcquisition gov = new LandAcquisition();
										if(!StringUtils.isEmpty(row2) && formatter.formatCellValue(row2.getCell(0)).trim().equals(la.getLa_id())) {
											val = formatter.formatCellValue(row2.getCell(0)).trim();
											if(!StringUtils.isEmpty(val)) { gov.setLa_id(val);}
									
											val = formatter.formatCellValue(row2.getCell(1)).trim();
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) 
												{
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter3.parse(val);
													dateString15 = formatter2.format(date15);													
													
													gov.setProposal_submission(dateString15);

												 
												}
												else
												{
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter1.parse(val);
													dateString15 = formatter2.format(date15);														
													
													gov.setProposal_submission(dateString15);
												}												
												
												
												
												
											}
											
										
											val = formatter.formatCellValue(row2.getCell(2)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter3.parse(val);
													dateString15 = formatter2.format(date15);													
													
													gov.setValuation_date(dateString15);

												 
												}
												else
												{
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter1.parse(val);
													dateString15 = formatter2.format(date15);														
													
													gov.setValuation_date(dateString15);
												}												
												
												
												
											
											
											
											}
											
											val = formatter.formatCellValue(row2.getCell(3)).trim();
											

											
											if(!StringUtils.isEmpty(val)) { 
												

												if(val.contains("/")) 
												{
													Date date16 = null;
													String dateString16 = null;
													date16 = formatter3.parse(val);
													dateString16 = formatter2.format(date16);													
													
													gov.setLetter_for_payment(dateString16);
												}
												else
												{
													Date date16 = null;
													String dateString16 = null;
													date16 = formatter1.parse(val);
													dateString16 = formatter2.format(date16);														
													gov.setLetter_for_payment(dateString16);
												}
											
											}
											
											val = formatter.formatCellValue(row2.getCell(4)).trim();
											if(!StringUtils.isEmpty(val)) {
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												//System.out.println(val);
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(4));
												}
												
												gov.setAmount_demanded(val);}
											
											
											val = formatter.formatCellValue(row2.getCell(5)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date17 = null;
													String dateString17 = null;
													date17 = formatter3.parse(val);
													dateString17 = formatter2.format(date17);														
													
													gov.setApproval_for_payment(dateString17);
												}
												else
												{
													Date date17 = null;
													String dateString17 = null;
													date17 = formatter1.parse(val);
													dateString17 = formatter2.format(date17);															
													gov.setApproval_for_payment(dateString17);
												}

											}
											
											val = formatter.formatCellValue(row2.getCell(6)).trim();
											
											
											
											if(!StringUtils.isEmpty(val)) 
											{

												if(val.contains("/")) 
												{
													Date date18 = null;
													String dateString18 = null;
													date18 = formatter3.parse(val);
													dateString18 = formatter2.format(date18);														
													
													gov.setPayment_date(dateString18);
												}
												else
												{
													Date date18 = null;
													String dateString18 = null;
													date18 = formatter1.parse(val);
													dateString18 = formatter2.format(date18);															
													gov.setPayment_date(dateString18);
												}
												
												
												
												
											}
											
											val = formatter.formatCellValue(row2.getCell(7)).trim();
											if(!StringUtils.isEmpty(val)) {
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(7));
												}
												gov.setAmount_paid(val);}
											
											val = formatter.formatCellValue(row2.getCell(8)).trim();
											
											
											
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) 
												{
													Date date19 = null;
													String dateString19 = null;
													date19 = formatter3.parse(val);
													dateString19 = formatter2.format(date19);															
													
													gov.setPossession_date(dateString19);
												}
												else
												{
													Date date19 = null;
													String dateString19 = null;
													date19 = formatter1.parse(val);
													dateString19 = formatter2.format(date19);																
													gov.setPossession_date(dateString19);
												}												
												
												
											
											}
											
							
											
											gov.setProposal_submission(DateParser.parse(gov.getProposal_submission()));
											gov.setLetter_for_payment(DateParser.parse(gov.getLetter_for_payment()));
											gov.setApproval_for_payment(DateParser.parse(gov.getApproval_for_payment()));
											gov.setPayment_date(DateParser.parse(gov.getPayment_date()));
											gov.setPossession_date(DateParser.parse(gov.getPossession_date()));
											gov.setValuation_date(DateParser.parse(gov.getValuation_date()));
										}
										if(!StringUtils.isEmpty(gov) && !StringUtils.isEmpty(gov.getLa_id())
												&& gov.getLa_id().equals(la.getLa_id())) {
											gObjList.add(gov);
										}
									}
							la.setGovList(gObjList);
							}
								if(forest != null){
									for(int j = 2; j <= forestSheet.getLastRowNum();j++){
										XSSFRow row2 = forestSheet.getRow(j);
										LandAcquisition fObj = new LandAcquisition();
										if(!StringUtils.isEmpty(row2) && formatter.formatCellValue(row2.getCell(0)).trim().equals(la.getLa_id())) {

										
											
											val = formatter.formatCellValue(row2.getCell(0)).trim();
											if(!StringUtils.isEmpty(val)) { fObj.setLa_id(val);}
											
											val = formatter.formatCellValue(row2.getCell(1)).trim();
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) {
													Date date = null;
													String dateString = null;
													date = formatter3.parse(val);
													dateString = formatter2.format(date);												
													fObj.setForest_online_submission(dateString);
												 
											}
											else
											{
											
												Date date = null;
												String dateString = null;
												date = formatter1.parse(val);
												dateString = formatter2.format(date);
												fObj.setForest_online_submission(dateString);
												
											}												
												
												
												
												
											
											}
											
											val = formatter.formatCellValue(row2.getCell(2)).trim();
											
											if(!StringUtils.isEmpty(val)) { 
											if(val.contains("/")) {
													Date date = null;
													String dateString = null;
													date = formatter3.parse(val);
													dateString = formatter2.format(date);												
													fObj.setForest_submission_date_to_dycfo(dateString);
												 
											}
											else
											{
											
												Date date = null;
												String dateString = null;
												date = formatter1.parse(val);
												dateString = formatter2.format(date);
												fObj.setForest_submission_date_to_dycfo(dateString);
												
											}
											}
											
											
											val = formatter.formatCellValue(row2.getCell(3)).trim();
											
											
	
											
											if(!StringUtils.isEmpty(val)) { 
												if(val.contains("/")) 
												{
													Date date1 = null;
													String dateString1 = null;
													date1 = formatter3.parse(val);
													dateString1 = formatter2.format(date1);												
													fObj.setForest_submission_date_to_ccf_thane(dateString1);
	
												 
												}
												else
												{
												
													Date date1 = null;
													String dateString1 = null;
													date1 = formatter1.parse(val);
													dateString1 = formatter2.format(date1);	
													fObj.setForest_submission_date_to_ccf_thane(dateString1);
												}
											}
											
											
											
											
											val = formatter.formatCellValue(row2.getCell(4)).trim();
											

											if(!StringUtils.isEmpty(val)) { 	
												
												
												if(val.contains("/")) 
												{
													Date date2 = null;
													String dateString2 = null;
													date2 = formatter3.parse(val);
													dateString2 = formatter2.format(date2);													
													
													fObj.setForest_submission_date_to_nodal_officer(dateString2);

												 
												}
												else
												{
												
													Date date2 = null;
													String dateString2 = null;
													date2 = formatter1.parse(val);
													dateString2 = formatter2.format(date2);													
													
													fObj.setForest_submission_date_to_nodal_officer(dateString2);
												}
										}
												

											
											val = formatter.formatCellValue(row2.getCell(5)).trim();
											

											
											if(!StringUtils.isEmpty(val)) {
												

												if(val.contains("/")) 
												{
													Date date3 = null;
													String dateString3 = null;
													date3 = formatter3.parse(val);
													dateString3 = formatter2.format(date3);													
													fObj.setForest_submission_date_to_revenue_secretary_mantralaya(dateString3);

												 
												}
												else
												{
												
													Date date3 = null;
													String dateString3 = null;
													date3 = formatter1.parse(val);
													dateString3 = formatter2.format(date3);													
													fObj.setForest_submission_date_to_revenue_secretary_mantralaya(dateString3);
												}
												
												
									}	

											
											val = formatter.formatCellValue(row2.getCell(6)).trim();
											

											
											if(!StringUtils.isEmpty(val)) {
												
												
												
												if(val.contains("/")) 
												{
													Date date4 = null;
													String dateString4 = null;
													date4 = formatter3.parse(val);
													dateString4 = formatter2.format(date4);													
													
													fObj.setForest_submission_date_to_regional_office_nagpur(dateString4);

												 
												}
												else
												{
												
													Date date4 = null;
													String dateString4 = null;
													date4 = formatter1.parse(val);
													dateString4 = formatter2.format(date4);													
													
													fObj.setForest_submission_date_to_regional_office_nagpur(dateString4);
												}												
												
												

											}
											
											val = formatter.formatCellValue(row2.getCell(7)).trim();
											

											
											
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{
													Date date5 = null;
													String dateString5 = null;
													date5 = formatter3.parse(val);
													dateString5 = formatter2.format(date5);													
													
													fObj.setForest_date_of_approval_by_regional_office_nagpur(dateString5);

												 
												}
												else
												{
													Date date5 = null;
													String dateString5 = null;
													date5 = formatter1.parse(val);
													dateString5 = formatter2.format(date5);													
													
													fObj.setForest_date_of_approval_by_regional_office_nagpur(dateString5);
												}													
												

											}
											
											val = formatter.formatCellValue(row2.getCell(8)).trim();
											

											
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{
													Date date6 = null;
													String dateString6 = null;
													date6 = formatter3.parse(val);
													dateString6 = formatter2.format(date6);													
													
													fObj.setForest_valuation_by_dycfo(dateString6);

												 
												}
												else
												{
													Date date6 = null;
													String dateString6 = null;
													date6 = formatter1.parse(val);
													dateString6 = formatter2.format(date6);													
													
													fObj.setForest_valuation_by_dycfo(dateString6);
												}													
												

										}
											
											val = formatter.formatCellValue(row2.getCell(9)).trim();
											if(!StringUtils.isEmpty(val)) {
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(9));
												}
												fObj.setForest_demanded_amount(val);}

											val = formatter.formatCellValue(row2.getCell(10)).trim();
											

											
											
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{
													Date date71 = null;
													String dateString71 = null;
													date71 = formatter3.parse(val);
													dateString71 = formatter2.format(date71);													
													
													
													fObj.setForest_approval_for_payment(dateString71);

												 
												}
												else
												{
													Date date71 = null;
													String dateString71 = null;
													date71 = formatter1.parse(val);
													dateString71 = formatter2.format(date71);													
													
													
													fObj.setForest_approval_for_payment(dateString71);
												}												
												

											}

											val = formatter.formatCellValue(row2.getCell(11)).trim();
											
											
											
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) 
												{
													Date date7 = null;
													String dateString7 = null;
													date7 = formatter3.parse(val);
													dateString7 = formatter2.format(date7);													
													
													
													
													fObj.setForest_payment_date(dateString7);

												 
												}
												else
												{
													Date date7 = null;
													String dateString7 = null;
													date7 = formatter1.parse(val);
													dateString7 = formatter2.format(date7);													
													
													
													
													fObj.setForest_payment_date(dateString7);
												}												
												

											}

											val = formatter.formatCellValue(row2.getCell(12)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(12));
												}
										
												fObj.setForest_payment_amount(val);}

											

											val = formatter.formatCellValue(row2.getCell(13)).trim();
											
												
											
											if(!StringUtils.isEmpty(val)) {
												
												
												
												if(val.contains("/")) 
												{
													Date date8 = null;
													String dateString8 = null;
													date8 = formatter3.parse(val);
													dateString8 = formatter2.format(date8);													
													
													
													
													fObj.setForest_possession_date(dateString8);

												 
												}
												else
												{
													Date date8 = null;
													String dateString8 = null;
													date8 = formatter1.parse(val);
													dateString8 = formatter2.format(date8);													
													
													
													
													fObj.setForest_possession_date(dateString8);
												}													
												

										}


											
											fObj.setForest_online_submission(DateParser.parse(fObj.getForest_online_submission()));
											fObj.setForest_submission_date_to_dycfo(DateParser.parse(fObj.getForest_submission_date_to_dycfo()));
											fObj.setForest_submission_date_to_ccf_thane(DateParser.parse(fObj.getForest_submission_date_to_ccf_thane()));
											fObj.setForest_submission_date_to_nodal_officer(DateParser.parse(fObj.getForest_submission_date_to_nodal_officer()));
											fObj.setForest_submission_date_to_revenue_secretary_mantralaya(DateParser.parse(fObj.getForest_submission_date_to_revenue_secretary_mantralaya()));
											fObj.setForest_submission_date_to_regional_office_nagpur(DateParser.parse(fObj.getForest_submission_date_to_regional_office_nagpur()));
											fObj.setForest_date_of_approval_by_regional_office_nagpur(DateParser.parse(fObj.getForest_date_of_approval_by_regional_office_nagpur()));
											fObj.setForest_valuation_by_dycfo(DateParser.parse(fObj.getForest_valuation_by_dycfo()));
											fObj.setForest_approval_for_payment(DateParser.parse(fObj.getForest_approval_for_payment()));
											fObj.setForest_payment_date(DateParser.parse(fObj.getForest_payment_date()));
											fObj.setForest_possession_date(DateParser.parse(fObj.getForest_possession_date()));
										}
										if(!StringUtils.isEmpty(fObj) && !StringUtils.isEmpty(fObj.getLa_id())
												&& fObj.getLa_id().equals(la.getLa_id())) {
											fObjList.add(fObj);
										}
									}
							la.setForestList(fObjList);
							}
								if(railway != null){
									for(int j = 2; j <= railwaySheet.getLastRowNum();j++){
										XSSFRow row2 = railwaySheet.getRow(j);
										LandAcquisition railways = new LandAcquisition();
										if(!StringUtils.isEmpty(row2) && formatter.formatCellValue(row2.getCell(0)).trim().equals(la.getLa_id())) {
											val = formatter.formatCellValue(row2.getCell(0)).trim();
											if(!StringUtils.isEmpty(val)) { railways.setLa_id(val);}
											
											val = formatter.formatCellValue(row2.getCell(1)).trim();
											if(!StringUtils.isEmpty(val)) {railways.setRailway_online_submission(val);}
											
											val = formatter.formatCellValue(row2.getCell(2)).trim();

											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{
														Date date9 = null;
														String dateString9 = null;
														date9 = formatter3.parse(val);
														dateString9 = formatter2.format(date9);													
														railways.setRailway_submission_date_to_DyCFO(dateString9);
													 
												}
												else
												{
												
													Date date9 = null;
													String dateString9 = null;
													date9 = formatter1.parse(val);
													dateString9 = formatter2.format(date9);	
													railways.setRailway_submission_date_to_DyCFO(dateString9);
													
												}												

											
											}
											
											val = formatter.formatCellValue(row2.getCell(3)).trim();


											
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
														Date date10 = null;
														String dateString10 = null;
														date10 = formatter3.parse(val);
														dateString10 = formatter2.format(date10);											
														railways.setRailway_submission_date_to_CCF_Thane(dateString10);
													 
												}
												else
												{
												
													Date date10 = null;
													String dateString10 = null;
													date10 = formatter1.parse(val);
													dateString10 = formatter2.format(date10);
													railways.setRailway_submission_date_to_CCF_Thane(dateString10);
													
												}												
												
												
												
											}
											
											val = formatter.formatCellValue(row2.getCell(4)).trim();
											

											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date11 = null;
													String dateString11 = null;
													date11 = formatter3.parse(val);
													dateString11 = formatter2.format(date11);											
													railways.setRailway_submission_date_to_nodal_officer_CCF_Nagpur(dateString11);
													 
												}
												else
												{
												
													Date date11 = null;
													String dateString11 = null;
													date11 = formatter1.parse(val);
													dateString11 = formatter2.format(date11);
													railways.setRailway_submission_date_to_nodal_officer_CCF_Nagpur(dateString11);
													
												}													
												
										}
											
											val = formatter.formatCellValue(row2.getCell(5)).trim();
											

											
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{
													Date date12 = null;
													String dateString12 = null;
													date12 = formatter3.parse(val);
													dateString12 = formatter2.format(date12);										
													railways.setRailway_submission_date_to_revenue_secretary_mantralaya(dateString12);
													 
												}
												else
												{
												
													Date date12 = null;
													String dateString12 = null;
													date12 = formatter1.parse(val);
													dateString12 = formatter2.format(date12);
													railways.setRailway_submission_date_to_revenue_secretary_mantralaya(dateString12);
													
												}												
											}
											
											val = formatter.formatCellValue(row2.getCell(6)).trim();

											
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{

													Date date13 = null;
													String dateString13 = null;
													date13 = formatter3.parse(val);
													dateString13 = formatter2.format(date13);									
													railways.setRailway_submission_date_to_regional_office_nagpur(dateString13);
													 
												}
												else
												{
												

													Date date13 = null;
													String dateString13 = null;
													date13 = formatter1.parse(val);
													dateString13 = formatter2.format(date13);
													railways.setRailway_submission_date_to_regional_office_nagpur(dateString13);
													
												}												
												
												
											}
											
											val = formatter.formatCellValue(row2.getCell(7)).trim();


											
											if(!StringUtils.isEmpty(val)) {
												

												if(val.contains("/")) 
												{

													Date date51 = null;
													String dateString51 = null;
													date51 = formatter3.parse(val);
													dateString51 = formatter2.format(date51);								
													railways.setRailway_date_of_approval_by_Rregional_Office_agpur(dateString51);
													 
												}
												else
												{
												

													Date date51 = null;
													String dateString51 = null;
													date51 = formatter1.parse(val);
													dateString51 = formatter2.format(date51);
													railways.setRailway_date_of_approval_by_Rregional_Office_agpur(dateString51);
													
												}

											}											
											
											
											
											val = formatter.formatCellValue(row2.getCell(8)).trim();
	
										
											
											
											if(!StringUtils.isEmpty(val)) {
												
												if(val.contains("/")) 
												{

													Date date67 = null;
													String dateString67 = null;
													date67 = formatter3.parse(val);
													dateString67 = formatter2.format(date67);								
													railways.setRailway_valuation_by_DyCFO(dateString67);
													 
												}
												else
												{
												
													Date date67 = null;
													String dateString67 = null;
													date67 = formatter1.parse(val);
													dateString67 = formatter2.format(date67);	
													railways.setRailway_valuation_by_DyCFO(dateString67);
													
												}												
												
												
												
											
											}
											
											
											
											val = formatter.formatCellValue(row2.getCell(9)).trim();
											
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(9));
												}
												railways.setRailway_demanded_amount(val);}											
											
											val = formatter.formatCellValue(row2.getCell(10)).trim();
											
											
											
											if(!StringUtils.isEmpty(val)) { 
												
												if(val.contains("/")) 
												{

													Date date68 = null;
													String dateString68 = null;
													date68 = formatter3.parse(val);
													dateString68 = formatter2.format(date68);							
													railways.setRailway_approval_for_payment(dateString68);
													 
												}
												else
												{
												
													Date date68 = null;
													String dateString68 = null;
													date68 = formatter1.parse(val);
													dateString68 = formatter2.format(date68);
													railways.setRailway_approval_for_payment(dateString68);
													
												}													
												
												
												
											
											}
											
											val = formatter.formatCellValue(row2.getCell(11)).trim();
											

											
											
											if(!StringUtils.isEmpty(val)) {
												
												
												if(val.contains("/")) 
												{

													Date date15 = null;
													String dateString15 = null;
													date15 = formatter3.parse(val);
													dateString15 = formatter2.format(date15);							
													railways.setRailway_payment_date(dateString15);
													 
												}
												else
												{
												
													Date date15 = null;
													String dateString15 = null;
													date15 = formatter1.parse(val);
													dateString15 = formatter2.format(date15);
													railways.setRailway_payment_date(dateString15);
													
												}													
												
												
												
											
											}
											

											
											val = formatter.formatCellValue(row2.getCell(12)).trim();
											if(!StringUtils.isEmpty(val)) { 
												int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
												if(c != 2) {
													val = getCellDataType(workbook,row2.getCell(12));
												}
												railways.setRailway_payment_amount(val);}
											
											val = formatter.formatCellValue(row2.getCell(13)).trim();
											
										
											
											if(!StringUtils.isEmpty(val)) { 
												
												
												if(val.contains("/")) 
												{

													Date date16 = null;
													String dateString16 = null;
													date16 = formatter3.parse(val);
													dateString16 = formatter2.format(date16);							
													railways.setRailway_possession_date(dateString16);
													 
												}
												else
												{
												
													Date date16 = null;
													String dateString16 = null;
													date16 = formatter1.parse(val);
													dateString16 = formatter2.format(date16);	
													railways.setRailway_possession_date(dateString16);
													
												}													
												
												
												
											
											}
											

											
											railways.setRailway_online_submission(DateParser.parse(railways.getRailway_online_submission()));
											railways.setRailway_submission_date_to_DyCFO(DateParser.parse(railways.getRailway_submission_date_to_DyCFO()));
											railways.setRailway_submission_date_to_CCF_Thane(DateParser.parse(railways.getRailway_submission_date_to_CCF_Thane()));
											railways.setRailway_submission_date_to_nodal_officer_CCF_Nagpur(DateParser.parse(railways.getRailway_submission_date_to_nodal_officer_CCF_Nagpur()));
											railways.setRailway_submission_date_to_revenue_secretary_mantralaya(DateParser.parse(railways.getRailway_submission_date_to_revenue_secretary_mantralaya()));
											railways.setRailway_submission_date_to_regional_office_nagpur(DateParser.parse(railways.getRailway_submission_date_to_regional_office_nagpur()));
											railways.setRailway_date_of_approval_by_Rregional_Office_agpur(DateParser.parse(railways.getRailway_date_of_approval_by_Rregional_Office_agpur()));
											railways.setRailway_valuation_by_DyCFO(DateParser.parse(railways.getRailway_valuation_by_DyCFO()));
											railways.setRailway_approval_for_payment(DateParser.parse(railways.getRailway_approval_for_payment()));
											railways.setRailway_payment_date(DateParser.parse(railways.getRailway_payment_date()));
											railways.setRailway_possession_date(DateParser.parse(railways.getRailway_possession_date()));
										}
										if(!StringUtils.isEmpty(railways) && !StringUtils.isEmpty(railways.getLa_id())
												&& railways.getLa_id().equals(la.getLa_id())) {
											rObjList.add(railways);
										}
								
									}
							la.setRailwayList(rObjList);
							}
						boolean flag = la.checkNullOrEmpty();

						if(!flag && !StringUtils.isEmpty(la.getLa_id())) {
							lasList.add(la);
						}

					}
					if(!lasList.isEmpty() && lasList != null){
						//count  = laService.uploadLAs(lasList);
						String[] arr  = landAquisitionService.uploadLAData(lasList,la);
						result[0] = arr[0];
						result[1] = arr[1];
						result[2] = arr[2];
						result[3] = arr[3];
						result[4] = arr[4];						
					}
				}
				workbook.close();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uploadLAs() : "+e.getMessage());
			throw new Exception(e);	
		}finally{
		    try{
		        if ( w != null)
		        	w.close( );
		    }catch ( IOException e){
		    	e.printStackTrace();
		    	logger.error("uploadLAs() : "+e.getMessage());
		    	throw new Exception(e);
		    }
		}
		
		return result;
	}
	
	private String[] uploadLASingleSheet(LandAcquisition obj, String userId, String userName) throws Exception {
		LandAcquisition la = null;
		List<LandAcquisition> lasList = new ArrayList<LandAcquisition>();
		String[] result = new String[5];
		Writer w = null;
		int count = 0;
		try {	
			MultipartFile excelfile = obj.getLaUploadFile();
			// Creates a workbook object from the uploaded excelfile
			if (!StringUtils.isEmpty(excelfile) && excelfile.getSize() > 0 ){
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				int sheetsCount = workbook.getNumberOfSheets();
				if(sheetsCount > 0) {
					
					XSSFSheet laSheet = workbook.getSheetAt(1);
					//System.out.println(uploadFilesSheet.getSheetName());
					//header row
					//XSSFRow headerRow = uploadFilesSheet.getRow(0);							
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					//System.out.println(uploadFilesSheet.getLastRowNum());
					for(int i = 2; i <= laSheet.getLastRowNum();i++){
						int v = laSheet.getLastRowNum();
						XSSFRow row = laSheet.getRow(i);
						// Sets the Read data to the model class
						// Cell cell = row.getCell(0);
						// String j_username = formatter.formatCellValue(row.getCell(0));
						//System.out.println(i);
						la = new LandAcquisition();
						la.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
						la.setUser_role_code(obj.getUser_role_code());
						String val = null;
						if(!StringUtils.isEmpty(row)) {								
							val = formatter.formatCellValue(row.getCell(0)).trim();
							if(!StringUtils.isEmpty(val)) { la.setProject_id_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(1)).trim();
							if(!StringUtils.isEmpty(val)) { la.setSurvey_number(val);}
							
							
							val = formatter.formatCellValue(row.getCell(2)).trim();
							if(!StringUtils.isEmpty(val)) { la.setCategory_fk(val);}
							
							val = formatter.formatCellValue(row.getCell(3)).trim();
							if(!StringUtils.isEmpty(val)) { la.setLa_sub_category_fk(val);}	
							
							
							val = formatter.formatCellValue(row.getCell(4)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(4));
								}
							
								la.setArea_of_plot(val);}	
							
							val = formatter.formatCellValue(row.getCell(5)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(5));
								}
								la.setArea_to_be_acquired(val);}	
							
							val = formatter.formatCellValue(row.getCell(6)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(6));
								}
								la.setArea_acquired(val);}	
							
							val = formatter.formatCellValue(row.getCell(7)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(7));
								}
								la.setLa_land_status_fk(val);}	
							
							val = formatter.formatCellValue(row.getCell(8)).trim();
							if(!StringUtils.isEmpty(val)) { la.setChainage_from(val);}	
							
							
							val = formatter.formatCellValue(row.getCell(9)).trim();
							if(!StringUtils.isEmpty(val)) { la.setChainage_to(val);}								
							
							val = formatter.formatCellValue(row.getCell(10)).trim();
							if(!StringUtils.isEmpty(val)) { la.setVillage(val);}										
							
							val = formatter.formatCellValue(row.getCell(11)).trim();
							if(!StringUtils.isEmpty(val)) { la.setTaluka(val);}
							
							val = formatter.formatCellValue(row.getCell(12)).trim();
							if(!StringUtils.isEmpty(val)) { la.setLatitude(val);}							
							
							val = formatter.formatCellValue(row.getCell(13)).trim();
							if(!StringUtils.isEmpty(val)) { la.setLongitude(val);}
							
							val = formatter.formatCellValue(row.getCell(14)).trim();
							if(!StringUtils.isEmpty(val)) { la.setDy_slr(val);}							
							
							val = formatter.formatCellValue(row.getCell(15)).trim();
							if(!StringUtils.isEmpty(val)) { la.setSdo(val);}	
							
							val = formatter.formatCellValue(row.getCell(16)).trim();
							if(!StringUtils.isEmpty(val)) { la.setCollector(val);}
							
							val = formatter.formatCellValue(row.getCell(17)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setProposal_submission_date_to_collector(val);}
							
							val = formatter.formatCellValue(row.getCell(18)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_fee_letter_received_date(val);}
							
							
							val = formatter.formatCellValue(row.getCell(19)).trim();
							if(!StringUtils.isEmpty(val)) {
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(19));
								}
								la.setJm_fee_amount(val);
							}
						
							val = formatter.formatCellValue(row.getCell(20)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_fee_paid_date(val);}								
							
							val = formatter.formatCellValue(row.getCell(21)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_start_date(val);}	
							
							val = formatter.formatCellValue(row.getCell(22)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_completion_date(val);}	
							
							val = formatter.formatCellValue(row.getCell(23)).trim();
							if(!StringUtils.isEmpty(val)) { 
								if(val.contains("/")) {
									
									 
								}
								la.setJm_sheet_date_to_sdo(val);}				
							
							val = formatter.formatCellValue(row.getCell(24)).trim();
							if(!StringUtils.isEmpty(val)) { la.setJm_remarks(val);}										
						
							val = formatter.formatCellValue(row.getCell(25)).trim();
							if(!StringUtils.isEmpty(val)) { 
								int c = org.apache.commons.lang3.StringUtils.countMatches(val, "$");
								if(c != 2) {
									val = getCellDataType(workbook,row.getCell(25));
								}
								la.setJm_approval(val);}
							
							val = formatter.formatCellValue(row.getCell(26)).trim();
							if(!StringUtils.isEmpty(val)) { la.setSpecial_feature(val);}
							
							val = formatter.formatCellValue(row.getCell(27)).trim();
							if(!StringUtils.isEmpty(val)) { la.setRemarks(val);}
							
							val = formatter.formatCellValue(row.getCell(28)).trim();
							if(!StringUtils.isEmpty(val)) { la.setIssues(val);}
					
							la.setProposal_submission_date_to_collector(DateParser.parse(la.getProposal_submission_date_to_collector()));
							la.setJm_fee_letter_received_date(DateParser.parse(la.getJm_fee_letter_received_date()));
							la.setJm_fee_paid_date(DateParser.parse(la.getJm_fee_paid_date()));
							la.setJm_start_date(DateParser.parse(la.getJm_start_date()));
							la.setJm_completion_date(DateParser.parse(la.getJm_completion_date()));
							la.setJm_sheet_date_to_sdo(DateParser.parse(la.getJm_sheet_date_to_sdo()));
						}





						boolean flag = la.checkNullOrEmpty();

						//if(!flag && !StringUtils.isEmpty(la.getLa_id())) {
							lasList.add(la);
						//}

					}
					if(!lasList.isEmpty() && lasList != null){
						//count  = laService.uploadLAs(lasList);
						String[] arr  = landAquisitionService.uploadLAData(lasList,la);
						result[0] = arr[0];
						result[1] = arr[1];
						result[2] = arr[2];
						result[3] = arr[3];
						result[4] = arr[4];						
					}
				}
				workbook.close();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uploadLAs() : "+e.getMessage());
			throw new Exception(e);	
		}finally{
		    try{
		        if ( w != null)
		        	w.close( );
		    }catch ( IOException e){
		    	e.printStackTrace();
		    	logger.error("uploadLAs() : "+e.getMessage());
		    	throw new Exception(e);
		    }
		}
		
		return result;
	}
	private String getCellDataType(XSSFWorkbook workbook, XSSFCell cell) {
		String val = null;
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator(); 

		// existing Sheet, Row, and Cell setup
		//workbook.setForceFormulaRecalculation(true);
		try {
			CellType type = cell.getCellType();
			if (!StringUtils.isEmpty(cell)) {
			    switch (type) {
			        case BOOLEAN:
			            val = String.valueOf(cell.getBooleanCellValue());
			            break;
			        case NUMERIC:
			        	val = String.valueOf(cell.getNumericCellValue());
			        	if(val.contains("E")){
			        		val = BigDecimal.valueOf(Double.parseDouble(val)).toPlainString();
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
}
