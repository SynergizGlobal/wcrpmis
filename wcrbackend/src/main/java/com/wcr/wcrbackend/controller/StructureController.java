package com.wcr.wcrbackend.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcr.wcrbackend.DTO.FullStructureResponse;
import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.DTO.ProjectStructureSummaryDto;
import com.wcr.wcrbackend.DTO.SaveStructureRequest;
import com.wcr.wcrbackend.DTO.StructureSaveRequest;
import com.wcr.wcrbackend.DTO.StructureSummaryDto;
import com.wcr.wcrbackend.repo.ProjectRepository;
import com.wcr.wcrbackend.repo.StructureRepository;
import com.wcr.wcrbackend.service.ProjectService;
import com.wcr.wcrbackend.service.StructureService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/structures")
@RequiredArgsConstructor
public class StructureController {

    private final StructureService structureService;

    @GetMapping("/summary/{projectId}")
    public List<StructureSummaryDto> getStructureSummary(@PathVariable String projectId) {
        return structureService.getStructureTypeSummary(projectId);
    }
    
    @GetMapping("/full/{projectId}")
    public FullStructureResponse getFullStructure(@PathVariable String projectId) {
        return structureService.getFullStructureData(projectId);
    }
    
    @GetMapping("/types")
    public List<String> getAllTypes() {
        return structureService.getAllStructureTypes();
    }
    
    // Save or Update
    @PostMapping("/saveOrUpdate")
    public ResponseEntity<String> saveOrUpdate(@RequestBody StructureSaveRequest req) {
        structureService.saveOrUpdate(req);
        return ResponseEntity.ok("SUCCESS");
    }

    // DELETE
    @DeleteMapping("/{structureId}")
    public ResponseEntity<String> deleteStructure(@PathVariable String structureId) {
        structureService.delete(structureId);
        return ResponseEntity.ok("DELETED");
    }

    @DeleteMapping("/type")
    public ResponseEntity<String> deleteStructureType(
            @RequestParam String projectId,
            @RequestParam String type) {

        structureService.deleteStructureType(projectId, type);
        return ResponseEntity.ok("Structure Type deleted successfully");
    }
    
    @GetMapping("/allProjectSummaries")
    public List<ProjectStructureSummaryDto> getAllProjectSummaries() {
        return structureService.getAllProjectSummaries();
    }
    
    @GetMapping("/project-chainage/{projectId}")
    public ResponseEntity<Map<String, BigDecimal>> getProjectChainage(@PathVariable String projectId) {
        return ResponseEntity.ok(structureService.getProjectChainage(projectId));
    }


    
}

