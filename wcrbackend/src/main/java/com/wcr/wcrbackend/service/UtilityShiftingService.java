package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.UtilityShifting;
import com.wcr.wcrbackend.repo.IUtilityShiftingRepo;

@Service
public class UtilityShiftingService implements IUtilityShiftingService {

	@Autowired
	private IUtilityShiftingRepo utilitiyShiftingRepo;
	@Override
	public List<UtilityShifting> getImpactedContractsListForUtilityShifting(UtilityShifting obj) throws Exception{
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getImpactedContractsListForUtilityShifting(obj);
	}
	@Override
	public List<UtilityShifting> getLocationListFilter(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getLocationListFilter(obj);
	}
	@Override
	public List<UtilityShifting> getUtilityCategoryListFilter(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getUtilityCategoryListFilter(obj);
	}
	@Override
	public List<UtilityShifting> getUtilityTypeListFilter(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getUtilityTypeListFilter(obj);
	}
	@Override
	public List<UtilityShifting> getUtilityStatusListFilter(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getUtilityStatusListFilter(obj);
	}
	@Override
	public List<UtilityShifting> getHodListForUtilityShifting(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getHodListForUtilityShifting(obj);
	}
	@Override
	public List<UtilityShifting> getReqStageList(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getReqStageList(obj);
	}
	@Override
	public List<UtilityShifting> getImpactedElementList(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getImpactedElementList(obj);
	}
	@Override
	public List<UtilityShifting> getProjectsListForUtilityShifting(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getProjectsListForUtilityShifting(obj);
	}
	@Override
	public List<UtilityShifting> getContractsListForUtilityShifting(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getContractsListForUtilityShifting(obj);
	}
	@Override
	public List<UtilityShifting> getUtilityTypeList(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getUtilityTypeList(obj);
	}
	@Override
	public List<UtilityShifting> getUtilityCategoryList(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getUtilityCategoryList(obj);
	}
	@Override
	public List<UtilityShifting> getUtilityExecutionAgencyList(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getUtilityExecutionAgencyList(obj);
	}
	@Override
	public List<UtilityShifting> getImpactedContractList(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getImpactedContractList(obj);
	}
	@Override
	public List<UtilityShifting> getRequirementStageList(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getRequirementStageList(obj);
	}
	@Override
	public List<UtilityShifting> getUnitListForUtilityShifting(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getUnitListForUtilityShifting(obj);
	}
	@Override
	public List<UtilityShifting> getUtilityTypeListForUtilityShifting(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getUtilityTypeListForUtilityShifting(obj);
	}
	@Override
	public List<UtilityShifting> getStatusListForUtilityShifting(UtilityShifting obj) throws Exception {
		// TODO Auto-generated method stub
		return utilitiyShiftingRepo.getStatusListForUtilityShifting(obj);
	}

}
