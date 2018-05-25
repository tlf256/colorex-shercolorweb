package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebTran;

public interface CustWebTranDao {

	public boolean create(CustWebTran custWebTran);
	
	public CustWebTran read(String customerId, int controlNbr, int lineNbr);
	
	public boolean update(CustWebTran custWebTran);
	
	public boolean delete(CustWebTran custWebTran);
	
	public List<CustWebTran> listForCustomerId(String customerId);
	
	public List<CustWebTran> listForTranCriteria(CustWebTran cwtCriteria);
	
	public List<CustWebTran> listForControlNbr(int controlNbr);
	
}
