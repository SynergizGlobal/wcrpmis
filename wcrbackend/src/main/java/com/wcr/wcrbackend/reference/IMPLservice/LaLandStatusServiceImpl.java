package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.LaLandStatusDao;
import com.wcr.wcrbackend.reference.Iservice.LaLandStatusService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class LaLandStatusServiceImpl implements LaLandStatusService{

	@Autowired
	LaLandStatusDao dao;

	@Override
	public TrainingType getLandAcquisitionStatusDetails(TrainingType obj) throws Exception {
		return dao.getLandAcquisitionStatusDetails(obj);
	}

	@Override
	public boolean updatelandAcquisitionStatus(TrainingType obj) throws Exception {
		return dao.updatelandAcquisitionStatus(obj);
	}

	@Override
	public boolean deletelandAcquisitionStatus(TrainingType obj) throws Exception {
		return dao.deletelandAcquisitionStatus(obj);
	}

	@Override
	public boolean addLaStatus(Safety obj) throws Exception {
		return dao.addLaStatus(obj);
	}
}
