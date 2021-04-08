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
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.TranHistoryService;
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
	private int lookupControlNbr = 0;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	TranHistoryService tranHistoryService;


	private boolean debugOn = false;
	
	public String display(){
		String retVal;
		try{
			updateMode = 0;
			jobFieldList = new ArrayList<JobField>();
			List<CustWebJobFields> custWebJobFields;
			CustWebTran webTran = null;
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			// look up custwebtran if we are copying in existing job fields
			if (lookupControlNbr > 0) {
				webTran = tranHistoryService.readTranHistory(reqObj.getCustomerID(), lookupControlNbr, 1);
			}

			custWebJobFields = customerService.getCustJobFields(reqObj.getCustomerID());
			
			if(custWebJobFields.size()>0){
				int ctr = 1;
				for(CustWebJobFields custField : custWebJobFields){
					JobField jobField = new JobField();
					jobField.setScreenLabel(custField.getScreenLabel());
					
					// get job field from custwebtran record
					if (webTran != null) {
						String existingJobField = "";
						switch (ctr) {
						case 1:
							existingJobField = webTran.getJobField01();
							break;
						case 2:
							existingJobField = webTran.getJobField02();
							break;
						case 3:
							existingJobField = webTran.getJobField03();
							break;
						case 4:
							existingJobField = webTran.getJobField04();
							break;
						case 5:
							existingJobField = webTran.getJobField05();
							break;
						case 6:
							existingJobField = webTran.getJobField06();
							break;
						case 7:
							existingJobField = webTran.getJobField07();
							break;
						case 8:
							existingJobField = webTran.getJobField08();
							break;
						case 9:
							existingJobField = webTran.getJobField09();
							break;
						case 10:
							existingJobField = webTran.getJobField10();
							break;
						}
						// copy in existing field
						jobField.setEnteredValue(existingJobField);
					
					// otherwise set default
					} else {
						jobField.setEnteredValue(custField.getFieldDefault());
					}
					logger.debug("Adding field " + jobField.getScreenLabel() + " value is " + jobField.getEnteredValue());
					jobFieldList.add(jobField);
					ctr++;
				}
				retVal = INPUT;
			} else{
				retVal = SUCCESS;
			}
			
			reqObj.setJobFieldList(jobFieldList);
			
			sessionMap.put(reqGuid, reqObj);

		} catch (Exception e) {
			logger.error(e.getMessage() + ": ", e);
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
			logger.error(e.getMessage() + ": ", e);
			retVal = ERROR;
		}
		
		return retVal;
		
	}
	
	public String execute(){
		String retVal;
		try {
			updateMode = 0;
			logger.debug("About to get object from map");
			logger.debug("reqGuid is " + reqGuid);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			List<String> validateMe = new ArrayList<String>();
			List<String> jobFieldLabels = new ArrayList<String>();
			
			logger.debug("about to process jobFieldList");
			if(jobFieldList==null){
				logger.debug("jobFieldList is null");
				retVal = ERROR;
			} else {
				logger.debug("jobFieldList is NOT null");
				
				int i = 0;
				for(JobField thisField : jobFieldList){
					logger.debug("thisField=" + thisField.getEnteredValue());
					// fill in screenLabel b/c lost on form submit
					thisField.setScreenLabel(reqObj.getJobFieldList().get(i).getScreenLabel());
					thisField.setEnteredValue(thisField.getEnteredValue());
					
					i++;
					validateMe.add(thisField.getEnteredValue());
					jobFieldLabels.add(thisField.getScreenLabel());
				}
				
				logger.debug("about to call validate");
				
				List<SwMessage> messages = customerService.validateCustJobFields(jobFieldLabels,validateMe);
				
				logger.debug("back from validate");
				logger.debug("message size is " + messages.size());
				if(messages.size()>0){
					// TODO process field entry errors
					int index = 0;
					for (SwMessage message : messages) {
						addFieldError(message.getCode(),message.getMessage());
					}
					retVal = INPUT;
				} else {
					// all good
					logger.debug("about to set JobFieldList");
					reqObj.setJobFieldList(jobFieldList);
					
					logger.debug("about to put map into session");
					sessionMap.put(reqGuid, reqObj);
					retVal = SUCCESS;
				} // end else no messages
			} // end else jobFieldList is not null
			
		} catch (Exception e) {
			logger.error(e.getMessage() + ": ", e);
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
			logger.error(e.getMessage() + ": ", e);
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

	public int getLookupControlNbr() {
		return lookupControlNbr;
	}

	public void setLookupControlNbr(int lookupControlNbr) {
		this.lookupControlNbr = lookupControlNbr;
	}

}
