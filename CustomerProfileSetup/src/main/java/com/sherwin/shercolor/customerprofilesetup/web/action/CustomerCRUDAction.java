package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.sherwin.shercolor.common.domain.Eula;
import com.sherwin.shercolor.common.domain.EulaHist;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.EulaService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;
import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;
import com.sherwin.shercolor.customerprofilesetup.web.model.RequestObject;

public class CustomerCRUDAction extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CustomerCRUDAction.class);
	private Map<String, Object> sessionMap;
	private String lookupCustomerId;
	private String crudmsg;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	TranHistoryService tranHistoryService;
	
	@Autowired
	EulaService eulaService;
	
	public String read() {
		try {
			List<String> clrntlist = new ArrayList<String>();
			List<CustParms> custList = new ArrayList<CustParms>();
			List<JobFields> jobFieldList = new ArrayList<JobFields>();
			List<LoginTrans> loginList = new ArrayList<LoginTrans>();
			
			List<CustWebParms> custParms = customerService.getAllCustWebParms(lookupCustomerId);
			
			RequestObject reqObj = new RequestObject();
			
			reqObj.setExistingCustomer(true);
						
			reqObj.setUpdateMode(true);
			//check for job history
			List<CustWebTran> jobHistory = tranHistoryService.getCustomerJobs(lookupCustomerId);
			
			if(jobHistory.isEmpty()) {
				reqObj.setHistory(false);
				System.out.println("Job history not found");
			}else {
				reqObj.setHistory(true);
				System.out.println("Job history found");
			}
			//map custwebparms to model object
			for(CustWebParms webparms : custParms) {
				CustParms cp = new CustParms();
				cp.setCustomerId(webparms.getCustomerId());
				if(webparms.getCustomerId().length()==9) {
					reqObj.setAccttype("natlWdigits");
				}else {
					reqObj.setAccttype("loginRequired");
				}
				cp.setSeqNbr(webparms.getSeqNbr());
				cp.setSwuiTitle(webparms.getSwuiTitle());
				cp.setCdsAdlFld(webparms.getCdsAdlFld());
				cp.setClrntSysId(webparms.getClrntSysId());
				clrntlist.add(webparms.getClrntSysId());
				cp.setActive(webparms.isActive());
				custList.add(cp);
			}
			
			reqObj.setCustList(custList);
			reqObj.setCustomerId(allowCharacters(custParms.get(0).getCustomerId()));
			reqObj.setSwuiTitle(allowCharacters(custParms.get(0).getSwuiTitle()));
			reqObj.setCdsAdlFld(allowCharacters(custParms.get(0).getCdsAdlFld()));
			reqObj.setActive(custParms.get(0).isActive());
			reqObj.setClrntList(clrntlist);
			
			List<CustWebJobFields> jobFields = customerService.getCustJobFields(lookupCustomerId);
			
			//map custwebjobfields to model object
			for(CustWebJobFields jobs : jobFields) {
				JobFields fields = new JobFields();
				fields.setSeqNbr(jobs.getSeqNbr());
				fields.setScreenLabel(jobs.getScreenLabel());
				fields.setFieldDefault(jobs.getFieldDefault());
				fields.setEntryRequired(jobs.isEntryRequired());
				fields.setActive(jobs.isActive());
				jobFieldList.add(fields);
			}
			
			reqObj.setJobFieldList(jobFieldList);
			
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
			
			List<EulaHist> scwEulaList = eulaService.eulaHistListForCustomerIdWebSite("CUSTOMERSHERCOLORWEB", lookupCustomerId);
			
			if(scwEulaList != null) {
				List<EulaHist> ehlist = new ArrayList<EulaHist>();
				for(EulaHist eh : scwEulaList) {
					if(eh.getActionType().equals("TOACTIVATE")) {
						ehlist.add(0, eh);
						reqObj.setToactivateRecord(true);
					} else {
						ehlist.add(eh);
					}
				}
				reqObj.setWebsite("CUSTOMERSHERCOLORWEB");
				reqObj.setEulaHistList(ehlist);
			} 
			
			Eula sherColorWebEula = eulaService.readActive("CUSTOMERSHERCOLORWEB", lookupCustomerId);
			
			if(sherColorWebEula.getCustomerId() != null) {
				reqObj.setEula(sherColorWebEula);
				reqObj.setUploadedEula(true);
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
			
			List<CustWebParms> existingRecords = customerService.getAllCustWebParms(reqObj.getCustomerId());
			List<CustWebLoginTransform> existingLogins = customerService.getCustLoginTrans(reqObj.getCustomerId());
			List<CustWebJobFields> existingJobs = customerService.getCustJobFields(reqObj.getCustomerId());
			
			List<CustWebParms> customerList = new ArrayList<CustWebParms>();
			List<CustWebJobFields> jobList = new ArrayList<CustWebJobFields>();
			List<CustWebLoginTransform> loginList = new ArrayList<CustWebLoginTransform>();
			
			boolean result = false;
			boolean saveOrUpdateCwp = false;
			boolean saveOrUpdateCwjf = false;
			boolean saveOrUpdateCwlt = false;
			
			if(!reqObj.isCustUnchanged()) {
				
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
				
				if(reqObj.isNewCustomer() || reqObj.isCustEdited()) {
					//create new CustWebParms records
					saveOrUpdateCwp = true;
				} else if(reqObj.isCustDeleted() || reqObj.isCustAdded()) {
					// customer already exists and
					// one or more (not all) records have been deleted or added
					// delete existing records, then recreate CustWebParms records
					result = customerService.deleteAllCustWebParms(existingRecords);
					if(!result) {
						addActionError("Error - Unable to update CustWebParms record(s)");
						return INPUT;
					}
					saveOrUpdateCwp = true;
				} else {
					addActionError("Error - Unable to create customer. Customer ID already exists");
					return INPUT;
				}
			}
			
			//create EulaHist record to activate eula
			if(reqObj.getEulaHistToActivate() != null) {
				// generate 6 digit random acceptance code
				//String ac = generateRandomDigit(6);
				//System.out.println("acceptance code = " + ac);
				
				EulaHist eh = reqObj.getEulaHistToActivate();
				//eh.setAcceptanceCode(ac);
				
				eulaService.createEulaHist(eh);
			}
			
			// create eula pdf
			if(reqObj.getEula() != null) {
				eulaService.createEula(reqObj.getEula());
			}
			
			if(!reqObj.isJobUnchanged()) {
				
				// map model object to domain object
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
				
				if(reqObj.isNewCustomer() || reqObj.isJobEdited()) {
					// if customer does not have existing records
					// or existing records can be updated
					saveOrUpdateCwjf = true;
				} else if(reqObj.isJobAdded() || reqObj.isJobDeleted()) {
					// one or more records have been deleted or added
					// delete existing records, then recreate CustWebJobFields records
					result = customerService.deleteAllCustWebJobFields(existingJobs);
					if(!result) {
						addActionError("Error - Unable to update CustWebJobFields record(s)");
						return INPUT;
					}
					saveOrUpdateCwjf = true;
				} else {
					addActionError("Error - Unable to change CustWebJobFields record(s)");
					return INPUT;
				}
			}
			
			if(!reqObj.isLoginUnchanged()) {
				
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
					if(reqObj.isNewCustomer() || reqObj.isLoginEdited()) {
						//create login transform records
						saveOrUpdateCwlt = true;
					} else if(reqObj.isLoginAdded() || reqObj.isLoginDeleted()) {
						// one or more records have been deleted
						// delete existing records, then
						// recreate CustWebLoginTransform records
						result = customerService.deleteAllCustWebLoginTrans(existingLogins);
						if(!result) {
							addActionError("Error - Unable to update CustWebLoginTransform record(s)");
							return INPUT;
						}
						saveOrUpdateCwlt = true;
					} else {
						addActionError("Error - Unable to change CustWebLoginTransform record(s)");
						return INPUT;
					}
				}
			}
			
			if(saveOrUpdateCwp || saveOrUpdateCwjf || saveOrUpdateCwlt) {
				if(!saveOrUpdateCwp) customerList = null;
				if(!saveOrUpdateCwjf) jobList = null;
				if(!saveOrUpdateCwlt) loginList = null;
				
				result = customerService.saveOrUpdateAllCustWebData(customerList, jobList, loginList);
				
				if(!result) {
					addActionError("Error - Unable to save CustWeb Data");
					return INPUT;
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
			
			
			// check if job history exists
			if(!reqObj.isHistory()) {
				// check if a EULA has been signed
				if(reqObj.getEulaHistToActivate() == null && (reqObj.getEulaHistList() != null && reqObj.getEulaHistList().get(0).isActiveAcceptanceCode() || reqObj.getEulaHistList() == null)) {
					
					boolean result = true;
					
					if(!existingRecords.isEmpty()) {
						result = customerService.deleteAllCustWebParms(existingRecords);
						if(!result) {
							addActionError("Error - Unable to delete CustWebParms record(s)");
							return INPUT;
						}
					}
					if(!existingLogins.isEmpty()) {
						result = customerService.deleteAllCustWebLoginTrans(existingLogins);
						if(!result) {
							addActionError("Error - Unable to delete CustWebLoginTransform record(s)");
							return INPUT;
						}
					}
					if(!existingJobfields.isEmpty()) {
						result = customerService.deleteAllCustWebJobFields(existingJobfields);
						if(!result) {
							addActionError("Error - Unable to delete CustWebJobFields record(s)");
							return INPUT;
						}
					}
					// check for toactivate record
					if(reqObj.getEulaHistToActivate() != null || (reqObj.getEulaHistList() != null && reqObj.getEulaHistList().get(0).isActiveAcceptanceCode())) {
						EulaHist eulaHistToActivate = reqObj.getEulaHistToActivate();
						EulaHist eulaHistListToActivate = reqObj.getEulaHistList().get(0);
						
						if(eulaHistToActivate == null) {
							result = eulaService.deleteEulaHist(eulaHistListToActivate);
						} else {
							result = eulaService.deleteEulaHist(eulaHistToActivate);
						}
						
						if(!result) {
							addActionError("Error - Unable to delete EulaHist TOACTIVATE record");
							return INPUT;
						}
					}
					// check for eula pdf record
					if(reqObj.getEula() != null) {
						result = eulaService.deleteEula(reqObj.getEula());
						if(!result) {
							addActionError("Error - Unable to delete Eula record");
							return INPUT;
						}
					}
					
					sessionMap.clear();
					crudmsg = "Delete Successful";
					sessionMap.put("msg", crudmsg);
					
				} else {
					crudmsg = "Delete Unsuccessful";
					sessionMap.put("msg", crudmsg);
				}
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
			List<CustWebParms> updatedRecords = new ArrayList<CustWebParms>();
			
			for(CustWebParms record : existingRecords) {
				record.setActive(false);
				updatedRecords.add(record);
			}
			
			customerService.saveOrUpdateAllCustWebData(updatedRecords, null, null);
			
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
	
	public List<EulaHist> createEulaHistList(List<EulaHist> eulaHistList) {
		List<EulaHist> ehlist = new ArrayList<EulaHist>();
		// create eulahist list with toactivate record first
		for(EulaHist eh : eulaHistList) {
			if(eh.getActionType().equals("TOACTIVATE")) {
				ehlist.add(0, eh);
			} else {
				ehlist.add(eh);
			}
		}
		return ehlist;
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
	
	/*public String generateRandomDigit(int length) {
		Random rand = new Random();
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; i++) {
			int randInt = rand.nextInt(9) + 1;
			sb.append(randInt);
		}
		return sb.toString();
	}*/

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

	public String getCrudmsg() {
		return crudmsg;
	}

	public void setCrudmsg(String crudmsg) {
		this.crudmsg = crudmsg;
	}

}
