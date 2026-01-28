package com.wcr.wcrbackend.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.wcr.wcrbackend.DTO.FormHistory;
import com.wcr.wcrbackend.DTO.StripChart;
import com.wcr.wcrbackend.constants.PageConstants;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.FormsHistoryDao;
import com.wcr.wcrbackend.service.NewActivitiesUpdateService;
import com.wcr.wcrbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class NewActivitiesUpdateController {


	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(NewActivitiesUpdateController.class);
	
	@Autowired
	FormsHistoryDao formsHistoryDao;
	
	@Value("${common.error.message}")
	public String commonError;
	
	@Value("${template.upload.formatError}")
	public String uploadformatError;
	
	@Value("${record.dataexport.success}")
	public String dataExportSucess;	
	
	@Value("${record.dataexport.invalid.directory}")
	public String dataExportInvalid;
	
	@Value("${record.dataexport.error}")
	public String dataExportError;	
	
	@Autowired
	NewActivitiesUpdateService newActivitiesUpdateService;
	
	@Autowired
	UserService userService;


	@RequestMapping(value="/newActivitiesUpdate",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> newAcivitiesUpdate(@RequestBody  StripChart obj,HttpSession session) throws IOException {
//		ModelAndView model = new ModelAndView(PageConstants.newActivitiesUpdate);
		
		Map<String,Object> res = new HashMap<>();
		
		
		try {
			
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}
			
			List<StripChart> projectsList = newActivitiesUpdateService.getNewActivitiesUpdateProjectsList(obj);
			res.put("projectsList", projectsList);
			

			List<StripChart> contractsList = newActivitiesUpdateService.getNewActivitiesUpdateContractsList(obj);
			res.put("contractsList", contractsList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AcivitiesBulkUpload : " + e.getMessage());
		}
		return res;
	}
	
	
	
	@RequestMapping(value="/modify-actuals",method=RequestMethod.GET)
	public ModelAndView ModifyActuals(@ModelAttribute  StripChart obj,HttpSession session) throws IOException {
		ModelAndView model = new ModelAndView(PageConstants.modifyActuals);
		try {
			
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}
			
			List<StripChart> projectsList = newActivitiesUpdateService.getNewActivitiesUpdateProjectsList(obj);
			model.addObject("projectsList", projectsList);
			
			List<StripChart> contractsList = newActivitiesUpdateService.getNewActivitiesUpdateContractsList(obj);
			model.addObject("contractsList", contractsList);
			
			List<StripChart> taskCodesList = newActivitiesUpdateService.getTaskCodesList(obj);
			model.addObject("taskCodesList", taskCodesList);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AcivitiesBulkUpload : " + e.getMessage());
		}
		return model;
	}	
	
	@RequestMapping(value="/new-activities-update/{activity_id}",method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView newAcivitiesBulkUpdate(@PathVariable("activity_id")String activity_id,@ModelAttribute  StripChart obj,HttpSession session) throws IOException {
		ModelAndView model = new ModelAndView(PageConstants.newActivitiesUpdate);
		try {
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}
			
			List<StripChart> projectsList = newActivitiesUpdateService.getNewActivitiesUpdateProjectsList(obj);
			model.addObject("projectsList", projectsList);
			
			/*List<StripChart> worksList = newActivitiesUpdateService.getNewActivitiesUpdateWorksList(obj);
			model.addObject("worksList", worksList);
			
			List<StripChart> contractsList = newActivitiesUpdateService.getNewActivitiesUpdateContractsList(obj);
			model.addObject("contractsList", contractsList);*/
			
			obj.setActivity_id(activity_id);
			StripChart activitiesData = newActivitiesUpdateService.getNewAcivitiesUpdateData(obj);
			model.addObject("activitiesData", activitiesData);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AcivitiesBulkUpload : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateProjectsList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateProjectsList(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> projects = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}		
			projects = newActivitiesUpdateService.getNewActivitiesUpdateProjectsList(obj);			
		}catch(Exception e){
			logger.error("getNewActivitiesUpdateProjectsList() : "+e.getMessage());
		}
		return projects;
	}
	
	@RequestMapping(value = "/ajax/getLatestRowData", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getLatestRowData(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> projects = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}		
			projects = newActivitiesUpdateService.getLatestRowData(obj);			
		}catch(Exception e){
			logger.error("getLatestRowData() : "+e.getMessage());
		}
		return projects;
	}
	
	@RequestMapping(value = "/ajax/getLastUpdateRows", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getLastUpdateRows(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> projects = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}			
			projects = newActivitiesUpdateService.getLastUpdateRows(obj);			
		}catch(Exception e){
			logger.error("getLastUpdateRows() : "+e.getMessage());
		}
		return projects;
	}	
	
	
	
	@RequestMapping(value = "/ajax/bindData", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> bindData(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> projects = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}		
			projects = newActivitiesUpdateService.bindData(obj);			
		}catch(Exception e){
			logger.error("bindData() : "+e.getMessage());
		}
		return projects;
	}	

	@RequestMapping(value = "/ajax/getNewActivitiesUpdateWorksList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateWorksList(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> works = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}
			works = newActivitiesUpdateService.getNewActivitiesUpdateWorksList(obj);			
		}catch(Exception e){
			logger.error("getNewActivitiesUpdateWorksList() : "+e.getMessage());
		}
		return works;
	}
	
	@RequestMapping(value = "/ajax/getDeleteActivitiesContractsList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getDeleteActivitiesUpdateContractsList(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> works = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}			
			works = newActivitiesUpdateService.getContractsList(obj);			
		}catch(Exception e){
			logger.error("getDeleteActivitiesUpdateContractsList() : "+e.getMessage());
		}
		return works;
	}
	
	
	
	@RequestMapping(value = "/exportActivitiesbyContract", method = { RequestMethod.GET, RequestMethod.POST })
	public void exportActivitiesbyContract(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @RequestBody StripChart obj, RedirectAttributes attributes) {
//		ModelAndView model = new ModelAndView();

		Map<String, Object> res = new HashMap<>();
		try {
//			model.setViewName("redirect:/new-activities-update");

			User uObj = (User) session.getAttribute("user");
			if (!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}

			ResultSet rs = newActivitiesUpdateService.getExportActivitiesbyContract(obj);

			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet executionOverviewReportSheet = workBook
					.createSheet(WorkbookUtil.createSafeSheetName("Activities"));

			workBook.setSheetOrder(executionOverviewReportSheet.getSheetName(), 0);

			byte[] blueRGB = new byte[] { (byte) 0, (byte) 176, (byte) 240 };
			byte[] yellowRGB = new byte[] { (byte) 255, (byte) 255, (byte) 0 };
			byte[] greenRGB = new byte[] { (byte) 146, (byte) 208, (byte) 80 };
			byte[] redRGB = new byte[] { (byte) 255, (byte) 0, (byte) 0 };
			byte[] whiteRGB = new byte[] { (byte) 255, (byte) 255, (byte) 255 };
			byte[] grayRGB = new byte[] { (byte) 217, (byte) 217, (byte) 217 };

			boolean isWrapText = true;
			boolean isBoldText = false;
			boolean isItalicText = false;
			int fontSize = 11;
			String fontName = "Times New Roman";
			CellStyle blueStyle = cellFormating(workBook, blueRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
					isWrapText, isBoldText, isItalicText, fontSize, fontName);
			CellStyle yellowStyle = cellFormating(workBook, yellowRGB, HorizontalAlignment.CENTER,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
			CellStyle greenStyle = cellFormating(workBook, greenRGB, HorizontalAlignment.CENTER,
					VerticalAlignment.CENTER, isWrapText, true, isItalicText, 12, fontName);
			CellStyle redStyle = cellFormating(workBook, redRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
					isWrapText, isBoldText, isItalicText, fontSize, fontName);
			CellStyle grayStyle = cellFormating(workBook, grayRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
					isWrapText, isBoldText, isItalicText, fontSize, fontName);
			CellStyle indexWhiteStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

			isBoldText = true;
			fontSize = 12;
			CellStyle whiteStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

			isWrapText = true;
			isBoldText = false;
			isItalicText = false;
			fontSize = 9;
			fontName = "Times New Roman";
			CellStyle sectionStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
			CellStyle sectionPercentStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
			CellStyle sectioncostStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.RIGHT,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
			CellStyle sectionunitsStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

			CellStyle centerStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
			CellStyle rightStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.RIGHT,
					VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);

			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			XSSFRow row1 = executionOverviewReportSheet.createRow(0);
			for (int col = 0; col < columnsNumber; col++) {
				Cell newpath = row1.createCell(col);
				newpath.setCellValue(rsmd.getColumnLabel(col + 1));
			}
			NumberFormat numberFormatter = new DecimalFormat("#0.00");
			while (rs.next()) {
				XSSFRow row = executionOverviewReportSheet.createRow(rs.getRow());
				for (int col = 0; col < columnsNumber; col++) {
					Cell newpath = row.createCell(col);
					if (col > 9) {
						String Str = rs.getString(col + 1);
						newpath.setCellValue(Float.parseFloat(Str));
					} else {
						newpath.setCellValue(rs.getString(col + 1));
					}

				}
			}
			/*******************************************************************************************/

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
			Date date = new Date();
			String fileName = "Export_New_Activities" + dateFormat.format(date);

			try {

				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

				workBook.write(response.getOutputStream()); // Write workbook to response.
				workBook.close();
				response.getOutputStream().flush();

//	                attributes.addFlashAttribute("success",dataExportSucess);
				res.put("error", dataExportError);

			} catch (FileNotFoundException e) {
//	                attributes.addFlashAttribute("error",dataExportInvalid);
				res.put("error", dataExportInvalid);

			} catch (IOException e) {
//	                attributes.addFlashAttribute("error",dataExportError);
				res.put("error", dataExportError);

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("exportExecutionOverviewReport : " + e.getMessage());
//			attributes.addFlashAttribute("error", commonError);	
			res.put("error", commonError);

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
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateContractsList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateContractsList(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> contracts = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}	
			
			contracts = newActivitiesUpdateService.getNewActivitiesUpdateContractsList(obj);			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNewActivitiesUpdateContractsList() : "+e.getMessage());
		}
		return contracts;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateStructures", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateStructures(@RequestBody StripChart obj,HttpSession session){
		List<StripChart> structures = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}				
			structures = newActivitiesUpdateService.getNewActivitiesUpdateStructures(obj);			
		}catch(Exception e){
			logger.error("getNewActivitiesUpdateStructures() : "+e.getMessage());
		}
		return structures;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateTaskCodes", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateTaskCodes(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> structures = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}				
			structures = newActivitiesUpdateService.getTaskCodesList(obj);	
		}catch(Exception e){
			logger.error("getNewActivitiesUpdateTaskCodes() : "+e.getMessage());
		}
		return structures;
	}
	
	@RequestMapping(value = "/ajax/getDeleteActivitiesStructures", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getDeleteActivitiesStructures(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> structures = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}				
			structures = newActivitiesUpdateService.getDeleteActivitiesStructures(obj);			
		}catch(Exception e){
			logger.error("getNewActivitiesUpdateStructures() : "+e.getMessage());
		}
		return structures;
	}	
	

	@RequestMapping(value = "/ajax/getContractStructures", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getContractStructures(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> structures = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}			
			structures = newActivitiesUpdateService.getContractStructures(obj);			
		}catch(Exception e){
			logger.error("getContractStructures() : "+e.getMessage());
		}
		return structures;
	}
	
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateInProgressStructures", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateInProgressStructures(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> structures = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}					
			structures = newActivitiesUpdateService.getNewActivitiesUpdateInProgressStructures(obj);			
		}catch(Exception e){
			logger.error("getNewActivitiesUpdateStructures() : "+e.getMessage());
		}
		return structures;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateLines", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateLines(@ModelAttribute StripChart obj){
		List<StripChart> structures = null;
		try{
			structures = newActivitiesUpdateService.getNewActivitiesUpdateLines(obj);			
		}catch(Exception e){
			logger.error("getNewActivitiesUpdateLines() : "+e.getMessage());
		}
		return structures;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateSections", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateSections(@ModelAttribute StripChart obj){
		List<StripChart> structures = null;
		try{
			structures = newActivitiesUpdateService.getNewActivitiesUpdateSections(obj);			
		}catch(Exception e){
			logger.error("getNewActivitiesUpdateSections() : "+e.getMessage());
		}
		return structures;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateComponentsList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateComponentsList(@RequestBody StripChart obj){
		List<StripChart> components = null;
		try{
			components = newActivitiesUpdateService.getNewActivitiesUpdateComponentsList(obj);			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNewActivitiesUpdateComponentsList() : "+e.getMessage());
		}
		return components;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateComponentIdsList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesUpdateComponentIds(@RequestBody StripChart obj){
		List<StripChart> componentIds = null;
		try{
			componentIds = newActivitiesUpdateService.getNewActivitiesUpdateComponentIds(obj);			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNewActivitiesUpdateComponentIds() : "+e.getMessage());
		}
		return componentIds;
	}
	
	@RequestMapping(value = "/ajax/getDeleteActivitiesComponentsList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getDeleteActivitiesComponentsList(@ModelAttribute StripChart obj){
		List<StripChart> components = null;
		try{
			components = newActivitiesUpdateService.getDeleteActivitiesComponentsList(obj);			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNewActivitiesUpdateComponentsList() : "+e.getMessage());
		}
		return components;
	}
	
	@RequestMapping(value = "/ajax/getDeleteActivitiesComponentIdsList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getDeleteActivitiesComponentIdsList(@ModelAttribute StripChart obj){
		List<StripChart> componentIds = null;
		try{
			componentIds = newActivitiesUpdateService.getDeleteActivitiesComponentIds(obj);			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNewActivitiesUpdateComponentIds() : "+e.getMessage());
		}
		return componentIds;
	}	
	
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateActivitiesList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getAcivitiesBulkActivitiesList(@ModelAttribute StripChart obj){
		List<StripChart> activities = null;
		try{
			activities = newActivitiesUpdateService.getAcivitiesBulkActivitiesList(obj);	
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getAcivitiesBulkActivitiesList() : "+e.getMessage());
		}
		return activities;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesUpdateDetails", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StripChart getNewActivitiesUpdateDetails(@ModelAttribute StripChart obj){
		StripChart data = null;
		try{
			data = newActivitiesUpdateService.getNewActivitiesUpdateDetails(obj);	
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNewActivitiesUpdateDetails() : "+e.getMessage());
		}
		return data;
	}
	
	@RequestMapping(value = "/ajax/getNewActivitiesfiltersList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getNewActivitiesfiltersList(@RequestBody StripChart obj,HttpSession session){
		List<StripChart> fileterData = null;
		try{
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}			
			fileterData = newActivitiesUpdateService.getNewActivitiesfiltersList(obj);	
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNewActivitiesfiltersList() : "+e.getMessage());
		}
		return fileterData;
	}
	
	
	@RequestMapping(value = "/ajax/getStructureTypesInActivitiesUpdate", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getStructureTypesInActivitiesUpdate(@RequestBody StripChart obj) {
		List<StripChart> objList = null;
		try {
			objList = newActivitiesUpdateService.getStructureTypesInActivitiesUpdate(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureTypesInActivitiesUpload : " + e.getMessage());
		}
		return objList;
	}	
	
	@RequestMapping(value = "/ajax/getStructureTypesInDeleteActivities", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getStructureTypesInDeleteActivities(@ModelAttribute StripChart obj) {
		List<StripChart> objList = null;
		try {
			objList = newActivitiesUpdateService.getStructureTypesInDeleteActivities(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureTypesInActivitiesUpload : " + e.getMessage());
		}
		return objList;
	}
	
	@RequestMapping(value = "/update-modify-actuals-bulk", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ModelAndView updateModifyActualsBulk(@ModelAttribute StripChart obj,RedirectAttributes attributes,HttpSession session){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/modify-actuals");
			
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
				obj.setCreated_by_user_id_fk(uObj.getUserId());
				
				
				obj.setUser_name(uObj.getUserName());
				obj.setDesignation(uObj.getDesignation());
				

			}	


			boolean flag =  newActivitiesUpdateService.updateModifyActualsBulk(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Updated Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Updating Acivities are failed. Try again.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Updating Acivities are failed. Try again.");
			logger.error("updateModifyActualsBulk : " + e.getMessage());
		}
		return model;
	}	
	
	
	

	@RequestMapping(value = "/update-new-activities-bulk", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> updateNewAcivitiesBulk(@RequestBody StripChart obj, RedirectAttributes attributes,
			HttpSession session) {
//		ModelAndView model = new ModelAndView();
		Map<String, Object> res = new HashMap<>();

		try {
//			model.setViewName("redirect:/new-activities-update");

			User uObj = (User) session.getAttribute("user");
			if (!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
				obj.setCreated_by_user_id_fk(uObj.getUserId());

				obj.setUser_name(uObj.getUserName());
				obj.setDesignation(uObj.getDesignation());

			}
			// obj.setProgress_date(DateParser.parse(obj.getProgress_date()));
			boolean flag = newActivitiesUpdateService.updateNewAcivitiesBulk(obj);
			if (flag) {
//				attributes.addFlashAttribute("success", "Acivities Updated Succesfully.");
				res.put("success", "Acivities Updated Succesfully.");

			} else {
//				attributes.addFlashAttribute("error","Updating Acivities are failed. Try again.");
				res.put("error", "Updating Acivities are failed. Try again.");

			}
		} catch (Exception e) {
//			attributes.addFlashAttribute("error","Updating Acivities are failed. Try again.");
			res.put("error", "Updating Acivities are failed. Try again.");

			logger.error("updateAcivitiesBulk : " + e.getMessage());
		}
		return res;
	}
	
	
	@RequestMapping(value = "/delete-activities-bulk", method = {RequestMethod.POST})
	public ModelAndView deleteActivitiesBulk(@ModelAttribute StripChart obj,RedirectAttributes attributes,HttpSession session){
		ModelAndView model = new ModelAndView();
		try{
			model.setViewName("redirect:/delete-activities");
			
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
				obj.setCreated_by_user_id_fk(uObj.getUserId());
				
				
				obj.setUser_name(uObj.getUserName());
				obj.setDesignation(uObj.getDesignation());
				
			}				
			//obj.setProgress_date(DateParser.parse(obj.getProgress_date()));
			boolean flag =  newActivitiesUpdateService.deleteAcivitiesBulk(obj);
			if(flag) {
				attributes.addFlashAttribute("success", "Acivities Deleted Succesfully.");
			}
			else {
				attributes.addFlashAttribute("error","Deleting Acivities failed.");
			}
		}catch (Exception e) {
			attributes.addFlashAttribute("error","Deleting Acivities failed.");
			logger.error("deleteActivitiesBulk : " + e.getMessage());
		}
		return model;
	}	
	
	@RequestMapping(value="/delete-activities",method=RequestMethod.GET)
	public ModelAndView DeleteActivities(@ModelAttribute  StripChart obj,HttpSession session) throws IOException {
		ModelAndView model = new ModelAndView(PageConstants.deleteActivities);
		try {
			
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}
			
			
			List<StripChart> projectsList = newActivitiesUpdateService.getProjectsList(obj);
			model.addObject("projectsList", projectsList);
			
			List<StripChart> contractsList = newActivitiesUpdateService.getContractsList(obj);
			model.addObject("contractsList", contractsList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DeleteActivities : " + e.getMessage());
		}
		return model;
	}
	
	
//	@RequestMapping(value = "/upload-new-activities", method = {RequestMethod.POST})
//	public ModelAndView uploadRisk(@ModelAttribute StripChart obj,RedirectAttributes attributes,HttpSession session){
//		ModelAndView model = new ModelAndView();
//		try {
//			String user_Id = (String) session.getAttribute("USER_ID");
//			String userName = (String) session.getAttribute("USER_NAME");
//			String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
//			
//			obj.setCreated_by_user_id_fk(user_Id);
//			obj.setUser_id(user_Id);
//			obj.setUser_name(userName);
//			obj.setDesignation(userDesignation);
//			model.setViewName("redirect:/new-activities-update");
//			
//			if(!StringUtils.isEmpty(obj.getStripChartFile())){
//				MultipartFile multipartFile = obj.getStripChartFile();
//				// Creates a workbook object from the uploaded excelfile
//				if (multipartFile.getSize() > 0){					
//					XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
//					// Creates a worksheet object representing the first sheet
//					int sheetsCount = workbook.getNumberOfSheets();
//					if(sheetsCount > 0) {
//						XSSFSheet risksDrawingsSheet = workbook.getSheetAt(0);
//						//System.out.println(uploadFilesSheet.getSheetName());
//						//header row
//						XSSFRow headerRow = risksDrawingsSheet.getRow(1);
//						//checking given file format
//						/*if(headerRow != null){
//							List<String> fileFormat = FileFormatModel.getStripChartFileFormat();	
//							int noOfColumns = headerRow.getLastCellNum();
//							if(9 == fileFormat.size()){
//								for (int i = 0; i < fileFormat.size();i++) {
//				                	//System.out.println(headerRow.getCell(i).getStringCellValue().trim());
//				                	//if(!fileFormat.get(i).trim().equals(headerRow.getCell(i).getStringCellValue().trim())){
//									String columnName = headerRow.getCell(i).getStringCellValue().trim();
//									if(!columnName.equals(fileFormat.get(i).trim()) && !columnName.contains(fileFormat.get(i).trim())){
//				                		attributes.addFlashAttribute("error",uploadformatError);
//				                		return model;
//				                	}
//								}
//							}else{
//								attributes.addFlashAttribute("error",uploadformatError);
//		                		return model;
//							}
//						}else{
//							attributes.addFlashAttribute("error",uploadformatError);
//	                		return model;
//						}*/
//						
//						boolean flagValue = uploadNewActivities(obj,user_Id,userName);
//						if(flagValue ==true) {
//							attributes.addFlashAttribute("success", " Activities Inserted/Updated successfully.");
//							FormHistory formHistory = new FormHistory();
//							formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
//							formHistory.setUser(obj.getDesignation()+" - "+obj.getUser_name());
//							formHistory.setModule_name_fk("Execution");
//							formHistory.setForm_name("Upload Activities");
//							formHistory.setForm_action_type("Upload");
//							formHistory.setForm_details("Activities Inserted/Updated successfully.");
//							formHistory.setWork(obj.getWork_id_fk());
//							formHistory.setContract(obj.getContract_id_fk());
//							
//							boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
//							/********************************************************************************/
//							
//						}
//						
//					}
//					workbook.close();
//				}
//			} else {
//				attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
//			logger.fatal("updateDataDate() : "+e.getMessage());
//		}
//		return model;
//	}
	
	
	@RequestMapping(value = "/upload-new-activities", method = {RequestMethod.POST})
	public Map<String, Object> uploadRisk(@ModelAttribute StripChart obj, RedirectAttributes attributes, HttpSession session) {
//	    ModelAndView model = new ModelAndView();
		
		Map<String, Object> res = new HashMap<>();
	    try {
	        String user_Id = (String) session.getAttribute("USER_ID");
	        String userName = (String) session.getAttribute("USER_NAME");
	        String userDesignation = (String) session.getAttribute("USER_DESIGNATION");

	        obj.setCreated_by_user_id_fk(user_Id);
	        obj.setUser_id(user_Id);
	        obj.setUser_name(userName);
	        obj.setDesignation(userDesignation);
//	        model.setViewName("redirect:/new-activities-update");

	        if (!StringUtils.isEmpty(obj.getStripChartFile())) {
	            MultipartFile multipartFile = obj.getStripChartFile();
	            if (multipartFile.getSize() > 0) {
	                XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
	                int sheetsCount = workbook.getNumberOfSheets();
	                if (sheetsCount > 0) {
	                    XSSFSheet risksDrawingsSheet = workbook.getSheetAt(0);
	                    XSSFRow headerRow = risksDrawingsSheet.getRow(1);

	                    // Add your file format validation code here if needed

	                    // Add logic to check for modified cells
	                    List<String> modifiedCells = new ArrayList<>();
	                    for (int rowNum = 1; rowNum <= risksDrawingsSheet.getLastRowNum(); rowNum++) {
	                        XSSFRow row = risksDrawingsSheet.getRow(rowNum);
	                        if (row != null) {
	                            for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
	                                XSSFCell cell = row.getCell(cellNum);
	                                if (cell != null && cell.getCellType() == CellType.FORMULA) {
	                                    // Capture the address of the modified cell
	                                    modifiedCells.add(cell.getAddress().formatAsString());
	                                }
	                            }
	                        }
	                    }

	                    if (!modifiedCells.isEmpty()) {
	                        String errorMessage = " Mismatch found in the cell " + String.join(", ", modifiedCells);
//	                        attributes.addFlashAttribute("error", errorMessage);
	                        res.put("error", errorMessage);

	                        workbook.close();
	                        return res;
	                    }

	                    boolean flagValue = uploadNewActivities(obj, user_Id, userName);
	                    if (flagValue == true) {
//	                        attributes.addFlashAttribute("success", " Activities Inserted/Updated successfully.");
	                        res.put("success", " Activities Inserted/Updated successfully.");

	                        FormHistory formHistory = new FormHistory();
	                        formHistory.setCreated_by_user_id_fk(obj.getCreated_by_user_id_fk());
	                        formHistory.setUser(obj.getDesignation() + " - " + obj.getUser_name());
	                        formHistory.setModule_name_fk("Execution");
	                        formHistory.setForm_name("Upload Activities");
	                        formHistory.setForm_action_type("Upload");
	                        formHistory.setForm_details("Activities Inserted/Updated successfully.");
	                        formHistory.setWork(obj.getWork_id_fk());
	                        formHistory.setContract(obj.getContract_id_fk());

	                        boolean history_flag = formsHistoryDao.saveFormHistory(formHistory);
	                    }
	                }
	                workbook.close();
	            }
	        } else {
//	            attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
	            res.put("error", "Something went wrong. Please try after some time");

	        }

	    } catch (Exception e) {
	        e.printStackTrace();
//	        attributes.addFlashAttribute("error", "Something went wrong. Please try after some time");
	        res.put("error", "Something went wrong. Please try after some time");
	        logger.fatal("updateDataDate() : " + e.getMessage());
	    }
	    return res;
	}

	private boolean uploadNewActivities(StripChart obj, String userId,String userName) throws Exception {
		StripChart stripChart = null;
		List<StripChart> stripChartList = new ArrayList<StripChart>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatter3 = new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");		
		Writer w = null;
		boolean count = false ;
		try {	
			MultipartFile excelfile = obj.getStripChartFile();
			// Creates a workbook object from the uploaded excelfile
			if (!StringUtils.isEmpty(excelfile) && excelfile.getSize() > 0 ){
				
				XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
				int sheetsCount = workbook.getNumberOfSheets();
				if(sheetsCount > 0) {
					XSSFSheet risksDrawingsSheet = workbook.getSheetAt(0);
					//System.out.println(uploadFilesSheet.getSheetName());
					//header row
					//XSSFRow headerRow = uploadFilesSheet.getRow(0);							
					DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
					//System.out.println(uploadFilesSheet.getLastRowNum());
					for(int i = 1; i <= risksDrawingsSheet.getLastRowNum();i++){
						XSSFRow row = risksDrawingsSheet.getRow(i);
						// Sets the Read data to the model class
						// Cell cell = row.getCell(0);
						// String j_username = formatter.formatCellValue(row.getCell(0));
						//System.out.println(i);
						stripChart = new StripChart();
						String val = null;
						if(!StringUtils.isEmpty(row)) {	
							
							stripChart.setCreated_by_user_id_fk(userId);
						  
							val = formatter.formatCellValue(row.getCell(0)).trim();
							if(!StringUtils.isEmpty(val)) { stripChart.setP6_task_code(val);}
							
							val = formatter.formatCellValue(row.getCell(1)).trim();
							if(!StringUtils.isEmpty(val)) { stripChart.setContract_short_name(val);}							
							
							String ConcatProgressDates="";
							String ConcatCompletedScopes="";
							XSSFRow headerRow = risksDrawingsSheet.getRow(1);
							int noOfColumns = headerRow.getLastCellNum();

							for(int k=10;k<noOfColumns;k++)
							{
								
								val = formatter.formatCellValue(risksDrawingsSheet.getRow(0).getCell(k)).trim();
								
								if(!StringUtils.isEmpty(val)) { 
									if(val.contains("/")) 
									{
										Date date24 = null;
										String dateString24 = null;
										date24 = formatter3.parse(val);
										dateString24 = formatter2.format(date24);	
										ConcatProgressDates=ConcatProgressDates+""+dateString24+"###";

										 
									}
									else
									{
									
										Date date24 = null;
										String dateString24 = null;
										date24 = formatter1.parse(val);
										dateString24 = formatter2.format(date24);
										ConcatProgressDates=ConcatProgressDates+""+dateString24+"###";

										
									}
									
								}									

								
							}
							stripChart.setProgress_date(ConcatProgressDates);
							for(int k=10;k<noOfColumns;k++)
							{
								val = formatter.formatCellValue(risksDrawingsSheet.getRow(i).getCell(k)).trim();
								if(!StringUtils.isEmpty(val)) 
								{ 
									ConcatCompletedScopes=ConcatCompletedScopes+""+formatter.formatCellValue(risksDrawingsSheet.getRow(i).getCell(k)).trim()+"###";
								}
								else
								{
									ConcatCompletedScopes=ConcatCompletedScopes+"NoValue"+"###";
								}
							}
							stripChart.setCompleted(ConcatCompletedScopes);
						
						}						
						boolean flag = stripChart.checkNullOrEmpty();
						
						if(!flag) {
							stripChartList.add(stripChart);
						}
					}
					
					if(!stripChartList.isEmpty()){
						count  = newActivitiesUpdateService.uploadNewActivities(stripChartList);
					}
				}
				workbook.close();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uploadNewActivities() : "+e.getMessage());
			throw new Exception(e);	
		}finally{
		    try{
		        if ( w != null)
		        	w.close( );
		    }catch ( IOException e){
		    	e.printStackTrace();
		    	logger.error("uploadNewActivities() : "+e.getMessage());
		    	throw new Exception(e);
		    }
		}
		
		return count;
	}
	
	

	@RequestMapping(value = "/ajax/getDeleteActivitiesfiltersList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<StripChart> getDeleteActivitiesfiltersList(@ModelAttribute StripChart obj,HttpSession session){
		List<StripChart> fileterData = null;
		try{
		
			User uObj = (User) session.getAttribute("user");
			if(!StringUtils.isEmpty(uObj)) {
				obj.setUser_type_fk(uObj.getUserTypeFk());
				obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
				obj.setUser_id(uObj.getUserId());
				obj.setDepartment_fk(uObj.getDepartmentFk());
			}
			
			
			fileterData = newActivitiesUpdateService.getDeleteActivitiesfiltersList(obj);	
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getNewActivitiesfiltersList() : "+e.getMessage());
		}
		return fileterData;
	}	
	
}

