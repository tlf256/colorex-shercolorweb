package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;

public interface CustWebTinterEventsDetailDao {

	public CustWebTinterEventsDetail read(String guid, String type, String name);

	public boolean create(CustWebTinterEventsDetail teDetail);
	
	public List<CustWebTinterEventsDetail> listForGuid(String masterGuid);
	
}
