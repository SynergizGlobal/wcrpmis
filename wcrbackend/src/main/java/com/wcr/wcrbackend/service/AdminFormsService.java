package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Admin;
import com.wcr.wcrbackend.repo.IAdminFormsRepository;

@Service
public class AdminFormsService implements IAdminFormsService {

	@Autowired
	private IAdminFormsRepository adminFormsRepository;
	@Override
	public List<Admin> getAdminForms() {
		// TODO Auto-generated method stub
		return adminFormsRepository.getAdminForms();
	}

}
