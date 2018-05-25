package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeServiceProdDataSetWrapper {

	@SerializedName("dsProd")
	OeServiceProdDataSet oeProdDataSet;

	public OeServiceProdDataSet getOeProdDataSet() {
		return oeProdDataSet;
	}

	public void setOeProdDataSet(OeServiceProdDataSet oeProdDataSet) {
		this.oeProdDataSet = oeProdDataSet;
	}

}
