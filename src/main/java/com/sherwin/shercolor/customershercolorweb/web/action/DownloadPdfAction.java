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
public class DownloadPdfAction extends ActionSupport  implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(DownloadPdfAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String pdfFile;
	private InputStream fileInputStream;
	
	@Value("${artifactoryToken}")
	private String artifactoryToken;

//Opens Help Menu links on the welcome page - edo78r
	
	public String execute() {
		String pdfFileName = "";
		try {
			switch(pdfFile) {
				case "SherColor_Web_Customer_Guide.pdf":
					pdfFileName = "SherColor_Web_Customer_Guide.pdf";
					break;
				case "SherColor_Web_XProtint_Tinter_Installation_Guide.pdf":
					pdfFileName = "SherColor_Web_XProtint_Tinter_Installation_Guide.pdf";
					break;
				case "SherColor_Web_Accutinter_Installation_Guide.pdf":
					pdfFileName = "SherColor_Web_Accutinter_Installation_Guide.pdf";
					break;
				case "SherColor_Web_Fluid_Management_Calibration.pdf":
					pdfFileName = "SherColor_Web_Fluid_Management_Calibration.pdf";
				    break;
				case "SherColor_Web_Color_Eye_Installation.pdf":
				    pdfFileName = "SherColor_Web_Color_Eye_Installation.pdf";
				    break;
				case "SherColor_Web_Corob_Installation_Guide.pdf":
				    pdfFileName = "SherColor_Web_Corob_Installation_Guide.pdf";
				    break;
				case "SherColor_Web_Corob_Calibration.pdf":
				    pdfFileName = "SherColor_Web_Corob_Calibration.pdf";
				    break;
				case "SherColor_Web_Dymo_Install.pdf":
				    pdfFileName = "SherColor_Web_Dymo_Install.pdf";
				    break;
				case "SherColor_Web_Zebra_Install.pdf":
				    pdfFileName = "SherColor_Web_Zebra_Install.pdf";
				    break;
				default:
					pdfFileName = "INVALID";
			}
			if (pdfFileName.equals("INVALID")) {
				logger.error("pdf filename is invalid");
				return ERROR;
			}
			
			Artifactory artifactory = ArtifactoryClientBuilder.create()
			        .setUrl("https://artifactory.sherwin.com/artifactory")
			        .setUsername("shercolorweb-reader")
			        .setPassword(artifactoryToken)
			        .build();
			
			fileInputStream = artifactory.repository("shercolorweb-generic-local")
			        .download("com/sherwin/SherColorWeb/" + pdfFileName)
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

	public String getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(String pdfFile) {
		this.pdfFile = pdfFile;
	}
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
}

