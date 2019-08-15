package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.ArrayList;
import java.util.List;

import org.owasp.encoder.Encode;

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
		if(keyField != null) {
			List<String> escKeyField = new ArrayList<String>();
			for(String kf : keyField) {
				if(kf != null) {
					Encode.forHtml(kf.trim());
				} else {
					continue;
				}
				escKeyField.add(kf);
				this.keyField = escKeyField;
			}
		} else {
			this.keyField = keyField;
		}
	}
	public List<String> getMasterAcctName() {
		return masterAcctName;
	}
	public void setMasterAcctName(List<String> masterAcctName) {
		if(masterAcctName != null) {
			List<String> escMasterAcctName = new ArrayList<String>();
			for(String man : masterAcctName) {
				if(man != null) {
					Encode.forHtml(man.trim());
				} else {
					continue;
				}
				escMasterAcctName.add(man);
				this.masterAcctName = escMasterAcctName;
			}
		} else {
			this.masterAcctName = masterAcctName;
		}
	}
	public List<String> getAcctComment() {
		return acctComment;
	}
	public void setAcctComment(List<String> acctComment) {
		if(acctComment != null) {
			List<String> escAcctComment = new ArrayList<String>();
			for(String ac : acctComment) {
				if(ac != null) {
					Encode.forHtml(ac.trim());
				} else {
					continue;
				}
				escAcctComment.add(ac);
				this.acctComment = escAcctComment;
			}
		} else {
			this.acctComment = acctComment;
		}
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
