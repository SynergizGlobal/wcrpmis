package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.P6Data;
import com.wcr.wcrbackend.repo.P6NewDataDao;

@Service
public class P6NewDataServiceImpl implements P6NewDataService{
	
	@Autowired
	P6NewDataDao dao;

 
	@Override
	public String updateP6Activities(List<P6Data> p6dataList,P6Data p6Data) throws Exception {
		return dao.updateP6Activities(p6dataList,p6Data);
	}


	@Override
	public List<P6Data> getActivityDataList(P6Data obj) throws Exception {
		return dao.getActivityDataList(obj);
	}


	@Override
	public String uploadP6WBSActivities(List<P6Data> wbsList, List<P6Data> activitiesList, P6Data p6data)
			throws Exception {
		return dao.uploadP6WBSActivities(wbsList,activitiesList,p6data);
	}



	@Override
	public List<P6Data> getFobList(P6Data obj) throws Exception {
		return dao.getFobList(obj);
	}
	
	@Override
	public List<P6Data> getContractsList(P6Data obj) throws Exception {
		return dao.getContractsList(obj);
	}
	
	@Override
	public List<P6Data> getFobListFilter(P6Data obj) throws Exception {
		return dao.getFobListFilter(obj);
	}
	
	@Override
	public List<P6Data> getContractsListFilter(P6Data obj) throws Exception {
		return dao.getContractsListFilter(obj);
	}

	@Override
	public List<P6Data> getUploadTypesFilter(P6Data obj) throws Exception {
		return dao.getUploadTypesFilter(obj);
	}

	@Override
	public List<P6Data> getStatusListFilter(P6Data obj) throws Exception {
		return dao.getStatusListFilter(obj);
	}


	@Override
	public List<P6Data> getWorksList(P6Data obj) throws Exception {
		return dao.getWorksList(obj);
	}


	@Override
	public List<P6Data> getProjectsList(P6Data obj) throws Exception {
		return dao.getProjectsList(obj);
	}

}
