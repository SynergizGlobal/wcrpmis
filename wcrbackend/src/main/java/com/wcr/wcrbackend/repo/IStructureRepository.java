package com.wcr.wcrbackend.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.ProjectStructureSummaryDto;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.DTO.StructureSummaryDto;

public interface IStructureRepository {

	  List<StructureSummaryDto> getStructureSummaryByProject(String projectId);
	  
	  FullStructureResponse getFullStructureData(String projectId);

	  List<Map<String, Object>> getStructuresByProject(String projectId);
	  
	  List<String> getAllStructureTypes();
	  
	  void insertStructure(String name, String structure, String projectId, String type,
              String details, BigDecimal fromChainage, BigDecimal toChainage);

      void updateStructure(String id, String name, String structure, String type,
              String details, BigDecimal fromChainage, BigDecimal toChainage);

	    void deleteStructure(String structureId);
	    
	    void deleteStructureType(String projectId, String type);
	    
	    List<ProjectStructureSummaryDto> getAllProjectSummaries();
	    
	    Map<String, BigDecimal> getProjectChainage(String projectId);
	    
	    
		List<Structure> getProjectsListForStructureForm(Structure obj) throws Exception;

		List<Structure> getWorkListForStructureForm(Structure obj) throws Exception;

		List<Structure> getContractListForStructureFrom(Structure obj) throws Exception;

		List<Structure> getStructuresListForStructureFrom(Structure obj) throws Exception;

		List<Structure> getDepartmentsListForStructureFrom(Structure obj) throws Exception;
		
		List<Structure> getResponsiblePeopleListForStructureForm(Structure obj) throws Exception;

		List<Structure> getWorkStatusListForStructureForm(Structure obj) throws Exception;

		List<Structure> getUnitsListForStructureForm(Structure obj) throws Exception;

		List<Structure> getFileTypeForStructureForm(Structure obj) throws Exception;

    
    
}


