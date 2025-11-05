package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Report;

public interface IReportAccessService {

	List<Report> getReportsList(Report obj) throws Exception;

	List<Report> getModulesFilterListInReport(Report obj) throws Exception;

	List<Report> getStatusFilterListInReport(Report obj) throws Exception;

	List<Report> getUserRolesInReportAccess(Report obj) throws Exception;

	List<Report> getUserTypesInReportAccess(Report obj) throws Exception;

	List<Report> getUsersInReportAccess(Report obj) throws Exception;

	List<Report> getModulesListForReportAccess(Report obj) throws Exception;

	List<Report> getFolderssListForReportAccess(Report obj) throws Exception;

	List<Report> getStatusListForReportAccess(Report obj) throws Exception;

	Report getReport(Report obj) throws Exception;

	boolean updateAccessReport(Report obj) throws Exception;

}
