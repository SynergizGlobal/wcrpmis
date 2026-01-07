package com.wcr.wcrbackend.reference.Idao;

import java.util.List;
import java.util.Map;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface DashboardTypeDao {
	
	public List<TrainingType> getDashboardTypesList() throws Exception;

	public boolean addDashboardType(TrainingType obj) throws Exception;
	
	boolean updateDashboardType(Map<String, String> payload) throws Exception;

	boolean deleteDashboardType(Map<String, String> payload) throws Exception;


}

