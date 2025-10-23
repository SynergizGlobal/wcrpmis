package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Design;

public interface IDesignRepo {

	List<Design> getP6ActivitiesData(Design obj) throws Exception;

}
