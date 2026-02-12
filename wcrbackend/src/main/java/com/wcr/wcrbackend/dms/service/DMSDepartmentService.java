package com.wcr.wcrbackend.dms.service;

import java.util.List;

import com.wcr.wcrbackend.dms.dto.DepartmentDTO;


public interface DMSDepartmentService {

	 public DepartmentDTO createDepartment(DepartmentDTO dto) ;
	 
	 public List<DepartmentDTO> getAllDepartments();
	 
	 public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto);
	 
	 public void deleteDepartment(Long id);
	 
}
