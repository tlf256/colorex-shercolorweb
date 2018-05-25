package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsClrntPK implements Serializable{
	private String clrntSysId;
	private String tintSysId;

	public String getClrntSysId() {
		return clrntSysId;
	}
	public String getTintSysId() {
		return tintSysId;
	}
	
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}

	public void setTintSysId(String tintSysId) {
		this.tintSysId = tintSysId;
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
