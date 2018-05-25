package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsMiscCodesPK implements Serializable{
	private String miscType;
	private String miscCode;
	
	public String getMiscType() {
		return miscType;
	}
	public String getMiscCode() {
		return miscCode;
	}
	
	public void setMiscType(String miscType) {
		this.miscType = miscType;
	}
	public void setMiscCode(String miscCode) {
		this.miscCode = miscCode;
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
