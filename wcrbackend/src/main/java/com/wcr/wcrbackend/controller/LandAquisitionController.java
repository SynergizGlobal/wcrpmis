package com.wcr.wcrbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
}
