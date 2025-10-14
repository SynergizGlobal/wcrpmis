package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.repo.ILandAquisitionRepo;

@Service
public class LandAquisitionService implements ILandAquisitionService {

	@Autowired
	private ILandAquisitionRepo landAquisitionRepository;
	
	@Override
	public int getTotalRecords(LandAcquisition obj, String searchParameter) throws Exception{
		// TODO Auto-generated method stub
		return landAquisitionRepository.getTotalRecords(obj, searchParameter);
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionList(LandAcquisition obj, int startIndex, int offset,
			String searchParameter) throws Exception{
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandAcquisitionList(obj, startIndex, offset,
				searchParameter);
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionStatusList(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandAcquisitionStatusList(obj);
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionVillagesList(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandAcquisitionVillagesList(obj);
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionTypesOfLandsList(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandAcquisitionTypesOfLandsList(obj);
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionSubCategoryList(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandAcquisitionSubCategoryList(obj);
	}

	@Override
	public List<LandAcquisition> getCoordinates(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getCoordinates(obj);
	}

	@Override
	public List<LandAcquisition> getSubCategoryList(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getSubCategoryList(obj);
	}

	@Override
	public List<LandAcquisition> getLandsList(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandsList(obj);
	}

	@Override
	public boolean checkSurveyNumber(String survey_number, String village_id, String la_id) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.checkSurveyNumber(survey_number, village_id, la_id);
	}

	@Override
	public List<LandAcquisition> getLADetails(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLADetails(obj);
	}

}
