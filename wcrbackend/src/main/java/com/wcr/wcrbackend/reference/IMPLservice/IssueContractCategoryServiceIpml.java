package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.IssueContractCategoryDao;
import com.wcr.wcrbackend.reference.Iservice.IssueContractCategoryService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class IssueContractCategoryServiceIpml implements IssueContractCategoryService{
	@Autowired
	IssueContractCategoryDao dao;

	@Override
	public List<TrainingType> getContractTypeDetails(TrainingType obj) throws Exception {
		return dao.getContractTypeDetails(obj);
	}

	@Override
	public List<TrainingType> gtIssueCategoryDetails(TrainingType obj) throws Exception {
		return dao.gtIssueCategoryDetails(obj);
	}

	@Override
	public List<TrainingType> getIssueContractCategory(TrainingType obj) throws Exception {
		return dao.getIssueContractCategory(obj);
	}

	@Override
	public boolean addIssueContractCategory(TrainingType obj) throws Exception {
		return dao.addIssueContractCategory(obj);
	}

	@Override
	public boolean updateIssueContractCategory(TrainingType obj) throws Exception {
		return dao.updateIssueContractCategory(obj);
	}

	@Override
	public boolean deleteIssueContractCategory(TrainingType obj) throws Exception {
		return dao.deleteIssueContractCategory(obj);
	}

	@Override
	public List<TrainingType> getContarctCategory(TrainingType obj) throws Exception {
		return dao.getContarctCategory(obj);
	}
}
