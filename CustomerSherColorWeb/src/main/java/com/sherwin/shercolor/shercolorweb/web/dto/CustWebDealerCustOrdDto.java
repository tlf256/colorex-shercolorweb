package com.sherwin.shercolor.shercolorweb.web.dto;

public class CustWebDealerCustOrdDto {

	private String 	customerId;
	private String 	dlrCustId;
	private String 	controlNbr;
	private String  custOrderNbr;
	private String	comments;
	private String 	dateAdded;
	private String 	dateUpdated;
	private String 	updateUser;

	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getDlrCustId() {
		return dlrCustId;
	}
	public void setDlrCustId(String dlrCustId) {
		this.dlrCustId = dlrCustId;
	}
	public String getControlNbr() {
		return controlNbr;
	}
	public void setControlNbr(String controlNbr) {
		this.controlNbr = controlNbr;
	}
	public String getCustOrderNbr() {
		return custOrderNbr;
	}
	public void setCustOrderNbr(String custOrderNbr) {
		this.custOrderNbr = custOrderNbr;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
		
}
