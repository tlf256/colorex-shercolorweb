package com.sherwin.shercolor.common.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CustWebActiveProdsPk implements Serializable{
	private int prodNbr;
	private int sizeCode;
	public int getProdNbr() {
		return prodNbr;
	}
	public void setProdNbr(int prodNbr) {
		this.prodNbr = prodNbr;
	}
	public int getSizeCode() {
		return sizeCode;
	}
	public void setSizeCode(int sizeCode) {
		this.sizeCode = sizeCode;
	}
	
}