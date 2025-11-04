package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.DTO.RandRMain;
import com.wcr.wcrbackend.DTO.Risk;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.DTO.UtilityShifting;

public interface IUserService {

	String getRoleCode(String userRoleNameFk);

	List<User> getUserTypesFilter(User obj) throws Exception;

	List<User> getUserRolesFilter(User obj) throws Exception;

	List<User> getUserDepartmentsFilter(User obj) throws Exception;

	List<User> getUserReportingToListFilter(User obj) throws Exception;

	List<User> getStructuresByContractId(User obj) throws Exception;

	List<User> getUsersList(User obj) throws Exception;

	String checkPMISKeyAvailability(User obj) throws Exception;

	List<User> getUserReportingToList(User obj) throws Exception;

	List<User> getUserRoles() throws Exception;

	List<User> getUserTypes() throws Exception;

	List<User> getUserDepartments() throws Exception;

	List<User> getPmisKeys() throws Exception;

	List<User> getModuleSList(User obj) throws Exception;

	List<Contract> getContractsList(User obj) throws Exception;

	List<Structure> getStructuresList(User obj) throws Exception;

	List<Risk> getRiskList(User obj) throws Exception;

	List<LandAcquisition> getLandList(User obj) throws Exception;

	List<UtilityShifting> getUtilityList(User obj) throws Exception;

	List<RandRMain> getRRList(User obj) throws Exception;

}
