package com.wcr.wcrbackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.ProjectStructureSummaryDto;
import com.wcr.wcrbackend.DTO.SaveStructureRequest;
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

        for (StructureTypeDto typeDto : req.getStructureTypes()) {

            for (StructureNameDto row : typeDto.getRows()) {

                if (row.getStructureId() == null || row.getStructureId().trim().isEmpty()) {
                    // INSERT
                    structureRepository.insertStructure(
                        row.getStructureName(),
                        projectId,
                        typeDto.getType()
                    );
                } else {
                    // UPDATE EXISTING ROW
                    structureRepository.updateStructure(
                        row.getStructureId(),
                        row.getStructureName(),
                        typeDto.getType()
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


}


