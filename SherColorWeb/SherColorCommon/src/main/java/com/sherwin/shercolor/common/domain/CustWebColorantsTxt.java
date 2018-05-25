package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CustWebColorantsTxt")
@IdClass(CustWebColorantsTxtPK.class)
public class CustWebColorantsTxt {

	private String customerId; 
	private String clrntSysId;
	private String tinterModel;
	private String tinterSerialNbr;
	private String clrntCode;
	private int position;
	private Double maxCanisterFill;
	private Double fillAlarmLevel;
	private Double fillStopLevel;
	private Double currentClrntAmount;
	
	
	///////////////  GETTERS  ////////////////////
	
	
	@Id
	@Column(name="CustomerId")
	@NotNull
	public String getCustomerId() {
		return customerId;
	}
		
	@Id
	@Column(name="ClrntSysId")
	@NotNull
	public String getClrntSysId() {
		return clrntSysId;
	}
	
	@Id
	@Column(name="TinterModel")
	@NotNull
	public String getTinterModel() {
		return tinterModel;
	}
	
	@Id
	@Column(name="TinterSerialNbr")
	@NotNull
	public String getTinterSerialNbr() {
		return tinterSerialNbr;
	}
	
	@Id
	@Column(name="ClrntCode")
	@NotNull
	public String getClrntCode() {
		return clrntCode;
	}
	
	@Id
	@Column(name="Position")
	@NotNull
	public int getPosition() {
		return position;
	}

	@Column(name="MaxCanisterFill")
	public Double getMaxCanisterFill() {
		return maxCanisterFill;
	}

	@Column(name="FillAlarmLevel")
	public Double getFillAlarmLevel() {
		return fillAlarmLevel;
	}

	@Column(name="FillStopLevel")
	public Double getFillStopLevel() {
		return fillStopLevel;
	}

	@Column(name="CurrentClrntAmount")
	public Double getCurrentClrntAmount() {
		return currentClrntAmount;
	}

	//////////////  SETTERS  ///////////////////////
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setTinterModel(String tinterModel) {
		this.tinterModel = tinterModel;
	}
	public void setTinterSerialNbr(String tinterSerialNbr) {
		this.tinterSerialNbr = tinterSerialNbr;
	}
	public void setClrntCode(String clrntCode) {
		this.clrntCode = clrntCode;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public void setMaxCanisterFill(Double maxCanisterFill) {
		this.maxCanisterFill = maxCanisterFill;
	}
	public void setFillAlarmLevel(Double fillAlarmLevel) {
		this.fillAlarmLevel = fillAlarmLevel;
	}
	public void setFillStopLevel(Double fillStopLevel) {
		this.fillStopLevel = fillStopLevel;
	}
	public void setCurrentClrntAmount(Double currentClrntAmount) {
		this.currentClrntAmount = currentClrntAmount;
	}
	
	
	

}
