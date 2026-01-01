package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.UtilityAlignmentDao;
import com.wcr.wcrbackend.reference.Iservice.UtilityAlignmentService;
import com.wcr.wcrbackend.reference.model.Safety;
@Service
public class UtilityAlignmentServiceImpl implements UtilityAlignmentService{


	@Autowired
	UtilityAlignmentDao dao;

	@Override
	public List<Safety> getUtilityAlignmentsList() throws Exception {
		return dao.getUtilityAlignmentsList();
	}

	@Override
	public boolean addUtilityAlignment(Safety obj) throws Exception {
		return dao.addUtilityAlignment(obj);
	}
}
