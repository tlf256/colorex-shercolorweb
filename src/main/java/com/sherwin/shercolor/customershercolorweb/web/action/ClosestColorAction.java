package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.ClosestColor;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class ClosestColorAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ClosestColorAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private boolean swactive;
	private String intExt;
	private boolean closestColor;
	List<ClosestColor> closestSwColors;
	List<ClosestColor> closestCmptColors;
	
	@Autowired
	private ColorService colorService;
	
	public String measure() {
		//redirect to coloreye measurement
		setClosestColor(true);
		return SUCCESS;
	}
	
	public String display() {
		
		return SUCCESS;
	}
	
	public String execute() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		// find closest colors
		closestSwColors = colorService.findClosestColorsFromCoord(reqObj.getColorCoordMap().get("colorCoord"), intExt, true);
		if(!swactive) {
			// also retrieve competitive colors
			closestCmptColors = colorService.findClosestColorsFromCoord(reqObj.getColorCoordMap().get("colorCoord"), null, false);
		}
		
		return SUCCESS;
	}
	
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);		
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public boolean isSwactive() {
		return swactive;
	}

	public void setSwactive(boolean swactive) {
		this.swactive = swactive;
	}

	public String getIntExt() {
		return intExt;
	}

	public void setIntExt(String intExt) {
		this.intExt = intExt;
	}

	public boolean isClosestColor() {
		return closestColor;
	}

	public void setClosestColor(boolean closestColor) {
		this.closestColor = closestColor;
	}

	public List<ClosestColor> getClosestSwColors() {
		return closestSwColors;
	}

	public void setClosestSwColors(List<ClosestColor> closestSwColors) {
		this.closestSwColors = closestSwColors;
	}

	public List<ClosestColor> getClosestCmptColors() {
		return closestCmptColors;
	}

	public void setClosestCmptColors(List<ClosestColor> closestCmptColors) {
		this.closestCmptColors = closestCmptColors;
	}
}
