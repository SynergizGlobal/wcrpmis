package com.wcr.wcrbackend.service;

import com.wcr.wcrbackend.entity.User;

public interface ILoginService {
	public User authenticate(String userId, String password) throws Exception;
	public void updateSessionId(String userId, String sessionId);
}
