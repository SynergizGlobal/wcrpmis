package com.wcr.wcrbackend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Design;
import com.wcr.wcrbackend.DTO.DesignsPaginationObject;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.IDesignService;
import com.wcr.wcrbackend.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/design")
public class DesignController {
	
	@Autowired
	private IDesignService designService;
	
	@Autowired
	private IUserService userService;
	
	Logger logger = Logger.getLogger(DesignController.class);
	
	@PostMapping(value = "/ajax/getP6ActivitiesData")
	public List<Design> getP6ActivitiesData(@RequestBody Design obj) {
		List<Design> objList = null;
		try {
			objList = designService.getP6ActivitiesData(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getP6ActivityData : " + e.getMessage());
		}
		return objList;
	}
	
	@PostMapping(value = "/ajax/getDesignUpdateStructures")
	public List<Design> getDesignUpdateStructures(@RequestBody Design obj) {
		List<Design> objList = null;
		try {
			objList = designService.getDesignUpdateStructures(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesignUpdateStructures : " + e.getMessage());
		}
		return objList;
	}	
	
	@PostMapping(value = "/ajax/getHodListFilterInDesign")
	public List<Design> getHodListFilter(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getHodListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getHodListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getDepartmentListFilterInDesign")
	public List<Design> getDepartmentListFilter(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getDepartmentListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDepartmentListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getContractListFilterInDesign")
	public List<Design> getContractListFilter(@RequestBody Design obj, HttpSession session) {
		List<Design> design = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());	
			
			design = designService.getContractListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getContractListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getStructureListFilterInDesign")
	public List<Design> getStructureListFilter(@RequestBody Design obj, HttpSession session) {
		List<Design> design = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());				
			design = designService.getStructureListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getStructureIdListFilter")
	public List<Design> getStructureIdListFilter(@RequestBody Design obj, HttpSession session) {
		List<Design> design = null;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());			
			design = designService.getStructureIdsListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructureIdsListFilter : " + e.getMessage());
		}
		return design;
	}	
	
	@PostMapping(value = "/ajax/getDrawingTypeListFilterInDesign")
	public List<Design> getDrawingTypeListFilter(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getDrawingTypeListFilter(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDrawingTypeListFilter : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getDesigns")
	public List<Design> getDesigns(@RequestBody Design obj) {
		List<Design> design = null;
		try {
			design = designService.getDesigns(obj);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("getDesigns : " + e.getMessage());
		}
		return design;
	}
	
	@PostMapping(value = "/ajax/getDesignsList")
	public DesignsPaginationObject getActivitiesList(@RequestBody Design obj, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		//PrintWriter pw = null;
		//JSONObject json = new JSONObject();
		String json2 = null;
		String userId = null;
		String userName = null;
		try {
			
			User uObj = (User) session.getAttribute("user");
			userId = uObj.getUserId();
			userName = uObj.getUserName();
			
			
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

			List<Design> designsList = new ArrayList<Design>();

			//Here is server side pagination logic. Based on the page number you could make call 
			//to the data base create new list and send back to the client. For demo I am shuffling 
			//the same list to show data randomly
			int startIndex = 0;
			int offset = pageDisplayLength;

			if (pageNumber == 1) {
				startIndex = 0;
				offset = pageDisplayLength;
				designsList = createPaginationData(startIndex, offset, obj, searchParameter);
			} else {
				startIndex = (pageNumber * offset) - offset;
				offset = pageDisplayLength;
				designsList = createPaginationData(startIndex, offset, obj, searchParameter);
			}

			//Search functionality: Returns filtered list based on search parameter
			//designsList = getListBasedOnSearchParameter(searchParameter,designsList);

			int totalRecords = getTotalRecords(obj, searchParameter,session);

			DesignsPaginationObject personJsonObject = new DesignsPaginationObject();
			//Set Total display record
			personJsonObject.setiTotalDisplayRecords(totalRecords);
			//Set Total record
			personJsonObject.setiTotalRecords(totalRecords);
			personJsonObject.setAaData(designsList);

			return personJsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"getActivitiesList : User Id - " + userId + " - User Name - " + userName + " - " + e.getMessage());
		}

		return null;
	}
	
	public List<Design> createPaginationData(int startIndex, int offset, Design obj, String searchParameter) {
		List<Design> earthWorkList = null;
		try {
			earthWorkList = designService.getDesignsList(obj, startIndex, offset, searchParameter);
		} catch (Exception e) {
			logger.error("createPaginationData : " + e.getMessage());
		}
		return earthWorkList;
	}
	
	public int getTotalRecords(Design obj, String searchParameter,HttpSession session) {
		int totalRecords = 0;
		try {
			User uObj = (User) session.getAttribute("user");
			obj.setUser_role_code(uObj.getUserRoleNameFk());
			obj.setUser_name(uObj.getUserName());				
			totalRecords = designService.getTotalRecords(obj, searchParameter);
		} catch (Exception e) {
			logger.error("getTotalRecords : " + e.getMessage());
		}
		return totalRecords;
	}
}
