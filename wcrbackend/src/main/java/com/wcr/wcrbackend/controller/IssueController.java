package com.wcr.wcrbackend.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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
import com.wcr.wcrbackend.DTO.Issue;
import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IIssueService;
import com.wcr.wcrbackend.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/issue")
public class IssueController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IIssueService issueService;
	
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
	
	Logger logger = Logger.getLogger(IssueController.class);
	
	@PostMapping(value = "/ajax/getContractsListFilterInIssue")
	public List<Issue> getContractsListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getContractsListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractsListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getDepartmentsListFilterInIssue")
	public List<Issue> getDepartmentsListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getDepartmentsListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getDepartmentsListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getCategoryListFilterInIssue")
	public List<Issue> getCategoryListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getCategoryListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getCategoryListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	
	@PostMapping(value = "/ajax/getResponsiblePersonsListFilterInIssue")
	public List<Issue> getResponsiblePersonsListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getResponsiblePersonsListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getResponsiblePersonsListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getStatusListFilterInIssue")
	public List<Issue> getStatusListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getStatusListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getStatusListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getHODListFilterInIssue")
	public List<Issue> getHODListFilterInIssue(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objList = issueService.getHODListFilterInIssue(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getHODListFilterInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getIssuesList")
	public List<Issue> getIssuesList(@RequestBody Issue obj,HttpSession session) {
		List<Issue> issues = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			issues = issueService.getIssuesList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssuesList : " + e.getMessage());
		}
		return issues;
	}
	
	@PostMapping(value = "/ajax/form/add-issue-form")
	public Map<String, List<Issue>> addIssueForm(HttpSession session,@RequestBody Issue obj) {
		Map<String, List<Issue>> map = new HashMap<>();
		
		List<Issue> issues = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			List<Issue> projectsList = issueService.getProjectsListForIssueForm(obj);
			map.put("projectsList", projectsList);
			List<Issue> contractsList = issueService.getContractsListForIssueForm(obj);
			map.put("contractsList", contractsList);
			List<Issue> issuesStatusList = issueService.getIssuesStatusList();
			map.put("issuesStatusList", issuesStatusList);
			List<Issue> issuesPriorityList = issueService.getIssuesPriorityList();
			map.put("issuesPriorityList", issuesPriorityList);
			List<Issue> issuesCategoryList = issueService.getIssuesCategoryList(obj);
			map.put("issuesCategoryList", issuesCategoryList);
			List<Issue> issueTitlesList = issueService.getIssueTitlesList(obj);
			map.put("issueTitlesList", issueTitlesList);
			List<Issue> departmentList = issueService.getDepartmentList();
			map.put("departmentList", departmentList);
			List<Issue> railwayList = issueService.getRailwayList();
			map.put("railwayList", railwayList);
			List<Issue> reportedByList = issueService.getReportedByList();
			map.put("reportedByList", reportedByList);
			List<Issue> responsiblePersonList = issueService.getResponsiblePersonList(null);
			map.put("responsiblePersonList", responsiblePersonList);
			List<Issue> escalatedToList = issueService.getEscalatedToList();
			map.put("escalatedToList", escalatedToList);
			List<Issue> otherOrganizations = issueService.getOtherOrganizationsList();
			map.put("otherOrganizations", otherOrganizations);
			List<Issue> issueFileTypes = issueService.getIssueFileTypes();
			map.put("issueFileTypes", issueFileTypes);
			List<Issue> structures = issueService.getStructures(obj);
			map.put("structures", structures);
			List<Issue> components = issueService.getComponents(obj);
			map.put("components", components);	
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssuesList : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value = "/ajax/getIssueTitlesListForIssuesForm")
	public List<Issue> getIssueTitlesListForIssuesForm(@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getIssueTitlesList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssueTitlesListForIssuesForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getStructureListForIssue")
	public List<Issue> getStructureListForIssue(@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getStructureListForIssue(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureListForIssue : " + e.getMessage());
		}
		return objsList;
	}	
	
	@PostMapping(value = "/ajax/getComponentListForIssue")
	public List<Issue> getComponentListForIssue(@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getComponentListForIssue(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getComponentListForIssue : " + e.getMessage());
		}
		return objsList;
	}	
	
	@PostMapping(value = "/ajax/getIssueCategoryListForIssuesForm")
	public List<Issue> getIssueCategoryListForIssuesForm(@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getIssuesCategoryList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssueCategoryListForIssuesForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getProjectsListForIssuesForm")
	public List<Issue> getProjectsListForIssueForm(HttpSession session,@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			objsList = issueService.getProjectsListForIssueForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getProjectsListForIssueForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getContractsListForIssuesForm")
	public List<Issue> getContractsListForIssueForm(HttpSession session,@RequestBody Issue obj) {
		List<Issue> objsList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			objsList = issueService.getContractsListForIssueForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractsListForIssueForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value = "/ajax/getIssueStatusListForIssuesForm")
	public List<Issue> getIssueStatusListForIssuesForm() {
		List<Issue> objsList = null;
		try {
			objsList = issueService.getIssuesStatusList();
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssueStatusListForIssuesForm : " + e.getMessage());
		}
		return objsList;
	}
	
	@PostMapping(value="/add-issue")
	public Boolean addIssue(@RequestBody Issue obj,HttpSession session) {
		//ModelAndView model = new ModelAndView();
		Boolean flag = false;
		try {
			//model.setViewName("redirect:/issues");
			User uObj = (User) session.getAttribute("user");
			String user_Id = uObj.getUserId();
			String userName = uObj.getUserName();
			String userDesignation = uObj.getDesignation();
			
			User user = (User)session.getAttribute("user");
			if(!StringUtils.isEmpty(user) && !StringUtils.isEmpty(user.getEmailId())) {
				obj.setReported_by_email_id(user.getEmailId());
			}
			
			obj.setDate(DateParser.parse(obj.getDate()));			
			obj.setResolved_date(DateParser.parse(obj.getResolved_date()));			
			obj.setEscalation_date(DateParser.parse(obj.getEscalation_date()));
			obj.setAssigned_date(DateParser.parse(obj.getAssigned_date()));
			if(!StringUtils.isEmpty(obj.getZonal_railway_fk()) && obj.getZonal_railway_fk().equals("MRVC")) {
				obj.setOther_organization(obj.getZonal_railway_fk() + " - " + obj.getOther_organization());
			}
			obj.setCreated_by_user_id_fk(user_Id);
			obj.setUser_name(userName);
			obj.setDesignation(userDesignation);
			
			obj.setStatus_fk(CommonConstants.ISSUE_STATUS_RAISED);
			
			flag = issueService.addIssue(obj);
			/*if(flag) {
				attributes.addFlashAttribute("success", "Issue "+obj.getStatus_fk()+" successfully");
			}else {
				attributes.addFlashAttribute("error", "Adding issue failed. Try again.");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			//attributes.addFlashAttribute("error", commonError);
			logger.error("addIssue : " + e.getMessage());
		}
		return flag;
	}
	
	@PostMapping(value="/ajax/form/get-issue")
	public Map<String, List<Issue>> getIssue(@RequestBody Issue obj,HttpSession session) {
		Map<String, List<Issue>> map = new HashMap<>();
		//ModelAndView model = new ModelAndView();
		try {
			//model.setViewName(PageConstants2.updateIssueForm);
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			//String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
			
			List<Issue> projectsList = issueService.getProjectsListForIssueForm(obj);
			map.put("projectsList", projectsList);
			
		
			List<Issue> contractsList = issueService.getContractsListForIssueForm(obj);
			map.put("contractsList", contractsList);
			
			List<Issue> issuesStatusList = issueService.getIssuesStatusList();
			map.put("issuesStatusList", issuesStatusList);
			
			List<Issue> issuesPriorityList = issueService.getIssuesPriorityList();
			map.put("issuesPriorityList", issuesPriorityList);
			
			List<Issue> issuesCategoryList = issueService.getIssuesCategoryList(obj);
			map.put("issuesCategoryList", issuesCategoryList);
			
			List<Issue> issueTitlesList = issueService.getIssueTitlesList(obj);
			map.put("issueTitlesList", issueTitlesList);
			
			List<Issue> departmentList = issueService.getDepartmentList();
			map.put("departmentList", departmentList);
			
			List<Issue> railwayList = issueService.getRailwayList();
			map.put("railwayList", railwayList);
			
			List<Issue> reportedByList = issueService.getReportedByList();
			map.put("reportedByList", reportedByList);
			
			List<Issue> responsiblePersonList = issueService.getResponsiblePersonList(null);
			map.put("responsiblePersonList", responsiblePersonList);
			
			List<Issue> escalatedToList = issueService.getEscalatedToList();
			map.put("escalatedToList", escalatedToList);
			
			List<Issue> otherOrganizations = issueService.getOtherOrganizationsList();
			map.put("otherOrganizations", otherOrganizations);
			
			List<Issue> issueFileTypes = issueService.getIssueFileTypes();
			map.put("issueFileTypes", issueFileTypes);
			
			List<Issue> structures = issueService.getStructures(obj);
			map.put("structures", structures);
			
			
			List<Issue> components = issueService.getComponents(obj);
			map.put("components", components);	
			
			List<Issue> actionTakens = issueService.getActionTakens(obj);
			map.put("actionTakens", actionTakens);			
			

		} catch (Exception e) {
			//attributes.addFlashAttribute("error", commonError);
			logger.error("getIssue : " + e.getMessage());
		}
		return map;
	}
	@PostMapping(value="/ajax/form/get-issue/issue")
	public Issue getIssue2(@RequestBody Issue obj,HttpSession session) {
		Issue issue = null;
		
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			issue = issueService.getIssue(obj);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getIssue : " + e.getMessage());
		}
		return issue;
	}
	
	@PostMapping(value = "/ajax/getResponsiblePersonsInIssue")
	public List<Issue> getResponsiblePersons(@RequestBody Issue obj,HttpSession session) {
		List<Issue> objList = null;
		try {
			objList = issueService.getResponsiblePersonList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getResponsiblePersonsInIssue : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value="/update-issue")
	public ResponseEntity<?> updateIssue(@RequestBody Issue obj,HttpSession session) {
		//ModelAndView model = new ModelAndView();
		try {
			//model.setViewName("redirect:/issues");
			User user = (User)session.getAttribute("user");
			String user_Id = user.getUserId();
			String userName = user.getUserName();
			String userDesignation = user.getDesignation();
			
			//User user = (User)session.getAttribute("user");
			if(!StringUtils.isEmpty(user) && !StringUtils.isEmpty(user.getEmailId())) {
				obj.setReported_by_email_id(user.getEmailId());
			}
			obj.setDate(DateParser.parse(obj.getDate()));			
			obj.setResolved_date(DateParser.parse(obj.getResolved_date()));
			obj.setEscalation_date(DateParser.parse(obj.getEscalation_date()));
			obj.setAssigned_date(DateParser.parse(obj.getAssigned_date()));
			obj.setCreated_by_user_id_fk(user_Id);
			obj.setUser_name(userName);
			obj.setDesignation(userDesignation);
			if(!StringUtils.isEmpty(obj.getZonal_railway_fk()) && obj.getZonal_railway_fk().equals("MRVC")) {
				obj.setOther_organization(obj.getZonal_railway_fk() + " - " + obj.getOther_organization());
			}
			
			boolean flag = issueService.updateIssue(obj);
			if(flag) {
				if(!StringUtils.isEmpty(obj.getStatus_fk()) && obj.getStatus_fk().equals(obj.getExisting_status_fk())) {
					return ResponseEntity.ok(Map.of("success", "Issue has been updated successfully"));
				}else{
					
					if(!StringUtils.isEmpty(obj.getAction())) {
						return ResponseEntity.ok(Map.of("success", "Issue has Re-Opened successfully"));
					}
					else
					{
						return ResponseEntity.ok(Map.of("success", "Issue "+obj.getStatus_fk()+" successfully"));
					}
				}
			}else {
				return ResponseEntity.ok(Map.of("error", "Updating issue failed. Try again."));
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.error("updateIssue : " + e.getMessage());
			return ResponseEntity.ok(Map.of("error", commonError));
		}
		//return model;
	}
	
	@PostMapping(value = "/export-issues")
	public ResponseEntity<?> exportIssues(HttpServletRequest request, HttpServletResponse response,HttpSession session,@RequestBody Issue issue){
		//ModelAndView view = new ModelAndView(PageConstants.issuesGrid);
		List<Issue> dataList = new ArrayList<Issue>();
		String userId = null;String userName = null;
		String user_role_code = null;String user_type = null;
		try {
			User user = (User)session.getAttribute("user");
			userId = user.getUserId();
			userName = user.getUserName();
			
			user_role_code = userService.getRoleCode(user.getUserRoleNameFk());
			user_type = user.getUserTypeFk();
			
			issue.setUser_id(userId);
			issue.setUser_role_code(user_role_code);
			issue.setUser_type(user_type);
			
			//view.setViewName("redirect:/issues");
			dataList = issueService.getIssuesList(issue);  
			if(dataList != null && dataList.size() > 0){
	            XSSFWorkbook  workBook = new XSSFWorkbook ();
	            XSSFSheet sheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Issue"));
		        workBook.setSheetOrder(sheet.getSheetName(), 0);
		        
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
		        
		        
		        
	            XSSFRow headingRow = sheet.createRow(0);
	            String headerString = "Issue ID^Project^Work^Contract^Short Description^Issue Pending Since^Location/Station/KM^Latitude^Longitude"
	            		+ "^Reported By^Raised On^Responsible Person^Assigned Date^Issue Category^Issue Status^Responsible Organization^Priority^Issue/Action Taken/Remarks^"
	            		+ "Escalated to^Escalation Date^Status After Escalation^Resolved Date^Description";
	            
	            String[] firstHeaderStringArr = headerString.split("\\^");
	            
	            for (int i = 0; i < firstHeaderStringArr.length; i++) {		        	
		        	Cell cell = headingRow.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(firstHeaderStringArr[i]);
				}
	            
	            short rowNo = 1;
	            for (Issue obj : dataList) {
	                XSSFRow row = sheet.createRow(rowNo);
	                int c = 0;
	                
	                Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getIssue_id());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getProject_id_fk() +"-"+obj.getProject_name());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getWork_id_fk()+"-"+obj.getWork_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_id_fk()+"-"+obj.getContract_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getTitle());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDate());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLocation());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLatitude());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLongitude());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getReported_by());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCreated_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getResponsible_person_designation());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getAssigned_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCategory_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getStatus_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getOther_organization());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getPriority_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCorrective_measure());
					

					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getEscalated_to_designation());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getEscalation_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRemarks());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getResolved_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDescription());
	                
	                rowNo++;
	            }
	            for(int columnIndex = 0; columnIndex < firstHeaderStringArr.length; columnIndex++) {
	        		sheet.setColumnWidth(columnIndex, 25 * 200);
				}
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
                Date date = new Date();
                String fileName = "Issues_"+dateFormat.format(date);
                
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
	            }catch(IOException e){
	                //e.printStackTrace();
	            	return ResponseEntity.ok(Map.of("error",dataExportError));
	            }
	         }else{
	        	 return ResponseEntity.ok(Map.of("error",dataExportNoData));
	         }
		}catch(Exception e){	
			e.printStackTrace();
			logger.error("exportIssues : : User Id - "+userId+" - User Name - "+userName+" - "+e.getMessage());
			return ResponseEntity.ok(Map.of("error", commonError));			
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
	
}
