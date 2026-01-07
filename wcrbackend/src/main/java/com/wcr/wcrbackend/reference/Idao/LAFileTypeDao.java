package com.wcr.wcrbackend.reference.Idao;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface LAFileTypeDao {

	TrainingType getLAFileTypeDetails(TrainingType obj) throws Exception;

	boolean addLAFileType(TrainingType obj) throws Exception;

	boolean updateLAFileType(TrainingType obj) throws Exception;

	boolean deleteLAFileType(TrainingType obj) throws Exception;

}
