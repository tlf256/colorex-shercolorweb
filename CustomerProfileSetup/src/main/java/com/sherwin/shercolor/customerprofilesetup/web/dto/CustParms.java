package com.sherwin.shercolor.customerprofilesetup.web.dto;

import org.owasp.encoder.Encode;

public class CustParms {
	
	private String customerId; 
	private int seqNbr; 
	private String swuiTitle;
	private String clrntSysId;
	private String cdsAdlFld;
	private boolean active;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		if (customerId!=null) {
			this.customerId = Encode.forHtml(customerId);
		} else {
			this.customerId = customerId;
		}
	}
	public int getSeqNbr() {
		return seqNbr;
	}
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
	}
	public String getSwuiTitle() {
		return swuiTitle;
	}
	public void setSwuiTitle(String swuiTitle) {
		if (swuiTitle!=null) {
			this.swuiTitle = Encode.forHtml(swuiTitle);
		} else {
			this.swuiTitle = swuiTitle;
		}
	}
	public String getClrntSysId() {
		return clrntSysId;
	}
	public void setClrntSysId(String clrntSysId) {
		if (clrntSysId!=null) {
			this.clrntSysId = Encode.forHtml(clrntSysId);
		} else {
			this.clrntSysId = clrntSysId;
		}
	}
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		if (cdsAdlFld!=null) {
			this.cdsAdlFld = Encode.forHtml(cdsAdlFld);
		} else {
			this.cdsAdlFld = cdsAdlFld;
		}
	}
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
