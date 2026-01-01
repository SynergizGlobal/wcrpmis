package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.P6Data;


public interface P6NewDataService {

	public String updateP6Activities(List<P6Data> p6dataList1,P6Data obj) throws Exception;

	public List<P6Data> getActivityDataList(P6Data obj) throws Exception;

	public String uploadP6WBSActivities(List<P6Data> wbsList, List<P6Data> activitiesList, P6Data p6data) throws Exception;

	public List<P6Data> getFobList(P6Data obj) throws Exception;
 
	public List<P6Data> getContractsList(P6Data obj) throws Exception;

	public List<P6Data> getFobListFilter(P6Data obj) throws Exception;

	public List<P6Data> getContractsListFilter(P6Data obj) throws Exception;

	public List<P6Data> getUploadTypesFilter(P6Data obj) throws Exception;

	public List<P6Data> getStatusListFilter(P6Data obj) throws Exception;

	public List<P6Data> getWorksList(P6Data obj) throws Exception;

	public List<P6Data> getProjectsList(P6Data obj) throws Exception;

}
