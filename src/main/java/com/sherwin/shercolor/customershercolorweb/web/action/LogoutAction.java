package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.springframework.stereotype.Component;

@Component
public class LogoutAction extends ActionSupport  implements SessionAware, LoginRequired  {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionMap;

	static Logger logger = LogManager.getLogger(LogoutAction.class);
	private String reqGuid;
	private String loMessage;

	public String display() {
		//logger.info("in logoutAction.display, sherLinkURL is " + sherLinkURL + " reqGuid is " + reqGuid);
		return SUCCESS;		 
	}
	
	@SuppressWarnings("rawtypes")
	public String execute() {

		HttpServletRequest request = ServletActionContext.getRequest();

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
				origReqObj.reset();
				sessionMap.put(origReqObj.getGuid(), origReqObj);
				sessionMap.remove(reqGuid);
				returnStatus = SUCCESS;
			}
			
			////////////////////////////////////////////////////////////////////
			//Remediate Qualys 150069 Static session ID
			request.getSession().invalidate();
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








	
	public String getLoMessage() {
		return loMessage;
	}



	public void setLoMessage(String loMessage) {
		this.loMessage = Encode.forHtml(loMessage);
	}


}
