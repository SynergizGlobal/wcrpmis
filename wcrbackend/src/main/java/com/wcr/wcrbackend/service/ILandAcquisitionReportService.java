package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.LandAcquisition;

public interface ILandAcquisitionReportService {

	List<LandAcquisition> getProjectsFilterListInLandReport(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getTypeOfLandListInLandReport(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getSubCategoryOfLandFilterListInLandReport(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getWorksFilterListInLandReport(LandAcquisition obj) throws Exception;

	LandAcquisition getLandAcquisitionData(LandAcquisition obj) throws Exception;
}
