package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Dashboard;
import com.wcr.wcrbackend.entity.User;

public interface IDashboardRepository {

	List<Dashboard> getDashboardsList(String dashboardType, User user);

}
