package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.OverviewDashboard;

public interface INavService {

	List<OverviewDashboard> getNavMenu(OverviewDashboard obj) throws Exception;

}
