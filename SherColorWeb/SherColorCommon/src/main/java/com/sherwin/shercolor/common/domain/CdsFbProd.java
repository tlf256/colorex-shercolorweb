package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CDS_FB_PROD")
@IdClass(CdsFbProdPK.class)
public class CdsFbProd {
	private String prodComp;
	private String salesNbr;
	private String fbBase;
	private String fbSubBase1;
	private String fbSubBase2;
	private double tsf;
	private String cdsAdlFld;
	private double szOverride ;
	
	@Id
	@Column(name="prod_comp")
	public String getProdComp() {
		return prodComp;
	}
	
	@Id
	@Column(name="sales_nbr")
	public String getSalesNbr() {
		return salesNbr;
	}
	
	@Id
	@Column(name="fb_base")
	public String getFbBase() {
		return fbBase;
	}
	
	@Column(name="fb_sub_base1")
	public String getFbSubBase1() {
		return fbSubBase1;
	}
	
	@Column(name="fb_sub_base2")
	public String getFbSubBase2() {
		return fbSubBase2;
	}
	
	@Column(name="tsf")
	public double getTsf() {
		return tsf;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	@Column(name="sz_override")
	public double getSzOverride() {
		return szOverride;
	}
	
	///////////////////  SETTERS   /////////////////////////////
	
	public void setProdComp(String prodComp) {
		this.prodComp = prodComp;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	public void setFbBase(String fbBase) {
		this.fbBase = fbBase;
	}
	public void setFbSubBase1(String fbSubBase1) {
		this.fbSubBase1 = fbSubBase1;
	}
	public void setFbSubBase2(String fbSubBase2) {
		this.fbSubBase2 = fbSubBase2;
	}
	public void setTsf(double tsf) {
		this.tsf = tsf;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public void setSzOverride(double szOverride) {
		this.szOverride = szOverride;
	}

}
