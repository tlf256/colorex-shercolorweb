package com.sherwin.shercolor.common.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.util.domain.SwMessage;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class TranHistoryServiceTest {

	@Autowired
	TranHistoryService target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveCreate() {
		CustWebTran webTran = new CustWebTran();
		
		webTran.setCustomerId("shercolortest");
		webTran.setJobField01("Main Campus");
		webTran.setJobField02("L");
		webTran.setJobField03("2");
		webTran.setJobField04("2531");
		webTran.setJobField05("Wall");
		webTran.setColorComp("SHERWIN-WILLAIAMS");
		webTran.setColorId("6611");
		webTran.setColorName("JOVIAL");
		
		SwMessage result = target.saveNewTranHistory(webTran);
		assertTrue(result==null);
		
	}

	@Test
	public void testPositiveRead() {
		CustWebTran webTran = new CustWebTran();
		
		webTran.setCustomerId("shercolortest");
		webTran.setJobField01("Main Campus");
		webTran.setJobField02("L");
		webTran.setJobField03("2");
		webTran.setJobField04("2531");
		webTran.setJobField05("Wall");
		webTran.setColorComp("SHERWIN-WILLAIAMS");
		webTran.setColorId("6611");
		webTran.setColorName("JOVIAL");
		
		SwMessage result = target.saveNewTranHistory(webTran);
		
		CustWebTran readit = target.readTranHistory(webTran.getCustomerId(),webTran.getControlNbr(),webTran.getLineNbr());
		System.out.println("in positive read test and read of controlNbr " + webTran.getControlNbr() + " returned controlNbr " + readit.getControlNbr());
		
		assertTrue(readit.getControlNbr()==webTran.getControlNbr());
		
	}

	@Test
	public void testFailedRead() {
		
		CustWebTran result = target.readTranHistory("CCF", 999999, 99);
		
		System.out.println("in failed read test and read of controlNbr 999999 returned controlNbr ");
		
		assertTrue(result==null);
		
	}

	@Test
	public void testPositiveGetCustomerOrder() {
		CustWebTran webTran = new CustWebTran();
		List<CustWebTran> readit = null;

		webTran.setCustomerId("shercolortest");
		webTran.setJobField01("Main Campus");
		webTran.setJobField02("L");
		webTran.setJobField03("2");
		webTran.setJobField04("2531");
		webTran.setJobField05("Wall");
		webTran.setColorComp("SHERWIN-WILLAIAMS");
		webTran.setColorId("6611");
		webTran.setColorName("JOVIAL");
		
		SwMessage result = target.saveNewTranHistory(webTran);
		
		readit = target.getCustomerOrder(1);

		System.out.println("Get Customer Order by Control Number - positive read test and read of controlNbr - number of records found is -> " + readit.size() );
		
		assertTrue(readit.get(0).getControlNbr()==webTran.getControlNbr());
		
	}

}
