package com.sherwin.shercolor.common.service;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.common.domain.CustWebJobFields;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.util.domain.SwMessage;

public interface CustomerService {
	
	/***
	 * Retrieve customer name for a given customer ID using sequence 1.
	 * @param customerID - the ID of the customer.
	 * @return The customer name.
	 */
	public String getDefaultCustomerTitle(String customerID);
	
	/***
	 * Retrieve default CustWebParms for a given customer ID using sequence 1.
	 * @param customerID - the ID of the customer.
	 * @return The default/base CustWebParms record for the given customer
	 */
	public CustWebParms getDefaultCustWebParms(String customerID);
	
	/***
	 * Retrieve all sequences of CustWebParms for a given customer ID.
	 * @param customerID - the the ID of the customer.
	 * @return A list of CustWebParms records that relate to the given customer
	 */
	public List<CustWebParms> getAllCustWebParms(String customerID);
	

	/***
	 * Retrieve a List of CustWebJobFields records for the Customer Id provided. NOTE - Sorted by Sequence Number
	 * @param customerId - Customer Id
	 * @return - List of CustWebJobFields records or Zero sized list if none found
	 */
	public List<CustWebJobFields> getCustJobFields(String customerId);
	
	
	/***
	 * Validate entries for the Customer Job Fields
	 * @param jobFieldList - List of entries for the Customer Job Fields
	 * @return a list of 0 or more SwMessages.  0 indicates successful validation.
	 */
	public List<SwMessage> validateCustJobFields(List<String> jobFieldList);
	
	/***
	 * Retrieve a List of CustWebDevices records for the Customer Id provided. NOTE - Sorted by Sequence Number
	 * @param customerId - Customer Id
	 * @return - List of CustWebDevices records or Zero sized list if none found
	 */
	public List<CustWebDevices> getCustDevices(String customerId);
	
	/***
	 * Retrieve a List of CustWebDevices spectro records for the Customer Id provided. NOTE - Sorted by Sequence Number
	 * @param customerId - Customer Id
	 * @return - List of CustWebDevices records or Zero sized list if none found
	 */
	public List<CustWebDevices> getCustSpectros(String customerId);
	
	/***
	 * Retrieve a List of CustWebDevices tinter records for the Customer Id provided. NOTE - Sorted by Sequence Number
	 * @param customerId - Customer Id
	 * @return - List of CustWebDevices records or Zero sized list if none found
	 */
	public List<CustWebDevices> getCustTinters(String customerId);
	
	/***
	 * Insert a new CustWebDevice record into the CUSTWEBDEVICES table.
	 * @param thisDevice - a populated device record.  Sequence Number will be set by the device.
	 * @return - an SwMessage record with any errors that occur.  Record will be empty if successful
	 */
	public SwMessage saveCustDevice(CustWebDevices thisDevice);
	
	/***
	 * Delete an existing CustWebDevice record from the CUSTWEBDEVICES table.
	 * @param thisDevice - a populated device record to be deleted.
	 * @return - an SwMessage record with any errors that occur.  Record will be empty if successful
	 */
	//public SwMessage deleteCustDevice(CustWebDevices thisDevice);
	
	/***
	 * Retrieve customer ID for a given customer using the keyfield (probably user ID).
	 * @param keyField - the ID of the customer.
	 * @return The customer ID.
	 */
	public String getLoginTransformCustomerId(String keyField);
	
}
