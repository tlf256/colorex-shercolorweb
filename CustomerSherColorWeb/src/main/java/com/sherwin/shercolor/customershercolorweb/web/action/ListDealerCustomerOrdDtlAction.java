package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.HibernateException;
import org.owasp.encoder.Encode;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.service.CustomerOrderService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustOrdDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerCustOrdDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebTranDto;
import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebTranDtoBuilder;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@SuppressWarnings("serial")
public class ListDealerCustomerOrdDtlAction extends ActionSupport implements SessionAware, LoginRequired {

	static Logger logger = LoggerFactory.getLogger(LookupJobAction.class);
	
	private Map<String, Object> sessionMap;
	
	@Autowired
	private CustomerOrderService		customerOrderService;

	@Autowired
	private TranHistoryService			tranHistoryService;

	private String 						reqGuid;
	private String 						lookupCustomerId; // Dealer i.d. from the page header.  
	private String 						lookupDlrCustId;  // Customer i.d. from customer order table display.
	private int 						lookupControlNbr; // Control nbr. from customer order table display.
	private CustWebDealerDto			custWebDealerDto;
	private CustWebDealerCustDto 		custWebDealerCustDto;
	private CustWebDealerCustOrdDto 	custWebDealerCustOrdDto;
	private List<CustWebTranDto>		listCustWebTranDto = new ArrayList<CustWebTranDto>();
 	
	public String display() {
		CustWebDealer				custWebDealer;
		CustWebDealerCust			custWebDealerCust;
		CustWebDealerCustOrd		custWebDealerCustOrd;
		List<CustWebTran>			listCustWebTran = new ArrayList<CustWebTran>();
		RequestObject reqObj 		= (RequestObject) sessionMap.get(reqGuid);
		lookupCustomerId			= reqObj.getCustomerID();
		logger.debug("Request GuId is -> " + reqGuid + " Dealer Id is -> " + lookupCustomerId +
				" Customer Id is -> " + lookupDlrCustId + "  Control Nbr is -> " + lookupControlNbr);
		
		try{
			custWebDealer = customerOrderService.readDealer(lookupCustomerId);
			custWebDealerDto = new CustWebDealerDtoBuilder().build(custWebDealer);

			custWebDealerCust = customerOrderService.readDealerCust(lookupCustomerId, lookupDlrCustId);
			custWebDealerCustDto = new CustWebDealerCustDtoBuilder().build(custWebDealerCust);
			
			custWebDealerCustOrd = customerOrderService.readDealerCustOrd(lookupCustomerId, lookupDlrCustId, lookupControlNbr);
			custWebDealerCustOrdDto = new CustWebDealerCustOrdDtoBuilder().build(custWebDealerCustOrd);
			
			listCustWebTran = tranHistoryService.getCustomerOrder(lookupControlNbr);
			listCustWebTranDto = new CustWebTranDtoBuilder().build(listCustWebTran);
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

	public TranHistoryService getTranHistoryService() {
		return tranHistoryService;
	}

	public void setTranHistoryService(TranHistoryService tranHistoryService) {
		this.tranHistoryService = tranHistoryService;
	}

	public List<CustWebTranDto> getListCustWebTranDto() {
		return listCustWebTranDto;
	}

	public void setListCustWebTranDto(List<CustWebTranDto> listCustWebTranDto) {
		this.listCustWebTranDto = listCustWebTranDto;
	}

	public String getLookupCustomerId() {
		return lookupCustomerId;
	}

	public void setLookupCustomerId(String lookupCustomerId) {
		this.lookupCustomerId = Encode.forHtml(lookupCustomerId);
	}

	public CustWebDealerDto getCustWebDealerDto() {
		return custWebDealerDto;
	}

	public void setCustWebDealerDto(CustWebDealerDto custWebDealerDto) {
		this.custWebDealerDto = custWebDealerDto;
	}

	public CustWebDealerCustDto getCustWebDealerCustDto() {
		return custWebDealerCustDto;
	}

	public void setCustWebDealerCustDto(CustWebDealerCustDto custWebDealerCustDto) {
		this.custWebDealerCustDto = custWebDealerCustDto;
	}

	public CustWebDealerCustOrdDto getCustWebDealerCustOrdDto() {
		return custWebDealerCustOrdDto;
	}

	public void setCustWebDealerCustOrdDto(CustWebDealerCustOrdDto custWebDealerCustOrdDto) {
		this.custWebDealerCustOrdDto = custWebDealerCustOrdDto;
	}

	public String getLookupDlrCustId() {
		return lookupDlrCustId;
	}

	public void setLookupDlrCustId(String lookupDlrCustId) {
		this.lookupDlrCustId = Encode.forHtml(lookupDlrCustId);
	}

	public int getLookupControlNbr() {
		return lookupControlNbr;
	}

	public void setLookupControlNbr(int lookupControlNbr) {
		this.lookupControlNbr = lookupControlNbr;
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);
	}

}
