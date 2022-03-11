package com.sherwin.shercolor.customershercolorweb.web.action;

import org.owasp.encoder.Encode;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;


import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.ecom.security.assertion.SsoHelper;
import com.sherwin.login.service.SWUserService;
import com.sherwin.login.util.SWLoginValidator;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class PasswordAction extends ActionSupport  implements SessionAware, LoginRequired {
	private String guid1;
	private String whereFrom;
	private String userPass;
	private String userPassConfirm;
	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionMap;
	private SWUserService swUserService;
	private SWLoginValidator swLoginValidator;

	
	static Logger logger = LogManager.getLogger(PasswordAction.class);
	
	public String display() {
		String returnStatus = "";

		try {
			 whereFrom = "EXPIRED";
			returnStatus = SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
		}
		return returnStatus;
	}
	
	public String display2() {
		String returnStatus = "";

		try {
			whereFrom = "USERCHANGE";
			returnStatus = SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
		}
		return returnStatus;
	}
	
	public String change() {
		String returnStatus = "";
		String userId = "";
		try {
			logger.info("change start, guid1 is " + guid1);
			RequestObject loginReqObj = (RequestObject) sessionMap.get(guid1);
			if(loginReqObj != null) {
				logger.info("got loginReqObj");
				userId = loginReqObj.getUserId();
				logger.info("got user ID is " + userId);
				
				//Do some comparisons and checks - make sure firstly that the password and 
				//the password confirmation match.
				if (!userPass.equals(userPassConfirm)) {
					logger.error("passwords do not match");
					addActionMessage(getText("passwordAction.passwordsDoNotMatch"));
					returnStatus = INPUT;
				} else {
					if (!swLoginValidator.validatePassword(userPass)) {	
						logger.error("password does not validate rules");
						addActionMessage(getText("passwordAction.passwordDoesNotMeetAllValidationRules"));
						returnStatus = INPUT;
					} else {		
						//call the SsoHelper and see what its response is now.
						logger.info("passwords match...");
						
						if (SsoHelper.getInstance().userPasswordReset(userId, userPass)) {
							logger.info("...and the reset was successful");
							//and update the SWUser's password change date.
							boolean isGoodPwdDateSet = swUserService.updatePasswordChangeDate(userId);
							logger.info("updatePasswordChangeDate is " + isGoodPwdDateSet);
							returnStatus = SUCCESS;
						} else {
							logger.error("...and the reset failed");
							returnStatus = INPUT;
						}
					}
				}
			} else {
				//loginReqObj is null, probably due to session timeout
				logger.error("loginReqObj is null");
				
				//return success to redirect to loginAction
				returnStatus = SUCCESS;
			}
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			returnStatus = ERROR;
		}
		logger.info("change end, returning " + returnStatus);
		return returnStatus;
	
	}
	
	public String cancel() {
		try {
			
			return SUCCESS;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
			
	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
		
	}
	
	public String getGuid1() {
		return guid1;
	}

	public void setGuid1(String guid1) {
		this.guid1 = guid1;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = Encode.forHtml(userPass);
	}
	
	public String getWhereFrom() {
		return whereFrom;
	}

	public void setWhereFrom(String whereFrom) {
		this.whereFrom = Encode.forHtml(whereFrom);
	}

	public String getUserPassConfirm() {
		return userPassConfirm;
	}

	public void setUserPassConfirm(String userPassConfirm) {
		this.userPassConfirm = Encode.forHtml(userPassConfirm);
	}
	
	public SWUserService getSwUserService() {
		return swUserService;
	}

	public void setSwUserService(SWUserService swUserService) {
		this.swUserService = swUserService;
	}
	
	
	public SWLoginValidator getSwLoginValidator() {
		return swLoginValidator;
	}

	public void setSwLoginValidator(SWLoginValidator swLoginValidator) {
		this.swLoginValidator = swLoginValidator;
	}

}