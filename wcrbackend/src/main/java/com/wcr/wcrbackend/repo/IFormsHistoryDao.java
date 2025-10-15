package com.wcr.wcrbackend.repo;

import com.wcr.wcrbackend.DTO.FormHistory;

public interface IFormsHistoryDao {

	public boolean saveFormHistory(FormHistory obj) throws Exception;

	boolean saveValidityFormHistory(FormHistory obj) throws Exception;

}
