package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Form;
import com.wcr.wcrbackend.repo.IFormAccessRepository;
@Service
public class FormAccessService implements IFormAccessService {

	@Autowired
	private IFormAccessRepository repo;
	@Override
	public List<Form> getFormsList(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getFormsList(obj);
	}
	@Override
	public List<Form> getModulesFilterListInForm(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getModulesFilterListInForm(obj);
	}
	@Override
	public List<Form> getStatusFilterListInForm(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getStatusFilterListInForm(obj);
	}
	@Override
	public List<Form> getModulesListForFormAccess(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getModulesListForFormAccess(obj);
	}
	@Override
	public List<Form> getFolderssListForFormAccess(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getFolderssListForFormAccess(obj);
	}
	@Override
	public List<Form> getStatusListForFormAccess(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getStatusListForFormAccess(obj);
	}
	@Override
	public List<Form> getUserRolesInFormAccess(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUserRolesInFormAccess(obj);
	}
	@Override
	public List<Form> getUserTypesInFormAccess(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUserTypesInFormAccess(obj);
	}
	@Override
	public List<Form> getUsersInFormAccess(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getUsersInFormAccess(obj);
	}
	@Override
	public Form getForm(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.getForm(obj);
	}
	@Override
	public Boolean addForm(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.addForm(obj);
	}
	@Override
	public Boolean updateForm(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.updateForm(obj);
	}
	@Override
	public Boolean updateAccessForm(Form obj) throws Exception {
		// TODO Auto-generated method stub
		return repo.updateAccessForm(obj);
	}

}
