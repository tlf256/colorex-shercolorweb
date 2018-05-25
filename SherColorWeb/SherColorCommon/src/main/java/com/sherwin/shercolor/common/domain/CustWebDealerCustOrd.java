package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CUSTWEBDEALERCUSTORD")
@IdClass(CustWebDealerCustOrdPK.class)
public class CustWebDealerCustOrd {

	private String 	customerId;
	private String 	dlrCustId;
	private int 	controlNbr;
	private String  custOrderNbr;
	private String	comments;
	private Date   	dateAdded;
	private Date   	dateUpdated;
	private String 	updateUser;
	
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
	
	@Id
	@Column(name="controlNbr")
	@NotNull
	public int getControlNbr() {
		return controlNbr;
	}
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	
	@Column(name="custOrderNbr")
	public String getCustOrderNbr() {
		return custOrderNbr;
	}
	public void setCustOrderNbr(String custOrderNbr) {
		this.custOrderNbr = custOrderNbr;
	}
	
	@Column(name="Comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	
}
