package com.sherwin.shercolor.swdevicehandler.device;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;
import com.sherwin.shercolor.swdevicehandler.domain.Error;
public class AbstractTinterImplTest {
	class TestTinter extends AbstractTinterImpl{
		
	}
	TestTinter testtinter = new TestTinter();
	TinterMessage msg = new TinterMessage();
	@Before
	public void setUp() throws Exception {
		msg.setMessageName("TinterMessage");
		msg.setErrorNumber(1);
		msg.setErrorMessage("Test 1");
		List<Error> l = new ArrayList<Error>();
		Error e = new Error();
		e.setMessage("Error 1");
		e.setNum(1);
		l.add(e);
		
		msg.setErrorList(l);
		
		msg.setCommandRC(1);
		msg.setStatus(1);
		msg.setErrorSeverity((short)1);
		msg.setJavaMessage("Java Message 1");
		msg.setLastInitDate(20170101);
	}

	@Test
	public void testInitStatus(){
		testtinter.setLastInitMessage(msg);
		//change errormessage
		TinterMessage newmsg = new TinterMessage();
		newmsg.setErrorNumber(2);
		newmsg.setErrorMessage("Test 2");
		List<Error> l = new ArrayList<Error>();
		Error e = new Error();
		e.setMessage("Error 2");
		e.setNum(2);
		l.add(e);
		
		newmsg.setErrorList(l);
		
		newmsg.setCommandRC(2);
		newmsg.setStatus(2);
		newmsg.setErrorSeverity((short)2);
		newmsg.setJavaMessage("Java Message 2");
		newmsg.setLastInitDate(0);
		
		
		//prove that orig errors are retrieved from last Init message
		testtinter.InitStatus(newmsg);
		assertEquals("Test 1",newmsg.getErrorMessage());
		assertEquals(1 , newmsg.getErrorNumber());
	
		assertEquals(1, newmsg.getErrorList().get(0).getNum());
		assertEquals("Error 1", newmsg.getErrorList().get(0).getMessage());
	
	
		
		assertEquals(1, newmsg.getCommandRC());
		assertEquals(1, newmsg.getStatus());
		assertEquals(1, newmsg.getErrorSeverity());
		assertEquals("Java Message 1", newmsg.getJavaMessage());		
		assertEquals(20170101,newmsg.getLastInitDate());
		
		
	}

	@Test
	public void testRespond() {
		testtinter.Respond(msg);
	}

	@Test
	public void testUpdateConsoleMessage() {
		testtinter.UpdateConsole(msg);
	}

	@Test
	public void testUpdateConsoleString() {
		testtinter.UpdateConsole("tested");
	}

	
	

	@Test
	public void testGetToDeviceQueue() {
		assertNotNull(testtinter.getToDeviceQueue());
	}

	@Test
	public void testGetFromDeviceQueue() {
		assertNotNull(testtinter.getFromDeviceQueue());
	}

	@Test
	public void testInit() {
		
	}
	
	@Test 
	public void testGetSerialPorts(){
		testtinter.GetSerialPorts();
	}

}
