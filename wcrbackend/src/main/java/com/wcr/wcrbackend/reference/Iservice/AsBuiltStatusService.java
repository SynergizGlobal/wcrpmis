package com.wcr.wcrbackend.reference.Iservice;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface AsBuiltStatusService {

	public TrainingType getAsBuiltStatusDetails(TrainingType obj) throws Exception;

	public boolean addAsBuiltStatus(TrainingType obj) throws Exception;

	public boolean updateAsBuiltStatus(TrainingType obj) throws Exception;

	public boolean deleteAsBuiltStatus(TrainingType obj) throws Exception;

}
