package com.sherwin.shercolor.swdevicehandler.device;






import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.sherwin.shercolor.swdevicehandler.domain.Colorant;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;



/**
 * tests to prove that driver errors correctly when config not done.
 *
 */

public class CorobTinterwithoutConfigTest {
	CorobTinter tinter = new CorobTinter();
	TinterMessage msg = new TinterMessage();
	
	@Before
	public void setConfig(){
		tinter.setConfiguration(null);
	}
	
	@Test
	public void testPurgewithoutConfig(){
		//msg.getConfiguration().setCanisterLayout(null);
		System.out.println("mytestPurgeWithoutConfig");
		msg.setCommand("PurgeAll");
		msg.getShotList().clear();//clear out old message
		tinter.PurgeAll(msg);
		if(msg.getCommandRC()== 0){
			assertEquals(-116,msg.getErrorNumber()); // if tinter is  connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(-116,msg.getErrorNumber()); // if tinter is notconnected and we have DEBUG=TRUE
		}
	}
	@Test
	public void testDispense(){
		msg.getShotList().clear();
		msg.setCommand("Dispense");
		Colorant c = new Colorant();
		c.setCode("W1");
		c.setPosition(1);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		
		c = new Colorant();
		c.setCode("N1");
		c.setPosition(2);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		
		c = new Colorant();
		c.setCode("R4");
		c.setPosition(3);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		
		c = new Colorant();
		c.setCode("R3");
		c.setPosition(4);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		
		c = new Colorant();
		c.setCode("G2");
		c.setPosition(5);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		
		c = new Colorant();
		c.setCode("B1");
		c.setPosition(7);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		
		c = new Colorant();
		c.setCode("Y3");
		c.setPosition(8);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		
		c = new Colorant();
		c.setCode("L1");
		c.setPosition(9);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		/*
		c = new Colorant();
		c.setCode("R2");
		c.setPosition(10);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		
		c = new Colorant();
		c.setCode("Y1");
		c.setPosition(11);
		c.setShots(13);
		c.setUom(128);
		msg.getShotList().add(c);
		*/
		tinter.Dispense(msg);
		if(msg.getCommandRC()== 0){
			assertEquals(-115,msg.getErrorNumber()); // if tinter is not connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(-115,msg.getErrorNumber()); // if tinter is connected and we have DEBUG=TRUE
		}
		
		
	}
}