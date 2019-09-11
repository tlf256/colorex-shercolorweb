package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;
import com.sherwin.shercolor.customerprofilesetup.web.model.Job;
import com.sherwin.shercolor.customerprofilesetup.web.model.RequestObject;

public class ProcessJobAction extends ActionSupport implements SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessJobAction.class);
	private Map<String, Object> sessionMap;
	private Job job;
	private boolean updateMode;
	private boolean newCustomer;
	
	@Autowired
	CustomerService customerService;
	
	public String execute() {
		try { 
			if(job!=null) {
				RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
				
				List<JobFields> jobList = new ArrayList<JobFields>();
				String[] reqlist = new String[10];
				setUpdateMode(false);
				setNewCustomer(true);
				
				int start = 0;
				int end = 9;
				int i = 0;
				int seqnbr = 1;
				//initial fill of string array
				Arrays.fill(reqlist, start, end, "false");
				//insert true at checked index
				if(job.getEntryRequired()!=null) {
					for(int index = 0; index < job.getEntryRequired().size(); index++) {
						reqlist[Integer.parseInt(job.getEntryRequired().get(index))] = "true";
					}
				}
				while(i<job.getScreenLabel().size()) {
					if(!job.getScreenLabel().get(i).equals("")) {
						//create new custwebjobfields record list
						JobFields newjob = new JobFields();
						newjob.setSeqNbr(seqnbr);
						newjob.setScreenLabel(allowCharacters(job.getScreenLabel().get(i)));
						newjob.setFieldDefault(allowCharacters(job.getFieldDefault().get(i)));
						if(reqlist[i].contains("true")) {
							newjob.setEntryRequired(true);
						} else {
							newjob.setEntryRequired(false);
						}
						newjob.setActive(true);
						jobList.add(newjob);
						seqnbr++;
					}
					i++;
				}
				
				reqObj.setJobFieldList(jobList);
				sessionMap.put("CustomerDetail", reqObj);
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
	
	public String allowCharacters(String escapedString) {
		String newString = "";
		if(escapedString != null) {
			if(escapedString.contains("&amp;")) {
				newString = escapedString.replaceAll("&amp;", "&");
			} else if(escapedString.contains("&#38;")) {
				newString = escapedString.replaceAll("&#38;", "&");
			} else if(escapedString.contains("&apos;")) {
				newString = escapedString.replaceAll("&apos;", "'");
			} else if(escapedString.contains("&#39;")) {
				newString = escapedString.replaceAll("&#39;", "'");
			} else {
				newString = escapedString;
			}
		}
		return newString;
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

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}

	public boolean isNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(boolean newCustomer) {
		this.newCustomer = newCustomer;
	}

}
