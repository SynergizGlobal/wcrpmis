package com.wcr.wcrbackend.reference.IMPLservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.UserTypeDao;
import com.wcr.wcrbackend.reference.Iservice.UserTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class UserTypeServiceImpl implements UserTypeService{
	@Autowired
	private UserTypeDao dao;

	@Override
	public TrainingType getUserTypeDetails(TrainingType obj) throws Exception {
		return dao.getUserTypeDetails(obj);
	}

	@Override
	public boolean addUserType(TrainingType obj) throws Exception {
		return dao.addUserType(obj);
	}

	@Override
	public boolean updateUserType(TrainingType obj) throws Exception {
		return dao.updateUserType(obj);
	}

	@Override
	public boolean deleteUserType(TrainingType obj) throws Exception {
		return dao.deleteUserType(obj);
	}
}
