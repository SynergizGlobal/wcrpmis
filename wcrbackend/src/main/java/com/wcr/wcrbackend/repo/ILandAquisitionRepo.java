package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.LandAcquisition;

public interface ILandAquisitionRepo {

	int getTotalRecords(LandAcquisition obj, String searchParameter) throws Exception;

	List<LandAcquisition> getLandAcquisitionList(LandAcquisition obj, int startIndex, int offset,
			String searchParameter) throws Exception;

	List<LandAcquisition> getLandAcquisitionStatusList(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getLandAcquisitionVillagesList(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getLandAcquisitionTypesOfLandsList(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getLandAcquisitionSubCategoryList(LandAcquisition obj) throws Exception;

}
