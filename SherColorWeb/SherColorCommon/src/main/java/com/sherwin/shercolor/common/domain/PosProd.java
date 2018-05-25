package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="POS_PROD")
@IdClass(PosProdPK.class)
public class PosProd {
	private String	salesNbr;
	private String prodNbr;
	private String descr;
	private String szCd;
	private boolean useTax;
	private String szDescr;
	private Integer vendNbr;
	private boolean onPrl;
	private String rateCd;
	private String dsc;
	private String upc;
	private String descr2;
	private String szDescr2;
	private boolean localBuy;
	private String whseStock;
	private Double minShip;
	private Integer packSz;
	private String mfgId;
	private String vendorNbr;
	private String extFlag;
	private int invenLevelPos;
	private int invenLevelOe;
	private int invenLevelInv;
	private int invenLevelPo;
	private boolean allowPrc;
	private String taxProdNbr;
	private String apoCode;
	private Integer taxGrp;
	private boolean allowInv;
//	private String materialType;
//	private String bomType;
//	private double weight;
//	private String transportHazardUsa;
	
	
	@Id
	@Column(name="sales_nbr")
	public String getSalesNbr() {
		return salesNbr;
	}
	
	@Column(name="prod_nbr")
	public String getProdNbr() {
		return prodNbr;
	}
	
	@Column(name="descr")
	public String getDescr() {
		return descr;
	}
	
	@Column(name="sz_cd")
	public String getSzCd() {
		return szCd;
	}
	
	@Column(name="use_tax")
	public boolean isUseTax() {
		return useTax;
	}
	
	@Column(name="sz_descr")
	public String getSzDescr() {
		return szDescr;
	}
	
	@Column(name="vend_nbr")
	public Integer getVendNbr() {
		return vendNbr;
	}
	
	@Column(name="on_prl")
	public boolean isOnPrl() {
		return onPrl;
	}
	
	@Column(name="rate_cd")
	public String getRateCd() {
		return rateCd;
	}
	
	@Column(name="dsc")
	public String getDsc() {
		return dsc;
	}
	
	@Column(name="upc")
	public String getUpc() {
		return upc;
	}
	
	@Column(name="descr2")
	public String getDescr2() {
		return descr2;
	}
	
	@Column(name="sz_descr2")
	public String getSzDescr2() {
		return szDescr2;
	}
	
	@Column(name="local_buy")
	public boolean isLocalBuy() {
		return localBuy;
	}
	
	@Column(name="whse_stock")
	public String getWhseStock() {
		return whseStock;
	}
	
	@Column(name="min_ship")
	public Double getMinShip() {
		return minShip;
	}
	
	@Column(name="pack_sz")
	public Integer getPackSz() {
		return packSz;
	}
	
	@Column(name="mfg_id")
	public String getMfgId() {
		return mfgId;
	}
	
	@Column(name="vendor_nbr")
	public String getVendorNbr() {
		return vendorNbr;
	}
	
	@Column(name="ext_flag")
	public String getExtFlag() {
		return extFlag;
	}
	
	@Column(name="inven_level_pos")
	public int getInvenLevelPos() {
		return invenLevelPos;
	}
	
	@Column(name="inven_level_oe")
	public int getInvenLevelOe() {
		return invenLevelOe;
	}
	
	@Column(name="inven_level_inv")
	public int getInvenLevelInv() {
		return invenLevelInv;
	}
	
	@Column(name="inven_level_po")
	public int getInvenLevelPo() {
		return invenLevelPo;
	}
	
	@Column(name="allow_prc")
	public boolean isAllowPrc() {
		return allowPrc;
	}
	
	@Column(name="tax_prod_nbr")
	public String getTaxProdNbr() {
		return taxProdNbr;
	}
	
	@Column(name="apo_code")
	public String getApoCode() {
		return apoCode;
	}
	
	@Column(name="tax_grp")
	public Integer getTaxGrp() {
		return taxGrp;
	}
	
	@Column(name="allow_inv")
	public boolean isAllowInv() {
		return allowInv;
	}

	
	
//	@DelimitedField(positionIndex = 30)
//	@Column(name="material_type")
//	public String getMaterialType() {
//		return materialType;
//	}
//	
//	@DelimitedField(positionIndex = 31)
//	@Column(name="bom_type")
//	public String getBomType() {
//		return bomType;
//	}
//	
//	@DelimitedField(positionIndex = 32)
//	@Column(name="weight")
//	public double getWeight() {
//		return weight;
//	}
//	
//	@DelimitedField(positionIndex = 33)
//	@Column(name="transport_hazard_usa")
//	public String getTransportHazardUsa() {
//		return transportHazardUsa;
//	}
	
	//////////////////////////////////////
	
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
//	public void setMaterialType(String materialType) {
//		this.materialType = materialType;
//	}
//	public void setBomType(String bomType) {
//		this.bomType = bomType;
//	}
//	public void setWeight(double weight) {
//		this.weight = weight;
//	}
//	public void setTransportHazardUsa(String transportHazardUsa) {
//		this.transportHazardUsa = transportHazardUsa;
//	}

}
