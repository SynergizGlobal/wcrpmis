package com.wcr.wcrbackend.reference.Iservice;


import java.util.List;

import com.wcr.wcrbackend.reference.model.TrainingType;

public interface IssueCategoryTitleService {

	List<TrainingType> gtIssueCategoryDetails(TrainingType obj) throws Exception;

	List<TrainingType> getIssueCategoryTitle(TrainingType obj) throws Exception;

	boolean addIssueCategoryTitle(TrainingType obj) throws Exception;

	boolean updateIssueCategoryTitle(TrainingType obj) throws Exception;

	boolean deleteIssueCategoryTitle(TrainingType obj) throws Exception;

	List<TrainingType> getTitles(TrainingType obj) throws Exception;

}
