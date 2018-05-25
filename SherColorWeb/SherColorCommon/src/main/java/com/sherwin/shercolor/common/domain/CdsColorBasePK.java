package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsColorBasePK implements Serializable{
	private String colorComp; 
	private String colorId;   
	private String ieFlag;   
	private int seqNbr;
	
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

	public String getIeFlag() {
		return ieFlag;
	}

	public void setIeFlag(String ieFlag) {
		this.ieFlag = ieFlag;
	}

	public int getSeqNbr() {
		return seqNbr;
	}
	
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
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
