package com.sherwin.shercolor.cal.service;


import java.util.List;

import com.sherwin.shercolor.cal.dao.CalDao;
import com.sherwin.shercolor.cal.domain.CalTemplate;
import com.sherwin.shercolor.cal.domain.Ecal;


public interface CalService {
	
	public CalTemplate getCalTemplate(String filename);
	
	List<Ecal> getEcalList(String customerid, String colorantid, String tintermodel, String tinterserial);

	Ecal selectEcal(String filename);

	void uploadEcal(Ecal ecal);

	void setDao(CalDao dao);
		
}
