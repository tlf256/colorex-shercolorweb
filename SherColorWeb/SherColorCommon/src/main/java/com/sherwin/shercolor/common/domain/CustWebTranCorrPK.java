package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CustWebTranCorrPK implements Serializable {
	private String customerId; 
	private int controlNbr; 
	private int lineNbr;
	private int cycle;
	private int unitNbr;
	private int step;
	
	public String getCustomerId() {
		return customerId;
	}
	public int getControlNbr() {
		return controlNbr;
	}
	public int getLineNbr() {
		return lineNbr;
	}
	public int getCycle() {
		return cycle;
	}
	public int getUnitNbr() {
		return unitNbr;
	}
	public int getStep() {
		return step;
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
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public void setUnitNbr(int unitNbr) {
		this.unitNbr = unitNbr;
	}
	public void setStep(int step) {
		this.step = step;
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
