package com.wcr.wcrbackend.reference.IMPLservice;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.reference.Idao.BankNameDao;
import com.wcr.wcrbackend.reference.Iservice.BankNameService;
import com.wcr.wcrbackend.reference.model.Bank;

@Service
public class BankNameServiceImpl implements BankNameService{

	@Autowired
	BankNameDao dao;

	@Override
	public List<Bank> getBankNamesList() throws Exception {
		return dao.getBankNamesList();
	}

	@Override
	public boolean addBankName(Bank obj) throws Exception {
		return dao.addBankName(obj);
	}

	@Override
	public boolean updateBankName(Bank obj) throws Exception {
		return dao.updateBankName(obj);
	}

	@Override
	public Bank getBankNameDetails(Bank obj) throws Exception {
		return dao.getBankNameDetails(obj);
	}

	@Override
	public boolean deleteBankName(Bank obj) throws Exception {
		return dao.deleteBankName(obj);
	}
}
