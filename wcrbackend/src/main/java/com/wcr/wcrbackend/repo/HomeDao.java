package com.wcr.wcrbackend.repo;

import java.util.List;

public interface HomeDao {
	
	public List<String> getExecutionStatusList() throws Exception;
}