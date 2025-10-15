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

	List<LandAcquisition> getCoordinates(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getSubCategoryList(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getLandsList(LandAcquisition obj) throws Exception;

	boolean checkSurveyNumber(String survey_number, String village_id, String la_id) throws Exception;

	List<LandAcquisition> getLADetails(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getStatusList() throws Exception;

	List<LandAcquisition> getProjectsList(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getLandsListForLAForm(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getIssueCatogoriesList() throws Exception;

	List<LandAcquisition> getSubCategorysListForLAForm(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getUnitsList() throws Exception;

	List<LandAcquisition> getLaFileType() throws Exception;

	List<LandAcquisition> getLaLandStatus() throws Exception;

	LandAcquisition getLandAcquisitionForm(LandAcquisition obj) throws Exception;

	List<LandAcquisition> getRailwayList(String la_id) throws Exception;

	List<LandAcquisition> getForestList(String la_id) throws Exception;

	List<LandAcquisition> getGovList(String la_id) throws Exception;

	List<LandAcquisition> getPrivateLandList(String la_id) throws Exception;

	List<LandAcquisition> getPrivateValList(String la_id) throws Exception;

	List<LandAcquisition> geprivateIRAList(String la_id) throws Exception;

	List<LandAcquisition> getLandAcquisitionList(LandAcquisition dObj) throws Exception;
	
	public boolean addLandAcquisition(LandAcquisition obj) throws Exception;

}
