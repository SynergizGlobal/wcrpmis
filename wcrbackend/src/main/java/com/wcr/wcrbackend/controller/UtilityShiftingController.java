package com.wcr.wcrbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IUserService;
import com.wcr.wcrbackend.service.IUtilityShiftingService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/utility-shifting")
public class UtilityShiftingController {
	
	Logger logger = Logger.getLogger(UtilityShiftingController.class);
	@Autowired
	private IUtilityShiftingService utilityShiftingService;
	
	@Autowired
	private IUserService userService;
	
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
	public Map<String,List<UtilityShifting>> addUtilityShifting(@RequestBody UtilityShifting obj,HttpSession session) {
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
}
