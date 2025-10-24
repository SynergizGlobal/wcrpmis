package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Contract;

public interface IContractRepo {

	List<Contract> getDepartmentList() throws Exception;

}
