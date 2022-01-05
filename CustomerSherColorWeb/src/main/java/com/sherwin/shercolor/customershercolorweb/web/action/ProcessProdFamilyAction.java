package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessProdFamilyAction extends ActionSupport implements SessionAware, LoginRequired {
	private Map<String, Object> sessionMap;
	@Autowired
	private FormulationService formulationService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ProductService productService;
	
	private Map<Integer, List<String>> colorProdFamilies;

	private String selectedProdFamily;
	private FormulaInfo displayFormula;
	private List<FormulaInfo> bothFormulas = new ArrayList<FormulaInfo>();
	private String percentOfFormula;
	private String reqGuid;
	private String returnStatus = "success";
	private List<SwMessage> validationMsgs;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(ProcessProdFamilyAction.class);
	
	
	public ProcessProdFamilyAction(){

	
	}
	
	// User hit the backup button on the "Better Performance Found in Different Base" page
		public String backItUp() {
			try {

				RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
				//reset colorant system and vinyl safe setting.
				reqObj.setClrntSys("");
				reqObj.setVinylExclude(false);
				reqObj.setLightSource("");
				sessionMap.put(reqGuid, reqObj);
			    return SUCCESS;
			} catch (RuntimeException e) {
				logger.error(e.getMessage(), e);
				return ERROR;
			}
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
					Optional<CdsProd> goodProd = productService.readCdsProd(item.getSalesNbr());
					reqObj.setFinish(goodProd.map(CdsProd::getFinish).orElse(""));
					reqObj.setKlass(goodProd.map(CdsProd::getKlass).orElse(""));
					reqObj.setIntExt(goodProd.map(CdsProd::getIntExt).orElse(""));
					reqObj.setBase(goodProd.map(CdsProd::getBase).orElse(""));
					reqObj.setComposite(goodProd.map(CdsProd::getComposite).orElse(""));
					reqObj.setQuality(goodProd.map(CdsProd::getQuality).orElse(""));
					if(reqObj.getColorType().equalsIgnoreCase("CUSTOM")){
						if(reqObj.getIntExt().equalsIgnoreCase("INTERIOR")){
							reqObj.setIntBases(reqObj.getBase());
						} else {
							reqObj.setExtBases(reqObj.getBase());
						}
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
			reqObj.setProductChoosenFromDifferentBase(true);
			return returnStatus;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String display() {
		Optional<CdsProd> theCdsProd;
		String theQuality = "";
		String theBase    = "";
		String theComment = getText("global.comment");
		DecimalFormat dffmt = new DecimalFormat("###.##");
		 try {
				colorProdFamilies = new HashMap<Integer, List<String>>();
				RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
				String theValue;
				String theKey;
				int hashCntr = 0;
				FormulationResponse oldFormula = (FormulationResponse) reqObj.getFormResponse();
				for(FormulaInfo item:oldFormula.getFormulas()) {
					List<String> rowData = new ArrayList<String>();
					theCdsProd = productService.readCdsProd(item.getSalesNbr());
					theQuality = theCdsProd.map(CdsProd::getQuality).orElse("");
					theBase = theCdsProd.map(CdsProd::getBase).orElse("");
					if (theComment.equals(getText("processProdFamilyAction.bestPerformance"))) {
						theComment = getText("processProdFamilyAction.productEntered");
						bothFormulas.add(item);
					}
					if (theComment.equals(getText("global.comment"))) {
						theComment = getText("processProdFamilyAction.bestPerformance");
						bothFormulas.add(item);
					}
					
					rowData.add(item.getProdNbr());
					rowData.add(theQuality);
					rowData.add(theBase);
					rowData.add(dffmt.format(item.getAverageDeltaE()));
					rowData.add(item.getContrastRatioThick().toString());
					rowData.add(theComment);
					
					colorProdFamilies.put(hashCntr, rowData);
					//logger.debug("Choose Product: Row " + hashCntr + " = " + colorProdFamilies.get(hashCntr));
					hashCntr = hashCntr + 1;
				}
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
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


	public Map<Integer, List<String>> getColorProdFamilies() {
		return colorProdFamilies;
	}

	public void setColorProdFamilies(Map<Integer, List<String>> colorProdFamilies) {
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
			sizeText = getText("processProdFamilyAction.qt");
		} else if (sizeCode.equals("16")) {
			sizeText = getText("processProdFamilyAction.gal");
		} else if (sizeCode.equals("20")) {
			sizeText = getText("processProdFamilyAction.5Gal");
		}
		
		return sizeText;
	}
	
}
