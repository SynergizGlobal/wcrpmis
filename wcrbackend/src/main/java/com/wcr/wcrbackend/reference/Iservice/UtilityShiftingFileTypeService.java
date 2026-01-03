package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;


public interface UtilityShiftingFileTypeService {


	public List<Safety> getUtilityShiftingFileTypeList() throws Exception;
	public Safety getUtilityShiftingFileTypeList(Safety obj) throws Exception;

	public boolean addUtilityShiftingFileType(Safety obj) throws Exception;
	public boolean updateUtilityShiftingFileType(Safety obj) throws Exception;
	public boolean deleteUtilityShiftingFileType(Safety obj) throws Exception;
}
