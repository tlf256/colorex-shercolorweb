package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.List;
import java.util.Map;

import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.service.CustomerOrderService;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustOrdDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustOrdDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class ListDealerCustomerOrdersAction extends ActionSupport implements SessionAware, LoginRequired {

	static Logger logger = LoggerFactory.getLogger(LookupJobAction.class);
	
	private Map<String, Object> sessionMap;
	
	@Autowired
	private CustomerOrderService		customerOrderService;

	private String 							reqGuid;
	private String 							lookupCustomerId; // Lookup for dealer customer i.d.
	private String 							lookupDlrCustId;  // Lookup for customer table display.
	private CustWebDealerDto				custWebDealerDto;
	private CustWebDealerCustDto 			custWebDealerCustDto;
	private List<CustWebDealerCustOrdDto> 	listCustWebDealerCustOrdDto;

	public String display() {
		List<CustWebDealerCustOrd> listCustWebDealerCustOrd;
		CustWebDealer custWebDealer;
		CustWebDealerCust custWebDealerCust;
		logger.debug("Request GuId is -> " + reqGuid + " lookup Cust Id is -> " + lookupCustomerId
				+ " lookup DlrCustid is -> " + lookupDlrCustId);
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		lookupCustomerId 	= reqObj.getCustomerID();
		try{
			custWebDealer = customerOrderService.readDealer(lookupCustomerId);
			custWebDealerDto = new CustWebDealerDtoBuilder().build(custWebDealer);

			custWebDealerCust = customerOrderService.readDealerCust(lookupCustomerId, lookupDlrCustId);
			custWebDealerCustDto = new CustWebDealerCustDtoBuilder().build(custWebDealerCust);
			
			listCustWebDealerCustOrd = customerOrderService.listDealerCustOrders(lookupCustomerId, lookupDlrCustId);
			listCustWebDealerCustOrdDto = new CustWebDealerCustOrdDtoBuilder().build(listCustWebDealerCustOrd);
		}	
		catch (HibernateException he){
			logger.error("Hibernation Exception caught" + he.toString() + " " + he.getMessage(), he);
			return ERROR;
		}
		catch (Exception e){
			logger.error("Exception caught" + e.toString() + " " + e.getMessage(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String execute(){
		return SUCCESS;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ListDealerCustomersAction.logger = logger;
	}

	public CustomerOrderService getCustomerOrderService() {
		return customerOrderService;
	}

	public void setCustomerOrderService(CustomerOrderService customerOrderService) {
		this.customerOrderService = customerOrderService;
	}

	public String getLookupCustomerId() {
		return lookupCustomerId;
	}

	public void setLookupCustomerId(String lookupCustomerId) {
		this.lookupCustomerId = Encode.forHtml(lookupCustomerId);
	}

	public String getLookupDlrCustId() {
		return lookupDlrCustId;
	}

	public void setLookupDlrCustId(String lookupDlrCustId) {
		this.lookupDlrCustId = Encode.forHtml(lookupDlrCustId);
	}

	public CustWebDealerDto getCustWebDealerDto() {
		return custWebDealerDto;
	}

	public void setCustWebDealerDto(CustWebDealerDto custWebDealerDto) {
		this.custWebDealerDto = custWebDealerDto;
	}

	public List<CustWebDealerCustOrdDto> getListCustWebDealerCustOrdDto() {
		return listCustWebDealerCustOrdDto;
	}

	public void setListCustWebDealerCustOrdDto(List<CustWebDealerCustOrdDto> listCustWebDealerCustOrdDto) {
		this.listCustWebDealerCustOrdDto = listCustWebDealerCustOrdDto;
	}

	public CustWebDealerCustDto getCustWebDealerCustDto() {
		return custWebDealerCustDto;
	}

	public void setCustWebDealerCustDto(CustWebDealerCustDto custWebDealerCustDto) {
		this.custWebDealerCustDto = custWebDealerCustDto;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);
	}

}
