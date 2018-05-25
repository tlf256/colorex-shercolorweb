/**
 * 
 */
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
 * @author djm301
 *
 */
public class CorobTinterTest {

	CorobTinter tinter = new CorobTinter();
	TinterMessage msg = new TinterMessage();
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Setup Unit test");
		CorobTinter.DEBUG=true;
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
	public void testIterateSerialPorts(){
		tinter.IterateSerialPorts(msg);
	}
	@Test
	public void testConfig(){
		String json="{\"id\":\"dc62e861-5d47-4df3-9b21-5bef2c707888\",\"messageName\":\"TinterMessage\",\"command\":\"Config\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"Corob D600\",\"serial\":\"434344\",\"canisterLayout\":[{\"pump\":1,\"code\":\"W1\"},{\"pump\":2,\"code\":\"N1\"},{\"pump\":3,\"code\":\"R4\"},{\"pump\":4,\"code\":\"R3\"},{\"pump\":5,\"code\":\"G2\"},{\"pump\":6,\"code\":\"NA\"},{\"pump\":7,\"code\":\"B1\"},{\"pump\":8,\"code\":\"Y3\"},{\"pump\":9,\"code\":\"L1\"},{\"pump\":10,\"code\":\"R2\"},{\"pump\":11,\"code\":\"Y1\"},{\"pump\":12,\"code\":\"NA\"}],\"canisterMap\":null},\"shotList\":[{\"code\":\"R3\",\"shots\":32,\"uom\":128,\"position\":0},{\"code\":\"W1\",\"shots\":32,\"uom\":128,\"position\":0}],\"status\":0,\"javaMessage\":\"\",\"commandRC\":0,\"errorNumber\":0,\"errorSeverity\":0,\"errorMessage\":0,\"errorList\":null}";
		
		TinterMessage msg1 = new Gson().fromJson(json,TinterMessage.class);
		
		tinter.ProcessQueueItem(msg1);
		assertEquals("Corob D600",msg1.getConfiguration().getModel());
	}
	@Test
	public void testCalUpload(){
		String json = "{\"id\":\"d70cf8c2-1e3d-4f49-8e5f-f411a147f372\",\"messageName\":\"TinterMessage\",\"command\":\"CalUpload\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"Corob D600\",\"serial\":\"S12343\",\"port\":null,\"canisterLayout\":[{\"pump\":1,\"code\":\"W1\"},{\"pump\":2,\"code\":\"N1\"},{\"pump\":3,\"code\":\"R4\"},{\"pump\":4,\"code\":\"R3\"},{\"pump\":5,\"code\":\"G2\"},{\"pump\":6,\"code\":\"NA\"},{\"pump\":7,\"code\":\"B1\"},{\"pump\":8,\"code\":\"Y3\"},{\"pump\":9,\"code\":\"L1\"},{\"pump\":10,\"code\":\"R2\"},{\"pump\":11,\"code\":\"Y1\"},{\"pump\":12,\"code\":\"NA\"}],\"canisterMap\":null},\"shotList\":null,\"status\":0,\"javaMessage\":\"\",\"commandRC\":0,\"errorNumber\":0,\"errorSeverity\":0,\"errorMessage\":0,\"errorList\":null}";
		
		TinterMessage msg1 = new Gson().fromJson(json,TinterMessage.class);
		
		tinter.ProcessQueueItem(msg1);
		assertEquals("Corob D600",msg1.getConfiguration().getModel());
	}
	@Test
	public void testDetect() {
	
		msg.setCommand("Detect");
		tinter.Detect(msg);
		if(msg.getCommandRC()== 0){
			assertTrue(msg.getErrorNumber()==0 || msg.getErrorNumber()==-10500); // if tinter is  connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(-3084,msg.getErrorNumber()); // if tinter is notconnected and we have DEBUG=TRUE
		}
	}
	@Test
	public void testAgitate() {
	
		msg.setCommand("Agitate");
		tinter.Agitate(msg);
		if(msg.getCommandRC()== 0){
			assertEquals(0,msg.getErrorNumber()); // if tinter is  connected and we have DEBUG=TRUE
		}
		else{
			assertTrue(msg.getErrorNumber() == 4 // simulator 
					|| msg.getErrorNumber() == -1);			//tinter disconnected	 
		}
	}
	@Test
	public void testRecirculate() {
	
		msg.setCommand("Recirculate");
		tinter.Recirculate(msg);
		if(msg.getCommandRC()== 0){
			assertEquals(0,msg.getErrorNumber()); // if tinter is  connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(-1,msg.getErrorNumber()); // if tinter is notconnected and we have DEBUG=TRUE
		}
	}
	@Test
	public void testBellowsUp() {
	
		msg.setCommand("Bellows Up");
		tinter.BellowsUp(msg);
		if(msg.getCommandRC()== 0){
			assertEquals(0,msg.getErrorNumber()); // if tinter is  connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(1,msg.getErrorNumber()); // if tinter is notconnected and we have DEBUG=TRUE
		}
	}
	@Test
	public void testBellowsDown() {
	
		msg.setCommand("Bellows Down");
		tinter.BellowsDown(msg);
		if(msg.getCommandRC()== 0){
			assertEquals(0,msg.getErrorNumber()); // if tinter is  connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(1,msg.getErrorNumber()); // if tinter is notconnected and we have DEBUG=TRUE
		}
	}
	
	@Test
	public void testOpenNozzle() {
	
		msg.setCommand("OpenNozzle");
		tinter.OpenNozzle(msg);
		if(msg.getCommandRC()== 0){
			assertEquals(0,msg.getErrorNumber()); // if tinter is  connected and we have DEBUG=TRUE
		}
		else if(msg.getCommandRC()== 2){ // for our beloved simulator DEBUG = ?
			assertEquals(2,msg.getErrorNumber()); // command not recognized
			assertEquals("Command not recognized",msg.getErrorMessage());
		}else{
			assertEquals(-1,msg.getErrorNumber()); // if tinter is notconnected and we have DEBUG=TRUE
		}
	}
	
	@Test
	public void testCloseNozzle() {
	
		msg.setCommand("CloseNozzle");
		tinter.CloseNozzle(msg);
		if(msg.getCommandRC()== 0){
			assertEquals(0,msg.getErrorNumber()); // if tinter is  connected and we have DEBUG=TRUE
		}
		else if(msg.getCommandRC()== 2){ // for our beloved simulator
			assertEquals(2,msg.getErrorNumber()); // command not recognized
			assertEquals("Command not recognized",msg.getErrorMessage());
		}else{
			assertEquals(-1,msg.getErrorNumber()); // if tinter is notconnected and we have DEBUG=TRUE
		}
	}
	
	@Test
	public void testPurgeAll() {
		
		msg.setCommand("PurgeAll");
		msg.getShotList().clear();//clear out old message
		tinter.PurgeAll(msg);
		for(com.sherwin.shercolor.swdevicehandler.domain.Error e:msg.getErrorList()){
		System.out.println(e.getNum() + " " + e.getMessage());
		}
		if(msg.getCommandRC()== 0){
			assertEquals(-10500,msg.getErrorNumber()); // if tinter is  connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(-1,msg.getErrorNumber()); // if tinter is notconnected and we have DEBUG=TRUE
		}
	}
	@Test
	public void testDispenseAmt(){
	
		String amt="0.000";
		String expected="30.2671";//40625;
				//131.0/128.0*CorobTinter.CC_PER_OZ;
		System.out.println("Expected: " + expected);
		
		double delta=0.000001; // a double is never exact!
		amt = tinter.DispenseAmt(131,128);
		System.out.println("result: " + amt);
		assertEquals(expected,amt);
			
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
			assertEquals(-10500,msg.getErrorNumber()); // if tinter is not connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(-3084,msg.getErrorNumber()); // if tinter is connected and we have DEBUG=TRUE
		}
		
		
	}
	@Test
	public void testProcessQueueItemNothingToDispense(){
		msg = new TinterMessage();
		msg.setMessageName("TinterMessage");
		msg.setCommand("Dispense");
		tinter.ProcessQueueItem(msg);
		assertEquals(-115, msg.getErrorNumber());
	}
	@Test
	public void testJsonDispense(){
		String json = "{\"id\":\"dc62e861-5d47-4df3-9b21-5bef2c707888\",\"messageName\":\"TinterMessage\",\"command\":\"Dispense\",\"configuration\":{\"colorantSystem\":\"CCE\",\"model\":\"Corob\",\"serial\":\"434344\",\"canisterLayout\":[{\"pump\":1,\"code\":\"W1\"},{\"pump\":2,\"code\":\"N1\"},{\"pump\":3,\"code\":\"R4\"},{\"pump\":4,\"code\":\"R3\"},{\"pump\":5,\"code\":\"G2\"},{\"pump\":6,\"code\":\"NA\"},{\"pump\":7,\"code\":\"B1\"},{\"pump\":8,\"code\":\"Y3\"},{\"pump\":9,\"code\":\"L1\"},{\"pump\":10,\"code\":\"R2\"},{\"pump\":11,\"code\":\"Y1\"},{\"pump\":12,\"code\":\"NA\"}],\"canisterMap\":null},\"shotList\":[{\"code\":\"R3\",\"shots\":32,\"uom\":128,\"position\":0},{\"code\":\"W1\",\"shots\":32,\"uom\":128,\"position\":0}],\"status\":0,\"javaMessage\":\"\",\"commandRC\":0,\"errorNumber\":0,\"errorSeverity\":0,\"errorMessage\":0,\"errorList\":null}";
		TinterMessage msg1 = new Gson().fromJson(json,TinterMessage.class);
		tinter.ProcessQueueItem(msg1);
		if(msg1.getCommandRC()== 0){
			assertEquals(-10500,msg1.getErrorNumber()); // if tinter is not connected and we have DEBUG=TRUE
		}
		else{
			assertEquals(-3084,msg1.getErrorNumber()); // if tinter is connected and we have DEBUG=TRUE
		}
	}
	@Test
	public void testProcessQueueItemUnsupportedCommand(){
		msg.setCommand("Bad Command");
		tinter.ProcessQueueItem(msg);
		assertEquals(-1005, msg.getErrorNumber());
	}

}
