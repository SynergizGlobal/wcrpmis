package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.AsBuiltStatusDao;
import com.wcr.wcrbackend.reference.Iservice.AsBuiltStatusService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class AsBuiltStatusServiceImpl implements AsBuiltStatusService{
	
	@Autowired
	AsBuiltStatusDao dao;

	@Override
	public TrainingType getAsBuiltStatusDetails(TrainingType obj) throws Exception {
		return dao.getAsBuiltStatusDetails(obj);
	}

	@Override
	public boolean addAsBuiltStatus(TrainingType obj) throws Exception {
		return dao.addAsBuiltStatus(obj);
	}

	@Override
	public boolean updateAsBuiltStatus(TrainingType obj) throws Exception {
		return dao.updateAsBuiltStatus(obj);
	}

	@Override
	public boolean deleteAsBuiltStatus(TrainingType obj) throws Exception {
		return dao.deleteAsBuiltStatus(obj);
	}
}
