package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.service.IProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectsController {
	@Autowired
	private IProjectService projectService;
    @GetMapping("/api/getProjectTypes")
    public List<Project> getProjectTypes() throws Exception{
        return projectService.getProjectTypes();
    }
    
    @GetMapping("/api/getProjects")
    public List<Project> getProjects() throws Exception{
        return projectService.getProjects();
    }
}
