package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.reference.Idao.IssueCategoryDao;
import com.wcr.wcrbackend.reference.Iservice.IssueCategoryService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class IssueCategoryServiceImpl implements IssueCategoryService{

	@Autowired
	IssueCategoryDao dao;

	@Override
	public List<Safety> getIssueCategoryList() throws Exception {
		return dao.getIssueCategoryList();
	}

	@Override
	public boolean addIssueCategory(Safety obj) throws Exception {
		return dao.addIssueCategory(obj);
	}

	@Override
	public TrainingType getIssueCategoryDetails(TrainingType obj) throws Exception {
		return dao.getIssueCategoryDetails(obj);
	}

	@Override
	public boolean updateIssueCategory(TrainingType obj) throws Exception {
		return dao.updateIssueCategory(obj);
	}

	@Override
	public boolean deleteIssueCategory(TrainingType obj) throws Exception {
		return dao.deleteIssueCategory(obj);
	}
}
