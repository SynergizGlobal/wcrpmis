package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;


public interface BackgroundTypeDao {


	public List<TrainingType> getBackgroundTypesList() throws Exception;

	public boolean addBackgroundType(TrainingType obj) throws Exception;

	public boolean updateBackgroundType(TrainingType obj) throws Exception;

	public TrainingType getBankGuaranteeDetails(TrainingType obj) throws Exception;

	public boolean deleteBankGuaranteeType(TrainingType obj) throws Exception;

}
