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
public class DownloadPdfAction extends ActionSupport  implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(DownloadPdfAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String pdfFile;
	
	private InputStream fileInputStream;

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

//Opens Help Menu links on the welcome page - edo78r
	
	public String execute() throws Exception {
		String pdfFileName = "";
		try {
			switch(pdfFile) {
				case "User Guide":
					pdfFileName = "SherColor_Web_Customer_Guide.pdf";
					break;
				case "Setting Up Your Accutinter":
					pdfFileName = "SherColor_Web_Accutinter_Installation_Guide.pdf";
					break;
				case "Calibrating Your Accutinter":
					pdfFileName = "SherColor_Web_Fluid_Management_Calibration.pdf";
				    break;
				case "Installing Your Color Eye":
				    pdfFileName = "SherColor_Web_Color_Eye_Installation.pdf";
				    break;
				case "Setting Up Your Corob":
				    pdfFileName = "SherColor_Web_Corob_Installation_Guide.pdf";
				    break;
				case "Calibrating Your Corob":
				    pdfFileName = "SherColor_Web_Corob_Calibration.pdf";
				    break;
				case "Installing Your Dymo Printer":
				    pdfFileName = "SherColor_Web_Dymo_Install.pdf";
				    break;
				case "Installing Your Zebra Printer":
				    pdfFileName = "SherColor_Web_Zebra_Install.pdf";
				    break;
				default:
					pdfFileName = "INVALID";
			}
			if (pdfFileName.equals("INVALID")) {
				return ERROR;
			}
			fileInputStream = new FileInputStream(new File("/web_apps/server/shercolor/external/" + pdfFileName));
	    	return SUCCESS;
		 } catch (Exception e) {
			logger.error(e.getMessage());
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
}

