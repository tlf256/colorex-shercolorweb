package com.sherwin.shercolor.customerprofilesetup.web.model;

public class CustomerSummary {
	private String customerId;
	private String customerName;
	private String clrntSysSummary;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getClrntSysSummary() {
		return clrntSysSummary;
	}
	public void setClrntSysSummary(String clrntSysSummary) {
		this.clrntSysSummary = clrntSysSummary;
	}
}
