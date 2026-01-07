package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.ContractorSpecializationsDao;
import com.wcr.wcrbackend.reference.Iservice.ContractorSpecializationService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class ContractorSpecializationServiceImpl implements ContractorSpecializationService{


	@Autowired
	ContractorSpecializationsDao dao;

	@Override
	public List<TrainingType> getContractorSpecializationsList() throws Exception {
		return dao.getContractorSpecializationsList();
	}

	@Override
	public boolean addContractorSpecialization(TrainingType obj) throws Exception {
		return dao.addContractorSpecialization(obj);
	}

	@Override
	public TrainingType getContractorSpecializationDetails(TrainingType obj) throws Exception {
		return dao.getContractorSpecializationDetails(obj);
	}

	@Override
	public boolean updateContractorSpecialization(TrainingType obj) throws Exception {
		return dao.updateContractorSpecialization(obj);
	}

	@Override
	public boolean deleteContractorSpecialization(TrainingType obj) throws Exception {
		return dao.deleteContractorSpecialization(obj);
	}
}
