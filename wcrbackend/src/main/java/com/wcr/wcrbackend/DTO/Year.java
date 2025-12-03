package com.wcr.wcrbackend.DTO;

public class Year {
	String financial_year_id,financial_year,year_of_revision;

	public String getYear_of_revision() {
		return year_of_revision;
	}

	public void setYear_of_revision(String year_of_revision) {
		this.year_of_revision = year_of_revision;
	}

	public String getFinancial_year_id() {
		return financial_year_id;
	}

	public void setFinancial_year_id(String financial_year_id) {
		this.financial_year_id = financial_year_id;
	}

	public String getFinancial_year() {
		return financial_year;
	}

	public void setFinancial_year(String financial_year) {
		this.financial_year = financial_year;
	}

}
