package com.sherwin.login.service;

import com.sherwin.login.annotation.SherColorLoginTransactionalTest;
import com.sherwin.login.domain.SWUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SherColorLoginTransactionalTest
public class SWUserServiceImplTest {

	@Autowired
	SWUserService target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}


	@Test
	public void testPositiveReadPk() {
		String loginId ="shercolortest";


		SWUser result = target.readSWUser(loginId);
		if (result!=null) {
			System.out.println("1. Read by loginId -> " + result.getLoginID() + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String loginId ="thatinvaliduser";
		SWUser result = target.readSWUser(loginId);
		if (result == null) {
			System.out.println("2. Read by loginId NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}

	@Test
	public void testPositiveGetLoginIdDirectMatch() {
		String loginId ="shercolortest";


		String result = target.getLoginId(loginId);
		if (result!=null) {
			System.out.println("5. GetloginId " + loginId + "-> " + result + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testPositiveGetLoginIdScatterCaseMatch() {
		String loginId ="SherColorTest";


		String result = target.getLoginId(loginId);
		if (result!=null) {
			System.out.println("6. GetloginId " + loginId + "-> " + result + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedDirectMatch() {
		String loginId ="thatinvaliduser";
		String result = target.getLoginId(loginId);
		if (result == null) {
			System.out.println("7. GetloginId (no users) - " + loginId + " returned null - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}

	@Test
	public void testFailedMultipleMatch() {
		String loginId ="multipleUsers";
		String result = target.getLoginId(loginId);
		if (result == null) {
			System.out.println("8. GetloginId (multiple users) - " + loginId + " returned null - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}

	@Test
	public void testPositiveDisable() {
		String loginId ="shercolortest";

		try {
			assertThat(target.disableActiveUser(loginId)).isEqualTo(1);
			System.out.println("3. disableActiveUser returned true - Test is SUCCESSFUL!");
		} catch (Exception e) {
			System.out.println("Exception from positive disable test - " + e.getMessage() + " - Test FAILED!");
		}
	}

	@Test
	public void testNegativeDisable() {
		String loginId ="thatinvaliduser";

		try {
			assertThat(target.disableActiveUser(loginId)).isEqualTo(0);
			System.out.println("4. disableActiveUser returned false - Test is SUCCESSFUL!");
		} catch (Exception e) {
			System.out.println("Exception from negative disable test - " + e.getMessage() + " - Test FAILED!");
		}
	}

}

