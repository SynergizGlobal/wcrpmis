package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface GeneralStatusService {
	
	public List<Safety> getGeneralStatusList() throws Exception;

	public boolean addGeneralStatus(Safety obj) throws Exception;

	public TrainingType getGeneralStatusDetails(TrainingType obj) throws Exception;

	public boolean updateGeneralStatus(TrainingType obj) throws Exception;

	public boolean deleteGeneralStatus(TrainingType obj) throws Exception;

}
