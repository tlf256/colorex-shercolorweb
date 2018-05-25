package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsRoomListPK implements Serializable{

	private String listName;
	private Integer seqNbr;
	
	public String getListName() {
		return listName;
	}
	public Integer getSeqNbr() {
		return seqNbr;
	}
	
	//////////////////////////////////////
	
	public void setListName(String listName) {
		this.listName = listName;
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
