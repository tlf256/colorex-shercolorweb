package com.sherwin.shercolor.colormath.domain;

import java.math.BigDecimal;





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


	public String getIlluminant() {
		return illuminant;
	}

	public String getIlluminant_desc() {
		return illuminant_desc;
	}
	

	public int getObserver() {
		return observer;
	}
	

	public BigDecimal getArxr() {
		return arxr;
	}
	

	public BigDecimal getAgxg() {
		return agxg;
	}
	

	public BigDecimal getAbxb() {
		return abxb;
	}
	

	public BigDecimal getAryr() {
		return aryr;
	}
	

	public BigDecimal getAgyg() {
		return agyg;
	}
	

	public BigDecimal getAbyb() {
		return abyb;
	}
	

	public BigDecimal getArzr() {
		return arzr;
	}
	

	public BigDecimal getAgzg() {
		return agzg;
	}
	

	public BigDecimal getAbzb() {
		return abzb;
	}
	

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
