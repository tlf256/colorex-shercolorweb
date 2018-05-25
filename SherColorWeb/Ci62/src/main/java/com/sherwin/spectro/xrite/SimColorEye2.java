package com.sherwin.spectro.xrite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.swdevicehandler.device.AbstractSpectroImpl;
import com.sherwin.shercolor.swdevicehandler.device.AbstractSpectroInterface;
import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.SpectralCurve;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroMessage;



public class SimColorEye2 extends AbstractSpectroImpl implements AbstractSpectroInterface{
	static Logger logger = LogManager.getLogger(SimColorEye2.class);


	public SimColorEye2(){
		init();
	
	}

	
	

	//default processqueueitem meant to be overridden
	@Override
	protected	
	void ProcessQueueItem(Message m){
		SpectroMessage msg = (SpectroMessage)m;
		if(m != null){
			System.out.println(this.getClass().getSimpleName() + " Sending " + msg.getCommand() + " to coloreye" );
			try {
				Thread.sleep(5000);
				msg.setErrorMessage("Simulated " + msg.getCommand() + " executed");
				this.getFromDeviceQueue().put(msg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public boolean Calibrate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SpectralCurve Measure() {
		// TODO Auto-generated method stub
		return null;
	}




	
}
