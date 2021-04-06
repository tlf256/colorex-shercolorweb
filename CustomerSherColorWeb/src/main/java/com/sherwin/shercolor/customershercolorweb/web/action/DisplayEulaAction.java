package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.Eula;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.common.service.EulaService;

@SuppressWarnings("serial")
public class DisplayEulaAction extends ActionSupport  implements SessionAware, LoginRequired {
	
	static Logger logger = LogManager.getLogger(DisplayEulaAction.class);
	private String eulaText1 = "";
	private String eulaText2 = "";
	private String eulaText3 = "";
	private String eulaText4 = "";
	private String eulaText5 = "";
	private String eulaText6 = "";
	private String eulaText7 = "";
	private String eulaText8 = "";
	private String eulaText9 = "";
	private String eulaText10 = "";
	private byte[] eulaPDF;
	private List<String> allEulas;
	private String eulaAgreementCode = "";
	private DataInputStream inputStream;
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private int eulaSeqNbr;
	
	@Autowired 
	private EulaService target;
	
	public String print() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			Eula validEula = target.readActive("CUSTOMERSHERCOLORWEB",reqObj.getCustomerID());
			logger.error("in print");
    		//Don't forget to set the PDF document.
    		eulaPDF = validEula.getEulaPdf();
    		if (eulaPDF==null) {
    			logger.info("eulaPDF is null when getting");
    		}

			inputStream = new DataInputStream(new ByteArrayInputStream(eulaPDF));

			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String execute() {
		logger.info("in DisplayEulaActuionExecute");
		try {
			//First, verify that the entered code is valid.  We will bounce this off a table as the code
			//will be customer specific (still need to define this part.)
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			if (!eulaAgreementCode.equals(target.getAcceptanceCode("CUSTOMERSHERCOLORWEB", reqObj.getCustomerID()))) {
				addFieldError("eulaAgreementCode", getText("displayEulaAction.agreementCodeIncorrect"));
				return INPUT;
			}
			
			//agreement code is valid, get the Eula for display.
			
			Eula validEula = target.readActive("CUSTOMERSHERCOLORWEB",reqObj.getCustomerID());
	        if (validEula!=null) {
	        	// Need to set the permission level here - store this in a session variable?
	    		@SuppressWarnings("rawtypes")
	    		Map session = (Map) ActionContext.getContext().get("session");
	    		allEulas = new ArrayList<String>();
	    		eulaText1 = validEula.getEulaText1();
	    		allEulas.add(eulaText1);
	    		eulaText2 = validEula.getEulaText2();
	    		if (eulaText2!=null) {
		    		if (!eulaText2.isEmpty()) {
		    			allEulas.add(eulaText2);
		    		}
	    		}
	    		eulaText3 = validEula.getEulaText3();
	    		if (eulaText3!=null) {
		    		if (!eulaText3.isEmpty()) {
		    			allEulas.add(eulaText3);
		    		}
	    		}
	    		eulaText4 = validEula.getEulaText4();
	    		if (eulaText4!=null) {
		    		if (!eulaText4.isEmpty()) {
		    			allEulas.add(eulaText4);
		    		}
	    		}
	    		eulaText5 = validEula.getEulaText5();
	    		if (eulaText5!=null) {
		    		if (!eulaText5.isEmpty()) {
		    			allEulas.add(eulaText5);
		    		}
	    		}
	    		eulaText6 = validEula.getEulaText6();
	    		if (eulaText6!=null) {
		    		if (!eulaText6.isEmpty()) {
		    			allEulas.add(eulaText6);
		    		}
	    		}
	    		eulaText7 = validEula.getEulaText7();
	    		if (eulaText7!=null) {
		    		if (!eulaText7.isEmpty()) {
		    			allEulas.add(eulaText7);
		    		}
	    		}
	    		eulaText8 = validEula.getEulaText8();
	    		if (eulaText8!=null) {
		    		if (!eulaText8.isEmpty()) {
		    			allEulas.add(eulaText8);
		    		}
	    		}
	    		eulaText9 = validEula.getEulaText9();
	    		if (eulaText9!=null) {
		    		if (!eulaText9.isEmpty()) {
		    			allEulas.add(eulaText9);
		    		}
	    		}
	    		eulaText10 = validEula.getEulaText10();
	    		if (eulaText10!=null) {
		    		if (!eulaText10.isEmpty()) {
		    			allEulas.add(eulaText10);
		    		}
	    		}
	    		eulaSeqNbr = validEula.getSeqNbr();
	    		

	    		logger.info("in DisplayEulaActuionExecute, returning SUCCESS");
	            return SUCCESS;
	        } else {
	        	logger.info("in DisplayEulaActuionExecute, returning error, validEula is null");
	            return ERROR;
	        }
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String start() {
		logger.info("in DisplayEulaActuionStart");
		return SUCCESS;
	}

	public String getEulaText1() {
		return eulaText1;
	}

	public void setEulaText1(String eulaText1) {
		this.eulaText1 = eulaText1;
	}

	public String getEulaText2() {
		return eulaText2;
	}

	public void setEulaText2(String eulaText2) {
		this.eulaText2 = eulaText2;
	}

	public String getEulaText3() {
		return eulaText3;
	}

	public void setEulaText3(String eulaText3) {
		this.eulaText3 = eulaText3;
	}

	public String getEulaText4() {
		return eulaText4;
	}

	public void setEulaText4(String eulaText4) {
		this.eulaText4 = eulaText4;
	}

	public String getEulaText5() {
		return eulaText5;
	}

	public void setEulaText5(String eulaText5) {
		this.eulaText5 = eulaText5;
	}

	public String getEulaText6() {
		return eulaText6;
	}

	public void setEulaText6(String eulaText6) {
		this.eulaText6 = eulaText6;
	}

	public String getEulaText7() {
		return eulaText7;
	}

	public void setEulaText7(String eulaText7) {
		this.eulaText7 = eulaText7;
	}

	public String getEulaText8() {
		return eulaText8;
	}

	public void setEulaText8(String eulaText8) {
		this.eulaText8 = eulaText8;
	}

	public String getEulaText9() {
		return eulaText9;
	}

	public void setEulaText9(String eulaText9) {
		this.eulaText9 = eulaText9;
	}

	public String getEulaText10() {
		return eulaText10;
	}

	public void setEulaText10(String eulaText10) {
		this.eulaText10 = eulaText10;
	}

	public List<String> getAllEulas() {
		return allEulas;
	}

	public void setAllEulas(List<String> allEulas) {
		this.allEulas = allEulas;
	}

	public String getEulaAgreementCode() {
		return eulaAgreementCode;
	}

	public void setEulaAgreementCode(String eulaAgreementCode) {
		this.eulaAgreementCode = Encode.forHtml(eulaAgreementCode);
	}
	
	public DataInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(DataInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public byte[] getEulaPDF() {
		return eulaPDF;
	}

	public void setEulaPDF(byte[] eulaPDF) {
		this.eulaPDF = eulaPDF;
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

	public int getEulaSeqNbr() {
		return eulaSeqNbr;
	}

	public void setEulaSeqNbr(int eulaSeqNbr) {
		this.eulaSeqNbr = eulaSeqNbr;
	}
}
