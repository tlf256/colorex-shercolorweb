package com.sherwin.login.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.domain.SWUserComments;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class SWLoginValidatorTest {

	@Autowired
	SWLoginValidator target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveValidate() {
		String validPassword = "SherColor1&";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testNegativeValidateMinLength() {
		String validPassword = "Qa2!";
		assertFalse(target.validatePassword(validPassword));
	}
	
	@Test
	public void testNegativeValidateMaxLength() {
		String validPassword = "QQQQQQQQQQQQQQQaaaaaaaaaaaaaaa222222222222222!!!!!!!!!!!!!!!!!!!";
		assertFalse(target.validatePassword(validPassword));
	}
	
	@Test
	public void testNegativeValidateNoUpCase() {
		String validPassword = "qqqaaa222!!!";
		assertFalse(target.validatePassword(validPassword));
	}
	
	@Test
	public void testNegativeValidateNoLowCase() {
		String validPassword = "QQQAAA222!!!";
		assertFalse(target.validatePassword(validPassword));
	}
	
	@Test
	public void testNegativeValidateNoNumbers() {
		String validPassword = "QQQaaaaaa!!!";
		assertFalse(target.validatePassword(validPassword));
	}
	
	@Test
	public void testNegativeValidateNoSpecialChars() {
		String validPassword = "QQQAAA22212434";
		assertFalse(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateExclam() {
		String validPassword = "SherColor1!";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateAtSign() {
		String validPassword = "SherColor1@";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidatePoundSign() {
		String validPassword = "SherColor1#";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateDollaSign() {
		String validPassword = "SherColor1$";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidatePctSign() {
		String validPassword = "SherColor1%";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateCarat() {
		String validPassword = "SherColor1^";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateAmpersand() {
		String validPassword = "SherColor1&";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateStar() {
		String validPassword = "SherColor1*";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateMultipleGoodSpecChars() {
		String validPassword = "Sher%Color1*";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateMultipleGoodSpecChars2() {
		String validPassword = "SherColor1*#";
		assertTrue(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateMultipleBadSpecChars() {
		String validPassword = "Sher(olor1+";
		assertFalse(target.validatePassword(validPassword));
	}
	
	@Test
	public void testPositiveValidateMultipleBadSpecChars2() {
		String validPassword = "SherColor1=]";
		assertFalse(target.validatePassword(validPassword));
	}
	
//	@Test
//	public void testPositiveValidateMultipleMixedSpecChars() {
//		String validPassword = "Sher(olor1*";
//		assertFalse(target.validatePassword(validPassword));
//	}
	
//	@Test
//	public void testPositiveValidateMultipleMixedSpecChars2() {
//		String validPassword = "SherColor1#]";
//		assertFalse(target.validatePassword(validPassword));
//	}

}
