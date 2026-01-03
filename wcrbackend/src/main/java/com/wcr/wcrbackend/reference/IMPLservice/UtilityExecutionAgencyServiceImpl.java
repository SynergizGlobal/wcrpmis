package com.wcr.wcrbackend.reference.IMPLservice;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.UtilityExecutionAgencyDao;
import com.wcr.wcrbackend.reference.Iservice.UtilityExecutionAgencyService;
import com.wcr.wcrbackend.reference.model.Safety;


@Service
public class UtilityExecutionAgencyServiceImpl implements UtilityExecutionAgencyService{

	@Autowired
	UtilityExecutionAgencyDao dao;

	@Override
	public Safety getUtilityExecutionAgencysList(Safety obj) throws Exception {
		return dao.getUtilityExecutionAgencysList(obj);
	}
	@Override
	public List<Safety> getUtilityExecutionAgencysList() throws Exception {
		return dao.getUtilityExecutionAgencysList();
	}	

	@Override
	public boolean addUtilityExecutionAgency(Safety obj) throws Exception {
		return dao.addUtilityExecutionAgency(obj);
	}
	@Override
	public boolean updateUtilityExecutionAgency(Safety obj) throws Exception {
		return dao.updateUtilityExecutionAgency(obj);
	}
	@Override
	public boolean deleteUtilityExecutionAgency(Safety obj) throws Exception {
		return dao.deleteUtilityExecutionAgency(obj);
	}
}
