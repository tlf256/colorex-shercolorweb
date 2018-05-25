package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebDealerCust;

public interface CustWebDealerCustDao {

	public boolean create(CustWebDealerCust custWebDealerCust);
	
	public CustWebDealerCust read(String customerId, String dlrCustomerId);
	
	public boolean update(CustWebDealerCust custWebDealerCust);
	
	public boolean delete(CustWebDealerCust custWebDealerCust);
	
	public List<CustWebDealerCust> listCustomers(String customerId);
	
	public List<CustWebDealerCust> listCustomersAutocomplete(String customerId, String partialKey);
}
