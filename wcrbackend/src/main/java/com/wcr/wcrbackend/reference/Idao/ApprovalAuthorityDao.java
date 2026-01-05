package com.wcr.wcrbackend.reference.Idao;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface ApprovalAuthorityDao {

	TrainingType getApprovalAuthorityDetails(TrainingType obj) throws Exception;

	boolean addApprovalAuthority(TrainingType obj) throws Exception;

	boolean updateApprovalAuthority(TrainingType obj) throws Exception;

	boolean deleteApprovalAuthority(TrainingType obj) throws Exception;

}
