package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wcr.wcrbackend.reference.Idao.RailwayDao;
import com.wcr.wcrbackend.reference.Iservice.RailwayService;
import com.wcr.wcrbackend.reference.model.Risk;
import com.wcr.wcrbackend.reference.model.TrainingType;
@Service
public class RailwayServiceImpl implements RailwayService{

	@Autowired
	RailwayDao dao;

	@Override
	public List<Risk> getRailwayList() throws Exception {
		return dao.getRailwayList();
	}

	@Override
	public boolean addRailway(Risk obj) throws Exception {
		return dao.addRailway(obj);
	}

	@Override
	public TrainingType getRailwayDetails(TrainingType obj) throws Exception {
		return dao.getRailwayDetails(obj);
	}

	@Override
	public boolean updateRailway(TrainingType obj) throws Exception {
		return dao.updateRailway(obj);
	}

	@Override
	public boolean deleteRailway(TrainingType obj) throws Exception {
		return dao.deleteRailway(obj);
	}
}
