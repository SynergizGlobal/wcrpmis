package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;
public interface UtilityStatusDao {

	public List<Safety> getUtilityStatusList() throws Exception;
	public Safety getUtilityStatusList(Safety obj) throws Exception;

	public boolean addUtilityStatus(Safety obj) throws Exception;
	public boolean updateUtilityStatus(Safety obj) throws Exception;
	public boolean deleteUtilityStatus(Safety obj) throws Exception;
}
