package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Project;

public interface IProjectService {

	List<Project> getProjectTypes() throws Exception;

	List<Project> getProjects() throws Exception;

}
