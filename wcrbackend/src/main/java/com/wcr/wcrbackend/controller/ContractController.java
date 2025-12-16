package com.wcr.wcrbackend.controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wcr.wcrbackend.DTO.BankGuarantee;
import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.Insurence;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.HomeService;
import com.wcr.wcrbackend.service.IContractService;
import com.wcr.wcrbackend.service.IUserService;
import com.wcr.wcrbackend.service.SafetyService;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/contract")
public class ContractController {
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
	
	Logger logger = Logger.getLogger(ContractController.class);
	
	@Autowired
	IContractService contractService;
	
//	@Autowired
//	WorkService workService;

	@Autowired
	SafetyService safetyService;
	
	@Autowired
	HomeService homeService;
	
	@Autowired
	private IUserService userService;
	
//	@Autowired
//	ActivitiesService activitiesService;

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

	
	
	@RequestMapping(value = "/ajax/getDesignationsFilterListInContract", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getDesignationsFilterList(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> designationsFilterList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			
			  if (uObj == null) {
	                throw new RuntimeException("User session expired");
	            }
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			
			designationsFilterList = contractService.getDesignationsFilterList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesignationsFilterList : " + e.getMessage());
		}
		return designationsFilterList;
	}
	
	
	@RequestMapping(value = "/ajax/getDyHODDesignationsFilterListInContract", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getDyHODDesignationsFilterList(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> dyHODDesignationsFilterList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			
			  if (uObj == null) {
	                throw new RuntimeException("User session expired");
	            }
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			
			dyHODDesignationsFilterList = contractService.getDyHODDesignationsFilterList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDyHODDesignationsFilterList : " + e.getMessage());
		}
		return dyHODDesignationsFilterList;
	}

	
	
	@RequestMapping(value = "/ajax/getContractorsFilterListInContract", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getContractorsFilterList(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> contractorsFilterList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			  if (uObj == null) {
	                throw new RuntimeException("User session expired");  }
	          
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			
			contractorsFilterList = contractService.contractorsFilterList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractorsFilterList : " + e.getMessage());
		}
		return contractorsFilterList;
	}
	

	@RequestMapping(value = "/ajax/getContractStatusFilterListInContract", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getContractStatusFilterListInContract(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			  if (uObj == null) {
	                throw new RuntimeException("User session expired");  }
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			
			dataList = contractService.getContractStatusFilterListInContract(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractStatusFilterListInContract : " + e.getMessage());
		}
		return dataList;
	}
	
	
	@RequestMapping(value = "/ajax/getStatusFilterListInContract", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getStatusFilterListInContract(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			  if (uObj == null) {
	                throw new RuntimeException("User session expired");  }
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			
			dataList = contractService.getStatusFilterListInContract(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStatusFilterListInContract : " + e.getMessage());
		}
		return dataList;
	}
	
	@RequestMapping(value = "/ajax/getContracts", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getContractList(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> contractList = null;
		try {
			User uObj = (User) session.getAttribute("user");
			  if (uObj == null) {
	                throw new RuntimeException("User session expired");  }
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());	
			
			contractList = contractService.contractList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("contractList : " + e.getMessage());
		}
		return contractList;
	}
	
	@RequestMapping(value = "/get-contract", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getcontract(@ModelAttribute Contract obj,HttpSession session){
		ModelAndView model = new ModelAndView();
		try{
			

			String user_Id = (String) session.getAttribute("USER_ID");
			String userName = (String) session.getAttribute("USER_NAME");
			String userDesignation = (String) session.getAttribute("USER_DESIGNATION");
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setCreated_by_user_id_fk(user_Id);
			obj.setUser_id(user_Id);
			obj.setUser_name(userName);
			obj.setDesignation(userDesignation);
			
			
		//	model.setViewName(PageConstants.updateContractForm);
			List<Contract> projectsList = contractService.getProjectsListForContractForm(obj);
			model.addObject("projectsList", projectsList);
			
			List<Contract> worksList = contractService.getWorkListForContractForm(obj);
			model.addObject("worksList", worksList);
			
			List<Contract> contractFileTypeList = contractService.getContractFileTypeList(obj);
			model.addObject("contractFileTypeList", contractFileTypeList);
			
			List<Contract> departmentList = contractService.getDepartmentList();
			model.addObject("departmentList", departmentList);
			
			List<com.wcr.wcrbackend.DTO.User> hodList = contractService.setHodList();
			model.addObject("hodList", hodList);
			
			List<com.wcr.wcrbackend.DTO.User> dyHodList = contractService.getDyHodList();
			model.addObject("dyHodList", dyHodList);
			
			List<Contract> contractor = contractService.getContractorsList();
			model.addObject("contractor", contractor);
			
			List<Contract> contract_type = contractService.getContractTypeList();
			model.addObject("contract_type", contract_type);
			
			List<Contract> insurance_type = contractService.getInsurenceTypeList();
			model.addObject("insurance_type", insurance_type);
			
			List<BankGuarantee> bankGuaranteeTYpe = contractService.bankGuarantee();
			model.addObject("bankGuaranteeTYpe", bankGuaranteeTYpe);
			
			List<Insurence> InsurenceType = contractService.insurenceType();
			model.addObject("InsurenceType", InsurenceType);
			
			List<Contract> responsiblePeopleList = contractService.getResponsiblePeopleList(obj);
			model.addObject("responsiblePeopleList", responsiblePeopleList);
			
			List<Contract> unitsList = contractService.getUnitsList(obj);
			model.addObject("unitsList", unitsList);
			
			List<Contract> contract_Status = contractService.getContractStatus();
			model.addObject("contract_Status", contract_Status);
			
			Contract contractDeatils = contractService.getContract(obj);
			model.addObject("contractDeatils", contractDeatils);
			
			List<Contract> bankNameList = contractService.getBankNameList(obj);
			model.addObject("bankNameList", bankNameList);			
			
			obj.setContract_status(contractDeatils.getStatus());
			List<Contract> contract_Statustype = contractService.getContractStatusType(obj);
			model.addObject("contract_Statustype", contract_Statustype);
			
			model.addObject("gotoTab", obj.getTab_name());
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Contract : " + e.getMessage());
		}
		return model;
	}
	
}
