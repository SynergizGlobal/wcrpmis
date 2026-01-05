 package com.wcr.wcrbackend.reference.Iservice;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface PurposeOfSubmissionService {

	TrainingType getPurposeOfSubmissionDetails(TrainingType obj) throws Exception;

	boolean addPurposeOfSubmission(TrainingType obj) throws Exception;

	boolean updatePurposeOfSubmission(TrainingType obj) throws Exception;

	boolean deletePurposeOfSubmission(TrainingType obj) throws Exception;

}
