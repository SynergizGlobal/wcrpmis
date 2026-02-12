package com.wcr.wcrbackend.dms.service;

import java.util.List;

import com.wcr.wcrbackend.dms.dto.ProjectDTO;

public interface ProjectService {
	
	public List<ProjectDTO> getAllProjects();

	public List<ProjectDTO> getProjectsByUserId(String userId);

	public List<ProjectDTO> getProjects(String userId, String userRoleNameFk);


}
