package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.List;

import com.sherwin.shercolor.common.domain.Eula;
import com.sherwin.shercolor.common.domain.EulaHist;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;
import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;

public class RequestObject {
	
	private String customerId;
	private String accttype;
	private String defaultClrntSys;
	private boolean active;
	private boolean history;
	private String swuiTitle;
	private String cdsAdlFld;
	private String website;
	private boolean newCustomer;
	private boolean existingCustomer;
	private List<CustParms> custList;
	private List<JobFields> jobFieldList;
	private List<LoginTrans> loginList;
	private boolean custEdited;
	private boolean jobEdited;
	private boolean loginEdited;
	private boolean custDeleted;
	private boolean jobDeleted;
	private boolean loginDeleted;
	private boolean custAdded;
	private boolean jobAdded;
	private boolean loginAdded;
	private boolean custUnchanged;
	private boolean jobUnchanged;
	private boolean loginUnchanged;
	private List<String> clrntList;
	private List<EulaHist> eulaHistList;
	private List<String> eulaList;
	private int seqNbr;
	private EulaHist eulaHistToActivate;
	private byte[] eulapdf;
	private Eula eula;
	private boolean toactivateRecord;
	private boolean uploadedEula;
	private boolean updateMode;
	
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
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public boolean isNewCustomer() {
		return newCustomer;
	}
	public void setNewCustomer(boolean newCustomer) {
		this.newCustomer = newCustomer;
	}
	public boolean isExistingCustomer() {
		return existingCustomer;
	}
	public void setExistingCustomer(boolean existingCustomer) {
		this.existingCustomer = existingCustomer;
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
	public boolean isCustEdited() {
		return custEdited;
	}
	public void setCustEdited(boolean custEdited) {
		this.custEdited = custEdited;
	}
	public boolean isJobEdited() {
		return jobEdited;
	}
	public void setJobEdited(boolean jobEdited) {
		this.jobEdited = jobEdited;
	}
	public boolean isLoginEdited() {
		return loginEdited;
	}
	public void setLoginEdited(boolean loginEdited) {
		this.loginEdited = loginEdited;
	}
	public boolean isCustDeleted() {
		return custDeleted;
	}
	public void setCustDeleted(boolean custDeleted) {
		this.custDeleted = custDeleted;
	}
	public boolean isJobDeleted() {
		return jobDeleted;
	}
	public void setJobDeleted(boolean jobDeleted) {
		this.jobDeleted = jobDeleted;
	}
	public boolean isLoginDeleted() {
		return loginDeleted;
	}
	public void setLoginDeleted(boolean loginDeleted) {
		this.loginDeleted = loginDeleted;
	}
	public boolean isCustAdded() {
		return custAdded;
	}
	public void setCustAdded(boolean custAdded) {
		this.custAdded = custAdded;
	}
	public boolean isJobAdded() {
		return jobAdded;
	}
	public void setJobAdded(boolean jobAdded) {
		this.jobAdded = jobAdded;
	}
	public boolean isLoginAdded() {
		return loginAdded;
	}
	public void setLoginAdded(boolean loginAdded) {
		this.loginAdded = loginAdded;
	}
	public boolean isCustUnchanged() {
		return custUnchanged;
	}
	public void setCustUnchanged(boolean custUnchanged) {
		this.custUnchanged = custUnchanged;
	}
	public boolean isJobUnchanged() {
		return jobUnchanged;
	}
	public void setJobUnchanged(boolean jobUnchanged) {
		this.jobUnchanged = jobUnchanged;
	}
	public boolean isLoginUnchanged() {
		return loginUnchanged;
	}
	public void setLoginUnchanged(boolean loginUnchanged) {
		this.loginUnchanged = loginUnchanged;
	}
	public List<String> getClrntList() {
		return clrntList;
	}
	public void setClrntList(List<String> clrntList) {
		this.clrntList = clrntList;
	}
	
	public List<EulaHist> getEulaHistList() {
		return eulaHistList;
	}
	public void setEulaHistList(List<EulaHist> eulaHistList) {
		this.eulaHistList = eulaHistList;
	}
	
	public List<String> getEulaList() {
		return eulaList;
	}
	public void setEulaList(List<String> eulaList) {
		this.eulaList = eulaList;
	}
	public int getSeqNbr() {
		return seqNbr;
	}
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
	}
	public EulaHist getEulaHistToActivate() {
		return eulaHistToActivate;
	}
	public void setEulaHistToActivate(EulaHist eulaHistToActivate) {
		this.eulaHistToActivate = eulaHistToActivate;
	}
	public byte[] getEulapdf() {
		return eulapdf;
	}
	public void setEulapdf(byte[] eulapdf) {
		this.eulapdf = eulapdf;
	}
	public Eula getEula() {
		return eula;
	}
	public void setEula(Eula eula) {
		this.eula = eula;
	}
	
	public boolean isToactivateRecord() {
		return toactivateRecord;
	}
	public void setToactivateRecord(boolean toactivateRecord) {
		this.toactivateRecord = toactivateRecord;
	}
	public boolean isUploadedEula() {
		return uploadedEula;
	}
	public void setUploadedEula(boolean uploadedEula) {
		this.uploadedEula = uploadedEula;
	}
	public boolean isUpdateMode() {
		return updateMode;
	}
	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}
	public void reset() {
		this.customerId = "";
		this.accttype = "";
		this.defaultClrntSys = "";
		this.active = false;
		this.history = false;
		this.swuiTitle = "";
		this.cdsAdlFld = "";
		this.jobFieldList = null;
		this.custList = null;
		this.loginList = null;
		this.clrntList = null;
		this.eulaHistList = null;
		this.eulaList = null;
	}
	
}
