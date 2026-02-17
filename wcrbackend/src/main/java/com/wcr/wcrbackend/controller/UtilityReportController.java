package com.wcr.wcrbackend.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.UserService;
import com.wcr.wcrbackend.service.UtilityReportService;
import com.wcr.wcrbackend.service.UtilityShiftingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class UtilityReportController {
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	public static Logger logger = Logger.getLogger(UtilityReportController.class);

	@Autowired
	UtilityReportService service;
	
	@Autowired
	UtilityShiftingService utilityShiftingService;	

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
	
	@Autowired
	UserService userService;
	
	
	@RequestMapping(value = "/utility-report", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> utilityReport (HttpSession session) {
		UtilityShifting obj = new UtilityShifting();
		Map<String, Object> res = new HashMap<>();
		try {
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			
			
			List<UtilityShifting> projectsList = service.getProjectsFilterListInutilityReport(obj);
			res.put("projectsList", projectsList);


			List<UtilityShifting> executionAgency = service.getExecutionAgencyListInutilityReport(obj);
			res.put("executionAgency", executionAgency);
			
			
			List<UtilityShifting> impactedContractsList = utilityShiftingService.getImpactedContractsListForUtilityShifting(obj);
			res.put("impactedContractsList", impactedContractsList);

			List<UtilityShifting> utilityHODList = utilityShiftingService.getHodListForUtilityShifting(obj);
			res.put("utilityHODList", utilityHODList);	

			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("utilityReport : " + e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/ajax/getProjectListForUtilityShiftingReportForm", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<UtilityShifting> getProjectListForUtilityShiftingReportForm(@ModelAttribute UtilityShifting obj) {
		List<UtilityShifting> objList = null;
		try {
			objList = service.getProjectsFilterListInutilityReport(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getProjectListForUtilityShiftingReportForm : " + e.getMessage());
		}
		return objList;
	}

	@RequestMapping(value = "/ajax/getAgencyListForUtilityShiftingReportForm", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<UtilityShifting> getLocationsListForUtilityShiftingReportForm(@ModelAttribute UtilityShifting obj) {
		List<UtilityShifting> objList = null;
		try {
			objList = service.getExecutionAgencyListInutilityReport(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getLocationsListForUtilityShiftingReportForm : " + e.getMessage());
		}
		return objList;
	}
	
	@RequestMapping(value = "/generate-utility-report", method = {RequestMethod.GET,RequestMethod.POST})
	public void generateRandRReport(@ModelAttribute UtilityShifting obj,HttpServletRequest request, HttpServletResponse response,HttpSession session,RedirectAttributes attributes){
		//ModelAndView model = new ModelAndView("redirect:/activities-progress-report");
		try{
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			String report_created_date = df.format(new Date());
			
			SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-YY");
			
			Map<String,List<UtilityShifting>> reportData = service.getUtilityShiftingReportData(obj);
			
			XSSFWorkbook  workBook = new XSSFWorkbook();
			
			/***************************************************************************/
	        
			byte[] blueRGB = new byte[]{(byte)214, (byte)255, (byte)255};
			byte[] yellowRGB = new byte[]{(byte)255, (byte)255, (byte)153};
	        byte[] greenRGB = new byte[]{(byte)146, (byte)208, (byte)80};
	        byte[] redRGB = new byte[]{(byte)255, (byte)0, (byte)0};
	        byte[] whiteRGB = new byte[]{(byte)255, (byte)255, (byte)255};
	        byte[] greyRGB = new byte[]{(byte)211, (byte)211, (byte)211};
	        
	        
	        boolean isWrapText = true;boolean isBoldText = true;boolean isItalicText = false; int fontSize = 11;String fontName = "Calibri";
	        CellStyle greenStyle = cellFormating(workBook,greenRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle greenStyle1 = cellFormating(workBook,yellowRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle bluetyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle whiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        
	        CellStyle indexWhiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	       
	        CellStyle indexWhiteStyle1 = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle indexWhiteStyle2 = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        
	        isWrapText = true;isBoldText = false;isItalicText = false; fontSize = 11;fontName = "Calibri";
	        CellStyle numberStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle activityNameStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle activityNameStyle1 = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle activityNameStyle2 = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle indexShadedStyle = cellFormating(workBook,greyRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,true,isItalicText,11,fontName);


	        /********************************************************/
          
	        if(!(StringUtils.isEmpty(reportData))){					
				
			        int x = 0,z=0,sNo = 1;  String workId = null;
					String headerString2 = "Utility ID^work^Execution Agency^Impacted Contract Short Name^Type of Utility Shifting ^Description^Requirement stage^Impacted Element^Affected Structures^Custodian^HOD^Target Date^Executed By"
							+ "^Latest Progress Date^Latest Progress Update^Actual Completion Date^Remarks";
			        String[] headerStringArr2 = headerString2.split("\\^");	  
			        
		   		 	XSSFSheet rrSheet2 = workBook.createSheet(WorkbookUtil.createSafeSheetName("Utility Shifting - Detail Report"));
		   		 	
			        XSSFRow headRow2 = rrSheet2.createRow(0);
			        
			        Cell cell2 = headRow2.createCell(0);
			        
			        XSSFRow mainHeadingRow2 = rrSheet2.createRow(1);
			        
			        cell2 = mainHeadingRow2.createCell(0);
			        cell2.setCellStyle(bluetyle);
			        //cell2.setCellValue(work_d+" Utility Shifting Detail Report");
			        
			        cell2.setCellValue("Utility Shifting Detail Report");
			        
			        for (int i = 1; i < headerStringArr2.length; i++) {		        	
				        cell2 = mainHeadingRow2.createCell(i);
				        cell2.setCellStyle(bluetyle);
						cell2.setCellValue("");
					}	
			        rrSheet2.addMergedRegion(new CellRangeAddress(1, 1, 0,headerStringArr2.length-1));
			        int rowNo2 = 3;
				    // workBook.setSheetOrder(rrSheet1.getSheetName(), sheetNo++);
		
			        
			        
			        XSSFRow headingRow2 = rrSheet2.createRow(rowNo2);
			        for (int i = 0; i < headerStringArr2.length ; i++) {	
			        	
			    			 cell2 = headingRow2.createCell(i);
			    	    	 cell2.setCellStyle(bluetyle);
							 cell2.setCellValue(headerStringArr2[i]);
					}	
			        rowNo2++; int sno2 = 1;x = 0; workId = null;
			        for (Map.Entry<String,List<UtilityShifting>> entry : reportData.entrySet()) {
			         
		               int d = 0;
		               XSSFRow row1 = rrSheet2.createRow(rowNo2);
			      	   
		               String work_name = entry.getKey();
		               
			    	   Cell cell = row1.createCell(d++);
				       cell.setCellStyle(greenStyle1);
				       cell.setCellValue(work_name);
						
				       for (int i = 1; i < headerStringArr2.length; i++) {		        	
					        cell = row1.createCell(i);
					        cell.setCellStyle(greenStyle1);
							cell.setCellValue("");
				       }	
				       rrSheet2.addMergedRegion(new CellRangeAddress(rowNo2,rowNo2, 0,headerStringArr2.length-1));
				       rowNo2++;
							
				       for (UtilityShifting zObj : entry.getValue()) { 
				        d = 0;
				        row1 = rrSheet2.createRow(rowNo2);
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getUtility_shifting_id());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getWork_short_name());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getExecution_agency_fk());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getContract_short_name());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getUtility_type_fk());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getUtility_description());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getRequirement_stage_fk());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getImpacted_element());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getAffected_structures());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getCustodian());
						
						/*
						 * String hod = ""; 
						 * if(!StringUtils.isEmpty(obj.getHod_user_id_fk())) {
						 * hod = obj.getHod_user_id_fk() +" - "+obj.getUser_name();}
						 */
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getDesignation());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getPlanned_completion_date());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getExecuted_by());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getLatest_progress_date());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getLatest_progress_update());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getShifting_completion_date());
						
						cell2 = row1.createCell(d++);
						cell2.setCellStyle(activityNameStyle1);
						cell2.setCellValue(zObj.getRemarks());
		
				        rowNo2++;
					}
			        for(int columnIndex = 0; columnIndex < headerStringArr2.length; columnIndex++) {
					  	//rrSheet2.setColumnWidth(0, 25 * 60);
					  	//rrSheet2.autoSizeColumn(columnIndex);
					  	rrSheet2.setColumnWidth(columnIndex, 35 * 125);
				    }    
			        
	        	}   
			        
			        
	        }
            /*******************************************************************************/
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
            Date date = new Date();
            String fileName = "Utility_shifting_Report_"+dateFormat.format(date);
            
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
                e.printStackTrace();
                logger.error("generateUtilityShifting : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportInvalid);
            }catch(IOException e){
                e.printStackTrace();
                logger.error("generateUtilityShifting : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportError);
            }
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generateUtilityShifting : " + e.getMessage());
		}
		//return model;
    }

	//@RequestMapping(value = "/generate-utility-report", method = {RequestMethod.GET,RequestMethod.POST})
	public void generateRandRReport2(@ModelAttribute UtilityShifting obj,HttpServletRequest request, HttpServletResponse response,HttpSession session,RedirectAttributes attributes){
		//ModelAndView model = new ModelAndView("redirect:/activities-progress-report");
		try{
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			String report_created_date = df.format(new Date());
			
			SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-YY");
		
			//List<UtilityShifting> structuresList = service.getStructuresList(obj);
			
			UtilityShifting reportData = service.getUtilityShiftingData(obj);
			
			//Map<String,List<UtilityShifting>> reportData = service.getUtilityShiftingReportData(obj);
			
			XSSFWorkbook  workBook = new XSSFWorkbook();
			
			/***************************************************************************/
	        
			byte[] blueRGB = new byte[]{(byte)214, (byte)255, (byte)255};
			byte[] yellowRGB = new byte[]{(byte)255, (byte)255, (byte)153};
	        byte[] greenRGB = new byte[]{(byte)146, (byte)208, (byte)80};
	        byte[] redRGB = new byte[]{(byte)255, (byte)0, (byte)0};
	        byte[] whiteRGB = new byte[]{(byte)255, (byte)255, (byte)255};
	        byte[] greyRGB = new byte[]{(byte)211, (byte)211, (byte)211};
	        
	        
	        boolean isWrapText = true;boolean isBoldText = true;boolean isItalicText = false; int fontSize = 11;String fontName = "Calibri";
	        CellStyle greenStyle = cellFormating(workBook,greenRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle greenStyle1 = cellFormating(workBook,yellowRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle bluetyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle whiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        
	        CellStyle indexWhiteStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	       
	        CellStyle indexWhiteStyle1 = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle indexWhiteStyle2 = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        
	        isWrapText = true;isBoldText = false;isItalicText = false; fontSize = 11;fontName = "Calibri";
	        CellStyle numberStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle activityNameStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle activityNameStyle1 = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle activityNameStyle2 = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

	        CellStyle indexShadedStyle = cellFormating(workBook,greyRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,true,isItalicText,11,fontName);


	        /********************************************************/
          
	        if(!(StringUtils.isEmpty(reportData))){
	        	
       		 	/*XSSFSheet rrSheet1 = workBook.createSheet(WorkbookUtil.createSafeSheetName("Utility Shifting - Summary Report"));
		        XSSFRow headRow = rrSheet1.createRow(0);
		        
		        Cell cell = headRow.createCell(0);
		        
		        XSSFRow mainHeadingRow = rrSheet1.createRow(1);
		        
		        cell = mainHeadingRow.createCell(0);
		        cell.setCellStyle(bluetyle);
		        String work_d = "";
				if(!StringUtils.isEmpty(obj.getWork_id_fk())) {
					// work_d = reportData.getReport1List().get(0).getWork_short_name()+" - " ;
		        }
		        cell.setCellValue(work_d+" Utility Shifting Summary Report");
		        
		        for (int i = 1; i < 5; i++) {		        	
			        cell = mainHeadingRow.createCell(i);
			        cell.setCellStyle(bluetyle);
					cell.setCellValue("");
				}	
		        rrSheet1.addMergedRegion(new CellRangeAddress(1, 1, 0,4));
				int rowNo = 3;
		
		        XSSFRow structureRow = rrSheet1.createRow(rowNo);
		
		        
				String headerString = "Sr No.^Execution Agency^Total no of utilities^Completed utilities (No's)^Total Remaining Utilities (No's)";
		        String[] headerStringArr = headerString.split("\\^");
		        
		        XSSFRow headingRow = rrSheet1.createRow(rowNo);
		        for (int i = 0; i < headerStringArr.length ; i++) {	
		        	
		    			 cell = headingRow.createCell(i);
		    	    	 cell.setCellStyle(bluetyle);
						 cell.setCellValue(headerStringArr[i]);
				}	
		
		        
		        
		        
		        int totalStructures = 0,totalPV = 0,totalRE = 0,totalHC = 0,totalBC = 0;
		        rowNo++; int rownum = 5;
		        String locName = null;
		        int x = 0,z=0,sNo = 1;  String workId = null;
	        	 for (UtilityShifting zObj : reportData.getReport1List()) {  
				            int c = 0;
		
			  
					        XSSFRow row = rrSheet1.createRow(rowNo);
					       
					        row = rrSheet1.createRow(rowNo);
					       	 if(!StringUtils.isEmpty(workId) && !workId.equals(zObj.getWork_short_name()) ){
						       		x = 0;
								   }
						    	 
						    	workId = zObj.getWork_short_name();
						       if(!StringUtils.isEmpty(workId) &&  x == 0) {
							   		System.out.println(workId);
							   		cell = row.createCell(c++);
							        cell.setCellStyle(greenStyle1);
									cell.setCellValue(workId);
									
									for (int i = 1; i < 5; i++) {		        	
								        cell = row.createCell(i);
								        cell.setCellStyle(greenStyle1);
										cell.setCellValue("");
									}	
									rrSheet1.addMergedRegion(new CellRangeAddress(rowNo,rowNo, 0,4));
									rowNo++;
									x++;
									row = rrSheet1.createRow(rowNo);
								}
						       c=0;
					        cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle1);
							cell.setCellValue(sNo);
				
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle1);
							cell.setCellValue(zObj.getExecution_agency_fk());
						
						
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle2);
							cell.setCellValue(zObj.getUtilities());
							totalStructures = totalStructures + Integer.parseInt(zObj.getUtilities());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle2);
							cell.setCellValue(zObj.getRemaining());
							totalPV = totalPV + Integer.parseInt(zObj.getRemaining());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle2);
							cell.setCellValue(zObj.getBalance());
							
					        rowNo++;
								        
	        	 }

	        	 XSSFRow row = rrSheet1.createRow(rowNo);
			       int c=0;
			        row = rrSheet1.createRow(rowNo);
			        
			        cell = row.createCell(c++);
					cell.setCellStyle(activityNameStyle);
					cell.setCellValue("");
		
				
				    cell = row.createCell(c++);
					cell.setCellStyle(indexWhiteStyle1);
					cell.setCellValue("Total");
					
					cell = row.createCell(c++);
					cell.setCellStyle(indexWhiteStyle2);
					cell.setCellValue(totalStructures);
		
					cell = row.createCell(c++);
					cell.setCellStyle(indexWhiteStyle2);
					cell.setCellValue(totalPV);
					
					cell = row.createCell(c++);
					cell.setCellStyle(indexWhiteStyle2);
					cell.setCellValue(totalStructures - totalPV );
				
					
				  for(int columnIndex = 1; columnIndex < headerStringArr.length; columnIndex++) {
				  	rrSheet1.setColumnWidth(0, 25 * 60);
				  	//rrSheet1.autoSizeColumn(columnIndex);
				  	rrSheet1.setColumnWidth(columnIndex, 35 * 125);
				   }
						   // rrSheet1.setColumnWidth(0, 25 * 120);
			*/
		    /**********************************************************************/
	        int x = 0,z=0,sNo = 1;  String workId = null;
			String headerString2 = "Utility ID^work^Execution Agency^Impacted Contract Short Name^Type of Utility Shifting ^Description^Requirement stage^Impacted Element^Affected Structures^Custodian^HOD^Target Date^Executed By"
					+ "^Latest Progress Date^Latest Progress Update^Actual Completion Date^Remarks";
	        String[] headerStringArr2 = headerString2.split("\\^");	  
	        
   		 	XSSFSheet rrSheet2 = workBook.createSheet(WorkbookUtil.createSafeSheetName("Utility Shifting - Detail Report"));
   		 	
	        XSSFRow headRow2 = rrSheet2.createRow(0);
	        
	        Cell cell2 = headRow2.createCell(0);
	        
	        XSSFRow mainHeadingRow2 = rrSheet2.createRow(1);
	        
	        cell2 = mainHeadingRow2.createCell(0);
	        cell2.setCellStyle(bluetyle);
	        //cell2.setCellValue(work_d+" Utility Shifting Detail Report");
	        
	        cell2.setCellValue("Utility Shifting Detail Report");
	        
	        for (int i = 1; i < headerStringArr2.length; i++) {		        	
		        cell2 = mainHeadingRow2.createCell(i);
		        cell2.setCellStyle(bluetyle);
				cell2.setCellValue("");
			}	
	        rrSheet2.addMergedRegion(new CellRangeAddress(1, 1, 0,headerStringArr2.length));
	        int rowNo2 = 3;
		    // workBook.setSheetOrder(rrSheet1.getSheetName(), sheetNo++);	        	

	        XSSFRow structureRow2 = rrSheet2.createRow(rowNo2);

	        
	        
	        XSSFRow headingRow2 = rrSheet2.createRow(rowNo2);
	        for (int i = 0; i < headerStringArr2.length ; i++) {	
	        	
	    			 cell2 = headingRow2.createCell(i);
	    	    	 cell2.setCellStyle(bluetyle);
					 cell2.setCellValue(headerStringArr2[i]);
			}	
	        rowNo2++; int sno2 = 1;x = 0; workId = null;
	        for (UtilityShifting zObj : reportData.getReport2List()) {  
               int d = 0;
               XSSFRow row1 = rrSheet2.createRow(rowNo2);
	      	   if(!StringUtils.isEmpty(workId) && !workId.equals(zObj.getWork_short_name()) ){
		       		x = 0;
		       }
			    	 
		       workId = zObj.getWork_short_name();
		       if(!StringUtils.isEmpty(workId) &&  x == 0) {
			   		//System.out.println(workId);
		    	   Cell cell = row1.createCell(d++);
			        cell.setCellStyle(greenStyle1);
					cell.setCellValue(workId);
					
					for (int i = 1; i < headerStringArr2.length; i++) {		        	
				        cell = row1.createCell(i);
				        cell.setCellStyle(greenStyle1);
						cell.setCellValue("");
					}	
					rrSheet2.addMergedRegion(new CellRangeAddress(rowNo2,rowNo2, 0,12));
					rowNo2++;
					x++;
					row1 = rrSheet2.createRow(rowNo2);
				}
		        d = 0;
	
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getUtility_shifting_id());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getWork_short_name());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getExecution_agency_fk());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getContract_short_name());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getUtility_type_fk());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getUtility_description());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getRequirement_stage_fk());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getImpacted_element());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getAffected_structures());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getCustodian());
				
				/*
				 * String hod = ""; 
				 * if(!StringUtils.isEmpty(obj.getHod_user_id_fk())) {
				 * hod = obj.getHod_user_id_fk() +" - "+obj.getUser_name();}
				 */
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getDesignation());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getPlanned_completion_date());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getExecuted_by());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getLatest_progress_date());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getLatest_progress_update());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getShifting_completion_date());
				
				cell2 = row1.createCell(d++);
				cell2.setCellStyle(activityNameStyle1);
				cell2.setCellValue(zObj.getRemarks());

		        rowNo2++;
			}
	        for(int columnIndex = 0; columnIndex < headerStringArr2.length; columnIndex++) {
			  	//rrSheet2.setColumnWidth(0, 25 * 60);
			  	//rrSheet2.autoSizeColumn(columnIndex);
			  	rrSheet2.setColumnWidth(columnIndex, 35 * 125);
			   }    
			        
			        
			        
			        
	        }
            /*******************************************************************************/
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
            Date date = new Date();
            String fileName = "Utility_shifting_Report_"+dateFormat.format(date);
            
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
                e.printStackTrace();
                logger.error("generateUtilityShifting : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportInvalid);
            }catch(IOException e){
                e.printStackTrace();
                logger.error("generateUtilityShifting : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportError);
            }
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generateUtilityShifting : " + e.getMessage());
		}
		//return model;
    }

	private ModelAndView noDataAlertCall(RedirectAttributes attributes) {
		ModelAndView model = new ModelAndView();
		try {
			attributes.addFlashAttribute("error", "No updates in this period");
		} catch (Exception e) {

		}
		return model;
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

		Font font = workBook.createFont();
		// font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		font.setFontHeightInPoints((short) fontSize);
		font.setFontName(fontName); // "Calibri"

		font.setItalic(isItalicText);
		font.setBold(isBoldText);
		// Applying font to the style
		style.setFont(font);

		return style;
	}
	
	
}

