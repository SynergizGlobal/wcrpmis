package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.ContractTypeDao;
import com.wcr.wcrbackend.reference.Iservice.ContractTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;


@Service
public class ContractTypeServiceImpl implements ContractTypeService {

	@Autowired
	ContractTypeDao dao;

	@Override
	public List<TrainingType> getContractTypesList() throws Exception {
		return dao.getContractTypesList();
	}

	@Override
	public boolean addContractType(TrainingType obj) throws Exception {
		return dao.addContractType(obj);
	}

	@Override
	public TrainingType getContractTypeDetails(TrainingType obj) throws Exception {
		return dao.getContractTypeDetails(obj);
	}

	@Override
	public boolean updateContractType(TrainingType obj) throws Exception {
		return dao.updateContractType(obj);
	}

	@Override
	public boolean deleteContractType(TrainingType obj) throws Exception {
		return dao.deleteContractType(obj);
	}
}
