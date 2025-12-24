package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.BankGuarantee;
import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.DTO.Insurence;
import com.wcr.wcrbackend.DTO.User;

 public interface IContractService {

	public List<Contract> getDepartmentList() throws Exception;
	public List<Contract> getDesignationsFilterList(Contract obj) throws Exception;
	public List<Contract> getDyHODDesignationsFilterList(Contract obj) throws Exception;
	public List<Contract> contractorsFilterList(Contract obj)throws Exception;
	public List<Contract> getContractStatusFilterListInContract(Contract obj) throws Exception;
	public List<Contract> getStatusFilterListInContract(Contract obj) throws Exception;
	public List<Contract> contractList(Contract obj)throws Exception;
    public List<Contract> getProjectsListForContractForm(Contract obj) throws Exception;
    public List<Contract> getWorkListForContractForm(Contract obj) throws Exception;
    public List<Contract> getContractFileTypeList(Contract obj)throws Exception;
    public List<User> setHodList()throws Exception;
    public List<User> getDyHodList() throws Exception;
	public List<Contract> getContractorsList()throws Exception; 
	public List<Contract> getContractTypeList()throws Exception;
	public List<Contract> getInsurenceTypeList()throws Exception;
	public List<BankGuarantee> bankGuarantee()throws Exception;
	public List<Insurence> insurenceType()throws Exception;
	public List<Contract> getResponsiblePeopleList(Contract obj)throws Exception;
	public List<Contract> getUnitsList(Contract obj) throws Exception;
	public List<Contract> getContractStatus() throws Exception;
	public Contract getContract(Contract obj)throws Exception;
	public List<Contract> getBankNameList(Contract obj) throws Exception;
	public List<Contract> getContractStatusType(Contract obj)throws Exception;
	public String addContract(Contract contract)throws Exception;
	public List<Contract> getExecutivesListForContractForm(Contract obj) throws Exception;
	public List<Contract> getHodList(Contract obj) throws Exception;
	public List<Contract> getDyHodList(Contract obj) throws Exception;
	public List<Contract> contractListForExport(Contract contract) throws Exception;	
	public List<Contract> contractRevisionsList(Contract contract) throws Exception;
	public List<Contract> contractBGList(Contract contract) throws Exception;
	public List<Contract> contractInsuranceList(Contract contract) throws Exception;
	public List<Contract> contractMilestoneList(Contract contract) throws Exception;

	
}
