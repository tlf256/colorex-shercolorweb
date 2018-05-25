package com.sherwin.shercolor.swdevicehandler.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="command")
public class SimSpectroResponseCommand {

	private String name;
	private int waitMilliseconds;
	private String responseMessage;
	private int errorNumber;
	private String errorMessage;
	private SpectralCurve theCurve;
	private List<String> curvePoints = new ArrayList<String>(40);
	
	public String getName() {
		return name;
	}
	public int getWaitMilliseconds() {
		return waitMilliseconds;
	}
	public int getErrorNumber() {
		return errorNumber;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	
	public SpectralCurve getCurve() {
		
		if (theCurve==null) {
			if (!curvePoints.isEmpty()) {
				//map the curve in from the curvepoints array if the curve is not populated yet.
				BigDecimal[] curvePointsAsBD = new BigDecimal[40];
				int arrayCntr = 0;
				for(String thisPoint: curvePoints) {
					curvePointsAsBD[arrayCntr] = new BigDecimal(thisPoint);
					arrayCntr += 1;
				}
				if (theCurve==null) {
					theCurve = new SpectralCurve();
				}
				theCurve.setCurve(curvePointsAsBD);
				theCurve.setCurvePointCnt(curvePoints.size());

			}
		}
		return theCurve;
	}
	
	@XmlElementWrapper(name="curvepoints")
    @XmlElement(name="curvepoint")
	public List<String> getCurvePoints() {
		return curvePoints;
	}
	

	public void setCurvePoints(List<String> curvepoints) {
		this.curvePoints = curvepoints;
	}
	
	
	@XmlElement(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="waitmilliseconds")
	public void setWaitMilliseconds(int waitMilliseconds) {
		this.waitMilliseconds = waitMilliseconds;
	}
	
	@XmlElement(name="errornumber")
	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}
	
	@XmlElement(name="errormessage")
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@XmlElement(name="responsemessage")
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	@XmlElement(name="spectralcurve")
	public void setSpectralCurve(SpectralCurve theCurve) {
		this.theCurve = theCurve;
	}
	
	
}
