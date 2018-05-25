package com.sherwin.shercolor.common.domain;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class OeServiceCdsProdCharzd {


	@SerializedName("prod-nbr")
	private String	 prodNbr;          

	@SerializedName("charzd-grp")
	private String	 charzdGrp;          

	@SerializedName("clrnt-sys")
	private String	 clrntSysId;           

	@SerializedName("rev")
	private String	 rev;                 

	@SerializedName("act-status")
	private String	 actStatus;          

	@SerializedName("lot-nbr")
	private String	 lotNbr;             

	@SerializedName("ifs-full-name")
	private String	 ifsFullName;       

	@SerializedName("cds-adl-fld")
	private String	 cdsAdlFld;         

	@SerializedName("prod-start-date")
	private Date	 prodStartDate;     

	@SerializedName("prod-end-date")
	private Date	 prodEndDate;       

	@SerializedName("factory-fill")
	private double	 factoryFill;          

	@SerializedName("min-fill")
	private double	 minFill;              

	@SerializedName("max-fill")
	private double	 maxFill;              

	@SerializedName("over-fill")
	private double	 overFill;             

	@SerializedName("film-pri")
	private int		 filmPri;            

	@SerializedName("film-sec")
	private int		 filmSec;            

	@SerializedName("bulk-deep")
	private boolean	 bulkDeep;           

	@SerializedName("is-white")
	private boolean	 isWhite;            

	@SerializedName("limit-clrnt")
	private String	 limitClrnt;         

	@SerializedName("exclude-clrnt")
	private String	 excludeClrnt;       

	@SerializedName("vinyl-exclude-clrnt")
	private String	 vinylExcludeClrnt; 

	@SerializedName("color-eng-ver")
	private String	 colorEngVer;       

	@SerializedName("hp-exclude-clrnts")
	private String	 hpExcludeClrnts;

	@SerializedName("form-eff-dt")
	private Date	 formEffDt;

	@SerializedName("largeObjectStorageID")
	private String largeObjectStorageId;

	@SerializedName("dbRowID")
	private String dbRowId;
	
	/////////////  CONSTRUCTORS   ////////////////////////
	public OeServiceCdsProdCharzd() {
		// default
	}
	
	public OeServiceCdsProdCharzd(CdsProdCharzd cdsProdCharzd) {
		// init from CdsProdCharzd object
		if(cdsProdCharzd.getProdNbr()==null) {this.prodNbr = "";} else {this.prodNbr = cdsProdCharzd.getProdNbr(); };          
		if(cdsProdCharzd.getCharzdGrp()==null) {this.charzdGrp = "";} else {this.charzdGrp = cdsProdCharzd.getCharzdGrp(); };
		if(cdsProdCharzd.getClrntSysId()==null) {this.clrntSysId = "";} else {this.clrntSysId = cdsProdCharzd.getClrntSysId(); };
		if(cdsProdCharzd.getRev()==null) {this.rev = "";} else {this.rev = cdsProdCharzd.getRev(); };
		if(cdsProdCharzd.getActStatus()==null) {this.actStatus = "";} else {this.actStatus = cdsProdCharzd.getActStatus(); };
		if(cdsProdCharzd.getLotNbr()==null) {this.lotNbr = "";} else {this.lotNbr = cdsProdCharzd.getLotNbr(); };
		if(cdsProdCharzd.getIfsFullName()==null) {this.ifsFullName = "";} else {this.ifsFullName = cdsProdCharzd.getIfsFullName(); };
		if(cdsProdCharzd.getCdsAdlFld()==null) {this.cdsAdlFld = "";} else {this.cdsAdlFld = cdsProdCharzd.getCdsAdlFld(); };
		if(cdsProdCharzd.getProdStartDate()==null) {this.prodStartDate = new Date();} else {this.prodStartDate = cdsProdCharzd.getProdStartDate(); };
		if(cdsProdCharzd.getProdEndDate()==null) {this.prodEndDate = new Date();} else {this.prodEndDate = cdsProdCharzd.getProdEndDate(); };
		this.factoryFill = cdsProdCharzd.getFactoryFill();
		this.minFill = cdsProdCharzd.getMinFill();
		this.maxFill = cdsProdCharzd.getMaxFill();
		this.overFill = cdsProdCharzd.getOverFill();
		this.filmPri = cdsProdCharzd.getFilmPri();
		this.filmSec = cdsProdCharzd.getFilmSec();
		this.bulkDeep = cdsProdCharzd.getBulkDeep();
		this.isWhite = cdsProdCharzd.getIsWhite();
		if(cdsProdCharzd.getLimitClrnt()==null) {this.limitClrnt = "";} else {this.limitClrnt = cdsProdCharzd.getLimitClrnt(); };
		if(cdsProdCharzd.getExcludeClrnt()==null) {this.excludeClrnt = "";} else {this.excludeClrnt = cdsProdCharzd.getExcludeClrnt(); };
		if(cdsProdCharzd.getVinyl_excludeClrnt()==null) {this.vinylExcludeClrnt = "";} else {this.vinylExcludeClrnt = cdsProdCharzd.getVinyl_excludeClrnt(); };
		if(cdsProdCharzd.getColorEngVer()==null) {this.colorEngVer = "";} else {this.colorEngVer = cdsProdCharzd.getColorEngVer(); };
		if(cdsProdCharzd.getHp_excludeClrnts()==null) {this.hpExcludeClrnts = "";} else {this.hpExcludeClrnts = cdsProdCharzd.getHp_excludeClrnts(); };
		if(cdsProdCharzd.getFormEffDt()==null) {this.formEffDt = new Date();} else {this.formEffDt = cdsProdCharzd.getFormEffDt(); };
		this.largeObjectStorageId = "";
		this.dbRowId = "";

	}
	
	
	////////////// GETTERS  /////////////////////////

	public String getProdNbr() {
		return prodNbr;
	}

	public String getCharzdGrp() {
		return charzdGrp;
	}

	public String getClrntSysId() {
		return clrntSysId;
	}

	public String getRev() {
		return rev;
	}

	public String getActStatus() {
		return actStatus;
	}

	public String getLotNbr() {
		return lotNbr;
	}

	public String getIfsFullName() {
		return ifsFullName;
	}

	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	public Date getProdStartDate() {
		return prodStartDate;
	}

	public Date getProdEndDate() {
		return prodEndDate;
	}

	public double getFactoryFill() {
		return factoryFill;
	}

	public double getMinFill() {
		return minFill;
	}

	public double getMaxFill() {
		return maxFill;
	}

	public double getOverFill() {
		return overFill;
	}

	public int getFilmPri() {
		return filmPri;
	}

	public int getFilmSec() {
		return filmSec;
	}

	public boolean isBulkDeep() {
		return bulkDeep;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public String getLimitClrnt() {
		return limitClrnt;
	}

	public String getExcludeClrnt() {
		return excludeClrnt;
	}

	public String getVinylExcludeClrnt() {
		return vinylExcludeClrnt;
	}

	public String getColorEngVer() {
		return colorEngVer;
	}

	public String getHpExcludeClrnts() {
		return hpExcludeClrnts;
	}

	public Date getFormEffDt() {
		return formEffDt;
	}

	public String getLargeObjectStorageId() {
		return largeObjectStorageId;
	}

	public String getDbRowId() {
		return dbRowId;
	}

	////////////// SETTERS  /////////////////////////

	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}

	public void setCharzdGrp(String charzdGrp) {
		this.charzdGrp = charzdGrp;
	}

	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public void setActStatus(String actStatus) {
		this.actStatus = actStatus;
	}

	public void setLotNbr(String lotNbr) {
		this.lotNbr = lotNbr;
	}

	public void setIfsFullName(String ifsFullName) {
		this.ifsFullName = ifsFullName;
	}

	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

	public void setProdStartDate(Date prodStartDate) {
		this.prodStartDate = prodStartDate;
	}

	public void setProdEndDate(Date prodEndDate) {
		this.prodEndDate = prodEndDate;
	}

	public void setFactoryFill(double factoryFill) {
		this.factoryFill = factoryFill;
	}

	public void setMinFill(double minFill) {
		this.minFill = minFill;
	}

	public void setMaxFill(double maxFill) {
		this.maxFill = maxFill;
	}

	public void setOverFill(double overFill) {
		this.overFill = overFill;
	}

	public void setFilmPri(int filmPri) {
		this.filmPri = filmPri;
	}

	public void setFilmSec(int filmSec) {
		this.filmSec = filmSec;
	}

	public void setBulkDeep(boolean bulkDeep) {
		this.bulkDeep = bulkDeep;
	}

	public void setWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}

	public void setLimitClrnt(String limitClrnt) {
		this.limitClrnt = limitClrnt;
	}

	public void setExcludeClrnt(String excludeClrnt) {
		this.excludeClrnt = excludeClrnt;
	}

	public void setVinylExcludeClrnt(String vinylExcludeClrnt) {
		this.vinylExcludeClrnt = vinylExcludeClrnt;
	}

	public void setColorEngVer(String colorEngVer) {
		this.colorEngVer = colorEngVer;
	}

	public void setHpExcludeClrnts(String hpExcludeClrnts) {
		this.hpExcludeClrnts = hpExcludeClrnts;
	}

	public void setFormEffDt(Date formEffDt) {
		this.formEffDt = formEffDt;
	}

	public void setLargeObjectStorageId(String largeObjectStorageId) {
		this.largeObjectStorageId = largeObjectStorageId;
	}

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}


	

}
