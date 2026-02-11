package com.wcr.dms.service.impl;

import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wcr.dms.common.CommonUtil;
import com.wcr.dms.dto.ProjectDTO;
import com.wcr.dms.service.ProjectService;
import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.ProjectRepository;
import com.wcr.wcrbackend.repo.UserDao;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final UserDao userRepository;
	
	@Override
	public List<ProjectDTO> getAllProjects() {
		// TODO Auto-generated method stub
		List<ProjectDTO> projectDTOs = new ArrayList<>();
		for (Project project : projectRepository.findAll()) {
			projectDTOs.add(ProjectDTO.builder()
					.id(project.getProject_name())
					.name(project.getProject_name())	
					.build());
		}
		return projectDTOs;
	}

	@Override
	public List<ProjectDTO> getProjectsByUserId(String userId) {
		List<ProjectDTO> projectDTOs = new ArrayList<>();
		for (String projectName : projectRepository.findByUserId(userId)) {
			projectDTOs.add(ProjectDTO.builder()
					.id(projectName)
					.name(projectName)		
					.build());
		}
		return projectDTOs;
	}

	@Override
	public List<ProjectDTO> getProjects(String userId, String userRoleNameFk) {
		User user = userRepository.findById(userId).get();
    	if(CommonUtil.isITAdminOrSuperUser(user)) {
    		//IT Admin
    		return this.getAllProjects();
    	} else if(userRoleNameFk.equals("Contractor")) {
    		return this.getProjectsByUserId(userId);
    	} else {
    		return this.getProjectsForOtherUsersByUserId(userId);
    	}
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
