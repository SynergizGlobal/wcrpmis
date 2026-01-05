package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.ApprovalAuthorityDao;
import com.wcr.wcrbackend.reference.Iservice.ApprovalAuthorityService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class ApprovalAuthorityServiceImpl implements ApprovalAuthorityService{
	
	@Autowired
	ApprovalAuthorityDao dao;

	@Override
	public TrainingType getApprovalAuthorityDetails(TrainingType obj) throws Exception {
		return dao.getApprovalAuthorityDetails(obj);
	}

	@Override
	public boolean addApprovalAuthority(TrainingType obj) throws Exception {
		return dao.addApprovalAuthority(obj);
	}

	@Override
	public boolean updateApprovalAuthority(TrainingType obj) throws Exception {
		return dao.updateApprovalAuthority(obj);
	}

	@Override
	public boolean deleteApprovalAuthority(TrainingType obj) throws Exception {
		return dao.deleteApprovalAuthority(obj);
	}
}
