package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.OverviewDashboard;

public interface INavRepository {

	List<OverviewDashboard> getNavMenu(OverviewDashboard obj) throws Exception;

}
