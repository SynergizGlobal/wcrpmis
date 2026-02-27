package com.wcr.wcrbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.BankGuarantee;
import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.Insurence;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.dms.common.CommonUtil;
import com.wcr.wcrbackend.dms.dto.ContractDTO;
import com.wcr.wcrbackend.repo.IContractRepo;
import com.wcr.wcrbackend.repo.UserDao;

@Service
public class ContractService implements IContractService {

	@Autowired
	private IContractRepo contractRepo;
	
	@Autowired
	private UserDao userRepository;
	
	@Override
	public List<Contract> getDepartmentList() throws Exception{
	
		return contractRepo.getDepartmentList();
	}

	@Override
	public List<Contract> getDesignationsFilterList(Contract obj) throws Exception {
	
		return contractRepo.getDesignationsFilterList(obj);
	}

	@Override
	public List<Contract> getDyHODDesignationsFilterList(Contract obj) throws Exception {
		return contractRepo.getDyHODDesignationsFilterList(obj);
	}
	
	@Override
	public List<Contract> contractorsFilterList(Contract obj) throws Exception {
		return contractRepo.contractorsFilterList(obj); 
	}

	@Override
	public List<Contract> getContractStatusFilterListInContract(Contract obj) throws Exception {
		return contractRepo.getContractStatusFilterListInContract(obj);
	}

	@Override
	public List<Contract> getStatusFilterListInContract(Contract obj) throws Exception {
		return contractRepo.getStatusFilterListInContract(obj);
	}

	
	@Override
	public List<Contract> contractList(Contract obj)throws Exception{
		return contractRepo.contractList(obj);
	}

	@Override
	public List<Contract> getProjectsListForContractForm(Contract obj) throws Exception {
		return contractRepo.getProjectsListForContractForm(obj);
	}

	@Override
	public List<Contract> getWorkListForContractForm(Contract obj) throws Exception {
		return contractRepo.getWorkListForContractForm(obj);
	}
	@Override
	public List<Contract> getContractFileTypeList(Contract obj) throws Exception {
		return contractRepo.getContractFileTypeList(obj);
	}
	@Override
	public List<User> setHodList()throws Exception{
		return contractRepo.setHodList();
	}
	@Override
	public List<User> getDyHodList() throws Exception {
		return contractRepo.getDyHodList();
	}

	@Override
	public List<Contract> getContractorsList() throws Exception {
		return contractRepo.getContractorsList();
	}
	
	@Override
	public List<Contract> getContractTypeList()throws Exception{
		return contractRepo.getContractTypeList();
	}
	@Override
	public List<Contract> getInsurenceTypeList()throws Exception{
		return contractRepo.getInsurenceTypeList();
	}

	@Override
	public List<BankGuarantee> bankGuarantee()throws Exception{
		return contractRepo.bankGuarantee();
	}
	@Override
	public List<Insurence> insurenceType()throws Exception{
		return contractRepo.insurenceType();
	}
	
	@Override
	public List<Contract> getResponsiblePeopleList(Contract obj) throws Exception {
		return contractRepo.getResponsiblePeopleList(obj);
	}

	@Override
	public List<Contract> getUnitsList(Contract obj) throws Exception {
		return contractRepo.getUnitsList(obj);
	}

	@Override
	public List<Contract> getContractStatus() throws Exception {
		return contractRepo.getContractStatus();
	}
	
	@Override
	public Contract getContract(Contract obj)throws Exception{
		return contractRepo.getContract(obj);
	}

	@Override
	public List<Contract> getBankNameList(Contract obj) throws Exception{
	
		return contractRepo.getBankNameList(obj);
	}	
	
	@Override
	public List<Contract> getContractStatusType(Contract obj)throws Exception{
		return contractRepo.getContractStatusType(obj);
	}
	
	@Override
	public String addContract(Contract contract)throws Exception{
		return contractRepo.addContract(contract);
	}

	@Override
	public List<Contract> getExecutivesListForContractForm(Contract obj) throws Exception {
		return contractRepo.getExecutivesListForContractForm(obj);
	}

	@Override
	public List<Contract> getHodList(Contract obj) throws Exception {
		return contractRepo.getHodList(obj);
	}
	@Override
	public List<Contract> getDyHodList(Contract obj) throws Exception {
		return contractRepo.getDyHodList(obj);
	}


	
	@Override
	public List<Contract> contractListForExport(Contract contract) throws Exception {
		return contractRepo.contractListForExport(contract);
	}
	
	@Override
	public List<Contract> contractRevisionsList(Contract contract) throws Exception {
		return contractRepo.contractRevisionsList(contract);
	}
	@Override
	public List<Contract> contractBGList(Contract contract) throws Exception {
		return contractRepo.contractBGList(contract);
	}
	@Override
	public List<Contract> contractInsuranceList(Contract contract) throws Exception {
		return contractRepo.contractInsuranceList(contract);
	}
	@Override
	public List<Contract> contractMilestoneList(Contract contract) throws Exception {
		return contractRepo.contractMilestoneList(contract);
	}

//	@Override
//	public List<ContractDTO> getContracts(String userId, String userRole) {
//		com.wcr.wcrbackend.entity.User user = userRepository.findById(userId).get();
//		if(CommonUtil.isITAdminOrSuperUser(user)) {
//    		//IT Admin
//    		return this.getAllContracts();
//    	} else if(userRole.equals("Contractor")) {
//    		return this.getContractsByUserId(userId);
//    	} else {
//    		return this.getContractsForOtherUsersByUserId(userId);
//    	}
//	
//	}
	@Override
	public List<ContractDTO> getContracts(String userId, String userRole) {

	    com.wcr.wcrbackend.entity.User user =
	            userRepository.findById(userId).orElseThrow();

	    if (CommonUtil.isITAdminOrSuperUser(user)) {
	        return contractRepo.findAllContracts();

	    } else if ("Contractor".equals(userRole)) {
	        return contractRepo.findContractsByUserId(userId);

	    } else {
	        return contractRepo.findContractsForOtherUsers(userId);
	    }
	}

//	
//	   private List<ContractDTO> getAllContracts() {
//		List<ContractDTO> projectDTOs = new ArrayList<>();
//		for (Contract contract : contractRepo.findAllContracts()) {
//			projectDTOs.add(ContractDTO.builder()
//					.id(contract.getContract_id())
//					.name(contract.getContract_short_name())		
//					.build());
//		}
//		return projectDTOs;
//	}
//		
//        private List<ContractDTO> getContractsByUserId(String userId) {
//		List<ContractDTO> projectDTOs = new ArrayList<>();
//		for (String contract : contractRepo.findContractsByUserId(userId)) {
//			projectDTOs.add(ContractDTO.builder()
//					.id(contract)
//					.name(contract)		
//					.build());
//		}
//		return projectDTOs;
//	}
//	
//	
//	private List<ContractDTO> getContractsForOtherUsersByUserId(String userId) {
//		List<ContractDTO> projectDTOs = new ArrayList<>();
//		for (String contract : contractRepo.findContractsForOtherUsers(userId)) {
//			projectDTOs.add(ContractDTO.builder()
//					.id(contract)
//					.name(contract)		
//					.build());
//		}
//		return projectDTOs;
//	}
	
	@Override
	public String updateContract(Contract contract)throws Exception{
		return contractRepo.updateContract(contract);

	}
}
