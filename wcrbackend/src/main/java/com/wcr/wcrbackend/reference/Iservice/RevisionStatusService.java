package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Risk;
import com.wcr.wcrbackend.reference.model.TrainingType;



public interface RevisionStatusService {
	
	public List<Risk> getRevisionStatusList() throws Exception;

	public boolean addRevisionStatus(Risk obj) throws Exception;

	public TrainingType getRevisionStatusDetails(TrainingType obj) throws Exception;

	public boolean updateRevisionStatus(TrainingType obj) throws Exception;

	public boolean deleteRevisionStatus(TrainingType obj) throws Exception;
}
