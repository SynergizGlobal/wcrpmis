package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;


public interface ITemplateUploadRepository {
	
	List<TrainingType> getTemplatesList() throws Exception;

	boolean uploadTemplate(TrainingType obj) throws Exception;

	boolean deleteTemplate(TrainingType obj) throws Exception;

}
