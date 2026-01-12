package com.wcr.wcrbackend.reference.Iservice;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface YesOrNoStatusService {

	public TrainingType getYesOrNoStatusDetails(TrainingType obj) throws Exception;

	public boolean addYesOrNoStatus(TrainingType obj) throws Exception;

	public boolean updateYesOrNoStatus(TrainingType obj) throws Exception;

	public boolean deleteYesOrNoStatus(TrainingType obj) throws Exception;
}
