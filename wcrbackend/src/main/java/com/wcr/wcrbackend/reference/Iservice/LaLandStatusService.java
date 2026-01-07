package com.wcr.wcrbackend.reference.Iservice;

import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface LaLandStatusService {

	TrainingType getLandAcquisitionStatusDetails(TrainingType obj) throws Exception;

	boolean addLaStatus(Safety obj) throws Exception;

	boolean updatelandAcquisitionStatus(TrainingType obj) throws Exception;

	boolean deletelandAcquisitionStatus(TrainingType obj) throws Exception;

}
