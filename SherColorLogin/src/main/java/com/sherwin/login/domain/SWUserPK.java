package com.sherwin.login.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@SuppressWarnings("serial")
public class SWUserPK implements Serializable{
	private String	loginID;
	private Integer acctNbr;

	public Integer getAcctNbr() {
		return acctNbr;
	}

	public void setAcctNbr(Integer acctNbr) {
		this.acctNbr = acctNbr;
	}

	public String getLoginID() {
		return loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
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

