package com.wcr.wcrbackend.reference.Iservice;

import java.util.List;

import com.wcr.wcrbackend.reference.model.Bank;

public interface BankNameService {

	public List<Bank> getBankNamesList() throws Exception;

	public boolean addBankName(Bank obj) throws Exception;

	public boolean updateBankName(Bank obj) throws Exception;

	public Bank getBankNameDetails(Bank obj) throws Exception;

	public boolean deleteBankName(Bank obj) throws Exception;
}
