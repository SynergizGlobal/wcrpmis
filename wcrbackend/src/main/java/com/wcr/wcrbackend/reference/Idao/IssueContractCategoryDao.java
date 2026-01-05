package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface IssueContractCategoryDao {

	List<TrainingType> getContractTypeDetails(TrainingType obj) throws Exception ;

	List<TrainingType> gtIssueCategoryDetails(TrainingType obj) throws Exception ;

	List<TrainingType> getIssueContractCategory(TrainingType obj) throws Exception ;

	boolean addIssueContractCategory(TrainingType obj) throws Exception ;

	boolean updateIssueContractCategory(TrainingType obj) throws Exception ;

	boolean deleteIssueContractCategory(TrainingType obj) throws Exception ;

	List<TrainingType> getContarctCategory(TrainingType obj) throws Exception ;

}
