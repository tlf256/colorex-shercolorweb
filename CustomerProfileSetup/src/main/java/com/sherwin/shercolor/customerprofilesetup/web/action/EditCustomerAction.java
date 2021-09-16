package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.Eula;
import com.sherwin.shercolor.common.domain.EulaHist;
import com.sherwin.shercolor.common.service.EulaService;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustProdComp;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustProfile;
import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;
import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;
import com.sherwin.shercolor.customerprofilesetup.web.model.CustEula;
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
	private CustEula eula;
	private String eulaType;
	private boolean edited;
	private Map<String, Object> sessionMap;
	private File eulafile;
	private boolean prodCompDeleted;
	private boolean prodCompAdded;
	private String prodComp;
	
	@Autowired
	EulaService eulaService;
	
	public String execute() {
		try {
			List<CustParms> editCustList = new ArrayList<CustParms>();
			//List<String> editclrntlist = new ArrayList<String>();
			List<LoginTrans> editLoginList = new ArrayList<LoginTrans>();
			List<JobFields> editJobList = new ArrayList<JobFields>();
			
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			String customerId = reqObj.getCustomerId();
			
			setEdited(true);
			
			/*for(int i = 0; i < cust.getClrntList().size(); i++) {
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
			}*/
			
			reqObj.setClrntList(clrntSysIds(cust.getDefaultClrntSys(), cust.getClrntList()));
			reqObj.setSwuiTitle(allowCharacters(cust.getSwuiTitle()));
			reqObj.setCdsAdlFld(allowCharacters(cust.getCdsAdlFld()));
			reqObj.setActive(cust.isActive());
			
			for(int i = 0; i < reqObj.getClrntList().size(); i++) {
				//set values for custwebparms record
				CustParms newcust = new CustParms();
				newcust.setCustomerId(customerId);
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
			
			String custType = cust.getCustType();
			
			if(custType != null) {
				if(custType.equals("CUSTOMER")) {
					reqObj.setProfile(null);
				} else {
					reqObj.setProfile(mapCustProfile(cust));
				}
			}
			
			String prodComps = cust.getProdComps();
			List<CustProdComp> prodCompList = reqObj.getProdCompList();
			
			if(prodComps != null) {
				// new data
				reqObj.setProdCompList(mapProdCompList(prodComps));
				
			} else {
				if(prodCompList != null || cust.getProdCompArr() != null) {
					// records either already exist or were entered previously
					// check if the list needs updating
					// if null then there isn't any data to process
					List<CustProdComp> editProdCompList = mapProdCompList(cust.getProdCompArr(), cust.getPrimaryProdComp());
					
					if(!editProdCompList.equals(prodCompList)) {
						// change has been made
						// save new data
						reqObj.setProdCompList(editProdCompList);
					}
				}
			}
			
			// from edit page, users can only upload a PDF,
			// change acceptance code, or activate a new EULA
			// a EULA type is assigned to every customer, new or existing
			if(eula != null) {
				// get eula type, if set here
				if(eulaType != null && !reqObj.getEulaType().equals(eulaType)) {
					// eula type has been changed
					reqObj.setEulaType(eulaType);
				}
				
				String website = "CUSTOMERSHERCOLORWEB";
				
				// the option to activate a new EULA only displays
				// if the EULA type was originally 'None'
				// if the EULA type is still 'None' at this point then
				// there is nothing to do, so skip this section		
				if(eulaType != null && eulaType != "None") {
					
					int seqNbr = 1;
					String acceptCode = eula.getAcceptCode();
					String inputDate = eula.getEffectiveDate();
					String effDate = null;
					String expDate = null;
					SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy");
					
					// both custom and template EULAs need at least
					// an effective date set
					if(inputDate == null && !eulaType.equals("SherColor Web EULA")) {
						// set effective date to today
						effDate = sdf.format(new Date());
					} else {
						effDate = eula.getEffectiveDate();
					}
					
					boolean saveUpdateEula = false;
					boolean saveUpdateEulaHist = false;
					
					List<EulaHist> ehlist = new ArrayList<EulaHist>();
					EulaHist eh = new EulaHist();
					
					Eula activeEula = eulaService.readActive(website, customerId);
					
					// check for existing data and any changes that
					// may have been made to the acceptance code only
					if(eulaType.equals("SherColor Web EULA") || eulaType.equals("Custom EULA Template")) {
						// both EULA types should have at least a TOACTIVATE
						// EULA hist record already created
						if(reqObj.getEulaHistToActivate() == null) {
							// TOACTIVATE record does not exist
							// assume user is activating a new EULA from here
							if(eulaType.equals("SherColor Web EULA")) {
								if(activeEula.getCustomerId() != null) {
									// standard CSW EULA is being used, get seqnbr
									// new customer - not possible to have a custom 
									// EULA associated with the customer ID
									seqNbr = activeEula.getSeqNbr();
									effDate = sdf.format(activeEula.getEffectiveDate());
									Date cswExpDate = activeEula.getExpirationDate();
									if(cswExpDate != null) {
										expDate = sdf.format(cswExpDate);
									}
								} else {
									addActionError("Custom EULA for " + activeEula.getCustomerId() + " already exists");
								}
							} else {
								// EULA template is being used
								// new customer - sequence number will stay 1
								// save template input to session
								reqObj.setTemplate(eula.getTemplate());
							}
							
							// create TOACTIVATE record
							eh = activateEula(customerId, acceptCode, eula);
							ehlist.add(0, eh);
							
							reqObj.setEulaHistToActivate(eh);
							reqObj.setEulaHistList(ehlist);
							
							saveUpdateEulaHist = true;
							saveUpdateEula = true;
						} else {
							// eula history, recent toactivate record
							// check if acceptance code has been changed
							if(!reqObj.getEulaHistToActivate().getAcceptanceCode().equals(acceptCode)) {
								// acceptance code has been changed
								// update TOACTIVATE record
								eh = reqObj.getEulaHistToActivate();
								eh.setAcceptanceCode(acceptCode);
								ehlist.add(0, eh);
								
								saveUpdateEulaHist = true;
							}
						}
					} else {
						// custom EULA
						// first check if a PDF has been uploaded
						if(eulafile != null) {
							
							if(activeEula.getCustomerId() != null && activeEula.getEulaPdf() != null) {
								addFieldError("eulafile", "EULA pdf for " + activeEula.getCustomerId() + " already exists");
							}
							
							byte[] filebytes = readBytesFromFile(eulafile);
							
							eula.setEulapdf(filebytes);
														
							saveUpdateEula = true;
						}
					}
					
					if(saveUpdateEula) {
						eula.setWebsite(website);
						eula.setSeqNbr(seqNbr);
						eula.setEffectiveDate(effDate);
						eula.setExpDate(expDate);
						reqObj.setEula(eula);
					}
					
					if(saveUpdateEulaHist) {
						reqObj.setEulaHistToActivate(eh);
						reqObj.setEulaHistList(ehlist);
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
	
	private List<String> clrntSysIds(String defaultClrntSys, List<String> clrntSysIds){
		List<String> clrntlist = new ArrayList<String>();
		
		for(String id : clrntSysIds) {
			if(id.equals(defaultClrntSys)) {
				clrntlist.add(0, id);
			} else {
				clrntlist.add(id);
			}
		}
		
		return clrntlist;
	}
	
	private CustProfile mapCustProfile(Customer customer) {
		CustProfile profile = null;
		
		profile = new CustProfile();
		profile.setCustType(customer.getCustType());
		profile.setUseRoomByRoom(customer.isUseRoomByRoom());
		profile.setUseLocatorId(customer.isUseLocatorId());
		
		return profile;
	}
	
	private List<CustProdComp> mapProdCompList(String prodComps){
		List<CustProdComp> custProdCompList = new ArrayList<CustProdComp>();
		
		if(prodComps != null && !prodComps.isEmpty()) {
			String[] prodCompsArr = prodComps.split(",");
			for(int i = 0; i < prodCompsArr.length; i++) {
				CustProdComp cpc = new CustProdComp();
				String prodComp = prodCompsArr[i].trim().toUpperCase();
				
				cpc.setProdComp(prodComp);
				if(i==0) {
					cpc.setPrimaryProdComp(true);
				} else {
					cpc.setPrimaryProdComp(false);
				}
				custProdCompList.add(cpc);
			}
		}
				
		return custProdCompList;
	}
	
	private List<CustProdComp> mapProdCompList(String[] prodComps, String primaryProdComp){
		List<CustProdComp> custProdCompList = new ArrayList<CustProdComp>();
		
		if(prodComps != null) {
			String primProdComp = primaryProdComp.trim().toUpperCase();
			for(int i = 0; i < prodComps.length; i++) {
				CustProdComp cpc = new CustProdComp();
				String prodComp = prodComps[i].trim().toUpperCase();
				if(!prodComp.isEmpty()) {
					cpc.setProdComp(prodComp);
					if(prodComp.equals(primProdComp)) {
						cpc.setPrimaryProdComp(true);
					} else {
						cpc.setPrimaryProdComp(false);
					}
					custProdCompList.add(cpc);
				}
			}
		}
				
		return custProdCompList;
	}
	
	private EulaHist activateEula(String customerId, String acceptCode, CustEula eula) {
		
		EulaHist eh = new EulaHist();
		Calendar c = Calendar.getInstance();
		
		eh.setActionType("TOACTIVATE");
		eh.setActionUser("UNSET");
		eh.setCustomerId(customerId);
		eh.setWebSite(eula.getWebsite());
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
	
	public String deleteProdComp() {
		RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
		
		List<CustProdComp> prodCompList = reqObj.getProdCompList();
		List<CustProdComp> deletedProdComps = new ArrayList<CustProdComp>();
		
		if(prodCompList != null) {
			for(CustProdComp pc : prodCompList) {
				if(pc.getProdComp().equalsIgnoreCase(getProdComp())) {
					deletedProdComps.add(pc);
					setProdCompDeleted(true);
				}
			}
		}
		
		reqObj.setDeletedProdComps(deletedProdComps);
		
		sessionMap.put("CustomerDetail", reqObj);
		
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
	
	public CustEula getEula() {
		return eula;
	}

	public void setEula(CustEula eula) {
		this.eula = eula;
	}

	public String getEulaType() {
		return eulaType;
	}

	public void setEulaType(String eulaType) {
		this.eulaType = eulaType;
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

	public boolean isProdCompDeleted() {
		return prodCompDeleted;
	}

	public void setProdCompDeleted(boolean prodCompDeleted) {
		this.prodCompDeleted = prodCompDeleted;
	}

	public boolean isProdCompAdded() {
		return prodCompAdded;
	}

	public void setProdCompAdded(boolean prodCompAdded) {
		this.prodCompAdded = prodCompAdded;
	}

	public String getProdComp() {
		return prodComp;
	}

	public void setProdComp(String prodComp) {
		this.prodComp = prodComp;
	}

}
