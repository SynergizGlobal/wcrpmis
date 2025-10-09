package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Dashboard;

public interface IDashboardRepository {

	List<Dashboard> getDashboardsList(String dashboardType);

}
