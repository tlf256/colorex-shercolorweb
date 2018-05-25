package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_MISC_CODES")
@IdClass(CdsMiscCodesPK.class)
public class CdsMiscCodes {
	private String miscType;
	private String miscCode;
	private String miscName;
	private String cdsAdlFld;
	
	@Id
	@Column(name="misc_type")
	@NotNull
	public String getMiscType() {
		return miscType;
	}

	@Id
	@Column(name="misc_code")
	@NotNull
	public String getMiscCode() {
		return miscCode;
	}
	
	@Column(name="misc_name")
	public String getMiscName() {
		return miscName;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	///////////////// SETTERS  ////////////////////////
	
	public void setMiscType(String miscType) {
		this.miscType = miscType;
	}
	public void setMiscCode(String miscCode) {
		this.miscCode = miscCode;
	}
	public void setMiscName(String miscName) {
		this.miscName = miscName;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

}
