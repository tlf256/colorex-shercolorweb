package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class CustWebTinterEventsDetailDaoTest {
	@Autowired
	CustWebTinterEventsDetailDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveInsert() {
		CustWebTinterEventsDetail teDetail = new CustWebTinterEventsDetail();
		
		teDetail.setGuid("123");
		teDetail.setType("PurgeUser");
		teDetail.setName("Joe Smith");
		teDetail.setQty(0F);
		
		boolean result = target.create(teDetail);
		
		System.out.println("TestPositiveInsert and result is " + result);
		
		assertTrue(result);
	}

	@Test
	public void testPositiveListForGuid() {
		CustWebTinterEventsDetail teDetail = new CustWebTinterEventsDetail();
		CustWebTinterEventsDetail teDetail2 = new CustWebTinterEventsDetail();
		
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
		
		List<CustWebTinterEventsDetail> recordList = target.listForGuid("TESTMASTERGUID");
		
		System.out.println("TestPositiveListForGuid and size is " + recordList.size());
		
		for(CustWebTinterEventsDetail item : recordList){
			System.out.println("--> " + item.getGuid() + " " + item.getType() + " " + item.getName() + " " + item.getQty());
		}
		
		assertTrue(recordList.size()>0);
	}
}
