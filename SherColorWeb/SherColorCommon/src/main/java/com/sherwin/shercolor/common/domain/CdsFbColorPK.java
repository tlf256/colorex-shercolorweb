package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsFbColorPK implements Serializable{
	private String colorComp;
	private String colorId;
	private String clrntSysId;
	private String fbBase;

	public String getColorComp() {
		return colorComp;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public String getClrntSysId() {
		return clrntSysId;
	}

	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}

	public String getFbBase() {
		return fbBase;
	}

	public void setFbBase(String fbBase) {
		this.fbBase = fbBase;
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
