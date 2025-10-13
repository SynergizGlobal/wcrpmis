package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Contract;

public interface IContractReportRepository {

	List<Contract> getProjectList() throws Exception;

	List<Contract> getHODListInContractReport(Contract obj) throws Exception;

	List<Contract> getContractorsListInContractReport(Contract obj) throws Exception;

	List<Contract> getContractStatusListInContractReport(Contract obj) throws Exception;

	List<Contract> getStatsuListInContractReport(Contract obj) throws Exception;

	List<Contract> getStatusofWorkItems(Contract obj) throws Exception;

	List<Contract> getContractListInContractReport(Contract obj) throws Exception;

}
