package com.sherwin.shercolor.util.domain;

import static org.junit.Assert.*;

import java.beans.IntrospectionException;

import org.apache.logging.log4j.Level;
import org.junit.Test;

import com.sherwin.shercolor.common.util.JavaBeanTester;


public class SwMessageTest {

	private SwMessage target;
	
	@Test
	public void testCreate() {
		target = new SwMessage();
		assertNotNull(target);
	}
	
	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(SwMessage.class, "severity");
		} catch (IntrospectionException e) {
			fail();
		}
	}
	
	@Test
	public void testSeverity() {
		
		try {
			target = new SwMessage();
			target.setSeverity(Level.WARN);
			if (target.getSeverity().compareTo(Level.WARN)!=0) {
				fail();
			}
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void testConstructor() {
		
		try {
			target = new SwMessage(Level.INFO,"CODE1", "THIS IS AN INFO MESSAGE FOR CODE1");
			
			if (target.getSeverity().compareTo(Level.INFO)!=0) {
				fail();
			}
			if (target.getCode().compareTo("CODE1")!=0) {
				fail();
			}
			if (target.getMessage().compareTo("THIS IS AN INFO MESSAGE FOR CODE1")!=0) {
				fail();
			}
			
		} catch (Exception e) {
			fail();
		}
		
	}

}
