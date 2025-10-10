package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.OverviewDashboard;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.service.INavService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/nav")
public class NavController {

	@Autowired
	private INavService navService;
	@PostMapping("/api/getNavMenu")
	public List<OverviewDashboard> getNavMenu(@RequestBody OverviewDashboard obj,HttpSession session) throws Exception{
		List<OverviewDashboard> overviewDashboard = null;
		try {
			String parentId = "0";
			obj.setParent_id(parentId);

			User uObj = (User) session.getAttribute("user");
 			obj.setUser_type_fk(uObj.getUserTypeFk());
 			obj.setUser_role_name_fk(uObj.getUserRoleNameFk());
			obj.setUser_id(uObj.getUserId());
			
			overviewDashboard = navService.getNavMenu(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return overviewDashboard;
	}
}
