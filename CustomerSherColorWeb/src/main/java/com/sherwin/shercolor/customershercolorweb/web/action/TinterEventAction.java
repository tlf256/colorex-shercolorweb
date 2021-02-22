package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.SimpleDateFormat;
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
import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

public class TinterEventAction extends ActionSupport  implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;

	@Autowired
	TinterService tinterService;

	static Logger logger = LogManager.getLogger(TinterEventAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String eventDate;
	private List<Map<String,Object>> tintEventDetail;
	public List<Map<String, Object>> getTintEventDetail() {
		return tintEventDetail;
	}

	private Map<String,Object> tinterMessage;

	

	private CustWebTinterEvents tintEvent = new CustWebTinterEvents();

	private TinterInfo newTinter; 

	public TinterInfo getNewTinter() {
		return newTinter;
	}
	public void setNewTinter(TinterInfo newTinter) {
		this.newTinter = newTinter;
	}
	public String configure() {
		if(sessionMap !=null && reqGuid != null) {

			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid); // when this is null during config we have issues.
			if(reqObj != null) {
				tintEvent.setCustomerId(reqObj.getCustomerID());
			}
			else {
				tintEvent.setCustomerId("NA");
			}
		}
		else {
			tintEvent.setCustomerId("NA");
		}
		
		String retVal = null;

		try {

			tintEvent.setClrntSysId(newTinter.getClrntSysId());
			tintEvent.setTinterModel(newTinter.getModel());  // when this is null during config we have issues or we have previous model
			tintEvent.setTinterSerialNbr(newTinter.getSerialNbr());
			
			
			retVal = processTinterEvent();
		
		} catch (Exception e) {
			logger.error(e.getMessage() + ": ", e);
			e.printStackTrace();
			retVal = ERROR;
		}
		
		return retVal;


	}
	public void getItemsFromSession() {
		if(sessionMap !=null) {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid); // when this is null during config we have issues.
			if(reqGuid != null) {
				TinterInfo tinter = reqObj.getTinter();
				if(tinter != null) {
					logger.debug("inside execute of TinterEvent. Tinter is... " + tinter.getClrntSysId() + " " + tinter.getModel() + " " + tinter.getSerialNbr());

					tintEvent.setCustomerId(reqObj.getCustomerID());
					tintEvent.setClrntSysId(tinter.getClrntSysId());
					tintEvent.setTinterModel(tinter.getModel());  // when this is null during config we have issues or we have previous model
					tintEvent.setTinterSerialNbr(tinter.getSerialNbr());
				}
			}
		}
	}
	private String processTinterEvent() {
		String retVal=null;

		Date stampDate;
		if(eventDate==null){
			logger.debug("TinterEvent date is null.  Using current date and time");
			stampDate = new Date();
		} else {
			logger.debug("TinterEvent date is " + eventDate.toString());
			SimpleDateFormat jsdf = new SimpleDateFormat("EE MMM d y H:m:s 'GMT'Z (zz)");
			try {
				stampDate = jsdf.parse(eventDate);
			} catch (Exception e) {
				stampDate = new Date();
			}
		}
		tintEvent.setDateTime(stampDate);

		List<CustWebTinterEventsDetail> tedList = new ArrayList<CustWebTinterEventsDetail>();
		if(tintEventDetail!=null){
			for(Map<String,Object> item : tintEventDetail){
				CustWebTinterEventsDetail ted = new CustWebTinterEventsDetail();
				if(item.get("type")!=null) ted.setType(item.get("type").toString());
				if(item.get("name")!=null) ted.setName(item.get("name").toString());
				if(item.get("qty")!=null) ted.setQty(Integer.parseInt(item.get("qty").toString()));
				if(ted.getName()!=null && ted.getType()!=null){
					tedList.add(ted);
				}
			}
		}

		//Now we process message to get function(command), error status, severity, error number, error message
		//there is a catch here though, an alternate errorList may appear in the JSON that overrides the error number and error message in the JSON
		//so we will merge the two together
		List<SwMessage> errorList = new ArrayList<SwMessage>();
		if(tinterMessage!=null){
			// Map tinterMessage items to TinterEvent class
			if(tinterMessage.get("command")!=null){
				tintEvent.setFunction(tinterMessage.get("command").toString());
			}
			//Build errorList array when errorNumber is not 0
			
			if(tinterMessage.get("errorNumber")!=null){
				int errorNbr = Integer.parseInt(tinterMessage.get("errorNumber").toString());
				if(errorNbr!=0){
					String sev = "2";
					//tintEvent.setErrorNumber(String.valueOf(errorNbr));
					//if(tinterMessage.get("errorMessage")!=null) tintEvent.setErrorMessage(tinterMessage.get("errorMessage").toString());
					//if(tinterMessage.get("errorSeverity")!=null) tintEvent.setErrorSeverity(tinterMessage.get("errorSeverity").toString());
					tintEvent.setErrorStatus("1");

					if(tinterMessage.get("errorSeverity")!=null) sev = tinterMessage.get("errorSeverity").toString();
					if(tinterMessage.get("errorSeverity")==null || (tinterMessage.get("errorSeverity")=="0")) {
						//severity not set by sender, figure it out by error number
						if(errorNbr!=-10500) sev = "1" ;
						else sev = "2";
					}
					tintEvent.setErrorSeverity(sev);

					SwMessage addError = new SwMessage();
					addError.setCode(String.valueOf(errorNbr));
					if(tinterMessage.get("errorMessage")!=null) addError.setMessage(tinterMessage.get("errorMessage").toString());
					errorList.add(addError);

					// now process errorList from JSON
					if(tinterMessage.get("errorList")!=null){
						@SuppressWarnings("unchecked")
						List<Map<String,Object>> jsonErrorList = (List<Map<String,Object>>) tinterMessage.get("errorList");
						for(Map<String,Object> jsonError : jsonErrorList){
							SwMessage anotherError = new SwMessage();
							if(jsonError.get("num")!=null) anotherError.setCode(jsonError.get("num").toString());
							if(jsonError.get("message")!=null) anotherError.setMessage(jsonError.get("message").toString());
							errorList.add(anotherError);
						}
					}// end errorList not null
				} else {
					tintEvent.setErrorStatus("0");
				} // end error number is 0
			}
		}

		List<SwMessage> writeErrorList = new ArrayList<SwMessage>();
		if(errorList.size()>0){
			// loop ErrorList and write out multiple events that are the same except severity/error number/error message
			for(SwMessage addError : errorList){
				tintEvent.setErrorNumber(addError.getCode());
				tintEvent.setErrorMessage(addError.getMessage());
				List<SwMessage> oneWriteeErrorList = tinterService.writeTinterEventAndDetail(tintEvent, tedList);
				writeErrorList.addAll(oneWriteeErrorList);
			}

		} else {
			writeErrorList = tinterService.writeTinterEventAndDetail(tintEvent, tedList);
		}
		// call tinter service to write tinter event and its details
		if(writeErrorList!=null && writeErrorList.size()>0){
			retVal = ERROR;
		} else {
			retVal = SUCCESS;
		}
		return retVal;
	}
	public String execute(){
		String retVal=null;

		try{
			getItemsFromSession();
			retVal = processTinterEvent();

		} catch (Exception e) {
			logger.error(e.getMessage() + ": ", e);
			e.printStackTrace();
			retVal = ERROR;
		}

		return retVal;
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public void setTintEventDetail(List<Map<String,Object>> tintEventDetail) {
		this.tintEventDetail = tintEventDetail;
	}

	
	public String getEventDate() {
		return eventDate;
	}
	public void setTinterMessage(Map<String, Object> tinterMessage) {
		this.tinterMessage = tinterMessage;
	}
	
	public Map<String, Object> getTinterMessage() {
		return tinterMessage;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = Encode.forHtml(eventDate);
	}
}