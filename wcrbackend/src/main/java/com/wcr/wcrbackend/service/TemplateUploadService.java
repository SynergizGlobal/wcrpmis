package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.model.TrainingType;
import com.wcr.wcrbackend.repo.ITemplateUploadRepository;

@Service
public class TemplateUploadService implements ITemplateUploadService {
	
	@Autowired
	ITemplateUploadRepository repo;

	@Override
	public List<TrainingType> getTemplatesList() throws Exception {
		return repo.getTemplatesList();
	}

	 @Override
	    public boolean uploadTemplate(TrainingType obj) throws Exception {

	        // Repository already:
	        // 1) sets old templates INACTIVE
	        // 2) inserts new ACTIVE template
	        return repo.uploadTemplate(obj);
	    }

	@Override
	public boolean deleteTemplate(TrainingType obj) throws Exception {
		return repo.deleteTemplate(obj);
	}

}