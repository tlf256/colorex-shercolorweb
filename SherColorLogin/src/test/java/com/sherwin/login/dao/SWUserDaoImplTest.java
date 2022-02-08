package com.sherwin.login.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.domain.SWUser;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorlogin.xml"})
@Transactional
public class SWUserDaoImplTest {

	@Autowired
	SWUserDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadEmail() {
		String loginId ="a@b.com";
		
		SWUser result = target.readByEmail(loginId);
		if (result!=null) {
			System.out.println("Read by email -> " + result.getLoginID() + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeReadEmail() {
		String loginId ="negativeemailtest@sherwin.com";
		
		SWUser result = target.readByEmail(loginId);
		if (result!=null) {
			System.out.println("Read by email -> " + result.getLoginID() + " Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	
	@Test
	public void testPositiveReadPk() {
		String loginId ="shercolortest";
		
		
		SWUser result = target.read(loginId);
		if (result!=null) {
			System.out.println("Read by loginId -> " + result.getLoginID() + " " + result.geteMail() + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String loginId ="thatinvaliduser";
		SWUser result = target.read(loginId);
		if (result == null) {
			System.out.println("Read by loginId NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	
	@Test
	public void testPositiveGetLoginIdDirectMatch() {
		String loginId ="shercolortest";
		
		
		String result = target.getLoginId(loginId);
		if (result!=null) {
			System.out.println("GetloginId " + loginId + "-> " + result + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
	
	@Test
	public void testPositiveGetLoginIdScatterCaseMatch() {
		String loginId ="SherColorTest";
		
		
		String result = target.getLoginId(loginId);
		if (result!=null) {
			System.out.println("GetloginId " + loginId + "-> " + result + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedDirectMatch() {
		String loginId ="thatinvaliduser";
		String result = target.getLoginId(loginId);
		if (result == null) {
			System.out.println("GetloginId (no users) - " + loginId + " returned null - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	
	@Test
	public void testFailedMultipleMatch() {
		String loginId ="multipleUsers";
		String result = target.getLoginId(loginId);
		if (result == null) {
			System.out.println("GetloginId (multiple users) - " + loginId + " returned null - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	
	@Test
	public void testPositiveDisable() {
		String loginId ="shercolortest";
		
		try {
			assertTrue(target.disableActiveUser(loginId));
			System.out.println("disableActiveUser returned true - Test is SUCCESSFUL!");
		} catch (Exception e) {
			System.out.println("Exception from positive disable test - " + e.getMessage() + " - Test FAILED!");
		}
	}

	@Test
	public void testNegativeDisable() {
		String loginId ="thatinvaliduser";
		
		try {
			assertFalse(target.disableActiveUser(loginId));
			System.out.println("disableActiveUser returned false - Test is SUCCESSFUL!");
		} catch (Exception e) {
			System.out.println("Exception from negative disable test - " + e.getMessage() + " - Test FAILED!");
		}
	}

}
