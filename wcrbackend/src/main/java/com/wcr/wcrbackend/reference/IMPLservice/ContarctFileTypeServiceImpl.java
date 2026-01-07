package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.ContarctFileTypeDao;
import com.wcr.wcrbackend.reference.Iservice.ContarctFileTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;


@Service
public class ContarctFileTypeServiceImpl implements ContarctFileTypeService{


	@Autowired
	ContarctFileTypeDao dao;

	@Override
	public List<TrainingType> getcontractFileType(TrainingType obj) throws Exception {
		return dao.getcontractFileType(obj);
	}

	@Override
	public boolean addContractFileType(TrainingType obj) throws Exception {
		return dao.addContractFileType(obj);
	}

	@Override
	public boolean updateContractFileType(TrainingType obj) throws Exception {
		return dao.updateContractFileType(obj);
	}

	@Override
	public boolean deleteContractFileType(TrainingType obj) throws Exception {
		return dao.deleteContractFileType(obj);
	}
}
