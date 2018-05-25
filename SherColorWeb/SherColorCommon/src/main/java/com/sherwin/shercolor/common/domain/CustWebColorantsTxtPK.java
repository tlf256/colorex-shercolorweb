package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CustWebColorantsTxtPK implements Serializable{
	
	private String customerId; 
	private String clrntSysId;
	private String tinterModel;
	private String tinterSerialNbr;
	private String clrntCode;
	private int position;
	
	public String getCustomerId() {
		return customerId;
	}
	public String getClrntSysId() {
		return clrntSysId;
	}
	public String getTinterModel() {
		return tinterModel;
	}
	public String getTinterSerialNbr() {
		return tinterSerialNbr;
	}
	public String getClrntCode() {
		return clrntCode;
	}
	public int getPosition() {
		return position;
	}
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

	@Override
	public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

	@Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
