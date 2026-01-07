package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;




public interface ContractorSpecializationsDao {

	public List<TrainingType> getContractorSpecializationsList() throws Exception;

	public boolean addContractorSpecialization(TrainingType obj) throws Exception;

	public TrainingType getContractorSpecializationDetails(TrainingType obj) throws Exception;

	public boolean updateContractorSpecialization(TrainingType obj) throws Exception;

	public boolean deleteContractorSpecialization(TrainingType obj) throws Exception;
}
