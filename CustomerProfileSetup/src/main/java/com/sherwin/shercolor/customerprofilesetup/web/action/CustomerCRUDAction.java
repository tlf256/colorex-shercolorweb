package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebCustomerProfile;
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
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustProfile;
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
			
			String custId = custParms.get(0).getCustomerId();
			
			RequestObject reqObj = new RequestObject();
			
			reqObj.setExistingCustomer(true);
						
			reqObj.setUpdateMode(true);
			//check for job history
			List<CustWebTran> jobHistory = tranHistoryService.getCustomerJobs(lookupCustomerId);
			
			if(jobHistory.isEmpty()) {
				reqObj.setHistory(false);
				logger.debug("Job history not found");
			}else {
				reqObj.setHistory(true);
				logger.debug("Job history found");
			}
			//map custwebparms to model object
			for(CustWebParms webparms : custParms) {
				CustParms cp = new CustParms();
				cp.setCustomerId(webparms.getCustomerId());
				cp.setSeqNbr(webparms.getSeqNbr());
				cp.setSwuiTitle(webparms.getSwuiTitle());
				cp.setCdsAdlFld(webparms.getCdsAdlFld());
				cp.setClrntSysId(webparms.getClrntSysId());
				clrntlist.add(webparms.getClrntSysId());
				cp.setActive(webparms.isActive());
				custList.add(cp);
			}
			
			reqObj.setCustList(custList);
			reqObj.setAccttype(getAcctType(custId));
			reqObj.setCustomerId(custId);
			reqObj.setSwuiTitle(allowCharacters(custParms.get(0).getSwuiTitle()));
			reqObj.setCdsAdlFld(allowCharacters(custParms.get(0).getCdsAdlFld()));
			reqObj.setActive(custParms.get(0).isActive());
			reqObj.setClrntList(clrntlist);
			
			List<CustWebJobFields> jobFields = customerService.getCustJobFields(lookupCustomerId);
			//6-14-21 first check if jobFields is null or empty 
			//since some accounts will be set up without them now
			if(jobFields != null && !jobFields.isEmpty()) {
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
			}
			
			List<CustWebLoginTransform> loginTrans = customerService.getCustLoginTrans(lookupCustomerId);
			//if customer has login ids, map custweblogintransform to model object
			if(loginTrans != null && !loginTrans.isEmpty()) {
				for(CustWebLoginTransform logtran : loginTrans) {
					LoginTrans trans = new LoginTrans();
					trans.setKeyField(allowCharacters(Encode.forHtml(logtran.getKeyField())));
					trans.setMasterAcctName(allowCharacters(Encode.forHtml(logtran.getMasterAcctName())));
					trans.setAcctComment(allowCharacters(Encode.forHtml(logtran.getAcctComment())));
					loginList.add(trans);
				}
				
				reqObj.setLoginList(loginList);
			}
			
			// get CustWebCustomerProfile record
			CustWebCustomerProfile custProfile = customerService.getCustWebCustomerProfile(lookupCustomerId);
			
			if(custProfile != null) {
				CustProfile profile = new CustProfile();
				profile.setCustType(custProfile.getCustomerType());
				profile.setUseRoomByRoom(custProfile.isUseRoomByRoom());
				profile.setUseLocatorId(custProfile.isUseLocatorId());
				reqObj.setProfile(profile);
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
	
		} catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.getMessage(), e);
			return ERROR;
		}
		
	}
	
	private String getAcctType(String custId) {
		String acctType = "";
		
		switch(custId.length()) {
		case 6:
			if(custId.substring(0, 2).equals("99")) {
				acctType = "natlWOdigits";
			} else {
				acctType = "intnatlCostCntr";
			}
			break;
		case 7:
			acctType = "intnatlWdigits";
			break;
		case 8:
			acctType = "intnatlWOdigits";
			break;
		case 9:
			acctType = "natlWdigits";
			break;
		default:
			//unexpected custId length
			logger.error("unrecognized customer ID");
		}
		
		return acctType;
	}

	public String createOrUpdate() {
		try {			
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			List<CustWebParms> existingRecords = customerService.getAllCustWebParms(reqObj.getCustomerId());
			List<CustWebLoginTransform> existingLogins = customerService.getCustLoginTrans(reqObj.getCustomerId());
			List<CustWebJobFields> existingJobs = customerService.getCustJobFields(reqObj.getCustomerId());
			//CustWebCustomerProfile existingProfile = customerService.getCustWebCustomerProfile(reqObj.getCustomerId());
			
			List<CustWebParms> customerList = new ArrayList<CustWebParms>();
			List<CustWebJobFields> jobList = new ArrayList<CustWebJobFields>();
			List<CustWebLoginTransform> loginList = new ArrayList<CustWebLoginTransform>();
			CustWebCustomerProfile profile = new CustWebCustomerProfile();
			
			// boolean for result of CRUD operations
			boolean result = false;
			// 
			boolean saveOrUpdateCwp = false;
			boolean saveOrUpdateCwjf = false;
			boolean saveOrUpdateCwlt = false;
			// CustWebCustomerProfile can only be saved
			boolean saveCwcp = false;
			// boolean to determine whether records need to be deleted then recreated.
			// Set only if a record has been added or deleted from result set.
			// not applicable to CustWebCustomerProfile records.
			boolean deleteCwp = false;
			boolean deleteCwjf = false;
			boolean deleteCwlt = false;
			
			if(!reqObj.isCustUnchanged()) {
				logger.info("CustWebParms records have been modified or need to be created");
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
				
				if(reqObj.isCustDeleted() || reqObj.isCustAdded()) {
					// customer already exists and
					// one or more records have been deleted or added
					// delete existing records, then recreate CustWebParms records
					deleteCwp = true;
					saveOrUpdateCwp = true;
					logger.info("CustWebParms record has been added or deleted");
				} else {
					// if there haven't been any records added or deleted
					// and the data has been changed or it is a new
					// customer, then save or update
					saveOrUpdateCwp = true;
				}
			}
			
			// check if CustWebCustomerProfile records need saved
			if(reqObj.getProfile() != null) {
				// customer type CUSTOMER does not get a CustWebCustomerProfile record
				if(!reqObj.getProfile().getCustType().equals("CUSTOMER")) {
					saveCwcp = true;
					profile = mapCustomerProfile(reqObj.getProfile(), reqObj.getCustomerId());
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
				logger.info("Created EulaHist TOACTIVATE record");
			}
			
			// create eula pdf
			if(reqObj.getEula() != null) {
				eulaService.createEula(reqObj.getEula());
				logger.info("Created Eula record");
			}
			
			if(!reqObj.isJobUnchanged()) {
				logger.info("CustWebJobFields records have been modified or need to be created");
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
				
				if(reqObj.isJobAdded() || reqObj.isJobDeleted()) {
					// one or more records have been deleted or added
					// delete existing records, then recreate CustWebJobFields records
					deleteCwjf = true;
					saveOrUpdateCwjf = true;
					logger.info("CustWebJobFields records have been added or deleted");
				} else {
					// if there haven't been any records added or deleted
					// and the data has been changed or it is a new
					// customer, then save or update
					saveOrUpdateCwjf = true;
				}
			}
			
			if(!reqObj.isLoginUnchanged()) {
				logger.info("CustWebLoginTransform records have been modified or need to be created");
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
					if(reqObj.isLoginAdded() || reqObj.isLoginDeleted()) {
						// one or more records have been deleted
						// delete existing records, then
						// recreate CustWebLoginTransform records
						deleteCwlt = true;
						saveOrUpdateCwlt = true;
						logger.info("CustWebLoginTransform records have been added or deleted");
					} else {
						// if there haven't been any records added or deleted
						// and the data has been changed or it is a new
						// customer, then save or update
						saveOrUpdateCwlt = true;
					}
				}
			}
			
			// set empty data to null if records 
			// do not need saved, updated or deleted
			if(!deleteCwp) existingRecords = null;
			if(!deleteCwjf) existingJobs = null;
			if(!deleteCwlt) existingLogins = null;
			if(!saveOrUpdateCwp) customerList = null;
			if(!saveOrUpdateCwjf) jobList = null;
			if(!saveOrUpdateCwlt) loginList = null;
			if(!saveCwcp) profile = null;
			
			// check if data has been modified or new data needs saved
			if(deleteCwp || deleteCwjf || deleteCwlt) {
				// One or more customer records have been either deleted or added to existing records.
				// Delete, then save all data which has not been set to null.
				// Currently, existing CustWebCustomerProfile records cannot be modified 
				// through this application, so pass null as CustWebCustomerProfile data
				result = modifyCustWebData(customerList, jobList, loginList, null, existingRecords, existingJobs, existingLogins);
				logger.info("Result of modification of CustomerSherColor Web customer data is " + result);
				if(!result) {
					addActionError("Error - Unable to modify CustWeb Data");
				}
				
			} else if((saveOrUpdateCwp || saveOrUpdateCwjf || saveOrUpdateCwlt || saveCwcp) && 
					(!deleteCwp && !deleteCwjf && !deleteCwlt)) {
				// save or update all data which has not been set to null
				result = customerService.saveOrUpdateAllCustWebData(customerList, jobList, loginList, profile);
				logger.info("Result of creation of CustomerSherColor Web customer data is " + result);
				if(!result) {
					addActionError("Error - Unable to save CustWeb Data");
				}
			} else {
				addActionError("Error - CustWeb Data has not been modified or created");
			}
			
			sessionMap.clear();
			crudmsg = "Save Successful";
			sessionMap.put("msg", crudmsg);
			
			return SUCCESS;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private CustWebCustomerProfile mapCustomerProfile(CustProfile profile, String customerId) {
		CustWebCustomerProfile cwcp = new CustWebCustomerProfile();
		cwcp.setCustomerId(customerId);
		cwcp.setCustomerType(profile.getCustType());
		cwcp.setUseRoomByRoom(profile.isUseRoomByRoom());
		cwcp.setUseLocatorId(profile.isUseLocatorId());
		
		return cwcp;
	}
	
	public String delete() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			//check for existing records
			List<CustWebParms> existingRecords = customerService.getAllCustWebParms(reqObj.getCustomerId());
			List<CustWebLoginTransform> existingLogins = customerService.getCustLoginTrans(reqObj.getCustomerId());
			List<CustWebJobFields> existingJobfields = customerService.getCustJobFields(reqObj.getCustomerId());
			CustWebCustomerProfile existingProfile = customerService.getCustWebCustomerProfile(reqObj.getCustomerId());
			
			// check if job history exists
			if(!reqObj.isHistory()) {
				// check if a EULA has been signed
				if(reqObj.getEulaHistToActivate() == null && (reqObj.getEulaHistList() != null && reqObj.getEulaHistList().get(0).isActiveAcceptanceCode() || reqObj.getEulaHistList() == null)) {
					
					boolean result = true;
					
					// no job history and EULA has not been signed
					result = customerService.deleteAllCustWebData(existingRecords, existingJobfields, existingLogins, existingProfile);
					
					if(!result) {
						addActionError("Error - Unable to delete Customer record(s), please contact administrator");
					}
					
					// EULA records deleted through EulaService, not CustomerService
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
						}
					}
					// check for eula pdf record
					if(reqObj.getEula() != null) {
						result = eulaService.deleteEula(reqObj.getEula());
						if(!result) {
							addActionError("Error - Unable to delete Eula record");
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
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
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
			
			customerService.saveOrUpdateAllCustWebData(updatedRecords, null, null, null);
			
			sessionMap.clear();
			crudmsg = "Customer has been set to inactive";
			sessionMap.put("msg", crudmsg);
			
			return SUCCESS;
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
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
	
	public boolean modifyCustWebData(List<CustWebParms> custList, List<CustWebJobFields> jobFieldsList, List<CustWebLoginTransform> loginList,
			CustWebCustomerProfile profile, List<CustWebParms> existingParmsList, List<CustWebJobFields> existingJFList, List<CustWebLoginTransform> existingLoginList) {
		boolean result = false;
		
		// modified CustWebCustomerProfile does not need to be deleted, only updated, if necessary
		// All other modified CustomerSherColorWeb customer data first needs deleted then saved
		result = customerService.deleteAllCustWebData(existingParmsList, existingJFList, existingLoginList, null);
		
		result = customerService.saveOrUpdateAllCustWebData(custList, jobFieldsList, loginList, profile);
		
		return result;
		
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
