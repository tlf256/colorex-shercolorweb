package com.sherwin.shercolor.swdevicehandler.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;

public class ConfigurationTest {
	Configuration c = new Configuration();
	
	@Before
	public void setUp() throws Exception {
	TinterMessage msg = new TinterMessage();
	msg.setMessageName("TinterMessage");
	String json="{\"id\":\"dc62e861-5d47-4df3-9b21-5bef2c707888\",\"messageName\":\"TinterMessage\",\"command\":\"Config\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"Corob D600\",\"serial\":\"434344\",\"canisterLayout\":[{\"pump\":1,\"code\":\"W1\"},{\"pump\":2,\"code\":\"N1\"},{\"pump\":3,\"code\":\"R4\"},{\"pump\":4,\"code\":\"R3\"},{\"pump\":5,\"code\":\"G2\"},{\"pump\":6,\"code\":\"NA\"},{\"pump\":7,\"code\":\"B1\"},{\"pump\":8,\"code\":\"Y3\"},{\"pump\":9,\"code\":\"L1\"},{\"pump\":10,\"code\":\"R2\"},{\"pump\":11,\"code\":\"Y1\"},{\"pump\":12,\"code\":\"NA\"}],\"canisterMap\":null},\"shotList\":[{\"code\":\"R3\",\"shots\":32,\"uom\":128,\"position\":0},{\"code\":\"W1\",\"shots\":32,\"uom\":128,\"position\":0}],\"status\":0,\"javaMessage\":\"\",\"commandRC\":0,\"errorNumber\":0,\"errorSeverity\":0,\"errorMessage\":0,\"errorList\":null}";
	msg = new Gson().fromJson(json,TinterMessage.class);
	c = msg.getConfiguration();
	}
	
	@Test
	public void testSaveToDisk() {
		
		String error = c.SaveToDisk("test");
		assertEquals("Success",error);
	}
	@Test
	public void testReadFromDisk(){
		String error = c.SaveToDisk("test"); // junits execute in an arbitrary order so..in case the read executes first
		Configuration new_config = Configuration.ReadFromDisk("test");
		assertNotNull(new_config);
		assertEquals(c.getSerial(),new_config.getSerial());
	}

}
