package com.wcr.wcrbackend.reference.IMPLservice;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.UtilityResponsibleExecutivesDao;
import com.wcr.wcrbackend.reference.Iservice.UtilityResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;


@Service
public class UtilityResponsibleExecutivesServiceImpl implements UtilityResponsibleExecutivesService{
	@Autowired
	 UtilityResponsibleExecutivesDao dao;

	@Override
	public List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception {
		return dao.getExecutivesDetails(obj);
	}

	@Override
	public boolean addUtilityShiftingExecutives(TrainingType obj) throws Exception {
		return dao.addUtilityShiftingExecutives(obj);
	}

	@Override
	public boolean updateUtilityShiftingExecutives(TrainingType obj) throws Exception {
		return dao.updateUtilityShiftingExecutives(obj);
	}

	@Override
	public List<TrainingType> getWorkDetails(TrainingType obj) throws Exception {
		return dao.getWorkDetails(obj);
	}
}
