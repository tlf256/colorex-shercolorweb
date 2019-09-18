package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
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
	private DataInputStream inputStream;
	private boolean updateMode;
	
	@Autowired
	EulaService eulaService;
	
	public String execute() {
		try { 
			
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			setUpdateMode(true);
			
			Eula activeEula = eulaService.readActive("CUSTOMERSHERCOLORWEB", reqObj.getCustomerId());
			
			/*if(activeEula.getCustomerId() == null) {
				String error = "EULA for " + reqObj.getCustomerId() + " is unavailable";
				sessionMap.put("error", error);
				return "noeula";
			} else {*/
				byte[] filebytes = activeEula.getEulaPdf();
				inputStream = new DataInputStream(new ByteArrayInputStream(filebytes));
			//}
			
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
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.setSessionMap(session);
	}
	
	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public DataInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(DataInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}

}
