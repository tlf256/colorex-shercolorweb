package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeServiceRequestWrapper {
	
	@SerializedName("request")
	OeServiceScenarioFormulation oeServiceScenarioFormulation;

	public OeServiceScenarioFormulation getOeServiceScenarioFormulation() {
		return oeServiceScenarioFormulation;
	}

	public void setOeServiceScenarioFormulation(OeServiceScenarioFormulation oeServiceScenarioFormulation) {
		this.oeServiceScenarioFormulation = oeServiceScenarioFormulation;
	}
	
	

}
