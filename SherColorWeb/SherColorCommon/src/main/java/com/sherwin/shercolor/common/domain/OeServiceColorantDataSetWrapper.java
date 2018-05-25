package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeServiceColorantDataSetWrapper {
	@SerializedName("dsColorantSys")
	OeServiceColorantDataSet OeColorantDataSet;

	public OeServiceColorantDataSet getOeColorantDataSet() {
		return OeColorantDataSet;
	}

	public void setOeColorantDataSet(OeServiceColorantDataSet oeColorantDataSet) {
		OeColorantDataSet = oeColorantDataSet;
	}
	
	
}
