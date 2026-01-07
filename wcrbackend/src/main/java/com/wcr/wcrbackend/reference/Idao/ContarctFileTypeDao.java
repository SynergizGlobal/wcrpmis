package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface ContarctFileTypeDao {

	List<TrainingType> getcontractFileType(TrainingType obj) throws Exception;

	boolean addContractFileType(TrainingType obj) throws Exception;

	boolean updateContractFileType(TrainingType obj) throws Exception;

	boolean deleteContractFileType(TrainingType obj) throws Exception;

}
