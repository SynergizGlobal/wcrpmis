package com.wcr.wcrbackend.repo;

import com.wcr.wcrbackend.DTO.ExecutionProgress;
import java.util.List;

public interface IExecutionRepository {
    List<ExecutionProgress> getExecutionProgress(String projectId);

	String getProjectName(String projectId);

	String getContractName(String contractId);

	List<ExecutionProgress> getContractsList(String projectId) throws Exception;

}
