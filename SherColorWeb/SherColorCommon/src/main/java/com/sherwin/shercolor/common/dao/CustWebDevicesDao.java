package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebDevices;



public interface CustWebDevicesDao {
	
	/***
	 * Read a CustWebDevices record for the Customer Id and Serial Number provided.
	 * @param customerId - Customer Id
	 * @param modelType - the model type of device
	 * @param serialNbr - the serial number of the device
	 * @return Single CustWebDevices record or null if not found
	 */
	public CustWebDevices read(String customerId, String modelType, String serialNbr);
	
	/***
	 * Retrieve a List of CustWebDevices records for the Customer Id provided.
	 * @param customerId - Customer Id
	 * @return - List of CustWebDevices records or Zero sized list if none found
	 */
	public List<CustWebDevices> listForCustomerId (String customerId);
	
	/***
	 * Retrieve a List of CustWebDevices records for the Customer Id provided.
	 * @param customerId - Customer Id
	 * @return - List of CustWebDevices records or Zero sized list if none found
	 */
	public List<CustWebDevices> listSpectrosForCustomerId (String customerId);
	
	/***
	 * Retrieve a List of CustWebDevices records for the Customer Id provided.
	 * @param customerId - Customer Id
	 * @return - List of CustWebDevices records or Zero sized list if none found
	 */
	public List<CustWebDevices> listTintersForCustomerId (String customerId);
	
	
	/***
	 * Insert a CustWebDevices record.
	 * @param custWebDev - a CustWebDevices record.
	 * @return - true if successfully inserted, false if an error occurred.
	 */
	public boolean create(CustWebDevices custWebDev);
	
	
	/***
	 * Delete a CustWebDevices record.
	 * @param custWebDev - a CustWebDevices record.
	 * @return - true if successfully deleted, false if an error occurred.
	 */
	public boolean delete(CustWebDevices custWebDev);
}
