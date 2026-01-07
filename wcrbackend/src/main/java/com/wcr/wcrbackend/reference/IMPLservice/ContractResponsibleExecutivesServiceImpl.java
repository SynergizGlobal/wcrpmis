package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.ContractResponsibleExecutivesDao;
import com.wcr.wcrbackend.reference.Iservice.ContractResponsibleExecutivesService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class ContractResponsibleExecutivesServiceImpl implements ContractResponsibleExecutivesService{


	@Autowired
	ContractResponsibleExecutivesDao dao;

	@Override
	public List<TrainingType> getExecutivesDetails(TrainingType obj) throws Exception {
		return dao.getExecutivesDetails(obj);
	}

	@Override
	public boolean addContractExecutives(TrainingType obj) throws Exception {
		return dao.addContractExecutives(obj);
	}

	@Override
	public boolean updateContractExecutives(TrainingType obj) throws Exception {
		return dao.updateContractExecutives(obj);
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
