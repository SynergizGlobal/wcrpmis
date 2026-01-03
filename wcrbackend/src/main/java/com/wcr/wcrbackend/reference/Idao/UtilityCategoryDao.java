package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;

public interface UtilityCategoryDao {
	
	public Safety getUtilityCategorysList(Safety obj) throws Exception;

	public boolean addUtilityCategory(Safety obj) throws Exception;
	public boolean updateUtilityCategory(Safety obj) throws Exception;
	public boolean deleteUtilityCategory(Safety obj) throws Exception;

	public List<Safety> getUtilityCategorysList() throws Exception;	
}