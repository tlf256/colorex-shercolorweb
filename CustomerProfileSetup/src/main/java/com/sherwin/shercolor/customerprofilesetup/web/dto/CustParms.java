package com.sherwin.shercolor.customerprofilesetup.web.dto;

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
		this.customerId = customerId;
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
		this.swuiTitle = swuiTitle;
	}
	public String getClrntSysId() {
		return clrntSysId;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		if(cdsAdlFld == null) {
			this.cdsAdlFld = "";
		} else {
			this.cdsAdlFld = cdsAdlFld;
		}
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
