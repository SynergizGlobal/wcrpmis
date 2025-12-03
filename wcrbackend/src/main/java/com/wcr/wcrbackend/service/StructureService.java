package com.wcr.wcrbackend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;


import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.ProjectStructureSummaryDto;
import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.DTO.StructureNameDto;
import com.wcr.wcrbackend.DTO.StructureSaveRequest;
import com.wcr.wcrbackend.DTO.StructureSummaryDto;
import com.wcr.wcrbackend.DTO.StructureTypeDto;
import com.wcr.wcrbackend.repo.IStructureRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StructureService {

    private final IStructureRepository structureRepository;

    public List<StructureSummaryDto> getStructureTypeSummary(String projectId) {
        return structureRepository.getStructureSummaryByProject(projectId);
    }

    public FullStructureResponse getFullStructureData(String projectId) {
        return structureRepository.getFullStructureData(projectId);
    }
    
    public List<Map<String, Object>> getStructures(String projectId) {
        return structureRepository.getStructuresByProject(projectId);
    }
    
    public List<String> getAllStructureTypes() {
        return structureRepository.getAllStructureTypes();
    }
   
 // SAVE or UPDATE Logic
    public void saveOrUpdate(StructureSaveRequest req) {

        String projectId = req.getProject();

        // Get project chainage limits
        Map<String, BigDecimal> range = structureRepository.getProjectChainage(projectId);

        BigDecimal min = range.get("fromChainage");
        BigDecimal max = range.get("toChainage");

        if (min == null || max == null) {
            throw new IllegalArgumentException("Project chainage boundaries not found.");
        }

        for (StructureTypeDto typeDto : req.getStructureTypes()) {

            for (StructureNameDto row : typeDto.getRows()) {

                BigDecimal from = row.getFromChainage();
                BigDecimal to   = row.getToChainage();

                /* ---------------------------
                   CHAINAGE VALIDATION
                ---------------------------- */
                if (from != null && from.compareTo(min) < 0) {
                    throw new IllegalArgumentException(
                        "From Chainage " + from + " cannot be less than project min " + min
                    );
                }

                if (to != null && to.compareTo(max) > 0) {
                    throw new IllegalArgumentException(
                        "To Chainage " + to + " cannot exceed project max " + max
                    );
                }

                /* ---------------------------
                   INSERT OR UPDATE
                ---------------------------- */
                if (row.getStructureId() == null || row.getStructureId().isBlank()) {

                    // INSERT
                    structureRepository.insertStructure(
                            row.getStructure(),
                            row.getStructureName(),
                            projectId,
                            typeDto.getType(),
                            row.getStructureDetails(),
                            from,
                            to
                        );

                } else {

                    // UPDATE
                    structureRepository.updateStructure(
                            row.getStructureId(),
                            row.getStructure(),
                            row.getStructureName(),
                            typeDto.getType(),
                            row.getStructureDetails(),
                            from,
                            to
                        );
                }
            }
        }
    }

    // DELETE
    public void delete(String structureId) {
        structureRepository.deleteStructure(structureId);
    }

    public void deleteStructureType(String projectId, String type) {
        structureRepository.deleteStructureType(projectId, type);
    }

    public List<ProjectStructureSummaryDto> getAllProjectSummaries() {
        return structureRepository.getAllProjectSummaries();
    }

    
    public Map<String, BigDecimal> getProjectChainage(String projectId) {
        return structureRepository.getProjectChainage(projectId);
    }

    
    public List<Structure> getProjectsListForStructureForm(Structure obj) throws Exception {
		return structureRepository.getProjectsListForStructureForm(obj);
	}
    
	
	public List<Structure> getWorkListForStructureForm(Structure obj) throws Exception {
		return structureRepository.getWorkListForStructureForm(obj);
	}

	
	public List<Structure> getContractListForStructureFrom(Structure obj) throws Exception {
		return structureRepository.getContractListForStructureFrom(obj);
	}

	public List<Structure> getResponsiblePeopleListForStructureForm(Structure obj) throws Exception {
		return structureRepository.getResponsiblePeopleListForStructureForm(obj);
	}
	
	public List<Structure> getStructuresListForStructureFrom(Structure obj) throws Exception {
		return structureRepository.getStructuresListForStructureFrom(obj);
	}

	
	public List<Structure> getDepartmentsListForStructureFrom(Structure obj) throws Exception {
		return structureRepository.getDepartmentsListForStructureFrom(obj);
	}

	public List<Structure> getWorkStatusListForStructureForm(Structure obj) throws Exception {
		return structureRepository.getWorkStatusListForStructureForm(obj);
	}

	
	public List<Structure> getUnitsListForStructureForm(Structure obj) throws Exception {
		return structureRepository.getUnitsListForStructureForm(obj);
	}

	
	public List<Structure> getFileTypeForStructureForm(Structure obj) throws Exception {
		return structureRepository.getFileTypeForStructureForm(obj);
	}
}


