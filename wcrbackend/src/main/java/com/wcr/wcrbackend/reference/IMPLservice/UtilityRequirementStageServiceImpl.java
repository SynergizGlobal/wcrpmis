package com.wcr.wcrbackend.reference.IMPLservice;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.UtilityRequirementStageDao;
import com.wcr.wcrbackend.reference.Iservice.UtilityRequirementStageService;
import com.wcr.wcrbackend.reference.model.Safety;


@Service
public class UtilityRequirementStageServiceImpl implements UtilityRequirementStageService{

	@Autowired
	UtilityRequirementStageDao dao;

	@Override
	public Safety getUtilityRequirementStagesList(Safety obj) throws Exception {
		return dao.getUtilityRequirementStagesList(obj);
	}
	@Override
	public List<Safety> getUtilityRequirementStagesList() throws Exception {
		return dao.getUtilityRequirementStagesList();
	}	

	@Override
	public boolean addUtilityRequirementStage(Safety obj) throws Exception {
		return dao.addUtilityRequirementStage(obj);
	}
	@Override
	public boolean updateUtilityRequirementStage(Safety obj) throws Exception {
		return dao.updateUtilityRequirementStage(obj);
	}
	@Override
	public boolean deleteUtilityRequirementStage(Safety obj) throws Exception {
		return dao.deleteUtilityRequirementStage(obj);
	}
}
