package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.ExecutionProgress;
import com.wcr.wcrbackend.repo.IExecutionRepository;

@Service
public class ExecutionService implements IExecutionService {

    @Autowired
    private IExecutionRepository executionRepository;

    @Override
    public List<ExecutionProgress> getExecutionProgress(String projectId) {
        return executionRepository.getExecutionProgress(projectId);
    }

	@Override
	public String getProjectName(String projectId) {
		return executionRepository.getProjectName(projectId);
	}

	@Override
	public String getContractName(String contractId) {
		return executionRepository.getContractName(contractId);
	}

	@Override
	public List<ExecutionProgress> getContractsList(String projectId) throws Exception {
		return executionRepository.getContractsList(projectId);
	}

}
