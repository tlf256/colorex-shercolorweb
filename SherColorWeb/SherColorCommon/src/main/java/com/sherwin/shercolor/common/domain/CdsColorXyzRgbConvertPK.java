package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsColorXyzRgbConvertPK implements Serializable{
	private String illuminant;
	private int observer;

	public String getIlluminant() {
		return illuminant;
	}

	public int getObserver() {
		return observer;
	}

	public void setIlluminant(String illuminant) {
		this.illuminant = illuminant;
	}

	public void setObserver(int observer) {
		this.observer = observer;
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
