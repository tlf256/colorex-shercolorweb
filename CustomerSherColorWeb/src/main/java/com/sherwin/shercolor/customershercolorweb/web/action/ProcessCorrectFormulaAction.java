package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sherwin.shercolor.common.entity.CustWebColorantsTxt;
import com.sherwin.shercolor.common.entity.CustWebTranCorr;
import com.sherwin.shercolor.common.model.FormulaInfo;
import com.sherwin.shercolor.common.model.FormulaIngredient;
import com.sherwin.shercolor.common.model.ProductFillInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.util.CorrectionInfoBuilder;
import com.sherwin.shercolor.customershercolorweb.util.CorrectionInfoBuilderImpl;
import com.sherwin.shercolor.customershercolorweb.web.model.CorrectionInfo;
import com.sherwin.shercolor.customershercolorweb.web.model.CorrectionStep;
import com.sherwin.shercolor.customershercolorweb.web.model.DispenseItem;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

public class ProcessCorrectFormulaAction extends ActionSupport implements SessionAware, LoginRequired {
	private Map<String, Object> sessionMap;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessCorrectFormulaAction.class.getName());
	
	private String reqGuid;
	private FormulaInfo displayFormula;
	
	private String errorMessage;

	// for display
	private int cycle;
	private int nextUnitNbr;
	private List<CorrectionStep> correctionHistory;
	private boolean sessionHasTinter;
	private boolean siteHasPrinter;
	private int lastStep;
	private int acceptedContNbr;
	private int[] skippedCont;
	private int[] discardedCont;
	private String maxLoadType;
	private double maxClrntLoad = 0.0;
	private double currClrntLoad = 0.0;
	private double clrntSpaceAvail = 0.0;
	
	// for percentAddtion mathematics
	private List<FormulaIngredient> ingredientList;
	private int percentOfFormula;
	
	// for convert correction display increments to dispense items
	private List<Map<String,Object>> correctionList;
	private List<DispenseItem> dispenseItemList;
	
	// for save correction step and post "ACCEPTED"
	private boolean mergeCorrWithStartingForm = false;
	private List<Map<String,Object>> shotList;
	private String reason;
	private String jsDateString;
	private String stepMethod;
	private String stepStatus;
	private String corrStatus;
	
	@Autowired
	TranHistoryService tranHistoryService;
	@Autowired
	FormulationService formulationService;
	@Autowired
	TinterService tinterService;
	@Autowired
	ProductService productService;

	TinterInfo tinter = null;
	
	public String display(){
		String retVal;
		
		try{
			logger.debug("Inside ProccessCorrectFormulaAction - display");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			displayFormula = reqObj.getDisplayFormula();
			
			setSiteHasPrinter(reqObj.isPrinterConfigured());
			 
			List<CustWebTranCorr> tranCorrList = tranHistoryService.getCorrections(reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr());
			
			sessionHasTinter = false;
			tinter = reqObj.getTinter();
			if(tinter!=null && tinter.getModel()!=null && !tinter.getModel().isEmpty()) sessionHasTinter = true;
			
			correctionHistory = new ArrayList<CorrectionStep>();
			
			CorrectionInfoBuilder corrBuilder = new CorrectionInfoBuilderImpl(tranHistoryService, tinterService);
			
			CorrectionInfo corrInfo = corrBuilder.getCorrectionInfo(reqObj, tranCorrList);
			logger.debug("back from getCorrInfo");
			nextUnitNbr = corrInfo.getNextUnitNbr();
			cycle = corrInfo.getCycle();
			lastStep = corrInfo.getLastStep();
			corrStatus = corrInfo.getCorrStatus();
			acceptedContNbr = corrInfo.getAcceptedContNbr();
			dispenseItemList = corrInfo.getAcceptedDispenseList();
			correctionHistory = corrInfo.getCorrectionStepList();
			discardedCont = corrInfo.getDiscardedCont();
			skippedCont = corrInfo.getSkippedCont();
			
			logger.debug("getting product fill info");
			// Colorant Fill details
			ProductFillInfo productFillInfo = productService.getProductFillInfo(reqObj.getSalesNbr(), reqObj.getClrntSys());
			if(productFillInfo.getProductMaxOverLoad()>0D){
				maxClrntLoad = productFillInfo.getProductMaxOverLoad();
				maxLoadType = "ACTUAL_ENFORCE";
			} else {
				if(productFillInfo.getProductMaxLoad()>0D){
					maxClrntLoad = productFillInfo.getProductMaxLoad();
					maxLoadType = "ACTUAL_WARN";
				} else {
					maxClrntLoad = productFillInfo.getEstimateMaxLoad();
					maxLoadType = "ESTIMATED_WARN";
				}
			}
			// Total up clrnt dispensed shots and divide by shots size to get oz
			int openShots = 0;
			int uom = 0;
			// current correction dispensed...
			logger.debug("sum curr corr dispensed");
			if(corrInfo.getOpenDispenseList()!=null && corrInfo.getOpenDispenseList().size()>0){
				for(DispenseItem item : corrInfo.getOpenDispenseList()){
					openShots = openShots + item.getShots();
				}
			}
			// current formula ...
			logger.debug("sum curr formula");
			for(FormulaIngredient ingr : displayFormula.getIngredients()){
				openShots += ingr.getShots();
				uom = ingr.getShotSize();
			}
			currClrntLoad = (double) openShots / (double) uom;
			clrntSpaceAvail = maxClrntLoad - currClrntLoad;

			retVal = INPUT;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			retVal = ERROR;
		}

		return retVal;
	}
	
	public String percentAddition(){
		String retVal;
		
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// get displayFormula and apply percent of formula, return ingredientList
			
			displayFormula = reqObj.getDisplayFormula();
			
			logger.debug("in percentAddition, about to call scaleByPercent");
			FormulaInfo adjFormula = formulationService.scaleFormulaByPercent(displayFormula, percentOfFormula);
			
			logger.debug("in percentAddition, origFormula ingredient count returned is " + displayFormula.getIngredients().size());
			for(FormulaIngredient item : displayFormula.getIngredients()){
				logger.debug("item " + item.getTintSysId() + " " + item.getName() + " " + Arrays.toString(item.getIncrement()));
			}

			ingredientList = adjFormula.getIngredients();
			
			logger.debug("in percentAddition, ingredient count returned is " + ingredientList.size());
			for(FormulaIngredient item : ingredientList){
				logger.debug("item " + item.getTintSysId() + " " + item.getName() + " " + Arrays.toString(item.getIncrement()));
			}

			retVal = SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			errorMessage = getText("processCorrectFormulaAction.exceptionThrown");  
			retVal = ERROR;
		}

		return retVal;
	}
	
	@SuppressWarnings("unchecked")
	public String convertFormulaToDispenseItems(){
		String retVal = null;
		logger.debug("inside convertFormulaToDispenseItems");
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			logger.debug("inside convertFormulaToDispenseItems: got reqObj");
			
			TinterInfo tinter = reqObj.getTinter();
			logger.debug("inside convertFormulaToDispenseItems: got tinter");
			
			if(correctionList!=null){
				if(correctionList.size()>0){
					logger.debug("inside convertFormulaToDispenseItems: walking map ");
					List<FormulaIngredient> ingredientList = new ArrayList<FormulaIngredient>();
					for(Map<String,Object> item : correctionList){
						String clrntString=null;
						logger.debug("inside convertFormulaToDispenseItems: item is " + item.toString());
						if(item.get("clrntString")!=null) clrntString= item.get("clrntString").toString();
						logger.debug("pulled clrntString and it is " + clrntString);
						List<Long> incrList = null;
						List<Object> objList = null;
						logger.debug(item.get("incrArray").getClass().getName());
						objList = (ArrayList<Object>) item.get("incrArray");
						for(Object obj : objList){
							logger.debug(obj.getClass().getName());
						}
						if(item.get("incrArray") != null) incrList = (ArrayList<Long>) item.get("incrArray");

						int[] incr = {0,0,0,0};
						if(clrntString!=null && incrList!=null && incrList.size()==4){
							for(int i=0;i<4;i++){
								incr[i] = incrList.get(i).intValue();
							}
							FormulaIngredient addMe = new FormulaIngredient();
							addMe.setIncrement(incr);
							addMe.setClrntSysId(reqObj.getClrntSys());
							addMe.setTintSysId(clrntString.substring(0, clrntString.indexOf("-")));
							ingredientList.add(addMe);
						} else {
							retVal = ERROR;
							errorMessage = getText("processCorrectFormulaAction.badClrntIncrementConversionFailed");//"Bad Colorant Increments! Colorant Increment Conversion Failed.";
						}
					} // end for each correctionList
					if(retVal==null || !retVal.equals(ERROR)){
						//ConvertIncrToShots
						logger.debug("about to convertIncrToShots");
						formulationService.convertIncrToShots(ingredientList);
						//Make a list of dispenseItems to return to user
						logger.debug("getting colorantMap for can position");
						HashMap<String,CustWebColorantsTxt> colorantMap = tinterService.getCanisterMap(reqObj.getCustomerID(), tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr());

						logger.debug("back from tinterService");
						if(colorantMap!=null){
							logger.debug("colorant map is not null");
							if(dispenseItemList==null) dispenseItemList = new ArrayList<DispenseItem>();
							else dispenseItemList.clear();

							for(FormulaIngredient ingr : ingredientList){
								logger.debug("pulling map info for " + ingr.getTintSysId());
								DispenseItem addItem = new DispenseItem();
								addItem.setClrntCode(ingr.getTintSysId());
								addItem.setShots(ingr.getShots());
								addItem.setUom(ingr.getShotSize());
								addItem.setPosition(colorantMap.get(ingr.getTintSysId()).getPosition());
								dispenseItemList.add(addItem);
							}

							retVal = SUCCESS;

						} else {
							logger.debug("colorant map is null for " + reqObj.getCustomerID() + " " + tinter.getClrntSysId() + " " + tinter.getModel() + " " + tinter.getSerialNbr());
							errorMessage = getText("processCorrectFormulaAction.clrntIncrementConversionFailed");
							retVal = ERROR;
						}
						retVal = SUCCESS;
					}
					
				} else {
					// empty list passed in
					logger.debug("inside convertFormulaToDispenseItems: correctionList is empty");
					errorMessage = getText("processCorrectFormulaAction.clrntIncrementConversionFailed");
					retVal = ERROR;
				}
			} else {
				// null list passed in
				logger.debug("inside convertFormulaToDispenseItems: correctionList is null");
				errorMessage = getText("processCorrectFormulaAction.clrntIncrementConversionFailed");
				retVal = ERROR;
			}

			
			retVal = SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			retVal = ERROR;
			errorMessage = getText("processCorrectFormulaAction.exceptionThrown");
		}

		return retVal;
		
	}
	
	public String saveCorrectionStep(){
		String retVal;
		
		try{
			logger.debug("in saveCorrectionStep, about to get reqObj");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// get Correction Step info, save to DB
			
			displayFormula = reqObj.getDisplayFormula();
			
			logger.debug("in saveCorrectionStep, about to parse info sent from javascript");
			
			CustWebTranCorr tranCorr = new CustWebTranCorr();
			tranCorr.setCustomerId(reqObj.getCustomerID());
			tranCorr.setControlNbr(reqObj.getControlNbr());
			tranCorr.setLineNbr(reqObj.getLineNbr());
			tranCorr.setCycle(cycle);
			tranCorr.setUnitNbr(nextUnitNbr);
			tranCorr.setReason(reason);
			tranCorr.setStatus(stepStatus);
			tranCorr.setUserId(reqObj.getFirstName()+ " " + reqObj.getLastName());
			SimpleDateFormat jsdf = new SimpleDateFormat("EE MMM d y H:m:s 'GMT'Z (zz)");
			//BKP-20170730-PSCWEB 316
			Date localeDateTime;
			if(jsDateString!=null && !jsDateString.isEmpty()){
				try {
					localeDateTime = jsdf.parse(jsDateString);
				} catch (Exception e) {
					localeDateTime = new Date();
				}
			} else {
				localeDateTime = new Date();
			}
			tranCorr.setDateTime(localeDateTime);
			tranCorr.setCorrMethod(stepMethod);
			tranCorr.setMergedWithOrig(false);
			tranCorr.setClrntSysId(reqObj.getClrntSys());
			// walk shotList to get clrnt, clrntAmt and shotSize
			if(shotList!=null && shotList.size()>0 && 
			   (!tranCorr.getCorrMethod().equalsIgnoreCase("SKIP CONTAINER") || !tranCorr.getCorrMethod().equalsIgnoreCase("PREVIOUSLY SKIPPED"))
			){
				int ctr = 1;
				for(Map<String,Object> item : shotList){
					String code = null; Long uom = null; Long shots = null;
					if(item.get("code")!=null) code = item.get("code").toString();
					logger.debug("about to convert uom to Long");
					if(item.get("uom")!=null) uom = (Long) item.get("uom");
					logger.debug("about to convert shots to Long");
					if(item.get("shots")!=null) shots = (Long) item.get("shots");
					if(code!=null && shots!=null && shots!=null){
						tranCorr.setShotSize(uom.intValue());
						switch (ctr) {
						case 1:
							tranCorr.setClrnt1(code);
							tranCorr.setClrntAmt1(shots.intValue());
							break;
						case 2:
							tranCorr.setClrnt2(code);
							tranCorr.setClrntAmt2(shots.intValue());
							break;
						case 3:
							tranCorr.setClrnt3(code);
							tranCorr.setClrntAmt3(shots.intValue());
							break;
						case 4:
							tranCorr.setClrnt4(code);
							tranCorr.setClrntAmt4(shots.intValue());
							break;
						case 5:
							tranCorr.setClrnt5(code);
							tranCorr.setClrntAmt5(shots.intValue());
							break;
						case 6:
							tranCorr.setClrnt6(code);
							tranCorr.setClrntAmt6(shots.intValue());
							break;
						case 7:
							tranCorr.setClrnt7(code);
							tranCorr.setClrntAmt7(shots.intValue());
							break;
						case 8:
							tranCorr.setClrnt8(code);
							tranCorr.setClrntAmt8(shots.intValue());
							break;
						} // end switch
						
						ctr++;
					} // end good shotList item check
				} // end for each shotList
			} // end shotList empty test
			
			// now write it
			SwMessage result = tranHistoryService.saveNewTranCorrection(tranCorr);

			if(result==null || result.getCode()==null){
				logger.debug("inside saveStep and stepStatus is " + stepStatus);
				if(!stepStatus.equalsIgnoreCase("OPEN") && nextUnitNbr==reqObj.getQuantityDispensed()) mergeCorrWithStartingForm = true;
				retVal = SUCCESS;
			} else {
				logger.debug("in saveStep tranHistoryService save returned " + result.getCode());
				retVal = ERROR;
				errorMessage = result.getMessage();
			}

		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			retVal = ERROR;
			errorMessage = getText("processCorrectFormulaAction.exceptionThrownWriteFailed");
		}

		return retVal;
		
	}
	
	
	public String postContainerStatus(){
		String retVal;
		
		try{
			logger.debug("in postContainerStatus, about to get reqObj");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			displayFormula = reqObj.getDisplayFormula();
			
			logger.debug("in postContainerStatus, about to parse info sent from javascript");
			
			if(stepStatus!=null){
				SwMessage result = tranHistoryService.updateTranCorrectionStatus(reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr(), cycle, nextUnitNbr, stepStatus);
				if(result==null || result.getCode()==null){
					logger.debug("updatedTranCorrStatus, nextUnitNbr is" + nextUnitNbr + " and qty disp is " + reqObj.getQuantityDispensed());
					if(nextUnitNbr==reqObj.getQuantityDispensed()) mergeCorrWithStartingForm = true;
					logger.debug("mergeCorr is " + mergeCorrWithStartingForm);
					// First time the formula is accepted the shotList information is not available for the first correction label
					// This conditional will help pass back information to help construct a shotList for the first accepted container
					if (stepStatus.equalsIgnoreCase("ACCEPTED")) {
						List<CustWebTranCorr> tranCorrList = tranHistoryService.getCorrections(reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr());
						CorrectionInfoBuilder corrBuilder = new CorrectionInfoBuilderImpl(tranHistoryService, tinterService);
						CorrectionInfo corrInfo = corrBuilder.getCorrectionInfo(reqObj, tranCorrList);
						dispenseItemList = corrInfo.getAcceptedDispenseList();
					}
					retVal = SUCCESS;
				} else {
					logger.debug("in postContainerStatus tranHistoryService update returned " + result.getCode());
					retVal = ERROR;
					errorMessage = getText("processCorrectFormulaAction.tranHistoryServiceUpdateFailed"); 
				}
			} else {
				retVal = ERROR;
				errorMessage = getText("processCorrectFormulaAction.missingStatusPostFailed"); 
			}
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			errorMessage = getText("processCorrectFormulaAction.exceptionThrown");
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

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}

	public List<CorrectionStep> getCorrectionHistory() {
		return correctionHistory;
	}

	public boolean isSessionHasTinter() {
		return sessionHasTinter;
	}

	public List<FormulaIngredient> getIngredientList() {
		return ingredientList;
	}

	public void setPercentOfFormula(int percentOfFormula) {
		this.percentOfFormula = percentOfFormula;
	}

	public List<DispenseItem> getDispenseItemList() {
		return dispenseItemList;
	}

	public void setCorrectionList(List<Map<String, Object>> correctionList) {
		this.correctionList = correctionList;
	}

	public int getNextUnitNbr() {
		return nextUnitNbr;
	}

	public void setNextUnitNbr(int nextUnitNbr) {
		this.nextUnitNbr = nextUnitNbr;
	}

	public void setShotList(List<Map<String, Object>> shotList) {
		this.shotList = shotList;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setJsDateString(String jsDateString) {
		this.jsDateString = Encode.forHtml(jsDateString);
	}

	public void setStepMethod(String stepMethod) {
		this.stepMethod = Encode.forHtml(stepMethod);
	}

	public void setStepStatus(String stepStatus) {
		if (stepStatus==null) {
			this.stepStatus = stepStatus;
		} else {
			this.stepStatus = Encode.forHtml(stepStatus);
		}
	}

	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getLastStep() {
		return lastStep;
	}

	public String getCorrStatus() {
		return corrStatus;
	}

	public int getAcceptedContNbr() {
		return acceptedContNbr;
	}

	public int[] getSkippedCont() {
		return skippedCont;
	}

	public int[] getDiscardedCont() {
		return discardedCont;
	}

	public boolean isMergeCorrWithStartingForm() {
		return mergeCorrWithStartingForm;
	}

	public double getMaxClrntLoad() {
		return maxClrntLoad;
	}

	public double getCurrClrntLoad() {
		return currClrntLoad;
	}

	public double getClrntSpaceAvail() {
		return clrntSpaceAvail;
	}

	public String getMaxLoadType() {
		return maxLoadType;
	}

	public TinterInfo getTinter() {
		return tinter;
	}

	public void setTinter(TinterInfo tinter) {
		this.tinter = tinter;
	}

	public boolean isSiteHasPrinter() {
		return siteHasPrinter;
	}

	public void setSiteHasPrinter(boolean siteHasPrinter) {
		this.siteHasPrinter = siteHasPrinter;
	}
	
}
