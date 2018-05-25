package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeFormInput {
	
	@SerializedName("dsFormulation")
	OeFormInputDataSet formulationIn;

	public OeFormInputDataSet getFormulationIn() {
		return formulationIn;
	}

	public void setFormulationIn(OeFormInputDataSet formulationIn) {
		this.formulationIn = formulationIn;
	}

	
}
