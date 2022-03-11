package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class DownloadExeAction extends ActionSupport  implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(DownloadExeAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	
	private InputStream fileInputStream;

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public String execute() {
		try {
			fileInputStream = new FileInputStream(new File("/web_apps/server/shercolor/external/SWDHSetup.exe"));
	    	return SUCCESS;
		 } catch (Exception e) {
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
