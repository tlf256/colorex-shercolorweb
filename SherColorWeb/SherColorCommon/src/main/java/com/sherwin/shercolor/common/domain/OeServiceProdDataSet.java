package com.sherwin.shercolor.common.domain;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OeServiceProdDataSet {

	@SerializedName("swProd")
	private List<OeServiceSwProd> swProd;

	@SerializedName("cdsProd")
	private List<OeServiceCdsProd> cdsProd;

	@SerializedName("prodCharzd")
	private List<OeServiceCdsProdCharzd> cdsProdCharzd;

	@SerializedName("objStorage")
	private List<OeServiceLargeObjectStorage> objStorage;
	
	public List<OeServiceSwProd> getSwProd() {
		return swProd;
	}
	public List<OeServiceCdsProd> getCdsProd() {
		return cdsProd;
	}
	public List<OeServiceCdsProdCharzd> getCdsProdCharzd() {
		return cdsProdCharzd;
	}
	public List<OeServiceLargeObjectStorage> getObjStorage() {
		return objStorage;
	}
	public void setSwProd(List<OeServiceSwProd> swProd) {
		this.swProd = swProd;
	}
	public void setCdsProd(List<OeServiceCdsProd> cdsProd) {
		this.cdsProd = cdsProd;
	}
	public void setCdsProdCharzd(List<OeServiceCdsProdCharzd> cdsProdCharzd) {
		this.cdsProdCharzd = cdsProdCharzd;
	}
	public void setObjStorage(List<OeServiceLargeObjectStorage> objStorage) {
		this.objStorage = objStorage;
	}
	
	
}
