package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ArtifactoryRequest;
import org.jfrog.artifactory.client.ArtifactoryResponse;
import org.jfrog.artifactory.client.impl.ArtifactoryRequestImpl;

import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@SuppressWarnings("serial")
@Component
public class DownloadExeAction extends ActionSupport  implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(DownloadExeAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String fileName="";
	
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
			
			ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl()
					.apiUrl("api/storage/shercolorweb-generic-local/SWDH?lastModified")
			        .method(ArtifactoryRequest.Method.GET)
			        .responseType(ArtifactoryRequest.ContentType.JSON);
			ArtifactoryResponse response = artifactory.restCall(repositoryRequest);	
			
			if (response.isSuccessResponse()) {
				String rawBody = response.getRawBody();
				JsonObject obj = JsonParser.parseString(rawBody).getAsJsonObject();
				String uri = obj.get("uri").toString();
				String separator = "SWDH/";
				int sepPos = uri.indexOf(separator);
				fileName = uri.substring(sepPos + separator.length(), uri.length()-1);
			} else {
				logger.error("Unsuccessful response from Artifactory: " + response);
				return ERROR;
			}
			
			if (fileName.startsWith("SWDHSetup")) {
				fileInputStream = artifactory.repository("shercolorweb-generic-local")
			        .download("SWDH/" + fileName)
			        .doDownload();
			} else {
				logger.error("Last modified file is not an SWDHSetup. Either remove the incorrect deployed file from Artifactory or update the prefix check");
				return ERROR;
			}
			
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
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

