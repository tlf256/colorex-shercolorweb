package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CustWebDealerCustOrdPK implements Serializable{

	private String 	customerId; 
	private String 	dlrCustId;
	private int 	controlNbr;
		
	public String getCustomerId() {
		return customerId;
	}
		
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
		
	public String getDlrCustId() {
		return dlrCustId;
	}
		
	public void setDlrCustId(String dlrCustId) {
		this.dlrCustId = dlrCustId;
	} 
	
	public int getControlNbr() {
		return controlNbr;
	}

	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
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


