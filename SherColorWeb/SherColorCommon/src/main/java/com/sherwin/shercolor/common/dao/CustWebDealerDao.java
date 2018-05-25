package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebDealer;

public interface CustWebDealerDao {

	public boolean create(CustWebDealer custWebDealer);
	
	public CustWebDealer read(String customerId);
	
	public CustWebDealer getDealer(String customerId);
	
	public boolean update(CustWebDealer custWebDealer);
	
	public boolean delete(CustWebDealer custWebDealer);
	
	public List<CustWebDealer> listDealers();

}
