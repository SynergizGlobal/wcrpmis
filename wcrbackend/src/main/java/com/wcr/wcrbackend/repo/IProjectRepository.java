package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Project;

public interface IProjectRepository {

	List<Project> getProjectTypes() throws Exception;

	List<Project> getProjects() throws Exception;

}
