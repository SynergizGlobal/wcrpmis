package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.DTO.Training;
import com.wcr.wcrbackend.DTO.Year;
import com.wcr.wcrbackend.dms.dto.ProjectDTO;

public interface IProjectService {

	public List<Project> getProjectList(Project project)throws Exception;

	public Project getProject(String projectId, Project project)throws Exception;

	public boolean updateProject(Project project)throws Exception;

	public boolean addProject(Project project)throws Exception;

	public boolean deleteProject(String projectId, Project project)throws Exception;

	public List<Project> getFileNames(String projectId) throws Exception;

	public List<Year> getYearList() throws Exception;

	public List<Project> getProjectPinkBookList() throws Exception;

	public List<Project> getProjectFileTypes() throws Exception;
	
	public List<Project> getAllDivisionsForRailWayZone(String railwayZone) throws Exception ;

	public List<Project> getProjectTypeDetails() throws Exception;

	public List<Project> getRailwayZones() throws Exception;

	public List<Project> getAllDivisions() throws Exception;

	public List<Project> getAllSections() throws Exception;

	public String[] uploadProjectChainagesData(List<Project> projectChainagesList, Project project) throws Exception;
	public boolean saveProjectChainagesDataUploadFile(Project obj) throws Exception;
	
	public List<ProjectDTO> getProjects(String userId, String userRoleNameFk);

}
