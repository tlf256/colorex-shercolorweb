package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.List;

import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;
import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;

public class RequestObject {
	
	private String customerId;
	private String accttype;
	/*private String ntlacctnbr;
	private String intntlacctnbr;
	private String cce;
	private String bac;
	private String eff;*/
	private String defaultClrntSys;
	private boolean active;
	private boolean history;
	private String swuiTitle;
	private String cdsAdlFld;
	private List<CustParms> custList;
	private List<JobFields> jobFieldList;
	private List<LoginTrans> loginList;
	private List<String> clrntList;
	
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
	/*public String getNtlacctnbr() {
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
	public String getEff() {
		return eff;
	}
	public void setEff(String eff) {
		this.eff = eff;
	}*/
	public String getDefaultClrntSys() {
		return defaultClrntSys;
	}
	public void setDefaultClrntSys(String defaultClrntSys) {
		this.defaultClrntSys = defaultClrntSys;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isHistory() {
		return history;
	}
	public void setHistory(boolean history) {
		this.history = history;
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
	public List<JobFields> getJobFieldList() {
		return jobFieldList;
	}
	public void setJobFieldList(List<JobFields> jobFieldList) {
		this.jobFieldList = jobFieldList;
	}
	public List<CustParms> getCustList() {
		return custList;
	}
	public void setCustList(List<CustParms> custList) {
		this.custList = custList;
	}
	public List<LoginTrans> getLoginList() {
		return loginList;
	}
	public void setLoginList(List<LoginTrans> loginList) {
		this.loginList = loginList;
	}
	
	public List<String> getClrntList() {
		return clrntList;
	}
	public void setClrntList(List<String> clrntList) {
		this.clrntList = clrntList;
	}
	
	public void reset() {
		this.customerId = "";
		this.accttype = "";
		/*this.ntlacctnbr = "";
		this.intntlacctnbr = "";
		this.cce = "";
		this.bac = "";
		this.eff = "";*/
		this.defaultClrntSys = "";
		this.active = false;
		this.history = false;
		this.swuiTitle = "";
		this.cdsAdlFld = "";
		this.jobFieldList = null;
		this.custList = null;
		this.loginList = null;
		this.clrntList = null;
	}
	
}
