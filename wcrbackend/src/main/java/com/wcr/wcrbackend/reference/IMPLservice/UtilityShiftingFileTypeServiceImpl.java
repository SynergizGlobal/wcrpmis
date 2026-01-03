package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.UtilityShiftingFileTypeDao;
import com.wcr.wcrbackend.reference.Iservice.UtilityShiftingFileTypeService;
import com.wcr.wcrbackend.reference.model.Safety;


@Service
public class UtilityShiftingFileTypeServiceImpl implements UtilityShiftingFileTypeService{


	@Autowired
	UtilityShiftingFileTypeDao dao;

	@Override
	public Safety getUtilityShiftingFileTypeList(Safety obj) throws Exception {
		return dao.getUtilityShiftingFileTypeList(obj);
	}
	@Override
	public List<Safety> getUtilityShiftingFileTypeList() throws Exception {
		return dao.getUtilityShiftingFileTypeList();
	}	

	@Override
	public boolean addUtilityShiftingFileType(Safety obj) throws Exception {
		return dao.addUtilityShiftingFileType(obj);
	}
	@Override
	public boolean updateUtilityShiftingFileType(Safety obj) throws Exception {
		return dao.updateUtilityShiftingFileType(obj);
	}
	@Override
	public boolean deleteUtilityShiftingFileType(Safety obj) throws Exception {
		return dao.deleteUtilityShiftingFileType(obj);
	}
}
