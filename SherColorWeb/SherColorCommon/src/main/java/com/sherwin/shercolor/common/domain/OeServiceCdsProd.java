package com.sherwin.shercolor.common.domain;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class OeServiceCdsProd {
	
	@SerializedName("sales-nbr")
	private String	salesNbr;        
	
	@SerializedName("int-ext")
	private String	intExt;          
	
	@SerializedName("base")
	private String	base;             
	
	@SerializedName("class")
	private String	klass;            
	
	@SerializedName("quality")
	private String	quality;         
	
	@SerializedName("composite")
	private String	composite;        
	
	@SerializedName("finish")
	private String	finish;           
	
	@SerializedName("charzd-grp")
	private String	charzdGrp;       
	
	@SerializedName("prd-group")
	private String	prdGroup;        
	
	@SerializedName("form-srce")
	private String	formSrce;        
	
	@SerializedName("last-update")
	private Date	lastUpdate;              
	
	@SerializedName("prep-comment")
	private String	prepComment;     
	
	@SerializedName("tsf")
	private double	tsf;              
	
	@SerializedName("cds-adl-fld")
	private String	cdsAdlFld;      
	
	@SerializedName("low-pres-disp")
	private boolean	lowPresDisp;    
	
	@SerializedName("is-chromatic")
	private boolean	isChromatic;     
	
	@SerializedName("prod-comp")
	private String	prodComp;        
	
	@SerializedName("disp-speed")
	private int	dispSpeed;       
	
	@SerializedName("percent-source")
	private String	percentSource;   
	
	@SerializedName("sz-override")
	private double	szOverride;      
	
	@SerializedName("entry-date")
	private Date	entryDate;
	
	@SerializedName("room-list")
	private String roomList;
	
	@SerializedName("params")
	private String params;
	
	@SerializedName("dbRowID")
	private String dbRowId;

	@SerializedName("Prod-Nbr")
	private String prodNbr;
	
	@SerializedName("largeObjectStorageID")
	private String largeObjectStorageId;
	
	///////////////// CONSTRUCTORS  ///////////////////////////////
	public OeServiceCdsProd(){
		// default
	}
	public OeServiceCdsProd(CdsProd cdsProd){
		// init from CdsProd class
		if(cdsProd.getSalesNbr()==null) { this.salesNbr = ""; } else { this.salesNbr = cdsProd.getSalesNbr(); };
		if(cdsProd.getIntExt()==null) { this.intExt = ""; } else { this.intExt = cdsProd.getIntExt(); };
		if(cdsProd.getBase()==null) { this.base = ""; } else { this.base = cdsProd.getBase(); };
		if(cdsProd.getKlass()==null) { this.klass = ""; } else { this.klass = cdsProd.getKlass(); };
		if(cdsProd.getQuality()==null) { this.quality = ""; } else { this.quality = cdsProd.getQuality(); };
		if(cdsProd.getComposite()==null) { this.composite = ""; } else { this.composite = cdsProd.getComposite(); };
		if(cdsProd.getFinish()==null) { this.finish = ""; } else { this.finish = cdsProd.getFinish(); };
		if(cdsProd.getCharzdGrp()==null) { this.charzdGrp = ""; } else { this.charzdGrp = cdsProd.getCharzdGrp(); };
		if(cdsProd.getPrdGroup()==null) { this.prdGroup = ""; } else { this.prdGroup = cdsProd.getPrdGroup(); };
		if(cdsProd.getFormSrce()==null) { this.formSrce = ""; } else { this.formSrce = cdsProd.getFormSrce(); };
		if(cdsProd.getLastUpdate()==null) { this.lastUpdate = new Date(); } else { this.lastUpdate = cdsProd.getLastUpdate(); };
		if(cdsProd.getPrepComment()==null) { this.prepComment = ""; } else { this.prepComment = cdsProd.getPrepComment(); };
		this.tsf = cdsProd.getTsf();
		if(cdsProd.getCdsAdlFld()==null) { this.cdsAdlFld = ""; } else { this.cdsAdlFld = cdsProd.getCdsAdlFld(); };
		this.lowPresDisp = cdsProd.getLowPresDisp();
		this.isChromatic = cdsProd.getIsChromatic();
		if(cdsProd.getProdComp()==null) { this.prodComp = ""; } else { this.prodComp = cdsProd.getProdComp(); };
		this.dispSpeed = cdsProd.getDispSpeed();
		if(this.getPercentSource()==null) { this.percentSource = ""; } else { this.percentSource = cdsProd.getPercentSource(); };
		this.szOverride = cdsProd.getSzOverride();
		if(this.getEntryDate()==null) { this.entryDate = new Date(); } else { this.entryDate = cdsProd.getEntryDate(); };
		this.roomList = "";
		this.params = "";
		this.dbRowId = "";
		this.prodNbr = "";
		this.largeObjectStorageId = "";
	}
	
	/////////////////   GETTERS  //////////////////////////////////

	public String getSalesNbr() {
		return salesNbr;
	}

	public String getIntExt() {
		return intExt;
	}

	public String getBase() {
		return base;
	}

	public String getKlass() {
		return klass;
	}

	public String getQuality() {
		return quality;
	}

	public String getComposite() {
		return composite;
	}

	public String getFinish() {
		return finish;
	}

	public String getCharzdGrp() {
		return charzdGrp;
	}

	public String getPrdGroup() {
		return prdGroup;
	}

	public String getFormSrce() {
		return formSrce;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public String getPrepComment() {
		return prepComment;
	}

	public double getTsf() {
		return tsf;
	}

	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	public boolean isLowPresDisp() {
		return lowPresDisp;
	}

	public boolean isChromatic() {
		return isChromatic;
	}

	public String getProdComp() {
		return prodComp;
	}

	public int getDispSpeed() {
		return dispSpeed;
	}

	public String getPercentSource() {
		return percentSource;
	}

	public double getSzOverride() {
		return szOverride;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public String getRoomList() {
		return roomList;
	}

	public String getParams() {
		return params;
	}

	public String getDbRowId() {
		return dbRowId;
	}

	public String getProdNbr() {
		return prodNbr;
	}

	public String getLargeObjectStorageId() {
		return largeObjectStorageId;
	}

	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}

	public void setIntExt(String intExt) {
		this.intExt = intExt;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public void setKlass(String klass) {
		this.klass = klass;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public void setComposite(String composite) {
		this.composite = composite;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public void setCharzdGrp(String charzdGrp) {
		this.charzdGrp = charzdGrp;
	}

	public void setPrdGroup(String prdGroup) {
		this.prdGroup = prdGroup;
	}

	public void setFormSrce(String formSrce) {
		this.formSrce = formSrce;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setPrepComment(String prepComment) {
		this.prepComment = prepComment;
	}

	public void setTsf(double tsf) {
		this.tsf = tsf;
	}

	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

	public void setLowPresDisp(boolean lowPresDisp) {
		this.lowPresDisp = lowPresDisp;
	}

	public void setChromatic(boolean isChromatic) {
		this.isChromatic = isChromatic;
	}

	public void setProdComp(String prodComp) {
		this.prodComp = prodComp;
	}

	public void setDispSpeed(int dispSpeed) {
		this.dispSpeed = dispSpeed;
	}

	public void setPercentSource(String percentSource) {
		this.percentSource = percentSource;
	}

	public void setSzOverride(double szOverride) {
		this.szOverride = szOverride;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public void setRoomList(String roomList) {
		this.roomList = roomList;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}

	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}

	public void setLargeObjectStorageId(String largeObjectStorageId) {
		this.largeObjectStorageId = largeObjectStorageId;
	}


	

}
