package com.wcr.wcrbackend.reference.Iservice;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface LAFileTypeService {

	TrainingType getLAFileTypeDetails(TrainingType obj) throws Exception;

	boolean addLAFileType(TrainingType obj) throws Exception;

	boolean updateLAFileType(TrainingType obj) throws Exception;

	boolean deleteLAFileType(TrainingType obj) throws Exception;

}
