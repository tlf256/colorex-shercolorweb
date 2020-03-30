package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebJobFields;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

public class ProcessJobFieldsAction extends ActionSupport implements SessionAware, LoginRequired {
	private Map<String, Object> sessionMap;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessJobFieldsAction.class);
	private String reqGuid;
	private List<JobField> jobFieldList;
	private FormulaInfo displayFormula;
	private int updateMode;
	
	@Autowired
	CustomerService customerService;

	private boolean debugOn = false;
	
	public String display(){
		String retVal;
		try{
			updateMode = 0;
			jobFieldList = new ArrayList<JobField>();
			List<CustWebJobFields> custWebJobFields;
			
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			custWebJobFields = customerService.getCustJobFields(reqObj.getCustomerID());
			
			if(custWebJobFields.size()>0){
				for(CustWebJobFields custField : custWebJobFields){
					JobField jobField = new JobField();
					jobField.setScreenLabel(custField.getScreenLabel());
					jobField.setEnteredValue(custField.getFieldDefault());
					if(debugOn) System.out.println("Adding field " + jobField.getScreenLabel() + " default is " + jobField.getEnteredValue());
					jobFieldList.add(jobField);
				}
				retVal = INPUT;
			} else{
				retVal = SUCCESS;
			}
			
			reqObj.setJobFieldList(jobFieldList);
			
			sessionMap.put(reqGuid, reqObj);

		} catch (Exception e) {
			logger.error(e.getMessage());
			retVal = ERROR;
		}
		
		return retVal;
		
	}
	
	public String displayupdate(){
		String retVal;
		try{
			updateMode=1;
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			jobFieldList = reqObj.getJobFieldList();
			
			reqObj.setQuantityDispensed(0);

			
			if(jobFieldList.size()>0){
				retVal = INPUT;
			} else{
				retVal = SUCCESS;
			}
			
			sessionMap.put(reqGuid, reqObj);

		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			retVal = ERROR;
		}
		
		return retVal;
		
	}
	
	public String execute(){
		String retVal;
		try {
			updateMode = 0;
			if(debugOn) System.out.println("About to get object from map");
			if(debugOn) System.out.println("reqGuid is " + reqGuid);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			List<String> validateMe = new ArrayList<String>();
			List<String> jobFieldLabels = new ArrayList<String>();
			
			if(debugOn) System.out.println("about to process jobFieldList");
			if(jobFieldList==null){
				if(debugOn) System.out.println("jobFieldList is null");
				retVal = ERROR;
			} else {
				if(debugOn) System.out.println("jobFieldList is NOT null");
				
				int i = 0;
				for(JobField thisField : jobFieldList){
					if(debugOn) System.out.println("thisField=" + thisField.getEnteredValue());
					// fill in screenLabel b/c lost on form submit
					thisField.setScreenLabel(reqObj.getJobFieldList().get(i).getScreenLabel());
					// encode entered value
					thisField.setEnteredValue(thisField.getEnteredValue());
					//if(debugOn) System.out.println("thisField after encoding: " + thisField.getEnteredValue());
					i++;
					validateMe.add(thisField.getEnteredValue());
					jobFieldLabels.add(thisField.getScreenLabel());
				}
				
				if(debugOn) System.out.println("about to call validate");
				
				List<SwMessage> messages = customerService.validateCustJobFields(jobFieldLabels,validateMe);
				
				if(debugOn) System.out.println("back from validate");
				if(debugOn) System.out.println("message size is " + messages.size());
				if(messages.size()>0){
					// TODO process field entry errors
					int index = 0;
					for (SwMessage message : messages) {
						addFieldError(message.getCode(),message.getMessage());
					}
					retVal = INPUT;
				} else {
					// all good
					if(debugOn) System.out.println("about to set JobFieldList");
					reqObj.setJobFieldList(jobFieldList);
					
					if(debugOn) System.out.println("about to put map into session");
					sessionMap.put(reqGuid, reqObj);
					retVal = SUCCESS;
				} // end else no messages
			} // end else jobFieldList is not null
			
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			retVal = ERROR;
		}
		
		return retVal;
	}
	
	
	public String executeupdate(){
		String retVal = execute();
		
		try{
			updateMode = 1;
			
			if(retVal.equals(SUCCESS)){
				// was successful, prep for display of formula...
				RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
				reqObj.setControlNbr(0); // reset control number
				displayFormula = reqObj.getDisplayFormula();
				
				sessionMap.put(reqGuid, reqObj);
			}

		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			retVal = ERROR;
		}
		return retVal;
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

	public List<JobField> getJobFieldList() {
		return jobFieldList;
	}

	public void setJobFieldList(List<JobField> jobFieldList) {
		this.jobFieldList = jobFieldList;
	}

	public int getUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(int updateMode) {
		this.updateMode = updateMode;
	}

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}

	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}

}
