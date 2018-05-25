package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebTranCorr;

public interface CustWebTranCorrDao {
	
	public boolean create(CustWebTranCorr custWebTranCorr);
	
	public CustWebTranCorr read(String customerId, int controlNbr, int lineNbr, int cycle, int unitNbr, int step);
	
	public boolean update(CustWebTranCorr custWebTranCorr);
	
	public boolean delete(CustWebTranCorr custWebTranCorr);
	
	public List<CustWebTranCorr> listForCustomerOrderLine(String customerId, int controlNbr, int lineNbr);
	
	public List<CustWebTranCorr> listForTranCorrCycleUnit(String customerId, int controlNbr, int lineNbr, int cycle, int unitNbr);

}
