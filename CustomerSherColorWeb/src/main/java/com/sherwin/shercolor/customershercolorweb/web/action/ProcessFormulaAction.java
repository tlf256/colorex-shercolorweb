package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.util.CorrectionInfoBuilder;
import com.sherwin.shercolor.customershercolorweb.util.CorrectionInfoBuilderImpl;
import com.sherwin.shercolor.customershercolorweb.util.ShercolorLabelPrintImpl;
import com.sherwin.shercolor.customershercolorweb.web.model.CorrectionInfo;
import com.sherwin.shercolor.customershercolorweb.web.model.CorrectionStep;
import com.sherwin.shercolor.customershercolorweb.web.model.DispenseItem;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;


@SuppressWarnings("serial")
public class ProcessFormulaAction extends ActionSupport implements SessionAware, LoginRequired  {

	private DataInputStream inputStream;
	static Logger logger = LogManager.getLogger(ProcessFormulaAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private FormulaInfo displayFormula;
	private List<DispenseItem> dispenseFormula;
	private int qtyDispensed;
	private TinterInfo tinter;
	private int recDirty;
	private boolean siteHasTinter;
	private boolean sessionHasTinter;
	private boolean midCorrection = false;

	private TinterService tinterService;
	private TranHistoryService tranHistoryService;
	private List<CustWebTran> tranHistory;

	public String display(){
		String retVal = null;

		try {
			//logger.info("reqGuid is " + reqGuid);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			displayFormula = reqObj.getDisplayFormula();
			qtyDispensed = reqObj.getQuantityDispensed();
			tinter = reqObj.getTinter();
			
			// setup formula display messages since this now intercepts other actions from going directly to displayFormula.jsp
			if(reqObj.getDisplayMsgs()!=null){
				for(SwMessage item:reqObj.getDisplayMsgs()) {
					addActionMessage(Encode.forHtml(item.getMessage()));
				}
				// after all have been presented to user, clear the messages from the request object
				// if deltae warning, do not clear display message because they will not go directly to final formula display
				if(displayFormula.getDeltaEWarning()==null || displayFormula.getDeltaEWarning().isEmpty()) {
					reqObj.getDisplayMsgs().clear();
				}
			}

			// Check if correction in process
			List<CustWebTranCorr> tranCorrList = tranHistoryService.getCorrections(reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr());
			
			CorrectionInfoBuilder corrBuilder = new CorrectionInfoBuilderImpl(tranHistoryService, tinterService);
			
			CorrectionInfo corrInfo = corrBuilder.getCorrectionInfo(reqObj, tranCorrList);
			if(corrInfo.getCorrStatus().equalsIgnoreCase("MIDUNIT") || corrInfo.getCorrStatus().equalsIgnoreCase("MIDCYCLE")){
				midCorrection = true;
				addActionMessage(Encode.forHtml("The Formula is Currently Being Corrected. Please Complete the Correction Cycle before taking other actions."));
			} else {
				midCorrection = false;
			}
			

			siteHasTinter = false;
			List<String> tinterList = tinterService.listOfModelsForCustomerId(reqObj.getCustomerID(), null);
			if(tinterList.size()>0) siteHasTinter = true;

			sessionHasTinter = false;
			// BMW: Update - 1/21/2018 - Added an additional conditional check here to prevent dispense
			// 							 when creating a job that uses a colorant system that does not
			//							 match the currently used tinter
			if(tinter!=null && tinter.getModel()!=null && !tinter.getModel().isEmpty() && tinter.getClrntSysId().equals(reqObj.getClrntSys())){
				sessionHasTinter = true;
				// Setup Tinter Colorant Dispense Info for Formula being displayed
				System.out.println("About to get colorant map for " + reqObj.getCustomerID() + " " + tinter.getClrntSysId() + " " + tinter.getModel() + " " + tinter.getSerialNbr());
				HashMap<String,CustWebColorantsTxt> colorantMap = tinterService.getCanisterMap(reqObj.getCustomerID(), tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr());

				System.out.println("back from tinterService");
				if(colorantMap!=null && !colorantMap.isEmpty()){
					
					//Validating completeness of colorantMap data returned from DB. If not, send error msg back to DisplayJobs.jsp
					for(FormulaIngredient ingr : displayFormula.getIngredients()){
						if(!colorantMap.containsKey(ingr.getTintSysId())){
							tranHistory = tranHistoryService.getCustomerJobs(reqObj.getCustomerID());
							addActionMessage(Encode.forHtml("The selected Job is missing Colorant - " + ingr.getTintSysId() + ". Cannot load Job."));
							System.out.println("Colorant map is incomplete for Colorant: " + ingr.getTintSysId() + " in Colorant System: " + ingr.getClrntSysId());
							return "errormsg";
						}
					}
					
					System.out.println("colorant map is not null");
					if(dispenseFormula==null) dispenseFormula = new ArrayList<DispenseItem>();
					else dispenseFormula.clear();

					for(FormulaIngredient ingr : displayFormula.getIngredients()){
						System.out.println("pulling map info for " + ingr.getTintSysId());
						DispenseItem addItem = new DispenseItem();
						addItem.setClrntCode(ingr.getTintSysId());
						System.out.println(addItem.getClrntCode());
						addItem.setShots(ingr.getShots());
						System.out.println(addItem.getShots());
						addItem.setUom(ingr.getShotSize());
						System.out.println(addItem.getUom());
						addItem.setPosition(colorantMap.get(ingr.getTintSysId()).getPosition());
						dispenseFormula.add(addItem);
					}

					retVal = SUCCESS;

				} else {
					System.out.println("colorant map is null for " + reqObj.getCustomerID() + " " + tinter.getClrntSysId() + " " + tinter.getModel() + " " + tinter.getSerialNbr());
					retVal = ERROR;
				}
			} else {
				// no tinter go on...
				retVal = SUCCESS;

			}

		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			e.printStackTrace();
			retVal = ERROR;
		}

		return retVal;

	}

	public String execute() {

		try {

			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}

	public String print() {

		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			ShercolorLabelPrintImpl printLabel = new ShercolorLabelPrintImpl();
			printLabel.CreateLabelPdf("label.pdf", reqObj);
			inputStream = new DataInputStream( new FileInputStream(new File("label.pdf")));

			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}

	public String save() {

		try {

			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}

	public DataInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(DataInputStream inputStream) {
		this.inputStream = inputStream;
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

	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}

	public List<DispenseItem> getDispenseFormula() {
		return dispenseFormula;
	}

	public void setDispenseFormula(List<DispenseItem> dispenseFormula) {
		this.dispenseFormula = dispenseFormula;
	}

	public TinterService getTinterService() {
		return tinterService;
	}

	public void setTinterService(TinterService tinterService) {
		this.tinterService = tinterService;
	}

	public int getQtyDispensed() {
		return qtyDispensed;
	}

	public void setQtyDispensed(int qtyDispensed) {
		this.qtyDispensed = qtyDispensed;
	}

	public TinterInfo getTinter() {
		return tinter;
	}

	public int getRecDirty() {
		return recDirty;
	}

	public void setRecDirty(int recDirty) {
		this.recDirty = recDirty;
	}

	public boolean isSessionHasTinter() {
		return sessionHasTinter;
	}

	public boolean isSiteHasTinter() {
		return siteHasTinter;
	}

	public TranHistoryService getTranHistoryService() {
		return tranHistoryService;
	}

	public void setTranHistoryService(TranHistoryService tranHistoryService) {
		this.tranHistoryService = tranHistoryService;
	}

	public boolean isMidCorrection() {
		return midCorrection;
	}

	public List<CustWebTran> getTranHistory() {
		return tranHistory;
	}

}
