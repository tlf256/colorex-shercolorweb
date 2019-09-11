package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.ArrayList;
import java.util.List;

import org.owasp.encoder.Encode;

import com.sherwin.shercolor.common.domain.Eula;

public class Customer {
	private String customerId;
	private String accttype;
	private String ntlacctnbr;
	private String intntlacctnbr;
	private String cce;
	private String bac;
	private String eff;
	private String defaultClrntSys;
	private String swuiTitle;
	private String cdsAdlFld;
	private boolean active;
	private boolean history;
	private List<String> clrntList;
	private String website;
	private boolean activateEula;
	private byte[] eulapdf;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		if(customerId != null) {
			this.customerId = Encode.forHtml(customerId.trim());
		} else {
			this.customerId = customerId;
		}
	}
	public String getAccttype() {
		return accttype;
	}
	public void setAccttype(String accttype) {
		if(accttype != null) {
			this.accttype = Encode.forHtmlAttribute(accttype);
		} else {
			this.accttype = accttype;
		}
	}
	public String getNtlacctnbr() {
		return ntlacctnbr;
	}
	public void setNtlacctnbr(String ntlacctnbr) {
		if(ntlacctnbr != null) {
			this.ntlacctnbr = Encode.forHtml(ntlacctnbr.trim());
		} else {
			this.ntlacctnbr = ntlacctnbr;
		}
	}
	public String getIntntlacctnbr() {
		return intntlacctnbr;
	}
	public void setIntntlacctnbr(String intntlacctnbr) {
		if(intntlacctnbr != null) {
			this.intntlacctnbr = Encode.forHtml(intntlacctnbr.trim());
		} else {
			this.intntlacctnbr = intntlacctnbr;
		}
	}
	public String getCce() {
		return cce;
	}
	public void setCce(String cce) {
		if(cce != null) {
			this.cce = Encode.forHtmlAttribute(cce);
		} else {
			this.cce = cce;
		}
	}
	public String getBac() {
		return bac;
	}
	public void setBac(String bac) {
		if(bac != null) {
			this.bac = Encode.forHtmlAttribute(bac);
		} else {
			this.bac = bac;
		}
	}
	public String getEff() {
		return eff;
	}
	public void setEef(String eff) {
		if(eff != null) {
			this.eff = Encode.forHtmlAttribute(eff);
		} else {
			this.eff = eff;
		}
	}
	public String getDefaultClrntSys() {
		return defaultClrntSys;
	}
	public void setDefaultClrntSys(String defaultClrntSys) {
		if(defaultClrntSys != null) {
			this.defaultClrntSys = Encode.forHtmlAttribute(defaultClrntSys);
		} else {
			this.defaultClrntSys = defaultClrntSys;
		}
	}
	public String getSwuiTitle() {
		return swuiTitle;
	}
	public void setSwuiTitle(String swuiTitle) {
		if(swuiTitle != null) {
			this.swuiTitle = Encode.forHtml(swuiTitle.trim());
		} else {
			this.swuiTitle = swuiTitle;
		}
	}
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		if(cdsAdlFld != null) {
			this.cdsAdlFld = Encode.forHtml(cdsAdlFld.trim());
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
	public boolean isHistory() {
		return history;
	}
	public void setHistory(boolean history) {
		this.history = history;
	}
	public List<String> getClrntList() {
		return clrntList;
	}
	public void setClrntList(List<String> clrntList) {
		if(clrntList != null) {
			List<String> escClrntList = new ArrayList<String>();
			for(String clrnt : clrntList) {
				if(clrnt != null) {
					Encode.forHtml(clrnt.trim());
				} else {
					continue;
				}
				escClrntList.add(clrnt);
				this.clrntList = escClrntList;
			}
		} else {
			this.clrntList = clrntList;
		}
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		if(website != null) {
			this.website = Encode.forHtml(website.trim());
		} else {
			this.website = website;
		}
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
	public void reset() {
		this.customerId = "";
		this.accttype = "";
		this.ntlacctnbr = "";
		this.intntlacctnbr = "";
		this.defaultClrntSys = "";
		this.cce = "";
		this.bac = "";
		this.eff = "";
		this.defaultClrntSys = "";
		this.cdsAdlFld = "";
		this.active = false;
		this.history = false;
		this.swuiTitle = "";
		this.clrntList = null;
	}
}
