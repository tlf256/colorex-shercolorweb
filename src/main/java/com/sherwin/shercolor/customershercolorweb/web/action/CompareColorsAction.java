package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class CompareColorsAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CompareColorsAction.class);
	private transient Map<String, Object> sessionMap;
	private String reqGuid;
	private boolean compareColors;
	
	@Override
	public String execute() {
		setCompareColors(true);
		//redirect to getColor
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		reqObj.setColorComp("");
		reqObj.setColorName("COLOR");
		reqObj.setColorID("MEASURED");
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

	public boolean isCompareColors() {
		return compareColors;
	}

	public void setCompareColors(boolean compareColors) {
		this.compareColors = compareColors;
	}

}
