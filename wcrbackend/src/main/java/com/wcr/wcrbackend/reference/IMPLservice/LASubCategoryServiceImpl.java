package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.LASubCategoryDao;
import com.wcr.wcrbackend.reference.Iservice.LASubCategoryService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class LASubCategoryServiceImpl implements LASubCategoryService{
	
	@Autowired
	LASubCategoryDao dao;

	@Override
	public List<TrainingType> getLASubCategoryList() throws Exception {
		return dao.getLASubCategoryList();
	}

	@Override
	public boolean addLASubCategory(TrainingType obj) throws Exception {
		return dao.addLASubCategory(obj);
	}

	@Override
	public TrainingType getLandAcquisitionSubCategoryDetails(TrainingType obj) throws Exception {
		return dao.getLandAcquisitionSubCategoryDetails(obj);
	}

	@Override
	public boolean updateLandAcquisitionSubCategory(TrainingType obj) throws Exception {
		return dao.updateLandAcquisitionSubCategory(obj);
	}

	@Override
	public boolean deleteLandAcquisitionSubCategory(TrainingType obj) throws Exception {
		return dao.deleteLandAcquisitionSubCategory(obj);
	}

	@Override
	public List<TrainingType> getLASubCategory(TrainingType obj) throws Exception {
		return dao.getLASubCategory(obj);
	}

}
