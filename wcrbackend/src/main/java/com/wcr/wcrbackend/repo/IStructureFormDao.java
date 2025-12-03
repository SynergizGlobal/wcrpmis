package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Structure;

public interface IStructureFormDao {
	
	
	List<Structure> getStructuresList(Structure obj, int startIndex, int offset, String searchParameter) throws Exception;
	
	
	int getTotalRecords(Structure obj, String searchParameter) throws Exception;
	
	
	List<Structure> getWorkStatusListForFilter(Structure obj) throws Exception;
	
	List<Structure> getContractsListForFilter(Structure obj) throws Exception;
	
	List<Structure> getStructureTypeListForFilter(Structure obj) throws Exception;

	List<Structure> getStructureDetailsLocations(Structure obj) throws Exception;

	List<Structure> getStructureDetailsTypes(Structure obj) throws Exception;

	Structure getStructuresFormDetails(Structure obj) throws Exception;

}