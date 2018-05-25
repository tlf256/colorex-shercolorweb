package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CUSTWEBLOGINTRANSFORM")
@IdClass(CustWebLoginTransformPK.class)
public class CustWebLoginTransform {
	private String keyField;
	private String customerId;
	private String masterAcctName;
	private String acctComment;

	@Id
	@Column(name="KEYFIELD")
	public String getKeyField() {
		return keyField;
	}
	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}
	
	@Column(name="CUSTOMERID")
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Column(name="MASTERACCTNAME")
	public String getMasterAcctName() {
		return masterAcctName;
	}
	public void setMasterAcctName(String masterAcctName) {
		this.masterAcctName = masterAcctName;
	}
	
	@Column(name="ACCTCOMMENT")
	public String getAcctComment() {
		return acctComment;
	}
	public void setAcctComment(String acctComment) {
		this.acctComment = acctComment;
	}
}
