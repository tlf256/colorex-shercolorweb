package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_CLRNT_COST")
@IdClass(CdsClrntCostPK.class)
public class CdsClrntCost {
	private String clrntSysId;
	private String tintSysId;
	private Date effDate;
	private Date expDate;
	private double cost;
	private String cdsAdlFld;
	
	@Id
	@Column(name="clrnt_sys_id")
	@NotNull
	public String getClrntSysId() {
		return clrntSysId;
	}
	
	@Id
	@Column(name="tint_sys_id")
	@NotNull
	public String getTintSysId() {
		return tintSysId;
	}

	@Column(name="eff_date")
	public Date getEffDate() {
		return effDate;
	}

	@Column(name="exp_date")
	public Date getExpDate() {
		return expDate;
	}

	@Column(name="cost")
	public double getCost() {
		return cost;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	// ****************** SETTERS ***********************************
	
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setTintSysId(String tintSysId) {
		this.tintSysId = tintSysId;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

	
}
