package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.DTO.Contract;
import com.wcr.wcrbackend.service.IContractReportService;

@RestController
@RequestMapping("/contract-report")
public class ContractReportController {
	
	@Autowired
	private IContractReportService contractReportService;
	
	@GetMapping("/api/getProjectList")
	public ResponseEntity<List<Contract>> getProjectList() throws Exception{
		List<Contract> contracts = contractReportService.getProjectList();
		return ResponseEntity.ok(contracts);
	}
	
	@PostMapping("/ajax/getHODListInContractReport")
	public ResponseEntity<List<Contract>> getHODListInContractReport(@RequestBody Contract obj) {
		List<Contract> hodList = null;
		try {
			hodList = contractReportService.getHODListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(hodList);
	}
	
	@PostMapping("/ajax/getContractorsListInContractReport")
	public ResponseEntity<List<Contract>> getContractorsListInContractReport(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getContractorsListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	
	@PostMapping("/ajax/getContractStatusListInContractReport")
	public ResponseEntity<List<Contract>> getContractStatusListInContractReport(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getContractStatusListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	
	@PostMapping("/ajax/getStatsuListInContractReport")
	public ResponseEntity<List<Contract>> getStatsuListInContractReport(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getStatsuListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	
	@PostMapping("/ajax/getStatusofWorkItems")
	public ResponseEntity<List<Contract>> getStatusofWorkItems(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getStatusofWorkItems(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
	@PostMapping("/ajax/getContractListInContractReport")
	public ResponseEntity<List<Contract>> getContractListInContractReport(@RequestBody Contract obj) {
		List<Contract> contractorsList = null;
		try {
			contractorsList = contractReportService.getContractListInContractReport(obj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(contractorsList);
	}
}
