package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_CLRNT_LIST")
@IdClass(CdsClrntListPK.class)
public class CdsClrntList {
	private String clrntListId;
	private String clrntSysId;
	private String tintSysId;
	private String descr;
	private String cdsAdlFld;
	
	@Id
	@Column(name="clrnt_list_id")
	@NotNull
	public String getClrntListId() {
		return clrntListId;
	}
	
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
	
	@Column(name="descr")
	public String getDescr() {
		return descr;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	

	public void setClrntListId(String clrntListId) {
		this.clrntListId = clrntListId;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setTintSysId(String tintSysId) {
		this.tintSysId = tintSysId;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

}
