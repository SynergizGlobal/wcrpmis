package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.InsuranceTypeDao;
import com.wcr.wcrbackend.reference.Iservice.InsuranceTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;


@Service
public class InsuranceTypeServiceImpl implements InsuranceTypeService{
	
	@Autowired
	InsuranceTypeDao dao;

	@Override
	public List<TrainingType> getInsuranceTypesList() throws Exception {
		return dao.getInsuranceTypesList();
	}

	@Override
	public boolean addInsuranceType(TrainingType obj) throws Exception {
		return dao.addInsuranceType(obj);
	}

	@Override
	public TrainingType getInsuranceTypesDetails(TrainingType obj) throws Exception {
		return dao.getInsuranceTypesDetails(obj);
	}

	@Override
	public boolean updateInsuranceTypes(TrainingType obj) throws Exception {
		return dao.updateInsuranceTypes(obj);
	}

	@Override
	public boolean deleteInsuranceTypes(TrainingType obj) throws Exception {
		return dao.deleteInsuranceTypes(obj);
	}

}
