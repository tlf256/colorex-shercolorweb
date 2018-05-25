package com.sherwin.shercolor.swdevicehandler.device;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;

public class SimTinterTest {
	SimTinter tinter = new SimTinter();
	TinterMessage msg = new TinterMessage();
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Setup Unit test");
		SimTinter.DEBUG=true;
		msg = new TinterMessage();
		msg.setMessageName("TinterMessage");
		String json="{\"id\":\"dc62e861-5d47-4df3-9b21-5bef2c707888\",\"messageName\":\"TinterMessage\",\"command\":\"Config\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"Corob D600\",\"serial\":\"434344\",\"canisterLayout\":[{\"pump\":1,\"code\":\"W1\"},{\"pump\":2,\"code\":\"N1\"},{\"pump\":3,\"code\":\"R4\"},{\"pump\":4,\"code\":\"R3\"},{\"pump\":5,\"code\":\"G2\"},{\"pump\":6,\"code\":\"NA\"},{\"pump\":7,\"code\":\"B1\"},{\"pump\":8,\"code\":\"Y3\"},{\"pump\":9,\"code\":\"L1\"},{\"pump\":10,\"code\":\"R2\"},{\"pump\":11,\"code\":\"Y1\"},{\"pump\":12,\"code\":\"NA\"}],\"canisterMap\":null},\"shotList\":[{\"code\":\"R3\",\"shots\":32,\"uom\":128,\"position\":0},{\"code\":\"W1\",\"shots\":32,\"uom\":128,\"position\":0}],\"status\":0,\"javaMessage\":\"\",\"commandRC\":0,\"errorNumber\":0,\"errorSeverity\":0,\"errorMessage\":0,\"errorList\":null}";
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
	
	@Test
	public void testProcessQueueItemPurgeAll(){
		msg = new TinterMessage();
		msg.setMessageName("TinterMessage");
		msg.setCommand("PurgeAll");
		tinter.ProcessQueueItem(msg);
		System.out.println("Back from PQI and message is " + msg.getErrorNumber() + " " + msg.getErrorMessage());
		assertEquals(0, msg.getErrorNumber());
	}
	

}
