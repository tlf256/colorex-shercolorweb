package com.sherwin.login.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;

import org.junit.Test;

import com.sherwin.login.util.JavaBeanTester;
import org.springframework.test.context.ActiveProfiles;

public class SWUserCommentsTest {

	private SWUserComments target;

	@Test
	public void testCreate() {
		target = new SWUserComments();
		assertNotNull(target);
	}

	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(SWUserComments.class);
		} catch (IntrospectionException e) {
			fail();
		}
	}

}
