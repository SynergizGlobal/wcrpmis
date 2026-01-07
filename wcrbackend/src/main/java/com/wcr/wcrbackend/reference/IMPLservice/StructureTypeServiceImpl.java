package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.StructureTypeDao;
import com.wcr.wcrbackend.reference.Iservice.StructureTypeService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class StructureTypeServiceImpl implements StructureTypeService {

	@Autowired
	private StructureTypeDao dao;

	@Override
	public List<Safety> getStructureTypesList() throws Exception {
		return dao.getStructureTypesList();
	}

	@Override
	public boolean addStructureType(Safety obj) throws Exception {
		return dao.addStructureType(obj);
	}

	@Override
	public TrainingType getStructureTypeDetails(TrainingType obj) throws Exception {
		return dao.getStructureTypeDetails(obj);
	}

	@Override
	public boolean updateStructureType(TrainingType obj) throws Exception {
		return dao.updateStructureType(obj);
	}

	@Override
	public boolean deleteStructureType(TrainingType obj) throws Exception {
		return dao.deleteStructureType(obj);
	}
}
