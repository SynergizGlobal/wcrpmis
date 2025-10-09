package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Dashboard;

public interface IDashboardService {

	List<Dashboard> getDashboardsList(String dashboardType);

}
