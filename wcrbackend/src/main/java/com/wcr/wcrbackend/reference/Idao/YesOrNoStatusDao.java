package com.wcr.wcrbackend.reference.Idao;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface YesOrNoStatusDao {

	public TrainingType getYesOrNoStatusDetails(TrainingType obj) throws Exception;

	public boolean addYesOrNoStatus(TrainingType obj) throws Exception;

	public boolean updateYesOrNoStatus(TrainingType obj) throws Exception;

	public boolean deleteYesOrNoStatus(TrainingType obj) throws Exception;

}
