package com.sherwin.shercolor.common.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_COLOR_XYZ_RGB_CONVERT")
@IdClass(CdsColorXyzRgbConvertPK.class)
public class CdsColorXyzRgbConvert {
	private String illuminant;
	private String illuminant_desc;
	private int observer;
	private BigDecimal arxr;
	private BigDecimal agxg;
	private BigDecimal abxb;
	private BigDecimal aryr;
	private BigDecimal agyg;
	private BigDecimal abyb;
	private BigDecimal arzr;
	private BigDecimal agzg;
	private BigDecimal abzb;
	private String cds_adl_fld;

	@Id
	@Column(name="illuminant")
	@NotNull
	public String getIlluminant() {
		return illuminant;
	}
	
	@Column(name="illuminant_desc")
	public String getIlluminant_desc() {
		return illuminant_desc;
	}
	
	@Id
	@Column(name="observer")
	@NotNull
	public int getObserver() {
		return observer;
	}
	
	@Column(name="arxr")
	public BigDecimal getArxr() {
		return arxr;
	}
	
	@Column(name="agxg")
	public BigDecimal getAgxg() {
		return agxg;
	}
	
	@Column(name="abxb")
	public BigDecimal getAbxb() {
		return abxb;
	}
	
	@Column(name="aryr")
	public BigDecimal getAryr() {
		return aryr;
	}
	
	@Column(name="agyg")
	public BigDecimal getAgyg() {
		return agyg;
	}
	
	@Column(name="abyb")
	public BigDecimal getAbyb() {
		return abyb;
	}
	
	@Column(name="arzr")
	public BigDecimal getArzr() {
		return arzr;
	}
	
	@Column(name="agzg")
	public BigDecimal getAgzg() {
		return agzg;
	}
	
	@Column(name="abzb")
	public BigDecimal getAbzb() {
		return abzb;
	}
	
	@Column(name="cds_adl_fld")
	public String getCds_adl_fld() {
		return cds_adl_fld;
	}
	
	/////////////////  SETTERS  ////////////////////
	
	public void setIlluminant(String illuminant) {
		this.illuminant = illuminant;
	}
	public void setIlluminant_desc(String illuminant_desc) {
		this.illuminant_desc = illuminant_desc;
	}
	public void setObserver(int observer) {
		this.observer = observer;
	}
	public void setArxr(BigDecimal arxr) {
		this.arxr = arxr;
	}
	public void setAgxg(BigDecimal agxg) {
		this.agxg = agxg;
	}
	public void setAbxb(BigDecimal abxb) {
		this.abxb = abxb;
	}
	public void setAryr(BigDecimal aryr) {
		this.aryr = aryr;
	}
	public void setAgyg(BigDecimal agyg) {
		this.agyg = agyg;
	}
	public void setAbyb(BigDecimal abyb) {
		this.abyb = abyb;
	}
	public void setArzr(BigDecimal arzr) {
		this.arzr = arzr;
	}
	public void setAgzg(BigDecimal agzg) {
		this.agzg = agzg;
	}
	public void setAbzb(BigDecimal abzb) {
		this.abzb = abzb;
	}
	public void setCds_adl_fld(String cds_adl_fld) {
		this.cds_adl_fld = cds_adl_fld;
	}                    

}
