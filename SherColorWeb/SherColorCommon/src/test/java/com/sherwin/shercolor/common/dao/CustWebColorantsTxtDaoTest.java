package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class CustWebColorantsTxtDaoTest {
	@Autowired
	CustWebColorantsTxtDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveCreate() {
		CustWebColorantsTxt colorantsTxt = new CustWebColorantsTxt();
		
		colorantsTxt.setCustomerId("TEST");
		colorantsTxt.setClrntSysId("CCE");
		colorantsTxt.setTinterModel("COROB D200");
		colorantsTxt.setTinterSerialNbr("1234");
		colorantsTxt.setClrntCode("B1");
		colorantsTxt.setPosition(5);
		colorantsTxt.setCurrentClrntAmount(55.2D);
		colorantsTxt.setFillAlarmLevel(32D);
		colorantsTxt.setMaxCanisterFill(232D);
		colorantsTxt.setFillStopLevel(1.2D);
		
		boolean result = target.create(colorantsTxt);
		assertTrue(result);
	}

	@Test
	public void testPositiveRead() {
		
		CustWebColorantsTxt colorantsTxt = target.read("DEFAULT", "CCE", "COROB D200", "DEFAULT", "L1", 7);
		
		System.out.println("Test Read of Default D200 L1 returned position " + colorantsTxt.getPosition());
		
		assertTrue(colorantsTxt.getPosition()==7);
		
	}

	@Test
	public void testPositiveUpdate() {
		CustWebColorantsTxt colorantsTxt = new CustWebColorantsTxt();
		
		colorantsTxt.setCustomerId("DEFAULT");
		colorantsTxt.setClrntSysId("CCE");
		colorantsTxt.setTinterModel("COROB D200");
		colorantsTxt.setTinterSerialNbr("DEFAULT");
		colorantsTxt.setClrntCode("B1");
		colorantsTxt.setPosition(11);
		colorantsTxt.setCurrentClrntAmount(55.2D);
		colorantsTxt.setFillAlarmLevel(32D);
		colorantsTxt.setMaxCanisterFill(232D);
		colorantsTxt.setFillStopLevel(1.2D);
		
		boolean result = target.update(colorantsTxt);
		
		CustWebColorantsTxt colorantsTxtUpdated = target.read("DEFAULT", "CCE", "COROB D200", "DEFAULT", "B1", 11);
		
		System.out.println("Test Read of Updated Default D200 B1 returned new CurrentClrntAmount " + colorantsTxtUpdated.getCurrentClrntAmount());
		
		assertTrue(result);
		
	}
	
	@Test
	public void testPositiveDelete() {
		CustWebColorantsTxt colorantsTxt = new CustWebColorantsTxt();
		
		colorantsTxt.setCustomerId("TEST");
		colorantsTxt.setClrntSysId("CCE");
		colorantsTxt.setTinterModel("COROB D200");
		colorantsTxt.setTinterSerialNbr("1234");
		colorantsTxt.setClrntCode("B1");
		colorantsTxt.setPosition(5);
		colorantsTxt.setCurrentClrntAmount(55.2D);
		colorantsTxt.setFillAlarmLevel(32D);
		colorantsTxt.setMaxCanisterFill(232D);
		colorantsTxt.setFillStopLevel(1.2D);
		
		boolean result = target.create(colorantsTxt);
		
		if(result) {
			result = false;
			result = target.delete(colorantsTxt);
		}
		
		assertTrue(result);
	}

	@Test
	public void testGetColorantListForD200() {
		List<CustWebColorantsTxt> colorantList = target.listForUniqueTinter("DEFAULT", "CCE", "COROB D200","DEFAULT");
		
		System.out.println("Colorant List for Default CCE Corob D200 is ...");
		if(colorantList.size()>0) {
			for(CustWebColorantsTxt record : colorantList){
				System.out.println("--> " + record.getClrntCode() + " " + record.getPosition() + " " + record.getMaxCanisterFill() + " " + record.getCurrentClrntAmount());
			}
		}
		assertTrue(colorantList.size()>0);
	}
	
	@Test
	public void testGetListOfCCETinters() {
		List<String> tinterList = target.listOfModelsForCustomerId("DEFAULT", "CCE");
		
		System.out.println("List of Default CCE Tinters is ...");
		if(tinterList.size()>0) {
			for(String item : tinterList){
				System.out.println("--> " + item);
			}
		}
		assertTrue(tinterList.size()>0);
	}
	
	@Test
	public void testGetColorantList(){
		List<String> list = target.listOfColorantSystemsByCustomerId("DEFAULT");
		if(list!=null){
			assertTrue(list.size()>0);
			for(String item : list){
				System.out.println("--> " + item);
			}
		}
		else{
			fail("no colorants returned");
		}
		
	}


}
