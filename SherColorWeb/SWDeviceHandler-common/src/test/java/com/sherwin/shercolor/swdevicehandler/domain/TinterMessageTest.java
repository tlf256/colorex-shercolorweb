package com.sherwin.shercolor.swdevicehandler.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;

public class TinterMessageTest {
	TinterMessage msg = new TinterMessage();
	
	@Before
	public void setUp() throws Exception {
	
	msg.setMessageName("TinterMessage");
	String json ="{\"command\":\"Detect\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"COROB D200\",\"serial\":\"TESTME\",\"port\":\"COM3\",\"canisterLayout\":[{\"pump\":1,\"code\":\"R4\"},{\"pump\":2,\"code\":\"R3\"},{\"pump\":3,\"code\":\"Y1\"},{\"pump\":4,\"code\":\"NA\"},{\"pump\":5,\"code\":\"N1\"},{\"pump\":6,\"code\":\"G2\"},{\"pump\":7,\"code\":\"L1\"},{\"pump\":8,\"code\":\"R2\"},{\"pump\":9,\"code\":\"W1\"},{\"pump\":10,\"code\":\"Y3\"},{\"pump\":11,\"code\":\"B1\"},{\"pump\":12,\"code\":\"NA\"}],\"canisterMap\":{\"map\":{\"1\":\"R4\",\"2\":\"R3\",\"3\":\"Y1\",\"4\":\"NA\",\"5\":\"N1\",\"6\":\"G2\",\"7\":\"L1\",\"8\":\"R2\",\"9\":\"W1\",\"10\":\"Y3\",\"11\":\"B1\",\"12\":\"NA\"},\"codeMap\":{\"R2\":8,\"R3\":2,\"R4\":1,\"NA\":12,\"N1\":5,\"L1\":7,\"Y1\":3,\"W1\":9,\"Y3\":10,\"G2\":6,\"B1\":11}}},\"status\":0,\"javaMessage\":\"\",\"commandRC\":4,\"errorNumber\":4,\"errorSeverity\":0,\"errorMessage\":\"Command Refused. Tinter Error\",\"lastInitDate\":20171011,\"id\":\"dcfc7428-de48-4939-84d4-b1180a3299de\",\"messageName\":\"TinterMessage\"}";
	msg = new Gson().fromJson(json,TinterMessage.class);

	}
	
	@Test
	public void testSaveToDisk() {
		
		String error = msg.SaveToDisk("test");
		assertEquals("Success",error);
	}
	@Test
	public void testReadFromDisk(){
		String error = msg.SaveToDisk("test"); // junits execute in an arbitrary order so..in case the read executes first
		TinterMessage new_tm = TinterMessage.ReadFromDisk("test");
		assertEquals("Success",error);
		assertNotNull(new_tm);
		assertEquals(new_tm.getLastInitDate(), msg.getLastInitDate());
	}

}
