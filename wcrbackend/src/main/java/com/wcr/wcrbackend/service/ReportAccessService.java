package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Report;
import com.wcr.wcrbackend.repo.IReportAccessRepository;
@Service
public class ReportAccessService implements IReportAccessService {

	@Autowired
	private IReportAccessRepository repo;
	@Override
	public List<Report> getReportsList(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getReportsList(obj);
	}
	@Override
	public List<Report> getModulesFilterListInReport(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getModulesFilterListInReport(obj);
	}
	@Override
	public List<Report> getStatusFilterListInReport(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getStatusFilterListInReport(obj);
	}
	@Override
	public List<Report> getUserRolesInReportAccess(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUserRolesInReportAccess(obj);
	}
	@Override
	public List<Report> getUserTypesInReportAccess(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUserTypesInReportAccess(obj);
	}
	@Override
	public List<Report> getUsersInReportAccess(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUsersInReportAccess(obj);
	}
	@Override
	public List<Report> getModulesListForReportAccess(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getModulesListForReportAccess(obj);
	}
	@Override
	public List<Report> getFolderssListForReportAccess(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getFolderssListForReportAccess(obj);
	}
	@Override
	public List<Report> getStatusListForReportAccess(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getStatusListForReportAccess(obj);
	}
	@Override
	public Report getReport(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getReport(obj);
	}
	@Override
	public boolean updateAccessReport(Report obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.updateAccessReport(obj);
	}

}
