package com.sherwin.shercolor.swdevicehandler.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="simtinterresponses")
public class SimTinterCommandResponseList {
	List<SimTinterResponseCommand> responseCommmand = new ArrayList<SimTinterResponseCommand>();

	public List<SimTinterResponseCommand> getResponseCommmand() {
		return responseCommmand;
	}

	@XmlElementWrapper(name="commandlist")
	@XmlElement(name="command")
	public void setResponseCommmand(List<SimTinterResponseCommand> responseCommmand) {
		this.responseCommmand = responseCommmand;
	}
	
	
	

}
