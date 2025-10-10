package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Alerts;
import com.wcr.wcrbackend.repo.IAlertsRepository;
@Service
public class AlertsService implements IAlertsService {

	@Autowired
	private IAlertsRepository alertsRepository;
	@Override
	public List<Alerts> getAdminForms(Alerts aObj) throws Exception{
		// TODO Auto-generated method stub
		return alertsRepository.getAdminForms(aObj);
	}

}
