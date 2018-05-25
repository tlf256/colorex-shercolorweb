package com.sherwin.shercolor.common.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
//import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.common.domain.CustWebJobFields;
import com.sherwin.shercolor.common.domain.CustWebParms;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
//@Rollback
public class CustomerServiceTest {
	@Autowired
	private CustomerService target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveGetDefaultCustomerTitle() {
		String searchCriteria ="DRM";
	
		String result = target.getDefaultCustomerTitle(searchCriteria);
		
		if(result!=""){
			System.out.println("Positive getDefaultCustomerTitle found - " + result);
		} else {
			System.out.println("Positive getDefaultCustomerTitle found blank - PROBLEM!!");
			result = null;
		}

		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetDefaultCustomerTitle() {
		String searchCriteria ="NONE";
	
		String result = target.getDefaultCustomerTitle(searchCriteria);
		
		if(result!=""){
			System.out.println("Negative getDefaultCustomerTitle found - " + result + " PROBLEM!!");
		} else {
			System.out.println("Negative getDefaultCustomerTitle found empty string, which is what we wanted");
			result = null;
		}

		assertNull(result);
	}
	
	@Test
	public void testPositiveGetDefaultCustWebParms() {
		String searchCriteria ="DRM";
	
		CustWebParms result = target.getDefaultCustWebParms(searchCriteria);
		
		if(result!=null){
			System.out.println("Positive getDefaultCustWebParms found - " + result.getSwuiTitle());
		}

		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetDefaultCustWebParms() {
		String searchCriteria ="NONE";
	
		CustWebParms result = target.getDefaultCustWebParms(searchCriteria);
		
		if(result!=null){
			System.out.println("Negative getDefaultCustWebParms found - " + result.getSwuiTitle());
		}

		assertNull(result);
	}
	
	@Test
	public void testPositiveGetAllCustWebParms() {
		String searchCriteria ="CCF";
	
		List<CustWebParms> result = target.getAllCustWebParms(searchCriteria);
		
		if(result.size()>0){
			System.out.println("Positive getAllCustWebParms found - " + result.size());
		} else {
			fail();
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetAllCustWebParms() {
		String searchCriteria ="NONE";
	
		List<CustWebParms> result = target.getAllCustWebParms(searchCriteria);
		
		if(result.size() > 0){
			fail();
		} else {
			System.out.println("Negative getAllCustWebParms found - " + result.size() + " results, which is what we wanted.");
		}

	}
	
	@Test
	public void testPositiveGetCustJobFields() {
		String searchCriteria ="CCF";
	
		List<CustWebJobFields> result = target.getCustJobFields(searchCriteria);
		
		if(result.size()>0){
			System.out.println("Positive getCustJobFields found - " + result.size());
		} else {
			fail();
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetCustJobFields() {
		String searchCriteria ="NONE";
	
		List<CustWebJobFields> result = target.getCustJobFields(searchCriteria);
		
		if(result.size() > 0){
			fail();
		} else {
			System.out.println("Negative getCustJobFields found - " + result.size() + " results, which is what we wanted.");
		}

	}
	
	@Test
	public void testPositiveGetCustDevices() {
		String searchCriteria ="CCF";
	
		List<CustWebDevices> result = target.getCustDevices(searchCriteria);
		
		if(result.size()>0){
			System.out.println("Positive getCustDevices found - " + result.size());
		} else {
			fail();
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetCustDevices() {
		String searchCriteria ="NONE";
	
		List<CustWebDevices> result = target.getCustDevices(searchCriteria);
		
		if(result.size() > 0){
			fail();
		} else {
			System.out.println("Negative getCustDevices found - " + result.size() + " results, which is what we wanted.");
		}

	}
	
	@Test
	public void testPositiveGetCustSpectros() {
		String searchCriteria ="CCF";
	
		List<CustWebDevices> result = target.getCustSpectros(searchCriteria);
		
		if(result.size()>0){
			System.out.println("Positive getCustSpectros found - " + result.size());
		} else {
			fail();
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetCustSpectros() {
		String searchCriteria ="NONE";
	
		List<CustWebDevices> result = target.getCustSpectros(searchCriteria);
		
		if(result.size() > 0){
			fail();
		} else {
			System.out.println("Negative getCustSpectros found - " + result.size() + " results, which is what we wanted.");
		}

	}
	
	@Test
	public void testPositiveGetCustTinters() {
		String searchCriteria ="CCF";
	
		List<CustWebDevices> result = target.getCustTinters(searchCriteria);
		
		if(result.size()>0){
			System.out.println("Positive getCustTinters found - " + result.size());
		} else {
			fail();
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetCustTinters() {
		String searchCriteria ="NONE";
	
		List<CustWebDevices> result = target.getCustTinters(searchCriteria);
		
		if(result.size() > 0){
			fail();
		} else {
			System.out.println("Negative getCustTinters found - " + result.size() + " results, which is what we wanted.");
		}

	}
	
	@Test
	public void testPositiveGetLoginTransformCustomerId() {
		String searchCriteria ="shercolorpen";
	
		String result = target.getLoginTransformCustomerId(searchCriteria);
		
		if(result!=""){
			System.out.println("POSITIVE getLoginTransformCustomerId found - " + result);
		} else {
			System.out.println("POSITIVE getLoginTransformCustomerId found - " + result + " PROBLEM!");
			result = null;
		}

		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetLoginTransformCustomerId() {
		String searchCriteria ="NONE";
	
		String result = target.getLoginTransformCustomerId(searchCriteria);
		
		if(result!=""){
			System.out.println("NEGATIVE getLoginTransformCustomerId found - " + result + " PROBLEM!");
		} else {
			System.out.println("NEGATIVE getLoginTransformCustomerId found empty string, which is what we want");
			result = null;
		}

		assertNull(result);
	}
	
	
}
