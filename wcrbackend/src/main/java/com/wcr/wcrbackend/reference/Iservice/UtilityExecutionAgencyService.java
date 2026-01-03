package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;

public interface UtilityExecutionAgencyService {

	public List<Safety> getUtilityExecutionAgencysList() throws Exception;
	public Safety getUtilityExecutionAgencysList(Safety obj) throws Exception;

	public boolean addUtilityExecutionAgency(Safety obj) throws Exception;
	public boolean updateUtilityExecutionAgency(Safety obj) throws Exception;
	public boolean deleteUtilityExecutionAgency(Safety obj) throws Exception;

}