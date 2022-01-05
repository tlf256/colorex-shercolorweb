package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.springframework.stereotype.Component;

public class LogoutAction extends ActionSupport  implements SessionAware, LoginRequired  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionMap;

	static Logger logger = LoggerFactory.getLogger(LogoutAction.class);

	private String reqGuid;
	private String sherLinkURL;
	private String loMessage;
	
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();

	public String display() {
		//logger.info("in logoutAction.display, sherLinkURL is " + sherLinkURL + " reqGuid is " + reqGuid);
		return SUCCESS;		 
	}
	
	@SuppressWarnings("rawtypes")
	public String execute() {
		String returnStatus = SUCCESS;
		try {
			request.logout();
			//logger.info("in logoutAction.execute - start, sherLinkURL is " + sherLinkURL + " reqGuid is " + reqGuid);
			if (reqGuid==null || reqGuid.isEmpty()) {
				returnStatus = SUCCESS;
			} else {
				// reset the existing request object.
				// but before resetting, set the action's sherLinkURL property.
				RequestObject origReqObj = (RequestObject) sessionMap.get(reqGuid);
				sherLinkURL = origReqObj.getSherLinkURL();
				origReqObj.reset();
				sessionMap.put(origReqObj.getGuid(), origReqObj);
				sessionMap.remove(reqGuid);
				returnStatus = SUCCESS;
			}
			
			////////////////////////////////////////////////////////////////////
			//Remediate Qualys 150069 Static session ID 
			try {
				((org.apache.struts2.dispatcher.SessionMap) sessionMap).invalidate();
			} catch (IllegalStateException e) {
				logger.error(e.getMessage(), e);
			}
			//AND/OR
			SessionMap mySession = (SessionMap) ActionContext.getContext().getSession();

			//invalidate
			mySession.invalidate();
			//END  Remediate Qualys 150069 Static session ID 
			////////////////////////////////////////////////////////////////////
			
				
			//logger.info("in logoutAction.execute - end, sherLinkURL is " + sherLinkURL);
			loMessage = getText("global.youHaveBeenLoggedOut");// 
			return returnStatus; 
		     
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
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



	public String getSherLinkURL() {
		return sherLinkURL;
	}



	public void setSherLinkURL(String sherLinkURL) {
		this.sherLinkURL = Encode.forHtml(sherLinkURL);
	}
	
	public String getLoMessage() {
		return loMessage;
	}



	public void setLoMessage(String loMessage) {
		this.loMessage = Encode.forHtml(loMessage);
	}


}
