package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface DesignResponsibleExecutivesDao {

	List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception;

	boolean addDesignExecutives(TrainingType obj) throws Exception;

	boolean updateDesignExecutives(TrainingType obj) throws Exception;

	List<TrainingType> getProjectDetails(TrainingType obj) throws Exception;

	List<TrainingType> getUsersDetails(TrainingType obj) throws Exception;

}
