package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import com.sherwin.shercolor.common.domain.Eula;
import com.sherwin.shercolor.common.domain.EulaHist;
import com.sherwin.shercolor.common.service.EulaService;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustProfile;
import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;
import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;
import com.sherwin.shercolor.customerprofilesetup.web.model.Customer;
import com.sherwin.shercolor.customerprofilesetup.web.model.Job;
import com.sherwin.shercolor.customerprofilesetup.web.model.Login;
import com.sherwin.shercolor.customerprofilesetup.web.model.RequestObject;

public class EditCustomerAction extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(EditCustomerAction.class);
	private Job job;
	private Login login;
	private Customer cust;
	private boolean edited;
	private Map<String, Object> sessionMap;
	private File eulafile;
	private Date effDate;
	private Date expDate;
	private String eulaText;
	
	@Autowired
	EulaService eulaService;
	
	public String execute() {
		try {
			List<CustParms> editCustList = new ArrayList<CustParms>();
			List<String> editclrntlist = new ArrayList<String>();
			List<LoginTrans> editLoginList = new ArrayList<LoginTrans>();
			List<JobFields> editJobList = new ArrayList<JobFields>();
			
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			setEdited(true);
			
			for(int i = 0; i < cust.getClrntList().size(); i++) {
				if(cust.getDefaultClrntSys().contains(cust.getClrntList().get(i))) {
					editclrntlist.add(0, cust.getClrntList().get(i));
				} else {
					editclrntlist.add(cust.getClrntList().get(i));
				}
			}
			if(cust.getCce()!=null) {
				if(cust.getDefaultClrntSys().contains("cce")) {
					editclrntlist.add(0, "CCE");
				} else {
					editclrntlist.add("CCE");
				}
			}
			if(cust.getBac()!=null) {
				if(cust.getDefaultClrntSys().contains("bac")) {
					editclrntlist.add(0, "BAC");
				} else {
					editclrntlist.add("BAC");
				}
			}
			if(cust.getEff()!=null) {
				if(cust.getDefaultClrntSys().contains("eff")) {
					editclrntlist.add(0, "844");
				} else {
					editclrntlist.add("844");
				}
			}
			
			reqObj.setClrntList(editclrntlist);
			reqObj.setSwuiTitle(allowCharacters(cust.getSwuiTitle()));
			reqObj.setCdsAdlFld(allowCharacters(cust.getCdsAdlFld()));
			reqObj.setActive(cust.isActive());
			
			for(int i = 0; i < reqObj.getClrntList().size(); i++) {
				//set values for custwebparms record
				CustParms newcust = new CustParms();
				newcust.setCustomerId(reqObj.getCustomerId());
				newcust.setSwuiTitle(reqObj.getSwuiTitle());
				newcust.setClrntSysId(reqObj.getClrntList().get(i));
				newcust.setSeqNbr(i+1);
				newcust.setCdsAdlFld(reqObj.getCdsAdlFld());
				newcust.setActive(reqObj.isActive());
				editCustList.add(newcust);
			}
			
			List<CustParms> resultCustList = new ArrayList<CustParms>();
			
			if(reqObj.getCustList().size() == editCustList.size()) {
				int index = 0;
				// check if data in edited list and 
				// original list match
				for(CustParms cp1 : editCustList) {
					CustParms cp2 = reqObj.getCustList().get(index);
					if(!cp1.getCustomerId().equals(cp2.getCustomerId()) || !cp1.getSwuiTitle().equals(cp2.getSwuiTitle()) || cp1.isActive() != cp2.isActive() || 
							cp1.getSeqNbr() != cp2.getSeqNbr() || !cp1.getCdsAdlFld().equals(cp2.getCdsAdlFld()) || !cp1.getClrntSysId().equals(cp2.getClrntSysId())) {
						resultCustList.add(cp1);
					}
					index++;
				}
			} else if(reqObj.getCustList().size() > editCustList.size()) {
				reqObj.setCustDeleted(true);
				reqObj.setCustList(editCustList);
			} else if(reqObj.getCustList().size() < editCustList.size()) {
				reqObj.setCustAdded(true);
				reqObj.setCustList(editCustList);
			}
			
			if(!resultCustList.isEmpty()) {
				reqObj.setCustList(editCustList);
				reqObj.setCustEdited(true);
			} else {
				if(!reqObj.isNewCustomer() && !reqObj.isCustAdded() && !reqObj.isCustDeleted()) {
					reqObj.setCustUnchanged(true);
				}
			}
			
			if(cust.getCustType() != "CUSTOMER") {
				reqObj.setProfile(mapCustProfile(cust));
			}
			
			if(eulafile != null) {
				// first check if customer already has an associated eula
				Eula activeEula = eulaService.readActive("CUSTOMERSHERCOLORWEB", reqObj.getCustomerId());
				
				if(activeEula.getCustomerId() != null) {
					addFieldError("custediterror", "EULA for " + activeEula.getCustomerId() + " already exists");
				}
				
				byte[] filebytes = readBytesFromFile(eulafile);
				
				Eula eula = new Eula();
				eula.setCustomerId(reqObj.getCustomerId());
				eula.setWebSite("CUSTOMERSHERCOLORWEB");
				eula.setSeqNbr(1);
				eula.setEffectiveDate(effDate);
				eula.setExpirationDate(expDate);
				eula.setEulaText1(eulaText);
				eula.setEulaPdf(filebytes);
				
				reqObj.setEula(eula);
			}
			
			if(reqObj.getEulaHistList() == null) {
				// no eula history
				
				// check if activate eula selected
				if(!cust.getWebsite().equals("None")) {
					List<EulaHist> ehlist = new ArrayList<EulaHist>();
					
					EulaHist eh = new EulaHist();
					
					Eula sherColorWebEula = eulaService.readActive("CUSTOMERSHERCOLORWEB", reqObj.getCustomerId());
					
					if(cust.getWebsite().equals("SherColor Web EULA") || cust.getWebsite().equals("Custom EULA")) {
						eh = activateEula(reqObj.getCustomerId(), cust.getAcceptCode(), sherColorWebEula);
						ehlist.add(0, eh);
						reqObj.setWebsite(sherColorWebEula.getWebSite());
						reqObj.setSeqNbr(sherColorWebEula.getSeqNbr());
					} else {
						//unexpected value
						addFieldError("custediterror", "Please select Eula from list");
					}
					
					reqObj.setEulaHistToActivate(eh);
					reqObj.setEulaHistList(ehlist);
				}
				
			} else {
				// eula history, includes recent toactivate record
				
				if(reqObj.getEulaHistToActivate() == null) {
					// TODO eula history, no recent toactivate record
					// option to create another toactivate record?
					
				} else {
					// eula history, recent toactivate record
					// check for true/false
					
					if(!cust.isActivateEula()) {
						// if recently added toactivate eula record is unchecked
						// remove record from request object
						reqObj.setEulaHistToActivate(null);
						// check if eulahistlist contains only toactivate record
						if(reqObj.getEulaHistList().size() > 1) {
							// TODO previous eula history with new toactivate record
						} else {
							reqObj.setEulaHistList(null);
						}
						
					}
				}
			}
			
			if(login!=null) {
				for(int i = 0; i < login.getKeyField().size(); i++) {
					if(!login.getKeyField().get(i).equals("")) {
						//set values for custweblogintrans record
						LoginTrans newlogin = new LoginTrans();
						newlogin.setKeyField(allowCharacters(login.getKeyField().get(i)));
						newlogin.setMasterAcctName(allowCharacters(login.getMasterAcctName().get(i)));
						newlogin.setAcctComment(allowCharacters(login.getAcctComment().get(i)));
						editLoginList.add(newlogin);
					}
				}
				
				if(reqObj.getLoginList() != null) {
					// customer had login ids previously set
					List<LoginTrans> resultLoginList = new ArrayList<LoginTrans>();
					
					if(reqObj.getLoginList().size() == editLoginList.size()) {
						int index = 0;
						// check if data in edited list and 
						// original list match
						for(LoginTrans lt1 : editLoginList) {
							LoginTrans lt2 = reqObj.getLoginList().get(index);
							if(!lt1.getKeyField().equals(lt2.getKeyField()) || !lt1.getMasterAcctName().equals(lt2.getMasterAcctName())
									|| !lt1.getAcctComment().equals(lt2.getAcctComment())) {
								resultLoginList.add(lt1);
							}
							index++;
						}
					} else if(reqObj.getLoginList().size() > editLoginList.size()) {
						reqObj.setLoginDeleted(true);
						reqObj.setLoginList(editLoginList);
					} else if(reqObj.getLoginList().size() < editLoginList.size()) {
						reqObj.setLoginAdded(true);
						reqObj.setLoginList(editLoginList);
					}
					
					if(!resultLoginList.isEmpty()) {
						reqObj.setLoginDeleted(true);
						reqObj.setLoginList(editLoginList);
						reqObj.setLoginEdited(true);
					} else {
						if(!reqObj.isNewCustomer() && !reqObj.isLoginAdded() && !reqObj.isLoginDeleted()) {
							reqObj.setLoginUnchanged(true);
						}
					}
				} else {
					// customer did not have any previous login ids set
					reqObj.setLoginList(editLoginList);
				}
				
				
			} else {
				reqObj.setLoginUnchanged(true);
			}
			
			if(job!=null) {
				String[] reqlist = new String[10];
				String[] actvlist = new String[10];
				int start = 0;
				int end = 10;
				int seqnbr = 1;
				//initial fill of string arrays with default false
				Arrays.fill(reqlist, start, end, "false");
				Arrays.fill(actvlist, start, end, "false");
				//insert true at checked index
				if(job.getEntryRequired()!=null) {
					for(int index = 0; index < job.getEntryRequired().size(); index++) {
						reqlist[Integer.parseInt(job.getEntryRequired().get(index))] = "true";
					}
				}
				if(job.getActive()!=null) {
					for(int index = 0; index < job.getActive().size(); index++) {
						actvlist[Integer.parseInt(job.getActive().get(index))] = "true";
					}
				}
				
				for(int i = 0; i < job.getScreenLabel().size(); i++) {
					if(!job.getScreenLabel().get(i).equals("")) {
						//set values for custwebjobfields record
						JobFields newjob = new JobFields();
						newjob.setSeqNbr(seqnbr);
						newjob.setScreenLabel(allowCharacters(job.getScreenLabel().get(i)));
						newjob.setFieldDefault(allowCharacters(job.getFieldDefault().get(i)));
						if(reqlist[i].contains("true")) {
							newjob.setEntryRequired(true);
						} else {
							newjob.setEntryRequired(false);
						}
						if(actvlist[i].contains("true")) {
							newjob.setActive(true);
						} else {
							newjob.setActive(false);
						}
						editJobList.add(newjob);
						seqnbr++;
					}
				}
				
				List<JobFields> resultJobList = new ArrayList<JobFields>();
				
				if(reqObj.getJobFieldList().size() == editJobList.size()) {
					int index = 0;
					// check if data in edited list and 
					// original list match
					for(JobFields jf1 : editJobList) {
						JobFields jf2 = reqObj.getJobFieldList().get(index);
						if(!jf1.getScreenLabel().equals(jf2.getScreenLabel()) || jf1.getSeqNbr() != jf2.getSeqNbr() || !jf1.getFieldDefault().equals(jf2.getFieldDefault()) 
								|| jf1.isEntryRequired() != jf2.isEntryRequired() || jf1.isActive() != jf2.isActive()) {
							resultJobList.add(jf1);
						}	
						index++;
					}
				} else if(reqObj.getJobFieldList().size() > editJobList.size()) {
					reqObj.setJobDeleted(true);
					reqObj.setJobFieldList(editJobList);
				} else if(reqObj.getJobFieldList().size() < editJobList.size()) {
					reqObj.setJobAdded(true);
					reqObj.setJobFieldList(editJobList);
				}
				
				if(!resultJobList.isEmpty()) {
					reqObj.setJobFieldList(editJobList);
					reqObj.setJobEdited(true);
				} else {
					if(!reqObj.isNewCustomer() && !reqObj.isJobAdded() && !reqObj.isJobDeleted()) {
						reqObj.setJobUnchanged(true);
					}
				}
				
			} else {
				reqObj.setJobUnchanged(true);
			}
			
			sessionMap.put("CustomerDetail", reqObj);
			
			return SUCCESS;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private CustProfile mapCustProfile(Customer customer) {
		CustProfile profile = null;
		
		profile = new CustProfile();
		profile.setCustType(customer.getCustType());
		profile.setUseRoomByRoom(customer.isUseRoomByRoom());
		profile.setUseLocatorId(customer.isUseLocatorId());
		
		return profile;
	}
	
	public EulaHist activateEula(String customerId, String acceptCode, Eula eula) {
		
		EulaHist eh = new EulaHist();
		Calendar c = Calendar.getInstance();
		
		eh.setActionType("TOACTIVATE");
		eh.setActionUser("UNSET");
		eh.setCustomerId(customerId);
		eh.setWebSite(eula.getWebSite());
		eh.setSeqNbr(eula.getSeqNbr());
		eh.setActionTimeStamp(c.getTime());
		eh.setAcceptanceCode(acceptCode);
		eh.setActiveAcceptanceCode(true);
		
		return eh;
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
	
	private static byte[] readBytesFromFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
         
        byte[] fileBytes = new byte[(int) file.length()];
        inputStream.read(fileBytes);
        inputStream.close();
         
        return fileBytes;
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

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public Customer getCust() {
		return cust;
	}

	public void setCust(Customer cust) {
		this.cust = cust;
	}
	
	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	public File getEulafile() {
		return eulafile;
	}

	public void setEulafile(File eulafile) {
		this.eulafile = eulafile;
	}

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getEulaText() {
		return eulaText;
	}

	public void setEulaText(String eulaText) {
		this.eulaText = allowCharacters(Encode.forHtml(eulaText.trim()));
	}

}
