package com.wcr.wcrbackend.DTO;

public class UDFDataDTO1 {

	private String fk_id; 
	private String proj_id; 
	private String component; //813
	private String component_id; //814
	private String structure; //815
	private String structure_type_fk;  //816
	 private String weightages; //839
	 private String unit; //840
	 private String scope; //841
	public UDFDataDTO1() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UDFDataDTO1(String fk_id, String proj_id,
			String component,  String component_id, String structure,  String structure_type_fk, String weightages,
			String unit,  String scope ) {
		super();
		this.fk_id = fk_id;
		this.proj_id = proj_id;
		this.component = component;
		this.component_id = component_id;
		this.structure = structure;
		this.structure_type_fk = structure_type_fk;
		this.weightages = weightages;
		this.unit = unit;
		this.scope = scope;
		
		
		
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
	public String getStructure_type_fk() {
		return structure_type_fk;
	}
	public void setStructure_type_fk(String structure_type_fk) {
		this.structure_type_fk = structure_type_fk;
	}
	public String getStructure() {
		return structure;
	}
	public void setStructure(String structure) {
		this.structure = structure;
	}
	public String getComponent_id() {
		return component_id;
	}
	public void setComponent_id(String component_id) {
		this.component_id = component_id;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getWeightages() {
		return weightages;
	}
	public void setWeightages(String weightages) {
		this.weightages = weightages;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
//	@Override
//	public String toString() {
//		return "UDFDataDTO1 [fk_id=" + fk_id + ", proj_id=" + proj_id + ", component=" + component + ", component_id="
//				+ component_id + ", structure=" + structure + ", structure_type_fk=" + structure_type_fk
//				+ ", weightages=" + weightages + ", unit=" + unit + ", scope=" + scope + "]";
//	}
	
	
	
	
	
}
