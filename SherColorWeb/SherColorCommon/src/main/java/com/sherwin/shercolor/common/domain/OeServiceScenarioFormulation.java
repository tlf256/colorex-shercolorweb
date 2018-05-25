package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeServiceScenarioFormulation {

	@SerializedName("dsFormulation")
	OeFormInput oeFormInput;
	
	@SerializedName("dsProd")
	OeServiceProdDataSetWrapper dsProdWrapper;
	
	@SerializedName("dsColor")
	OeServiceColorDataSetWrapper dsColorWrapper;
	
	@SerializedName("dsColorantSys")
	OeServiceColorantDataSetWrapper dsColorantWrapper;

	public OeFormInput getOeFormInput() {
		return oeFormInput;
	}

	public OeServiceProdDataSetWrapper getDsProdWrapper() {
		return dsProdWrapper;
	}

	public OeServiceColorDataSetWrapper getDsColorWrapper() {
		return dsColorWrapper;
	}

	public void setOeFormInput(OeFormInput oeFormInput) {
		this.oeFormInput = oeFormInput;
	}

	public void setDsProdWrapper(OeServiceProdDataSetWrapper dsProdWrapper) {
		this.dsProdWrapper = dsProdWrapper;
	}

	public void setDsColorWrapper(OeServiceColorDataSetWrapper dsColorWrapper) {
		this.dsColorWrapper = dsColorWrapper;
	}

	public OeServiceColorantDataSetWrapper getDsColorantWrapper() {
		return dsColorantWrapper;
	}

	public void setDsColorantWrapper(OeServiceColorantDataSetWrapper dsColorantWrapper) {
		this.dsColorantWrapper = dsColorantWrapper;
	}

	
	
	
}
