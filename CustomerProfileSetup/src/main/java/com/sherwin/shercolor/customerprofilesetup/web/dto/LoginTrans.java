package com.sherwin.shercolor.customerprofilesetup.web.dto;

import org.owasp.encoder.Encode;

public class LoginTrans {
	
	private String keyField;
	private String masterAcctName;
	private String acctComment;
	
	public String getKeyField() {
		return keyField;
	}
	public void setKeyField(String keyField) {
		if (keyField!=null) {
			this.keyField = Encode.forHtml(keyField);
		} else {
			this.keyField = keyField;
		}
	}
	public String getMasterAcctName() {
		return masterAcctName;
	}
	public void setMasterAcctName(String masterAcctName) {
		if (masterAcctName!=null) {
			this.masterAcctName = Encode.forHtml(masterAcctName);
		} else {
			this.masterAcctName = masterAcctName;
		}
	}
	public String getAcctComment() {
		return acctComment;
	}
	public void setAcctComment(String acctComment) {
		if (acctComment!=null) {
			this.acctComment = Encode.forHtml(acctComment);
		} else {
			this.acctComment = acctComment;
		}
	}
	
}
