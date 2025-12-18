package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.entity.Activity;


public interface ProgressApprovalService {

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
