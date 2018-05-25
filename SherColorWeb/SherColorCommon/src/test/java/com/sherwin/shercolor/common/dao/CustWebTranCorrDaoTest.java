package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.CustWebTranCorr;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class CustWebTranCorrDaoTest {
	@Autowired
	CustWebTranCorrDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveCreate() {
		CustWebTranCorr webTranCorr = new CustWebTranCorr();
		
		webTranCorr.setCustomerId("shercolortest");
		webTranCorr.setControlNbr(9999);
		webTranCorr.setLineNbr(99);
		webTranCorr.setCycle(9);
		webTranCorr.setUnitNbr(9);
		webTranCorr.setStep(9);
		webTranCorr.setReason("TEST");
		
		boolean result = target.create(webTranCorr);
		assertTrue(result);
		
	}

	@Test
	public void testPositiveRead(){
		CustWebTranCorr webTranCorr = new CustWebTranCorr();
		
		webTranCorr.setCustomerId("shercolortest");
		webTranCorr.setControlNbr(9999);
		webTranCorr.setLineNbr(99);
		webTranCorr.setCycle(9);
		webTranCorr.setUnitNbr(9);
		webTranCorr.setStep(9);
		webTranCorr.setReason("TEST");
		
		boolean result = target.create(webTranCorr);

		CustWebTranCorr readit = target.read(webTranCorr.getCustomerId(),webTranCorr.getControlNbr(),webTranCorr.getLineNbr(),webTranCorr.getCycle(),webTranCorr.getUnitNbr(),webTranCorr.getStep());
		System.out.println("in positive read test and read of controlNbr " + webTranCorr.getControlNbr() + " returned controlNbr " + readit.getControlNbr() + " step is " + readit.getStep());
		
		assertTrue(readit.getControlNbr()==webTranCorr.getControlNbr());
	}
	
	@Test
	public void testPositiveUpdate(){
		CustWebTranCorr webTranCorr = new CustWebTranCorr();
		
		webTranCorr.setCustomerId("shercolortest");
		webTranCorr.setControlNbr(9999);
		webTranCorr.setLineNbr(99);
		webTranCorr.setCycle(9);
		webTranCorr.setUnitNbr(9);
		webTranCorr.setStep(9);
		webTranCorr.setReason("TEST");
		
		boolean result = target.create(webTranCorr);
		
		CustWebTranCorr readit = target.read(webTranCorr.getCustomerId(),webTranCorr.getControlNbr(),webTranCorr.getLineNbr(),webTranCorr.getCycle(),webTranCorr.getUnitNbr(),webTranCorr.getStep());
		
		readit.setReason("TESTUPDATE");
		
		boolean updatetest = target.update(readit);
		
		System.out.println("in positive update test and update controlNbr is " + webTranCorr.getControlNbr() + " and update result is " + updatetest + " step is " + readit.getStep());
		
		assertTrue(readit.getControlNbr()==webTranCorr.getControlNbr() && updatetest);

	}
	
	@Test
	public void testPositiveListForCustomerControlLine(){
		CustWebTranCorr webTranCorr = new CustWebTranCorr();
		
		webTranCorr.setCustomerId("shercolortest");
		webTranCorr.setControlNbr(9999);
		webTranCorr.setLineNbr(99);
		webTranCorr.setCycle(9);
		webTranCorr.setUnitNbr(9);
		webTranCorr.setStep(9);
		webTranCorr.setReason("TEST");
		
		boolean result = target.create(webTranCorr);
		
		List<CustWebTranCorr> readList = target.listForCustomerOrderLine(webTranCorr.getCustomerId(),webTranCorr.getControlNbr(),webTranCorr.getLineNbr());
		System.out.println("in positive List for CustomerId/ContrlNbr/LineNbr " +webTranCorr.getCustomerId() + " and found " + readList.size() + " records.");
		
		assertTrue(readList.size()>0);

	}
}
