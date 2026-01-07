package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;




public interface ContractTypeDao {

	public List<TrainingType> getContractTypesList() throws Exception;

	public boolean addContractType(TrainingType obj) throws Exception;

	public TrainingType getContractTypeDetails(TrainingType obj) throws Exception;

	public boolean updateContractType(TrainingType obj) throws Exception;

	public boolean deleteContractType(TrainingType obj) throws Exception;
}
