package com.sherwin.shercolor.swdevicehandler.device;


import java.util.Enumeration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.swdevicehandler.domain.Message;

import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;
import com.sherwin.shercolor.swdevicehandler.view.TheUserConsole;

import gnu.io.CommPortIdentifier;

//Place for all common functions, similar to gti.c

public abstract class AbstractTinterImpl extends AbstractDevice  implements AbstractTinterInterface{
	
	public Logger abstractlogger = LogManager.getLogger(getClass());
	private TinterMessage lastInitMessage;
	

	public void InitStatus(TinterMessage msg) {
		if(this.getLastInitMessage()!=null){
			msg.setErrorList(this.getLastInitMessage().getErrorList());
			msg.setErrorMessage(this.getLastInitMessage().getErrorMessage());
			msg.setErrorNumber(this.getLastInitMessage().getErrorNumber());
			msg.setCommandRC(this.getLastInitMessage().getCommandRC());
			msg.setStatus(this.getLastInitMessage().getStatus());
			msg.setErrorSeverity(this.getLastInitMessage().getErrorSeverity());
			msg.setJavaMessage(this.getLastInitMessage().getJavaMessage());
			msg.setLastInitDate(this.getLastInitMessage().getLastInitDate());
		}
	}
	
	public void Dispense(TinterMessage m){
		Respond(m);
		System.out.println("Dispense called");
	}
	public void Respond(TinterMessage msg){
		try {
			this.getFromDeviceQueue().put(msg);
			UpdateConsole(msg.getJavaMessage());
			UpdateConsole(msg.getErrorMessage());
			
		} catch (InterruptedException e) {
			abstractlogger.error("stacktrace",e);
		}
	}
	public void UpdateConsole(Message m){
		TinterMessage t = (TinterMessage)m;
		if(TheUserConsole.isConsoleStarted()){
		

			TheUserConsole.AppendTinterConsole(t);
			System.out.println(this.getClass().getSimpleName() +" Sending " + t.getCommand() + " to console" );
		}
		else{
			System.out.println("UpdateConsole function:" + this.getClass().getSimpleName() +" Not Sending " + t.getCommand() + " to console because you never started one." );
		}
	}

	public void UpdateConsole(String message) {
		if(TheUserConsole.isConsoleStarted()){
			TheUserConsole.AppendTinterConsole(message);
			System.out.println(this.getClass().getSimpleName() +" Sending " + message + " to console" );
		}
		else{
			System.out.println(this.getClass().getSimpleName() +" Not Sending " + message + " to console because you never started one." );
		}

	}
	

	@SuppressWarnings("rawtypes")
	public Enumeration GetSerialPorts(){
		   Enumeration portList = CommPortIdentifier.getPortIdentifiers();
	     
	        while(portList.hasMoreElements()){
	             CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
	               if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	                    System.out.println("Trying serial port:" + portId.getName());
	               }
	        }
	        return portList;
	}
	public TinterMessage getLastInitMessage() {
		if(lastInitMessage==null){
			lastInitMessage = TinterMessage.ReadFromDisk(null);
		}
		return lastInitMessage;
	}
	public void setLastInitMessage(TinterMessage lastInitMessage) {
		this.lastInitMessage = lastInitMessage;
		lastInitMessage.SaveToDisk(null);
		
	}
	
}
