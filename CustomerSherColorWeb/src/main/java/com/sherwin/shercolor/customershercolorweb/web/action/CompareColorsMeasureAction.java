package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class CompareColorsMeasureAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CompareColorsMeasureAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	
	public String display() {
		try {
			return SUCCESS;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String execute() {
		try {
			//setFilter(false);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			List<ColorCoordinates> coordList = reqObj.getCoordinatesList();
			
			if(coordList == null) {
				coordList = new ArrayList<ColorCoordinates>();
			}
			
			
			
			reqObj.setCoordinatesList(coordList);
			
			sessionMap.put(reqGuid, reqObj);
			
			return SUCCESS;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
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

}
