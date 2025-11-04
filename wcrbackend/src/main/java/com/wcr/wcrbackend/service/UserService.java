package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.DTO.RandRMain;
import com.wcr.wcrbackend.DTO.Risk;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.repo.IUserDao;

import jakarta.transaction.Transactional;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDoaService;
	@Override
	public String getRoleCode(String userRoleNameFk) {
		// TODO Auto-generated method stub
		return userDoaService.getRoleCode(userRoleNameFk);
	}
	@Override
	public List<User> getUserTypesFilter(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUserTypesFilter(obj);
	}
	@Override
	public List<User> getUserRolesFilter(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUserRolesFilter(obj);
	}
	@Override
	public List<User> getUserDepartmentsFilter(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUserDepartmentsFilter(obj);
	}
	@Override
	public List<User> getUserReportingToListFilter(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUserReportingToListFilter(obj);
	}
	@Override
	public List<User> getStructuresByContractId(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getStructuresByContractId(obj);
	}
	@Override
	public List<User> getUsersList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUsersList(obj);
	}
	@Override
	public String checkPMISKeyAvailability(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.checkPMISKeyAvailability(obj);
	}
	@Override
	public List<User> getUserReportingToList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUserReportingToList(obj);
	}
	@Override
	public List<User> getUserRoles() throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUserRoles();
	}
	@Override
	public List<User> getUserTypes() throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUserTypes();
	}
	@Override
	public List<User> getUserDepartments() throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUserDepartments();
	}
	@Override
	public List<User> getPmisKeys() throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getPmisKeys();
	}
	@Override
	public List<User> getModuleSList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getModuleSList(obj);
	}
	@Override
	public List<Contract> getContractsList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getContractsList(obj);
	}
	@Override
	public List<Structure> getStructuresList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getStructuresList(obj);
	}
	@Override
	public List<Risk> getRiskList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getRiskList(obj);
	}
	@Override
	public List<LandAcquisition> getLandList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getLandList(obj);
	}
	@Override
	public List<UtilityShifting> getUtilityList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUtilityList(obj);
	}
	@Override
	public List<RandRMain> getRRList(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getRRList(obj);
	}
	@Override
	@Transactional
	public String addUser(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.addUser(obj);
	}
	@Override
	public User getUser(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUser(obj);
	}
	@Override
	@Transactional
	public boolean updateUser(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.updateUser(obj);
	}
	@Override
	public boolean deleteUser(User obj) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.deleteUser(obj);
	}
	@Override
	public List<User> getUsersExportList(User user) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getUsersExportList(user);
	}
	@Override
	public List<User> getReportingToUserId(String reporting_to_id_srfk) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.getReportingToUserId(reporting_to_id_srfk);
	}
	@Override
	public int uploadUsers(List<User> usersList) throws Exception {
		// TODO Auto-generated method stub
		return userDoaService.uploadUsers(usersList);
	}

}
