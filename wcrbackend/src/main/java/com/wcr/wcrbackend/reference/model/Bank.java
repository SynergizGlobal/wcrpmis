package com.wcr.wcrbackend.reference.model;

import java.util.List;

public class Bank {
	
	private String bank_name_old,bank_name_fk,bank_name,bank_name_new,Table_name,column_name,captiliszedTableName,tName;
	private List<Bank> bankNameList;
	private List<Bank> bankNameList1;
	private List<Bank> tablesList;
	private List<Bank> countList;
	private List<Bank> dList;
	private List<Bank> dList1;	

	public String getBank_name_old() {
		return bank_name_old;
	}

	public void setBank_name_old(String bank_name_old) {
		this.bank_name_old = bank_name_old;
	}

	public String getBank_name_fk() {
		return bank_name_fk;
	}

	public void setBank_name_fk(String bank_name_fk) {
		this.bank_name_fk = bank_name_fk;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_name_new() {
		return bank_name_new;
	}

	public void setBank_name_new(String bank_name_new) {
		this.bank_name_new = bank_name_new;
	}

	public List<Bank> getBankNameList() {
		return bankNameList;
	}

	public void setBankNameList(List<Bank> bankNameList) {
		this.bankNameList = bankNameList;
	}

	public List<Bank> getBankNameList1() {
		return bankNameList1;
	}

	public void setBankNameList1(List<Bank> bankNameList1) {
		this.bankNameList1 = bankNameList1;
	}

	public String getTable_name() {
		return Table_name;
	}

	public void setTable_name(String table_name) {
		Table_name = table_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getCaptiliszedTableName() {
		return captiliszedTableName;
	}

	public void setCaptiliszedTableName(String captiliszedTableName) {
		this.captiliszedTableName = captiliszedTableName;
	}

	public List<Bank> getTablesList() {
		return tablesList;
	}

	public void setTablesList(List<Bank> tablesList) {
		this.tablesList = tablesList;
	}

	public List<Bank> getCountList() {
		return countList;
	}

	public void setCountList(List<Bank> countList) {
		this.countList = countList;
	}

	public List<Bank> getdList() {
		return dList;
	}

	public void setdList(List<Bank> dList) {
		this.dList = dList;
	}

	public List<Bank> getdList1() {
		return dList1;
	}

	public void setdList1(List<Bank> dList1) {
		this.dList1 = dList1;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}
	

}
