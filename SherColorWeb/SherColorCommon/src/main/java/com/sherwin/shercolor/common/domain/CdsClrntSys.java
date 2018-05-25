package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_CLRNT_SYS")
@IdClass(CdsClrntSysPK.class)
public class CdsClrntSys {
	private String clrntSysId;
	private String clrntSysName;
	private Date effDate;
	private Date expDate;
	private boolean defaultInd;
	private String cdsAdlFld;
	private String clrntShotSz;
	private String forceProd;
	private String excludeProd;
	private String clrntSysSuffix;
	
	@Id
	@Column(name="clrnt_sys_id")
	@NotNull
	public String getClrntSysId() {
		return clrntSysId;
	}

	@Column(name="clrnt_sys_name")
	public String getClrntSysName() {
		return clrntSysName;
	}

	@Column(name="eff_date")
	public Date getEffDate() {
		return effDate;
	}

	@Column(name="exp_date")
	public Date getExpDate() {
		return expDate;
	}

	@Column(name="default_ind")
	public boolean isDefaultInd() {
		return defaultInd;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	@Column(name="clrnt_shot_sz")
	public String getClrntShotSz() {
		return clrntShotSz;
	}

	@Column(name="force_prod")
	public String getForceProd() {
		return forceProd;
	}

	@Column(name="exclude_prod")
	public String getExcludeProd() {
		return excludeProd;
	}

	@Column(name="clrnt_sys_suffix")
	public String getClrntSysSuffix() {
		return clrntSysSuffix;
	}
	
	///////////////  SETTERS  /////////////
	
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setClrntSysName(String clrntSysName) {
		this.clrntSysName = clrntSysName;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	public void setDefaultInd(boolean defaultInd) {
		this.defaultInd = defaultInd;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public void setClrntShotSz(String clrntShotSz) {
		this.clrntShotSz = clrntShotSz;
	}
	public void setForceProd(String forceProd) {
		this.forceProd = forceProd;
	}
	public void setExcludeProd(String excludeProd) {
		this.excludeProd = excludeProd;
	}
	public void setClrntSysSuffix(String clrntSysSuffix) {
		this.clrntSysSuffix = clrntSysSuffix;
	}


}
