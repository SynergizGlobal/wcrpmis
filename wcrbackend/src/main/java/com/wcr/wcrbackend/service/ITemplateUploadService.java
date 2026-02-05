package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface ITemplateUploadService {
	
	List<TrainingType> getTemplatesList() throws Exception;

	boolean uploadTemplate(TrainingType obj) throws Exception;

	boolean deleteTemplate(TrainingType obj) throws Exception;


}
