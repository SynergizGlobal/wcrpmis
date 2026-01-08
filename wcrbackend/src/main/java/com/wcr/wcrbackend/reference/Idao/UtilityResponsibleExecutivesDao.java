package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface UtilityResponsibleExecutivesDao {

	List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception;

	boolean addUtilityShiftingExecutives(TrainingType obj) throws Exception;

	boolean updateUtilityShiftingExecutives(TrainingType obj) throws Exception;

	List<TrainingType> getProjectDetails(TrainingType obj) throws Exception;

}
