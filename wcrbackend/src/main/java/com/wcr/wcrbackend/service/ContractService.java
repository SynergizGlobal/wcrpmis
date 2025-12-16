package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.BankGuarantee;
import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.Insurence;
import com.wcr.wcrbackend.DTO.User;
import com.wcr.wcrbackend.repo.IContractRepo;

@Service
public class ContractService implements IContractService {

	@Autowired
	private IContractRepo contractRepo;
	
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
}
