package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsClrntSysPK implements Serializable{
	private String clrntSysId;

	public String getClrntSysId() {
		return clrntSysId;
	}

	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
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
