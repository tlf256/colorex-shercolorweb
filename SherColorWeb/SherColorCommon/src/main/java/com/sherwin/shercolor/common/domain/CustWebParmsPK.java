package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CustWebParmsPK implements Serializable{
	private String customerId; 
	private int seqNbr;

	public String getCustomerId() {
		return customerId;
	}
	public int getSeqNbr() {
		return seqNbr;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
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
