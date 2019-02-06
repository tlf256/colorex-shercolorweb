package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.List;

import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;

public class Customer {
	private String customerId;
	private String accttype;
	private String ntlacctnbr;
	private String intntlacctnbr;
	private String cce;
	private String bac;
	private String eef;
	private String defaultClrntSys;
	private String swuiTitle;
	private String cdsAdlFld;
	private boolean active;
	private boolean history;
	private List<String> clrntList;
	private List<CustParms> custList;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAccttype() {
		return accttype;
	}
	public void setAccttype(String accttype) {
		this.accttype = accttype;
	}
	public String getNtlacctnbr() {
		return ntlacctnbr;
	}
	public void setNtlacctnbr(String ntlacctnbr) {
		this.ntlacctnbr = ntlacctnbr;
	}
	public String getIntntlacctnbr() {
		return intntlacctnbr;
	}
	public void setIntntlacctnbr(String intntlacctnbr) {
		this.intntlacctnbr = intntlacctnbr;
	}
	public String getCce() {
		return cce;
	}
	public void setCce(String cce) {
		this.cce = cce;
	}
	public String getBac() {
		return bac;
	}
	public void setBac(String bac) {
		this.bac = bac;
	}
	public String getEef() {
		return eef;
	}
	public void setEef(String eef) {
		this.eef = eef;
	}
	public String getDefaultClrntSys() {
		return defaultClrntSys;
	}
	public void setDefaultClrntSys(String defaultClrntSys) {
		this.defaultClrntSys = defaultClrntSys;
	}
	public String getSwuiTitle() {
		return swuiTitle;
	}
	public void setSwuiTitle(String swuiTitle) {
		this.swuiTitle = swuiTitle;
	}
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean getHistory() {
		return history;
	}
	public void setHistory(boolean history) {
		this.history = history;
	}
	public List<String> getClrntList() {
		return clrntList;
	}
	public void setClrntList(List<String> clrntList) {
		this.clrntList = clrntList;
	}
	public List<CustParms> getCustList() {
		return custList;
	}
	public void setCustList(List<CustParms> custList) {
		this.custList = custList;
	}
	
	public void reset() {
		this.customerId = "";
		this.accttype = "";
		this.ntlacctnbr = "";
		this.intntlacctnbr = "";
		this.defaultClrntSys = "";
		this.cce = "";
		this.bac = "";
		this.eef = "";
		this.defaultClrntSys = "";
		this.cdsAdlFld = "";
		this.active = false;
		this.swuiTitle = "";
		this.clrntList = null;
		this.custList = null;
	}
}
