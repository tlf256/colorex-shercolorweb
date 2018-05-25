package com.sherwin.shercolor.common.service;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.util.domain.SwMessage;

public interface TranHistoryService {

	/***
	 * Creates a new record in the CustWebTran table. This will get the next available control number for the customer.
	 * @param custWebTran - Transaction History record to be added to the CustWebTran table.
	 * @return - When successful the SwMessage will be null, if error occurs then it will be returned in the SwMessage object.
	 */
	public SwMessage saveNewTranHistory (CustWebTran custWebTran);
	
	/***
	 * Updates the record in the CustWebTran table. 
	 * @param custWebTran - Transaction History record to be added to the CustWebTran table.
	 * @return - When successful the SwMessage will be null, if error occurs then it will be returned in the SwMessage object.
	 */
	public SwMessage updateTranHistory (CustWebTran custWebTran);
	
	public CustWebTran readTranHistory(String customerId, int controlNbr, int lineNbr);
	
	public List<CustWebTran> getCustomerJobs(String customerId);
	
	public List<CustWebTranCorr> getCorrections(String customerId, int controlNbr, int lineNbr);
	
	public SwMessage saveNewTranCorrection(CustWebTranCorr custWebTranCorr);
	
	public SwMessage updateTranCorrectionStatus(String customerId, int controlNbr, int lineNbr, int cycle, int unitNbr, String status);
	
	public List<CustWebTranCorr> getAcceptedCorrectionsForCycle(String customerId, int controlNbr, int lineNbr, int cycle);
	
	public SwMessage updateTranCorrectionMerged(String customerId, int controlNbr, int lineNbr, int cycle);
	
	public List<FormulaIngredient> mapTranCorrClrntFieldsToIngredientList(CustWebTranCorr tranCorr);

	public List<CustWebTran> getCustomerOrder(int controlNbr);
	
}
