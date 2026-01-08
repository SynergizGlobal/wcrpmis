package com.wcr.wcrbackend.reference.Iservice;
import java.util.List;
import com.wcr.wcrbackend.reference.model.Risk;
import com.wcr.wcrbackend.reference.model.TrainingType;
public interface RailwayService {
	
	public List<Risk> getRailwayList() throws Exception;

	public boolean addRailway(Risk obj) throws Exception;

	public TrainingType getRailwayDetails(TrainingType obj) throws Exception;

	public boolean updateRailway(TrainingType obj) throws Exception;

	public boolean deleteRailway(TrainingType obj) throws Exception;
}
