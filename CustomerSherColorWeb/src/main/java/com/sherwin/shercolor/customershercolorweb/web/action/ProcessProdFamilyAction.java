package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

public class ProcessProdFamilyAction extends ActionSupport implements SessionAware, LoginRequired {
	private Map<String, Object> sessionMap;
	private FormulationService formulationService;
	private CustomerService customerService;
	private ProductService productService;
	
	private Map<String, String> colorProdFamilies;
	private Map<String, String> colorProdFamilies2;
	private String firstFormula;

	private String selectedProdFamily;
	private FormulaInfo displayFormula;
	private List<FormulaInfo> bothFormulas = new ArrayList<FormulaInfo>();
	private String percentOfFormula;
	private String reqGuid;
	private String returnStatus = "success";
	private List<SwMessage> validationMsgs;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessProdFamilyAction.class);
	
	
	public ProcessProdFamilyAction(){

	
	}
	
	public String execute() {
		//wherein we formulate!
		String selectedProdNbr = selectedProdFamily;

		try {
			//logger.debug("in ProcessProdFamilyAction Execute");
			//logger.debug("selectedProdNbr=" + selectedProdNbr);
			
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			FormulationResponse oldFormula = (FormulationResponse) reqObj.getFormResponse();
			reqObj.setPercentageFactor(100);

			
			for(FormulaInfo item:oldFormula.getFormulas()) {
				if (item.getProdNbr().equals(selectedProdNbr)) {
					//logger.debug(" got a formulaInfo match for selectedProdNbr=" + selectedProdNbr);
					displayFormula = formulationService.scaleFormulaByPercent(item, reqObj.getPercentageFactor());
					//logger.debug("scaled Formula, and got a displayFormula");
					//logger.debug("deltaE warning is " + displayFormula.getDeltaEWarning());
					//logger.debug("deltaE over dark is" + displayFormula.getDeltaEOverDarkThin());
					reqObj.setDisplayFormula(displayFormula); 
					//12-12-2016*BKP*Also reset the product and sales numbers and their associated info.
					reqObj.setProdNbr(item.getProdNbr());
					reqObj.setSizeCode(item.getSizeCode());
					reqObj.setSalesNbr(item.getSalesNbr());
					PosProd posprod = productService.readPosProd(item.getSalesNbr());
					reqObj.setUpc(posprod.getUpc());
					//logger.debug("set reqObjprodnbr, sizecode and salesnbr");
					CdsProd goodProd = productService.readCdsProd(item.getSalesNbr());
					String[] prepCmt = new String[2];
					

					if (goodProd!=null) {
						//logger.debug("got goodprod and its not null");
						prepCmt = goodProd.getPrepComment().split("-");
						reqObj.setFinish(goodProd.getFinish());
						reqObj.setKlass(goodProd.getKlass());
						reqObj.setIntExt(goodProd.getIntExt());
						reqObj.setBase(goodProd.getBase());
						reqObj.setComposite(goodProd.getComposite());
						reqObj.setQuality(goodProd.getQuality());
						if(reqObj.getColorType().equalsIgnoreCase("CUSTOM")){
							if(reqObj.getIntExt().equalsIgnoreCase("INTERIOR")){
								reqObj.setIntBases(goodProd.getBase());
							} else {
								reqObj.setExtBases(goodProd.getBase());
							}
						}
					} else {
						reqObj.setFinish("");
						reqObj.setKlass("");
						reqObj.setIntExt("");
						reqObj.setBase("");
						reqObj.setComposite("");
						reqObj.setQuality("");
					}
					reqObj.setSizeText(productService.getSizeText(reqObj.getSizeCode()));
					//logger.debug("ready to validate and set validationmessages");
					validationMsgs = formulationService.validateFormulation(displayFormula);
					//logger.debug("back from validation");
					for(SwMessage item2:validationMsgs) {
						addActionMessage(item2.getMessage());
					}
				}
			}
	
		
			sessionMap.put(reqGuid, reqObj);
			//logger.debug("put reqObj back in session");
			
			return returnStatus;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public String display() {
		CdsProd theCdsProd;
		String theQuality = "";
		String theBase    = "";
		String theComment = "comment";
		DecimalFormat dffmt = new DecimalFormat("###.##");
		 try {
				colorProdFamilies = new HashMap<String, String>();
				colorProdFamilies2 = new HashMap<String, String>();
				firstFormula = "";
				RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
				String theValue;
				String theKey;
				int hashCntr = 0;
				FormulationResponse oldFormula = (FormulationResponse) reqObj.getFormResponse();
				for(FormulaInfo item:oldFormula.getFormulas()) {
					theCdsProd = productService.readCdsProd(item.getSalesNbr());
					if (theCdsProd!=null) {
						 theQuality = theCdsProd.getQuality();
						 theBase    = theCdsProd.getBase();
					} else {
						 theQuality = "";
						 theBase    = "";
					}
					if (theComment.equals("Best Performance")) {
						theComment = "Product Entered";
						bothFormulas.add(item);
					}
					if (theComment.equals("comment")) {
						theComment = "Best Performance";
						bothFormulas.add(item);
					}
					theValue = item.getProdNbr() + "-" 
							+ theQuality  + "-"
							+ theBase + "-" 
							+ dffmt.format(item.getAverageDeltaE()) + "-" 
							+ item.getContrastRatioThick() + "-"
							+ theComment;
					theKey = hashCntr + Character.toString((char) 31)  + item.getProdNbr() + Character.toString((char) 31) + " " 
							+ theQuality + Character.toString((char) 31) + " "
							+ theBase + Character.toString((char) 31) + " " 
							+ dffmt.format(item.getAverageDeltaE()) + Character.toString((char) 31) + " " 
							+ item.getContrastRatioThin() + Character.toString((char) 31) + " "
							+ theComment;
					colorProdFamilies.put(theKey, theValue);
					colorProdFamilies2.put(String.valueOf(hashCntr), theKey);
					if (firstFormula.isEmpty()) {
						firstFormula = theKey;
					}
					//logger.debug("colorProdFamilies2.get(0) = " + colorProdFamilies2.get("0"));
					hashCntr = hashCntr + 1;
				}
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
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
	
	public List<FormulaInfo> getBothFormulas() {
		return bothFormulas;
	}

	public void setBothFormulas(List<FormulaInfo> theFormulas) {
		this.bothFormulas = theFormulas;
	}
	
	public FormulationService getFormulationService() {
		return formulationService;
	}


	public void setFormulationService(FormulationService formulationService) {
		this.formulationService = formulationService;
	}
	

	public CustomerService getCustomerService() {
		return customerService;
	}


	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String getPercentOfFormula() {
		return percentOfFormula;
	}

	public void setPercentOfFormula(String percentOfFormula) {
		this.percentOfFormula = Encode.forHtml(percentOfFormula);
	}
	
	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}


	public Map<String, String> getColorProdFamilies() {
		return colorProdFamilies;
	}

	public void setColorProdFamilies(Map<String, String> colorProdFamilies) {
		this.colorProdFamilies = colorProdFamilies;
	}

	public String getSelectedProdFamily() {
		return selectedProdFamily;
	}

	public void setSelectedProdFamily(String selectedProdFamily) {
		this.selectedProdFamily = selectedProdFamily;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	private String getSizeText(String sizeCode) {
		String sizeText = "";
		
		if (sizeCode==null) {
			sizeText = "";
		} else if (sizeCode.equals("14")) {
			sizeText = "QT";
		} else if (sizeCode.equals("16")) {
			sizeText = "GAL";
		} else if (sizeCode.equals("20")) {
			sizeText = "5GAL";
		}
		
		return sizeText;
	}

	public Map<String, String> getColorProdFamilies2() {
		return colorProdFamilies2;
	}

	public void setColorProdFamilies2(Map<String, String> colorProdFamilies2) {
		this.colorProdFamilies2 = colorProdFamilies2;
	}

	public String getFirstFormula() {
		return firstFormula;
	}

	public void setFirstFormula(String firstFormula) {
		this.firstFormula = Encode.forHtml(firstFormula);
	}

}
