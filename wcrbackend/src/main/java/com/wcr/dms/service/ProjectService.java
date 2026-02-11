package com.wcr.dms.service;

import java.util.List;

import com.wcr.dms.dto.ProjectDTO;

public interface ProjectService {
	
	public List<ProjectDTO> getAllProjects();

	public List<ProjectDTO> getProjectsByUserId(String userId);

	public List<ProjectDTO> getProjects(String userId, String userRoleNameFk);


}
