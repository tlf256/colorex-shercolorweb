package com.sherwin.shercolor.common.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OeServiceColorDataSet {

	@SerializedName("ColorMast")
	private List<OeServiceColorMast> colorMastList;
	
	@SerializedName("ColorStand")
	private List<OeServiceColorStand> colorStandList;
	
	@SerializedName("ColorBase")
	private List<OeServiceColorBase> colorBaseList;
	

	public List<OeServiceColorMast> getColorMastList() {
		return colorMastList;
	}

	public List<OeServiceColorStand> getColorStandList() {
		return colorStandList;
	}

	public List<OeServiceColorBase> getColorBaseList() {
		return colorBaseList;
	}

	public void setColorMastList(List<OeServiceColorMast> colorMastList) {
		this.colorMastList = colorMastList;
	}

	public void setColorStandList(List<OeServiceColorStand> colorStandList) {
		this.colorStandList = colorStandList;
	}

	public void setColorBaseList(List<OeServiceColorBase> colorBaseList) {
		this.colorBaseList = colorBaseList;
	}
	
	
	
}
