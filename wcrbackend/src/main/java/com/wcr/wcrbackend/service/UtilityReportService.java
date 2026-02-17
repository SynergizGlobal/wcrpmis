package com.wcr.wcrbackend.service;

import java.util.List;
import java.util.Map;

import com.wcr.wcrbackend.DTO.UtilityShifting;

public interface UtilityReportService {

	List<UtilityShifting> getProjectsFilterListInutilityReport(UtilityShifting obj) throws Exception;

	List<UtilityShifting> getWorksFilterListInutilityReport(UtilityShifting obj) throws Exception;

	List<UtilityShifting> getExecutionAgencyListInutilityReport(UtilityShifting obj) throws Exception;

	UtilityShifting getUtilityShiftingData(UtilityShifting obj) throws Exception;

	Map<String, List<UtilityShifting>> getUtilityShiftingReportData(UtilityShifting obj) throws Exception;


}
