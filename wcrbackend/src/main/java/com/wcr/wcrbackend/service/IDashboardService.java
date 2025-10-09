package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Dashboard;
import com.wcr.wcrbackend.entity.User;

public interface IDashboardService {

	List<Dashboard> getDashboardsList(String dashboardType, User user);

}
