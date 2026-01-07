package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface StructureTypeService {
	
	public List<Safety> getStructureTypesList() throws Exception;

	public boolean addStructureType(Safety obj) throws Exception;

	public TrainingType getStructureTypeDetails(TrainingType obj) throws Exception;

	public boolean updateStructureType(TrainingType obj) throws Exception;

	public boolean deleteStructureType(TrainingType obj) throws Exception;

}
