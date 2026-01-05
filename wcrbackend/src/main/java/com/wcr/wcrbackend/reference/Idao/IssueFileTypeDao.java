package com.wcr.wcrbackend.reference.Idao;

import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface IssueFileTypeDao {

	List<TrainingType> getIssueFileType(TrainingType obj) throws Exception;

	boolean addIssueFileType(TrainingType obj) throws Exception;

	boolean updateIssueFileType(TrainingType obj) throws Exception;

	boolean deleteIssueFileType(TrainingType obj) throws Exception;

}
