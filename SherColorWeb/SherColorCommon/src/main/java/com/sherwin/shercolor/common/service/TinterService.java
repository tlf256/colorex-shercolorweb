package com.sherwin.shercolor.common.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

public interface TinterService {

	public List<SwMessage> preDispenseCheck(CustWebDevices deviceInfo, FormulaInfo formInfo);
	
	/***
	 * Service to return a HashMap of colorant code to CustWebColorantsTxt record
	 * @param customerId
	 * @param clrntSysId
	 * @param tinterModel
	 * @param tinterSerialNbr
	 * @return - When failed return a null HashMap, otherwise the return HashMap<String clrntCode, CustWebColorantsTxt>
	 */
	public HashMap<String, CustWebColorantsTxt> getCanisterMap(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr);
	
	public List<SwMessage> decrementFormulaDispense(CustWebDevices deviceInfo, FormulaInfo formInfo);

	public List<CustWebColorantsTxt> getCanisterList(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr);
	public boolean saveColorantsTxt(List<CustWebColorantsTxt> colorantList);
	List<String> listOfColorantSystemsByCustomerId(String customerId);

	public List<String> listOfModelsForCustomerId(String customerId, String clrntSysId);

	/***
	 * Service to determine if the tinter model has an automatic nozzle cover
	 * @param customerId
	 * @param clrntSysId
	 * @param tinterModel
	 * @param tinterSerialNbr
	 * @return - True if the tinter model has an automatic nozzle cover, false if it doesn't
	 */
	public boolean hasAutoNozzleCover(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr);
	
	public CustWebTinterEvents getLastPurgeDateAndUser(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr);
	
	
	public List<SwMessage> writeTinterEventAndDetail(CustWebTinterEvents tintEvent, List<CustWebTinterEventsDetail> teDetails);
	
	/***
	 * Service to update records in the CustWebColorantsTxt table
	 * @param colorantsTxtList - List of CustWebColorantsTxt to be written(updated)
	 * @return - Returns a list of Errors upon failure, upon success the list will be empty
	 */
	public List<SwMessage> updateColorantLevels(List<CustWebColorantsTxt> colorantsTxtList);


	public int conditionalSaveColorantsTxt(List<CustWebColorantsTxt> colorantList);

	public boolean deleteColorantTxtItem(CustWebColorantsTxt colorant);

}
