package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.LandAcquisition;
import com.wcr.wcrbackend.repo.LandAcquisitionReportRepository;


@Service
public class LandAcquisitionReportService implements ILandAcquisitionReportService{


	@Autowired
	LandAcquisitionReportRepository repo;

	@Override
	public List<LandAcquisition> getProjectsFilterListInLandReport(LandAcquisition obj) throws Exception {
		return repo.getProjectsFilterListInLandReport(obj);
	}

	@Override
	public List<LandAcquisition> getTypeOfLandListInLandReport(LandAcquisition obj) throws Exception {
		return repo.getTypeOfLandListInLandReport(obj);
	}

	@Override
	public List<LandAcquisition> getSubCategoryOfLandFilterListInLandReport(LandAcquisition obj) throws Exception {
		return repo.getSubCategoryOfLandFilterListInLandReport(obj);
	}

	@Override
	public List<LandAcquisition> getWorksFilterListInLandReport(LandAcquisition obj) throws Exception {
		return repo.getWorksFilterListInLandReport(obj);
	}

	@Override
	public LandAcquisition getLandAcquisitionData(LandAcquisition obj) throws Exception {
		return repo.getLandAcquisitionData(obj);
	}
}
