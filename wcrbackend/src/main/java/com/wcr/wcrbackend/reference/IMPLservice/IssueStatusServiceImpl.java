package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.reference.Idao.IssueStatusDao;
import com.wcr.wcrbackend.reference.Iservice.IssueStatusService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class IssueStatusServiceImpl implements IssueStatusService{
	@Autowired
	IssueStatusDao dao;

	@Override
	public List<Safety> getIssueStatusList() throws Exception {
		return dao.getIssueStatusList();
	}

	@Override
	public boolean addIssueStatus(Safety obj) throws Exception {
		return dao.addIssueStatus(obj);
	}

	@Override
	public TrainingType getIssueStatusDetails(TrainingType obj) throws Exception {
		return dao.getIssueStatusDetails(obj);
	}

	@Override
	public boolean updateIssueStatus(TrainingType obj) throws Exception {
		return dao.updateIssueStatus(obj);
	}

	@Override
	public boolean deleteIssueStatus(TrainingType obj) throws Exception {
		return dao.deleteIssueStatus(obj);
	}
}
