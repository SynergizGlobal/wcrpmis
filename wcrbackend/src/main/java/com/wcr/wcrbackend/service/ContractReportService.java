package com.wcr.wcrbackend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.repo.IContractReportRepository;

@Service
public class ContractReportService implements IContractReportService {

	@Autowired
	private IContractReportRepository contractReportRepository;
	@Override
	public List<Contract> getProjectList() throws Exception {
		return contractReportRepository.getProjectList();
	}
	@Override
	public List<Contract> getHODListInContractReport(Contract obj) throws Exception {
		return contractReportRepository.getHODListInContractReport(obj);
	}

	@Override
	public List<Contract> getContractorsListInContractReport(Contract obj) throws Exception {
		return contractReportRepository.getContractorsListInContractReport(obj);
	}

	@Override
	public List<Contract> getContractStatusListInContractReport(Contract obj) throws Exception {
		return contractReportRepository.getContractStatusListInContractReport(obj);
	}

	@Override
	public Map<String,List<Contract>> getContractsListForReport(Contract obj) throws Exception {
		return contractReportRepository.getContractsListForReport(obj);
	}
	
	@Override
	public Contract generateListofContractsReport(Contract obj) throws Exception {
		return contractReportRepository.generateListofContractsReport(obj);
	}
	
	@Override
	public Contract generateContractBgInsuranceReport(Contract obj) throws Exception
	{
		return contractReportRepository.generateContractBgInsuranceReport(obj);
	}
	
	@Override
	public Contract generateContractCompletionReport(Contract obj) throws Exception
	{
		return contractReportRepository.generateContractCompletionReport(obj);
	}

	@Override
	public Map<String,List<Contract>> getContractsBankGuaranteeForReport(Contract obj) throws Exception {
		return contractReportRepository.getContractsBankGuaranteeForReport(obj);
	}

	@Override
	public Map<String,List<Contract>> getContractsInsuranceForReport(Contract obj) throws Exception {
		return contractReportRepository.getContractsInsuranceForReport(obj);
	}
	
	@Override
	public Map<String,List<Contract>> getContractsDocReport(Contract obj) throws Exception {
		return contractReportRepository.getContractsDocReport(obj);
	}

	@Override
	public Map<String,List<Contract>> getContractsDocBGInsuranceForReport(Contract obj) throws Exception {
		return contractReportRepository.getContractsDocBGInsuranceForReport(obj);
	}
	@Override
	public Map<String,List<Contract>> getContractsDocBGInsuranceForAutoEmailReport(Contract obj) throws Exception {
		return contractReportRepository.getContractsDocBGInsuranceForAutoEmailReport(obj);
	}

	@Override
	public List<Contract> getContractListInContractReport(Contract obj) throws Exception {
		return contractReportRepository.getContractListInContractReport(obj);
	}

	@Override
	public Contract getContractDetailsForReport(Contract obj) throws Exception {
		return contractReportRepository.getContractDetailsForReport(obj);
	}

	@Override
	public Contract getProgressDetailsAsOnDate(Contract obj) throws Exception {
		return contractReportRepository.getProgressDetailsAsOnDate(obj);
	}

	@Override
	public List<Contract> getMilestoneDetailsForReport(Contract obj) throws Exception {
		return contractReportRepository.getMilestoneDetailsForReport(obj);
	}

	@Override
	public List<Contract> getBGDetailsForReport(Contract obj) throws Exception {
		return contractReportRepository.getBGDetailsForReport(obj);
	}

	@Override
	public List<Contract> getInsuranceDetailsForReport(Contract obj) throws Exception {
		return contractReportRepository.getInsuranceDetailsForReport(obj);
	}

	@Override
	public Contract getContractClosureDetails(Contract obj) throws Exception {
		return contractReportRepository.getContractClosureDetails(obj);
	}

	@Override
	public Contract getContractorDetails(Contract obj) throws Exception {
		return contractReportRepository.getContractorDetails(obj);
	}

	@Override
	public List<Contract> getKeyPersonnelForReport(Contract obj) throws Exception {
		return contractReportRepository.getKeyPersonnelForReport(obj);
	}
	@Override
	public List<Contract> getStatusofWorkItems(Contract obj) throws Exception {
		return contractReportRepository.getStatusofWorkItems(obj);
	}	
	
	@Override
	public String getEmailIdsOfDepartments(String management) throws Exception {
		return contractReportRepository.getEmailIdsOfDepartments(management);
	}

	@Override
	public List<Contract> getStatsuListInContractReport(Contract obj) throws Exception {
		return contractReportRepository.getStatsuListInContractReport(obj);
	}

	@Override
	public List<Contract> getTheListOfExpiringBgs(Contract obj) throws Exception {
		
		return contractReportRepository.getTheListOfExpiringBgs(obj);
	}

	@Override
	public List<Contract> getContractDownload(Contract obj) throws Exception {
		
		return contractReportRepository.getContractDownload(obj);
	}

	@Override
	public boolean UpdateLetterStatus(Contract obj) throws Exception {
		
		return contractReportRepository.UpdateLetterStatus(obj);
	}

	@Override
	public List<Contract> generateContractBGDetails(Contract obj) throws Exception {
		
		return contractReportRepository.generateContractBGDetails(obj);
	}

	@Override
	public List<Contract> getTheListOfExpiringInsurances(Contract obj) throws Exception {
		return contractReportRepository.getTheListOfExpiringInsurances(obj);
	}

	@Override
	public List<Contract> generatContractInsuranceDetails(Contract obj) throws Exception {
		
		return contractReportRepository.generatContractInsuranceDetails(obj);
	}

	@Override
	public boolean UpdateInsuranceLetterStatus(Contract obj) throws Exception {
		return contractReportRepository.UpdateInsuranceLetterStatus(obj);
	}

	@Override
	public boolean UpdateDateOfCompletionLetterStatus(Contract obj) throws Exception {
		return contractReportRepository.UpdateDateOfCompletionLetterStatus(obj);
	}

	@Override
	public List<Contract> generatContractDOCDetails(Contract obj) throws Exception {
		return contractReportRepository.generatContractDOCDetails(obj);
	}

	@Override
	public List<Contract> getTheListOfExpiringDocs(Contract obj) throws Exception {
		return contractReportRepository.getTheListOfExpiringDocs(obj);
	}

}
