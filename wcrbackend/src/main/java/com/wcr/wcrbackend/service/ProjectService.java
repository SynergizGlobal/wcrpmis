package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Project;
import com.wcr.wcrbackend.repo.IProjectRepository;

@Service
public class ProjectService implements IProjectService {

	@Autowired
	private IProjectRepository projectRepository;
	@Override
	public List<Project> getProjectTypes() throws Exception {
		// TODO Auto-generated method stub
		return projectRepository.getProjectTypes();
	}
	@Override
	public List<Project> getProjects() throws Exception {
		// TODO Auto-generated method stub
		return projectRepository.getProjects();
	}

}
