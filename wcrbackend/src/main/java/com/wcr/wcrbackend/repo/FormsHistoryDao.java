package com.wcr.wcrbackend.repo;

import com.wcr.wcrbackend.DTO.FormHistory;

public interface FormsHistoryDao {
	public boolean saveFormHistory(FormHistory obj) throws Exception;

	
}