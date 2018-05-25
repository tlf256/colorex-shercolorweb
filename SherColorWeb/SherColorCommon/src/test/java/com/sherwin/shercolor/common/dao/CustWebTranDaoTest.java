package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebTran;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class CustWebTranDaoTest {

	@Autowired
	CustWebTranDao target;

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
		
		double[] projCurve = new double[40];
		Arrays.fill(projCurve, 0D);
		webTran.setProjCurve(projCurve);

		
		boolean result = target.create(webTran);
		assertTrue(result);
		
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
		
		boolean result = target.create(webTran);
		
		CustWebTran readit = target.read(webTran.getCustomerId(),webTran.getControlNbr(),webTran.getLineNbr());
		System.out.println("in positive read test and read of controlNbr " + webTran.getControlNbr() + " returned controlNbr " + readit.getControlNbr());
		
		assertTrue(readit.getControlNbr()==webTran.getControlNbr());
		
	}

	@Test
	public void testPositiveUpdate() {
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
		
		double[] projCurve = new double[40];
		Arrays.fill(projCurve, 0D);
		webTran.setProjCurve(projCurve);

		boolean result = target.create(webTran);
		
		CustWebTran readit = target.read(webTran.getCustomerId(),webTran.getControlNbr(),webTran.getLineNbr());
		
		readit.setColorId("6385");
		boolean updateResult = target.update(readit);
		
		System.out.println("in positive update test and update controlNbr " + webTran.getControlNbr() + " returned controlNbr " + readit.getControlNbr());
		
		assertTrue(readit.getControlNbr()==webTran.getControlNbr() && updateResult);
		
	}

	@Test
	public void testPositiveListForCustomerId() {
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
		
		double[] projCurve = new double[40];
		Arrays.fill(projCurve, 0D);
		webTran.setProjCurve(projCurve);
		//webTran.setMeasCurve(projCurve);

		boolean result = target.create(webTran);
		
		List<CustWebTran> readList = target.listForCustomerId(webTran.getCustomerId());
		System.out.println("in positive List for Customer Id " +webTran.getCustomerId() + " and found " + readList.size() + " records.");
		
		assertTrue(readList.size()>0);
		
	}

	@Test
	public void testPositiveListForControlNbr() {
		CustWebTran webTran = new CustWebTran();
		
		webTran.setControlNbr(1235813);
		webTran.setCustomerId("shercolortest");
		webTran.setJobField01("Main Campus");
		webTran.setJobField02("L");
		webTran.setJobField03("2");
		webTran.setJobField04("2531");
		webTran.setJobField05("Wall");
		webTran.setColorComp("SHERWIN-WILLAIAMS");
		webTran.setColorId("6611");
		webTran.setColorName("JOVIAL");
		
		double[] projCurve = new double[40];
		Arrays.fill(projCurve, 0D);
		webTran.setProjCurve(projCurve);
		//webTran.setMeasCurve(projCurve);

		System.out.println("control nbr -> " + webTran.getControlNbr());
		boolean result = target.create(webTran);
		
		List<CustWebTran> readList = target.listForControlNbr(webTran.getControlNbr());
		System.out.println("in positive List for Control Nbr " + webTran.getControlNbr() + " and found " + readList.size() + " records.");
		
		assertTrue(readList.size()>0);
		
	}
}
