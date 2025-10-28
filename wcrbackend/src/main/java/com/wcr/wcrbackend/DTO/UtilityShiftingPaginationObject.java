package com.wcr.wcrbackend.DTO;

import java.util.List;

public class UtilityShiftingPaginationObject {
	private int iTotalDisplayRecords; 
	private int iTotalRecords;
	private List<UtilityShifting> aaData;
	
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public List<UtilityShifting> getAaData() {
		return aaData;
	}
	public void setAaData(List<UtilityShifting> aaData) {
		this.aaData = aaData;
	}
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
}
