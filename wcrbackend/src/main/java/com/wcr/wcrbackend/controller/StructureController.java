package com.wcr.wcrbackend.controller;

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
    
    @Autowired
    private ProjectService projectService;

    @GetMapping("/summary/{projectId}")
    public List<StructureSummaryDto> getStructureSummary(@PathVariable String projectId) {
        return structureService.getStructureTypeSummary(projectId);
    }
    
    @GetMapping("/full/{projectId}")
    public FullStructureResponse getFullStructure(@PathVariable String projectId) {
        return structureService.getFullStructureData(projectId);
    }

    @GetMapping("/project/{projectId}")
    public List<Map<String, Object>> getStructures(@PathVariable String projectId) {
        return structureService.getStructures(projectId);
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

    
    @GetMapping("/summary/all")
    public ResponseEntity<?> getAllProjectSummaries() throws Exception {

        List<Project> projects = projectService.getProjects();  // ✔ using your existing service

        List<Map<String, Object>> result = new ArrayList<>();

        for (Project p : projects) {

            String projectId = p.getProject_id();      // ✔ correct getter
            String projectName = p.getProject_name();  // ✔ correct getter

            List<StructureSummaryDto> summary =
                    structureService.getStructureTypeSummary(projectId);

            Map<String, Object> row = new HashMap<>();
            row.put("projectId", projectId);
            row.put("projectName", projectName);
            row.put("structureTypes", summary);

            result.add(row);
        }

        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/projectsWithStructures")
    public List<String> getProjectsWithStructures() {
        return structureService.getProjectsWithStructures();
    }
    
}

