package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;

public interface UtilityRequirementStageService {
	
	public List<Safety> getUtilityRequirementStagesList() throws Exception;
	public Safety getUtilityRequirementStagesList(Safety obj) throws Exception;

	public boolean addUtilityRequirementStage(Safety obj) throws Exception;
	public boolean updateUtilityRequirementStage(Safety obj) throws Exception;
	public boolean deleteUtilityRequirementStage(Safety obj) throws Exception;

}