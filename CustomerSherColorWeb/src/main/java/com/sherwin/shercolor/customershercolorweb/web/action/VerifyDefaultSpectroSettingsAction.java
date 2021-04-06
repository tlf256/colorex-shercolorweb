package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class VerifyDefaultSpectroSettingsAction extends ActionSupport implements SessionAware, LoginRequired{

	private Map<String, Object> sessionMap;
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(SpectroConfigureAction.class);
	private String reqGuid;
	
	public String backItUp() {
		try {
			//blank out the color info as we are backing up.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			sessionMap.put(reqGuid, reqObj);
			return SUCCESS;
			
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return ERROR;
			}
	}
	
	public String display() {
		
		try {
			//System.out.println("IN MANAGESTOREDMEASUREMENTSACTION DISPLAY METHOD");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			sessionMap.put(reqGuid, reqObj);
			String customerId = reqObj.getCustomerID();
			
		    return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	
}
