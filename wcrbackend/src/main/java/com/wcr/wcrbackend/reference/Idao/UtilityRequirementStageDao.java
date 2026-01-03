package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;

public interface UtilityRequirementStageDao {

	public List<Safety> getUtilityRequirementStagesList() throws Exception;
	public Safety getUtilityRequirementStagesList(Safety obj) throws Exception;

	public boolean addUtilityRequirementStage(Safety obj) throws Exception;
	public boolean updateUtilityRequirementStage(Safety obj) throws Exception;
	public boolean deleteUtilityRequirementStage(Safety obj) throws Exception;
}
