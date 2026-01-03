package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.RrResponsibleExecutivesDao;
import com.wcr.wcrbackend.reference.Iservice.RrResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class RrResponsibleExecutivesServiceImpl implements RrResponsibleExecutivesService{
	@Autowired
	RrResponsibleExecutivesDao dao;
	@Override
	public List<TrainingType> getUsersDetails(TrainingType obj) throws Exception {
		return dao.getUsersDetails(obj);
	}
	
}