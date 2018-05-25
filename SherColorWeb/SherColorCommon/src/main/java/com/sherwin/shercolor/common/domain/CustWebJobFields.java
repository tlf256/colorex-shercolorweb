package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CUSTWEBJOBFIELDS")
@IdClass(CustWebJobFieldsPK.class)
public class CustWebJobFields {
	private String customerId; 
	private int seqNbr;
	private String screenLabel;
	private String fieldDefault;
	private boolean entryRequired;
	private boolean active;
	
	///////////////   GETTERS  ////////////////////
	
	@Id
	@Column(name="CustomerId")
	@NotNull
	public String getCustomerId() {
		return customerId;
	}

	@Id
	@Column(name="SeqNbr")
	@NotNull
	public int getSeqNbr() {
		return seqNbr;
	}
	
	@Column(name="ScreenLabel")
	public String getScreenLabel() {
		return screenLabel;
	}
	
	@Column(name="FieldDefault")
	public String getFieldDefault() {
		return fieldDefault;
	}
	
	@Column(name="EntryRequired")
	public boolean isEntryRequired() {
		return entryRequired;
	}
	
	@Column(name="Active")
	public boolean isActive() {
		return active;
	}
	
	////////////////////  SETTERS   /////////////////////////
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
	}
	public void setScreenLabel(String screenLabel) {
		this.screenLabel = screenLabel;
	}
	public void setFieldDefault(String fieldDefault) {
		this.fieldDefault = fieldDefault;
	}
	public void setEntryRequired(boolean entryRequired) {
		this.entryRequired = entryRequired;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	
}
