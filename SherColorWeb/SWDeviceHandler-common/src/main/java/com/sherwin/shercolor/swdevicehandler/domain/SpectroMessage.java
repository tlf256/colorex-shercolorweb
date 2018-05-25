package com.sherwin.shercolor.swdevicehandler.domain;

public class SpectroMessage extends Message {
//	String id;
    String command;    
    String model;
    String responseMessage;
    long rc;                      //response
    Integer errorCode;
    String errorMessage;  
    SpectralCurve curve;
    SpectroConfiguration spectroConfig;
    
	
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	
	public long getRc() {
		return rc;
	}
	public void setRc(long rc) {
		this.rc = rc;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public SpectralCurve getCurve() {
		return curve;
	}
	public void setCurve(SpectralCurve curve) {
		this.curve = curve;
	}
    
	public SpectroConfiguration getSpectroConfig() {
		return spectroConfig;
	}
	public void setSpectroConfig(SpectroConfiguration spectroConfig) {
		this.spectroConfig = spectroConfig;
	}
	
    
    
}
