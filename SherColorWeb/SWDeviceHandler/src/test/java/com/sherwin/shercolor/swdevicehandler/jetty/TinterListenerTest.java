package com.sherwin.shercolor.swdevicehandler.jetty;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TinterListenerTest {

	private TinterListener tinterlistener = new TinterListener();
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testInitTinterQueueListener() {
		//fail("Not yet implemented");
	}

	@Test
	public void testInitConsoleQueueListener() {
		//fail("Not yet implemented");
	}

	@Test
	public void testOnWebSocketConnect() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testOnWebSocketClose() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testOnWebSocketError() {
		//fail("Not yet implemented");
	}

	@Test
	public void testOnWebSocketText() {
		//fail("Not yet implemented");
	}

	@Test
	public void testOnWebSocketBinary() {
		//fail("Not yet implemented");
	}
	@Test
	public void testGetSWDeviceHandlerVersion() {
		//fail("Not yet implemented");
		String returnedVersion = tinterlistener.getSWDeviceHandlerVersion();
		System.out.println(returnedVersion);
	}

}
