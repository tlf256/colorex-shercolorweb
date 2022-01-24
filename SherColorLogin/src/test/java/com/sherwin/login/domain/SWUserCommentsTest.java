package com.sherwin.login.domain;

import com.sherwin.login.util.JavaBeanTester;
import org.junit.Test;

import java.beans.IntrospectionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
