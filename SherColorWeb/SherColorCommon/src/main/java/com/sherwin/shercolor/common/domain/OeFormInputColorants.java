package com.sherwin.shercolor.common.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OeFormInputColorants {

	@SerializedName("clrnt-tint-sys-id")
	private List<String> tintSysIds = new ArrayList<String>();
	
	@SerializedName("clrnt-shots")
	private List<Integer> shots = new ArrayList<Integer>();

	public List<String> getTintSysIds() {
		return tintSysIds;
	}

	public List<Integer> getShots() {
		return shots;
	}

	public void setTintSysIds(List<String> tintSysIds) {
		this.tintSysIds = tintSysIds;
	}

	public void setShots(List<Integer> shots) {
		this.shots = shots;
	}
	
	

}
