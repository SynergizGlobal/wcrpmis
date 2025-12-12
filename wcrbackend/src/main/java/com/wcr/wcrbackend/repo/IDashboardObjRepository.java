package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.DashboardFilterDTO;
import com.wcr.wcrbackend.DTO.DashboardObj;

public interface IDashboardObjRepository {
	
    List<DashboardObj> getDashboardNames();
    
    List<DashboardObj> getDashboardList(DashboardFilterDTO filter);
    
	/* List<DashboardObj> getAllDashboards(); */ 

}
