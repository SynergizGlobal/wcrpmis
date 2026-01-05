package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface DrawingTypeService {

	public List<TrainingType> getDrawingTypesList() throws Exception;

	public boolean addDrawingType(TrainingType obj) throws Exception;

	public TrainingType getDrawingTypeDetails(TrainingType obj) throws Exception;

	public boolean updateDrawingType(TrainingType obj) throws Exception;

	public boolean deleteDrawingType(TrainingType obj) throws Exception;
}
