package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsColorStandPK implements Serializable {
	private String spectroModel;     
	private String spectroMode;   
	private String colorComp;     
	private String colorId;
	private int seqNbr;

	public String getSpectroModel() {
		return spectroModel;
	}

	public String getSpectroMode() {
		return spectroMode;
	}

	public String getColorComp() {
		return colorComp;
	}

	public String getColorId() {
		return colorId;
	}

	public void setSpectroModel(String spectroModel) {
		this.spectroModel = spectroModel;
	}

	public void setSpectroMode(String spectroMode) {
		this.spectroMode = spectroMode;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
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
