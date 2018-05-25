package com.sherwin.shercolor.common.domain;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_COLOR_MAST")
@IdClass(CdsColorMastPK.class)
public class CdsColorMast {
	private String colorComp;
	private String colorId;
	private String colorName;
	private String palette;
	private Date entryDate;
	private Date lastUpdate;
	private String forceClrnt;
	private String forceClrntSys;
	private String CdsAdlFld;
	private String swatchId;
	private String colorCompPrt;
	private String primerId;
	private boolean isVinylSiding;
	private String forceProd;
	private String excludeProd;
	private boolean isHp;
	private String locId;
	private Date expDate;
	private String xrefId;
	private String xrefComp;
	private Integer curveKey;
	// not yet.. private boolean excludeVinyl;
	
	
	@Id
	@Column(name="color_comp")
	@NotNull
	public String getColorComp() {
		return colorComp;
	}
	
	@Id
	@Column(name="color_id")
	@NotNull
	public String getColorId() {
		return colorId;
	}

	@Column(name="color_name")
	public String getColorName() {
		return colorName;
	}

	@Column(name="palette")
	public String getPalette() {
		return palette;
	}

	@Column(name="entry_date")
	public Date getEntryDate() {
		return entryDate;
	}

	@Column(name="last_update")
	public Date getLastUpdate() {
		return lastUpdate;
	}

	@Column(name="force_clrnt")
	public String getForceClrnt() {
		return forceClrnt;
	}

	@Column(name="force_clrnt_sys")
	public String getForceClrntSys() {
		return forceClrntSys;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return CdsAdlFld;
	}

	@Column(name="swatch_id")
	public String getSwatchId() {
		return swatchId;
	}

	@Column(name="color_comp_prt")
	public String getColorCompPrt() {
		return colorCompPrt;
	}

	@Column(name="primer_id")
	public String getPrimerId() {
		return primerId;
	}

	@Column(name="is_vinyl_siding")
	public boolean getIsVinylSiding() {
		return isVinylSiding;
	}

	@Column(name="force_prod")
	public String getForceProd() {
		return forceProd;
	}

	@Column(name="exclude_prod")
	public String getExcludeProd() {
		return excludeProd;
	}

	@Column(name="is_hp")
	public boolean getIsHp() {
		return isHp;
	}

	@Column(name="loc_id")
	public String getLocId() {
		return locId;
	}

	@Column(name="exp_date")
	public Date getExpDate() {
		return expDate;
	}

	@Column(name="xref_id")
	public String getXrefId() {
		return xrefId;
	}

	@Column(name="xref_comp")
	public String getXrefComp() {
		return xrefComp;
	}

	@Column(name="curve_key")
	public Integer getCurveKey() {
		return curveKey;
	}

	///////////////  SETTERS   ///////////////////
	
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}	

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public void setPalette(String palette) {
		this.palette = palette;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public void setForceClrnt(String forceClrnt) {
		this.forceClrnt = forceClrnt;
	}

	public void setForceClrntSys(String forceClrntSys) {
		this.forceClrntSys = forceClrntSys;
	}

	public void setCdsAdlFld(String cdsAdlFld) {
		CdsAdlFld = cdsAdlFld;
	}

	public void setSwatchId(String swatchId) {
		this.swatchId = swatchId;
	}

	public void setColorCompPrt(String colorCompPrt) {
		this.colorCompPrt = colorCompPrt;
	}

	public void setPrimerId(String primerId) {
		this.primerId = primerId;
	}

	public void setIsVinylSiding(boolean isVinylSiding) {
		this.isVinylSiding = isVinylSiding;
	}

	public void setForceProd(String forceProd) {
		this.forceProd = forceProd;
	}

	public void setExcludeProd(String excludeProd) {
		this.excludeProd = excludeProd;
	}

	public void setIsHp(boolean isHp) {
		this.isHp = isHp;
	}
	
	public void setLocId(String locId) {
		this.locId = locId;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public void setXrefId(String xrefId) {
		this.xrefId = xrefId;
	}

	public void setXrefComp(String xrefComp) {
		this.xrefComp = xrefComp;
	}

	public void setCurveKey(Integer curveKey) {
		this.curveKey = curveKey;
	}

	

}
