package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.DesignFileTypeDao;
import com.wcr.wcrbackend.reference.Iservice.DesignFileTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class DesignFileTypeServiceImpl implements DesignFileTypeService{


	@Autowired
	DesignFileTypeDao dao;

	@Override
	public TrainingType getDesignFileTypeDetails(TrainingType obj) throws Exception {
		return dao.getDesignFileTypeDetails(obj);
	}

	@Override
	public boolean addDesignFileType(TrainingType obj) throws Exception {
		return dao.addDesignFileType(obj);
	}

	@Override
	public boolean updateDesignFileType(TrainingType obj) throws Exception {
		return dao.updateDesignFileType(obj);
	}

	@Override
	public boolean deleteDesignFileType(TrainingType obj) throws Exception {
		return dao.deleteDesignFileType(obj);
	}
}
