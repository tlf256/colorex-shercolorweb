package com.sherwin.shercolor.common.domain;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_CLRNT")
@IdClass(CdsClrntPK.class)
public class CdsClrnt {
	private String clrntSysId;
	private String tintSysId;
	private String fbSysId;
	private String name;
	private String engSysId;
	private String organicInd;
	private String salesNbr;
	private double cost;
	private String cdsAdlFld;
	private String statusInd;
	private String excludeProd;
	private String forceProd;
	private String mixRestrictions;
	private String replaceFor;
	private String stdAltClrntSys;
	private String vsAltClrntSys;
	private String hpAltClrntSys;
	
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

	@Column(name="fb_sys_id")
	@NotNull
	public String getFbSysId() {
		return fbSysId;
	}

	@Column(name="name")
	@NotNull
	public String getName() {
		return name;
	}

	@Column(name="eng_sys_id")
	public String getEngSysId() {
		return engSysId;
	}

	@Column(name="organic_ind")
	public String getOrganicInd() {
		return organicInd;
	}

	@Column(name="sales_nbr")
	public String getSalesNbr() {
		return salesNbr;
	}

	@Column(name="cost")
	public double getCost() {
		return cost;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	@Column(name="status_ind")
	public String getStatusInd() {
		return statusInd;
	}

	@Column(name="exclude_prod")
	public String getExcludeProd() {
		return excludeProd;
	}

	@Column(name="force_prod")
	public String getForceProd() {
		return forceProd;
	}

	@Column(name="mix_restrictions")
	public String getMixRestrictions() {
		return mixRestrictions;
	}

	@Column(name="replace_for")
	public String getReplaceFor() {
		return replaceFor;
	}

	@Column(name="std_alt_clrnt_sys")
	public String getStdAltClrntSys() {
		return stdAltClrntSys;
	}

	@Column(name="vs_alt_clrnt_sys")
	public String getVsAltClrntSys() {
		return vsAltClrntSys;
	}

	@Column(name="hp_alt_clrnt_sys")
	public String getHpAltClrntSys() {
		return hpAltClrntSys;
	}
	
	///////////////  SETTERS /////////////
	
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}

	public void setTintSysId(String tintSysId) {
		this.tintSysId = tintSysId;
	}

	public void setFbSysId(String fbSysId) {
		this.fbSysId = fbSysId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEngSysId(String engSysId) {
		this.engSysId = engSysId;
	}

	public void setOrganicInd(String organicInd) {
		this.organicInd = organicInd;
	}

	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

	public void setStatusInd(String statusInd) {
		this.statusInd = statusInd;
	}

	public void setExcludeProd(String excludeProd) {
		this.excludeProd = excludeProd;
	}

	public void setForceProd(String forceProd) {
		this.forceProd = forceProd;
	}

	public void setMixRestrictions(String mixRestrictions) {
		this.mixRestrictions = mixRestrictions;
	}

	public void setReplaceFor(String replaceFor) {
		this.replaceFor = replaceFor;
	}

	public void setStdAltClrntSys(String stdAltClrntSys) {
		this.stdAltClrntSys = stdAltClrntSys;
	}

	public void setVsAltClrntSys(String vsAltClrntSys) {
		this.vsAltClrntSys = vsAltClrntSys;
	}

	public void setHpAltClrntSys(String hpAltClrntSys) {
		this.hpAltClrntSys = hpAltClrntSys;
	}
	
	
	public static Comparator<CdsClrnt> NameComparator = new Comparator<CdsClrnt>(){
		public int compare(CdsClrnt e1, CdsClrnt e2){
			//ascending by name
			return (e1.getName().compareTo(e2.getName()));
		}
	};

	

}
