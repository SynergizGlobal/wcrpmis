package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.DTO.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface IssueCategoryDao {
	
	public List<Safety> getIssueCategoryList() throws Exception;

	public boolean addIssueCategory(Safety obj) throws Exception;

	public TrainingType getIssueCategoryDetails(TrainingType obj) throws Exception;

	public boolean updateIssueCategory(TrainingType obj) throws Exception;

	public boolean deleteIssueCategory(TrainingType obj) throws Exception;
}
