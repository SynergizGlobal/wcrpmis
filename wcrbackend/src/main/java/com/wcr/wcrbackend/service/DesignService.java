package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Design;
import com.wcr.wcrbackend.repo.IDesignRepo;
@Service
public class DesignService implements IDesignService {
	@Autowired
	private IDesignRepo designRepo;
	@Override
	public List<Design> getP6ActivitiesData(Design obj) throws Exception{
		// TODO Auto-generated method stub
		return designRepo.getP6ActivitiesData(obj);
	}
	@Override
	public List<Design> getDesignUpdateStructures(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesignUpdateStructures(obj);
	}
	@Override
	public List<Design> getHodListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getHodListFilter(obj);
	}
	@Override
	public List<Design> getDepartmentListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDepartmentListFilter(obj);
	}
	@Override
	public List<Design> getContractListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getContractListFilter(obj);
	}
	@Override
	public List<Design> getStructureListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStructureListFilter(obj);
	}
	@Override
	public List<Design> getStructureIdsListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getStructureIdsListFilter(obj);
	}
	@Override
	public List<Design> getDrawingTypeListFilter(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDrawingTypeListFilter(obj);
	}
	@Override
	public List<Design> getDesigns(Design obj) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesigns(obj);
	}
	@Override
	public List<Design> getDesignsList(Design obj, int startIndex, int offset, String searchParameter)
			throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDesignsList(obj, startIndex, offset, searchParameter);
	}
	@Override
	public int getTotalRecords(Design obj, String searchParameter) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getTotalRecords(obj, searchParameter);
	}
	@Override
	public List<Design> getDrawingRepositoryDesignsList(Design obj, int startIndex, int offset, String searchParameter)
			throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getDrawingRepositoryDesignsList(obj, startIndex, offset, searchParameter);
	}
	@Override
	public int getTotalDrawingRepositoryRecords(Design obj, String searchParameter) throws Exception {
		// TODO Auto-generated method stub
		return designRepo.getTotalDrawingRepositoryRecords(obj, searchParameter);
	}

}
