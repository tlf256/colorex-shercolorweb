package com.sherwin.login.service;

import com.sherwin.login.annotation.SherColorLoginTransactionalTest;
import com.sherwin.login.domain.SWUserComments;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;



@RunWith(SpringJUnit4ClassRunner.class)
@SherColorLoginTransactionalTest
public class SWUserCommentServiceImplTest {

    //TODO need to add more tests!

	@Autowired
	SWUserCommentService target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveCreate() {
		SWUserComments thisComment = new SWUserComments();

		thisComment.setComments("test comment message");
		thisComment.setCreatedBy("system");
		thisComment.setLoginID("bxp874");
		thisComment.setCreatedDate(new Date());
		assertTrue(target.createOrUpdateEntry(thisComment));

	}
}
