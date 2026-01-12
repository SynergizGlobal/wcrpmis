package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.YesOrNoStatusDao;
import com.wcr.wcrbackend.reference.Iservice.YesOrNoStatusService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class YesOrNoStatusServiceImpl implements YesOrNoStatusService{

	@Autowired
	private YesOrNoStatusDao dao;

	@Override
	public TrainingType getYesOrNoStatusDetails(TrainingType obj) throws Exception {
		return dao.getYesOrNoStatusDetails(obj);
	}

	@Override
	public boolean addYesOrNoStatus(TrainingType obj) throws Exception {
		return dao.addYesOrNoStatus(obj);
	}

	@Override
	public boolean updateYesOrNoStatus(TrainingType obj) throws Exception {
		return dao.updateYesOrNoStatus(obj);
	}

	@Override
	public boolean deleteYesOrNoStatus(TrainingType obj) throws Exception {
		return dao.deleteYesOrNoStatus(obj);
	}
}

