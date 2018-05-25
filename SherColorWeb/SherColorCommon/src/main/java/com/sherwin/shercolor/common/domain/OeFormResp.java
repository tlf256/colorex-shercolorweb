package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeFormResp {

	@SerializedName("dsFormulation")
	private OeFormRespDataSet formulationDataSet;
	
	public OeFormRespDataSet getFormulationDataSet() {
		return formulationDataSet;
	}

	public void setFormulationDataSet(OeFormRespDataSet formulationDataSet) {
		this.formulationDataSet = formulationDataSet;
	}
	
}
