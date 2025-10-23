package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.repo.ISafetyRepo;
@Service
public class SafetyService implements ISafetyService {

	@Autowired
	ISafetyRepo safetyRepo;
	@Override
	public List<Safety> getProjectsListForSafetyForm(Safety obj) throws Exception {
		// TODO Auto-generated method stub
		return safetyRepo.getProjectsListForSafetyForm(obj);
	}
	@Override
	public List<Safety> getContractsListForSafetyForm(Safety obj) throws Exception {
		// TODO Auto-generated method stub
		return safetyRepo.getContractsListForSafetyForm(obj);
	}

}
