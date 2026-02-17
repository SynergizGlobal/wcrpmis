package com.wcr.wcrbackend.repo;

import java.util.List;
import java.util.Map;

import com.wcr.wcrbackend.DTO.Contract;

public interface IContractReportRepository {

	List<Contract> getProjectList() throws Exception;

	List<Contract> getHODListInContractReport(Contract obj) throws Exception;

	List<Contract> getContractorsListInContractReport(Contract obj) throws Exception;

	List<Contract> getContractStatusListInContractReport(Contract obj) throws Exception;

	Map<String,List<Contract>> getContractsListForReport(Contract obj) throws Exception;

	Map<String,List<Contract>> getContractsBankGuaranteeForReport(Contract obj) throws Exception;

	Map<String,List<Contract>> getContractsInsuranceForReport(Contract obj) throws Exception;

	Map<String,List<Contract>> getContractsDocReport(Contract obj) throws Exception;

	Map<String,List<Contract>> getContractsDocBGInsuranceForReport(Contract obj) throws Exception;
	
	List<Contract> getContractListInContractReport(Contract obj) throws Exception;
	
	Contract getContractDetailsForReport(Contract obj) throws Exception;

	Contract getProgressDetailsAsOnDate(Contract obj) throws Exception;

	List<Contract> getMilestoneDetailsForReport(Contract obj) throws Exception;

	List<Contract> getBGDetailsForReport(Contract obj) throws Exception;

	List<Contract> getInsuranceDetailsForReport(Contract obj) throws Exception;

	Contract getContractClosureDetails(Contract obj) throws Exception;

	Contract getContractorDetails(Contract obj) throws Exception;

	List<Contract> getKeyPersonnelForReport(Contract obj) throws Exception;

	String getEmailIdsOfDepartments(String management) throws Exception;

	Map<String, List<Contract>> getContractsDocBGInsuranceForAutoEmailReport(Contract obj) throws Exception;

	List<Contract> getStatusofWorkItems(Contract obj) throws Exception;

	List<Contract> getStatsuListInContractReport(Contract obj) throws Exception;

	Contract generateListofContractsReport(Contract obj)  throws Exception;

	Contract generateContractBgInsuranceReport(Contract obj) throws Exception;

	Contract generateContractCompletionReport(Contract obj) throws Exception;

	List<Contract> getTheListOfExpiringBgs(Contract obj) throws Exception;

	List<Contract> getContractDownload(Contract obj) throws Exception;

	boolean UpdateLetterStatus(Contract obj) throws Exception;

	List<Contract> generateContractBGDetails(Contract obj) throws Exception;

	List<Contract> getTheListOfExpiringInsurances(Contract obj) throws Exception;

	List<Contract> generatContractInsuranceDetails(Contract obj) throws Exception;

	boolean UpdateInsuranceLetterStatus(Contract obj) throws Exception;

	boolean UpdateDateOfCompletionLetterStatus(Contract obj)  throws Exception;

	List<Contract> generatContractDOCDetails(Contract obj) throws Exception;

	List<Contract> getTheListOfExpiringDocs(Contract obj) throws Exception;

	

}
