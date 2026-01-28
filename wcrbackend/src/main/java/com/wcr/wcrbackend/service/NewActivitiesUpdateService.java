package com.wcr.wcrbackend.service;

import java.sql.ResultSet;
import java.util.List;

import com.wcr.wcrbackend.DTO.StripChart;

public interface NewActivitiesUpdateService {

	public StripChart getAcivitiesBulkData(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesUpdateProjectsList(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesUpdateWorksList(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesUpdateContractsList(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesUpdateStructures(StripChart obj) throws Exception;
	
	public List<StripChart> getDeleteActivitiesStructures(StripChart obj) throws Exception;
	
	public List<StripChart> getNewActivitiesUpdateInProgressStructures(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesUpdateLines(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesUpdateSections(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesUpdateComponentsList(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesUpdateComponentIds(StripChart obj) throws Exception;
	
	public List<StripChart> getDeleteActivitiesComponentsList(StripChart obj) throws Exception;

	public List<StripChart> getDeleteActivitiesComponentIds(StripChart obj) throws Exception;	

	public List<StripChart> getAcivitiesBulkActivitiesList(StripChart obj) throws Exception;

	public StripChart getNewActivitiesUpdateDetails(StripChart obj) throws Exception;

	public List<StripChart> getNewActivitiesfiltersList(StripChart obj) throws Exception;
	
	List<StripChart> getStructureTypesInActivitiesUpdate(StripChart obj) throws Exception;
	
	List<StripChart> getStructureTypesInDeleteActivities(StripChart obj) throws Exception;

	public boolean updateNewAcivitiesBulk(StripChart obj) throws Exception;
	
	public boolean updateModifyActualsBulk(StripChart obj) throws Exception;
	
	public boolean deleteAcivitiesBulk(StripChart obj) throws Exception;
	
	public boolean insertFOBDailyUpdate(StripChart obj) throws Exception;

	public StripChart getNewAcivitiesUpdateData(StripChart obj) throws Exception;
	
	public List<StripChart> getDeleteActivitiesfiltersList(StripChart obj) throws Exception;

	public List<StripChart> getProjectsList(StripChart obj) throws Exception;

	public List<StripChart> getWorksList(StripChart obj) throws Exception;

	public List<StripChart> getContractsList(StripChart obj) throws Exception;	
	
	public boolean uploadNewActivities(List<StripChart> stripChartList) throws Exception;
	
	public ResultSet getExportActivitiesbyContract(StripChart obj) throws Exception;	
	
	public List<StripChart> getTaskCodesList(StripChart obj) throws Exception;
	
	public List<StripChart> getContractStructures(StripChart obj) throws Exception;

	public List<StripChart> getLatestRowData(StripChart obj) throws Exception;

	public List<StripChart> bindData(StripChart obj) throws Exception;

	public List<StripChart> getLastUpdateRows(StripChart obj) throws Exception;
	
}
