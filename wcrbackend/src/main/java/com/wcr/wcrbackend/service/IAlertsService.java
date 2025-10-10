package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Alerts;

public interface IAlertsService {

	List<Alerts> getAdminForms(Alerts aObj) throws Exception;

}
