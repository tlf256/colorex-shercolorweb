package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebSpectroEvents;
import com.sherwin.shercolor.common.service.SpectroService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class SearchSpectroLogAction extends ActionSupport implements SessionAware, LoginRequired {

	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(SearchSpectroLogAction.class);
	
	private transient Map<String, Object> sessionMap;
	private String reqGuid;	
	private List<String> spectroCommands;
	private String spectroCommand;
	private String fromDate;
	private String toDate;
	private transient List<CustWebSpectroEvents> spectroEventResults;
		
	@Autowired
	private transient SpectroService spectroService;

	
	public String display() {
		List<String> commands = new ArrayList<>();
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		try {
			logger.info("in SearchSpectroLogAction.display.. reqObj.customerId is {} and reqObj.getSpectro().getModel() is {} and reqObj.getSpectro().getSerialNbr() is {}",
					reqObj.getCustomerID(), reqObj.getSpectro().getModel(), reqObj.getSpectro().getSerialNbr());
		
			if(reqObj.getSpectro() != null && reqObj.getSpectro().getModel() != null && reqObj.getSpectro().getSerialNbr() != null) {
				commands = spectroService.getSpectroCommands(reqObj.getCustomerID(), reqObj.getSpectro().getModel(), reqObj.getSpectro().getSerialNbr());
			} else {
				commands = spectroService.getSpectroCommands(reqObj.getCustomerID());
			}
			
			logger.info("commands.size is {}", commands.size());
			if(!commands.isEmpty()) {
				setSpectroCommands(commands);
			}
			
		} catch(RuntimeException e) {
			logger.error("Exception Caught: {} {}", e, e.getMessage());
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String logLookup() {
		List<CustWebSpectroEvents> results = new ArrayList<>();
		SimpleDateFormat jsdf = new SimpleDateFormat("MM/dd/yyyy");
		Date toRequestTime = null;
		Date fromRequestTime = null;
		String cmd;
		String fdate;
		String tdate;
		String spectroModel = "";
		String spectroSerial = "";
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		try {
			logger.info("in SearchSpectroLogAction.logLookup.. reqObj.customerId is {} and reqObj.getSpectro().getModel() is {} and reqObj.getSpectro().getSerialNbr() is {}",
					reqObj.getCustomerID(), reqObj.getSpectro().getModel(), reqObj.getSpectro().getSerialNbr());
			cmd = URLDecoder.decode(this.spectroCommand.trim(), "UTF-8");
			fdate = URLDecoder.decode(this.fromDate.trim(), "UTF-8");
			tdate = URLDecoder.decode(this.toDate.trim(), "UTF-8");
			if(reqObj.getSpectro() != null && reqObj.getSpectro().getModel() != null && reqObj.getSpectro().getSerialNbr() != null) {
				spectroModel = reqObj.getSpectro().getModel();
				spectroSerial = reqObj.getSpectro().getSerialNbr();
			}
			
			if(!fdate.equals("")) {
				fromRequestTime = jsdf.parse(fdate);
			}
			
			if(!tdate.equals("")) {
				toRequestTime = jsdf.parse(tdate);
			}
			logger.info("cmd: {}. fdate: {}. tdate: {}. spectroModel: {}. spectroSerial: {}. CustomerId {}", cmd, fdate, tdate, spectroModel, spectroSerial, reqObj.getCustomerID());
			results = spectroService.searchSpectroEvents(reqObj.getCustomerID(), spectroModel, spectroSerial, cmd, fromRequestTime, toRequestTime);
			logger.info("results size is {}",  results.size());
			setSpectroEventResults(results);
			
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException Caught in SearchSpectroLogAction.logLookup");
			return ERROR;
		} catch (ParseException e) {
			logger.error("ParseException Caught in SearchSpectroLogAction.logLookup");
			return ERROR;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public List<String> getSpectroCommands() {
		return spectroCommands;
	}

	public void setSpectroCommands(List<String> spectroCommands) {
		this.spectroCommands = spectroCommands;
	}

	public String getSpectroCommand() {
		return spectroCommand;
	}

	public void setSpectroCommand(String spectroCommand) {
		this.spectroCommand = spectroCommand;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public List<CustWebSpectroEvents> getSpectroEventResults() {
		return spectroEventResults;
	}

	public void setSpectroEventResults(List<CustWebSpectroEvents> spectroEventResults) {
		this.spectroEventResults = spectroEventResults;
	}
}
