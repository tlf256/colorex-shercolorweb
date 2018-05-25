package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeServiceColorBase {


	@SerializedName("color-comp")
	private String colorComp;

	@SerializedName("color-id")
	private String colorId;

	@SerializedName("ie-flag")
	private String ieFlag;   

	@SerializedName("seq-nbr")
	private int seqNbr;

	@SerializedName("base")
	private String base;	   

	@SerializedName("cds-adl-fld")
	private String cdsAdlFld;

	@SerializedName("prod-comp")
	private String prodComp;

	@SerializedName("suppress-de")
	private boolean suppressDe;

	@SerializedName("dbRowiD")
	private String dbRowId;

	/////////  CONTRUCTORS  /////////////////
	public OeServiceColorBase(){
		// default
	}
	
	public OeServiceColorBase(CdsColorBase cdsColorBase){
		this.colorComp = cdsColorBase.getColorComp(); 
		this.colorId = cdsColorBase.getColorId();
		this.ieFlag = cdsColorBase.getIeFlag();
		this.seqNbr = cdsColorBase.getSeqNbr();
		this.base = cdsColorBase.getBase();
		this.cdsAdlFld = cdsColorBase.getCdsAdlFld();
		this.prodComp = cdsColorBase.getProdComp();
		this.suppressDe = cdsColorBase.isSuppressDe();
	}

	/////////////////  GETTERS/SETTERS  ///////////////
	
	public String getColorComp() {
		return colorComp;
	}

	public String getColorId() {
		return colorId;
	}

	public String getIeFlag() {
		return ieFlag;
	}

	public int getSeqNbr() {
		return seqNbr;
	}

	public String getBase() {
		return base;
	}

	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	public String getProdComp() {
		return prodComp;
	}

	public boolean isSuppressDe() {
		return suppressDe;
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

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}
	
	
}
