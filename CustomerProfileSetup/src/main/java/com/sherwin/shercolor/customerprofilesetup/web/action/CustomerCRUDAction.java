package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebJobFields;
import com.sherwin.shercolor.common.domain.CustWebLoginTransform;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;
import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;
import com.sherwin.shercolor.customerprofilesetup.web.model.Customer;
import com.sherwin.shercolor.customerprofilesetup.web.model.RequestObject;

public class CustomerCRUDAction extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CustomerCRUDAction.class);
	private Map<String, Object> sessionMap;
	private String lookupCustomerId;
	private boolean updateMode;
	private String crudmsg;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	TranHistoryService tranHistoryService;
	
	public String read() {
		try {
			List<String> clrntlist = new ArrayList<String>();
			List<JobFields> jobFieldList = new ArrayList<JobFields>();
			List<LoginTrans> loginList = new ArrayList<LoginTrans>();
			
			List<CustWebParms> custParms = customerService.getAllCustWebParms(lookupCustomerId);
			
			RequestObject reqObj = new RequestObject();
			CustParms customer = new CustParms();
			
			updateMode = true;
			//check for job history
			List<CustWebTran> jobHistory = tranHistoryService.getCustomerJobs(lookupCustomerId);
			
			if(jobHistory.isEmpty()) {
				reqObj.setHistory(false);
			}else {
				reqObj.setHistory(true);
			}
			//map custwebparms to model object
			for(CustWebParms webparms : custParms) {
				customer.setCustomerId(Encode.forHtml(webparms.getCustomerId()));
				if(webparms.getCustomerId().length()==9) {
					reqObj.setAccttype("natlWdigits");
				}else {
					reqObj.setAccttype("loginRequired");
				}
				customer.setSwuiTitle(Encode.forHtml(webparms.getSwuiTitle()));
				customer.setCdsAdlFld(Encode.forHtml(webparms.getCdsAdlFld()));
				clrntlist.add(Encode.forHtml(webparms.getClrntSysId()));
				customer.setActive(webparms.isActive());
			}
						
			reqObj.setCustomerId(allowCharacters(customer.getCustomerId()));
			reqObj.setSwuiTitle(allowCharacters(customer.getSwuiTitle()));
			reqObj.setCdsAdlFld(allowCharacters(customer.getCdsAdlFld()));
			reqObj.setActive(customer.isActive());
			reqObj.setClrntList(clrntlist);
			
			List<CustWebJobFields> jobFields = customerService.getCustJobFields(lookupCustomerId);
			
			//map custwebjobfields to model object
			for(CustWebJobFields jobs : jobFields) {
				JobFields fields = new JobFields();
				fields.setSeqNbr(jobs.getSeqNbr());
				fields.setScreenLabel(Encode.forHtml(jobs.getScreenLabel()));
				fields.setFieldDefault(Encode.forHtml(jobs.getFieldDefault()));
				fields.setEntryRequired(jobs.isEntryRequired());
				fields.setActive(jobs.isActive());
				jobFieldList.add(fields);
			}
			
			// create new arraylist to hold fields with allowed special characters
			List<JobFields> jflist = new ArrayList<JobFields>();
			
			// allow certain special characters 
			for(JobFields jf : jobFieldList) {
				JobFields jobfields = new JobFields();
				jobfields.setSeqNbr(jf.getSeqNbr());
				jobfields.setScreenLabel(allowCharacters(jf.getScreenLabel()));
				jobfields.setFieldDefault(allowCharacters(jf.getFieldDefault()));
				jobfields.setEntryRequired(jf.isEntryRequired());
				jobfields.setActive(jf.isActive());
				jflist.add(jobfields);
			}
			
			reqObj.setJobFieldList(jflist);
			
			List<CustWebLoginTransform> loginTrans = customerService.getCustLoginTrans(lookupCustomerId);
			//if customer has login ids, map custweblogintransform to model object
			if(!loginTrans.isEmpty()) {
				for(CustWebLoginTransform logtran : loginTrans) {
					LoginTrans trans = new LoginTrans();
					trans.setKeyField(Encode.forHtml(logtran.getKeyField()));
					trans.setMasterAcctName(Encode.forHtml(logtran.getMasterAcctName()));
					trans.setAcctComment(Encode.forHtml(logtran.getAcctComment()));
					loginList.add(trans);
				}
				
				// create new arraylist to hold fields with allowed special characters
				List<LoginTrans> ltlist = new ArrayList<LoginTrans>();
				
				// allow certain special characters 
				for(LoginTrans lt : loginList) {
					LoginTrans login = new LoginTrans();
					login.setKeyField(allowCharacters(lt.getKeyField()));
					login.setMasterAcctName(allowCharacters(lt.getMasterAcctName()));
					login.setAcctComment(allowCharacters(lt.getAcctComment()));
					ltlist.add(login);
				}
				
				reqObj.setLoginList(ltlist);
			}
			
			sessionMap.put("CustomerDetail", reqObj);
						
			return SUCCESS;
	
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
		
	}

	public String createOrUpdate() {
		try {			
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			List<CustWebParms> customerList = new ArrayList<CustWebParms>();
			List<CustWebJobFields> jobList = new ArrayList<CustWebJobFields>();
			List<CustWebLoginTransform> loginList = new ArrayList<CustWebLoginTransform>();
			//get date
			Date sqlDate = new Date(System.currentTimeMillis());
			//map session values to custwebparms domain object
			for(CustParms cust : reqObj.getCustList()) {
				CustWebParms custWebParms = new CustWebParms();
				custWebParms.setCustomerId(cust.getCustomerId());
				custWebParms.setSeqNbr(cust.getSeqNbr());
				custWebParms.setAbbrev("SW");
				custWebParms.setStoreComp("SW");
				custWebParms.setColorComp("SHERWIN-WILLIAMS");
				custWebParms.setProdComp("SW");
				custWebParms.setClrntSysId(cust.getClrntSysId());
				custWebParms.setActive(cust.isActive());
				custWebParms.setSwuiTyp("SW");
				custWebParms.setSwuiTitle(cust.getSwuiTitle());
				custWebParms.setAltProdComp1("SWMZDP");
				custWebParms.setFormRule("2DK");
				custWebParms.setBulkDeep(true);
				custWebParms.setBulkDn("6,4");
				custWebParms.setBulkUp("10,12,14");
				custWebParms.setBulkStart("8");
				custWebParms.setColorPrime(true);
				custWebParms.setOpacityCtrl(true);
				custWebParms.setFormQtrShot(true);
				custWebParms.setTargetCr2(true);
				custWebParms.setCdsAdlFld(cust.getCdsAdlFld());
				custWebParms.setLastUpd(sqlDate);
				customerList.add(custWebParms);
			}
			
			//map session values to custwebjobfields domain object
			for(JobFields job : reqObj.getJobFieldList()) {
				CustWebJobFields custWebJobs = new CustWebJobFields();
				custWebJobs.setCustomerId(reqObj.getCustomerId());
				custWebJobs.setSeqNbr(job.getSeqNbr());
				custWebJobs.setScreenLabel(job.getScreenLabel());
				custWebJobs.setFieldDefault(job.getFieldDefault());
				custWebJobs.setEntryRequired(job.isEntryRequired());
				custWebJobs.setActive(job.isActive());
				jobList.add(custWebJobs);
			}
			
			if(reqObj.getLoginList() != null) {
				//map session values to custweblogintransform domain object
				for(LoginTrans login : reqObj.getLoginList()) {
					CustWebLoginTransform custWebLogin = new CustWebLoginTransform();
					custWebLogin.setCustomerId(reqObj.getCustomerId());
					custWebLogin.setKeyField(login.getKeyField());
					custWebLogin.setMasterAcctName(login.getMasterAcctName());
					custWebLogin.setAcctComment(login.getAcctComment());
					loginList.add(custWebLogin);
				}
			}
			
			List<CustWebParms> existingRecords = customerService.getAllCustWebParms(reqObj.getCustomerId());
			List<CustWebLoginTransform> existingLogins = customerService.getCustLoginTrans(reqObj.getCustomerId());
			List<CustWebJobFields> existingJobs = customerService.getCustJobFields(reqObj.getCustomerId());
			
			
			if(existingRecords.isEmpty()) {
				//create parms records
				for(CustWebParms record : customerList) {
					customerService.createCustWebParmsRecord(record);
				}
			} else {
				//'update' parms records
				for(CustWebParms exrecord : existingRecords) {
					customerService.deleteCustWebParmsRecord(exrecord);
				}
				for(CustWebParms record : customerList) {
					customerService.createCustWebParmsRecord(record);
				}
			}
			
			if(!loginList.isEmpty()) {
				if(existingLogins.isEmpty()) {
					//create login transform records
					for(CustWebLoginTransform logtran : loginList) {
						customerService.createCustWebLoginTransRecord(logtran);
					}
				} else {
					//'update' login transform records
					for(CustWebLoginTransform exlogin : existingLogins) {
						customerService.deleteCustWebLoginTransRecord(exlogin);
					}
					for(CustWebLoginTransform logtran : loginList) {
						customerService.createCustWebLoginTransRecord(logtran);
					}
				}
			}
			
			if(existingJobs.isEmpty()) {
				//create job fields records
				for(CustWebJobFields jobfld : jobList) {
					customerService.createCustWebJobFieldsRecord(jobfld);
				}
			} else {
				//'update' job fields records
				for(CustWebJobFields exjob : existingJobs) {
					customerService.deleteCustWebJobFieldsRecord(exjob);
				}
				for(CustWebJobFields jobfld : jobList) {
					customerService.createCustWebJobFieldsRecord(jobfld);
				}
			}
			sessionMap.clear();
			crudmsg = "Save Successful";
			sessionMap.put("msg", crudmsg);
			
			return SUCCESS;
			
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public String delete() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			//check for existing records
			List<CustWebParms> existingRecords = customerService.getAllCustWebParms(reqObj.getCustomerId());
			List<CustWebLoginTransform> existingLogins = customerService.getCustLoginTrans(reqObj.getCustomerId());
			List<CustWebJobFields> existingJobfields = customerService.getCustJobFields(reqObj.getCustomerId());
			
			if(!reqObj.isHistory()) {
				if(!existingRecords.isEmpty()) {
					for(CustWebParms record : existingRecords) {
						customerService.deleteCustWebParmsRecord(record);
					}
				}
				if(!existingLogins.isEmpty()) {
					for(CustWebLoginTransform logins : existingLogins) {
						customerService.deleteCustWebLoginTransRecord(logins);
					}
				}
				if(!existingJobfields.isEmpty()) {
					for(CustWebJobFields jobfields : existingJobfields) {
						customerService.deleteCustWebJobFieldsRecord(jobfields);
					}
				}
				sessionMap.clear();
				crudmsg = "Delete Successful";
				sessionMap.put("msg", crudmsg);
			}else {
				crudmsg = "Delete Unsuccessful";
				sessionMap.put("msg", crudmsg);
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
	
	public String setInactive() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			List<CustWebParms> existingRecords = customerService.getAllCustWebParms(reqObj.getCustomerId());
	
			for(CustWebParms record : existingRecords) {
				record.setActive(false);
				customerService.updateCustWebParmsRecord(record);
			}
			
			sessionMap.clear();
			crudmsg = "Customer has been set to inactive";
			sessionMap.put("msg", crudmsg);
			
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
				newString = escapedString.replace("&amp;", "&");
			} else if(escapedString.contains("&#38;")) {
				newString = escapedString.replaceAll("&#38;", "&");
			} else if(escapedString.contains("&apos;")) {
				newString = escapedString.replace("&apos;", "'");
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
	
	public String getLookupCustomerId() {
		return lookupCustomerId;
	}
	public void setLookupCustomerId(String lookupCustomerId) {
		this.lookupCustomerId = lookupCustomerId;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}

	public String getCrudmsg() {
		return crudmsg;
	}

	public void setCrudmsg(String crudmsg) {
		this.crudmsg = crudmsg;
	}

}
