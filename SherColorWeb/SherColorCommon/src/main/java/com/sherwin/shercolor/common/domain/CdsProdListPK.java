package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsProdListPK implements Serializable{
	private String prodListId;
	private String salesNbr;

	
	public String getProdListId() {
		return prodListId;
	}

	public String getSalesNbr() {
		return salesNbr;
	}

	public void setProdListId(String prodListId) {
		this.prodListId = prodListId;
	}

	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
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
