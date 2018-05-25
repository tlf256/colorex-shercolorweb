package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
public class CdsFbProdPK implements Serializable{
	private String prodComp;
	private String salesNbr;
	private String fbBase;

	public String getProdComp() {
		return prodComp;
	}

	public String getSalesNbr() {
		return salesNbr;
	}

	public String getFbBase() {
		return fbBase;
	}

	public void setProdComp(String prodComp) {
		this.prodComp = prodComp;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
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
