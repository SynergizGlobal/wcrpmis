package com.wcr.wcrbackend.service;

import java.util.List;

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
		// TODO Auto-generated method stub
		return contractReportRepository.getProjectList();
	}
	@Override
	public List<Contract> getHODListInContractReport(Contract obj) throws Exception {
		// TODO Auto-generated method stub
		return contractReportRepository.getHODListInContractReport(obj);
	}
	@Override
	public List<Contract> getContractorsListInContractReport(Contract obj) throws Exception {
		// TODO Auto-generated method stub
		return contractReportRepository.getContractorsListInContractReport(obj);
	}
	@Override
	public List<Contract> getContractStatusListInContractReport(Contract obj) throws Exception {
		// TODO Auto-generated method stub
		return contractReportRepository.getContractStatusListInContractReport(obj);
	}
	@Override
	public List<Contract> getStatsuListInContractReport(Contract obj) throws Exception {
		// TODO Auto-generated method stub
		return contractReportRepository.getStatsuListInContractReport(obj);
	}
	@Override
	public List<Contract> getStatusofWorkItems(Contract obj) throws Exception {
		// TODO Auto-generated method stub
		return contractReportRepository.getStatusofWorkItems(obj);
	}
	@Override
	public List<Contract> getContractListInContractReport(Contract obj) throws Exception {
		// TODO Auto-generated method stub
		return contractReportRepository.getContractListInContractReport(obj);
	}

}
