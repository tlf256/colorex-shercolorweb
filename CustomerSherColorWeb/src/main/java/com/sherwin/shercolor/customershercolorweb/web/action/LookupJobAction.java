package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CdsClrntSys;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CustWebJobFields;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.ColorantService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.JobHistoryInfo;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;


@SuppressWarnings("serial")
public class LookupJobAction extends ActionSupport implements SessionAware, LoginRequired {
	

	static Logger logger = LogManager.getLogger(LookupJobAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private List<JobField> jobFieldList;
	private int lookupControlNbr;
	private FormulaInfo displayFormula;

	@Autowired
	ColorMastService colorMastService;
	
	@Autowired
	CustomerService customerService;

	@Autowired
	ProductService productService;
	
	@Autowired
	TranHistoryService tranHistoryService;

	@Autowired
	ColorantService colorantService;
	
	@Autowired
	FormulationService formulationService;
	
	List<CustWebTran> tranHistory;
	
	List<JobHistoryInfo> jobHistory;
	
	public String display() {
		
		try {
			jobFieldList = new ArrayList<JobField>();
			List<CustWebJobFields> custWebJobFields;

			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			custWebJobFields = customerService.getCustJobFields(reqObj.getCustomerID());
			
			if(custWebJobFields.size()>0){
				for(CustWebJobFields custField : custWebJobFields){
					JobField jobField = new JobField();
					jobField.setScreenLabel(custField.getScreenLabel());
					jobField.setEnteredValue(custField.getFieldDefault());
					jobFieldList.add(jobField);
				}
			}
			
			reqObj.setJobFieldList(jobFieldList);
			
			sessionMap.put(reqGuid, reqObj);
			
			tranHistory = tranHistoryService.getCustomerJobs(reqObj.getCustomerID());
			jobHistory = new ArrayList<JobHistoryInfo>();
			
			int index= 0;
			for (CustWebTran webTran : tranHistory) {
				if(webTran.isDeleted()) {
					continue;
				} else {
					JobHistoryInfo job = new JobHistoryInfo();
					job.setClrntSysId(tranHistory.get(index).getClrntSysId());
					job.setColorId(tranHistory.get(index).getColorId());
					job.setColorName(tranHistory.get(index).getColorName());
					job.setControlNbr(tranHistory.get(index).getControlNbr());
					job.setProdNbr(tranHistory.get(index).getProdNbr());
					job.setQuantityDispensed(tranHistory.get(index).getQuantityDispensed());
					job.setRgbHex(tranHistory.get(index).getRgbHex());
					job.setSizeCode(tranHistory.get(index).getSizeCode());
					job.setRecipe(getDefaultRecipeInfo(webTran));
					if (job.getRecipe().isEmpty()) {
						job.setNumberOfColorants(0);
					} else {
						job.setNumberOfColorants(job.getRecipe().size());
					}
					job.setJobFieldList(getJobFields(webTran));
					jobHistory.add(job);
				}
				index++;
			}

				return SUCCESS;
		
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
	}
	
	public String execute(){
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			String customerId = reqObj.getCustomerID();
			
			CustWebTran webTran = tranHistoryService.readTranHistory(customerId, lookupControlNbr, 1);

			mapCustWebTranToRequestObject(webTran, reqObj);
			
			displayFormula = reqObj.getDisplayFormula();
			
			// load up can label warnings...
			List<SwMessage> canLabelMsgs = formulationService.canLabelFormulationWarnings(displayFormula);
			reqObj.setCanLabelMsgs(canLabelMsgs);

			sessionMap.put(reqGuid, reqObj);
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
		
	}

	private void mapCustWebTranToRequestObject(CustWebTran webTran, RequestObject reqObj){

		reqObj.setCustomerID(webTran.getCustomerId());
		//reqObj.setCustomerName(webTran.getCustomerId()); //TODO get from another source
		reqObj.setCustomerName(customerService.getDefaultCustomerTitle(webTran.getCustomerId()));
		reqObj.setControlNbr(webTran.getControlNbr());
		reqObj.setLineNbr(webTran.getLineNbr());
		reqObj.setColorType(webTran.getColorType());
		reqObj.setColorComp(webTran.getColorComp());
		reqObj.setColorID(webTran.getColorId());
		reqObj.setColorName(webTran.getColorName());
		reqObj.setPrimerId(webTran.getPrimerId());
		reqObj.setRgbHex(webTran.getRgbHex());
		reqObj.setSalesNbr(webTran.getSalesNbr());
		reqObj.setProdNbr(webTran.getProdNbr());
		
		PosProd posProd = productService.readPosProd(reqObj.getSalesNbr());
		reqObj.setUpc(posProd.getUpc());
		
		reqObj.setSizeCode(webTran.getSizeCode());
		reqObj.setSizeText(productService.getSizeText(webTran.getSizeCode()));
		reqObj.setClrntSys(webTran.getClrntSysId());
		//reqObj.setUserId(webTran.getUserId());
		//reqObj.setFormSource(webTran.getFormSource());
		//reqObj.setFormMethod(webTran.getFormMethod());
		reqObj.setInitTranDate(webTran.getInitTranDate());
		reqObj.setLastTranDate(webTran.getLastTranDate());
		reqObj.setVinylExclude(webTran.isVinylSafe());
		reqObj.setPercentageFactor(webTran.getFormPct());
		
		//formula fields
		FormulaInfo formula = new FormulaInfo();
		List<FormulaIngredient> recipe = new ArrayList<FormulaIngredient>();
		
		recipe = getDefaultRecipeInfo(webTran);
		
		
		formula.setIngredients(recipe);
		formula.setAverageDeltaE(webTran.getAverageDeltaE());
		formula.setClrntSysId(webTran.getClrntSysId());
		if(webTran.getClrntSysId()!=null){
			CdsClrntSys cdsClrntSys = colorantService.getColorantSystem(webTran.getClrntSysId());
			formula.setClrntSysName(cdsClrntSys.getClrntSysName());
			List<String> incrHdr = colorantService.getColorantIncrementHeader(webTran.getClrntSysId());
			formula.setIncrementHdr(incrHdr);
		}
		formula.setColorantCost(webTran.getEngDecisionValue());
		formula.setColorComp(webTran.getColorComp());
		formula.setColorEngineVersion(webTran.getColorEngVer());
		formula.setColorId(webTran.getColorId());
		formula.setContrastRatioThick(webTran.getCrThick());
		formula.setContrastRatioThin(webTran.getCrThin());
		Double[] deltaes = {0D,0D,0D};
		deltaes[0] = webTran.getDeltaEPrimary();
		deltaes[1] = webTran.getDeltaESecondary();
		deltaes[2] = webTran.getDeltaETertiary();
		formula.setDeltaEs(deltaes);
		//formula.setDeltaEComment(webTran.getDeltaEComement()); not saved to DB
		//formula.setDeltaEOverDarkThick(webTran.getDeltaEOverDarkThick); not saved to DB
		//formula.setDeltaEOverDarkThin(webTran.getDeltaEOverDarkThin); not saved to DB
		//formula.setDeltaEThin(webTran.getDeltaEThin()); not saved to DB
		formula.setDeltaEWarning("");
		formula.setFormulationTime(webTran.getFormulationTime());
		String[] illums = {"","",""};
		illums[0] = webTran.getIllumPrimary();
		illums[1] = webTran.getIllumSecondary();
		illums[2] = webTran.getIllumTertiary();
		formula.setIllums(illums);
		formula.setMetamerismIndex(webTran.getMetamerismIndex());
		formula.setPercent(webTran.getFormPct());
		formula.setProdNbr(webTran.getProdNbr());
		formula.setRule(webTran.getFormulaRule());
		formula.setSalesNbr(webTran.getSalesNbr());
		formula.setSizeCode(webTran.getSizeCode());
		//formula.setSource(webTran.getFormSource());
		//formula.setSourceDescr(webTran.getSourceDescr():
		formula.setSpd(webTran.getSpd());
	
		formula.setSource(webTran.getFormSource());
		formula.setSourceDescr(webTran.getFormMethod());
		
		Double[] measCurve = new Double[40];
		Arrays.fill(measCurve, 0D);
		if(webTran.getMeasCurve()!=null && webTran.getMeasCurve().length==40){
			for(int i=0;i<40;i++){
				measCurve[i] = webTran.getMeasCurve()[i];			}
		}
		formula.setMeasuredCurve(measCurve);
		Double[] projCurve = new Double[40];
		Arrays.fill(projCurve, 0D);
		if(webTran.getProjCurve()!=null && webTran.getProjCurve().length==40){
			for(int i=0;i<40;i++){
				projCurve[i] = webTran.getProjCurve()[i];			}
		}
		formula.setProjectedCurve(projCurve);
		reqObj.setDisplayFormula(formula);
		
		// BMW: Update - 1/28/2019 - Put the JobField init code into it's own method so it can be
		//							 called again to pass JobFields to the JobHistoryInfo object.
		List<JobField> jobFields = getJobFields(webTran);
		reqObj.setJobFieldList(jobFields);
		
		// lookup fields not stored to DB tran record
		CdsProd cdsProd = productService.readCdsProd(webTran.getSalesNbr());
		if(cdsProd!=null){
			reqObj.setBase(cdsProd.getBase());
			reqObj.setComposite(cdsProd.getComposite());
			reqObj.setFinish(cdsProd.getFinish());
			reqObj.setIntExt(cdsProd.getIntExt());
			reqObj.setKlass(cdsProd.getKlass());
			reqObj.setQuality(cdsProd.getQuality());
		}
		
		// lookup color fields
		CdsColorMast cdsColorMast = colorMastService.read(webTran.getColorComp(), webTran.getColorId());
		if(cdsColorMast!=null){
			reqObj.setColorVinylOnly(cdsColorMast.getIsVinylSiding());
		}
		
		// set a blank formulaResponse
		reqObj.setFormResponse(new FormulationResponse());
		reqObj.getFormResponse().setMessages(new ArrayList<SwMessage>());
		
		reqObj.setQuantityDispensed(webTran.getQuantityDispensed());
	}
	
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);
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
	public List<CustWebTran> getTranHistory() {
		return tranHistory;
	}
	
	public List<JobHistoryInfo> getJobHistory() {
		return jobHistory;
	}
	
	public List<JobField> getJobFieldList() {
		return jobFieldList;
	}

	public void setJobFieldList(List<JobField> jobFieldList) {
		this.jobFieldList = jobFieldList;
	}

	public int getLookupControlNbr() {
		return lookupControlNbr;
	}

	public void setLookupControlNbr(int lookupControlNbr) {
		this.lookupControlNbr = lookupControlNbr;
	}

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}

	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}
	
	protected List<FormulaIngredient> getDefaultRecipeInfo(CustWebTran webTran){
		
		List<FormulaIngredient> recipe = new ArrayList<FormulaIngredient>();
		
		if(webTran.getClrnt1()!=null){
			FormulaIngredient item1 = new FormulaIngredient();
			item1.setClrntSysId(webTran.getClrntSysId());
			item1.setTintSysId(webTran.getClrnt1());
			item1.setShots(webTran.getClrntAmt1());
			item1.setShotSize(webTran.getShotSize());
			recipe.add(item1);
		}
		if(webTran.getClrnt2()!=null){
			FormulaIngredient item2 = new FormulaIngredient();
			item2.setClrntSysId(webTran.getClrntSysId());
			item2.setTintSysId(webTran.getClrnt2());
			item2.setShots(webTran.getClrntAmt2());
			item2.setShotSize(webTran.getShotSize());
			recipe.add(item2);
		}
		if(webTran.getClrnt3()!=null){
			FormulaIngredient item3 = new FormulaIngredient();
			item3.setClrntSysId(webTran.getClrntSysId());
			item3.setTintSysId(webTran.getClrnt3());
			item3.setShots(webTran.getClrntAmt3());
			item3.setShotSize(webTran.getShotSize());
			recipe.add(item3);
		}
		if(webTran.getClrnt4()!=null){
			FormulaIngredient item4 = new FormulaIngredient();
			item4.setClrntSysId(webTran.getClrntSysId());
			item4.setTintSysId(webTran.getClrnt4());
			item4.setShots(webTran.getClrntAmt4());
			item4.setShotSize(webTran.getShotSize());
			recipe.add(item4);
		}
		if(webTran.getClrnt5()!=null){
			FormulaIngredient item5 = new FormulaIngredient();
			item5.setClrntSysId(webTran.getClrntSysId());
			item5.setTintSysId(webTran.getClrnt5());
			item5.setShots(webTran.getClrntAmt5());
			item5.setShotSize(webTran.getShotSize());
			recipe.add(item5);
		}
		if(webTran.getClrnt6()!=null){
			FormulaIngredient item6 = new FormulaIngredient();
			item6.setClrntSysId(webTran.getClrntSysId());
			item6.setTintSysId(webTran.getClrnt6());
			item6.setShots(webTran.getClrntAmt6());
			item6.setShotSize(webTran.getShotSize());
			recipe.add(item6);
		}
		if(webTran.getClrnt7()!=null){
			FormulaIngredient item7 = new FormulaIngredient();
			item7.setClrntSysId(webTran.getClrntSysId());
			item7.setTintSysId(webTran.getClrnt7());
			item7.setShots(webTran.getClrntAmt7());
			item7.setShotSize(webTran.getShotSize());
			recipe.add(item7);
		}
		if(webTran.getClrnt8()!=null){
			FormulaIngredient item8 = new FormulaIngredient();
			item8.setClrntSysId(webTran.getClrntSysId());
			item8.setTintSysId(webTran.getClrnt8());
			item8.setShots(webTran.getClrntAmt8());
			item8.setShotSize(webTran.getShotSize());
			recipe.add(item8);
		}
		// Complete formulaInfo with following two fields
		if (!recipe.isEmpty()) {
			formulationService.fillIngredientInfoFromTintSysId(recipe);
			formulationService.convertShotsToIncr(recipe);
		}
		
		return recipe;
	}

	protected List<JobField> getJobFields(CustWebTran webTran){
		List<CustWebJobFields> CustWebJobFields = customerService.getCustJobFields(webTran.getCustomerId());
		List<JobField> jobFields = new ArrayList<JobField>();
		int ctr = 0;
		for(CustWebJobFields thisJobField : CustWebJobFields){
			ctr++;
			JobField jobField = new JobField();
			jobField.setScreenLabel(thisJobField.getScreenLabel());
			if(ctr==1) jobField.setEnteredValue(webTran.getJobField01());
			if(ctr==2) jobField.setEnteredValue(webTran.getJobField02());
			if(ctr==3) jobField.setEnteredValue(webTran.getJobField03());
			if(ctr==4) jobField.setEnteredValue(webTran.getJobField04());
			if(ctr==5) jobField.setEnteredValue(webTran.getJobField05());
			if(ctr==6) jobField.setEnteredValue(webTran.getJobField06());
			if(ctr==7) jobField.setEnteredValue(webTran.getJobField07());
			if(ctr==8) jobField.setEnteredValue(webTran.getJobField08());
			if(ctr==9) jobField.setEnteredValue(webTran.getJobField09());
			if(ctr==10) jobField.setEnteredValue(webTran.getJobField10());
			if(jobField.getEnteredValue()==null) jobField.setEnteredValue("");
			jobFields.add(jobField);
		}
		
		return jobFields;
	}

}
