package com.sherwin.login.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;

import org.junit.Test;

import com.sherwin.login.util.JavaBeanTester;

public class SWUserTest {

	private SWUser target;

	@Test
	public void testCreate() {
		target = new SWUser();
		assertNotNull(target);
	}

	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(SWUser.class);
		} catch (IntrospectionException e) {
			fail();
		}
	}

}
