package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.Eula;
import com.sherwin.shercolor.common.domain.EulaHist;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.EulaService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustProdComp;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustProfile;
import com.sherwin.shercolor.customerprofilesetup.web.model.CustEula;
import com.sherwin.shercolor.customerprofilesetup.web.model.Customer;
import com.sherwin.shercolor.customerprofilesetup.web.model.RequestObject;

public class ProcessCustomerAction extends ActionSupport implements SessionAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessCustomerAction.class);
	private Customer customer;
	private CustEula eula;
	private String eulaType;
	private Map<String, Object> sessionMap;
	private String result;
	private String AcctNbr;
	private List<String> prodCompList;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	EulaService eulaService;
	
	@Autowired
	ProductService productService;

	public String execute() {
		try {	
			logger.trace("begin ProcessCustomerAction.execute");
			RequestObject reqObj = (RequestObject) sessionMap.get("CustomerDetail");
			
			String custId = customerId(customer);
			
			//reqObj.setNewCustomer(true);
			reqObj.setCustUnchanged(false);
			reqObj.setCustomerId(custId);
			reqObj.setAccttype(customer.getAccttype());
			reqObj.setSwuiTitle(allowCharacters(customer.getSwuiTitle()));
			reqObj.setCdsAdlFld(allowCharacters(customer.getCdsAdlFld()));
			//reqObj.setDefaultClrntSys(customer.getDefaultClrntSys());
			List<String> clrntlist = clrntSysIds(customer.getDefaultClrntSys(), customer.getClrntSysIds());
			reqObj.setClrntList(clrntlist);
			reqObj.setActive(true);
			reqObj.setHistory(false);
			
			List<CustParms> newcustlist = new ArrayList<CustParms>();
			
			for(int j = 0; j < clrntlist.size(); j++) {
				//create new custwebparms record
				CustParms newcust = new CustParms();
				newcust.setCustomerId(reqObj.getCustomerId());
				newcust.setSwuiTitle(reqObj.getSwuiTitle());
				newcust.setCdsAdlFld(reqObj.getCdsAdlFld());
				newcust.setSeqNbr(j+1);
				newcust.setClrntSysId(clrntlist.get(j));
				newcust.setActive(true);
				newcustlist.add(newcust);
			}
			
			reqObj.setCustList(newcustlist);
			reqObj.setProfile(mapCustProfile(customer));
			reqObj.setEulaType(eulaType);
						
			if(!eulaType.equals("None")) {
				String customerId = reqObj.getCustomerId();
				String website = "CUSTOMERSHERCOLORWEB";
				int seqNbr = 1;
				String inputDate = eula.getEffectiveDate();
				String effDate = null;
				String expDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("d-M-yy");
				
				// both custom and template EULAs need at least
				// an effective date set
				if(inputDate == null && !eulaType.equals("SherColor Web EULA")) {
					// set effective date to today
					effDate = sdf.format(new Date());
				} else {
					effDate = eula.getEffectiveDate();
				}
				
				// check if custom EULA was chosen
				// this type of EULA does not require
				// an activation record
				if(!eulaType.equals("Custom EULA")) {
					// EULA activation record needs created
					List<EulaHist> ehlist = new ArrayList<EulaHist>();
					EulaHist eh = new EulaHist();
					String acceptCode = eula.getAcceptCode();
					
					if(eulaType.equals("SherColor Web EULA")) {
						// standard CSW EULA is being used, get seqnbr
						// new customer - not possible to have a custom 
						// EULA associated with the customer ID
						Eula sherColorWebEula = eulaService.readActive(website, customerId);
						seqNbr = sherColorWebEula.getSeqNbr();
						effDate = sdf.format(sherColorWebEula.getEffectiveDate());
						
						Date cswExpDate = sherColorWebEula.getExpirationDate();
						
						if(cswExpDate != null) {
							expDate = sdf.format(cswExpDate);
						}
					} else if(eulaType.equals("Custom EULA Template")) {
						// EULA template is being used
						// new customer - sequence number will stay 1
						// save template input to session
						reqObj.setTemplate(eula.getTemplate());
					}
					
					// create eula activation record
					eh = activateEula(customerId, acceptCode, seqNbr);
					ehlist.add(0, eh);
					
					reqObj.setEulaHistToActivate(eh);
					reqObj.setEulaHistList(ehlist);
				}
				
				eula.setWebsite(website);
				eula.setSeqNbr(seqNbr);
				eula.setEffectiveDate(effDate);
				eula.setExpDate(expDate);
				
				reqObj.setEula(eula);
			}
			
			reqObj.setProdCompList(mapProdCompList(customer.getProdComps()));
			
			sessionMap.put("CustomerDetail", reqObj);
			
			logger.trace("end ProcessCustomerAction.execute");
			
			if(reqObj.getAccttype().equals("natlWdigits")) {
				int custIdNum = Integer.parseInt(custId);
				if(custIdNum >= 400000000 && custIdNum <= 400000012) {
					//internal account, login records are optional
					return SUCCESS;
				} else {
					return "nologin";
				}
			} else {
				return SUCCESS;
			}
			
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage(), he);
			return ERROR;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private String customerId(Customer customer) {
		String custId = "";
		//check for entered account number
		switch(customer.getAccttype()) {
		case "natlWdigits":  //customerid = account number
			custId = customer.getNtlacctnbr();
			break;
		case "natlWOdigits":  //create customerid
			//lookup national customer ids
			List<CustWebParms> custWebParms = customerService.getNatlCustomerIds();
			
			if(custWebParms.isEmpty()) {
				// list should not be empty
				addActionError("Database Error - National customer cannot be created. Please contact administrator.");
			} else {
				Object[] idList = custWebParms.toArray();
				
				int result = Integer.parseInt(idList[0].toString().substring(2)) + 1;
				//create new id beginning with 99
				StringBuilder newResult = new StringBuilder();
				newResult.append("99");
				newResult.append(result);
				if(newResult.length() < 6) {
					while(newResult.length() < 6) {
						newResult.insert(2, 0);
					}
				}
				custId = newResult.toString();
			}
			
			break;
		case "intnatlWdigits":  //customerid = account number
			custId = customer.getIntntlacctnbr();
			break;
		case "intnatlWOdigits":  //create customerid
			//lookup international customer ids
			List<CustWebParms> custParms = customerService.getIntnatlCustomerIds();
			
			if(custParms.isEmpty()) {
				//first international id created with 'INTL'
				//reqObj.setCustomerId("INTL0001");
				// list should not be empty
				addActionError("Database Error - International customer cannot be created. Please contact administrator.");
			} else {
				Object[] custIdList = custParms.toArray();
				
				int nextId = Integer.parseInt(custIdList[0].toString().substring(4)) + 1;
				//create new id beginning with intl
				StringBuilder newId = new StringBuilder();
				newId.append("INTL");
				newId.append(nextId);
				if(newId.length() < 8) {
					while(newId.length() < 8) {
						newId.insert(4, 0);
					}
				}
				custId = newId.toString();
			}
			
			break;
		case "intnatlCostCntr":
			custId = customer.getCostCenter();
			break;
		default:
			// result not expected
			logger.error("String is junk, return to form");
			addActionError("Error - Unexpected value. Please retry request.");
			return INPUT;
		}
		
		return custId;
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
		
		if(!customer.getCustType().equals("CUSTOMER")) {
			profile = new CustProfile();
			profile.setCustType(customer.getCustType());
			profile.setUseRoomByRoom(customer.isUseRoomByRoom());
			profile.setUseLocatorId(customer.isUseLocatorId());
		}
		
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
	
	private EulaHist activateEula(String customerId, String acceptCode, int seqNbr) {
		
		EulaHist eh = new EulaHist();
		Calendar c = Calendar.getInstance();
		
		eh.setActionType("TOACTIVATE");
		eh.setActionUser("UNSET");
		eh.setCustomerId(customerId);
		eh.setWebSite("CUSTOMERSHERCOLORWEB");
		eh.setSeqNbr(seqNbr);
		eh.setActionTimeStamp(c.getTime());
		eh.setAcceptanceCode(acceptCode);
		eh.setActiveAcceptanceCode(true);
		
		return eh;
	}
	
	public String setActionProdCompList() {
		
		setProdCompList(productService.getDistinctProdComps());
		
		return SUCCESS;
	}
	
	public String checkAcctNbr() {
		try {
			List<Object> acctNbrList = customerService.getAllCustomerIds();
			if(acctNbrList.contains(AcctNbr)) {
				setResult("true");
			} else {
				setResult("false");
			}
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	private String allowCharacters(String escapedString) {
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustEula getEula() {
		return eula;
	}

	public void setEula(CustEula eula) {
		this.eula = eula;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getEulaType() {
		return eulaType;
	}

	public void setEulaType(String eulaType) {
		this.eulaType = eulaType;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAcctNbr() {
		return AcctNbr;
	}

	public void setAcctNbr(String acctNbr) {
		AcctNbr = acctNbr;
	}

	public List<String> getProdCompList() {
		return prodCompList;
	}

	public void setProdCompList(List<String> prodCompList) {
		this.prodCompList = prodCompList;
	}

}
