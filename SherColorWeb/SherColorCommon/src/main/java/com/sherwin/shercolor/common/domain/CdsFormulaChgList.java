package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CDS_FORMULA_CHG_LIST")
@IdClass(CdsFormulaChgListPK.class)
public class CdsFormulaChgList {

	private String colorComp;
	private String colorId;
	private String salesNbr;
	private String clrntSysId;
	private String prodNbr;
	private Date effDate;
	private Date expDate;
	private String cdsAdlFld;
	private String typeCode;
	private String cdsMessageId;
	
	
	@Id
	@Column(name="color_comp")
	public String getColorComp() {
		return colorComp;
	}

	@Id
	@Column(name="color_id")
	public String getColorId() {
		return colorId;
	}

	@Id
	@Column(name="sales_nbr")
	public String getSalesNbr() {
		return salesNbr;
	}
	
	@Id
	@Column(name="clrnt_sys_id")
	public String getClrntSysId() {
		return clrntSysId;
	}
	
	@Column(name="prod_nbr")
	public String getProdNbr() {
		return prodNbr;
	}
	
	@Column(name="eff_date")
	public Date getEffDate() {
		return effDate;
	}
	
	@Column(name="exp_date")
	public Date getExpDate() {
		return expDate;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	@Column(name="type_code")
	public String getTypeCode() {
		return typeCode;
	}
	
	@Column(name="cdsMessageId")
	public String getCdsMessageId() {
		return cdsMessageId;
	}
	
	///////////////  SETTERS  ///////////////////

	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public void setCdsMessageId(String cdsMessageId) {
		this.cdsMessageId = cdsMessageId;
	}
	
	
	
}
