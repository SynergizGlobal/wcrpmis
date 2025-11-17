package com.wcr.wcrbackend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wcr.wcrbackend.entity.Contractor;

public interface ContractorRepository extends JpaRepository<Contractor, String> {

	Contractor findTopByOrderByContractorIdDesc();

}
