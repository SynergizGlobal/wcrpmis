package com.wcr.wcrbackend.reference.Iservice;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface StageService {

	TrainingType getStageDetails(TrainingType obj) throws Exception;

	boolean addStage(TrainingType obj) throws Exception;

	boolean updateStage(TrainingType obj) throws Exception;

	boolean deleteStage(TrainingType obj) throws Exception;

}
