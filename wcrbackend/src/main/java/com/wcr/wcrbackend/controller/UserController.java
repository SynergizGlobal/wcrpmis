package com.wcr.wcrbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.DTO.RandRMain;
import com.wcr.wcrbackend.DTO.Risk;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.service.IUserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
public class UserController {
	Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	IUserService userService;
	
	
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
	
	@PostMapping(value = "/ajax/getUserTypesFilterInUser")
	public List<User> getUserTypesFilterInUser(@RequestBody User obj) {
		List<User> users = null;
		try {
			users = userService.getUserTypesFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserTypesFilterInUser : " + e.getMessage());
		}
		return users;
	}
	
	@PostMapping(value = "/ajax/getUserRolesFilterInUser")
	public List<User> getUserRolesFilterInUser(@RequestBody User obj) {
		List<User> users = null;
		try {
			users = userService.getUserRolesFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserRolesFilterInUser : " + e.getMessage());
		}
		return users;
	}
	
	@PostMapping(value = "/ajax/getUserDepartmentsFilterInUser")
	public List<User> getUserDepartmentsFilterInUser(@RequestBody User obj) {
		List<User> users = null;
		try {
			users = userService.getUserDepartmentsFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserDepartmentsFilterInUser : " + e.getMessage());
		}
		return users;
	}
	
	@PostMapping(value = "/ajax/getUserReportingToListFilterInUser")
	public List<User> getUserReportingToListFilterInUser(@RequestBody User obj) {
		List<User> users = null;
		try {
			users = userService.getUserReportingToListFilter(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserReportingToListFilterInUser : " + e.getMessage());
		}
		return users;
	}
	@PostMapping(value = "/ajax/getStructuresByContractId")
	public List<User> getStructuresByContractId(@RequestBody User obj) {
		List<User> users = null;
		try {
			users = userService.getStructuresByContractId(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getStructuresByContractId : " + e.getMessage());
		}
		return users;
	}
	
	@PostMapping(value = "/ajax/getUsersList")
	public List<User> getUsersList(@RequestBody User obj) {
		List<User> users = null;
		//List<User> usersExport = null;
		try {
			users = userService.getUsersList(obj);
			//usersExport = userService.getUsersExportList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUsersList : " + e.getMessage());
		}
		return users;
	}
	
	@PostMapping(value = "/ajax/checkPMISKeyAvailability")
	public User checkPMISKeyAvailability(@RequestBody User obj) {
		String pmis_key = null;
		User dObj = new User();
		try {
			pmis_key = userService.checkPMISKeyAvailability(obj);
			dObj.setKeyAvailability(pmis_key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("checkPMISKeyAvailability : " + e.getMessage());
		}
		return dObj;
	}
	
	@PostMapping(value = "/ajax/getUserReportingToList")
	public List<User> getUserReportingToList(@RequestBody User obj) {
		List<User> reportingToList = null;
		try {
			reportingToList = userService.getUserReportingToList(obj);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserReportingToList : " + e.getMessage());
		}
		return reportingToList;
	}
	
	@PostMapping(value="/ajax/form/add-user-form")
	public Map<String,List<User>> addUserForm(HttpSession session, @RequestBody User obj) {
		//ModelAndView model = new ModelAndView();
		Map<String,List<User>> map = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditUser);
			
			//model.addObject("action", "add");
			
			List<User> roles = userService.getUserRoles();
			map.put("roles", roles);
			
			List<User> types = userService.getUserTypes();
			map.put("types", types);
			
			List<User> departments = userService.getUserDepartments();
			map.put("departments", departments);
			
			List<User> reportingToList = userService.getUserReportingToList(null);
			map.put("reportingToList", reportingToList);
			
			List<User> pmisKeys = userService.getPmisKeys();
			map.put("pmisKeys", pmisKeys);
			
			
			
			List<User> moduleList = userService.getModuleSList(obj);
			map.put("moduleList", moduleList);
			
		} catch (Exception e) {
			logger.error("addUserForm : " + e.getMessage());
		}
		return map;
	}
	@PostMapping(value="/ajax/form/add-user-form/getContractsList")
	public Map<String,List<Contract>> addUserForm1(HttpSession session, @RequestBody User obj) {
		//ModelAndView model = new ModelAndView();
		Map<String,List<Contract>> map = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditUser);
			
			//model.addObject("action", "add");
			
			List<Contract> contractsList = userService.getContractsList(obj);
			map.put("contractsList", contractsList);
			
		} catch (Exception e) {
			logger.error("addUserForm : " + e.getMessage());
		}
		return map;
	}
	@PostMapping(value="/ajax/form/add-user-form/getStructuresList")
	public Map<String,List<Structure>> addUserForm2(HttpSession session, @RequestBody User obj) {
		//ModelAndView model = new ModelAndView();
		Map<String,List<Structure>> map = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditUser);
			
			//model.addObject("action", "add");
			
			List<Structure> structuresList = userService.getStructuresList(obj);
			map.put("structuresList", structuresList);
			
		} catch (Exception e) {
			logger.error("addUserForm : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value="/ajax/form/add-user-form/getRiskList")
	public Map<String,List<Risk>> addUserForm3(HttpSession session, @RequestBody User obj) {
		//ModelAndView model = new ModelAndView();
		Map<String,List<Risk>> map = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditUser);
			
			//model.addObject("action", "add");
			
			List<Risk> riskList = userService.getRiskList(obj);
			map.put("riskList", riskList);
			
		} catch (Exception e) {
			logger.error("addUserForm : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value="/ajax/form/add-user-form/getLandList")
	public Map<String,List<LandAcquisition>> addUserForm4(HttpSession session, @RequestBody User obj) {
		//ModelAndView model = new ModelAndView();
		Map<String,List<LandAcquisition>> map = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditUser);
			
			//model.addObject("action", "add");
			
			List<LandAcquisition> landList = userService.getLandList(obj);
			map.put("landList", landList);
			
		} catch (Exception e) {
			logger.error("addUserForm : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value="/ajax/form/add-user-form/getUtilityList")
	public Map<String,List<UtilityShifting>> addUserForm5(HttpSession session, @RequestBody User obj) {
		//ModelAndView model = new ModelAndView();
		Map<String,List<UtilityShifting>> map = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditUser);
			
			//model.addObject("action", "add");
			
			List<UtilityShifting> utilityList = userService.getUtilityList(obj);
			map.put("utilityList", utilityList);
			
		} catch (Exception e) {
			logger.error("addUserForm : " + e.getMessage());
		}
		return map;
	}
	
	@PostMapping(value="/ajax/form/add-user-form/getRRList")
	public Map<String,List<RandRMain>> addUserForm6(HttpSession session, @RequestBody User obj) {
		//ModelAndView model = new ModelAndView();
		Map<String,List<RandRMain>> map = new HashMap<>();
		try {
			//model.setViewName(PageConstants2.addEditUser);
			
			//model.addObject("action", "add");
			
			List<RandRMain> rrList = userService.getRRList(obj);
			map.put("rrList", rrList);
			
		} catch (Exception e) {
			logger.error("addUserForm : " + e.getMessage());
		}
		return map;
	}
}