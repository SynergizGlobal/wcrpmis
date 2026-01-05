package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface IssueStatusDao {

	public List<Safety> getIssueStatusList() throws Exception;

	public boolean addIssueStatus(Safety obj) throws Exception;

	public TrainingType getIssueStatusDetails(TrainingType obj) throws Exception;

	public boolean updateIssueStatus(TrainingType obj) throws Exception;

	public boolean deleteIssueStatus(TrainingType obj) throws Exception;
}
