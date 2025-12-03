package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.DTO.Training;
import com.wcr.wcrbackend.DTO.Year;
import com.wcr.wcrbackend.repo.IProjectRepository;

@Service
public class ProjectService implements IProjectService {

	@Autowired
	private IProjectRepository projectRepository;
	@Override
	public List<Project> getProjectList(Project project)throws Exception{
	return projectRepository.getProjectList(project);}
	
	
	@Override
	public Project getProject(String projectId, Project project)throws Exception{
		return projectRepository.getProject(projectId,project);}

	@Override
	public boolean updateProject(Project project)throws Exception{
		return projectRepository.updateProject(project);
	}

	@Override
	public boolean addProject(Project project)throws Exception{
		return projectRepository.addProject(project);
	}
	@Override
	public boolean deleteProject(String projectId, Project project)throws Exception{
		return projectRepository.deleteProject(projectId,project);
	}


	@Override
	public List<Project> getFileNames(String projectId) throws Exception {
		return projectRepository.getFileNames(projectId);
	}


	@Override
	public List<Year> getYearList() throws Exception {
		return projectRepository.getYearList();
	}


	@Override
	public List<Project> getProjectPinkBookList() throws Exception {
		return projectRepository.getProjectPinkBookList();
	}


	@Override
	public List<Project> getProjectFileTypes() throws Exception {
		return projectRepository.getProjectFileTypes();
	}


	@Override
	public List<Project> getProjectTypeDetails() throws Exception {
		return projectRepository.getProjectTypeDetails();
	}


	@Override
	public List<Project> getRailwayZones() throws Exception {
		return projectRepository.getRailwayZones();
	}
	
	@Override
	public List<Project> getAllDivisions() throws Exception
	{
		return projectRepository.getAllDivisions();
	}
	
	@Override
	public List<Project> getAllSections() throws Exception
	{
		return projectRepository.getAllSections();
	}


	@Override
	public String[] uploadProjectChainagesData(List<Project> projectChainagesList, Project project) throws Exception {
		return projectRepository.uploadProjectChainagesData(projectChainagesList,project);
	}


	@Override
	public boolean saveProjectChainagesDataUploadFile(Project obj) throws Exception {
		return projectRepository.saveProjectChainagesDataUploadFile(obj);
	}
}
