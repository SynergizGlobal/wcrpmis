package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.IssueOtherOrganizationDao;
import com.wcr.wcrbackend.reference.Iservice.IssueOtherOrganizationService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class IssueOtherOrganizationServiceImpl implements IssueOtherOrganizationService{

	@Autowired
	IssueOtherOrganizationDao dao;

	@Override
	public List<TrainingType> getIssueOtherOrganizationDetails(TrainingType obj) throws Exception {
		return dao.getIssueOtherOrganizationDetails(obj);
	}

	@Override
	public boolean addIssueOtherOrganization(TrainingType obj) throws Exception {
		return dao.addIssueOtherOrganization(obj);
	}

	@Override
	public boolean updateIssueOtherOrganization(TrainingType obj) throws Exception {
		return dao.updateIssueOtherOrganization(obj);
	}

	@Override
	public boolean deleteIssueOtherOrganization(TrainingType obj) throws Exception {
		return dao.deleteIssueOtherOrganization(obj);
	}
}
