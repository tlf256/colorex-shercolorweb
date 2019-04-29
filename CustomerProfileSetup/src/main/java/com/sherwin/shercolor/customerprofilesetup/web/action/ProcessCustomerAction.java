package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.model.Customer;

public class ProcessCustomerAction extends ActionSupport implements SessionAware {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessCustomerAction.class);
	private Customer customer;
	private Map<String, Object> sessionMap;
	private String result;
	private String AcctNbr;

	@Autowired
	CustomerService customerService;

	public String execute() {
		try {	
			//check for entered account number
			switch(customer.getAccttype()) {
			case "natlWdigits":  //customerid = account number
				customer.setCustomerId(customer.getNtlacctnbr().trim());
				break;
			case "natlWOdigits":  //create customerid
				//lookup national customer ids
				List<CustWebParms> custWebParms = customerService.getNatlCustomerIds();
				
				if(custWebParms.isEmpty()) {
					//first national id created with '99'
					customer.setCustomerId("990001");
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
					customer.setCustomerId(newResult.toString());
				}
				
				break;
			case "intnatlWdigits":  //customerid = account number
				customer.setCustomerId(customer.getIntntlacctnbr().trim());
				break;
			case "intnatlWOdigits":  //create customerid
				//lookup international customer ids
				List<CustWebParms> custParms = customerService.getIntnatlCustomerIds();
				
				if(custParms.isEmpty()) {
					//first international id created with 'INTL'
					customer.setCustomerId("INTL0001");
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
					customer.setCustomerId(newId.toString());
				}
				
				break;
			}
						
			List<String> clrntlist = new ArrayList<String>();
			
			if(customer.getCce()!=null) {
				if(customer.getDefaultClrntSys().contains("cce")) {
					clrntlist.add(0, "CCE");
				} else {
					clrntlist.add("CCE");
				}
			}
			if(customer.getBac()!=null) {
				if(customer.getDefaultClrntSys().contains("bac")) {
					clrntlist.add(0, "BAC");
				} else {
					clrntlist.add("BAC");
				}
			}
			if(customer.getEef()!=null) {
				if(customer.getDefaultClrntSys().contains("884")) {
					clrntlist.add(0, "884");
				} else {
					clrntlist.add("884");
				}
			}
			customer.setClrntList(clrntlist);
			customer.setActive(true);
			
			List<CustParms> newcustlist = new ArrayList<CustParms>();
			
			for(int j = 0; j < clrntlist.size(); j++) {
				//create new custwebparms record
				CustParms newcust = new CustParms();
				newcust.setCustomerId(customer.getCustomerId());
				newcust.setSwuiTitle(customer.getSwuiTitle().trim());
				newcust.setCdsAdlFld(customer.getCdsAdlFld().trim());
				newcust.setSeqNbr(j+1);
				newcust.setClrntSysId(clrntlist.get(j));
				newcust.setActive(true);
				newcustlist.add(newcust);
			}
			
			customer.setCustList(newcustlist);
			customer.setHistory(false);
			sessionMap.put("CustomerDetail", customer);	
			
			if(customer.getAccttype().equals("natlWdigits")) {
				return "nologin";
			} else {
				return SUCCESS;
			}
			
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public String checkAcctNbr() {
		try {
			List<Object> acctNbrList = customerService.getAllCustomerIds();
			if(acctNbrList.contains(AcctNbr)) {
				setResult("true");
			} else {
				setResult("false");
			}
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
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

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
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

}
