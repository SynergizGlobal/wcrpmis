package com.wcr.wcrbackend.reference.Idao;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface SubmittedDao {

	TrainingType getSubmittedDetails(TrainingType obj) throws Exception;

	boolean addSubmitted(TrainingType obj) throws Exception;

	boolean updateSubmitted(TrainingType obj) throws Exception;

	boolean deleteSubmitted(TrainingType obj) throws Exception;

}
