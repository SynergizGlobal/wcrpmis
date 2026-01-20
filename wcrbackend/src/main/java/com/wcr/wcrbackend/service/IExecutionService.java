package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.ExecutionProgress;

public interface IExecutionService {

	List<ExecutionProgress> getExecutionProgress(String projectId);

	String getProjectName(String projectId);

	String getContractName(String contractId);

	List<ExecutionProgress> getContractsList(String projectId) throws Exception;
}
