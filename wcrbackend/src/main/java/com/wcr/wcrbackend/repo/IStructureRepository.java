package com.wcr.wcrbackend.repo;

import java.util.List;
import java.util.Map;

import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.StructureNameDto;
import com.wcr.wcrbackend.DTO.StructureSummaryDto;

public interface IStructureRepository {

	  List<StructureSummaryDto> getStructureSummaryByProject(String projectId);
	  
	  FullStructureResponse getFullStructureData(String projectId);

	  List<Map<String, Object>> getStructuresByProject(String projectId);
	  
	  List<String> getAllStructureTypes();
	  
	  void insertStructure(String structureName, String projectId, String type);
	  
	  void updateStructure(String id, String name, String type);

	    void deleteStructure(String structureId);
	    
	    void deleteStructureType(String projectId, String type);
	    
	    List<String> getProjectsWithStructures();
	    
    
    
}


