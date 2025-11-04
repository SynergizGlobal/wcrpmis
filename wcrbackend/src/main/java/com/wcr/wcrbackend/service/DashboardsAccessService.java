package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.DashboardObj;
import com.wcr.wcrbackend.repo.IDashboardAccessRepository;

import jakarta.transaction.Transactional;

@Service
public class DashboardsAccessService implements IDashboardsAccessService {

	@Autowired
	private IDashboardAccessRepository repo;
	
	@Override
	public List<DashboardObj> getDashboardsList(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getDashboardsList(obj);
	}

	@Override
	public List<DashboardObj> getModulesFilterListInDashboard(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getModulesFilterListInDashboard(obj);
	}

	@Override
	public List<DashboardObj> getDashboardTypesFilterListInDashboard(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getDashboardTypesFilterListInDashboard(obj);
	}

	@Override
	public List<DashboardObj> getStatusFilterListInDashboard(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getStatusFilterListInDashboard(obj);
	}

	@Override
	public List<DashboardObj> getContractsListForDashboardForm(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getContractsListForDashboardForm(obj);
	}

	@Override
	public List<DashboardObj> getUserRolesInDashboardAccess(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUserRolesInDashboardAccess(obj);
	}

	@Override
	public List<DashboardObj> getUserTypesInDashboardAccess(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUserTypesInDashboardAccess(obj);
	}

	@Override
	public List<DashboardObj> getUsersInDashboardAccess(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUsersInDashboardAccess(obj);
	}

	@Override
	public List<DashboardObj> getStatusListForDashboardForm(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getStatusListForDashboardForm(obj);
	}

	@Override
	public DashboardObj getDashboardForm(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getDashboardForm(obj);
	}

	@Override
	@Transactional
	public Boolean updateTableauDashboard(DashboardObj obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.updateTableauDashboard(obj);
	}

}
