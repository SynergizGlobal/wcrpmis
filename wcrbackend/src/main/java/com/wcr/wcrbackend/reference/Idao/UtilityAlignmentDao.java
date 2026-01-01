package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Safety;

public interface UtilityAlignmentDao {

	public List<Safety> getUtilityAlignmentsList() throws Exception;

	public boolean addUtilityAlignment(Safety obj) throws Exception;
}