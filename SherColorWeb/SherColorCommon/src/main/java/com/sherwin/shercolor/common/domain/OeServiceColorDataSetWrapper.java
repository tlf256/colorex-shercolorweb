package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeServiceColorDataSetWrapper {

	@SerializedName("dsColor")
	OeServiceColorDataSet oeColorDataSet;

	public OeServiceColorDataSet getOeColorDataSet() {
		return oeColorDataSet;
	}

	public void setOeColorDataSet(OeServiceColorDataSet oeColorDataSet) {
		this.oeColorDataSet = oeColorDataSet;
	}

	
}
