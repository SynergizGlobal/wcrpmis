package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.entity.Activity;


public interface ProgressApprovalDao {

	List<Activity> getApprovableActivities(Activity obj) throws Exception;
	
	List<Activity> getWorksInApprovableActivities(Activity obj) throws Exception;

	List<Activity> getContractsInApprovableActivities(Activity obj) throws Exception;
	
	List<Activity> getStructuresInApprovableActivities(Activity obj) throws Exception;
	
	List<Activity> getDepartmentsInApprovableActivities(Activity obj) throws Exception;
	
	List<Activity> getUpdatedByListInApprovableActivities(Activity obj) throws Exception;

	Activity approveActivityProgress(Activity obj) throws Exception;

	Activity rejectActivityProgress(Activity obj) throws Exception;

	Activity approveMultipleActivityProgress(Activity obj) throws Exception;

	Activity rejectMultipleActivityProgress(Activity obj) throws Exception;

}
