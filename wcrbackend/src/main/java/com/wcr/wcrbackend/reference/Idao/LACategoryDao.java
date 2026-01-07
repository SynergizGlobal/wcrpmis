package com.wcr.wcrbackend.reference.Idao;

import java.util.List;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface LACategoryDao {

	public List<TrainingType> getLACategoryList() throws Exception;

	public boolean addLACategory(TrainingType obj) throws Exception;

	public TrainingType getLandAcquisitionCategoryDetails(TrainingType obj) throws Exception;

	public boolean updateLandAcquisitionCategory(TrainingType obj) throws Exception;

	public boolean deleteLandAcquisitionCategory(TrainingType obj) throws Exception;

}
