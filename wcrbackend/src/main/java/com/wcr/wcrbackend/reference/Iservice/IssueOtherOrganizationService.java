package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface IssueOtherOrganizationService {

	List<TrainingType> getIssueOtherOrganizationDetails(TrainingType obj) throws Exception;

	boolean addIssueOtherOrganization(TrainingType obj) throws Exception;

	boolean updateIssueOtherOrganization(TrainingType obj) throws Exception;

	boolean deleteIssueOtherOrganization(TrainingType obj) throws Exception;

}
