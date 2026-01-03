package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface RrResponsibleExecutivesDao {
	
	List<TrainingType> getUsersDetails(TrainingType obj) throws Exception;

}