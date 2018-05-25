package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsMessagePK implements Serializable {
	private String cdsMessageId;
	private String module;
	
	public String getCdsMessageId() {
		return cdsMessageId;
	}
	public String getModule() {
		return module;
	}
	public void setCdsMessageId(String cdsMessageId) {
		this.cdsMessageId = cdsMessageId;
	}
	public void setModule(String module) {
		this.module = module;
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
