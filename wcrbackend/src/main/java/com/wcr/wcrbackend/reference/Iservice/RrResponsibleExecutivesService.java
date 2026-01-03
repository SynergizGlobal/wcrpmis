package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface RrResponsibleExecutivesService {
	
	
	List<TrainingType> getUsersDetails(TrainingType obj) throws Exception;
	
}