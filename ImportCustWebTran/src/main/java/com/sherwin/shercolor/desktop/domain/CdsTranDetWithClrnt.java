package com.sherwin.shercolor.desktop.domain;

import java.util.List;

public class CdsTranDetWithClrnt {
	private CdsTranDet tranDet;
	private List<CdsTranClrnt> tranClrntList;
	
	public CdsTranDet getTranDet() {
		return tranDet;
	}
	public List<CdsTranClrnt> getTranClrntList() {
		return tranClrntList;
	}
	public void setTranDet(CdsTranDet tranDet) {
		this.tranDet = tranDet;
	}
	public void setTranClrntList(List<CdsTranClrnt> tranClrntList) {
		this.tranClrntList = tranClrntList;
	}
	

}
