package com.sherwin.shercolor.common.domain;

import java.util.ArrayList;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class OeFormInputParms {

	@SerializedName("store-nbr")
	private int customerId; 
	
	@SerializedName("seq-nbr")
	private int seqNbr; 
	
	@SerializedName("abbrev")
	private String abbrev;
	
	@SerializedName("store-comp")
	private String storeComp;
	
	@SerializedName("color-comp")
	private String colorComp;
	
	@SerializedName("prod-comp")
	private String prodComp;
	
	@SerializedName("clrnt-sys-id")
	private String clrntSysId;
	
	@SerializedName("active")
	private boolean active;
	
	@SerializedName("swui-typ")
	private String swuiTyp;
	
	@SerializedName("swui-title")
	private String swuiTitle;
	
	@SerializedName("alt-color-comp")
	private ArrayList<String> altColorComp;
	
	@SerializedName("alt-prod-comp")
	private ArrayList<String> altProdComp;
	
	@SerializedName("form-rule")
	private String formRule;
	
	@SerializedName("bulk-deep")
	private boolean bulkDeep;
	
	@SerializedName("bulk-dn")
	private String bulkDn;
	
	@SerializedName("bulk-up")
	private String bulkUp;
	
	@SerializedName("bulk-start")
	private String bulkStart;
	
	@SerializedName("color-prime")
	private boolean colorPrime;
	
	@SerializedName("opacity-ctrl")
	private boolean opacityCtrl;
	
	@SerializedName("form-qtr-shot")
	private boolean formQtrShot;
	
	@SerializedName("target-cr2")
	private boolean targetCr2;
	
	@SerializedName("last-upd")
	private Date lastUpd;
	
	@SerializedName("cds-adl-fld")
	private String cdsAdlFld;

	@SerializedName("scRequestID")
	private String scRequestId;

	@SerializedName("dbRowID")
	private String dbRowId;
	
	//////////////  GETTERS  ////////////////

	public int getCustomerId() {
		return customerId;
	}

	public int getSeqNbr() {
		return seqNbr;
	}

	public String getAbbrev() {
		return abbrev;
	}

	public String getStoreComp() {
		return storeComp;
	}

	public String getColorComp() {
		return colorComp;
	}

	public String getProdComp() {
		return prodComp;
	}

	public String getClrntSysId() {
		return clrntSysId;
	}

	public boolean isActive() {
		return active;
	}

	public String getSwuiTyp() {
		return swuiTyp;
	}

	public String getSwuiTitle() {
		return swuiTitle;
	}

	public ArrayList<String> getAltColorComp() {
		return altColorComp;
	}

	public ArrayList<String> getAltProdComp() {
		return altProdComp;
	}

	public String getFormRule() {
		return formRule;
	}

	public boolean isBulkDeep() {
		return bulkDeep;
	}

	public String getBulkDn() {
		return bulkDn;
	}

	public String getBulkUp() {
		return bulkUp;
	}

	public String getBulkStart() {
		return bulkStart;
	}

	public boolean isColorPrime() {
		return colorPrime;
	}

	public boolean isOpacityCtrl() {
		return opacityCtrl;
	}

	public boolean isFormQtrShot() {
		return formQtrShot;
	}

	public boolean isTargetCr2() {
		return targetCr2;
	}

	public Date getLastUpd() {
		return lastUpd;
	}

	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	public String getScRequestId() {
		return scRequestId;
	}

	public String getDbRowId() {
		return dbRowId;
	}

	//////////////  SETTERS  ///////////////
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
	}

	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}

	public void setStoreComp(String storeComp) {
		this.storeComp = storeComp;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}

	public void setProdComp(String prodComp) {
		this.prodComp = prodComp;
	}

	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setSwuiTyp(String swuiTyp) {
		this.swuiTyp = swuiTyp;
	}

	public void setSwuiTitle(String swuiTitle) {
		this.swuiTitle = swuiTitle;
	}

	public void setAltColorComp(ArrayList<String> altColorComp) {
		this.altColorComp = altColorComp;
	}

	public void setAltProdComp(ArrayList<String> altProdComp) {
		this.altProdComp = altProdComp;
	}

	public void setFormRule(String formRule) {
		this.formRule = formRule;
	}

	public void setBulkDeep(boolean bulkDeep) {
		this.bulkDeep = bulkDeep;
	}

	public void setBulkDn(String bulkDn) {
		this.bulkDn = bulkDn;
	}

	public void setBulkUp(String bulkUp) {
		this.bulkUp = bulkUp;
	}

	public void setBulkStart(String bulkStart) {
		this.bulkStart = bulkStart;
	}

	public void setColorPrime(boolean colorPrime) {
		this.colorPrime = colorPrime;
	}

	public void setOpacityCtrl(boolean opacityCtrl) {
		this.opacityCtrl = opacityCtrl;
	}

	public void setFormQtrShot(boolean formQtrShot) {
		this.formQtrShot = formQtrShot;
	}

	public void setTargetCr2(boolean targetCr2) {
		this.targetCr2 = targetCr2;
	}

	public void setLastUpd(Date lastUpd) {
		this.lastUpd = lastUpd;
	}

	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

	public void setScRequestId(String scRequestId) {
		this.scRequestId = scRequestId;
	}

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}

	

}
