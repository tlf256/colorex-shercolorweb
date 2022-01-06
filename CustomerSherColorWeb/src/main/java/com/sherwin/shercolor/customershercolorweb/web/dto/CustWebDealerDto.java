package com.sherwin.shercolor.customershercolorweb.web.dto;

public class CustWebDealerDto {

	private String 	customerId;
	private String 	dealerName;
	private String  dlrPhoneNbr;
	private String	homeStore;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getDlrPhoneNbr() {
		return dlrPhoneNbr;
	}
	public void setDlrPhoneNbr(String dlrPhoneNbr) {
		this.dlrPhoneNbr = dlrPhoneNbr;
	}
	public String getHomeStore() {
		return homeStore;
	}
	public void setHomeStore(String homeStore) {
		this.homeStore = homeStore;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	private String  comments;

}
