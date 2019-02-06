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

public class ProcessJobAction extends ActionSupport implements SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessJobAction.class);
	private Map<String, Object> sessionMap;
	private Job job;
	private boolean updateMode;
	
	@Autowired
	CustomerService customerService;
	
	public String execute() {
		try { 
			if(job.getScreenLabel()!=null) {
				List<JobFields> jobList = new ArrayList<JobFields>();
				String[] reqlist = new String[10];
				setUpdateMode(false);
				
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
						newjob.setScreenLabel(job.getScreenLabel().get(i).trim());
						newjob.setFieldDefault(job.getFieldDefault().get(i).trim());
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
				job.setJobFieldList(jobList);
				sessionMap.put("JobDetail", job);
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

}
