package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface LandResponsibleExecutivesDao {

	List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception;

	boolean addLandAcquisitionExecutives(TrainingType obj) throws Exception;

	boolean updateLandAcquisitionExecutives(TrainingType obj) throws Exception;

	List<TrainingType> getProjectDetails(TrainingType obj) throws Exception;

}
