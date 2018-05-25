package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsProdNotesPK implements Serializable{

	private String salesNbr;
	private Integer seqNbr;
	
	public String getSalesNbr() {
		return salesNbr;
	}
	public Integer getSeqNbr() {
		return seqNbr;
	}
	
	//////////////////////////////////////////////////
	
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	public void setSeqNbr(Integer seqNbr) {
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
