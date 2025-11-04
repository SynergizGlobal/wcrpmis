package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.DashboardObj;

public interface IDashboardsAccessService {

	List<DashboardObj> getDashboardsList(DashboardObj obj) throws Exception;

	List<DashboardObj> getModulesFilterListInDashboard(DashboardObj obj) throws Exception;

	List<DashboardObj> getDashboardTypesFilterListInDashboard(DashboardObj obj) throws Exception;

	List<DashboardObj> getStatusFilterListInDashboard(DashboardObj obj) throws Exception;

	List<DashboardObj> getContractsListForDashboardForm(DashboardObj obj) throws Exception;

	List<DashboardObj> getUserRolesInDashboardAccess(DashboardObj obj) throws Exception;

	List<DashboardObj> getUserTypesInDashboardAccess(DashboardObj obj) throws Exception;

	List<DashboardObj> getUsersInDashboardAccess(DashboardObj obj) throws Exception;

	List<DashboardObj> getStatusListForDashboardForm(DashboardObj obj) throws Exception;

	DashboardObj getDashboardForm(DashboardObj obj) throws Exception;

	Boolean updateTableauDashboard(DashboardObj obj) throws Exception;

}
