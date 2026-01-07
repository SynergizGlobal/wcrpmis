package com.wcr.wcrbackend.reference.Idao;

import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

public interface LaLandStatusDao {

	TrainingType getLandAcquisitionStatusDetails(TrainingType obj) throws Exception;

	boolean addLaStatus(Safety obj) throws Exception;

	boolean updatelandAcquisitionStatus(TrainingType obj) throws Exception;

	boolean deletelandAcquisitionStatus(TrainingType obj) throws Exception;

}
