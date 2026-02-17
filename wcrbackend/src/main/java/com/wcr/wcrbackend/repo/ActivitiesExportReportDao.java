package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.StripChart;

public interface ActivitiesExportReportDao {

	List<StripChart> getProjectsFilterListInActivitiesExportReport(StripChart obj) throws Exception;

	List<StripChart> getWorksFilterListInActivitiesExportReport(StripChart obj) throws Exception;

	List<StripChart> getContractListInActivitiesExportReport(StripChart obj) throws Exception;

	StripChart generateActivitiesExportReport(StripChart obj) throws Exception;

	List<StripChart> generateTPCStatusReport(StripChart obj) throws Exception;

	List<StripChart> getDivisionList(StripChart obj) throws Exception;

	List<StripChart> generateMCDOProgressReport(StripChart obj)  throws Exception;

	List<StripChart> generateMCDOProgressReport1(StripChart obj) throws Exception;

	List<StripChart> generateTPCStructureList(StripChart obj) throws Exception;

	List<StripChart> generateTPCStructureCumList(StripChart obj) throws Exception;

	List<StripChart> getWorksListForSelectedProject(StripChart obj) throws Exception;
	List<StripChart> generateStationImprovementsReport(StripChart obj) throws Exception;

	List<StripChart> getStationImprovementDivisionList(StripChart obj) throws Exception;
}
