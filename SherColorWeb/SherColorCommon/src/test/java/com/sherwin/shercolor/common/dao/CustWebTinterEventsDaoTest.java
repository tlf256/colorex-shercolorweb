package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class CustWebTinterEventsDaoTest {

	@Autowired
	CustWebTinterEventsDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveInsert() {
		CustWebTinterEvents tintEvent = new CustWebTinterEvents();
		
		tintEvent.setCustomerId("CCF");
		tintEvent.setClrntSysId("CCE");
		tintEvent.setTinterModel("COROB D600");
		tintEvent.setTinterSerialNbr("S17GSILL2");
		tintEvent.setFunction("Purge");
		tintEvent.setDateTime(new Date());
		tintEvent.setErrorStatus("0");
		
		String result = target.create(tintEvent);
		
		System.out.println("TestPositiveInsert and result is " + result);
		
		assertNotNull(result);
	}

	@Test
	public void testPositiveListForTinterAndFunction() {
		CustWebTinterEvents teDetail = new CustWebTinterEvents();
		CustWebTinterEvents teDetail2 = new CustWebTinterEvents();
		
//		teDetail.setGuid("123");
//		teDetail.setType("Purge User");
//		teDetail.setName("Joe Smith");
//		teDetail.setQty(0F);
//		boolean result = target.create(teDetail);
//		System.out.println("creat 1 guid is " +result);
//		
//		teDetail2.setGuid("123");
//		teDetail2.setType("Purge Date");
//		teDetail2.setName("9/22/2017");
//		teDetail2.setQty(0F);
//		boolean result2 = target.create(teDetail2);
//		System.out.println("creat 2 guid is " +result2);
		
		List<CustWebTinterEvents> recordList = target.listForTinterAndFunction("CCF", "CCE", "COROB D600", "S17GSILL2", "PurgeAll", false);
		
		System.out.println("TestPositiveListForTinterAndFunction and size is " + recordList.size());
		
		for(CustWebTinterEvents item : recordList){
			System.out.println("--> " + item.getGuid() + " " + item.getCustomerId() + " " + item.getDateTime() + " " + item.getFunction());
		}
		
		assertTrue(recordList.size()>0);
	}
	
	@Test
	public void testPositiveGetLastPurge() {
		CustWebTinterEvents teTintEvents = new CustWebTinterEvents();
		CustWebTinterEvents teTintEvents2 = new CustWebTinterEvents();
		
//		teDetail.setGuid("123");
//		teDetail.setType("Purge User");
//		teDetail.setName("Joe Smith");
//		teDetail.setQty(0F);
//		boolean result = target.create(teDetail);
//		System.out.println("creat 1 guid is " +result);
//		
//		teDetail2.setGuid("123");
//		teDetail2.setType("Purge Date");
//		teDetail2.setName("9/22/2017");
//		teDetail2.setQty(0F);
//		boolean result2 = target.create(teDetail2);
//		System.out.println("creat 2 guid is " +result2);
		
		CustWebTinterEvents record = target.findLastPurge("CCF", "CCE", "COROB D600", "S17GSILL2");
		
		System.out.println("TestPositiveGetLastPurge and record is " + record.getFunction() + " " + record.getDateTime());
		
		assertNotNull(record);
	}
}
