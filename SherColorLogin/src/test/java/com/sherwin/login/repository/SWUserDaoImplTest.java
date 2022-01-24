package com.sherwin.login.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.domain.SWUser;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class SWUserDaoImplTest {

	@Autowired
	SwUserRepository target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveReadPk() {
		String loginId ="shercolortest";


		SWUser result = target.findByLoginID(loginId);
		if (result!=null) {
			System.out.println("Read by loginId -> " + result.getLoginID() + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
		assertThat(result.getLoginID()).isEqualTo(loginId);
	}

	@Test
	public void testFailedReadPk() {
		String loginId ="thatinvaliduser";
		SWUser result = target.findByLoginID(loginId);
		if (result == null) {
			System.out.println("Read by loginId NOT FOUND - Test is SUCCESSFUL!");
		}
		assertNull(result);
	}

	@Test
	public void testPositiveGetLoginIdScatterCaseMatch() {
		String loginId ="SherColorTest";


		String result = target.getLoginID(loginId);
		if (result!=null) {
			System.out.println("GetloginId " + loginId + "-> " + result + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
		assertThat(result).isEqualToIgnoringCase(loginId);
	}

	@Test
	public void testFailedDirectMatch() {
		String loginId ="thatinvaliduser";
		String result = target.getLoginID(loginId);
		if (result == null) {
			System.out.println("GetloginId (no users) - " + loginId + " returned null - Test is SUCCESSFUL!");
		}
		assertNull(result);
	}

	@Test
	public void testFailedMultipleMatch() {
		String loginId ="multipleUsers";
		String result = target.getLoginID(loginId);
		if (result == null) {
			System.out.println("GetloginId (multiple users) - " + loginId + " returned null - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}

	@Test
	public void testPositiveDisable() {
		String loginId ="shercolortest";

		try {
			assertThat(target.disableActiveUser(loginId)).isEqualTo(1);
			assertThat(target.findByLoginID(loginId).getActiveUser()).isEqualTo("N");
			System.out.println("disableActiveUser returned true - Test is SUCCESSFUL!");
		} catch (Exception e) {
			System.out.println("Exception from positive disable test - " + e.getMessage() + " - Test FAILED!");
		}
	}

	@Test
	public void testNegativeDisable() {
		String loginId ="thatinvaliduser";

		try {
			assertThat(target.disableActiveUser(loginId)).isEqualTo(0);
			System.out.println("disableActiveUser returned false - Test is SUCCESSFUL!");
		} catch (Exception e) {
			System.out.println("Exception from negative disable test - " + e.getMessage() + " - Test FAILED!");
		}
	}
}
