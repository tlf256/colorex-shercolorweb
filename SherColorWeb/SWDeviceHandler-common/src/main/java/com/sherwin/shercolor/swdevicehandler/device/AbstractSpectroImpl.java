package com.sherwin.shercolor.swdevicehandler.device;

//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;

import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.SpectralCurve;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroMessage;
import com.sherwin.shercolor.swdevicehandler.view.TheUserConsole;

public abstract class AbstractSpectroImpl extends AbstractDevice implements AbstractSpectroInterface {
	//private final static BlockingQueue<String> spectroResponseQueue= new LinkedBlockingQueue<>();
	private String spectroDllName;
	private String spectroDllPath;
	protected Integer errorCode;
	protected String errorMsg;
	protected SpectralCurve measCurve;
	
	
	
//	public static BlockingQueue<String> getSpectroResponseQueue() {
//		return spectroResponseQueue;
//	}
	public String getSpectroDllName() {
		return spectroDllName;
	}
	public void setSpectroDllName(String spectroDllName) {
		this.spectroDllName = spectroDllName;
	}
	public String getSpectroDllPath() {
		return spectroDllPath;
	}
	public void setSpectroDllPath(String spectroDllPath) {
		this.spectroDllPath = spectroDllPath;
	}
	public Integer getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	
	public SpectralCurve getMeasuredCurve() {
		return measCurve;
	}
	
	@Override
	public void UpdateConsole(Message m){
		if(TheUserConsole.isConsoleStarted()){
		SpectroMessage msg = (SpectroMessage)m;
		TheUserConsole.AppendSpectroConsole(msg);
				System.out.println(this.getClass().getSimpleName() +" Sending " + msg.getCommand() + " to console" );
		}
	}
//	@Override
//	public void UpdateConsole(String message) {
//		if(TheUserConsole.isConsoleStarted()){
//		TheUserConsole.AppendSpectroConsole(message);
//		}
//		
//	}
	
}
