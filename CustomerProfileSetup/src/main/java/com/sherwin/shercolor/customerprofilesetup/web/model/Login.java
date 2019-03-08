package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.List;

import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;

public class Login {
	private List<String> keyField;
	private List<String> masterAcctName;
	private List<String> acctComment;
	private List<LoginTrans> loginList;
	
	public List<String> getKeyField() {
		return keyField;
	}
	public void setKeyField(List<String> keyField) {
		this.keyField = keyField;
	}
	public List<String> getMasterAcctName() {
		return masterAcctName;
	}
	public void setMasterAcctName(List<String> masterAcctName) {
		this.masterAcctName = masterAcctName;
	}
	public List<String> getAcctComment() {
		return acctComment;
	}
	public void setAcctComment(List<String> acctComment) {
		this.acctComment = acctComment;
	}
	public List<LoginTrans> getLoginList() {
		return loginList;
	}
	public void setLoginList(List<LoginTrans> loginList) {
		this.loginList = loginList;
	}
	
	public void reset() {
		this.keyField = null;
		this.masterAcctName = null;
		this.acctComment = null;
		this.loginList = null;
	}
}
