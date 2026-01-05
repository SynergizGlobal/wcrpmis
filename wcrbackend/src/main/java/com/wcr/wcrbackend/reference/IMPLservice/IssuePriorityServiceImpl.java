package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.IssuePriorityDao;
import com.wcr.wcrbackend.reference.Iservice.IssuePriorityService;
import com.wcr.wcrbackend.reference.model.Safety;
import com.wcr.wcrbackend.reference.model.TrainingType;

@Service
public class IssuePriorityServiceImpl implements IssuePriorityService{
		@Autowired
		IssuePriorityDao dao;

		@Override
		public List<Safety> getIssuePriorityList() throws Exception {
			return dao.getIssuePriorityList();
		}

		@Override
		public boolean addIssuePriority(Safety obj) throws Exception {
			return dao.addIssuePriority(obj);
		}

		@Override
		public TrainingType getIssuePriorityDetails(TrainingType obj) throws Exception {
			return dao.getIssuePriorityDetails(obj);
		}

		@Override
		public boolean updateIssuePriority(TrainingType obj) throws Exception {
			return dao.updateIssuePriority(obj);
		}

		@Override
		public boolean deleteIssuePriority(TrainingType obj) throws Exception {
			return dao.deleteIssuePriority(obj);
		}
	}
