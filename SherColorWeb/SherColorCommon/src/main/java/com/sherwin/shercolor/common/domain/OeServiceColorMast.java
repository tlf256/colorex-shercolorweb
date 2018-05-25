package com.sherwin.shercolor.common.domain;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class OeServiceColorMast {


	@SerializedName("color-comp")
	private String colorComp;

	@SerializedName("color-id")
	private String colorId;

	@SerializedName("color-name")
	private String colorName;

	@SerializedName("palette")
	private String palette;

	@SerializedName("entry-date")
	private Date entryDate;

	@SerializedName("last-update")
	private Date lastUpdate;

	@SerializedName("force-clrnt")
	private String forceClrnt;

	@SerializedName("force-clrnt-sys")
	private String forceClrntSys;

	@SerializedName("cds-adl-fld")
	private String cdsAdlFld;

	@SerializedName("swatch-id")
	private String swatchId;

	@SerializedName("color-comp-prt")
	private String colorCompPrt;

	@SerializedName("primer-id")
	private String primerId;

	@SerializedName("is-vinyl-siding")
	private boolean isVinylSiding;

	@SerializedName("force-prod")
	private String forceProd;

	@SerializedName("exclude-prod")
	private String excludeProd;

	@SerializedName("is-hp")
	private boolean isHp;

	@SerializedName("loc-id")
	private String locId;

	@SerializedName("exp-date")
	private Date expDate;

	@SerializedName("xref-id")
	private String xrefId;

	@SerializedName("xref-comp")
	private String xrefComp;

	@SerializedName("curve-key")
	private int curveKey;

	@SerializedName("exclude-vinyl")
	private boolean excludeVinyl;

	@SerializedName("dbRowiD")
	private String dbRowId;

	////////////  CONSTRUCTORS  /////////////////////
	public OeServiceColorMast(){
		// default
	}
	
	public OeServiceColorMast(CdsColorMast cdsColorMast){
		this.colorComp = cdsColorMast.getColorComp();
		this.colorId = cdsColorMast.getColorId();
		this.colorName = cdsColorMast.getColorName();
		this.palette = cdsColorMast.getPalette();
		this.entryDate = cdsColorMast.getEntryDate();
		this.lastUpdate = cdsColorMast.getLastUpdate();
		this.forceClrnt = cdsColorMast.getForceClrnt();
		this.forceClrntSys = cdsColorMast.getForceClrntSys();
		this.cdsAdlFld = cdsColorMast.getCdsAdlFld();
		this.swatchId = cdsColorMast.getSwatchId();
		this.colorCompPrt = cdsColorMast.getColorCompPrt();
		this.primerId = cdsColorMast.getPrimerId();
		this.isVinylSiding = cdsColorMast.getIsVinylSiding();
		this.forceProd = cdsColorMast.getForceProd();
		this.excludeProd = cdsColorMast.getExcludeProd();
		this.isHp = cdsColorMast.getIsHp();
		this.locId = cdsColorMast.getLocId();
		this.expDate = cdsColorMast.getExpDate();
		this.xrefId = cdsColorMast.getXrefId();
		this.xrefComp = cdsColorMast.getXrefComp();
		this.curveKey = cdsColorMast.getCurveKey();
		this.excludeVinyl = false;
		this.dbRowId = "";
		
	}

	////////////////////  GETTERS/SETTERS  //////////////////////////
	
	public String getColorComp() {
		return colorComp;
	}

	public String getColorId() {
		return colorId;
	}

	public String getColorName() {
		return colorName;
	}

	public String getPalette() {
		return palette;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public String getForceClrnt() {
		return forceClrnt;
	}

	public String getForceClrntSys() {
		return forceClrntSys;
	}

	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	public String getSwatchId() {
		return swatchId;
	}

	public String getColorCompPrt() {
		return colorCompPrt;
	}

	public String getPrimerId() {
		return primerId;
	}

	public boolean isVinylSiding() {
		return isVinylSiding;
	}

	public String getForceProd() {
		return forceProd;
	}

	public String getExcludeProd() {
		return excludeProd;
	}

	public boolean isHp() {
		return isHp;
	}

	public String getLocId() {
		return locId;
	}

	public Date getExpDate() {
		return expDate;
	}

	public String getXrefId() {
		return xrefId;
	}

	public String getXrefComp() {
		return xrefComp;
	}

	public int getCurveKey() {
		return curveKey;
	}

	public boolean isExcludeVinyl() {
		return excludeVinyl;
	}

	public String getDbRowId() {
		return dbRowId;
	}

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
		this.cdsAdlFld = cdsAdlFld;
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

	public void setVinylSiding(boolean isVinylSiding) {
		this.isVinylSiding = isVinylSiding;
	}

	public void setForceProd(String forceProd) {
		this.forceProd = forceProd;
	}

	public void setExcludeProd(String excludeProd) {
		this.excludeProd = excludeProd;
	}

	public void setHp(boolean isHp) {
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

	public void setCurveKey(int curveKey) {
		this.curveKey = curveKey;
	}

	public void setExcludeVinyl(boolean excludeVinyl) {
		this.excludeVinyl = excludeVinyl;
	}

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}

}
