package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Design;

public interface IDesignRepo {

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

}
