package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CDS_PROD_CHARZD")
@IdClass(CdsProdCharzdPK.class)
public class CdsProdCharzd {
	
	private String	 prodNbr;          
	private String	 charzdGrp;          
	private String	 clrntSysId;           
	private String	 rev;                 
	private String	 actStatus;          
	private String	 lotNbr;             
	private String	 ifsFullName;       
	private String	 cdsAdlFld;         
	private Date	 prodStartDate;     
	private Date	 prodEndDate;       
	private double	 factoryFill;          
	private double	 minFill;              
	private double	 maxFill;              
	private double	 overFill;             
	private int		 filmPri;            
	private int		 filmSec;            
	private boolean	 bulkDeep;           
	private boolean	 isWhite;            
	private String	 limitClrnt;         
	private String	 excludeClrnt;       
	private String	 vinyl_excludeClrnt; 
	private String	 colorEngVer;       
	private String	 hp_excludeClrnts;
	private Date	 formEffDt;
	
	
	@Id
	@Column(name="prod_nbr")
	public String getProdNbr() {
		return prodNbr;
	}
	
	@Column(name="charzd_grp")
	public String getCharzdGrp() {
		return charzdGrp;
	}
	
	@Id
	@Column(name="clrnt_sys")
	public String getClrntSysId() {
		return clrntSysId;
	}
	
	@Column(name="rev")
	public String getRev() {
		return rev;
	}
	
	@Column(name="act_status")
	public String getActStatus() {
		return actStatus;
	}
	
	@Column(name="lot_nbr")
	public String getLotNbr() {
		return lotNbr;
	}
	
	@Column(name="ifs_full_name")
	public String getIfsFullName() {
		return ifsFullName;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	@Column(name="prod_start_date")
	public Date getProdStartDate() {
		return prodStartDate;
	}
	
	@Column(name="prod_end_date")
	public Date getProdEndDate() {
		return prodEndDate;
	}
	
	@Column(name="factory_fill")
	public double getFactoryFill() {
		return factoryFill;
	}
	
	@Column(name="min_fill")
	public double getMinFill() {
		return minFill;
	}
	
	@Column(name="max_fill")
	public double getMaxFill() {
		return maxFill;
	}
	
	@Column(name="over_fill")
	public double getOverFill() {
		return overFill;
	}
	
	@Column(name="film_pri")
	public int getFilmPri() {
		return filmPri;
	}
	
	@Column(name="film_sec")
	public int getFilmSec() {
		return filmSec;
	}
	
	@Column(name="bulk_deep")
	public boolean getBulkDeep() {
		return bulkDeep;
	}
	
	@Column(name="is_white")
	public boolean getIsWhite() {
		return isWhite;
	}
	
	@Column(name="limit_clrnt")
	public String getLimitClrnt() {
		return limitClrnt;
	}
	
	@Column(name="exclude_clrnt")
	public String getExcludeClrnt() {
		return excludeClrnt;
	}
	
	@Column(name="vinyl_exclude_clrnt")
	public String getVinyl_excludeClrnt() {
		return vinyl_excludeClrnt;
	}
	
	@Column(name="color_eng_ver")
	public String getColorEngVer() {
		return colorEngVer;
	}
	
	@Column(name="hp_exclude_clrnts")
	public String getHp_excludeClrnts() {
		return hp_excludeClrnts;
	}
	
	@Column(name="form_eff_dt")
	public Date getFormEffDt() {
		return formEffDt;
	}

	/////////  SETTERS  ////////////////
	
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
	public void setIsWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}
	public void setLimitClrnt(String limitClrnt) {
		this.limitClrnt = limitClrnt;
	}
	public void setExcludeClrnt(String excludeClrnt) {
		this.excludeClrnt = excludeClrnt;
	}
	public void setVinyl_excludeClrnt(String vinyl_excludeClrnt) {
		this.vinyl_excludeClrnt = vinyl_excludeClrnt;
	}
	public void setColorEngVer(String colorEngVer) {
		this.colorEngVer = colorEngVer;
	}
	public void setHp_excludeClrnts(String hp_excludeClrnts) {
		this.hp_excludeClrnts = hp_excludeClrnts;
	}   
	public void setFormEffDt(Date formEffDt) {
		this.formEffDt = formEffDt;
	}



}
