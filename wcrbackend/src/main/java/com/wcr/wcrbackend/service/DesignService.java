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

}
