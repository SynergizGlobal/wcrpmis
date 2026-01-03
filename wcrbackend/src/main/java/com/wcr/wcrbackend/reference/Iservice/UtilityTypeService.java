package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;
public interface UtilityTypeService {

	public List<Safety> getUtilityTypesList() throws Exception;
	public Safety getUtilityTypesList(Safety obj) throws Exception;

	public boolean addUtilityType(Safety obj) throws Exception;
	public boolean updateUtilityType(Safety obj) throws Exception;
	public boolean deleteUtilityType(Safety obj) throws Exception;
}
