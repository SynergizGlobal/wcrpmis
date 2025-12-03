package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Structure;
import com.wcr.wcrbackend.repo.IStructureFormDao;

@Service
public class StructureFormServiceImpl implements IStructureFormService{

	@Autowired
	IStructureFormDao dao;

	@Override
	public List<Structure> getStructuresList(Structure obj, int startIndex, int offset, String searchParameter)
			throws Exception {
		return dao.getStructuresList(obj,startIndex,offset,searchParameter);
	}
	
	
	@Override
	public int getTotalRecords(Structure obj, String searchParameter) throws Exception {
		return dao.getTotalRecords(obj,searchParameter);
	}


	@Override
	public List<Structure> getWorkStatusListForFilter(Structure obj) throws Exception {
		return dao.getWorkStatusListForFilter(obj);
	}


	@Override
	public List<Structure> getContractsListForFilter(Structure obj) throws Exception {
		return dao.getContractsListForFilter(obj);
	}


	@Override
	public List<Structure> getStructureTypeListForFilter(Structure obj) throws Exception {
		return dao.getStructureTypeListForFilter(obj);
	}


	@Override
	public List<Structure> getStructureDetailsLocations(Structure obj) throws Exception {
		return dao.getStructureDetailsLocations(obj);
	}

	@Override
	public List<Structure> getStructureDetailsTypes(Structure obj) throws Exception {
		return dao.getStructureDetailsTypes(obj);
	}

	@Override
	public Structure getStructuresFormDetails(Structure obj) throws Exception {
		return dao.getStructuresFormDetails(obj);
	}
}
	