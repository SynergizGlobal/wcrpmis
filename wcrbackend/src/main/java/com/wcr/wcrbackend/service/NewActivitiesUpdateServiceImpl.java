package com.wcr.wcrbackend.service;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.StripChart;
import com.wcr.wcrbackend.repo.NewActivitiesUpdateDao;

@Service
public class NewActivitiesUpdateServiceImpl implements NewActivitiesUpdateService{

	@Autowired
	NewActivitiesUpdateDao dao;
	
	@Override
	public StripChart getAcivitiesBulkData(StripChart obj) throws Exception {
		return dao.getAcivitiesBulkData(obj);
	}

	@Override
	public List<StripChart> getNewActivitiesUpdateProjectsList(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateProjectsList(obj);
	}

	@Override
	public List<StripChart> getNewActivitiesUpdateWorksList(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateWorksList(obj);
	}

	@Override
	public List<StripChart> getNewActivitiesUpdateContractsList(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateContractsList(obj);
	}

	@Override
	public List<StripChart> getNewActivitiesUpdateStructures(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateStructures(obj);
	}
	
	@Override
	public List<StripChart> getDeleteActivitiesStructures(StripChart obj) throws Exception {
		return dao.getDeleteActivitiesStructures(obj);
	}	
	
	@Override
	public List<StripChart> getNewActivitiesUpdateInProgressStructures(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateInProgressStructures(obj);
	}	

	@Override
	public List<StripChart> getNewActivitiesUpdateLines(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateLines(obj);
	}

	@Override
	public List<StripChart> getNewActivitiesUpdateSections(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateSections(obj);
	}
	
	@Override
	public List<StripChart> getNewActivitiesUpdateComponentsList(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateComponentsList(obj);
	}

	@Override
	public List<StripChart> getNewActivitiesUpdateComponentIds(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateComponentIds(obj);
	}
	
	@Override
	public List<StripChart> getDeleteActivitiesComponentsList(StripChart obj) throws Exception {
		return dao.getDeleteActivitiesComponentsList(obj);
	}

	@Override
	public List<StripChart> getDeleteActivitiesComponentIds(StripChart obj) throws Exception {
		return dao.getDeleteActivitiesComponentIds(obj);
	}	

	@Override
	public List<StripChart> getAcivitiesBulkActivitiesList(StripChart obj) throws Exception {
		return dao.getAcivitiesBulkActivitiesList(obj);
	}

	@Override
	public StripChart getNewActivitiesUpdateDetails(StripChart obj) throws Exception {
		return dao.getNewActivitiesUpdateDetails(obj);
	}

	@Override
	public List<StripChart> getNewActivitiesfiltersList(StripChart obj) throws Exception {
		return dao.getActivitiesfiltersList(obj);
	}

	@Override
	public boolean updateNewAcivitiesBulk(StripChart obj) throws Exception {
		return dao.updateAcivitiesBulk(obj);
	}
	
	@Override
	public boolean deleteAcivitiesBulk(StripChart obj) throws Exception {
		return dao.deleteAcivitiesBulk(obj);
	}	
	
	@Override
	public boolean insertFOBDailyUpdate(StripChart obj) throws Exception {
		return dao.insertFOBDailyUpdate(obj);
	}
	
	@Override
	public List<StripChart> getStructureTypesInActivitiesUpdate(StripChart obj) throws Exception {
		return dao.getStructureTypesInActivitiesUpdate(obj);
	}
	
	@Override
	public List<StripChart> getStructureTypesInDeleteActivities(StripChart obj) throws Exception {
		return dao.getStructureTypesInDeleteActivities(obj);
	}	

	@Override
	public StripChart getNewAcivitiesUpdateData(StripChart obj) throws Exception {
		return dao.getNewAcivitiesUpdateData(obj);
	}
	
	@Override
	public List<StripChart> getProjectsList(StripChart obj) throws Exception {
		return dao.getProjectsList(obj);
	}

	@Override
	public List<StripChart> getWorksList(StripChart obj) throws Exception {
		return dao.getWorksList(obj);
	}

	@Override
	public List<StripChart> getContractsList(StripChart obj) throws Exception {
		return dao.getContractsList(obj);
	}	

	@Override
	public List<StripChart> getDeleteActivitiesfiltersList(StripChart obj) throws Exception {
		return dao.getDeleteActivitiesfiltersList(obj);
	}	
	
	@Override
	public boolean uploadNewActivities(List<StripChart> stripChartList) throws Exception
	{
		return dao.uploadNewActivities(stripChartList);
	}
	
	@Override
	public ResultSet getExportActivitiesbyContract(StripChart obj) throws Exception
	{
		return dao.getExportActivitiesbyContract(obj);
	}

	@Override
	public List<StripChart> getTaskCodesList(StripChart obj) throws Exception {
		return dao.getTaskCodesList(obj);
	}

	@Override
	public boolean updateModifyActualsBulk(StripChart obj) throws Exception {
		return dao.updateModifyActualsBulk(obj);
		
	}
	
	@Override
	public List<StripChart> getContractStructures(StripChart obj) throws Exception
	{
		return dao.getContractStructures(obj);
	}

	@Override
	public List<StripChart> getLatestRowData(StripChart obj) throws Exception {
		return dao.getLatestRowData(obj);
	}

	@Override
	public List<StripChart> bindData(StripChart obj) throws Exception {
		return dao.bindData(obj);
	}

	@Override
	public List<StripChart> getLastUpdateRows(StripChart obj) throws Exception {
		return dao.getLastUpdateRows(obj);
	}
}
