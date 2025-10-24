package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Design;

public interface IDesignService {

	List<Design> getP6ActivitiesData(Design obj) throws Exception;

	List<Design> getDesignUpdateStructures(Design obj) throws Exception;

	List<Design> getHodListFilter(Design obj) throws Exception;

	List<Design> getDepartmentListFilter(Design obj) throws Exception;

	List<Design> getContractListFilter(Design obj) throws Exception;

	List<Design> getStructureListFilter(Design obj) throws Exception;

	List<Design> getStructureIdsListFilter(Design obj) throws Exception;

	List<Design> getDrawingTypeListFilter(Design obj) throws Exception;

	List<Design> getDesigns(Design obj) throws Exception;

	List<Design> getDesignsList(Design obj, int startIndex, int offset, String searchParameter) throws Exception;

	int getTotalRecords(Design obj, String searchParameter) throws Exception;

	List<Design> getDrawingRepositoryDesignsList(Design obj, int startIndex, int offset, String searchParameter) throws Exception;

	int getTotalDrawingRepositoryRecords(Design obj, String searchParameter) throws Exception;

	List<Design> getProjectsListForDesignForm(Design obj) throws Exception;

	List<Design> getContractList() throws Exception;

	List<Design> getPreparedByList() throws Exception;

	List<Design> componentList() throws Exception;

	List<Design> getApprovingRailwayList() throws Exception;

	List<Design> structureList() throws Exception;

	List<Design> drawingTypeList() throws Exception;

	List<Design> getRevisionStatuses() throws Exception;

	List<Design> getApprovalAuthority() throws Exception;

	List<Design> getStage() throws Exception;

	List<Design> getSubmitted() throws Exception;

	List<Design> getStructureId() throws Exception;

	List<Design> getSubmssionpurpose() throws Exception;

	List<Design> getDesignFileType() throws Exception;

	List<Design> getAsBuiltStatuses() throws Exception;

	Design getDesignDetails(Design obj) throws Exception;

	List<Design> getStructureTypeListFilter(Design obj) throws Exception;

	List<Design> getContractsListForDesignForm(Design obj) throws Exception;

	List<Design> getComponentsforDesign(Design obj) throws Exception;

	List<Design> getStructureIdsforDesign(Design obj) throws Exception;

	List<Design> getStructureTypesforDesign(Design obj) throws Exception;

	List<Design> getDesignUploadsList(Design obj) throws Exception;

	List<Design> getHodList(Design obj) throws Exception;

	List<Design> getDyHodList(Design obj) throws Exception;

}
