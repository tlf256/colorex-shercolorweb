package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CustWebTranPK implements Serializable{
	private String customerId; 
	private int controlNbr; 
	private int lineNbr;
	
	public String getCustomerId() {
		return customerId;
	}
	public int getControlNbr() {
		return controlNbr;
	}
	public int getLineNbr() {
		return lineNbr;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	public void setLineNbr(int lineNbr) {
		this.lineNbr = lineNbr;
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
