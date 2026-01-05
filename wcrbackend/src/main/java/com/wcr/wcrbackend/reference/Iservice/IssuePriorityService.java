package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface IssuePriorityService {

	public List<Safety> getIssuePriorityList() throws Exception;

	public boolean addIssuePriority(Safety obj) throws Exception;

	public TrainingType getIssuePriorityDetails(TrainingType obj) throws Exception;

	public boolean updateIssuePriority(TrainingType obj) throws Exception;

	public boolean deleteIssuePriority(TrainingType obj) throws Exception;
}
