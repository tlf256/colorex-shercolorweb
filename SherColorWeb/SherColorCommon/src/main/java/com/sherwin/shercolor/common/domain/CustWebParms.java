package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CUSTWEBPARMS")
@IdClass(CustWebParmsPK.class)
public class CustWebParms {
	private String customerId; 
	private int seqNbr; 
	private String abbrev;
	private String storeComp;
	private String colorComp;
	private String prodComp;
	private String clrntSysId;
	private boolean active;
	private String swuiTyp;
	private String swuiTitle;
	private String altProdComp1;
	private String altProdComp2;
	private String altProdComp3;
	private String altProdComp4;
	private String altProdComp5;
	private String altColorComp1;
	private String altColorComp2;
	private String altColorComp3;
	private String altColorComp4;
	private String altColorComp5;
	private String formRule;
	private boolean bulkDeep;
	private String bulkDn;
	private String bulkUp;
	private String bulkStart;
	private boolean colorPrime;
	private boolean opacityCtrl;
	private boolean formQtrShot;
	private boolean targetCr2;
	private Date lastUpd;
	private String cdsAdlFld;
	
	@Id
	@Column(name="CustomerId")
	@NotNull
	public String getCustomerId() {
		return customerId;
	}
	
	@Id
	@Column(name="SeqNbr")
	@NotNull
	public int getSeqNbr() {
		return seqNbr;
	}
	
	@Column(name="Abbrev")
	public String getAbbrev() {
		return abbrev;
	}
	
	@Column(name="StoreComp")
	public String getStoreComp() {
		return storeComp;
	}
	
	@Column(name="ColorComp")
	public String getColorComp() {
		return colorComp;
	}
	
	@Column(name="ProdComp")
	public String getProdComp() {
		return prodComp;
	}
	
	@Column(name="ClrntSysId")
	public String getClrntSysId() {
		return clrntSysId;
	}
	
	@Column(name="Active")
	public boolean isActive() {
		return active;
	}
	
	@Column(name="SwuiTyp")
	public String getSwuiTyp() {
		return swuiTyp;
	}
	
	@Column(name="SwuiTitle")
	public String getSwuiTitle() {
		return swuiTitle;
	}
	
	@Column(name="AltProdComp1")
	public String getAltProdComp1() {
		return altProdComp1;
	}
	
	@Column(name="AltProdComp2")
	public String getAltProdComp2() {
		return altProdComp2;
	}
	
	@Column(name="AltProdComp3")
	public String getAltProdComp3() {
		return altProdComp3;
	}
	
	@Column(name="AltProdComp4")
	public String getAltProdComp4() {
		return altProdComp4;
	}
	
	@Column(name="AltProdComp5")
	public String getAltProdComp5() {
		return altProdComp5;
	}
	
	@Column(name="AltColorComp1")
	public String getAltColorComp1() {
		return altColorComp1;
	}
	
	@Column(name="AltColorComp2")
	public String getAltColorComp2() {
		return altColorComp2;
	}
	
	@Column(name="AltColorComp3")
	public String getAltColorComp3() {
		return altColorComp3;
	}
	
	@Column(name="AltColorComp4")
	public String getAltColorComp4() {
		return altColorComp4;
	}
	
	@Column(name="AltColorComp5")
	public String getAltColorComp5() {
		return altColorComp5;
	}
	
	@Column(name="FormRule")
	public String getFormRule() {
		return formRule;
	}
	
	@Column(name="BulkDeep")
	public boolean isBulkDeep() {
		return bulkDeep;
	}
	
	@Column(name="BulkDn")
	public String getBulkDn() {
		return bulkDn;
	}
	
	@Column(name="BulkUp")
	public String getBulkUp() {
		return bulkUp;
	}
	
	@Column(name="BulkStart")
	public String getBulkStart() {
		return bulkStart;
	}
	
	@Column(name="ColorPrime")
	public boolean isColorPrime() {
		return colorPrime;
	}
	
	@Column(name="OpacityCrtl")
	public boolean isOpacityCtrl() {
		return opacityCtrl;
	}
	
	@Column(name="FormQtrShot")
	public boolean isFormQtrShot() {
		return formQtrShot;
	}
	
	@Column(name="TargetCr2")
	public boolean isTargetCr2() {
		return targetCr2;
	}
	
	@Column(name="LastUpd")
	public Date getLastUpd() {
		return lastUpd;
	}
	
	@Column(name="CdsAdlFld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	//////////////  SETTERS  //////////////////
	public void setCustomerId(String customerId) {
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
	public void setAltProdComp1(String altProdComp1) {
		this.altProdComp1 = altProdComp1;
	}
	public void setAltProdComp2(String altProdComp2) {
		this.altProdComp2 = altProdComp2;
	}
	public void setAltProdComp3(String altProdComp3) {
		this.altProdComp3 = altProdComp3;
	}
	public void setAltProdComp4(String altProdComp4) {
		this.altProdComp4 = altProdComp4;
	}
	public void setAltProdComp5(String altProdComp5) {
		this.altProdComp5 = altProdComp5;
	}
	public void setAltColorComp1(String altColorComp1) {
		this.altColorComp1 = altColorComp1;
	}
	public void setAltColorComp2(String altColorComp2) {
		this.altColorComp2 = altColorComp2;
	}
	public void setAltColorComp3(String altColorComp3) {
		this.altColorComp3 = altColorComp3;
	}
	public void setAltColorComp4(String altColorComp4) {
		this.altColorComp4 = altColorComp4;
	}
	public void setAltColorComp5(String altColorComp5) {
		this.altColorComp5 = altColorComp5;
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
	


}
