package com.sherwin.shercolor.common.dao;

import java.util.List;
import java.util.Map;

import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;

public interface CustWebColorantsTxtDao  {
	
	public boolean create(CustWebColorantsTxt custWebTran);
	
	public CustWebColorantsTxt read(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr, String clrntCode, int position);
	
	public boolean update(CustWebColorantsTxt custWebTran);
	
	public boolean delete(CustWebColorantsTxt custWebTran);
	
	public List<CustWebColorantsTxt> listForUniqueTinter(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr);
	
	public List<String> listOfModelsForCustomerId(String customerId, String clrntSysId);

	public List<String> listOfColorantSystemsByCustomerId(String customerId);
	
	
		

	

}
