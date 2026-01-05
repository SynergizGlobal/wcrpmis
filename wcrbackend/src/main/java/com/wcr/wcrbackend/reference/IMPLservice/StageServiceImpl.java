package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.StageDao;
import com.wcr.wcrbackend.reference.Iservice.StageService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class StageServiceImpl implements StageService{

	@Autowired
	StageDao dao;

	@Override
	public TrainingType getStageDetails(TrainingType obj) throws Exception {
		return dao.getStageDetails(obj);
	}

	@Override
	public boolean addStage(TrainingType obj) throws Exception {
		return dao.addStage(obj);
	}

	@Override
	public boolean updateStage(TrainingType obj) throws Exception {
		return dao.updateStage(obj);
	}

	@Override
	public boolean deleteStage(TrainingType obj) throws Exception {
		return dao.deleteStage(obj);
	}
}
