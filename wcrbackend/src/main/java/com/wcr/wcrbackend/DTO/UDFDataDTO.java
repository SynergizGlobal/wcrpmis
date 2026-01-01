package com.wcr.wcrbackend.DTO;

public class UDFDataDTO {

	private String udf_type_id;
	private String fk_id;
	private String proj_id;
	private String udf_date;
	private String udf_number;
	private String udf_text;
	private String udf_code_id;
	

	public UDFDataDTO(String[] input) {
		
		
		if (input.length == 7) {
			this.udf_type_id = input[1];
			this.fk_id = input[2];
			this.proj_id = input[3];
			this.udf_date = input[4];
			this.udf_number = input[5];
			this.udf_text = input[6];
			this.udf_code_id = "";
		} else {
			throw new RuntimeException("input data is not correct");
		}
	}

	public UDFDataDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UDFDataDTO(String udf_type_id, String fk_id, String proj_id, String udf_date, String udf_number,
			String udf_text, String udf_code_id) {
		super();
		this.udf_type_id = udf_type_id;
		this.fk_id = fk_id;
		this.proj_id = proj_id;
		this.udf_date = udf_date;
		this.udf_number = udf_number;
		this.udf_text = udf_text;
		this.udf_code_id = udf_code_id;
	}

	public String getUdf_type_id() {
		return udf_type_id;
	}

	public void setUdf_type_id(String udf_type_id) {
		this.udf_type_id = udf_type_id;
	}

	public String getFk_id() {
		return fk_id;
	}

	public void setFk_id(String fk_id) {
		this.fk_id = fk_id;
	}

	public String getProj_id() {
		return proj_id;
	}

	public void setProj_id(String proj_id) {
		this.proj_id = proj_id;
	}

	public String getUdf_date() {
		return udf_date;
	}

	public void setUdf_date(String udf_date) {
		this.udf_date = udf_date;
	}

	public String getUdf_number() {
		return udf_number;
	}

	public void setUdf_number(String udf_number) {
		this.udf_number = udf_number;
	}

	public String getUdf_text() {
		return udf_text;
	}

	public void setUdf_text(String udf_text) {
		this.udf_text = udf_text;
	}

	public String getUdf_code_id() {
		return udf_code_id;
	}

	public void setUdf_code_id(String udf_code_id) {
		this.udf_code_id = udf_code_id;
	}
	@Override
	public String toString() {
		return "UDFDataDTO [udf_type_id=" + udf_type_id + ", fk_id=" + fk_id + ", proj_id=" + proj_id + ", udf_date="
				+ udf_date + ", udf_number=" + udf_number + ", udf_text=" + udf_text + ", udf_code_id=" + udf_code_id
				+ "]";
	}

}
