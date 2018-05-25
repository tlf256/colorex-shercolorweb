package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsFormulaChgListPK implements Serializable{
	private String colorComp; 
	private String colorId;
	private String salesNbr;
	private String clrntSysId;
	
	public String getColorComp() {
		return colorComp;
	}
	public String getColorId() {
		return colorId;
	}
	public String getSalesNbr() {
		return salesNbr;
	}
	public String getClrntSysId() {
		return clrntSysId;
	}
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
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
