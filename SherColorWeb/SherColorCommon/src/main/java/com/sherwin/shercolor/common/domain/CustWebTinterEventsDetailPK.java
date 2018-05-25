package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@SuppressWarnings("serial")
public class CustWebTinterEventsDetailPK implements Serializable{
	private String guid;
	private String type;
	private String name;

	
	public String getGuid() {
		return guid;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
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
