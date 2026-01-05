package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.IssueCategoryTitleDao;
import com.wcr.wcrbackend.reference.Iservice.IssueCategoryTitleService;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class IssueCategoryTitleServiceImpl implements IssueCategoryTitleService{
	
	@Autowired
	IssueCategoryTitleDao dao;

	@Override
	public List<TrainingType> gtIssueCategoryDetails(TrainingType obj) throws Exception {
		return dao.gtIssueCategoryDetails(obj);
	}

	@Override
	public List<TrainingType> getIssueCategoryTitle(TrainingType obj) throws Exception {
		return dao.getIssueCategoryTitle(obj);

	}

	@Override
	public boolean addIssueCategoryTitle(TrainingType obj) throws Exception {
		return dao.addIssueCategoryTitle(obj);

	}

	@Override
	public boolean updateIssueCategoryTitle(TrainingType obj) throws Exception {
		return dao.updateIssueCategoryTitle(obj);

	}

	@Override
	public boolean deleteIssueCategoryTitle(TrainingType obj) throws Exception {
		return dao.deleteIssueCategoryTitle(obj);

	}

	@Override
	public List<TrainingType> getTitles(TrainingType obj) throws Exception {
		return dao.getTitles(obj);
	}
}
