package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="CDS_COLOR_RGBV1")
@IdClass(CdsColorRgbv1PK.class)
public class CdsColorRgbv1 {

	private String colorComp;
	private String colorId;
	private int rgbRed;
	private int rgbGreen;
	private int rgbBlue;
	private String rgbHex;
	private Double hsvHue;
	private Double hsvSaturation;
	private Double hsvValue;
	private int lrv;
	private String cdsAdlFld;
	
	@Id
	@Column(name="color_comp")
	@NotNull
	public String getColorComp() {
		return colorComp;
	}
	
	@Id
	@Column(name="color_id")
	@NotNull
	public String getColorId() {
		return colorId;
	}
	
	@Column(name="rgb_red")
	public int getRgbRed() {
		return rgbRed;
	}
	
	@Column(name="rgb_green")
	public int getRgbGreen() {
		return rgbGreen;
	}
	
	@Column(name="rgb_blue")
	public int getRgbBlue() {
		return rgbBlue;
	}
	
	@Column(name="rgb_hex")
	public String getRgbHex() {
		return rgbHex;
	}
	
	@Column(name="hsv_hue")
	public Double getHsvHue() {
		return hsvHue;
	}
	
	@Column(name="hsv_saturation")
	public Double getHsvSaturation() {
		return hsvSaturation;
	}
	
	@Column(name="hsvValue")
	public Double getHsvValue() {
		return hsvValue;
	}
	
	@Column(name="lrv")
	public int getLrv() {
		return lrv;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	////////////////  SETTERS  ////////////////////
	
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setRgbRed(int rgbRed) {
		this.rgbRed = rgbRed;
	}
	public void setRgbGreen(int rgbGreen) {
		this.rgbGreen = rgbGreen;
	}
	public void setRgbBlue(int rgbBlue) {
		this.rgbBlue = rgbBlue;
	}
	public void setRgbHex(String rgbHex) {
		this.rgbHex = rgbHex;
	}
	public void setHsvHue(Double hsvHue) {
		this.hsvHue = hsvHue;
	}
	public void setHsvSaturation(Double hsvSaturation) {
		this.hsvSaturation = hsvSaturation;
	}
	public void setHsvValue(Double hsvValue) {
		this.hsvValue = hsvValue;
	}
	public void setLrv(int lrv) {
		this.lrv = lrv;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	
	

}
