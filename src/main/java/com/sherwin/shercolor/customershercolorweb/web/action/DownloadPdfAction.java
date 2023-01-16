package com.sherwin.shercolor.customershercolorweb.web.action;


import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.ArtifactoryRequest;
import org.jfrog.artifactory.client.ArtifactoryResponse;
import org.jfrog.artifactory.client.impl.ArtifactoryRequestImpl;

import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject; 
import com.google.gson.JsonParser;

import com.opensymphony.xwork2.ActionSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@SuppressWarnings("serial")
@Component
public class DownloadPdfAction extends ActionSupport  implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(DownloadPdfAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String pdfType;
	private String fileName="";
	
	private InputStream fileInputStream;
	
	@Value("${artifactoryToken}")
	private String artifactoryToken;

//Opens Help Menu links on the welcome page - edo78r

	@Override
	public String execute() {
		String dirPath = "";
		String repoName = "shercolorweb-generic-local";
		try {
			switch(pdfType) {
				case "SherColor_Web_Customer_Guide":
					dirPath = "SherColorWeb/SherColor_Web_Customer_Guide";
					break;
				case "SherColor_Web_XProtint_Tinter_Installation_Guide":
					dirPath = "SherColorWeb/SherColor_Web_XProtint_Tinter_Installation_Guide";
					break;
				case "SherColor_Web_Accutinter_Installation_Guide":
					dirPath = "SherColorWeb/SherColor_Web_Accutinter_Installation_Guide";
					break;
				case "SherColor_Web_Fluid_Management_Calibration":
					dirPath = "SherColorWeb/SherColor_Web_Fluid_Management_Calibration";
				    break;
				case "SherColor_Web_Color_Eye_Installation":
				    dirPath = "SherColorWeb/SherColor_Web_Color_Eye_Installation";
				    break;
				case "SherColor_Web_Corob_Installation_Guide":
				    dirPath = "SherColorWeb/SherColor_Web_Corob_Installation_Guide";
				    break;
				case "SherColor_Web_Corob_Calibration":
				    dirPath = "SherColorWeb/SherColor_Web_Corob_Calibration";
				    break;
				case "SherColor_Web_Dymo_Install":
				    dirPath = "SherColorWeb/SherColor_Web_Dymo_Install";
				    break;
				case "SherColor_Web_Zebra_Install":
				    dirPath = "SherColorWeb/SherColor_Web_Zebra_Install";
				    break;
				default:
					dirPath  = "INVALID";
			}
			if (dirPath.equals("INVALID")) {
				logger.error("pdf filename is invalid");
				return ERROR;
			}
			
			Artifactory artifactory = ArtifactoryClientBuilder.create()
			        .setUrl("https://artifactory.sherwin.com/artifactory")
			        .setUsername("shercolorweb-reader")
			        .setPassword(artifactoryToken)
			        .build();
			
			ArtifactoryRequest repositoryRequest = new ArtifactoryRequestImpl()
					.apiUrl("api/storage/" + repoName + "/" + dirPath + "?lastModified")
			        .method(ArtifactoryRequest.Method.GET)
			        .responseType(ArtifactoryRequest.ContentType.JSON);
			ArtifactoryResponse response = artifactory.restCall(repositoryRequest);	
			
			if (response.isSuccessResponse()) {			
				String rawBody = response.getRawBody();
				JsonObject obj = JsonParser.parseString(rawBody).getAsJsonObject();
				String uri = obj.get("uri").toString();
				String separator = dirPath + "/";
				int sepPos = uri.indexOf(separator);
				fileName = uri.substring(sepPos + separator.length(), uri.length()-1);
			} else {
				logger.error("Unsuccessful response from Artifactory: {}", response);
				return ERROR;
			}
			
			if (fileName.startsWith(pdfType)) {
				fileInputStream = artifactory.repository(repoName)
			        .download(dirPath + "/" + fileName)
			        .doDownload();
			} else {
				logger.error("Last modified file is {}, not of type {}. Either remove the incorrect deployed file from Artifactory or update the prefix check", () -> FilenameUtils.getName(fileName), () -> pdfType);
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

	public String getPdfType() {
		return pdfType;
	}

	public void setPdfType(String pdfType) {
		this.pdfType = pdfType;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
}

