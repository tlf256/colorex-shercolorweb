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
import com.sherwin.shercolor.customerprofilesetup.web.model.CustomerSummary;

public class CustomerAction extends ActionSupport implements SessionAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CustomerAction.class);
	private Map<String, Object> sessionMap;
	private List<CustomerSummary> custSummList;
	private boolean updateMode;
	
	@Autowired
	CustomerService customerService;
	
	public String execute() {
		try {
			custSummList = new ArrayList<CustomerSummary>();
			
			List<Object[]> custList = customerService.getCustSummaryList();
			
			for(Object[] item : custList) {
				CustomerSummary summary = new CustomerSummary();
				summary.setCustomerId(item[0].toString());
				summary.setCustomerName(item[1].toString());
				summary.setClrntSysSummary(item[2].toString());
				custSummList.add(summary);
			}
						
			return SUCCESS;
		
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
	}
	
	public String display() {
		try {
			
			return SUCCESS;
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public String update() { 
		try { 
			updateMode=true;
			
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
	 
	
	public String reset() {
		 try {
			 sessionMap.clear();
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public String cancel() {
		try {
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public List<CustomerSummary> getCustSummList() {
		return custSummList;
	}
	
	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}
	
	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

}
