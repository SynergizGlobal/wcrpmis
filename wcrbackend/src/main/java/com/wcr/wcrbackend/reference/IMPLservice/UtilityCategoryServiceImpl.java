package com.wcr.wcrbackend.reference.IMPLservice;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.UtilityCategoryDao;
import com.wcr.wcrbackend.reference.Iservice.UtilityCategoryService;
import com.wcr.wcrbackend.reference.model.Safety;

@Service
public class UtilityCategoryServiceImpl implements UtilityCategoryService{

	@Autowired
	 UtilityCategoryDao dao;

	@Override
	public Safety getUtilityCategorysList(Safety obj) throws Exception {
		return dao.getUtilityCategorysList(obj);
	}
	@Override
	public List<Safety> getUtilityCategorysList() throws Exception {
		return dao.getUtilityCategorysList();
	}	

	@Override
	public boolean addUtilityCategory(Safety obj) throws Exception {
		return dao.addUtilityCategory(obj);
	}
	@Override
	public boolean updateUtilityCategory(Safety obj) throws Exception {
		return dao.updateUtilityCategory(obj);
	}
	@Override
	public boolean deleteUtilityCategory(Safety obj) throws Exception {
		return dao.deleteUtilityCategory(obj);
	}	
}

