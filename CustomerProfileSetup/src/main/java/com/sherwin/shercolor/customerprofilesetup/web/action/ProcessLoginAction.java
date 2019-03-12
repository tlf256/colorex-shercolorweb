package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;
import com.sherwin.shercolor.customerprofilesetup.web.model.Login;

public class ProcessLoginAction extends ActionSupport implements SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessLoginAction.class);
	private Login login;
	private Map<String, Object> sessionMap;
	private String result = "false";
	private String keyfield;
	
	@Autowired
	CustomerService customerService;
	
	@Override
	public String execute() {
		try {
			if(login.getKeyField()!=null) {
				List<LoginTrans> loginList = new ArrayList<LoginTrans>();
				for(int i = 0; i < login.getKeyField().size(); i++) {
					if(!login.getKeyField().get(i).equals("")) {
						//create new custweblogintransform record list
						LoginTrans newlogin = new LoginTrans();
						newlogin.setKeyField(login.getKeyField().get(i).trim());
						if(login.getMasterAcctName()!=null) {
							newlogin.setMasterAcctName(login.getMasterAcctName().get(i).trim());
						} else {
							newlogin.setMasterAcctName(null);
						}
						if(login.getAcctComment()!=null) {
							newlogin.setAcctComment(login.getAcctComment().get(i).trim());
						} else {
							newlogin.setAcctComment(null);
						}
						loginList.add(newlogin);
					}
				}
				login.setLoginList(loginList);
				sessionMap.put("LoginDetail", login);
			}
			
			return SUCCESS;
			
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public String checkKeyfield() {
		try {
			List<Object> keyfieldList = customerService.getAllLoginTransKeyfields();
			if(keyfieldList.contains(keyfield)) {
				result = "true";
			} else {
				result = "false";
			}
			/*result = new ArrayList<String>();
			int i = 0;
			if(keyfield!=null) {
				while(i<keyfield.size()) {
					if(keyfieldList.contains(keyfield.get(i))) {
						result.add(i, "true");
					} else {
						result.add(i, "false");
					}
					i++;
				}
			}*/
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);
	}
	
	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getKeyfield() {
		return keyfield;
	}

	public void setKeyfield(String keyfield) {
		this.keyfield = keyfield;
	}
	
}
