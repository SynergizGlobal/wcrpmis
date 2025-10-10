package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Alerts;

public interface IAlertsRepository {

	List<Alerts> getAdminForms(Alerts aObj) throws Exception;

}
