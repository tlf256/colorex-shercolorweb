package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CUSTWEBDEVICES")
@IdClass(CustWebDevicesPK.class)
public class CustWebDevices {
	private String customerId; 
	private String serialNbr;
	private String deviceType;
	private String deviceModel;
	
	///////////////   GETTERS  ////////////////////
	
	@Id
	@Column(name="CustomerId")
	@NotNull
	public String getCustomerId() {
		return customerId;
	}
	
	@Id
	@Column(name="SerialNbr")
	@NotNull
	public String getSerialNbr() {
		return serialNbr;
	}
	
	@Column(name="DeviceType")
	public String getDeviceType() {
		return deviceType;
	}
	
	@Column(name="DeviceModel")
	public String getDeviceModel() {
		return deviceModel;
	}
	
	
	////////////////////  SETTERS   /////////////////////////
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setSerialNbr(String serialNbr) {
		this.serialNbr = serialNbr;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

}
