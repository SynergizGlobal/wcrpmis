package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.LaStatusDao;
import com.wcr.wcrbackend.reference.Iservice.LaStatusService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class LaStatusServiceImpl implements LaStatusService{

	@Autowired
	LaStatusDao dao;

	@Override
	public List<Safety> getIaStatusList() throws Exception {
		return dao.getIaStatusList();
	}

	@Override
	public boolean addLaStatus(Safety obj) throws Exception {
		return dao.addLaStatus(obj);
	}

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
}
