package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface ExecutionStatusService {

	public List<Safety> getExecutionStatusList() throws Exception;

	public boolean addExecutionStatus(TrainingType obj) throws Exception;

	public TrainingType getExecutionStatusDetails(TrainingType obj) throws Exception;

	public boolean updateExecutionStatus(TrainingType obj) throws Exception;

	public boolean deleteExecutionStatus(TrainingType obj) throws Exception;
}
