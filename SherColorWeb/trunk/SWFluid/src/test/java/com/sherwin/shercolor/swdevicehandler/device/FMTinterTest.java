package com.sherwin.shercolor.swdevicehandler.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.ConnectException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;

//to get most of these working, either copy the FMSocketServer directory into your current workspace/FMTinter, or 
//because this will try to stop and start the socket server,  you will need to set your working directory to, "Other", and choose the directory of your 
		// tinterconfig.conf && FMSocketServer directory

public class FMTinterTest {
	FMTinter tinter = new FMTinter(false,"test");
	TinterMessage msg = new TinterMessage();
	boolean skipTests = false;

	@Before
	public void setUp() throws Exception {
		System.out.println("Setup Unit test");
		//CorobTinter.DEBUG=true;
		msg = new TinterMessage();
		msg.setMessageName("TinterMessage");
		String json="{\"id\":\"dc62e861-5d47-4df3-9b21-5bef2c707888\",\"messageName\":\"TinterMessage\",\"command\":\"Config\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"FM 8000DE\",\"serial\":\"TESTFM\",\"canisterLayout\":[{\"pump\":1,\"code\":\"W1\"},{\"pump\":2,\"code\":\"N1\"},{\"pump\":3,\"code\":\"R4\"},{\"pump\":4,\"code\":\"R3\"},{\"pump\":5,\"code\":\"G2\"},{\"pump\":6,\"code\":\"NA\"},{\"pump\":7,\"code\":\"B1\"},{\"pump\":8,\"code\":\"Y3\"},{\"pump\":9,\"code\":\"L1\"},{\"pump\":10,\"code\":\"R2\"},{\"pump\":11,\"code\":\"Y1\"},{\"pump\":12,\"code\":\"NA\"}]},\"shotList\":[{\"code\":\"R3\",\"shots\":32,\"uom\":128,\"position\":0},{\"code\":\"L1\",\"shots\":128,\"uom\":128,\"position\":0}],\"status\":0,\"javaMessage\":\"\",\"commandRC\":0,\"errorNumber\":0,\"errorSeverity\":0,\"errorMessage\":0,\"errorList\":null}";
		msg = new Gson().fromJson(json,TinterMessage.class);
		Configuration new_config = msg.getConfiguration();
		if(new_config !=null  && new_config.getCanisterLayout() != null){
			try{

				tinter.setConfiguration(new Configuration(new_config));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Ignore @Test
	public void testOpenNozzle() {
		
			msg.setCommand("OpenNozzle");
			tinter.OpenNozzle(msg);
			System.out.println(msg.getErrorNumber() + "-" + msg.getErrorMessage());
			
			assertTrue(msg.getErrorNumber() <= 0);

		

	}
	@Ignore @Test
	public void testCloseNozzle() {
		
			msg.setCommand("CloseNozzle");
			tinter.CloseNozzle(msg);
			System.out.println(msg.getErrorNumber() + "-" + msg.getErrorMessage());
			
			assertTrue(msg.getErrorNumber() <= 0);
		


	}
	@Ignore @Test
	public void testAgitate() {
		
			msg.setCommand("Agitate");
			tinter.Agitate(msg);
			System.out.println(msg.getErrorNumber() + "-" + msg.getErrorMessage());
			
			assertTrue(msg.getErrorNumber() <= 0);
		


	}
	@Test
	public void testDispense() {
		
			msg.setCommand("Dispense");
			tinter.Dispense(msg);
			System.out.println(msg.getErrorNumber() + "-" + msg.getErrorMessage());
			
			assertTrue(msg.getErrorNumber() <= 0);
		


	}
	
	@Test
	public void testStopSocketServer() {
		
			
			tinter.StopFMSocketServer();
		

	}
	@Test
	public void testInit() {
		//this will try to stop and start the socket server, so you need to set your working directory to, "Other", and choose the directory of your 
		// tinterconfig.conf && FMSocketServer directory
		
			msg.setCommand("Init");
			tinter.Detect(msg);
			System.out.println(msg.getErrorNumber() + "-" + msg.getErrorMessage());
			
			assertTrue(msg.getErrorNumber()<= 0);
		

	}
	@Test
	public void testDetectAtStartUp() {
		//this will try to stop and start the socket server, so you need to set your working directory to, "Other", and choose the directory of your 
		// tinterconfig.conf && FMSocketServer directory
		
			msg.setCommand("Init");
			tinter.DetectAtStartup();
			
		

	}
	@Test
	public void testInitStatus() {
		
			msg.setCommand("InitStatus");
			tinter.InitStatus(msg);
			System.out.println(msg.getErrorNumber() + "-" + msg.getErrorMessage() + "Last Init Date" + msg.getLastInitDate());
			
			assertTrue(msg.getErrorNumber() <= 0);
		

	}
	@Ignore @Test //none of our supported machines recirculate 
	public void testRecirc() {
		
			msg.setCommand("Recirculate");
			tinter.Recirculate(msg);
			System.out.println(msg.getErrorNumber() + "-" + msg.getErrorMessage());
			
			assertTrue(msg.getErrorNumber() <= 0);
		

	}
}
