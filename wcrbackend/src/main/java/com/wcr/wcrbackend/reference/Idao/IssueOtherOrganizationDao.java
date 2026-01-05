package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface IssueOtherOrganizationDao {

	List<TrainingType> getIssueOtherOrganizationDetails(TrainingType obj) throws Exception;

	boolean addIssueOtherOrganization(TrainingType obj) throws Exception;

	boolean updateIssueOtherOrganization(TrainingType obj) throws Exception;

	boolean deleteIssueOtherOrganization(TrainingType obj) throws Exception;

}
