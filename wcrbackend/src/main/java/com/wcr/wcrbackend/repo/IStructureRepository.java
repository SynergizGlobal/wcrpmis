package com.wcr.wcrbackend.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.ProjectStructureSummaryDto;
import com.wcr.wcrbackend.DTO.StructureNameDto;
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
    
    
}


