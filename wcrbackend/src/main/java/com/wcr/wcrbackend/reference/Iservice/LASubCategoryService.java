package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface LASubCategoryService {

	public List<TrainingType> getLASubCategoryList() throws Exception;

	public boolean addLASubCategory(TrainingType obj) throws Exception;

	public TrainingType getLandAcquisitionSubCategoryDetails(TrainingType obj) throws Exception;

	public boolean updateLandAcquisitionSubCategory(TrainingType obj) throws Exception;

	public boolean deleteLandAcquisitionSubCategory(TrainingType obj) throws Exception;

	public List<TrainingType> getLASubCategory(TrainingType obj) throws Exception;

}
