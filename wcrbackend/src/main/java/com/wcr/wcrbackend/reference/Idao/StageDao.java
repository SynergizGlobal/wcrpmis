package com.wcr.wcrbackend.reference.Idao;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface StageDao {

	TrainingType getStageDetails(TrainingType obj) throws Exception;

	boolean addStage(TrainingType obj) throws Exception;

	boolean updateStage(TrainingType obj) throws Exception;

	boolean deleteStage(TrainingType obj) throws Exception;

}
