package com.sherwin.shercolor.shercolorweb.web.action;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import org.springframework.stereotype.Component;

@Component
public class TinterVideoAction extends ActionSupport  implements SessionAware, LoginRequired{
	private static final long serialVersionUID = 1L;
	
	static Logger logger = LoggerFactory.getLogger(TinterVideoAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	
	private String vidContentType;
	private String vidFileName;
	
	private DataInputStream inputStream;

	
	public String getCleanNozzle(){
		String retVal;

		try{

			logger.debug("Inside getCLeanNozzle video action");
			
			inputStream = new DataInputStream( new FileInputStream(new File("/web_apps/server/shercolor/deploy/nozzle open.mp4")));

			logger.debug("returning success");
			
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage(), e);
			retVal = ERROR;
		}
		
		return retVal;
	}
	
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public String getVidContentType() {
		return vidContentType;
	}

	public String getVidFileName() {
		return vidFileName;
	}

	public DataInputStream getInputStream() {
		return inputStream;
	}





}
