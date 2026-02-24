package com.wcr.wcrbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.DTO.Training;
import com.wcr.wcrbackend.DTO.Year;
import com.wcr.wcrbackend.dms.common.CommonUtil;
import com.wcr.wcrbackend.dms.dto.ProjectDTO;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.IProjectRepository;
import com.wcr.wcrbackend.repo.UserDao;

@Service
public class ProjectService implements IProjectService {

	@Autowired
	private IProjectRepository projectRepository;
	
	@Autowired
	private UserDao userRepository;
	
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
	public List<Project> getAllDivisionsForRailWayZone(String railwayZone) throws Exception {
		return projectRepository.getAllDivisionsForRailWayZone(railwayZone);
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


	@Override
	public List<ProjectDTO> getProjects(String userId, String userRoleNameFk) {
		
		User user = userRepository.findById(userId).get();
    	if(CommonUtil.isITAdminOrSuperUser(user)) {
    		
    		return this.getAllProjects();
    	} else if(userRoleNameFk.equals("Contractor")) {
    		return this.getProjectsByUserId(userId);
    	} else {
    		return this.getProjectsForOtherUsersByUserId(userId);
    	}
	}

	
          private List<ProjectDTO> getAllProjects() {
		
		List<ProjectDTO> projectDTOs = new ArrayList<>();
		for (Project project : projectRepository.findAll()) {
			projectDTOs.add(ProjectDTO.builder()
					.id(project.getProject_id())
					.name(project.getProject_name())		
					.build());
		}
		return projectDTOs;
	}
	
	private List<ProjectDTO> getProjectsByUserId(String userId) {
		List<ProjectDTO> projectDTOs = new ArrayList<>();
		for (String projectName : projectRepository.findByUserId(userId)) {
			projectDTOs.add(ProjectDTO.builder()
					.id(projectName)
					.name(projectName)		
					.build());
		}
		return projectDTOs;
	}
	private List<ProjectDTO> getProjectsForOtherUsersByUserId(String userId) {
		List<ProjectDTO> projectDTOs = new ArrayList<>();
		for (String projectName : projectRepository.getProjectsForOtherUsersByUserId(userId)) {
			projectDTOs.add(ProjectDTO.builder()
					.id(projectName)
					.name(projectName)		
					.build());
		}
		return projectDTOs;
	}
}
