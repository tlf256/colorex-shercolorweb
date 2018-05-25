package com.sherwin.shercolor.common.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.util.domain.SwMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class TinterServiceTest {

	@Autowired
	TinterService target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}


	@Test
	public void testGetColorantMap(){
		
		HashMap<String,CustWebColorantsTxt> colorantMap = target.getCanisterMap("DEFAULT", "CCE", "COROB D200", "DEFAULT");
		
		assertNotNull(colorantMap);
		
	}
	
	@Test
	public void testHasNozzleTrue(){
		boolean result = target.hasAutoNozzleCover("DEFAULT", "CCE", "COROB D600", "DEFAULT");
		
		System.out.println("Test Nozzle True is " + result);
		
		assertTrue(result);
	}
	
	@Test
	public void testHasNozzleFalse(){
		boolean result = target.hasAutoNozzleCover("DEFAULT", "CCE", "COROB D200", "DEFAULT");
		
		System.out.println("Test Nozzle False is " + false);
		
		assertFalse(result);
		
		
	}
	
	@Test
	public void testGetLastPurgeInfo(){
		
		CustWebTinterEvents result = target.getLastPurgeDateAndUser("CCF", "CCE", "COROB D600", "S17GSILL2");
		

		if (result!=null) System.out.println("Last Purged on " + result.getDateTime() + " by " + result.getEventDetails());
		
		assertNotNull(result);
	}
	@Test
	public void testUpdateColorantLevelsGoodPass(){
		List<CustWebColorantsTxt> recordList = new ArrayList<CustWebColorantsTxt>();
		
		CustWebColorantsTxt record1 = new CustWebColorantsTxt();
		record1.setCustomerId("DEFAULT");
		record1.setClrntSysId("CCE");
		record1.setTinterModel("COROB D600");
		record1.setTinterSerialNbr("DEFAULT");
		record1.setClrntCode("B1");
		record1.setPosition(7);
		record1.setMaxCanisterFill(512D);
		record1.setFillAlarmLevel(64D);
		record1.setFillStopLevel(1.2D);
		record1.setCurrentClrntAmount(100D);

		CustWebColorantsTxt record2 = new CustWebColorantsTxt();
		record2.setCustomerId("DEFAULT");
		record2.setClrntSysId("CCE");
		record2.setTinterModel("COROB D600");
		record2.setTinterSerialNbr("DEFAULT");
		record2.setClrntCode("N1");
		record2.setPosition(2);
		record2.setMaxCanisterFill(512D);
		record2.setFillAlarmLevel(64D);
		record2.setFillStopLevel(1.2D);
		record2.setCurrentClrntAmount(100D);
		
		recordList.add(record1);
		recordList.add(record2);
		
		List<SwMessage> errorList = target.updateColorantLevels(recordList);
		
		System.out.println("Update Colorant Level returned " + errorList.size() + " errors");
		
		assertTrue(errorList.size()==0);
		
		
}
	
	
	@Test
	public void testlistOfColorantSystemsByCustomerId(){
		List<String> list = target.listOfColorantSystemsByCustomerId("DEFAULT");
		assertTrue(list.contains("CCE"));
		
	}
	@Test
	public void testlistOfModelsForCustomerId(){
		List<String> list = target.listOfModelsForCustomerId("DEFAULT","CCE");
		assertTrue(list.contains("COROB D600"));
		
	}
	@Test
	public void testConditionalSave(){
		int result = 0;
		CustWebColorantsTxt record1 = new CustWebColorantsTxt();
		record1.setCustomerId("TEST");
		record1.setClrntSysId("CCE");
		record1.setTinterModel("COROB TEST");
		record1.setTinterSerialNbr("TEST");
		record1.setClrntCode("B1");
		record1.setPosition(7);
		record1.setMaxCanisterFill(512D);
		record1.setFillAlarmLevel(64D);
		record1.setFillStopLevel(1.2D);
		record1.setCurrentClrntAmount(100D);
		List<CustWebColorantsTxt> colorantList  = new ArrayList<CustWebColorantsTxt>();
		colorantList.add(record1);
		target.deleteColorantTxtItem(record1); // delete so we start fresh.
		result = target.conditionalSaveColorantsTxt( colorantList); //save it once to db;  result is either 0
		assertEquals(0,result);
		result = target.conditionalSaveColorantsTxt( colorantList); // save it again; result=1 means record was already there so do nothing.
		assertEquals(1,result);
	}
	
}
