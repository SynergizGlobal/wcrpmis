package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Form;

public interface IFormAccessRepository {

	List<Form> getFormsList(Form obj) throws Exception;

	List<Form> getModulesFilterListInForm(Form obj) throws Exception;

	List<Form> getStatusFilterListInForm(Form obj) throws Exception;

	List<Form> getModulesListForFormAccess(Form obj) throws Exception;

	List<Form> getFolderssListForFormAccess(Form obj) throws Exception;

	List<Form> getStatusListForFormAccess(Form obj) throws Exception;

	List<Form> getUserRolesInFormAccess(Form obj) throws Exception;

	List<Form> getUserTypesInFormAccess(Form obj) throws Exception;

	List<Form> getUsersInFormAccess(Form obj) throws Exception;

	Form getForm(Form obj) throws Exception;

	Boolean addForm(Form obj) throws Exception;

	Boolean updateForm(Form obj) throws Exception;

	Boolean updateAccessForm(Form obj) throws Exception;

}
