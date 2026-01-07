package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.DashboardTypeDao;
import com.wcr.wcrbackend.reference.Iservice.DashboardTypeService;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class DashboardTypeServiceImpl implements DashboardTypeService{

	@Autowired
	private DashboardTypeDao dao;

	@Override
	public List<TrainingType> getDashboardTypesList() throws Exception {
		return dao.getDashboardTypesList();
	}

	@Override
	public boolean addDashboardType(TrainingType obj) throws Exception {
		return dao.addDashboardType(obj);
	}
	
	@Override
	public boolean updateDashboardType(Map<String, String> payload) throws Exception {
	    return dao.updateDashboardType(payload);
	}

	@Override
	public boolean deleteDashboardType(Map<String, String> payload) throws Exception {
	    return dao.deleteDashboardType(payload);
	}


}
