package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CDS_PROD")
@IdClass(CdsProdPK.class)
public class CdsProd {
	private String	salesNbr;        
	private String	intExt;          
	private String	base;             
	private String	klass;            
	private String	quality;         
	private String	composite;        
	private String	finish;           
	private String	charzdGrp;       
	private String	prdGroup;        
	private String	formSrce;        
	private Date	lastUpdate;              
	private String	prepComment;     
	private double	tsf;              
	private String	cdsAdlFld;      
	private boolean	lowPresDisp;    
	private boolean	isChromatic;     
	private String	prodComp;        
	private int	dispSpeed;       
	private String	percentSource;   
	private double	szOverride;      
	private Date	entryDate;
	
	
	@Id
	@Column(name="sales_nbr")
	public String getSalesNbr() {
		return salesNbr;
	}
	
	@Column(name="int_ext")
	public String getIntExt() {
		return intExt;
	}
	
	@Column(name="base")
	public String getBase() {
		return base;
	}
	
	@Column(name="class")
	public String getKlass() {
		return klass;
	}
	
	@Column(name="quality")
	public String getQuality() {
		return quality;
	}
	
	@Column(name="composite")
	public String getComposite() {
		return composite;
	}
	
	@Column(name="finish")
	public String getFinish() {
		return finish;
	}
	
	@Column(name="charzd_grp")
	public String getCharzdGrp() {
		return charzdGrp;
	}
	
	@Column(name="prd_group")
	public String getPrdGroup() {
		return prdGroup;
	}
	
	@Column(name="form_srce")
	public String getFormSrce() {
		return formSrce;
	}
	
	@Column(name="last_update")
	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	@Column(name="prep_comment")
	public String getPrepComment() {
		return prepComment;
	}
	
	@Column(name="tsf")
	public double getTsf() {
		return tsf;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	@Column(name="low_pres_disp")
	public boolean getLowPresDisp() {
		return lowPresDisp;
	}
	
	@Column(name="is_chromatic")
	public boolean getIsChromatic() {
		return isChromatic;
	}
	
	@Column(name="prod_comp")
	public String getProdComp() {
		return prodComp;
	}
	
	@Column(name="disp_speed")
	public int getDispSpeed() {
		return dispSpeed;
	}
	
	@Column(name="percent_source")
	public String getPercentSource() {
		return percentSource;
	}
	
	@Column(name="sz_override")
	public double getSzOverride() {
		return szOverride;
	}
	
	@Column(name="entry_date")
	public Date getEntryDate() {
		return entryDate;
	}
	
	////////////  SETTERS  ////////////////
	
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
	public void setIsChromatic(boolean isChromatic) {
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

	
}
