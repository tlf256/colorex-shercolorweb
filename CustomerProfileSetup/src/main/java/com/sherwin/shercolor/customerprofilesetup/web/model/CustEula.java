package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.Date;

public class CustEula {
	private String website;
	private int seqNbr;
	private String effectiveDate;
	private String expDate;
	private String acceptCode;
	private String eulaText1;
	private boolean activateEula;
	private byte[] eulapdf;
	private String template;
	
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public int getSeqNbr() {
		return seqNbr;
	}
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getAcceptCode() {
		return acceptCode;
	}
	public void setAcceptCode(String acceptCode) {
		this.acceptCode = acceptCode;
	}
	public String getEulaText1() {
		return eulaText1;
	}
	public void setEulaText1(String eulaText1) {
		this.eulaText1 = eulaText1;
	}
	public boolean isActivateEula() {
		return activateEula;
	}
	public void setActivateEula(boolean activateEula) {
		this.activateEula = activateEula;
	}
	public byte[] getEulapdf() {
		return eulapdf;
	}
	public void setEulapdf(byte[] eulapdf) {
		this.eulapdf = eulapdf;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
}
