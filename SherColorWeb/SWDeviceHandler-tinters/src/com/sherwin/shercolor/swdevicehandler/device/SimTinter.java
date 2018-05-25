package com.sherwin.shercolor.swdevicehandler.device;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.swdevicehandler.domain.Message;

import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;



public class SimTinter extends AbstractTinterImpl implements TinterInterface {
	static Logger logger = LogManager.getLogger(SimTinter.class);


	public SimTinter(){
		//init();  for now handling in TinterListener... make it easier to unit test too!
	
	}

	
	
	@Override
	public void Detect(TinterMessage msg){

	}
	
	@Override
	public void Dispense(TinterMessage msg){
		
	}

	//default processqueueitem meant to be overridden
		@Override
		protected	
		void ProcessQueueItem(Message m){
			TinterMessage msg = (TinterMessage)m;
			if(m != null){
				System.out.println(this.getClass().getSimpleName() + " Sending " + msg.getCommand() + " to tinter" );
				try {
					Thread.sleep(5000);
					msg.setErrorMessage("Simulated " + msg.getCommand() + " executed");
					this.getFromDeviceQueue().put(msg);
					UpdateConsole(msg.getErrorMessage());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}



		@Override
		public void PurgeAll(TinterMessage arg0) {
			// TODO Auto-generated method stub
			
		}
	
}
