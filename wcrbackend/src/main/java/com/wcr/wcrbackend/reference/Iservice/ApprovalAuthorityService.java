package com.wcr.wcrbackend.reference.Iservice;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface ApprovalAuthorityService {

	TrainingType getApprovalAuthorityDetails(TrainingType obj) throws Exception;

	boolean addApprovalAuthority(TrainingType obj) throws Exception;

	boolean updateApprovalAuthority(TrainingType obj) throws Exception;

	boolean deleteApprovalAuthority(TrainingType obj) throws Exception;

}
