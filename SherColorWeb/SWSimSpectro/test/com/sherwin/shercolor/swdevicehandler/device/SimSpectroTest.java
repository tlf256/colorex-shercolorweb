package com.sherwin.shercolor.swdevicehandler.device;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroConfiguration;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroMessage;
import com.sherwin.shercolor.swdevicehandler.device.SimSpectro;

public class SimSpectroTest {
	SimSpectro spectro = new SimSpectro();
	SpectroMessage msg = new SpectroMessage();
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Setup Unit test");
		spectro.setSpectroResponseXmlPath("/swdevicehandler");
		SimSpectro.DEBUG=true;
		msg = new SpectroMessage();
		msg.setMessageName("SpectroMessage");
		String json="{\"id\":\"dc62e861-5d47-4df3-9b21-5bef2c707888\",\"messageName\":\"SpectroMessage\",\"command\":\"Config\", \"curve\":{\"curve\":[],\"curvePointCnt\":0},\"spectroConfig\":{\"model\":\"SWSimSpectro\",\"serial\":\"\",\"port\":\"USB\"}}";
		msg = new Gson().fromJson(json,SpectroMessage.class);
		SpectroConfiguration new_config = msg.getSpectroConfig();
		if(new_config !=null){
			try{	
				spectro.setConfiguration(new SpectroConfiguration(new_config));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testProcessQueueItemBadCommand(){
		msg = new SpectroMessage();
		msg.setMessageName("SpectroMessage");
		msg.setCommand("BadCommand");
		spectro.ProcessQueueItem(msg);
		System.out.println("Back from PQI and error code is " + msg.getErrorCode() + " error msg is " + msg.getErrorMessage() + " response is " + msg.getResponseMessage());
		assertEquals("-1", msg.getErrorCode().toString());
	}
	
	@Test
	public void testProcessQueueItemGetSerialNumber(){
		msg = new SpectroMessage();
		msg.setMessageName("SpectroMessage");
		msg.setCommand("GetSerialNumber");
		spectro.ProcessQueueItem(msg);
		System.out.println("Back from PQI and error code is " + msg.getErrorCode() + " error msg is " + msg.getErrorMessage() + " response is " + msg.getResponseMessage());
		assertEquals("123456", msg.getResponseMessage());
	}
	
	@Test
	public void testProcessQueueItemReadConfig(){
		System.out.println("Starting ReadConfig test");
		msg = new SpectroMessage();
		msg.setMessageName("SpectroMessage");
		msg.setCommand("ReadConfig");
		spectro.ProcessQueueItem(msg);
		System.out.println("Back from PQI and error code is " + msg.getErrorCode() + " error msg is " + msg.getErrorMessage() + " response is " + msg.getResponseMessage());
		//assertEquals("123456", msg.getResponseMessage());
	}
	
	@Test
	public void testProcessQueueItemGetSWCurve(){
		msg = new SpectroMessage();
		msg.setMessageName("SpectroMessage");
		msg.setCommand("SWMeasure");
		spectro.ProcessQueueItem(msg);
		System.out.println("Back from PQI and error code is " + msg.getErrorCode() + " error msg is " + msg.getErrorMessage() + " response is " + msg.getResponseMessage());
		System.out.println("Curve count is " + msg.getCurve().getCurvePointCnt());
		System.out.println("Curve point 5 is " + msg.getCurve().getCurve()[5]);
		assertEquals(6, msg.getCurve().getCurve()[5].intValue());
	}

}
