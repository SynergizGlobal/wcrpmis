package com.wcr.wcrbackend.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.DTO.LandAquisationPaginationObject;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.ILandAquisitionService;
import com.wcr.wcrbackend.service.IUserService;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.WorkbookUtil;
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
	
	Logger logger = Logger.getLogger(LandAquisitionController.class);
	@PostMapping("/ajax/get-land-acquisition")
	public ResponseEntity<?> getLandAquisition(@RequestBody LandAcquisition obj, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		String json2 = null;
		String userId = null;
		String userName = null;
		try {
			userId = (String) session.getAttribute("USER_ID");
			userName = (String) session.getAttribute("USER_NAME");

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

			List<LandAcquisition> dataList = new ArrayList<LandAcquisition>();

			//Here is server side pagination logic. Based on the page number you could make call 
			//to the data base create new list and send back to the client. For demo I am shuffling 
			//the same list to show data randomly
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

			//Search functionality: Returns filtered list based on search parameter
			//lasList = getListBasedOnSearchParameter(searchParameter,lasList);

			int totalRecords = getTotalRecords(obj, searchParameter, session);

			LandAquisationPaginationObject personJsonObject = new LandAquisationPaginationObject();
			//Set Total display record
			personJsonObject.setiTotalDisplayRecords(totalRecords);
			//Set Total record
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
	public int getTotalRecords(LandAcquisition obj, String searchParameter,HttpSession session) {
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
	public List<LandAcquisition> createPaginationData(int startIndex, int offset, LandAcquisition obj, String searchParameter,HttpSession session) {
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
	public List<LandAcquisition> getProjectsList(@RequestBody LandAcquisition obj,HttpSession session) {
		List<LandAcquisition> projectsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			projectsList = landAquisitionService.getLandAcquisitionStatusList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getProjectsList : " + e.getMessage());
		}
		return projectsList;
	}
	
	@PostMapping("/ajax/getVillagesFilterListInLandAcquisition")
	public List<LandAcquisition> getVillagesList(@RequestBody LandAcquisition obj,HttpSession session) {
		List<LandAcquisition> villagesList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			villagesList = landAquisitionService.getLandAcquisitionVillagesList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getVillagesList : " + e.getMessage());
		}
		return villagesList;
	}
	@PostMapping("/ajax/getTypesOfLandsFilterListInLandAcquisition")
	public List<LandAcquisition> getTypesOfLandsList(@RequestBody LandAcquisition obj,HttpSession session) {
		List<LandAcquisition> typesOfLandsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			typesOfLandsList = landAquisitionService.getLandAcquisitionTypesOfLandsList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getTypesOfLandsList : " + e.getMessage());
		}
		return typesOfLandsList;
	}
	
	@PostMapping("/ajax/getSubCategoryFilterListInLandAcquisition")
	public List<LandAcquisition> getSubCategorysList(@RequestBody LandAcquisition obj,HttpSession session) {
		List<LandAcquisition> subCategoryList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			subCategoryList = landAquisitionService.getLandAcquisitionSubCategoryList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getSubCategorysList : " + e.getMessage());
		}
		return subCategoryList;
	}
	@PostMapping("/ajax/getCoordinates")
	public List<LandAcquisition> getCoordinates(@RequestBody LandAcquisition obj,HttpSession session) {
		List<LandAcquisition> objsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			objsList = landAquisitionService.getCoordinates(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getCoordinates : " + e.getMessage());
		}
		return objsList;
	}
	@PostMapping("/ajax/getSubCategorysList")
	public List<LandAcquisition> getSubCategoryList(@RequestBody LandAcquisition obj){
		List<LandAcquisition> objList = null;
		try{
			objList = landAquisitionService.getSubCategoryList(obj);			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getSubCategoryList() : "+e.getMessage());
		}
		return objList;
	}
	
	@PostMapping("/ajax/getLandsList")
	public List<LandAcquisition> getLandsList(@RequestBody LandAcquisition obj){
		List<LandAcquisition> objList = null;
		try{
			objList = landAquisitionService.getLandsList(obj);			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getLandsList() : "+e.getMessage());
		}
		return objList;
	}
	@PostMapping("/ajax/checkSurveyNumber")
	public boolean checkSurveyNumber(String survey_number,String village_id,String la_id) throws Exception {
		boolean flag = false;
		try {
			flag = landAquisitionService.checkSurveyNumber(survey_number,village_id,la_id);
		} catch (Exception e) {
			logger.error("checkSurveyNumber : " + e.getMessage());
		}
		return flag;
	}
	
	@PostMapping("/ajax/getLADetails")
	public List<LandAcquisition> getLADetails(@RequestBody LandAcquisition obj,HttpSession session) {
		List<LandAcquisition> LADetails = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			LADetails = landAquisitionService.getLADetails(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getLADetails : " + e.getMessage());
		}
		return LADetails;
	}
	@GetMapping("form/ajax/getStatusList")
	public List<LandAcquisition> getStatusList() throws Exception{
		return landAquisitionService.getStatusList();
	}
	
	@PostMapping("form/ajax/getProjectsList")
	public List<LandAcquisition> getProjectsList0(@RequestBody LandAcquisition obj,HttpSession session) throws Exception{
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		return landAquisitionService.getProjectsList(obj);
	}
	@PostMapping("form/ajax/getLandsListForLAForm")
	public List<LandAcquisition> getLandsListForLAForm(@RequestBody LandAcquisition obj,HttpSession session) throws Exception{
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		return landAquisitionService.getLandsListForLAForm(obj);
	}
	@GetMapping("form/ajax/getIssueCatogoriesList")
	public List<LandAcquisition> getIssueCatogoriesList() throws Exception{
		return landAquisitionService.getIssueCatogoriesList();
	}
	
	@PostMapping("form/ajax/getSubCategorysListForLAForm")
	public List<LandAcquisition> getSubCategorysListForLAForm(@RequestBody LandAcquisition obj,HttpSession session) throws Exception{
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		return landAquisitionService.getSubCategorysListForLAForm(obj);
	}
	@GetMapping("form/ajax/getUnitsList")
	public List<LandAcquisition> getUnitsList() throws Exception{
		return landAquisitionService.getUnitsList();
	}
	@GetMapping("form/ajax/getLaFileType")
	public List<LandAcquisition> getLaFileType() throws Exception{
		return landAquisitionService.getLaFileType();
	}
	@GetMapping("form/ajax/getLaLandStatus")
	public List<LandAcquisition> getLaLandStatus() throws Exception{
		return landAquisitionService.getLaLandStatus();
	}
	@PostMapping("form/ajax/getLandAcquisitionForm")
	public LandAcquisition getLandAcquisitionForm(@RequestBody LandAcquisition obj,HttpSession session) throws Exception{
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		return landAquisitionService.getLandAcquisitionForm(obj);
	}
	
	@PostMapping(value = "/export-land-acquisition")
	public void exportLandAcquisition(HttpServletRequest request, HttpServletResponse response,HttpSession session,@RequestBody LandAcquisition dObj){
		//ModelAndView view = new ModelAndView(PageConstants.landAcquisition);
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
			
			//view.setViewName("redirect:/land-acquisition");
			dataList =   landAquisitionService.getLandAcquisitionList(dObj);
		   
			if(dataList != null && dataList.size() > 0){
	            XSSFWorkbook  workBook = new XSSFWorkbook ();
	            XSSFSheet Landsheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Land Acquisition"));
				XSSFSheet privateIRASheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Private (Indian Railway Act)"));
				XSSFSheet privateValSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Private Land valuation"));
				XSSFSheet privateLandSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Private Land Acquisition"));
				XSSFSheet govSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Government Land Acquisition"));
				XSSFSheet forestSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Forest Land Acquisition"));
				XSSFSheet railwaySheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Railway Land Aquisition"));
				
		        workBook.setSheetOrder(Landsheet.getSheetName(), 0);
				workBook.setSheetOrder(privateIRASheet.getSheetName(), 1);
				workBook.setSheetOrder(privateValSheet.getSheetName(), 2);
				workBook.setSheetOrder(privateLandSheet.getSheetName(), 3);
				workBook.setSheetOrder(govSheet.getSheetName(), 4);
				workBook.setSheetOrder(forestSheet.getSheetName(), 5);
				workBook.setSheetOrder(railwaySheet.getSheetName(), 6);
		        
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
					/*	if(privateIRAList.size()< 1) {
							 int a = 0;
							XSSFRow row = privateIRASheet.createRow(rowNo3);
							for(int k = 0;k < secondHeaderStringArr.length;k++) {
								Cell cell2 = row.createCell(a++);
								cell2.setCellStyle(whiteStyle);
								cell2.setCellValue("No Data");
							}
							privateIRASheet.addMergedRegion(new CellRangeAddress(1,1,0,(secondHeaderStringArr.length - 1 )));
							break;
						}*/
				
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
					/*		if(privateValList.size()< 1) {
					int a = 0;
					XSSFRow row = privateValSheet.createRow(rowNo2);
								for(int k = 0;k < thirdHeaderStringArr.length;k++) {
									Cell cell2 = row.createCell(a++);
									cell2.setCellStyle(whiteStyle);
									cell2.setCellValue("No Data");
								}
								privateValSheet.addMergedRegion(new CellRangeAddress(1,1,0,(thirdHeaderStringArr.length - 1 )));
								break;
							}*/
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
					/*		if(privateLandList.size()< 1) {
					int a = 0;
					XSSFRow row = privateLandSheet.createRow(rowNo4);
								for(int k = 0;k < fourthHeaderStringArr.length;k++) {
									Cell cell2 = row.createCell(a++);
									cell2.setCellStyle(whiteStyle);
									cell2.setCellValue("No Data");
								}
								privateLandSheet.addMergedRegion(new CellRangeAddress(1,1,0,(fourthHeaderStringArr.length - 1 )));
								break;
							}*/
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
					/*		if(govList.size()< 1) {
					int a = 0;
					XSSFRow row = govSheet.createRow(rowNo5);
								for(int k = 0;k < fifthHeaderStringArr.length;k++) {
									Cell cell2 = row.createCell(a++);
									cell2.setCellStyle(whiteStyle);
									cell2.setCellValue("No Data");
								}
								govSheet.addMergedRegion(new CellRangeAddress(1,1,0,(fifthHeaderStringArr.length - 1 )));
								break;
							}*/
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
					/*		if(forestList.size()< 1) {
					int a = 0;
					XSSFRow row = forestSheet.createRow(rowNo6);
								for(int k = 0;k < sixthHeaderStringArr.length;k++) {
									Cell cell2 = row.createCell(a++);
									cell2.setCellStyle(whiteStyle);
									cell2.setCellValue("No Data");
								}
								forestSheet.addMergedRegion(new CellRangeAddress(1,1,0,(sixthHeaderStringArr.length - 1 )));
								break;
							}*/
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
					/*		if(railwayList.size()< 1) {
					int a = 0;
					XSSFRow row = railwaySheet.createRow(rowNo7);
								for(int k = 0;k < seventhHeaderStringArr.length;k++) {
									Cell cell2 = row.createCell(a++);
									cell2.setCellStyle(whiteStyle);
									cell2.setCellValue("No Data");
								}
								railwaySheet.addMergedRegion(new CellRangeAddress(1,1,0,(seventhHeaderStringArr.length - 1 )));
								break;
							}*/
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
					/*	   if(dataList.size()< 1) {
							    int a = 0;
							    XSSFRow row = Landsheet.createRow(rowNo);
								for(int k = 0;k < firstHeaderStringArr.length;k++) {
									Cell cell2 = row.createCell(a++);
									cell2.setCellStyle(whiteStyle);
									cell2.setCellValue("No Data");
								}
								Landsheet.addMergedRegion(new CellRangeAddress(1,1,0,(firstHeaderStringArr.length - 1 )));
								break;
							}*/
	                XSSFRow row = Landsheet.createRow(rowNo);
	                int c = 0;
	               
	                Cell cell = row.createCell(c++);
	                cell.setCellStyle(sectionStyle);
	                cell.setCellValue(obj.getWork_id_fk() + " - "+obj.getWork_short_name());
					
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
				
	        	for(int columnIndex = 0; columnIndex < 29; columnIndex++) {
	        		Landsheet.setColumnWidth(columnIndex, 25 * 200);
				}
	        	for(int columnIndex = 0; columnIndex < 38; columnIndex++) {
	        		privateIRASheet.setColumnWidth(columnIndex, 25 * 200);
				}
	        	for(int columnIndex = 0; columnIndex <16; columnIndex++) {
	        		privateValSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	        	for(int columnIndex = 0; columnIndex < 21; columnIndex++) {
	        		privateLandSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	        	for(int columnIndex = 0; columnIndex < 21; columnIndex++) {
	        		govSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	        	for(int columnIndex = 0; columnIndex < 16; columnIndex++) {
	        		forestSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	        	for(int columnIndex = 0; columnIndex < 16; columnIndex++) {
	        		railwaySheet.setColumnWidth(columnIndex, 25 * 200);
				}
	        
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
	            Date date = new Date();
	            String fileName = "Land_Aquisition"+dateFormat.format(date);
	            
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
	            	
	                
	                //attributes.addFlashAttribute("success",dataExportSucess);
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
	                //attributes.addFlashAttribute("error",dataExportInvalid);
	            }catch(IOException e){
	                //e.printStackTrace();
	                //attributes.addFlashAttribute("error",dataExportError);
	            }
			}else{
				//attributes.addFlashAttribute("error",dataExportNoData);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
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
		
		org.apache.poi.ss.usermodel.Font font = workBook.createFont();
        //font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setFontHeightInPoints((short)fontSize);  
        font.setFontName(fontName);  //"Times New Roman"
        
        font.setItalic(isItalicText); 
        font.setBold(isBoldText);
        // Applying font to the style  
        style.setFont(font); 
        
        return style;
	}
	
	private CellStyle cellFormatingColor(XSSFWorkbook workBook,byte[] rgb,HorizontalAlignment hAllign, VerticalAlignment vAllign, boolean isWrapText,boolean isBoldText,boolean isItalicText,int fontSize,String fontName) {
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
		
		org.apache.poi.ss.usermodel.Font font = workBook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        font.setFontHeightInPoints((short)fontSize);  
        font.setFontName(fontName);  //"Times New Roman"
        
        font.setItalic(isItalicText); 
        font.setBold(isBoldText);
        // Applying font to the style  
        style.setFont(font); 
        
        return style;
	}
}
