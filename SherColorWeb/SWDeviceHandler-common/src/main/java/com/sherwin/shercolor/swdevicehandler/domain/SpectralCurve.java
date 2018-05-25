package com.sherwin.shercolor.swdevicehandler.domain;

import java.math.BigDecimal;

public class SpectralCurve {
	private BigDecimal[] curve;

	private int curvePointCnt;
	
	
	public int getCurvePointCnt() {
		return curvePointCnt;
	}
	public void setCurvePointCnt(int curvePointCnt) {
		this.curvePointCnt = curvePointCnt;
	}
	public BigDecimal[] getCurve() {
		return curve;
	}
	public void setCurve(BigDecimal[] curve) {
		this.curve = curve;
	}
	
}
