package com.sherwin.shercolor.common.service;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;

public interface CustomerOrderService {

	public boolean createDealer(CustWebDealer custWebDealer);

	public CustWebDealer readDealer(String customerId);
	
	public boolean updateDealer(CustWebDealer custWebDealer);
	
	public List<CustWebDealer> listDealers();
	
	public boolean deleteDealer(CustWebDealer custWebDealer);
	
	public boolean createDealerCust(CustWebDealerCust custWebDealerCust);

	public CustWebDealerCust readDealerCust(String customerId, String dlrCustId);
	
	public boolean updateDealerCust(CustWebDealerCust custWebDealerCust);

	public List<CustWebDealerCust> listDealerCustomers(String customerId);
	
	public boolean deleteDealerCust(CustWebDealerCust custWebDealerCust);
	
	public boolean createDealerCustOrd(CustWebDealerCustOrd custWebDealerCustOrd);
	
	public CustWebDealerCustOrd readDealerCustOrd(String customerId, String dlrCustId, int controlNbr);
	
	public boolean updateDealerCustOrd(CustWebDealerCustOrd custWebDealerCustOrd);
	
	public List<CustWebDealerCustOrd> listDealerCustOrders(String customerId, String dlrCustId);
	
	public boolean deleteDealerCustOrd(CustWebDealerCustOrd custWebDealerCustOrd);
	
	public List<CustWebDealerCust> listDealerCustomersAutocomplete(String customerId, String dlrCustId);

}
