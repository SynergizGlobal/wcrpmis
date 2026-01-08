package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;
import com.wcr.wcrbackend.reference.model.TrainingType;
public interface DocumentTypeService {

	public List<TrainingType> getDocumentTypesList() throws Exception;

	public boolean addDocumentType(TrainingType obj) throws Exception;
	
	public TrainingType getDocumentTypeDetails(TrainingType obj) throws Exception;

	public boolean updateDocumentType(TrainingType obj) throws Exception;

	public boolean deleteDocumentType(TrainingType obj) throws Exception;


}
