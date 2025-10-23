package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Safety;

public interface ISafetyRepo {

	List<Safety> getProjectsListForSafetyForm(Safety obj) throws Exception;

	List<Safety> getContractsListForSafetyForm(Safety obj) throws Exception;

}
