package com.sherwin.shercolor.common.service;


import java.util.List;

import com.sherwin.shercolor.common.dao.CustWebEcalDao;
import com.sherwin.shercolor.common.domain.CustWebEcal;


public interface EcalService {
	List<CustWebEcal> getEcalsByCustomer(String customerid);
	List<CustWebEcal> getEcalTemplate(String colorantid, String tintermodel);
	
	List<CustWebEcal> getEcalList(String customerid, String colorantid, String tintermodel, String tinterserial);

	CustWebEcal selectEcal(String filename);

	void uploadEcal(CustWebEcal ecal);

	void setDao(CustWebEcalDao dao);
	CustWebEcal selectGData(String colorantId);
		
}
