package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class TinterVideoAction extends ActionSupport  implements SessionAware, LoginRequired{
	private static final long serialVersionUID = 1L;
	
	static Logger logger = LogManager.getLogger(TinterEventAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	
	private String vidContentType;
	private String vidFileName;
	
	private DataInputStream inputStream;

	
	public String getCleanNozzle(){
		String retVal;

		try{

			System.out.println("Inside getCLeanNozzle video action");
			
			inputStream = new DataInputStream( new FileInputStream(new File("/web_apps/server/shercolor/deploy/nozzle open.mp4")));

			System.out.println("returning success");
			
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			e.printStackTrace();
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
