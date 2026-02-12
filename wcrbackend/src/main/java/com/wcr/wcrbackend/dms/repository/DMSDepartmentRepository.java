package com.wcr.wcrbackend.dms.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.wcr.wcrbackend.dms.entity.Department;


public interface DMSDepartmentRepository extends JpaRepository<Department, Long>{

	Optional<Department> findByName(String name);
	
}
