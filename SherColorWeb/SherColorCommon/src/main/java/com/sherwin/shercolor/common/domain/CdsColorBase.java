package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@Table(name="CDS_COLOR_BASE")
@IdClass(CdsColorBasePK.class)
public class CdsColorBase {
	private String colorComp; 
	private String colorId;   
	private String ieFlag;   
	private int seqNbr;
	private String base;	   
	private String cdsAdlFld;
	private String prodComp;
	private boolean suppressDe;
	
	
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
	@Column(name="ie_flag")
	public String getIeFlag() {
		return ieFlag;
	}

	@Id
	@Column(name="seq_nbr")
	public int getSeqNbr() {
		return seqNbr;
	}

	@Column(name="base")
	public String getBase() {
		return base;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	@Column(name="prod_comp")
	public String getProdComp() {
		return prodComp;
	}

	@Column(name="suppress_de")
	public boolean isSuppressDe() {
		return suppressDe;
	}
	
	//////////////////  SETTERS  /////////////////////////////
	
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setIeFlag(String ieFlag) {
		this.ieFlag = ieFlag;
	}
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public void setProdComp(String prodComp) {
		this.prodComp = prodComp;
	}  
	public void setSuppressDe(boolean suppressDe) {
		this.suppressDe = suppressDe;
	}  


}
