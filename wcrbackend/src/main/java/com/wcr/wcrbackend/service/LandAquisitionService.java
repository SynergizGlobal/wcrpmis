package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.repo.ILandAquisitionRepo;

import jakarta.transaction.Transactional;

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

	@Override
	public List<LandAcquisition> getStatusList() throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getStatusList();
	}

	@Override
	public List<LandAcquisition> getProjectsList(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getProjectsList(obj);
	}

	@Override
	public List<LandAcquisition> getLandsListForLAForm(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandsListForLAForm(obj);
	}

	@Override
	public List<LandAcquisition> getIssueCatogoriesList() throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getIssueCatogoriesList();
	}

	@Override
	public List<LandAcquisition> getSubCategorysListForLAForm(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getSubCategorysListForLAForm(obj);
	}

	@Override
	public List<LandAcquisition> getUnitsList() throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getUnitsList();
	}

	@Override
	public List<LandAcquisition> getLaFileType() throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLaFileType();
	}

	@Override
	public List<LandAcquisition> getLaLandStatus() throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLaLandStatus();
	}

	@Override
	public LandAcquisition getLandAcquisitionForm(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandAcquisitionForm(obj);
	}

	@Override
	public List<LandAcquisition> getRailwayList(String la_id) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getRailwayList(la_id);
	}

	@Override
	public List<LandAcquisition> getForestList(String la_id) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getForestList(la_id);
	}

	@Override
	public List<LandAcquisition> getGovList(String la_id) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getGovList(la_id);
	}

	@Override
	public List<LandAcquisition> getPrivateLandList(String la_id) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getPrivateLandList(la_id);
	}

	@Override
	public List<LandAcquisition> getPrivateValList(String la_id) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getPrivateValList(la_id);
	}

	@Override
	public List<LandAcquisition> geprivateIRAList(String la_id) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.geprivateIRAList(la_id);
	}

	@Override
	public List<LandAcquisition> getLandAcquisitionList(LandAcquisition dObj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.getLandAcquisitionList(dObj);
	}

	@Override
	@Transactional
	public boolean addLandAcquisition(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.addLandAcquisition(obj);
	}

	@Override
	@Transactional
	public boolean updateLandAcquisition(LandAcquisition obj) throws Exception {
		// TODO Auto-generated method stub
		return landAquisitionRepository.updateLandAcquisition(obj);
	}

}
