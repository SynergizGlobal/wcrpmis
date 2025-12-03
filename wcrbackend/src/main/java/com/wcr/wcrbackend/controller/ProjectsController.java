package com.wcr.wcrbackend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.DTO.Year;
import com.wcr.wcrbackend.service.IProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectsController {
	@Autowired
	private IProjectService projectService;
    
    @GetMapping("/api/projectTypes")
    public ResponseEntity<List<Project>> getProjectTypes() throws Exception {
        return ResponseEntity.ok(projectService.getProjectTypeDetails());
    }

    @GetMapping("/api/railwayZones")
    public ResponseEntity<List<Project>> getRailwayZones() throws Exception {
        return ResponseEntity.ok(projectService.getRailwayZones());
    }

    @GetMapping("/api/yearList")
    public ResponseEntity<List<Year>> getYearList() throws Exception {
        return ResponseEntity.ok(projectService.getYearList());
    }

    @GetMapping("/api/divisions")
    public ResponseEntity<List<Project>> getDivisions() throws Exception {
        return ResponseEntity.ok(projectService.getAllDivisions());
    }

    @GetMapping("/api/sections")
    public ResponseEntity<List<Project>> getSections() throws Exception {
        return ResponseEntity.ok(projectService.getAllSections());
    }

    @GetMapping("/api/fileTypes")
    public ResponseEntity<List<Project>> getProjectFileTypes() throws Exception {
        return ResponseEntity.ok(projectService.getProjectFileTypes());
    }  

    @GetMapping("/api/getProjects")
    public ResponseEntity<?> getProjects(Project project) {
        try {
            List<Project> projectList = projectService.getProjectList(project);
            return ResponseEntity.ok(projectList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error fetching projects: " + e.getMessage());
        }
    }
    
    @GetMapping("/api/getProjectList")
    public List<Project> getProjectList() throws Exception{
        try {
            return projectService.getProjectList(new Project()); // or pass filter
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    } 
    
    @GetMapping("/api/getProject/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable("projectId") String projectId) {
        try {
            Project project = projectService.getProject(projectId, new Project());
            if (project == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
            }
            return ResponseEntity.ok(project);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error fetching project: " + e.getMessage());
        }
    }

    @PostMapping(value = "/api/addProject", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> addProjectAjax(@RequestBody Project project, HttpSession session) {
        try {
            String user_Id = (String) session.getAttribute("USER_ID");
            String userName = (String) session.getAttribute("USER_NAME");
            String userDesignation = (String) session.getAttribute("USER_DESIGNATION");

            project.setCreated_by_user_id_fk(user_Id);
            project.setUser_name(userName);
            project.setDesignation(userDesignation);
            project.setCreated_by(userName);

            boolean flag = projectService.addProject(project);

            if (flag) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Project added successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("success", false, "message", "Adding project failed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error: " + e.getMessage()));
        }
    }

    @PutMapping("/api/updateProject/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable("projectId") String projectId, @RequestBody Project project) {
        try {
            project.setProject_id(projectId);
            boolean flag = projectService.updateProject(project);
            if (flag) {
                return ResponseEntity.ok("Project updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update project");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error updating project: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/deleteProject/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable("projectId") String projectId) {
        try {
            boolean flag = projectService.deleteProject(projectId, new Project());
            if (flag) {
                return ResponseEntity.ok("Project deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete project");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting project: " + e.getMessage());
        }
    }
}
