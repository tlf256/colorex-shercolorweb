package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebEcal;

public interface CustWebEcalDao {
	
	List<CustWebEcal> getEcalTemplate(String colorantid, String tintermodel);
	List<CustWebEcal> getEcalList(String customerid);
	List<CustWebEcal> getEcalList(String customerid, String tintermodel, String tinterserial);
	List<CustWebEcal> getEcalList(String customerid, String colorantid, String tintermodel, String tinterserial);
	int uploadEcal(CustWebEcal ecal);
	
	CustWebEcal selectEcal(String filename);
	int deleteEcal(String filename);
	CustWebEcal selectGData(String colorantId);


	


}
