package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.LACategoryDao;
import com.wcr.wcrbackend.reference.Iservice.LACategoryService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class LACategoryServiceImpl implements LACategoryService{

	@Autowired
	LACategoryDao dao;

	@Override
	public List<TrainingType> getLACategoryList() throws Exception {
		return dao.getLACategoryList();
	}

	@Override
	public boolean addLACategory(TrainingType obj) throws Exception {
		return dao.addLACategory(obj);
	}

	@Override
	public TrainingType getLandAcquisitionCategoryDetails(TrainingType obj) throws Exception {
		return dao.getLandAcquisitionCategoryDetails(obj);
	}

	@Override
	public boolean updateLandAcquisitionCategory(TrainingType obj) throws Exception {
		return dao.updateLandAcquisitionCategory(obj);
	}

	@Override
	public boolean deleteLandAcquisitionCategory(TrainingType obj) throws Exception {
		return dao.deleteLandAcquisitionCategory(obj);
	}
}
