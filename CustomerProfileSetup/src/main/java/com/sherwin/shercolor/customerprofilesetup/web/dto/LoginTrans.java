package com.sherwin.shercolor.customerprofilesetup.web.dto;

public class LoginTrans {
	
	private String keyField;
	private String masterAcctName;
	private String acctComment;
	
	public String getKeyField() {
		return keyField;
	}
	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}
	public String getMasterAcctName() {
		return masterAcctName;
	}
	public void setMasterAcctName(String masterAcctName) {
		if(masterAcctName ==null) {
			this.masterAcctName = "";
		} else {
			this.masterAcctName = masterAcctName;
		}
	}
	public String getAcctComment() {
		return acctComment;
	}
	public void setAcctComment(String acctComment) {
		if(acctComment == null) {
			this.acctComment = "";
		} else {
			this.acctComment = acctComment;
		}
	}
	
}
