package com.wcr.wcrbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.repo.IUserDao;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDoaService;
	@Override
	public String getRoleCode(String userRoleNameFk) {
		// TODO Auto-generated method stub
		return userDoaService.getRoleCode(userRoleNameFk);
	}

}
