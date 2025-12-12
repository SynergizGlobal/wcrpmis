package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.DashboardFilterDTO;
import com.wcr.wcrbackend.DTO.DashboardObj;
import com.wcr.wcrbackend.repo.DashboardObjRepository;
import com.wcr.wcrbackend.repo.ModuleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardObjService {

	 private final DashboardObjRepository dashboardRepo;
	    private final ModuleRepository moduleRepo;
	
	 public List<DashboardObj> getDashboardNames() {
	        return dashboardRepo.getDashboardNames();
	    }

	    public List<String> getModules() {
	        return moduleRepo.getModules();
	    }
	    
	    
	    public List<DashboardObj> getDashboardList(DashboardFilterDTO f) {
	        return dashboardRepo.getDashboardList(f);
	    }
	    
	    public DashboardObj getDashboardById(String id) {
	        return dashboardRepo.getDashboardById(id);
	    }

	    
	    public int saveDashboard(DashboardObj dto) throws Exception {
	        return dashboardRepo.saveDashboard(dto);
	    }

	    public void updateDashboard(DashboardObj dto) throws Exception {
	    	dashboardRepo.updateDashboard(dto);
	    }

}
