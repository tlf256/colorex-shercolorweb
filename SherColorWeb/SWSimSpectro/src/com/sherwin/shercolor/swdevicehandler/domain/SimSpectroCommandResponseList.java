package com.sherwin.shercolor.swdevicehandler.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="simspectroresponses")
public class SimSpectroCommandResponseList {
	List<SimSpectroResponseCommand> responseCommmand = new ArrayList<SimSpectroResponseCommand>();

	public List<SimSpectroResponseCommand> getResponseCommmand() {
		return responseCommmand;
	}

	@XmlElementWrapper(name="commandlist")
	@XmlElement(name="command")
	public void setResponseCommmand(List<SimSpectroResponseCommand> responseCommmand) {
		this.responseCommmand = responseCommmand;
	}
	
	
	

}
