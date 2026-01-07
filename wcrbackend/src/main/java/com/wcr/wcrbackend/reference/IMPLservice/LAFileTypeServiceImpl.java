package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.LAFileTypeDao;
import com.wcr.wcrbackend.reference.Iservice.LAFileTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class LAFileTypeServiceImpl implements LAFileTypeService{

	@Autowired
	LAFileTypeDao dao;

	@Override
	public TrainingType getLAFileTypeDetails(TrainingType obj) throws Exception {
		return dao.getLAFileTypeDetails(obj);
	}

	@Override
	public boolean addLAFileType(TrainingType obj) throws Exception {
		return dao.addLAFileType(obj);
	}

	@Override
	public boolean updateLAFileType(TrainingType obj) throws Exception {
		return dao.updateLAFileType(obj);
	}

	@Override
	public boolean deleteLAFileType(TrainingType obj) throws Exception {
		return dao.deleteLAFileType(obj);
	}

}
