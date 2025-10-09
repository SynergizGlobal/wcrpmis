package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Forms;
import com.wcr.wcrbackend.entity.User;

public interface IFormsRepository {
	List<Forms> getUpdateForms(User user);

	List<Forms> getReportForms(User user);
}
