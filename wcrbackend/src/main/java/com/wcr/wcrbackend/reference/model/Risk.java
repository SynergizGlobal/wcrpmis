package com.wcr.wcrbackend.reference.model;

public class Risk {
	
	private String risk_classification_id, notification_type, financial_year,sub_area,area_short_name, risk_area_fk,id, type_fk, category,notification_type_icon,
	 resource_type_fk, sub_resource_type,classification, minimum, maximum,area, item_no,revision_status,report_type,railway_id, railway_name, rr_location;

	public String getRr_location() {
		return rr_location;
	}

	public void setRr_location(String rr_location) {
		this.rr_location = rr_location;
	}

	public String getResource_type_fk() {
		return resource_type_fk;
	}

	public void setResource_type_fk(String resource_type_fk) {
		this.resource_type_fk = resource_type_fk;
	}

	public String getSub_resource_type() {
		return sub_resource_type;
	}

	public void setSub_resource_type(String sub_resource_type) {
		this.sub_resource_type = sub_resource_type;
	}

	public String getArea_short_name() {
		return area_short_name;
	}

	public void setArea_short_name(String area_short_name) {
		this.area_short_name = area_short_name;
	}

	public String getFinancial_year() {
		return financial_year;
	}

	public void setFinancial_year(String financial_year) {
		this.financial_year = financial_year;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType_fk() {
		return type_fk;
	}

	public void setType_fk(String type_fk) {
		this.type_fk = type_fk;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSub_area() {
		return sub_area;
	}

	public void setSub_area(String sub_area) {
		this.sub_area = sub_area;
	}

	public String getRisk_area_fk() {
		return risk_area_fk;
	}

	public void setRisk_area_fk(String risk_area_fk) {
		this.risk_area_fk = risk_area_fk;
	}

	public String getNotification_type() {
		return notification_type;
	}

	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}

	public String getNotification_type_icon() {
		return notification_type_icon;
	}

	public void setNotification_type_icon(String notification_type_icon) {
		this.notification_type_icon = notification_type_icon;
	}

	public String getRailway_id() {
		return railway_id;
	}

	public void setRailway_id(String railway_id) {
		this.railway_id = railway_id;
	}

	public String getRailway_name() {
		return railway_name;
	}

	public void setRailway_name(String railway_name) {
		this.railway_name = railway_name;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	public String getRevision_status() {
		return revision_status;
	}

	public void setRevision_status(String revision_status) {
		this.revision_status = revision_status;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getItem_no() {
		return item_no;
	}

	public void setItem_no(String item_no) {
		this.item_no = item_no;
	}

	public String getRisk_classification_id() {
		return risk_classification_id;
	}

	public void setRisk_classification_id(String risk_classification_id) {
		this.risk_classification_id = risk_classification_id;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getMinimum() {
		return minimum;
	}

	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}

	public String getMaximum() {
		return maximum;
	}

	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}

}
