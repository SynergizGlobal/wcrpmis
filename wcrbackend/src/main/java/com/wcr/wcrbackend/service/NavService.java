package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.OverviewDashboard;
import com.wcr.wcrbackend.repo.INavRepository;
@Service
public class NavService implements INavService {

	@Autowired
	private INavRepository navRepository;
	@Override
	public List<OverviewDashboard> getNavMenu(OverviewDashboard obj) throws Exception{
		// TODO Auto-generated method stub
		return navRepository.getNavMenu(obj);
	}

}
