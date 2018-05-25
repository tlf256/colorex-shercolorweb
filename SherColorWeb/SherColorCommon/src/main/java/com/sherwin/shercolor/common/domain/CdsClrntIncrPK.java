package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@SuppressWarnings("serial")
public class CdsClrntIncrPK implements Serializable{
	private String clrntSysId;
	private String incr;

	public String getClrntSysId() {
		return clrntSysId;
	}

	public String getIncr() {
		return incr;
	}
	
	///////////////////////////////
	

	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}

	public void setIncr(String incr) {
		this.incr = incr;
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
