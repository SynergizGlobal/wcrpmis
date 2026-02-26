package com.wcr.wcrbackend.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.LandAcquisitionReportService;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api/land-report")
public class LandAcquisitionReportController {

	 private static final Logger logger =
	            LoggerFactory.getLogger(LandAcquisitionReportController.class);

	    @Autowired
	    private LandAcquisitionReportService service;

	    @GetMapping("/list")
	    public ResponseEntity<List<LandAcquisition>> getProjects(HttpSession session) {
	        try {
	            User uObj = (User) session.getAttribute("user");

	            if (uObj == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	            }

	            LandAcquisition obj = new LandAcquisition();
	            obj.setUser_id(uObj.getUserId());
	            obj.setUser_role_code(uObj.getUserRoleNameFk());
	            obj.setUser_type_fk(uObj.getUserTypeFk());

	            List<LandAcquisition> list =
	                    service.getProjectsFilterListInLandReport(obj);

	            return ResponseEntity.ok(list);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.internalServerError().build();
	        }
	    }

	    /* ===============================
	       GET PROJECT LIST
	       =============================== */
	    @GetMapping("/project-list")
	    public ResponseEntity<?> getProjectList(HttpSession session) {

	        try {

	        	   User user = (User) session.getAttribute("user");

		            if (user == null) {
		                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		                        .body("Session expired");
		            }

		            LandAcquisition obj = new LandAcquisition();

		            // USE SAME FIELDS AS MVP
		            obj.setUser_id(user.getUserId());
		            obj.setUser_role_code(user.getUserRoleNameFk());
		            obj.setUser_type_fk(user.getUserTypeFk());
	        
		            System.out.println(user.getUserRoleNameFk());
		            System.out.println(user.getUserTypeFk());
		            System.out.println(user.getUserId());

	            List<LandAcquisition> list =
	                    service.getProjectsFilterListInLandReport(obj);

	            return ResponseEntity.ok(list);

	        } catch (Exception e) {
	            logger.error("getProjectList error", e);
	            return ResponseEntity.internalServerError().build();
	        }
	    }
	    /* ===============================
	       GET TYPE OF LAND
	       =============================== */
	    @GetMapping("/type-list")
	    public ResponseEntity<?> getTypeOfLandList(HttpSession session) {

	        try {

	            User user = (User) session.getAttribute("user");

	            if (user == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                        .body("Session expired");
	            }

	            LandAcquisition obj = new LandAcquisition();

	            // USE SAME FIELDS AS MVP
	            obj.setUser_id(user.getUserId());
	            obj.setUser_role_code(user.getUserRoleNameFk());
	            obj.setUser_type_fk(user.getUserTypeFk());

	            List<LandAcquisition> list =
	                    service.getTypeOfLandListInLandReport(obj);

	            return ResponseEntity.ok(list);

	        } catch (Exception e) {
	            logger.error("getTypeOfLandList error", e);
	            return ResponseEntity.internalServerError().build();
	        }
	    }
	    /* ===============================
	       GET SUB CATEGORY LIST
	       =============================== */
	    @GetMapping("/sub-category-list")
	    public ResponseEntity<?> getSubCategoryList(HttpSession session) {

	        try {

	            User user = (User) session.getAttribute("user");

	            if (user == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                        .body("Session expired");
	            }

	            LandAcquisition obj = new LandAcquisition();

	            obj.setUser_id(user.getUserId());
	            obj.setUser_role_code(user.getUserRoleNameFk());
	            obj.setUser_type_fk(user.getUserTypeFk());

	            List<LandAcquisition> list =
	                    service.getSubCategoryOfLandFilterListInLandReport(obj);

	            return ResponseEntity.ok(list);

	        } catch (Exception e) {
	            logger.error("getSubCategoryList error", e);
	            return ResponseEntity.internalServerError().build();
	        }
	    }

	    
	    /* =========================================================
	       GENERATE LAND ACQUISITION EXCEL REPORT
	       ========================================================= */
	    @PostMapping("/generate")
	    public ResponseEntity<byte[]> generateLandReport(
	            @RequestBody LandAcquisition obj,
	            HttpSession session) {

	        try {

	            User uObj = (User) session.getAttribute("user");

	            if (uObj == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	            }

	            obj.setUser_type_fk(uObj.getUserTypeFk());
	            obj.setUser_role_code(uObj.getUserRoleNameFk());
	            obj.setUser_id(uObj.getUserId());

	            LandAcquisition reportData = service.getLandAcquisitionData(obj);

	            // 👇 THIS is the fix
	            XSSFWorkbook workbook = buildWorkbook(obj, reportData);

	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            workbook.write(out);
	            workbook.close();

	            byte[] excelBytes = out.toByteArray();
	            if (reportData == null ||
	            	    reportData.getReport1List() == null ||
	            	    reportData.getReport1List().isEmpty()) {

	            	    return ResponseEntity
	            	            .status(HttpStatus.BAD_REQUEST)
	            	            .body("No Data Found".getBytes());
	            	}

	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION,
	                            "attachment; filename=Land_Acquisition_Report.xlsx")
	                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                    .body(excelBytes);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(("Error: " + e.getMessage()).getBytes());
	        }
	    }
	    
	    private XSSFWorkbook buildWorkbook(
	            LandAcquisition obj,
	            LandAcquisition reportData) throws Exception {

	        XSSFWorkbook workBook = new XSSFWorkbook();
        
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
        CellStyle activityNameStyle3 = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

        
        isWrapText = true;isBoldText = false;isItalicText = false; fontSize = 11;fontName = "Calibri";
        CellStyle numberStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
        CellStyle activityNameStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
        CellStyle activityNameStyle1 = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

        CellStyle activityNameStyle2 = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

        CellStyle indexShadedStyle = cellFormating(workBook,greyRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,true,isItalicText,11,fontName);


        /********************************************************/
      
        if (reportData != null &&
        	    reportData.getReport1List() != null &&
        	    !reportData.getReport1List().isEmpty()) {
        	
   		 	XSSFSheet rrSheet1 = workBook.createSheet(WorkbookUtil.createSafeSheetName("Land Acquisition - Summary Report"));
	        XSSFRow headRow = rrSheet1.createRow(0);
	        
	        Cell cell = headRow.createCell(0);
	        
	        XSSFRow mainHeadingRow = rrSheet1.createRow(1);
	        
	        cell = mainHeadingRow.createCell(0);
	        cell.setCellStyle(bluetyle);
	        String work_d = "";
			if(!StringUtils.isEmpty(obj.getWork_id_fk())) {
				 //work_d = reportData.getReport1List().get(0).getWork_short_name()+" - " ;
	        }
	        cell.setCellValue(work_d+" Land Acquisition - Summary Report");
	        
	        for (int i = 1; i < 6; i++) {		        	
		        cell = mainHeadingRow.createCell(i);
		        cell.setCellStyle(bluetyle);
				cell.setCellValue("");
			}	
	        rrSheet1.addMergedRegion(new CellRangeAddress(1, 1, 0,5));
	int rowNo = 3;

    XSSFRow structureRow = rrSheet1.createRow(rowNo);

    /**********************************************************************/
	String headerString = "Sr No.^Type of Land^Sub Category of Land^Area to be Acquired (Ha)^Area  Acquired (Ha)^Balance Land Acquisition (HA)";
    String[] headerStringArr = headerString.split("\\^");
    
    XSSFRow headingRow = rrSheet1.createRow(rowNo);
    for (int i = 0; i < headerStringArr.length ; i++) {	
    	
			 cell = headingRow.createCell(i);
	    	 cell.setCellStyle(bluetyle);
			 cell.setCellValue(headerStringArr[i]);
	}	

    Double totalATB = 0.0,totalAE = 0.0,totalBal = 0.0;
    rowNo++; int rownum = 6;
    String categoty = null;
    String workId = null;
    int x = 0,z=0,sNo = 1,v = 1;
   
        	 for (LandAcquisition zObj : reportData.getReport1List()) {  
			            int c = 0;
			            XSSFRow row = rrSheet1.createRow(rowNo);
			          
					   	if(!StringUtils.isEmpty(categoty) &&  !categoty.equals(zObj.getCategory_fk()) 
					   			&& (StringUtils.isEmpty( obj.getType_of_land()) || StringUtils.isEmpty( obj.getType_of_land()) )) {
					   		System.out.println(categoty);
					   		 sNo++;
					   		int firstRow = rownum;
					   		int lastRow = rowNo - 1;

					   		if (lastRow >= firstRow) {
					   		    rrSheet1.addMergedRegion(
					   		        new CellRangeAddress(firstRow, lastRow, 1, 1)
					   		    );
					   		    rrSheet1.addMergedRegion(
					   		        new CellRangeAddress(firstRow, lastRow, 0, 0)
					   		    );
					   		}

					   		rownum = rowNo;
					   		if(!workId.equals(zObj.getWork_short_name()) ){
								 x = 0; 
							   } 
						}else if(!StringUtils.isEmpty(categoty) && !StringUtils.isEmpty(workId) && !workId.equals(zObj.getWork_short_name())){
							System.out.println(categoty);
					   		 sNo++;
					   		int firstRow = rownum;
					   		int lastRow = rowNo - 1;   // IMPORTANT

					   		if (lastRow > firstRow) {
					   		    rrSheet1.addMergedRegion(
					   		        new CellRangeAddress(firstRow, lastRow, 1, 1)
					   		    );
					   		    rrSheet1.addMergedRegion(
					   		        new CellRangeAddress(firstRow, lastRow, 0, 0)
					   		    );
					   		}
							 rownum = lastRow +1;
							 if(!workId.equals(zObj.getWork_short_name()) ){
								 x = 0; 
							   } 
							
						}
				    	categoty = zObj.getCategory_fk();
		  /***********************************************************************/
				    	 if(!StringUtils.isEmpty(workId) && !workId.equals(zObj.getWork_short_name()) ){
				    		 rownum++;
						   }
				    	 
				    	workId = zObj.getWork_short_name();
				       if(!StringUtils.isEmpty(workId) &&  x == 0) {
					   		System.out.println(workId);
					   		cell = row.createCell(c++);
					        cell.setCellStyle(greenStyle1);
							cell.setCellValue(workId);
							
							for (int i = 1; i < 6; i++) {		        	
						        cell = row.createCell(i);
						        cell.setCellStyle(greenStyle1);
								cell.setCellValue("");
							}	
							rrSheet1.addMergedRegion(new CellRangeAddress(rowNo,rowNo, 0,5));
							rowNo++;
							x++;
							row = rrSheet1.createRow(rowNo);
						}
				        c=0;
				        
				        cell = row.createCell(c++);
						cell.setCellStyle(activityNameStyle1);
						cell.setCellValue(sNo);
			
						cell = row.createCell(c++);
						cell.setCellStyle(activityNameStyle3);
						cell.setCellValue(zObj.getCategory_fk());
					
						
				
					    cell = row.createCell(c++);
						cell.setCellStyle(activityNameStyle1);
						String subCat = "-";
						if(!StringUtils.isEmpty( zObj.getLa_sub_category()))
							 subCat = zObj.getLa_sub_category();
						cell.setCellValue(subCat);
						
						cell = row.createCell(c++);
						cell.setCellStyle(activityNameStyle2);
						cell.setCellValue(zObj.getArea_to_be_acquired());
						totalATB = totalATB + Double.parseDouble(zObj.getArea_to_be_acquired());
						
						cell = row.createCell(c++);
						cell.setCellStyle(activityNameStyle2);
						cell.setCellValue(zObj.getArea_acquired());
						totalAE = totalAE + Double.parseDouble(zObj.getArea_acquired());
						
						cell = row.createCell(c++);
						cell.setCellStyle(activityNameStyle2);
						cell.setCellValue(zObj.getBalance_area());
						totalBal = totalBal + Double.parseDouble(zObj.getBalance_area());
				        rowNo++;
							        
        	 }
        	 int firstRow = rownum;
        	 int lastRow = rowNo - 1;

        	 if (lastRow >= firstRow) {
        	     rrSheet1.addMergedRegion(
        	         new CellRangeAddress(firstRow, lastRow, 1, 1)
        	     );
        	     rrSheet1.addMergedRegion(
        	         new CellRangeAddress(firstRow, lastRow, 0, 0)
        	     );
        	 }
        	 XSSFRow row = rrSheet1.createRow(rowNo);
		       int c=0;
		        row = rrSheet1.createRow(rowNo);
		        
		        cell = row.createCell(c++);
				cell.setCellStyle(activityNameStyle);
				cell.setCellValue("");
	
				cell = row.createCell(c++);
				cell.setCellStyle(activityNameStyle);
				cell.setCellValue("");
		
			    cell = row.createCell(c++);
				cell.setCellStyle(indexWhiteStyle1);
				cell.setCellValue("Total");
				
				cell = row.createCell(c++);
				cell.setCellStyle(indexWhiteStyle2);
				cell.setCellValue(totalATB);
	
				cell = row.createCell(c++);
				cell.setCellStyle(indexWhiteStyle2);
				cell.setCellValue(totalAE);
				
				cell = row.createCell(c++);
				cell.setCellStyle(indexWhiteStyle2);
				cell.setCellValue(totalBal);
				
			  for(int columnIndex = 1; columnIndex < headerStringArr.length; columnIndex++) {
			  	rrSheet1.setColumnWidth(0, 25 * 60);
			  	//rrSheet1.autoSizeColumn(columnIndex);
			  	rrSheet1.setColumnWidth(columnIndex, 35 * 130);
			   }
        
					   // rrSheet1.setColumnWidth(0, 25 * 120);
       		 	XSSFSheet rrSheet2 = workBook.createSheet(WorkbookUtil.createSafeSheetName("Land Acquisition - Detail Report"));
       		 	
		        XSSFRow headRow2 = rrSheet2.createRow(0);
		        
		        Cell cell2 = headRow2.createCell(0);
		        
		        XSSFRow mainHeadingRow2 = rrSheet2.createRow(1);
		        
		        cell2 = mainHeadingRow2.createCell(0);
		        cell2.setCellStyle(bluetyle);
		        cell2.setCellValue(work_d+" Land Acquisition - Detail Report");
		        
		        for (int i = 1; i < 11; i++) {		        	
			        cell2 = mainHeadingRow2.createCell(i);
			        cell2.setCellStyle(bluetyle);
					cell2.setCellValue("");
				}	
		        rrSheet2.addMergedRegion(new CellRangeAddress(1, 1, 0,10));
		        int rowNo2 = 3;
			    // workBook.setSheetOrder(rrSheet1.getSheetName(), sheetNo++);	        	

        XSSFRow structureRow2 = rrSheet2.createRow(rowNo2);

        /**********************************************************************/
		String headerString2 = "Sr No.^LA ID^Survey Number^Type of Land^Sub Category of Land"
				+ "^Village^Area to be Acquired (Ha)^Area Acquired (Ha)^Balance Land Acquisition (Ha)^Land Status^Issue";
        String[] headerStringArr2 = headerString2.split("\\^");
        workId = null;x = 0;
        XSSFRow headingRow2 = rrSheet2.createRow(rowNo2);
        for (int i = 0; i < headerStringArr2.length ; i++) {	
        	
    			 cell2 = headingRow2.createCell(i);
    	    	 cell2.setCellStyle(bluetyle);
				 cell2.setCellValue(headerStringArr2[i]);
		}	
        rowNo2++; int sno2 = 1;
        for (LandAcquisition zObj : reportData.getReport2List()) {  
        	 XSSFRow row1 = rrSheet2.createRow(rowNo2);
             int d = 0;
            if(!StringUtils.isEmpty(workId) && !workId.equals(zObj.getWork_short_name()) ){
	       		x = 0;
			   }
	    	 
	    	workId = zObj.getWork_short_name();
	       if(!StringUtils.isEmpty(workId) &&  x == 0) {
		   		System.out.println(workId);
		   		cell = row1.createCell(d++);
		        cell.setCellStyle(greenStyle1);
				cell.setCellValue(workId);
				
				for (int i = 1; i < 11; i++) {		        	
			        cell = row1.createCell(i);
			        cell.setCellStyle(greenStyle1);
					cell.setCellValue("");
				}	
				rrSheet2.addMergedRegion(new CellRangeAddress(rowNo2,rowNo2, 0,10));
				rowNo2++;
				x++;
				row1 = rrSheet2.createRow(rowNo2);
			}
			
			d = 0;
	        
	        cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle1);
			cell2.setCellValue(sno2++);

			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle1);
			cell2.setCellValue(zObj.getLa_id());
			
			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle1);
			cell2.setCellValue(zObj.getSurvey_number());
			
			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle1);
			cell2.setCellValue(zObj.getCategory_fk());
			
			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle1);
			String subCat = "-";
			if(!StringUtils.isEmpty( zObj.getLa_sub_category()))
				 subCat = zObj.getLa_sub_category();
			cell2.setCellValue(subCat);
			
		    cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle);
			cell2.setCellValue(zObj.getVillage());
			
			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle2);
			cell2.setCellValue(zObj.getArea_to_be_acquired());
			
			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle2);
			cell2.setCellValue(zObj.getArea_acquired());
			
			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle2);
			cell2.setCellValue(zObj.getBalance_area());
			
			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle1);
			cell2.setCellValue(zObj.getLa_land_status_fk());
			
			cell2 = row1.createCell(d++);
			cell2.setCellStyle(activityNameStyle1);
			cell2.setCellValue(zObj.getIssue_id());				

	        rowNo2++;
		}
        for(int columnIndex = 1; columnIndex < headerStringArr2.length; columnIndex++) {
		  	rrSheet2.setColumnWidth(0, 25 * 60);
		  	//rrSheet2.autoSizeColumn(columnIndex);
		  	rrSheet2.setColumnWidth(columnIndex, 35 * 135);
		   }    
		        
		        
		        
		        
        }
        /*******************************************************************************/
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        Date date = new Date();
        String fileName = "Land_Acquisition_Report_"+dateFormat.format(date);
        
        return workBook;
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
