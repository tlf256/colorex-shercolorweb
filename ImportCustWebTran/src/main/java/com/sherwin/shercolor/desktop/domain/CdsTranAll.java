package com.sherwin.shercolor.desktop.domain;

import java.util.List;

public class CdsTranAll {
	private CdsTranMast tranMast;
	private List<CdsTranDetWithClrnt> tranDetWithClrntList;
	
	public CdsTranMast getTranMast() {
		return tranMast;
	}
	public List<CdsTranDetWithClrnt> getTranDetWithClrntList() {
		return tranDetWithClrntList;
	}
	public void setTranMast(CdsTranMast tranMast) {
		this.tranMast = tranMast;
	}
	public void setTranDetWithClrntList(List<CdsTranDetWithClrnt> tranDetWithClrntList) {
		this.tranDetWithClrntList = tranDetWithClrntList;
	}
	
	

}
