package com.wcr.wcrbackend.reference.Idao;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface DesignFileTypeDao {

	TrainingType getDesignFileTypeDetails(TrainingType obj) throws Exception;

	boolean addDesignFileType(TrainingType obj) throws Exception;

	boolean updateDesignFileType(TrainingType obj) throws Exception;

	boolean deleteDesignFileType(TrainingType obj) throws Exception;

}
