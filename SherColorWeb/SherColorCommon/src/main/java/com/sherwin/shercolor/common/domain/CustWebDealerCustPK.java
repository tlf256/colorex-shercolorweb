package com.sherwin.shercolor.common.domain;
import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CustWebDealerCustPK implements Serializable{

	private String customerId; 
	private String dlrCustId;
		
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
		
	@Override
	public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
	    return HashCodeBuilder.reflectionHashCode(this);
	}
}

