package com.sherwin.shercolor.cal.dao;

import java.util.List;

import com.sherwin.shercolor.cal.domain.CalTemplate;
import com.sherwin.shercolor.cal.domain.Ecal;

public interface CalDao {
	public byte[] readFile(String file);
	  public void writeFile(CalTemplate myblob);
	public CalTemplate selectBlob(String filename);

	public void booHoo();
	List<Ecal> getEcalList(String customerid, String tintermodel, String tinterserial);
	List<Ecal> getEcalList(String customerid, String colorantid, String tintermodel, String tinterserial);
	void uploadEcal(Ecal ecal);
	
	Ecal selectEcal(String filename);
	void deleteEcal(String filename);
	


}
