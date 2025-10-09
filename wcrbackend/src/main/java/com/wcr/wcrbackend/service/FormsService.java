package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Forms;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.IFormsRepository;

@Service
public class FormsService implements IFormsService{
	@Autowired
	private IFormsRepository formsRepository;
	@Override
	public List<Forms> getUpdateForms(User user) {
		// TODO Auto-generated method stub
		return formsRepository.getUpdateForms(user);
	}
	@Override
	public List<Forms> getReportForms(User user) {
		// TODO Auto-generated method stub
		return formsRepository.getReportForms(user);
	}

}
