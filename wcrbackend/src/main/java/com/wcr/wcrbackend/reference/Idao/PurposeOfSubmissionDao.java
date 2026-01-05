package com.wcr.wcrbackend.reference.Idao;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface PurposeOfSubmissionDao {

	TrainingType getPurposeOfSubmissionDetails(TrainingType obj) throws Exception;

	boolean addPurposeOfSubmission(TrainingType obj) throws Exception;

	boolean updatePurposeOfSubmission(TrainingType obj) throws Exception;

	boolean deletePurposeOfSubmission(TrainingType obj) throws Exception;

}
