package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;




public interface ContractTypeService {

	public List<TrainingType> getContractTypesList() throws Exception;

	public boolean addContractType(TrainingType obj) throws Exception;

	public TrainingType getContractTypeDetails(TrainingType obj) throws Exception;

	public boolean updateContractType(TrainingType obj) throws Exception;

	public boolean deleteContractType(TrainingType obj) throws Exception;


}
