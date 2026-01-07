package com.wcr.wcrbackend.reference.Idao;
import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;



public interface ContractResponsibleExecutivesDao {

	List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception;

	boolean addContractExecutives(TrainingType obj) throws Exception;

	boolean updateContractExecutives(TrainingType obj) throws Exception;

	List<TrainingType> getWorkDetails(TrainingType obj) throws Exception;

	List<TrainingType> getUsersDetails(TrainingType obj) throws Exception;

}
