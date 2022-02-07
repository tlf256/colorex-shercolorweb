package com.sherwin.shercolor.customershercolorweb.web.action;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.domain.OeFormInputRequest;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;


public class GenerateFormulaAction extends ActionSupport implements SessionAware, LoginRequired  {
	private Map<String, Object> sessionMap;
	private FormulationService formulationService;
	private ProductService productService;
	private CustomerService customerService;
	private static final long serialVersionUID = 1L;
	
	static Logger logger = LogManager.getLogger(GenerateFormulaAction.class);
	private String colorComp;
	private String colorID;
	private String colorName;
	private String salesNbr;
	private String clrntSys;
	private String displayDate;

	private FormulationResponse theFormula;
	private FormulaInfo displayFormula;
	
	private List<SwMessage> validationMsgs;

	private String reqGuid;
	
	public String execute() {
		//wherein we formulate!
		
		try {
			//set current date for label display after execution.
			Calendar now = Calendar.getInstance();
			int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
			int month = now.get(Calendar.MONTH) + 1;
			String dayOfMonthStr = ((dayOfMonth < 10) ? "0" : "") + month;
			String monthStr = ((month < 10) ? "0" : "") + month;
			setDisplayDate(monthStr+"/"+dayOfMonthStr+"/"+now.get(Calendar.YEAR));
		 
			//call the product service to get all the product info for display
			
			
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			// check if package color
			// if true, skip formulation
			if(reqObj.isPackageColor()) {
				// instantiate empty FormulationResponse obj for package color
				theFormula = new FormulationResponse();
				displayFormula = new FormulaInfo();
				
				// set displayFormula sourceDescr and clrntsys
				displayFormula.setSourceDescr("PACKAGE COLOR");
				displayFormula.setClrntSysId(reqObj.getClrntSys());
				
				List<FormulaInfo> formList = new ArrayList<FormulaInfo>();
				formList.add(displayFormula);
				
				theFormula.setFormulas(formList);
				// status needs to be complete to bypass 
				theFormula.setStatus("COMPLETE");
				
				reqObj.setDisplayFormula(displayFormula);
				reqObj.setFormResponse(theFormula);
			} else {
				 
				OeFormInputRequest oeRequest = new OeFormInputRequest();
				oeRequest.setClrntSysId(reqObj.getClrntSys());
				oeRequest.setColorComp(reqObj.getColorComp());
				oeRequest.setColorId(reqObj.getColorID());
				oeRequest.setSalesNbr(reqObj.getSalesNbr());
				oeRequest.setVinylSafe(reqObj.isVinylExclude());
				String[] illums = new String[3];
				if (reqObj.getLightSource().equals("A")) {
					illums[0] = "A";
					illums[1] = "D65";
					illums[2] = "F2";
					oeRequest.setIllum(illums);
				}
				if (reqObj.getLightSource().equals("D65")) {
					illums[0] = "D65";
					illums[1] = "A";
					illums[2] = "F2";
					oeRequest.setIllum(illums);
				}
				if (reqObj.getLightSource().equals("F2")) {
					illums[0] = "F2";
					illums[1] = "A";
					illums[2] = "D65";
					oeRequest.setIllum(illums);
				}
				
					
				CustWebParms  custWebParms = customerService.getDefaultCustWebParms(reqObj.getCustomerID()); 
				String defaultClrntSys = custWebParms.getClrntSysId();
				//in the case of multiple possible colorant systems, we need to assure that the colorant system
				//on the custWebParms matches the one we are passing in, or the formulation server will return
				//an error.
				custWebParms.setClrntSysId(reqObj.getClrntSys());
				

				
				//call the formulation service
				logger.debug(reqObj.getColorType());
				if(reqObj.getColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")){
					theFormula = formulationService.formulate(oeRequest, custWebParms);
				} else {
					if(reqObj.getColorType().equalsIgnoreCase("COMPETITIVE")) {
						theFormula = formulationService.prodFamilyFormulate(oeRequest, custWebParms);
						
					} else {
						if (reqObj.getColorType().equalsIgnoreCase("CUSTOMMATCH") || reqObj.getColorType().equalsIgnoreCase("SAVEDMEASURE")) {
							//set the curve from the requestObject
							double[] dblCurve = new double[40];
							for (int x=0; x<40; x++) {
								dblCurve[x] = reqObj.getCurveArray()[x].doubleValue();
							}
							oeRequest.setColorCurve(dblCurve);
							theFormula = formulationService.prodFamilyFormulate(oeRequest, custWebParms);
						} else {
							// custom
							// only doing manuals now so return Manual
							 return "manual";
						}
					}
					
				}
				
				// get SwMessages from the FormulationResponse
				List<SwMessage> swmsgList = theFormula.getMessages();
				 
				//save the returned formula in the session using the guid for later display.
				if (theFormula.getFormulas().size() > 0) {
					reqObj.setFormResponse(theFormula);
					sessionMap.put(theFormula.getFormulas().get(0).getOeRequestId() , theFormula);
				}
				
				if (theFormula.getStatus().equals("COMPLETE")) {
					reqObj.setDisplayFormula(theFormula.getFormulas().get(0));
					displayFormula = theFormula.getFormulas().get(0);
					// check for warnings and set those as well.
					validationMsgs = formulationService.validateFormulation(displayFormula, swmsgList);
					
					//PSCWEB-101 - if the colorant system used does not match the customer's default colorant system,
					//             add a warning.
					if (!defaultClrntSys.equals(reqObj.getClrntSys())) {
						SwMessage csMsg = new SwMessage();
						csMsg.setCode("NONDFLTCLRNTSYS");
						csMsg.setMessage(getText("generateFormulaAction.formulaRequiresClrntSys", 
								new String[] {reqObj.getClrntSys(),reqObj.getClrntSys()}));
						csMsg.setSeverity(Level.WARN);
						validationMsgs.add(csMsg);
					}
					
					for(SwMessage item:validationMsgs) {
						addActionMessage(item.getMessage());
					}

					//and post the validation message list to the request object for use in printing.
					reqObj.setDisplayMsgs(validationMsgs);
					
					List<SwMessage> canLabelMsgs = formulationService.canLabelFormulationWarnings(displayFormula, swmsgList);
					reqObj.setCanLabelMsgs(canLabelMsgs);
					
				}
				
				if (theFormula.getStatus().equals("ERROR")) {
					for(SwMessage item:theFormula.getMessages()) {
						// only show errors in the SwMessage list
						if(item.getSeverity().isInRange(Level.FATAL, Level.ERROR)){
							addFieldError("getFormulation", item.getMessage());
						}
					}
				}
			}
				
			sessionMap.put(reqGuid, reqObj);
			return theFormula.getStatus().toLowerCase();
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}


	public String getColorComp() {
		return colorComp;
	}


	public void setColorComp(String colorComp) {
		this.colorComp = Encode.forHtml(colorComp);
	}


	public String getColorID() {
		return colorID;
	}


	public void setColorID(String colorID) {
		this.colorID = Encode.forHtml(colorID);
	}


	public String getSalesNbr() {
		return salesNbr;
	}


	public void setSalesNbr(String salesNbr) {
		this.salesNbr = Encode.forHtml(salesNbr);
	}


	public String getColorName() {
		return colorName;
	}


	public void setColorName(String colorName) {
		this.colorName = Encode.forHtml(colorName);
	}


	public String getClrntSys() {
		return clrntSys;
	}


	public void setClrntSys(String clrntSys) {
		this.clrntSys = Encode.forHtml(clrntSys);
	}


	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}


	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}


	public FormulationResponse getTheFormula() {
		return theFormula;
	}


	public void setTheFormula(FormulationResponse theFormula) {
		this.theFormula = theFormula;
	}

	public String getDisplayDate() {
		return displayDate;
	}


	public void setDisplayDate(String displayDate) {
		//Have the app set current date when formulation is called.
		//this.displayDate = displayDate;
	}
	
	public String getReqGuid() {
		return reqGuid;
	}


	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}


	public ProductService getProductService() {
		return productService;
	}


	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public FormulationService getFormulationService() {
		return formulationService;
	}


	public void setFormulationService(FormulationService formulationService) {
		this.formulationService = formulationService;
	}


	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}


	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}


	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}


	public CustomerService getCustomerService() {
		return customerService;
	}


	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
