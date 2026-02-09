package com.wcr.wcrbackend.controller;


import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.wcr.wcrbackend.DTO.BankGuarantee;
import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.Insurence;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.HomeService;
import com.wcr.wcrbackend.service.IContractService;
import com.wcr.wcrbackend.service.IUserService;
import com.wcr.wcrbackend.service.SafetyService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
//
//	@RequestMapping(value = "/get-contract", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView getcontract(@ModelAttribute Contract obj,HttpSession session){
//		ModelAndView model = new ModelAndView();
//		try{
//			
//
//			
//			String user_Id = (String) session.getAttribute("userId");
//			String userName = (String) session.getAttribute("userName");
//			String userDesignation = (String) session.getAttribute("designation");
//			
//			System.out.println("User_id:"+ user_Id);
//			System.out.println("UserNmae:"+ userName);
//      		System.out.println("UserDegination:" + userDesignation);
//			
//			User uObj = (User) session.getAttribute("user");
//			obj.setUser_type_fk(uObj.getUserTypeFk());
//			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
//			obj.setCreated_by_user_id_fk(user_Id);
//			obj.setUser_id(user_Id);
//			obj.setUser_name(userName);
//			obj.setDesignation(userDesignation);
//			
//			
//		//	model.setViewName(PageConstants.updateContractForm);
//			List<Contract> projectsList = contractService.getProjectsListForContractForm(obj);
//			model.addObject("projectsList", projectsList);
//			
//			List<Contract> worksList = contractService.getWorkListForContractForm(obj);
//			model.addObject("worksList", worksList);
//			
//			List<Contract> contractFileTypeList = contractService.getContractFileTypeList(obj);
//			model.addObject("contractFileTypeList", contractFileTypeList);
//			
//			List<Contract> departmentList = contractService.getDepartmentList();
//			model.addObject("departmentList", departmentList);
//			
//			List<com.wcr.wcrbackend.DTO.User> hodList = contractService.setHodList();
//			model.addObject("hodList", hodList);
//			
//			List<com.wcr.wcrbackend.DTO.User> dyHodList = contractService.getDyHodList();
//			model.addObject("dyHodList", dyHodList);
//			
//			List<Contract> contractor = contractService.getContractorsList();
//			model.addObject("contractor", contractor);
//			
//			List<Contract> contract_type = contractService.getContractTypeList();
//			model.addObject("contract_type", contract_type);
//			
//			List<Contract> insurance_type = contractService.getInsurenceTypeList();
//			model.addObject("insurance_type", insurance_type);
//			
//			List<BankGuarantee> bankGuaranteeTYpe = contractService.bankGuarantee();
//			model.addObject("bankGuaranteeTYpe", bankGuaranteeTYpe);
//			
//			List<Insurence> InsurenceType = contractService.insurenceType();
//			model.addObject("InsurenceType", InsurenceType);
//			
//			List<Contract> responsiblePeopleList = contractService.getResponsiblePeopleList(obj);
//			model.addObject("responsiblePeopleList", responsiblePeopleList);
//			
//			List<Contract> unitsList = contractService.getUnitsList(obj);
//			model.addObject("unitsList", unitsList);
//			
//			List<Contract> contract_Status = contractService.getContractStatus();
//			model.addObject("contract_Status", contract_Status);
//			
//			Contract contractDeatils = contractService.getContract(obj);
//			model.addObject("contractDeatils", contractDeatils);
//			
//			List<Contract> bankNameList = contractService.getBankNameList(obj);
//			model.addObject("bankNameList", bankNameList);			
//			
//			obj.setContract_status(contractDeatils.getStatus());
//			List<Contract> contract_Statustype = contractService.getContractStatusType(obj);
//			model.addObject("contract_Statustype", contract_Statustype);
//			
//			model.addObject("gotoTab", obj.getTab_name());
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("Contract : " + e.getMessage());
//		}
//		return model;
//	}
//	
//	@RequestMapping(value = "/get-contract/{contract_id}", method = {RequestMethod.GET,RequestMethod.POST})
//	public ModelAndView getcontract(@ModelAttribute Contract obj,@PathVariable("contract_id") String contract_id,HttpSession session ){
//		ModelAndView model = new ModelAndView();
//		try{
//			
//			String user_Id = (String) session.getAttribute("userId");
//			String userName = (String) session.getAttribute("userName");
//			String userDesignation = (String) session.getAttribute("designation");
//			
//		
//			User uObj = (User) session.getAttribute("user");
//            
//			obj.setUser_type_fk(uObj.getUserTypeFk());
//			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
//			obj.setCreated_by_user_id_fk(user_Id);
//			obj.setUser_id(user_Id);
//			obj.setUser_name(userName);
//			obj.setDesignation(userDesignation);
//			
//			
//			//model.setViewName(PageConstants.updateContractForm);
//			List<Contract> projectsList = contractService.getProjectsListForContractForm(obj);
//			model.addObject("projectsList", projectsList);
//			
//			List<Contract> worksList = contractService.getWorkListForContractForm(obj);
//			model.addObject("worksList", worksList);
//			
//			List<Contract> contractFileTypeList = contractService.getContractFileTypeList(obj);
//			model.addObject("contractFileTypeList", contractFileTypeList);
//			
//			List<Contract> departmentList = contractService.getDepartmentList();
//			model.addObject("departmentList", departmentList);
//			
//			List<com.wcr.wcrbackend.DTO.User> hodList = contractService.setHodList();
//			model.addObject("hodList", hodList);
//			
//			List<com.wcr.wcrbackend.DTO.User> dyHodList = contractService.getDyHodList();
//			model.addObject("dyHodList", dyHodList);
//			
//			List<Contract> contractor = contractService.getContractorsList();
//			model.addObject("contractor", contractor);
//			 
//			List<Contract> contract_type = contractService.getContractTypeList();
//			model.addObject("contract_type", contract_type);
//			
//			List<Contract> insurance_type = contractService.getInsurenceTypeList();
//			model.addObject("insurance_type", insurance_type);
//			
//			List<BankGuarantee> bankGuaranteeTYpe = contractService.bankGuarantee();
//			model.addObject("bankGuaranteeTYpe", bankGuaranteeTYpe);
//			
//			List<Insurence> InsurenceType = contractService.insurenceType();
//			model.addObject("InsurenceType", InsurenceType);
//			
//			List<Contract> contract_Status = contractService.getContractStatus();
//			model.addObject("contract_Status", contract_Status);
//			
//			List<Contract> unitsList = contractService.getUnitsList(obj);
//			model.addObject("unitsList", unitsList);
//			
//			List<Contract> bankNameList = contractService.getBankNameList(obj);
//			model.addObject("bankNameList", bankNameList);			
//			
//			obj.setContract_id(contract_id);
//			Contract contractDeatils = contractService.getContract(obj);
//			model.addObject("contractDeatils", contractDeatils);
//			
//			obj.setContract_status(contractDeatils.getStatus());
//			List<Contract> contract_Statustype = contractService.getContractStatusType(obj);
//			model.addObject("contract_Statustype", contract_Statustype);
//			
//			model.addObject("gotoTab", obj.getTab_name());
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			logger.error("Contract : " + e.getMessage());
//		}
//		return model;
//	}
//	

    @RequestMapping(
        value = "/get-contractt",
        method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseEntity<Map<String, Object>> getcontractt(
            @ModelAttribute Contract obj,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1️⃣ Session values (unchanged)
            String user_Id = (String) session.getAttribute("userId");
            String userName = (String) session.getAttribute("userName");
            String userDesignation = (String) session.getAttribute("designation");

            User uObj = (User) session.getAttribute("user");
            if (uObj == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "success", false,
                                "message", "Session expired"
                        ));
            }

            obj.setUser_type_fk(uObj.getUserTypeFk());
            obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
            obj.setCreated_by_user_id_fk(user_Id);
            obj.setUser_id(user_Id);
            obj.setUser_name(userName);
            obj.setDesignation(userDesignation);

           
            response.put("projectsList",
                    contractService.getProjectsListForContractForm(obj));

            response.put("worksList",
                    contractService.getWorkListForContractForm(obj));

            response.put("contractFileTypeList",
                    contractService.getContractFileTypeList(obj));

            response.put("departmentList",
                    contractService.getDepartmentList());

            response.put("hodList",
                    contractService.setHodList());

            response.put("dyHodList",
                    contractService.getDyHodList());

            response.put("contractor",
                    contractService.getContractorsList());

            response.put("contract_type",
                    contractService.getContractTypeList());

            response.put("insurance_type",
                    contractService.getInsurenceTypeList());

            response.put("bankGuaranteeTYpe",
                    contractService.bankGuarantee());

            response.put("InsurenceType",
                    contractService.insurenceType());

            response.put("responsiblePeopleList",
                    contractService.getResponsiblePeopleList(obj));

            response.put("unitsList",
                    contractService.getUnitsList(obj));

            response.put("contract_Status",
                    contractService.getContractStatus());

            Contract contractDeatils = contractService.getContract(obj);
            response.put("contractDeatils", contractDeatils);

            response.put("bankNameList",
                    contractService.getBankNameList(obj));

            if (contractDeatils != null) {
                obj.setContract_status(contractDeatils.getStatus());
                response.put("contract_Statustype",
                        contractService.getContractStatusType(obj));
            }

            response.put("gotoTab", obj.getTab_name());

            response.put("success", true);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Contract : " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Internal server error"
                    ));
        }
    }
    
//
//	@RequestMapping(value = "/add-contract", method = {RequestMethod.GET,RequestMethod.POST})
//	@ResponseBody	
//	public Map<String, Object> addContract1(@RequestBody Contract contract, HttpSession session){
//
//		Map<String, Object> res = new HashMap<>();
//		
//		
//		System.out.println("Getting inside the controlle");
//		System.out.println("Contract:::" + contract);
//		System.out.println("Revision No: " + Arrays.toString(contract.getRevisionno()));
//	
//		try{
//			String user_Id = (String) session.getAttribute("userId");
//			String userName = (String) session.getAttribute("userName");
//			String userDesignation = (String) session.getAttribute("designation");
//			
//			contract.setCreated_by_user_id_fk(user_Id);
//			contract.setUser_id(user_Id);
//			contract.setUser_name(userName);
//			contract.setDesignation(userDesignation);
//			
//		//	System.out.println("HOD ID FK = " + contract.getHod_user_id_fk());
//			//System.out.println("Documentsss = "  + contract.getAttachment());
//
//			
//			User uObj = (User) session.getAttribute("user");
//			contract.setUser_type_fk(uObj.getUserTypeFk());
//			contract.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
//			contract.setDoc(DateParser.parse(contract.getDoc()));
//			contract.setCa_date(DateParser.parse(contract.getCa_date()));
//			contract.setDate_of_start(DateParser.parse(contract.getDate_of_start()));			
//			contract.setLoa_date(DateParser.parse(contract.getLoa_date()));
//			contract.setActual_completion_date(DateParser.parse(contract.getActual_completion_date()));		
//			contract.setContract_closure_date(DateParser.parse(contract.getContract_closure_date()));
//			contract.setCompletion_certificate_release(DateParser.parse(contract.getCompletion_certificate_release()));		
//			contract.setFinal_takeover(DateParser.parse(contract.getFinal_takeover()));
//			contract.setFinal_bill_release(DateParser.parse(contract.getFinal_bill_release()));
//			contract.setDefect_liability_period(DateParser.parse(contract.getDefect_liability_period()));
//			contract.setRetention_money_release(DateParser.parse(contract.getRetention_money_release()));
//			contract.setPbg_release(DateParser.parse(contract.getPbg_release()));
//			contract.setBg_date(DateParser.parse(contract.getBg_date()));
//			contract.setRelease_date(DateParser.parse(contract.getRelease_date()));
//			contract.setPlanned_date_of_award(DateParser.parse(contract.getPlanned_date_of_award()));
//			contract.setPlanned_date_of_completion(DateParser.parse(contract.getPlanned_date_of_completion()));
//			
//			//contract.setContract_status("Open");
//			//contract.setContract_status_fk("Not Started");
//		
//			String contractid =  contractService.addContract(contract);			
//			if(!StringUtils.isEmpty(contractid)) {
////				attributes.addFlashAttribute("success", "Contract "+contractid+" Added Succesfully.");
//				res.put("success", "Contract "+contractid+" Added Succesfully."); 
//
//			} else {
////				attributes.addFlashAttribute("error","Adding Contract is failed. Try again.");
//				res.put("error","Adding Contract is failed. Try again.");
//
//			}
//		 }catch (Exception e) {
////			attributes.addFlashAttribute("error","Adding Contract is failed. Try again.");
//				res.put("error","Adding Contract is failed. Try again.");
//
//			logger.error("Project : " + e.getMessage());
//		}
//		return res;
//	}
//	
	
    
    @PostMapping(
    	    value = "/add-contract", 
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    	    produces = MediaType.APPLICATION_JSON_VALUE
    	)
    @ResponseBody	
    public Map<String, Object> addContract1(
    	    @RequestPart("payload") String contractJson,
    	    @RequestPart(value = "contractDocumentFiles[]", required = false) MultipartFile[] contractDocumentFiles, HttpSession session){

		Map<String, Object> res = new HashMap<>();
		
		try {
	       
	        // Convert JSON to Contract object
	        ObjectMapper objectMapper = new ObjectMapper();
	        Contract contract = objectMapper.readValue(contractJson, Contract.class);
	        
	     
	        
	        // Set the files to the contract object
	        if (contractDocumentFiles != null && contractDocumentFiles.length > 0) {
	            contract.setContractDocumentFiles(contractDocumentFiles);
	        }
		
	      
	
			String user_Id = (String) session.getAttribute("userId");
			String userName = (String) session.getAttribute("userName");
			String userDesignation = (String) session.getAttribute("designation");
			
			contract.setCreated_by_user_id_fk(user_Id);
			contract.setUser_id(user_Id);
			contract.setUser_name(userName);
			contract.setDesignation(userDesignation);
			
		//	System.out.println("HOD ID FK = " + contract.getHod_user_id_fk());
			//System.out.println("Documentsss = "  + contract.getAttachment());

			
			User uObj = (User) session.getAttribute("user");
			contract.setUser_type_fk(uObj.getUserTypeFk());
			contract.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			contract.setDoc(DateParser.parse(contract.getDoc()));
			contract.setCa_date(DateParser.parse(contract.getCa_date()));
			contract.setDate_of_start(DateParser.parse(contract.getDate_of_start()));			
			contract.setLoa_date(DateParser.parse(contract.getLoa_date()));
			contract.setActual_completion_date(DateParser.parse(contract.getActual_completion_date()));		
			contract.setContract_closure_date(DateParser.parse(contract.getContract_closure_date()));
			contract.setCompletion_certificate_release(DateParser.parse(contract.getCompletion_certificate_release()));		
			contract.setFinal_takeover(DateParser.parse(contract.getFinal_takeover()));
			contract.setFinal_bill_release(DateParser.parse(contract.getFinal_bill_release()));
			contract.setDefect_liability_period(DateParser.parse(contract.getDefect_liability_period()));
			contract.setRetention_money_release(DateParser.parse(contract.getRetention_money_release()));
			contract.setPbg_release(DateParser.parse(contract.getPbg_release()));
			contract.setBg_date(DateParser.parse(contract.getBg_date()));
			contract.setRelease_date(DateParser.parse(contract.getRelease_date()));
			contract.setPlanned_date_of_award(DateParser.parse(contract.getPlanned_date_of_award()));
			contract.setPlanned_date_of_completion(DateParser.parse(contract.getPlanned_date_of_completion()));
			
			//contract.setContract_status("Open");
			//contract.setContract_status_fk("Not Started");
		
			String contractid =  contractService.addContract(contract);			
			if(!StringUtils.isEmpty(contractid)) {
//				attributes.addFlashAttribute("success", "Contract "+contractid+" Added Succesfully.");
				res.put("success", "Contract "+contractid+" Added Succesfully."); 

			} else {
//				attributes.addFlashAttribute("error","Adding Contract is failed. Try again.");
				res.put("error","Adding Contract is failed. Try again.");

			}
		 }catch (Exception e) {
//			attributes.addFlashAttribute("error","Adding Contract is failed. Try again.");
				res.put("error","Adding Contract is failed. Try again.");

			logger.error("Project : " + e.getMessage());
		}
		return res;
	}
	

    
//    
//    @PostMapping(
//    	    value = "/add-contract",
//    	    consumes = "application/json",
//    	    produces = "application/json"
//    	)
//    	@ResponseBody
//    	public Map<String, Object> addContract1(
//    	        @RequestBody Contract contract,
//    	        HttpSession session) {
//
//    	    Map<String, Object> res = new HashMap<>();
//
//    	    System.out.println("Inside add-contract JSON controller");
//    	    System.out.println("Contract ::: " + contract);
//
//    	    try {
//    	        String user_Id = (String) session.getAttribute("userId");
//    	        String userName = (String) session.getAttribute("userName");
//    	        String userDesignation = (String) session.getAttribute("designation");
//
//    	        contract.setCreated_by_user_id_fk(user_Id);
//    	        contract.setUser_id(user_Id);
//    	        contract.setUser_name(userName);
//    	        contract.setDesignation(userDesignation);
//
//    	        User uObj = (User) session.getAttribute("user");
//    	        contract.setUser_type_fk(uObj.getUserTypeFk());
//    	        contract.setUser_role_code(
//    	                userService.getRoleCode(uObj.getUserRoleNameFk())
//    	        );
//
//    	        // ---- DATE PARSING (UNCHANGED) ----
//    	        contract.setDoc(DateParser.parse(contract.getDoc()));
//    	        contract.setCa_date(DateParser.parse(contract.getCa_date()));
//    	        contract.setDate_of_start(DateParser.parse(contract.getDate_of_start()));
//    	        contract.setLoa_date(DateParser.parse(contract.getLoa_date()));
//    	        contract.setActual_completion_date(DateParser.parse(contract.getActual_completion_date()));
//    	        contract.setContract_closure_date(DateParser.parse(contract.getContract_closure_date()));
//    	        contract.setCompletion_certificate_release(DateParser.parse(contract.getCompletion_certificate_release()));
//    	        contract.setFinal_takeover(DateParser.parse(contract.getFinal_takeover()));
//    	        contract.setFinal_bill_release(DateParser.parse(contract.getFinal_bill_release()));
//    	        contract.setDefect_liability_period(DateParser.parse(contract.getDefect_liability_period()));
//    	        contract.setRetention_money_release(DateParser.parse(contract.getRetention_money_release()));
//    	        contract.setPbg_release(DateParser.parse(contract.getPbg_release()));
//    	        contract.setBg_date(DateParser.parse(contract.getBg_date()));
//    	        contract.setRelease_date(DateParser.parse(contract.getRelease_date()));
//    	        contract.setPlanned_date_of_award(DateParser.parse(contract.getPlanned_date_of_award()));
//    	        contract.setPlanned_date_of_completion(DateParser.parse(contract.getPlanned_date_of_completion()));
//
//    	        String contractid = contractService.addContract(contract);
//
//    	        if (!StringUtils.isEmpty(contractid)) {
//    	            res.put("success", true);
//    	            res.put("message", "Contract " + contractid + " Added Successfully");
//    	        } else {
//    	            res.put("success", false);
//    	            res.put("message", "Adding Contract failed. Try again.");
//    	        }
//
//    	    } catch (Exception e) {
//    	        logger.error("Add Contract Error", e);
//    	        res.put("success", false);
//    	        res.put("message", "Adding Contract failed due to server error.");
//    	    }
//
//    	    return res;
//    	}


	@RequestMapping(value = "/saveedit-contract", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView saveeditContract(@ModelAttribute  Contract contract,RedirectAttributes attributes,HttpSession session){
		ModelAndView model = new ModelAndView();
		try{
			String user_Id = (String) session.getAttribute("userId");
			String userName = (String) session.getAttribute("userName");
			String userDesignation = (String) session.getAttribute("designation");
			
			contract.setCreated_by_user_id_fk(user_Id);
			contract.setUser_id(user_Id);
			contract.setUser_name(userName);
			contract.setDesignation(userDesignation);
			
			User uObj = (User) session.getAttribute("user");
			
			contract.setUser_type_fk(uObj.getUserTypeFk());
			contract.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			
			contract.setDoc(DateParser.parse(contract.getDoc()));
			contract.setCa_date(DateParser.parse(contract.getCa_date()));
			contract.setDate_of_start(DateParser.parse(contract.getDate_of_start()));			
			contract.setLoa_date(DateParser.parse(contract.getLoa_date()));
			contract.setActual_completion_date(DateParser.parse(contract.getActual_completion_date()));		
			contract.setContract_closure_date(DateParser.parse(contract.getContract_closure_date()));
			contract.setCompletion_certificate_release(DateParser.parse(contract.getCompletion_certificate_release()));		
			contract.setFinal_takeover(DateParser.parse(contract.getFinal_takeover()));
			contract.setFinal_bill_release(DateParser.parse(contract.getFinal_bill_release()));
			contract.setDefect_liability_period(DateParser.parse(contract.getDefect_liability_period()));
			contract.setRetention_money_release(DateParser.parse(contract.getRetention_money_release()));
			contract.setPbg_release(DateParser.parse(contract.getPbg_release()));
			contract.setBg_date(DateParser.parse(contract.getBg_date()));
			contract.setRelease_date(DateParser.parse(contract.getRelease_date()));
			contract.setPlanned_date_of_award(DateParser.parse(contract.getPlanned_date_of_award()));
			contract.setPlanned_date_of_completion(DateParser.parse(contract.getPlanned_date_of_completion()));
		
			String contractid =  contractService.addContract(contract);	
			model.setViewName("redirect:/get-contract/"+contractid);

		 }catch (Exception e) {
			attributes.addFlashAttribute("error","Adding Contract is failed. Try again.");
			logger.error("Project : " + e.getMessage());
		}
		return model;
	}
	
	
	 @PostMapping("/addcontract")
	    public ResponseEntity<?> addContract(@RequestBody Contract obj, HttpSession session) {
	        try {

	        	String user_Id = (String) session.getAttribute("userId");
				String userName = (String) session.getAttribute("userName");
				String userDesignation = (String) session.getAttribute("designation");
				
	            
	            if (user_Id == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                        .body(Map.of(
	                                "success", false,
	                                "message", "Session expired. Please login again."
	                        ));
	            }

	            obj.setCreated_by_user_id_fk(user_Id);
	            obj.setUser_id(user_Id);
	            obj.setUser_name(userName);
	            obj.setDesignation(userDesignation);

	      
	            Map<String, Object> response = new HashMap<>();

	            response.put("projectsList", contractService.getProjectsListForContractForm(obj));
	            response.put("worksList", contractService.getWorkListForContractForm(obj));
	            response.put("contractFileTypeList", contractService.getContractFileTypeList(obj));
	            response.put("departmentList", contractService.getDepartmentList());
	            response.put("hodList", contractService.setHodList());
	            response.put("dyHodList", contractService.getDyHodList());
	            response.put("contractors", contractService.getContractorsList());
	            response.put("contract_type", contractService.getContractTypeList());
	            response.put("insurance_type", contractService.getInsurenceTypeList());
	            response.put("contractList", contractService.contractList(obj));
	            response.put("bankGuaranteeType", contractService.bankGuarantee());
	            response.put("insurenceType", contractService.insurenceType());
	            response.put("contract_Status", contractService.getContractStatus());
	            response.put("contract_Statustype", contractService.getContractStatusType(obj));

	            response.put("success", true);
	            response.put("message", "Contract master data loaded successfully");

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(Map.of(
	                            "success", false,
	                            "message", "Error loading contract data",
	                            "error", e.getMessage()
	                    ));
	        }
	    }
	

	   
	 @RequestMapping(value = "/add-contract-form", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE) 
	    public ResponseEntity<?> addContractForm( @RequestBody(required = false) Contract obj,  HttpSession session) {
	          

	        try {
	            if (obj == null) {
	                obj = new Contract();
	            }

	            String user_Id = (String) session.getAttribute("userId");
				String userName = (String) session.getAttribute("userName");
				String userDesignation = (String) session.getAttribute("designation");
				
	            
	           User uObj = (User) session.getAttribute("user");

	            if (user_Id == null || uObj == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                        .body(Map.of(
	                                "success", false,
	                                "message", "Session expired. Please login again."
	                        ));
	            }

	            obj.setCreated_by_user_id_fk(user_Id);
	            obj.setUser_id(user_Id);
	            obj.setUser_name(userName);
	            obj.setDesignation(userDesignation);
	            obj.setUser_type_fk(uObj.getUserTypeFk());
	            obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
	            
	   
	            Map<String, Object> response = new HashMap<>();

	            response.put("projectsList", contractService.getProjectsListForContractForm(obj));
	            response.put("worksList", contractService.getWorkListForContractForm(obj));
	            response.put("contractFileTypeList", contractService.getContractFileTypeList(obj));
	            response.put("departmentList", contractService.getDepartmentList());
	            response.put("hodList", contractService.setHodList());
	            response.put("dyHodList", contractService.getDyHodList());
	            response.put("contractors", contractService.getContractorsList());
	            response.put("contract_type", contractService.getContractTypeList());
	            response.put("insurance_type", contractService.getInsurenceTypeList());
	            response.put("contractList", contractService.contractList(obj));
	            response.put("bankGuaranteeType", contractService.bankGuarantee());
	            response.put("insurenceType", contractService.insurenceType());
	            response.put("contract_Statustype", contractService.getContractStatusType(obj));
	            response.put("contract_Status", contractService.getContractStatus());
	            response.put("responsiblePeopleList", contractService.getResponsiblePeopleList(obj));
	            response.put("unitsList", contractService.getUnitsList(obj));
	            response.put("bankNameList", contractService.getBankNameList(obj));

	            response.put("success", true);
	            response.put("message", "Add Contract Form master data loaded successfully");

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {
	            logger.error("Contract : ", e);
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(Map.of(
	                            "success", false,
	                            "message", "Error loading contract form data",
	                            "error", e.getMessage()
	                    ));
	        }
	    }
	 
	@RequestMapping(value = "/ajax/getContractStatusLIstFormContractFom", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getContractStatusLIstFormContractFom(@ModelAttribute Contract obj) {
		List<Contract> dataList = null;  
		try {
			dataList = contractService.getContractStatusType(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractStatusLIstFormContractFom : " + e.getMessage());
		}
		return dataList;
	}
	
	@RequestMapping(value = "/ajax/getExecutivesListForContractForm", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getExecutivesListForContractForm(@RequestBody Contract obj) {
		List<Contract> dataList = null;  
		System.out.println("DDDDDDDDDDDDDD"+ obj.getDepartment_fk());
		try {
			dataList = contractService.getExecutivesListForContractForm(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getExecutivesListForContractForm : " + e.getMessage());
		}
		return dataList;
	}
//	
//	   @PostMapping(
//		    value = "/ajax/getExecutivesListForContractForm",
//		    consumes = MediaType.APPLICATION_JSON_VALUE,
//		    produces = MediaType.APPLICATION_JSON_VALUE
//		)
//		public List<Contract> getExecutivesListForContractForm(@RequestBody Contract obj) throws Exception {
//		    return contractService.getExecutivesListForContractForm(obj);
//		}

	@RequestMapping(value = "/ajax/getHodList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getHodList(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			dataList = contractService.getHodList(obj);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getHodList : " + e.getMessage());
		}
		return dataList;
	}
	
	
	@RequestMapping(value = "/ajax/getDyHodList", method = {RequestMethod.GET,RequestMethod.POST},produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Contract> getDyHodList(@ModelAttribute Contract obj,HttpSession session) {
		List<Contract> dataList = null;  
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			dataList = contractService.getDyHodList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDyHodList : " + e.getMessage());
		}
		return dataList;
	}
	
//	@RequestMapping(value = "/export-contract", method = {RequestMethod.GET,RequestMethod.POST})
//	public void exportContract(HttpServletRequest request, HttpServletResponse response,HttpSession session,@ModelAttribute Contract contract,RedirectAttributes attributes){
//	//	ModelAndView view = new ModelAndView(PageConstants.contractGrid);
//		try {
//			String user_Id = (String) session.getAttribute("userId");
//			String userRoleCode = (String) session.getAttribute("userRoleNameFk");
//	
//			contract.setUser_id(user_Id);
//			contract.setUser_role_code(userRoleCode);
//			
//			//view.setViewName("redirect:/contract");
//			
//			List<Contract> dataList = contractService.contractListForExport(contract);  
//			List<Contract> revisionsDataList = contractService.contractRevisionsList(contract); 
//			List<Contract> bgDataList = contractService.contractBGList(contract); 
//			List<Contract> insuranceDataList = contractService.contractInsuranceList(contract); 
//			List<Contract> milestoneDataList = contractService.contractMilestoneList(contract); 
//			
//			
//			if(dataList != null && dataList.size() > 0){
//	            XSSFWorkbook  workBook = new XSSFWorkbook ();
//	            XSSFSheet contractsSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Contract"));
//	            XSSFSheet revisionsSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Revision Details"));
//	            XSSFSheet bgSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("BG"));
//	            XSSFSheet insuranceSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Insurance"));
//	            XSSFSheet milestoneSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Milestone"));
//	            
//		        workBook.setSheetOrder(contractsSheet.getSheetName(), 0);
//		        workBook.setSheetOrder(revisionsSheet.getSheetName(), 1);
//		        workBook.setSheetOrder(bgSheet.getSheetName(), 2);
//		        workBook.setSheetOrder(insuranceSheet.getSheetName(), 3);
//		        workBook.setSheetOrder(milestoneSheet.getSheetName(), 4);
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
//		        CellStyle sectioncostStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        CellStyle sectionunitsStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        
//		        CellStyle centerStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        CellStyle rightStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
//		        
//		        
//	            XSSFRow headingRow = contractsSheet.createRow(0);
//	            String headerString = "Work^Contract ID^Contract Name^Contract Short Name^Contractor^Department^HOD^DY HOD^Bank Funded(Yes/No)^Bank Name^Type of Review^Notice Inviting Tender^Contract Type^Scope of Contract"
//	            		+ "^Estimated Cost\n(Rs in Lakhs)^Planned Date of Award^Awarded Cost\n(Rs in Lakhs)^LOA Letter Number^LOA Date^CA NO^CA Date^Date of Start^DOC^"
//	            		+ "Actual Completion Date^Final Taking over by Client^Date of issue of Completion Certificate^Date of Payment of Final bill^Date of release of Final Retention / BG^Completion  Cost\n(Rs in Lakhs)^"
//	            		+ "End date of Defect Liability Period^Date of release of PBG^Date of Contract Closure^Contract Status^Status of Work^Bank Guarantee Requried^Insurance Requried^Tally Head";
//	            
//	            String[] headerStringArr = headerString.split("\\^");
//	            
//	            for (int i = 0; i < headerStringArr.length; i++) {		        	
//		        	Cell cell = headingRow.createCell(i);
//			        cell.setCellStyle(greenStyle);
//					cell.setCellValue(headerStringArr[i]);
//				}
//	            
//	            NumberFormat numberFormatter = new DecimalFormat("#0.00");
//	            
//	            short rowNo = 1;
//	            for (Contract obj : dataList) {
//	                XSSFRow row = contractsSheet.createRow(rowNo);
//	                int c = 0;
//	                
//	                Cell cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getWork_id_fk() +" - "+obj.getWork_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_id());
//					
//	                cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_name());
//					
//	                cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_short_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue((!StringUtils.isEmpty(obj.getContractor_id_fk())?obj.getContractor_id_fk():"")+(!StringUtils.isEmpty(obj.getContractor_name())?" - "+obj.getContractor_name():""));
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getHod_department());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getDesignation());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getDy_hod_designation());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getBank_funded());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getBank_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getType_of_review());	
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getNoticeinvitingtender());						
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_type_fk());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getScope_of_contract());
//					
//					String estimated_cost = "";
//					String estimated_cost_units = "";
//					if(!StringUtils.isEmpty(obj.getEstimated_cost())) {
//						estimated_cost = obj.getEstimated_cost();
//					}
//					if(!StringUtils.isEmpty(obj.getEstimated_cost_units())) {
//						estimated_cost_units = obj.getEstimated_cost_units();
//					}
//					
//					
//					
//					Double estimated_cost_value = null;
//					if(!StringUtils.isEmpty(estimated_cost) && !StringUtils.isEmpty(estimated_cost_units)) {
//						double val = (Double.parseDouble(estimated_cost)*Double.parseDouble(estimated_cost_units))/100000;
//						estimated_cost_value = Double.parseDouble(numberFormatter.format(val));
//					}
//					cell = row.createCell(c++);
//					cell.setCellStyle(rightStyle);
//					if(!StringUtils.isEmpty(estimated_cost_value)) {
//						cell.setCellValue(estimated_cost_value);
//					}else {
//						cell.setCellValue("");
//					}
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getPlanned_date_of_award());
//					
//					String awarded_cost = "";
//					String awarded_cost_units = "";
//					if(!StringUtils.isEmpty(obj.getAwarded_cost())) {
//						awarded_cost = obj.getAwarded_cost();
//					}
//					if(!StringUtils.isEmpty(obj.getAwarded_cost_units())) {
//						awarded_cost_units = obj.getAwarded_cost_units();
//					}
//					Double awarded_cost_value = null;
//					if(!StringUtils.isEmpty(awarded_cost) && !StringUtils.isEmpty(awarded_cost_units)) {
//						double val = (Double.parseDouble(awarded_cost)*Double.parseDouble(awarded_cost_units))/100000;
//						awarded_cost_value = Double.parseDouble(numberFormatter.format(val));
//					}
//					cell = row.createCell(c++);
//					cell.setCellStyle(rightStyle);
//					if(!StringUtils.isEmpty(awarded_cost_value)) {
//						cell.setCellValue(awarded_cost_value);
//					}else {
//						cell.setCellValue("");
//					}
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getLoa_letter_number());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getLoa_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getCa_no());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getCa_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getDate_of_start());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getDoc());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getActual_completion_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getFinal_takeover());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getCompletion_certificate_release());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getFinal_bill_release());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getRetention_money_release());
//					
//					String completed_cost = "";
//					String completed_cost_units = "";
//					if(!StringUtils.isEmpty(obj.getCompleted_cost())) {
//						completed_cost = obj.getCompleted_cost();
//					}
//					if(!StringUtils.isEmpty(obj.getCompleted_cost_units())) {
//						completed_cost_units = obj.getCompleted_cost_units();
//					}
//					Double completed_cost_value = null;
//					if(!StringUtils.isEmpty(completed_cost) && !StringUtils.isEmpty(completed_cost_units)) {
//						double val = (Double.parseDouble(completed_cost)*Double.parseDouble(completed_cost_units))/100000;
//						completed_cost_value = Double.parseDouble(numberFormatter.format(val));
//					}
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(rightStyle);
//					if(!StringUtils.isEmpty(completed_cost_value)) {
//						cell.setCellValue(completed_cost_value);
//					}else {
//						cell.setCellValue("");
//					}
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getDefect_liability_period());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getPbg_release());					
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getContract_closure_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getContract_status());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getContract_status_fk());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getBg_required());
//	                
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getInsurance_required());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getTally_head());
//	                
//	                rowNo++;
//	            }
//	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//	            	contractsSheet.setColumnWidth(columnIndex, 25 * 200);
//				}
//	            
//	            
//	            /********************************** Revision Details *********************************************************/
//	            
//	            headingRow = revisionsSheet.createRow(0);
//	            headerString = "Contract ID^Contract Short Name^Revision Number^Revised Contract Value\n(Rs in Lakhs)^Current^Revised DOC^Current^Approval by Bank(Yes/No)^Remarks";
//	            
//	            headerStringArr = headerString.split("\\^");
//	            
//	            for (int i = 0; i < headerStringArr.length; i++) {		        	
//		        	Cell cell = headingRow.createCell(i);
//			        cell.setCellStyle(greenStyle);
//					cell.setCellValue(headerStringArr[i]);
//				}
//	            
//	            rowNo = 1;
//	            for (Contract obj : revisionsDataList) {
//	                XSSFRow row = revisionsSheet.createRow(rowNo);
//	                int c = 0;
//	                
//	                Cell cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_id());
//					
//	                cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_short_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getRevision_number());
//					
//					String revised_amount = "";
//					String revised_amount_units = "";
//					if(!StringUtils.isEmpty(obj.getRevised_amount())) {
//						revised_amount = obj.getRevised_amount();
//					}
//					if(!StringUtils.isEmpty(obj.getRevised_amount_units())) {
//						revised_amount_units = obj.getRevised_amount_units();
//					}
//					Double revised_amount_value = null;
//					if(!StringUtils.isEmpty(revised_amount) && !StringUtils.isEmpty(revised_amount_units)) {
//						double val = (Double.parseDouble(revised_amount)*Double.parseDouble(revised_amount_units))/100000;
//						revised_amount_value = Double.parseDouble(numberFormatter.format(val));
//					}
//					cell = row.createCell(c++);
//					cell.setCellStyle(rightStyle);
//					if(!StringUtils.isEmpty(revised_amount_value)) {
//						cell.setCellValue(revised_amount_value);
//					}else {
//						cell.setCellValue("");
//					}
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getRevision_amounts_status());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getRevised_doc());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getRevision_status());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getApprovalbybank());				
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getRemarks());
//	                
//	                rowNo++;
//	            }
//	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//	            	revisionsSheet.setColumnWidth(columnIndex, 25 * 200);
//				}
//	            
//	            /********************************** BG Details *********************************************************/
//	            
//	            headingRow = bgSheet.createRow(0);
//	            headerString = "Contract ID^Contract Short Name^Code^BG Type^Issuing Bank^BG / FDR Number^Amount\n(Rs in Lakhs)^BG / FDR Date^Expiry Date^Release Date";
//	            
//	            headerStringArr = headerString.split("\\^");
//	            
//	            for (int i = 0; i < headerStringArr.length; i++) {		        	
//		        	Cell cell = headingRow.createCell(i);
//			        cell.setCellStyle(greenStyle);
//					cell.setCellValue(headerStringArr[i]);
//				}
//	            
//	            rowNo = 1;
//	            for (Contract obj : bgDataList) {
//	                XSSFRow row = bgSheet.createRow(rowNo);
//	                int c = 0;
//	                
//	                Cell cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_id());
//					
//	                cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_short_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(rightStyle);
//					cell.setCellValue(obj.getCode());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getBg_type_fk());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getIssuing_bank());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getBg_number());
//					
//					String bg_value = "";
//					String bg_value_units = "";
//					if(!StringUtils.isEmpty(obj.getBg_value())) {
//						bg_value = obj.getBg_value();
//					}
//					if(!StringUtils.isEmpty(obj.getBg_value_units())) {
//						bg_value_units = obj.getBg_value_units();
//					}
//					Double bg_value_value = null;
//					if(!StringUtils.isEmpty(bg_value) && !StringUtils.isEmpty(bg_value_units)) {
//						double val = (Double.parseDouble(bg_value)*Double.parseDouble(bg_value_units))/100000;
//						bg_value_value = Double.parseDouble(numberFormatter.format(val));
//					}
//					cell = row.createCell(c++);
//					cell.setCellStyle(rightStyle);
//					if(!StringUtils.isEmpty(bg_value_value)) {
//						cell.setCellValue(bg_value_value);
//					}else {
//						cell.setCellValue("");
//					}
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getBg_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getBg_valid_upto());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getRelease_date());
//					
//	                
//	                rowNo++;
//	            }
//	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//	            	bgSheet.setColumnWidth(columnIndex, 25 * 200);
//				}
//	            
//	            /********************************** Insurance Details *********************************************************/
//	            
//	            headingRow = insuranceSheet.createRow(0);
//	            headerString = "Contract ID^Contract Short Name^Insurance Type^Issuing Agency^Agency Address^Insurance Number^Insurance Value\n(Rs in Lakhs)^Valid Upto^Release";
//	            
//	            headerStringArr = headerString.split("\\^");
//	            
//	            for (int i = 0; i < headerStringArr.length; i++) {		        	
//		        	Cell cell = headingRow.createCell(i);
//			        cell.setCellStyle(greenStyle);
//					cell.setCellValue(headerStringArr[i]);
//				}
//	            
//	            rowNo = 1;
//	            for (Contract obj : insuranceDataList) {
//	                XSSFRow row = insuranceSheet.createRow(rowNo);
//	                int c = 0;
//	                
//	                Cell cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_id());
//					
//	                cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_short_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getInsurance_type_fk());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getIssuing_agency());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getAgency_address());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getInsurance_number());
//					
//					String insurance_value = "";
//					String insurance_value_units = "";
//					if(!StringUtils.isEmpty(obj.getInsurance_value())) {
//						insurance_value = obj.getInsurance_value();
//					}
//					if(!StringUtils.isEmpty(obj.getInsurance_value_units())) {
//						insurance_value_units = obj.getInsurance_value_units();
//					}
//					Double insurance_value_value = null;
//					if(!StringUtils.isEmpty(insurance_value) && !StringUtils.isEmpty(insurance_value_units)) {
//						double val = (Double.parseDouble(insurance_value)*Double.parseDouble(insurance_value_units))/100000;
//						insurance_value_value = Double.parseDouble(numberFormatter.format(val));
//					}
//					cell = row.createCell(c++);
//					cell.setCellStyle(rightStyle);
//					if(!StringUtils.isEmpty(insurance_value_value)) {
//						cell.setCellValue(insurance_value_value);
//					}else {
//						cell.setCellValue("");
//					}
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getInsurence_valid_upto());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getInsurance_status());
//					
//	                
//	                rowNo++;
//	            }
//	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//	            	insuranceSheet.setColumnWidth(columnIndex, 25 * 200);
//				}
//	            
//	            /********************************** Milestone Details *********************************************************/
//	            
//	            headingRow = milestoneSheet.createRow(0);
//	            headerString = "Contract ID^Contract Short Name^Milestone ID^Milestone Name^Milestone Date^Actual Date^Revision^Remarks ";
//	            
//	            headerStringArr = headerString.split("\\^");
//	            
//	            for (int i = 0; i < headerStringArr.length; i++) {		        	
//		        	Cell cell = headingRow.createCell(i);
//			        cell.setCellStyle(greenStyle);
//					cell.setCellValue(headerStringArr[i]);
//				}
//	            
//	            rowNo = 1;
//	            for (Contract obj : milestoneDataList) {
//	                XSSFRow row = milestoneSheet.createRow(rowNo);
//	                int c = 0;
//					
//					Cell cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_id());
//					
//	                cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getContract_short_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getMilestone_id());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getMilestone_name());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getMilestone_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(centerStyle);
//					cell.setCellValue(obj.getActual_date());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getRevision());
//					
//					cell = row.createCell(c++);
//					cell.setCellStyle(sectionStyle);
//					cell.setCellValue(obj.getRemarks());
//					
//	                rowNo++;
//	            }
//	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//	            	milestoneSheet.setColumnWidth(columnIndex, 25 * 200);
//				}
//	            
//	            /*******************************************************************************************/
//	            
//	            
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
//                Date date = new Date();
//                String fileName = "Contract_"+dateFormat.format(date);
//                
//	            try{
//	                /*FileOutputStream fos = new FileOutputStream(fileDirectory +fileName+".xls");
//	                workBook.write(fos);
//	                fos.flush();*/
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
//	               // attributes.addFlashAttribute("success",dataExportSucess);
//	            	//response.setContentType("application/vnd.ms-excel");
//	            	//response.setHeader("Content-Disposition", "attachment; filename=filename.xls");
//	            	//XSSFWorkbook  workbook = new XSSFWorkbook ();
//	            	// ...
//	            	// Now populate workbook the usual way.
//	            	// ...
//	            	//workbook.write(response.getOutputStream()); // Write workbook to response.
//	            	//workbook.close();
//	            }catch(FileNotFoundException e){
//	                //e.printStackTrace();
//	                attributes.addFlashAttribute("error",dataExportInvalid);
//	            }catch(IOException e){
//	                //e.printStackTrace();
//	                attributes.addFlashAttribute("error",dataExportError);
//	            }
//	         }else{
//	        	 attributes.addFlashAttribute("error",dataExportNoData);
//	         }
//		}catch(Exception e){	
//			e.printStackTrace();
//			logger.error("exportContract : "+e.getMessage());
//			attributes.addFlashAttribute("error", commonError);			
//		}
//		//return view;
//	}
//	
//	
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

	//===================================================

	@RequestMapping(value = "/export-contract", method = {RequestMethod.GET,RequestMethod.POST})
	public void exportContract(HttpServletRequest request, HttpServletResponse response,HttpSession session,@ModelAttribute Contract contract,RedirectAttributes attributes){
	//	ModelAndView view = new ModelAndView(PageConstants.contractGrid);
		try {
			
			System.out.println("Controller Hit...................................");
			
		    String userId = (String) session.getAttribute("userId");
		    String userName = (String) session.getAttribute("userName");
		//	String userRoleCode = (String) session.getAttribute("USER_ROLE_CODE");
			User uObj = (User) session.getAttribute("user");
			contract.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			contract.setUser_id(userId);
			//contract.setUser_role_code(userRoleCode);
		//	view.setViewName("redirect:/contract");
			
			System.out.println("Contractt......." + contract);
			
			List<Contract> dataList = contractService.contractListForExport(contract);  
			List<Contract> revisionsDataList = contractService.contractRevisionsList(contract); 
			List<Contract> bgDataList = contractService.contractBGList(contract); 
			List<Contract> insuranceDataList = contractService.contractInsuranceList(contract); 
			List<Contract> milestoneDataList = contractService.contractMilestoneList(contract); 
			
			
			if(dataList != null && dataList.size() > 0){
	            XSSFWorkbook  workBook = new XSSFWorkbook ();
	            XSSFSheet contractsSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Contract"));
	            XSSFSheet revisionsSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Revision Details"));
	            XSSFSheet bgSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("BG"));
	            XSSFSheet insuranceSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Insurance"));
	            XSSFSheet milestoneSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Milestone"));
	            
		        workBook.setSheetOrder(contractsSheet.getSheetName(), 0);
		        workBook.setSheetOrder(revisionsSheet.getSheetName(), 1);
		        workBook.setSheetOrder(bgSheet.getSheetName(), 2);
		        workBook.setSheetOrder(insuranceSheet.getSheetName(), 3);
		        workBook.setSheetOrder(milestoneSheet.getSheetName(), 4);
		        
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
		        CellStyle sectioncostStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        CellStyle sectionunitsStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        
		        CellStyle centerStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.CENTER,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        CellStyle rightStyle = cellFormating(workBook,whiteRGB,HorizontalAlignment.RIGHT,VerticalAlignment.CENTER,isWrapText,isBoldText,isItalicText,fontSize,fontName);
		        
		        
	            XSSFRow headingRow = contractsSheet.createRow(0);
	            String headerString = "Work^Contract ID^Contract Name^Contract Short Name^Contractor^Department^HOD^DY HOD^Bank Funded(Yes/No)^Bank Name^Type of Review^Notice Inviting Tender^Contract Type^Scope of Contract"
	            		+ "^Estimated Cost\n(Rs in Lakhs)^Planned Date of Award^Awarded Cost\n(Rs in Lakhs)^LOA Letter Number^LOA Date^CA NO^CA Date^Date of Start^DOC^"
	            		+ "Actual Completion Date^Final Taking over by Client^Date of issue of Completion Certificate^Date of Payment of Final bill^Date of release of Final Retention / BG^Completion  Cost\n(Rs in Lakhs)^"
	            		+ "End date of Defect Liability Period^Date of release of PBG^Date of Contract Closure^Contract Status^Status of Work^Bank Guarantee Requried^Insurance Requried^Tally Head";
	            
	            String[] headerStringArr = headerString.split("\\^");
	            
	            for (int i = 0; i < headerStringArr.length; i++) {		        	
		        	Cell cell = headingRow.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(headerStringArr[i]);
				}
	            
	            NumberFormat numberFormatter = new DecimalFormat("#0.00");
	            
	            short rowNo = 1;
	            for (Contract obj : dataList) {
	                XSSFRow row = contractsSheet.createRow(rowNo);
	                int c = 0;
	                
	                Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getWork_id_fk() +" - "+obj.getWork_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_id());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_name());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue((!StringUtils.isEmpty(obj.getContractor_id_fk())?obj.getContractor_id_fk():"")+(!StringUtils.isEmpty(obj.getContractor_name())?" - "+obj.getContractor_name():""));
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getHod_department());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDesignation());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getDy_hod_designation());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getBank_funded());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getBank_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getType_of_review());	
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getNoticeinvitingtender());						
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_type_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getScope_of_contract());
					
					String estimated_cost = "";
					String estimated_cost_units = "";
					if(!StringUtils.isEmpty(obj.getEstimated_cost())) {
						estimated_cost = obj.getEstimated_cost();
					}
					if(!StringUtils.isEmpty(obj.getEstimated_cost_units())) {
						estimated_cost_units = obj.getEstimated_cost_units();
					}
					
					
					
					Double estimated_cost_value = null;
					if(!StringUtils.isEmpty(estimated_cost) && !StringUtils.isEmpty(estimated_cost_units)) {
						double val = (Double.parseDouble(estimated_cost)*Double.parseDouble(estimated_cost_units))/100000;
						estimated_cost_value = Double.parseDouble(numberFormatter.format(val));
					}
					cell = row.createCell(c++);
					cell.setCellStyle(rightStyle);
					if(!StringUtils.isEmpty(estimated_cost_value)) {
						cell.setCellValue(estimated_cost_value);
					}else {
						cell.setCellValue("");
					}
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getPlanned_date_of_award());
					
					String awarded_cost = "";
					String awarded_cost_units = "";
					if(!StringUtils.isEmpty(obj.getAwarded_cost())) {
						awarded_cost = obj.getAwarded_cost();
					}
					if(!StringUtils.isEmpty(obj.getAwarded_cost_units())) {
						awarded_cost_units = obj.getAwarded_cost_units();
					}
					Double awarded_cost_value = null;
					if(!StringUtils.isEmpty(awarded_cost) && !StringUtils.isEmpty(awarded_cost_units)) {
						double val = (Double.parseDouble(awarded_cost)*Double.parseDouble(awarded_cost_units))/100000;
						awarded_cost_value = Double.parseDouble(numberFormatter.format(val));
					}
					cell = row.createCell(c++);
					cell.setCellStyle(rightStyle);
					if(!StringUtils.isEmpty(awarded_cost_value)) {
						cell.setCellValue(awarded_cost_value);
					}else {
						cell.setCellValue("");
					}
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getLoa_letter_number());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getLoa_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getCa_no());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getCa_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getDate_of_start());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getDoc());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getActual_completion_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getFinal_takeover());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getCompletion_certificate_release());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getFinal_bill_release());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getRetention_money_release());
					
					String completed_cost = "";
					String completed_cost_units = "";
					if(!StringUtils.isEmpty(obj.getCompleted_cost())) {
						completed_cost = obj.getCompleted_cost();
					}
					if(!StringUtils.isEmpty(obj.getCompleted_cost_units())) {
						completed_cost_units = obj.getCompleted_cost_units();
					}
					Double completed_cost_value = null;
					if(!StringUtils.isEmpty(completed_cost) && !StringUtils.isEmpty(completed_cost_units)) {
						double val = (Double.parseDouble(completed_cost)*Double.parseDouble(completed_cost_units))/100000;
						completed_cost_value = Double.parseDouble(numberFormatter.format(val));
					}
					
					cell = row.createCell(c++);
					cell.setCellStyle(rightStyle);
					if(!StringUtils.isEmpty(completed_cost_value)) {
						cell.setCellValue(completed_cost_value);
					}else {
						cell.setCellValue("");
					}
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getDefect_liability_period());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getPbg_release());					
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getContract_closure_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getContract_status());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getContract_status_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getBg_required());
	                
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getInsurance_required());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getTally_head());
	                
	                rowNo++;
	            }
	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
	            	contractsSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	            
	            
	            /********************************** Revision Details *********************************************************/
	            
	            headingRow = revisionsSheet.createRow(0);
	            headerString = "Contract ID^Contract Short Name^Revision Number^Revised Contract Value\n(Rs in Lakhs)^Current^Revised DOC^Current^Approval by Bank(Yes/No)^Remarks";
	            
	            headerStringArr = headerString.split("\\^");
	            
	            for (int i = 0; i < headerStringArr.length; i++) {		        	
		        	Cell cell = headingRow.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(headerStringArr[i]);
				}
	            
	            rowNo = 1;
	            for (Contract obj : revisionsDataList) {
	                XSSFRow row = revisionsSheet.createRow(rowNo);
	                int c = 0;
	                
	                Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_id());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRevision_number());
					
					String revised_amount = "";
					String revised_amount_units = "";
					if(!StringUtils.isEmpty(obj.getRevised_amount())) {
						revised_amount = obj.getRevised_amount();
					}
					if(!StringUtils.isEmpty(obj.getRevised_amount_units())) {
						revised_amount_units = obj.getRevised_amount_units();
					}
					Double revised_amount_value = null;
					if(!StringUtils.isEmpty(revised_amount) && !StringUtils.isEmpty(revised_amount_units)) {
						double val = (Double.parseDouble(revised_amount)*Double.parseDouble(revised_amount_units))/100000;
						revised_amount_value = Double.parseDouble(numberFormatter.format(val));
					}
					cell = row.createCell(c++);
					cell.setCellStyle(rightStyle);
					if(!StringUtils.isEmpty(revised_amount_value)) {
						cell.setCellValue(revised_amount_value);
					}else {
						cell.setCellValue("");
					}
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getRevision_amounts_status());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getRevised_doc());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getRevision_status());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getApprovalbybank());				
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRemarks());
	                
	                rowNo++;
	            }
	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
	            	revisionsSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	            
	            /********************************** BG Details *********************************************************/
	            
	            headingRow = bgSheet.createRow(0);
	            headerString = "Contract ID^Contract Short Name^Code^BG Type^Issuing Bank^BG / FDR Number^Amount\n(Rs in Lakhs)^BG / FDR Date^Expiry Date^Release Date";
	            
	            headerStringArr = headerString.split("\\^");
	            
	            for (int i = 0; i < headerStringArr.length; i++) {		        	
		        	Cell cell = headingRow.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(headerStringArr[i]);
				}
	            
	            rowNo = 1;
	            for (Contract obj : bgDataList) {
	                XSSFRow row = bgSheet.createRow(rowNo);
	                int c = 0;
	                
	                Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_id());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(rightStyle);
					cell.setCellValue(obj.getCode());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getBg_type_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getIssuing_bank());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getBg_number());
					
					String bg_value = "";
					String bg_value_units = "";
					if(!StringUtils.isEmpty(obj.getBg_value())) {
						bg_value = obj.getBg_value();
					}
					if(!StringUtils.isEmpty(obj.getBg_value_units())) {
						bg_value_units = obj.getBg_value_units();
					}
					Double bg_value_value = null;
					if(!StringUtils.isEmpty(bg_value) && !StringUtils.isEmpty(bg_value_units)) {
						double val = (Double.parseDouble(bg_value)*Double.parseDouble(bg_value_units))/100000;
						bg_value_value = Double.parseDouble(numberFormatter.format(val));
					}
					cell = row.createCell(c++);
					cell.setCellStyle(rightStyle);
					if(!StringUtils.isEmpty(bg_value_value)) {
						cell.setCellValue(bg_value_value);
					}else {
						cell.setCellValue("");
					}
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getBg_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getBg_valid_upto());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getRelease_date());
					
	                
	                rowNo++;
	            }
	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
	            	bgSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	            
	            /********************************** Insurance Details *********************************************************/
	            
	            headingRow = insuranceSheet.createRow(0);
	            headerString = "Contract ID^Contract Short Name^Insurance Type^Issuing Agency^Agency Address^Insurance Number^Insurance Value\n(Rs in Lakhs)^Valid Upto^Release";
	            
	            headerStringArr = headerString.split("\\^");
	            
	            for (int i = 0; i < headerStringArr.length; i++) {		        	
		        	Cell cell = headingRow.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(headerStringArr[i]);
				}
	            
	            rowNo = 1;
	            for (Contract obj : insuranceDataList) {
	                XSSFRow row = insuranceSheet.createRow(rowNo);
	                int c = 0;
	                
	                Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_id());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getInsurance_type_fk());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getIssuing_agency());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getAgency_address());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getInsurance_number());
					
					String insurance_value = "";
					String insurance_value_units = "";
					if(!StringUtils.isEmpty(obj.getInsurance_value())) {
						insurance_value = obj.getInsurance_value();
					}
					if(!StringUtils.isEmpty(obj.getInsurance_value_units())) {
						insurance_value_units = obj.getInsurance_value_units();
					}
					Double insurance_value_value = null;
					if(!StringUtils.isEmpty(insurance_value) && !StringUtils.isEmpty(insurance_value_units)) {
						double val = (Double.parseDouble(insurance_value)*Double.parseDouble(insurance_value_units))/100000;
						insurance_value_value = Double.parseDouble(numberFormatter.format(val));
					}
					cell = row.createCell(c++);
					cell.setCellStyle(rightStyle);
					if(!StringUtils.isEmpty(insurance_value_value)) {
						cell.setCellValue(insurance_value_value);
					}else {
						cell.setCellValue("");
					}
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getInsurence_valid_upto());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getInsurance_status());
					
	                
	                rowNo++;
	            }
	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
	            	insuranceSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	            
	            /********************************** Milestone Details *********************************************************/
	            
	            headingRow = milestoneSheet.createRow(0);
	            headerString = "Contract ID^Contract Short Name^Milestone ID^Milestone Name^Milestone Date^Actual Date^Revision^Remarks ";
	            
	            headerStringArr = headerString.split("\\^");
	            
	            for (int i = 0; i < headerStringArr.length; i++) {		        	
		        	Cell cell = headingRow.createCell(i);
			        cell.setCellStyle(greenStyle);
					cell.setCellValue(headerStringArr[i]);
				}
	            
	            rowNo = 1;
	            for (Contract obj : milestoneDataList) {
	                XSSFRow row = milestoneSheet.createRow(rowNo);
	                int c = 0;
					
					Cell cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_id());
					
	                cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getContract_short_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getMilestone_id());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getMilestone_name());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getMilestone_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(centerStyle);
					cell.setCellValue(obj.getActual_date());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRevision());
					
					cell = row.createCell(c++);
					cell.setCellStyle(sectionStyle);
					cell.setCellValue(obj.getRemarks());
					
	                rowNo++;
	            }
	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
	            	milestoneSheet.setColumnWidth(columnIndex, 25 * 200);
				}
	            
	            /*******************************************************************************************/
	            
	            
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
                Date date = new Date();
                String fileName = "Contract_"+dateFormat.format(date);
                
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
	            	
	                
	                attributes.addFlashAttribute("success",dataExportSucess);
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
	            }catch(IOException e){
	                //e.printStackTrace();
	                attributes.addFlashAttribute("error",dataExportError);
	            }
	         }else{
	        	 attributes.addFlashAttribute("error",dataExportNoData);
	         }
		}catch(Exception e){	
			e.printStackTrace();
			logger.error("exportContract : "+e.getMessage());
			attributes.addFlashAttribute("error", commonError);			
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
	
	
//	
//
//@RequestMapping(value = "/export-contract", method = {RequestMethod.GET, RequestMethod.POST})
//public void exportContract(
//    HttpServletRequest request, 
//    HttpServletResponse response,
//    HttpSession session,
//    @RequestParam(required = false) String dy_hod_designation,
//    @RequestParam(required = false) String designation,
//    @RequestParam(required = false) String contractor_id_fk,
//    @RequestParam(required = false) String contract_status_fk,
//    @RequestParam(required = false) String contract_status,
//    @RequestParam(required = false) String project_id_fk,
//    @RequestParam(required = false) String searchStr
//) {
//    try {
//        String userId = (String) session.getAttribute("USER_ID");
//        String userName = (String) session.getAttribute("USER_NAME");
//        String userRoleCode = (String) session.getAttribute("USER_ROLE_CODE");
//        
//        // Create contract object with filters
//        Contract contract = new Contract();
//        contract.setUser_id(userId);
//        contract.setUser_role_code(userRoleCode);
//        contract.setDy_hod_designation(dy_hod_designation);
//        contract.setDesignation(designation);
//        contract.setContractor_id_fk(contractor_id_fk);
//        contract.setContract_status_fk(contract_status_fk);
//        contract.setContract_status(contract_status);
//        contract.setProject_id_fk(project_id_fk);
//        contract.setSearchStr(searchStr);
//        
//        // Fetch data based on filters
//        List<Contract> dataList = contractService.contractListForExport(contract);  
//        List<Contract> revisionsDataList = contractService.contractRevisionsList(contract); 
//        List<Contract> bgDataList = contractService.contractBGList(contract); 
//        List<Contract> insuranceDataList = contractService.contractInsuranceList(contract); 
//        List<Contract> milestoneDataList = contractService.contractMilestoneList(contract);
//        
//        if(dataList == null || dataList.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"error\": \"No data available for export\"}");
//            return;
//        }
//        
//        try (XSSFWorkbook workBook = new XSSFWorkbook()) {
//            // Create sheets
//            XSSFSheet contractsSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Contract"));
//            XSSFSheet revisionsSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Revision Details"));
//            XSSFSheet bgSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("BG"));
//            XSSFSheet insuranceSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Insurance"));
//            XSSFSheet milestoneSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Milestone"));
//            
//            workBook.setSheetOrder(contractsSheet.getSheetName(), 0);
//            workBook.setSheetOrder(revisionsSheet.getSheetName(), 1);
//            workBook.setSheetOrder(bgSheet.getSheetName(), 2);
//            workBook.setSheetOrder(insuranceSheet.getSheetName(), 3);
//            workBook.setSheetOrder(milestoneSheet.getSheetName(), 4);
//            
//            // Define colors
//            byte[] blueRGB = new byte[]{(byte)0, (byte)176, (byte)240};
//            byte[] yellowRGB = new byte[]{(byte)255, (byte)192, (byte)0};
//            byte[] greenRGB = new byte[]{(byte)146, (byte)208, (byte)80};
//            byte[] redRGB = new byte[]{(byte)255, (byte)0, (byte)0};
//            byte[] whiteRGB = new byte[]{(byte)255, (byte)255, (byte)255};
//            
//            // Create styles
//            boolean isWrapText = true;
//            boolean isBoldText = true;
//            boolean isItalicText = false; 
//            int fontSize = 11;
//            String fontName = "Times New Roman";
//            
//            CellStyle blueStyle = cellFormating(workBook, blueRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle yellowStyle = cellFormating(workBook, yellowRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle greenStyle = cellFormating(workBook, greenRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle redStyle = cellFormating(workBook, redRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle whiteStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle indexWhiteStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            
//            isWrapText = true;
//            isBoldText = false;
//            isItalicText = false; 
//            fontSize = 9;
//            
//            CellStyle sectionStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle sectioncostStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle sectionunitsStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle centerStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            CellStyle rightStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, isWrapText, isBoldText, isItalicText, fontSize, fontName);
//            
//            /********************************** Contract Sheet *********************************************************/
//            
//            XSSFRow headingRow = contractsSheet.createRow(0);
//            String headerString = "Work^Contract ID^Contract Name^Contract Short Name^Contractor^Department^HOD^DY HOD^Bank Funded(Yes/No)^Bank Name^Type of Review^Notice Inviting Tender^Contract Type^Scope of Contract"
//                    + "^Estimated Cost\n(Rs in Lakhs)^Planned Date of Award^Awarded Cost\n(Rs in Lakhs)^LOA Letter Number^LOA Date^CA NO^CA Date^Date of Start^DOC^"
//                    + "Actual Completion Date^Final Taking over by Client^Date of issue of Completion Certificate^Date of Payment of Final bill^Date of release of Final Retention / BG^Completion  Cost\n(Rs in Lakhs)^"
//                    + "End date of Defect Liability Period^Date of release of PBG^Date of Contract Closure^Contract Status^Status of Work^Bank Guarantee Requried^Insurance Requried^Tally Head";
//            
//            String[] headerStringArr = headerString.split("\\^");
//            
//            for (int i = 0; i < headerStringArr.length; i++) {              
//                Cell cell = headingRow.createCell(i);
//                cell.setCellStyle(greenStyle);
//                cell.setCellValue(headerStringArr[i]);
//            }
//            
//            NumberFormat numberFormatter = new DecimalFormat("#0.00");
//            
//            short rowNo = 1;
//            for (Contract obj : dataList) {
//                XSSFRow row = contractsSheet.createRow(rowNo);
//                int c = 0;
//                
//                Cell cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getWork_id_fk() +" - "+obj.getWork_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_id());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_short_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue((!StringUtils.isEmpty(obj.getContractor_id_fk())?obj.getContractor_id_fk():"")+(!StringUtils.isEmpty(obj.getContractor_name())?" - "+obj.getContractor_name():""));
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getHod_department());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getDesignation());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getDy_hod_designation());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getBank_funded());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getBank_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getType_of_review());   
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getNoticeinvitingtender());                      
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_type_fk());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getScope_of_contract());
//                
//                // Estimated Cost
//                String estimated_cost = "";
//                String estimated_cost_units = "";
//                if(!StringUtils.isEmpty(obj.getEstimated_cost())) {
//                    estimated_cost = obj.getEstimated_cost();
//                }
//                if(!StringUtils.isEmpty(obj.getEstimated_cost_units())) {
//                    estimated_cost_units = obj.getEstimated_cost_units();
//                }
//                
//                Double estimated_cost_value = null;
//                if(!StringUtils.isEmpty(estimated_cost) && !StringUtils.isEmpty(estimated_cost_units)) {
//                    double val = (Double.parseDouble(estimated_cost)*Double.parseDouble(estimated_cost_units))/100000;
//                    estimated_cost_value = Double.parseDouble(numberFormatter.format(val));
//                }
//                cell = row.createCell(c++);
//                cell.setCellStyle(rightStyle);
//                if(!StringUtils.isEmpty(estimated_cost_value)) {
//                    cell.setCellValue(estimated_cost_value);
//                }else {
//                    cell.setCellValue("");
//                }
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getPlanned_date_of_award());
//                
//                // Awarded Cost
//                String awarded_cost = "";
//                String awarded_cost_units = "";
//                if(!StringUtils.isEmpty(obj.getAwarded_cost())) {
//                    awarded_cost = obj.getAwarded_cost();
//                }
//                if(!StringUtils.isEmpty(obj.getAwarded_cost_units())) {
//                    awarded_cost_units = obj.getAwarded_cost_units();
//                }
//                Double awarded_cost_value = null;
//                if(!StringUtils.isEmpty(awarded_cost) && !StringUtils.isEmpty(awarded_cost_units)) {
//                    double val = (Double.parseDouble(awarded_cost)*Double.parseDouble(awarded_cost_units))/100000;
//                    awarded_cost_value = Double.parseDouble(numberFormatter.format(val));
//                }
//                cell = row.createCell(c++);
//                cell.setCellStyle(rightStyle);
//                if(!StringUtils.isEmpty(awarded_cost_value)) {
//                    cell.setCellValue(awarded_cost_value);
//                }else {
//                    cell.setCellValue("");
//                }
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getLoa_letter_number());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getLoa_date());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getCa_no());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getCa_date());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getDate_of_start());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getDoc());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getActual_completion_date());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getFinal_takeover());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getCompletion_certificate_release());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getFinal_bill_release());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getRetention_money_release());
//                
//                // Completed Cost
//                String completed_cost = "";
//                String completed_cost_units = "";
//                if(!StringUtils.isEmpty(obj.getCompleted_cost())) {
//                    completed_cost = obj.getCompleted_cost();
//                }
//                if(!StringUtils.isEmpty(obj.getCompleted_cost_units())) {
//                    completed_cost_units = obj.getCompleted_cost_units();
//                }
//                Double completed_cost_value = null;
//                if(!StringUtils.isEmpty(completed_cost) && !StringUtils.isEmpty(completed_cost_units)) {
//                    double val = (Double.parseDouble(completed_cost)*Double.parseDouble(completed_cost_units))/100000;
//                    completed_cost_value = Double.parseDouble(numberFormatter.format(val));
//                }
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(rightStyle);
//                if(!StringUtils.isEmpty(completed_cost_value)) {
//                    cell.setCellValue(completed_cost_value);
//                }else {
//                    cell.setCellValue("");
//                }
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getDefect_liability_period());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getPbg_release());                    
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getContract_closure_date());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getContract_status());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getContract_status_fk());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getBg_required());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getInsurance_required());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getTally_head());
//                
//                rowNo++;
//            }
//            
//            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//                contractsSheet.setColumnWidth(columnIndex, 25 * 200);
//            }
//            
//            /********************************** Revision Details *********************************************************/
//            
//            headingRow = revisionsSheet.createRow(0);
//            headerString = "Contract ID^Contract Short Name^Revision Number^Revised Contract Value\n(Rs in Lakhs)^Current^Revised DOC^Current^Approval by Bank(Yes/No)^Remarks";
//            headerStringArr = headerString.split("\\^");
//            
//            for (int i = 0; i < headerStringArr.length; i++) {              
//                Cell cell = headingRow.createCell(i);
//                cell.setCellStyle(greenStyle);
//                cell.setCellValue(headerStringArr[i]);
//            }
//            
//            rowNo = 1;
//            for (Contract obj : revisionsDataList) {
//                XSSFRow row = revisionsSheet.createRow(rowNo);
//                int c = 0;
//                
//                Cell cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_id());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_short_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getRevision_number());
//                
//                String revised_amount = "";
//                String revised_amount_units = "";
//                if(!StringUtils.isEmpty(obj.getRevised_amount())) {
//                    revised_amount = obj.getRevised_amount();
//                }
//                if(!StringUtils.isEmpty(obj.getRevised_amount_units())) {
//                    revised_amount_units = obj.getRevised_amount_units();
//                }
//                Double revised_amount_value = null;
//                if(!StringUtils.isEmpty(revised_amount) && !StringUtils.isEmpty(revised_amount_units)) {
//                    double val = (Double.parseDouble(revised_amount)*Double.parseDouble(revised_amount_units))/100000;
//                    revised_amount_value = Double.parseDouble(numberFormatter.format(val));
//                }
//                cell = row.createCell(c++);
//                cell.setCellStyle(rightStyle);
//                if(!StringUtils.isEmpty(revised_amount_value)) {
//                    cell.setCellValue(revised_amount_value);
//                }else {
//                    cell.setCellValue("");
//                }
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getRevision_amounts_status());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getRevised_doc());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getRevision_status());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getApprovalbybank());              
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getRemarks());
//                
//                rowNo++;
//            }
//            
//            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//                revisionsSheet.setColumnWidth(columnIndex, 25 * 200);
//            }
//            
//            /********************************** BG Details *********************************************************/
//            
//            headingRow = bgSheet.createRow(0);
//            headerString = "Contract ID^Contract Short Name^Code^BG Type^Issuing Bank^BG / FDR Number^Amount\n(Rs in Lakhs)^BG / FDR Date^Expiry Date^Release Date";
//            headerStringArr = headerString.split("\\^");
//            
//            for (int i = 0; i < headerStringArr.length; i++) {              
//                Cell cell = headingRow.createCell(i);
//                cell.setCellStyle(greenStyle);
//                cell.setCellValue(headerStringArr[i]);
//            }
//            
//            rowNo = 1;
//            for (Contract obj : bgDataList) {
//                XSSFRow row = bgSheet.createRow(rowNo);
//                int c = 0;
//                
//                Cell cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_id());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_short_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(rightStyle);
//                cell.setCellValue(obj.getCode());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getBg_type_fk());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getIssuing_bank());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getBg_number());
//                
//                String bg_value = "";
//                String bg_value_units = "";
//                if(!StringUtils.isEmpty(obj.getBg_value())) {
//                    bg_value = obj.getBg_value();
//                }
//                if(!StringUtils.isEmpty(obj.getBg_value_units())) {
//                    bg_value_units = obj.getBg_value_units();
//                }
//                Double bg_value_value = null;
//                if(!StringUtils.isEmpty(bg_value) && !StringUtils.isEmpty(bg_value_units)) {
//                    double val = (Double.parseDouble(bg_value)*Double.parseDouble(bg_value_units))/100000;
//                    bg_value_value = Double.parseDouble(numberFormatter.format(val));
//                }
//                cell = row.createCell(c++);
//                cell.setCellStyle(rightStyle);
//                if(!StringUtils.isEmpty(bg_value_value)) {
//                    cell.setCellValue(bg_value_value);
//                }else {
//                    cell.setCellValue("");
//                }
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getBg_date());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getBg_valid_upto());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getRelease_date());
//                
//                rowNo++;
//            }
//            
//            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//                bgSheet.setColumnWidth(columnIndex, 25 * 200);
//            }
//            
//            /********************************** Insurance Details *********************************************************/
//            
//            headingRow = insuranceSheet.createRow(0);
//            headerString = "Contract ID^Contract Short Name^Insurance Type^Issuing Agency^Agency Address^Insurance Number^Insurance Value\n(Rs in Lakhs)^Valid Upto^Release";
//            headerStringArr = headerString.split("\\^");
//            
//            for (int i = 0; i < headerStringArr.length; i++) {              
//                Cell cell = headingRow.createCell(i);
//                cell.setCellStyle(greenStyle);
//                cell.setCellValue(headerStringArr[i]);
//            }
//            
//            rowNo = 1;
//            for (Contract obj : insuranceDataList) {
//                XSSFRow row = insuranceSheet.createRow(rowNo);
//                int c = 0;
//                
//                Cell cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_id());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_short_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getInsurance_type_fk());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getIssuing_agency());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getAgency_address());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getInsurance_number());
//                
//                String insurance_value = "";
//                String insurance_value_units = "";
//                if(!StringUtils.isEmpty(obj.getInsurance_value())) {
//                    insurance_value = obj.getInsurance_value();
//                }
//                if(!StringUtils.isEmpty(obj.getInsurance_value_units())) {
//                    insurance_value_units = obj.getInsurance_value_units();
//                }
//                Double insurance_value_value = null;
//                if(!StringUtils.isEmpty(insurance_value) && !StringUtils.isEmpty(insurance_value_units)) {
//                    double val = (Double.parseDouble(insurance_value)*Double.parseDouble(insurance_value_units))/100000;
//                    insurance_value_value = Double.parseDouble(numberFormatter.format(val));
//                }
//                cell = row.createCell(c++);
//                cell.setCellStyle(rightStyle);
//                if(!StringUtils.isEmpty(insurance_value_value)) {
//                    cell.setCellValue(insurance_value_value);
//                }else {
//                    cell.setCellValue("");
//                }
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getInsurence_valid_upto());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getInsurance_status());
//                
//                rowNo++;
//            }
//            
//            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//                insuranceSheet.setColumnWidth(columnIndex, 25 * 200);
//            }
//            
//            /********************************** Milestone Details *********************************************************/
//            
//            headingRow = milestoneSheet.createRow(0);
//            headerString = "Contract ID^Contract Short Name^Milestone ID^Milestone Name^Milestone Date^Actual Date^Revision^Remarks ";
//            headerStringArr = headerString.split("\\^");
//            
//            for (int i = 0; i < headerStringArr.length; i++) {              
//                Cell cell = headingRow.createCell(i);
//                cell.setCellStyle(greenStyle);
//                cell.setCellValue(headerStringArr[i]);
//            }
//            
//            rowNo = 1;
//            for (Contract obj : milestoneDataList) {
//                XSSFRow row = milestoneSheet.createRow(rowNo);
//                int c = 0;
//                
//                Cell cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_id());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getContract_short_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getMilestone_id());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getMilestone_name());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getMilestone_date());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(centerStyle);
//                cell.setCellValue(obj.getActual_date());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getRevision());
//                
//                cell = row.createCell(c++);
//                cell.setCellStyle(sectionStyle);
//                cell.setCellValue(obj.getRemarks());
//                
//                rowNo++;
//            }
//            
//            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//                milestoneSheet.setColumnWidth(columnIndex, 25 * 200);
//            }
//            
//            /*******************************************************************************************/
//            
//            // Generate filename with timestamp
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
//            Date date = new Date();
//            String fileName = "Contract_" + dateFormat.format(date);
//            
//            // Set response headers
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
//            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//            response.setHeader("Pragma", "no-cache");
//            response.setHeader("Expires", "0");
//            
//            // Write workbook to response output stream
//            workBook.write(response.getOutputStream());
//            response.getOutputStream().flush();
//            
//            logger.info("Contract export successful: " + fileName);
//            
//        } catch (IOException e) {
//            logger.error("Error writing Excel file to response: " + e.getMessage(), e);
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.setContentType("application/json");
//            try {
//                response.getWriter().write("{\"error\": \"Error generating export file\"}");
//            } catch (IOException ex) {
//                logger.error("Error writing error response", ex);
//            }
//        }
//        
//    } catch (Exception e) {
//        e.printStackTrace();
//        logger.error("exportContract error: " + e.getMessage(), e);
//        try {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"error\": \"An error occurred during export\"}");
//        } catch (IOException ex) {
//            logger.error("Error writing error response", ex);
//        }
//    }
//}
//
//private CellStyle cellFormating(XSSFWorkbook workBook, byte[] rgb, HorizontalAlignment hAllign, 
//                               VerticalAlignment vAllign, boolean isWrapText, boolean isBoldText, 
//                               boolean isItalicText, int fontSize, String fontName) {
//    CellStyle style = workBook.createCellStyle();
//    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//    
//    if (style instanceof XSSFCellStyle) {
//        XSSFCellStyle xssfcellcolorstyle = (XSSFCellStyle)style;
//        xssfcellcolorstyle.setFillForegroundColor(new XSSFColor(rgb, null));
//    }
//    
//    style.setBorderBottom(BorderStyle.MEDIUM);
//    style.setBorderTop(BorderStyle.MEDIUM);
//    style.setBorderLeft(BorderStyle.MEDIUM);
//    style.setBorderRight(BorderStyle.MEDIUM);
//    style.setAlignment(hAllign);
//    style.setVerticalAlignment(vAllign);
//    style.setWrapText(isWrapText);
//    
//    Font font = workBook.createFont();
//    font.setFontHeightInPoints((short)fontSize);  
//    font.setFontName(fontName);
//    font.setItalic(isItalicText); 
//    font.setBold(isBoldText);
//    
//    style.setFont(font); 
//    
//    return style;
//}
	
	
//	@RequestMapping(value = "/export-contract", method = {RequestMethod.GET, RequestMethod.POST})
//	public void exportContract(
//	    HttpServletRequest request, 
//	    HttpServletResponse response,
//	    HttpSession session,
//	    @RequestParam(required = false) String dy_hod_designation,
//	    @RequestParam(required = false) String designation,
//	    @RequestParam(required = false) String contractor_id_fk,
//	    @RequestParam(required = false) String contract_status_fk,
//	    @RequestParam(required = false) String contract_status,
//	    @RequestParam(required = false) String project_id_fk,
//	    @RequestParam(required = false) String searchStr
//	) {
//	    XSSFWorkbook workBook = null;
//	    
//	    try {
//	       //// String userId = (String) session.getAttribute("USER_ID");
//	        //String userName = (String) session.getAttribute("USER_NAME");
//	       // String userRoleCode = (String) session.getAttribute("USER_ROLE_CODE");
//	    	String userId = (String) session.getAttribute("userId");
//			String userName = (String) session.getAttribute("userName");
//			String userDesignation = (String) session.getAttribute("designation");
//		
//	       // logger.info("Export started for user: " + userId);
//			
//	        System.out.println("Export started for user: " + userId);
//	        
//	        
//	        // Create contract object with filters
//	        Contract contract = new Contract();
//	        contract.setUser_id(userId);
//	        contract.setUser_role_code(userRoleCode);
//	        contract.setDy_hod_designation(dy_hod_designation);
//	        contract.setDesignation(designation);
//	        contract.setContractor_id_fk(contractor_id_fk);
//	        contract.setContract_status_fk(contract_status_fk);
//	        contract.setContract_status(contract_status);
//	        contract.setProject_id_fk(project_id_fk);
//	        contract.setSearchStr(searchStr);
//	        
//	        // Fetch data based on filters
//	        List<Contract> dataList = contractService.contractListForExport(contract);
//	        
//	        System.out.println("Found " + (dataList != null ? dataList.size() : 0) + " contracts for export");
//	        
//	        if(dataList == null || dataList.isEmpty()) {
//	        	   System.out.println("No data available for export");
//	            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//	            response.setContentType("text/plain");
//	            response.getWriter().write("No data available for export");
//	            response.getWriter().flush();
//	            return;
//	        }
//	        
//	        List<Contract> revisionsDataList = contractService.contractRevisionsList(contract); 
//	        List<Contract> bgDataList = contractService.contractBGList(contract); 
//	        List<Contract> insuranceDataList = contractService.contractInsuranceList(contract); 
//	        List<Contract> milestoneDataList = contractService.contractMilestoneList(contract);
//	        
//	        // Create workbook
//	        workBook = new XSSFWorkbook();
//	        
//	        // Create sheets
//	        XSSFSheet contractsSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Contract"));
//	        XSSFSheet revisionsSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Revision Details"));
//	        XSSFSheet bgSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("BG"));
//	        XSSFSheet insuranceSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Insurance"));
//	        XSSFSheet milestoneSheet = workBook.createSheet(WorkbookUtil.createSafeSheetName("Milestone"));
//	        
//	        // Set sheet order
//	        workBook.setSheetOrder(contractsSheet.getSheetName(), 0);
//	        workBook.setSheetOrder(revisionsSheet.getSheetName(), 1);
//	        workBook.setSheetOrder(bgSheet.getSheetName(), 2);
//	        workBook.setSheetOrder(insuranceSheet.getSheetName(), 3);
//	        workBook.setSheetOrder(milestoneSheet.getSheetName(), 4);
//	        
//	        // Define colors
//	        byte[] greenRGB = new byte[]{(byte)146, (byte)208, (byte)80};
//	        byte[] whiteRGB = new byte[]{(byte)255, (byte)255, (byte)255};
//	        
//	        // Create styles
//	        CellStyle greenStyle = cellFormating(workBook, greenRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, true, false, 11, "Times New Roman");
//	        CellStyle sectionStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, true, false, false, 9, "Times New Roman");
//	        CellStyle centerStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true, false, false, 9, "Times New Roman");
//	        CellStyle rightStyle = cellFormating(workBook, whiteRGB, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, true, false, false, 9, "Times New Roman");
//	        
//	        /********************************** Contract Sheet *********************************************************/
//	        
//	        XSSFRow headingRow = contractsSheet.createRow(0);
//	        String headerString = "Work^Contract ID^Contract Name^Contract Short Name^Contractor^Department^HOD^DY HOD^Bank Funded(Yes/No)^Bank Name^Type of Review^Notice Inviting Tender^Contract Type^Scope of Contract"
//	                + "^Estimated Cost (Rs in Lakhs)^Planned Date of Award^Awarded Cost (Rs in Lakhs)^LOA Letter Number^LOA Date^CA NO^CA Date^Date of Start^DOC^"
//	                + "Actual Completion Date^Final Taking over by Client^Date of issue of Completion Certificate^Date of Payment of Final bill^Date of release of Final Retention BG^Completion Cost (Rs in Lakhs)^"
//	                + "End date of Defect Liability Period^Date of release of PBG^Date of Contract Closure^Contract Status^Status of Work^Bank Guarantee Required^Insurance Required^Tally Head";
//	        
//	        String[] headerStringArr = headerString.split("\\^");
//	        
//	        for (int i = 0; i < headerStringArr.length; i++) {              
//	            Cell cell = headingRow.createCell(i);
//	            cell.setCellStyle(greenStyle);
//	            cell.setCellValue(headerStringArr[i]);
//	        }
//	        
//	        NumberFormat numberFormatter = new DecimalFormat("#0.00");
//	        
//	        int rowNo = 1;
//	        for (Contract obj : dataList) {
//	            XSSFRow row = contractsSheet.createRow(rowNo);
//	            int c = 0;
//	            
//	            // Work
//	            Cell cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getWork_id_fk()) + " - " + nullSafe(obj.getWork_name()));
//	            
//	            // Contract ID
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getContract_id()));
//	            
//	            // Contract Name
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getContract_name()));
//	            
//	            // Contract Short Name
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getContract_short_name()));
//	            
//	            // Contractor
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            String contractor = "";
//	            if (!StringUtils.isEmpty(obj.getContractor_id_fk())) {
//	                contractor = obj.getContractor_id_fk();
//	            }
//	            if (!StringUtils.isEmpty(obj.getContractor_name())) {
//	                contractor += (contractor.isEmpty() ? "" : " - ") + obj.getContractor_name();
//	            }
//	            cell.setCellValue(contractor);
//	            
//	            // Department
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getHod_department()));
//	            
//	            // HOD
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getDesignation()));
//	            
//	            // Dy HOD
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getDy_hod_designation()));
//	            
//	            // Bank Funded
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getBank_funded()));
//	            
//	            // Bank Name
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getBank_name()));
//	            
//	            // Type of Review
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getType_of_review()));
//	            
//	            // Notice Inviting Tender
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getNoticeinvitingtender()));
//	            
//	            // Contract Type
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getContract_type_fk()));
//	            
//	            // Scope of Contract
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getScope_of_contract()));
//	            
//	            // Estimated Cost
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(rightStyle);
//	            Double estimatedCost = calculateCost(obj.getEstimated_cost(), obj.getEstimated_cost_units(), numberFormatter);
//	            if (estimatedCost != null) {
//	                cell.setCellValue(estimatedCost);
//	            }
//	            
//	            // Planned Date of Award
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getPlanned_date_of_award()));
//	            
//	            // Awarded Cost
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(rightStyle);
//	            Double awardedCost = calculateCost(obj.getAwarded_cost(), obj.getAwarded_cost_units(), numberFormatter);
//	            if (awardedCost != null) {
//	                cell.setCellValue(awardedCost);
//	            }
//	            
//	            // LOA Letter Number
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getLoa_letter_number()));
//	            
//	            // LOA Date
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getLoa_date()));
//	            
//	            // CA NO
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getCa_no()));
//	            
//	            // CA Date
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getCa_date()));
//	            
//	            // Date of Start
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getDate_of_start()));
//	            
//	            // DOC
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getDoc()));
//	            
//	            // Actual Completion Date
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getActual_completion_date()));
//	            
//	            // Final Takeover
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getFinal_takeover()));
//	            
//	            // Completion Certificate Release
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getCompletion_certificate_release()));
//	            
//	            // Final Bill Release
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getFinal_bill_release()));
//	            
//	            // Retention Money Release
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getRetention_money_release()));
//	            
//	            // Completed Cost
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(rightStyle);
//	            Double completedCost = calculateCost(obj.getCompleted_cost(), obj.getCompleted_cost_units(), numberFormatter);
//	            if (completedCost != null) {
//	                cell.setCellValue(completedCost);
//	            }
//	            
//	            // Defect Liability Period
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getDefect_liability_period()));
//	            
//	            // PBG Release
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getPbg_release()));
//	            
//	            // Contract Closure Date
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getContract_closure_date()));
//	            
//	            // Contract Status
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getContract_status()));
//	            
//	            // Status of Work
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getContract_status_fk()));
//	            
//	            // BG Required
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getBg_required()));
//	            
//	            // Insurance Required
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(centerStyle);
//	            cell.setCellValue(nullSafe(obj.getInsurance_required()));
//	            
//	            // Tally Head
//	            cell = row.createCell(c++);
//	            cell.setCellStyle(sectionStyle);
//	            cell.setCellValue(nullSafe(obj.getTally_head()));
//	            
//	            rowNo++;
//	        }
//	        
//	        // Set column widths
//	        for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//	            contractsSheet.setColumnWidth(columnIndex, 25 * 256); // 25 characters wide
//	        }
//	        
//	        /********************************** Add other sheets similarly *********************************************************/
//	        // I'll add the revision sheet as an example, you can do the same for BG, Insurance, and Milestone
//	        
//	        if (revisionsDataList != null && !revisionsDataList.isEmpty()) {
//	            headingRow = revisionsSheet.createRow(0);
//	            headerString = "Contract ID^Contract Short Name^Revision Number^Revised Contract Value (Rs in Lakhs)^Current^Revised DOC^Current^Approval by Bank(Yes/No)^Remarks";
//	            headerStringArr = headerString.split("\\^");
//	            
//	            for (int i = 0; i < headerStringArr.length; i++) {              
//	                Cell cell = headingRow.createCell(i);
//	                cell.setCellStyle(greenStyle);
//	                cell.setCellValue(headerStringArr[i]);
//	            }
//	            
//	            rowNo = 1;
//	            for (Contract obj : revisionsDataList) {
//	                XSSFRow row = revisionsSheet.createRow(rowNo);
//	                int c = 0;
//	                
//	                Cell cell = row.createCell(c++);
//	                cell.setCellStyle(sectionStyle);
//	                cell.setCellValue(nullSafe(obj.getContract_id()));
//	                
//	                cell = row.createCell(c++);
//	                cell.setCellStyle(sectionStyle);
//	                cell.setCellValue(nullSafe(obj.getContract_short_name()));
//	                
//	                cell = row.createCell(c++);
//	                cell.setCellStyle(sectionStyle);
//	                cell.setCellValue(nullSafe(obj.getRevision_number()));
//	                
//	                cell = row.createCell(c++);
//	                cell.setCellStyle(rightStyle);
//	                Double revisedAmount = calculateCost(obj.getRevised_amount(), obj.getRevised_amount_units(), numberFormatter);
//	                if (revisedAmount != null) {
//	                    cell.setCellValue(revisedAmount);
//	                }
//	                
//	                cell = row.createCell(c++);
//	                cell.setCellStyle(centerStyle);
//	                cell.setCellValue(nullSafe(obj.getRevision_amounts_status()));
//	                
//	                cell = row.createCell(c++);
//	                cell.setCellStyle(centerStyle);
//	                cell.setCellValue(nullSafe(obj.getRevised_doc()));
//	                
//	                cell = row.createCell(c++);
//	                cell.setCellStyle(centerStyle);
//	                cell.setCellValue(nullSafe(obj.getRevision_status()));
//	                
//	                cell = row.createCell(c++);
//	                cell.setCellStyle(centerStyle);
//	                cell.setCellValue(nullSafe(obj.getApprovalbybank()));
//	                
//	                cell = row.createCell(c++);
//	                cell.setCellStyle(sectionStyle);
//	                cell.setCellValue(nullSafe(obj.getRemarks()));
//	                
//	                rowNo++;
//	            }
//	            
//	            for(int columnIndex = 0; columnIndex < headerStringArr.length; columnIndex++) {
//	                revisionsSheet.setColumnWidth(columnIndex, 25 * 256);
//	            }
//	        }
//	        
//	        // Similar code for bgSheet, insuranceSheet, and milestoneSheet...
//	        // (Use the same pattern as above)
//	        
//	        /*******************************************************************************************/
//	        
//	        // Generate filename with timestamp
//	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
//	        Date date = new Date();
//	        String fileName = "Contract_" + dateFormat.format(date) + ".xlsx";
//	        
//	        System.out.println("Generating Excel file: " + fileName);
//	        
//	        // IMPORTANT: Set headers BEFORE writing to output stream
//	        // Clear any previous response content
//	        response.reset();
//	        
//	        // Set content type
//	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//	        
//	        // Set headers
//	        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//	        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//	        response.setHeader("Pragma", "no-cache");
//	        response.setHeader("Expires", "0");
//	        
//	        // Write workbook to output stream
//	        ServletOutputStream outputStream = response.getOutputStream();
//	        workBook.write(outputStream);
//	        outputStream.flush();
//	        outputStream.close();
//	        
//	        System.out.println("Excel file generated successfully");
//	        
//	    } catch (Exception e) {
//	    	  // System.out.print("Error in exportContract: " + e.getMessage(), e);
//	        e.printStackTrace();
//	        
//	        // Clean up if error occurs
//	        if (workBook != null) {
//	            try {
//	                workBook.close();
//	            } catch (IOException ex) {
//	            	 //  System.out.print("Error closing workbook", ex);
//	            }
//	        }
//	        
//	        // Don't try to write error response if response is already committed
//	        if (!response.isCommitted()) {
//	            try {
//	                response.reset();
//	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//	                response.setContentType("text/plain");
//	                response.getWriter().write("Error generating export: " + e.getMessage());
//	                response.getWriter().flush();
//	            } catch (IOException ex) {
//	                logger.error("Error writing error response", ex);
//	            }
//	        }
//	    } finally {
//	        // Close workbook in finally block
//	        if (workBook != null) {
//	            try {
//	                workBook.close();
//	            } catch (IOException e) {
//	                logger.error("Error closing workbook in finally block", e);
//	            }
//	        }
//	    }
//	}
//
//	// Helper method to handle null values
//	private String nullSafe(String value) {
//	    return value != null ? value : "";
//	}
//
//	// Helper method to calculate cost
//	private Double calculateCost(String cost, String units, NumberFormat formatter) {
//	    if (StringUtils.isEmpty(cost) || StringUtils.isEmpty(units)) {
//	        return null;
//	    }
//	    try {
//	        double val = (Double.parseDouble(cost) * Double.parseDouble(units)) / 100000;
//	        return Double.parseDouble(formatter.format(val));
//	    } catch (NumberFormatException e) {
//	        logger.error("Error calculating cost: " + e.getMessage());
//	        return null;
//	    }
//	}
//
//	private CellStyle cellFormating(XSSFWorkbook workBook, byte[] rgb, HorizontalAlignment hAllign, 
//	                               VerticalAlignment vAllign, boolean isWrapText, boolean isBoldText, 
//	                               boolean isItalicText, int fontSize, String fontName) {
//	    CellStyle style = workBook.createCellStyle();
//	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//	    
//	    if (style instanceof XSSFCellStyle) {
//	        XSSFCellStyle xssfcellcolorstyle = (XSSFCellStyle)style;
//	        xssfcellcolorstyle.setFillForegroundColor(new XSSFColor(rgb, null));
//	    }
//	    
//	    style.setBorderBottom(BorderStyle.THIN); // Changed from MEDIUM to THIN for better performance
//	    style.setBorderTop(BorderStyle.THIN);
//	    style.setBorderLeft(BorderStyle.THIN);
//	    style.setBorderRight(BorderStyle.THIN);
//	    style.setAlignment(hAllign);
//	    style.setVerticalAlignment(vAllign);
//	    style.setWrapText(isWrapText);
//	    
//	    Font font = workBook.createFont();
//	    font.setFontHeightInPoints((short)fontSize);  
//	    font.setFontName(fontName);
//	    font.setItalic(isItalicText); 
//	    font.setBold(isBoldText);
//	    
//	    style.setFont(font); 
//	    
//	    return style;
//	}
}
