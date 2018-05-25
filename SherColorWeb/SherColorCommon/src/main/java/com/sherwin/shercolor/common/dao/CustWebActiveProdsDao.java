package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebActiveProds;

public interface CustWebActiveProdsDao {

public CustWebActiveProds getActiveProdBySalesNbr(String salesnbr);
	
public CustWebActiveProds sgetActiveProdBySalesNbr(String salesnbr);

public CustWebActiveProds getActiveProdByRexSize(String prodnbr, String sizeCd);

public List<CustWebActiveProds> getActiveProdByRex(String prodnbr);

public List<CustWebActiveProds> getAllActiveProds();

}
