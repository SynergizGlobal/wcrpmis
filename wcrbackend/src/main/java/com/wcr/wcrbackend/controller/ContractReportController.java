package com.wcr.wcrbackend.controller;

import java.util.List;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.xml.bind.JAXBElement;

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
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.Docx4jProperties;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.structure.PageDimensions;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.DocPropsCorePart;
import org.docx4j.openpackaging.parts.DocPropsExtendedPart;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.DocumentSettingsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.utils.BufferUtil;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTSettings;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.CTTblLayoutType;
import org.docx4j.wml.CTVerticalJc;
import org.docx4j.wml.Color;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.FldChar;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.Hdr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.HeaderReference;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.STFldCharType;
import org.docx4j.wml.STHint;
import org.docx4j.wml.STPageOrientation;
import org.docx4j.wml.STTblLayoutType;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.SectPr.PgSz;
import org.docx4j.wml.TcPrInner.TcBorders;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;
import org.docx4j.wml.PPrBase.Spacing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.CommonConstants2;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.common.DocxTableCreationForContractReport;
import com.wcr.wcrbackend.common.EMailSender;
import com.wcr.wcrbackend.constants.PageConstants2;
import com.wcr.wcrbackend.service.ContractService;
import com.wcr.wcrbackend.service.IContractReportService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/contract-report")
public class ContractReportController {
	
	public static Logger logger = Logger.getLogger(ContractReportController.class);
	
	@Autowired
	private IContractReportService contractReportService;
	
	@Autowired
	ContractService contractService;
	
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
	
	@GetMapping("/api/getProjectList")
	public ResponseEntity<List<Contract>> getProjectList() throws Exception{
		List<Contract> contracts = contractReportService.getProjectList();
		return ResponseEntity.ok(contracts);
	}
	
	@PostMapping("/ajax/getHODListInContractReport")
	public ResponseEntity<List<Contract>> getHODListInContractReport(@RequestBody Contract obj) {
		List<Contract> hodList = null;
		try {
			hodList = contractReportService.getHODListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(hodList);
	}
	
	@PostMapping("/ajax/getContractorsListInContractReport")
	public ResponseEntity<List<Contract>> getContractorsListInContractReport(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getContractorsListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	
	@PostMapping("/ajax/getContractStatusListInContractReport")
	public ResponseEntity<List<Contract>> getContractStatusListInContractReport(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getContractStatusListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	
	@PostMapping("/ajax/getStatsuListInContractReport")
	public ResponseEntity<List<Contract>> getStatsuListInContractReport(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getStatsuListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	
	@PostMapping("/ajax/getStatusofWorkItems")
	public ResponseEntity<List<Contract>> getStatusofWorkItems(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getStatusofWorkItems(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	@PostMapping("/ajax/getContractListInContractReport")
	public ResponseEntity<List<Contract>> getContractListInContractReport(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getContractListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	
	///
	
	
	@RequestMapping(value = "/contract-report", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView contractReport(@ModelAttribute Contract obj,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView(PageConstants2.contractReport);
		try{
			if(!StringUtils.isEmpty(obj)) {
				model.addObject("report_no", obj.getId());
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("contractReport : " + e.getMessage());
		}
		return model;
    }
	
	@RequestMapping(value = "/contract-report/{report_no}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView contractReports(@PathVariable("report_no")String report_no,RedirectAttributes attributes){
		ModelAndView model = new ModelAndView(PageConstants2.contractReport);
		try{
			Contract obj = new Contract();
			List<Contract> projectsList = contractService.getProjectsListForContractForm(obj);
			model.addObject("projectsList", projectsList);
			
			model.addObject("report_no", report_no);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("contractReports : " + e.getMessage());
		}
		return model;
     }
	
	
	@RequestMapping(value="/contract-report-auto-email",method=RequestMethod.GET)
	public void contractReportAutoEmail(){
		try {
			Contract obj=new Contract();
			
			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
            String currentDate = sqlDate.format(date);
	           
            obj.setDate(DateParser.parse(obj.getDate()));	
            
            DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfInput = new SimpleDateFormat("dd-MM-yyyy");			
			
            Calendar c1 = Calendar.getInstance();
            
            c1.set(Calendar.DATE, 16);
            c1.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            c1.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
            String date1=sdf.format(getFirstDateOfMonth(new Date()));
            String date2=sdf.format(new Date());
            String date3=sdf.format(c1.getTime());
            
            if((date1.compareTo(date2)==0) || (date3.compareTo(date2)==0)){
	            if(date1.compareTo(date2)==0){
	               Calendar cal = Calendar.getInstance();
	               
	               cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	               String LastDate=sdfInput.format(cal.getTime());
	         	   obj.setDate(DateParser.parse(LastDate));
	            }
	            if(date3.compareTo(date2)==0){
	        	   Calendar cal2 = Calendar.getInstance(); 
	        	   cal2.set(Calendar.DATE, 15);
	        	   cal2.add(Calendar.MONTH, 1);
	        	   String NextMonthSameDate=sdfInput.format(cal2.getTime());
	        	   obj.setDate(DateParser.parse(NextMonthSameDate));
	            }
					
		        //obj.setDate("2021-08-31");
            
	            /*****************************************************************************************************************/
	            
	            Map<String,List<Contract>> allDeptContractsList = contractReportService.getContractsDocBGInsuranceForAutoEmailReport(obj);
	            
	            String recipients = contractReportService.getEmailIdsOfDepartments("CMD&DF");
	            
	            boolean flag1 = generatContractDocBGInsuranceReportAutoEmail(currentDate,obj,allDeptContractsList,recipients,report_created_date);
	            
	            /*****************************************************************************************************************/
				
				obj.setDepartment_fk("Engg");
				
				Map<String,List<Contract>> enggDeptContractslist = contractReportService.getContractsDocBGInsuranceForAutoEmailReport(obj);
				
				recipients = contractReportService.getEmailIdsOfDepartments("DP&CE");
				
				boolean flag2 = generatContractDocBGInsuranceReportAutoEmail(currentDate,obj,enggDeptContractslist,recipients,report_created_date);
				
				/*****************************************************************************************************************/
				
				obj.setDepartment_fk("'Elec','S&T'");
				
				Map<String,List<Contract>> ele_SAndTDeptContractslist = contractReportService.getContractsDocBGInsuranceForAutoEmailReport(obj);
				
				recipients = contractReportService.getEmailIdsOfDepartments("DT");
				
				boolean flag3 = generatContractDocBGInsuranceReportAutoEmail(currentDate,obj,ele_SAndTDeptContractslist,recipients,report_created_date);
				
				/*****************************************************************************************************************/
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("contractReportAutoEmail : " + e.getMessage());
		}
	}	
	
	public static Date getFirstDateOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }


	
	@RequestMapping(value="/bg-contractual-letters",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView bgContractualLetters(HttpSession session){
		ModelAndView model = new ModelAndView(PageConstants2.displayOfTheExpiringBgs);
		try {

		}catch (Exception e) {
			e.printStackTrace();
			logger.error("bgContractualLetters : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value="/insurance-contractual-letters",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView insuranceContractualLetters(HttpSession session){
		ModelAndView model = new ModelAndView(PageConstants2.displayOfTheExpiringInsurances);
		try {

		}catch (Exception e) {
			e.printStackTrace();
			logger.error("insuranceContractualLetters : " + e.getMessage());
		}
		return model;
	}
	
	@RequestMapping(value="/doc-contractual-letters",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView dateOfCompletionContractualLetters(HttpSession session){
		ModelAndView model = new ModelAndView(PageConstants2.displayOfTheExpiringDateOfCompletions);
		try {

		}catch (Exception e) {
			e.printStackTrace();
			logger.error("dateOfCompletionContractualLetters : " + e.getMessage());
		}
		return model;
	}	
	
	@RequestMapping(value = "/ajax/UpdateLetterStatus", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean UpdateLetterStatus(@ModelAttribute Contract obj) {
		boolean flag = false;
		try {
			flag = contractReportService.UpdateLetterStatus(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("UpdateLetterStatus : " + e.getMessage());
		}
		return flag;
	}
	
	@RequestMapping(value = "/ajax/UpdateInsuranceLetterStatus", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean UpdateInsuranceLetterStatus(@ModelAttribute Contract obj) {
		boolean flag = false;
		try {
			flag = contractReportService.UpdateInsuranceLetterStatus(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("UpdateInsuranceLetterStatus : " + e.getMessage());
		}
		return flag;
	}
	
	@RequestMapping(value = "/ajax/UpdateDateOfCompletionLetterStatus", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean UpdateDateOfCompletionLetterStatus(@ModelAttribute Contract obj) {
		boolean flag = false;
		try {
			flag = contractReportService.UpdateDateOfCompletionLetterStatus(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("UpdateDateOfCompletionLetterStatus : " + e.getMessage());
		}
		return flag;
	}	
	
	@RequestMapping(value = "/ajax/get-contract-bg-details", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Contract> generatContractBGDetails(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> contractList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUser_type_fk());
			obj.setUser_role_code(uObj.getUser_role_code());
			obj.setUser_id(uObj.getUser_id());
			contractList = contractReportService.generateContractBGDetails(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractBGDetails : " + e.getMessage());
		}
		return contractList;
    }
	
	@RequestMapping(value = "/ajax/get-contract-insurance-details", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Contract> generatContractInsuranceDetails(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> contractList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUser_type_fk());
			obj.setUser_role_code(uObj.getUser_role_code());
			obj.setUser_id(uObj.getUser_id());
			contractList = contractReportService.generatContractInsuranceDetails(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractInsuranceDetails : " + e.getMessage());
		}
		return contractList;
    }	
	
	@RequestMapping(value = "/ajax/get-contract-doc-details", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Contract> generatContractDOCDetails(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> contractList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUser_type_fk());
			obj.setUser_role_code(uObj.getUser_role_code());
			obj.setUser_id(uObj.getUser_id());
			contractList = contractReportService.generatContractDOCDetails(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractDOCDetails : " + e.getMessage());
		}
		return contractList;
    }	
	
	@RequestMapping(value = "/ajax/generate-bg-contractual-letters", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Contract> generatBGContractualLettersReport(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> contractList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUser_type_fk());
			obj.setUser_role_code(uObj.getUser_role_code());
			obj.setUser_id(uObj.getUser_id());
			contractList = contractReportService.getTheListOfExpiringBgs(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatBGContractualLettersReport : " + e.getMessage());
		}
		return contractList;
    }
	
	@RequestMapping(value = "/ajax/generate-insurance-contractual-letters", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Contract> generatInsuranceContractualLettersReport(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> contractList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUser_type_fk());
			obj.setUser_role_code(uObj.getUser_role_code());
			obj.setUser_id(uObj.getUser_id());
			contractList = contractReportService.getTheListOfExpiringInsurances(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatInsuranceContractualLettersReport : " + e.getMessage());
		}
		return contractList;
    }
	
	@RequestMapping(value = "/ajax/generate-doc-contractual-letters", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Contract> generatDOCContractualLettersReport(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> contractList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUser_type_fk());
			obj.setUser_role_code(uObj.getUser_role_code());
			obj.setUser_id(uObj.getUser_id());
			contractList = contractReportService.getTheListOfExpiringDocs(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatDOCContractualLettersReport : " + e.getMessage());
		}
		return contractList;
    }	
	
	@RequestMapping(value = "/generate-contract-download/{contract_id}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getContractDownload(@PathVariable("contract_id") String contract_id,@ModelAttribute Contract obj,HttpServletRequest request,HttpServletResponse response,HttpSession session, RedirectAttributes attributes){
		ModelAndView model = new ModelAndView("redirect:/bg-contractual-letters");
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
            String currentDate = sqlDate.format(date);
	           
            obj.setDate(DateParser.parse(obj.getDate()));
            
			boolean flag = generatContractBGExpiryLetterReport(response,currentDate,obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractBGExpiryLetterReport : " + e.getMessage());
		}
		return model;
	}

	@RequestMapping(value = "/generate-contract-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView generatContractReport(@ModelAttribute Contract obj,HttpServletRequest request,HttpServletResponse response,HttpSession session, RedirectAttributes attributes){
		ModelAndView model = new ModelAndView("redirect:/contract-report/{id}");
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
            String currentDate = sqlDate.format(date);
            
            obj.setDate(DateParser.parse(obj.getDate()));
            
			if(obj.getStatus()==null)
			{
				
				boolean flag = generatContractReportForAll(response,currentDate,obj);

			}
			else
			{
				boolean flag = generatContractReport(response,currentDate,obj);
			}
	           
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractReport : " + e.getMessage());
		}
		return model;
    }

	@RequestMapping(value = "/generate-bg-insurance-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	private void generateContractBgInsuranceReport(HttpServletResponse response, String currentDate, Contract obj) {
			try{
				
				SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-YY");
				
				Contract reportData = contractReportService.generateContractBgInsuranceReport(obj);
				
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
		        CellStyle greenStyle1 = cellFormating(workBook,greenRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        CellStyle bluetyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        CellStyle blueGreentyle = cellFormating(workBook,blueRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

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
	          
		        if(!(StringUtils.isEmpty(reportData))){
		        	
	       		 	XSSFSheet rrSheet1 = workBook.createSheet(WorkbookUtil.createSafeSheetName("List of Bank Guarantees/Insurances Report"));
			        XSSFRow headRow = rrSheet1.createRow(0);
			        
			        Cell cell = headRow.createCell(0);
			        
			        XSSFRow mainHeadingRow = rrSheet1.createRow(1);
			        
			        cell = mainHeadingRow.createCell(0);
			        cell.setCellStyle(bluetyle);
			        cell.setCellValue("List of Bank Guarantees/Insurances Report");
			        
			        for (int i = 1; i < 12; i++) {		        	
				        cell = mainHeadingRow.createCell(i);
				        cell.setCellStyle(bluetyle);
						cell.setCellValue("");
					}	
			        rrSheet1.addMergedRegion(new CellRangeAddress(1, 1, 0,12));
			int rowNo = 3;

	        XSSFRow structureRow = rrSheet1.createRow(rowNo);

	        /**********************************************************************/
			String headerString = "S. No.^Contract ID^Contract Short Name^Contractor Name^BG/Insurance^BG/Insurance Type^Issuing Bank^BG Number^Amount (INR)^Raised Date^Expiry Date^Release Date";
	        String[] headerStringArr = headerString.split("\\^");
	        
	        XSSFRow headingRow = rrSheet1.createRow(rowNo);
	        for (int i = 0; i < headerStringArr.length ; i++) {	
	        	
	    			 cell = headingRow.createCell(i);
	    	    	 cell.setCellStyle(whiteStyle);
					 cell.setCellValue(headerStringArr[i]);
			}	
	        
	        List<String> WorksList = new ArrayList<>(); 
	        
	        for (Contract zObj : reportData.getReport1List())  
	    	{
	    		if(WorksList.indexOf(zObj.getWork_short_name())==-1)
	    		{
	    			WorksList.add(zObj.getWork_short_name());
	    		}
	    	}       
	        rowNo++;
	        int x = 0,z=0;

			for (int i2 = 0; i2 < WorksList.size(); i2++) 
			{   
				
				int k=1;
				int m=0;
		        	 for (Contract zObj : reportData.getReport1List()) { 
		        		 
		        		 
		        		 if(WorksList.get(i2).compareTo(zObj.getWork_short_name())==0)
		        		 {
		        			 if(m==0) 
		        			 {
				     	        XSSFRow headWorkRow = rrSheet1.createRow(rowNo);
				    	        cell = headWorkRow.createCell(0);
				    	        cell.setCellStyle(bluetyle);
				    	        cell.setCellValue("Work:");
				    	        
				    	        cell = headWorkRow.createCell(1);
				    	        cell.setCellStyle(blueGreentyle);
				    	        cell.setCellValue(WorksList.get(i2));			    	        
				    	        
				    	        for (int i = 2; i < 12; i++) {		        	
				    		        cell = mainHeadingRow.createCell(i);
				    		        cell.setCellStyle(bluetyle);
				    				cell.setCellValue("");
				    			}	
				    	        rrSheet1.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 1,12));       
				    	        
				    	        rowNo++;
				    	        m++;
		        			 }
		        		 
		        		 
		        		 
		        		  XSSFRow row = rrSheet1.createRow(rowNo);
					            int c = 0;
					            
								cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(k);				            
							
							    cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getContract_id());
								
								cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getContract_name());
							
							    cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getContractor_name());
								
								cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getBg_insurance());
								
								cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getBg_insurance_type());
							
							    cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getIssuing_bank());
								
								cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getBg_insurance_number());
							
							    cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getAmount_inr());
								
								cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getRaised_date());
							
							    cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getExpiry_date());
								
								cell = row.createCell(c++);
								cell.setCellStyle(activityNameStyle);
								cell.setCellValue(zObj.getRelease_date());
						        rowNo++;
						        k++;
		        	 }
		        	 }
			}
					  for(int columnIndex = 1; columnIndex < headerStringArr.length; columnIndex++) {
					  	rrSheet1.setColumnWidth(columnIndex, 25 * 150);
					  	//rrSheet1.autoSizeColumn(columnIndex);
					  	rrSheet1.setColumnWidth(4, 35 * 235);
						rrSheet1.setColumnWidth(5, 35 * 235);
					   }

	            /*******************************************************************************/
	            
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
	            Date date = new Date();
	            String fileName = "List_of_Bank_Guarantees/Insurances_Report_"+dateFormat.format(date);
	            
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
	                logger.error("generateContractBgInsuranceReport : " + e.getMessage());
	                //attributes.addFlashAttribute("error",dataExportInvalid);
	            }catch(IOException e){
	                e.printStackTrace();
	                logger.error("generateContractBgInsuranceReport : " + e.getMessage());
	                //attributes.addFlashAttribute("error",dataExportError);
	            }
		      }
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("generateContractBgInsuranceReport : " + e.getMessage());
			}
	    
		}	
	
	
	@RequestMapping(value = "/generate-contract-completion-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	private void generateContractCompletionReport(HttpServletResponse response, String currentDate, Contract obj) {
		try{
			
			SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-YY");
			
			Contract reportData = contractReportService.generateContractCompletionReport(obj);
			
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
	        CellStyle greenStyle1 = cellFormating(workBook,greenRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle bluetyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle blueGreentyle = cellFormating(workBook,blueRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

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
          
	        if(!(StringUtils.isEmpty(reportData))){
	        	
       		 	XSSFSheet rrSheet1 = workBook.createSheet(WorkbookUtil.createSafeSheetName("Date of Completion Report"));
		        XSSFRow headRow = rrSheet1.createRow(0);
		        
		        Cell cell = headRow.createCell(0);
		        
		        XSSFRow mainHeadingRow = rrSheet1.createRow(1);
		        
		        cell = mainHeadingRow.createCell(0);
		        cell.setCellStyle(bluetyle);
		        cell.setCellValue("Date of Completion Report");
		        
		        for (int i = 1; i < 14; i++) {		        	
			        cell = mainHeadingRow.createCell(i);
			        cell.setCellStyle(bluetyle);
					cell.setCellValue("");
				}	
		        rrSheet1.addMergedRegion(new CellRangeAddress(1, 1, 0,14));
		int rowNo = 3;

        XSSFRow structureRow = rrSheet1.createRow(rowNo);

        /**********************************************************************/
		String headerString = "S. No.^Status of Contract^Contract ID^Contract Short Name^Contractor Name^LOA Date^Original Completion Date^Revised Completion Date^Progress % ";
        String[] headerStringArr = headerString.split("\\^");
        
        XSSFRow headingRow = rrSheet1.createRow(rowNo);
        for (int i = 0; i < headerStringArr.length ; i++) {	
        	
    			 cell = headingRow.createCell(i);
    	    	 cell.setCellStyle(whiteStyle);
				 cell.setCellValue(headerStringArr[i]);
		}	
        
        List<String> WorksList = new ArrayList<>(); 
        
        for (Contract zObj : reportData.getReport1List())  
    	{
    		if(WorksList.indexOf(zObj.getWork_short_name())==-1)
    		{
    			WorksList.add(zObj.getWork_short_name());
    		}
    	}       
        rowNo++;
        int x = 0,z=0;

		for (int i2 = 0; i2 < WorksList.size(); i2++) 
		{   
			
			int k=1;
			int m=0;
	        	 for (Contract zObj : reportData.getReport1List()) { 
	        		 
	        		 
	        		 if(WorksList.get(i2).compareTo(zObj.getWork_short_name())==0)
	        		 {
	        			 if(m==0) 
	        			 {
			     	        XSSFRow headWorkRow = rrSheet1.createRow(rowNo);
			    	        cell = headWorkRow.createCell(0);
			    	        cell.setCellStyle(bluetyle);
			    	        cell.setCellValue("Work:");
			    	        
			    	        cell = headWorkRow.createCell(1);
			    	        cell.setCellStyle(blueGreentyle);
			    	        cell.setCellValue(WorksList.get(i2));			    	        
			    	        
			    	        for (int i = 2; i < 14; i++) {		        	
			    		        cell = mainHeadingRow.createCell(i);
			    		        cell.setCellStyle(bluetyle);
			    				cell.setCellValue("");
			    			}	
			    	        rrSheet1.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 1,14));       
			    	        
			    	        rowNo++;
			    	        m++;
	        			 }
	        		 
	        		 
	        		 
	        		  XSSFRow row = rrSheet1.createRow(rowNo);
				            int c = 0;
				            
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(k);				            
				       
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getContract_status());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getContract_id());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getContract_name());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getContractor_name());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getLoa_date());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getOriginal_completion_date());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getRevised_date_of_completion());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getPercent_progress());
					        rowNo++;
					        k++;
	        	 }
	        	 }
		}
				  for(int columnIndex = 1; columnIndex < headerStringArr.length; columnIndex++) {
				  	rrSheet1.setColumnWidth(columnIndex, 25 * 150);
				  	//rrSheet1.autoSizeColumn(columnIndex);
				  	rrSheet1.setColumnWidth(4, 35 * 235);
					rrSheet1.setColumnWidth(5, 35 * 235);
				   }

            /*******************************************************************************/
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
            Date date = new Date();
            String fileName = "Date_of_Completion_Report_"+dateFormat.format(date);
            
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
                logger.error("generateContractCompletionReport : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportInvalid);
            }catch(IOException e){
                e.printStackTrace();
                logger.error("generateContractCompletionReport : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportError);
            }
	      }
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generateContractCompletionReport : " + e.getMessage());
		}
    
	}	

	private boolean generatContractReportForAll(HttpServletResponse response, String currentDate, Contract obj) {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			
			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date()); 
			
			Map<String,List<Contract>> list = contractReportService.getContractsListForReport(obj);
			
			boolean landscape = true;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.CENTER;
			

			String headerTextRight = "";
			
			String headerTextMiddle = "List of All Contracts";			


			int tabs1 = 8;int tabs2 = 5;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);			 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForContractReportAll(wordMLPackage, mp, factory,list,report_created_date);
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				InputStream targetStream = new ByteArrayInputStream(byteArray);
				String FILE_EXTENSION = ".docx";
				String fileName = "List of Contracts Report - " + currentDate + FILE_EXTENSION;
				
				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				response.setContentType("application/pdf");
				response.setContentType("application/msword");
				response.setContentType("application/vnd.ms-word");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				//copies all bytes from a file to an output stream
				IOUtils.copy(targetStream, response.getOutputStream());
				//flushes output stream
				response.getOutputStream().flush();
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }	
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractReport >> " + e.getMessage());
			flag = false;
		}
		
		return flag;
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
        font.setFontName(fontName);  //"Calibri"
        
        font.setItalic(isItalicText); 
        font.setBold(isBoldText);
        // Applying font to the style  
        style.setFont(font); 
        
        return style;
	}
	
	@RequestMapping(value = "/generate-list-of-contracts-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	private void generateListofContractsReport(HttpServletResponse response, String currentDate, Contract obj) {
		try{
			
			SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-YY");
			
			Contract reportData = contractReportService.generateListofContractsReport(obj);
			
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
	        CellStyle greenStyle1 = cellFormating(workBook,greenRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle bluetyle = cellFormating(workBook,blueRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
	        CellStyle blueGreentyle = cellFormating(workBook,blueRGB,HorizontalAlignment.LEFT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);

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
          
	        if(!(StringUtils.isEmpty(reportData))){
	        	
       		 	XSSFSheet rrSheet1 = workBook.createSheet(WorkbookUtil.createSafeSheetName("List of Contracts Report"));
		        XSSFRow headRow = rrSheet1.createRow(0);
		        
		        Cell cell = headRow.createCell(0);
		        
		        XSSFRow mainHeadingRow = rrSheet1.createRow(1);
		        
		        cell = mainHeadingRow.createCell(0);
		        cell.setCellStyle(bluetyle);
		        cell.setCellValue("List of Contracts Report");
		        
		        for (int i = 1; i < 12; i++) {		        	
			        cell = mainHeadingRow.createCell(i);
			        cell.setCellStyle(bluetyle);
					cell.setCellValue("");
				}	
		        rrSheet1.addMergedRegion(new CellRangeAddress(1, 1, 0,13));
		int rowNo = 3;

        XSSFRow structureRow = rrSheet1.createRow(rowNo);

        /**********************************************************************/
		String headerString = "S. No.^Status of Contract^Contract ID^Contract Short Name^Contractor Name^LOA Date^Original Completion Date^Revised Completion Date^Progress %^Estimated Cost (Cr)^Awarded Cost (Cr)^Revised Cost (Cr)^Expenditure (Cr) ";
        String[] headerStringArr = headerString.split("\\^");
        
        XSSFRow headingRow = rrSheet1.createRow(rowNo);
        for (int i = 0; i < headerStringArr.length ; i++) {	
        	
    			 cell = headingRow.createCell(i);
    	    	 cell.setCellStyle(whiteStyle);
				 cell.setCellValue(headerStringArr[i]);
		}	
        
        List<String> WorksList = new ArrayList<>(); 
        
        for (Contract zObj : reportData.getReport1List())  
    	{
    		if(WorksList.indexOf(zObj.getWork_short_name())==-1)
    		{
    			WorksList.add(zObj.getWork_short_name());
    		}
    	}       
        rowNo++;
        int x = 0,z=0;

		for (int i2 = 0; i2 < WorksList.size(); i2++) 
		{   
			
			int k=1;
			int m=0;
	        	 for (Contract zObj : reportData.getReport1List()) { 
	        		 
	        		 
	        		 if(WorksList.get(i2).compareTo(zObj.getWork_short_name())==0)
	        		 {
	        			 if(m==0) 
	        			 {
			     	        XSSFRow headWorkRow = rrSheet1.createRow(rowNo);
			    	        cell = headWorkRow.createCell(0);
			    	        cell.setCellStyle(bluetyle);
			    	        cell.setCellValue("Work:");
			    	        
			    	        cell = headWorkRow.createCell(1);
			    	        cell.setCellStyle(blueGreentyle);
			    	        cell.setCellValue(WorksList.get(i2));			    	        
			    	        
			    	        for (int i = 2; i < 12; i++) {		        	
			    		        cell = mainHeadingRow.createCell(i);
			    		        cell.setCellStyle(bluetyle);
			    				cell.setCellValue("");
			    			}	
			    	        rrSheet1.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 1,12));       
			    	        
			    	        rowNo++;
			    	        m++;
	        			 }
	        		 
	        		 
	        		 
	        		  XSSFRow row = rrSheet1.createRow(rowNo);
				            int c = 0;
				            
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(k);				            
				       
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getContract_status());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getContract_id());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getContract_name());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getContractor_name());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getLoa_date());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getOriginal_completion_date());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getRevised_date_of_completion());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getPercent_progress());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getEstimated_cost());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getAwarded_cost());
						
						    cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getRevised_cost());
							
							cell = row.createCell(c++);
							cell.setCellStyle(activityNameStyle);
							cell.setCellValue(zObj.getExpenditure());
							
					        rowNo++;
					        k++;
	        	 }
	        	 }
		}
				  for(int columnIndex = 1; columnIndex < headerStringArr.length; columnIndex++) {
				  	rrSheet1.setColumnWidth(columnIndex, 25 * 150);
				  	//rrSheet1.autoSizeColumn(columnIndex);
				  	rrSheet1.setColumnWidth(4, 35 * 235);
					rrSheet1.setColumnWidth(5, 35 * 235);
				   }

            /*******************************************************************************/
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
            Date date = new Date();
            String fileName = "List_of_Contracts_Report_"+dateFormat.format(date);
            
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
                logger.error("generateListofContractsReport : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportInvalid);
            }catch(IOException e){
                e.printStackTrace();
                logger.error("generateListofContractsReport : " + e.getMessage());
                //attributes.addFlashAttribute("error",dataExportError);
            }
	      }
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generateListofContractsReport : " + e.getMessage());
		}
    
	}
	
	@RequestMapping(value = "/generate-contract-doc-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView generatContractDocReport(@ModelAttribute Contract obj,HttpServletRequest request,HttpServletResponse response,HttpSession session, RedirectAttributes attributes){
		ModelAndView model = new ModelAndView("redirect:/contract-report/{id}");
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
            String currentDate = sqlDate.format(date);
	           
            obj.setDate(DateParser.parse(obj.getDate()));
            
			boolean flag = generatContractDocReport(response,currentDate,obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractDocReport : " + e.getMessage());
		}
		return model;
    }
	
	@RequestMapping(value = "/generate-contract-doc-bg-insurance-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView generatContractDocBgInsuranceReport(@PathVariable("id") String id,@ModelAttribute Contract obj,HttpServletRequest request,HttpServletResponse response,HttpSession session, RedirectAttributes attributes){
		ModelAndView model = new ModelAndView("redirect:/contract-report/"+id);
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
            String currentDate = sqlDate.format(date);
	           
            obj.setDate(DateParser.parse(obj.getDate()));
            
			boolean flag = generatContractDocBGInsuranceReport(response,currentDate,obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractDocBGInsuranceReport : " + e.getMessage());
		}
		return model;
    }
	
	@RequestMapping(value = "/generate-contract-bg-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView generatContractBankGuaranteeReport(@ModelAttribute Contract obj,HttpServletRequest request,HttpServletResponse response,HttpSession session, RedirectAttributes attributes){
		ModelAndView model = new ModelAndView("redirect:/contract-report/{id}");
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
            String currentDate = sqlDate.format(date);
	           
            obj.setDate(DateParser.parse(obj.getDate()));
            
			boolean flag = generatContractBankGuaranteeReport(response,currentDate,obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractBankGuaranteeReport : " + e.getMessage());
		}
		return model;
    }
	
	@RequestMapping(value = "/generate-contract-insurance-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView generatContractInsuranceReport(@ModelAttribute Contract obj,HttpServletRequest request,HttpServletResponse response,HttpSession session, RedirectAttributes attributes){
		ModelAndView model = new ModelAndView("redirect:/contract-report/{id}");
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
            String currentDate = sqlDate.format(date);
	           
            obj.setDate(DateParser.parse(obj.getDate()));
            
			boolean flag = generatContractInsuranceReport(response,currentDate,obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractInsuranceReport : " + e.getMessage());
		}
		return model;
    }
	
	@RequestMapping(value = "/generate-contract-detail-report/{id}", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView generatContractDetailReport(@ModelAttribute Contract obj,HttpServletRequest request,HttpServletResponse response,HttpSession session, RedirectAttributes attributes){
		ModelAndView model = new ModelAndView("redirect:/contract-report/{id}");
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
            String currentDate = sqlDate.format(date);
	           
            obj.setDate(DateParser.parse(obj.getDate()));
            
			boolean flag = generatContractDetailReport(response,currentDate,obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractDetailReport : " + e.getMessage());
		}
		return model;
    }

	private boolean generatContractDetailReport(HttpServletResponse response, String currentDate, Contract obj) {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			
			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date()); 
			
			Contract contractDetails = contractReportService.getContractDetailsForReport(obj);
			Contract progressDetailsAsOnDate = contractReportService.getProgressDetailsAsOnDate(obj);
			List<Contract> milestoneDetails = contractReportService.getMilestoneDetailsForReport(obj);
			List<Contract> bgDetails = contractReportService.getBGDetailsForReport(obj);
			List<Contract> insuranceDetails = contractReportService.getInsuranceDetailsForReport(obj);			
			Contract contractClosureDetails = contractReportService.getContractClosureDetails(obj);
			Contract contractorDetails = contractReportService.getContractorDetails(obj);
			List<Contract> keyPersonnels = contractReportService.getKeyPersonnelForReport(obj);
			
			boolean landscape = false;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.CENTER;
			
			String headerTextMiddle = "PMIS Report - Contract Detail";
			
			String headerTextRight = report_created_date;
			
			//String headerText = "PMIS Report - Contract Details";
			
			int tabs1 = 4;int tabs2 = 2;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);			 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForContractDetailsReport(wordMLPackage, mp, factory,
					contractDetails,progressDetailsAsOnDate,milestoneDetails,bgDetails,insuranceDetails,contractClosureDetails,
					contractorDetails,keyPersonnels);
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				InputStream targetStream = new ByteArrayInputStream(byteArray);
				String FILE_EXTENSION = ".docx";
				String fileName = "Contract Detail Report - " + currentDate + FILE_EXTENSION;
				
				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				response.setContentType("application/pdf");
				response.setContentType("application/msword");
				response.setContentType("application/vnd.ms-word");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				//copies all bytes from a file to an output stream
				IOUtils.copy(targetStream, response.getOutputStream());
				//flushes output stream
				response.getOutputStream().flush();
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }	
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractReport >> " + e.getMessage());
			flag = false;
		}
		
		return flag;
	}


	private boolean generatContractReport(HttpServletResponse response, String currentDate, Contract obj) {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			
			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date()); 
			
			Map<String,List<Contract>> list = contractReportService.getContractsListForReport(obj);
			
			boolean landscape = true;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.CENTER;
			

			String headerTextRight = "";
			String appendHeader = "";
			
			if(obj.getStatus()==null)
			{
				
				headerTextRight="";
				appendHeader="All";
			}
			if (!StringUtils.isEmpty(obj.getStatus())) 
			{
				String Diff=obj.getStatus();
				String ComVal="Open";
				String ComValCl="Closed";
				String ComValYet="Yet to be Awarded";
				
				if(Diff.compareTo(ComVal)==0)
				{
				
					headerTextRight="OPEN CONTRACTS";
					appendHeader="Open";
				}
				else if(Diff.compareTo(ComValCl)==0)
				{
					
					headerTextRight="CLOSED CONTRACTS";
					appendHeader="Closed";
				}
				else if(Diff.compareTo(ComValYet)==0)
				{
					
					headerTextRight="YET TO BE AWARDED CONTRACTS";
					appendHeader="Yet to be Awarded";
				}
			}
			
			String headerTextMiddle = "List of "+appendHeader+" Contracts";			


			int tabs1 = 8;int tabs2 = 5;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);			 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForContractReport(wordMLPackage, mp, factory,list,report_created_date,obj.getStatus());
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				InputStream targetStream = new ByteArrayInputStream(byteArray);
				String FILE_EXTENSION = ".docx";
				String fileName = "List of Contracts Report - " + currentDate + FILE_EXTENSION;
				
				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				response.setContentType("application/pdf");
				response.setContentType("application/msword");
				response.setContentType("application/vnd.ms-word");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				//copies all bytes from a file to an output stream
				IOUtils.copy(targetStream, response.getOutputStream());
				//flushes output stream
				response.getOutputStream().flush();
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }	
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractReport >> " + e.getMessage());
			flag = false;
		}
		
		return flag;
	}
	
	private boolean generatContractBankGuaranteeReport(HttpServletResponse response, String currentDate, Contract obj) {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			
			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date()); 
			Map<String,List<Contract>> list = contractReportService.getContractsBankGuaranteeForReport(obj);
			
			boolean landscape = false;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.CENTER;
			
			String headerTextRight = "";
			if(obj.getDate()!=null && obj.getDate()!="")
			{
				
				headerTextRight="(Expiry by "+DateParser.parseToIndianDateFormatWithDot(obj.getDate())+")";
			}			
			
			String headerTextMiddle = "Contract BG Validity Report";
			
			//String headerTextRight = report_created_date;
			
			//String headerText = "PMIS Report - Status of Contract BG";
			
			int tabs1 = 8;int tabs2 = 4;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);				 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForContractBGReport(wordMLPackage, mp, factory,list,report_created_date);
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				InputStream targetStream = new ByteArrayInputStream(byteArray);
				String FILE_EXTENSION = ".docx";
				String fileName = "Contract BG Report - " + currentDate + FILE_EXTENSION;
				
				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				response.setContentType("application/pdf");
				response.setContentType("application/msword");
				response.setContentType("application/vnd.ms-word");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				//copies all bytes from a file to an output stream
				IOUtils.copy(targetStream, response.getOutputStream());
				//flushes output stream
				response.getOutputStream().flush();
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractBankGuaranteeReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }	
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractBankGuaranteeReport >> " + e.getMessage());
			flag = false;
		}
		
		return flag;
	}
	
	
	public boolean generatContractDocBGInsuranceReportAutoEmail(String currentDate, Contract obj, Map<String, List<Contract>> list, String recipients, String report_created_date) throws Exception {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			           
			boolean landscape = false;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.CENTER;
			
			String headerTextRight = "";
			if(obj.getDate()!=null && obj.getDate()!="")
			{
				
				headerTextRight="(Expiry by "+DateParser.parseToIndianDateFormatWithDot(obj.getDate())+")";
			}			
			
			String headerTextMiddle = "Contract DOC, BG and Insurance Validity Report for Works in Progress";
			
			//String headerTextRight = report_created_date;
			
			//String headerText = "PMIS Report - Status of Contract BG";
			
			int tabs1 = 8;int tabs2 = 4;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);				 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForContractDocBGInsuranceReport(wordMLPackage, mp, factory,list,report_created_date);
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				
				String file_extention = "docx";
				String docbginsurance_file_name = "Contract-DOC-BG-and-Insurance-Validity-Report-for-Works-in-Progress";

				String cc = "", bcc = CommonConstants.BCC_MAIL,
						subject = "PMIS - Contract DOC, BG and Insurance Validity Report for Works in Progress", body = "";

				if (!StringUtils.isEmpty(recipients)) {
					EMailSender emailSender = new EMailSender();
					emailSender.sendEmailWithContractReportsAttachment(recipients, cc, bcc, subject, body,
							docbginsurance_file_name, file_extention, byteArray);
				}
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractDocBGInsuranceReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractDocBGInsuranceReport >> " + e.getMessage());
			flag = false;
			throw new Exception(e);
		}
		
		return flag;
	}
	
	
	private boolean generatContractDocBGInsuranceReport(HttpServletResponse response, String currentDate, Contract obj) throws Exception {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			
			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date()); 
			Map<String,List<Contract>> list = contractReportService.getContractsDocBGInsuranceForReport(obj);
			
			boolean landscape = false;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.CENTER;
			
			String headerTextRight = "";
			if(obj.getDate()!=null && obj.getDate()!="")
			{
				
				headerTextRight="(Expiry by "+DateParser.parseToIndianDateFormatWithDot(obj.getDate())+")";
			}			
			
			String headerTextMiddle = "Contract DOC, BG and Insurance Validity Report";
			
			//String headerTextRight = report_created_date;
			
			//String headerText = "PMIS Report - Status of Contract BG";
			
			int tabs1 = 8;int tabs2 = 4;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);				 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForContractDocBGInsuranceReport(wordMLPackage, mp, factory,list,report_created_date);
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				InputStream targetStream = new ByteArrayInputStream(byteArray);
				String FILE_EXTENSION = ".docx";
				String fileName = "Contract DOC BG and Insurance Validity Report - " + currentDate + FILE_EXTENSION;
				
				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				response.setContentType("application/pdf");
				response.setContentType("application/msword");
				response.setContentType("application/vnd.ms-word");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				//copies all bytes from a file to an output stream
				IOUtils.copy(targetStream, response.getOutputStream());
				//flushes output stream
				response.getOutputStream().flush();
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractDocBGInsuranceReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }	
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractDocBGInsuranceReport >> " + e.getMessage());
			flag = false;
			throw new Exception(e);
		}
		
		return flag;
	}
	
	
	private boolean generatContractDocReport(HttpServletResponse response, String currentDate, Contract obj) {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			
			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date()); 
			Map<String,List<Contract>> list = contractReportService.getContractsDocReport(obj);
			
			boolean landscape = false;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.CENTER;
			
			String headerTextRight = "";
			if(obj.getDate()!=null && obj.getDate()!="")
			{
				
				headerTextRight="(Expiry by "+DateParser.parseToIndianDateFormatWithDot(obj.getDate())+")";
			}
			
			String headerTextMiddle = "Contract DOC Validity Report";
			
			//String headerTextRight = report_created_date;
			
			
			//String headerText = "PMIS Report - Status of Contract BG";
			
			int tabs1 = 8;int tabs2 = 4;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);				 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForContractDocReport(wordMLPackage, mp, factory,list,report_created_date);
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				InputStream targetStream = new ByteArrayInputStream(byteArray);
				String FILE_EXTENSION = ".docx";
				String fileName = "Contract DOC Validity Report - " + currentDate + FILE_EXTENSION;
				
				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				response.setContentType("application/pdf");
				response.setContentType("application/msword");
				response.setContentType("application/vnd.ms-word");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				//copies all bytes from a file to an output stream
				IOUtils.copy(targetStream, response.getOutputStream());
				//flushes output stream
				response.getOutputStream().flush();
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractDocReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }	
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractDocReport >> " + e.getMessage());
			flag = false;
		}
		
		return flag;
	}
	
	private boolean generatContractBGExpiryLetterReport(HttpServletResponse response, String currentDate, Contract obj) {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			
			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date()); 
			List<Contract> list = contractReportService.getContractDownload(obj);
			
			boolean landscape = false;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.LEFT;
			
			String headerTextRight = "";

				
			headerTextRight=CommonConstants2.DOCX_LOGO + "/" + "logocom.png";
						
			
			String headerTextMiddle = "BG Expiry Letter For "+obj.getContract_id();
			
			//String headerTextRight = report_created_date;
			
			//String headerText = "PMIS Report - Status of Contract Insurance";
			
			int tabs1 = 8;int tabs2 = 3;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);			 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForListofContractsClosertoBGExpiryDateReport(wordMLPackage, mp, factory,list,report_created_date);
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				InputStream targetStream = new ByteArrayInputStream(byteArray);
				String FILE_EXTENSION = ".docx";
				String fileName = "BG Expiry Letter For - "+obj.getContract_id()+" on " + currentDate + FILE_EXTENSION;
				
				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				response.setContentType("application/pdf");
				response.setContentType("application/msword");
				response.setContentType("application/vnd.ms-word");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				//copies all bytes from a file to an output stream
				IOUtils.copy(targetStream, response.getOutputStream());
				//flushes output stream
				response.getOutputStream().flush();
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractBGExpiryLetterReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }	
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractBGExpiryLetterReport >> " + e.getMessage());
			flag = false;
		}
		
		return flag;
	}
	
	private boolean generatContractInsuranceReport(HttpServletResponse response, String currentDate, Contract obj) {
		//XWPFDocument document = new XWPFDocument(); 
		//StringBuilder repositoryExcerpts = new StringBuilder(); 
		byte[] byteArray;        
        //ObjectFactory objectFactory = new ObjectFactory();
		boolean flag = false;
		try{			
			//DateFormat df = new SimpleDateFormat("dd-MMM-YYYY HH:mm"); 
			DateFormat df = new SimpleDateFormat("dd-MM-YYYY hh:mm aa");
			String report_created_date = df.format(new Date()); 
			Map<String,List<Contract>> list = contractReportService.getContractsInsuranceForReport(obj);
			
			boolean landscape = false;
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4, landscape);
			
			MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
			ObjectFactory factory = Context.getWmlObjectFactory();
			
			String imagePath = CommonConstants2.DOCX_LOGO + "/" + "report_logo_mrvc.png";

			JcEnumeration imageAlignment = JcEnumeration.CENTER;
			
			String headerTextRight = "";
			if(obj.getDate()!=null && obj.getDate()!="")
			{
				
				headerTextRight="(Expiry by "+DateParser.parseToIndianDateFormatWithDot(obj.getDate())+")";
			}			
			
			String headerTextMiddle = "Contract Insurance Validity Report";
			
			//String headerTextRight = report_created_date;
			
			//String headerText = "PMIS Report - Status of Contract Insurance";
			
			int tabs1 = 8;int tabs2 = 3;
			
			Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2);
			//Relationship relationship = createHeaderPart(wordMLPackage, mp, factory,headerText);			 
			createHeaderReference(wordMLPackage, mp, factory, relationship);
			relationship = createFooterPageNumPart(wordMLPackage, mp, factory);
			createFooterReference(wordMLPackage, mp, factory, relationship);
			 			  
			DocxTableCreationForContractReport.createTableForContractInsuranceReport(wordMLPackage, mp, factory,list,report_created_date);
	    	  
						
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){	
				wordMLPackage.save(bos);
				byteArray = bos.toByteArray();
				InputStream targetStream = new ByteArrayInputStream(byteArray);
				String FILE_EXTENSION = ".docx";
				String fileName = "Contract Insurance Report - " + currentDate + FILE_EXTENSION;
				
				response.setContentType("application/.csv");
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setContentType("application/vnd.ms-excel");
				response.setContentType("application/pdf");
				response.setContentType("application/msword");
				response.setContentType("application/vnd.ms-word");
				// add response header
				response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
				//copies all bytes from a file to an output stream
				IOUtils.copy(targetStream, response.getOutputStream());
				//flushes output stream
				response.getOutputStream().flush();
				
				flag = true;
		    }catch (Exception e) {
				e.printStackTrace();
				logger.error("generatContractInsuranceReport >> FileNotFoundException occurs.." + e.getMessage());
				flag = false;
		    }	
		 	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("generatContractInsuranceReport >> " + e.getMessage());
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * Creates a WordprocessingMLPackage, using default page size and orientation.
	 * From 2.7.1, these are read from docx4j.properties, or if not found, default
	 * to A4 portrait.
	 * 
	 * The WordprocessingMLPackage contains a MainDocumentPart (with content), 
	 * Styles part, DocPropsCorePart part, and DocPropsExtendedPart.
	 */
	public static WordprocessingMLPackage createPackage() throws InvalidFormatException {
		
		String papersize= Docx4jProperties.getProperties().getProperty("docx4j.PageSize", "A4");
		logger.info("Using paper size: " + papersize);
		
		String landscapeString = Docx4jProperties.getProperties().getProperty("docx4j.PageOrientationLandscape", "false");
		boolean landscape= Boolean.parseBoolean(landscapeString);
		logger.info("Landscape orientation: " + landscape);
		
		return createPackage(
				PageSizePaper.valueOf(papersize), landscape); 
	}
	
	/**
	 * Creates a WordprocessingMLPackage, containing a MainDocumentPart (with content), 
	 * Styles part, DocPropsCorePart part, and DocPropsExtendedPart.
	 * 
	 * The content contains sectPr specifying paper size and orientation.
	 * 
	 * @param sz
	 * @param landscape
	 * @return
	 * @throws InvalidFormatException
	 */
	public static WordprocessingMLPackage createPackage(PageSizePaper sz, boolean landscape ) throws InvalidFormatException {
		
				
		// Create a package
		WordprocessingMLPackage wmlPack = new WordprocessingMLPackage();

		// Create main document part
		MainDocumentPart wordDocumentPart = new MainDocumentPart();		
		
		// Create main document part content
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		org.docx4j.wml.Body  body = factory.createBody();		
		org.docx4j.wml.Document wmlDocumentEl = factory.createDocument();
		
		wmlDocumentEl.setBody(body);
		
		// Create a basic sectPr using our Page model
		PageDimensions page = new PageDimensions();
		page.setPgSize(sz, landscape);
		
		SectPr sectPr = factory.createSectPr();
		body.setSectPr(sectPr);
		sectPr.setPgSz(  page.getPgSz() );
		sectPr.setPgMar( page.getPgMar() );
				
		// Put the content in the part
		wordDocumentPart.setJaxbElement(wmlDocumentEl);
						
		// Add the main document part to the package relationships
		// (creating it if necessary)
		wmlPack.addTargetPart(wordDocumentPart);
				
		// Create a styles part
		Part stylesPart = new org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart();
		try {
			((org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart) stylesPart)
					.unmarshalDefaultStyles();
			
			// Add the styles part to the main document part relationships
			// (creating it if necessary)
			wordDocumentPart.addTargetPart(stylesPart); // NB - add it to main doc part, not package!			
			
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();	
			logger.error(e.getMessage(), e);
		}
		
		// Metadata: docx4j 2.7.1 can populate some of this from docx4j.properties
		// See SaveToZipFile
		DocPropsCorePart core = new DocPropsCorePart();
		org.docx4j.docProps.core.ObjectFactory coreFactory = new org.docx4j.docProps.core.ObjectFactory();
		core.setJaxbElement(coreFactory.createCoreProperties() );
		wmlPack.addTargetPart(core);
			
		
		DocPropsExtendedPart app = new DocPropsExtendedPart();
		org.docx4j.docProps.extended.ObjectFactory extFactory = new org.docx4j.docProps.extended.ObjectFactory();
		app.setJaxbElement(extFactory.createProperties() );
		wmlPack.addTargetPart(app);	
		
    	// DocumentSettingsPart (/word/settings.xml)
        // Set overrideTableStyleFontSizeAndJustification to true,
        // like Word 2010/2013/2016.  Since v3.3.0
    	DocumentSettingsPart dsp = new DocumentSettingsPart();
    	wmlPack.getMainDocumentPart().addTargetPart(dsp);
    	dsp.setJaxbElement(new CTSettings());
    	dsp.setOverrideTableStyleFontSizeAndJustification(true);
				
		// Return the new package
		return wmlPack;
		
	}
	

	public void setDocSectionBreak(WordprocessingMLPackage wordPackage,String orientation){
	      try{
	         SectPr sectPr=null;
	         ObjectFactory objectFactory = new ObjectFactory();
	         sectPr = objectFactory.createSectPr();
	    	  
	    	  
	         PgSz landscape = new PgSz(); 
	          
	         if("landscape".equalsIgnoreCase(orientation)){
	        	 landscape.setH(BigInteger.valueOf(11906));
		         landscape.setW(BigInteger.valueOf(16383));		          
		         /*sectPr = (org.docx4j.wml.SectPr)org.docx4j.XmlUtils.unmarshalString("<w:sectPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
	                    + "<w:pgSz w:w=\"16839\" w:h=\"11907\" w:orient=\""+temp.orientation+"\"/>"
	                    + "</w:sectPr>");*/
	             landscape.setOrient(STPageOrientation.LANDSCAPE);   
	         }else{
	             /*sectPr = (org.docx4j.wml.SectPr)org.docx4j.XmlUtils.unmarshalString("<w:sectPr xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
	                          + "<w:pgSz w:w=\"16839\" w:h=\"11907\" />"                          + "</w:sectPr>");*/
	             landscape.setOrient(STPageOrientation.PORTRAIT);
	         }
	         sectPr.setPgSz(landscape);
	         P p = objectFactory.createP();
	         PPr createPPr = objectFactory.createPPr();
	         createPPr.setSectPr(sectPr);
	         p.setPPr(createPPr);
	         
	         /*wordPackage.getMainDocumentPart().addAltChunk(
	               org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType.Html,
	               sectionContent.toString().getBytes());*/
	         /*org.docx4j.wml.Br objBr = new org.docx4j.wml.Br();
	         objBr.setType(org.docx4j.wml.STBrType.PAGE);
	         wordPackage.getMainDocumentPart().addObject(objBr);*/
	         
	         wordPackage.getMainDocumentPart().addObject(p);
	    }catch (Exception e) {
			e.printStackTrace();
		}
	      
	}
	
	 private void changeOrientation(MainDocumentPart mdp, STPageOrientation stPageOrientation) {
          ObjectFactory objectFactory = new ObjectFactory();
          SectPr sectionLandscape = objectFactory.createSectPr();
          SectPr.PgSz landscape = new SectPr.PgSz();
          landscape.setOrient(stPageOrientation);
          landscape.setH(BigInteger.valueOf(11906));
          landscape.setW(BigInteger.valueOf(16383));
          sectionLandscape.setPgSz(landscape);
          org.docx4j.wml.P p = objectFactory.createP();
          PPr createPPr = objectFactory.createPPr();
          createPPr.setSectPr(sectionLandscape);
          p.setPPr(createPPr);
          mdp.addObject(p);
    }

    public static P newImage( WordprocessingMLPackage wordMLPackage, byte[] bytes, 
            String filenameHint, String altText, int id1, int id2) throws Exception {
    	
    	Integer HEADER_TOP_OFFSET = 0;
    	
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
        Inline inline = imagePart.createImageInline( filenameHint, altText, id1, id2,8500,false);
        
        /*String anchorXml = XmlUtils.marshaltoString(inline, true, false, Context.jc, Namespaces.NS_WORD12, "anchor",
                Inline.class);
           
             org.docx4j.dml.ObjectFactory dmlFactory = new org.docx4j.dml.ObjectFactory();
            org.docx4j.dml.wordprocessingDrawing.ObjectFactory wordDmlFactory = new org.docx4j.dml.wordprocessingDrawing.ObjectFactory();

        
        Anchor anchor = (Anchor) XmlUtils.unmarshalString(anchorXml, Context.jc, Anchor.class);
        
        anchor.setSimplePos(dmlFactory.createCTPoint2D());
       anchor.getSimplePos().setX(20L);
       anchor.getSimplePos().setY(50L);
       anchor.setSimplePosAttr(false);
       anchor.setPositionH(wordDmlFactory.createCTPosH());
       anchor.getPositionH().setAlign(STAlignH.CENTER);
       anchor.getPositionH().setRelativeFrom(STRelFromH.MARGIN);
       anchor.setPositionV(wordDmlFactory.createCTPosV());
       anchor.getPositionV().setPosOffset(HEADER_TOP_OFFSET);
       anchor.getPositionV().setRelativeFrom(STRelFromV.PAGE);
       anchor.setWrapNone(wordDmlFactory.createCTWrapNone());*/
       
        
        ObjectFactory factory = new ObjectFactory();
        P  p = factory.createP();
        R  run = factory.createR();
        p.getContent().add(run);
        Drawing drawing = factory.createDrawing();      
        run.getContent().add(drawing);       
        drawing.getAnchorOrInline().add(inline);
        
        PPr pPr = p.getPPr();
		if (pPr == null) {
			pPr = factory.createPPr();
		}
		Jc jc = pPr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		jc.setVal(JcEnumeration.CENTER);
		pPr.setJc(jc);
		p.setPPr(pPr);

        return p;
    } 
    
    private static void addPageBreak(MainDocumentPart documentPart) {
		 ObjectFactory objectFactory = new ObjectFactory();
	        P paragraph = objectFactory.createP();
	        R run = objectFactory.createR();
	        P p = objectFactory.createP();
	        // Create object for r
	        R r = objectFactory.createR();
	        p.getContent().add(r);
	        // Create object for br
	        Br br = objectFactory.createBr();
	        r.getContent().add(br);
	        br.setType(org.docx4j.wml.STBrType.PAGE);
	        documentPart.addObject(p);
	}

	private String getAlphabet(int i) {
		 return i < 0 ? "" : getAlphabet((i / 26) - 1) + (char)(65 + i % 26);
	}
	
	
	
	public Relationship createHeaderPart(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory,
			String imagePath, JcEnumeration imageAlignment, String headerTextMiddle, String headerTextRight,int tabs1,int tabs2) throws Exception {
		HeaderPart headerPart = new HeaderPart();
		Relationship rel = t.addTargetPart(headerPart);
		// After addTargetPart, so image can be added properly
		headerPart.setJaxbElement(getHdr(wordprocessingMLPackage, factory,
				headerPart,imagePath,imageAlignment,headerTextMiddle,headerTextRight,tabs1,tabs2));
		return rel;
	}
	
	public Relationship createHeaderPart(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory,String headerText) throws Exception {
		HeaderPart headerPart = new HeaderPart();
		Relationship rel = t.addTargetPart(headerPart);
		// After addTargetPart, so image can be added properly
		headerPart.setJaxbElement(getHdr(wordprocessingMLPackage, factory,
				headerPart,headerText));
		return rel;
	}
	
	public Hdr getHdr(WordprocessingMLPackage wordprocessingMLPackage,
			ObjectFactory factory, HeaderPart sourcePart,
			String imagePath, JcEnumeration imageAlignment, String headerTextMiddle, String headerTextRight,int tabs1,int tabs2) throws Exception {
		Hdr hdr = factory.createHdr();
		//String path = CommonConstants.DOCX_LOGO+"/docx-logo.png";
		//String path = CommonConstants.DOCX_LOGO+"/"+"ircon-report-header.png";
		P p = factory.createP();
		R r = factory.createR();
		if(!StringUtils.isEmpty(imagePath)) {
			File file = new File(imagePath);
			java.io.InputStream is = new java.io.FileInputStream(file);
			
			String filenameHint = null;
	        String altText = null;

	        int id1 = 0;
	        int id2 = 1;
			p = newImage(wordprocessingMLPackage, factory, sourcePart,
					BufferUtil.getBytesFromInputStream(is), altText,
					filenameHint, id1, id2, imageAlignment);
		}
		hdr.getContent().add(p);
		
		/********************************************/
		
		/*p = factory.createP();
		r = factory.createR();
		
		RPr boldRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA,
				true, false, false, false);
		
		
		if(!StringUtils.isEmpty(headerTextMiddle)) {
			for (int i = 0; i < tabs1; i++) {
				R.Tab rtab = factory.createRTab();
		        JAXBElement<org.docx4j.wml.R.Tab> rtabWrapped = factory.createRTab(rtab);
		        r.getContent().add( rtabWrapped);
			}			
			p.getContent().add(r);
		
			Text txt = factory.createText();
			txt.setValue(headerTextMiddle);
			r = factory.createR();
			r.getContent().add(txt);
			r.setRPr(boldRPr);
			p.getContent().add(r);
		}
		
		if(!StringUtils.isEmpty(headerTextRight)) {
			for (int i = 0; i < tabs2; i++) {
				R.Tab rtab = factory.createRTab();
		        JAXBElement<org.docx4j.wml.R.Tab> rtabWrapped = factory.createRTab(rtab);
		        r.getContent().add( rtabWrapped);
			}
			Text txt = factory.createText();
			txt.setValue(headerTextRight);
			r = factory.createR();
			r.getContent().add(txt);
			r.setRPr(boldRPr);
			p.getContent().add(r);
		}
		
		hdr.getContent().add(p);*/	
		
		/*******************************************************************************/

		/*RPr titleRpr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA, true, false, false, false);
		
		Tbl table = factory.createTbl();
		
		TblPr tableProps = new TblPr();
		CTTblLayoutType tblLayoutType = new CTTblLayoutType();
		STTblLayoutType stTblLayoutType = STTblLayoutType.FIXED;
		tblLayoutType.setType(stTblLayoutType);
		tableProps.setTblLayout(tblLayoutType);
		table.setTblPr(tableProps);
		
		Tr titleRow = factory.createTr();
		addTableCell(factory, wordprocessingMLPackage, titleRow, "", titleRpr, JcEnumeration.CENTER, true, "ffffff");
		addTableCell(factory, wordprocessingMLPackage, titleRow, headerTextMiddle, titleRpr, JcEnumeration.CENTER, true,
				"ffffff");
		addTableCell(factory, wordprocessingMLPackage, titleRow, headerTextRight, titleRpr, JcEnumeration.RIGHT, true,
				"ffffff");
		table.getContent().add(titleRow);
		setTableAlign(factory, table, JcEnumeration.CENTER);
		
		hdr.getContent().add(table);*/
		
		/*******************************************************************************/
		
		p = factory.createP();
		r = factory.createR();
		RPr garamondBoldRPr = getRPr(factory, "Garamond", "000000", "28", STHint.EAST_ASIA,
				true, false, false, false);
		
		
		if(!StringUtils.isEmpty(headerTextMiddle)) {
			PPr pPr = p.getPPr();
			if (pPr == null) {
				pPr = factory.createPPr();
			}
			Jc jc = pPr.getJc();
			if (jc == null) {
				jc = new Jc();
			}
			jc.setVal(JcEnumeration.CENTER);
			pPr.setJc(jc);
			p.setPPr(pPr);
			
			Text txt = factory.createText();
			txt.setValue(headerTextMiddle);
			r = factory.createR();
			r.getContent().add(txt);
			r.setRPr(garamondBoldRPr);
			p.getContent().add(r);
		}		
		hdr.getContent().add(p);
		/**************************************************************/
		
		RPr calibriBoldRPr = getRPr(factory, "Calibri (Body)", "000000", "20", STHint.EAST_ASIA,
				true, false, false, false);
		p = factory.createP();
		r = factory.createR();
		if(!StringUtils.isEmpty(headerTextRight)) {
			PPr pPr = p.getPPr();
			if (pPr == null) {
				pPr = factory.createPPr();
			}
			Jc jc = pPr.getJc();
			if (jc == null) {
				jc = new Jc();
			}
			jc.setVal(JcEnumeration.CENTER);
			pPr.setJc(jc);
			p.setPPr(pPr);
			
			Text txt = factory.createText();
			txt.setValue(headerTextRight);
			r = factory.createR();
			r.getContent().add(txt);
			r.setRPr(calibriBoldRPr);
			p.getContent().add(r);
		}
		hdr.getContent().add(p);
		
		/*******************************************************************************/
		
		return hdr;
	}
	
	public void addTableCell(ObjectFactory factory, WordprocessingMLPackage wordMLPackage, Tr tableRow, String content,
			RPr rpr, JcEnumeration jcEnumeration, boolean hasBgColor, String backgroudColor) {
		Tc tableCell = factory.createTc();
		P p = factory.createP();
		setParagraphAlign(factory, p, jcEnumeration);
		//Text t = factory.createText();
		//t.setValue(content);
		R run = factory.createR();
		run.setRPr(rpr);

		//run.getContent().add(t);

		p.getContent().add(run);
		if (content != null) {
			String[] contentArr = content.split("\n");
			Text text = factory.createText();
			text.setSpace("preserve");
			text.setValue(contentArr[0]);
			run.getContent().add(text);

			for (int i = 1, len = contentArr.length; i < len; i++) {
				Br br = factory.createBr();
				run.getContent().add(br);
				text = factory.createText();
				text.setSpace("preserve");
				text.setValue(contentArr[i]);
				run.getContent().add(text);
			}
		}

		TcPr tcPr = tableCell.getTcPr();
		if (tcPr == null) {
			tcPr = factory.createTcPr();
		}

		CTVerticalJc valign = factory.createCTVerticalJc();
		valign.setVal(STVerticalJc.CENTER);
		tcPr.setVAlign(valign);

		//Removing space in cells
		PPr pPr = factory.createPPr();
		Spacing spacing = new Spacing();
		spacing.setBefore(BigInteger.TWO);
		spacing.setAfter(BigInteger.TWO);
		//spacing.setAfterLines(BigInteger.TEN);
		//spacing.setBeforeLines(BigInteger.TEN);
		pPr.setSpacing(spacing);

		Jc justification = factory.createJc();
		justification.setVal(jcEnumeration);
		pPr.setJc(justification);

		p.setPPr(pPr);

		tableCell.getContent().add(p);
		if (hasBgColor) {
			CTShd shd = tcPr.getShd();
			if (shd == null) {
				shd = factory.createCTShd();
			}
			shd.setColor("auto");
			shd.setFill(backgroudColor);
			tcPr.setShd(shd);
		}

		TcBorders tcb = factory.createTcPrInnerTcBorders();
		CTBorder ctb = factory.createCTBorder();
		STBorder stb = STBorder.NONE;
		ctb.setVal(stb);
		tcb.setBottom(ctb);
		tcb.setRight(ctb);
		tcb.setLeft(ctb);
		tcb.setTop(ctb);
		tcPr.setTcBorders(tcb);

		tableCell.setTcPr(tcPr);

		tableRow.getContent().add(tableCell);
	}

	public static void setTableAlign(ObjectFactory factory, Tbl table, JcEnumeration jcEnumeration) {
		TblPr tablePr = table.getTblPr();
		if (tablePr == null) {
			tablePr = factory.createTblPr();
		}
		Jc jc = tablePr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		jc.setVal(jcEnumeration);
		tablePr.setJc(jc);

		TblWidth tblwidth = factory.createTblWidth();
		tblwidth.setW(BigInteger.valueOf(5000)); // 5000 = 100%
		tblwidth.setType("pct");
		tablePr.setTblW(tblwidth);

		table.setTblPr(tablePr);
	}
	
	public Hdr getHdr(WordprocessingMLPackage wordprocessingMLPackage,
			ObjectFactory factory, HeaderPart sourcePart,String headerText) throws Exception {
		Hdr hdr = factory.createHdr();
		
		P p = factory.createP();
		R r = factory.createR();		
		
		RPr boldRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA,
				true, false, false, false);
		
		
		if(!StringUtils.isEmpty(headerText)) {			
			PPr pPr = p.getPPr();
			if (pPr == null) {
				pPr = factory.createPPr();
			}
			Jc jc = pPr.getJc();
			if (jc == null) {
				jc = new Jc();
			}
			jc.setVal(JcEnumeration.CENTER);
			pPr.setJc(jc);
			p.setPPr(pPr);

			Text txt = factory.createText();
			txt.setValue(headerText);
			r = factory.createR();
			r.getContent().add(txt);
			r.setRPr(boldRPr);
			p.getContent().add(r);
		}
		
		
		
		hdr.getContent().add(p);	
		
		return hdr;
	}
	
	public P newImage(WordprocessingMLPackage wordMLPackage,
			ObjectFactory factory, HeaderPart sourcePart, byte[] bytes,
			String filenameHint, String altText, int id1, int id2,
			JcEnumeration jcEnumeration) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage,sourcePart, bytes);
		Inline inline = imagePart.createImageInline(filenameHint, altText, id1,
				id2, false);
		P p = factory.createP();
		R run = factory.createR();
		p.getContent().add(run);
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		PPr pPr = p.getPPr();
		if (pPr == null) {
			pPr = factory.createPPr();
		}
		Jc jc = pPr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		jc.setVal(jcEnumeration);
		pPr.setJc(jc);
		p.setPPr(pPr);
		return p;
	}
	
	public void createHeaderReference(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory, Relationship relationship)
			throws InvalidFormatException {
		List<SectionWrapper> sections = wordprocessingMLPackage
				.getDocumentModel().getSections();
		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = factory.createSectPr();
			t.addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}
		/*HeaderReference headerReference = factory.createHeaderReference();
		headerReference.setId(relationship.getId());
		headerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(headerReference);*/
		
		HeaderReference headerReference = factory.createHeaderReference();
		headerReference = factory.createHeaderReference();
		headerReference.setId(relationship.getId());
		//headerReference.setType(HdrFtrRef.DEFAULT);
		headerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(headerReference);
		
		/*headerReference = factory.createHeaderReference();
		headerReference.setId(relationship.getId());
		//headerReference.setType(HdrFtrRef.DEFAULT);
		headerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(headerReference);*/
		
		BooleanDefaultTrue value = new BooleanDefaultTrue();
        value.setVal(Boolean.TRUE);
        sectPr.setTitlePg(value);        
	}
	
	public void createHeaderReferenceRepeatHeader(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory, Relationship relationship)
			throws InvalidFormatException {
		List<SectionWrapper> sections = wordprocessingMLPackage
				.getDocumentModel().getSections();
		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = factory.createSectPr();
			t.addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}
		/*HeaderReference headerReference = factory.createHeaderReference();
		headerReference.setId(relationship.getId());
		headerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(headerReference);*/
		
		
		  HeaderReference headerReference = factory.createHeaderReference();
		  headerReference = factory.createHeaderReference();
		  headerReference.setId(relationship.getId());
		  headerReference.setType(HdrFtrRef.DEFAULT);
		  //headerReference.setType(HdrFtrRef.FIRST);
		  sectPr.getEGHdrFtrReferences().add(headerReference);
		 
		
		BooleanDefaultTrue value = new BooleanDefaultTrue();
        value.setVal(Boolean.TRUE);
        sectPr.setTitlePg(value);        
	}
	
	public void addPageBreak(WordprocessingMLPackage wordMLPackage,
			ObjectFactory factory) {
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
		Br breakObj = new Br();
		breakObj.setType(STBrType.PAGE);
		P paragraph = factory.createP();
		paragraph.getContent().add(breakObj);
		documentPart.addObject(paragraph);
	}
	
	
	public void addEmptyParagraph(WordprocessingMLPackage wordMLPackage,
			MainDocumentPart t, ObjectFactory factory) throws Exception {
		RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA,
				true, false, false, false);
		P paragraph = factory.createP();
		setParagraphAlign(factory, paragraph, JcEnumeration.CENTER);
		Text txt = factory.createText();
		txt.setValue("");
		R run = factory.createR();
		run.getContent().add(txt);
		run.setRPr(titleRPr);
		paragraph.getContent().add(run);
		t.addObject(paragraph);
	}
	
	
	public void addHeading(WordprocessingMLPackage wordMLPackage,
			MainDocumentPart t, ObjectFactory factory,String contentValue) throws Exception {
		RPr titleRPr = getRPr(factory, "Calibri", "000000", "28", STHint.EAST_ASIA,
				true, true, false, false);
		RPr boldRPr = getRPr(factory, "Calibri", "000000", "22", STHint.EAST_ASIA,
				true, false, false, false);
		RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA,
				false, false, false, false);		
		
		P paragraph = factory.createP();
		setParagraphAlign(factory, paragraph, JcEnumeration.CENTER);
		Text txt = factory.createText();
		txt.setValue(contentValue);
		R run = factory.createR();
		run.getContent().add(txt);
		run.setRPr(titleRPr);
		paragraph.getContent().add(run);
		t.addObject(paragraph);		
	}
	
	
	public RPr getRPr(ObjectFactory factory, String fontFamily,
			String colorVal, String fontSize, STHint sTHint, boolean isBlod,
			boolean isUnderLine, boolean isItalic, boolean isStrike) {
		RPr rPr = factory.createRPr();
		RFonts rf = new RFonts();
		rf.setHint(sTHint);
		rf.setAscii(fontFamily);
		rf.setHAnsi(fontFamily);
		rPr.setRFonts(rf);

		BooleanDefaultTrue bdt = factory.createBooleanDefaultTrue();
		rPr.setBCs(bdt);
		if (isBlod) {
			rPr.setB(bdt);
		}
		if (isItalic) {
			rPr.setI(bdt);
		}
		if (isStrike) {
			rPr.setStrike(bdt);
		}
		if (isUnderLine) {
			U underline = new U();
			underline.setVal(UnderlineEnumeration.SINGLE);
			rPr.setU(underline);
		}

		Color color = new Color();
		color.setVal(colorVal);
		rPr.setColor(color);

		HpsMeasure sz = new HpsMeasure();
		sz.setVal(new BigInteger(fontSize));
		rPr.setSz(sz);
		rPr.setSzCs(sz);
		return rPr;
	}
	
	public void setParagraphAlign(ObjectFactory factory, P p,
			JcEnumeration jcEnumeration) {
		PPr pPr = p.getPPr();
		if (pPr == null) {
			pPr = factory.createPPr();
		}
		Jc jc = pPr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		jc.setVal(jcEnumeration);
		pPr.setJc(jc);
		p.setPPr(pPr);
	}
	
	public Relationship createFooterPageNumPart(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory) throws Exception {
		FooterPart footerPart = new FooterPart();
		footerPart.setPackage(wordprocessingMLPackage);
		footerPart.setJaxbElement(createFooterWithPageNr(factory));
		return t.addTargetPart(footerPart);
	}

	public Ftr createFooterWithPageNr(ObjectFactory factory) {
		Ftr ftr = factory.createFtr();
		P paragraph = factory.createP();
		RPr fontRPr = getRPr(factory, "Calibri", "000000", "20", STHint.EAST_ASIA,
				false, false, false, false);
		R run = factory.createR();
		run.setRPr(fontRPr);
		paragraph.getContent().add(run);

		addPageTextField(factory, paragraph, "Page ");
		addFieldBegin(factory, paragraph);
		addPageNumberField(factory, paragraph);
		addFieldEnd(factory, paragraph);
		addPageTextField(factory, paragraph, " of ");

		//addPageTextField(factory, paragraph, "Test");
		addFieldBegin(factory, paragraph);
		addTotalPageNumberField(factory, paragraph);
		addFieldEnd(factory, paragraph);
		//addPageTextField(factory, paragraph, "Test");
		setParagraphAlign(factory, paragraph, JcEnumeration.CENTER);
		ftr.getContent().add(paragraph);
		return ftr;
	}
	
	
	public void addFieldBegin(ObjectFactory factory, P paragraph) {
		R run = factory.createR();
		FldChar fldchar = factory.createFldChar();
		fldchar.setFldCharType(STFldCharType.BEGIN);
		run.getContent().add(fldchar);
		paragraph.getContent().add(run);
	}

	public void addFieldEnd(ObjectFactory factory, P paragraph) {
		FldChar fldcharend = factory.createFldChar();
		fldcharend.setFldCharType(STFldCharType.END);
		R run3 = factory.createR();
		run3.getContent().add(fldcharend);
		paragraph.getContent().add(run3);
	}

	public void addPageNumberField(ObjectFactory factory, P paragraph) {
		R run = factory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue("PAGE  \\* MERGEFORMAT ");
		run.getContent().add(factory.createRInstrText(txt));
		paragraph.getContent().add(run);
	}

	public void addTotalPageNumberField(ObjectFactory factory, P paragraph) {
		R run = factory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue("NUMPAGES  \\* MERGEFORMAT ");
		run.getContent().add(factory.createRInstrText(txt));
		paragraph.getContent().add(run);
	}

	private void addPageTextField(ObjectFactory factory, P paragraph,
			String value) {
		R run = factory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue(value);
		run.getContent().add(txt);
		paragraph.getContent().add(run);
	}
	
	
	public void createFooterReference(
			WordprocessingMLPackage wordprocessingMLPackage,
			MainDocumentPart t, ObjectFactory factory, Relationship relationship)
			throws InvalidFormatException {
		List<SectionWrapper> sections = wordprocessingMLPackage
				.getDocumentModel().getSections();
		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = factory.createSectPr();
			t.addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}
		FooterReference footerReference = factory.createFooterReference();
		footerReference.setId(relationship.getId());
		footerReference.setType(HdrFtrRef.FIRST);
		sectPr.getEGHdrFtrReferences().add(footerReference);
		
		footerReference = factory.createFooterReference();
		footerReference.setId(relationship.getId());
		footerReference.setType(HdrFtrRef.DEFAULT);
		sectPr.getEGHdrFtrReferences().add(footerReference);
	}

	public Hdr getTextHdr(WordprocessingMLPackage wordprocessingMLPackage,
			ObjectFactory factory, Part sourcePart, String content,
			JcEnumeration jcEnumeration) throws Exception {
		Hdr hdr = factory.createHdr();
		P headP = factory.createP();
		Text text = factory.createText();
		text.setValue(content);
		R run = factory.createR();
		run.getContent().add(text);
		headP.getContent().add(run);

		PPr pPr = headP.getPPr();
		if (pPr == null) {
			pPr = factory.createPPr();
		}
		Jc jc = pPr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		jc.setVal(jcEnumeration);
		pPr.setJc(jc);
		headP.setPPr(pPr);
		hdr.getContent().add(headP);
		return hdr;
	}

	public Ftr getTextFtr(WordprocessingMLPackage wordprocessingMLPackage,
			ObjectFactory factory, Part sourcePart, String content,
			JcEnumeration jcEnumeration) throws Exception {
		Ftr ftr = factory.createFtr();
		P footerP = factory.createP();
		Text text = factory.createText();
		text.setValue(content);
		R run = factory.createR();
		run.getContent().add(text);
		footerP.getContent().add(run);

		PPr pPr = footerP.getPPr();
		if (pPr == null) {
			pPr = factory.createPPr();
		}
		Jc jc = pPr.getJc();
		if (jc == null) {
			jc = new Jc();
		}
		jc.setVal(jcEnumeration);
		pPr.setJc(jc);
		footerP.setPPr(pPr);
		ftr.getContent().add(footerP);
		return ftr;
	}
}

