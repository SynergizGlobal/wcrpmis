package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.DesignResponsibleExecutivesDao;
import com.wcr.wcrbackend.reference.Iservice.DesignResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class DesignResponsibleExecutivesServiceImpl implements DesignResponsibleExecutivesService{


	@Autowired
	DesignResponsibleExecutivesDao dao;

	@Override
	public List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception {
		return dao.getExecutivesDetails(obj);
	}

	@Override
	public boolean addDesignExecutives(TrainingType obj) throws Exception {
		return dao.addDesignExecutives(obj);
	}

	@Override
	public boolean updateDesignExecutives(TrainingType obj) throws Exception {
		return dao.updateDesignExecutives(obj);
	}

	@Override
	public List<TrainingType> getWorkDetails(TrainingType obj) throws Exception {
		return dao.getWorkDetails(obj);
	}

	@Override
	public List<TrainingType> getUsersDetails(TrainingType obj) throws Exception {
		return dao.getUsersDetails(obj);
	}
	
}
