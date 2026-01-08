package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wcr.wcrbackend.reference.Idao.ProjectTypeDao;
import com.wcr.wcrbackend.reference.Iservice.ProjectTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class ProjectTypeServiceImpl implements ProjectTypeService{
	
	@Autowired
	 ProjectTypeDao dao;

	@Override
	public List<TrainingType> getProjectType(TrainingType obj) throws Exception {
		return dao.getProjectType(obj);
	}

	@Override
	public boolean addProjectType(TrainingType obj) throws Exception {
		return dao.addProjectType(obj);
	}

	@Override
	public boolean updateProjectType(TrainingType obj) throws Exception {
		return dao.updateProjectType(obj);
	}

	@Override
	public boolean deleteProjectType(TrainingType obj) throws Exception {
		return dao.deleteProjectType(obj);
	}
}
