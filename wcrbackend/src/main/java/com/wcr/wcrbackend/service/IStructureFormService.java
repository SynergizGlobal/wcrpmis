package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Structure;


public interface IStructureFormService {

	
	List<Structure> getStructuresList(Structure obj, int startIndex, int offset, String searchParameter) throws Exception;
	
	int getTotalRecords(Structure obj, String searchParameter) throws Exception;
	
	List<Structure> getWorkStatusListForFilter(Structure obj) throws Exception;
	
	List<Structure> getContractsListForFilter(Structure obj) throws Exception;
	
	List<Structure> getStructureTypeListForFilter(Structure obj) throws Exception;
	
//	List<Structure> getProjectsListForStructureForm(Structure obj) throws Exception;
//	
//	List<Structure> getWorkListForStructureForm(Structure obj) throws Exception;
//	
//	List<Structure> getContractListForStructureFrom(Structure obj) throws Exception;
//	
//	List<Structure> getStructuresListForStructureFrom(Structure obj) throws Exception;
//
//	List<Structure> getDepartmentsListForStructureFrom(Structure obj) throws Exception;
//	
	List<Structure> getStructureDetailsLocations(Structure obj) throws Exception;

	List<Structure> getStructureDetailsTypes(Structure obj) throws Exception;

	Structure getStructuresFormDetails(Structure obj) throws Exception;
	
	boolean updateStructureForm(Structure obj) throws Exception;
	
	
	
	
	
//	
//	List<Structure> getWorkStatusList(Structure obj) throws Exception;
//
//	List<Structure> getWorksListForFilter(Structure obj) throws Exception;
//
//	List<Structure> getContractsListForFilter(Structure obj) throws Exception;
//	List<Structure> getStructureTypeListForFilter(Structure obj) throws Exception;
//	List<Structure> getWorkStatusListForFilter(Structure obj) throws Exception;
//
//
//
//
//	List<Structure> getStructureDetailsLocations(Structure obj) throws Exception;
//
//	List<Structure> getStructureDetailsTypes(Structure obj) throws Exception;
//
//	Structure getStructuresFormDetails(Structure obj) throws Exception;
//
//	boolean addStructureForm(Structure obj) throws Exception;
//
//	boolean updateStructureForm(Structure obj) throws Exception;

}
