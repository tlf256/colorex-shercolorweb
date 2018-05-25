package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsColorRgbv1PK implements Serializable{
	private String colorComp;
	private String colorId;

	public String getColorComp() {
		return colorComp;
	}
	public String getColorId() {
		return colorId;
	}
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
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
