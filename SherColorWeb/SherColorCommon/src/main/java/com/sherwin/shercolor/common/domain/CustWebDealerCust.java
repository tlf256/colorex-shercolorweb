package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CUSTWEBDEALERCUST")
@IdClass(CustWebDealerCustPK.class)
public class CustWebDealerCust {

	private String customerId;
	private String dlrCustId;
	private String dlrCustName;
	private String dlrCustAddress;
	private String dlrCustCity;
	private String dlrCustState;
	private String dlrCustZip;
	private String dlrCustCountry;
	private String dlrCustPhoneNbr;
	private String dlrCustContact;
	private Date   dateAdded;
	private Date   dateUpdated;
	private String updateUser;
	private String comments;
	
	@Id
	@Column(name="customerId")
	@NotNull
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Id
	@Column(name="dlrCustId")
	@NotNull
	public String getDlrCustId() {
		return dlrCustId;
	}
	
	public void setDlrCustId(String dlrCustId) {
		this.dlrCustId = dlrCustId;
	}
	
	@Column(name="dlrCustName")
	public String getDlrCustName() {
		return dlrCustName;
	}
	public void setDlrCustName(String dlrCustName) {
		this.dlrCustName = dlrCustName;
	}

	@Column(name="dlrCustAddress")
	public String getDlrCustAddress() {
		return dlrCustAddress;
	}
	public void setDlrCustAddress(String dlrCustAddress) {
		this.dlrCustAddress = dlrCustAddress;
	}

	@Column(name="dlrCustCity")
	public String getDlrCustCity() {
		return dlrCustCity;
	}
	public void setDlrCustCity(String dlrCustCity) {
		this.dlrCustCity = dlrCustCity;
	}

	@Column(name="dlrCustState")
	public String getDlrCustState() {
		return dlrCustState;
	}
	public void setDlrCustState(String dlrCustState) {
		this.dlrCustState = dlrCustState;
	}
	
	@Column(name="dlrCustZip")
	public String getDlrCustZip() {
		return dlrCustZip;
	}
	public void setDlrCustZip(String dlrCustZip) {
		this.dlrCustZip = dlrCustZip;
	}
	
	@Column(name="dlrCustCountry")
	public String getDlrCustCountry() {
		return dlrCustCountry;
	}
	public void setDlrCustCountry(String dlrCustCountry) {
		this.dlrCustCountry = dlrCustCountry;
	}
	
	@Column(name="dlrCustContact")
	public String getDlrCustContact() {
		return dlrCustContact;
	}
	public void setDlrCustContact(String dlrCustContact) {
		this.dlrCustContact = dlrCustContact;
	}
	
	@Column(name="dateAdded")
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	@Column(name="dateUpdated")
	public Date getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	@Column(name="updateUser")
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Column(name="dlrCustPhoneNbr")
	public String getDlrCustPhoneNbr() {
		return dlrCustPhoneNbr;
	}
	public void setDlrCustPhoneNbr(String dlrCustPhoneNbr) {
		this.dlrCustPhoneNbr = dlrCustPhoneNbr;
	}

	@Column(name="comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
