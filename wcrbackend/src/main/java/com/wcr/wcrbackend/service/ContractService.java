package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.repo.IContractRepo;

@Service
public class ContractService implements IContractService {

	@Autowired
	private IContractRepo contractRepo;
	
	@Override
	public List<Contract> getDepartmentList() throws Exception{
		// TODO Auto-generated method stub
		return contractRepo.getDepartmentList();
	}

}
