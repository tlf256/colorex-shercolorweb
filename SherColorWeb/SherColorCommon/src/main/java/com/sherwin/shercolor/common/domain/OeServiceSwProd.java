package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeServiceSwProd {

	@SerializedName("sales-nbr")
	private String salesNbr;

	@SerializedName("prod-nbr")
	private String prodNbr;

	@SerializedName("descr")
	private String descr;

	@SerializedName("sz-cd")
	private String szCd;

	@SerializedName("use-tax")
	private boolean useTax;

	@SerializedName("sz-descr")
	private String szDescr;

	@SerializedName("vend-nbr")
	private Integer vendNbr;

	@SerializedName("on-prl")
	private boolean onPrl;

	@SerializedName("rate-cd")
	private String rateCd;

	@SerializedName("dsc")
	private String dsc;

	@SerializedName("upc")
	private String upc;

	@SerializedName("descr2")
	private String descr2;

	@SerializedName("sz-descr2")
	private String szDescr2;

	@SerializedName("local-buy")
	private boolean localBuy;

	@SerializedName("whse-stock")
	private String whseStock;

	@SerializedName("min-ship")
	private Double minShip;

	@SerializedName("pack-sz")
	private Integer packSz;

	@SerializedName("mfg-id")
	private String mfgId;

	@SerializedName("vendor-nbr")
	private String vendorNbr;

	@SerializedName("ext-flag")
	private String extFlag;

	@SerializedName("inven-level-pos")
	private int invenLevelPos;

	@SerializedName("inven-level-oe")
	private int invenLevelOe;

	@SerializedName("inven-level-inv")
	private int invenLevelInv;

	@SerializedName("inven-level-po")
	private int invenLevelPo;

	@SerializedName("allow-prc")
	private boolean allowPrc;

	@SerializedName("tax-prod-nbr")
	private String taxProdNbr;

	@SerializedName("apo-code")
	private String apoCode;

	@SerializedName("tax-grp")
	private Integer taxGrp;

	@SerializedName("allow-inv")
	private boolean allowInv;

	@SerializedName("material-type")
	private String materialType;

	@SerializedName("bom-type")
	private String bomType;

	@SerializedName("weight")
	private double weight;

	@SerializedName("tranport-hazard-usa")
	private String transportHazardUsa;
	
	@SerializedName("dbRowID")
	private String dbRowId;


	////////////// CONTRUCTORS  ///////////////
	public OeServiceSwProd(){
		
		// default
	}
	
	public OeServiceSwProd(PosProd posProd){
		if(posProd.getSalesNbr()==null) { this.salesNbr = ""; } else { this.salesNbr = posProd.getSalesNbr(); };
		if(posProd.getProdNbr()==null) { this.prodNbr = ""; } else { this.prodNbr = posProd.getProdNbr(); };
		if(posProd.getDescr()==null) { this.descr = ""; } else { this.descr = posProd.getDescr(); };
		if(posProd.getSzCd()==null) { this.szCd = ""; } else { this.szCd = posProd.getSzCd(); };
		this.useTax = posProd.isUseTax();
		if(posProd.getSzDescr()==null) { this.szDescr = ""; } else { this.szDescr = posProd.getSzDescr(); };
		if(posProd.getVendNbr()==null) { this.vendNbr = 0; } else { this.vendNbr = posProd.getVendNbr(); };
		this.onPrl = posProd.isOnPrl();
		if(posProd.getRateCd()==null) { this.rateCd = ""; } else { this.rateCd = posProd.getRateCd(); };
		if(posProd.getDsc()==null) { this.dsc = ""; } else { this.dsc = posProd.getDsc(); };
		if(posProd.getUpc()==null) { this.upc = ""; } else { this.upc = posProd.getUpc(); };
		if(posProd.getDescr2()==null) { this.descr2 = ""; } else { this.descr2 = posProd.getDescr2(); };
		if(posProd.getSzDescr2()==null) { this.szDescr2 = ""; } else { this.szDescr2 = posProd.getSzDescr2(); };
		this.localBuy = posProd.isLocalBuy();
		if(posProd.getWhseStock()==null) { this.whseStock = ""; } else { this.whseStock = posProd.getWhseStock(); };
		this.minShip = posProd.getMinShip();
		this.packSz = posProd.getPackSz();
		if(posProd.getMfgId()==null) { this.mfgId = ""; } else { this.mfgId = posProd.getMfgId(); };
		if(posProd.getVendorNbr()==null) { this.vendorNbr = ""; } else { this.vendorNbr = posProd.getVendorNbr(); };
		if(posProd.getExtFlag()==null) { this.extFlag = ""; } else { this.extFlag = posProd.getExtFlag(); };
		this.invenLevelPos = posProd.getInvenLevelPos();
		this.invenLevelOe = posProd.getInvenLevelOe();
		this.invenLevelInv = posProd.getInvenLevelInv();
		this.invenLevelPo = posProd.getInvenLevelPo();
		this.allowPrc = posProd.isAllowPrc();
		if(posProd.getTaxProdNbr()==null) { this.taxProdNbr = ""; } else { this.taxProdNbr = posProd.getTaxProdNbr(); };
		if(posProd.getApoCode()==null) { this.apoCode = ""; } else { this.apoCode = posProd.getApoCode(); };
		this.taxGrp = posProd.getTaxGrp();
		this.allowInv = posProd.isAllowInv();
		this.materialType = "";
		this.bomType = "";
		this.weight = 0d;
		this.transportHazardUsa = "";
		this.dbRowId = "";
		
	}
	
	//////////////  GETTERS  //////////////////
	
	public String getSalesNbr() {
		return salesNbr;
	}

	public String getProdNbr() {
		return prodNbr;
	}

	public String getDescr() {
		return descr;
	}

	public String getSzCd() {
		return szCd;
	}

	public boolean isUseTax() {
		return useTax;
	}

	public String getSzDescr() {
		return szDescr;
	}

	public Integer getVendNbr() {
		return vendNbr;
	}

	public boolean isOnPrl() {
		return onPrl;
	}

	public String getRateCd() {
		return rateCd;
	}

	public String getDsc() {
		return dsc;
	}

	public String getUpc() {
		return upc;
	}

	public String getDescr2() {
		return descr2;
	}

	public String getSzDescr2() {
		return szDescr2;
	}

	public boolean isLocalBuy() {
		return localBuy;
	}

	public String getWhseStock() {
		return whseStock;
	}

	public Double getMinShip() {
		return minShip;
	}

	public Integer getPackSz() {
		return packSz;
	}

	public String getMfgId() {
		return mfgId;
	}

	public String getVendorNbr() {
		return vendorNbr;
	}

	public String getExtFlag() {
		return extFlag;
	}

	public int getInvenLevelPos() {
		return invenLevelPos;
	}

	public int getInvenLevelOe() {
		return invenLevelOe;
	}

	public int getInvenLevelInv() {
		return invenLevelInv;
	}

	public int getInvenLevelPo() {
		return invenLevelPo;
	}

	public boolean isAllowPrc() {
		return allowPrc;
	}

	public String getTaxProdNbr() {
		return taxProdNbr;
	}

	public String getApoCode() {
		return apoCode;
	}

	public Integer getTaxGrp() {
		return taxGrp;
	}

	public boolean isAllowInv() {
		return allowInv;
	}

	public String getMaterialType() {
		return materialType;
	}

	public String getBomType() {
		return bomType;
	}

	public double getWeight() {
		return weight;
	}

	public String getTransportHazardUsa() {
		return transportHazardUsa;
	}

	public String getDbRowId() {
		return dbRowId;
	}

	
	//////////////  SETTERS  //////////////////
	
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}

	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public void setSzCd(String szCd) {
		this.szCd = szCd;
	}

	public void setUseTax(boolean useTax) {
		this.useTax = useTax;
	}

	public void setSzDescr(String szDescr) {
		this.szDescr = szDescr;
	}

	public void setVendNbr(Integer vendNbr) {
		this.vendNbr = vendNbr;
	}

	public void setOnPrl(boolean onPrl) {
		this.onPrl = onPrl;
	}

	public void setRateCd(String rateCd) {
		this.rateCd = rateCd;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public void setDescr2(String descr2) {
		this.descr2 = descr2;
	}

	public void setSzDescr2(String szDescr2) {
		this.szDescr2 = szDescr2;
	}

	public void setLocalBuy(boolean localBuy) {
		this.localBuy = localBuy;
	}

	public void setWhseStock(String whseStock) {
		this.whseStock = whseStock;
	}

	public void setMinShip(Double minShip) {
		this.minShip = minShip;
	}

	public void setPackSz(Integer packSz) {
		this.packSz = packSz;
	}

	public void setMfgId(String mfgId) {
		this.mfgId = mfgId;
	}

	public void setVendorNbr(String vendorNbr) {
		this.vendorNbr = vendorNbr;
	}

	public void setExtFlag(String extFlag) {
		this.extFlag = extFlag;
	}

	public void setInvenLevelPos(int invenLevelPos) {
		this.invenLevelPos = invenLevelPos;
	}

	public void setInvenLevelOe(int invenLevelOe) {
		this.invenLevelOe = invenLevelOe;
	}

	public void setInvenLevelInv(int invenLevelInv) {
		this.invenLevelInv = invenLevelInv;
	}

	public void setInvenLevelPo(int invenLevelPo) {
		this.invenLevelPo = invenLevelPo;
	}

	public void setAllowPrc(boolean allowPrc) {
		this.allowPrc = allowPrc;
	}

	public void setTaxProdNbr(String taxProdNbr) {
		this.taxProdNbr = taxProdNbr;
	}

	public void setApoCode(String apoCode) {
		this.apoCode = apoCode;
	}

	public void setTaxGrp(Integer taxGrp) {
		this.taxGrp = taxGrp;
	}

	public void setAllowInv(boolean allowInv) {
		this.allowInv = allowInv;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public void setBomType(String bomType) {
		this.bomType = bomType;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setTransportHazardUsa(String transportHazardUsa) {
		this.transportHazardUsa = transportHazardUsa;
	}

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}

	
	

}
