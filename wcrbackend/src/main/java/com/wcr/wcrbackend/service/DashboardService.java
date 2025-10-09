package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Dashboard;
import com.wcr.wcrbackend.repo.IDashboardRepository;
@Service
public class DashboardService implements IDashboardService {

	@Autowired
	private IDashboardRepository dashboardRepository;
	
	@Override
	public List<Dashboard> getDashboardsList(String dashboardType) {
		// TODO Auto-generated method stub
		return dashboardRepository.getDashboardsList(dashboardType);
	}

}
