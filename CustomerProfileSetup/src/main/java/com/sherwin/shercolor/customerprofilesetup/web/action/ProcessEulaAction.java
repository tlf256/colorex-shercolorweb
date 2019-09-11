package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.Eula;
import com.sherwin.shercolor.common.service.EulaService;
import com.sherwin.shercolor.customerprofilesetup.web.model.RequestObject;

public class ProcessEulaAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(EditCustomerAction.class);
	private Map<String, Object> sessionMap;
	private String eulafile;
	private Date effDate;
	private Date expDate;
	
	@Autowired
	EulaService eulaService;
	
	public String execute() throws IOException {
		try { 
			
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			byte[] filebytes = readBytesFromFile(eulafile);
						
			Eula eula = new Eula();
			eula.setCustomerId(reqObj.getCustomerId());
			eula.setEffectiveDate(effDate);
			eula.setExpirationDate(expDate);
			eula.setEulaPdf(filebytes);
			
			Eula activeEula = eulaService.readActive("CUSTOMERSHERCOLORWEB", reqObj.getCustomerId());
			if(activeEula == null) {
				eulaService.createEula(eula);
				addFieldError("custediterror", "EULA upload successful");
			} else {
				addFieldError("custediterror", "EULA for " + reqObj.getCustomerId() + " already exists");
				return INPUT;
			}
			
			reqObj.setEula(eula);
			
			return SUCCESS;
	  
		} catch (HibernateException he) { 
			logger.error("HibernateException Caught: "
					+ he.toString() + " " + he.getMessage()); 
			return ERROR; 
		} catch (Exception e){ 
			logger.error(e.getMessage()); 
			return ERROR; 
		} 
	}
	
	public String display() {
		try { 
			
			
			
			return SUCCESS;
	  
		} catch (HibernateException he) { 
			logger.error("HibernateException Caught: "
					+ he.toString() + " " + he.getMessage()); 
			return ERROR; 
		} catch (Exception e){ 
			logger.error(e.getMessage()); 
			return ERROR; 
		} 
	}
	
	private static byte[] readBytesFromFile(String filePath) throws IOException {
        File inputFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(inputFile);
         
        byte[] fileBytes = new byte[(int) inputFile.length()];
        inputStream.read(fileBytes);
        inputStream.close();
         
        return fileBytes;
    }
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.setSessionMap(sessionMap);
	}
	
	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getEulafile() {
		return eulafile;
	}

	public void setEulafile(String eulafile) {
		this.eulafile = eulafile;
	}

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

}
