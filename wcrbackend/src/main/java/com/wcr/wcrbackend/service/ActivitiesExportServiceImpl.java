package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.StripChart;
import com.wcr.wcrbackend.repo.ActivitiesExportReportDao;
@Service
public class ActivitiesExportServiceImpl implements ActivitiesExportService{
	
	@Autowired
	ActivitiesExportReportDao dao;
	@Override
	public List<StripChart> getProjectsFilterListInActivitiesExportReport(StripChart obj) throws Exception {
		return dao.getProjectsFilterListInActivitiesExportReport(obj);
	}
	@Override
	public List<StripChart> getWorksFilterListInActivitiesExportReport(StripChart obj) throws Exception {
		return dao.getWorksFilterListInActivitiesExportReport(obj);
	}
	@Override
	public List<StripChart> getContractListInActivitiesExportReport(StripChart obj) throws Exception {
		return dao.getContractListInActivitiesExportReport(obj);
	}
	@Override
	public StripChart generateActivitiesExportReport(StripChart obj) throws Exception {
		return dao.generateActivitiesExportReport(obj);
	}
	@Override
	public List<StripChart> generateTPCStatusReport(StripChart obj) throws Exception {
		return dao.generateTPCStatusReport(obj);
	}
	@Override
	public List<StripChart> getDivisionList(StripChart obj) throws Exception {
		return dao.getDivisionList(obj);
	}
	@Override
	public List<StripChart> generateMCDOProgressReport(StripChart obj) throws Exception {
		return dao.generateMCDOProgressReport(obj);
	}
	@Override
	public List<StripChart> generateMCDOProgressReport1(StripChart obj) throws Exception {
		return dao.generateMCDOProgressReport1(obj);
	}
	@Override
	public List<StripChart> generateTPCStructureList(StripChart obj) throws Exception {
		return dao.generateTPCStructureList(obj);
	}
	@Override
	public List<StripChart> generateTPCStructureCumList(StripChart obj) throws Exception {
		return dao.generateTPCStructureCumList(obj);
	}
	@Override
	public List<StripChart> getWorksListForSelectedProject(StripChart obj) throws Exception {
		return dao.getWorksListForSelectedProject(obj);
	}
	@Override
	public List<StripChart> generateStationImprovementsReport(StripChart obj) throws Exception {
		return dao.generateStationImprovementsReport(obj);
	}
	@Override
	public List<StripChart> getStationImprovementDivisionList(StripChart obj) throws Exception {
		return dao.getStationImprovementDivisionList(obj);
	}
}
