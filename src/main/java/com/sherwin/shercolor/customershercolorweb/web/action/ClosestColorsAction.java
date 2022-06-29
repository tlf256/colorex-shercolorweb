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

public class ClosestColorsAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ClosestColorsAction.class);
	private transient Map<String, Object> sessionMap;
	private String reqGuid;
	private boolean swactive;
	private boolean closestColors;
	private transient List<ClosestColor> closestSwColors;
	private transient List<ClosestColor> closestCmptColors;
	
	@Autowired
	private transient ColorService colorService;
	
	public String measure() {
		//redirect to coloreye measurement
		setClosestColors(true);
		return SUCCESS;
	}
	
	public String display() {
		
		return SUCCESS;
	}
	
	@Override
	public String execute() {
		logger.info("begin ClosestColorsAction execute");
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// find closest colors
			closestSwColors = colorService.findClosestColorsFromCoord(reqObj.getColorCoordMap().get("colorCoord"), true);
			if(!swactive) {
				// also retrieve competitive colors
				closestCmptColors = colorService.findClosestColorsFromCoord(reqObj.getColorCoordMap().get("colorCoord"), false);
			}
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
<<<<<<< HEAD
			addActionError("An Error occurred while processing request. Please retry.");
=======
>>>>>>> d56151fd3a208e81ba7c1f3d44867f271a9fd8da
			return ERROR;
		}
		logger.info("success ClosestColorsAction execute");
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

	public boolean isClosestColors() {
		return closestColors;
	}

	public void setClosestColors(boolean closestColors) {
		this.closestColors = closestColors;
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
