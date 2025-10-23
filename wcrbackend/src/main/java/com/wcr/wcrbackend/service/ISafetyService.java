package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Safety;

public interface ISafetyService {

	List<Safety> getProjectsListForSafetyForm(Safety obj) throws Exception;

	List<Safety> getContractsListForSafetyForm(Safety obj) throws Exception;

}
