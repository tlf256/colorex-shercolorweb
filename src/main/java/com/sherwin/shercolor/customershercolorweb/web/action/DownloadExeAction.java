package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;

import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.opensymphony.xwork2.ActionSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@SuppressWarnings("serial")
@Component
public class DownloadExeAction extends ActionSupport  implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(DownloadExeAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private InputStream fileInputStream;
	
	@Value("${artifactoryToken}")
	private String artifactoryToken;


	public String execute() {
		try {
			Artifactory artifactory = ArtifactoryClientBuilder.create()
			        .setUrl("https://artifactory.sherwin.com/artifactory")
			        .setUsername("shercolorweb-reader")
			        .setPassword(artifactoryToken)
			        .build();
			
			fileInputStream = artifactory.repository("shercolorweb-generic-local")
			        .download("com/sherwin/SherColorWeb/SWDHSetup.exe")
			        .doDownload();    	
			
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

	public InputStream getFileInputStream() {
		return fileInputStream;
	}
}

