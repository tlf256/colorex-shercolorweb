package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebDevices;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/shercolorcommon.xml" })
@Transactional
public class CustWebDevicesDaoTest {

	@Autowired
	private CustWebDevicesDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveCreate() {
		CustWebDevices webDev = new CustWebDevices();
		
		webDev.setCustomerId("shercolortest");
		webDev.setDeviceType("SPECTRO");
		webDev.setDeviceModel("L");
		webDev.setSerialNbr("2");

		
		boolean result = target.create(webDev);
		assertTrue(result);
		
	}
	
	@Test
	public void testPositiveDelete() {
		CustWebDevices webDev = new CustWebDevices();
		
		webDev.setCustomerId("CCF");
		webDev.setDeviceType("SPECTRO");
		webDev.setDeviceModel("Ci52+SWS");
		webDev.setSerialNbr("001065");

		
		boolean result = target.delete(webDev);
		assertTrue(result);
		
	}

	@Test
	public void testPositiveRead() {

		String customerId = "CCF";
		String modelType = "Ci52+SWS";
		String serialNbr = "001065";


		CustWebDevices result = target.read(customerId, modelType, serialNbr);

		assertNotNull(result);
	}

	@Test
	public void testNegativeRead() {
		String customerId = "JUNK";
		String modelType = "JUNKY";
		String serialNbr = "MOREJUNK";
		
		CustWebDevices result = target.read(customerId, modelType, serialNbr);

		assertNull(result);
	}
	
	@Test
	public void testPositiveReadAllByCustId() {
		String customerId = "CCF";

		List<CustWebDevices> result = target.listForCustomerId(customerId);

		System.out.println("CustWebDevices count for " + customerId + " is " + result.size());
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeReadAllByCustId() {
		String customerId = "JUNK";

		List<CustWebDevices> result = target.listForCustomerId(customerId);

		System.out.println("CustWebDevices count for " + customerId + " is " + result.size());
		assertNotNull(result);
	}

	@Test
	public void testPositiveReadSpectrosByCustId() {
		String customerId = "CCF";

		List<CustWebDevices> result = target.listSpectrosForCustomerId(customerId);

		System.out.println("CustWebDevices SPECTRO count for " + customerId + " is " + result.size());
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeReadSpectrosByCustId() {
		String customerId = "JUNK";

		List<CustWebDevices> result = target.listSpectrosForCustomerId(customerId);

		System.out.println("CustWebDevices SPECTRO count for " + customerId + " is " + result.size());
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveReadTintersByCustId() {
		String customerId = "CCF";

		List<CustWebDevices> result = target.listTintersForCustomerId(customerId);

		System.out.println("CustWebDevices TINTER count for " + customerId + " is " + result.size());
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeReadTintersByCustId() {
		String customerId = "JUNK";

		List<CustWebDevices> result = target.listTintersForCustomerId(customerId);

		System.out.println("CustWebDevices TINTER count for " + customerId + " is " + result.size());
		assertNotNull(result);
	}
}

