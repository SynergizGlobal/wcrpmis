package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.PurposeOfSubmissionDao;
import com.wcr.wcrbackend.reference.Iservice.PurposeOfSubmissionService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class PurposeOfSubmissionServiceImpl implements PurposeOfSubmissionService{

	@Autowired
	PurposeOfSubmissionDao dao;

	@Override
	public TrainingType getPurposeOfSubmissionDetails(TrainingType obj) throws Exception {
		return dao.getPurposeOfSubmissionDetails(obj);
	}

	@Override
	public boolean addPurposeOfSubmission(TrainingType obj) throws Exception {
		return dao.addPurposeOfSubmission(obj);
	}

	@Override
	public boolean updatePurposeOfSubmission(TrainingType obj) throws Exception {
		return dao.updatePurposeOfSubmission(obj);
	}

	@Override
	public boolean deletePurposeOfSubmission(TrainingType obj) throws Exception {
		return dao.deletePurposeOfSubmission(obj);
	}
}
