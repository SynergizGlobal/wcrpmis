package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface LandResponsibleExecutivesService {

	List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception;

	boolean addLandAcquisitionExecutives(TrainingType obj) throws Exception;

	boolean updateLandAcquisitionExecutives(TrainingType obj) throws Exception;

	List<TrainingType> getProjectDetails(TrainingType obj) throws Exception;

}
