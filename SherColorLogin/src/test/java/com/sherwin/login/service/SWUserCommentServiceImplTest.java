package com.sherwin.login.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.domain.SWUserComments;



//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:config/spring/shercolorlogin.xml"})
//@Transactional
//public class SWUserCommentServiceImplTest {
//
//	@Autowired
//	SWUserCommentService target;
//	
//	@Test
//	public void testCreate() {
//		assertNotNull(target);
//	}
//	
//	@Test
//	public void testPositiveCreate() {
//		SWUserComments thisComment = new SWUserComments();
//		
//		thisComment.setComments("test comment message");
//		thisComment.setCreatedBy("system");
//		thisComment.setLoginID("bxp874");
//		thisComment.setCreatedDate(new Date());
//		assertTrue(target.createOrUpdateEntry(thisComment));
//	
//	}
//
//	@Test
//	public void testFailedUpdate() {
////		String loginId ="thatinvaliduser";
////		SWUser result = target.read(loginId);
////		if (result == null) {
////			System.out.println("Read by loginId NOT FOUND - Test is SUCCESSFUL!");
////		}
////		assertTrue(result == null);
//	}
//	
//
//
//}
