package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Forms;
import com.wcr.wcrbackend.entity.User;

public interface IFormsService {
	List<Forms> getUpdateForms(User user);

	List<Forms> getReportForms(User user);
}
