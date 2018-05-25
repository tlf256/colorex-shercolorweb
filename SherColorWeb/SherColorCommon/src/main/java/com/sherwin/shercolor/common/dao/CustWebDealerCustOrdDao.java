package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;

public interface CustWebDealerCustOrdDao {

	public boolean create(CustWebDealerCustOrd custWebDealerCust);
	
	public CustWebDealerCustOrd read(String customerId, String dlrCustomerId, int controlNbr);
	
	public boolean update(CustWebDealerCustOrd custWebDealerCustOrd);
	
	public boolean delete(CustWebDealerCustOrd custWebDealerCustOrd);
	
	public List<CustWebDealerCustOrd> listCustOrders(String customerId, String dlrCustId);

}
