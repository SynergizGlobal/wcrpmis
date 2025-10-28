package com.wcr.wcrbackend.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.wcr.wcrbackend.DTO.UtilityShiftingPaginationObject;
import com.wcr.wcrbackend.common.DateParser;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IUserService;
import com.wcr.wcrbackend.service.IUtilityShiftingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	public Map<String,List<UtilityShifting>> addUtilityShiftingForm(@RequestBody UtilityShifting obj,HttpSession session) {
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
	
	@PostMapping(value="/addUtilityShifting")
	public boolean addUtilityShifting(@RequestBody UtilityShifting obj,HttpSession session) {
		User uObj = (User) session.getAttribute("user");
		String user_Id = uObj.getUserId();
		String userName = uObj.getUserName();
		String userDesignation = uObj.getDesignation();
		try {
			obj.setCreated_by_user_id_fk(user_Id);
			obj.setUser_id(user_Id);
			obj.setUser_name(userName);
			obj.setDesignation(userDesignation);
			User user = (User)session.getAttribute("user");
			
			obj.setStart_date(DateParser.parse(obj.getStart_date()));			
			obj.setShifting_completion_date(DateParser.parse(obj.getShifting_completion_date()));
			obj.setPlanned_completion_date(DateParser.parse(obj.getPlanned_completion_date()));
			obj.setIdentification(DateParser.parse(obj.getIdentification()));
			
			
			boolean flag = utilityShiftingService.addUtilityShifting(obj);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			//attributes.addFlashAttribute("error", commonError);
			logger.error("addUtilityShifting : " + e.getMessage());
		}
		return false;
	}
	
	@PostMapping(value = "/ajax/getUtilityShiftingList")
	public UtilityShiftingPaginationObject getUtilityShiftingList(@RequestBody UtilityShifting obj, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		//PrintWriter pw = null;
		//JSONObject json = new JSONObject();
		String json2 = null;
		String userId = null;
		String userName = null,user_role_name=null;
		try {
			User uObj = (User) session.getAttribute("user");
			userId = uObj.getUserId();
			userName = uObj.getUserName();
			user_role_name = uObj.getUserRoleNameFk();
			//pw = response.getWriter();
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

			List<UtilityShifting> utilityShiftingList = new ArrayList<UtilityShifting>();

			//Here is server side pagination logic. Based on the page number you could make call 
			//to the data base create new list and send back to the client. For demo I am shuffling 
			//the same list to show data randomly
			int startIndex = 0;
			int offset = pageDisplayLength;

			if (pageNumber == 1) {
				startIndex = 0;
				offset = pageDisplayLength;
				utilityShiftingList = createPaginationData(session,startIndex, offset, obj, searchParameter);
			} else {
				startIndex = (pageNumber * offset) - offset;
				offset = pageDisplayLength;
				utilityShiftingList = createPaginationData(session,startIndex, offset, obj, searchParameter);
			}

			//Search functionality: Returns filtered list based on search parameter
			//UtilityShiftingList = getListBasedOnSearchParameter(searchParameter,UtilityShiftingList);

			int totalRecords = getTotalRecords(obj, searchParameter, session);

			UtilityShiftingPaginationObject personJsonObject = new UtilityShiftingPaginationObject();
			//Set Total display record
			personJsonObject.setiTotalDisplayRecords(totalRecords);
			//Set Total record
			personJsonObject.setiTotalRecords(totalRecords);
			personJsonObject.setAaData(utilityShiftingList);
			return personJsonObject;
			//Gson gson = new GsonBuilder().setPrettyPrinting().create();
			//json2 = gson.toJson(personJsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"getUtilityShiftingList : User Id - " + userId + " - User Name - " + userName + " - " + e.getMessage());
		}
		return null;
		//pw.println(json2);
	}

	/**
	 * @param searchParameter 
	 * @param activity 
	 * @return
	 */
	public int getTotalRecords(UtilityShifting obj, String searchParameter,HttpSession session) {
		int totalRecords = 0;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			totalRecords = utilityShiftingService.getTotalRecords(obj, searchParameter);
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
	public List<UtilityShifting> createPaginationData(HttpSession session, int startIndex, int offset, UtilityShifting obj, String searchParameter) {
		List<UtilityShifting> objList = null;
		try {

			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			objList = utilityShiftingService.getUtilityShiftingList(obj, startIndex, offset, searchParameter);
		} catch (Exception e) {
			logger.error("createPaginationData : " + e.getMessage());
		}
		return objList;
	}	
	
	@PostMapping(value="/ajax/form/get-utility-shifting")
	public Map<String, List<UtilityShifting>> getUtilityShifting(@RequestBody UtilityShifting obj,HttpSession session) {
		//ModelAndView model = new ModelAndView();
		Map<String, List<UtilityShifting>> map = new HashMap<>();
		try {
		
			User uObj = (User) session.getAttribute("user");
			obj.setUser_type_fk(uObj.getUserTypeFk());
			obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
			obj.setUser_id(uObj.getUserId());
			
			//model.setViewName(PageConstants.addEditUtilityShifting);
			//model.addObject("action", "edit");
			
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
			
		} catch (Exception e) {
			//attributes.addFlashAttribute("error", commonError);
			logger.error("getUtilityShifting : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value="/ajax/form/get-utility-shifting/get-utility-shifting")
	public UtilityShifting getUtilityShifting2(@RequestBody UtilityShifting obj,HttpSession session) throws Exception{
		User uObj = (User) session.getAttribute("user");
		obj.setUser_type_fk(uObj.getUserTypeFk());
		obj.setUser_role_code(userService.getRoleCode(uObj.getUserRoleNameFk()));
		obj.setUser_id(uObj.getUserId());
		
		UtilityShifting utilityShifting = utilityShiftingService.getUtilityShifting(obj);
		return utilityShifting;
	}
	
	@PostMapping(value="/updateUtilityShifting")
	public boolean updateUtilityShifting(@RequestBody UtilityShifting obj,HttpSession session) {
		//ModelAndView model = new ModelAndView();
		try {
			//model.setViewName("redirect:/utilityshifting");

			User uObj = (User) session.getAttribute("user");
			String user_Id = uObj.getUserId();
			String userName = uObj.getUserName();
			String userDesignation = uObj.getDesignation();
			
			obj.setCreated_by_user_id_fk(user_Id);
			obj.setUser_id(user_Id);
			obj.setUser_name(userName);
			obj.setDesignation(userDesignation);
			
			User user = (User)session.getAttribute("user");
			
	
			obj.setStart_date(DateParser.parse(obj.getStart_date()));			
			obj.setShifting_completion_date(DateParser.parse(obj.getShifting_completion_date()));
			obj.setPlanned_completion_date(DateParser.parse(obj.getPlanned_completion_date()));
			obj.setIdentification(DateParser.parse(obj.getIdentification()));
			obj.setCreated_by_user_id_fk(user_Id);
			
			boolean flag = utilityShiftingService.updateUtilityShifting(obj);
			return flag;
		} catch (Exception e) {
			//attributes.addFlashAttribute("error", commonError);
			logger.error("updateUtilityShifting : " + e.getMessage());
		}
		return false;
	}
	
	@PostMapping(value = "/ajax/getUtilityShiftingUploadsList")
	public List<UtilityShifting> getUtilityShiftingUploadsList(@RequestBody UtilityShifting obj) {
		List<UtilityShifting> objsList = null;
		try {
			objsList = utilityShiftingService.getUtilityShiftingUploadsList(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getUtilityShiftingUploadsList : " + e.getMessage());
		}
		return objsList;
	}
	
	
	
}
