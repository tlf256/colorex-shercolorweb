package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CustWebDevicesPK implements Serializable{
	private String customerId; 
	private String modelType;
	private String serialNbr;

	public String getCustomerId() {
		return customerId;
	}
	public String getSerialNbr() {
		return serialNbr;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setSerialNbr(String serialNbr) {
		this.serialNbr = serialNbr;
	}
	
	public void setModelType(String theModelType) {
		this.modelType = theModelType;
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