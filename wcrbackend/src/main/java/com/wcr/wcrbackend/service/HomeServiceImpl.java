package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.repo.HomeDao;


@Service
public class HomeServiceImpl implements HomeService {
	
	
	@Autowired
	HomeDao dao;


	@Override
	public List<String> getExecutionStatusList() throws Exception {
		return dao.getExecutionStatusList();
	}
}	